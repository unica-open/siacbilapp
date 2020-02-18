/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;


/**
 * Classe per il caricamento <em>AJAX</em> dell'Elemento del Piano dei Conti.
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElementoPianoDeiContiFullAjaxAction extends ElementoPianoDeiContiAjaxAction {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2178489303804907115L;

	@Override
	protected boolean gestisciEventualeIdVuoto(Integer id) {
		return false;
	}
	
	@Override
	protected LeggiTreePianoDeiConti definisciRequest(Integer id) {
		return model.creaRequestLeggiTreePianoDeiConti(id==null?118917:id);
	}

}
