/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

/**
 * The Class InserisciCespiteModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class InserisciDonazioneRinvenimentoCespiteModel extends BaseInserisciCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8341829816800489822L;

	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public InserisciDonazioneRinvenimentoCespiteModel() {
		setTitolo("donazione/rinvenimento cespite");
	}

	@Override
	public Boolean getFlagDonazioneRinvenimento() {
		return Boolean.TRUE;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciDonazioneRinvenimentoCespite";
	}

	@Override
	public String getFormTitle() {
		return "Inserisci donazione/rinvenimento";
	}
}
