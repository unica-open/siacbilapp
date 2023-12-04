/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.anagcomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaComponenteCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.anagcomp.ElementoComponenteCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Filippo Lobue
 * @version 1.0.0 - 27/09/2019
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaComponenteCapitoloAjaxAction extends PagedDataTableAjaxAction<ElementoComponenteCapitolo, 
RisultatiRicercaComponenteCapitoloAjaxModel, TipoComponenteImportiCapitolo, RicercaSinteticaTipoComponenteImportiCapitolo, RicercaSinteticaTipoComponenteImportiCapitoloResponse> {
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = 6070112707026461016L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN =
			"<div class='btn-group'> " +
				"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
				"<ul class='dropdown-menu pull-right' >";
	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaComponenteCapitolo_aggiorna.do?uidComponenteCapitolo=%source.uid%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			
	
	"<li><a href='#modaleAnnullaTipoComponenteImportiCapitolo' class='annullaTipoComponenteImportiCapitolo'>annulla</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
 
	
	@Autowired private transient TipoComponenteImportiCapitoloService componenteCapitoloService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaComponenteCapitoloAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_COMPONENTE_CAPITOLO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_COMPONENTE_CAPITOLO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaTipoComponenteImportiCapitolo request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaTipoComponenteImportiCapitolo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoComponenteCapitolo getInstance(TipoComponenteImportiCapitolo e) throws FrontEndBusinessException {
		return new ElementoComponenteCapitolo(e);
	}
	
	@Override
	protected RicercaSinteticaTipoComponenteImportiCapitoloResponse getResponse(RicercaSinteticaTipoComponenteImportiCapitolo request) {
		return componenteCapitoloService.ricercaSinteticaTipoComponenteImportiCapitolo(request);
	}
	
	@Override
	protected ListaPaginata<TipoComponenteImportiCapitolo> ottieniListaRisultati(RicercaSinteticaTipoComponenteImportiCapitoloResponse response) {
		return response.getListaTipoComponenteImportiCapitolo();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoComponenteCapitolo instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, 
			boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		 
		boolean gestioneAggiorna = gestisciAggiornamento(instance);
		boolean gestioneAnnulla = gestisciAnnullamento(instance);
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
	
		appendIfTrue(strAzioni, gestioneAggiorna, AZIONI_CONSENTITE_AGGIORNA);
		appendIfTrue(strAzioni, gestioneAnnulla, AZIONI_CONSENTITE_ANNULLA);
		strAzioni.append(AZIONI_CONSENTITE_END);
		String azioni = strAzioni.toString().replaceAll("%source.uid%", Integer.toString(instance.getUid()));
		instance.setAzioni(azioni);
		
	}
 

	/**
	 * Gestione dell'aggiornamento: controlla se sia possibile aggiornare  il componente
	 * 
	 * @param instance l'istanza del wrapper
	 * 
	 * @return <code>true</code> se il componente &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciAggiornamento(ElementoComponenteCapitolo instance) {
		// Abilitato per l’azione OP-CIC-componenteCapitolo 
		boolean aggiorna = true; //AzioniConsentiteFactory.isConsentito(AzioniConsentite.COMPONENTE_CAPITOLO_AGGIORNA, sessionHandler.getAzioniConsentite());
		if (instance.isAnnullato()) {
			aggiorna=false;
		} 
		
		return aggiorna;
	}

	/**
	 * Gestione dell'aggiornamento: controlla se sia possibile annulla il componente
	 * 
	 * @param instance l'istanza del wrapper
	 * 
	 * @return <code>true</code> se il componente &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciAnnullamento(ElementoComponenteCapitolo instance) {
		
		boolean annulla = true;//AzioniConsentiteFactory.isConsentito(AzioniConsentite.COMPONENTE_CAPITOLO_AGGIORNA, sessionHandler.getAzioniConsentite());
		
		if (instance.isAnnullato()) {
			annulla=false;
		} 
		// Abilitato per l’azione OP-CIC-componenteCapitolo 
		
		return annulla;
	}
 

	    
	
}