/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import java.util.Date;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataGSAModel;
/**
 * Classe base di action per i risultati di ricerca per la validazione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaValidazionePrimaNotaIntegrataGSAAction extends RisultatiRicercaValidazionePrimaNotaIntegrataBaseAction<RisultatiRicercaValidazionePrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5068677020629814602L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Espongo il breadcrumb
		String result = super.execute();
		// SIAC-5853: inizializzazione dalla data definitiva
		model.setDataRegistrazioneLibroGiornale(new Date());
		return result;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRiepilogo() {
		return BilSessionParameter.RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA;
	}
	
}