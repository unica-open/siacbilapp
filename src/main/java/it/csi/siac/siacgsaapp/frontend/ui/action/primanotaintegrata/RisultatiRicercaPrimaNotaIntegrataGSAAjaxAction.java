/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseAjaxAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di action per i risultati di ricerca della prima nota integrata, gestione AJAX. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaIntegrataGSAAjaxAction extends RisultatiRicercaPrimaNotaIntegrataBaseAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2151157094226181748L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaIntegrataGSAAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_GSA);
	}

	@Override
	protected String getCodiceAmbito() {
		return Ambito.AMBITO_GSA.getSuffix();
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaValidaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_VALIDA_GSA;
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRicercaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_RICERCA_GSA;
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaGestisciPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_GESTISCI_GSA;
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRateiRisconti() {
		return AzioneConsentitaEnum.RATEI_RISCONTI_GSA;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaAggiornaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_AGGIORNA_GSA;
	}

	@Override
	protected boolean isStatoCoerenteConAggiornamento(ElementoPrimaNotaIntegrata instance) {
		//SIAC-6195: abilitare l'azione 'aggiorna' anche quando la prima nota è definitiva. intervento per il solo ambito GSA
		return !StatoOperativoPrimaNota.ANNULLATO.equals(instance.getStatoOperativoPrimaNota());
	}
	
	//SIAC-6208
	@Override
	protected boolean gestisciAggiornaRisconti(List<AzioneConsentita> listaAzioniConsentite,ElementoPrimaNotaIntegrata instance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean gestisciAggiornaRateo(List<AzioneConsentita> listaAzioniConsentite,ElementoPrimaNotaIntegrata instance) {
		return false;
	}

	@Override
	protected boolean gestisciInserisciRisconti(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return false;
	}

	@Override
	protected boolean gestisciInserisciRateo(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return false;
	}
	
	protected boolean isFaseBilancioCompatibileConAggiornamento() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE);
	}

	protected boolean isFaseBilancioCompatibileConValidazione() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return true;
	}

	protected boolean isFaseBilancioCompatibileConAnnullamento() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE);
	}
	
	protected boolean isFaseBilancioCompatibileConCollegamento() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return true;
	}

}
