/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException.Level;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step1.Scelta;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoUEBModel;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.importi.ElementoImportiVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la variazione degli importi UEB.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 06/07/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciVariazioneImportiUEBAction extends InserisciVariazioneImportiBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8768814561401283422L;
	
	@Override
	public void prepareEnterStep2() {
		if(model == null) {
			try {
				super.prepareExecute();
			} catch (Exception e) {
				throw new GenericFrontEndMessagesException("impossibile inizializzare il model", e, Level.ERROR);
			}
		}
		model.setCdu("OP-GESC001-insVar");
		model.getScelta().setScelta(Scelta.IMPORTI);
		super.prepareEnterStep2();
	}
	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String enterStep3() {
		final String methodName = "enterStep3";

		log.debug(methodName, "Inizializzazione delle liste");

		
		SpecificaVariazioneImportoUEBModel modelUEB = model.getSpecificaUEB();
		if (modelUEB.getListaUEBDaInserireNellaVariazione() == null) {
			log.debug(methodName, "listaUEBDaInserireNellaVariazione == null");
			modelUEB.setListaUEBDaInserireNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		}
		if (modelUEB.getListaUEBNellaVariazione() == null) {
			log.debug(methodName, "listaUEBNellaVariazione == null");
			modelUEB.setListaUEBNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		}
		if (modelUEB.getListaUEBNellaVariazioneCollassate() == null) {
			log.debug(methodName, "listaUEBNellaVariazioneCollassate == null");
			modelUEB.setListaUEBNellaVariazioneCollassate(new ArrayList<ElementoCapitoloVariazione>());
		}
		if (modelUEB.getListaUEBDaAnnullare() == null) {
			log.debug(methodName, "listaUEBDaAnnullare == null");
			modelUEB.setListaUEBDaAnnullare(new ArrayList<ElementoCapitoloVariazione>());
		}

		// Controllo di non essere rientrato dall'inserimento di una nuova UEB
		Boolean nuovaUEB = sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB);
		modelUEB.setRientroDaInserimentoNuovaUEB(nuovaUEB);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB, null);
		

		return SUCCESS;
	}

	/* **************************************************************************
	 * **************************************************************************
	 * *** Interazioni AJAX con la pagina di specificazione della variazione ****
	 * **************************************************************************
	 * **************************************************************************/

	/**
	 * Metodo per ottenere la lista dei capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliDaInserireNellaVariazioneUEB() {
		return SUCCESS;
	}

	/**
	 * Metodo per ottenere la lista dei capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliNellaVariazioneUEB() {
		model.getSpecificaUEB()
				.setElementoImportiVariazione(ElementoImportiVariazioneFactory.getInstance(model.getSpecificaUEB().getListaUEBNellaVariazione()));

		final String methodName = "leggiCapitoliNellaVariazione";

		log.debug(methodName, "Richiamo il webService di ricercaDettagliVariazioneImportoCapitoloNellaVariazione");

		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione();
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse response = variazioneDiBilancioService.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(request);
		
		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE, response.getListaDettaglioVariazioneImportoCapitolo());
		
		model.setTotaleStanziamentiCassaEntrata(response.getTotaleStanziamentiCassaEntrata());
		model.setTotaleStanziamentiCassaSpesa(response.getTotaleStanziamentiCassaSpesa());
		model.setTotaleStanziamentiEntrata(response.getTotaleStanziamentiEntrata());
		model.setTotaleStanziamentiSpesa(response.getTotaleStanziamentiSpesa());
		model.setTotaleStanziamentiResiduiEntrata(response.getTotaleStanziamentiResiduiEntrata());
		model.setTotaleStanziamentiResiduiSpesa(response.getTotaleStanziamentiResiduiSpesa());
				
		return SUCCESS;		
	}

	/**
	 * Metodo per il popolamento della lista degli importi da aggiornare nella Variazione UEB.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String popolaListaAggiornamentoImportiNellaVariazioneUEB() {
		return SUCCESS;
	}

	/**
	 * Metodo per il controllo delle azioni consentite all'utente.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String controllaAzioniConsentiteAllUtenteUEB() {
		TipoCapitolo tipoCapitolo = model.getSpecificaUEB().getElementoCapitoloVariazione().getTipoCapitolo();
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		model.getSpecificaUEB().setUtenteAbilitatoAdAnnullamento(AzioniConsentiteFactory.isAnnullaConsentito(tipoCapitolo, listaAzioniConsentite));
		model.getSpecificaUEB().setUtenteAbilitatoAdInserimento(AzioniConsentiteFactory.isInserisciConsentito(tipoCapitolo, listaAzioniConsentite));
		
		// Pulisco anche la lista delle UEB da inserire nella Variazione
		model.getSpecificaUEB().setListaUEBDaInserireNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		
		return SUCCESS;
	}

	/**
	 * Metodo per aggiungere un singolo capitolo nella lista dei capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiungiCapitoliDaInserireNellaVariazioneUEB() {
		final String methodName = "aggiungiCapitoliDaInserireNellaVariazioneUEB";
		ElementoCapitoloVariazione capitoloDaInserireInVariazione = model.getSpecificaUEB().getElementoCapitoloVariazione();
		List<ElementoCapitoloVariazione> listaUEBDaInserireNellaVariazione = model.getSpecificaUEB().getListaUEBDaInserireNellaVariazione();

		log.debug(methodName, "Valido la variazione da inserire nell'elenco di quelle temporanee");
		if (!validaInserimentoCapitoloNellaVariazione(capitoloDaInserireInVariazione, model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);

		// Re-injetto la lista nel model, e reimposto il capitolo come null
		model.getSpecificaUEB().setElementoCapitoloVariazione(null);
		model.getSpecificaUEB().setListaUEBDaInserireNellaVariazione(listaUEBDaInserireNellaVariazione);
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		
		return SUCCESS;
	}

	/**
	 * Metodo per aggiungere i capitoli da inserire nella variazione effettivamente nella variazione stessa.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiungiCapitoliNellaVariazioneUEB() {
		final String methodName = "aggiungiCapitoliNellaVariazioneUEB";
		
		ElementoCapitoloVariazione elementoCapitoloVariazione = model.getSpecificaUEB().getElementoCapitoloVariazione();
		
		if(!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazione, model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())){
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}
		
		InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitoloUEB();
		
		
		log.debug(methodName, "richiamo il servizio di inserimento");
		//*
		 InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(res);
			
		} //*/
		
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		return SUCCESS;
	}

	/**
	 * Metodo per aggiornare i capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaCapitoliDaInserireNellaVariazioneUEB() {
		final String methodName = "aggiornaCapitoliDaInserireNellaVariazioneUEB";
		log.debug(methodName, "Aggiorno la lista");
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiornare = model.getSpecificaUEB().getElementoCapitoloVariazione();
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiornare, model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			return SUCCESS;
		}

		// 3403
		AggiornaDettaglioVariazioneImportoCapitolo req = model.creaRequestAggiornaDettaglioVariazioneImportoCapitoloUEB();
		
		///*
		AggiornaDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.aggiornaDettaglioVariazioneImportoCapitolo(req);
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}//*/
		log.debug(methodName, "richiamato il webService di aggiornamento capitoli nella variazione");
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);

		return SUCCESS;
	}

	/**
	 * Metodo per aggiornare i capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaCapitoliNellaVariazioneUEB() {
		final String methodName = "aggiornaCapitoliNellaVariazioneUEB";
		
		ElementoCapitoloVariazione elementoCapitoloVariazione = model.getSpecificaUEB().getElementoCapitoloVariazione();
		
		if(!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazione,  model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())){
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}
		
		// 3403
		AggiornaDettaglioVariazioneImportoCapitolo req = model.creaRequestAggiornaDettaglioVariazioneImportoCapitoloUEB();
				
		///*
		AggiornaDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.aggiornaDettaglioVariazioneImportoCapitolo(req);
				
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}//*/
		log.debug(methodName, "richiamato il webService di aggiornamento capitoli nella variazione");
		
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);

		return SUCCESS;
	}

	/**
	 * Metodo per eliminare i capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String eliminaCapitoliDaInserireNellaVariazioneUEB() {
		final String methodName = "eliminaCapitoliDaInserireNellaVariazioneUEB";
		log.debug(methodName, "Modifico la lista eliminando l'elemento");

		EliminaDettaglioVariazioneImportoCapitolo req = model.creaRequestEliminaDettaglioVariazioneImportoCapitolo();
		
		log.debug(methodName, "richiamo il webservice di eliminazione capitolo nella variazione");
		EliminaDettaglioVariazioneImportoCapitoloResponse response = variazioneDiBilancioService.eliminaDettaglioVariazioneImportoCapitolo(req);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo nella variazione");
			addErrori(response);
		}
		
		// Controllo se l'elemento è stato inserito in stato provvisorio
		if (Boolean.TRUE.equals(model.getSpecificaUEB().getElementoCapitoloVariazione().getDaInserire())) {
			log.debug(methodName, "Il capitolo è provvisorio: eliminarlo");
			ServiceResponse responseEliminazione = eliminaCapitoloProvvisorio(model.getSpecificaUEB().getElementoCapitoloVariazione().unwrap());
			if(responseEliminazione.hasErrori()){
				log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo");
				addErrori(responseEliminazione);
			}
		}

		return SUCCESS;
	}

	/**
	 * Metodo per eliminare i capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String eliminaCapitoliNellaVariazioneUEB() {
		final String methodName = "eliminaCapitoliNellaVariazioneUEB";

		log.debug(methodName, "Modifico la lista eliminando l'elemento");

		EliminaDettaglioVariazioneImportoCapitolo req = model.creaRequestEliminaDettaglioVariazioneImportoCapitoloUEB();
		
		log.debug(methodName, "richiamo il webservice di eliminazione capitolo nella variazione");
		EliminaDettaglioVariazioneImportoCapitoloResponse response = variazioneDiBilancioService.eliminaDettaglioVariazioneImportoCapitolo(req);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo nella variazione");
			addErrori(response);
		}
		
		// Controllo se l'elemento è stato inserito in stato provvisorio
		if (Boolean.TRUE.equals(model.getSpecificaUEB().getElementoCapitoloVariazione().getDaInserire())) {
			log.debug(methodName, "Il capitolo è provvisorio: eliminarlo");
			ServiceResponse responseEliminazione = eliminaCapitoloProvvisorio(model.getSpecificaUEB().getElementoCapitoloVariazione().unwrap());
			if(responseEliminazione.hasErrori()){
				log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo");
				addErrori(responseEliminazione);
			}
		}
		
		return SUCCESS;
		
	}

	/**
	 * Redirige verso la creazione di un nuovo capitolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	@AnchorAnnotation(name = "Redirezione nuovo capitolo UEB", value = "%{cdu}", afterAction = true)
	public String redirezioneVersoNuovoCapitoloUEB() {
		final String methodName = "redirezioneVersoNuovoCapitoloUEB";
		String tipoCapitolo = model.getSpecificaUEB().getTipoCapitolo().toLowerCase(getLocale());
		String tipoApplicazione = model.getDefinisci().getApplicazione().getDescrizione().toLowerCase(getLocale());
		String compoundName = tipoCapitolo + "_" + tipoApplicazione;
		log.debug(methodName, "Redirezione verso inserimento: " + compoundName);
				
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, Boolean.TRUE);
		return compoundName;
	}

	/**
	 * Redirige verso la creazione di una nuova UEB.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	@AnchorAnnotation(name = "Redirezione nuova UEB", value = "%{cdu}", afterAction = true)
	public String redirezioneVersoNuovoUEB() {
		final String methodName = "redirezioneVersoNuovoUEB";
		SpecificaVariazioneImportoUEBModel specifica = model.getSpecificaUEB();

		// Aumento il numero della UEB, se presente
		Integer numeroUEB = specifica.getNumeroUEB();
		if (numeroUEB == null) {
			numeroUEB = Integer.valueOf(0);
		}
		numeroUEB++;
		specifica.setNumeroUEB(numeroUEB);

		log.debug(methodName, "Il numero della prossima UEB è " + numeroUEB);

		// Imposto in sessione la comunicazione del fatto che stiamo ragionanado sulla singola UEB: devo aggiungerla alla listaUEBDaInserireNellaVariazione
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, Boolean.TRUE);

		String tipoCapitolo = specifica.getTipoCapitolo().toLowerCase(getLocale());
		String tipoApplicazione = model.getDefinisci().getApplicazione().getDescrizione().toLowerCase(getLocale());
		String compoundName = tipoCapitolo + "_" + tipoApplicazione;
		log.debug(methodName, "Redirezione verso inserimento: " + compoundName);
		return compoundName;
	}

	/**
	 * Controlla l'annullabilit&agrave; di un capitolo con tutte le UEB collegate.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String annullaCapitoloUEB() {
		final String methodName = "annullaCapitoloUEB";
		
		List<ElementoCapitoloVariazione> listaUEBDaAnnullare = model.getSpecificaUEB().getListaUEBDaAnnullare();
		
		//ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare = model.getSpecificaUEB().getElementoCapitoloVariazione();
//		// Unwrap del wrapper
		
		for (ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare : listaUEBDaAnnullare) {
			Capitolo<?, ?> capitoloDaAnnullare = elementoCapitoloVariazioneDaAnnullare.unwrap();
//
			log.debug(methodName, "Invocazione del servizio di verifica annullabilità");
			ServiceResponse responseAnnullabilita = verificaAnnullabilitaCapitolo(capitoloDaAnnullare);
			if (responseAnnullabilita == null) {
				addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Invocazione non possibile per il capitolo"
						+ elementoCapitoloVariazioneDaAnnullare.getAnnoCapitolo() + " / " + elementoCapitoloVariazioneDaAnnullare.getNumeroCapitolo() + "/"
						+ elementoCapitoloVariazioneDaAnnullare.getNumeroArticolo()));
				return SUCCESS;
			} else if (Boolean.FALSE.equals(ReflectionUtil.getBooleanField(responseAnnullabilita, "annullabilitaCapitolo"))) {
				addErrori(responseAnnullabilita);
				return SUCCESS;
			} 

			InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitoloUEBPerAnnullamento(elementoCapitoloVariazioneDaAnnullare);
		
		
			log.debug(methodName, "richiamo il servizio di inserimento");
			//*
			InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.debug(methodName, "invocazione terminata con esito fallimento");
				addErrori(res);
			
			}
		}
		return SUCCESS;
		
	}

	
	@Override
	protected void impostaFlagCassaIncongruente(boolean isCassaIncongruente) {
		model.getSpecificaUEB().setCassaIncongruente(isCassaIncongruente);
		
	}

	@Override
	protected void impostaFlagCassaIncongruenteDopoDefinizione(boolean isCassaDopoDefinizioneIncongruente) {
		model.getSpecificaUEB().setCassaDopoDefinizioneIncongruente(isCassaDopoDefinizioneIncongruente); 
		
	}

	@Override
	protected void impostaFlagIgnoraValidazione(boolean ignoraValidazione) {
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.valueOf(ignoraValidazione));		
	}

	@Override
	protected void impostaFlagIgnoraValidazioneImportiDopoDefinizione(boolean ignoraValidazioneCapitoloDopoDefinizione) {
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.valueOf(ignoraValidazioneCapitoloDopoDefinizione));	
	}
	
	//XXX: portare su
	
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapUscitaPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaPrevisione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapUscitaGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaGestione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapEntrataPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataPrevisione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapEntrataGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataGestione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	
	
	/**
	 * Effettua ricerca capitolo nella variazione.
	 *
	 * @param req la request da inviare
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaCapitoloNellaVariazione(RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req) {
		final String methodName = "effettuaRicercaCapitoloNellaVariazione";
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse res = variazioneDiBilancioService.ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori durante l'esecuzione del servizio di ricerca singolo dettaglio variazione importo capitolo nella variazione");
			addErrori(res);
			return INPUT;
		}
		ElementoCapitoloVariazione elementoCapitoloVariazioneTrovato = ElementoCapitoloVariazioneFactory.getInstanceFromSingoloDettaglio(res.getDettaglioVariazioneImportoCapitolo(), model.isGestioneUEB());
		model.getSpecificaImporti().setElementoCapitoloVariazioneTrovatoNellaVariazione(elementoCapitoloVariazioneTrovato);
		return SUCCESS;
	}

	


}
