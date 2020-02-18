/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con la denominazione del Capitolo con popover.
 * @author Elisa Chiari
 * @version 1.0.0 - 01/04/2016
 *
 */
public class DenominazioneCapitoloDataInfo extends PopoverDataInfo {

	/**
	 * Crea la stringa per il campo di denominazione della tabella
	 * @param name il nome del campo da visualizzare
	 * @param dataPlacement il posizionamento del popover
	 * @param descrizioneCapitoloKey la descrizione del capitolo
	 * @param descrizioneArticoloKey la descrizione dell'articolo
	 * @param codiceTipoCapitoloKey il codice del tipo del capitolo
	 * @param annoCapitoloKey l'anno del capitolo
	 * @param numeroCapitoloKey il numero del capitolo
	 * @param numeroArticoloKey il numero dell'articolo
	 */
	public DenominazioneCapitoloDataInfo(String name, String dataPlacement,
			String descrizioneCapitoloKey, String descrizioneArticoloKey, String codiceTipoCapitoloKey, String annoCapitoloKey, String numeroCapitoloKey, String numeroArticoloKey) {
		super(name, 
				"<div class='popover-content'> <strong> Descrizione capitolo</strong> <br/> {0} </div> " +
				"<div class='popover-content'> <strong> Descrizione articolo </strong> <br/> {1} </div> ", 
				dataPlacement, 
				"", 
				"{2}-{3}/{4}/{5}", 
				descrizioneCapitoloKey,descrizioneArticoloKey, codiceTipoCapitoloKey, annoCapitoloKey, numeroCapitoloKey, numeroArticoloKey);
	}

}
