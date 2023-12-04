/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.GenericCronoprogrammaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.QuadroEconomicoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Classe astratta di Action per il Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/02/2014
 * @version 1.1.0 - 21/10/2015 - CR 2450 gestione capitolo
 * 
 * @param <M> la parametrizzazione del Model 
 *
 */
public abstract class GenericCronoprogrammaAction<M extends GenericCronoprogrammaModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5982698022780399256L;
	
	/** Serviz&icirc; dei classificatori bil */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; del progetto */
	@Autowired protected transient ProgettoService progettoService;
	
	@Autowired private transient ProvvedimentoService provvedimentoService;
	/** Serviz&icirc; per il capitolo di entrata previsione */
	@Autowired protected transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	
	/** Serviz&icirc; per il capitolo di entrata gestione */
	@Autowired protected transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	/** Serviz&icirc; per il capitolo di entrata gestione */
	@Autowired protected transient QuadroEconomicoService quadroEconomicoService;
	/**
	 * Effettua una ricerca di dettaglio del Progetto e carica il risultato.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void effettuaCaricamentoDettaglioProgetto() throws WebServiceInvocationFailureException {
		final String methodName = "effettuaCaricamentoDettaglioProgetto";
		
		log.debug(methodName, "Caricamento del dettaglio del programma");
		RicercaDettaglioProgetto request = model.creaRequestRicercaDettaglioProgetto();
		logServiceRequest(request);
		RicercaDettaglioProgettoResponse response = progettoService.ricercaDettaglioProgetto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioProgetto.class, response));
		}
		
		model.popolaProgettoaDallaResponse(response);
	}
	
	/**
	 * Carica la lista dei tipi di atto .
	 *
	 * @return the string
	 */
	//SIAC-6255
	public String  caricaListaQuadroEconomico() {
		final String methodName="caricaQuadroEconomico";
		List<QuadroEconomico> listaQuadroEconomico = sessionHandler.getParametro(BilSessionParameter.LISTA_QUADRO_ECONOMICO_CRONOPROGRAMMA);
		if(listaQuadroEconomico != null && !listaQuadroEconomico.isEmpty()) {
			model.setListaQuadroEconomico(listaQuadroEconomico);
			return SUCCESS;
		}
		RicercaSinteticaQuadroEconomico req = model.creaRequestRicercaSinteticaQuadroEconomico();
		RicercaSinteticaQuadroEconomicoResponse response = quadroEconomicoService.ricercaSinteticaQuadroEconomico(req);
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del metodo");
			model.setListaQuadroEconomico(new ArrayList<QuadroEconomico>());
			addErrori(response);
			return SUCCESS;
		}
		ListaPaginata<QuadroEconomico> quadriEconomici = response.getListQuadroEconomico();
		model.setListaQuadroEconomico(quadriEconomici);
		sessionHandler.setParametro(BilSessionParameter.LISTA_QUADRO_ECONOMICO_CRONOPROGRAMMA, quadriEconomici);
		return SUCCESS;
	}
	
	/**
	 * Caricamento della lista dei tipi atto
	 */
	protected void caricaListaTipiAtto() {
		final String methodName = "caricaListaTipiAtto";
		log.debug(methodName, "Caricamento della lista dei tipi di atto");
		

		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
				log.info(methodName, "listaTipoAtto.size:"+listaTipoAtto.size());
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}
	
	/**
	 * Effettua una ricerca dei classificatori e carica le liste.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void effettuaCaricamentoListeClassificatori() throws WebServiceInvocationFailureException {
		final String methodName = "effettuaCaricamentoListeClassificatori";
		log.debug(methodName, "Caricamento delle liste dei classificatori");
		
		caricamentoListeEntrata();
		caricamentoListeSpesa();
		
		// TODO: tipoFinanziamento?
	}
	
	/**
	 * Caricamento delle liste di spesa.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricamentoListeSpesa() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoListeSpesa";
		List<Missione> listaMissione = sessionHandler.getParametro(BilSessionParameter.LISTA_MISSIONE);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaMissione == null || listaTitoloSpesa == null) {
			// Carico da servizio
			log.debug(methodName, "Necessario ottenere le liste da servizio");
			LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
			// Controllo gli errori
			listaMissione = response.getClassificatoriMissione();
			listaTitoloSpesa = response.getClassificatoriTitoloSpesa();
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_MISSIONE, listaMissione);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, listaTitoloSpesa);
		}
		
		model.setListaMissione(listaMissione);
		model.setListaTitoloSpesa(listaTitoloSpesa);
	}
	
	/**
	 * Caricamento delle liste di entrata.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricamentoListeEntrata() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoListeEntrata";
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		
		if(listaTitoloEntrata == null) {
			// Carico da servizio
			log.debug(methodName, "Necessario ottenere le liste da servizio");
			LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
			listaTitoloEntrata = response.getClassificatoriTitoloEntrata();
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, listaTitoloEntrata);
		}
		
		model.setListaTitoloEntrata(listaTitoloEntrata);
	}

	/**
	 * Ottiene la response per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice rispetto cui ottenere la response
	 * 
	 * @return la response ottenuta
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice.getConstant());
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(LeggiClassificatoriByTipoElementoBil.class, response));
		}
		return response;
	}
	
	/**
	 * Validazione dell'anagrafica del Cronoprogramma.
	 */
	protected void validaAnagrafica() {
		checkNotNull(model.getCronoprogramma(), "Cronoprogramma", true);
		checkNotNullNorEmpty(model.getCronoprogramma().getCodice(), "Versione");
		checkNotNullNorEmpty(model.getCronoprogramma().getDescrizione(), "Descrizione");
		checkNotNull(model.getCronoprogramma().getCronoprogrammaDaDefinire(), "Cronoprogramma da definire");
	}
	

	/**
	 * Validazione dei Dettagli collegati al Cronoprogramma
	 * 
	 * @throws ParamValidationException nel caso in cui almeno una validazione non sia andata a buon fine
	 */
	protected void validaDettagli(){
		checkCondition(!model.getListaDettaglioEntrataCronoprogramma().isEmpty() && !model.getListaDettaglioUscitaCronoprogramma().isEmpty(),
				ErroreBil.CRONOPROGRAMMA_INCOMPLETO.getErrore(), true);
		
		BigDecimal totaleSpese = model.calcolaTotaleSpese();
		BigDecimal totaleEntrate = model.calcolaTotaleEntrate();
		
		warnCondition(totaleSpese.compareTo(totaleEntrate) == 0, ErroreBil.TOTALE_SPESE_DIVERSO_DAL_TOTALE_ENTRATE.getErrore(), true);
		warnCondition(model.checkAnniImportiSpeseCoerentiConEntrate(), ErroreBil.SPESE_CON_ANNO_RIFERIMENTO_FINANZIAMENTO_NON_PRESENTE.getErrore(), true);
		warnCondition(model.checkImportiSpesePerAnnoCoerenteConImportiEntratePerAnno(), ErroreBil.TOTALE_DELLE_SPESE_SUPERIORE_AL_TOTALE_DEI_FINANZIEMENTI_DI_RIFERIMENTO.getErrore(), true);
	}
	
	/**
	 * Ingresso nella pagina.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enter() {
		return SUCCESS;
	}
	
	/* ***** Metodi AJAX ***** */
	
	/**
	 * Ottiene le liste dei dettagli di entrata e di uscita.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListeDettaglio() {
		return SUCCESS;
	}
	
	/**
	 * Inserisce un dettaglio di Entrata del Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciDettaglioCronoprogrammaEntrata() {
		List<TipologiaTitolo> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		model.popolaDettaglioEntrataCronoprogramma(listaInSessione);
		
		model.getListaDettaglioEntrataCronoprogramma().add(model.getDettaglioEntrataCronoprogramma());
		model.sortListaDettaglioEntrataByAnnoDiCompetenza(model.getListaDettaglioEntrataCronoprogramma());
		model.setDettaglioEntrataCronoprogramma(null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #inserisciDettaglioCronoprogrammaEntrata()}
	 */
	public void validateInserisciDettaglioCronoprogrammaEntrata() {
		String methodName = "validateInserisciDettaglioCronoprogrammaEntrata";
		DettaglioEntrataCronoprogramma dec = model.getDettaglioEntrataCronoprogramma();
		//SIAC-8791 - //task-62
		//checkNotNull(dec.getNumeroCapitolo(), "Capitolo Entrata");
		//checkNotNull(dec.getNumeroArticolo(), "Articolo Entrata");
		checkNotNullNorEmpty(dec.getDescrizioneCapitolo(), "Descrizione Entrata");
		checkNotNull(dec.getAnnoCompetenza(), "Anno");
		checkNotNull(dec.getStanziamento(), "Valore previsto");
		//task-171
		//checkNotNull(dec.getIsAvanzoAmministrazione(), "Avanzo di amministrazione");
		boolean isNecessarioControlloTipologia = false;
		try {
			isNecessarioControlloTipologia = necessitaControlloTitoloTipologia(dec);
		} catch(WebServiceInvocationFailureException wsife){
			log.warn(methodName, wsife.getMessage());
		}

		if(!isNecessarioControlloTipologia) {
			return;
		}
		
		checkNotNullNorInvalidUid(dec.getTitoloEntrata(), "Titolo");
		checkNotNullNorInvalidUid(dec.getTipologiaTitolo(), "Tipologia");
	}
	
	/**
	 * Controlla se si necessita del controllo su tipologia e titolo. <br/>
	 * <strong>SIAC-4103</strong><br/>
	 * Non obbligatorio se da capitolo esistente di tipo AAM. Oppure da capitolo Non esistente ma di tipo Avanzo
	 * 
	 * @param dec il dettaglio di entrata
	 * @return <code>true</code> nel caso in cui il controllo su titolo e tipologia sia necessario; <code>false</code> altrimenti
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private boolean necessitaControlloTitoloTipologia(DettaglioEntrataCronoprogramma dec) throws WebServiceInvocationFailureException {
		final String methodName = "necessitaControlloTitoloTipologia";
		if(dec.getCapitolo() == null || dec.getCapitolo().getUid() == 0) {
			// Non esistente. Controllo il tipo avanzo
			log.debug(methodName, "Capitolo non esistente: controllo sull'avanzo di amministrazione");
			return !Boolean.TRUE.equals(dec.getIsAvanzoAmministrazione());
		}
		
		log.debug(methodName, "Capitolo esistente. Controllo sul tipo");
		// Il capitolo e' impostato. Devo ottenerne i dati
		if(model.isCollegatoAProgettoDiPrevisione()) {
			return checkCapitoloEntrataPrevisione(dec);
		}
		
		return checkCapitoloEntrataGestione(dec);
	}

	/**
	 * @param dec
	 * @return
	 * @throws WebServiceInvocationFailureException
	 */
	private boolean checkCapitoloEntrataPrevisione(DettaglioEntrataCronoprogramma dec)
			throws WebServiceInvocationFailureException {
		RicercaDettaglioModulareCapitoloEntrataPrevisione req = model.creaRequestRicercaDettaglioModulareCapitoloEntrataPrevisione();
		RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.ricercaDettaglioModulareCapitoloEntrataPrevisione(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioModulareCapitoloEntrataPrevisione.class, res));
		}
		
		CapitoloEntrataPrevisione cep = res.getCapitoloEntrataPrevisione();
		checkCondition(cep != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo di entrata", "uid " + dec.getCapitolo().getUid()), true);
		checkCondition(cep.getCategoriaCapitolo() != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("il capitolo non presenta una categoria"));
		
		return !BilConstants.CODICE_CATEGORIA_CAPITOLO_AVANZO_AMMINISTRAZIONE.getConstant().equalsIgnoreCase(cep.getCategoriaCapitolo().getCodice());
	}
	
	//SIAC-6255
	private boolean checkCapitoloEntrataGestione(DettaglioEntrataCronoprogramma dec)
			throws WebServiceInvocationFailureException {
		RicercaDettaglioModulareCapitoloEntrataGestione req = model.creaRequestRicercaDettaglioModulareCapitoloEntrataGestione();
		RicercaDettaglioModulareCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaDettaglioModulareCapitoloEntrataGestione(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioModulareCapitoloEntrataGestione.class, res));
		}
		
		CapitoloEntrataGestione cep = res.getCapitoloEntrataGestione();
		checkCondition(cep != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo di entrata", "uid " + dec.getCapitolo().getUid()), true);
		checkCondition(cep.getCategoriaCapitolo() != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("il capitolo non presenta una categoria"));
		
		return !BilConstants.CODICE_CATEGORIA_CAPITOLO_AVANZO_AMMINISTRAZIONE.getConstant().equalsIgnoreCase(cep.getCategoriaCapitolo().getCodice());
	}
	
	/**
	 * Preparazione per il metodo {@link #inserisciDettaglioCronoprogrammaUscita()}.
	 */
	public void prepareInserisciDettaglioCronoprogrammaUscita() {
		model.setCronoprogrammaDaDefinire(null);
		model.setDettaglioUscitaCronoprogramma(null);
	}

	/**
	 * Inserisce un dettaglio di Entrata del Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciDettaglioCronoprogrammaUscita() {
		List<Programma> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_PROGRAMMA);
		model.popolaDettaglioUscitaCronoprogramma(listaInSessione);
		
		model.getListaDettaglioUscitaCronoprogramma().add(model.getDettaglioUscitaCronoprogramma());
		model.sortListaDettaglioUscitaByAnnoEntrataSpesa(model.getListaDettaglioUscitaCronoprogramma());
		model.setDettaglioUscitaCronoprogramma(null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #inserisciDettaglioCronoprogrammaUscita()}.
	 */
	public void validateInserisciDettaglioCronoprogrammaUscita() {
		DettaglioUscitaCronoprogramma duc = model.getDettaglioUscitaCronoprogramma();
		//SIAC-8791 - //task-62
		//checkNotNull(duc.getNumeroCapitolo(), "Capitolo Spesa");
		//checkNotNull(duc.getNumeroArticolo(), "Articolo Spesa");
		checkNotNullNorEmpty(duc.getDescrizioneCapitolo(), "Descrizione Spesa");
		checkNotNull(duc.getStanziamento(), "Valore previsto");
		checkNotNullNorInvalidUid(duc.getMissione(), "Missione");
		checkNotNullNorInvalidUid(duc.getProgramma(), "Programma");
		checkNotNullNorInvalidUid(duc.getTitoloSpesa(), "Titolo");
		
		if(shouldCheckAnnoUscita()) {
			checkNotNull(duc.getAnnoCompetenza(), "Anno spesa");
			checkNotNull(duc.getAnnoEntrata(), "Anno entrata");
			checkCondition(duc.getAnnoCompetenza() == null || duc.getAnnoEntrata() == null || duc.getAnnoCompetenza().compareTo(duc.getAnnoEntrata()) >= 0,
					ErroreBil.COMPETENZA_SPESA_ANTECEDENTE_COMPETENZA_ENTRATA.getErrore());
		}
	}
	
	/**
	 * Controlla se sia necessario effettuare i controlli per l'anno nel caso delle uscite
	 * @return <code>true</code> se il controllo sia da effettuare; <code>false</code> altrimenti
	 */
	private boolean shouldCheckAnnoUscita() {
		return !Boolean.TRUE.equals(model.getCronoprogrammaDaDefinire());
	}
	
	/**
	 * Preparazione per il metodo {@link #consultaTotaliEntrata()}.
	 */
	public void prepareConsultaTotaliEntrata() {
		model.setCronoprogrammaDaDefinire(null);
	}

	/**
	 * Consulta i totali di Entrata del Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaTotaliEntrata() {
		if(Boolean.TRUE.equals(model.getCronoprogrammaDaDefinire())) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il cronoprogramma e' da definire, la consultazione degli importi non e' disponibile"));
			return INPUT;
		}
		
		model.popolaMappaTotaliEntrata();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #consultaTotaliUscita()}.
	 */
	public void prepareConsultaTotaliUscita() {
		model.setCronoprogrammaDaDefinire(null);
	}
	
	/**
	 * Consulta i totali di Uscita del Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaTotaliUscita() {
		if(Boolean.TRUE.equals(model.getCronoprogrammaDaDefinire())) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il cronoprogramma e' da definire, la consultazione degli importi non e' disponibile"));
			return INPUT;
		}
		model.popolaMappaTotaliUscita();
		return SUCCESS;
	}
	
	/**
	 * Popola il form di aggiornamento del dettaglio di entrata del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String popolaAggiornamentoDettaglioCronoprogrammaEntrata() {
		caricaListaTipologiaTitolo();
		return SUCCESS;
	}
	
	/**
	 * Popola il form di aggiornamento del dettaglio di uscita del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String popolaAggiornamentoDettaglioCronoprogrammaUscita() {
		caricaListaProgramma();
		return SUCCESS;
	}
	
	/**
	 * Annulla l'aggiornamento del dettaglio di entrata del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaAggiornamentoDettaglioCronoprogrammaEntrata() {
		model.setDettaglioEntrataCronoprogramma(null);
		return SUCCESS;
	}
	
	/**
	 * Annulla l'aggiornamento del dettaglio di uscita del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaAggiornamentoDettaglioCronoprogrammaUscita() {
		model.setDettaglioUscitaCronoprogramma(null);
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaDettaglioCronoprogrammaEntrata()}.
	 */
	public void prepareAggiornaDettaglioCronoprogrammaEntrata() {
		DettaglioEntrataCronoprogramma dettaglioEntrataCronoprogramma = model.getDettaglioEntrataCronoprogramma();
		if (dettaglioEntrataCronoprogramma == null) {
			return;
		}
		dettaglioEntrataCronoprogramma.setTitoloEntrata(null);
		dettaglioEntrataCronoprogramma.setTipologiaTitolo(null);
	}
	
	/**
	 * Aggiorna il dettaglio di entrata del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaDettaglioCronoprogrammaEntrata() {
		List<TipologiaTitolo> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		model.popolaDettaglioEntrataCronoprogramma(listaInSessione);
		
		DettaglioEntrataCronoprogramma dettaglioEntrataDaAggiornare = model.getDettaglioEntrataCronoprogramma();
		Integer indice = model.getIndiceDettaglioNellaLista();
		
		if(indice == null || indice.intValue() == -1) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessun dettaglio con uid = " + dettaglioEntrataDaAggiornare.getUid() + 
					" presente nella lista dei dettagli di entrata associati al cronoprogramma"));
			return SUCCESS;
		}
		
		model.getListaDettaglioEntrataCronoprogramma().set(indice.intValue(), dettaglioEntrataDaAggiornare);
		
		model.setDettaglioEntrataCronoprogramma(null);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaDettaglioCronoprogrammaEntrata()}
	 */
	public void validateAggiornaDettaglioCronoprogrammaEntrata() {
		validateInserisciDettaglioCronoprogrammaEntrata();
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaDettaglioCronoprogrammaUscita()}.
	 */
	public void prepareAggiornaDettaglioCronoprogrammaUscita() {
		model.setCronoprogrammaDaDefinire(null);
		model.setDettaglioUscitaCronoprogramma(null);
	}
	
	/**
	 * Aggiorna il dettaglio di uscita del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaDettaglioCronoprogrammaUscita() {
		List<Programma> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_PROGRAMMA);
		model.popolaDettaglioUscitaCronoprogramma(listaInSessione);
		
		DettaglioUscitaCronoprogramma dettaglioUscitaDaAggiornare = model.getDettaglioUscitaCronoprogramma();
		Integer indice = model.getIndiceDettaglioNellaLista();
		
		if(indice == null || indice.intValue() == -1) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessun dettaglio con uid = " + dettaglioUscitaDaAggiornare.getUid() + 
					" presente nella lista dei dettagli di entrata associati al cronoprogramma"));
			return SUCCESS;
		}
		
		model.getListaDettaglioUscitaCronoprogramma().set(indice.intValue(), dettaglioUscitaDaAggiornare);
		
		model.setDettaglioUscitaCronoprogramma(null);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaDettaglioCronoprogrammaUscita()}
	 */
	public void validateAggiornaDettaglioCronoprogrammaUscita() {
		validateInserisciDettaglioCronoprogrammaUscita();
	}
	
	/**
	 * Verifica la quadratura del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String verificaQuadratura() {
		final String methodName = "verificaQuadratura";
		model.calcolaQuadraturaCronoprogramma();
		
		Map<String, BigDecimal> quadratura = model.getQuadratura();
		log.debug(methodName, "Entrate: " + quadratura.get("entrata") + " -- Uscite: " + quadratura.get("uscita")
			+ " --- Differenza: " + quadratura.get("differenza"));
		
		return SUCCESS;
	}
	
	/**
	 * Calcola data fine lavori.
	 *
	 * @return the string
	 */
	//SIAC-6255
	public String calcolaDataFineLavori() {
		Date dataFineLavori = model.calcolaDataFineLavori(model.getDurataInGiorni(), model.getDataInizioLavori());
		if(dataFineLavori == null) {
			model.setDataFineLavoriString("");
			return SUCCESS;
		}
		model.setDataFineLavoriString(FormatUtils.formatDate(dataFineLavori));
		return SUCCESS;
		
	}
	
	/**
	 * Cancella un dettaglio di Entrata dal Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String cancellaDettaglioCronoprogrammaEntrata();
	
	/**
	 * Cancella un dettaglio di Entrata dal Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String cancellaDettaglioCronoprogrammaUscita();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Carica la lista della Tipologia Titolo a partire dal Titolo Entrata.
	 */
	private void caricaListaTipologiaTitolo() {
		final String methodName = "caricaListaTipologiaTitolo";
		if(model.getDettaglioEntrataCronoprogramma() == null || model.getDettaglioEntrataCronoprogramma().getTitoloEntrata() == null) {
			log.debug(methodName, "Nessun titolo entrata per cui caricare la tipologia");
			return;
		}
		LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(model.getDettaglioEntrataCronoprogramma().getTitoloEntrata().getUid());
		LeggiClassificatoriBilByIdPadreResponse response = classificatoreBilService.leggiClassificatoriByIdPadre(request);
		model.setListaTipologiaTitolo(response.getClassificatoriTipologiaTitolo());
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPOLOGIA_TITOLO, response.getClassificatoriTipologiaTitolo());
	}
	
	/**
	 * Carica la lista del Programma a partire dalla Missione.
	 */
	private void caricaListaProgramma() {
		final String methodName = "caricaListaProgramma";
		if(model.getDettaglioUscitaCronoprogramma() == null || model.getDettaglioUscitaCronoprogramma().getMissione() == null) {
			log.debug(methodName, "Nessuna missione per cui caricare il programma");
			return;
		}
		LeggiClassificatoriBilByIdPadre request = model.creaRequestLeggiClassificatoriBilByIdPadre(model.getDettaglioUscitaCronoprogramma().getMissione().getUid());
		LeggiClassificatoriBilByIdPadreResponse response = classificatoreBilService.leggiClassificatoriByIdPadre(request);
		model.setListaProgramma(response.getClassificatoriProgramma());
		sessionHandler.setParametro(BilSessionParameter.LISTA_PROGRAMMA, response.getClassificatoriProgramma());
	}
	
	/**
	 * 
	 * @param listaCronoprogrammaResponse  e' la lista di tutti cronoprogrammi collegati al progettto
	 * @return la lista dei cronoprogrammi collegati al progetto con anno bilancio=anno dell'esericizio
	 */
	protected List<Cronoprogramma> filtraListaCronoprogrammaPerAnnoDiBilancio(List<Cronoprogramma> listaCronoprogrammaResponse) {
		
		List<Cronoprogramma> listaCronoprogrammaFiltrata = new ArrayList<Cronoprogramma>();
		
		//listaCronoprogrammaFiltrat=filtro la listaCronoprogrammaResponse  in base all'anno di bilancio in corso
		Bilancio bilancioAttuale = model.getBilancio();
		for (Cronoprogramma c : listaCronoprogrammaResponse) {
			if(c.getBilancio().getAnno() == bilancioAttuale.getAnno() && StatoOperativoCronoprogramma.VALIDO.equals(c.getStatoOperativoCronoprogramma())) {
				listaCronoprogrammaFiltrata.add(c);
			}
		}
		return listaCronoprogrammaFiltrata;
	}
	
	// CR-2450
	
	/**
	 * Preparazione per il metodo {@link #apriInserisciDettaglioEntrata()}
	 */
	public void prepareApriInserisciDettaglioEntrata() {
		DettaglioEntrataCronoprogramma dettaglioEntrataCronoprogramma = new DettaglioEntrataCronoprogramma();
		dettaglioEntrataCronoprogramma.setIsAvanzoAmministrazione(Boolean.FALSE);
		model.setDettaglioEntrataCronoprogramma(dettaglioEntrataCronoprogramma);
	}
	
	/**
	 * Segnaposto per l'apertura del dettaglio di entrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriInserisciDettaglioEntrata() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #apriInserisciDettaglioUscita()}
	 */
	public void prepareApriInserisciDettaglioUscita() {
		model.setDettaglioUscitaCronoprogramma(null);
	}
	
	/**
	 * Segnaposto per l'apertura del dettaglio di uscita.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriInserisciDettaglioUscita() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #apriAggiornaDettaglioEntrata()}
	 */
	public void prepareApriAggiornaDettaglioEntrata() {
		model.setDettaglioEntrataCronoprogramma(null);
		model.setIndiceDettaglioNellaLista(null);
	}
	
	/**
	 * Popola il form di aggiornamento del dettaglio di uscita del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String apriAggiornaDettaglioEntrata() {
		// Caricamento del dettaglio
		DettaglioEntrataCronoprogramma dec = findDettaglioByRow(model.getListaDettaglioEntrataCronoprogramma(), model.getIndiceDettaglioNellaLista());
		model.setDettaglioEntrataCronoprogramma(dec);
		
		caricaListaTipologiaTitolo();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #apriAggiornaDettaglioUscita()}
	 */
	public void prepareApriAggiornaDettaglioUscita() {
		model.setDettaglioUscitaCronoprogramma(null);
		model.setIndiceDettaglioNellaLista(null);
	}
	
	/**
	 * Popola il form di aggiornamento del dettaglio di uscita del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String apriAggiornaDettaglioUscita() {
		// Caricamento del dettaglio
		DettaglioUscitaCronoprogramma duc = findDettaglioByRow(model.getListaDettaglioUscitaCronoprogramma(), model.getIndiceDettaglioNellaLista());
		model.setDettaglioUscitaCronoprogramma(duc);
		
		caricaListaProgramma();
		return SUCCESS;
	}
	
	/**
	 * Ottiene il dettaglio dalla lista fornita a partire dall'indice fornito
	 * @param lista  la lista da cui ottenere il dettaglio
	 * @param indice l'indice da ottenere
	 * @return il dettaglio all'indice fornito, se valido; <code>null</code> altrimenti
	 */
	private <D extends DettaglioBaseCronoprogramma> D findDettaglioByRow(List<D> lista, Integer indice) {
		final String methodName = "findDettaglioByRow";
		if(indice == null || lista == null) {
			log.info(methodName, "Null value: non restituisco alcunche'");
			return null;
		}
		int idx = indice.intValue();
		if(idx < 0 || idx >= lista.size()) {
			log.info(methodName, "Indice della lista non valido - non vale al condizione 0 <= " + idx + " < " + lista.size());
			return null;
		}
		return lista.get(idx);
	}

	/**
	 * Risultato concernente i dettagli del cronopogramma
	 * @author Alessandro Marchino
	 *
	 */
	public static class DettagliCronoprogrammaJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 2169836079429564569L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, listaDettaglioEntrataCronoprogramma.*, listaDettaglioUscitaCronoprogramma.*";

		/** Empty default constructor */
		public DettagliCronoprogrammaJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
