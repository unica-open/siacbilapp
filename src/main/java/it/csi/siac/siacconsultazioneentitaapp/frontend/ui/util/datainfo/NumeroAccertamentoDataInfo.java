/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 22/04/2016
 *
 */
public class NumeroAccertamentoDataInfo extends PopoverDataInfo {

	/**
	 * Costruttore
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento del popover
	 * @param descrizioneMandatoKey la descrizione del mandato
	 * @param annoMandatoKey l'anno del mandato
	 * @param numeroMandatoKey il numero del mandato
	 */
	public NumeroAccertamentoDataInfo(String name, String dataPlacement,String descrizioneMandatoKey, String annoMandatoKey, String numeroMandatoKey) {
		super(name, "{0}", dataPlacement, "Descrizione", "{1,number,#}/{2,number,#}", descrizioneMandatoKey, annoMandatoKey, numeroMandatoKey);
		
	}

}
