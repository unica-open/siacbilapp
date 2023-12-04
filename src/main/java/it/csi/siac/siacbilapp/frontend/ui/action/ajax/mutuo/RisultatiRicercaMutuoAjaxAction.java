/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.helper.MutuoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaMutuoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.mutuo.ElementoMutuo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.mutuo.ElementoMutuoFactory;
import it.csi.siac.siacbilser.frontend.webservice.MutuoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaMutuoAjaxAction 
	extends PagedDataTableAjaxAction<ElementoMutuo, RisultatiRicercaMutuoAjaxModel, Mutuo, RicercaSinteticaMutuo, RicercaSinteticaMutuoResponse> {

	private static final long serialVersionUID = -5062644373441318228L;

	
	private transient MutuoActionHelper mutuoActionHelper;
	
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaMutuo_aggiorna.do?mutuo.uid=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='risultatiRicercaMutuo_consulta.do?mutuo.uid=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient MutuoService mutuoService;
	
	public RisultatiRicercaMutuoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_MUTUO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_MUTUO);
	}
	
	@PostConstruct
	protected void postConstruct() {
		mutuoActionHelper = new MutuoActionHelper(this);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMutuo request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMutuo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoMutuo getInstance(Mutuo e) throws FrontEndBusinessException {
		return ElementoMutuoFactory.getInstance(e);
	}

	@Override
	protected RicercaSinteticaMutuoResponse getResponse(RicercaSinteticaMutuo request) {
		return mutuoService.ricercaSinteticaMutuo(request);
	}

	@Override
	protected ListaPaginata<Mutuo> ottieniListaRisultati(RicercaSinteticaMutuoResponse response) {
		return response.getMutui();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoMutuo mutuo, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		appendIfTrue(azioniBuilder, true, AZIONI_CONSENTITE_BEGIN);
		appendIfTrue(azioniBuilder, isAggiornaConsentito(mutuo), AZIONI_CONSENTITE_AGGIORNA);
		appendIfTrue(azioniBuilder, isAnnullaConsentito(mutuo), AZIONI_CONSENTITE_ANNULLA);
		appendIfTrue(azioniBuilder, isConsultaConsentito(), AZIONI_CONSENTITE_CONSULTA);
		appendIfTrue(azioniBuilder, true, AZIONI_CONSENTITE_END);
		
		String azioni = azioniBuilder.toString().replaceAll("%UID%", mutuo.getUid() + "");
		
		mutuo.setAzioni(azioni);
	}
	
	private boolean isAggiornaConsentito(ElementoMutuo instance) {
		return 
			isAzioneConsentita(AzioneConsentitaEnum.OP_MUT_gestisciMutuo) && 
			mutuoActionHelper.isValidFaseBilancio() && 
			!StatoMutuo.Annullato.getCodice().equals(instance.getCodiceStatoMutuo());	
	}
	
	
	private boolean isAnnullaConsentito(ElementoMutuo instance) {
		return 
			isAzioneConsentita(AzioneConsentitaEnum.OP_MUT_gestisciMutuo) && 
			mutuoActionHelper.isValidFaseBilancio() && 
			StatoMutuo.Bozza.getCodice().equals(instance.getCodiceStatoMutuo());	
	}


	private boolean isConsultaConsentito() {
		return isAzioneConsentita(AzioneConsentitaEnum.OP_MUT_leggiMutuo);
	}
}
