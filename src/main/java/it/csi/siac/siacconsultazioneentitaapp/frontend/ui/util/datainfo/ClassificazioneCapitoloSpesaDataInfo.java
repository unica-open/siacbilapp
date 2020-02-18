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
public class ClassificazioneCapitoloSpesaDataInfo extends PopoverDataInfo {

	/**
	 * Crea la stringa per popolare il campo colonna della classificazione del capitolo
	 * @param name il nome della colonna
	 * @param dataPlacement il placement del popover ("top", "bottom", "right", "left")
	 * @param codiceMissioneKey il codice della missione
	 * @param descrizioneMissioneKey la descrizione della missione
	 * @param codiceProgrammaKey il codice del programma
	 * @param descrizioneProgrammaKey la descrizione del programma
	 * @param codiceTitoloSpesaKey il codice del titolo di spesa
	 * @param descrizioneTitoloSpesaKey la descrizione del titolo di spesa
	 * @param codiceMacroaggregatoKey il codice del macroaggregato
	 * @param descrizioneMacroaggregatoKey la descrizione del macroaggregato
	 */
	public ClassificazioneCapitoloSpesaDataInfo(String name,String dataPlacement,
			String codiceMissioneKey,String descrizioneMissioneKey, String codiceProgrammaKey, 
			String descrizioneProgrammaKey, String codiceTitoloSpesaKey, String descrizioneTitoloSpesaKey, String codiceMacroaggregatoKey, String descrizioneMacroaggregatoKey) {
		super(name, 
				"<div class='popover-content'> <strong> Missione </strong> <br/> {0} - {1} </div> " +
				"<div class='popover-content'> <strong> Programma </strong> <br/> {2} - {3} </div> " +
				"<div class='popover-content'> <strong> Titolo </strong> <br/> {4} - {5} </div> " + 
				"<div class='popover-content'> <strong> Macroaggregato </strong> <br/> {6} - {7} </div> ",
				dataPlacement,
				// come nell'inserimento variazione
				"Classificatori", 
				"{2}-{6}", 
				codiceMissioneKey,descrizioneMissioneKey, codiceProgrammaKey, descrizioneProgrammaKey, codiceTitoloSpesaKey, descrizioneTitoloSpesaKey, codiceMacroaggregatoKey, descrizioneMacroaggregatoKey);
	}
	
}