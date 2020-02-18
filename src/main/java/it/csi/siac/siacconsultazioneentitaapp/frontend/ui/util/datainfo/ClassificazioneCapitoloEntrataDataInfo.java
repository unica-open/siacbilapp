/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con la classificazione del Capitolo con popover.
 * 
 * @author Domenico
 *
 */
public class ClassificazioneCapitoloEntrataDataInfo extends PopoverDataInfo {


	/**
	 * Crea la stringa per popolare il campo colonna della classificazione del capitolo
	 * @param name il nome della colonna
	 * @param dataPlacement il placement del popover ("top", "bottom", "right", "left")
	 * @param codiceTitoloEntrataKey il codice del titolo di entrata
	 * @param descrizioneTitoloEntrataKey la descrizione del titolo di entrata
	 * @param codiceTipologiaKey il codice del tipologia
	 * @param descrizioneTipologiaKey la descrizione della tipologia
	 * @param codiceCategoriaKey il codice del programma
	 * @param descrizioneCategoriaKey la descrizione del programma
	 */
	public ClassificazioneCapitoloEntrataDataInfo(String name,String dataPlacement,
			String codiceTitoloEntrataKey,String descrizioneTitoloEntrataKey, String codiceTipologiaKey, String descrizioneTipologiaKey, String codiceCategoriaKey, 
			String descrizioneCategoriaKey) {
		super(name, 
				"<div class='popover-content'> <strong> Titolo </strong> <br/> {0} - {1} </div> " +
				"<div class='popover-content'> <strong> Tipologia </strong> <br/> {4} - {5} </div> "+
				"<div class='popover-content'> <strong> Categoria </strong> <br/> {2} - {3} </div> " ,
				dataPlacement,
				// come nell'inserimento variazione
				"Classificatori", 
				"{2}-{4}",
				codiceTitoloEntrataKey, descrizioneTitoloEntrataKey, codiceTipologiaKey, descrizioneTipologiaKey, codiceCategoriaKey, descrizioneCategoriaKey);
	}
	
}