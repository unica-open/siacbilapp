/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.variazionecespite;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.variazionecespite.ElementoVariazioneCespite;
import it.csi.siac.siaccespser.model.StatoVariazioneCespite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * The Class RisultatiRicercaVariazioneCespiteAjaxAction.
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaVariazioneCespiteAjaxAction extends BaseRisultatiRicercaVariazioneCespiteAjaxAction {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 9006809515666812579L;

	private static final String AZIONI_CONSENTITE_BEGIN =  new StringBuilder()
			.append("<div class=\"btn-group\">")
			.append("<button class=\"btn dropdown-toggle\" data-placement=\"left\" data-toggle=\"dropdown\" href=\"#\">Azioni <span class=\"caret\"></span></button>")
			.append("<ul class=\"dropdown-menu pull-right\">").toString();

	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a data-aggiorna-variazione-cespite href=\"#\">aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_ELIMINA = "<li><a data-elimina-variazione-cespite href=\"#\">elimina</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a data-consulta-variazione-cespite href=\"#\">consulta</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaVariazioneCespiteAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_VARIAZIONE_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_VARIAZIONE_CESPITE);
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoVariazioneCespite instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		StringBuilder azioniBuilder = new StringBuilder();
		
		boolean aggiornamentoPossibile = isConsentitoAggiornamento(instance, listaAzioniConsentite) && !StatoVariazioneCespite.DEFINITIVO.equals(instance.getStatoVariazioneCespite());
		boolean eliminazionePossibile =  isConsentitoEliminazione(instance, listaAzioniConsentite) && !StatoVariazioneCespite.DEFINITIVO.equals(instance.getStatoVariazioneCespite());
		
		addIfConsentito(azioniBuilder, true, AZIONI_CONSENTITE_BEGIN);
		addIfConsentito(azioniBuilder, aggiornamentoPossibile, AZIONI_CONSENTITE_AGGIORNA);
		addIfConsentito(azioniBuilder, eliminazionePossibile, AZIONI_CONSENTITE_ELIMINA);
		addIfConsentito(azioniBuilder, isConsentitoConsultazione(instance, listaAzioniConsentite), AZIONI_CONSENTITE_CONSULTA);
		addIfConsentito(azioniBuilder, true, AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
	/**
	 * Controlla se sia consentito l'aggiornamento
	 * @param instance l'istanza della variazione
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return se l'aggiornamento sia consentito
	 */
	private boolean isConsentitoAggiornamento(ElementoVariazioneCespite instance, List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.VARIAZIONE_CESPITI_AGGIORNA, listaAzioniConsentite);
	}
	
	/**
	 * Controlla se sia consentita l'eliminazione
	 * @param instance l'istanza della variazione
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return se l'eliminazione sia consentita
	 */
	private boolean isConsentitoEliminazione(ElementoVariazioneCespite instance, List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.VARIAZIONE_CESPITI_ELIMINA, listaAzioniConsentite);
	}
	
	/**
	 * Controlla se sia consentita la consultazione
	 * @param instance l'istanza della variazione
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return se l'eliminazione sia consentita
	 */
	private boolean isConsentitoConsultazione(ElementoVariazioneCespite instance, List<AzioneConsentita> listaAzioniConsentite) {
		return true;
	}

}
