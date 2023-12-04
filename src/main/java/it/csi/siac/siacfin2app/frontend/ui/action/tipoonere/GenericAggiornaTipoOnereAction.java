/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.tipoonere.AggiornaTipoOnereModel;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Classe generica di Action per l'aggiornamento del tipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericAggiornaTipoOnereAction.MODEL_SESSION_NAME)
public class GenericAggiornaTipoOnereAction extends GenericTipoOnereAction<AggiornaTipoOnereModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5805539134939619551L;

	/** Nome del model in sessione */
	protected static final String MODEL_SESSION_NAME = "AggiornaTipoOnere";
	
	/** Serviz&icirc; del capitolo di entrata gestione */
	@Autowired protected transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	/** Serviz&icirc; del capitolo di uscita gestione */
	@Autowired protected transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	
	@Override
	public void prepare() throws Exception {
		// Pulisco il model
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Inizializzazione della action
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Carica la liste delle classi Soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(ListeGestioneSoggetto.class, response));
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}

	/**
	 * Ottiene le liste delle causali.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaCausali() {
		// Placeholder per la chiamata
		return SUCCESS;
	}
	
	/**
	 * Controlla il soggetto.
	 * 
	 * @param soggetto il soggetto da controllare
	 * 
	 * @return il soggetto caricato da servizio
	 * 
	 * @throws WebServiceInvocationFailureException nwl caso di errore nell'invocazione del servizio 
	 */
	protected Soggetto checkSoggetto(Soggetto soggetto) throws WebServiceInvocationFailureException {
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			// Se non ho il soggetto, non faccio nulla
			return null;
		}
		if(soggetto.getUid() != 0) {
			// Ho gia' caricato il dato: esco
			return soggetto;
		}
		
		// Provo a vedere se ho il dato in sessione. In tal caso, lo leggo
		Soggetto s = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		if(s == null || !soggetto.getCodiceSoggetto().equals(s.getCodiceSoggetto())) {
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			}
			if(response.getSoggetto() == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", request.getParametroSoggettoK().getCodice()));
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			}
			s = response.getSoggetto();
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, s);
		}
		
		checkCondition(!StatoOperativoAnagrafica.ANNULLATO.equals(s.getStatoOperativo()), ErroreFin.SOGGETTO_ANNULLATO.getErrore(), true);
		checkCondition(!StatoOperativoAnagrafica.BLOCCATO.equals(s.getStatoOperativo()), ErroreFin.SOGGETTO_BLOCCATO.getErrore(), true);
		
		return s;
	}
	
	/**
	 * Controlla la coerenza tra soggetto e movimento di gestione.
	 * 
	 * @param s il soggetto
	 * @param movimentoGestione l'accertamento
	 */
	protected void checkCoerenzaSoggetto(Soggetto s, MovimentoGestione movimentoGestione) {
		final String methodName = "checkCoerenzaSoggetto";
		if(s == null) {
			// Se non ho il soggetto, esco
			return;
		}
		Soggetto soggettoMovimento = movimentoGestione.getSoggetto();
		checkCondition(soggettoMovimento == null || soggettoMovimento.getUid() == s.getUid(),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il soggetto del movimento e del tipo onere devono coincidere"), true);
		
		ClasseSoggetto classeSoggetto = movimentoGestione.getClasseSoggetto();
		if(classeSoggetto == null) {
			// Dovrebbe andare bene?
			return;
		}
		for(ClassificazioneSoggetto cs : s.getElencoClass()) {
			if(cs.getSoggettoClasseCode().equalsIgnoreCase(classeSoggetto.getCodice())) {
				log.debug(methodName, "ho trovato un codice classe corrispondente: " + cs.getSoggettoClasseCode());
				return;
			}
		}
		checkCondition(false, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il movimento " + movimentoGestione.getNumeroBigDecimal()
				+ " non appartiene alla classificazione del soggetto del tipo onere"), true);
	}
	
	/**
	 * Controlla il capitolo di entrata.
	 * 
	 * @param capitoloEntrataGestione il capitolo da controllare
	 * 
	 * @return il capitolo caricato da servizio
	 * 
	 * @throws WebServiceInvocationFailureException nwl caso di errore nell'invocazione del servizio
	 */
	protected CapitoloEntrataGestione checkCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) throws WebServiceInvocationFailureException {
		String methodName = "checkCapitoloEntrataGestione";
		// Se non ho il capitolo, non faccio nulla
		if(capitoloEntrataGestione == null
				|| capitoloEntrataGestione.getAnnoCapitolo() == null
				|| capitoloEntrataGestione.getNumeroCapitolo() == null
				|| capitoloEntrataGestione.getNumeroArticolo() == null) {
			return null;
		}
		if(capitoloEntrataGestione.getUid() != 0) {
			log.debug(methodName, "ho già l'uid, non faccio la ricerca del capitolo");
			// Ho gia' caricato il dato: esco
			return capitoloEntrataGestione;
		}
		RicercaPuntualeCapitoloEntrataGestione request = model.creaRequestRicercaPuntualeCapitoloEntrataGestione();
		logServiceRequest(request);
		RicercaPuntualeCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaPuntualeCapitoloEntrataGestione.class, response));
		}
		
		return response.getCapitoloEntrataGestione();
	}
	
	/**
	 * Controlla il capitolo di uscita.
	 * 
	 * @param capitoloUscitaGestione il capitolo da controllare
	 * 
	 * @return il capitolo caricato da servizio
	 * 
	 * @throws WebServiceInvocationFailureException nwl caso di errore nell'invocazione del servizio
	 */
	protected CapitoloUscitaGestione checkCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) throws WebServiceInvocationFailureException {
		// Se non ho il capitolo, non faccio nulla
		if(capitoloUscitaGestione == null
				|| capitoloUscitaGestione.getAnnoCapitolo() == null
				|| capitoloUscitaGestione.getNumeroCapitolo() == null
				|| capitoloUscitaGestione.getNumeroArticolo() == null) {
			return null;
		}
		if(capitoloUscitaGestione.getUid() != 0) {
			// Ho gia' caricato il dato: esco
			return capitoloUscitaGestione;
		}
		RicercaPuntualeCapitoloUscitaGestione request = model.creaRequestRicercaPuntualeCapitoloUscitaGestione();
		logServiceRequest(request);
		RicercaPuntualeCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaPuntualeCapitoloUscitaGestione.class, response));
		}
		
		return response.getCapitoloUscitaGestione();
	}
	
}
