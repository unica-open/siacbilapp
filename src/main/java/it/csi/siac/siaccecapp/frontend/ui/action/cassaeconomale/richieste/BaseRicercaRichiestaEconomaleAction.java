/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe base di action per la ricerca dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseRicercaRichiestaEconomaleAction<M extends BaseRicercaRichiestaEconomaleModel> extends BaseRichiestaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4808407253774585779L;
	
	@Autowired private transient CodificheService codificheService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		caricaListe();
		// Carica tipo di richiesta economale
		caricaTipoRichiestaEconomale();
		// Carica cassa economale
		impostaCassaEconomale();
	}
	
	/**
	 * Caricamento delle liste per la ricerca.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void caricaListe() throws WebServiceInvocationFailureException {
		caricaListeClassificatoriGenerici();
		caricaListaStatoOperativoRichiestaEconomale();
	}
	
	/**
	 * Caricamento della lista degli stati operativi della richiesta economale.
	 */
	private void caricaListaStatoOperativoRichiestaEconomale() {
		model.setListaStatoOperativoRichiestaEconomale(Arrays.asList(StatoOperativoRichiestaEconomale.values()));
	}
	
	/**
	 * Caricamento del tipo di richiesta economale.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	private void caricaTipoRichiestaEconomale() throws WebServiceInvocationFailureException {
		final String methodName = "caricaTipoRichiestaEconomale";
		List<TipoRichiestaEconomale> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_RICHIESTA_ECONOMALE);
		if(lista == null) {
			log.debug(methodName, "Lista tipo richiesta economale in sessione non presente. Caricamento da servizio");
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoRichiestaEconomale.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(request, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			lista = response.getCodifiche(TipoRichiestaEconomale.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_RICHIESTA_ECONOMALE, lista);
		}
		TipoRichiestaEconomale tipoRichiestaEconomale = findTipoRichiestaEconomale(lista);
		log.debug(methodName, "Tipo richiesta economale trovata? " + (tipoRichiestaEconomale == null ? "false" : "true, con uid " + tipoRichiestaEconomale.getUid()));
		model.setTipoRichiestaEconomale(tipoRichiestaEconomale);
	}
	
	/**
	 * Ricerca il tipo corretto di richiesta economale a partire dalla lista dei tipi ottenuti da servizio.
	 * 
	 * @param lista la lista dei tipi di richiesta
	 * 
	 * @return il tipo di richiesta
	 */
	protected abstract TipoRichiestaEconomale findTipoRichiestaEconomale(List<TipoRichiestaEconomale> lista);
	
	@Override
	public String execute() throws Exception {
		checkCasoDUsoApplicabile();
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		
		AzioniConsentite[] azioniRichieste = retrieveAzioniConsentite();
		boolean consentito = AzioniConsentiteFactory.isConsentitoAll(azioniConsentite, azioniRichieste);
		if(!consentito) {
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non si dispone dei permessi necessari per l'esecuzione").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	/**
	 * Ottiene le azioni consentite richieste per l'attivazione della funzionalit&agrave;
	 * @return le azioni richieste
	 */
	protected abstract AzioniConsentite[] retrieveAzioniConsentite();

	/**
	 * Effettua la ricerca della richiesta economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		RicercaSinteticaModulareRichiestaEconomale request = model.creaRequestRicercaSinteticaModulareRichiestaEconomale();
		logServiceRequest(request);
		RicercaSinteticaModulareRichiestaEconomaleResponse response = richiestaEconomaleService.ricercaSinteticaModulareRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		
		// Impostazione dati in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MODULARE_RICHIESTA_ECONOMALE, response.getRichiesteEconomali());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MODULARE_RICHIESTA_ECONOMALE, request);
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getTotaleImporti());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		
		RichiestaEconomale richiestaEconomale = model.getRichiestaEconomale();
		Date dataCreazioneDa = model.getDataCreazioneDa();
		Date dataCreazioneA = model.getDataCreazioneA();
		
		Date dataMovimentoDa = model.getDataMovimentoDa();
		Date dataMovimentoA = model.getDataMovimentoA();
		
		boolean ricercaValida = checkCampoValorizzato(richiestaEconomale.getNumeroRichiesta(), "Numero richiesta")
				// SIAC-4497
				|| checkCampoValorizzato(dataCreazioneDa, "Data richiesta da")
				|| checkCampoValorizzato(dataCreazioneA, "Data richiesta a")
				// SIAC-4552
				|| checkCampoValorizzato(dataMovimentoDa, "Data movimento da")
				|| checkCampoValorizzato(dataMovimentoA, "Data movimento a")
				|| checkCondizioneValida(richiestaEconomale.getSospeso() != null && richiestaEconomale.getSospeso().getNumeroSospeso() != null, "Numero sospeso")
				|| checkCondizioneValida(richiestaEconomale.getMovimento() != null && richiestaEconomale.getMovimento().getNumeroMovimento() != null, "Numero movimento")
				|| checkExistanceMatricola(richiestaEconomale.getSoggetto())
				|| checkStringaValorizzata(richiestaEconomale.getDescrizioneDellaRichiesta(), "Descrizione della spesa")
				|| checkCampoValorizzato(richiestaEconomale.getStatoOperativoRichiestaEconomale(), "Stato")
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico1())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico2())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico3());
		
		// Se ho entrambe le date, la data da non deve essere successiva la data a
		checkCondition(dataCreazioneDa == null || dataCreazioneA == null || !dataCreazioneDa.after(dataCreazioneA), ErroreCore.FORMATO_NON_VALIDO.getErrore("Data operazione", "la data di creazione da non puo' essere successiva alla data da creazione a"));
		checkCondition(dataMovimentoDa == null || dataMovimentoA == null || !dataMovimentoDa.after(dataMovimentoA), ErroreCore.FORMATO_NON_VALIDO.getErrore("Data operazione", "la data del movimento da non puo' essere successiva alla data del movimento a"));
		
		if(!ricercaValida) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	

	/**
	 * Controllo l'esistenza (ma non l'obbligatoriet&agrave;) della matricola.
	 * 
	 * @param soggetto il soggetto da controllare
	 * 
	 * @return <code>true</code> se la matricola &eacute; stata impostata; <code>false</code> in caso contrario
	 */
	private boolean checkExistanceMatricola(Soggetto soggetto) {
		if(soggetto == null || StringUtils.isBlank(soggetto.getMatricola())) {
			// Non ho il soggetto
			return false;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Il dato c'e', e' solo errato.
			addErrori(response);
		} else if(response.getSoggetto() == null) {
			// Non ho il soggetto
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "matricola " + soggetto.getMatricola()));
		} else {
			model.getRichiestaEconomale().setSoggetto(response.getSoggetto());
		}
		return true;
	}

}
