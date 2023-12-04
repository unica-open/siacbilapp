/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe base di action per l'inserimento e l'aggiornamento del rendiconto per l'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleAction<M extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleModel>
		extends BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1446193184749263830L;
	
	@Autowired private transient DocumentoIvaService documentoIvaService;

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

			//ho la richiesta quindi ho la matricola.. se il soggetto non ha la matricola gli forso quella della richiesta
			if (model.getRendicontoRichiesta().getRichiestaEconomale().getSoggetto()!= null && model.getRendicontoRichiesta().getRichiestaEconomale().getSoggetto().getMatricola() == null) {
				model.getRendicontoRichiesta().getRichiestaEconomale().getSoggetto().setMatricola(model.getRendicontoRichiesta().getRichiestaEconomale().getMatricola());
			}
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(model.getRendicontoRichiesta().getRichiestaEconomale());
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			}
			if(response.getSoggetto() == null) {
				String errorMsg = "Nessun soggetto corrispondente alla matricola " + codiceSoggetto + " trovato";
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
		
		//SIAC-7763 mantengo la descrizione poiche' richiesta
		// Pulisco la descrizione
//		rendicontoRichiesta.getRichiestaEconomale().setDescrizioneDellaRichiesta(null);
		RendicontoRichiesta rendiconto = model.getRendicontoRichiestaCopia() != null ? 
				model.getRendicontoRichiestaCopia() : model.getRendicontoRichiesta();
				
		// passo l'uid del rendiconto
		rendicontoRichiesta.setUid(rendiconto.getUid());
		
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
		
		// Caricamento del dato per la modalita di pagamento cassa
		caricaModalitaPagamentoCassa(movimento);
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
	public String completeStep3(){
		return innerCompleteStep3Rendiconto(model.getRendicontoRichiesta() != null ? model.getRendicontoRichiesta() : model.getRendicontoRichiestaCopia());
	}
	
	/**
	 * Caricamento della lista dei tipi giustificativo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
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
				String errorMsg = createErrorInServiceInvocationString(RicercaTipoGiustificativo.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaTipoGiustificativo = response.getTipiGiustificativi();
			sessionHandler.setParametro(getBilSessionParameterListaTipoGiustificativo(), listaTipoGiustificativo);
		}
		model.setListaTipoGiustificativo(listaTipoGiustificativo);
	}
	
	/**
	 * Ottiene il parametro di sessione per la lista dei tipi giustificativo-
	 * 
	 * @return il parametro di sessione per la lista
	 */
	protected BilSessionParameter getBilSessionParameterListaTipoGiustificativo() {
		return BilSessionParameter.LISTA_TIPO_GIUSTIFICATIVO_RIMBORSO;
	}
	
	/**
	 * Caricamento della lista delle valute
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaValuta() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaValuta";
		List<Valuta> listaValuta = sessionHandler.getParametro(BilSessionParameter.LISTA_VALUTA);
		if(listaValuta == null) {
			log.debug(methodName, "Lista di Valuta non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaValuta request = model.creaRequestRicercaValuta();
			logServiceRequest(request);
			RicercaValutaResponse response = documentoIvaService.ricercaValuta(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaValuta.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaValuta = response.getListaValuta();
			sessionHandler.setParametro(BilSessionParameter.LISTA_VALUTA, listaValuta);
		}
		model.setListaValuta(listaValuta);
	}
	
}
