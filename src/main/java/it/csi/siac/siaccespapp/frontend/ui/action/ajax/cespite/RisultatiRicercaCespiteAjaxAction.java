/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.cespite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite.ElementoCespite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * The Class RisultatiRicercaCespiteAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCespiteAjaxAction extends BaseRisultatiRicercaCespiteAjaxAction {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 9006809515666812579L;

	private static final Pattern PATTERN = Pattern.compile("%(UID)%");
	
	private static final String AZIONI_CONSENTITE_BEGIN =  new StringBuilder()
			.append("<div class='btn-group'> ")
			.append("<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>")
			.append("<ul class='dropdown-menu pull-right' >").toString();

	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a href='risultatiRicercaCespite_aggiorna.do?uidCespite=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = "<li><a class='eliminaCespite' data-toggle='modal'>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a href='risultatiRicercaCespite_consulta.do?uidCespite=%UID%'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_RIVALUTAZIONI = "<li><a href='risultatiRicercaCespite_rivalutazioni.do?uidCespite=%UID%'>rivalutazioni</a></li>";
	
	private static final String AZIONI_CONSENTITE_SVALUTAZIONI = "<li><a href='risultatiRicercaCespite_svalutazioni.do?uidCespite=%UID%'>svalutazioni</a></li>";
	
	private static final String AZIONI_CONSENTITE_DISMISSIONI= "<li><a href='#' class='dismissioni'>dismissioni</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	
	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaCespiteAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CESPITE);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoCespite instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		boolean aggiornaConsentito = AzioniConsentiteFactory.isConsentito(AzioniConsentite.TIPO_BENE_CESPITE_AGGIORNA, listaAzioniConsentite);
		boolean variazioneConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.VARIAZIONE_CESPITI_AGGIORNA, listaAzioniConsentite);
		boolean dismissioneConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.DISMISSIONE_CESPITE_AGGIORNA, listaAzioniConsentite);
		StringBuilder azioniBuilder = new StringBuilder()
		    .append(AZIONI_CONSENTITE_BEGIN)
		    .append(aggiornaConsentito ? AZIONI_CONSENTITE_AGGIORNA : "")
		    .append(!instance.isCollegatoAPrimeNote()? AZIONI_CONSENTITE_ELIMINA : "")
		    .append(AZIONI_CONSENTITE_CONSULTA)
		    .append(variazioneConsentita? AZIONI_CONSENTITE_RIVALUTAZIONI : "")
		    .append(variazioneConsentita? AZIONI_CONSENTITE_SVALUTAZIONI : "")
		    .append(dismissioneConsentita? AZIONI_CONSENTITE_DISMISSIONI : "")
		    .append(AZIONI_CONSENTITE_END);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("%UID%", instance.getUid() + "");
		
		String azioni = StringUtilities.replacePlaceholders(PATTERN, azioniBuilder.toString(), map, false);
		
		instance.setAzioni(azioni);
	}

}
