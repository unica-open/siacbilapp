/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException.Level;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.AggiornaVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step1.Scelta;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.VariazioneDiBilancio;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe astratta per la gestione dell'aggiornamento della variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 09/01/2014
 *
 */
public abstract class AggiornaVariazioneBaseAction extends VariazioneBaseAction<AggiornaVariazioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4310173694921291365L;
	
	@Override
	public void prepare() throws Exception {
		cleanErrori();
		cleanMessaggi();
		cleanInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	public String execute() throws Exception {
		
		if((this instanceof AggiornaVariazioneImportiAction && !Scelta.IMPORTI.isConsentita(sessionHandler.getAzioniConsentite())) || 
				(this instanceof AggiornaVariazioneCodificheAction && !Scelta.CODIFICHE.isConsentita(sessionHandler.getAzioniConsentite()))){
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'operatore non e' abilitato a questa operazione.").getTesto(), Level.ERROR);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Ingresso nella pagina.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String enterPage();
	
	/**
	 * Metodo per il salvataggio dell'aggiornamento della variazione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 * @throws ApplicationException nel caso di errore nel salvataggio
	 */
	public abstract String salva() throws ApplicationException;
	
	/**
	 * Metodo per l'annullamento della variazione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 * @throws ApplicationException nel caso di errore nell'annullamento
	 */
	public abstract String annulla() throws ApplicationException;
	
	/**
	 * Metodo per la conclusione dell'aggiornamento della variazione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 * @throws ApplicationException nel caso di errore nella conclusione
	 */
	public abstract String concludi() throws ApplicationException;
	
	//SIAC-7530
	protected void controllaStatoOperativoVariazione(VariazioneDiBilancio variazione) {
		String methodName = "controllaStatoOperativoVariazione";
		List<String> listaStatiOperativiAmmessi = new ArrayList<String>();
		listaStatiOperativiAmmessi.add(StatoOperativoVariazioneBilancio.BOZZA.getDescrizione());
		listaStatiOperativiAmmessi.add(StatoOperativoVariazioneBilancio.GIUNTA.getDescrizione());
		listaStatiOperativiAmmessi.add(StatoOperativoVariazioneBilancio.CONSIGLIO.getDescrizione());
		listaStatiOperativiAmmessi.add(StatoOperativoVariazioneBilancio.PRE_BOZZA.getDescrizione());
		
		//SIAC-7584
		if(variazione == null) {
			log.debug(methodName, "Entita' non trovata: [null]");
			throw new GenericFrontEndMessagesException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Variazione").getTesto(), Level.ERROR);
		}
		
		if(!listaStatiOperativiAmmessi.contains(variazione.getStatoOperativoVariazioneDiBilancio().getDescrizione())) {
			log.debug(methodName, "operazione non ammessa : l'operazione e' valida solo per le variazioni in stato di: BOZZA, GIUNTA e CONSIGLIO.");
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("operazione valida solo per le variazioni in stato di: BOZZA, GIUNTA e CONSIGLIO.").getTesto(), Level.ERROR);					
		}
		
	}
	
	/**
	 * Metodo per il caricamento della lista dei tipi di atto.
	 */
	protected void caricaListaTipiAtto() {
		final String methodName = "caricaListaTipiAtto";
		
		// Caricamento della combo del tipo di provvedimento
		log.debug(methodName, "Caricamento combo tipo provvedimento");
		// Chiamo il servizio
		TipiProvvedimento request = model.creaRequestTipiProvvedimento();
		TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
		// Imposto la lista nel model
		List<TipoAtto> listaTipoAtto = response.getElencoTipi();
		model.setListaTipoAtto(listaTipoAtto);
		log.debug(methodName, "Caricata combo tipo atto");
	}
	
	/**
	 * Metodo per il caricamento dei dati relativi al bilancio.
	 */
	protected void caricaBilancio() {
		final String methodName = "caricaBilancio";
		
		// Ricerca di dettaglio
		log.debug(methodName, "Ricerca del dettaglio del Bilancio");
		// Chiamo il servizio
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);
		// Imposto il bilancio nel model
		Bilancio bilancio = response.getBilancio();
		model.setBilancio(bilancio);
		log.debug(methodName, "Caricato il dettaglio del bilancio");
	}
	
	@Override
	public void validate() {
		checkNotNull(StringUtils.trimToNull(model.getDescrizione()), "descrizione");
	}
	
	/**
	 * Controlla l'abilitazione per le operazioni dell'utente.
	 */
	protected void controllaAbilitazioneOperazioni() {
		FaseBilancio faseBilancio = model.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		ApplicazioneVariazione applicazioneVariazione = model.getApplicazione();
		StatoOperativoVariazioneBilancio statoDellaVariazione = model.getStatoOperativoVariazioneDiBilancio();
		if(!coerente(faseBilancio, applicazioneVariazione)) {
			model.setSalvaAbilitato(Boolean.FALSE);
			model.setAnnullaAbilitato(Boolean.FALSE);
			model.setCampiNonModificabili(Boolean.TRUE);
		}
		
		if(StatoOperativoVariazioneBilancio.BOZZA.equals(statoDellaVariazione)) {
			// Se la variazione è in bozza, allora è possibile annullarla
			model.setAnnullaAbilitato(Boolean.TRUE);
		} else if(statoDellaVariazione == StatoOperativoVariazioneBilancio.DEFINITIVA) {
			// Se la variazione è definitiva, allora non è modificabile
			model.setSalvaAbilitato(Boolean.FALSE);
			model.setAnnullaAbilitato(Boolean.FALSE);
		}
	}
	
	/**
	 * Controlla se la fase di bilancio sia coerente con l'applicazione della variazione.
	 * 
	 * @param faseBilancio           la fase del bilancio
	 * @param applicazioneVariazione l'applicazione della variazione
	 * 
	 * @return <code>true</code> se le fasi sono coerenti; <code>false</code> in caso contrario
	 */
	private boolean coerente(FaseBilancio faseBilancio, ApplicazioneVariazione applicazioneVariazione) {
		boolean result = false;
		
		if(ApplicazioneVariazione.PREVISIONE.equals(applicazioneVariazione)) {
			// Previsione si può fare solo quando il bilancio è in PREVISIONE o in ESERCIZIO_PROVVISORIO
			result = FaseBilancio.PREVISIONE.equals(faseBilancio) ||
					FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio);
		} else if (ApplicazioneVariazione.GESTIONE.equals(applicazioneVariazione)) {
			// Gestione si può fare solo quando il bilancio è in ESERCIZIO_PROVVISORIO, GESTIONE o ASSESTAMENTO
			// SIAC-4637: anche in PREDISPOSIZIONE_CONSUNTIVO
			result = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
					FaseBilancio.GESTIONE.equals(faseBilancio) ||
					FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
					FaseBilancio.ASSESTAMENTO.equals(faseBilancio);
		} else {
			// Assestamento si può fare solo quando il bilancio è in ASSESTAMENTO
			result = FaseBilancio.ASSESTAMENTO.equals(faseBilancio);
		}
		
		return result;
	}
	
	/**
	 * Estrae l'id attivit&agrave; e lo imposta nel model nel caso in cui esso non sia <code>null</code>.
	 * 
	 * @param idAttivita l'id da impostare
	 */
	protected void extractIdAttivitaIfNotNull(String idAttivita) {
		if(idAttivita != null) {
			model.setIdAttivita(idAttivita);
		}
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// SIAC-4637
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
//				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}

	/**
	 * Imposta l'id nell'azione di report variazioni
	 */
	protected void setIdAzioneReportVariazioni() {
		Azione azione = findAzione(String.format("OP-REP-ReportVariazioniBilancio-%s", sessionHandler.getAnnoEsercizio()));
		if (azione != null) {
			model.setIdAzioneReportVariazioni(azione.getUid());
		}
	}
	
	
}
