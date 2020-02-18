/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamento;

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
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento.AggiornaPagamentoCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'aggiornamento del pagamento.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 01/02/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPagamentoCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO)
public class AggiornaPagamentoCassaEconomaleAction extends BaseInserisciAggiornaPagamentoCassaEconomaleAction<AggiornaPagamentoCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3682262904511519127L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo semplicemente il super. Aggiungo il breadcrumb
		return super.execute();
	}

	@Override
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException {
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
		//impostaMatricolaAggiornamento(richiestaEconomale);
		//conservo una copia del movimento gestione iniziale della richiesta per ripristinare i dati all'annulla
		impostaDatiClonePerAnnulla(richiestaEconomale);
		
	}
	
	@Override
	protected void impostaDatiAggiornamento(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiAggiornamento(richiestaEconomale);
		
		
		model.setAutomatica(richiestaEconomale.getSubdocumenti()!= null && !richiestaEconomale.getSubdocumenti().isEmpty());
	}

	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulisco i campi. Basta pulirne uno
		model.setRichiestaEconomale(null);
	}
	
	@Override
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		
		// Ho la richiesta
		RichiestaEconomale re = model.getRichiestaEconomale();
		
		try {
			checkSoggettoFattura(re.getSoggetto());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		}
		checkCondition(!model.getListaGiustificativo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Giustificativi"));
		checkNotNullNorEmpty(re.getDescrizioneDellaRichiesta(), "Descrizione della spesa");
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

		AggiornaRichiestaEconomale request = model.creaRequestAggiornaRichiestaEconomale();
		logServiceRequest(request);
		AggiornaRichiestaEconomaleResponse response = richiestaEconomaleService.aggiornaRichiestaEconomalePagamento(request);
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
		
		impostaMessaggioSuccessoPerStep3();
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
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_AGGIORNA, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_ABILITA};
	}
	
}
