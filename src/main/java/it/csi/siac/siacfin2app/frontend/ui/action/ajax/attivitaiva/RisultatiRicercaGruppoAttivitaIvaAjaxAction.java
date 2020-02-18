/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.attivitaiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva.RisultatiRicercaGruppoAttivitaIvaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

/**
 * Action per i risultati di ricerca del GruppoAttivitaIva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaGruppoAttivitaIvaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoGruppoAttivitaIva, 
	RisultatiRicercaGruppoAttivitaIvaAjaxModel, GruppoAttivitaIva, RicercaSinteticaGruppoAttivitaIva, RicercaSinteticaGruppoAttivitaIvaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right dropdown-menu-datatable' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='#' class='aggiornaGruppoAttivitaIva'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='#' class='consultaGruppoAttivitaIva'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = 
			"<li><a href='#' class='eliminaGruppoAttivitaIva'>elimina</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient GruppoAttivitaIvaService gruppoAttivitaIvaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaGruppoAttivitaIvaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_GRUPPO_ATTIVITA_IVA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_GRUPPO_ATTIVITA_IVA);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaGruppoAttivitaIva request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaGruppoAttivitaIva request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoGruppoAttivitaIva ottieniIstanza(GruppoAttivitaIva e) throws FrontEndBusinessException {
		return new ElementoGruppoAttivitaIva(e, model.getAnnoEsercizioInt());
	}
	
	@Override
	protected RicercaSinteticaGruppoAttivitaIvaResponse ottieniResponse(RicercaSinteticaGruppoAttivitaIva request) {	
		return gruppoAttivitaIvaService.ricercaSinteticaGruppoAttivitaIva(request);
	}
	
	@Override
	protected ListaPaginata<GruppoAttivitaIva> ottieniListaRisultati(RicercaSinteticaGruppoAttivitaIvaResponse response) {
		return response.getListaGruppoAttivitaIva();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoGruppoAttivitaIva instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.GRUPPO_ATTIVITA_IVA_AGGIORNA, listaAzioniConsentite);
		Boolean isEliminaConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.GRUPPO_ATTIVITA_IVA_ELIMINA, listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.GRUPPO_ATTIVITA_IVA_CONSULTA, listaAzioniConsentite);
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isConsultaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if(Boolean.TRUE.equals(isEliminaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_ELIMINA);
		}
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		instance.setAzioni(azioniBuilder.toString());
	}
	
}
