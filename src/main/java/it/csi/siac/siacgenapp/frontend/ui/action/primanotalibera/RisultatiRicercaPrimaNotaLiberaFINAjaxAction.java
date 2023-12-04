/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseAjaxAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di action per i risultati di ricerca della primanota libera, gestione AJAX.
 * 
 * @author Paggio Simona 
 * @version 1.0.0 - 05/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 07/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaLiberaFINAjaxAction extends RisultatiRicercaPrimaNotaLiberaBaseAjaxAction {

	
	/**
	 * Per La serializzazione
	 */
	private static final long serialVersionUID = 336333233340313078L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaLiberaFINAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTALIBERA_GEN);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTALIBERA_GEN);
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaGestionePrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_GEN_GESTIONE;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaValidazionePrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_GEN_VALIDA;
		}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRicercaPrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_GEN_RICERCA;	 	
	}
	
	@Override
	protected String getCodiceAmbito() {
		return "FIN";
	}

	@Override
	protected boolean isStatoOperativoCoerenteConAggiornamento(ElementoPrimaNotaLibera instance) {
		return StatoOperativoPrimaNota.PROVVISORIO.equals(instance.getStatoOperativoPrimaNota());
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