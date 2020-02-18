/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.AggiornaCronoprogrammaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per l'aggiornamento del Cronoprogramma.
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 13/02/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaCronoprogrammaAction extends GenericCronoprogrammaAction<AggiornaCronoprogrammaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7882848972184627557L;

	@Override
	public void prepare() throws Exception {
		// Pulisco tutto
		cleanErrori();
		cleanInformazioni();
		cleanMessaggi();
		impostaValoriAggiornabilitaCronoprogramma();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
		caricaListaTipiAtto();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	@SkipValidation
	public String execute() throws Exception {
		final String methodName = "execute";
		model.clean();
		checkCasoDUsoApplicabile(model.getTitolo());
		
		try {
			effettuaCaricamentoDettaglioCronoprogramma();
			effettuaCaricamentoDettaglioProgetto();
			effettuaCaricamentoListeClassificatori();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * Imposta valori aggiornabilita cronoprogramma.
	 * Dai Requisiti:
	 * <p>
	 * I cronoprogrammi, nei seguenti due casi:
	 * <ul>
	 * 	<li>cronoprogramma di gestione associato ad un provvedimento definitivo</li>
	 *  <li>cronoprogramma di previsione scelto per FPV</li>
	 * </ul>
	 *   non risulta più modificabile, ma l'utente potr&agrave; comunque entrare nella pagina di aggiornamento.
	 * Il sistema visualizzer&agrave; l'elenco delle entrate e delle spese previste senza azioni abilitate (aggiorna o annulla) 
	 * ed un messaggio in cui avvisa che il cronoprogramma non è più modificabile.
	 * </p>
	 */
	private void impostaValoriAggiornabilitaCronoprogramma() {
		if(model.isCronoAggiornabile() ||  model.getCronoprogramma() == null) {
			return;
		}
		boolean cronoGestioneAggiornabile = model.isCollegatoAProgettoDiGestione() && StatoOperativoCronoprogramma.PROVVISORIO.equals(model.getCronoprogramma().getStatoOperativoCronoprogramma());
		boolean cronoPrevisioneAggiornabile = model.isCollegatoAProgettoDiPrevisione() && !Boolean.TRUE.equals(model.getCronoprogramma().getUsatoPerFpv());
		model.setCronoAggiornabile(cronoGestioneAggiornabile || cronoPrevisioneAggiornabile);
		if(!model.isCronoAggiornabile()) {
			addMessaggio(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("cronoprogramma", "non modificabile"));
		}
	}

	/**
	 * Aggiorna il Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		
		if(checkPresenzaCronoprogrammaAssociato()) {
			return INPUT;
		}
		
		// Non vi sono cronoprogrammi già associati
		AggiornaAnagraficaCronoprogramma request = model.creaRequestAggiornaAnagraficaCronoprogramma();
		logServiceRequest(request);
		AggiornaAnagraficaCronoprogrammaResponse response = progettoService.aggiornaAnagraficaCronoprogramma(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nell'invocazione del servizio di aggiornamento cronoprogramma");
			addErrori(response);
			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link AggiornaCronoprogrammaAction#aggiornaCDU()}.
	 */
	public void validateAggiornaCDU(){
		validaAnagrafica();
		try {
			validaDettagli();
		} catch(ParamValidationException e) {
			// Verificatasi un'eccezione. Non importa: ignoro e proseguo
		}
		setMessaggiInSessionePerActionSuccessiva();
		logActionErrorsAndMessages();
	}

	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		final String codiceAzione = AzioniConsentite.CRONOPROGRAMMA_AGGIORNA.getNomeAzione();
		List<AzioneConsentita> list = sessionHandler.getAzioniConsentite();
		for(AzioneConsentita azioneConsentita : list) {
			if(azioneConsentita.getAzione().getNome().equalsIgnoreCase(codiceAzione)) {
				// Sono a posto
				return;
			}
		}
		// Non mi è consentito giungere all'azione
		Errore errore = ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("inserimento cronoprogramma");
		throw new GenericFrontEndMessagesException(errore.getTesto(), GenericFrontEndMessagesException.Level.ERROR);
	}
		
	/**
	 * Effettua una ricerca di dettaglio del Cronoprogramma e carica il risultato-
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void effettuaCaricamentoDettaglioCronoprogramma() throws WebServiceInvocationFailureException {
		final String methodName = "effettuaCaricamentoDettaglioCronoprogramma";
		
		log.debug(methodName, "Caricamento del cronoprogramma");
		RicercaDettaglioCronoprogramma request = model.creaRequestRicercaDettaglioCronoprogramma();
		logServiceRequest(request);
		RicercaDettaglioCronoprogrammaResponse response = progettoService.ricercaDettaglioCronoprogramma(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		model.popolaDettaglioCronoprogramma(response.getCronoprogramma());
	}
	
	/**
	 * Controlla se un altro cronoprogramma con stesso codice sia gi&agrave; stato associato al progetto.
	 * 
	 * @return <code>true</code> se vi &eacute; un cronoprogramma associato, non pari all'attuale; <code>false</code> in caso contrario
	 */
	private boolean checkPresenzaCronoprogrammaAssociato() { 
		final String methodName = "checkPresenzaCronoprogrammaAssociato";
		// Ricerco il cronoprogramma
		RicercaCronoprogramma request = model.creaRequestRicercaCronoprogramma();
		RicercaCronoprogrammaResponse response = progettoService.ricercaCronoprogramma(request);
		
		boolean erroriNellInvocazione = response.hasErrori() && !response.verificatoErrore(ErroreCore.ENTITA_NON_TROVATA.getCodice());
		List<Cronoprogramma> cronos = response.getCronoprogramma();
		boolean cronoTrovato = cronos != null && !cronos.isEmpty();
		
		if(erroriNellInvocazione) {
			// Ho un altro errore: esco
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return true;
		}

		
		if(cronoTrovato){
			for (Cronoprogramma cronoprogramma : cronos) {
				if(cronoprogramma.getUid() != model.getUidCronoprogramma().intValue()) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Pulizia del model.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String clean() {
		final String methodName = "clean";
		model.setCronoprogramma(null);
		model.getListaDettaglioEntrataCronoprogramma().clear();
		model.getListaDettaglioUscitaCronoprogramma().clear();
		
		try {
			// Ricarico il cronoprograma
			effettuaCaricamentoDettaglioCronoprogramma();
		} catch (WebServiceInvocationFailureException wsife) {
			log.error(methodName, "Fallimento nella pulizia della action: " + wsife.getMessage(), wsife);
		}
		
		return SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* ***** Metodi AJAX ***** */
	
	@Override
	public String cancellaDettaglioCronoprogrammaEntrata() {
		Integer indice = model.getIndiceDettaglioNellaLista();
		if(indice != null && indice.intValue() > -1) {
			DettaglioEntrataCronoprogramma dettaglioEliminato = model.getListaDettaglioEntrataCronoprogramma().remove(indice.intValue());
			model.getListaDettaglioEntrataCronoprogrammaDaEliminare().add(dettaglioEliminato);
		}
		return SUCCESS;
	}
	
	@Override
	public String cancellaDettaglioCronoprogrammaUscita() {
		Integer indice = model.getIndiceDettaglioNellaLista();
		if(indice != null && indice.intValue() > -1) {
			DettaglioUscitaCronoprogramma dettaglioEliminato = model.getListaDettaglioUscitaCronoprogramma().remove(indice.intValue());
			model.getListaDettaglioUscitaCronoprogrammaDaEliminare().add(dettaglioEliminato);
		}
		return SUCCESS;
	}
	
}
