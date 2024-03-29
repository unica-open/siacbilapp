/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.causale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.causale.RisultatiRicercaCausaleAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale.ElementoCausale;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale.ElementoCausaleEntrataFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;

/**
 * Action per i risultati di ricerca della causale.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 13/05/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCausaleEntrataAjaxAction extends PagedDataTableAjaxAction<ElementoCausale, 
	RisultatiRicercaCausaleAjaxModel, CausaleEntrata, RicercaSinteticaCausaleEntrata, RicercaSinteticaCausaleEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaCausaleEntrataAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
			
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='#' class='consultazioneCausale'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient PreDocumentoEntrataService predocumentoEntrataService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAUSALI_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAUSALI_ENTRATA);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCausaleEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCausaleEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoCausale getInstance(CausaleEntrata e) throws FrontEndBusinessException {
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = 
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		return ElementoCausaleEntrataFactory.getInstance(e, listaTipoAtto, listaStrutturaAmministrativoContabile, model.isGestioneUEB());
	}
	
	@Override
	protected RicercaSinteticaCausaleEntrataResponse getResponse(RicercaSinteticaCausaleEntrata request) {	
		return predocumentoEntrataService.ricercaSinteticaCausaleEntrata(request);
	}
	
	@Override
	protected ListaPaginata<CausaleEntrata> ottieniListaRisultati(RicercaSinteticaCausaleEntrataResponse response) {
		return response.getCausaliEntrata();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoCausale instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isAggiornaConsentitoCausaleSpesa(listaAzioniConsentite);
		Boolean isAnnullaConsentita = AzioniConsentiteFactory.isAnnullaConsentitoCausaleSpesa(listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsultaConsentitoCausaleSpesa(listaAzioniConsentite);
		
		boolean statoOperativoValido = instance.checkStatoOperativoValido();
		boolean statoOperativoAnnullato = instance.checkStatoOperativoAnnullato();

		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita) && !statoOperativoAnnullato) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isAnnullaConsentita) &&  statoOperativoValido) {
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
