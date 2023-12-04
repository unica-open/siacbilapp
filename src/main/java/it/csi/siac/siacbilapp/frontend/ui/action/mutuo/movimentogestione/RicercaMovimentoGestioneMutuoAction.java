/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.movimentogestione;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.capitolo.helper.CapitoloEntrataGestioneActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.capitolo.helper.CapitoloUscitaGestioneActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.action.tipofinanziamento.helper.TipoFinanziamentoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.movimentogestione.RicercaMovimentoGestioneMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.movimentogestione.TipoMovimentoGestione;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.MovimentoGestione;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMovimentoGestioneMutuoAction extends BaseMutuoAction<RicercaMovimentoGestioneMutuoModel> {

	private static final long serialVersionUID = -2396982736848875052L;

	protected transient CapitoloUscitaGestioneActionHelper capitoloUscitaGestioneActionHelper;
	protected transient CapitoloEntrataGestioneActionHelper capitoloEntrataGestioneActionHelper;

	@PostConstruct
	protected void postConstruct() {
		super.postConstruct();
		capitoloUscitaGestioneActionHelper = new CapitoloUscitaGestioneActionHelper(this);
		capitoloEntrataGestioneActionHelper = new CapitoloEntrataGestioneActionHelper(this);
		tipoFinanziamentoActionHelper = new TipoFinanziamentoActionHelper(this);
	}
	
	@Override
	public void prepareEnterPage() {
		super.prepareEnterPage();
		
		try {
			model.setListaClasseSoggetto(soggettoActionHelper.caricaListaClasseSoggetto());
			model.setListaTipoAtto(provvedimentoActionHelper.caricaListaTipoAtto());
			model.setListaTipoComponente(capitoloUscitaGestioneActionHelper.caricaListaTipoComponentiImportiCapitolo());
			model.setListaTipiFinanziamento(tipoFinanziamentoActionHelper.caricaListaTipoFinanziamento());
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
		String tipoMovimento = model.getMovimentoGestione().getTipoMovimento();
		
		String ret = null;

		if (TipoMovimentoGestione.IMPEGNO.getCodice().equals(tipoMovimento)) {
			ricercaImpegni();
			ret = "risultatiRicercaImpegnoMutuo";
		}
		
		if (TipoMovimentoGestione.ACCERTAMENTO.getCodice().equals(tipoMovimento)) {
			ricercaAccertamenti();
			ret = "risultatiRicercaAccertamentoMutuo";
		}

		if (hasErrori()) {
			prepareEnterPage();
			enterPage();
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_MOVIMENTI_GESTIONE_MUTUO, null);

		return ret;
	}

	private void ricercaImpegni() {
		RicercaImpegniAssociabiliMutuo req = model.creaRequestRicercaImpegniAssociabiliMutuo();
		logServiceRequest(req);
		
		RicercaImpegniAssociabiliMutuoResponse res = mutuoService.ricercaImpegniAssociabiliMutuo(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			return;
		}
		
		if(res.getImpegni().getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_GESTIONE_MUTUO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_GESTIONE_MUTUO, res.getImpegni());
	}
	
	
	private void ricercaAccertamenti() {
		RicercaAccertamentiAssociabiliMutuo req = model.creaRequestRicercaAccertamentiAssociabiliMutuo();
		logServiceRequest(req);
		
		RicercaAccertamentiAssociabiliMutuoResponse res = mutuoService.ricercaAccertamentiAssociabiliMutuo(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			return;
		}
		
		if(res.getAccertamenti().getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return;
		}
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_GESTIONE_MUTUO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_GESTIONE_MUTUO, res.getAccertamenti());
	}
	
	public void validateRicerca() throws Exception {   

		MovimentoGestione movimentoGestione = model.getMovimentoGestione();
		
		try {
			checkMovimentoGestione(movimentoGestione);
			checkCapitoloGestione(movimentoGestione.getCapitolo());
			provvedimentoActionHelper.validateAttoAmministrativo(movimentoGestione.getAttoAmministrativo());
			soggettoActionHelper.findSoggetto(movimentoGestione.getSoggetto());

			checkCondition(
				movimentoGestione.getCapitolo().getUid() > 0 ||
				provvedimentoActionHelper.attoAmministrativoValorizzato(movimentoGestione.getAttoAmministrativo()) ||
				movimentoGestione.getSoggetto().getUid() > 0 ||
				NumberUtil.isValidAndGreaterThanZero(movimentoGestione.getAnno()),
				ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore()
			);
		}
		finally {
			callPrepareEnterPageOnErrors();
		}
	}
	
	private void checkMovimentoGestione(MovimentoGestione movimentoGestione) {
		checkCondition(
			NumberUtil.isValidAndGreaterThanZero(movimentoGestione.getAnno()) || 
			! NumberUtil.isValidAndNotEqualToZero(movimentoGestione.getNumero()), 
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno movimento")
		);
	}
	
	private void checkCapitoloGestione(Capitolo<? extends ImportiCapitolo, ? extends ImportiCapitolo> capitolo) {
		
		capitolo.setAnnoCapitolo(getBilancio().getAnno());
		
		if (TipoMovimentoGestione.IMPEGNO.getCodice().equals(model.getMovimentoGestione().getTipoMovimento())) {
			capitoloUscitaGestioneActionHelper.findCapitoloUscitaGestione(capitolo);
		}		
		if (TipoMovimentoGestione.ACCERTAMENTO.getCodice().equals(model.getMovimentoGestione().getTipoMovimento())) {
			capitoloEntrataGestioneActionHelper.findCapitoloEntrataGestione(capitolo);
		}		
	}
	
}
