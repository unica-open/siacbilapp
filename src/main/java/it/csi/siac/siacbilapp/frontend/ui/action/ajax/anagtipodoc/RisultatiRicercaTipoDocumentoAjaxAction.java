/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.anagtipodoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaTipoDocumentoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.anagtipodoc.ElementoTipoDocumento;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Action per i risultati di ricerca del Tipo Documento.
 * 
 *  
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoDocumentoAjaxAction extends PagedDataTableAjaxAction<ElementoTipoDocumento, RisultatiRicercaTipoDocumentoAjaxModel, TipoDocFEL, RicercaSinteticaTipoDocumentoFEL, RicercaSinteticaTipoDocumentoFELResponse> {
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = 6070112707026461016L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN =
			"<div class='btn-group'> " +
				"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
				"<ul class='dropdown-menu pull-right' >";
	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaTipoDocumento_aggiorna.do?uidTipoDocumento=%source.uid%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
				"<li><a href='#modaleEliminaTipoDocumentoCapitolo' class='annullaTipoDocumento'>elimina</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
 
	@Autowired
	private transient TipoDocumentoFELService tipoDocumentoServiceFEL;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoDocumentoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TIPO_DOCUMENTO_FEL);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TIPO_DOCUMENTO_FEL);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaTipoDocumentoFEL request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaTipoDocumentoFEL request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoTipoDocumento getInstance(TipoDocFEL e) throws FrontEndBusinessException {
		return new ElementoTipoDocumento(e);
	}
	
	@Override
	protected RicercaSinteticaTipoDocumentoFELResponse getResponse(RicercaSinteticaTipoDocumentoFEL request) {
		return tipoDocumentoServiceFEL.ricercaSinteticaTipoDocumentoFEL(request);
	}
	
	@Override
	protected ListaPaginata<TipoDocFEL> ottieniListaRisultati(RicercaSinteticaTipoDocumentoFELResponse response) {
		return response.getListaTipoDocFEL();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoTipoDocumento instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, 
			boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		 
		boolean gestioneAggiorna = gestisciAggiornamento(instance);
	 	boolean gestioneAnnulla = gestisciAnnullamento(instance);
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
	
		appendIfTrue(strAzioni, gestioneAggiorna, AZIONI_CONSENTITE_AGGIORNA);
		appendIfTrue(strAzioni, gestioneAnnulla, AZIONI_CONSENTITE_ANNULLA);
		strAzioni.append(AZIONI_CONSENTITE_END);
		String azioni = strAzioni.toString().replaceAll("%source.uid%", instance.getStringaCodice());
		instance.setAzioni(azioni);
		
	}
 

	/**
	 * Gestione dell'aggiornamento: controlla se sia possibile aggiornare  il tipo documento
	 * 
	 * @param instance l'istanza del wrapper
	 * 
	 * @return <code>true</code> se il componente &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciAggiornamento(ElementoTipoDocumento instance) {
		
		
		// Abilitato per l’azione OP-COM-gestTipoDocumento
		boolean aggiorna = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.TIPO_DOCUMENTO_AGGIORNA, sessionHandler.getAzioniConsentite());
		 
		
		return aggiorna;
	}

	/**
	 * Gestione dell'aggiornamento: controlla se sia possibile annulla il tipo documento
	 * 
	 * @param instance l'istanza del wrapper
	 * 
	 * @return <code>true</code> se il componente &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciAnnullamento(ElementoTipoDocumento instance) {
		
		// Abilitato per l’azione OP-COM-gestTipoDocumento
		boolean annulla = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.TIPO_DOCUMENTO_AGGIORNA, sessionHandler.getAzioniConsentite());
		
 
		
		return annulla;
	}
 

	    
	
}