/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso;

/*
 * SIAC-8046 19/03/2021 CM Task 2.1 
 * 
 * Enum creata per gestire gli errori per cui è lecito attivare il pulsante di aggiorna(matita)
 *  nella tabella delle riconciliazioni per la funzionalità di ricerca pagoPa
 * 
 * */
public enum ErroreRiconciliazioneDocEnum {

	
	 DATI_DI_RICONCILIAZIONE_SENZA_ESTREMI_ACCERTAMENTO("14"),
	 ERRORE_IN_INSERIMENTO_DATI_DI_DETTAGLIO_ELABORAZIONE_FLUSSO_DI_RICONCILIAZIONE("23"),
	 DATI_RICONCILIAZIONE_CON_ACCERTAMENTO_PRIVO_DI_SOGGETTO_O_INESISTENTE("51");
	
	
	private String constant;

	private ErroreRiconciliazioneDocEnum(String constant) {
		this.constant = constant;
	}

	/**
	 * @return the constant
	 */
	public String getConstant() {
		return constant;
	}
	
	
}
