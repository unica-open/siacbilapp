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

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.ConsultaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.ConsultaPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per la consultazione della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPrimaNotaIntegrataGSAAction extends ConsultaPrimaNotaIntegrataBaseAction<ConsultaPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -1842382164060226092L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		String result = super.execute();
		if(model.isValidazioneAbilitata() && model.getPrimaNota() != null && model.getPrimaNota().getDataRegistrazioneLibroGiornale() == null) {
			model.getPrimaNota().setDataRegistrazioneLibroGiornale(new Date());
		}
		return result;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaValidaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_VALIDA_GSA;
	}

	@Override
	protected BilSessionParameter getParametroSessioneRequest() {
		return BilSessionParameter.REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA;
	}

	@Override
	protected BilSessionParameter getParametroSessioneLista() {
		return BilSessionParameter.RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA;
	}
	
}
