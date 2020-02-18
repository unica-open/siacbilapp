/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.TipoDocumentoAjaxModel;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di Action per le azioni AJAX del TipoDocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/set/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class TipoDocumentoAjaxAction extends GenericBilancioAction<TipoDocumentoAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1194912414752670740L;

	/**
	 * Filtra i tipi di documento presenti in sessione sulla base del tipo di famiglia documento fornito.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String filter() {
		List<TipoDocumento> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoDocumento> resultingList = listaInSessione;
		if(listaInSessione != null && model.getTipoFamigliaDocumento() != null) {
			TipoFamigliaDocumento tfd = model.getTipoFamigliaDocumento();
			resultingList = new ArrayList<TipoDocumento>();
			
			for(TipoDocumento td : listaInSessione) {
				if(tfd.equals(td.getTipoFamigliaDocumento())) {
					resultingList.add(td);
				}
			}
		}
		
		model.setListaTipoDocumento(resultingList);
		return SUCCESS;
	}
	
}
