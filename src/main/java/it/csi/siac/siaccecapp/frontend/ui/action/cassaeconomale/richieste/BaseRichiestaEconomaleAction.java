/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe base di action base per la richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/02/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public class BaseRichiestaEconomaleAction<M extends BaseRichiestaEconomaleModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6077802101582435658L;

	/** Serviz&icirc; della richiesta economale */
	@Autowired protected transient RichiestaEconomaleService richiestaEconomaleService;
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	/** Serviz&icirc; della stampa cassa economale */
	@Autowired protected transient StampaCassaEconomaleService stampaCassaEconomaleService;
	
	/**
	 * Ottiene la cassa economale e la imposta nel model.
	 */
	protected void impostaCassaEconomale() {
		final String methodName = "impostaCassaEconomale";
		CassaEconomale cassaEconomale = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE);
		log.debug(methodName, "CassaEconomale impostata nel model. Null? " + (cassaEconomale == null ? "true" : ("false: uid " + cassaEconomale.getUid())));
		model.setCassaEconomale(cassaEconomale);
	}
	
	/**
	 * Caricamento della lista dei classificatori generici.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListeClassificatoriGenerici() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListeClassificatoriGenerici";
		List<ClassificatoreGenerico> listaClassificatoreGenerico1 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_51);
		List<ClassificatoreGenerico> listaClassificatoreGenerico2 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_52);
		List<ClassificatoreGenerico> listaClassificatoreGenerico3 = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_53);
		
		boolean isListaPresente = listaClassificatoreGenerico1 != null && listaClassificatoreGenerico2 != null && listaClassificatoreGenerico3 != null;
		if(!isListaPresente) {
			log.debug(methodName, "Almeno una lista di classificatori generici per la cassa economale non presente in sessione. Caricamento da servizio");
			// Devo caricare le liste
			RicercaClassificatoriGenericiCassaEconomale request = model.creaRequestRicercaClassificatoriGenericiCassaEconomale();
			logServiceRequest(request);
			RicercaClassificatoriGenericiCassaEconomaleResponse response = richiestaEconomaleService.ricercaClassificatoriGenericiCassaEconomale(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaClassificatoriGenericiCassaEconomale.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			listaClassificatoreGenerico1 = response.getListaClassificatori(TipologiaClassificatore.CLASSIFICATORE_51);
			listaClassificatoreGenerico2 = response.getListaClassificatori(TipologiaClassificatore.CLASSIFICATORE_52);
			listaClassificatoreGenerico3 = response.getListaClassificatori(TipologiaClassificatore.CLASSIFICATORE_53);
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_51, listaClassificatoreGenerico1);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_52, listaClassificatoreGenerico2);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_53, listaClassificatoreGenerico3);
		}
		
		log.debug(methodName, "Estrazione dei label dei classificatori");
		String labelClassificatoreGenerico1 = estraiLabel(listaClassificatoreGenerico1);
		String labelClassificatoreGenerico2 = estraiLabel(listaClassificatoreGenerico2);
		String labelClassificatoreGenerico3 = estraiLabel(listaClassificatoreGenerico3);
		
		model.setLabelClassificatoreGenerico1(labelClassificatoreGenerico1);
		model.setLabelClassificatoreGenerico2(labelClassificatoreGenerico2);
		model.setLabelClassificatoreGenerico3(labelClassificatoreGenerico3);
		
		model.setListaClassificatoreGenerico1(listaClassificatoreGenerico1);
		model.setListaClassificatoreGenerico2(listaClassificatoreGenerico2);
		model.setListaClassificatoreGenerico3(listaClassificatoreGenerico3);
	}

	/**
	 * Estrae il label per la lista di codifiche fornita.
	 * 
	 * @param list la lista di codifiche da cui estrarre il label
	 * 
	 * @return il label corrispondente alla lista
	 */
	private <C extends ClassificatoreGenerico> String estraiLabel(List<C> list) {
		if(list != null && !list.isEmpty()) {
			return list.get(0).getTipoClassificatore().getDescrizione();
		}
		return null;
	}

	/**
	 * Controlla la validita e obbligatorieta della data fornita.
	 * 
	 * @param date      la data da controllare
	 * @param nomeCampo il nome del campo
	 */
	protected void checkValidDate(Date date, String nomeCampo) {
		final String methodName = "checkValidDate";
		try {
			checkNotNull(date, nomeCampo, true);
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Il campo " + nomeCampo + " e' null. E' inutile proseguire con i controlli");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		
		cal.setTime(date);
		int chosenYear = cal.get(Calendar.YEAR);
		
		checkCondition(chosenYear <= nowYear, ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, ": non deve essere successivo all'anno corrente"));
	}
	
	
	/**
	 * Controlla la validita della data fornita, ma non l'obbligatoriet&agrave;.
	 * <br>
	 * Controllare che sia una spesa dell'anno in corso o dell'anno precedente (per gestire a gennaio l'inserimento di rimborsi con giustificativi di dicembre).
	 * 
	 * @param date      la data da controllare
	 * @param nomeCampo il nome del campo
	 */
	protected void checkValidNoMandatoryDate(Date date, String nomeCampo) {

		if (date !=null) {
			Calendar cal = Calendar.getInstance();
			int nowYear = cal.get(Calendar.YEAR);
			
			cal.setTime(date);
			int chosenYear = cal.get(Calendar.YEAR);
			
			checkCondition(chosenYear <= nowYear && chosenYear >= nowYear - 1,
					ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, ": non deve essere successivo all'anno corrente ne' antecedente l'anno precedente"));
		}
	}
	
	/**
	 * Controllo per la matricola.
	 * 
	 * @param matricola il soggetto da controllare
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void checkMatricola(Soggetto matricola) throws WebServiceInvocationFailureException {
		checkCondition(matricola != null && StringUtils.isNotBlank(matricola.getMatricola()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Matricola"), true);
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		if(soggetto == null || listaModalitaPagamentoSoggetto == null || !matricola.getMatricola().equals(soggetto.getMatricola())) {
			RicercaSoggettoPerChiaveResponse response = caricaSoggetto(matricola);
			
			soggetto = response.getSoggetto();
			listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			checkCondition(soggetto != null, ErroreCore.NESSUN_DATO_REPERITO.getErrore(), true);
			
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		checkSoggettoValidoSeAnagraficaSoggetti(soggetto);
		
		impostazioneDatiSoggetto(model.getRichiestaEconomale(), soggetto);
		model.getRichiestaEconomale().setSoggetto(soggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Controllo per il soggettoFattura.
	 * 
	 * @param soggettoFattura il soggetto da controllare
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void checkSoggettoFattura(Soggetto soggettoFattura) throws WebServiceInvocationFailureException {
		//checkCondition(soggettoFattura != null ), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"), true);
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		if(soggetto == null || listaModalitaPagamentoSoggetto == null || !soggettoFattura.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())) {
			RicercaSoggettoPerChiaveResponse response = caricaSoggettoFattura(soggettoFattura);
			
			soggetto = response.getSoggetto();
			listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			checkCondition(soggetto != null, ErroreCore.NESSUN_DATO_REPERITO.getErrore(), true);
			
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		checkSoggettoValidoSeAnagraficaSoggetti(soggetto);
		
		impostazioneDatiSoggetto(model.getRichiestaEconomale(), soggetto);
		model.getRichiestaEconomale().setSoggetto(soggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Controlla se il soggetto sia valido nel caso in cui provenga dall'anagrafica soggetti.
	 * 
	 * @param soggetto il soggetto da controllare
	 */
	protected void checkSoggettoValidoSeAnagraficaSoggetti(Soggetto soggetto) {
		// TODO: condizione per anagrafica soggetti
		checkCondition(StatoOperativoAnagrafica.VALIDO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
	}

	/**
	 * Carica il soggetto.
	 * 
	 * @param soggetto il soggetto da cercare
	 * 
	 * @return la response della ricerca del soggetto
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private RicercaSoggettoPerChiaveResponse caricaSoggetto(Soggetto soggetto) throws WebServiceInvocationFailureException {
		final String methodName = "caricaSoggetto";
		// Ho la richiesta quindi ho la matricola. Se il soggetto non ha la matricola gli forzo quella della richiesta
		log.debug(methodName, "Matricola Soggetto " + soggetto.getMatricola());
		if (soggetto.getMatricola()== null && (model.getRichiestaEconomale()!=null && model.getRichiestaEconomale().getMatricola()!=null)) {
			soggetto.setMatricola(model.getRichiestaEconomale().getMatricola());
			log.debug(methodName, "Matricola Soggetto da richiesta" + soggetto.getMatricola());
		}
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response);
			log.debug(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(response.getSoggetto() == null) {
			String errorMsg = "Nessun soggetto corrispondente alla matricola " + soggetto.getMatricola() + " trovato";
			log.debug(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", soggetto.getMatricola()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Ho il dettaglio: lo restituisco
		return response;
	}
	
	/**
	 * Carica il soggetto della fattura(potrebbe non avere matricola)
	 * 
	 * @param soggetto il soggetto da cercare
	 * 
	 * @return la response della ricerca del soggetto
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private RicercaSoggettoPerChiaveResponse caricaSoggettoFattura(Soggetto soggetto) throws WebServiceInvocationFailureException {
		final String methodName = "caricaSoggettoFattura";
		
		log.debug(methodName, "Codice Soggetto " + soggetto.getCodiceSoggetto());

		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoFatturaPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response);
			log.debug(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(response.getSoggetto() == null) {
			String errorMsg = "Nessun soggetto corrispondente al codiceSoggetto " + soggetto.getCodiceSoggetto() + " trovato";
			log.debug(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", soggetto.getCodiceSoggetto()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Ho il dettaglio: lo restituisco
		return response;
	}

	/**
	 * Impostazione dei dati del soggetto nella richiesta economale.
	 * 
	 * @param richiestaEconomale la richiesta da popolare
	 * @param soggetto           il soggetto tramite cui popolare la richiesta
	 */
	protected void impostazioneDatiSoggetto(RichiestaEconomale richiestaEconomale, Soggetto soggetto) {
		richiestaEconomale.setSoggetto(soggetto);
		
		// Spiattello i dati sulla richiesta economale
		richiestaEconomale.setMatricola(soggetto.getMatricola());
		richiestaEconomale.setNome(soggetto.getNome());
		// SIAC-4792: se il soggetto non ha cognome (persona giuridica) prendo la denominazione
		richiestaEconomale.setCognome(StringUtils.isNotBlank(soggetto.getCognome()) ? soggetto.getCognome() : soggetto.getDenominazione());
		richiestaEconomale.setCodiceFiscale(soggetto.getCodiceFiscale());
	}
	
	/**
	 * SIAC-4584
	 * <br/>
	 * Check della cassa: nel caso in cui vi siano delle incongruenze uscire subito
	 * @param msgSuffix il suffisso del messaggio
	 * @throws WebServiceInvocationFailureException in caso di incongruenze nella cassa
	 */
	protected void checkCassa(String msgSuffix) throws WebServiceInvocationFailureException {
		CassaEconomale cassaEconomale = model.getCassaEconomale();
		if(!TipoDiCassa.CONTANTI.equals(cassaEconomale.getTipoDiCassa()) && StringUtils.isBlank(cassaEconomale.getNumeroContoCorrente())) {
			Errore errore = ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La cassa economale non ha un numero di conto corrente associato, impossibile effettuare " + msgSuffix);
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
	}
}
