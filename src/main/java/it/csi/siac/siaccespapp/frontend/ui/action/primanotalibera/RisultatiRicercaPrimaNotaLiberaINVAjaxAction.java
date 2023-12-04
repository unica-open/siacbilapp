/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.primanotalibera;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseAjaxAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di action per i risultati di ricerca della primanota libera, gestione AJAX.
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaLiberaINVAjaxAction extends RisultatiRicercaPrimaNotaLiberaBaseAjaxAction {


	/**
	 * Per La serializzazione
	 */
	private static final long serialVersionUID = 336333233340313078L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaLiberaINVAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTALIBERA_INV);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTALIBERA_INV);
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaGestionePrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_INV_GESTIONE;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaValidazionePrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_INV_VALIDA;
		}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRicercaPrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_INV_RICERCA;	 	
	}
	
	@Override
	protected String getCodiceAmbito() {
		return "INV";
	}

	@Override
	protected boolean isStatoOperativoCoerenteConAggiornamento(ElementoPrimaNotaLibera instance) {
		return false;
	}
	
	/* TODO da gestire al momento simula l'annulla*/
	
	@Override
	protected boolean gestisciRifiuto(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaLibera instance) {
		return !faseBilancioInValues(super.faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestionePrimaNotaLibera(), listaAzioniConsentite)
				&& !StatoOperativoPrimaNota.ANNULLATO.equals(instance.getStatoOperativoPrimaNota())
				// SIAC-6578
				&& !StatoOperativoPrimaNota.DEFINITIVO.equals(instance.getStatoOperativoPrimaNota());
	}

	@Override
	protected boolean gestisciAnnullamento(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaLibera instance) {
		return false;
	}
	
	//SIAC-8323
	protected boolean isFaseBilancioCoerenteConAggiornamento() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.CHIUSO);
	}
	//SIAC-8323
	protected boolean isFaseBilancioCoerenteConValidazione() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.CHIUSO);
	}
	//SIAC-8323
	protected boolean isFaseBilancioCoerenteConAnnullamento() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO);
	}
	
}