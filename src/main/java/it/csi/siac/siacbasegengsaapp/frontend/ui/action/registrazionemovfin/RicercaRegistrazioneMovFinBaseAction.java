/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
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
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
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
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di base GEN/GSA per la ricerca della registrazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 * @version 1.0.1 - 21/12/2015 - JIRA-2738 - caricamento asincrono delle liste
 * @param <M> la tipizzazione del model
 *
 */
public abstract class RicercaRegistrazioneMovFinBaseAction<M extends RicercaRegistrazioneMovFinBaseModel> extends GenericBilancioAction<M> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;

	/** Servizio per il classificatore bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient RegistrazioneMovFinService registrazioneMovFinService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient CausaleService causaleService;
	//SIAC-5290
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient SoggettoService soggettoService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	

	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}

	@Override
	public String execute() throws Exception {
		checkCasoDUsoApplicabile();
		caricaListaTipoFinanziamento();
		caricaListaClasseSoggetto();
		caricaListaTipoAtto();
		caricaListe();
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle liste.
	 */
	protected abstract void caricaListe();
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile =
				FaseBilancio.PLURIENNALE.equals(faseBilancio)
				|| FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}
	
	/**
	 * Imposta la lista degli stati operativi della registrazione
	 */
	protected void caricaListaStati() {
		final String methodName = "caricaListaStati";
		log.debug(methodName, "Caricamento della lista degli stati operativi della registrazione");
		
		List<StatoOperativoRegistrazioneMovFin> listaStati = Arrays.asList(StatoOperativoRegistrazioneMovFin.values());
		model.setListaStati(listaStati);
	}

	//SIAC-5799
	/**
	 * Caricamento della lista del tipo atto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaTipoAtto == null) {
			TipiProvvedimento req = model.creaRequestTipiProvvedimento();
			TipiProvvedimentoResponse res = provvedimentoService.getTipiProvvedimento(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			
			listaTipoAtto = res.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}

	
	/**
	 * Ricerca e imposta le liste dei tipi evento di entrata e di spesa
	 */
	protected void caricaListaTipiEventoDaSessione() {
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento == null) {
			return;
		}
		List<TipoEvento> resultSpesa = filterSpesa(listaTipoEvento);
		List<TipoEvento> resultEntrata = filterEntrata(listaTipoEvento);
		model.setListaTipiEventoEntrata(resultEntrata);
		model.setListaTipiEventoSpesa(resultSpesa);
	}
	
	/**
	 * Ricerca e imposta le liste dei tipi evento di entrata e di spesa
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaTipiEvento() {
		final String methodName = "caricaListaTipiEvento";
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				log.info(methodName, createErrorInServiceInvocationString(request, response));
				return INPUT;
			}
			listaTipoEvento = response.getCodifiche(TipoEvento.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
		}
		// Filtro gli integrati
		listaTipoEvento = filtraTipiEventoIntegrati(listaTipoEvento);
		model.setListaTipiEvento(listaTipoEvento);
		List<TipoEvento> resultSpesa = filterSpesa(listaTipoEvento);
		List<TipoEvento> resultEntrata = filterEntrata(listaTipoEvento);
		model.setListaTipiEventoEntrata(resultEntrata);
		model.setListaTipiEventoSpesa(resultSpesa);
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
			if(te != null && te.getListaTipoCausaleEP().contains(TipoCausale.Integrata) && filtraExtr(te)) {
				res.add(te);
			}
		}
		return res;
	}
	
	/**
	 * Filtro per EXTR
	 * @param te il tipo evento
	 * @return se il tipo evento &eacute; EXTR
	 */
	protected boolean filtraExtr(TipoEvento te) {
		return !BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(te.getCodice());
	}

	/**
	 * Filtra i tipi di entrata.
	 * 
	 * @param list la lista dei tipi
	 * @return la lista dei tipi di entrata
	 */
	private List<TipoEvento> filterEntrata(List<TipoEvento> list) {
		List<TipoEvento> result = new ArrayList<TipoEvento>();
		for(TipoEvento tipoEvento : list){
			if(tipoEvento.isTipoEntrata()){
				result.add(tipoEvento);
			}
		}
		return result;
	}
	
	/**
	 * Filtra i tipi di spesa.
	 * 
	 * @param list la lista dei tipi
	 * @return la lista dei tipi di entrata
	 */
	private List<TipoEvento> filterSpesa(List<TipoEvento> list) {
		List<TipoEvento> result = new ArrayList<TipoEvento>();
		for(TipoEvento tipoEvento : list){
			if(tipoEvento.isTipoSpesa()){
				result.add(tipoEvento);
			}
		}
		return result;
	}

	/**
	 * Caricamento dei tipi di evento entrata o spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaTipiEvento(){
		if("E".equals(model.getTipoElenco())){
			model.setListaTipiEvento(model.getListaTipiEventoEntrata());
		} else if("S".equals(model.getTipoElenco())){
			model.setListaTipiEvento(model.getListaTipiEventoSpesa());
		}
		return SUCCESS;
	}

	/**
	 * Ricerca e imposta la lista degli eventi.
	 * 
	 * @return una strinfa corrispondente al risultato dell'invocazione
	 */
	public String ricercaEventi(){
		RicercaEventiPerTipo req = model.creaRequestRicercaEventiPerTipo();
		RicercaEventiPerTipoResponse res = causaleService.ricercaEventiPerTipo(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
		}
		model.setListaEventi(res.getEventi());
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #ricercaRegistrazioneMovFin()}.
	 */
	public void prepareRicercaRegistrazioneMovFin() {
		model.setTipoElenco(null);
		model.setEvento(null);
		model.setAnnoMovimento(null);
		model.setNumeroMovimento(null);
		model.setNumeroSubmovimento(null);
		model.setDataRegistrazioneDa(null);
		model.setDataRegistrazioneA(null);
		model.setRegistrazioneMovFin(null);
		//SIAC-5290
		model.setCapitolo(null);
		model.setSoggetto(null);
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.setAccertamento(null);
		model.setImpegno(null);
		model.setSubAccertamento(null);
		model.setSubImpegno(null);
		//SIAC-5799
		model.setAttoAmministrativo(null);
		//SIAC-5944
		model.setStrutturaAmministrativoContabile(null);
	}
	
	/**
	 * Ricerca delle registrazioni sulla base dei criteri impostati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaRegistrazioneMovFin() {
		final String methodName = "ricercaRegistrazioneMovFin";

		preCreazioneRequestRicercaSintetica();
		log.info(methodName, "Effettua la ricerca");
		
		RicercaSinteticaRegistrazioneMovFin request = model.creaRequestRicercaSinteticaRegistrazioneMovFin();
		logServiceRequest(request);

		if(request.getStrutturaAmministrativoContabile()!=null)	{			
			log.info(methodName,"StrutturaAmministrativoContabile selezionata  "+ request.getStrutturaAmministrativoContabile().getUid());
		}		
				
		/*
		if(request.getAttoAmministrativo()!=null)	{			
			log.info(methodName,"anno provvedimento impostato in ricerca"+ request.getAttoAmministrativo().getUid());
		}		
		*/
		RicercaSinteticaRegistrazioneMovFinResponse response = registrazioneMovFinService.ricercaSinteticaRegistrazioneMovFin(request);
		logServiceResponse(response);

		
		if (response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			model.impostoDatiNelModel();
			return INPUT;
		}
		
		if(response.getRegistrazioniMovFin().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			model.impostoDatiNelModel();
			return INPUT;
		}
		log.debug(methodName, "Totale: "+response.getRegistrazioniMovFin().getTotaleElementi());
		 
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(request, response);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per fornire possibili estensioni prima della creazione della request di ricerca sintatica
	 */
	protected void preCreazioneRequestRicercaSintetica() {
		// Da implementare nelle sottoclassi se necessario
	}

	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaRegistrazioneMovFin request, RicercaSinteticaRegistrazioneMovFinResponse response) {
		BilSessionParameter bilSessionParameterRequest = getBilSessionParameterRequest();
		BilSessionParameter bilSessionParameterRisultati = getBilSessionParameterRisultati();
		BilSessionParameter bilSessionParameterRicerca = getBilSessionParameterRicerca();
		
		sessionHandler.setParametroXmlType(bilSessionParameterRequest, request);
		sessionHandler.setParametro(bilSessionParameterRisultati, response.getRegistrazioniMovFin());
		sessionHandler.setParametro(bilSessionParameterRicerca, model.componiStringaRiepilogo());
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
	 * @return il parametro di sessione corrispondente al riepilogo della ricerca
	 */
	protected abstract BilSessionParameter getBilSessionParameterRicerca();

	/**
	 * Validazione per il metodo {@link #ricercaRegistrazioneMovFin()}.
	 */
	public void validateRicercaRegistrazioneMovFin() {
		checkNotNullNorInvalidUid(model.getTipoEvento(), "Tipo Evento");
		
		checkCondition((model.getAnnoMovimento() == null && StringUtils.isBlank(model.getNumeroMovimento()))
			|| (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero movimento", ": i campi devono essere entrambi valorizzati o entrambi non valorizzati"));
		
		checkCondition(model.getNumeroSubmovimento() == null || model.getNumeroSubmovimento() == 0 || (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero submovimento", ": valorizzare anche anno e numero movimento"));
		/*
		checkCondizioneValida(model.getAttoAmministrativo() != null
				&& model.getAttoAmministrativo().getAnno() != 0
				&& model.getAttoAmministrativo().getNumero() != 0
				&& model.getAttoAmministrativo().getTipoAtto() != null && model.getAttoAmministrativo().getTipoAtto().getUid() != 0, "provvedimento");
		*/
		if (hasErrori()) {
			model.impostoDatiNelModel();
		}
	}
	
	/**
	 * Validazione per il metodo {@link RicercaRegistrazioneMovFinBaseAction#ricercaRegistrazioneMovFin()}.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	public boolean checkIsValidaRicercaRegistrazione() {
		final String methodName = "checkIsValidaRicercaRegistrazione";
		log.debugStart(methodName, "Verifica campi");
		
		if(!checkPresenzaIdEntita(model.getEvento())){
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("evento"));
		}
		validaContoFinanziario();

		return !hasErrori();
	}

	
	/**
	 * Validazione del conto finanziario
	 */
	protected void validaContoFinanziario() {
		if(model.getRegistrazioneMovFin() != null
				&& model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato() != null
				&& StringUtils.isNotBlank(model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice())){
			LeggiElementoPianoDeiContiByCodiceAndAnno request = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno();
			LeggiElementoPianoDeiContiByCodiceAndAnnoResponse response = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(request);
			logServiceResponse(response);			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
			}
			model.getRegistrazioneMovFin().setElementoPianoDeiContiAggiornato(response.getElementoPianoDeiConti());
		}
	}
	
	//SIAC-5290
	/**
	 * Controllo di validit&agrave; del capitolo
	 * @return <code>true</code> se il capitolo &eacute; valido
	 */
	protected boolean checkCapitolo() {
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
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipiEvento(), model.getTipoEvento());
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
				// Se non gestisco le UEB il numero di UEB e' preselezionato con 1
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
	 * Caricamento della lista del tipo finanziamento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaTipoFinanziamento() throws WebServiceInvocationFailureException {
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
	 * Controlli per il soggetto
	 */
	protected void checkSoggetto() {
		final String methodName = "checkSoggetto";
		if(model.getSoggetto() == null || StringUtils.isBlank(model.getSoggetto().getCodiceSoggetto())) {
			log.debug(methodName, "Soggetto non presente");
			model.setSoggetto(null);
			return;
		}
		RicercaSoggettoPerChiave req = model.creaRequestRicercaSoggettoPerChiave();
		RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return;
		}
		if(res.getSoggetto() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getSoggetto().getCodiceSoggetto()));
			return;
		}
		model.setSoggetto(res.getSoggetto());
	}
	
	/**
	 * Caricamento della lista della classe soggetto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClasseSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClasseSoggetto == null) {
			ListeGestioneSoggetto req = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse res = soggettoService.listeGestioneSoggetto(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			
			listaClasseSoggetto = res.getListaClasseSoggetto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClasseSoggetto);
		}
		
		model.setListaClasseSoggetto(listaClasseSoggetto);
	}
	
	//SIAC-5290
	
	/**
	 * Controllo del movimento di gestione
	 * @return se il moviemtno risulta essere valido
	 */
	protected boolean checkMovimentoGestione() {
		final String methodName = "checkMovimentoGestione";
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			// Il tipo di evento non e' impostato. Esco con errore
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Capitolo", "non e' possibile selezionare un impegno se non e' selezionato un tipo di evento"));
			return false;
		}
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipiEvento(), model.getTipoEvento());
		if(tipoEvento == null) {
			// Tipo evento non valido
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Tipo evento", "non e' un tipo evento censito per l'ente"));
			return false;
		}
		String eventoCode = tipoEvento.getCodice();
		if(! (BilConstants.CODICE_TIPO_EVENTO_ORDINATIVO_PAGAMENTO.getConstant().equals(eventoCode) || BilConstants.CODICE_TIPO_EVENTO_ORDINATIVO_INCASSO.getConstant().equals(eventoCode))) {
			log.warn(methodName, "il tipo evento selezionato non permette il filtro per movimento di gestione");
			//ignoro il controllo sul movimento gestione.
			return true;
		}
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
	 * Effettua una validazione dell'accertamento e del subaccertamento forniti in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
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
	 * Controllo per l'atto amministrativo
	 * @return se l'atto risulta essere valido
	 */
	protected boolean checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		checkCondition(aa == null || !(aa.getAnno() != 0 ^ aa.getNumero() != 0), ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e numero atto", "devono essere entrambi valorizzati o entrambi non valorizzati"));
		
		if(aa == null || aa.getAnno() == 0 || aa.getNumero() == 0) {
			//filtro non valorizzato
			model.setAttoAmministrativo(null);
			return true;
		}
		
		
		if(model.getAttoAmministrativo() != null && model.getAttoAmministrativo().getUid() != 0) {
			model.setAttoAmministrativo(model.getAttoAmministrativo());
		}
		log.info(methodName, "Ricerca provvedimento con anno " + aa.getAnno() + " e numero " + aa.getNumero());
		// Se ho i dati dell'atto amministrativo, controllo che siano corretti
		try {
			List<AttoAmministrativo> lista = controlloEsistenzaAttoAmministrativo();
			model.setAttoAmministrativo(lista.get(0));				
			model.setListaAttoAmministrativo(lista);	
			return true;
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione dell'Atto Amministrativo: " + pve.getMessage());
		}
		return false;
	}
	
	/**
	 * Controlla la presenza dell'atto amministrativo .
	 */
	//--ANTO SIAC-5799
	private List<AttoAmministrativo> controlloEsistenzaAttoAmministrativo() {
		final String methodName = "controlloEsistenzaAttoAmministrativo";
		RicercaProvvedimento req = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = createErrorInServiceInvocationString(req, res);
			log.info(methodName, errorMessage);
			addErrori(res);
			throw new ParamValidationException(errorMessage);
		}
		checkCondition(!res.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);
		return res.getListaAttiAmministrativi();

	}
	
}
