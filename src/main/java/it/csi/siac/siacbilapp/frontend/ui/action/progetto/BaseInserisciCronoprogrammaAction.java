/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.InserisciCronoprogrammaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe base di Action per l'inserimento del Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/05/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciCronoprogrammaAction<M extends InserisciCronoprogrammaModel> extends GenericCronoprogrammaAction<M> {
	
	/** Per la serializzazinoe */
	private static final long serialVersionUID = -7036021832630170573L;
	
	/** Il nome del model per l'inserimento con progetto */
	public static final String MODEL_NAME_INSERIMENTO_CON_PROGETTO = "InserisciProgettoModel";
	/** Il nome del model per l'inserimento senza progetto */
	public static final String MODEL_NAME_INSERIMENTO_SENZA_PROGETTO = "InserisciCronoprogrammaModel";

	@Override
	public void prepare() throws Exception {
		// Pulisco tutto
		cleanErrori();
		cleanMessaggi();
		cleanInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
		//SIAC-6255
		caricaListaTipiAtto();
		caricaListaTipoProgetto();
	}
	
	private void caricaListaTipoProgetto() {
		model.setListaTipiProgetto(Arrays.asList(TipoProgetto.values()));
	}

	/**
	 * Inserisce il Cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciCDU() {
		final String methodName = "inserisciCDU";
		
		try {
			// Non vi sono cronoprogrammi già associati
			inserisceCronoprogramma();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}
	
	/**
	 * Inserimento del cronoprogramma.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento del servizio
	 */
	protected void inserisceCronoprogramma() throws WebServiceInvocationFailureException {
		final String methodName = "inserisceCronoprogramma";
		log.debug(methodName, "Inserimento del cronoprogramma");
		
		InserisceCronoprogramma request = model.creaRequestInserisceCronoprogramma(); 
		logServiceRequest(request);
		InserisceCronoprogrammaResponse response = progettoService.inserisceCronoprogramma(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		log.debug(methodName, "Inserito cronoprogramma con uid " + response.getCronoprogramma().getUid());
	}
	
	/**
	 * Validazione per il metodo {@link BaseInserisciCronoprogrammaAction#inserisciCDU()}.
	 */
	public void validateInserisciCDU() {
		validaAnagrafica();
		try {
			validaDettagli();
		} catch(ParamValidationException e) {
			// Verificatasi un'eccezione. Non importa: ignoro e proseguo
		}
		setMessaggiInSessionePerActionSuccessiva();
		logActionErrorsAndMessages();
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		final String codiceAzione = AzioniConsentite.CRONOPROGRAMMA_INSERISCI.getNomeAzione();
		List<AzioneConsentita> list = sessionHandler.getAzioniConsentite();
		for(AzioneConsentita azioneConsentita : list) {
			if(azioneConsentita.getAzione().getNome().equalsIgnoreCase(codiceAzione)) {
				// Sono a posto
				return;
			}
		}
		// Non mi è consentito giungere all'azione
		Errore errore = ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("inserimento cronoprogramma");
		throw new GenericFrontEndMessagesException(errore.getTesto(), GenericFrontEndMessagesException.Level.ERROR);
	}
		
	/**
	 * Effettua una ricerca dei Cronoprogrammi collegati al Progetto e carica il risultato.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void effettuaCaricamentoCronoprogrammiCollegati() throws WebServiceInvocationFailureException {
		final String methodName = "effettuaCaricamentoCronoprogrammiCollegati";
		log.info(methodName, "Caricamento cronoprogrammi collegati");

		// Ricerca dei cronoprogrammi afferenti il progetto
		RicercaDeiCronoprogrammiCollegatiAlProgetto request = model.creaRequestRicercaDeiCronoprogrammiCollegatiAlProgetto();
		logServiceRequest(request);
		RicercaDeiCronoprogrammiCollegatiAlProgettoResponse response = progettoService.ricercaDeiCronoprogrammiCollegatiAlProgetto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}

		// popolazione vecchia = senza applicare il filtro sull'anno del bilancio in corso
		List<Cronoprogramma> listaCronoprogrammaFiltrata = filtraListaCronoprogrammaPerAnnoDiBilancio(response.getCronoprogrami());
		// popolazione nuova
		model.popolaCronoprogrammiCollegatiFiltrataPerAnnoBilancio(listaCronoprogrammaFiltrata);
	}
	
	
	
	/* ***** Metodi AJAX ***** */
	
	@Override
	public String cancellaDettaglioCronoprogrammaEntrata() {
		Integer indice = model.getIndiceDettaglioNellaLista();
		if(indice != null && indice.intValue() > -1) {
			model.getListaDettaglioEntrataCronoprogramma().remove(indice.intValue());
		}
		return SUCCESS;
	}
	
	@Override
	public String cancellaDettaglioCronoprogrammaUscita() {
		Integer indice = model.getIndiceDettaglioNellaLista();
		if(indice != null && indice.intValue() > -1) {
			model.getListaDettaglioUscitaCronoprogramma().remove(indice.intValue());
		}
		return SUCCESS;
	}
	
	/**
	 * Copia i dettagli da un cronoprogramma gi&agrave; presente.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String copiaDaCronoprogramma() {
		final String methodName = "copiaDaCronoprogramma";
		log.info(methodName, "popolo dettagli cronoprogramma copiandoli da altro cronoprogramma ");

		RicercaDettaglioCronoprogramma request = model.creaRequestRicercaDettaglioCronoprogramma();
		RicercaDettaglioCronoprogrammaResponse response = progettoService.ricercaDettaglioCronoprogramma(request);
		
		if(checkErroriResponse(response, methodName)) {
			return SUCCESS;
		}
		
		model.popolaDatiDaCopiareDaCronoprogramma(response.getCronoprogramma());
		
		model.setUidCronoprogrammaDaCopiare(null);
		return SUCCESS;
	}
	
	/**
	 * Pulizia del model.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String clean() {
		model.setCronoprogramma(null);
		model.getListaDettaglioEntrataCronoprogramma().clear();
		model.getListaDettaglioUscitaCronoprogramma().clear();
		
		return SUCCESS;
	}
	
	/**
	 * Risultato concernente i dettagli del cronopogramma
	 * @author Alessandro Marchino
	 *
	 */
	public static class CronoprogrammaDaCopiareJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 2169836079429564569L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, listaDettaglioEntrataCronoprogramma.*, listaDettaglioUscitaCronoprogramma.*, cronoprogramma.*";

		/** Empty default constructor */
		public CronoprogrammaDaCopiareJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
	/**
	 * Validate carica cronoprogrammi da copiare.
	 */
	//SIAC-6255
	public void validateCaricaCronoprogrammiDaCopiare() {
		checkNotNull(model.getTipoProgettoRicerca(), "tipo progetto");
		checkNotNullNorEmpty(model.getProgetto().getCodice(), "codice progetto");
	}
	
	/**
	 * Carica cronoprogrammi da copiare.
	 *
	 * @return the string
	 */
	public String caricaCronoprogrammiDaCopiare() {
		//NDB: ottimizzare il caricamento, servono solo i dati: codice, descrizione, usatoPerFPV, uid
		RicercaDeiCronoprogrammiCollegatiAlProgetto req = model.creaRequestRicercaDeiCronoprogrammiCollegatiAlProgetto();
		RicercaDeiCronoprogrammiCollegatiAlProgettoResponse response = progettoService.ricercaDeiCronoprogrammiCollegatiAlProgetto(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		// popolazione nuova
		model.setListaCronoprogrammiDaCopiare(response.getCronoprogrami());
		return SUCCESS;
	}
	
}
