/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conciliazione;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione.GestioneConciliazionePerTitoloModel;

/**
 * Classe di action per la gestione della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneConciliazionePerTitoloAction extends GenericBilancioAction<GestioneConciliazionePerTitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6336488287181055699L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ConciliazioneService conciliazioneService;
	@Autowired private transient ContoService contoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento delle liste, rigorosamente dalla sessione
		caricaListaClasseDiConciliazione();
		caricaListaTitoloSpesaDaSessione();
		caricaListaMacroaggregatoDaSessione();
		caricaListaTitoloEntrataDaSessione();
		caricaListaTipologiaTitoloDaSessione();
		caricaListaCategoriaTipologiaTitoloDaSessione();
		caricaListaClassePianoDaSessione();
	}
	
	/**
	 * Caricamento della lista delle classi di conciliazione.
	 */
	private void caricaListaClasseDiConciliazione() {
		//CR-4697 la classe di conciliazione CONTI e' un wrapper per tutti i conti, non devo poter associare a lei dei conti
		List<ClasseDiConciliazione> listaClassiDiConciliazione = new ArrayList<ClasseDiConciliazione>();
		for (ClasseDiConciliazione classeDiConciliazione : ClasseDiConciliazione.values()) {
			if(ClasseDiConciliazione.CONTI.equals(classeDiConciliazione)){
				continue;
			}
			listaClassiDiConciliazione.add(classeDiConciliazione);
		}
		model.setListaClasseDiConciliazione(listaClassiDiConciliazione);
	}

	/**
	 * Caricamento della lista del titolo di spesa da sessione.
	 */
	private void caricaListaTitoloSpesaDaSessione() {
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		model.setListaTitoloSpesa(listaTitoloSpesa);
	}

	/**
	 * Caricamento della lista del macroaggregato da sessione.
	 */
	private void caricaListaMacroaggregatoDaSessione() {
		List<Macroaggregato> listaMacroaggregato = sessionHandler.getParametro(BilSessionParameter.LISTA_MACROAGGREGATO);
		model.setListaMacroaggregato(listaMacroaggregato);
	}

	/**
	 * Caricamento della lista del titolo di entrata da sessione.
	 */
	private void caricaListaTitoloEntrataDaSessione() {
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		model.setListaTitoloEntrata(listaTitoloEntrata);
	}

	/**
	 * Caricamento della lista della tipologia titolo da sessione.
	 */
	private void caricaListaTipologiaTitoloDaSessione() {
		List<TipologiaTitolo> listaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		model.setListaTipologiaTitolo(listaTipologiaTitolo);
	}

	/**
	 * Caricamento della lista della categoria tipologia titolo da sessione.
	 */
	private void caricaListaCategoriaTipologiaTitoloDaSessione() {
		List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO);
		model.setListaCategoriaTipologiaTitolo(listaCategoriaTipologiaTitolo);
	}

	/**
	 * Caricamento della lista della classe piano da sessione.
	 */
	private void caricaListaClassePianoDaSessione() {
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GSA);
		model.setListaClassePiano(listaClassePiano);
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile();
		try {
			caricaListaTitoloSpesa();
			caricaListaMacroaggregato();
			caricaListaTitoloEntrata();
			caricaListaTipologiaTitolo();
			caricaListaCategoriaTipologiaTitolo();
			caricaListaClassePiano();
		} catch(WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException("Impossibile caricare le liste per la conciliazione: " + wsife.getMessage(), wsife);
		}
		return SUCCESS;
	}

	@Override
	protected void checkCasoDUsoApplicabile() {
		final String methodName = "checkCasoDUsoApplicabile";
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.warn(methodName, "Errori nel caricamento del bilancio");
			addErrori(response);
			throw new GenericFrontEndMessagesException(createErrorInServiceInvocationString(request, response));
		}
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
		model.setFaseBilancio(faseBilancio);
	}

	/**
	 * Caricamento della lista del titolo di spesa.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio.
	 */
	private void caricaListaTitoloSpesa() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTitoloSpesa";
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		if(listaTitoloSpesa == null) {
			LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			logServiceRequest(request);
			LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, msg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(msg);
			}
			listaTitoloSpesa = response.getClassificatoriTitoloSpesa();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, listaTitoloSpesa);
		}
		
		model.setListaTitoloSpesa(listaTitoloSpesa);
	}

	/**
	 * Caricamento della lista del macroaggregato.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio.
	 */
	private void caricaListaMacroaggregato() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaMacroaggregato";
		List<Macroaggregato> listaMacroaggregato = sessionHandler.getParametro(BilSessionParameter.LISTA_MACROAGGREGATO);
		if(listaMacroaggregato == null && model.getTitoloSpesa() != null && model.getTitoloSpesa().getUid() != 0) {
			LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(model.getTitoloSpesa().getUid());
			logServiceRequest(request);
			LeggiClassificatoriBilByIdPadreResponse response = classificatoreBilService.leggiClassificatoriByIdPadre(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, msg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(msg);
			}
			listaMacroaggregato = response.getClassificatoriMacroaggregato();
			sessionHandler.setParametro(BilSessionParameter.LISTA_MACROAGGREGATO, listaMacroaggregato);
		}
		model.setListaMacroaggregato(listaMacroaggregato);
	}

	/**
	 * Caricamento della lista del titolo di entrata.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio.
	 */
	private void caricaListaTitoloEntrata() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTitoloEntrata";
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		if(listaTitoloEntrata == null) {
			LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
			logServiceRequest(request);
			LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, msg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(msg);
			}
			listaTitoloEntrata = response.getClassificatoriTitoloEntrata();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, listaTitoloEntrata);
		}
		model.setListaTitoloEntrata(listaTitoloEntrata);
	}

	/**
	 * Caricamento della lista della tipologia titolo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio.
	 */
	private void caricaListaTipologiaTitolo() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipologiaTitolo";
		List<TipologiaTitolo> listaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		if(listaTipologiaTitolo == null && model.getTitoloEntrata() != null && model.getTitoloEntrata().getUid() != 0) {
			LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(model.getTitoloEntrata().getUid());
			logServiceRequest(request);
			LeggiClassificatoriBilByIdPadreResponse response = classificatoreBilService.leggiClassificatoriByIdPadre(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, msg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(msg);
			}
			listaTipologiaTitolo = response.getClassificatoriTipologiaTitolo();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO, listaTipologiaTitolo);
		}
		model.setListaTipologiaTitolo(listaTipologiaTitolo);
	}

	/**
	 * Caricamento della lista della categoria tipologia titolo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio.
	 */
	private void caricaListaCategoriaTipologiaTitolo() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCategoriaTipologiaTitolo";
		List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO);
		if(listaCategoriaTipologiaTitolo == null && model.getTitoloEntrata() != null && model.getTitoloEntrata().getUid() != 0) {
			LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(model.getTitoloEntrata().getUid());
			logServiceRequest(request);
			LeggiClassificatoriBilByIdPadreResponse response = classificatoreBilService.leggiClassificatoriByIdPadre(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, msg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(msg);
			}
			listaCategoriaTipologiaTitolo = response.getClassificatoriCategoriaTipologiaTitolo();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO, listaCategoriaTipologiaTitolo);
		}
		model.setListaCategoriaTipologiaTitolo(listaCategoriaTipologiaTitolo);
	}

	/**
	 * Caricamento della lista della classe piano.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio.
	 */
	private void caricaListaClassePiano() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaClassePiano";
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GSA);
		if(listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche("ClassePiano_" + Ambito.AMBITO_GSA.getSuffix());
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, errorMsg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GSA, listaClassePiano);
		}
		model.setListaClassePiano(listaClassePiano);
	}
	
	/**
	 * Ricerca sintetica della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSintetica() {
		final String methodName = "ricercaSintetica";
		
		RicercaSinteticaConciliazionePerTitolo request = model.creaRequestRicercaSinteticaConciliazionePerTitolo();
		logServiceRequest(request);
		RicercaSinteticaConciliazionePerTitoloResponse response = conciliazioneService.ricercaSinteticaConciliazionePerTitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		
		popolaClassificatoriDaSessione();
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CONCILIAZIONI_PER_TITOLO, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CONCILIAZIONI_PER_TITOLO, response.getConciliazioni());
		
		return SUCCESS;
	}
	
	/**
	 * Popolamento dei classificatori dalla sessione.
	 */
	private void popolaClassificatoriDaSessione() {
		TitoloSpesa titoloSpesaRicerca = ComparatorUtils.searchByUidEventuallyNull(model.getListaTitoloSpesa(), model.getTitoloSpesaRicerca());
		Macroaggregato macroaggregatoRicerca = ComparatorUtils.searchByUidEventuallyNull(model.getListaMacroaggregato(), model.getMacroaggregatoRicerca());
		TitoloEntrata titoloEntrataRicerca = ComparatorUtils.searchByUidEventuallyNull(model.getListaTitoloEntrata(), model.getTitoloEntrataRicerca());
		TipologiaTitolo tipologiaTitoloRicerca = ComparatorUtils.searchByUidEventuallyNull(model.getListaTipologiaTitolo(), model.getTipologiaTitoloRicerca());
		CategoriaTipologiaTitolo categoriaTipologiaTitoloRicerca = ComparatorUtils.searchByUidEventuallyNull(model.getListaCategoriaTipologiaTitolo(), model.getCategoriaTipologiaTitoloRicerca());
		
		model.setTitoloSpesaRicerca(titoloSpesaRicerca);
		model.setMacroaggregatoRicerca(macroaggregatoRicerca);
		model.setTitoloEntrataRicerca(titoloEntrataRicerca);
		model.setTipologiaTitoloRicerca(tipologiaTitoloRicerca);
		model.setCategoriaTipologiaTitoloRicerca(categoriaTipologiaTitoloRicerca);
	}

	/**
	 * Validazione per il metodo {@link #ricercaSintetica()}.
	 */
	public void validateRicercaSintetica() {
		checkNotNull(model.getClasseDiConciliazione(), "Classe di conciliazione");
		
		checkNotNullNorEmpty(model.getEntrataSpesaRicerca(), "Tipo");
		checkCondition(model.getEntrataSpesaRicerca() == null || model.isEntrata(model.getEntrataSpesaRicerca()) || model.isSpesa(model.getEntrataSpesaRicerca()),
				ErroreCore.VALORE_NON_VALIDO.getErrore("Tipo", ": sono accettati solo i valori ENTRATA o USCITA"), true);
		
		if(model.isEntrata(model.getEntrataSpesaRicerca())) {
			// Controlli per l'entrata
			checkNotNullNorInvalidUid(model.getTitoloEntrataRicerca(), "Titolo entrata");
			checkNotNullNorInvalidUid(model.getTipologiaTitoloRicerca(), "Tipologia titolo");
		} else if(model.isSpesa(model.getEntrataSpesaRicerca())) {
			// Controlli per la spesa
			checkNotNullNorInvalidUid(model.getTitoloSpesaRicerca(), "Titolo spesa");
		}
	}

	/**
	 * Eliminazione della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		EliminaConciliazionePerTitolo request = model.creaRequestEliminaConciliazionePerTitolo();
		logServiceRequest(request);
		EliminaConciliazionePerTitoloResponse response = conciliazioneService.eliminaConciliazionePerTitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Eliminata conciliazione con uid " + model.getConciliazionePerTitolo().getUid());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #elimina()}.
	 */
	public void validateElimina() {
		checkNotNullNorInvalidUid(model.getConciliazionePerTitolo(), "conciliazione");
	}

	/**
	 * Ricerca di dettaglio della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		RicercaDettaglioConciliazionePerTitolo request = model.creaRequestRicercaDettaglioConciliazionePerTitolo();
		logServiceRequest(request);
		RicercaDettaglioConciliazionePerTitoloResponse response = conciliazioneService.ricercaDettaglioConciliazionePerTitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Ricercata conciliazione con uid " + model.getConciliazionePerTitolo().getUid());
		model.setConciliazionePerTitolo(response.getConciliazionePerTitolo());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaDettaglio()}.
	 */
	public void validateRicercaDettaglio() {
		checkNotNullNorInvalidUid(model.getConciliazionePerTitolo(), "conciliazione");
	}

	/**
	 * Inserimento della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisci() {
		final String methodName = "inserisci";
		InserisceConciliazionePerTitolo request = model.creaRequestInserisceConciliazionePerTitolo();
		logServiceRequest(request);
		InserisceConciliazionePerTitoloResponse response = conciliazioneService.inserisceConciliazionePerTitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Creata conciliazione con uid " + response.getConciliazionePerTitolo().getUid());
		model.setConciliazionePerTitolo(response.getConciliazionePerTitolo());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #inserisci()}.
	 */
	public void validateInserisci() {
		checkNotNull(model.getConciliazionePerTitolo(), "conciliazione", true);
		validazioneCampiBaseConciliazione();
	}

	/**
	 * Validazione dei campi di base per la conciliazione
	 */
	private void validazioneCampiBaseConciliazione() {
		checkNotNull(model.getConciliazionePerTitolo().getClasseDiConciliazione(), "classe di conciliazione");
		checkNotNullNorEmpty(model.getEntrataSpesa(), "Tipo");
		if("E".equals(model.getEntrataSpesa())) {
			checkNotNullNorInvalidUid(model.getTitoloEntrata(), "Titolo");
			checkNotNullNorInvalidUid(model.getTipologiaTitolo(), "Tipologia");
			checkNotNullNorInvalidUid(model.getCategoriaTipologiaTitolo(), "Categoria");
		} else if("S".equals(model.getEntrataSpesa())) {
			checkNotNullNorInvalidUid(model.getTitoloSpesa(), "Titolo");
			checkNotNullNorInvalidUid(model.getMacroaggregato(), "Macroaggregato");
		}
		checkNotNullNorInvalidUid(model.getClassePiano(), "Classe");
		checkCondition(model.getConciliazionePerTitolo().getConto() != null && StringUtils.isNotBlank(model.getConciliazionePerTitolo().getConto().getCodice()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto"));
		
		checkConto();
	}

	/**
	 * Controllo per i conti
	 */
	private void checkConto() {
		final String methodName = "checkConto";
		if(hasErrori()) {
			log.debug(methodName, "Salto la validazione del conto: ho gia' degli errori");
			return;
		}
		
		RicercaSinteticaConto request = model.creaRequestRicercaSinteticaConto();
		logServiceRequest(request);
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return;
		}
		log.debug(methodName, "Numero conti trovati per codice " + model.getConciliazionePerTitolo().getConto().getCodice() + ": " + response.getTotaleElementi());
		String strConto = "Il conto " + model.getConciliazionePerTitolo().getConto().getCodice();
		checkCondition(response.getTotaleElementi() > 0, ErroreCore.ENTITA_NON_COMPLETA.getErrore(strConto, "non ha un'istanza valida nell'anno di bilancio in corso"), true);
		checkCondition(response.getTotaleElementi() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Conto"), true);
		
		Conto conto = response.getConti().get(0);
		checkCondition(Boolean.TRUE.equals(conto.getAttivo()), ErroreCore.ENTITA_NON_COMPLETA.getErrore(strConto, "non e' attivo"));
		checkCondition(Boolean.TRUE.equals(conto.getContoFoglia()), ErroreCore.ENTITA_NON_COMPLETA.getErrore(strConto, "non e' un conto foglia"));
		model.getConciliazionePerTitolo().setConto(conto);
	}

	/**
	 * Aggiornamento della conciliazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		AggiornaConciliazionePerTitolo request = model.creaRequestAggiornaConciliazionePerTitolo();
		logServiceRequest(request);
		AggiornaConciliazionePerTitoloResponse response = conciliazioneService.aggiornaConciliazionePerTitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Aggiornata conciliazione con uid " + response.getConciliazionePerTitolo().getUid());
		model.setConciliazionePerTitolo(response.getConciliazionePerTitolo());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		checkNotNullNorInvalidUid(model.getConciliazionePerTitolo(), "conciliazione", true);
		validazioneCampiBaseConciliazione();
	}
}
