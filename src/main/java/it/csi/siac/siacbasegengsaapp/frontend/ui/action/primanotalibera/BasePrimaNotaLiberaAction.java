/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.BasePrimaNotaLiberaModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;

/**
 * SIAC-8134
 * 
 * @author todescoa
 *
 * @param <M> model
 */
public abstract class BasePrimaNotaLiberaAction<M extends BasePrimaNotaLiberaModel> extends GenericBilancioAction<M> {

	/** per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	/**
	 * SIAC-8134
	 * Caricamento dell'azione per la gestione della SAC
	 */
	public void caricaAzionePerSAC() {
		//da implementare a superclasse
	}

}
