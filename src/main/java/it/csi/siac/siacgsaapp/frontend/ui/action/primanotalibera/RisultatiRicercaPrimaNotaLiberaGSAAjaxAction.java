/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotalibera;

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
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaLiberaGSAAjaxAction extends RisultatiRicercaPrimaNotaLiberaBaseAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -848728021207131807L;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaLiberaGSAAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTALIBERA_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTALIBERA_GSA);
	}
	
	@Override
	protected String getCodiceAmbito() {
		return "GSA";
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaGestionePrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_GSA_GESTIONE;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaValidazionePrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_GSA_VALIDA;
		}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRicercaPrimaNotaLibera() {
		return AzioneConsentitaEnum.PRIMANOTALIBERA_GSA_RICERCA;	 	
	}
	
	@Override
	protected boolean isStatoOperativoCoerenteConAggiornamento(ElementoPrimaNotaLibera instance) {
		return !StatoOperativoPrimaNota.ANNULLATO.equals(instance.getStatoOperativoPrimaNota());
	}
	
	//SIAC-8323
	protected boolean isFaseBilancioCoerenteConAggiornamento() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return true;
	}
	//SIAC-8323
	protected boolean isFaseBilancioCoerenteConValidazione() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return true;
	}
	//SIAC-8323
	protected boolean isFaseBilancioCoerenteConAnnullamento() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE);
	}


}
