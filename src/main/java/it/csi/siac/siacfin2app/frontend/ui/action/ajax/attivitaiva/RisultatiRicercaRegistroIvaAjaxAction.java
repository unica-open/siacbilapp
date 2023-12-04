/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.attivitaiva;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva.RisultatiRicercaRegistroIvaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Action per i risultati di ricerca del RegistroIva.
 *
 * @author Alessandro Marchino
 * @version 1.0.0 - 03/06/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistroIvaAjaxAction extends PagedDataTableAjaxAction<ElementoRegistroIva,
	RisultatiRicercaRegistroIvaAjaxModel, RegistroIva, RicercaSinteticaRegistroIva, RicercaSinteticaRegistroIvaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;
	
	private static final Pattern PATTERN = Pattern.compile("%(UID|GRUPPOATTIVITAIVA|TIPOREGISTROIVA|CODICE|DESCRIZIONE|FLAGLIQUIDAZIONEIVA)%");

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\"> " +
			"<button class=\"btn dropdown-toggle\" data-placement=\"left\" data-toggle=\"dropdown\" href=\"#\">Azioni <span class=\"caret\"></span></button>" +
			"<ul class=\"dropdown-menu pull-right dropdown-menu-datatable\">";

	private static final String AZIONI_CONSENTITE_AGGIORNA =
			"<li><a href=\"risultatiRicercaRegistroIva_aggiorna.do?uidDaAggiornare=%UID%\" class=\"aggiornaRegistroIva\">aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href=\"#\" class=\"consultaRegistroIva\" data-gruppo-attivita-iva=\"%GRUPPOATTIVITAIVA%\" data-tipo-registro-iva=\"%TIPOREGISTROIVA%\" data-codice=\"%CODICE%\" data-descrizione=\"%DESCRIZIONE%\" data-liquidazione-iva=\"%FLAGLIQUIDAZIONEIVA%\">consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA =
			"<li><a href=\"#\" class=\"eliminaRegistroIva\" data-uid=\"%UID%\" data-codice=\"%CODICE%\" data-function=\"elimina\" data-msg=\"Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?\" data-ask>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_BLOCCA_REGISTRO =
			"<li><a href=\"#\" class=\"bloccaRegistroIva\" data-uid=\"%UID%\" data-codice=\"%CODICE%\" data-function=\"blocca\" data-msg=\"Stai per bloccare l'elemento selezionato: sei sicuro di voler proseguire?\" data-ask>blocca registro</a></li>";
	
	private static final String AZIONI_CONSENTITE_SBLOCCA_REGISTRO =
			"<li><a href=\"#\" class=\"sbloccaRegistroIva\" data-uid=\"%UID%\" data-codice=\"%CODICE%\" data-function=\"sblocca\" data-msg=\"Stai per sbloccare l'elemento selezionato: sei sicuro di voler proseguire?\" data-ask>sblocca registro</a></li>";
	
	private static final String AZIONI_CONSENTITE_RECUPERA_PROTOCOLLO =
			"<li><a href=\"risultatiRicercaRegistroIva_recuperaProtocollo.do?uidRegistroSelezionato=%UID%\" class=\"recuperaProtocolloRegistroIva\">recupera protocollo</a></li>";
	
	private static final String AZIONI_CONSENTITE_ALLINEA_PROTOCOLLO =
			"<li><a href=\"#\" class=\"allineaProtocolloRegistroIva\" data-uid=\"%UID%\" data-codice=\"%CODICE%\" data-function=\"allinea\" data-msg=\"Stai per allineare l'elemento selezionato: sei sicuro di voler proseguire?\" data-ask>allinea protocollo</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient RegistroIvaService registroIvaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistroIvaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_REGISTRO_IVA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_REGISTRO_IVA);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaRegistroIva request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaRegistroIva request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoRegistroIva getInstance(RegistroIva e) throws FrontEndBusinessException {
		return new ElementoRegistroIva(e);
	}
	
	@Override
	protected RicercaSinteticaRegistroIvaResponse getResponse(RicercaSinteticaRegistroIva request) {
		return registroIvaService.ricercaSinteticaRegistroIva(request);
	}
	
	@Override
	protected ListaPaginata<RegistroIva> ottieniListaRisultati(RicercaSinteticaRegistroIvaResponse response) {
		return response.getListaRegistroIva();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoRegistroIva instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		
		boolean isBackOffice = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.REGISTRO_IVA_AGGIORNA_BACKOFFICE, listaAzioniConsentite);
		boolean isRegistroBloccato = Boolean.TRUE.equals(instance.getFlagBloccato());
		
		boolean isAggiornaConsentita = isBackOffice || (AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.REGISTRO_IVA_AGGIORNA, listaAzioniConsentite) && !isRegistroBloccato);
		boolean isEliminaConsentita =  isBackOffice || (AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.REGISTRO_IVA_ELIMINA, listaAzioniConsentite) && !isRegistroBloccato);
		boolean isConsultaConsentita = isBackOffice || (AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.REGISTRO_IVA_CONSULTA, listaAzioniConsentite) && !isRegistroBloccato);
		
		//CR-3791
		boolean isBloccaRegistroConsentita = isBackOffice && !isRegistroBloccato;
		boolean isModificheBackOfficeARegistroBloccatoConsentite = isBackOffice && isRegistroBloccato;
			
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(isAggiornaConsentita) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(isConsultaConsentita) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if(isEliminaConsentita) {
			azioniBuilder.append(AZIONI_CONSENTITE_ELIMINA);
		}
		
		if(isBloccaRegistroConsentita) {
			azioniBuilder.append(AZIONI_CONSENTITE_BLOCCA_REGISTRO);
		}
		
		if(isModificheBackOfficeARegistroBloccatoConsentite) {
			azioniBuilder.append(AZIONI_CONSENTITE_SBLOCCA_REGISTRO);
			azioniBuilder.append(AZIONI_CONSENTITE_ALLINEA_PROTOCOLLO);
			azioniBuilder.append(AZIONI_CONSENTITE_RECUPERA_PROTOCOLLO);
		}

		azioniBuilder.append(AZIONI_CONSENTITE_END);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("%UID%", instance.getUid() + "");
		map.put("%GRUPPOATTIVITAIVA%", instance.getGruppoAttivitaIva());
		map.put("%TIPOREGISTROIVA%", instance.getTipoRegistroIva());
		map.put("%CODICE%", instance.getCodice());
		map.put("%DESCRIZIONE%", instance.getDescrizione());
		// SIAC-6276 CR-1179B
		map.put("%FLAGLIQUIDAZIONEIVA%", instance.getIsFlagLiquidazioneIva());
		
		String azioni = StringUtilities.replacePlaceholders(PATTERN, azioniBuilder.toString(), map, false);
		instance.setAzioni(azioni);
	}
	
}