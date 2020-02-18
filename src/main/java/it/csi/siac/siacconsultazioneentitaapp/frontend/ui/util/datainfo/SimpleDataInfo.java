/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Dato con un solo dato.
 * 
 * @author Domenico
 *
 */
public class SimpleDataInfo extends BaseDataInfo {
	
	/**
	 * Costruttore.
	 * @param name il nome del campo
	 * @param campoKey la chiave del campo
	 */
	public SimpleDataInfo(String name, String campoKey) {
		super(name, "{0}", campoKey);
	}
	
	
}