/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe base di action per la consultazione della richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 0202/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseConsultaRichiestaEconomaleAction<M extends BaseConsultaRichiestaEconomaleModel> extends BaseRiepilogoRichiestaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3011358347583219784L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// SIAC-5623: Controlla se il caso d'uso sia abilitato
		checkCasoDUsoApplicabile();
		
		RicercaDettaglioRichiestaEconomale request = model.creaRequestRicercaDettaglioRichiestaEconomale();
		logServiceRequest(request);
		RicercaDettaglioRichiestaEconomaleResponse response = richiestaEconomaleService.ricercaDettaglioRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioRichiestaEconomale.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca della richiesta economale con uid " + model.getRichiestaEconomale().getUid() + " avvenuta con successo");
		
		RichiestaEconomale richiestaEconomale = response.getRichiestaEconomale();
		checkTipoRichiestaEconomaleValida(richiestaEconomale);
		impostaRichiestaEconomale(richiestaEconomale);
		
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		
		AzioneConsentitaEnum[] azioniRichieste = retrieveAzioniConsentite();
		boolean consentito = AzioniConsentiteFactory.isConsentitoAll(azioniConsentite, azioniRichieste);
		if(!consentito) {
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non si dispone dei permessi necessari per l'esecuzione").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	/**
	 * Ottiene le azioni consentite richieste per l'attivazione della funzionalit&agrave;
	 * @return le azioni richieste
	 */
	protected abstract AzioneConsentitaEnum[] retrieveAzioniConsentite();
	
	/**
	 * Controlla se il tipo di richiesta economale sia valido per la funzionalit&agrave;
	 * 
	 * @param richiestaEconomale la richiesta economale da controllare
	 */
	protected void checkTipoRichiestaEconomaleValida(RichiestaEconomale richiestaEconomale) {
		// Implementazione inizialmente vuota. Da implementare se il controllo e' da effettuare
	}

	/**
	 * Impostazione della richiesta economale.
	 * 
	 * @param richiestaEconomale la richiesta da impostare nel model
	 */
	protected void impostaRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		model.setRichiestaEconomale(richiestaEconomale);
	}

	/**
	 * Validazione del metodo {@link #execute()}.
	 */
	public void validateExecute() {
		checkNotNullNorInvalidUid(model.getRichiestaEconomale(), "Richiesta economale", true);
	}
}
