/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.convalida.provvisorio;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.ConvalidaAllegatoAttoPerProvvisorioCassaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

/**
 * Classe di action per la convalida dell'allegato atto per il provvisorio.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/10/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConvalidaAllegatoAttoPerProvvisorioCassaAction extends GenericAllegatoAttoAction<ConvalidaAllegatoAttoPerProvvisorioCassaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5524923579602221271L;
	
	@Autowired private transient ProvvisorioService provvisorioService;
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile("OP-COM-convDetQuietanza");
		
		log.debug(methodName, "Impostazione dei valori di default");
		
		// Imposto i dati del provvisorio
		ProvvisorioDiCassa provvisorioDiCassa = new ProvvisorioDiCassa();
		provvisorioDiCassa.setAnno(model.getAnnoEsercizioInt());
		provvisorioDiCassa.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
		model.setProvvisorioDiCassa(provvisorioDiCassa);
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		ProvvisorioDiCassa pdc = model.getProvvisorioDiCassa();
		if(pdc == null) {
			pdc = new ProvvisorioDiCassa();
			pdc.setAnno(model.getAnnoEsercizioInt());
			pdc.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
		}
		pdc.setNumero(null);
	}
	
	/**
	 * Completamento dello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		final String methodName = "completeStep1";
		try {
			if(TipoProvvisorioDiCassa.E.equals(model.getProvvisorioDiCassa().getTipoProvvisorioDiCassa())) {
				ottieniListaSubdocumentiEntrata();
			} else if(TipoProvvisorioDiCassa.S.equals(model.getProvvisorioDiCassa().getTipoProvvisorioDiCassa())) {
				ottieniListaSubdocumentiSpesa();
			}
		}catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return INPUT;
		}
		log.debug(methodName, "Lista ottenuta. Prosecuzione allo step successivo");
		return SUCCESS;
	}
	
	/**
	 * Ottiene una prima invocazione al servizio di ricerca quote di spesa.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private void ottieniListaSubdocumentiSpesa() throws WebServiceInvocationFailureException {
		RicercaQuotaSpesa request = model.creaRequestRicercaQuoteSpesa();
		logServiceRequest(request);
		RicercaQuotaSpesaResponse response = documentoSpesaService.ricercaQuotaSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		// Controllo di avere elementi nella response
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		model.setNumeroDocumentiCollegati(response.getTotaleElementi());
		model.setTotaleDocumentiCollegati(response.getTotaleImporti());
		// Invocazione avvenuta con successo. Imposto i dati in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA, response.getListaSubdocumenti());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUOTE_SPESA, request);
	}

	/**
	 * Ottiene una prima invocazione al servizio di ricerca quote di entrata.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private void ottieniListaSubdocumentiEntrata() throws WebServiceInvocationFailureException {
		RicercaQuotaEntrata request = model.creaRequestRicercaQuoteEntrata();
		logServiceRequest(request);
		RicercaQuotaEntrataResponse response = documentoEntrataService.ricercaQuotaEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		// Controllo di avere elementi nella response
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		model.setNumeroDocumentiCollegati(response.getTotaleElementi());
		model.setTotaleDocumentiCollegati(response.getTotaleImporti());
		// Invocazione avvenuta con successo. Imposto i dati in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_ENTRATA, response.getListaSubdocumenti());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUOTE_ENTRATA, request);
	}

	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		ProvvisorioDiCassa provvisorioDiCassa = model.getProvvisorioDiCassa();
		checkNotNull(provvisorioDiCassa.getTipoProvvisorioDiCassa(), "Tipo provvisorio");
		checkNotNull(provvisorioDiCassa.getNumero(), "Numero");
		
		if(hasErrori()) {
			// Non continuo con la validazione del dato
			return;
		}
		validateProvvisorioDiCassa();
	}
	
	/**
	 * Validazione del provvisorio di cassa.
	 */
	private void validateProvvisorioDiCassa() {
		final String methodName = "validateProvvisorioDiCassa";
		// Ricerca per chiave
		RicercaProvvisorioDiCassaPerChiave request = model.creaRequestRicercaProvvisorioDiCassaPerChiave();
		logServiceRequest(request);
		RicercaProvvisorioDiCassaPerChiaveResponse response = provvisorioService.ricercaProvvisorioDiCassaPerChiave(request);
		logServiceResponse(response);
		
		// Validazione della response
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return;
		}
		if(response.isFallimento()) {
			log.info(methodName, "Fallimento nell'invocazione del servizio RicercaProvvisorioDiCassaPerChiave");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(": fallimento del servizio RicercaProvvisorioDiCassaPerChiave"));
			return;
		}
		ProvvisorioDiCassa pdc = response.getProvvisorioDiCassa();
		if(pdc == null) {
			log.info(methodName, "Provvisorio non trovato");
			ProvvisorioDiCassa pdcModel = model.getProvvisorioDiCassa();
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Provvisorio di cassa", pdcModel.getAnno() + "/" + pdcModel.getNumero()));
			return;
		}
		checkCondition(pdc.getDataAnnullamento() == null, ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Provvisorio di cassa", "annullato"));
		checkCondition(pdc.getDataRegolarizzazione() == null, ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Provvisorio di cassa", "regolarizzato"));
		log.debug(methodName, "Trovato Provvisorio con uid " + pdc.getUid());
		model.setProvvisorioDiCassa(pdc);
	}

	/**
	 * Ingresso nello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Convalida del dato.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		ConvalidaAllegatoAttoPerProvvisorio request = model.creaRequestConvalidaAllegatoAttoPerProvvisorio();
		logServiceRequest(request);
		
		// SIAC-5575: l'operazione asincrona deve essere sotto nome 'CONVALIDA'
		AzioneRichiesta azioneRichiesta = AzioniConsentite.ALLEGATO_ATTO_CONVALIDA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = allegatoAttoService.convalidaAllegatoAttoPerProvvisorioAsync(wrapRequestToAsync(request, azioneRichiesta));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Convalida allegato atto per provvisorio"));
		model.setConvalidaEffettuabile(Boolean.FALSE);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		// Dovrebbe essere inutile. Ma male non fa
		checkNotNull(model.getConvalidaManuale(), "Tipo convalida");
	}
	
}
