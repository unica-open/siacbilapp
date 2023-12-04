/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.tipoonere;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.tipoonere.RisultatiRicercaTipoOnereAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.tipoonere.ElementoTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.TipoOnereService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di Action per i risultati di ricerca del tipo onere, per i dati AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoOnereAjaxAction extends PagedDataTableAjaxAction<ElementoTipoOnere, RisultatiRicercaTipoOnereAjaxModel,
	TipoOnere, RicercaSinteticaTipoOnere, RicercaSinteticaTipoOnereResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3518287425012203833L;
	
	/** Stringa iniziale delle azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";
	/** Stringa delle azioni consentite riguardante l'aggiornamento */
	private static final String AZIONI_CONSENTITE_AGGIORNA =
			"<li><a href='ricercaTipoOnere_aggiorna.do?tipoOnere.uid=%UID%'>aggiorna</a></li>";
	/** Stringa delle azioni consentite riguardante la consultazione */
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href='ricercaTipoOnere_consulta.do?tipoOnere.uid=%UID%'>consulta</a></li>";
	/** Stringa finale delle azioni consentite */
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient TipoOnereService tipoOnereService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoOnereAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TIPO_ONERE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TIPO_ONERE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaTipoOnere request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaTipoOnere request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoTipoOnere getInstance(TipoOnere e) throws FrontEndBusinessException {
		return new ElementoTipoOnere(e);
	}

	@Override
	protected RicercaSinteticaTipoOnereResponse getResponse(RicercaSinteticaTipoOnere request) {
		return tipoOnereService.ricercaSinteticaTipoOnere(request);
	}

	@Override
	protected ListaPaginata<TipoOnere> ottieniListaRisultati(RicercaSinteticaTipoOnereResponse response) {
		return response.getListaTipoOnere();
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
	protected void handleAzioniConsentite(ElementoTipoOnere instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato,
			boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.TIPO_ONERE_AGGIORNA, listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.TIPO_ONERE_CONSULTA, listaAzioniConsentite);
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isConsultaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}

}
