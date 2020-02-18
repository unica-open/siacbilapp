/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccommon.util.DataValidator;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.AggiornaPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

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
			
			checkDecentrato();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}

	/**
	 * Controlla se l'utente sia decentrato
	 */
	private void checkDecentrato() {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isDecentrato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite);
		model.setUtenteDecentrato(Boolean.TRUE.equals(isDecentrato));
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Caricamento valori default
		checkCasoDUsoApplicabile("Aggiornamento preDocumento di entrata");
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		
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
		
		log.debug(methodName, "PreDocumento caricato. Imposto i dati nel model");
		model.impostaPreDocumento(response.getPreDocumentoEntrata());
		
		AttoAmministrativo attoAmministrativo = caricaAttoAmministrativoSePresente();
		if(attoAmministrativo != null) {
			model.impostaAttoAmministrativo(attoAmministrativo);
		}
		
		if(response.getPreDocumentoEntrata().getProvvisorioDiCassa() != null && response.getPreDocumentoEntrata().getProvvisorioDiCassa().getUid() !=0){
			model.setProvvisorioCassa(response.getPreDocumentoEntrata().getProvvisorioDiCassa());
		}
		
		model.setForzaDisponibilitaAccertamento(false);
		
		caricaListaCausaleEntrata();
		
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
				ErroreCore.VALORE_NON_VALIDO.getErrore("importo",": l'importo deve essere positivo"));
		
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausaleEntrata(), "Causale");
		
		// Validazione logica
		// SIAC-4574: non e' piu' necessario controllare la data di competenza
		DatiAnagraficiPreDocumento datiAnagraficiPreDocumentoEntrata = model.getDatiAnagraficiPreDocumento();
		
		// Validazioni specifiche
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneAccertamentoSubAccertamento();
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
