/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/04/2017
 */
public class ImportoDocumentoDataInfo extends PopoverDataInfo {

	/**
	 * Costruttore
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento del popover
	 * @param importoSubdocKey l'importo del subdoc
	 * @param importoDocKey l'imprto del doc
	 */
	public ImportoDocumentoDataInfo(String name, String dataPlacement,String importoSubdocKey, String importoDocKey) {
		super(name,
				"{1,number,###,##0.00}",
				dataPlacement,
				"Importo documento",
				"{0,number,###,##0.00}",
				importoSubdocKey,
				importoDocKey);
	}

}
