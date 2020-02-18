/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle;

/**
 * Enum rappresentante i tipi di operazione che &eacute; possibile efettuare sui TipoOperazioneCassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/12/2014
 *
 */
public enum OperazioneTipiOperazioneDiCassa {
	
	/** Operazione di inserimento */
	INSERIMENTO("Inserimento tipo operazione di cassa"),
	/** Operazione di aggiornamento */
	AGGIORNAMENTO("Aggiornamento tipo operazione di cassa");
	
	private String descrizione;
	
	private OperazioneTipiOperazioneDiCassa(String descrizione) {
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
