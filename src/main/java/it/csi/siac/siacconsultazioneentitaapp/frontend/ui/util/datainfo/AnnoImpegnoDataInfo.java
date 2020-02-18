/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con la classificazione dell'Impegno con popover.
 * 
 * @author Domenico
 *
 */
public class AnnoImpegnoDataInfo extends PopoverDataInfo {
	
	
	/**
	 * Costruttore del campo
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento
	 * @param annoImpegnoKey l'anno dell'impegno
	 * @param numeroImpegnoKey il numero dell'impegno
	 * @param descrizioneImpegnoKey la descrizione dell'impegno
	 */
	public AnnoImpegnoDataInfo(String name, String dataPlacement, String annoImpegnoKey, String numeroImpegnoKey,String descrizioneImpegnoKey) {
		super(name,
				"<div class='popover-content'> "
				+ "<strong>Descrizione: </strong> {2}<br/> "
				+ "</div>",
				dataPlacement,
				"",
				"{0,number,#}/{1,number,#}",
				annoImpegnoKey, numeroImpegnoKey, descrizioneImpegnoKey);
	}
	
	
}