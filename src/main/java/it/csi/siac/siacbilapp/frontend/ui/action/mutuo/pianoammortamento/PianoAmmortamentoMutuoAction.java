/*
.*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.pianoammortamento;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento.PianoAmmortamentoMutuoModel;
import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.business.utility.mutuo.RataMutuoLineMapper;
import it.csi.siac.siacbilser.business.utility.mutuo.helper.CalcolaRateMutuoHelper;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.MimeType;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommon.util.collections.filter.NullFilter;
import it.csi.siac.siaccommon.util.date.DateUtil;
import it.csi.siac.siaccommon.util.file.CsvExportUtil;
import it.csi.siac.siaccommon.util.fileparser.CsvFileParser;
import it.csi.siac.siaccommon.util.fileparser.DelimitedTextFileParser;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import xyz.timedrain.arianna.plugin.BreadCrumb;


public abstract class PianoAmmortamentoMutuoAction extends BaseMutuoAction<PianoAmmortamentoMutuoModel> {

	private static final long serialVersionUID = 2020318842439985171L;
	
	private static String MODELLO_PIANO_AMMORTAMENTO_FILE_NAME = "SIAC-modello-piano-ammortamento-mutuo.csv";
	
	private int idx;
	
	private String codiceStatoSalva;

	@Override
	public void prepareEnterPage()  {
		super.prepareEnterPage();
		try {
			model.setListaPeriodoMutuo(mutuoActionHelper.caricaListaPeriodoMutuo());
		}
		catch (WebServiceInvocationFailureException e) {
			throwSystemExceptionErroreDiSistema(e);
		}
	}
	
	public String enterPage() throws Exception {
		
		final String methodName = "enterPage";

		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo();
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		model.setMutuoFromResponse(response);
		
		return SUCCESS;
	}
	
	@Override
	@BreadCrumb(BaseMutuoAction.MODEL_TITOLO)
	public String execute() throws Exception {
		super.execute();
		
		sessionHandler.setParametro(BilSessionParameter.ELENCO_RATE_MUTUO, readElencoRateMutuo());
		
		return SUCCESS;
	}

	private List<RataMutuo> readElencoRateMutuo() throws Exception {
		if (model.getMutuo().getStatoMutuo().isBozza()) {
			return new ArrayList<RataMutuo>();
		}
		
		Mutuo mutuo = mutuoActionHelper.leggiDettaglioMutuo(MutuoModelDetail.PianoAmmortamento);
		
		return mutuo.getElencoRate();
	}
	
	public String calcolaRate() {
		
		caricaDatiPagina();
		
		CalcolaRateMutuoHelper calcolaRateMutuoHelper = CalcolaRateMutuoHelper.getInstance();
		
		model.getMutuo().setElencoRate(
			calcolaRateMutuoHelper.calcolaRate(
				1,
				model.getMutuo().getScadenzaPrimaRata(),
				MutuoUtil.calcNumeroRate(model.getMutuo().getPeriodoRimborso(), model.getMutuo().getDurataAnni()),
				null,
				model.getMutuo().getSommaMutuataIniziale(), 
				BigDecimalUtil.percent(model.getMutuo().getTassoInteresse()), 
				model.getMutuo().getPeriodoRimborso(), 
				BigDecimal.ZERO
			)
		);
		
		setTotaliImportiMutuo();
		
		sessionHandler.setParametro(BilSessionParameter.ELENCO_RATE_MUTUO, model.getMutuo().getElencoRate());
		
		return SUCCESS;
	}
	
	public String aggiungiRata() {
		
		caricaDatiPagina();
		setElencoRateFromSession();
		mergeElencoRate();
		
		addRataAndAdjust();

		setTotaliImportiMutuo();
		
		sessionHandler.setParametro(BilSessionParameter.ELENCO_RATE_MUTUO, model.getMutuo().getElencoRate());
		
		return SUCCESS;
	}

	private void setTotaliImportiMutuo() {
		model.getMutuo().setTotaliImporti();
	}

	private void mergeElencoRate() {
		
		List<RataMutuo> elencoRate = model.getMutuo().getElencoRate();
		
		for (int i = 0; i < elencoRate.size(); i++) {
			RataMutuo rataMutuo = elencoRate.get(i);
			rataMutuo.setDataScadenza(model.getDataScadenza()[i]);
			rataMutuo.setImportoTotale(model.getImportoTotale()[i]);
			rataMutuo.setImportoQuotaCapitale(model.getImportoQuotaCapitale()[i]);
			rataMutuo.setImportoQuotaInteressi(model.getImportoQuotaInteressi()[i]);
			rataMutuo.setImportoQuotaOneri(model.getImportoQuotaOneri()[i]);
		}
	}

	private void addRataAndAdjust() {
		List<RataMutuo> elencoRate = model.getMutuo().getElencoRate();
		PeriodoRimborsoMutuo periodoRimborsoMutuo = model.getMutuo().getPeriodoRimborso();
		
		if (elencoRate.isEmpty()) {
			elencoRate.add(newRataMutuo());
			return;
		}
		
		int k = idx < elencoRate.size() ? idx : idx-1;
		
		elencoRate.add(idx, cloneRataMutuo(elencoRate.get(k)));
		
		for (int i = k + 1; i < elencoRate.size(); i++) {
			RataMutuo rataMutuo = elencoRate.get(i);
			
			rataMutuo.setNumeroRataPiano(rataMutuo.getNumeroRataPiano() + 1);
			rataMutuo.setDataScadenza(DateUtils.addMonths(rataMutuo.getDataScadenza(), periodoRimborsoMutuo.getNumeroMesi()));
			rataMutuo.setNumeroRataAnno(MutuoUtil.calcNumeroRataAnno(rataMutuo.getDataScadenza(), rataMutuo.getNumeroRataPiano(), periodoRimborsoMutuo));
			rataMutuo.setAnno(DateUtil.getYear(rataMutuo.getDataScadenza()));
		}
	}
	
	private RataMutuo newRataMutuo() {
		RataMutuo newRataMutuo = new RataMutuo();
		
		newRataMutuo.setDataScadenza(model.getMutuo().getScadenzaPrimaRata());
		newRataMutuo.setAnno(model.getMutuo().getAnnoInizio());
		newRataMutuo.setNumeroRataAnno(1);
		newRataMutuo.setNumeroRataPiano(1);
		newRataMutuo.setImportoTotale(BigDecimal.ZERO);
		
		return newRataMutuo;
	}

	private RataMutuo cloneRataMutuo(RataMutuo rataMutuo) {
		RataMutuo rataMutuoClone = new RataMutuo();
		
		rataMutuoClone.setAnno(rataMutuo.getAnno());
		rataMutuoClone.setDataScadenza(rataMutuo.getDataScadenza());
		rataMutuoClone.setNumeroRataAnno(rataMutuo.getNumeroRataAnno());
		rataMutuoClone.setNumeroRataPiano(rataMutuo.getNumeroRataPiano());
		rataMutuoClone.setImportoTotale(BigDecimal.ZERO);
		
		return rataMutuoClone;
	}

	public String eliminaRata() {
		
		caricaDatiPagina();
		setElencoRateFromSession();
		mergeElencoRate();

		removeRataAndAdjust();
		
		setTotaliImportiMutuo();

		sessionHandler.setParametro(BilSessionParameter.ELENCO_RATE_MUTUO, model.getMutuo().getElencoRate());
		
		return SUCCESS;
	}
	
	private void removeRataAndAdjust() {
		List<RataMutuo> elencoRate = model.getMutuo().getElencoRate();
		PeriodoRimborsoMutuo periodoRimborsoMutuo = model.getMutuo().getPeriodoRimborso();

		elencoRate.remove(idx);

		for (int i = idx; i < elencoRate.size(); i++) {
			RataMutuo rataMutuo = elencoRate.get(i);
			
			rataMutuo.setNumeroRataPiano(rataMutuo.getNumeroRataPiano() - 1);
			rataMutuo.setDataScadenza(DateUtils.addMonths(rataMutuo.getDataScadenza(), -periodoRimborsoMutuo.getNumeroMesi()));
			rataMutuo.setNumeroRataAnno(MutuoUtil.calcNumeroRataAnno(rataMutuo.getDataScadenza(), rataMutuo.getNumeroRataPiano(), periodoRimborsoMutuo));
			rataMutuo.setAnno(DateUtil.getYear(rataMutuo.getDataScadenza()));
		}
	}
	
	public void validateSalva() {
		caricaDatiPagina();
		setElencoRateFromSession();
		mergeElencoRate();
		setTotaliImportiMutuo();

		NullFilter<BigDecimal> nullFilterBigDecimal = new NullFilter<BigDecimal>();

		checkCondition(ArrayUtil.findFirst(model.getDataScadenza(), new NullFilter<Date>())<0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data scadenza"));
		checkCondition(ArrayUtil.findFirst(model.getImportoTotale(), nullFilterBigDecimal)<0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo totale"));
		checkCondition(ArrayUtil.findFirst(model.getImportoQuotaCapitale(), nullFilterBigDecimal)<0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo quota capitale"));
		checkCondition(ArrayUtil.findFirst(model.getImportoQuotaInteressi(), nullFilterBigDecimal)<0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo quota interessi"));
		checkCondition(ArrayUtil.findFirst(model.getImportoQuotaOneri(), nullFilterBigDecimal)<0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo quota oneri"));
	}
	
	public String salva() {
		final String methodName = "salva";
		
		ricalcolaDebitiInizialeEResiduo();
		
		AggiornaPianoAmmortamentoMutuo request = model.creaRequestAggiornaPianoAmmortamentoMutuo(StatoMutuo.fromCodice(codiceStatoSalva)); 
		AggiornaPianoAmmortamentoMutuoResponse response = mutuoService.aggiornaPianoAmmortamentoMutuo(request);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AggiornaPianoAmmortamentoMutuo.class, response));
			addErrori(response);
			prepareEnterPage();

			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		
		return SUCCESS;
	}

	
	private void ricalcolaDebitiInizialeEResiduo() {
		BigDecimal debitoIniziale = model.getMutuo().getSommaMutuataIniziale();
		
		for (RataMutuo rataMutuo : model.getMutuo().getElencoRate()) {
			rataMutuo.setDebitoIniziale(debitoIniziale);
			rataMutuo.calcDebitoResiduo();
			debitoIniziale = rataMutuo.getDebitoResiduo();
		}
	}

	public String annulla() {
		final String methodName = "annulla";
		
		log.debug(methodName, "Annullamento del mutuo avente uid " + model.getMutuo().getUid());
		
		AnnullaPianoAmmortamentoMutuo request = model.creaRequestAnnullaPianoAmmortamentoMutuo();
		AnnullaPianoAmmortamentoMutuoResponse response = mutuoService.annullaPianoAmmortamentoMutuo(request);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AnnullaPianoAmmortamentoMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.ELENCO_RATE_MUTUO, null);
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		
		return SUCCESS;
		
	}
	
	public String caricaRateDaFile() throws Exception {

		caricaDatiPagina();
		
		model.getMutuo().setElencoRate(new ArrayList<RataMutuo>());
		
		DelimitedTextFileParser<RataMutuo> rataMutuoParser = new CsvFileParser<RataMutuo>(model.getFileElencoRate());
		rataMutuoParser.setLineMapper(new RataMutuoLineMapper());
		rataMutuoParser.skipLine(); // skip file header
		
		rataMutuoParser.parse(new Predicate<RataMutuo>() {
			@Override
			public void apply(RataMutuo source) {
				source.setNumeroRataPiano(rataMutuoParser.getLineNumber() - 1);
				checkImportoTotale(source);
				model.getMutuo().getElencoRate().add(source);
			}

			private void checkImportoTotale(RataMutuo source) {
				BigDecimal importoTotaleCalc = BigDecimalUtil.sum(source.getImportoQuotaCapitale(), source.getImportoQuotaInteressi(), source.getImportoQuotaOneri());
				
				if (!BigDecimalUtil.equalWithinTolerance(importoTotaleCalc, source.getImportoTotale(), 1e-3)) {
					source.setImportoTotale(importoTotaleCalc);
					addMessaggio(MessaggioBil.IMPORTO_RATA_NON_CORRETTO.getMessaggio(String.valueOf(source.getNumeroRataPiano())));
				}
			}
		});

		setTotaliImportiMutuo();
		
		sessionHandler.setParametro(BilSessionParameter.ELENCO_RATE_MUTUO, model.getMutuo().getElencoRate());

		addInformazione(MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio("File inviato correttamente"));

		return SUCCESS;
	}
	
	public String scaricaModelloCsv()  {
		final String methodName = "scaricaModelloCsv";
		
		try {
			initFileDownload(MODELLO_PIANO_AMMORTAMENTO_FILE_NAME, 
					MimeType.CSV.getMimeType(), 
					IOUtils.toByteArray(openModelloPianoAmmortamento()));
		}
		catch (IOException e) {
			prepareEnterPage();
			addErroreDiSistema(methodName, e);
			return INPUT;
		}

		return SUCCESS;
	}

	private InputStream openModelloPianoAmmortamento() {
		return this.getClass().getClassLoader().getResourceAsStream("mutui/" + MODELLO_PIANO_AMMORTAMENTO_FILE_NAME);
	}

	public String scaricaPianoCsv()  {
		final String methodName = "scaricaPianoCsv";
		
		try {
			CsvExportUtil csvExportUtil = new CsvExportUtil();
			
			csvExportUtil.appendTo(openModelloPianoAmmortamento());
			
			setElencoRateFromSession();
			
			csvExportUtil.addLines(model.getMutuo().getElencoRate(), new Function<RataMutuo, String[]>(){
	
				@Override
				public String[] map(RataMutuo rataMutuo) {
					return new String[] {
							String.valueOf(rataMutuo.getNumeroRataPiano()), 
							String.valueOf(rataMutuo.getAnno()), 
							String.valueOf(rataMutuo.getNumeroRataAnno()), 
							DateUtil.formatDate(rataMutuo.getDataScadenza()), 
							NumberUtil.toDecimal(rataMutuo.getImportoTotale()) , 
							NumberUtil.toDecimal(rataMutuo.getImportoQuotaCapitale()),
							NumberUtil.toDecimal(rataMutuo.getImportoQuotaInteressi()), 
							NumberUtil.toDecimal(rataMutuo.getImportoQuotaOneri()), 
							NumberUtil.toDecimal(rataMutuo.getDebitoIniziale()), 
							StringUtils.replace(NumberUtil.toDecimal(rataMutuo.getDebitoResiduo()), "-0,00", "0,00")
					};
				}}
			);
		
			initFileDownload("piano-ammortamento-mutuo-"+ model.getMutuo().getNumero()+ ".csv", MimeType.CSV.getMimeType(), csvExportUtil.export());
		}
		catch (IOException e) {
			prepareEnterPage();
			addErroreDiSistema(methodName, e);
			return INPUT;
		}

		return SUCCESS;
	}
	
	public String scaricaPianoExcel() throws Exception {
		setElencoRateFromSession();
		
		mutuoActionHelper.scaricaPianoAmmortamentoExcel();
		
		return SUCCESS;
		
	}

	private void caricaDatiPagina() {
		prepareEnterPage();
		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo();
		model.setMutuoFromResponse(response);
	}

	private void setElencoRateFromSession() {
		model.getMutuo().setElencoRate(ObjectUtils.defaultIfNull(sessionHandler.getParametro(BilSessionParameter.ELENCO_RATE_MUTUO), new ArrayList<RataMutuo>()));
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getCodiceStatoSalva() {
		return codiceStatoSalva;
	}

	public void setCodiceStatoSalva(String codiceStatoSalva) {
		this.codiceStatoSalva = codiceStatoSalva;
	}
}
