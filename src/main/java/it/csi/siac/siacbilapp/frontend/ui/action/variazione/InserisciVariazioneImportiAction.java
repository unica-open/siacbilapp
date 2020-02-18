/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;

/**
 * Classe di action per la variazione degli importi.
 * 
 * @author Marchino Alessandro
 * @author Elisa Chiari
 * @version 1.0.0 18/10/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciVariazioneImportiAction extends InserisciVariazioneImportiBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8768814561401283421L;

	/**
	 * Metodo per aggiornare i capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaCapitoliNellaVariazione() {
		final String methodName = "aggiornaCapitoliNellaVariazione";
		log.debug(methodName, "Aggiorno la lista");
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiornare = model.getSpecificaImporti().getElementoCapitoloVariazione();
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiornare, model.getSpecificaImporti().getIgnoraValidazione(), model.getSpecificaImporti().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}

		// 3403
		AggiornaDettaglioVariazioneImportoCapitolo req = model.creaRequestAggiornaDettaglioVariazioneImportoCapitolo();
		
		///*
		AggiornaDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.aggiornaDettaglioVariazioneImportoCapitolo(req);
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}//*/
		log.debug(methodName, "Il webService di aggiornamento capitoli nella variazione ha avuto successo. Imposto a false i parametri ignoravalidazione e ignoraValidazioneDopoDefinizione");
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per aggiungere i capitoli da inserire nella variazione effettivamente nella variazione stessa.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiungiCapitoliNellaVariazione() {
		final String methodName = "aggiungiCapitoliNellaVariazione";
		
		ElementoCapitoloVariazione elementoCapitoloVariazione = model.getSpecificaImporti().getElementoCapitoloVariazione();
		
		if(!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazione, model.getSpecificaImporti().getIgnoraValidazione(), model.getSpecificaImporti().getIgnoraValidazioneImportiDopoDefinizione())){
			model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
			return SUCCESS;
		}
		
		InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitolo();
		
		log.debug(methodName, "richiamo il servizio di inserimento");
		//*
		 InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(res);
			
		} //*/
		
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		return SUCCESS;
	}
	
	@Override
	protected void impostaFlagCassaIncongruente(boolean isCassaIncongruente) {
		model.getSpecificaImporti().setCassaIncongruente(isCassaIncongruente);
		
	}

	@Override
	protected void impostaFlagCassaIncongruenteDopoDefinizione(boolean isCassaDopoDefinizioneIncongruente) {
		model.getSpecificaImporti().setCassaDopoDefinizioneIncongruente(isCassaDopoDefinizioneIncongruente); 
		
	}

	@Override
	protected void impostaFlagIgnoraValidazione(boolean ignoraValidazione) {
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.valueOf(ignoraValidazione));		
	}

	@Override
	protected void impostaFlagIgnoraValidazioneImportiDopoDefinizione(boolean ignoraValidazioneCapitoloDopoDefinizione) {
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.valueOf(ignoraValidazioneCapitoloDopoDefinizione));	
	}
	
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
	
	/**
	 * Go to aggiorna.
	 *
	 * @return the string
	 */
	public String goToAggiorna() {
		return SUCCESS;
	}
}
