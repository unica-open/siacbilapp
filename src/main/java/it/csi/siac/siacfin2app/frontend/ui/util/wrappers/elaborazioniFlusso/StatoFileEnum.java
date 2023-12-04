/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso;

/*
 * SIAC-8046 19/03/2021 CM Task 2.1 
 * 
 * Enum creata per gestire lo stato dei file collegati e per i quali è lecito attivare il pulsante di aggiorna(matita)
 *  nella tabella delle riconciliazioni per la funzionalità di ricerca pagoPa
 * 
 * */
public enum StatoFileEnum {

	 ELABORATO_IN_CORSO("ELABORATO_IN_CORSO"),
	 ELABORATO_IN_CORSO_ER("ELABORATO_IN_CORSO_ER"),
	 ELABORATO_IN_CORSO_SC("ELABORATO_IN_CORSO_SC"),
	 ELABORATO_ERRATO("ELABORATO_ERRATO"),
	 ELABORATO_SCARTATO("ELABORATO_SCARTATO");
	
	private String constant;

	private StatoFileEnum(String constant) {
		this.constant = constant;
	}

	/**
	 * @return the constant
	 */
	public String getConstant() {
		return constant;
	}
	
	
	
}
