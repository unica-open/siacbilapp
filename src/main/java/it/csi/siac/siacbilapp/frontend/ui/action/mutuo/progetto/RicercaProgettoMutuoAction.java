/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.progetto;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.action.tipoambito.helper.TipoAmbitoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.progetto.RicercaProgettoMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaProgettoMutuoAction extends BaseMutuoAction<RicercaProgettoMutuoModel> {

	private static final long serialVersionUID = -2396982736848875052L;

	protected transient TipoAmbitoActionHelper tipoAmbitoActionHelper;

	@PostConstruct
	protected void postConstruct() {
		super.postConstruct();
		tipoAmbitoActionHelper = new TipoAmbitoActionHelper(this);
	}
	

	@Override
	public void prepareEnterPage() {
		super.prepareEnterPage();
		
		try {
			model.setListaTipoAtto(provvedimentoActionHelper.caricaListaTipoAtto());
			model.setListaTipoAmbito(tipoAmbitoActionHelper.caricaListaTipoAmbito());
		}
		catch (WebServiceInvocationFailureException e) {
			throwSystemExceptionErroreDiSistema(e);
		}
	}
	
	@Override
	public String enterPage() throws Exception {
		super.enterPage();

		model.setMutuo(mutuoActionHelper.leggiDettaglioMutuo(MutuoModelDetail.Soggetto));
		return SUCCESS;
	}
	

	public String ricerca() throws Exception {
		
		String ret = "risultatiRicercaProgettoMutuo";

		RicercaProgettiAssociabiliMutuo req = model.creaRequestRicercaProgettiAssociabiliMutuo();
		logServiceRequest(req);
		
		RicercaProgettiAssociabiliMutuoResponse res = mutuoService.ricercaProgettiAssociabiliMutuo(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
		}
		
		if(res.getProgetti().getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROGETTI_MUTUO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROGETTI_MUTUO, res.getProgetti());
		
		if (hasErrori()) {
			prepareEnterPage();
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_PROGETTI_MUTUO, null);

		return ret;
	}

	
	public void validateRicerca() throws Exception {   

		Progetto progetto = model.getProgetto();
		
		try {
			provvedimentoActionHelper.validateAttoAmministrativo(progetto.getAttoAmministrativo());
		}
		finally {
			callPrepareEnterPageOnErrors();
		}
	}
}
