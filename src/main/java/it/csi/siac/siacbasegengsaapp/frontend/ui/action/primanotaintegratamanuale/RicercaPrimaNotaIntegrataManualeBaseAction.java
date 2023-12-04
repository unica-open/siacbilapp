/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.RicercaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di action comune FIN-GSA per la ricerca della prima nota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 * @param <M> la tipizzazione del model
 */
public abstract class RicercaPrimaNotaIntegrataManualeBaseAction<M extends RicercaPrimaNotaIntegrataManualeBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1922611835837024577L;
	/** Chiave per l'accertamento */
	private static final String CHOICE_ACCERTAMENTO = "Accertamento";
	/** Chiave per l'impegno */
	private static final String CHOICE_IMPEGNO = "Impegno";

	@Autowired private transient CausaleService causaleService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ContoService contoService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient PrimaNotaService primaNotaService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		caricaListe();
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
	 * @return il parametro di sessione corrispondente alla lista delle causali (GSA o GEN)
	 */
	protected abstract BilSessionParameter getBilSessionParameterListeCausali();
	
	/**
	 * Caricamento delle liste per l'interazione utente.
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	protected void caricaListe() {
		try {
			caricaListaStatoOperativoPrimaNotaLibera();
			caricaListaEvento();
			caricaListaCausale();
			caricaListaClassi();
			caricaListaTitoli();
		} catch (WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife);
		}
	}

	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassi() {
		RicercaCodifiche reqRC = model.creaRequestRicercaClassi();
		logServiceRequest(reqRC);
		RicercaCodificheResponse resRC = codificheService.ricercaCodifiche(reqRC);
		logServiceResponse(resRC);
		
		if(!resRC.hasErrori()){
			model.setListaClassi(resRC.getCodifiche(ClassePiano.class));
		} else{
			addErrori(resRC);
		}
	}


	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio
	 * di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(
			String codice) {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		return res;
	}

	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaTitoli() {
		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		List<TitoloEntrata> listaTE = response.getClassificatoriTitoloEntrata();
		
		model.setListaTitoloEntrata(listaTE);
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		List<Missione> listaMissione = responseSpesa.getClassificatoriMissione();
		model.setListaTitoloSpesa(listaTS); 
		model.setListaMissione(listaMissione);
	}


	/**
	 * Caricamento della lista delle causali.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaCausale() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCausale";
		List<CausaleEP> listaCausaliEp = sessionHandler.getParametro(getBilSessionParameterListeCausali());
		// Se non ho i dati, effettuo la ricerca

		if (listaCausaliEp == null) {
			// recuperare da servizio la lista causali
			RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
			RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);

			if (res.hasErrori()) {
				String errorMsg = createErrorInServiceInvocationString(RicercaSinteticaModulareCausale.class, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			listaCausaliEp = res.getCausali();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(getBilSessionParameterListeCausali(), listaCausaliEp);
		}

		model.setListaCausaleEP(listaCausaliEp);
		
	}

	/**
	 * Caricamento della lista degli stati operativi per la prima nota libera.
	 */
	private void caricaListaStatoOperativoPrimaNotaLibera() {
		model.setListaStatoOperativoPrimaNota(Arrays.asList(StatoOperativoPrimaNota.values()));
	}

	/**
	 * Caricamento della lista degli eventi.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaEvento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaEvento";

		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		if (listaEvento == null) {
			log.debug(methodName, "Lista di Evento non presente in sessione. Caricamento da servizio");
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(Evento.class);
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);

			if (res.hasErrori()) {
				addErrori(res);
				String msgErrore = createErrorInServiceInvocationString(RicercaCodifiche.class, res);
				throw new WebServiceInvocationFailureException(msgErrore);
			}

			listaEvento = res.getCodifiche(Evento.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, listaEvento);
		}
		// verifica tipo causale tipo evento libera
		for (Iterator<Evento> it = listaEvento.iterator(); it.hasNext();) {
			Evento e = it.next();
			if (e == null || e.getTipoEvento() == null || !BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(e.getTipoEvento().getCodice())){
				it.remove();
			}
		}
		model.setListaEvento(listaEvento);
		
		if(!listaEvento.isEmpty()) {
			model.setEvento(listaEvento.get(0));
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
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
			throw new GenericFrontEndMessagesException(createErrorInServiceInvocationString(RicercaDettaglioBilancio.class, res));
		}
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}

	/**
	 * Controlla la validita del codice conto.
	 * 
	 * @return <code>true</code> se il codice conto &eacute; impostato e
	 *         corretto; <code>false</code> altrimenti
	 */
	private boolean controlloCodiceConto() {
		final String methodName = "controlloCodiceConto";
		if (model.getConto() == null || StringUtils.isBlank(model.getConto().getCodice())) {
			log.debug(methodName, "Nessun conto selezionato");
			return false;
		}

		RicercaSinteticaConto req = model.creaRequestRicercaSinteticaConto(model.getConto());
		logServiceRequest(req);
		RicercaSinteticaContoResponse res = contoService.ricercaSinteticaConto(req);
		logServiceResponse(res);

		if (res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaConto.class, res));
			addErrori(res);
			return false;
		}

		checkCondition(res.getTotaleElementi() > 0,
				ErroreCore.ENTITA_INESISTENTE.getErrore("Conto", model.getConto().getCodice()), true);
		Conto conto = res.getConti().get(0);
		// Imposto il conto nel model
		model.setConto(conto);

		return true;
	}

	/**
	 * Controlla la validita dele date inserite.
	 * 
	 * @return <code>true</code> se le date sono corrette; <code>false</code>
	 *         altrimenti
	 */
	private boolean controlloDate() {
		if (model.getDataRegistrazioneDA() == null || model.getDataRegistrazioneA() == null) {
			return false;
		}

		checkCondition(!model.getDataRegistrazioneDA().after(model.getDataRegistrazioneA()),
				ErroreCore.DATE_INCONGRUENTI.getErrore("La data di registrazione DA deve essere antecedente alla data di registrazione A"),
				true);

		return true;
	}

	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		checkCondition(model.getPrimaNotaLibera() != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), true);

		boolean ricercaValida = checkCampoValorizzato(model.getPrimaNotaLibera().getNumero(), "Numero Prima nota")
				|| checkCampoValorizzato(model.getPrimaNotaLibera().getNumeroRegistrazioneLibroGiornale(),
						"Numero Prima nota")
				|| checkPresenzaIdEntita(model.getEvento()) || checkPresenzaIdEntita(model.getCausaleEP())
				|| checkStringaValorizzata(model.getConto().getCodice(), "Codice conto")
				|| checkCampoValorizzato(model.getPrimaNotaLibera().getStatoOperativoPrimaNota(), "Stato operativo")
				|| checkStringaValorizzata(model.getPrimaNotaLibera().getDescrizione(), "Descrizione Prima nota")
				|| checkCampoValorizzato(model.getDataRegistrazioneDA(), "Data Registrazione Definitiva Prima nota DA")
				|| checkCampoValorizzato(model.getDataRegistrazioneA(), "Data Registrazione Definitiva Prima nota A")
				|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaDA(), "Data Registrazione Provvisorio Prima nota DA")
				|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaA(), "Data Registrazione Provvisorio Prima nota A")
				|| checkCampoValorizzato(model.getImporto(), "Importo Prima nota")
				|| checkCampoValorizzato(model.getMissione(), "Missione")
				|| checkCampoValorizzato(model.getProgramma(), "Programma")
				|| checkUlterioriCampi();

		ricercaValida = controlloCodiceConto() || ricercaValida;
		ricercaValida = controlloDate() || ricercaValida;
		ricercaValida = checkMovimentoGestione() || ricercaValida;

		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
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
	 * Controlli sul movgest
	 * @return se la ricerca del movgest sia valida
	 */
	private boolean checkMovimentoGestione() {
		// Validazione movimento gestione
		if(StringUtils.isBlank(model.getTipoMovimento())) {
			// Tipo di movimento non presente
			return false;
		}
		checkCondition(CHOICE_ACCERTAMENTO.equals(model.getTipoMovimento()) || CHOICE_IMPEGNO.equals(model.getTipoMovimento()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo movimento di gestione", "deve essere pari a \"" + CHOICE_IMPEGNO + "\" ovvero \"" + CHOICE_ACCERTAMENTO + "\""));
		if(model.getAnnoMovimentoGestione() == null && model.getNumeroMovimentoGestione() == null) {
			// Dati non presenti
			return false;
		}
		checkCondition(model.getAnnoMovimentoGestione() != null && model.getNumeroMovimentoGestione() != null,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Dati movimento gestione", "anno e numero devono essere entrambi presenti o assenti"));
		
		if(CHOICE_ACCERTAMENTO.equals(model.getTipoMovimento())) {
			// Ricerca e validazione dell'accertamento
			return validateAccertamento();
		} else if(CHOICE_IMPEGNO.equals(model.getTipoMovimento())) {
			// Ricerca e validazione dell'impegno
			return validateImpegno();
		}
		return false;
	}

	/**
	 * Validazione dell'accertamento
	 * @return se l'avvertamento &eacute; valido
	 */
	private boolean validateAccertamento() {
		final String methodName = "validateAccertamento";
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nella ricerca dell'accertamento");
			addErrori(res);
			return false;
		}
		if(res.getAccertamento() == null) {
			log.info(methodName, "Accertamento non presente");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione()));
			return false;
		}
		model.setAccertamento(res.getAccertamento());
		if(model.getNumeroSubmovimentoGestione() != null) {
			// Cerca sub
			SubAccertamento sa = new SubAccertamento();
			sa.setNumeroBigDecimal(new BigDecimal(model.getNumeroSubmovimentoGestione().intValue()));
			sa = ComparatorUtils.findByNumeroMovimentoGestione(res.getAccertamento().getElencoSubAccertamenti(), sa);
			
			checkCondition(sa != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione() + "-" + model.getNumeroSubmovimentoGestione()));
			model.setSubAccertamento(sa);
		} 
		return !hasErrori();
	}
	
	/**
	 * Validazione dell'impegno
	 * @return se l'impegno &eacute; valido
	 */
	private boolean validateImpegno() {
		final String methodName = "validateImpegno";
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nella ricerca dell'impegno");
			addErrori(res);
			return false;
		}
		if(res.getImpegno() == null) {
			log.info(methodName, "Impegno non presente");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione()));
			return false;
		}
		model.setImpegno(res.getImpegno());
		if(model.getNumeroSubmovimentoGestione() != null) {
			// Cerca sub
			SubImpegno si = new SubImpegno();
			si.setNumeroBigDecimal(new BigDecimal(model.getNumeroSubmovimentoGestione().intValue()));
			si = ComparatorUtils.findByNumeroMovimentoGestione(res.getImpegno().getElencoSubImpegni(), si);
			
			checkCondition(si != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione() + "-" + model.getNumeroSubmovimentoGestione()));
			model.setSubImpegno(si);
		} 
		return !hasErrori();
	}

	/**
	 * Ottiene la lista dei conti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		impostaEventi();
		
		RicercaSinteticaPrimaNotaIntegrataManuale req = model.creaRequestRicercaSinteticaPrimaNotaIntegrataManuale();
		logServiceRequest(req);

		RicercaSinteticaPrimaNotaIntegrataManualeResponse res = primaNotaService.ricercaSinteticaPrimaNotaIntegrataManuale(req);
		logServiceResponse(res);
		if (res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaPrimaNotaIntegrataManuale.class, res));
			addErrori(res);
			return INPUT;
		}

		log.debug(methodName, "Numero di risultati trovati: " + res.getTotaleElementi());
		if (res.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}

		// Impostazione dati in sessione
		impostaParametriInSessione(req, res);

		return SUCCESS;

	}

	/**
	 * Impostazione degli eventi nel model per la ricerca corretta
	 */
	private void impostaEventi() {
		final String methodName = "impostaEventi";
		List<Evento> eventi = new ArrayList<Evento>();
		model.setEventi(eventi);
		// Ho sempre l'evento EXTR
		eventi.add(model.getEvento());
		
		String tipoMovimento = model.getTipoMovimento();
		if(StringUtils.isBlank(tipoMovimento)) {
			log.debug(methodName, "Tipo movimento non impostato: non aggiungo movimenti");
			return;
		}
		
		// Ordine SUBIMPEGNO, IMPEGNO, SUBACCERTAMENTO, ACCERTAMENTO
		MovimentoGestione mg = ObjectUtils.firstNonNull(model.getSubImpegno(), model.getImpegno(), model.getSubAccertamento(), model.getAccertamento());
		if(mg != null && mg.getUid() != 0) {
			// Devo calcolare l'evento corretto
			Class<?> clazz = mg.getClass();
			log.debug(methodName, "Recupero l'evento corrispondente all'entita' di tipo " + clazz.getSimpleName());
			String newCode = "EXTR-" + clazz.getSimpleName().replaceAll("[a-z]", "");
			
			Evento evento = findEventoByCode(newCode);
			eventi.add(evento);
			return;
		}
		
		// Non ho il movimento di gestione. Recupero tutti i dati per testata e sub
		log.debug(methodName, "Recuper gli eventi per il tipo movimento " + tipoMovimento);
		String codeTestata = "EXTR-" + tipoMovimento.replaceAll("[a-z]", "");
		String codeSub = "EXTR-S" + tipoMovimento.replaceAll("[a-z]", "");
		
		Evento eventoTestata = findEventoByCode(codeTestata);
		Evento eventoSub = findEventoByCode(codeSub);
		eventi.add(eventoTestata);
		eventi.add(eventoSub);
	}
	
	/**
	 * Recupera l'evento di dato codice
	 * @param codice il codice dell'evento
	 * @return l'evento
	 */
	private Evento findEventoByCode(String codice) {
		Evento evento = ComparatorUtils.findByCodice(model.getListaEvento(), codice);
		if(evento == null || evento.getCodice() == null) {
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile reperire l'evento con codice " + codice).getTesto());
		}
		return evento;
	}

	/**
	 * Impostazione dei parametri in sessione
	 * @param req la request
	 * @param res la response
	 */
	private void impostaParametriInSessione(RicercaSinteticaPrimaNotaIntegrataManuale req, RicercaSinteticaPrimaNotaIntegrataManualeResponse res) {

		BilSessionParameter bilSessionParameterRequest = getBilSessionParameterRequest();
		BilSessionParameter bilSessionParameterRisultati = getBilSessionParameterRisultati();

		sessionHandler.setParametro(bilSessionParameterRisultati, res.getPrimeNote());
		sessionHandler.setParametroXmlType(bilSessionParameterRequest, req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
}
