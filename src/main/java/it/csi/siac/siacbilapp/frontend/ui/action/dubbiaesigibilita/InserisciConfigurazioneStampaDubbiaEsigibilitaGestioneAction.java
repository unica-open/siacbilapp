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

import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.AggiornaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.EliminaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.EliminaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.RipristinaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione, Gestione
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciConfigurazioneStampaDubbiaEsigibilitaGestioneAction extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<InserisciConfigurazioneStampaDubbiaEsigibilitaGestioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1048555718689347461L;
	
	@Override
	public boolean isConsentitoTornaInBozza() {
		return isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_CONFSTPFCDE_GES_BOZZA);
	}
	
	@Override
	protected boolean checkCompatibilitaFaseBilancio(FaseBilancio faseBilancio) {
		return FaseBilancio.GESTIONE.equals(faseBilancio) || FaseBilancio.ASSESTAMENTO.equals(faseBilancio); //SIAC-8565
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
				TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE, StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
		logServiceRequest(req);
	
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.salvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String ricercaCapitoli() {
		caricaListaAccantonamentoFondi();
		return SUCCESS;
	}
	
	@Override
	public String confermaCapitoli() {
		List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaTemp = model.getListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp();
	
		for (CapitoloEntrataGestione c : model.getListaCapitoloEntrataGestione()) {
			addCapitoloIfNotPresent(c, new AccantonamentoFondiDubbiaEsigibilitaGestione(), listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		}
		
		model.setListaCapitoloEntrataGestione(new ArrayList<CapitoloEntrataGestione>());
		
		return SUCCESS;
	}
	
	//SIAC-7858 CM 08/06/2021 Inizio
	public String confermaCapitoliModale() {
		
		final String methodName = "confermaCapitoliModale";
		
		//vedi confermaCapitoli
		List<AccantonamentoFondiDubbiaEsigibilitaGestione> listaAccantonamentoFondiDubbiaEsigibilitaTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
		
		for (CapitoloEntrataGestione c : model.getListaCapitoloEntrataGestione()) {
			AccantonamentoFondiDubbiaEsigibilitaGestione afder = new AccantonamentoFondiDubbiaEsigibilitaGestione();
			afder.setCapitolo(c);
			listaAccantonamentoFondiDubbiaEsigibilitaTemp.add(afder);
		}
		
		model.setListaCapitoloEntrataGestione(new ArrayList<CapitoloEntrataGestione>());
		model.setListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp(listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		
		InserisceAccantonamentoFondiDubbiaEsigibilitaGestione req = model.creaRequestCopiaCapitoliModaleAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);
		
		InserisceAccantonamentoFondiDubbiaEsigibilitaGestioneResponse res = fondiDubbiaEsigibilitaService.inserisceAccantonamentoFondiDubbiaEsigibilitaGestione(req);
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
		model.setListaAccantonamentoFondiDubbiaEsigibilitaGestioneSelezionati(null);
	}
	
	@Override
	public String salvaCapitoli() {
		AggiornaAccantonamentoFondiDubbiaEsigibilitaGestione req = model.creaRequestAggiornaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		AggiornaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse res = fondiDubbiaEsigibilitaService.aggiornaAccantonamentoFondiDubbiaEsigibilitaGestione(req);
		logServiceResponse(res);
	
		// SIAC-8356 warning in caso di lista vuota al salvataggio
		if(Esito.FALLIMENTO.equals(res.getEsito()) && CollectionUtils.isEmpty(req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione())) {
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

		EliminaAccantonamentoFondiDubbiaEsigibilitaGestione req = model.creaRequestEliminaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		EliminaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse res = fondiDubbiaEsigibilitaService.eliminaAccantonamentoFondiDubbiaEsigibilitaGestione(req);
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

		RipristinaAccantonamentoFondiDubbiaEsigibilitaGestione req = model.creaRequestRipristinaAccantonamentoFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		RipristinaAccantonamentoFondiDubbiaEsigibilitaGestioneResponse res = fondiDubbiaEsigibilitaService.ripristinaAccantonamentoFondiDubbiaGestioneEsigibilita(req);
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
		SalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestSalvaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioNuovo(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
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
		model.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>());
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
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione req = model.creaRequestSimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImport(tipoImportazione);
		logServiceRequest(req);
		
		SimulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse res = fondiDubbiaEsigibilitaService.simulaInserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio per tipo " + tipoImportazione);
			addErrori(res);
			return INPUT;
		}
		
		model.setListaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp(res.extractByType(AccantonamentoFondiDubbiaEsigibilitaGestione.class));
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());

		return SUCCESS;
	}
	
	/**
	 * Estrazione degli accantonamenti dalle response
	 * @param res la reponse con i dati da popolare
	 */
	private void extractAccantonamenti(BaseAccantonamentoFondiDubbiaEsigibilitaResponse res) {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaGestione(res.extractByType(AccantonamentoFondiDubbiaEsigibilitaGestione.class));
	}

	@Override
	public String estraiInFoglioDiCalcolo() {
		return estraiInFoglioDiCalcolo(TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE);
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
		private static final String INCLUDE_PROPERTIES = "errori.*,informazioni.*,accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.*,listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp.*";

		/** Empty default constructor */
		public SimulaPopolaDaJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}

}
