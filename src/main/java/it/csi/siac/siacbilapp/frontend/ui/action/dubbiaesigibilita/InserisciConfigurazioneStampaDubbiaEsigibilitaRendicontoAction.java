/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.dubbiaesigibilita;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.AggiornaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconet;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconetResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.EliminaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.InserisceAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.RipristinaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendicontoResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.TipologiaEstrazioniFogliDiCalcolo;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * Classe di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoAction extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1048555718689347461L;
	
	@Override
	public boolean isConsentitoTornaInBozza() {
		return Boolean.TRUE;
	}
	
	@Override
	protected boolean checkCompatibilitaFaseBilancio(FaseBilancio faseBilancio) {
		return FaseBilancio.GESTIONE.equals(faseBilancio) || FaseBilancio.ASSESTAMENTO.equals(faseBilancio) || FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio); //SIAC-8565
	}
	
	@Override
	public String caricaListaAccantonamentoFondi() {
		RicercaAccantonamentoFondiDubbiaEsigibilita req = model.creaRequestRicercaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);

		RicercaAccantonamentoFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.ricercaAccantonamentoFondiDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) { 
			addErrori(res);
			return INPUT;
		}
		extractAccantonamenti(res);
		
		return SUCCESS;
	}
	
	public String salvaCampiReport() {
		final String methodName = "salvaCampiReport";
		
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
				TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		logServiceRequest(req);
		
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.salvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
		
		if (res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
		}
		
		return SUCCESS;
	}
	
	public String calcolaCreditiStralciati() {
		final String methodName = "calcolaCreditiStralciati";
		
		CalcolaImportiPerAllegatoArconet req = model.creaRequestCalcolaImportiAllegatoArconet();
		logServiceRequest(req);
		log.debug(methodName, JAXBUtility.marshall(req));
		
		CalcolaImportiPerAllegatoArconetResponse res = 
				fondiDubbiaEsigibilitaService.calcolaCampiAllegatoArconet(req);
		
		logServiceResponse(res);

		if (res.hasErrori()) {
			model.addErrori(res.getErrori());
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
		}
		
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		
		return SUCCESS;
	}

	public String salvaAttributi() {
		final String methodName = "salvaAttributi";
		
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
				TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		logServiceRequest(req);
	
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.salvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		return caricaListaAccantonamentoFondi();
	}
	
	public String ricercaCapitoli() {
		caricaListaAccantonamentoFondi();
		return SUCCESS;
	}
	
	@Override
	public String confermaCapitoli() {
		List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaTemp = model.getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp();
	
		for (CapitoloEntrataGestione c : model.getListaCapitoloEntrataGestione()) {
			addCapitoloIfNotPresent(c, new AccantonamentoFondiDubbiaEsigibilitaRendiconto(), listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		}
		
		model.setListaCapitoloEntrataGestione(new ArrayList<CapitoloEntrataGestione>());
		
		return SUCCESS;
	}
	
	//SIAC-7858 CM 08/06/2021 Inizio
	public String confermaCapitoliModale() {
		
		final String methodName = "confermaCapitoliModale";
		
		//vedi confermaCapitoli
		List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
		
		for (CapitoloEntrataGestione c : model.getListaCapitoloEntrataGestione()) {
			AccantonamentoFondiDubbiaEsigibilitaRendiconto afder = new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
			afder.setCapitolo(c);
			listaAccantonamentoFondiDubbiaEsigibilitaTemp.add(afder);
		}
		
		model.setListaCapitoloEntrataGestione(new ArrayList<CapitoloEntrataGestione>());
		model.setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp(listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		
		InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto req = model.creaRequestCopiaCapitoliModaleAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);
		
		InserisceAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse res = fondiDubbiaEsigibilitaService.inserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		extractAccantonamenti(res);
		
		return SUCCESS;
	}
	//SIAC-7858 CM 08/06/2021 Fine
	
	/**
	 * Preparazione per il metodo {@link #salvaCapitoli()}
	 */
	public void prepareSalvaCapitoli() {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati(null);
	}
	
	@Override
	public String salvaCapitoli() {
		AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto req = model.creaRequestAggiornaAccantonamentoFondiDubbiaEsigibilita();
		
		logServiceRequest(req);
	
		AggiornaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse res = fondiDubbiaEsigibilitaService.aggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto(req);
		logServiceResponse(res);
	
		// SIAC-8356 warning in caso di lista vuota al salvataggio
		if(Esito.FALLIMENTO.equals(res.getEsito()) && CollectionUtils.isEmpty(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto())) {
			addMessaggio(ErroreBil.NESSUN_ELEMENTO_SALVATO.getErrore());
			log.debug("salvaCapitoli", "Nessun elemento e' stato salvato, lista vuota");
			return INPUT;
		}
		
		if (res.hasErrori()) { 
			addErrori(res);
			log.debug("salvaCapitoli", "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		extractAccantonamenti(res);
		
		return SUCCESS;
	}
	
	@Override
	public String eliminaAccantonamento() {
		
		final String methodName = "eliminaAccantonamento";

		EliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto req = model.creaRequestEliminaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		EliminaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse res = fondiDubbiaEsigibilitaService.eliminaAccantonamentoFondiDubbiaEsigibilitaRendiconto(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		extractAccantonamenti(res);

		return SUCCESS;
	}

	@Override
	public String ripristinaAccantonamento() {

		final String methodName = "ripristinaAccantonamento";

		RipristinaAccantonamentoFondiDubbiaEsigibilitaRendiconto req = model.creaRequestRipristinaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		RipristinaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse res = fondiDubbiaEsigibilitaService.ripristinaAccantonamentoFondiDubbiaRendicontoEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		extractAccantonamenti(res);

		return SUCCESS;
	}
	
	public String nuovaVersione() {
		final String methodName = "nuovaVersione";
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioNuovo(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
		logServiceRequest(req);
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.salvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
		if(res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			addErrori(res);
			return INPUT;
		}
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		try {
			caricaAttributiPerCopia();
			// In una nuova versione l'accantonamento graduale e' sempre a 100
			resetAccantonamentoGradualePerNuovaVersione();
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio: " + wsife.getMessage());
			return INPUT;
		}
		model.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>());
		return SUCCESS;
	}
	
	public void preSimulaPopolaDaPrevisione() {
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
	}
	
	public String simulaPopolaDaPrevisione() {
		return simulaPopolaDa(TipoImportazione.DA_PREVISIONE);
	}
	
	public void preSimulaPopolaDaRendiconto() {
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
	}
	
	public String simulaPopolaDaRendiconto() {
		return simulaPopolaDa(TipoImportazione.DA_RENDICONTO);
	}
	
	public void preSimulaPopolaDaGestione() {
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
	}
	
	public String simulaPopolaDaGestione() {
		return simulaPopolaDa(TipoImportazione.DA_GESTIONE);
	}
	
	public void preSimulaPopolaDaAnagraficaCapitoli() {
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
	}
	
	public String simulaPopolaDaAnagraficaCapitoli() {
		return simulaPopolaDa(TipoImportazione.DA_ANAGRAFICA_CAPITOLI);
	}
	
	public void preSimulaPopolaDaElaborazione() {
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
	}
	
	public String simulaPopolaDaElaborazione() {
		return simulaPopolaDa(TipoImportazione.DA_VERSIONE_PRECEDENTE);
	}
	
	public void validateSimulaPopolaDaElaborazione() {
		checkNotNullNorInvalidUid(model.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(), "accantonamento da copiare");
	}

	private String simulaPopolaDa(TipoImportazione tipoImportazione) {
		final String methodName = "simulaPopolaDa";
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto req = model.creaRequestSimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(tipoImportazione);
		logServiceRequest(req);
		
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendicontoResponse res = fondiDubbiaEsigibilitaService.simulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportRendiconto(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio per tipo " + tipoImportazione);
			addErrori(res);
			return INPUT;
		}
		
		model.setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp(res.extractByType(AccantonamentoFondiDubbiaEsigibilitaRendiconto.class));
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());

		return SUCCESS;
	}
	
	/**
	 * Estrazione degli accantonamenti dalle response
	 * @param res la reponse con i dati da popolare
	 */
	private void extractAccantonamenti(BaseAccantonamentoFondiDubbiaEsigibilitaResponse res) {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(res.extractByType(AccantonamentoFondiDubbiaEsigibilitaRendiconto.class));
	}

	@Override
	public String estraiInFoglioDiCalcolo() {
		return estraiInFoglioDiCalcolo(TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO);
	}

	public String estraiFoglioDiCalcoloCreditiStralciati() {
		return estraiInFoglioDiCalcoloSecondari(TipologiaEstrazioniFogliDiCalcolo.CREDITI_STRALCIATI);
	}

	public String estraiFoglioDiCalcoloAccertamentiAnniSuccessivi() {
		return estraiInFoglioDiCalcoloSecondari(TipologiaEstrazioniFogliDiCalcolo.ACCERTAMENTI_ANNI_SUCCESSIVI);
	}
	
	/**
	 * Classe di Result specifica per la simulazione del popolamento.
	 * @author interlogic
	 * @version 1.0.0 - 20/07/2021
	 */
	public static class SimulaPopolaDaJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = -2623614405784634055L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*,informazioni.*,accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.*,listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp.*";

		/** Empty default constructor */
		public SimulaPopolaDaJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}

}
