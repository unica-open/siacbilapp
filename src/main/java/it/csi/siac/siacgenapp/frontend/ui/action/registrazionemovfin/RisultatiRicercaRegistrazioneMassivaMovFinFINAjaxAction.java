/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseAjaxAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca.ElementoRegistrazioneMovFin;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Action per i risultati di ricerca massiva delle registrazioniMovFin per il modulo GEN.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/11/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistrazioneMassivaMovFinFINAjaxAction extends RisultatiRicercaRegistrazioneMovFinBaseAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3256483917866869438L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMassivaMovFinFINAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GEN);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GEN);
	}

	@Override
	protected String getCodiceAmbito() {
		return "FIN";
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoRegistrazioneMovFin instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		// Nothing to do
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaGestionePrimaNotaIntegrata() {
		return null;
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaGestioneRegistrazioneMovFin() {
		return null;
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaRicercaMovFin() {
		return null;
	}
	
	@Override
	protected boolean isAggiornaPdCConsentita(List<AzioneConsentita> listaAzioniConsentite) {
		return false;
	}
}
