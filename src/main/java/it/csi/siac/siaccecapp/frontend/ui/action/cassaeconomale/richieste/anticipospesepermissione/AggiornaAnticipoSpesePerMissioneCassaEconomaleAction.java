/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException.Level;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.AggiornaAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'aggiornamento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 04/02/2015
 * @version 1.0.1 - 31/03/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO)
public class AggiornaAnticipoSpesePerMissioneCassaEconomaleAction extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction<AggiornaAnticipoSpesePerMissioneCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2373400174962039272L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo semplicemente il super. Aggiungo il breadcrumb
		return super.execute();
	}

	@Override
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException{
		RicercaDettaglioRichiestaEconomale request = model.creaRequestRicercaDettaglioRichiestaEconomale(model.getRichiestaEconomale());
		logServiceRequest(request);
		RicercaDettaglioRichiestaEconomaleResponse response = richiestaEconomaleService.ricercaDettaglioRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			throwExceptionFromErrori(response.getErrori());
		}
		
		RichiestaEconomale richiestaEconomale = response.getRichiestaEconomale();
		if(!isValidTipoRichiestaEconomale(richiestaEconomale)) {
			Errore errore = ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la richiesta economale selezionata non e' della tipologia corretta.");
			addErrore(errore);
			throw new GenericFrontEndMessagesException(errore.getTesto(), Level.ERROR);
		}
		
		Valuta valutaEuro = ComparatorUtils.findByCodice(model.getListaValuta(), BilConstants.VALUTA_CODICE_EURO.getConstant());
		if(valutaEuro != null) {
			model.setUidValutaEuro(valutaEuro.getUid());
		}
		
		impostaDatiAggiornamento(richiestaEconomale);
		impostaMatricolaAggiornamento(richiestaEconomale);
		
		//conservo una copia del movimento gestione iniziale della richiesta per ripristinare i dati all'annulla
		impostaDatiClonePerAnnulla(richiestaEconomale);
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulisco i campi. Basta pulirne uno
		model.setRichiestaEconomale(null);
		model.getMezziDiTrasportoSelezionati().clear();
	}
	
	/**
	 * Preparazione sul metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.getRichiestaEconomale().setMovimento(null);
	}
	
	/**
	 * Completa il secondo step dell'inserimento della richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		if(model.isGestioneHR()) {
			//azzero il soggetto per poter salare correttamente mi serviva solo per impostare i primi campi
			model.getRichiestaEconomale().setSoggetto(null);
		}
		AggiornaRichiestaEconomale request = model.creaRequestAggiornaRichiestaEconomale();
		logServiceRequest(request);
		AggiornaRichiestaEconomaleResponse response = richiestaEconomaleService.aggiornaRichiestaEconomaleAnticipoSpesePerMissione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Aggiornata richiesta economale con uid " + response.getRichiestaEconomale().getUid());
		
		// Imposto i dati nel model
		model.setRichiestaEconomale(response.getRichiestaEconomale());
		
		caricaClassificatoriDaLista();
		
		// TODO: Caricamento dei dati dell'ultimo rendiconto stampato in definitivo
		
		impostaMessaggioSuccessoPerStep3();
		cleanImpegnoFromSession();

		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		if(model.isRichiestaEconomalePrenotata()) {
			checkNotNull(model.getRichiestaEconomale(), "Movimento", true);
			checkNotNull(model.getRichiestaEconomale().getMovimento(), "Movimento", true);
			Movimento m = model.getRichiestaEconomale().getMovimento();
			
			checkNotNull(m.getDataMovimento(), "Data operazione");
			checkNotNullNorInvalidUid(m.getModalitaPagamentoCassa(), "Modalita' di pagamento");
			checkNotNullNorInvalidUid(m.getModalitaPagamentoDipendente(), "Modalita' di pagamento del dipendente");
			checkDettaglioPagamento(m);
		} else {
			// Non sono prenotato. Quindi, avevo il movimento gia' prima. Lo ripristino
			RichiestaEconomale richiestaEconomaleCopia = model.getRichiestaEconomaleCopia();
			model.getRichiestaEconomale().setMovimento(richiestaEconomaleCopia.getMovimento());
		}
		
		checkMovimentoGestione(model.getMovimentoGestione(), model.getSubMovimentoGestione());
	}
	
	/**
	 * Annulla i dati dello step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1(){
		model.setClassificatoreGenerico1(model.getClassificatoreGenerico1Copia());
		model.setClassificatoreGenerico2(model.getClassificatoreGenerico2Copia());
		model.setClassificatoreGenerico3(model.getClassificatoreGenerico3Copia());
		return SUCCESS;
	}
	
	/**
	 * Annulla i dati dello step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2(){
		String methodName = "annullaStep2";
		log.debug(methodName, "mov: " + model.getMovimentoGestione().getNumero() + " copia: "+model.getMovimentoGestioneCopia().getNumero());
		model.setMovimentoGestione(model.getMovimentoGestioneCopia());
		model.setSubMovimentoGestione(model.getSubMovimentoGestioneCopia());
		return SUCCESS;
	}
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_AGGIORNA, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_ABILITA};
	}
}
