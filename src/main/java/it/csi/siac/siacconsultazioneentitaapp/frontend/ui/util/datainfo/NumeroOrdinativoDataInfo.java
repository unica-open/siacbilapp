/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con il numero del Provvedimento in un popover.
 * 
 * @author Domenico
 */
public class NumeroOrdinativoDataInfo extends PopoverDataInfo {
	
	/**
	 * Costruttore.
	 * 
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento del popover
	 * @param annoOrdKey l'anno dell'ordinativo
	 * @param numOrdKey il numero dell'ordinativo
	 * @param statoKey lo stato
	 */
	public NumeroOrdinativoDataInfo(String name, String dataPlacement, String annoOrdKey, String numOrdKey, String statoKey) {
		super(name,"<div class='popover-content'> "
				+ "<strong>Stato: </strong> {2}<br/> ",
				dataPlacement,
				"Ordinativo",
				"{0,number,#}/{1,number,#}",
				annoOrdKey, numOrdKey, statoKey);
	}

}