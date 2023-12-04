/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AggiornaAllegatoAttoModel.TabVisualizzazione;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.InserisciAllegatoAttoModel;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.VerificaBloccoRORHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe di Action per l'inserimento dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/set/2014
 * @version 1.0.1 - 15/set/2014 - Aggiunta della classe base
 * @version 1.0.2 - 22/ott/2015 - CR-2466 - obbligatoriet&agrave; SAC per provvedimento automatico
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciAllegatoAttoAction extends GenericAllegatoAttoAction<InserisciAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4168060280742278347L;
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	
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
		checkCasoDUsoApplicabile(model.getTitolo());
		// Caricamento liste
		try {
			log.debug(methodName, "Caricamento liste classificatori");
			caricaListaTipoAtto();
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setDatiSensibili(Boolean.FALSE);
		model.setAllegatoAtto(allegatoAtto);
		caricaStrutturaUtente();
		
		// Se ho avuto un errore nel caricamento della lista, esco
		checkMetodoConclusoSenzaErrori();
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento della Struttura Amministrativo Contabile dell'utente
	 */
	private void caricaStrutturaUtente() {
		final String methodName = "caricaStrutturaUtente";
		if(sessionHandler.getAzioniConsentite() == null) {
			log.info(methodName, "Azioni consentite non caricate in sessione");
			return;
		}
		List<StrutturaAmministrativoContabile> sacs = ottieniListaStruttureAmministrativoContabiliDaSessione();
		for(AzioneConsentita ac : sessionHandler.getAzioniConsentite()) {
			// Considero solo la mia azione
			if(ac == null || ac.getAzione() == null
					|| (!AzioneConsentitaEnum.ALLEGATO_ATTO_INSERISCI_CENTRALE.getNomeAzione().equals(ac.getAzione().getNome())
							&& !AzioneConsentitaEnum.ALLEGATO_ATTO_INSERISCI_DECENTRATO.getNomeAzione().equals(ac.getAzione().getNome()))) {
				// Azione non di pertinenza
				continue;
			}
			for(StrutturaAmministrativoContabile sac : sacs) {
				if(sac != null && sac.getUid() != 0) {
					log.debug(methodName, "Impostazione SAC con uid " + sac.getUid() + " dell'utente");
					model.setStrutturaAmministrativoContabile(sac);
					return;
				}
			}
		}
	}

	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		model.setAttoAmministrativo(null);
		model.setStrutturaAmministrativoContabile(null);
		// Svuoto il boolean: problema di Struts2 che non lo ripopola a false
		model.getAllegatoAtto().setDatiSensibili(Boolean.FALSE);
		// Svuoto la data: vedasi sopra
		model.getAllegatoAtto().setDataScadenza(null);
	}
	
	/**
	 * Metodo di completamento dello step1 di inserimento dell'allegato atto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		final String methodName = "completeStep1";
		// Invocazione del servizio
		InserisceAllegatoAtto request = model.creaRequestInserisceAllegatoAtto();
		logServiceRequest(request);
		InserisceAllegatoAttoResponse response = allegatoAttoService.inserisceAllegatoAtto(request);
		logServiceResponse(response);
		
		// Controllo la presenza di eventuali errori nella response
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceAllegatoAtto.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Allegato inserito correttamente");
		// Imposto il dato nel model
		model.setAllegatoAtto(response.getAllegatoAtto());
		
		// Carico la lista degli elenchi
		model.setListaElencoDocumentiAllegato(response.getAllegatoAtto().getElenchiDocumentiAllegato());
		return SUCCESS;
	}
	
	/**
	 * Metodo di validazione per {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		Date now = new Date();
		AllegatoAtto aa = model.getAllegatoAtto();
		// Dati obbligatorii
		checkCondition(StringUtils.isNotBlank(aa.getCausale()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Causale"));
		checkCondition(model.getAttoAutomatico() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo atto"));
		
		// Validazione logica
		checkCondition(aa.getDataScadenza() == null || aa.getDataScadenza().after(now) || DateUtils.isSameDay(aa.getDataScadenza(), now),
				ErroreCore.DATE_INCONGRUENTI.getErrore("la Data scadenza non puo' essere antecedente la data odierna"));
		
		// Default per il flagRitenute
		if(aa.getFlagRitenute() == null) {
			aa.setFlagRitenute(Boolean.FALSE);
		}
		
		try {
			caricaTipoAttoALG();
			// Controllo il provvedimento
			checkProvvedimento();
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione del provvedimento: " + pve.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * Controlla la correttezza dei dati del provvedimento.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void checkProvvedimento() throws WebServiceInvocationFailureException{
		final String methodName = "checkProvvedimento";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		checkNotNull(aa, "Atto amministrativo", true);
		checkCondition(Boolean.TRUE.equals(model.getAttoAutomatico()) || aa.getAnno() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Il campo anno e' obbligatorio"));
		checkCondition(Boolean.TRUE.equals(model.getAttoAutomatico()) || (aa.getNumero() != 0 && model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("I campi numero e tipo sono obbligatori"));
		if(hasErrori()) {
			log.debug(methodName, "Errori nella validazione dell'atto amministrativo");
			return;
		}
		// Imposto la SAC
		if(model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0) {
			List<StrutturaAmministrativoContabile> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
			StrutturaAmministrativoContabile sac = ComparatorUtils.searchByUidWithChildren(lista, model.getStrutturaAmministrativoContabile());
			model.setStrutturaAmministrativoContabile(sac);
			model.getAttoAmministrativo().setStrutturaAmmContabile(sac);
		}
		
		if(aa.getNumero() == 0 && !hasErrori()){
			checkNotNullNorInvalidUid(model.getStrutturaAmministrativoContabile(), "Struttura Amministrativa", true);
			checkCondition(BilConstants.CODICE_CDC.getConstant().equals(model.getStrutturaAmministrativoContabile().getTipoClassificatore().getCodice()),
					ErroreCore.VALORE_NON_CONSENTITO.getErrore("Struttura Amministrativa", ": deve essere un SETTORE"),
				true);
			inserisciProvvedimentoAutomatico();
			return;
		}
		checkProvvedimentoEsistente();
		checkProvvedimentoNonCollegato();
	}
	
	/**
	 * Inserisce un provvedimento nel caso in cui il numero non venga specificato
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void inserisciProvvedimentoAutomatico() throws WebServiceInvocationFailureException {
		final String methodName = "inserisciProvvedimentoAutomatico";
		InserisceProvvedimento request = model.creaRequestInserisceProvvedimento();
		logServiceRequest(request);
		InserisceProvvedimentoResponse response = provvedimentoService.inserisceProvvedimento(request);
		logServiceResponse(response);
		// Controllo di non aver errori
		if(response.hasErrori()) {
			String msg = createErrorInServiceInvocationString(InserisceProvvedimento.class, response);
			log.info(methodName, msg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(msg);
		}
		model.setAttoAmministrativo(response.getAttoAmministrativoInserito());
		model.setMessaggioAggiuntivo(". Inserito provvedimento automatico: " + model.getAttoAmministrativo().getAnno()+"/"+model.getAttoAmministrativo().getNumero()+"/ALG");
		
	}
	
	private void caricaTipoAttoALG(){
		for(TipoAtto tipo : model.getListaTipoAtto()){
			if("ALG".equals(tipo.getCodice())){
				model.setTipoAttoALG(tipo);
				return;
			}
		}
	}

	/**
	 * Controlla che il provvedimento sia esistente ed univoco.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void checkProvvedimentoEsistente() throws WebServiceInvocationFailureException {
		final String methodName = "checkProvvedimentoEsistente";
		// Invocazione del servizio
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo di non aver errori
		if(response.hasErrori()) {
			String msg = createErrorInServiceInvocationString(RicercaProvvedimento.class, response);
			log.info(methodName, msg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(msg);
		}
		// Controllo di aver almeno un provvedimento
		checkCondition(!response.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);
		// Controllo di avere al piu' un provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getAttoAmministrativo().getStrutturaAmmContabile(), true);
		
		AttoAmministrativo aa = response.getListaAttiAmministrativi().get(0);
		checkCondition(StatoOperativoAtti.DEFINITIVO.equals(aa.getStatoOperativoAtti()),
				ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione Allegato atto", "Definitivo"));
		// Imposto l'atto nel model
		model.setAttoAmministrativo(aa);
		model.setStrutturaAmministrativoContabile(aa.getStrutturaAmmContabile());
	}

	/**
	 * Controlla che il provvedimento non sia gi&agrave; collegato a un elenco o a una quota
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void checkProvvedimentoNonCollegato() throws WebServiceInvocationFailureException {
		final String methodName = "checkProvvedimentoNonCollegato";
		// Invocazione del servizio
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimentoCollegato();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo di non aver errori
		if(response.hasErrori()) {
			String msg = createErrorInServiceInvocationString(RicercaProvvedimento.class, response);
			log.info(methodName, msg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(msg);
		}
		// Controllo di non avere atti amministrativi legati a documenti o ad elenchi
		checkCondition(response.getListaAttiAmministrativi().isEmpty(), ErroreFin.ATTO_GIA_ABBINATO.getErrore(""), true);
	}

	/**
	 * Metodo di ingresso nello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		caricaListaStatoOperativoElencoDocumenti();
		impostaInformazioneSuccesso(model.getMessaggioAggiuntivo());
		
		// Carico in sessione la lista degli elenchi collegati
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO, model.getListaElencoDocumentiAllegato());
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, null);
		return SUCCESS;
	}
	
	/**
	 * Ottiene la lista degli elenchi presenti nel model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaElenchi() {
		return SUCCESS;
	}

	/**
	 * Preparazione per l'esecuzione del metodo {@link #associaElenco()}.
	 */
	public void prepareAssociaElenco() {
		model.setElencoDocumentiAllegato(null);
	}
	
	/**
	 * Metodo per l'associazione dell'elenco all'allegato atto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String associaElenco() {
		final String methodName = "associaElenco";
		validazioneAssociazione();
		
		//SIAC-7470
		controlloBloccoROR();
		
		if(hasErrori()) {
			log.debug(methodName, "Errore di validazione dei dati");
			return SUCCESS;
		}
		
		AssociaElenco request = model.creaRequestAssociaElenco();
		logServiceRequest(request);
		AssociaElencoResponse response = allegatoAttoService.associaElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AssociaElenco.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		model.getListaElencoDocumentiAllegato().add(response.getElencoDocumentiAllegato());
		
		// Inserimento andato a buon fine
		return SUCCESS;
	}

	//SIAC-7470: cerco di recuperare prima i subdoc dall'allegato, e da questi l'impegno
	private void controlloBloccoROR(){
		if(model.getElencoDocumentiAllegato() != null && AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.BLOCCO_SU_LIQ_IMP_RESIDUI, sessionHandler.getAzioniConsentite())){
			ElencoDocumentiAllegato elenco = model.getElencoDocumentiAllegato();
			if(elenco != null){
				RicercaSinteticaQuoteElenco request = model.creaRequestRicercaSinteticaQuoteElenco();
				request.setElencoDocumentiAllegato(elenco);
				if(model.getAllegatoAtto() !=  null && model.getAllegatoAtto().getElencoSoggettiDurc() != null && !model.getAllegatoAtto().getElencoSoggettiDurc().isEmpty())
					request.setSoggetto(model.getAllegatoAtto().getElencoSoggettiDurc().get(0));
				request.setParametriPaginazione(new ParametriPaginazione(0,10));
				RicercaSinteticaQuoteElencoResponse response = allegatoAttoService.ricercaSinteticaQuoteElenco(request);
				if(response.getSubdocumenti() != null && !response.getSubdocumenti().isEmpty()){
					for(Subdocumento<?, ?> subdoc:response.getSubdocumenti()){
						if(subdoc != null && subdoc.getMovimentoGestione() != null && subdoc.getMovimentoGestione() instanceof Impegno){
							Impegno impegno = (Impegno)subdoc.getMovimentoGestione();
							if(impegno.getListaModificheMovimentoGestioneSpesa() == null)
								//è necessario ricaricare l'impegno per averlo completo delle Modifiche necessarie al controllo
								try{
									Impegno comodo = ricercaImpegnoPerChiaveOttimizzato(impegno);
									if(comodo != null)
										impegno = comodo;
								}catch(Exception e){
									checkCondition(false, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non recuperabile"));
								}
							//controllo bloccoROR
							boolean test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno,  model.getAnnoEsercizioInt());
							if(test){
								checkCondition(!test, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
							}else if(impegno.getElencoSubImpegni() != null && !impegno.getElencoSubImpegni().isEmpty()){
								for(int k = 0; k < impegno.getElencoSubImpegni().size(); k++){
									test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno.getElencoSubImpegni().get(k), model.getAnnoEsercizioInt());
									if(test)
										break;
								}
								if(test)
									checkCondition(!test, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Ricerca l'impegno per chiave.
	 * 
	 * @return l'impegno trovato
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	//SIAC-7470
	private Impegno ricercaImpegnoPerChiaveOttimizzato(Impegno impegno) throws WebServiceInvocationFailureException {
		//SIAC-6997-BLOCCOROR: se arrivo in questo punto mi è necessario ricercare l'impegno per avere la lista di MovimentoModifica
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(impegno);
		logServiceRequest(req);
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(RicercaImpegnoPerChiaveOttimizzato.class, res);
			addErrori(res);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(res.isFallimento()) {
			//il servizio e' fallito: lancio un errore
			String errorMsg = "Risultato ottenuto dal servizio RicercaImpegnoPerChiave: FALLIMENTO";
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(res.getImpegno() == null) {
			//il servizio non ha trovato l'impegno: lancio un errore
			String chiaveImpegno = req.getpRicercaImpegnoK().getAnnoEsercizio() + "/" + req.getpRicercaImpegnoK().getAnnoImpegno() + "/" + req.getpRicercaImpegnoK().getNumeroImpegno();
			String errorMsg = "Impegno non presente per chiave " + chiaveImpegno;
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", chiaveImpegno));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		impegno = res.getImpegno();
		// Default per le liste
		// IL SERVIZIO RESTITUISCE, SE HO SCELTO UN SUBIMPEGNO, UN IMPEGNO CON UN SOLO ELEMENTO 
		//IN impegno.elencoSubImpegni. ALTRIMENTI NON CARICO I SUB
		// GLI IMPEGNI E SUBIMPEGNI TROVATI DEVONO ESSERE IN STATO DEFINITIVO GIA' DAL PRINCIPIO. OPPURE LO CONTROLLO DOPO
		impegno.setElencoSubImpegni(defaultingList(impegno.getElencoSubImpegni()));

		// Inizializzo il capitolo se non gia' presente
		if(impegno.getCapitoloUscitaGestione() == null) {
			// Se il capitolo non e' stato impostato dal servizio, lo imposto io
			impegno.setCapitoloUscitaGestione(res.getCapitoloUscitaGestione());
		}
		return impegno;
	}
	
	/**
	 * Valida l'associazione tra elenco ed allegato.
	 */
	private void validazioneAssociazione() {
		checkCondition(model.getElencoDocumentiAllegato() != null && model.getElencoDocumentiAllegato().getUid() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elenco"));
	}
	
	/**
	 * Redirezione all'associazione del movimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String associaMovimento() {
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_INSERIMENTO);
		return SUCCESS;
	}
	
	//8853
	private void validateAssociaMovimento() {
		checkCondition(model.getElencoDocumentiAllegato() != null && model.getElencoDocumentiAllegato().getUid() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elenco"));
	}
	/**
	 * Redirezione all'associazione del documento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String associaDocumento() {
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_INSERIMENTO);
		return SUCCESS;
	}
	
	/**
	 * Redirezione all'aggiornamento dell'atto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneAggiornamentoAllegatoAtto() {
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_INSERIMENTO);
		// Imposto l'ingresso nella seconda pagina
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, TabVisualizzazione.ELENCO);
		return SUCCESS;
	}
	
}

