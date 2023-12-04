/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna.BaseAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrata;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrataFactory;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;
/**
 * Classe di action per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseAggiornaPrimaNotaIntegrataBaseAction<M extends BaseAggiornaPrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {
	
	/**Per la serializzazione */
	private static final long serialVersionUID = -829984116468700177L;
	
	@Autowired private transient PrimaNotaService primaNotaService;
	/** Serviz&icirc; delle codifiche */
	@Autowired private transient CodificheService codificheService;
	private static final Map<TipoCollegamento, String> MAPPING_DATI_FINANZIARI;
		
	static {
		Map<TipoCollegamento, String> tmp = new HashMap<TipoCollegamento, String>();
		tmp.put(TipoCollegamento.IMPEGNO, "impegno");
		tmp.put(TipoCollegamento.SUBIMPEGNO, "impegno");
		tmp.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, "impegno");
		tmp.put(TipoCollegamento.ACCERTAMENTO, "accertamento");
		tmp.put(TipoCollegamento.SUBACCERTAMENTO, "accertamento");
		tmp.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, "accertamento");
		tmp.put(TipoCollegamento.LIQUIDAZIONE, "liquidazione");
		tmp.put(TipoCollegamento.DOCUMENTO_SPESA, "documentoSpesa");
		tmp.put(TipoCollegamento.SUBDOCUMENTO_SPESA, "documentoSpesa");
		tmp.put(TipoCollegamento.DOCUMENTO_ENTRATA, "documentoEntrata");
		tmp.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, "documentoEntrata");
		
		MAPPING_DATI_FINANZIARI = Collections.unmodifiableMap(tmp);
	}
	
	/**
	 * Popolamento del model
	 * @param primaNota la prima nota tramite cui popolare il model
	 */
	protected abstract void popolaModel(PrimaNota primaNota);
	
	/**
	 * Controlla che le scritture siano corrette. Devono essere:
	 * <ul>
	 *     <li>
	 *         presenti almeno 2 scritture su conti con segni differenti (pu&ograve; essere anche lo stesso conto devono essere diversi i segni),
	 *         altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Devono essere presenti almeno due conti con segni differenti.')&gt;</code>
	 *     </li>
	 *     <li>
	 *         il totale dare deve essere uguale al totale avere: altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Il totale DARE deve essere UGUALE al totale AVERE.')&gt;</code>
	 *     </li>
	 *     <li>
	 *         SIAC-5802: eliminato il controllo.
	 *         <br/>
	 *         <del>Se l'algoritmo viene richiamato da aggiorna prima  nota integrata occorre verificare che la somma degli importi dare e/o avere 
	 *         non differisca dall'importo del movimento che ha generato prima nota; quindi se la prima nota &eacute; stata generata da un impegno 
	 *         che ha importo 100 e in aggiorna vengono inseriti tre conti diversi, la ripartizione degli importi sui tre conti deve fare 
	 *         comunque 100, se non fosse cos&igrave; visualizzare il messaggio <code>&lt;COR_ERR_0044
	 *         Operazione non consentita ('Prima nota con importo difforme rispetto al dato finanziario')&gt;</code></del>
	 *     </li>
	 * </ul>
	 */
	protected abstract void checkScrittureCorrette();
	
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
	public String execute() {
		final String methodName = "execute";
		checkCasoDUsoApplicabile();
		caricaListaClassi();
		PrimaNota primaNota;
		try {
			primaNota = caricaPrimaNotaDaServizio();
			caricaDatiUlteriori(primaNota);
		} catch(WebServiceInvocationFailureException wsife) {
			// Salvo tutto ed esco
			setErroriInSessionePerActionSuccessiva();
			setMessaggiInSessionePerActionSuccessiva();
			setInformazioniInSessionePerActionSuccessiva();
			return SUCCESS;
		}
		leggiEventualiInformazioniAzionePrecedente();
		log.debug(methodName, "Trovata primaNota corrispondente all'uid " + primaNota.getUid());
		popolaModel(primaNota);
		calcolaTotali();
		return SUCCESS;
	}
	
	/**
	 * Calcolo dei totali
	 */
	protected void calcolaTotali(){
		//implementazione vuota di default
	}
	
	/**
	 * Caricamento di dati ulteriori
	 * @param primaNota la prima nota
	 * @throws WebServiceInvocationFailureException in caso di eccezioni nelle invocazioni dei servizi
	 */
	protected void caricaDatiUlteriori(PrimaNota primaNota) throws WebServiceInvocationFailureException {
		// To be implemented
	}
	/**
	 * Caricamento del dettaglio della prima nota da servizio.
	 * 
	 * @return la prima nota del servizio
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private PrimaNota caricaPrimaNotaDaServizio() throws WebServiceInvocationFailureException {
		final String methodName = "caricaPrimaNotaDaServizio";
		
		RicercaDettaglioPrimaNota request = model.creaRequestRicercaDettaglioPrimaNota();
		RicercaDettaglioPrimaNotaResponse response = primaNotaService.ricercaDettaglioPrimaNota(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(RicercaDettaglioPrimaNota.class, response);
			log.info(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		if(response.getPrimaNota() == null) {
			String errorMsg = "Nessuna prima nota corrispondente all'uid " + model.getPrimaNota().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Prima nota integrata", model.getPrimaNota().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		if(response.getPrimaNota().getListaMovimentiEP() == null || response.getPrimaNota().getListaMovimentiEP().isEmpty()) {
			String errorMsg = "Nessun movimento EP afferente ala prima nota corrispondente all'uid " + model.getPrimaNota().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Movimento EP prima nota integrata", model.getPrimaNota().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
	
		//CR -3647
		return response.getPrimaNota();
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				isFaseBilancioNonCompatibile(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}

	protected boolean isFaseBilancioNonCompatibile(FaseBilancio faseBilancio) {
		return FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio) ||
		FaseBilancio.CHIUSO.equals(faseBilancio);
	}
	
	
	/**
	 * Atterraggio sulla pagina.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterPage() {
		return SUCCESS;
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
		}else{
			addErrori(resRC);
		}
	}
	
	/**
	 * Ottiene i dati finanziari
	 * @return una stringa corrispondente ai risultati dell'invocazione
	 */
	public String ottieniDatiFinanziari() {
		final String methodName = "ottieniDatiFinanziari";
		//String tipoMovimento = PrimaNotaMoviventoHelper.ottieniTipoMovimentoByTipocollegamento(model.getTipoCollegamento());
		String tipoMovimento = MAPPING_DATI_FINANZIARI.get(model.getTipoCollegamento());
		model.setTipoMovimento(tipoMovimento);
		
		OttieniEntitaCollegatePrimaNota req = model.creaRequestOttieniEntitaCollegatePrimaNota();
		OttieniEntitaCollegatePrimaNotaResponse res = primaNotaService.ottieniEntitaCollegatePrimaNota(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(OttieniEntitaCollegatePrimaNota.class, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Ricerca effettuata con successo. Trovati " + res.getTotaleElementi() + " elementi");
		impostaDatiFinanziariFromRequestEResponse(req, res);	
		
		return SUCCESS;
	}
	
	/**
	 *  A partire dalla request ({@link OttieniEntitaCollegatePrimaNota}) e dalla response ({@link OttieniEntitaCollegatePrimaNotaResponse}), imposta nel model e in sessione i dati necessari alla consultazione ed, eventualmente, alla gestione paginata della tabella
	 *  
	 *   @params req la request da impostare in sessione per la tabella paginata
	 *   @params req la response da cui ottenere gli elementi da consultare
	 * */
	private void impostaDatiFinanziariFromRequestEResponse(OttieniEntitaCollegatePrimaNota req,	OttieniEntitaCollegatePrimaNotaResponse res) {
		List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> wrappers = ElementoMovimentoConsultazionePrimaNotaIntegrataFactory.getInstances(res.getEntitaCollegate());
		popolaDatiaccessorii(wrappers);
		model.setListaDatiFinanziari(wrappers);
		
		impostaDatiPerPaginazioneDatiFinanziari(req,res);
	}
	
	/**
	 * Impostazione dei dati per la paginazione dei dati finanziari
	 * @param req la request
	 * @param res la response
	 */
	protected abstract void impostaDatiPerPaginazioneDatiFinanziari(OttieniEntitaCollegatePrimaNota req, OttieniEntitaCollegatePrimaNotaResponse res);
	

	/**
	 *  Popola il model  con eventuali dati accessori che devono essere estrapolati dall'elemento
	 *  @param wrappers la lista di ElementoMovimentoConsultazionePrimaNotaIntegrata da cui estrapolare i dati accessori
	 * */
	private void popolaDatiaccessorii(List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> wrappers) {
		for(ElementoMovimentoConsultazionePrimaNotaIntegrata<?> el : wrappers){
			if(el.getDatiAccessorii() != null){
				model.setDatiAccessoriiMovimentoFinanziario(el.getDatiAccessorii());
				break;
			}
		}
		
	}

	/**
	 * Validazione per il metodo {@link #ottieniDatiFinanziari()}
	 */
	public void validateOttieniDatiFinanziari() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "Prima nota");
		checkNotNull(model.getTipoCollegamento(), "Tipo collegamento");
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiorna()}
	 */
	public void prepareAggiorna() {
		model.setProseguiImportiNonCongruenti(false);
	}
	
	/**
	 * Validazione per il metodo {@link #aggiorna()}.
	 */
	public void validateAggiorna() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNota(), "Prima Nota Integrata ", true);
		//checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione ");
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione ");

		checkScrittureCorrette();
	}
	
	/**
	 * Completamento salva
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String aggiorna() {
		final String methodName = "aggiorna";
		
		// XXX: workaround per problema di non-invocazione del validate causa struts-*.xml
		try {
			validateAggiorna();
		} catch(ParamValidationException pve) {
			// Silently ignore
		}
		if(hasErrori()) {
			return INPUT;
		}
		if(hasMessaggi()) {
			return ASK;
		}
		
		// Inserimento della causale
		AggiornaPrimaNota request = model.creaRequestAggiornaPrimaNotaIntegrata();
		AggiornaPrimaNotaResponse response = primaNotaService.aggiornaPrimaNotaGEN(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'aggiornamento della Prima Nota integrata");
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "aggiornata correttamente Prima Nota integrata con uid " + response.getPrimaNota().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNota(response.getPrimaNota());
		//impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		model.setShowPulsanteAggiornamento(false);
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
}
