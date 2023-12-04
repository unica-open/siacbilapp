/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.RisultatiRicercaPreDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Action per i risultati di ricerca del preDocumento di spesa
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 24/04/2014
 * @version 1.0.1 - 04/12/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPreDocumentoSpesaAction extends RisultatiRicercaPreDocumentoGenericAction<RisultatiRicercaPreDocumentoSpesaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2271333474213144286L;
	
	@Autowired private transient PreDocumentoSpesaService preDocumentoSpesaService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
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
		gestisciRientro();
		
		// Imposto il totale
		BigDecimal importoTotale = sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA_PREDOCUMENTO);
		model.setImportoTotale(importoTotale);
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		// Carico i dati della fase del bilancio per l'abilitazione delle operazioni
		caricaFaseBilancio();
		
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		controlloInserisciAbilitato(azioniConsentite);
		controlloAssociaAbilitato(azioniConsentite);
		controlloDefinisciAbilitato();
		
		// SIAC-4280
		controlloModificaAssociazioneAbilitato(BilSessionParameter.ABILITATA_MODIFICA_ASSOCIAZIONE_IMPUTAZIONI_CONTABILI_PREDOCUMENTO_SPESA);
		model.setCausaleSpesa(sessionHandler.getParametro(BilSessionParameter.CAUSALE_SELEZIONATA_PREDOCUMENTO_SPESA, CausaleSpesa.class));
		
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
		RicercaSinteticaPreDocumentoSpesa req = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_SPESA, RicercaSinteticaPreDocumentoSpesa.class);
		req.setSoloImporto(Boolean.TRUE);
		RicercaSinteticaPreDocumentoSpesaResponse res = preDocumentoSpesaService.ricercaSinteticaPreDocumentoSpesa(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSinteticaPreDocumentoSpesa.class, res));
		}
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA_PREDOCUMENTO, res.getImportoTotale());
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
		boolean inserisciAbilitato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_SPESA_INSERISCI, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_SPESA_INSERISCI_DECENTRATO, listaAzioniConsentite);
		inserisciAbilitato = inserisciAbilitato && Boolean.TRUE.equals(model.getFaseBilancioAbilitata());
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
		boolean associaAbilitato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_SPESA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_SPESA_AGGIORNA_DECENTRATO, listaAzioniConsentite);
		// Controllo sui criteri di ricerca
		associaAbilitato = associaAbilitato && controllaCriteriRicerca(true, StatoOperativoPreDocumento.INCOMPLETO) && Boolean.TRUE.equals(model.getFaseBilancioAbilitata());
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
		// Controllo sui criteri di ricerca
		boolean definisceAbilitato = controllaCriteriRicerca(true, StatoOperativoPreDocumento.COMPLETO)
				&& Boolean.TRUE.equals(model.getFaseBilancioAbilitata());
		log.debug(methodName, "Definisce abilitato? " + definisceAbilitato);
		model.setDefinisciAbilitato(Boolean.valueOf(definisceAbilitato));
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
		AnnullaPreDocumentoSpesa req = model.creaRequestAnnullaPreDocumentoSpesa();
		logServiceRequest(req);
		AnnullaPreDocumentoSpesaResponse res = preDocumentoSpesaService.annullaPreDocumentoSpesa(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AnnullaPreDocumentoSpesa.class, res));
			addErrori(res);
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
		
		AssociaImputazioniContabiliPreDocumentoSpesa req = model.creaRequestAssociaImputazioniContabiliPreDocumentoSpesa(idOperazioneAsincrona, 
				sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_SPESA, RicercaSinteticaPreDocumentoSpesa.class));
		logServiceRequest(req);
		
		preDocumentoSpesaService.associaImputazioniContabiliPreDocumentoSpesa(req);
		
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
		
		RicercaSinteticaPreDocumentoSpesa requestSintetica = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_SPESA, RicercaSinteticaPreDocumentoSpesa.class);
		DefiniscePreDocumentoSpesa req = model.creaRequestDefiniscePreDocumentoSpesa(requestSintetica);
		
		AzioneRichiesta azioneRichiesta = AzioneConsentitaEnum.PREDOCUMENTO_SPESA_RICERCA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = preDocumentoSpesaService.definiscePreDocumentoSpesaAsync(wrapRequestToAsync(req, azioneRichiesta));
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		log.info(methodName, "Invocato servizio asincrono di definizione predisposizione di pagamento");
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
		model.setCausaleSpesa(sessionHandler.getParametro(BilSessionParameter.CAUSALE_SELEZIONATA_PREDOCUMENTO_SPESA, CausaleSpesa.class));
		impostaImputazioniContabili();
		return SUCCESS;
	}
	
	/**
	 * Impostazione delle imputazioni contabili per la modifica delle stesse
	 */
	private void impostaImputazioniContabili() {
		if(model.getCausaleSpesa() == null) {
			return;
		}
		CausaleSpesa cs = model.getCausaleSpesa();
		
		CapitoloUscitaGestione capitolo = cs.getCapitoloUscitaGestione();
		Impegno movimentoGestione = cs.getImpegno();
		SubImpegno subMovimentoGestione = cs.getSubImpegno();
		Soggetto soggetto = cs.getSoggetto();
		AttoAmministrativo attoAmministrativo = cs.getAttoAmministrativo();
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
	 * Associa le imputazioni contabili ai preDocumenti selezionati, o a tutti i preDocumenti relativi all'invocazione con modifiche.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaConModifiche() {
		final String methodName = "associaConModifiche";
		
		AssociaImputazioniContabiliVariatePreDocumentoSpesa req = model.creaRequestAssociaImputazioniContabiliVariatePreDocumentoSpesa(
				sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_SPESA, RicercaSinteticaPreDocumentoSpesa.class));
		
		AzioneRichiesta azioneRichiesta = AzioneConsentitaEnum.PREDOCUMENTO_SPESA_RICERCA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response =
				preDocumentoSpesaService.associaImputazioniContabiliVariatePreDocumentoSpesaAsync(wrapRequestToAsync(req, azioneRichiesta));
		
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
			validazioneImpegnoSubImpegno();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore in validazione dei dati: " + wsife.getMessage());
		}
		
	}
	
	/**
	 * Effettua una validazione dell'impegno e del subimpegno forniti in input.
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void validazioneImpegnoSubImpegno()  throws WebServiceInvocationFailureException{
		Impegno impegno = model.getMovimentoGestione();
		SubImpegno subImpegno = model.getSubMovimentoGestione();
		
		if(impegno == null || (impegno.getAnnoMovimento() == 0 || impegno.getNumeroBigDecimal() == null)) {
			return;
		}
		
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException("Errori nell'invocazione della ricercaImpegnoPerChiaveOttimizzato");
		}
		if(res.isFallimento() || res.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal()));
			throw new WebServiceInvocationFailureException("Errori nell'invocazione della ricercaImpegnoPerChiaveOttimizzato: entita' non trovata");
		}
		
		impegno = res.getImpegno();
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumeroBigDecimal() != null) {
			BigDecimal numero = subImpegno.getNumeroBigDecimal();
			// Controlli di validita' sull'impegno
			subImpegno = findSubMovimentoByNumero(res.getImpegno().getElencoSubImpegni(), subImpegno);
			if(subImpegno == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal() + "-" + numero));
				return;
			}
			model.setSubMovimentoGestione(subImpegno);
			// Controllo stato
			checkCondition("D".equals(subImpegno.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			
		} else {
			// Controlli di validità sul subimpegno
			checkCondition("D".equals(impegno.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
		}
		
		// Controllo anno
		checkCondition(impegno.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("subaccertamento", "l'anno deve essere non superiore all'anno di esercizio"));
	}

	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void validazioneCapitolo() throws WebServiceInvocationFailureException {
		CapitoloUscitaGestione capitolo = model.getCapitolo();
		// Se non ho il capitolo, sono a posto
		if(isCapitoloNonValorizzato(capitolo)) {
			return;
		}
		
		RicercaSinteticaCapitoloUscitaGestione req = model.creaRequestRicercaSinteticaCapitoloUscitaGestione();
		RicercaSinteticaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException("Fallimento nell'invocazione del servizio RicercaSinteticaCapitoloUscitaGestione");
		}
		
		StringBuilder classificazioneCapitolo =  new StringBuilder()
				.append(capitolo.getAnnoCapitolo())
				.append("/")
				.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			classificazioneCapitolo.append("/").append(capitolo.getNumeroUEB());
		}
		
		int totaleElementi = res.getTotaleElementi();
		checkCondition(totaleElementi > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", classificazioneCapitolo.toString()));
		checkCondition(totaleElementi < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Capitolo"));
		
		if(totaleElementi == 1) {
			// Imposto i dati del capitolo
			model.setCapitolo(res.getCapitoli().get(0));
		}
	}

}
