/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.helper;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.BaseActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.action.provvedimento.helper.ProvvedimentoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.soggetto.helper.SoggettoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.MutuoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.LeggiPeriodiRimborsoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.LeggiPeriodiRimborsoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.MovimentiGestioneAssociatiMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.MovimentiGestioneAssociatiMutuoExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.PianoAmmortamentoMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.PianoAmmortamentoMutuoExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.ProgettiAssociatiMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.ProgettiAssociatiMutuoExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RipartizioneMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RipartizioneMutuoExcelReportResponse;
import it.csi.siac.siacbilser.model.mutuo.AccertamentoAssociatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.ImpegnoAssociatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.MovimentoGestioneAssociatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.MimeType;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public class MutuoActionHelper extends BaseActionHelper {

	@Autowired protected transient MutuoService mutuoService;

	protected transient SoggettoActionHelper soggettoActionHelper;
	protected transient ProvvedimentoActionHelper provvedimentoActionHelper;
	
	
//	private <MGA extends MovimentoGestioneAssociatoMutuo<? extends MovimentoGestione>> Comparator<? extends MGA> getMovimentoGestioneAssociatoMutuoComparator(){
//		return new Comparator<MGA>() {
//
//			@Override
//			public int compare(MGA arg0, MGA arg1) {
//					return arg0.getMovimentoGestione().getAnno().compareTo(arg1.getMovimentoGestione().getAnno()) != 0 ?
//							arg0.getMovimentoGestione().getAnno().compareTo(arg1.getMovimentoGestione().getAnno()) :
//								arg0.getMovimentoGestione().getNumero().compareTo(arg1.getMovimentoGestione().getNumero());
//				}};		
//	}
	
	public MutuoActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
		
		soggettoActionHelper = new SoggettoActionHelper(action);
		provvedimentoActionHelper = new ProvvedimentoActionHelper(action);
	}
	
	@SuppressWarnings("unchecked")
	protected BaseMutuoAction <? extends BaseMutuoModel> getAction() {
		return (BaseMutuoAction<? extends BaseMutuoModel>) action;
	}
	
	public List<PeriodoRimborsoMutuo> caricaListaPeriodoMutuo() throws WebServiceInvocationFailureException{
		List<PeriodoRimborsoMutuo> list = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_PERIODI_RIMBORSO_MUTUO);
		if(list == null) {
			LeggiPeriodiRimborsoMutuo req = creaRequestLeggiPeriodiRimborsoMutuo();
			action.logServiceRequest(req);
			LeggiPeriodiRimborsoMutuoResponse response = mutuoService.leggiPeriodiRimborsoMutuo(req);
			action.logServiceResponse(response);
		
			checkErrors(LeggiPeriodiRimborsoMutuo.class, response);
					
			list = response.getListaPeriodoRimborsoMutuo();
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_PERIODI_RIMBORSO_MUTUO, list);
		
		}
		
		return list;
	}

	public RicercaDettaglioMutuoResponse ricercaDettaglioMutuo(MutuoModelDetail...mutuoModelDetails) {
		return ricercaDettaglioMutuoWithRequest(getAction().getModel().creaRequestRicercaDettaglioMutuoWithModelDetails(mutuoModelDetails));
	}

	private RicercaDettaglioMutuoResponse ricercaDettaglioMutuo(RicercaDettaglioMutuo optionalRequest) {
		final String methodName = "ricercaDettaglioMutuo";
		
		BaseMutuoAction<? extends BaseMutuoModel> action = getAction();
		
		log.debug(methodName, "Ricerco il mutuo");
		action.getModel().getMutuo().setUid(action.getIdMutuoFromSession());
		RicercaDettaglioMutuo request = optionalRequest == null ? action.getModel().creaRequestRicercaDettaglioMutuo() : optionalRequest;
		action.logServiceRequest(request);
		
		request.setAnnoBilancio(action.getAnnoBilancio()); 
		
		RicercaDettaglioMutuoResponse response = mutuoService.ricercaDettaglioMutuo(request);
		action.logServiceResponse(response);
		
		return response;
	}
	
	public RicercaDettaglioMutuoResponse ricercaDettaglioMutuo() {
		return ricercaDettaglioMutuo((RicercaDettaglioMutuo) null);
	}

	private RicercaDettaglioMutuoResponse ricercaDettaglioMutuoWithRequest(RicercaDettaglioMutuo request) {
		return ricercaDettaglioMutuo(request);
	}


	public Mutuo leggiDettaglioMutuo(MutuoModelDetail...mutuoModelDetails) throws WebServiceInvocationFailureException {
		RicercaDettaglioMutuoResponse res = ricercaDettaglioMutuo(mutuoModelDetails);

		checkErrors(RicercaDettaglioMutuo.class, res);
		
		return res.getMutuo();
	}

	
	public List<StatoMutuo> caricaListaStatoMutuo() {
		return Arrays.asList(StatoMutuo.values());
	}
	
	private LeggiPeriodiRimborsoMutuo creaRequestLeggiPeriodiRimborsoMutuo() {
		LeggiPeriodiRimborsoMutuo req = new LeggiPeriodiRimborsoMutuo();
		req.setDataOra(new Date());
		req.setRichiedente(action.getModel().getRichiedente());

		return req;
	}
	
	
	public void checkFaseBilancio() {
		if (! isValidFaseBilancio()) {
			action.throwOperazioneIncompatibileFaseBilancioCorrente();
		}
	}

	public boolean isValidFaseBilancio() {
		return action.getBilancio().isInFaseAttuale(FaseBilancio.GESTIONE, FaseBilancio.ESERCIZIO_PROVVISORIO);
	}
	
	public void scaricaPianoAmmortamentoExcel() throws WebServiceInvocationFailureException {
		BaseMutuoAction<? extends BaseMutuoModel> action = getAction();
		
		PianoAmmortamentoMutuoExcelReport req = action.getModel().creaMutuoExcelReportRequest(PianoAmmortamentoMutuoExcelReport.class);
		PianoAmmortamentoMutuoExcelReportResponse res = mutuoService.pianoAmmortamentoMutuoExcelReport(req);
		
		checkErrors(PianoAmmortamentoMutuoExcelReport.class, res);
		
		action.initFileDownload(
				"piano-ammortamento-mutuo-"+ action.getModel().getMutuo().getNumero()+ "." + res.getExtension(),
				MimeType.XLSX.getMimeType(), 
				res.getReport());
		
	}

	public void scaricaMovimentiGestioneAssociatiExcel() throws Exception {
		BaseMutuoAction<? extends BaseMutuoModel> action = getAction();

		action.getModel().setMutuo(leggiDettaglioMutuo(MutuoModelDetail.MovimentiGestioneAssociati));
		
		sortMovimentiGestioneAssociati();
		
		MovimentiGestioneAssociatiMutuoExcelReport req = action.getModel().creaMutuoExcelReportRequest(MovimentiGestioneAssociatiMutuoExcelReport.class);
		MovimentiGestioneAssociatiMutuoExcelReportResponse res = mutuoService.movimentiGestioneAssociatiMutuoExcelReport(req);	 
	
		checkErrors(MovimentiGestioneAssociatiMutuoExcelReport.class, res);
		
		action.initFileDownload(
				"movimenti-associati-mutuo-"+ action.getModel().getMutuo().getNumero()+ "." + res.getExtension(),
				MimeType.XLSX.getMimeType(), 
				res.getReport());
	}

	public void scaricaProgettiAssociatiExcel() throws WebServiceInvocationFailureException {
		BaseMutuoAction<? extends BaseMutuoModel> action = getAction();
		
		action.getModel().setMutuo(leggiDettaglioMutuo(MutuoModelDetail.ProgettiAssociati));		
		
		ProgettiAssociatiMutuoExcelReport req = action.getModel().creaMutuoExcelReportRequest(ProgettiAssociatiMutuoExcelReport.class);
		ProgettiAssociatiMutuoExcelReportResponse res = mutuoService.progettiAssociatiMutuoExcelReport(req);
		
		checkErrors(ProgettiAssociatiMutuoExcelReport.class, res);
		
		action.initFileDownload(
				"progetti-associati-mutuo-"+ action.getModel().getMutuo().getNumero()+ "." + res.getExtension(),
				MimeType.XLSX.getMimeType(), 
				res.getReport());
		
	}

	public void scaricaRipartizioneExcel() throws WebServiceInvocationFailureException {
		BaseMutuoAction<? extends BaseMutuoModel> action = getAction();
		
		RipartizioneMutuoExcelReport req = action.getModel().creaMutuoExcelReportRequest(RipartizioneMutuoExcelReport.class);
		RipartizioneMutuoExcelReportResponse res = mutuoService.ripartizioneMutuoExcelReport(req);

		checkErrors(RipartizioneMutuoExcelReport.class, res);
		
		action.initFileDownload(
				"ripartizione-capitoli-mutuo-"+ action.getModel().getMutuo().getNumero()+ "." + res.getExtension(),
				MimeType.XLSX.getMimeType(), 
				res.getReport());
				
	}
	
	public void sortMovimentiGestioneAssociati() throws WebServiceInvocationFailureException{
		
		BaseMutuoAction<? extends BaseMutuoModel> action = getAction();
		
		CollectionUtil.sort(action.getModel().getMutuo().getElencoAccertamentiAssociati(), new Comparator<AccertamentoAssociatoMutuo>() {
			@Override
			public int compare(AccertamentoAssociatoMutuo arg0, AccertamentoAssociatoMutuo arg1) {
					return arg0.getMovimentoGestione().getAnno().compareTo(arg1.getMovimentoGestione().getAnno()) != 0 ?
							arg0.getMovimentoGestione().getAnno().compareTo(arg1.getMovimentoGestione().getAnno()) :
								arg0.getMovimentoGestione().getNumero().compareTo(arg1.getMovimentoGestione().getNumero());
				}});
		
		
		CollectionUtil.sort(action.getModel().getMutuo().getElencoImpegniAssociati(), new Comparator<ImpegnoAssociatoMutuo>() {
			@Override
			public int compare(ImpegnoAssociatoMutuo arg0, ImpegnoAssociatoMutuo arg1) {
					return arg0.getMovimentoGestione().getAnno().compareTo(arg1.getMovimentoGestione().getAnno()) != 0 ?
							arg0.getMovimentoGestione().getAnno().compareTo(arg1.getMovimentoGestione().getAnno()) :
								arg0.getMovimentoGestione().getNumero().compareTo(arg1.getMovimentoGestione().getNumero());
				}});

	}

	private void checkErrors(Class<? extends ServiceRequest> req, ServiceResponse res) throws WebServiceInvocationFailureException {
		
		if(res.hasErrori()) {
			action.addErrori(res);
			throw new WebServiceInvocationFailureException(action.createErrorInServiceInvocationString(req, res));
		}
	}
}
