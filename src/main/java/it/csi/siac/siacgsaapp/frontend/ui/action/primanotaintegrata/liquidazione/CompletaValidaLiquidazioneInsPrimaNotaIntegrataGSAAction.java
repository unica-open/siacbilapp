/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata.liquidazione;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione.GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPGSASelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.liquidazione.CompletaValidaLiquidazioneInsPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio
 * 
 * @author Valentina
 * 
 * @version 1.0.0 - 15/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_LIQUIDAZIONE_GSA)
public class CompletaValidaLiquidazioneInsPrimaNotaIntegrataGSAAction extends GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction<CompletaValidaLiquidazioneInsPrimaNotaIntegrataGSAModel>  {

	/** per serializzazione **/
	private static final long serialVersionUID = -1710396232536647700L;
	

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return super.execute();
	}

	@Override
	protected BilSessionParameter getSessionParameterListaCausaleEPIntegrata() {
		return BilSessionParameter.LISTA_CAUSALE_EP_INTEGRATA_GSA;
	}
	
	@Override
	protected CausaleEPSelector istanziaSelettoreCausale() {
		return new CausaleEPGSASelector();
	}
	

}

