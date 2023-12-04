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
public class NumeroProvvedimentoDataInfo extends PopoverDataInfo {
	
	/**
	 * Costruttore.
	 * 
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento del popover
	 * @param annoProvvKey l'anno del provvedimento
	 * @param numProvvKey il numero del provvedimento
	 * @param codiceTipoProvvKey il codice del tipo provvedimento
	 * @param descrizioneTipoProvvKey la descrizione del tipo provvedimento
	 * @param codiceDirezioneKey il codice della direzione
	 * @param descrizioneDirezioneKey la descrizione della direzione
	 * @param statoKey lo stato
	 */
	public NumeroProvvedimentoDataInfo(String name, String dataPlacement, String annoProvvKey, String numProvvKey, String codiceTipoProvvKey, String descrizioneTipoProvvKey, String codiceDirezioneKey, String descrizioneDirezioneKey, String statoKey,
			//SIAC-8188
			String descrizioneProvvedimento, String allegatoAttoDescrizione) {
		super(name,"<strong>Tipo: </strong> {2} - {3}<br/> "
				+ "<strong>Direzione: </strong> {4} - {5}<br/> "
				+ "<strong>Stato: </strong> {6}<br/> "
				+ "<strong>Desc prov: </strong> {7}<br/> "
				+ "<strong>Desc allegato: </strong> {8}<br/> ",
				dataPlacement,
				"Provvedimento",
				"{0}/{1,number,#}/{2}",
				annoProvvKey, numProvvKey, codiceTipoProvvKey, descrizioneTipoProvvKey, codiceDirezioneKey, descrizioneDirezioneKey,statoKey, 
				//SIAC-8188
				descrizioneProvvedimento, allegatoAttoDescrizione);
	}

}