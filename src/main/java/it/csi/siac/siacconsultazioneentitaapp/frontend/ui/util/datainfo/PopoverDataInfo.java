/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con popover.
 * 
 * @author Domenico
 *
 */
public class PopoverDataInfo extends BaseDataInfo {
	
	
	/**
	 * Classe di adapter per i campi delle colonne con informazioni nel popover
	 * @param name nome della colonna
	 * @param dataContent contenuto del popover (con eventuale HTML)
	 * @param dataPlacement "top", "left", "right" o "bottom"
	 * @param dataOriginalTitle titolo del popover
	 * @param columnValue valore che viene visualizzato all'interno della colonna (al di fuori del popover)
	 * @param campiKeys campi
	 */
	public PopoverDataInfo(String name, String dataContent, String dataPlacement, String dataOriginalTitle, String columnValue, String... campiKeys) {
		
		super(name, "<a data-toggle=\"popover\" "
				+ "data-content=\"" + dataContent + "\" "
				+ "data-container=\"body\" "
				+ "data-placement=\"" + dataPlacement + "\" "
				+ "rel=\"popover\" "
				+ "data-trigger=\"hover\" " 
				+ " href=\"#\" data-html=\"true\" "
				+ "data-original-title=\"" + dataOriginalTitle + "\">"
				+ "<i class=\"icon-info-sign\"></i> &nbsp;	"
				+ columnValue + " </a>", 
				campiKeys);
	}
	
	
}