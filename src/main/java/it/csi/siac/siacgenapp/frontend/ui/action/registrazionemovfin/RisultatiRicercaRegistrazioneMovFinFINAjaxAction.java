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
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Action per i risultati di ricerca delle registrazioniMovFin per il modulo GEN.
 *
 * @author Valentina
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/05/2015
 * @version 1.1.0 - 05/10/2015 - aggancio GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistrazioneMovFinFINAjaxAction extends RisultatiRicercaRegistrazioneMovFinBaseAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 246998591875617454L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistrazioneMovFinFINAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_REGISTRAZIONI_MOV_FIN_GEN);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_REGISTRAZIONI_MOV_FIN_GEN);
	}

	@Override
	protected String getCodiceAmbito() {
		return "FIN";
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaGestionePrimaNotaIntegrata() {
		return AzioniConsentite.PRIMA_NOTA_INTEGRATA_GESTISCI_GEN;
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaGestioneRegistrazioneMovFin() {
		return AzioniConsentite.REGISTRAZIONE_MOV_FIN_GESTISCI_GEN;
	}

	@Override
	protected AzioniConsentite getAzioneConsentitaRicercaMovFin() {
		return AzioniConsentite.REGISTRAZIONE_MOV_FIN_RICERCA_GEN;
	}
	
	@Override
	protected boolean isAggiornaPdCConsentita(List<AzioneConsentita> listaAzioniConsentite) {
		return Boolean.TRUE.equals(AzioniConsentiteFactory.isConsentito(AzioniConsentite.PIANO_DEI_CONTI_AGGIORNA_SU_REGISTRO, listaAzioniConsentite));
	}
}
