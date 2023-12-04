/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.action.elaborazioniflussopagopa;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.elaborazioniflussopagopa.GenericElaborazioniFlussoModel;

/**
 * SIAC-8005
 * Classe di action generica per i flussi eleaborazione.
 * 
 * @author Alessandro Todesco
 * @version 1.0.0 - 10/02/2021
 *
 */
public class GenericElaborazioniFlussoAction<M extends GenericElaborazioniFlussoModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1040275435310765173L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	/**
	 * SIAC-8005
	 * Metodo di utilita' per gestire la numerazione in base al parametro di sessione richiesto
	 * @param String methodName
	 */
	protected void  startPositionFromSessionParamter(String methodName, BilSessionParameter bilSession) {
		int startPosition = 0;
		
		Integer startPositionInSessione = sessionHandler.getParametro(bilSession);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "StartPosition = " + startPosition);
	}
	
}
