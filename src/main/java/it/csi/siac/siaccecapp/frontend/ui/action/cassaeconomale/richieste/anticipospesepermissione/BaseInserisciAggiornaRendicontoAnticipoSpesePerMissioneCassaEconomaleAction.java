/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe base di action per l'inserimento e l'aggiornamento del rendiconto per l'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleAction<M extends BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel>
		extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1446193184749263830L;

	@Override
	protected void caricaListe() throws WebServiceInvocationFailureException {
		// Carico meno dati
		caricaListaTipoGiustificativo();
		caricaListaValuta();
	}

	@Override
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException {
		final String methodName = "impostazioneValoriDefaultStep1";
		// Ricerco il dettaglio del soggetto
		ricercaDettaglioSoggetto();
		
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
	protected void ricercaDettaglioRichiestaEconomale() throws WebServiceInvocationFailureException {
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
	 * Ricerca il dettaglio del soggetto della richiesta economale.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	@Override
	protected void ricercaDettaglioSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioSoggetto";
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		Soggetto soggettoNelModel = getSoggettoDaModel();
		
		if(soggettoNelModel==null || soggetto == null || listaSedeSecondariaSoggetto == null || listaModalitaPagamentoSoggetto == null ||  !soggettoNelModel.getMatricola().equals(soggetto.getMatricola())) {
			String codiceSoggetto = calcolaCodiceSoggetto(soggettoNelModel, model.getRendicontoRichiesta().getRichiestaEconomale());
			log.debug(methodName, "Caricamento dei dati da servizio per soggetto " + codiceSoggetto);

			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(model.getRendicontoRichiesta().getRichiestaEconomale());
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			if(response.getSoggetto() == null) {
				String errorMsg = "Nessun soggetto corrispondente al codice " + codiceSoggetto + " trovato";
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", codiceSoggetto));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			soggetto = response.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			if (model.isGestioneHR() && model.getMaySearchHR() ) {
				listaModalitaPagamentoSoggetto = defaultingList(response.getSoggetto().getModalitaPagamentoList());
			
			} else {
				
				listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			}
			//listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		model.getRendicontoRichiesta().getRichiestaEconomale().setSoggetto(soggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}

	@Override
	protected Soggetto getSoggettoDaModel() {
		return model.getRendicontoRichiesta().getRichiestaEconomale().getSoggetto();
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
		checkNotNull(model.getRendicontoRichiesta().getRichiestaEconomale().getDatiTrasfertaMissione(), "Richiesta", true);
		checkNotNullNorEmpty(model.getRendicontoRichiesta().getRichiestaEconomale().getDatiTrasfertaMissione().getMotivo(), "Descrizione");
		
		// O restituzion altro ufficio o  totale o inserimento giustificativi
		checkCondition(Boolean.TRUE.equals(model.getRestituzioneAltroUfficio()) || Boolean.TRUE.equals(model.getRestituzioneTotale()) || !model.getListaGiustificativo().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Necessario effettuare un'operazione: selezionare 'Restituzione totale' o 'Restituzione altro ufficio' o inserire almeno un giustificativio"));
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
	 * Preparazione del metodo completeStep2().
	 */
	public void prepareCompleteStep2() {
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.getRendicontoRichiesta().setMovimento(null);
	}
	
	/**
	 * Validazione per il metodo completeStep2().
	 */
	public void validateCompleteStep2() {
		checkNotNull(model.getRendicontoRichiesta(), "Rendiconto", true);
		checkNotNull(model.getRendicontoRichiesta().getMovimento(), "Movimento", true);
		
		Movimento movimento = model.getRendicontoRichiesta().getMovimento();
		
		checkNotNull(movimento.getDataMovimento(), "Data operazione");
		checkNotNullNorInvalidUid(movimento.getModalitaPagamentoCassa(), "Modalita' di pagamento");
		checkNotNullNorInvalidUid(movimento.getModalitaPagamentoDipendente(), "Modalita' di pagamento del dipendente");
		checkDettaglioPagamento(movimento);
		
		checkMovimentoGestione(model.getMovimentoGestione(), model.getSubMovimentoGestione());
		if (model.getRestituzioneAltroUfficio().equals(Boolean.TRUE)) {
			
			impostaModalitaPagamentoAltroUfficio(movimento);
		} else {
		
			// Caricamento del dato per la modalita di pagamento cassa
			caricaModalitaPagamentoCassa(movimento);
			// SIAC-4736
			// Caricamento del dato per la modalita di pagamento dip
//			impostaModalitaPagamentoDipendente(movimento, movimento.getModalitaPagamentoCassa().getTipoDiCassa());
		}
	}
	
	@Override
	protected void caricaClassificatoriDaLista() {
		// Modalita' pagamento cassa
		if(model.getRendicontoRichiesta().getMovimento() != null && model.getRendicontoRichiesta().getMovimento().getModalitaPagamentoCassa() != null) {
			ModalitaPagamentoCassa modalitaPagamentoCassa = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoCassa(),
					model.getRendicontoRichiesta().getMovimento().getModalitaPagamentoCassa());
			model.getRendicontoRichiesta().getMovimento().setModalitaPagamentoCassa(modalitaPagamentoCassa);
		}
		
		// Classificatori dei giustificativi
		for(Giustificativo g : model.getRendicontoRichiesta().getGiustificativi()) {
			TipoGiustificativo tg = ComparatorUtils.searchByUidEventuallyNull(model.getListaTipoGiustificativo(), g.getTipoGiustificativo());
			g.setTipoGiustificativo(tg);
		}
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
	/**
	 * popola la richiesta Economale quella del model di base 
	 */
	public void popolaRichiestaEconomale() {
		if (model.getRichiestaEconomaleCopia() != null && model.getRichiestaEconomaleCopia().getUid() != 0) {
			model.setRichiestaEconomale(model.getRichiestaEconomaleCopia());
		}
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListaTipoGiustificativo() {
		return BilSessionParameter.LISTA_TIPO_GIUSTIFICATIVO_RIMBORSO;
	}
	
	/**
	 * Inizializzazione dei dati del movimento
	 * aggiungo gestione per restituzione altro ufficio (il pag &eacute; tramite iban della cassa)
	 */
	@Override
	protected void inizializzaDatiMovimento() {
		final String methodName = "inizializzaDatiMovimento";
		Movimento movimento = ottieniMovimentoDalModel();
		if(movimento == null) {
			log.debug(methodName, "Inizializzazione del movimento");
			movimento = new Movimento();
			impostaMovimentoNelModel(movimento);
		}
		
		Date now = new Date();
		log.debug(methodName, "Impostazione del default per la data movimento: " + now);
		movimento.setDataMovimento(now);
		TipoDiCassa tipoDiCassa = model.getCassaEconomale().getTipoDiCassa();
		log.debug(methodName, "Tipo di cassa economale: " + tipoDiCassa);
		if (model.getRestituzioneAltroUfficio().equals(Boolean.TRUE)) {
			
			impostaModalitaPagamentoAltroUfficio(movimento);
		} else {
			if(TipoDiCassa.CONTANTI.equals(tipoDiCassa) || TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(tipoDiCassa)) {
			
			
				setModalitaDiPagamentoByTipoDiCassa(movimento, tipoDiCassa);
				impostaModalitaPagamentoDipendente(movimento, tipoDiCassa);
			}
			
		}
		
	}
	
	/**
	 * Impostazione della modalit&agrave; di pagamento del dipendente.
	 * <br/>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa non &eacute; contanti, occorre preimpostare il campo con il valore della modalit&agrave: di pagamento del dipendente
	 *     presente sull'anagrafica soggetto (se compilata).
	 * </p>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa &eacute; 'Contanti' impostare il campo fisso a Contanti senza lasciare la possibilit&agrave; di modificarlo
	 * </p>
	 * 
	 * @param movimento il movimento da popolare
	 */
	protected void impostaModalitaPagamentoAltroUfficio(Movimento movimento) {
		final String methodName = "impostaModalitaPagamentoAltroUfficio";
		ModalitaPagamentoCassa mpc = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoCassa(), "CC");
		log.debug(methodName, "Restituzione Altro Ufficio . Ricercata la modalita' di pagamento cassa di tipo 'CC'. Trovata? "
				+ (mpc == null ? "false" : "true, con uid " + mpc.getUid()));
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), "CCB");
		log.debug(methodName, "Restituzione Altro Ufficio . Ricercata la modalita' di pagamento dipendente di tipo 'CCB'. Trovata? "
				+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
		
		// Imposto anche il dettaglio del pagamento come iban della cassa
		String dettaglioPagamento = model.getCassaEconomale().getNumeroContoCorrente();
	
		movimento.setModalitaPagamentoCassa(mpc);
		movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		movimento.setDettaglioPagamento(dettaglioPagamento);
	}
	
	/**
	 * Caricamento della lista dei tipi giustificativo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	@Override
	protected void caricaListaTipoGiustificativo() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoGiustificativo";
		List<TipoGiustificativo> listaTipoGiustificativo = sessionHandler.getParametro(getBilSessionParameterListaTipoGiustificativo());
		if(listaTipoGiustificativo == null) {
			log.debug(methodName, "Lista di TipoGiustificativo non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaTipoGiustificativo request = model.creaRequestRicercaTipoGiustificativo();
			logServiceRequest(request);
			RicercaTipoGiustificativoResponse response = richiestaEconomaleService.ricercaTipoGiustificativo(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(request, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaTipoGiustificativo = response.getTipiGiustificativi();
			sessionHandler.setParametro(getBilSessionParameterListaTipoGiustificativo(), listaTipoGiustificativo);
		}
		model.setListaTipoGiustificativo(listaTipoGiustificativo);
	}
}
