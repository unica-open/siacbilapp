/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RicercaValidazionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe base di action per la ricerca della prima nota libera per la validazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.1.0 - 16/06/2015
 * @param <M> la tipizzazione del model
 */
public abstract class RicercaValidazionePrimaNotaIntegrataBaseAction<M extends RicercaValidazionePrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7319994681410757757L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ContoService contoService;
	@Autowired private transient PrimaNotaService primaNotaService;
	@Autowired private transient SoggettoService soggettoService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		caricaListe();
	}
	
	/**
	 * Caricamento delle liste per l'interazione utente.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	private void caricaListe() {
		final String methodName = "caricaListe";
		//provo a pcaricare il tipo evento dalla sessione
		caricaListaTipoEventoDaSessione();
		caricaListaEvento();
		//carico gli stati della registrazione (dall'enum StatoOperativoPrimaNota)
		caricaListaStatoOperativoRegistrazioneMovFin();
		//prendo la lista classi dalla sessione tramite BilSessionParameter.LISTA_CLASSE_PIANO_GEN
		caricaListaClassiDaSessione();
		//prendo la lista classi dalla sessione tramite BilSessionParameter.LISTA_TITOLO_ENTRATA/SPESA
		caricaListaTitoliDaSessione();
		
		
		try {
			//carico tramite servizio la lista dei tipi atto
			caricaListaTipoAtto();
			//carico tramite servizio la lista delle classi soggetto
			caricaListaClasseSoggetto();
			// SIAC-5292
			// Carico la lista dei tipi finanziamento
			caricaListaTipoFinanziamento();
		} catch(WebServiceInvocationFailureException wsife) {
			//uno dei servizi precedentemente invocati e' fallito. Continuo.
			log.info(methodName, "caricaListe");
		}
	}
	
	/**
	 * Caricamento della lista del tipo atto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(listaTipoAtto == null) {
			//la lista tipo atto non risulta essere popolata, la devo caricare tramite servizio
			TipiProvvedimento req = model.creaRequestTipiProvvedimento();
			TipiProvvedimentoResponse res = provvedimentoService.getTipiProvvedimento(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco lanciando un'errore.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			
			listaTipoAtto = res.getElencoTipi();
			//setto in sessione la lista appena caricata per successivi utilizzi
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}
	
	/**
	 * Caricamento della lista della classe soggetto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClasseSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClasseSoggetto == null) {
			//la lista classe soggetto non risulta essere popolata, la devo caricare tramite servizio
			ListeGestioneSoggetto req = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse res = soggettoService.listeGestioneSoggetto(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			
			listaClasseSoggetto = res.getListaClasseSoggetto();
			//setto in sessione la lista appena caricata per successivi utilizzi
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClasseSoggetto);
		}
		//setto nel model la lista ottenuta dalla sessione o dal servizio
		model.setListaClasseSoggetto(listaClasseSoggetto);
	}
	
	/**
	 * Caricamento della lista del tipo finanziamento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoFinanziamento() throws WebServiceInvocationFailureException {
		List<TipoFinanziamento> listaTipiFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if(listaTipiFinanziamento == null) {
			LeggiClassificatoriGenericiByTipoElementoBil req = 
					model.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			LeggiClassificatoriGenericiByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			
			listaTipiFinanziamento = res.getClassificatoriTipoFinanziamento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaTipiFinanziamento);
		}
		
		model.setListaTipiFinanziamento(listaTipiFinanziamento);
	}
	
	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassiDaSessione() {
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
		if(listaClassePiano != null) {
			//la lista classe piano e' preesente, la setto nel model
			model.setListaClassi(listaClassePiano);
		}
		
	}
	
	/**
	 * Caricamento della lista delle classi.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassi() {
		final String methodName = "caricaListaClassi";
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
		if(listaClassePiano == null) {
			// la lista non e' presente in sessione, chiamo il servizio
			RicercaCodifiche req = model.creaRequestRicercaClassi();
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, createErrorInServiceInvocationString(req, res));
				addErrori(res);
				return INPUT;
			}
			
			listaClassePiano = res.getCodifiche(ClassePiano.class);
			
			//setto in sessione la lista per successivi utilizzi
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN, listaClassePiano);
		}
		
		//setto la lista in sessione per successivi utilizzi
		model.setListaClassi(listaClassePiano);
		return SUCCESS;
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	private void caricaListaTitoliDaSessione() {
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata != null) {
			//sono presenti titoli di entrata: li setto
			model.setListaTitoloEntrata(listaTitoloEntrata);
		}
		if(listaTitoloSpesa != null) {
			//sono presenti titoli di spesa: li setto
			model.setListaTitoloSpesa(listaTitoloSpesa);
		}
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaTitoli() {
		final String methodName = "caricaListaTitoli";
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata == null || listaTitoloSpesa == null) {
			// le liste non sono presenti in sessione, chiamo il servizio
			LeggiClassificatoriByTipoElementoBilResponse responseEntrata;
			LeggiClassificatoriByTipoElementoBilResponse responseSpesa;
			
			try {
				responseEntrata = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
				responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			} catch(WebServiceInvocationFailureException wsife) {
				//il servizio e' terminato con errori, esco
				log.info(methodName, wsife.getMessage());
				return INPUT;
			}
			
			listaTitoloEntrata = responseEntrata.getClassificatoriTitoloEntrata();
			listaTitoloSpesa = responseSpesa.getClassificatoriTitoloSpesa();
			
			//setto le liste in sessione per successivi utilizzi
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, listaTitoloEntrata);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, listaTitoloSpesa);
		}
		
		//setto nel model le liste ottenute
		model.setListaTitoloEntrata(listaTitoloEntrata);
		model.setListaTitoloSpesa(listaTitoloSpesa);
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		return res;
	}
	
	/**
	 * Caricamento della lista del tipo evento.
	 */
	private void caricaListaTipoEventoDaSessione() {
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento != null) {
			//setto la lista nel model
			model.setListaTipoEvento(listaTipoEvento);
		}
	}
	
	/**
	 * Caricamento della lista del tipo evento.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaTipoEvento() {
		final String methodName = "caricaListaTipoEvento";
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		
		if(listaTipoEvento == null) {
			// Carico i dati da servizio
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.warn(methodName, createErrorInServiceInvocationString(req, res));
				addErrori(res);
				return INPUT;
			}
			listaTipoEvento = res.getCodifiche(TipoEvento.class);
			
			//setto la lista in sessione per successivi utilizzi
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
		}
		
		model.setListaTipoEvento(filtraTipiEventoIntegrati(listaTipoEvento));
		return SUCCESS;
	}
	
	/**
	 * Filtra i soli tipi di evento di tipologia INTEGRATA.
	 * @param input la lista dei tipi di evento
	 * @return i tipi di evento integrati
	 */
	private List<TipoEvento> filtraTipiEventoIntegrati(List<TipoEvento> input) {
		List<TipoEvento> res = new ArrayList<TipoEvento>();
		for(TipoEvento te : input) {
			if(te.getListaTipoCausaleEP().contains(TipoCausale.Integrata)) {
				//la causale e' di tipo integrato: aggiungo il tipo evento alla listas
				res.add(te);
			}
		}
		return res;
	}
	
	/**
	 * Caricamento della lista dell'evento.
	 */
	private void caricaListaEvento() {
		// Carico quella in sessione e basta
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		model.setListaEvento(listaEvento);
	}
	/**
	 * Caricamento della lista degli stati operativi per la registrazione movimento fin.
	 */
	private void caricaListaStatoOperativoRegistrazioneMovFin() {
		List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota = Arrays.asList(StatoOperativoPrimaNota.values());
		model.setListaStatoOperativoPrimaNota(listaStatoOperativoPrimaNota);
	}
	
	@Override
	public String execute() throws Exception {
		// Impostazione dei valori di default
		impostaValoriDefault();
		
		checkCasoDUsoApplicabile();
		
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei valori di default
	 */
	private void impostaValoriDefault() {
		//se devo validare, lo stato della prima nota e' provvisorio
		model.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(req);
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Lancio l'eccezione ed esco
			throwExceptionFromErrori(res.getErrori());
		}
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		
		// Controllo che il bilancio NON sia in fase CHIUSO, PLURIENNALE, PREVISIONE
		if(getFaseDiBilancioNonCompatibile(faseBilancio)) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}

	/**
	 * Controlla se la fase di bilancio &eacute; in fase non compatibile
	 * @param faseBilancio la fase di bilancio
	 * @return se la fase &eacute; non compatibile
	 */
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
		return 
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio) ||
		FaseBilancio.CHIUSO.equals(faseBilancio);
	}
	
	
	/**
	 * Metodo di ricerca effettiva della prima nota integrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		
		
		
		RicercaSinteticaPrimaNotaIntegrataValidabile req = model.creaRequestRicercaSinteticaPrimaNotaIntegrataValidabile();
		logServiceRequest(req);

		//log.info(methodName, JAXBUtility.marshall(req));
		
		RicercaSinteticaPrimaNotaIntegrataValidabileResponse res = primaNotaService.ricercaSinteticaPrimaNotaIntegrataValidabile(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		
		log.debug(methodName, "Numero di risultati trovati: " + res.getTotaleElementi());
		if(res.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}

		sessionHandler.setParametro(getBilSessionParameterDati(), res.getPrimeNote());
		sessionHandler.setParametroXmlType(getBilSessionParameterRequest(), req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		//SIAC-5799
		String riepilogo = componiStringaRiepilogoCapitoloMovgestSac();
		sessionHandler.setParametro(getBilSessionParameterRiepilogo(), StringUtils.isNotBlank(riepilogo)? riepilogo : null);
		return SUCCESS;
	}
	
	/**
	 * Ottiene il parametro di sessione dei dati
	 * @return il parametro di sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterDati();
	/**
	 * Ottiene il parametro di sessione della request
	 * @return il parametro di sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterRequest();
	/**
	 * Ottiene il parametro di sessione del riepilogo
	 * @return il parametro di sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterRiepilogo();
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		//la prima nota non puo' essere null
		checkNotNull(model.getPrimaNota(), "Prima nota", true);
		//il tipo evento e' obbligatorio
		checkNotNullNorInvalidUid(model.getTipoEvento(), "tipo evento");
		
		//devo avere almeno un campo popolato per la ricerca
		boolean ricercaValida = checkPresenzaIdEntita(model.getTipoEvento())
			|| checkPresenzaIdEntita(model.getEvento())
			|| checkPresenzaIdEntita(model.getCausaleEP())
			|| checkCampoValorizzato(model.getPrimaNota().getNumero(), "numero prima nota")
			|| checkCampoValorizzato(model.getAnnoMovimento(), "anno movimento")
			|| checkCampoValorizzato(model.getNumeroMovimento(), "numero movimento")
			|| checkCampoValorizzato(model.getNumeroSubmovimento(), "numero submovimento")
			|| checkStringaValorizzata(model.getPrimaNota().getDescrizione(), "descrizione")
			|| checkCampoValorizzato(model.getDataRegistrazioneDa(), "data registrazione da")
			|| checkCampoValorizzato(model.getDataRegistrazioneA(), "data registrazione a")
			|| checkCondizioneValida(model.getAttoAmministrativo() != null
				&& model.getAttoAmministrativo().getAnno() != 0
				&& model.getAttoAmministrativo().getNumero() != 0
				&& model.getAttoAmministrativo().getTipoAtto() != null && model.getAttoAmministrativo().getTipoAtto().getUid() != 0, "provvedimento");
		//qualora vi sia un conto, ne controllo la corretteza e validita'
		ricercaValida = checkConto() || ricercaValida;
		//qualora vi sia un piano dei conti, ne controllo la corretteza e validita'
		ricercaValida = checkPianoDeiConti() || ricercaValida;
		//qualora vi sia una data di registrazione, ne controllo la corretteza e validita'
		ricercaValida = checkDateRegistrazione() || ricercaValida;
		// SIAC-5292
		ricercaValida = checkCapitolo() || ricercaValida;
		
		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		//controllo le data impostate come criterio di ricerca, in modo tale da non avere un range troppo esteso
		validaDatePerRicercaDocumento();
		
		//qualora vi sia un soggetto presente, ne controllo la corretteza e validita'
		validateSoggetto();
		//qualora vi sia un atto amministrativo, ne controllo la presenza e validita'
		checkAttoAmministrativo();
		//qualora vi sia un MovimentoGestione, ne controllo la presenza e validita'		
		checkMovimentoGestione();
		
	}
	
	/**
	 * Validazione delle date
	 * <br/>
	 * Nel caso in cui il tipo evento sia relativo al documento (DE, DS) diviene necessario impostare le date di registrazione da/a e che queste non distino tra di esse pi&uacute; di un mese.
	 */
	private void validaDatePerRicercaDocumento() {
		final String methodName = "validaDate";
		
		//controllo di aver impostato un tipo evento
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			log.debug(methodName, "Tipo evento non specificato, salto il controllo");
			return;
		}
		//del tipo evento viene passato solo l'uid. Ottengo anche codice e descrizione
		TipoEvento te = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		
		if(!BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_ENTRATA.getConstant().equals(te.getCodice()) && !BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_SPESA.getConstant().equals(te.getCodice())) {
			log.debug(methodName, "Tipo evento con codice " + te.getCodice() + ", non di tipo " + BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_ENTRATA.getConstant() + " o "
					+ BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_SPESA.getConstant() + ": controllo sulle date non necessario");
			//non effettuo il controllo sulle date per il tipo evento
			return;
		}
		
		// JIRA SIAC-5553 - Se è definito il provvedimento le date non sono obbligatorie
		if(model.getAttoAmministrativo().getAnno() != 0 
			|| model.getAttoAmministrativo().getNumero() != 0
			|| (model.getAttoAmministrativo().getTipoAtto() != null && model.getAttoAmministrativo().getTipoAtto().getUid() != 0)) {
			//non effettuo il controllo sulle date perchè presente il filtro sul provvedimento
			return;
		}

		// Se sono arrivato fino a qui, devo controllare che le date ci siano: sono obbligatorie
		checkNotNull(model.getDataRegistrazioneDa(), "data registrazione da");
		checkNotNull(model.getDataRegistrazioneA(), "data registrazione a");
		if(model.getDataRegistrazioneDa() == null || model.getDataRegistrazioneA() == null) {
			// Sono gia' in errore
			log.debug(methodName, "Date non valorizzate");
			return;
		}
		
		Calendar limit = Calendar.getInstance();
		limit.setTime(model.getDataRegistrazioneDa());
		limit.add(Calendar.MONTH, 1);
		Date limitDate = limit.getTime();
		
		checkCondition(!model.getDataRegistrazioneA().after(limitDate), ErroreCore.DATE_INCONGRUENTI.getErrore(": quando il tipo evento e' relativo ai documenti le date di registrazione non possono distare piu' di mese"));
	}

	/**
	 * Carica il soggetto qualora impostatao
	 * 
	 */
	protected void validateSoggetto() {
		final String methodName = "validateSoggetto";
		boolean soggettoPresente =  model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto());
		
		if(!soggettoPresente){
			//l'utente non ha specificato un soggetto come criterio di ricerca
			log.debug(methodName, "Nessun soggetto da caricare.");
			return;
		}
		
		RicercaSoggettoPerChiave req = model.creaRequestRicercaSoggettoPerChiave();
		RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		Soggetto soggetto = res.getSoggetto();
		if(soggetto == null) {
			log.info(methodName, "Soggetto == null");
			//l'utente ha specificato un soggetto non presente in archivio
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getSoggetto().getCodiceSoggetto()));
			return;
		}
		
		log.debug(methodName, "Soggetto individuato: " + soggetto.getDenominazione() + " (uid:" + soggetto.getUid() + ").");
		//setto nel model il soggetto trovato con tutti i dati ottenuti dal servizio
		model.setSoggetto(soggetto);
	}
	
	/**
	 * Controlli per l'atto amministrativo
	 */
	private void checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		if(aa == null) {
			log.debug(methodName, "Atto amministrativo non presente");
			return;
		}
		//controllo sia stata specificata la chiave del provvedimento
		checkCondition((aa.getAnno() != 0 && aa.getNumero() != 0 && aa.getTipoAtto() != null && aa.getTipoAtto().getUid() != 0)
				|| (aa.getAnno() == 0 && aa.getNumero() == 0 && (aa.getTipoAtto() == null || aa.getTipoAtto().getUid() == 0)),
			ErroreCore.VALORE_NON_VALIDO.getErrore("Provvedimento", ": e' necessario specificare anno, numero e tipo per effettuare la ricerca"), true);
		
		// SIAC-4644, modifica: anche il tipo di atto deve essere obbligatorio
		if(aa.getAnno() == 0 || aa.getNumero() == 0 || aa.getTipoAtto() == null || aa.getTipoAtto().getUid() == 0) {
			log.debug(methodName, "Atto amministrativo non presente");
			model.setAttoAmministrativo(null);
			return;
		}
		//del tipo atto viene passato solo l'uid. Ottengo anche codice e descrizione
		TipoAtto ta = ComparatorUtils.searchByUid(model.getListaTipoAtto(), aa.getTipoAtto());
		aa.setTipoAtto(ta);
		
		RicercaProvvedimento req = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return;
		}
		List<AttoAmministrativo> sacs = filterSac(res.getListaAttiAmministrativi(), aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0);
		try {
			// Controllo le SAC: deve esserci al piu' un provvedimento senza SAC
			checkUnicoAttoAmministrativo(sacs, aa.getStrutturaAmmContabile(), true);
		} catch (ParamValidationException pve) {
			log.debug(methodName, pve.getMessage());
			return;
		}
		model.setAttoAmministrativo(sacs.get(0));
	}
	
	/**
	 * Controllo di validit&agrave; del capitolo
	 * @return <code>true</code> se il capitolo &eacute; valido
	 */
	private boolean checkCapitolo() {
		final String methodName = "checkCapitolo";
		if(capitoloNonValorizzato(model.getCapitolo())) {
			log.debug(methodName, "Il capitolo non e' staato fornito correttamente");
			return false;
		}
		if(capitoloParziamenteValorizzato(model.getCapitolo())) {
			// Il capitolo e' compilato solo in parte
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Capitolo",
					": devono essere valorizzati i parametri Capitolo/Articolo" + (model.isGestioneUEB() ? "/UEB" : "")));
			return false;
		}
		// Il capitolo e' presente
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			// Il tipo di evento non e' impostato. Esco con errore
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Capitolo", "non e' possibile selezionare un capitolo se non e' selezionato un tipo di evento"));
			return false;
		}
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		if(tipoEvento == null) {
			// Tipo evento non valido
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Tipo evento", "non e' un tipo evento censito per l'ente"));
			return false;
		}
		
		if(tipoEvento.isTipoSpesa()) {
			return checkCapitoloSpesa();
		}
		if(tipoEvento.isTipoEntrata()) {
			return checkCapitoloEntrata();
		}
		log.debug(methodName, "Evento di tipo ne' entrata ne' spesa: ignoro il capitolo");
		return false;
	}
	
	/**
	 * Controlla se il capitolo sia valorizzato
	 * @param cap il capitolo da controllare
	 * @return se il capitolo sia valorizzato
	 */
	private boolean capitoloNonValorizzato(Capitolo<?, ?> cap) {
		return cap == null ||
				(cap.getNumeroCapitolo() == null
				&& cap.getNumeroArticolo() == null
				&& (!model.isGestioneUEB() || cap.getNumeroUEB() == null));
	}
	
	/**
	 * Controlla se il capitolo sia parzialmente valorizzato
	 * @param cap il capitolo da controllare
	 * @return se il capitolo sia valorizzato
	 */
	private boolean capitoloParziamenteValorizzato(Capitolo<?, ?> cap) {
		return cap.getNumeroCapitolo() == null
				|| cap.getNumeroArticolo() == null
				|| cap.getNumeroUEB() == null;
	}

	/**
	 * Check del capitolo di spesa
	 * @return se il capitolo esiste
	 */
	private boolean checkCapitoloSpesa() {
		final String methodName = "checkCapitoloSpesa";
		RicercaPuntualeCapitoloUscitaGestione req = model.creaRequestRicercaPuntualeCapitoloUscitaGestione();
		RicercaPuntualeCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return false;
		}
		if(res.getCapitoloUscitaGestione() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo di uscita gestione", componiStringaCapitolo(model.getCapitolo())));
			return false;
		}
		model.setCapitolo(res.getCapitoloUscitaGestione());
		return true;
	}
	
	/**
	 * Check del capitolo di entrata
	 * @return se il capitolo esiste
	 */
	private boolean checkCapitoloEntrata() {
		final String methodName = "checkCapitoloEntrata";
		RicercaPuntualeCapitoloEntrataGestione req = model.creaRequestRicercaPuntualeCapitoloEntrataGestione();
		RicercaPuntualeCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return false;
		}
		if(res.getCapitoloEntrataGestione() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo di entrata gestione", componiStringaCapitolo(model.getCapitolo())));
			return false;
		}
		model.setCapitolo(res.getCapitoloEntrataGestione());
		return true;
	}
	
	/**
	 * Compone la stringa identificativa del capitolo
	 * @param cap il capitolo
	 * @return la stringa del capitolo
	 */
	private String componiStringaCapitolo(Capitolo<?, ?> cap) {
		StringBuilder sb = new StringBuilder();
		sb.append(cap.getAnnoCapitolo())
			.append("/")
			.append(cap.getNumeroCapitolo())
			.append("/")
			.append(cap.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			sb.append("/")
				.append(cap.getNumeroUEB());
		}
		return sb.toString();
	}

	/**
	 * Filtra gli atti tramite le sac
	 * @param atti gli atti da filtrare
	 * @param sacNull se la struttura sia null
	 */
	private List<AttoAmministrativo> filterSac(List<AttoAmministrativo> atti, boolean sacNull) {
		if(!sacNull) {
			return atti;
		}
		List<AttoAmministrativo> res = new ArrayList<AttoAmministrativo>();
		for(AttoAmministrativo aa : atti) {
			if(aa != null && (aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0)) {
				//questo provvedimento non ha la sac: lo aggiungo alla lista.
				res.add(aa);
			}
		}
		return res;
	}
	
	/**
	 * Controllo per il Conto.
	 * 
	 * @return <code>true</code> se il conto &eacute; stato valorizzato in modo valido; <code>false</code> altrimenti 
	 */
	private boolean checkConto() {
		final String methodName = "checkConto";
		if(model.getConto() == null || StringUtils.isBlank(model.getConto().getCodice())) {
			// Conto non valorizzato. La validazione e' trivialmente false
			return false;
		}
		
		RicercaSinteticaConto req = model.creaRequestRicercaSinteticaConto();
		logServiceRequest(req);
		RicercaSinteticaContoResponse res = contoService.ricercaSinteticaConto(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}
		try {
			//devo avere due conti
			checkCondition(res.getTotaleElementi() > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto", model.getConto().getCodice()), true);
			checkCondition(res.getTotaleElementi() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Conto"), true);
			
			Conto conto = res.getConti().get(0);
			model.setConto(conto);
		} catch (ParamValidationException pve) {
			log.info(methodName, "Errore di validazione per il conto: " + pve.getMessage());
			// Errore di validazione
			return false;
		}
		
		return true;
	}
	
	/**
	 * Controllo per il Piano dei Conti.
	 * 
	 * @return <code>true</code> se il piano dei conti &eacute; stato valorizzato in modo valido; <code>false</code> altrimenti 
	 */
	private boolean checkPianoDeiConti() {
		final String methodName = "checkPianoDeiConti";
		if(model.getRegistrazioneMovFin() == null || model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato() == null
				|| StringUtils.isBlank(model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice())) {
			// Non ho il campo: trivialmente il controllo non e' valido
			return false;
		}
		
		// Ricerca del conto
		LeggiElementoPianoDeiContiByCodiceAndAnno req = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno();
		logServiceRequest(req);
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse res = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Se ho errori esco
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}
		
		try {
			// 1. Il conto deve esistere
			checkCondition(res.getElementoPianoDeiConti() != null,
				ErroreCore.ENTITA_INESISTENTE.getErrore("Il conto finanziario", model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice()),
				true);
			
			ElementoPianoDeiConti epdc = res.getElementoPianoDeiConti();
			
			// 2. Deve essere valido nell'anno di bilancio
			checkCondition(ValidationUtil.isEntitaValidaPerAnnoEsercizio(epdc, model.getAnnoEsercizioInt()),
				ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " non ha un'istanza valida nell'anno di bilancio"),
				true);
			
			// 3. Deve essere di quinto livello
			checkCondition(epdc.getLivello() == 5,
				ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " e' di livello " + epdc.getLivello()),
				true);
			
			model.getRegistrazioneMovFin().setElementoPianoDeiContiAggiornato(epdc);
		} catch(ParamValidationException pve) {
			// Errore di validazione
			log.info(methodName, "Errore di validazione per il piano dei conti: " + pve.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Controllo per le date di registrazione: se valorizzate entrambe, quella <code>da</code> non pu&oacute; essere successiva quella <code>a</code.
	 * 
	 * @return <code>true</code> se la condizione sulle date &eacute; rispettata; <code>false</code> altrimenti
	 */
	private boolean checkDateRegistrazione() {
		if(model.getDataRegistrazioneDa() == null || model.getDataRegistrazioneA() == null) {
			// Almeno una data non e' presente. La condizione e' trivialmente non verificata
			return false;
		}
		
		//la data di registrazione da deve essere successiva alla data di reguistrazione A
		boolean condition = !model.getDataRegistrazioneDa().after(model.getDataRegistrazioneA());
		checkCondition(condition, ErroreCore.FORMATO_NON_VALIDO.getErrore("data registrazione da, data registrazione a", "la data da non puo' essere successiva alla data a"));
		
		return condition;
	}
	
	/**
	 * Controlla il movimento di gestione
	 * @return se il movimento sia valido
	 */
	protected boolean checkMovimentoGestione() {
		final String methodName = "checkMovimentoGestione";
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			// Il tipo di evento non e' impostato. Esco con errore
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Movimento Gestione", "non e' possibile selezionare un movimento gestione se non e' selezionato un tipo di evento"));
			return false;
		}
		
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		
		MovimentoGestione movimentoGestioneDaControllare  = tipoEvento.isTipoEntrata() ?  model.getAccertamento() : model.getImpegno();
		if(movimentoGestioneDaControllare == null || movimentoGestioneDaControllare.getAnnoMovimento()==0 || movimentoGestioneDaControllare.getNumero() == null) {
			log.debug(methodName, "L'impegno non e' stato fornito correttamente");
			return true;
		}
		// Il capitolo e' presente
		
		if(tipoEvento.isTipoSpesa()) {
			return checkImpegno();
		}
		if(tipoEvento.isTipoEntrata()) {
			return checkAccertamento();
		}
		log.debug(methodName, "Evento di tipo ne' entrata ne' spesa: ignoro il capitolo");
		return true;
	}

	/**
	 * Controlla l'accertamento
	 * @return se l'accertamento &acute; valido
	 */
	protected boolean checkAccertamento() {
		final String methodName = "checkAccertamento";
		Accertamento accertamento =  model.getAccertamento();
		SubAccertamento subAccertamento = model.getSubAccertamento();
		
		if(accertamento == null || (accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null)) {
			return false;
		}
		
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nell'invocazione della ricercaAccertamentoPerChiaveOttimizzato");
			addErrori(response);
			return false;
		}
		if(response.isFallimento() || response.getAccertamento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento()+"/"+accertamento.getNumero()));
			return false;
		}
		
		accertamento = response.getAccertamento();
		
		model.setMovimentoGestione(accertamento);
		
		if(subAccertamento != null && subAccertamento.getNumero() != null) {
			BigDecimal numero = subAccertamento.getNumero();
			// Controlli di validità sull'impegno
			subAccertamento = findSubAccertamentoLegatoAccertamentoByNumero(response.getAccertamento(), subAccertamento);
			if(subAccertamento == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero() + "-" + numero));
				return false;
			}
			model.setSubMovimentoGestione(subAccertamento);
		} 
		
		return true;
	}
	
	 /* Trova il subAccertamento nell'elenco degli subaccertamenti dell'accertamento, se presente.
	 * 
	 * @param accertamento    l'accertamento tra i cui subAccertamenti trovare quello fornito
	 * @param subAccertamento il subaccertamento da cercare
	 * 
	 * @return il subaccertamento legato, se presente; <code>null</code> in caso contrario
	 */
	private SubAccertamento findSubAccertamentoLegatoAccertamentoByNumero(Accertamento accertamento, SubAccertamento subAccertamento) {
		SubAccertamento result = null;
		if(accertamento.getElencoSubAccertamenti() != null) {
			for(SubAccertamento s : accertamento.getElencoSubAccertamenti()) {
				if(s.getNumero().compareTo(subAccertamento.getNumero()) == 0) {
					result = s;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Ricerca dell'impegno per chiave
	 * @return la response dell'impegno
	 */
	private boolean checkImpegno() {
		
		Impegno impegno = model.getImpegno();
		SubImpegno subImpegno = model.getSubImpegno();
		
		boolean impegnoValorizzato = impegno != null && impegno.getAnnoMovimento() != 0 && impegno.getNumero() != null;
		boolean subImpegnoValorizzato = subImpegno != null && subImpegno.getNumero() != null;

		
		checkCondition(impegnoValorizzato || !subImpegnoValorizzato , ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("per indicare un subimpegno e' necessario indicare anche un impegno."), true);
		
		
		
		if(!impegnoValorizzato){
			return true;
		}
		
	
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return false;
		}
		
		if(response.isFallimento() || response.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", impegno.getAnnoMovimento()+"/"+impegno.getNumero()));
			return false;
		}
		
		impegno = response.getImpegno();
		
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumero() != null) {
			BigDecimal numero = subImpegno.getNumero();
			// Controlli di validità sull'impegno
			subImpegno = findSubImpegnoLegatoImpegnoByNumero(response.getImpegno(), subImpegno);
			if(subImpegno == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", ottieniChiaveMovgest(impegno) +  "-" + numero));
				return false;
			}
			model.setSubMovimentoGestione(subImpegno);
			
		} 		
		return true;
	}
	
	/**
	 * Trova il subImpegno nell'elenco degli subimpegni dell'impegno, se presente.
	 * 
	 * @param impegno    l'impegno tra i cui subImpegni trovare quello fornito
	 * @param subImpegno il subimpegno da cercare
	 * 
	 * @return il subimpegno legato, se presente; <code>null</code> in caso contrario
	 */
	private SubImpegno findSubImpegnoLegatoImpegnoByNumero(Impegno impegno, SubImpegno subImpegno) {
		SubImpegno result = null;
		if(impegno.getElencoSubImpegni() != null) {
			for(SubImpegno s : impegno.getElencoSubImpegni()) {
				if(s.getNumero().compareTo(subImpegno.getNumero()) == 0) {
					result = s;
					break;
				}
			}
		}
		return result;
	}
	
	private String ottieniChiaveMovgest(MovimentoGestione movimentoGestione) {
		StringBuilder sb = new StringBuilder();
		if(movimentoGestione != null) {
			sb.append(movimentoGestione.getAnnoMovimento())
				.append('/')
				.append(movimentoGestione.getNumero());
		}
		return sb.toString();
	}
	
	/**
	 * Componi stringa riepilogo capitolo movgest sac.
	 *
	 * @return the string
	 */
	private String componiStringaRiepilogoCapitoloMovgestSac() {
		StringBuilder sb = new StringBuilder();
		boolean capitoloValorizzato = idEntitaPresente(model.getCapitolo());
		boolean movimentoGestioneValorizzato = idEntitaPresente(model.getMovimentoGestione());
		boolean sacValorizzata = idEntitaPresente(model.getStrutturaAmministrativoContabile());
		if(!capitoloValorizzato && !movimentoGestioneValorizzato && ! sacValorizzata) {
			return sb.toString();
		}
		String capitolo = concatenaStringhe(capitoloValorizzato, " capitolo ", componiStringaCapitolo(model.getCapitolo()));
		String movgest = concatenaStringhe(movimentoGestioneValorizzato, " movimento gestione ", ottieniChiaveMovgest(model.getMovimentoGestione()));
		String sac = concatenaStringhe(sacValorizzata, " struttura amministrativa ", model.getStringaSACCapitolo());
		
		return sb.append(capitolo).append(sac).append(movgest).toString();
	}
	
	/**
	 * Concatena stringhe.
	 *
	 * @param concatena the concatena
	 * @param prefixes the prefixes da concatenare
	 * @return the string
	 */
	private String concatenaStringhe(boolean concatena, String... prefixes) {
		StringBuilder sb = new StringBuilder();
		if(!concatena) {
			return sb.toString();
		}
		for (String prefix : prefixes) {
			sb.append(prefix);
		}
		return sb.toString();
	}
}
