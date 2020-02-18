/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.RisultatiRicercaPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Action per i risultati di ricerca del preDocumento di entrata
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/04/2014
 * @version 1.0.1 - 04/12/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPreDocumentoEntrataAction extends RisultatiRicercaPreDocumentoGenericAction<RisultatiRicercaPreDocumentoEntrataModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7555382740787163444L;
	
	@Autowired private transient PreDocumentoEntrataService preDocumentoEntrataService;
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		checkDecentrato();
		// SIAC-5041
		checkModificaAccertamentoDisponibile();
	}
	
	/**
	 * Controlla se l'utente sia decentrato
	 */
	private void checkDecentrato() {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isDecentrato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite);
		model.setUtenteDecentrato(Boolean.TRUE.equals(isDecentrato));
	}
	
	/**
	 * Controlla se la modifica della disponibilit&agrave; dell'accertamento sia possibile
	 */
	private void checkModificaAccertamentoDisponibile() {
		// La modifica e' consentita se non ho l'azione
		boolean modificaConsentita = !AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_MODIFICA_ACC_NON_AMMESSA, sessionHandler.getAzioniConsentite());
		model.setModificaAccertamentoDisponibile(modificaConsentita);
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() {
		final String methodName = "execute";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		
		log.debug(methodName, "StartPosition = " + startPosition);
		
		// SIAC-4384
		try {
			gestisciRientro();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			return INPUT;
		}
		
		// Imposto il totale
		BigDecimal importoTotale = sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA_PREDOCUMENTO);
		model.setImportoTotale(importoTotale);
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		// Carico i dati della fase del bilancio per l'abilitazione delle operazioni
		caricaFaseBilancio();
		// Controllo l'abilitazione delle funzioni
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		controlloInserisciAbilitato(azioniConsentite);
		controlloAssociaAbilitato(azioniConsentite);
		controlloDefinisciAbilitato();
		controlloDataTrasmissioneAbilitato(azioniConsentite);
		
		// SIAC-4280
		controlloModificaAssociazioneAbilitato(BilSessionParameter.ABILITATA_MODIFICA_ASSOCIAZIONE_IMPUTAZIONI_CONTABILI_PREDOCUMENTO_ENTRATA);
		model.setCausaleEntrata(sessionHandler.getParametro(BilSessionParameter.CAUSALE_SELEZIONATA_PREDOCUMENTO_ENTRATA, CausaleEntrata.class));
		
		return SUCCESS;
	}
	
	/**
	 * Gestione del rientro: ricalcolo l'importo totale
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void gestisciRientro() throws WebServiceInvocationFailureException {
		final String methodName = "gestisciRientro";
		if(!Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO))) {
			return;
		}
		log.info(methodName, "Rientro nella funzionalita'. Necessario ricalcolare il totale");
		RicercaSinteticaPreDocumentoEntrata req = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, RicercaSinteticaPreDocumentoEntrata.class);
		req.setSoloImporto(Boolean.TRUE);
		RicercaSinteticaPreDocumentoEntrataResponse res = preDocumentoEntrataService.ricercaSinteticaPreDocumentoEntrata(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA_PREDOCUMENTO, res.getImportoTotale());
	}

	/**
	 * Impostazione delle imputazioni contabili per la modifica delle stesse
	 */
	private void impostaImputazioniContabili() {
		if(model.getCausaleEntrata() == null) {
			return;
		}
		CausaleEntrata ce = model.getCausaleEntrata();
		
		CapitoloEntrataGestione capitolo = ce.getCapitoloEntrataGestione();
		Accertamento movimentoGestione = ce.getAccertamento();
		SubAccertamento subMovimentoGestione = ce.getSubAccertamento();
		Soggetto soggetto = ce.getSoggetto();
		AttoAmministrativo attoAmministrativo = ce.getAttoAmministrativo();
		TipoAtto tipoAtto = null;
		StrutturaAmministrativoContabile strutturaAmministrativoContabileAttoAmministrativo = null;
		if(attoAmministrativo != null) {
			tipoAtto = attoAmministrativo.getTipoAtto();
			strutturaAmministrativoContabileAttoAmministrativo = attoAmministrativo.getStrutturaAmmContabile();
		}
		
		model.setCapitolo(capitolo);
		model.setMovimentoGestione(movimentoGestione);
		model.setSubMovimentoGestione(subMovimentoGestione);
		model.setSoggetto(soggetto);
		model.setAttoAmministrativo(attoAmministrativo);
		model.setTipoAtto(tipoAtto);
		model.setStrutturaAmministrativoContabileAttoAmministrativo(strutturaAmministrativoContabileAttoAmministrativo);
	}

	/**
	 * Abilitato se azione consentita in
	 * <ul>
	 *     <li>OP-ENT-insPreDoc</li>
	 *     <li>OP-ENT-insPreDocDec</li>
	 * </ul>
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 */
	private void controlloInserisciAbilitato(List<AzioneConsentita> listaAzioniConsentite) {
		final String methodName = "controlloInserisciAbilitato";
		boolean inserisciAbilitato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_INSERISCI, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_INSERISCI_DECENTRATO, listaAzioniConsentite);
		// SIAC-4382
		inserisciAbilitato = inserisciAbilitato && Boolean.TRUE.equals(model.getFaseBilancioAbilitata()) && !model.isUtenteDecentrato();
		log.debug(methodName, "Inserisci abilitato? " + inserisciAbilitato);
		model.setInserisciAbilitato(Boolean.valueOf(inserisciAbilitato));
	}

	/**
	 * Abilitato se azione consentita in
	 * <ul>
	 *     <li>OP-ENT-aggPreDoc</li>
	 *     <li>OP-ENT-aggPreDocDec</li>
	 * </ul>
	 * Inoltre, devono esserci ulteriori controlli sui parametri di ricerca
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @see #controllaCriteriRicerca(boolean)
	 */
	private void controlloAssociaAbilitato(List<AzioneConsentita> listaAzioniConsentite) {
		final String methodName = "controlloAssociaAbilitato";
		// Controllo sulle azioni
		boolean associaAbilitato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite);
		// Controllo sui criteri di ricerca
		associaAbilitato = associaAbilitato && controllaCriteriRicerca(false, StatoOperativoPreDocumento.INCOMPLETO) && Boolean.TRUE.equals(model.getFaseBilancioAbilitata());
		log.debug(methodName, "Associa abilitato? " + associaAbilitato);
		model.setAssociaAbilitato(Boolean.valueOf(associaAbilitato));
	}
	
	/**
	 * Abilitato se azione consentita in
	 * <ul>
	 *     <li>OP-ENT-defPreDoc</li>
	 *     <li>OP-ENT-defPreDocDec</li>
	 * </ul>
	 * Inoltre, devono esserci ulteriori controlli sui parametri di ricerca
	 * 
	 * @see #controllaCriteriRicerca(boolean)
	 */
	private void controlloDefinisciAbilitato() {
		final String methodName = "controlloDefinisciAbilitato";
		// Controllo sulle azioni
		boolean definisceAbilitato = true;
		// Controllo sui criteri di ricerca
		definisceAbilitato = definisceAbilitato && controllaCriteriRicerca(false, StatoOperativoPreDocumento.COMPLETO) && Boolean.TRUE.equals(model.getFaseBilancioAbilitata());
		log.debug(methodName, "Definisce abilitato? " + definisceAbilitato);
		model.setDefinisciAbilitato(Boolean.valueOf(definisceAbilitato));
	}
	
	/**
	 * Abilitato se azione consentita in
	 * <ul>
	 *     <li>OP-ENT-aggPreDoc</li>
	 *     <li><strike>OP-ENT-aggPreDocDec</strike></li>
	 * </ul>
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 */
	private void controlloDataTrasmissioneAbilitato(List<AzioneConsentita> listaAzioniConsentite) {
		final String methodName = "controlloDataTrasmissioneAbilitato";
		// Controllo sulle azioni
		boolean dataTrasmissioneAbilitato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA, listaAzioniConsentite)
				&& !AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite);
		// Controllo sui criteri di ricerca
		log.debug(methodName, "Data di trasmissione abilitata? " + dataTrasmissioneAbilitato);
		model.setDataTrasmissioneAbilitato(Boolean.valueOf(dataTrasmissioneAbilitato));
	}
	
	/**
	 * Annullamento del documento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Annullamento del predocumento con uid: " + model.getUidDaAnnullare());
		
		log.debug(methodName, "Creazione della request ed invocazione del servizio");
		AnnullaPreDocumentoEntrata req = model.creaRequestAnnullaPreDocumentoEntrata();
		logServiceRequest(req);
		AnnullaPreDocumentoEntrataResponse response = preDocumentoEntrataService.annullaPreDocumentoEntrata(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "PreDocumento annullato: imposto il valore TRUE al parametro RIENTRO e torno alla pagina");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
	// Metodi asincroni
	
	/**
	 * Associa le imputazioni contabili ai preDocumenti selezionati, o a tutti i preDocumenti relativi all'invocazione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associa() {
		final String methodName = "associa";
		
		// Inizio con il richiedere il token
		Integer idOperazioneAsincrona;
		try {
			idOperazioneAsincrona = inserimentoOperazioneAsincrona();
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del servizio di richiesta token", e);
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(e.getCause().getMessage()));
			return SUCCESS;
		}
		
		AssociaImputazioniContabiliPreDocumentoEntrata req = model.creaRequestAssociaImputazioniContabiliPreDocumentoEntrata(idOperazioneAsincrona, 
				sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, RicercaSinteticaPreDocumentoEntrata.class));
		logServiceRequest(req);
		
		preDocumentoEntrataService.associaImputazioniContabiliPreDocumentoEntrata(req);
		
		log.info(methodName, "Invocato servizio asincrono di associazione imputazioni contabili");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Definisce le imputazioni contabili ai preDocumenti selezionati, o a tutti i preDocumenti relativi all'invocazione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String definisci() {
		// SIAC-5001
		final String methodName = "definisci";
		
		RicercaSinteticaPreDocumentoEntrata requestSintetica = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, RicercaSinteticaPreDocumentoEntrata.class);
		DefiniscePreDocumentoEntrata req = model.creaRequestDefiniscePreDocumentoEntrata(requestSintetica);
		
		AzioneRichiesta azioneRichiesta = AzioniConsentite.PREDOCUMENTO_ENTRATA_RICERCA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = preDocumentoEntrataService.definiscePreDocumentoEntrataAsync(wrapRequestToAsync(req, azioneRichiesta));
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		log.info(methodName, "Invocato servizio asincrono di definizione predisposizione di incasso");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		model.setIdAzioneAsync(response.getIdOperazioneAsincrona());
		return SUCCESS;
	}
	
	// SIAC-4280
	
	/**
	 * Apertura dell'associazione con modifiche
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriAssociaConModifiche() {
		model.setCausaleEntrata(sessionHandler.getParametro(BilSessionParameter.CAUSALE_SELEZIONATA_PREDOCUMENTO_ENTRATA, CausaleEntrata.class));
		impostaImputazioniContabili();
		return SUCCESS;
	}
	
	/**
	 * Associa le imputazioni contabili ai preDocumenti selezionati, o a tutti i preDocumenti relativi all'invocazione con modifiche.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaConModifiche() {
		final String methodName = "associaConModifiche";
		
		AssociaImputazioniContabiliVariatePreDocumentoEntrata req = model.creaRequestAssociaImputazioniContabiliVariatePreDocumentoEntrata(
				sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, RicercaSinteticaPreDocumentoEntrata.class));
		
		AzioneRichiesta azioneRichiesta = AzioniConsentite.PREDOCUMENTO_ENTRATA_RICERCA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response =
				preDocumentoEntrataService.associaImputazioniContabiliVariatePreDocumentoEntrataAsync(wrapRequestToAsync(req, azioneRichiesta));
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		log.info(methodName, "Invocato servizio asincrono di associazione imputazioni contabili");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		model.setIdAzioneAsync(response.getIdOperazioneAsincrona());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #associaConModifiche()}
	 */
	public void validateAssociaConModifiche() {
		final String methodName = "validateAssociaConModifiche";
		checkCondition(Boolean.TRUE.equals(model.getInviaTutti()) || !model.getListaUid().isEmpty(),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("necessario specificare i predocumenti da associare o se si desidera associare tutti"));
		try {
			validazioneSoggetto();
			validazioneAttoAmministrativo();
			validazioneCapitolo();
			validazioneAccertamentoSubAccertamento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore in validazione dei dati: " + wsife.getMessage());
		}
		
	}
	
	/**
	 * Effettua una validazione dell'accertamento e del subaccertamento forniti in input.
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void validazioneAccertamentoSubAccertamento()  throws WebServiceInvocationFailureException{
		Accertamento accertamento = model.getMovimentoGestione();
		SubAccertamento subAccertamento = model.getSubMovimentoGestione();
		
		if(accertamento == null || (accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null)) {
			return;
		}
		
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errori nell'invocazione della ricercaAccertamentoPerChiaveOttimizzato");
		}
		if(response.isFallimento() || response.getAccertamento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero()));
			throw new WebServiceInvocationFailureException("Errori nell'invocazione della ricercaAccertamentoPerChiaveOttimizzato: entita' non trovata");
		}
		
		accertamento = response.getAccertamento();
		model.setMovimentoGestione(accertamento);
		
		if(subAccertamento != null && subAccertamento.getNumero() != null) {
			BigDecimal numero = subAccertamento.getNumero();
			// Controlli di validita' sull'impegno
			subAccertamento = findSubMovimentoByNumero(response.getAccertamento().getSubAccertamenti(), subAccertamento);
			if(subAccertamento == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero() + "-" + numero));
				return;
			}
			model.setSubMovimentoGestione(subAccertamento);
			// Controllo stato
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subAccertamento.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			
		} else {
			// Controlli di validità sul subimpegno
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
		}
		
		// Controllo anno
		checkCondition(accertamento.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("subaccertamento", "l'anno deve essere non superiore all'anno di esercizio"));
		
	}
	

	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void validazioneCapitolo() throws WebServiceInvocationFailureException {
		CapitoloEntrataGestione capitolo = model.getCapitolo();
		// Se non ho il capitolo, sono a posto
		if(isCapitoloNonValorizzato(capitolo)) {
			return;
		}
		
		RicercaSinteticaCapitoloEntrataGestione req = model.creaRequestRicercaSinteticaCapitoloEntrataGestione();
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(req);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException("Fallimento nell'invocazione del servizio RicercaSinteticaCapitoloEntrataGestione");
		}
		
		StringBuilder classificazioneCapitolo = new StringBuilder()
				.append(capitolo.getAnnoCapitolo())
				.append("/")
				.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			classificazioneCapitolo
				.append("/")
				.append(capitolo.getNumeroUEB());
		}
		
		int totaleElementi = response.getTotaleElementi();
		checkCondition(totaleElementi > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", classificazioneCapitolo.toString()));
		checkCondition(totaleElementi < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Capitolo"));
		
		if(totaleElementi == 1) {
			// Imposto i dati del capitolo
			model.setCapitolo(response.getCapitoli().get(0));
		}
	}
	
	// SIAC-4383
	
	/**
	 * Aggiornamento della data di trasmissione.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaDataTrasmissione() {
		final String methodName = "aggiornaDataTrasmissione";

		AggiornaDataTrasmissionePreDocumentoEntrata req = model.creaRequestAggiornaDataTrasmissionePreDocumentoEntrata(
				sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, RicercaSinteticaPreDocumentoEntrata.class));
		
		AzioneRichiesta azioneRichiesta = AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = preDocumentoEntrataService.aggiornaDataTrasmissionePreDocumentoEntrataAsync(wrapRequestToAsync(req, azioneRichiesta));
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		log.info(methodName, "Invocato servizio asincrono di associazione imputazioni contabili");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		model.setIdAzioneAsync(response.getIdOperazioneAsincrona());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaDataTrasmissione()}.
	 */
	public void validateAggiornaDataTrasmissione() {
		checkCondition(Boolean.TRUE.equals(model.getInviaTutti()) || !model.getListaUid().isEmpty(),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("necessario specificare i predocumenti da associare o se si desidera associare tutti"));
		checkNotNull(model.getDataTrasmissione() != null, "data trasmissione");
	}
}
