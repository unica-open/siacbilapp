/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.vincolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaVincoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.ricerca.ElementoVincolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.ricerca.ElementoVincoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.VincoloCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 02/01/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaVincoloAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoVincolo, 
	RisultatiRicercaVincoloAjaxModel, VincoloCapitoli, RicercaVincolo, RicercaVincoloResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaVincoloAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='risultatiRicercaVincoloConsulta.do?uidDaConsultare=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient VincoloCapitoloService vincoloCapitoloService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaVincoloAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_VINCOLO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_VINCOLO);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaVincolo request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaVincolo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoVincolo ottieniIstanza(VincoloCapitoli e) throws FrontEndBusinessException {
		return ElementoVincoloFactory.getInstance(e);
	}

	@Override
	protected RicercaVincoloResponse ottieniResponse(RicercaVincolo request) {
		return vincoloCapitoloService.ricercaVincolo(request);
	}

	@Override
	protected ListaPaginata<VincoloCapitoli> ottieniListaRisultati(RicercaVincoloResponse response) {
		return response.getVincoloCapitoli();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoVincolo instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isAggiornaConsentitoVincolo(listaAzioniConsentite);
		Boolean isAnnullaConsentita = AzioniConsentiteFactory.isAnnullaConsentitoVincolo(listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsultaConsentitoVincolo(listaAzioniConsentite);
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita) && instance.isValido()) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isAnnullaConsentita) && instance.isValido()) {
			azioniBuilder.append(AZIONI_CONSENTITE_ANNULLA);
		}
		if(Boolean.TRUE.equals(isConsultaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
}
