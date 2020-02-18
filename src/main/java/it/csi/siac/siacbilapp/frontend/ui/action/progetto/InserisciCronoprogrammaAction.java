/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.progetto.InserisciProgettoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per l'inserimento del Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @author Nazha Ahmad
 * @version 1.1.0 - 13/02/2014
 * @version 1.1.0 - 22/05/2015 - rifattorizzazione per gestione del wizard del progetto
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciCronoprogrammaAction.MODEL_NAME_INSERIMENTO_CON_PROGETTO)
public class InserisciCronoprogrammaAction extends BaseInserisciCronoprogrammaAction<InserisciProgettoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 488381582214984854L;
	
	@Override
	public void prepareExecute() throws Exception {
		// Non faccio alcunche'
		//SIAC-6783
		model.setCronoprogramma(new Cronoprogramma());
		model.getListaDettaglioEntrataCronoprogramma().clear();
		model.getListaDettaglioUscitaCronoprogramma().clear();
	}
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		checkCasoDUsoApplicabile();		
		try {
			effettuaCaricamentoListeClassificatori();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		model.setAttoAmministrativo(null);
		model.setCronoAggiornabile(true);
		return SUCCESS;
	}
	
	@Override
	public String inserisciCDU() {
		final String methodName = "inserisciCDU";
		
		try {
			gestioneAnagraficaProgetto();
			// Non vi sono cronoprogrammi già associati
			inserisceCronoprogramma();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}

	/**
	 * Gestione anagrafica progetto.
	 *
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	private void gestioneAnagraficaProgetto() throws WebServiceInvocationFailureException {
		if(model.getProgetto().getUid() != 0) {
			//succede nel caso in cui io ho gia' chiamato inserisci cdu, l'inserimento del progetto e' andato a buon fine ma quello del cronoprogramma no
			return;
		}
		// Inserisco il progetto
		controlloProgettoNonGiaPresente();
		inserisciAnagraficaProgetto();
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
		
		
		RicercaPuntualeProgetto request = model.creaRequestRicercaPuntualeProgetto(model.getProgetto().getTipoProgetto());
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
		
		InserisceAnagraficaProgetto request = model.creaRequestInserisceAnagraficaProgettoDaCronoProgramma();
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
		model.setUidProgetto(progetto.getUid());
	}
	
	/**
	 * Redirezione al progetto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String backToProgetto() {
		//SIAC-6783
		model.setCronoprogramma(null);
		//lascio i campi precedenti comentati, perche' vi sono Jira contrastanti al riguardo, potrebnbero cambiare idea
//		model.getCronoprogramma().setAttoAmministrativo(model.getAttoAmministrativo());
		model.setAttoAmministrativo(model.getProgetto().getAttoAmministrativo());
		return SUCCESS;
	}
	
	/**
	 * Check importi dettagli uscita cronoprogramma.
	 */
	private void checkImportiDettagliUscitaCronoprogramma() {
		BigDecimal valoreComplessivoProgetto =model.getProgetto().getValoreComplessivo();
		if(valoreComplessivoProgetto == null || BigDecimal.ZERO.compareTo(valoreComplessivoProgetto) == 0) {
			//SCELTA IMPLEMENTATIVA: il valore complessivo del progetto non e' obbligatorio, se e' presente il valore di default non lo metto
			return;
		}
		BigDecimal importoRigheSpesa = BigDecimal.ZERO;
		for (DettaglioUscitaCronoprogramma dett : model.getListaDettaglioUscitaCronoprogramma()) {
			if(dett.getUid()!=0 && dett.getDataFineValidita()!=null) {
				//questo cronoprogramma verra' cancellato: non va conteggiato
				continue;
			}
			importoRigheSpesa = importoRigheSpesa.add(dett.getStanziamento());			
		}
		
		checkCondition(importoRigheSpesa.compareTo(valoreComplessivoProgetto) <= 0, ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" totale delle spese previste", "superato il valore complessivo del progetto (valore complessivo = " + valoreComplessivoProgetto.toPlainString() + ")."));
	}
	

	@Override
	protected void validaDettagli(){
		super.validaDettagli();
//		checkImportiDettagliUscitaCronoprogramma();
	}
}
