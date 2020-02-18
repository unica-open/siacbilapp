/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.InserisciRendicontoAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO)
public class InserisciRendicontoAnticipoSpeseCassaEconomaleAction extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleAction<InserisciRendicontoAnticipoSpeseCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3600678433908075016L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo semplicemente il super. Aggiungo il breadcrumb
		return super.execute();
	}
	
	@Override
	protected String getTestoCheckCassa() {
		return "la gestione del rendiconto";
	}
	
	@Override
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException {
		// Inizializzo il rendiconto
		checkCassa("la gestione del rendiconto");
		model.setRendicontoRichiesta(new RendicontoRichiesta());
		// Caricamento del dettaglio della richiesta
		ricercaDettaglioRichiestaEconomale();
		
		// Richiamo il metodo della classe padre
		super.impostazioneValoriDefaultStep1();
	}
	
	/**
	 * Ricerca il dettaglio della richiesta economale.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void ricercaDettaglioRichiestaEconomale() throws WebServiceInvocationFailureException {
		RicercaDettaglioRichiestaEconomale request = model.creaRequestRicercaDettaglioRichiestaEconomale(model.getRichiestaEconomale());
		logServiceRequest(request);
		RicercaDettaglioRichiestaEconomaleResponse response = richiestaEconomaleService.ricercaDettaglioRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		RichiestaEconomale richiestaEconomale = response.getRichiestaEconomale();
		
		model.getRendicontoRichiesta().setRichiestaEconomale(richiestaEconomale);
		model.setRichiestaEconomaleCopia(richiestaEconomale);
	}
	
	/**
	 * Completa il secondo step dell'inserimento della richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		
		// Invocazione del servizio
		InserisceRendicontoRichiesta request = model.creaRequestInserisceRendicontoRichiesta();
		logServiceRequest(request);
		InserisceRendicontoRichiestaResponse response = richiestaEconomaleService.inserisceRendicontoRichiesta(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Rendiconto con uid " + response.getRendicontoRichiesta().getUid() + " inserito per richiesta economale " + model.getRendicontoRichiesta().getRichiestaEconomale().getUid());
		model.setRendicontoRichiesta(response.getRendicontoRichiesta());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		cleanImpegnoFromSession();

		return SUCCESS;
	}
	
	/**
	 * Pulisce il form dello step 1 riportandolo allo stato iniziale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1(){
		model.setRendicontoRichiesta(null);
		model.setUidRichiesta(model.getRichiestaEconomale().getUid());
		return SUCCESS;
	}
	
	/**
	 * Pulisce il form dello step 2 riportandolo allo stato iniziale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2(){
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.getRendicontoRichiesta().getMovimento().setModalitaPagamentoCassa(null);
		model.getRendicontoRichiesta().getMovimento().setModalitaPagamentoDipendente(null);
		model.getRendicontoRichiesta().getMovimento().setDettaglioPagamento(null);
		model.getRendicontoRichiesta().getMovimento().setBic(null);
		model.getRendicontoRichiesta().getMovimento().setContoCorrente(null);
		
		return SUCCESS;
	}
	
	// Lotto M
	@Override
	protected void impostazioneValoriDefaultStep2() {
		super.impostazioneValoriDefaultStep2();
		
		// Copio l'impegno
		Impegno impegno = model.getRichiestaEconomaleCopia().getImpegno();
		SubImpegno subImpegno = model.getRichiestaEconomaleCopia().getSubImpegno();
		
		// SIAC-5192
		String dettaglioPagamento = calcolaDettaglioPagamentoRendiconto(model.getImportoDaIntegrare());
		model.getRendicontoRichiesta().getMovimento().setDettaglioPagamento(dettaglioPagamento);
		
		model.setMovimentoGestione(impegno);
		model.setSubMovimentoGestione(subImpegno);
	}
	
	@Override
	public String selezionaIban() {
		return SUCCESS;
	}
	
	@Override
	public String caricaDettaglioPagamento() {
		return SUCCESS;
	}
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_INSERISCI_RENDICONTO, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA};
	}

}
