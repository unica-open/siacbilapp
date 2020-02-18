/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RisultatiRicercaAllegatoAttoBaseModel;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 *
 * @author elisa
 * @version 1.0.0 - 26/feb/2018
 * @param <M> the generic type
 */
public abstract class RisultatiRicercaAllegatoAttoBaseAction<M extends RisultatiRicercaAllegatoAttoBaseModel> extends GenericAllegatoAttoAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6203792610769346089L;

	@Override
	public void prepare() throws Exception {
		// Preparazione base
		super.prepare();
		final String methodName = "prepare";
		// Ottengo la posizione di partenza
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart);
		// Riottengo la stringa di riepilogo
		String stringaRiepilogo = sessionHandler.getParametro(BilSessionParameter.RIEPILOGO_RICERCA_ALLEGATO_ATTO);
		log.debug(methodName, "Stringa di riepilogo: " + stringaRiepilogo);
		model.setStringaRiepilogo(stringaRiepilogo);
	}

}
