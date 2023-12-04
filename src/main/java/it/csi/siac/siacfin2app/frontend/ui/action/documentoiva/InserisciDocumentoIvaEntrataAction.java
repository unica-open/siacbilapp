/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.InserisciDocumentoIvaEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per l'inserimento del Documento Iva Entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 23/06/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciDocumentoIvaEntrataAction extends GenericDocumentoIvaEntrataAction<InserisciDocumentoIvaEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1163571826338675652L;
	
	@Override
	public void prepare() throws Exception {
		// Pulisco le liste
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Check caso d'uso applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		
		try {
			// Ricerca dettaglio documento spesa
			ricercaDettaglioDocumento();
			
			// Ricerca di dettaglio del soggetto
			caricaSoggetto();
			
			// Ricerca della quota se associata
			if(Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())) {
				ricercaDettaglioQuota();
			}
			
			// Caricamento valori default
			caricamentoValoriDefault();
			impostaFlagMovimentoResiduo();
			
			// Caricamento classificatori
			caricamentoListe(TipoFamigliaDocumento.ENTRATA);
		} catch (WebServiceInvocationFailureException e) {
			log.error("execute", "Errore nell'invocazione dei servizi per la preparazione dell'inserimento del DocumentoIvaEntrata: " + e.getMessage(), e);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		// Ho caricato tutto: sono pronto a mostrare la pagina
		return SUCCESS;
	}
	
	private void impostaFlagMovimentoResiduo() {
		if(Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())){
			controllaMovimentoResiduo(model.getSubdocumento());
		}else{
			for(SubdocumentoEntrata subdocumentoEntrata : model.getDocumento().getListaSubdocumenti()){
				controllaMovimentoResiduo(subdocumentoEntrata);
			}
		}
		
	}

	private void controllaMovimentoResiduo(SubdocumentoEntrata subdocumento) {
		if(subdocumento.getAccertamento() != null && subdocumento.getAccertamento().getAnnoMovimento() < model.getBilancio().getAnno()){
			model.setMovimentoResiduo(true);
		}
	}
	
	/**
	 * Carica i valori di default per la maschera.
	 */
	private void caricamentoValoriDefault() {
		// Subdocumento
		SubdocumentoIvaEntrata subdocumentoIva = model.getSubdocumentoIva();
		if(subdocumentoIva == null) {
			subdocumentoIva = new SubdocumentoIvaEntrata();
			model.setSubdocumentoIva(subdocumentoIva);
		}
		Date now = new Date();
		subdocumentoIva.setDataRegistrazione(now);
		subdocumentoIva.setDataProtocolloDefinitivo(now);
		subdocumentoIva.setDataProtocolloProvvisorio(now);
		
		// TipoRegistrazioneIva
		TipoRegistrazioneIva tri = ComparatorUtils.findByCodice(model.getListaTipoRegistrazioneIva(), "01");
		subdocumentoIva.setTipoRegistrazioneIva(tri);
		
		// Aliquota
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = model.getAliquotaSubdocumentoIva();
		if(aliquotaSubdocumentoIva == null) {
			aliquotaSubdocumentoIva = new AliquotaSubdocumentoIva();
			model.setAliquotaSubdocumentoIva(aliquotaSubdocumentoIva);
		}
	}
	
	/**
	 * Inserisce il documento Iva di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciDocumentoIva() {
		final String methodName = "inserisciDocumentoIva";
		
		InserisceSubdocumentoIvaEntrata request = model.creaRequestInserisceSubdocumentoIvaEntrata();
		logServiceRequest(request);
		InserisceSubdocumentoIvaEntrataResponse response = documentoIvaEntrataService.inserisceSubdocumentoIvaEntrata(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Ho degli errori
			log.info(methodName, "Errore nell'invocazione del servizio InserisceSubdocumentoIvaEntrata");
			addErrori(response);
//			//reimposto i valori che potrebbero essere stati cambiati dalla creazione della request
//			model.getSubdocumentoIva().setDataProtocolloDefinitivo(model.getSubdocumentoIva().getDataProtocolloDefinitivo() != null ? model.getSubdocumentoIva().getDataProtocolloDefinitivo() : new Date());
//			model.getSubdocumentoIva().setDataProtocolloProvvisorio(model.getSubdocumentoIva().getDataProtocolloProvvisorio() != null ? model.getSubdocumentoIva().getDataProtocolloProvvisorio() : new Date());
//			model.setRegistrazioneSenzaProtocollo(false);
			return INPUT;
		}
		// Documento IVA inserito
		SubdocumentoIvaEntrata sie = response.getSubdocumentoIvaEntrata();
		log.debug(methodName, "Inserimento del documentoIvaEntrata con uid " + sie.getUid() + " andata a buon fine");
		model.setSubdocumentoIva(sie);
		// Imposto l'uid nel model
		model.setUidSubdocumentoIva(sie.getUid());
		impostaInformazioneSuccesso();
		setMessaggiInSessionePerActionSuccessiva();
		setInformazioniInSessionePerActionSuccessiva();
		
		// Distruggo il crumb corrente per evitare si reinserire un altro documento iva (JIRA SIAC-1280)
		dissolveCurrentCrumb();
		
		return SUCCESS;
	}
	
	/**
	 * Valida l'inserimento del Documento Iva.
	 */
	public void validateInserisciDocumentoIva() {
		final String methodName = "validateInserisciDocumentoIva";
		SubdocumentoIvaEntrata sie = model.getSubdocumentoIva();
		// Controllo i campi obbligatorii
		checkNotNullNorInvalidUid(sie.getTipoRegistrazioneIva(), "Tipo Registrazione");
		checkNotNull(model.getTipoRegistroIva(), "Tipo Registro Iva");
		checkNotNullNorInvalidUid(sie.getRegistroIva(), "Registro");
		
		boolean dataDef = (model.getTipoRegistroIva() == TipoRegistroIva.VENDITE_IVA_IMMEDIATA || model.getTipoRegistroIva() == TipoRegistroIva.CORRISPETTIVI)
				&& sie.getDataProtocolloDefinitivo() != null;
		boolean dataProv = model.getTipoRegistroIva() == TipoRegistroIva.VENDITE_IVA_DIFFERITA && sie.getDataProtocolloProvvisorio() != null;

		checkCondition(dataDef || dataProv || model.isRegistrazioneSenzaProtocollo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data protocollo"));
		
		//controllo che il registro non sia bloccato. valutare se fare controllo solo lato backend
		checkModificheARegistroAbilitate(sie.getRegistroIva());
		
		checkCondition(model.getListaAliquotaSubdocumentoIva() != null && !model.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreFin.NON_CI_SONO_MOVIMENTI_IVA_ASSOCIATI.getErrore());
		
		// Carica il RegistroIva
		if(model.getTipoRegistroIva() != null) {
			try {
				caricaListaRegistroIva(model.getTipoRegistroIva(), model.getAttivitaIva());
			} catch (WebServiceInvocationFailureException e) {
				log.debug(methodName, e.getMessage());
			}
		}
		
		// Validazione data protocollo
		Date dataProtocollo = model.getTipoRegistroIva() == TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA ? 
				sie.getDataProtocolloDefinitivo() :
				sie.getDataProtocolloProvvisorio();
		if(dataProtocollo != null) {
			checkCondition(!dataProtocollo.before(model.getDocumento().getDataEmissione()), ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
		}
		// Controllo totali
		BigDecimal totaleParziale = model.getTotaleTotaleMovimentiIva();
		BigDecimal importo = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota()) ? model.getSubdocumento().getImporto() : model.getImportoRilevanteIva();
		ErroreFin erroreImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota()) ?
				ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_IMPORTO_QUOTA :
				ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_TOTALE_IMPORTO_RILEVANTE_IVA;
		ErroreFin warningImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota()) ?
				ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_DELLA_QUOTA :
				ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_RILEVANTE_IVA;
		
		String totaleParzialeFormatted = FormatUtils.formatCurrency(totaleParziale);
		String importoFormatted = FormatUtils.formatCurrency(importo);
		boolean isNotaCredito = model.getDocumento().getTipoDocumento().isNotaCredito();
		
		checkCondition((isNotaCredito && importo.add(totaleParziale).signum() >= 0) || (!isNotaCredito && importo.subtract(totaleParziale).signum() >= 0), erroreImporti.getErrore(totaleParzialeFormatted, importoFormatted));
		warnCondition((isNotaCredito && importo.add(totaleParziale).signum() == 0) || (!isNotaCredito && importo.subtract(totaleParziale).signum() == 0), warningImporti.getErrore(totaleParzialeFormatted, importoFormatted));
	}
	
	/**
	 * Carica i movimenti Iva relativi al subdocumento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimentiIva() {
		return SUCCESS;
	}
	
	/**
	 * Prepare specifico per l'apertura del collapse dei MovimentiIva
	 */
	public void prepareApriCollapseMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(new AliquotaSubdocumentoIva());
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}
	
	/**
	 * Apre il collapse dei Movimenti Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriCollapseMovimentiIva() {
		// Non faccio nulla: restituisco solo la pagina vuota
		return SUCCESS;
	}
	
	/**
	 * Popola il movimento e restituisce la pagina corretta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriModaleMovimentiIva() {
		// Prendo il movimento iva relativo alla riga selezionata
		int riga = model.getRiga().intValue();
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = ReflectionUtil.deepClone(model.getListaAliquotaSubdocumentoIva().get(riga));
		model.setAliquotaSubdocumentoIva(aliquotaSubdocumentoIva);
		
		model.setPercentualeAliquotaIva(aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeAliquota());
		model.setPercentualeIndetraibilitaAliquotaIva(aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeIndetraibilita());
		return SUCCESS;
	}
	
	/**
	 * Prepare specifico per l'inserimento del MovimentiIva
	 */
	public void prepareInserisciMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(null);
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}
	
	/**
	 * Inserisce il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciMovimentiIva() {
		final String methodName = "inserisciMovimentiIva";
		validateMovimentiIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaSubdocumentoIva(), false, model.getDocumento().getTipoDocumento().isNotaCredito());
		if(hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'inserimento del movimento iva");
			return SUCCESS;
		}
		
		popolaAliquotaIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaIva());
		// Non ho errori: aggiungo il movimento alla lista in sessione
		model.addAliquotaSubdocumentoIva(model.getAliquotaSubdocumentoIva());
		// Pulisco l'aliquota
		model.setAliquotaSubdocumentoIva(null);
		
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Popola l'aliquota a partire dall'uid della stessa e dalla lista delle possibili.
	 * 
	 * @param aliquota l'aliquota da cui ottenere l'uid
	 * @param lista    la lista in cui cercare l'aliquota
	 */
	private void popolaAliquotaIva(AliquotaSubdocumentoIva aliquota, List<AliquotaIva> lista) {
		// Ottengo i dati dell'aliquota
		AliquotaIva aliquotaIva = ComparatorUtils.searchByUid(lista, aliquota.getAliquotaIva());
		aliquota.setAliquotaIva(aliquotaIva);
	}

	/**
	 * Prepare specifico per l'aggiornamento del MovimentiIva
	 */
	public void prepareAggiornaMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(null);
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}
	
	/**
	 * Aggiorna il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaMovimentiIva() {
		final String methodName = "aggiornaMovimentiIva";
		validateMovimentiIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaSubdocumentoIva(), true, model.getDocumento().getTipoDocumento().isNotaCredito());
		if(hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'aggiornamento del movimento iva");
			return SUCCESS;
		}
		// Popolo l'aliquota
		popolaAliquotaIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaIva());
		// Non ho errori: sostituisco il movimento nella lista in sessione
		int indiceDaEliminare = model.getRiga().intValue();
		model.getListaAliquotaSubdocumentoIva().remove(indiceDaEliminare);
		model.addAliquotaSubdocumentoIva(model.getAliquotaSubdocumentoIva());
		
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Elimina il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaMovimentiIva() {
		int indiceDaEliminare = model.getRiga().intValue();
		// Elimino il movimento dalla lista in sessione
		model.getListaAliquotaSubdocumentoIva().remove(indiceDaEliminare);
		
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
}
