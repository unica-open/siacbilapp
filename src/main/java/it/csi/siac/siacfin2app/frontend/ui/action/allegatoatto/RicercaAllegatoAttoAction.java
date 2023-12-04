/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.Arrays;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RicercaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

/**
 * Classe di Action per la ricerca dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/set/2014
 * @version 1.0.1 - 15/set/2014 - Aggiunta della classe base
 * @version 1.0.2 - 26/feb/2018 aggiunta ulteriore classe base
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaAllegatoAttoAction extends RicercaAllegatoAttoBaseAction<RicercaAllegatoAttoModel> {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -229729395008485189L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void caricaListaStatoOperativoAllegatoAtto() {
		model.setListaStatoOperativoAllegatoAtto(Arrays.asList(StatoOperativoAllegatoAtto.values()));
	}

	@Override
	protected BilSessionParameter ottieniParametroSessioneRequest() {
		return BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO;
	}

	@Override
	protected BilSessionParameter ottieniParametroSessioneRisultatiRicerca() {
		// TODO Auto-generated method stub
		return BilSessionParameter.RISULTATI_RICERCA_ALLEGATO_ATTO;
	}
	
}

