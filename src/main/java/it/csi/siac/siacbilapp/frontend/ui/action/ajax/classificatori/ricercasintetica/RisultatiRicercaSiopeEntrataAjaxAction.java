/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori.ricercasintetica;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.classificatori.RisultatiRicercaSiopeEntrataAjaxModel;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siaccorser.model.Codifica;

/**
 * Action per i risultati di ricerca del SIOPE di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/dic/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSiopeEntrataAjaxAction extends RisultatiRicercaClassificatoreGerachicoAjaxAction<SiopeEntrata, RisultatiRicercaSiopeEntrataAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSiopeEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SIOPE_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SIOPE_ENTRATA);
	}
	
	@Override
	protected SiopeEntrata ottieniIstanza(Codifica e) throws FrontEndBusinessException {
		if(e instanceof SiopeEntrata) {
			return (SiopeEntrata) e;
		}
		// TODO: vedere se inserire un'eccezione
		return new SiopeEntrata();
	}

}
