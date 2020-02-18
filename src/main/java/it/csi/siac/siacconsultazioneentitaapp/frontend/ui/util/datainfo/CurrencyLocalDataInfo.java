/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con un solo dato di tipo Importo.
 * 
 * @author Domenico
 * @author Alessandro Marchino
 * @version 1.1.0 - gestione della tipizzazione del risultato
 *
 */
public class CurrencyLocalDataInfo extends BaseDataInfo {
	
	/**
	 * Costruttore per l'importo
	 * @param name il nome del campo
	 * @param campoKey la chiave del campo
	 */
	public CurrencyLocalDataInfo(String name, String campoKey) {
		super(name, "{0,number,currency}", campoKey);
	}
	
}