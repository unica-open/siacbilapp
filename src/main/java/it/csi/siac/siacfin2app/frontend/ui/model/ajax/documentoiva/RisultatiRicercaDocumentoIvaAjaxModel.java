/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documentoiva;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva.ElementoDocumentoIva;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 17/06/2014
 * 
 * @param <D>   il tipo del documento
 * @param <SD>  il tipo del subdocumento
 * @param <SDI> il tipo di subdocumento iva
 *
 */
public class RisultatiRicercaDocumentoIvaAjaxModel<D extends Documento<SD, SDI>, SD extends Subdocumento<D, SDI>, SDI extends SubdocumentoIva<D, SD, SDI>> 
		extends GenericRisultatiRicercaAjaxModel<ElementoDocumentoIva<D, SD, SDI>> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1949840562109980124L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoIvaAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Documenti Iva - AJAX");
	}
		
}
