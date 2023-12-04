/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RicercaCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di action per la ricerca della causale EP.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 06/05/2015
 * @param <M> la tipizzazione del model
 */

public abstract class RicercaCausaleEPBaseAction <M extends RicercaCausaleEPBaseModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5154435562952917356L;
	
	@Autowired private transient CausaleService causaleService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient ContoService contoService;
	@Autowired private transient SoggettoService soggettoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		caricaListe();
	}
	
	
	/**
	 * Caricamento delle liste per l'interazione utente .
	 * 
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	protected abstract void caricaListe();

	
	/**
	 * Caricamento della lista del Tipo causale.
	 */
	protected void caricaListaTipoCausale() {
		model.setListaTipoCausale(Arrays.asList(TipoCausale.values()));
	}
	
	/**
	 * Caricamento della lista dello Stato Causale.
	 */
	protected void caricaListaStatoOperativoCausaleEP() {
		model.setListaStatoOperativoCausaleEP(Arrays.asList(StatoOperativoCausaleEP.values()));
	}
	
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
		// Imposto la fase in sessione per i risultati della ricerca, ma non effettuo caching su di esso
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		checkCondition(model.getCausaleEP() != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), true);
		
		checkCondition(isRicercaValida(), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}


	/**
	 * Controlla se la ricerca sia valida
	 * @return se la ricerca &eacute; valida
	 */
	protected boolean isRicercaValida() {
		boolean ricercaValida = checkCampoValorizzato(model.getCausaleEP().getTipoCausale(), "Tipo causale")
				|| checkCampoValorizzato(model.getCausaleEP().getStatoOperativoCausaleEP(), "Stato operativo")
				|| checkStringaValorizzata(model.getCausaleEP().getCodice(), "Codice causale")
				|| checkPresenzaIdEntita(model.getTipoEvento())
				|| checkPresenzaIdEntita(model.getEvento());
		
		ricercaValida = controlloCodiceConto() || ricercaValida;
		ricercaValida = controlloCodiceContoFinanziario() || ricercaValida;
		ricercaValida = controlloCodiceSoggetto() || ricercaValida;
		return ricercaValida;
	}
	
	/**
	 * Controlla la validita del codice conto.
	 * 
	 * @return <code>true</code> se il codice conto &eacute; impostato e corretto; <code>false</code> altrimenti
	 */
	private boolean controlloCodiceConto() {
		final String methodName = "controlloCodiceConto";
		if(model.getConto() == null || StringUtils.isBlank(model.getConto().getCodice())) {
			log.debug(methodName, "Nessun conto selezionato");
			return false;
		}
		
		RicercaSinteticaConto request = model.creaRequestRicercaSinteticaConto(model.getConto());
		logServiceRequest(request);
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaConto.class, response));
			addErrori(response);
			return false;
		}
		
		checkCondition(response.getTotaleElementi() > 0, ErroreCore.ENTITA_INESISTENTE.getErrore("Conto", model.getConto().getCodice()), true);
		Conto conto = response.getConti().get(0);
		// Imposto il conto nel model
		model.setConto(conto);
		
		return true;
	}
	
	/**
	 * Controlla la validita del codice conto finanziario.
	 * 
	 * @return <code>true</code> se il codice conto finanziario &eacute; impostato e corretto; <code>false</code> altrimenti
	 */
	private boolean controlloCodiceContoFinanziario() {
		final String methodName = "controlloCodiceContoFinanziario";
		if(model.getElementoPianoDeiConti() == null || StringUtils.isBlank(model.getElementoPianoDeiConti().getCodice())) {
			log.debug(methodName, "Nessun conto finanziario selezionato");
			return false;
		}
		
		LeggiElementoPianoDeiContiByCodiceAndAnno request = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(model.getElementoPianoDeiConti());
		logServiceRequest(request);
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse response = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(LeggiElementoPianoDeiContiByCodiceAndAnno.class, response));
			addErrori(response);
			return false;
		}
		
		checkCondition(response.getElementoPianoDeiConti() != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Conto finanziario", model.getElementoPianoDeiConti().getCodice()), true);
		// Imposto il dato nel model
		ElementoPianoDeiConti elementoPianoDeiConti = response.getElementoPianoDeiConti();
		model.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		return true;
	}

	/**
	 * Controlla la validita del codice soggetto.
	 * 
	 * @return <code>true</code> se il codice soggetto &eacute; impostato e corretto; <code>false</code> altrimenti
	 */
	private boolean controlloCodiceSoggetto() {
		final String methodName = "controlloCodiceSoggetto";
		if(model.getSoggetto() == null || StringUtils.isBlank(model.getSoggetto().getCodiceSoggetto())) {
			log.debug(methodName, "Nessun soggetto selezionato");
			return false;
		}
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		if(soggetto == null || listaSedeSecondariaSoggetto == null || listaModalitaPagamentoSoggetto == null || !model.getSoggetto().getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())) {
			log.debug(methodName, "Caricamento dei dati da servizio");
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.debug(methodName, createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
				addErrori(response);
				return false;
			}
			checkCondition(response.getSoggetto() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getSoggetto().getCodiceSoggetto()), true);
			
			soggetto = response.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		model.setSoggetto(soggetto);
		
		return true;
	}

	/**
	 * Effettuazione della ricerca.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		
		RicercaSinteticaCausale request = model.creaRequestRicercaSinteticaCausale();
		logServiceRequest(request);
		RicercaSinteticaCausaleResponse response = causaleService.ricercaSinteticaCausale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaCausale.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Numero di risultati trovati: " + response.getTotaleElementi());
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Impostazione dati in sessione
		impostaParametriInSessione(request, response);
		return SUCCESS;
	}

	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(LeggiClassificatoriByTipoElementoBil.class, response));
		}
		return response;
	}
	
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaCausale request, RicercaSinteticaCausaleResponse response) {
		BilSessionParameter bilSessionParameterRequest = getBilSessionParameterRequest();
		BilSessionParameter bilSessionParameterRisultati = getBilSessionParameterRisultati();
		
		
		sessionHandler.setParametroXmlType(bilSessionParameterRequest, request);
		sessionHandler.setParametro(bilSessionParameterRisultati, response.getCausali());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
	
	/**
	 * @return il parametro di sessione corrispondente alla request
	 */
	protected abstract BilSessionParameter getBilSessionParameterRequest();

	/**
	 * @return il parametro di sessione corrispondente ai risultati della ricerca
	 */
	protected abstract BilSessionParameter getBilSessionParameterRisultati();
	
	/**
	 * Ottiene il parametro di sessione per la lista delle classi piano.
	 * 
	 * @return il parametro per la sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaClassePiano();	
	
	/**
	 * Ottiene il parametro di sessione per la lista delle classi soggetto.
	 * 
	 * @return il parametro per la sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaClasseSoggetto();	
	/**
	 * Caricamento della lista del tipo evento.
	 */
	protected void caricaListaTipoEventoDaSessione() {
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento != null) {
			model.setListaTipoEvento(listaTipoEvento);
		}
	}
	
	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassiDaSessione() {
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassePiano());
		if(listaClassePiano != null) {
			model.setListaClassi(listaClassePiano);
		}
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaTitoliDaSessione() {
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata != null) {
			model.setListaTitoloEntrata(listaTitoloEntrata);
		}
		if(listaTitoloSpesa != null) {
			model.setListaTitoloSpesa(listaTitoloSpesa);
		}
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaClassiSoggettoDaSessione() {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(getBilSessionParameterListaClasseSoggetto());
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
	
	/**
	 * Caricamento della lista dell'evento.
	 */
	protected void caricaListaEvento() {
		// Carico quella in sessione e basta
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		model.setListaEvento(listaEvento);
	}
	
}

