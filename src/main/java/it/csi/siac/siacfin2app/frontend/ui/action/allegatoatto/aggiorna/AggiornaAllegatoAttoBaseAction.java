/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.aggiorna;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.ElaborazioniService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttiva;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttivaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AggiornaAllegatoAttoModel;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AggiornaAllegatoAttoModel.TabVisualizzazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegatoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * Classe di Action per l'aggiornamento dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.1.0 - 22/ott/2014 - Definita come classe base, subclassata per i var&icirc; tabs
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AggiornaAllegatoAttoBaseAction extends GenericAllegatoAttoAction<AggiornaAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4952085131870676752L;
	
	/** Nome del model per la sessione */
	protected static final String MODEL_SESSION_NAME = "AggiornaAllegatoAtto";

	/** Il service per il soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ElaborazioniService elaborazioniService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Pulisco il model
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		// Controllo sull'applicabilita' del caso d'uso riferentesi allo stato del bilancio
		checkCasoDUsoApplicabile(model.getTitolo());
		// Distrugge le vecchie ancore
		destroyAnchors();
		
		EsisteElaborazioneAttiva reqEEA = model.creaRequestEsisteElaborazioneAttiva();
		// Check elaborazione attiva
		EsisteElaborazioneAttivaResponse resEEA = elaborazioniService.esisteElaborazioneAttiva(reqEEA);
		if(resEEA.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(EsisteElaborazioneAttiva.class, resEEA));
			throwExceptionFromErrori(resEEA.getErrori());
		}

		if(resEEA.getEsisteElaborazioneAttiva()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Elaborazione per allegato atto in corso - uid: " + model.getUidAllegatoAtto());
			throwExceptionFromErrore(ErroreBil.ERRORE_GENERICO.getErrore("Elaborazione per allegato atto in corso"));
		}
		
		// Caricamento del dettaglio
		RicercaDettaglioAllegatoAtto req = model.creaRequestRicercaDettaglioAllegatoAtto();
		logServiceRequest(req);
		RicercaDettaglioAllegatoAttoResponse res = allegatoAttoService.ricercaDettaglioAllegatoAtto(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAllegatoAtto.class, res));
			throwExceptionFromErrori(res.getErrori());
		}
		
		// Controllo sullo stato dell'Allegato Atto
		checkStatoAllegatoCompatibileConOperazione(res.getAllegatoAtto());
		
		// Imposto i dati nel model
		AllegatoAtto allegatoAtto = res.getAllegatoAtto();
		model.setAllegatoAtto(allegatoAtto);
		model.setAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		model.setListaElencoDocumentiAllegato(allegatoAtto.getElenchiDocumentiAllegato());
		
		// Carico gli elenchi
		caricaListaStatoOperativoElencoDocumenti();
		
		// Imposto l'eventuale default per la pagina di ingresso
		defaultPaginaIngresso();
		
		// SIAC-5021
		impostaAbilitazioneStampa();
		impostaAbilitazioneInvioFlux();
		
		// SIAC-5410
		caricaListeSiope();
		
		//SIAC-5589
		checkAndObtainListaClassiSoggetto();
				
		//SIAC-7470
		//controlloBloccoROR(allegatoAtto);
				
		// Leggo i dati dell'azione precedente
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		
		caricamentoListaContoTesoreria();
		
		
		// Imposto i dati in sessione
		impostaDatiInSessione();
		return SUCCESS;
	}
	
	/*/SIAC-7470: cerco di recuperare prima i subdoc dall'allegato, e da questi l'impegno
	private void controlloBloccoROR(AllegatoAtto allegatoAtto){
		if(allegatoAtto.getElenchiDocumentiAllegato() != null && !allegatoAtto.getElenchiDocumentiAllegato().isEmpty()){
			ElencoDocumentiAllegato elenco = allegatoAtto.getElenchiDocumentiAllegato().get(0);
			if(elenco != null){
				RicercaSinteticaQuoteElenco request = model.creaRequestRicercaSinteticaQuoteElenco();
				request.setElencoDocumentiAllegato(elenco);
				if(allegatoAtto.getElencoSoggettiDurc() != null && !allegatoAtto.getElencoSoggettiDurc().isEmpty())
					request.setSoggetto(allegatoAtto.getElencoSoggettiDurc().get(0));
				request.setParametriPaginazione(new ParametriPaginazione(0,10));
				RicercaSinteticaQuoteElencoResponse response = allegatoAttoService.ricercaSinteticaQuoteElenco(request);
				if(response.getSubdocumenti() != null && !response.getSubdocumenti().isEmpty()){
					for(Subdocumento subdoc:response.getSubdocumenti()){
						if(subdoc != null){
							if(subdoc.getMovimentoGestione() != null && subdoc.getMovimentoGestione() instanceof Impegno){
								Impegno impegno = (Impegno)subdoc.getMovimentoGestione();
								if(impegno.getListaModificheMovimentoGestioneSpesa() == null){
									//considerare l'ipotesi di inserire un caricamento dell'impegno (provare con modifiche presenti tipo 2019/4)
								}
								//controllo bloccoROR
								boolean test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno,  model.getAnnoEsercizioInt());
								if(test){
									throwExceptionFromErrori(Arrays.asList(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile")));
								}else if(impegno.getElencoSubImpegni() != null && !impegno.getElencoSubImpegni().isEmpty()){
									for(int k = 0; k < impegno.getElencoSubImpegni().size(); k++){
										test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno.getElencoSubImpegni().get(k), model.getAnnoEsercizioInt());
										if(test)
											break;
									}
									if(test){
										throwExceptionFromErrori(Arrays.asList(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile")));
									}
								}
							}
						}
					}
				}
			}
		}
	}*/
	
//	protected void checkAndObtainListaContoTesoreria() {
//		List<ContoTesoreria> listaContoTesoreria = model.getListaContoTesoreria();
//		if(!listaContoTesoreria.isEmpty()) {
//			return;
//		}
//		
//		LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
//		LeggiContiTesoreriaResponse response = preDocumentoSpesaService.leggiContiTesoreria(request);
//		if(!response.hasErrori()) {
//			listaContoTesoreria = response.getContiTesoreria();
//			model.setListaContoTesoreria(listaContoTesoreria);
//		}
//	}
//	
	/**
	 * Caricamento delle liste per il SIOPE plus
	 */
	private void caricaListeSiope() {
		if(!model.getListaSiopeAssenzaMotivazione().isEmpty()) {
			// Ho gia' la lista nel model
			return;
		}
		
		RicercaCodifiche req = model.creaRequestRicercaCodifiche(SiopeAssenzaMotivazione.class);
		RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
		
		if(!res.hasErrori()) {
			model.setListaSiopeAssenzaMotivazione(res.getCodifiche(SiopeAssenzaMotivazione.class));
		}
		
	}

	/**
	 * Controlla se la stampa sia abilitata
	 */
	private void impostaAbilitazioneStampa() {
		// La stampa e' abilitata se l'utente puo' effettuare la ricerca
		boolean isStampaAbilitato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_RICERCA, sessionHandler.getAzioniConsentite())
				// e l'allegato e' collegato ad almeno una quota di spesa
				&& Boolean.TRUE.equals(model.getAllegatoAtto().getIsAssociatoAdAlmenoUnaQuotaSpesa());
		model.setStampaAbilitato(isStampaAbilitato);
	}

	/**
	 * Controlla se l'invio a FLUX sia abilitato
	 */
	private void impostaAbilitazioneInvioFlux() {
		AllegatoAtto aa = model.getAllegatoAtto();
		// L'invio a flux e' abilitato se l'utente ha la profilazione corretta
		boolean invioFluxAbilitato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_INVIA, sessionHandler.getAzioniConsentite())
				// Se l'allegato e' in stato COMPLETATO o DA_COMPLETARE    o PARZIALMENTE_CONVALIDATO (SIAC-6295)
				&& (StatoOperativoAllegatoAtto.COMPLETATO.equals(aa.getStatoOperativoAllegatoAtto()) || StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(aa.getStatoOperativoAllegatoAtto()) || StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(aa.getStatoOperativoAllegatoAtto()))
				// Se l'ente gestisce l'integrazione ad atti
				&& model.isIntegrazioneAttiLiquidazione()
				// Se associato ad almeno una quota di spesa
				&& Boolean.TRUE.equals(aa.getIsAssociatoAdAlmenoUnaQuotaSpesa())
				//task-14
				&& !abilitaChecklist(); 
		model.setInvioFluxAbilitato(invioFluxAbilitato);
	}

	/**
	 * Distrugge le famiglie delle ancore non pi&uacute; utilizzate.
	 */
	private void destroyAnchors() {
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_INSERIMENTO);
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_ASSOCIAZIONE_DOCUMENTO);
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_ASSOCIAZIONE_MOVIMENTO);
	}

	/**
	 * Controlla che lo stato dell'AllegatoAtto sia compatibile con l'operazione richiesta.
	 * 
	 * @param allegatoAtto l'allegato il cui stato &eacute; da controllare
	 */
	private void checkStatoAllegatoCompatibileConOperazione(AllegatoAtto allegatoAtto) {
		final String methodName = "checkStatoAllegatoCompatibileConOperazione";
		List<AzioneConsentita> listaAzioneConsentita = sessionHandler.getAzioniConsentite();
		if(!checkDecentratoValido(allegatoAtto, listaAzioneConsentita) && !checkCentraleValido(allegatoAtto, listaAzioneConsentita)) {
			log.info(methodName, "Stato dell'allegato con uid " + allegatoAtto.getUid() + " non valido: " + allegatoAtto.getStatoOperativoAllegatoAtto());
			throwExceptionFromErrori(Arrays.asList(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore()));
		}
	}

	/**
	 * Controlla se l'azione sia valida per il decentrato.
	 * 
	 * @param listaAzioneConsentita le azioni consentite
	 * @param allegatoAtto          l'allegato da validare
	 * 
	 * @return <code>true</code> se l'allegato e' valido per l'azione decentrata; <code>false</code> in caso contrario.
	 *         <br>
	 *         Restituisce <code>true</code> se l'azione non &eacute; decentrata
	 */
	private boolean checkDecentratoValido(AllegatoAtto allegatoAtto, List<AzioneConsentita> listaAzioneConsentita) {
		// Per l’azione OP-COM-aggAttoAllegatoDec si deve verificare anche che lo stato dell’atto selezionato sia 'DA COMPLETARE'.
		return !AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_AGGIORNA_DECENTRATO, listaAzioneConsentita)
			|| StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(allegatoAtto.getStatoOperativoAllegatoAtto());
	}

	/**
	 * Controlla se l'azione sia valida per il centrale.
	 * 
	 * @param listaAzioneConsentita le azioni consentite
	 * @param allegatoAtto          l'allegato da validare
	 * 
	 * @return <code>true</code> se l'allegato e' valido per l'azione centrale; <code>false</code> in caso contrario.
	 *         <br>
	 *         Restituisce <code>true</code> se l'azione non &eacute; centrale
	 */
	private boolean checkCentraleValido(AllegatoAtto allegatoAtto, List<AzioneConsentita> listaAzioneConsentita) {
		// Per l’azione OP-COM-aggAttoAllegatoCen anche che lo stato dell’atto selezionato sia diverso da 'ANNULLATO', 'RIFIUTATO', 'DA COMPLETARE' e 'CONVALIDATO'.
		return !AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_AGGIORNA_CENTRALE, listaAzioneConsentita)
			|| (!StatoOperativoAllegatoAtto.ANNULLATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.RIFIUTATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto()));
	}
	
	/**
	 * Fornisce un default per la pagina di ingresso del caso d'uso.
	 */
	private void defaultPaginaIngresso() {
		TabVisualizzazione tab = sessionHandler.getParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO);
		if(tab == null) {
			tab = TabVisualizzazione.DATI;
		}
		model.setTabVisualizzazione(tab);
		// Svuoto il parametro
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, null);
	}
	
	/**
	 * Impostazione di alcuni dati in sessione, per 
	 */
	private void impostaDatiInSessione() {
		// In sessione appongo: LISTA ELENCO DOCUMENTI, LISTA DATI SOGGETTO
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO, model.getListaElencoDocumentiAllegato());
		sessionHandler.setParametro(BilSessionParameter.LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO, model.getAllegatoAtto().getDatiSoggettiAllegati());
	}
	
	/**
	 * Redirezione verso l'associazione del Documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaDocumento() {
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, TabVisualizzazione.ELENCO);
		return SUCCESS;
	}
	
	/**
	 * Redirezione verso l'associazione del Movimento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaMovimento() {
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, TabVisualizzazione.ELENCO);
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle liste dei dati soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio.
	 */
	protected void caricaListaDatiSoggettoAllegato() throws WebServiceInvocationFailureException {
		RicercaDatiSoggettoAllegato req = model.creaRequestRicercaDatiSoggettoAllegato();
		logServiceRequest(req);
		RicercaDatiSoggettoAllegatoResponse res = allegatoAttoService.ricercaDatiSoggettoAllegato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDatiSoggettoAllegato.class, res));
		}
		// Ho i dati. Li imposto in sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO, res.getDatiSoggettiAllegati());
	}
	
	/**
	 * Validazione per le variazioni sul soggetto allegato.
	 */
	protected void validazioneModificheDatiSoggettoAllegato() {
		DatiSoggettoAllegato dsa = model.getDatiSoggettoAllegato();
		
		// Controllo che non siano nulli
		checkNotNull(dsa, "dati soggetto", true);
		
		checkCondition(dsa.getDataSospensione() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data sospensione"));
		checkCondition(StringUtils.isNotBlank(dsa.getCausaleSospensione()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("causale sospensione"));
		
		checkCondition(dsa.getDataSospensione() == null || dsa.getDataSospensione().compareTo(new Date()) <= 0,
				ErroreCore.DATE_INCONGRUENTI.getErrore("la data di sospensione non puo' essere futura"));
		checkCondition(dsa.getDataSospensione() == null || dsa.getDataRiattivazione() == null || dsa.getDataRiattivazione().compareTo(dsa.getDataSospensione()) >= 0,
			ErroreCore.DATE_INCONGRUENTI.getErrore("la data di riattivazione non puo' essere successiva alla data di sospensione"));
	}
	
	/**
	 * Impostazione nel model dei dati soggetto allegato a partire dalla response
	 * @param res la response del servizio di ricerca
	 */
	protected void impostaDatiSoggettoAllegatoDaRicerca(RicercaDatiSospensioneAllegatoAttoResponse res) {
		final String methodName = "impostaDatiSoggettoAllegatoDaRicerca";
		if(!CollectionUtil.hasAtMostOneSingleElement(res.getCausaleSospensioneAllegato())
				|| !CollectionUtil.hasAtMostOneSingleElement(res.getDataSospensione())
				|| !CollectionUtil.hasAtMostOneSingleElement(res.getDataRiattivazione())) {
			log.debug(methodName, "Ho piu' di un elemento. Non poss determinare univocamente i dati.");
			model.setDatiSoggettoAllegatoDeterminatiUnivocamente(false);
			return;
		}
		
		model.setDatiSoggettoAllegatoDeterminatiUnivocamente(true);
		
		DatiSoggettoAllegato dsa = new DatiSoggettoAllegato();
		dsa.setCausaleSospensione(CollectionUtil.getFirst(res.getCausaleSospensioneAllegato()));
		dsa.setDataSospensione(CollectionUtil.getFirst(res.getDataSospensione()));
		dsa.setDataRiattivazione(CollectionUtil.getFirst(res.getDataRiattivazione()));
		model.setDatiSoggettoAllegato(dsa);
	}
	
	/**
	 * Classe di Result specifica per i dati di sospensione del soggetto
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 29/08/2017
	 *
	 */
	public static class DatiSospensioneSoggettoAllegatoJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 7507866337885283256L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, datiSoggettoAllegato.*, datiSoggettoAllegatoDeterminatiUnivocamente";
		/** Empty default constructor */
		public DatiSospensioneSoggettoAllegatoJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
	
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 */
	protected void checkAndObtainListaClassiSoggetto() {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			//la lista non e' presente in sessione, la devo ricaricare da servizio
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				listaClassiSoggetto = response.getListaClasseSoggetto();
				//ordino la lista per codice
				ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
				//setto la lista ordinata in sessione per successivi utilizzi
				sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
			}
		}
		//setto la lista in sessione, sia che io l'abbia ottenuta da servizio o da sessione
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
	
}
