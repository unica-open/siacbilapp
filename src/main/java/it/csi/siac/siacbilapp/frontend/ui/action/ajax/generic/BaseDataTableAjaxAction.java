/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.BaseDataTableAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

public abstract class BaseDataTableAjaxAction<S extends Serializable, M extends BaseDataTableAjaxModel<S>, 
	E extends Entita, REQ extends ServiceRequest, RES extends ServiceResponse> extends GenericBilancioAction<M> {

	private static final long serialVersionUID = -2879505078278694455L;

	/**
	 * Gestisci l'elenco delle azioni consentite, se applicabile.
	 * 
	 * @param instance            l'istanza rispetto cui gestire le azioni consentite
	 * @param daRientro           se si stia arrivando dal rientro
	 * @param isAggiornaAbilitato se l'aggiornamento sia abilitato
	 * @param isAnnullaAbilitato  se l'annullamento sia abilitato
	 * @param isConsultaAbilitato se la consultazione sia abilitata
	 * @param isEliminaAbilitato  se l'eliminazione sia abilitata
	 */
	protected void handleAzioniConsentite(S instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
	}
	

	
	/**
	 * Ottiene il wrapper per le azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return il wrapper
	 */
	protected AzioniConsentiteWrapper getAzioniConsentiteWrapperInstance(List<AzioneConsentita> listaAzioniConsentite) {
		return new AzioniConsentiteWrapper();
	}
	
	/**
	 * Esecuzione di ulteriori operazioni a termine della chiamata.
	 */
	protected void executeEnd() {
		// Implementazione di dafault vuota
	}
	
	protected abstract S getInstance(E e) throws FrontEndBusinessException;
	protected abstract RES getResponse(REQ req);

	protected void addIfConsentito(Appendable app, boolean consentito, String azione) {
		if(consentito) {
			try {
				app.append(azione);
			} catch (IOException e) {
				// Should NEVER happen
				throw new IllegalArgumentException("Impossibile aggiungere l'azione", e);
			}
		}
	}
}
