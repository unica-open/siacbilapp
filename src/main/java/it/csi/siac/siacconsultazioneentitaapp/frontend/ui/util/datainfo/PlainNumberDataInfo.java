/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Dato con un solo dato di tipo numerico da gestire come numero senza separatori decimali.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 11/07/2016
 *
 */
public class PlainNumberDataInfo extends BaseDataInfo {
	
	/**
	 * Costruttore.
	 * @param name il nome del campo
	 * @param campoKey la chiave del campo
	 */
	public PlainNumberDataInfo(String name, String campoKey) {
		super(name, "{0,number,#}", campoKey);
	}
}