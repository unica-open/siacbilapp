/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con il numero del mandato in un popover.
 * @author Elisa Chiari
 * @version 1.0.0 - 31/03/2016 
 *
 */
public class NumeroMandatoDataInfo extends PopoverDataInfo {

	/**
	 * Costruttore.
	 * 
	 * @param name il nome
	 * @param numeroMandatoKey il numero del mandato
	 * @param numeroSubMandatoKey il numero del sub
	 * @param descrizioneMandatoKey la descrizione del mandato
	 */
	public NumeroMandatoDataInfo(String name, String numeroMandatoKey, String numeroSubMandatoKey, String descrizioneMandatoKey) {
		super(name, 
				"<p class='popover-content'> {2}</p>", 
				"right", 
				"", 
				"{0,number,#}/{1}",
				numeroMandatoKey,
				numeroSubMandatoKey,
				descrizioneMandatoKey);
	}

}
