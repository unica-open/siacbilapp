/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con il numero del capitolo.
 * @author Elisa Chiari
 *
 */
public class NumeroCapitoloDataInfo extends BaseDataInfo {

	/**
	 * Costruttore.
	 * 
	 * @param name il nome del campo
	 * @param pattern il pattern da usare
	 * @param campiKeys i campi da usare come chiave
	 */
	public NumeroCapitoloDataInfo(String name, String pattern, String... campiKeys) {
		super(name, pattern, campiKeys);
	}

}
