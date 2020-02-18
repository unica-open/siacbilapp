/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * @author Pro Logic
 */
public class PianoDeiContiDataInfo extends PopoverDataInfo {

	/**
	 * @param name il nome della colonna
	 * @param dataPlacement il posizionamento del popover
	 * @param codicePDC il codice del PDC
	 * @param descrizionePDC la descrizione del PDC
	 */
	public PianoDeiContiDataInfo(String name, String dataPlacement, String codicePDC, String descrizionePDC) {
		super(name, "{1}", dataPlacement, "Descrizione", "{0}", codicePDC, descrizionePDC);
	}

}
