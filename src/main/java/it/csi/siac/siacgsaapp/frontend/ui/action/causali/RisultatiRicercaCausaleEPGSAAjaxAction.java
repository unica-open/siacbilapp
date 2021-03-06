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
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;

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
	protected AzioniConsentite getAzioneConsentitaGestioneCausaleEP() {
		return AzioniConsentite.CAUSALEEP_GSA_GESTIONE;
	}
	
	@Override
	protected AzioniConsentite getAzioneConsentitaRicercaCausaleEP() {
		return AzioniConsentite.CAUSALEEP_GSA_RICERCA;
	}


	@Override
	protected String getCodiceAmbitoForPattern() {
		return "GSA";
	}
	
	
}
