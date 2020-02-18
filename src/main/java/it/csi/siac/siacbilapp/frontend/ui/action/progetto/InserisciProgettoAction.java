/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.InserisciProgettoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione dell'inserimento del Progetto.
 * 
 * @author Osorio Alessandra,Nazha Ahmad
 * @version 1.0.0 - 04/02/2013
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciCronoprogrammaAction.MODEL_NAME_INSERIMENTO_CON_PROGETTO)
public class InserisciProgettoAction extends GenericProgettoAction<InserisciProgettoModel>{
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		final String methodName = "prepareExecute";
		log.debug(methodName, "Preparazione della action");
		setModel(null);
		super.prepare();			
		caricaListeCodifiche();
		caricaListaModalitaAffidamento();
		model.setAttoAmministrativo(null);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		if(model.getProgetto() == null) {
			model.setProgetto(new Progetto());
			model.getProgetto().setRilevanteFPV(Boolean.FALSE);
			model.getProgetto().setInvestimentoInCorsoDiDefinizione(Boolean.TRUE);
			model.getProgetto().setTipoProgetto(obtainTipoProgettoByFaseBilancio());
		}
		
		return SUCCESS;
	}
	
	/**
	 * Ingresso nella pagina.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enter() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #salvaProgetto()}.
	 */
	public void prepareSalvaProgetto() {
		cleanModel();
	}
	
	/**
	 * Pulizia dei dati del model.
	 */
	private void cleanModel() {
		model.setProgetto(null);
		model.setAttoAmministrativo(null);
		model.setUidProvvedimento(null);
	}
	
	/**
	 * Salva l'anagrafica del Progetto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String salvaProgetto() {
		final String methodName = "salvaProgetto";
		
		try {
			controlloProgettoNonGiaPresente();
			inserisciAnagraficaProgetto();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		impostaInformazioneSuccesso();
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #salvaProgetto()}.
	 */
	public void validateSalvaProgetto() {
		innerValidate();
	}
	
	/**
	 * Preparazione per il metodo {@link #proseguiProgetto()}.
	 */
	public void prepareProseguiProgetto() {
		cleanModel();
	}
	
	/**
	 * Salva l'anagrafica del Progetto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String proseguiProgetto() {
		final String methodName = "proseguiProgetto";
		
		try {
			controlloProgettoNonGiaPresente();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		model.getProgetto().setAttoAmministrativo(model.getAttoAmministrativo());		
		log.debug(methodName, "Redirigo al cronoprogramma. Sara' tale funzionalita' a eseguire l'inserimento del progetto");
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #proseguiProgetto()}.
	 */
	public void validateProseguiProgetto() {
		innerValidate();
	}

	/**
	 * Controlla che il progetto non siac gi&agrave; presente in archivio.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void controlloProgettoNonGiaPresente() throws WebServiceInvocationFailureException {
		final String methodName = "controlloProgettoNonGiaPresente";
		if(model.isCodiceProgettoAutomatico()) {
			log.debug(methodName, "Il codice e' impostato in automatico dal sistema, suppongo che vada bene");
			return;
		}
		log.debug(methodName, "Controllo che non vi sia un progetto gia' inserito utilizzando la ricerca puntuale");
		
		RicercaPuntualeProgetto request = model.creaRequestRicercaPuntualeProgetto(obtainTipoProgettoByFaseBilancio());
		logServiceRequest(request);
		
		RicercaPuntualeProgettoResponse response = progettoService.ricercaPuntualeProgetto(request);
		logServiceResponse(response);
		
		if(response.hasErrori() && !response.verificatoErrore(ErroreCore.ENTITA_NON_TROVATA)) {
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		if(response.getProgetto() != null) {
			addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Progetto", model.getProgetto().getCodice()));
			throw new WebServiceInvocationFailureException("Progetto gis' esistente");
		}
		
		log.debug(methodName, "Nessun progetto gia' presente");
	}
	
	/**
	 * Inserimento dell'anagrafica del progetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void inserisciAnagraficaProgetto() throws WebServiceInvocationFailureException {
		final String methodName = "inserisciAnagraficaProgetto";
		
		log.debug(methodName, "Inserimento dell'anagrafica del progetto");
		
		InserisceAnagraficaProgetto request = model.creaRequestInserisceAnagraficaProgetto();
		logServiceRequest(request);
		
		InserisceAnagraficaProgettoResponse response = progettoService.inserisceAnagraficaProgetto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		Progetto progetto = response.getProgetto();
		log.debug(methodName, "Inserito progetto con uid " + progetto.getUid());
		
		// Injetto il progetto ottenuto dalla response nel model
		model.setProgetto(progetto);
		model.setIdProgetto(progetto.getUid());
	}
	
	/**
	 * Validazione interna.
	 */
	private void innerValidate() {
		if(model.getProgetto().getInvestimentoInCorsoDiDefinizione()==null) {
			model.getProgetto().setInvestimentoInCorsoDiDefinizione(Boolean.FALSE);
		}
		Progetto progetto = model.getProgetto();
		checkNotNull(progetto, "Progetto", true);
		checkCondition(model.isCodiceProgettoAutomatico() || StringUtils.isNotBlank(progetto.getCodice()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
		// SIAC-6005
		// checkNotNull(progetto.getDescrizione(), "Descrizione");
		checkNotNullNorEmpty(progetto.getDescrizione(), "Descrizione");
		
		
		// Controlla che il progetto abbia associato un provvedimento
		// CR 2305 PROVVEDIMENTO NON E' piu obbligatorio
		// SIAC-4427 - controllo ripristinato
		checkCondition(model.isProvvedimentoValorizzato() , ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento"));
		
		// Controlla che il provvedimento non sia annullato
		checkCondition(!model.isProvvedimentoValorizzato() || !"ANNULLATO".equalsIgnoreCase(model.getAttoAmministrativo().getStatoOperativo()), ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore());
		
		//SIAC-6255
		TipoProgetto tipoProgetto = obtainTipoProgettoByFaseBilancio();
		checkCondition(tipoProgetto.equals(progetto.getTipoProgetto()) , ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo progetto, atteso " + tipoProgetto.getDescrizione()));
	}
	
	/**
	 * Pulizia del model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String pulisci() {
		cleanModel();
		
		// Inserisco i dati di default
		Progetto progetto = new Progetto();
		progetto.setRilevanteFPV(Boolean.FALSE);
		progetto.setInvestimentoInCorsoDiDefinizione(Boolean.TRUE);
		progetto.setTipoProgetto(obtainTipoProgettoByFaseBilancio());
		model.setProgetto(progetto);
		
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		Bilancio bilancio = model.getBilancio();
		if(bilancio == null || bilancio.getUid() == 0 || bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio()== null) {
			RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
			RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
			bilancio =  response.getBilancio();
		}
		
		
		FaseBilancio faseBilancio = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Il Bilancio non deve essere in fase Pluriennale, Previsione né chiuso
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.CHIUSO.equals(faseBilancio) || 
				FaseBilancio.ASSESTAMENTO.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
		
		super.checkCasoDUsoApplicabile(cdu);
	}
	
}
