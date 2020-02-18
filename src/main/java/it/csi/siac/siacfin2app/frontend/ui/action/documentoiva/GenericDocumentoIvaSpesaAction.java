/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.GenericDocumentoIvaSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe generica di action per il Documento Iva Spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/06/2014
 * @param <M> la tipizzazione del model
 *
 */
public class GenericDocumentoIvaSpesaAction<M extends GenericDocumentoIvaSpesaModel> extends GenericDocumentoIvaAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1176078305388440013L;
	
	/** Serviz&icirc; del documento iva di spesa */
	@Autowired protected transient DocumentoIvaSpesaService documentoIvaSpesaService;
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/**
	 * Controlla gli importi e fornisce eventuali messaggi di errore.
	 * 
	 * @param totaleParziale il totale parziale
	 * @param importo        l'importo del documento di riferimento
	 */
	protected void controlloImporti(BigDecimal totaleParziale, BigDecimal importo) {
		final String methodName = "controlloImporti";
		// Se il documento è INTRASTAT, effettuo il controllo più blando
		if(Boolean.TRUE.equals(model.getSubdocumentoIva().getFlagIntracomunitario())) {
			log.debug(methodName, "sono intrastat--quindi dentro l'if");
			warnCondition(importo.subtract(totaleParziale).signum() != 0, ErroreFin.DOCUMENTO_IVA_SPESA_INTRASTAT.getErrore());
		} else {
			log.debug(methodName, "NON sono intrastat--quindi dentro l'else");
			ErroreFin erroreImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota()) ?
				ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_IMPORTO_QUOTA :
				ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_TOTALE_IMPORTO_RILEVANTE_IVA;
			ErroreFin warningImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota()) ?
				ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_DELLA_QUOTA :
				ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_RILEVANTE_IVA;
			
			log.debug(methodName, "totale parziale: " + totaleParziale);
			log.debug(methodName, "importo: " + importo);
			log.debug(methodName, "importo e totale parziale sono uguali?  " + (importo.subtract(totaleParziale).signum() == 0));
			
			String totaleParzialeFormatted = FormatUtils.formatCurrency(totaleParziale);
			String importoFormatted = FormatUtils.formatCurrency(importo);
			boolean isNotaCredito = model.getDocumento().getTipoDocumento().isNotaCredito();
			
			checkCondition((isNotaCredito && importo.add(totaleParziale).signum() >= 0) || (!isNotaCredito && importo.subtract(totaleParziale).signum() >= 0), erroreImporti.getErrore(totaleParzialeFormatted, importoFormatted));
			warnCondition((isNotaCredito && importo.add(totaleParziale).signum() == 0) || (!isNotaCredito && importo.subtract(totaleParziale).signum() == 0),
					warningImporti.getErrore(totaleParzialeFormatted, importoFormatted));
		}
	}
	
	/**
	 * Effettua una ricerca di dettaglio del subdocumento iva.
	 * 
	 * @param uid l'uid del subdocumento iva da cercare
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void ricercaDettaglioSubdocumentoIva(Integer uid) throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioSubdocumentoIva";
		RicercaDettaglioSubdocumentoIvaSpesa request = model.creaRequestRicercaDettaglioSubdocumentoIvaSpesa(uid);
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.ricercaDettaglioSubdocumentoIvaSpesa(request);
		logServiceResponse(response);
		
		// Se ho errori, lancio l'eccezione sì da uscire immediatamente
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaSpesa per subdocumento iva con uid: " +
					model.getUidSubdocumentoIva());
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaSpesa");
		}
		SubdocumentoIvaSpesa sis = response.getSubdocumentoIvaSpesa();
		// Imposto i dati del subdocumento iva nel model
		
		TipoRelazione tr = sis.getTipoRelazione();
		// Controllo se sia necessario ricercare il documento iva padre
		if(TipoRelazione.NOTA_CREDITO_IVA == tr && sis.getSubdocumentoIvaPadre() != null) {
			ricercaDettaglioSubdocumentoIva(sis.getSubdocumentoIvaPadre().getUid());
			impostaDatiNelModelNota(sis);
			impostaFlagNota();
		} else {
			impostaDatiNelModel(sis);
			for(SubdocumentoIvaSpesa nota : sis.getListaNoteDiCredito()) {
				impostaDatiNelModelNota(nota);
			}
		}
	}
	
	/**
	 * Imposta il flag della nota.
	 */
	protected void impostaFlagNota() {
		// TODO: implementare nella classe concreta
	}
	
	/**
	 * Imposta i dati del subdocumento iva nel model.
	 * 
	 * @param sis il subdocumento da impostare nel model
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui la ricerca dettaglio non abbia fornito i dati corretti
	 */
	protected void impostaDatiNelModel(SubdocumentoIvaSpesa sis) throws WebServiceInvocationFailureException {
		model.setSubdocumentoIva(sis);
		// Controllo se sono collegato al documento
		boolean collegatoDocumento = sis.getDocumento() != null && sis.getDocumento().getUid() != 0;
		if(collegatoDocumento) {
			// Imposto l'uid del documento
			model.setUidDocumentoCollegato(sis.getDocumento().getUid());
		} else {
			SubdocumentoSpesa ss = sis.getSubdocumento();
			if(ss == null) {
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaSpesa: subdocumento collegato non presente");
			} else if (ss.getDocumento() == null) {
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaSpesa: documento collegato al subdocumento non presente");
			}
			// Imposto l'uid del subdocumento
			model.setUidQuotaDocumentoCollegato(ss.getUid());
			model.setUidDocumentoCollegato(ss.getDocumento().getUid());
			model.setTipoSubdocumentoIvaQuota(Boolean.TRUE);
		}
			// Imposto le aliquote
			model.setListaAliquotaSubdocumentoIva(sis.getListaAliquotaSubdocumentoIva());
							
			// Imposto i dati della valuta
			model.setValuta(sis.getValuta());
			model.setListaValuta(Arrays.asList(sis.getValuta()));
			model.setImportoInValuta(sis.getImportoInValuta());
				
			if(sis.getSubdocumentoIvaEntrata() != null && sis.getSubdocumentoIvaEntrata().getUid() != 0) {
					impostaDatiIntracomunitari(sis.getSubdocumentoIvaEntrata());
				}
	}
	
	/**
	 * Imposta i dati intracomunitar&icirc; relativi alla controregistrazione.
	 * 
	 * @param subdocumentoIvaEntrata la controregistrazione
	 */
	private void impostaDatiIntracomunitari(SubdocumentoIvaEntrata subdocumentoIvaEntrata) {
		model.setUidIntracomunitarioDocumento(subdocumentoIvaEntrata.getUid());
		
		RegistroIva reg = subdocumentoIvaEntrata.getRegistroIva();
		model.setTipoRegistroIvaIntracomunitarioDocumento(reg.getTipoRegistroIva());
		model.setRegistroIvaIntracomunitarioDocumento(reg);
		
		AttivitaIva ai = subdocumentoIvaEntrata.getAttivitaIva();
		model.setAttivitaIvaIntracomunitarioDocumento(ai);
		
		// Imposto a TRUE la preesistenza del flag
		model.setFlagIntracomunitarioPreesistente(Boolean.TRUE);
	}

	/**
	 * Imposta i dati del subdocumento iva nel model.
	 * 
	 * @param nota la nota di credito iva da impostare nel model
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui la ricerca dettaglio non abbia fornito i dati corretti
	 */
	protected void impostaDatiNelModelNota(SubdocumentoIvaSpesa nota) throws WebServiceInvocationFailureException {
		// Implementazione vuota di default. Da override-are nel caso si voglia implementare una logica
	}
	
	/**
	 * Effettua una ricerca di dettaglio del documento (non iva) cui associare i dati.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumento";
		RicercaDettaglioDocumentoSpesa request = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumentoCollegato());
		logServiceRequest(request);
		RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Se ho errori, lancio l'eccezione sì da uscire immediatamente
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio RicercaDettaglioDocumentoSpesa per documento con uid: " + model.getUidDocumentoCollegato());
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioDocumentoSpesa");
		}
		// Imposto i dati del documento nel model
		model.setDocumento(response.getDocumento());
		model.setSoggetto(response.getDocumento().getSoggetto());
	}
	
	/**
	 * Ottiene il dettaglio della quota a partire dal subdocumento.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void ricercaDettaglioQuota() throws WebServiceInvocationFailureException {
		List<SubdocumentoSpesa> listaSubdocumentoSpesa = model.getDocumento().getListaSubdocumenti();
		Integer uidSubdocumento = model.getUidQuotaDocumentoCollegato();
		
		if(uidSubdocumento == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("uid subdocumento"));
			throw new WebServiceInvocationFailureException("Errore nel caricamento del subdocumento. Uid mancante");
		}
		
		// Cerco il subdocumento per uid
		for(SubdocumentoSpesa ss : listaSubdocumentoSpesa) {
			if(ss.getUid() == uidSubdocumento.intValue()) {
				// Imposto il subdocumento
				model.setSubdocumento(ss);
				// Esco, evitando di ciclare inutilmente
				break;
			}
		}
	}
	
	@Override
	protected void caricaListaAttivitaIvaPerQuote() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaAttivitaIvaPerQuote";
		
		Impegno impegno = model.getSubdocumento().getImpegno();
		CapitoloUscitaGestione cug = null;
		if(impegno != null) {
			try {
				impegno = ricercaImpegno(impegno);
				cug = impegno.getCapitoloUscitaGestione();
			} catch(WebServiceInvocationFailureException e) {
				log.info(methodName, e.getMessage());
			}
		}
		
		if(cug == null || cug.getUid() == 0) {
			// Fallback al caricamento di ogni attivita
			caricaListaAttivitaIvaTotale();
			return;
		}
		
		// Ho un capitolo: cerco tutte le attivita relative
		log.debug(methodName, "Capitolo trovato con uid: " + cug.getUid());
		// Ricerca delle attivita collegate
		RicercaRelazioneAttivitaIvaCapitolo request = model.creaRequestRicercaRelazioneAttivitaIvaCapitolo(cug);
		logServiceRequest(request);
		RicercaRelazioneAttivitaIvaCapitoloResponse response = attivitaIvaCapitoloService.ricercaRelazioneAttivitaIvaCapitolo(request);
		logServiceResponse(response);
		
		if(response.hasErrori() || response.getListaAttivitaIva().isEmpty()) {
			log.info(methodName, "Errore nell'invocazione del servizio o lista vuota: redirigo al caricamento di ogni attivita");
			caricaListaAttivitaIvaTotale();
			return;
		}
		
		model.setListaAttivitaIva(response.getListaAttivitaIva());
	}
	
	@Override
	protected void caricaListaTipoRegistroIva() {
		List<TipoRegistroIva> listaTipoRegistroIva = Arrays.asList(TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA, TipoRegistroIva.ACQUISTI_IVA_DIFFERITA);
		model.setListaTipoRegistroIva(listaTipoRegistroIva);
	}
	
	@Override
	protected void caricaListaTipoRegistrazioneIva() throws WebServiceInvocationFailureException {
		super.caricaListaTipoRegistrazioneIva(Boolean.FALSE, Boolean.TRUE);
	}

	/**
	 * Ricerca l'impegno data la chiave logica e restituisce il valore dell'impegno trovato, se presente.
	 * 
	 * @param impegno l'impegno da ricercare
	 * 
	 * @return l'impegno
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	private Impegno ricercaImpegno(Impegno impegno) throws WebServiceInvocationFailureException {
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(impegno);
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		if(response.isFallimento()) {
			throw new WebServiceInvocationFailureException("Fallimento dell'invocazione del servizio RicercaImpegnoPerChiaveOttimizzato per impegno "
				+ impegno.getAnnoMovimento() + "/" + impegno.getNumero().toPlainString());
		}
		if(response.getImpegno() == null) {
			throw new WebServiceInvocationFailureException("Nessun impegno trovato dal servizio RicercaImpegnoPerChiaveOttimizzato per impegno "
				+ impegno.getAnnoMovimento() + "/" + impegno.getNumero().toPlainString());
		}
		return response.getImpegno();
	}
	
}
