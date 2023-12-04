/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import java.math.BigDecimal;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException.Level;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.AggiornaRendicontoAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per l'aggiornamento del rendiconto dell'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO_RENDICONTO)
public class AggiornaRendicontoAnticipoSpeseCassaEconomaleAction extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleAction<AggiornaRendicontoAnticipoSpeseCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2373400174962039272L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo semplicemente il super. Aggiungo il breadcrumb
		return super.execute();
	}
	
	@Override
	protected String getTestoCheckCassa() {
		// Il testo deve specificare che sono in rendiconto
		return "la gestione del rendiconto";
	}
	
	@Override
	protected void caricaListe() throws WebServiceInvocationFailureException {
		// Carico meno dati
		// Caricamento tipo giustificativo
		caricaListaTipoGiustificativo();
		// Caricamento valuta
		caricaListaValuta();
	}

	@Override
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException {
		// Controlla che la cassa permetta l'operazione
		checkCassa("la gestione del rendiconto");
		// Caricamento del dettaglio del rendiconto
		ricercaDettaglioRendicontoRichiesta();
		
		// Impostazione dei dati di default
		super.impostazioneValoriDefaultStep1();
		// SIAC-5192
		controllaOriginariamenteRestituzione();
	}
	
	/**
	 * Controlla se originariamente il rendiconto fosse una restituzione
	 */
	private void controllaOriginariamenteRestituzione() {
		BigDecimal importoDaRestituire = model.getImportoDaRestituire();
		// Originariamente era una restituzione se l'importo da restituire era > 0
		model.setOriginariamenteRestituzione(importoDaRestituire != null && importoDaRestituire.signum() > 0);
	}
	
	/**
	 * Ricerca di dettaglio del rendiconto della richiesta.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void ricercaDettaglioRendicontoRichiesta() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioRendicontoRichiesta";
		// Ricerco il dettaglio del rendiconto
		RicercaDettaglioRendicontoRichiesta req = model.creRequestRicercaDettaglioRendicontoRichiesta();
		logServiceRequest(req);
		// Invocazione del servizio
		RicercaDettaglioRendicontoRichiestaResponse res = richiestaEconomaleService.ricercaDettaglioRendicontoRichiesta(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		checkServiceResponseException(methodName, RicercaDettaglioRendicontoRichiesta.class, res);
		// Response senza errori
		log.debug(methodName, "Rendiconto con uid " + model.getRendicontoRichiesta().getUid() + " trovato");
		
		// Controllo del tipo di richiesta
		RendicontoRichiesta rendicontoRichiesta = res.getRendicontoRichiesta();
		if(!isValidTipoRichiestaEconomale(rendicontoRichiesta.getRichiestaEconomale())) {
			// Richiesta economale non valida
			Errore errore = ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la richiesta economale selezionata non e' della tipologia corretta.");
			addErrore(errore);
			throw new GenericFrontEndMessagesException(errore.getTesto(), Level.ERROR);
		}
		
		// Impostazione dei dati
		model.setRendicontoRichiesta(rendicontoRichiesta);
		// Clone del rendiconto
		RendicontoRichiesta rendicontoRichiestaClone = ReflectionUtil.deepClone(rendicontoRichiesta);
		model.setRendicontoRichiestaCopia(rendicontoRichiestaClone);
		
		model.setMovimentoGestione(rendicontoRichiesta.getImpegno());
		model.setSubMovimentoGestione(rendicontoRichiesta.getSubImpegno());
		
		// Copio i dati della richiesta
		model.setRichiestaEconomaleCopia(rendicontoRichiestaClone.getRichiestaEconomale());
		
		model.setListaGiustificativo(rendicontoRichiesta.getGiustificativi());
		
		// Impostazione dati per l'annullamento
		impostaDatiClonePerAnnullaRendiconto(rendicontoRichiesta);
		
	}
	
	@Override
	protected void impostazioneValoriDefaultStep2() {
		final String methodName = "impostazioneValoriDefaultStep2";
		if((model.getIsAggiornamento() || model.isCopied()) && model.getRendicontoRichiestaCopia().getMovimento() != null && model.getRendicontoRichiestaCopia().getMovimento().getUid() != 0) {
			// Se sono in aggiornamento, oppure ho copiato i dati, non faccio nulla
			log.debug(methodName, "Reimposto il movimento");
			// Imposto solo il movimento
			Movimento movimentoClone = ReflectionUtil.deepClone(model.getRendicontoRichiestaCopia().getMovimento());
			model.getRendicontoRichiesta().setMovimento(movimentoClone);
			
			// SIAC-5192
			modificaDettaglioPagamentoSeCambioTipoRendiconto();
			
			return;
		}
		inizializzaDatiMovimento();
	}
	
	/**
	 * Se sono passato da restituzione a integrazione o viceversa devo ricaricare i dati del dettaglio di pagamento
	 */
	private void modificaDettaglioPagamentoSeCambioTipoRendiconto() {
		final String methodName = "modificaDettaglioPagamentoSeCambioTipoRendiconto";
		BigDecimal importoDaRestituire = model.getImportoDaRestituire();
		// Controllo se adesso sono una restituzione o un'integrazione
		boolean isRestituzione = importoDaRestituire != null && importoDaRestituire.signum() > 0;
		// Se il tipo (integrazione/restituzione) non e' cambiato, esco
		if(isRestituzione == model.isOriginariamenteRestituzione()) {
			log.debug(methodName, "Il tipo del rendiconto non e' modificato [" + (isRestituzione ? "restituzione" : "integrazione") + "]: mantengo i dati originali");
			return;
		}
		// Reimposto il dato: in questa maniera sono certo di non perdermi dati nel caso torni indietro
		model.setOriginariamenteRestituzione(isRestituzione);
		// Il tipo e' cambiato: devo ricalcolare il dettaglio pagamento
		String dettaglioPagamento = calcolaDettaglioPagamentoRendiconto(model.getImportoDaIntegrare());
		model.getRendicontoRichiesta().getMovimento().setDettaglioPagamento(dettaglioPagamento);
	}

	/**
	 * Completa il secondo step dell'inserimento della richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		// Aggiorno il rendiconto
		AggiornaRendicontoRichiesta req = model.creaRequestAggiornaRendicontoRichiesta();
		logServiceRequest(req);
		// Invoco il servizio
		AggiornaRendicontoRichiestaResponse res = richiestaEconomaleService.aggiornaRendicontoRichiesta(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaRendicontoRichiesta.class, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Aggiornato rendiconto richiesta con uid " + res.getRendicontoRichiesta().getUid());
		
		// Imposto i dati nel model
		model.setRendicontoRichiesta(res.getRendicontoRichiesta());
		
		// Caricamento dei classificatori
		caricaClassificatoriDaLista();
		
		// Impostazione dell'informazione di successo
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		// Pulizia della sessione: tolgo l'impegno si' da forzarne il ricaricamento
		cleanImpegnoFromSession();

		return SUCCESS;
	}
	
	/**
	 * Annulla i dati dello step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1(){
		// Reimposto l'uid del rendiconto
		model.setUidRendiconto(model.getRendicontoRichiesta().getUid());
		return SUCCESS;
	}
	
	/**
	 * Annulla i dati dello step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2(){
		// Ripristino il movimento e il submovimento di gestione
		String methodName = "annullaStep2";
		log.debug(methodName, "mov: " + model.getMovimentoGestione().getNumeroBigDecimal() + " copia: " + model.getMovimentoGestioneCopia().getNumeroBigDecimal());
		model.setMovimentoGestione(model.getMovimentoGestioneCopia());
		model.setSubMovimentoGestione(model.getSubMovimentoGestioneCopia());
		return SUCCESS;
	}
	
	/**
	 * Completamento del terzo step dell'inserimento con eventuale stampa.
	 * 
	 * @data_modifica 31/08/2015 
	 * Lotto M stampa ricevuta del rendiconto
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String completeStep3() {
		// Completamento dello step3 per il rendiconto
		return innerCompleteStep3Rendiconto(model.getRendicontoRichiesta() != null ? model.getRendicontoRichiesta() : model.getRendicontoRichiestaCopia());
	}
	
	@Override
	public String selezionaIban() {
		// Placeholder
		return SUCCESS;
	}
	
	@Override
	public String caricaDettaglioPagamento() {
		// Placeholder
		return SUCCESS;
	}
	
	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA_RENDICONTO, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA};
	}

}
