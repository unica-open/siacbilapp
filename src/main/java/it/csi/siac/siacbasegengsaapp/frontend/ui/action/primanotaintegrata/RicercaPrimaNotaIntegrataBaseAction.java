/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RicercaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
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
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe base di action per la ricerca della prima nota libera.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 * @version 1.0.1 - 21/12/2015 - JIRA-2739 - caricamento asincrono delle liste
 * 
 * @param <M> la tipizzazione del model
 */
public abstract class RicercaPrimaNotaIntegrataBaseAction<M extends RicercaPrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 9187925799276716203L;
	
	/** Servizio per il classificatore bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ContoService contoService;
	@Autowired private transient PrimaNotaService primaNotaService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	@Autowired private transient SoggettoService soggettoService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		caricaListe();
	}
	
	@Override
	public String execute() throws Exception {
		// SIAC-5242: caricamento della fase di bilancio
		caricaFaseBilancio();
		return SUCCESS;
	}
	
	/**
	 * Caricamento della fase di bilancio
	 */
	private void caricaFaseBilancio() {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		if(res.hasErrori()) {
			throw new GenericFrontEndMessagesException(createErrorInServiceInvocationString(req, res));
		}
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}
	
	/**
	 * Caricamento delle liste per l'interazione utente.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	protected abstract void caricaListe();
	
	
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
	
	/**
	 * Caricamento della lista delle classi.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassi() {
		final String methodName = "caricaListaClassi";
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassePiano());
		if(listaClassePiano == null) {
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
			sessionHandler.setParametro(getBilSessionParameterListaClassePiano(), listaClassePiano);
		}
		
		model.setListaClassi(listaClassePiano);
		return SUCCESS;
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
	 * Ottiene il parametro di sessione per la lista delle classi piano.
	 * 
	 * @return il parametro per la sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaClassePiano();
	
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
	 * Caricamento della lista dell'evento.
	 */
	protected void caricaListaEvento() {
		// Carico quella in sessione e basta
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		model.setListaEvento(listaEvento);
	}
	/**
	 * Caricamento della lista degli stati operativi per la registrazione movimento fin.
	 */
	protected void caricaListaStatoOperativoRegistrazioneMovFin() {
		List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota = Arrays.asList(StatoOperativoPrimaNota.values());
		model.setListaStatoOperativoPrimaNota(listaStatoOperativoPrimaNota);
	}
	
	/**
	 * Metodo di ricerca effettiva della prima nota integrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		
		RicercaSinteticaPrimaNotaIntegrata req = model.creaRequestRicercaSinteticaPrimaNotaIntegrata();
		logServiceRequest(req);
		RicercaSinteticaPrimaNotaIntegrataResponse res = primaNotaService.ricercaSinteticaPrimaNotaIntegrata(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			model.impostoDatiNelModel();
			return INPUT;
		}
		
		log.debug(methodName, "Numero di risultati trovati: " + res.getTotaleElementi());
		if(res.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			model.impostoDatiNelModel();
			return INPUT;
		}
		
		// Impostazione dati in sessione
		sessionHandler.setParametro(getBilSessionParameterRisultati(), res.getPrimeNote());
		sessionHandler.setParametroXmlType(getBilSessionParameterRequest(), req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		//SIAC-5799
		String riepilogo = componiStringaRiepilogoCapitoloMovgestSac();
		sessionHandler.setParametro(getBilSessionParameterRiepilogo(), StringUtils.isNotBlank(riepilogo)? riepilogo : null);
		return SUCCESS;
	}
	
	/**
	 * @return il parametro di sessione corrispondente alla request
	 */
	protected abstract BilSessionParameter getBilSessionParameterRequest();

	/**
	 * @return il parametro di sessione corrispondente ai risultati della ricerca
	 */
	protected abstract BilSessionParameter getBilSessionParameterRisultati();
	
	//SIAC-5799
	/**
	 * @return il parametro di sessione corrispondente al riepilogo della ricerca
	 */
	protected abstract BilSessionParameter getBilSessionParameterRiepilogo();

	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		final String methodName = "validateEffettuaRicerca";
		checkNotNull(model.getPrimaNota(), "Prima nota", true);
		
		boolean ricercaValida = checkPresenzaIdEntita(model.getTipoEvento())
			|| checkPresenzaIdEntita(model.getEvento())
			|| checkPresenzaIdEntita(model.getCausaleEP())
			|| checkCampoValorizzato(model.getPrimaNota().getNumero(), "numero prima nota")
			|| checkCampoValorizzato(model.getPrimaNota().getNumeroRegistrazioneLibroGiornale(), "numero prima nota definitivo")
			|| checkCampoValorizzato(model.getAnnoMovimento(), "anno movimento")
			|| checkCampoValorizzato(model.getNumeroMovimento(), "numero movimento")
			|| checkCampoValorizzato(model.getNumeroSubmovimento(), "numero submovimento")
			|| checkCampoValorizzato(model.getPrimaNota().getStatoOperativoPrimaNota(), "stato")
			|| checkStringaValorizzata(model.getPrimaNota().getDescrizione(), "descrizione")
			|| checkCampoValorizzato(model.getImportoDocumentoDa(), "importo da")
			|| checkCampoValorizzato(model.getImportoDocumentoA(), "importo a")
			|| checkCampoValorizzato(model.getDataRegistrazioneDefinitivaDa(), "data registrazione da")
			|| checkCampoValorizzato(model.getDataRegistrazioneDefinitivaA(), "data registrazione a")
			|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaDa(), "data registrazione provvisoria da")
			|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaA(), "data registrazione provvisoria a")
			|| checkCondizioneValida(model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto()), "soggetto")
			|| checkCondizioneValida(model.getAttoAmministrativo() != null
				&& model.getAttoAmministrativo().getAnno() != 0
				&& model.getAttoAmministrativo().getNumero() != 0
				&& model.getAttoAmministrativo().getTipoAtto() != null && model.getAttoAmministrativo().getTipoAtto().getUid() != 0, "provvedimento")
			|| checkUlterioriCampi();
		
		ricercaValida = checkConto() || ricercaValida;
		ricercaValida = checkPianoDeiConti() || ricercaValida;
		ricercaValida = checkDateRegistrazione(model.getDataRegistrazioneDefinitivaDa(), model.getDataRegistrazioneDefinitivaA(), "data registrazione definitiva") || ricercaValida;
		ricercaValida = checkDateRegistrazione(model.getDataRegistrazioneProvvisoriaDa(), model.getDataRegistrazioneProvvisoriaA(), "data registrazione provvisoria") || ricercaValida;
		// SIAC-5292
		ricercaValida = checkCapitolo() || ricercaValida;
		
		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		checkCondition((model.getAnnoMovimento() == null && StringUtils.isBlank(model.getNumeroMovimento()))
				|| (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero movimento", ": i campi devono essere entrambi valorizzati o entrambi non valorizzati"));
		checkCondition(model.getNumeroSubmovimento() == null || model.getNumeroSubmovimento() == 0 || (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero submovimento", ": valorizzare anche anno e numero movimento"));
		checkCondition(model.getImportoDocumentoDa() == null || model.getImportoDocumentoA() == null || model.getImportoDocumentoA().compareTo(model.getImportoDocumentoDa()) >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importi da/a", "l'importo a non puo' essere inferiore all'importo da"));
		
		try {
			checkSoggetto();
			checkAttoAmministrativo();
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione dei campi: " + pve.getMessage());
		}
		
		//qualora vi sia un MovimentoGestione, ne controllo la presenza e validita'		
		checkMovimentoGestione();
		
		if (hasErrori()) {
			model.impostoDatiNelModel();
		}
	}
	
	/**
	 * Controllo su ulteriori campi per la validit&agrave; del form
	 * @return se i campi ulteriori siano validi
	 */
	protected boolean checkUlterioriCampi() {
		// Da implementare nelle sottoclassi se necessario
		return false;
	}

	/**
	 * Controlli per il soggetto
	 */
	private void checkSoggetto() {
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
	 * Controlli per l'atto amministrativo
	 */
	private void checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		if(aa == null) {
			log.debug(methodName, "Atto amministrativo non presente");
			return;
		}
		checkCondition((aa.getAnno() != 0 && aa.getNumero() != 0 && aa.getTipoAtto() != null && aa.getTipoAtto().getUid() != 0)
				|| (aa.getAnno() == 0 && aa.getNumero() == 0 && (aa.getTipoAtto() == null || aa.getTipoAtto().getUid() == 0)),
			ErroreCore.VALORE_NON_VALIDO.getErrore("Provvedimento", ": e' necessario specificare anno, numero e tipo per effettuare la ricerca"), true);
		
		// SIAC-4644, modifica: anche il tipo di atto deve essere obbligatorio
		if(aa.getAnno() == 0 || aa.getNumero() == 0 || aa.getTipoAtto() == null || aa.getTipoAtto().getUid() == 0) {
			log.debug(methodName, "Atto amministrativo non presente");
			model.setAttoAmministrativo(null);
			return;
		}
		// Prendo il tipoAtto
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
	protected boolean checkPianoDeiConti() {
		// Implementare nella classe che necessita del controllo
		// Restituisco false perche' non voglio considerarlo come condizione soddisfatta
		return false;
	}
	
	/**
	 * Controllo per le date di registrazione: se valorizzate entrambe, quella <code>da</code> non pu&oacute; essere successiva quella <code>a</code>.
	 * 
	 * @param dataDa la data di inizio
	 * @param dataA la data di fine
	 * @param name il nome del campo
	 * 
	 * @return <code>true</code> se la condizione sulle date &eacute; rispettata; <code>false</code> altrimenti
	 */
	private boolean checkDateRegistrazione(Date dataDa, Date dataA, String name) {
		if(dataDa == null || dataA == null) {
			// Almeno una data non e' presente. La condizione e' trivialmente non verificata
			return false;
		}
		
		boolean condition = !dataDa.after(dataA);
		checkCondition(condition, ErroreCore.FORMATO_NON_VALIDO.getErrore(name + " da, " + name + " a", "la data da non puo' essere successiva alla data a"));
		
		return condition;
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
		if(cap == null) {
			return sb.toString();
		}
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
	
	//SIAC-6002
	/**
	 * Controllo per il movimento di gestione
	 * @return se il movimento di gestione &eacute; valido
	 */
	protected boolean checkMovimentoGestione() {
		final String methodName = "checkMovimentoGestione";
		if(hasNoDatiMovimentoGestione(model.getAccertamento()) && hasNoDatiMovimentoGestione(model.getImpegno())) {
			log.debug(methodName, "Il movimento di gestione non e' stato fornito correttamente");
			return true;
		}
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			// Il tipo di evento non e' impostato. Esco con errore
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Movimento Gestione", "non e' possibile selezionare un movimento gestione se non e' selezionato un tipo di evento"));
			return false;
		}
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		
		// Il capitolo e' presente
		
		if(tipoEvento.isTipoSpesa()) {
			return checkImpegno();
		}
		if(tipoEvento.isTipoEntrata()) {
			return checkAccertamento();
		}
		log.debug(methodName, "Evento di tipo ne' entrata ne' spesa: ignoro il movimento di gestione");
		return true;
	}
	
	/**
	 * Controlla se non vi siano i dati per il movimento di gestione
	 * @param mg il movimento di gestione
	 * @return se il movimento di gestione non ha dati
	 */
	private boolean hasNoDatiMovimentoGestione(MovimentoGestione mg) {
		return mg == null || mg.getAnnoMovimento() == 0 || mg.getNumero() == null;
	}
	
	/**
	 * Controlla l'accertamento
	 * @return se l'accertamento sia valido
	 */
	protected boolean checkAccertamento() {
		final String methodName = "checkAccertamento";
		Accertamento accertamento =  model.getAccertamento();
		SubAccertamento subAccertamento = model.getSubAccertamento();
		
		if(accertamento == null || (accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null)) {
			return false;
		}
		
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(req);
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nell'invocazione della ricercaAccertamentoPerChiaveOttimizzato");
			addErrori(res);
			return false;
		}
		if(res.isFallimento() || res.getAccertamento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento()+"/"+accertamento.getNumero()));
			return false;
		}
		
		accertamento = res.getAccertamento();
		
		model.setMovimentoGestione(accertamento);
		
		if(subAccertamento != null && subAccertamento.getNumero() != null) {
			BigDecimal numero = subAccertamento.getNumero();
			// Controlli di validità sull'impegno
			subAccertamento = findSubAccertamentoLegatoAccertamentoByNumero(res.getAccertamento(), subAccertamento);
			if(subAccertamento == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero() + "-" + numero));
				return false;
			}
			model.setSubMovimentoGestione(subAccertamento);
		} 
		
		return true;
	}
	
	/**
	 * Trova il subAccertamento nell'elenco degli subaccertamenti dell'accertamento, se presente.
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
		
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(req);
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return false;
		}
		
		if(res.isFallimento() || res.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", impegno.getAnnoMovimento()+"/"+impegno.getNumero()));
			return false;
		}
		
		impegno = res.getImpegno();
		
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumero() != null) {
			BigDecimal numero = subImpegno.getNumero();
			// Controlli di validità sull'impegno
			subImpegno = findSubImpegnoLegatoImpegnoByNumero(res.getImpegno(), subImpegno);
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
	
	/**
	 * Calcola la chiave del movimento
	 * @param movimentoGestione il movimento
	 * @return la chiave del movimento
	 */
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
