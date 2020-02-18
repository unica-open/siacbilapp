/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * @author Pro Logic
 *
 */
public class DocumentoDataInfo extends PopoverDataInfo {

	/**
	 * @param name il nome della colonna
	 * @param dataPlacement il posizionamento del popover
	 * @param descrizioneDocumentoKey la chiave della descrizione del documento
	 * @param annoDocumentoKey l'anno del documento
	 * @param codiceTipoDocumentoKey il codice del tipo del documento
	 * @param numeroDocumentoKey il numero del documento
	 */
	public DocumentoDataInfo(String name, String dataPlacement,
			String descrizioneDocumentoKey, String annoDocumentoKey, String codiceTipoDocumentoKey,String numeroDocumentoKey) {
		super(name, "{0}", dataPlacement, "Descrizione","{1,number,#}/{2}/{3}",
				descrizioneDocumentoKey, annoDocumentoKey, codiceTipoDocumentoKey, numeroDocumentoKey);
	}

}
