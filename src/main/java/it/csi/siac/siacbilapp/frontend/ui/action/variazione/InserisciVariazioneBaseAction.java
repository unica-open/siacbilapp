/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.InserisciVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step2.DefinisciVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazioneResponse;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.UtenteNonLoggatoException;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Classe astratta per la gestione delle variazioni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 15/10/2013
 */
public abstract class InserisciVariazioneBaseAction extends VariazioneBaseAction<InserisciVariazioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4016878117491637145L;
	
	private static final String ETICHETTA_ORGANO_AMM_DEFAULT = "GIUNTA";
	private static final String ETICHETTA_ORGANO_LEG_DEFAULT = "CONSIGLIO";

	//SIAC-6884
	private boolean isDecentrato;
		
	public boolean isDecentrato() {
		return isDecentrato;
	}

	public void setDecentrato(boolean isDecentrato) {
		this.isDecentrato = isDecentrato;
	}
	//END SIAC-6884
		
	@Override
	public void prepare() throws Exception {
		try {
			cleanErrori();
			cleanMessaggi();
			cleanInformazioni();
			
			//SIAC-6884
			Account account = sessionHandler.getAccount();
			boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
			setDecentrato(decentrato);
			model.setIsDecentrato(decentrato);
			//END SIAC-6884
			
		} catch(NullPointerException e) {
			throw new UtenteNonLoggatoException("Utente non più loggato", e);
		}
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	/*
	 * Primo ingresso nella action. Equivalente a enterStep1.
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	@AnchorAnnotation(value = "OP-GESC001-insVar", name = "Variazioni STEP 1")
	@SkipValidation
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	// Step n sarà della forma: 
	// Ingresso nello step: prepareEnterStepN -> enterStepN
	// Esecuzione dello step: prepareExecuteStepN -> validateExecuteStepN -> executestepN
	
	/* **** STEP 1 **** */
	
	/**
	 * Scelta.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String executeStep1() {
		final String methodName = "executeStep1";
		log.debugStart(methodName,"");
		
		// Le tre possibili scelte per il risultato
		final String codifiche = "codifiche";
		final String suffix = model.isGestioneUEB()? "UEB" : "";
		final String importi = "importi" + suffix;
		
		String sc = model.getScelta().isCodifiche() ? codifiche : importi;
		log.debug(methodName, "Risultato della scelta: " + sc);
		
		String cdu = sc.equals(codifiche) ? "OP-GESC040-insVarCod" : model.getIsDecentrato() ? "OP-GESC001-insVarDecentrato": "OP-GESC001-insVar";
		model.setCdu(cdu);
		log.debugEnd(methodName,"");
		return sc;
	}
	
	/**
	 * Validazione per il primo step.
	 */
	public void validateExecuteStep1() {
		checkNotNull(model.getScelta().getScelta(), "Tipo di variazione", true);
		checkCondition(model.getScelta().getScelta().isConsentita(sessionHandler.getAzioniConsentite()), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'operatore non e' abilitato a questa operazione."));
	}
	
	/* **** STEP 2 **** */
	
	/**
	 * Preparazione per lo step 2.
	 */
	private void preparePerLoStep2() {
		final String methodName = "preparePerLoStep2";
		log.debugStart(methodName, "");
		
		// Crea un nuovo model: utile per la navigazione
		DefinisciVariazioneModel modelStep2 = model.getDefinisci();
		
		// Caricamento delle combo (in base alla fase bilancio)
		
		caricaFaseBilancio();
		
		caricaTipoApplicazione();
		
		caricaTipoVariazione();
		
		// Tipo Atto Amministrativo
		log.debug(methodName, "Caricamento combo tipo provvedimento");
		// Chiamo il servizio
		TipiProvvedimento request = model.creaRequestTipiProvvedimento();
		TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
		// Imposto la lista nel model
		List<TipoAtto> listaTipoAtto = response.getElencoTipi();
		modelStep2.setListaTipoAtto(listaTipoAtto);
		log.debug(methodName, "Caricata combo tipo atto");
		
		// Anno della variazione
		log.debug(methodName, "Caricamento combo anno variazione");
		// Ottengo l'anno in corso dalla sessione
		int annoInCorso = Integer.parseInt(sessionHandler.getAnnoEsercizio());
		log.debug(methodName, "Caricata combo anno variazione");
		
		//SIAC-6177
		caricaEtichetteGiuntaConsiglio(modelStep2);
		
		model.setDefinisci(modelStep2);
		
		//SIAC-6884 - è stato appurato che in elenco sono presenti sia settori che direzioni, ma per ogni settore è sicuramente
		//presente anche la sua direzione di riferimento, pertanto è sufficiente filtrare l'elenco per tipo CDC/CDR (settore/direzione)
		if(model.getIsDecentrato()){
			List<StrutturaAmministrativoContabile> lista = sessionHandler.getAccount().getStruttureAmministrativeContabili();
			if(lista != null && !lista.isEmpty()){
				List<StrutturaAmministrativoContabile> elenco = new ArrayList<StrutturaAmministrativoContabile>();
				if(lista.size() == 1){
					//ho un solo elemento, certamente è una direzione
					model.getDefinisci().setDirezioneProponente(lista.get(0));
				}else if(lista.size() > 1){
					//devo filtrare l'elenco per codice
					for(int j = 0; j < lista.size(); j++){
						if(lista.get(j).getTipoClassificatore() != null && lista.get(j).getTipoClassificatore().getCodice() != null){
							if(lista.get(j).getTipoClassificatore().getCodice().equals(TipologiaClassificatore.CDR.name())){
								//si tratta di una Direzione
								elenco.add(lista.get(j));
							}
						}
					}
					model.getDefinisci().setListaDirezioni(elenco);
				}
			}
		}
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Carica etichette giunta consiglio.
	 *
	 * @param modelStep2 the model step 2
	 */
	private void caricaEtichetteGiuntaConsiglio(DefinisciVariazioneModel modelStep2) {
		Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
		String organoAmministrativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_AMM, ETICHETTA_ORGANO_AMM_DEFAULT);
		String organoLegislativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_LEG, ETICHETTA_ORGANO_LEG_DEFAULT);
		modelStep2.setEtichettaConsiglio(organoLegislativo);
		modelStep2.setEtichettaGiunta(organoAmministrativo);
	}
	
	/**
	 * @param gestioneLivelli
	 * @return
	 */
	private static String ottieniEtichettaByConfigurazioneEnte(Map<TipologiaGestioneLivelli, String> gestioneLivelli, TipologiaGestioneLivelli tipologiaGestioneLivelli, String etichettaDefault) {
		String parametroEnteOrganoAmm = gestioneLivelli.get(tipologiaGestioneLivelli);
		String organoAmministrativo = StringUtils.isNotBlank(parametroEnteOrganoAmm)? parametroEnteOrganoAmm : etichettaDefault;
		return organoAmministrativo;
	}

	/**
	 * Preparazione della action per l'ingresso nello step 2.
	 */
	public void prepareEnterStep2() {
		preparePerLoStep2();
	}
	
	/**
	 * Preparazione della action per l'esecuzione dello step 2.
	 */
	public void prepareExecuteStep2() {
		preparePerLoStep2();
	}
	
	/**
	 * Ingresso nello step 2.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "%{model.cdu}", name="Variazioni STEP 2", afterAction = true)
	public abstract String enterStep2();
	
	/**
	 * Esecuzione dello step 2.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String executeStep2() {
		return SUCCESS;
	}
	
	/**
	 * Validazione dell'esecuzione dello step 2.
	 */
	public void validateExecuteStep2() {
		final String methodName = "validateExecuteStep2";
		log.debugStart(methodName, "");
		
		DefinisciVariazioneModel model2 = model.getDefinisci();
		
		// Controllo i vari campi
		checkNotNull(model2.getApplicazione(), "Applicazione");
		checkNotNullNorEmpty(model2.getDescrizioneVariazione(), "Descrizione");
		checkNotNull(model2.getTipoVariazione(), "Tipo Variazione");
		//SIAC-6884
		if(model.getIsDecentrato()){
			//JIRA-7330
			checkNotNullNorInvalidUid(model2.getDirezioneProponente(), "Direzione Proponente");
			//checkNotNull(model2.getDirezioneProponente(), "Direzione Proponente");
			if(model2.getDirezioneProponente().getUid() > 0){
				if(model2.getListaDirezioni() != null && model2.getListaDirezioni().size() > 1){
					for(int j = 0; j < model2.getListaDirezioni().size(); j++){
						if(model2.getListaDirezioni().get(j).getUid() == model2.getDirezioneProponente().getUid()){
							model2.setDirezioneProponente(model2.getListaDirezioni().get(j));
						} 
					}
				} 
			}
		}
		//SIAC-6883
//		checkCondition(!(model2.getAnnoVariazioneAbilitato().booleanValue() && model2.getAnnoVariazione() == null), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno di competenza"));
		// Se il provvedimento e' stato scelto, controllo che non sia annullato
		checkProvvedimento();
		
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Effettua i controlli sul provvedimento, se presente.
	 */
	protected void checkProvvedimento() {
		AttoAmministrativo attoAmministrativoDaControllare = model.getDefinisci().getAttoAmministrativo();
		if( attoAmministrativoDaControllare== null || attoAmministrativoDaControllare.getUid() == 0) {
			//non e' stato digitato il provvedimento, esco.
			return;
		}
		AttoAmministrativo attoAmministrativo = checkProvvedimentoPresenteNonAnnullato(attoAmministrativoDaControllare);
		if( attoAmministrativo == null || attoAmministrativo.getUid() == 0) {
			//il provvedimento e' stato digitato ma non puo' essere associato alla variazione perche' non ha superato i controlli
			return;
		}
		// Popolo i dati del model
		model.getDefinisci().setAttoAmministrativo(attoAmministrativo);
		
	}

	/**
	 * Controlla che il provvedimento provvedimento sia presente su db e non sia annullato.
	 *
	 * @param attoAmministrativoDaControllare the atto amministrativo da controllare
	 * @return l'atto amministrativo con i dati caricati da db, se presente e non annullato. In caso contrario, ritorna null.
	 */
	protected AttoAmministrativo checkProvvedimentoPresenteNonAnnullato(AttoAmministrativo attoAmministrativoDaControllare) {
		//controllo se il provvedimento e' presente tra gli ultimi caricati da db
		List<AttoAmministrativo> listaProvvedimenti = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVEDIMENTO);
		AttoAmministrativo attoAmministrativo = ComparatorUtils.searchByUidEventuallyNull(listaProvvedimenti, attoAmministrativoDaControllare);
		
		if(attoAmministrativo == null){
			//devo chiamare il servizio per vedere se esiste
			RicercaProvvedimento req = model.creaRequestRicercaProvvedimento(attoAmministrativoDaControllare);
			RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);
			if (res.hasErrori()) {
				addErrori(res);
				return null;
			}
			if(res.getListaAttiAmministrativi().isEmpty()) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Atto amministrativo", "uid " + attoAmministrativoDaControllare.getUid()));
				return null;
			}
			attoAmministrativo = res.getListaAttiAmministrativi().get(0);
			checkCondition(attoAmministrativo != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Provvedimento", "con uid " + attoAmministrativoDaControllare.getUid()));
		}
		
		if(attoAmministrativo != null){
			checkCondition(!StatoOperativoAtti.ANNULLATO.getDescrizione().equalsIgnoreCase(attoAmministrativo.getStatoOperativo()), ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore(""));
		}
		return attoAmministrativo;
	}

	/* **** STEP 3 **** */
	
	/**
	 * Ingresso nello step 3.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "%{model.cdu}", name="Variazioni STEP 3", afterAction = true)
	public abstract String enterStep3();
	
	/**
	 * Esecuzione dello step 3.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public abstract String executeStep3();
	
	/* **** STEP 4 **** */
	
	/**
	 * Ingresso nello step 4.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "%{cdu}", name="Variazioni STEP 4", afterAction = true)
	public abstract String enterStep4();
	
	/* **** Metodi di utilita' **** */
	/**
	 * Carica la combo del tipo di applicazione basandosi sul bilancio fornito dal cruscotto.
	 */
	private void caricaTipoApplicazione() {
		final String methodName = "caricaTipoApplicazione";
		final Map<FaseBilancio, List<ApplicazioneVariazione>> mappaFasiApplicazioni = new EnumMap<FaseBilancio, List<ApplicazioneVariazione>>(FaseBilancio.class);
		
		// Inizializzo la mappa con una lista vuota
		for(FaseBilancio faseBilancio : FaseBilancio.values()) {
			mappaFasiApplicazioni.put(faseBilancio, new ArrayList<ApplicazioneVariazione>());
		}
		
		mappaFasiApplicazioni.put(FaseBilancio.PREVISIONE, Arrays.asList(ApplicazioneVariazione.PREVISIONE));
		mappaFasiApplicazioni.put(FaseBilancio.ESERCIZIO_PROVVISORIO, Arrays.asList(ApplicazioneVariazione.PREVISIONE, ApplicazioneVariazione.GESTIONE));
		mappaFasiApplicazioni.put(FaseBilancio.GESTIONE, Arrays.asList(ApplicazioneVariazione.GESTIONE));
		mappaFasiApplicazioni.put(FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO, Arrays.asList(ApplicazioneVariazione.GESTIONE));
		
		
		// Tipo Applicazione
		log.debug(methodName, "Caricamento combo tipo applicazione");
		List<ApplicazioneVariazione> listaTipoApplicazione = new ArrayList<ApplicazioneVariazione>();
		listaTipoApplicazione.addAll(mappaFasiApplicazioni.get(model.getFaseBilancio()));
		
		model.getDefinisci().setListaTipoApplicazione(listaTipoApplicazione);
		log.debug(methodName, "Caricata combo tipo applicazione");
	}

	/**
	 * Effettuo una ricerca di dettaglio per il bilancio
	 * */
	private void caricaFaseBilancio() {
 
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);
		
		Bilancio bilancio = response.getBilancio();
		FaseBilancio faseBilancio = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		model.setFaseBilancio(faseBilancio);
	}
	
	/**
	 * Carica il tipo di variazione.
	 */
	private void caricaTipoVariazione() {
		final String methodName = "caricaTipoVariazione";
		log.debug(methodName, "Caricamento combo tipo variazione");
		RicercaTipoVariazione request = model.creaRequestRicercaTipoVariazione();
		logServiceRequest(request);
		RicercaTipoVariazioneResponse response = variazioneDiBilancioService.ricercaTipoVariazione(request);
		logServiceResponse(response);
		
		List<TipoVariazione> listaTipoVariazione = ottieniListaTipiVariazione(response);
		
		model.getDefinisci().setListaTipoVariazione(listaTipoVariazione);
		log.debug(methodName, "Caricata combo tipo variazione");
	}

	/**
	 * Ottiene la lista dei tipi variazione filtrata a partire dalla lista ottenuta dal servizio
	 * **/
	private List<TipoVariazione> ottieniListaTipiVariazione(RicercaTipoVariazioneResponse response) {
		List<TipoVariazione> listaTipoVariazione = new ArrayList<TipoVariazione>();
		listaTipoVariazione.addAll(response.getElencoTipiVariazione());
		
		// I due casi differenti, a seconda della scelta effettuata nel passo 1
		if("IMPORTI".equals(model.getScelta().getSceltaSt())) {
			listaTipoVariazione.remove(TipoVariazione.VARIAZIONE_CODIFICA);
			if(FaseBilancio.PREVISIONE.equals(model.getFaseBilancio())){
				listaTipoVariazione.remove(TipoVariazione.VARIAZIONE_PER_ASSESTAMENTO);
			}
		} else {
			listaTipoVariazione.retainAll(Arrays.asList(TipoVariazione.VARIAZIONE_CODIFICA));
		}
		return listaTipoVariazione;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
//				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
