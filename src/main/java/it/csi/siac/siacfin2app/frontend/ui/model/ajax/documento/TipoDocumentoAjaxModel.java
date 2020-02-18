/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di model per le azioni AJAX del tipo Documento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/set/2014
 *
 */
public class TipoDocumentoAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1389696933583516458L;
	
	private TipoFamigliaDocumento tipoFamigliaDocumento;
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	/**
	 * @return the tipoFamigliaDocumento
	 */
	public TipoFamigliaDocumento getTipoFamigliaDocumento() {
		return tipoFamigliaDocumento;
	}
	/**
	 * @param tipoFamigliaDocumento the tipoFamigliaDocumento to set
	 */
	public void setTipoFamigliaDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) {
		this.tipoFamigliaDocumento = tipoFamigliaDocumento;
	}
	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}
	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento != null ? listaTipoDocumento : new ArrayList<TipoDocumento>();
	}
	
}
