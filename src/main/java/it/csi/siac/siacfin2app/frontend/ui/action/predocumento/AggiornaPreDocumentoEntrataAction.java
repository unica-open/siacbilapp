/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommon.util.DataValidator;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.AggiornaPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumentoResponse;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Classe di action per l'aggiornamento del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 29/04/2014
 * @version 1.0.1 - 11/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaPreDocumentoEntrataAction extends GenericPreDocumentoEntrataAction<AggiornaPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3410995502977493215L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoCausale();
			caricaListaContoCorrente();
			caricaListaNazioni();
			caricaListaSesso();
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
			caricaListaTipoFinanziamento();
			caricaListaCausaleEntrata();
			caricaListaTipoDocumento();
			checkisFromCompletaDefinisci();
			
			checkDecentrato();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}

	/**
	 * Controlla se viene da una ricerca di completa definisci
	 */
	private void checkisFromCompletaDefinisci() {
		String methodName = "checkisFromCompletaDefinisci";
		if(sessionHandler.containsKey(BilSessionParameter.FROM_COMPLETA_DEFINISCI)) {
			log.debug(methodName, ": vengo da Completa Definisci PreDoc " );
			model.setFromCompletaDefinisci((Boolean) sessionHandler.getParametro(BilSessionParameter.FROM_COMPLETA_DEFINISCI));
		}
	}
	
	/**
	 * Controlla se l'utente sia decentrato
	 */
	private void checkDecentrato() {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isDecentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite);
		model.setUtenteDecentrato(Boolean.TRUE.equals(isDecentrato));
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute(){
		final String methodName = "execute";
		// Caricamento valori default
		checkCasoDUsoApplicabile("Aggiornamento preDocumento di entrata");
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		
		checkIsReturn();
		
		// Ricerca di dettaglio del PreDocumento
		RicercaDettaglioPreDocumentoEntrata req = model.creaRequestRicercaDettaglioPreDocumentoEntrata();
		logServiceRequest(req);
		RicercaDettaglioPreDocumentoEntrataResponse response = preDocumentoEntrataService.ricercaDettaglioPreDocumentoEntrata(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio preDocumento spesa");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
		}
		
		//SIAC-6780
		try {
			impostaDatiNelModel(response.getPreDocumentoEntrata(), null);
		} catch (Exception e) {
			log.error("execute", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		//
		
		return SUCCESS;
	}
	
	private void checkIsReturn() {
		if((model.getUidPreDocumentoDaAggiornare() == null || model.getUidPreDocumentoDaAggiornare() == 0) && sessionHandler.containsKey(BilSessionParameter.PREDOC_SELEZIONATO)) {
			model.setUidPreDocumentoDaAggiornare(((PreDocumentoEntrata) sessionHandler.getParametro(BilSessionParameter.PREDOC_SELEZIONATO)).getUid());
		}
	}

	//SIAC-6780
	//raggruppo in un unico metodo dei passagi condivisi dal rientro dopo l'associa e l'execute
	private void impostaDatiNelModel(PreDocumentoEntrata preDoc, SubdocumentoEntrata subDoc) throws WebServiceInvocationFailureException {
		String methodName = "impostaDatiNelModel";
		
		log.debug(methodName, "PreDocumento caricato. Imposto i dati nel model");
		model.impostaPreDocumento(preDoc);
		
		AttoAmministrativo attoAmministrativo = caricaAttoAmministrativoSePresente();
		if(attoAmministrativo != null) {
			model.impostaAttoAmministrativo(attoAmministrativo);
		}
		
		if(preDoc.getProvvisorioDiCassa() != null && preDoc.getProvvisorioDiCassa().getUid() !=0){
			model.setProvvisorioCassa(preDoc.getProvvisorioDiCassa());
		}
		
		model.setForzaDisponibilitaAccertamento(false);
		
		//controllo che sia presente il subdoc
		if(subDoc != null 
				&& subDoc.getNumero() != null 
				&& subDoc.getDocumento().getNumero() != null
				&& subDoc.getDocumento().getAnno() != null
				&& subDoc.getNumero() != null
				&& subDoc.getDataCreazione() != null
				&& subDoc.getAccertamento() != null) {
			model.setSubdocumento(subDoc);
		} else {
			log.error(methodName, "Subocumento non caricato correttamente nel model");
		}
		caricaListaCausaleEntrata();

	}
	
	//SIAC-6780
	public String ritornoPerAssociaQuotaPreDocumento() {
		final String methodName = "aggiornaPreDocumentoEntrata - ritornoPerAssociaQuotaPreDocumento";

		if(!(sessionHandler.containsKey(BilSessionParameter.PREDOC_SELEZIONATO)
				&& sessionHandler.getParametro(BilSessionParameter.PREDOC_SELEZIONATO) != null)) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}
		
		if(!(model.getUidSubDocumentoDaAssociare() != null && model.getUidSubDocumentoDaAssociare() != 0)) {
			log.debug(methodName, "Validazione fallita");
			addErrore(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore());
			return INPUT;
		}

		//passo direttamente il predoc anzichè l'uid risparmio una chiamata
		model.setPreDocumento((PreDocumentoEntrata) sessionHandler.getParametro(BilSessionParameter.PREDOC_SELEZIONATO));
		//COLLEGO SUBDOC A PREDOC
		AggiornaPreDocumentoEntrataCollegaDocumento request = model.creaRequestCollegaDocumento();
		AggiornaPreDocumentoEntrataCollegaDocumentoResponse response = preDocumentoEntrataService.aggiornaPreDocumentoEntrataCollegaDocumento(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio subdocumento Entrata");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
			return INPUT;
		}
		
		try {
			impostaDatiNelModel(response.getPreDocumentoEntrataAggiornato(), response.getSubDocumentoEntrata());
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		//TODO migliorare il codice dell'informazione
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
	//SIAC-6780
	public String cercaQuotePerAssociaDocumento() {
		final String methodName = "aggiornaPreDocumentoEntrata - cercaDocumento";
		
		if (!(model.getDocumento().getAnno() != null || model.getDocumento().getNumero() != null || model.getDocumento().getTipoDocumento().getUid() != 0 || model.getDocumento().getSoggetto() != null)) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}
		
		log.debug(methodName, "Effettua la ricerca");
		
		RicercaQuoteDaAssociarePredocumento request = model.creaRequestRicercaQuoteDaAssociarePerCollegaDocumento();
		logServiceRequest(request);
		RicercaQuoteDaAssociarePredocumentoResponse response = documentoService.ricercaQuoteDaAssociarePredocumento(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		if(response.getListaSubdocumenti().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Totale: "+response.getListaSubdocumenti().getTotaleElementi());
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_ASSOCIARE, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_ASSOCIARE, response.getListaSubdocumenti());
		
		log.debug(methodName, "Totale quote di spesa: " + response.getTotaleElementi());
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getTotaleImporti());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		//TODO put the importoPreDocumento in session for the comparision to the collegaDocumento's action.
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_PREDOC_TO_COMPARE, model.getPreDocumento().getImporto() != null ? model.getPreDocumento().getImporto() : BigDecimal.ZERO);
//		sessionHandler.setParametro(BilSessionParameter.UID_PREDOC_SELEZIONATO, model.getPreDocumento().getUid());
		sessionHandler.setParametro(BilSessionParameter.PREDOC_SELEZIONATO, model.getPreDocumento());
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna il preDocumento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		
		log.debug(methodName, "Aggiornamento del PreDocumento di Entrata");
		
		if(StringUtils.isNotEmpty(model.getMessaggioRichiestaConfermaProsecuzione())){
			
			log.debug(methodName, "Sono presenti dei messaggi. E' necessaria la conferma dell'utente. ");
			model.setRichiediConfermaUtente(true);
			
			return INPUT;
		}
		
		model.setRichiediConfermaUtente(false);
		
		AggiornaPreDocumentoDiEntrata req = model.creaRequestAggiornaPreDocumentoDiEntrata();
		logServiceRequest(req);
		AggiornaPreDocumentoDiEntrataResponse response = preDocumentoEntrataService.aggiornaPreDocumentoDiEntrata(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento nell'invocazione del servizio");
			addErrori(response);
			return INPUT;
		}
		
		PreDocumentoEntrata preDocumentoEntrata = response.getPreDocumentoEntrata();
		
		log.debug(methodName, "Aggiornato il PreDocumento numero " + preDocumentoEntrata.getNumero() + " con uid " + preDocumentoEntrata.getUid());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		setMessaggiInSessionePerActionSuccessiva();
		
		model.setPreDocumento(preDocumentoEntrata);
		model.setUidPreDocumentoDaAggiornare(preDocumentoEntrata.getUid());
		
		// Redirigo verso ricerca o aggiornamento a seconda che sia arrivato da ricerca o da cruscotto
		String result = AGGIORNA;
		
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.class))) {
			sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.FALSE);
			result = RICERCA;
		}

		if(Boolean.TRUE.equals(model.isFromCompletaDefinisci())) {
			result = "ricercaCompletaDefinisci";
		}
		
		return result;
	}
	
	/**
	 * Validazione per l'inserimento del preDocumento.
	 */
	public void validateAggiornamento() {
		// Check campi obbligatori
		PreDocumentoEntrata preDocumentoEntrata = model.getPreDocumento();
		checkNotNull(preDocumentoEntrata.getDataCompetenza(), "Data");
		checkNotNullNorEmpty(preDocumentoEntrata.getPeriodoCompetenza(), "Periodo competenza");
		checkNotNull(preDocumentoEntrata.getImporto(), "Importo");
		checkCondition(preDocumentoEntrata.getImporto() == null || preDocumentoEntrata.getImporto().signum()>0,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("importo",": l'importo deve essere positivo"));
		
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausaleEntrata(), "Causale");
		
		// Validazione logica
		// SIAC-4574: non e' piu' necessario controllare la data di competenza
		DatiAnagraficiPreDocumento datiAnagraficiPreDocumentoEntrata = model.getDatiAnagraficiPreDocumento();
		
		// Validazioni specifiche
		validazioneSoggetto();
		validazioneCapitolo();
		//SIAC-7470 - anche in aggiornamento controllo il bloccoROR
		validazioneAccertamentoSubAccertamento(Integer.valueOf(1));
		validazioneAttoAmministrativo();
		
		//metodi aggiunti in data 05/06/2015
		validazioneProvvisorioDiCassaPredocumentoDiEntrata();
		
		controlloConguenzaSoggettoMovimentoGestione(model.getSoggetto(), model.getMovimentoGestione(), model.getSubMovimentoGestione(),
				"predisposizione di incasso", "accertamento");
		controlloCongruenzaCapitoloAccertamento();
		
		// Controlli su codice fiscale e partita IVA
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumentoEntrata.getCodiceFiscale()) || 
				ValidationUtil.isValidCodiceFiscaleEvenTemporary(datiAnagraficiPreDocumentoEntrata.getCodiceFiscale()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("codice fiscale", ": il codice fiscale non e' sintatticamente valido"));
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumentoEntrata.getPartitaIva()) ||
				DataValidator.isValidPartitaIVA(datiAnagraficiPreDocumentoEntrata.getPartitaIva()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("partita IVA", ": la partita IVA non e' sintatticamente valida"));
	}
	
	@Override
	protected BilSessionParameter getParametroListaCausale() {
		return BilSessionParameter.LISTA_CAUSALE_ENTRATA_NON_ANNULLATE;
	}
	
}
