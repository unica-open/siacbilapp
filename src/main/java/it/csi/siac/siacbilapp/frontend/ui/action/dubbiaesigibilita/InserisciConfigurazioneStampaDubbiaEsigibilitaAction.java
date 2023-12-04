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

import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.AggiornaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.AggiornaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.EliminaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.EliminaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.InserisceAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.InserisceAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.RipristinaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * Classe di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * 
 * @author Alessio Romanato
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/10/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciConfigurazioneStampaDubbiaEsigibilitaAction extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<InserisciConfigurazioneStampaDubbiaEsigibilitaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1048555718689347461L;
	
	@Override
	public boolean isConsentitoTornaInBozza() {
		return Boolean.TRUE;
	}
	
	@Override
	protected boolean checkCompatibilitaFaseBilancio(FaseBilancio faseBilancio) {
		//SIAC-8565 e SIAC-8572
		return FaseBilancio.PREVISIONE.equals(faseBilancio) || FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) || FaseBilancio.GESTIONE.equals(faseBilancio); 
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

	public String salvaAttributi() {
		final String methodName = "salvaAttributi";
		
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
				TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE, StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
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
		List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp = model.getListaAccantonamentoFondiDubbiaEsigibilitaTemp();
	
		for (CapitoloEntrataPrevisione c : model.getListaCapitoloEntrataPrevisione()) {
			addCapitoloIfNotPresent(c, new AccantonamentoFondiDubbiaEsigibilita(), listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		}
		
		model.setListaCapitoloEntrataPrevisione(new ArrayList<CapitoloEntrataPrevisione>());
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #salvaCapitoli()}
	 */
	public void prepareSalvaCapitoli() {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaSelezionati(null);
	}
	
	@Override
	public String salvaCapitoli() {

		AggiornaAccantonamentoFondiDubbiaEsigibilita req = model.creaRequestAggiornaAccantonamentoFondiDubbiaEsigibilita();
		
		logServiceRequest(req);
	
		AggiornaAccantonamentoFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.aggiornaAccantonamentoFondiDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		// SIAC-8356 warning in caso di lista vuota al salvataggio
		if(Esito.FALLIMENTO.equals(res.getEsito()) && CollectionUtils.isEmpty(req.getListaAccantonamentoFondiDubbiaEsigibilita())) {
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
	public String ripristinaAccantonamento() {

		final String methodName = "ripristinaAccantonamento";

		RipristinaAccantonamentoFondiDubbiaEsigibilita req = model.creaRequestRipristinaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		RipristinaAccantonamentoFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.ripristinaAccantonamentoFondiDubbiaEsigibilita(req);
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
	public String eliminaAccantonamento() {

		final String methodName = "eliminaAccantonamento";

		EliminaAccantonamentoFondiDubbiaEsigibilita req = model.creaRequestEliminaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		EliminaAccantonamentoFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.eliminaAccantonamentoFondiDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		extractAccantonamenti(res);

		return SUCCESS;
	}

	/**
	 * Annullamento dei dati in pagina
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaSelezionati(null);
		model.setListaAccantonamentoFondiDubbiaEsigibilitaTemp(null);
		return SUCCESS;
	}
	
//	//SIAC-7858 CM 04/06/2021 Inizio
//	public String modificaStatoInDefinitivo() {
//		return modificaStato(StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA);
//	}
//	public String modificaStatoInBozza() {
//		return modificaStato(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
//	}
//	//SIAC-7858 CM 04/06/2021 Fine
//
//	private String modificaStato(StatoAccantonamentoFondiDubbiaEsigibilita stato) {
//		final String methodName = "modificaStato";
//		
//		ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(stato);
//		logServiceRequest(req);
//		
//		ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.modificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
//		logServiceResponse(res);
//	
//		// Controllo gli errori
//		if(res.hasErrori()) {
//			//si sono verificati degli errori: esco.
//			addErrori(res);
//			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio di modifica stato in " + stato);
//			return INPUT;
//		}
//		
//		return SUCCESS;
//	}
	
	//SIAC-7858 CM 08/06/2021 Inizio
	
	public String confermaCapitoliModale() {
		
		final String methodName = "confermaCapitoliModale";
		
		//vedi confermaCapitoli
		List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
		
		for (CapitoloEntrataPrevisione c : model.getListaCapitoloEntrataPrevisione()) {
			AccantonamentoFondiDubbiaEsigibilita afde = new AccantonamentoFondiDubbiaEsigibilita();
			afde.setCapitolo(c);
			listaAccantonamentoFondiDubbiaEsigibilitaTemp.add(afde);
		}
		
		model.setListaCapitoloEntrataPrevisione(new ArrayList<CapitoloEntrataPrevisione>());
		model.setListaAccantonamentoFondiDubbiaEsigibilitaTemp(listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		
		InserisceAccantonamentoFondiDubbiaEsigibilita req = model.creaRequestCopiaCapitoliModaleAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);
		
		InserisceAccantonamentoFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.inserisceAccantonamentoFondiDubbiaEsigibilita(req);
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
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport req = model.creaRequestSimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(tipoImportazione);
		logServiceRequest(req);
		
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportResponse res = fondiDubbiaEsigibilitaService.simulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio per tipo " + tipoImportazione);
			addErrori(res);
			return INPUT;
		}
		
		model.setListaAccantonamentoFondiDubbiaEsigibilitaTemp(res.extractByType(AccantonamentoFondiDubbiaEsigibilita.class));
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());

		return SUCCESS;
	}
	
	public String nuovaVersione() {
		final String methodName = "nuovaVersione";
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioNuovo(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
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
		model.setListaAccantonamentoFondiDubbiaEsigibilita(new ArrayList<AccantonamentoFondiDubbiaEsigibilita>());
		return SUCCESS;
	}
	
	@Override
	public String estraiInFoglioDiCalcolo() {
		return estraiInFoglioDiCalcolo(TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE);
	}
	
	/**
	 * Estrazione degli accantonamenti dalle response
	 * @param res la reponse con i dati da popolare
	 */
	private void extractAccantonamenti(BaseAccantonamentoFondiDubbiaEsigibilitaResponse res) {
		model.setListaAccantonamentoFondiDubbiaEsigibilita(res.extractByType(AccantonamentoFondiDubbiaEsigibilita.class));
	}
	
	/**
	 * Preparazione per {@link #contaAccantonamentiDefinitivi()}
	 */
	public void prepareContaAccantonamentiDefinitivi() {
		model.setNumeroAccantonamentiDefinitivi(null);
	}
	
	/**
	 * Conto degli accantonamenti definitivi
	 * @return il risultato dell'invocazione
	 */
	public String contaAccantonamentiDefinitivi() {
		final String methodName = "contaAccantonamentiDefinitivi";
		RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestRicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDefinitivi();
		logServiceRequest(req);
		RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.ricercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
		if(res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Elementi trovati: " + res.getTotaleElementi());
		model.setNumeroAccantonamentiDefinitivi(res.getTotaleElementi());
		return SUCCESS;
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
		private static final String INCLUDE_PROPERTIES = "errori.*,informazioni.*,accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.*,listaAccantonamentoFondiDubbiaEsigibilitaTemp.*";

		/** Empty default constructor */
		public SimulaPopolaDaJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
