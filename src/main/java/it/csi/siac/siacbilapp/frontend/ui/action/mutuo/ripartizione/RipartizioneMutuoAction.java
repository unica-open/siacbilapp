/*
.*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.ripartizione;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.capitolo.helper.CapitoloUscitaGestioneActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.action.tipofinanziamento.helper.TipoFinanziamentoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.ripartizione.RipartizioneMutuoModel;
import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoRipartizioneMutuo;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import xyz.timedrain.arianna.plugin.BreadCrumb;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RipartizioneMutuoAction extends BaseMutuoAction<RipartizioneMutuoModel> {

	private static final long serialVersionUID = 1163245439373301859L;
	
	private int idx;
	
	private static final MutuoModelDetail[] DETTAGLIO_MUTUO_MODEL_DETAILS = 
			ArrayUtil.toArray(MutuoModelDetail.PeriodoRimborso, MutuoModelDetail.Stato, MutuoModelDetail.Soggetto, MutuoModelDetail.RipartizioneCapitoliConTotali);
	
	protected transient CapitoloUscitaGestioneActionHelper capitoloUscitaGestioneActionHelper;
	
	@PostConstruct
	protected void postConstruct() {
		super.postConstruct();
		capitoloUscitaGestioneActionHelper = new CapitoloUscitaGestioneActionHelper(this);
		tipoFinanziamentoActionHelper = new TipoFinanziamentoActionHelper(this);
	}	
	
	@Override
	public void prepareEnterPage() {
		super.prepareEnterPage();
		
		try {
			model.setListaTipiFinanziamento(tipoFinanziamentoActionHelper.caricaListaTipoFinanziamento());
		}
		catch (WebServiceInvocationFailureException e) {
			throwSystemExceptionErroreDiSistema(e);
		}
	}	
	
	@Override
	@BreadCrumb(BaseMutuoAction.MODEL_TITOLO)
	public String execute() throws Exception {
		super.execute();
		
		sessionHandler.setParametro(BilSessionParameter.ELENCO_RIPARTIZIONE_MUTUO, readElencoRipartizioneMutuo());
		
		return SUCCESS;
	}
	
	private List<RipartizioneMutuo> readElencoRipartizioneMutuo() throws Exception {
		
		Mutuo mutuo = mutuoActionHelper.leggiDettaglioMutuo(MutuoModelDetail.RipartizioneCapitoliConTotali);
		
		return mutuo.getElencoRipartizioneMutuo();
	}	
	
	public String enterPage() throws Exception {
		
		final String methodName = "enterPage";

		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		
		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo(DETTAGLIO_MUTUO_MODEL_DETAILS);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		model.setMutuoFromResponse(response);
		
		return SUCCESS;
	}	
	
	public String eliminaRipartizione() {
		
		caricaDatiPagina();
		setElencoRipartizioneFromSession(); 
		remove();
		setTotaliRipartizioneInteressiMutuo();

		sessionHandler.setParametro(BilSessionParameter.ELENCO_RIPARTIZIONE_MUTUO, model.getMutuo().getElencoRipartizioneMutuo());
		
		return SUCCESS;
	}
	
	public void validateAggiungiRipartizione() {
		
		caricaDatiPagina();
		
		setElencoRipartizioneFromSession();
		setTotaliRipartizioneInteressiMutuo();
		
		checkRipartizioneCapitale();
		
		checkCondition(model.getRipartizioneMutuo().getTipoRipartizioneMutuo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo ripartizione"));
		checkCondition(model.getRipartizioneMutuo().getCapitolo().getNumeroCapitolo() != null,ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("capitolo"));
		checkCondition(model.getRipartizioneMutuo().getCapitolo().getNumeroArticolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("articolo"));
		checkCondition(model.getRipartizioneMutuo().getRipartizioneImporto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo"));
		checkCondition(NumberUtil.isValidAndGreaterThanZero(model.getRipartizioneMutuo().getRipartizioneImporto()), ErroreCore.VALORE_NON_CONSENTITO.getErrore("importo"," - inserire un importo positivo"));
		
		capitoloUscitaGestioneActionHelper.findCapitoloUscitaGestione(model.getRipartizioneMutuo().getCapitolo());
		
		checkRipartizioneInteressi();
	}
	
	public String aggiungiRipartizione() {
		
		addAndSort();
		
		setTotaliRipartizioneInteressiMutuo();

		sessionHandler.setParametro(BilSessionParameter.ELENCO_RIPARTIZIONE_MUTUO, model.getMutuo().getElencoRipartizioneMutuo());
		
		return SUCCESS;
	}
	
	
	public void validateSalva() {
		caricaDatiPagina();
		setElencoRipartizioneFromSession();
	}
		
	public String salva() {
		final String methodName = "salva";
		
		AggiornaRipartizioneMutuo request = model.creaRequestAggiornaRipartizioneMutuo(); 
		AggiornaRipartizioneMutuoResponse response = mutuoService.aggiornaRipartizioneMutuo(request);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AggiornaRipartizioneMutuo.class, response));
			addErrori(response);
			prepareEnterPage();
			setTotaliRipartizioneInteressiMutuo();

			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}	
	
	public String annulla() {
		final String methodName = "annulla";
		
		log.debug(methodName, "Annullamento ripartizione del mutuo con id " + model.getMutuo().getUid());
		
		AnnullaRipartizioneMutuo request = model.creaRequestAnnullaRipartizioneMutuo();
		AnnullaRipartizioneMutuoResponse response = mutuoService.annullaRipartizioneMutuo(request);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AnnullaRipartizioneMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
		
	}
	
	public String scaricaRipartizioneExcel() throws Exception {
		setElencoRipartizioneFromSession();
		
		mutuoActionHelper.scaricaRipartizioneExcel();

		return SUCCESS;
		
	}	
	
	private void caricaDatiPagina() {
		prepareEnterPage(); // da ridefinire come fatto su PianoAmmortamentoMutuoAction ? 
		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo(DETTAGLIO_MUTUO_MODEL_DETAILS);
		model.setMutuoFromResponse(response);
	}
	
	private void setElencoRipartizioneFromSession() {
		model.getMutuo().setElencoRipartizioneMutuo(ObjectUtils.defaultIfNull(sessionHandler.getParametro(BilSessionParameter.ELENCO_RIPARTIZIONE_MUTUO), new ArrayList<RipartizioneMutuo>()));
	}	
	
	private void remove() {
		
		List<RipartizioneMutuo> ripartizioneMutuo = model.getMutuo().getElencoRipartizioneMutuo();
		ripartizioneMutuo.remove(idx);
		
	}	
	private void addAndSort() {
		List<RipartizioneMutuo> ripartizioneMutuo = model.getMutuo().getElencoRipartizioneMutuo();
		
		model.getRipartizioneMutuo().setRipartizionePercentuale(MutuoUtil.calcPercentualeRipartizione(
				model.getRipartizioneMutuo().getTipoRipartizioneMutuo(), model.getRipartizioneMutuo().getRipartizioneImporto(), model.getMutuo().getSommaMutuataIniziale()));
		
		ripartizioneMutuo.add(model.getRipartizioneMutuo());
		
		 CollectionUtil.sort(ripartizioneMutuo, new Comparator<RipartizioneMutuo>() {
			@Override
			public int compare(RipartizioneMutuo arg0, RipartizioneMutuo arg1) {
				return TipoRipartizioneMutuo.Capitale.equals(arg0.getTipoRipartizioneMutuo()) ? -1 : 1;
			}});
	}	
	
	private void setTotaliRipartizioneInteressiMutuo() {
		model.getMutuo().setTotaliRipartizioneInteressi();
	}	
	
	private void checkRipartizioneCapitale() {
		
		if (TipoRipartizioneMutuo.Interessi.equals(model.getRipartizioneMutuo().getTipoRipartizioneMutuo())) {
			return;
		}
		List<RipartizioneMutuo> ripartizioneMutuoCapitaleList = getRipartizioneMutuoListFiltered(TipoRipartizioneMutuo.Capitale);
		checkCondition(ripartizioneMutuoCapitaleList==null || ripartizioneMutuoCapitaleList.isEmpty(), ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("e' ammessa una e una sola ripartizione di tipo Capitale"));
	}
	
	private void checkRipartizioneInteressi() {
		
		if (TipoRipartizioneMutuo.Capitale.equals(model.getRipartizioneMutuo().getTipoRipartizioneMutuo())) {
			return;
		}
		checkCondition(
				CollectionUtil.findFirst(getRipartizioneMutuoListFiltered(TipoRipartizioneMutuo.Interessi), new Filter<RipartizioneMutuo>() {
			@Override
			public boolean isAcceptable(RipartizioneMutuo source) {
				return model.getRipartizioneMutuo().getCapitolo().getUid() == source.getCapitolo().getUid();}}) == null, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("non e' possibile inserire piu' ripartizioni di tipo Interesse sullo stesso capitolo"));
	}	
	
	
	private List<RipartizioneMutuo> getRipartizioneMutuoListFiltered (TipoRipartizioneMutuo tipoRipartizioneMutuo){
		return MutuoUtil.getRipartizioneMutuoListFiltered(model.getMutuo().getElencoRipartizioneMutuo(), tipoRipartizioneMutuo);
	}	
	
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public void setTipoRipartizioneMutuoStr(String tipoRipartizioneMutuo) {
		model.getRipartizioneMutuo().setTipoRipartizioneMutuo(TipoRipartizioneMutuo.fromCodice(tipoRipartizioneMutuo));
	}

	public String getTipoRipartizioneMutuoStr() {
		return model.getRipartizioneMutuo().getTipoRipartizioneMutuo().getCodice();
	}

}
