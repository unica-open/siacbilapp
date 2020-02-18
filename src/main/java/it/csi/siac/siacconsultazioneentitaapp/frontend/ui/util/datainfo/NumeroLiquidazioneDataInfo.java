/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con il numero della liquidazione in un popover.
 * @author Elisa Chiari
 * @version 1.0.0 - 31/03/2016
 *
 */
public class NumeroLiquidazioneDataInfo extends PopoverDataInfo {

	/**
	 * Costruttore.
	 * 
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento del popover
	 * @param numeroLiquidazioneKey il numero della liquidazione
	 * @param descrizioneLiquidazionekey la descrizione della liquidazione
	 */
	public NumeroLiquidazioneDataInfo(String name, String dataPlacement,String numeroLiquidazioneKey, String descrizioneLiquidazionekey) {
		super(name, 
				"<p class='popover-content'> <strong>Descrizione</strong> <br/> {1} </div>", 
				dataPlacement, 
				"", 
				"{0,number,#}", 
				numeroLiquidazioneKey,
				descrizioneLiquidazionekey  
				);
	}

}
