/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.causali;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.RisultatiRicercaCausaleEPBaseAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per i risultati di ricerca della causale EP, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 31/03/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCausaleEPGSAAjaxAction extends RisultatiRicercaCausaleEPBaseAjaxAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8142372042131977829L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleEPGSAAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAUSALE_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAUSALE_GSA);
	}
	
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaGestioneCausaleEP() {
		return AzioneConsentitaEnum.CAUSALEEP_GSA_GESTIONE;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRicercaCausaleEP() {
		return AzioneConsentitaEnum.CAUSALEEP_GSA_RICERCA;
	}


	@Override
	protected String getCodiceAmbitoForPattern() {
		return "GSA";
	}
	
	@Override
	protected boolean isFaseBilancioCoerenteConAzioniModificaCausale() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE);
	}
	
	
}
