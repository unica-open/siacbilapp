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
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioni;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioniResponse;
import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.elaborazioniflusso.RisultatiRicercaElaborazioniFlussoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso.ElementoElaborazioniFlusso;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Vincenzo Gambino
 * @version 1.0.0 - 20/07/2020
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaElaborazioniFlussoAjaxAction extends PagedDataTableAjaxAction<ElementoElaborazioniFlusso, 
RisultatiRicercaElaborazioniFlussoAjaxModel, Elaborazione, RicercaElaborazioni, RicercaElaborazioniResponse> {
	
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
	public RisultatiRicercaElaborazioniFlussoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ELABORAZIONI_FLUSSO_PAGO_PA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ELABORAZIONI_FLUSSO_PAGO_PA);
	}
	
	//SIAC-8005
	@Override
	protected boolean controllaDaRientro() {
		return sessionHandler.containsKey(BilSessionParameter.RIENTRO_POSIZIONE_START);
	}
	
	@Override
	protected int ottieniValoreInizio(boolean daRientro) {
		return daRientro ? (Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) : 1;
	}
	
	@Override
	protected boolean isAggiornareRientroPosizioneStartConsultazione() {
		return false;
	}
	//
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaElaborazioni request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaElaborazioni request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoElaborazioniFlusso getInstance(Elaborazione e) throws FrontEndBusinessException {
		return new ElementoElaborazioniFlusso(e);
	}
	
	@Override
	protected RicercaElaborazioniResponse getResponse(RicercaElaborazioni request) {
		return pagoPAService.ricercaElaborazioni(request);
		
	}
	
	@Override
	protected ListaPaginata<Elaborazione> ottieniListaRisultati(RicercaElaborazioniResponse response) {
		return response.getElaborazioni();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoElaborazioniFlusso instance, boolean daRientro, boolean isAggiornaAbilitato,
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