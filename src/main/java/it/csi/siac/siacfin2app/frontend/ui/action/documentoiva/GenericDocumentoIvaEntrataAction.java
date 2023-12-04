/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.GenericDocumentoIvaEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Classe generica di action per il Documento Iva Entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 23/06/2014
 * @param <M> la tipizzazione del model
 *
 */
public class GenericDocumentoIvaEntrataAction<M extends GenericDocumentoIvaEntrataModel> extends GenericDocumentoIvaAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6749256249452846090L;
	
	/** Serviz&icirc; del documento iva di entrata */
	@Autowired protected transient DocumentoIvaEntrataService documentoIvaEntrataService;
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	/**
	 * Effettua una ricerca di dettaglio del subdocumento iva.
	 * 
	 * @param uid l'uid del subdocumento iva da cercare
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void ricercaDettaglioSubdocumentoIva(Integer uid) throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioSubdocumentoIva";
		RicercaDettaglioSubdocumentoIvaEntrata request = model.creaRequestRicercaDettaglioSubdocumentoIvaEntrata(uid);
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaEntrataResponse response = documentoIvaEntrataService.ricercaDettaglioSubdocumentoIvaEntrata(request);
		logServiceResponse(response);
		
		// Se ho errori, lancio l'eccezione sì da uscire immediatamente
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaEntrata per subdocumento iva con uid: " +
					model.getUidSubdocumentoIva());
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaEntrata");
		}
		SubdocumentoIvaEntrata sie = response.getSubdocumentoIvaEntrata();
		// Imposto i dati del subdocumento iva nel model
		
		// Controllo se sia necessario ricercare il documento iva padre
		if(TipoRelazione.NOTA_CREDITO_IVA == sie.getTipoRelazione() && sie.getSubdocumentoIvaPadre() != null) {
			ricercaDettaglioSubdocumentoIva(sie.getSubdocumentoIvaPadre().getUid());
			impostaDatiNelModelNota(sie);
			impostaFlagNota();
		} else {
			impostaDatiNelModel(sie);
			for(SubdocumentoIvaEntrata nota : sie.getListaNoteDiCredito()) {
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
	 * @param sie il subdocumento da impostare nel model
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui la ricerca dettaglio non abbia fornito i dati corretti
	 */
	protected void impostaDatiNelModel(SubdocumentoIvaEntrata sie) throws WebServiceInvocationFailureException {
		model.setSubdocumentoIva(sie);
		// Controllo se sono collegato al documento
		boolean collegatoDocumento = sie.getDocumento() != null && sie.getDocumento().getUid() != 0;
		if(collegatoDocumento) {
			// Imposto l'uid del documento
			model.setUidDocumentoCollegato(sie.getDocumento().getUid());
		} else {
			SubdocumentoEntrata se = sie.getSubdocumento();
			if(se == null) {
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaEntrata: subdocumento collegato non presente");
			} else if (se.getDocumento() == null) {
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioSubdocumentoIvaEntrata: documento collegato al subdocumento non presente");
			}
			// Imposto l'uid del subdocumento
			model.setUidQuotaDocumentoCollegato(se.getUid());
			model.setUidDocumentoCollegato(se.getDocumento().getUid());
			model.setTipoSubdocumentoIvaQuota(Boolean.TRUE);
		}
		// Imposto le aliquote
		model.setListaAliquotaSubdocumentoIva(sie.getListaAliquotaSubdocumentoIva());
		// Imposto l'attivitaIva
		model.setAttivitaIva(sie.getAttivitaIva());
		// Imposto il tipoRegistroIva
		model.setTipoRegistroIva(sie.getRegistroIva().getTipoRegistroIva());
		
		
		// Imposto i dati della valuta
		if(Boolean.TRUE.equals(model.getSubdocumentoIva().getFlagIntracomunitario())){
			model.setValuta(sie.getValuta());
			model.setListaValuta(Arrays.asList(sie.getValuta()));
			model.setImportoInValuta(sie.getImportoInValuta());
		}
		
	}
	
	/**
	 * Imposta i dati del subdocumento iva nel model.
	 * 
	 * @param nota la nota di credito iva da impostare nel model
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui la ricerca dettaglio non abbia fornito i dati corretti
	 */
	protected void impostaDatiNelModelNota(SubdocumentoIvaEntrata nota) throws WebServiceInvocationFailureException {
		// Implementazione vuota di default. Da override-are nel caso si voglia implementare una logica
	}
	
	/**
	 * Effettua una ricerca di dettaglio del documento (non iva) cui associare i dati.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumento";
		RicercaDettaglioDocumentoEntrata request = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumentoCollegato());
		logServiceRequest(request);
		RicercaDettaglioDocumentoEntrataResponse response = documentoEntrataService.ricercaDettaglioDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Se ho errori, lancio l'eccezione sì da uscire immediatamente
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio RicercaDettaglioDocumentoEntrata per documento con uid: " + model.getUidDocumentoCollegato());
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioDocumentoEntrata");
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
		List<SubdocumentoEntrata> listaSubdocumentoEntrata = model.getDocumento().getListaSubdocumenti();
		Integer uidSubdocumento = model.getUidQuotaDocumentoCollegato();
		
		if(uidSubdocumento == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("uid subdocumento"));
			throw new WebServiceInvocationFailureException("Errore nel caricamento del subdocumento. Uid mancante");
		}
		
		// Cerco il subdocumento per uid
		for(SubdocumentoEntrata se : listaSubdocumentoEntrata) {
			if(se.getUid() == uidSubdocumento.intValue()) {
				// Imposto il subdocumento
				model.setSubdocumento(se);
				// Esco, evitando di ciclare inutilmente
				break;
			}
		}
	}
	
	@Override
	protected void caricaListaAttivitaIvaPerQuote() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaAttivitaIvaPerQuote";
		
		Accertamento a = model.getSubdocumento().getAccertamento();
		CapitoloEntrataGestione ceg = null;
		if(a != null) {
			try {
				a = ricercaAccertamento(a);
				ceg = a.getCapitoloEntrataGestione();
			} catch(WebServiceInvocationFailureException e) {
				log.info(methodName, e.getMessage());
			}
		}
		
		if(ceg == null || ceg.getUid() == 0) {
			// Fallback al caricamento di ogni attivita
			caricaListaAttivitaIvaTotale();
			return;
		}
		
		// Ho un capitolo: cerco tutte le attivita relative
		log.debug(methodName, "Capitolo trovato con uid: " + ceg.getUid());
		// Ricerca delle attivita collegate
		RicercaRelazioneAttivitaIvaCapitolo request = model.creaRequestRicercaRelazioneAttivitaIvaCapitolo(ceg);
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
		List<TipoRegistroIva> listaTipoRegistroIva = Arrays.asList(TipoRegistroIva.VENDITE_IVA_IMMEDIATA,
			TipoRegistroIva.VENDITE_IVA_DIFFERITA, TipoRegistroIva.CORRISPETTIVI);
		model.setListaTipoRegistroIva(listaTipoRegistroIva);
	}
	
	@Override
	protected void caricaListaTipoRegistrazioneIva() throws WebServiceInvocationFailureException {
		super.caricaListaTipoRegistrazioneIva(Boolean.TRUE, Boolean.FALSE);
		
		// Escludo il TRI '02 - INTRASTAT'
		for(Iterator<TipoRegistrazioneIva> it = model.getListaTipoRegistrazioneIva().iterator(); it.hasNext();) {
			TipoRegistrazioneIva tri = it.next();
			// Se il codice e' '02' o la descrizione 'INTRASTAT', escludo il TRI
			if(BilConstants.TIPO_REGISTRAZIONE_IVA_INTRASTAT_CODICE.getConstant().equalsIgnoreCase(tri.getCodice())
					|| BilConstants.TIPO_REGISTRAZIONE_IVA_INTRASTAT_DESCRIZIONE.getConstant().equalsIgnoreCase(tri.getDescrizione())) {
				it.remove();
			}
		}
	}

	/**
	 * Ricerca l'accertamento data la chiave logica e restituisce il valore dell'accertamento trovato, se presente.
	 * 
	 * @param accertamento l'accertamento da ricercare
	 * 
	 * @return l'accertamento
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	private Accertamento ricercaAccertamento(Accertamento accertamento) throws WebServiceInvocationFailureException {
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento);
		logServiceRequest(request);
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		if(response.isFallimento()) {
			throw new WebServiceInvocationFailureException("Fallimento dell'invocazione del servizio RicercaAccertamentoPerChiaveOttimizzato per impegno "
				+ accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal().toPlainString());
		}
		if(response.getAccertamento() == null) {
			throw new WebServiceInvocationFailureException("Nessun impegno trovato dal servizio RicercaAccertamentoPerChiaveOttimizzato per impegno "
				+ accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal().toPlainString());
		}
		return response.getAccertamento();
	}
	
}
