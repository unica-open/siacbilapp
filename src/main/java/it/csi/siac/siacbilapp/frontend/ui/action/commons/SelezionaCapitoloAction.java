/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.json.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.SelezionaCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.handler.DefaultServiceErrorHandler;
import it.csi.siac.siacbilapp.frontend.ui.util.handler.FondiDubbiaEsigibilitaDatoNonPresenteServiceErrorHandler;
import it.csi.siac.siacbilapp.frontend.ui.util.handler.FondiDubbiaEsigibilitaRendicontoDatoNonPresenteServiceErrorHandler;
import it.csi.siac.siacbilapp.frontend.ui.util.handler.ServiceErrorHandler;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la selezione del Capitolo sul modale.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 09/04/2014 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SelezionaCapitoloAction extends CapitoloEntrataAction<SelezionaCapitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3239926375169556435L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		List<TipoFinanziamento> listaTipoFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		model.setListaTipoFinanziamento(listaTipoFinanziamento);
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Metodo di selezione del Capitolo di Entrata Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * 
	 * @throws IllegalArgumentException nel caso in cui la creazione della Request fallisca
	 */
	public String entrataGestione() {
		final String methodName = "entrataGestione";
		
		if(!validaSelezionaCapitolo()) {
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		innerCapitoloEntrataGestione(null, new DefaultServiceErrorHandler());
		return SUCCESS;
	}
	
	/**
	 * Metodo di selezione del Capitolo di Entrata Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * 
	 * @throws IllegalArgumentException nel caso in cui la creazione della Request fallisca
	 */
	public String entrataPrevisione() {
		final String methodName = "entrataPrevisione";
		if(!validaSelezionaCapitolo()) {
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		innerCapitoloEntrataPrevisione(null, new DefaultServiceErrorHandler());
		return SUCCESS;
	}
	
	/**
	 * Metodo di selezione del Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * 
	 * @throws IllegalArgumentException nel caso in cui la creazione della Request fallisca
	 */
	public String uscitaGestione() {
		final String methodName="uscitaGestione";
		
		if(!validaSelezionaCapitolo()) {
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		
		RicercaSinteticaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaCapitoloUscitaGestione(null);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
			
		// Imposto i parametri in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_USCITA_GESTIONE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_USCITA_GESTIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);

		return SUCCESS;
	}

	/**
	 * Metodo di selezione del Capitolo di Uscita Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * 
	 * @throws IllegalArgumentException nel caso in cui la creazione della Request fallisca
	 */
	public String uscitaPrevisione() {
		final String methodName="uscitaPrevisione";
		
		if(!validaSelezionaCapitolo()) {
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		
		RicercaSinteticaCapitoloUscitaPrevisione request = model.creaRequestRicercaSinteticaCapitoloUscitaPrevisione(null);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(request);
		logServiceResponse(response);
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		// Imposto i parametri in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_USCITA_PREVISIONE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_USCITA_PREVISIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}

	/**
	 * Validazione del form.
	 * @return true se non vi sono degli errori
	 */
	private boolean validaSelezionaCapitolo() {
		final String methodName = "validaSelezionaCapitolo";
		try {
			innerValidate();
		} catch (ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione: " + pve.getMessage());
		}
		return !hasErrori();
	}
	
	
	/**
	 * Metodo di selezione del Capitolo di Entrata Previsione per fondi di dubbia esigibilit&agrave;.
	 * @return la String corrispondente al risultato della Action
	 */
	public String capitoloEntrataPrevisioneFondiDubbiaEsigibilita() {
		return innerCapitoloEntrataPrevisione(EnumSet.noneOf(ImportiCapitoloEnum.class), new FondiDubbiaEsigibilitaDatoNonPresenteServiceErrorHandler());
	}
	
	/**
	 * Validazione per il metodo {@link #capitoloEntrataPrevisioneFondiDubbiaEsigibilita()}
	 */
	public void validateCapitoloEntrataPrevisioneFondiDubbiaEsigibilita() {
		innerValidate();
	}
	
	/**
	 * Metodo di selezione del Capitolo di Entrata Gestione per fondi di dubbia esigibilit&agrave;.
	 * @return la String corrispondente al risultato della Action
	 */
	public String capitoloEntrataGestioneFondiDubbiaEsigibilita() {
		return innerCapitoloEntrataGestione(EnumSet.noneOf(ImportiCapitoloEnum.class), new FondiDubbiaEsigibilitaRendicontoDatoNonPresenteServiceErrorHandler());
	}
	
	/**
	 * Validazione per il metodo {@link #capitoloEntrataGestioneFondiDubbiaEsigibilita()}
	 */
	public void validateCapitoloEntrataGestioneFondiDubbiaEsigibilita() {
		innerValidate();
	}
	
	
	
	
	
	
	/**
	 * Metodo interno di selezione del Capitolo di Entrata Previsione
	 * @param importiCapitolo gli importi
	 * @param serviceErrorHandler l'handler per gli errori del servizio
	 * @return la String corrispondente al risultato della Action
	 */
	private String innerCapitoloEntrataPrevisione(Set<ImportiCapitoloEnum> importiCapitolo, ServiceErrorHandler serviceErrorHandler) {
		final String methodName = "innerCapitoloEntrataPrevisione";
		
		RicercaSinteticaCapitoloEntrataPrevisione request = model.creaRequestRicercaSinteticaCapitoloEntrataPrevisione(importiCapitolo);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaSinteticaCapitoloEntrataPrevisione(request);
		logServiceResponse(response);
		 
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			serviceErrorHandler.handleError(this, response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Imposto i parametri in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_ENTRATA_PREVISIONE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_ENTRATA_PREVISIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo interno di selezione del Capitolo di Entrata Gestione
	 * @param importiCapitolo gli importi
	 * @param serviceErrorHandler l'handler per gli errori del servizio
	 * @return la String corrispondente al risultato della Action
	 */
	private String innerCapitoloEntrataGestione(Set<ImportiCapitoloEnum> importiCapitolo, ServiceErrorHandler serviceErrorHandler) {
		final String methodName = "innerCapitoloEntrataGestione";
		
		RicercaSinteticaCapitoloEntrataGestione request = model.creaRequestRicercaSinteticaCapitoloEntrataGestione(importiCapitolo);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		logServiceResponse(response);
		 
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			serviceErrorHandler.handleError(this, response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Imposto i parametri in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_ENTRATA_GESTIONE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_ENTRATA_GESTIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo interno di validazione comune
	 */
	private void innerValidate() {
		checkCondition(model.getCapitolo() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"), true);
		checkCondition(model.getCapitolo().getAnnoCapitolo() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), true);
		
		List<TipoFinanziamento> listaTipoFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		// SIAC-4470
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TipologiaTitolo> listaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO);
		List<ElementoPianoDeiConti> listaElementoPianoDeiConti = sessionHandler.getParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI);
		
		model.setTipoFinanziamento(ComparatorUtils.searchByUid(listaTipoFinanziamento, model.getTipoFinanziamento()));
		model.setStrutturaAmministrativoContabile(ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, model.getStrutturaAmministrativoContabile()));
		
		model.setTitoloEntrata(ComparatorUtils.searchByUid(listaTitoloEntrata, model.getTitoloEntrata()));
		model.setTipologiaTitolo(ComparatorUtils.searchByUid(listaTipologiaTitolo, model.getTipologiaTitolo()));
		model.setCategoriaTipologiaTitolo(ComparatorUtils.searchByUid(listaCategoriaTipologiaTitolo, model.getCategoriaTipologiaTitolo()));
		model.setElementoPianoDeiConti(ComparatorUtils.searchByUidWithChildren(listaElementoPianoDeiConti, model.getElementoPianoDeiConti()));
	}

	/**
	 * Result per la selezione del capitolo con la gestione tipo-CustomJSON
	 * @author Marchino Alessandro
	 */
	public static class SelezionaCapitoloCustomJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 6329785687375729990L;
		private static final String INCLUDE_PROPERTIES = "listaCapitoli.*";
		/** Costruttore vuoto di default */
		public SelezionaCapitoloCustomJSONResult() {
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	/**
	 * Result per la selezione del capitolo con la gestione JSON canonica
	 * @author Marchino Alessandro
	 */
	public static class SelezionaCapitoloJSONResult extends JSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = -5420254649562843875L;
		private static final String INCLUDE_PROPERTIES = "errori.*, listaCapitoli.*";
		/** Costruttore vuoto di default */
		public SelezionaCapitoloJSONResult() {
			setIgnoreHierarchy(false);
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
