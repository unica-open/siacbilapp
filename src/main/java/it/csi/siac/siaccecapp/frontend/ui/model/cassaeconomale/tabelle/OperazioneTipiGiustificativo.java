/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle;

/**
 * Enum rappresentante i tipi di operazione che &eacute; possibile efettuare sui TipoGiustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/12/2014
 *
 */
public enum OperazioneTipiGiustificativo {
	
	/** Operazione di inserimento */
	INSERIMENTO("Inserimento giustificativo"),
	/** Operazione di aggiornamento */
	AGGIORNAMENTO("Aggiornamento giustificativo");
	
	private String descrizione;
	
	private OperazioneTipiGiustificativo(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}
	
	/**
	 * @return the editabile
	 */
	public boolean isEditabile() {
		return INSERIMENTO.equals(this);
	}
	
}
