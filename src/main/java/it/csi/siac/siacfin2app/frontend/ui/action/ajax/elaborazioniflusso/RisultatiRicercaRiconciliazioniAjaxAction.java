/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.elaborazioniflusso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.pagopa.frontend.webservice.PagoPAService;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDocResponse;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.elaborazioniflusso.RisultatiRicercaRiconciliazioniAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso.ElementoElaborazioneRiconciliazioneDoc;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Vincenzo Gambino
 * @version 1.0.0 - 20/07/2020
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRiconciliazioniAjaxAction extends PagedDataTableAjaxAction<ElementoElaborazioneRiconciliazioneDoc, 
RisultatiRicercaRiconciliazioniAjaxModel, RiconciliazioneDoc, RicercaRiconciliazioniDoc, RicercaRiconciliazioniDocResponse> {
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = 6070112707026461016L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN =
			"<div class='btn-group'> " +
				"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
				"<ul class='dropdown-menu pull-right' >";
	
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href='risultatiElaborazioniFlussoConsulta.do?uidDaConsultare=%source.uid%'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient PagoPAService pagoPAService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaRiconciliazioniAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_RICONCILIAZIONE_PAGO_PA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_RICONCILIAZIONE_PAGO_PA);
	}
	
	//SIAC-8005
	@Override
	protected boolean controllaDaRientro() {
		return sessionHandler.containsKey(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE);
	}
	
	@Override
	protected boolean isAggiornareRientroPosizioneStart() {
		return false;
	}

	@Override
	protected boolean isAggiornareRientroPosizioneStartConsultazione() {
		return true;
	}
	//
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaRiconciliazioniDoc request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaRiconciliazioniDoc request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoElaborazioneRiconciliazioneDoc getInstance(RiconciliazioneDoc e) throws FrontEndBusinessException {

		//SIAC-8046 CM 18/03/2021 Task 2.1 Inizio
		//List<AzioneConsentita> listaAzioneConsentita = sessionHandler.getAzioniConsentite();
		e.setAzioneAggiornaAttiva(AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.AGGIORNA_ANNO_NUMERO_ACCERTAMENTO_RICONCILIAZIONE, sessionHandler.getAzioniConsentite()));
		//SIAC-8046 CM 18/03/2021 Task 2.1 Fine
		return new ElementoElaborazioneRiconciliazioneDoc(e);
	}
	
	@Override
	protected RicercaRiconciliazioniDocResponse getResponse(RicercaRiconciliazioniDoc request) {
		return pagoPAService.ricercaRiconciliazioniDoc(request);
		
	}
	
	@Override
	protected ListaPaginata<RiconciliazioneDoc> ottieniListaRisultati(RicercaRiconciliazioniDocResponse response) {
		return response.getRiconciliazioniDoc();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoElaborazioneRiconciliazioneDoc instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		boolean gestioneConsulta = gestisciConsultazione();
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
		appendIfTrue(strAzioni, gestioneConsulta, AZIONI_CONSENTITE_CONSULTA);
		strAzioni.append(AZIONI_CONSENTITE_END);

		String azioni = strAzioni.toString().replaceAll("%source.uid%", Integer.toString(instance.getUid()));
		instance.setAzioni(azioni);
	}
	
	

	/**
	 * Gestione della consultazione: controlla se sia possibile consultare l'allegato.
	 * 
	 * @return <code>true</code> se l'allegato &eacute; consultabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciConsultazione() {
		// Posso sempre consultare
		return true;
	}
	
}