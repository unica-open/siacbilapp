/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;

/**
 * Action per i risultati di ricerca delle registrazioniMovFin per il modulo GSA.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistrazioneMovFinGSAAjaxAction extends RisultatiRicercaRegistrazioneMovFinBaseAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4192387439940222346L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMovFinGSAAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_REGISTRAZIONI_MOV_FIN_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_REGISTRAZIONI_MOV_FIN_GSA);
	}

	@Override
	protected String getCodiceAmbito() {
		return "GSA";
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaGestionePrimaNotaIntegrata() {
		return AzioniConsentite.PRIMA_NOTA_INTEGRATA_GESTISCI_GSA;
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaGestioneRegistrazioneMovFin() {
		return AzioniConsentite.REGISTRAZIONE_MOV_FIN_GESTISCI_GSA;
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaRicercaMovFin() {
		return AzioniConsentite.REGISTRAZIONE_MOV_FIN_RICERCA_GSA;
	}
}
