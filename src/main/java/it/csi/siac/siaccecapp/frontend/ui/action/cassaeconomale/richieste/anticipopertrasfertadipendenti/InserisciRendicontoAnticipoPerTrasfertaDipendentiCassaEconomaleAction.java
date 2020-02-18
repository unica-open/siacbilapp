/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti.InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo per trasferta dipendenti.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO)
public class InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleAction extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleAction<InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1500678433908075016L;

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
	protected void caricaListe() throws WebServiceInvocationFailureException {
		// Carico meno dati
		caricaListaTipoGiustificativo();
		caricaListaValuta();
	}

	@Override
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException {
		final String methodName = "impostazioneValoriDefaultStep1";
		// Inizializzo il rendiconto
		model.setRendicontoRichiesta(new RendicontoRichiesta());
		// Caricamento del dettaglio della richiesta
		ricercaDettaglioRichiestaEconomale();
		
		// Carico l'uid della fattura
		List<TipoGiustificativo> listaTipoGiustificativo = model.getListaTipoGiustificativo();
		TipoGiustificativo tipoGiustificativo = ComparatorUtils.findByCodice(listaTipoGiustificativo, BilConstants.CODICE_TIPO_GIUSTIFICATIVO_FATTURA.getConstant());
		if(tipoGiustificativo != null) {
			log.debug(methodName, "Il tipo giustificativo 'FATTURA' ha uid " + tipoGiustificativo.getUid());
			model.setUidFattura(tipoGiustificativo.getUid());
		}
		
		// Carico l'uid della valuta corrispondente all'euro (serve per i giustificativi)
		List<Valuta> listaValuta = model.getListaValuta();
		Valuta valuta = ComparatorUtils.findByCodice(listaValuta, BilConstants.VALUTA_CODICE_EURO.getConstant());
		if(valuta != null) {
			log.debug(methodName, "La valuta 'EURO' ha uid " + valuta.getUid());
			model.setUidValutaEuro(valuta.getUid());
		}
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
	 * Imposta la restituzione totale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String restituzioneTotale() {
		model.setRestituzioneTotale(Boolean.TRUE);
		model.getListaGiustificativo().clear();
		return SUCCESS;
	}

	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		RendicontoRichiesta rendicontoRichiesta = new RendicontoRichiesta();
		rendicontoRichiesta.setRichiestaEconomale(model.getRichiestaEconomaleCopia());
		
		// Pulisco la descrizione
		rendicontoRichiesta.getRichiestaEconomale().setDescrizioneDellaRichiesta(null);
		
		// Pulisco i campi
		model.setRendicontoRichiesta(rendicontoRichiesta);
	}
	
	@Override
	public void validateCompleteStep1() {
		checkNotNull(model.getRendicontoRichiesta(), "Rendiconto", true);
		checkNotNull(model.getRendicontoRichiesta().getRichiestaEconomale(), "Richiesta", true);
		
		checkNotNullNorEmpty(model.getRendicontoRichiesta().getRichiestaEconomale().getDescrizioneDellaRichiesta(), "Descrizione");
		
		// O restituzione totale o inserimento giustificativi
		checkCondition(Boolean.TRUE.equals(model.getRestituzioneTotale()) || !model.getListaGiustificativo().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Necessario effettuare un'operazione: selezionare 'Restituzione totale' o inserire almeno un giustificativio"));
	}
	
	@Override
	protected Movimento ottieniMovimentoDalModel() {
		return model.getRendicontoRichiesta().getMovimento();
	}
	
	@Override
	protected void impostaMovimentoNelModel(Movimento movimento) {
		model.getRendicontoRichiesta().setMovimento(movimento);
	}
	
	/**
	 * Preparazione sul metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.getRendicontoRichiesta().setMovimento(null);
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
		// Copio l'uid
		model.setRendicontoRichiesta(response.getRendicontoRichiesta());
		cleanImpegnoFromSession();

		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkNotNull(model.getRendicontoRichiesta(), "Rendiconto", true);
		checkNotNull(model.getRendicontoRichiesta().getMovimento(), "Movimento", true);
		Movimento movimento = model.getRendicontoRichiesta().getMovimento();
		
		checkNotNull(movimento.getDataMovimento(), "Data operazione");
		checkNotNullNorInvalidUid(movimento.getModalitaPagamentoCassa(), "Modalita' di pagamento");
		
		checkMovimentoGestione(model.getMovimentoGestione(), model.getSubMovimentoGestione());
		
		// Caricamento del dato per la modalita di pagamento cassa
		caricaModalitaPagamentoCassa(movimento);
	}
	
	/**
	 * Carica i dati per la modalit&agrave; di pagamento della cassa.
	 * 
	 * @param movimento il movimento da popolare
	 */
	private void caricaModalitaPagamentoCassa(Movimento movimento) {
		List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa = model.getListaModalitaPagamentoCassa();
		ModalitaPagamentoCassa modalitaPagamentoCassa = ComparatorUtils.searchByUidEventuallyNull(listaModalitaPagamentoCassa, movimento.getModalitaPagamentoCassa());
		movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
	}

	@Override
	public String completeStep3() {
		return innerCompleteStep3Rendiconto(model.getRendicontoRichiesta() != null ? model.getRendicontoRichiesta() : model.getRendicontoRichiestaCopia());
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
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_PER_TRASFERTA_DIPENDENTI_INSERISCI_RENDICONTO, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_PER_TRASFERTA_DIPENDENTI_ABILITA};
	}
}
