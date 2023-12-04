/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.InserisciAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogata;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'inserimento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 22/01/2015
 * @version 1.0.1 - 31/03/2015
 * @version 1.1.0 - 07/12/2015 - integrazione HR
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO)
public class InserisciAnticipoSpesePerMissioneCassaEconomaleAction extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleAction<InserisciAnticipoSpesePerMissioneCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1337173859871196765L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		sessionHandler.setParametro(BilSessionParameter.LISTA_MISSIONE_CARICAMENTO_ESTERNO, null);
		// Richiamo semplicemente il super. Aggiungo il breadcrumb
		return super.execute();
	}
	
	@Override
	protected void impostazioneValoriDefaultStep1() {
		final String methodName = "impostazioneValoriDefaultStep1";
		RichiestaEconomale richiestaEconomale = model.getRichiestaEconomale();
		if(richiestaEconomale == null) {
			log.debug(methodName, "Inizializzazione della richiesta economale");
			richiestaEconomale = new RichiestaEconomale();
			model.setRichiestaEconomale(richiestaEconomale);
		}
		DatiTrasfertaMissione datiTrasfertaMissione = richiestaEconomale.getDatiTrasfertaMissione();
		if(datiTrasfertaMissione == null) {
			log.debug(methodName, "Inizializzazione dei dati di trasferta missione");
			datiTrasfertaMissione = new DatiTrasfertaMissione();
			richiestaEconomale.setDatiTrasfertaMissione(datiTrasfertaMissione);
		}
		log.debug(methodName, "Impostazione del default per il flag estero: FALSE");
		datiTrasfertaMissione.setFlagEstero(Boolean.FALSE);
		
		Valuta valutaEuro = ComparatorUtils.findByCodice(model.getListaValuta(), BilConstants.VALUTA_CODICE_EURO.getConstant());
		if(valutaEuro != null) {
			log.debug(methodName, "Impostazione del default per il le valute: EURO, uid " + valutaEuro.getUid());
			model.setUidValutaEuro(valutaEuro.getUid());
		}
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		final String methodName = "prepareCompleteStep1";
		// Se sono da caricamento esterno, non pulisco
		if(model.getRichiestaEconomale() != null && StringUtils.isNotBlank(model.getRichiestaEconomale().getIdMissioneEsterna())) {
			log.debug(methodName, "Da caricamento esterno");
			return;
		}
		
		// Pulisco i campi. Basta pulirne uno
		model.setRichiestaEconomale(null);
		model.getMezziDiTrasportoSelezionati().clear();
	}
	
	@Override
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		// TODO: valutare la seguente asserzione
		// Se sono da caricamento esterno mi fido dei dati ottenuti
		if(StringUtils.isNotBlank(model.getRichiestaEconomale().getIdMissioneEsterna())) {
			log.debug(methodName, "Da caricamento esterno");
			try {
				checkMatricola(model.getRichiestaEconomale().getSoggetto());
			} catch(ParamValidationException pve) {
				log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
			} catch(WebServiceInvocationFailureException wsife) {
				log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
			}
			return;
		}
		
		checkNotNull(model.getRichiestaEconomale().getDatiTrasfertaMissione(), "Dati della richiesta", true);
		// Ho la richiesta
		RichiestaEconomale re = model.getRichiestaEconomale();
		DatiTrasfertaMissione dtm = model.getRichiestaEconomale().getDatiTrasfertaMissione();
		try {
			checkMatricola(re.getSoggetto());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		}
		
		checkNotNullNorEmpty(dtm.getMotivo(), "Motivo trasferta");
		checkNotNull(dtm.getFlagEstero(), "Estero");
		checkNotNullNorEmpty(dtm.getLuogo(), "Luogo");
		
		checkValidDate(dtm.getDataInizio(), "Data inizio");
		checkValidDate(dtm.getDataFine(), "Data fine");
		checkCondition(dtm.getDataInizio() == null || dtm.getDataFine() == null || dtm.getDataInizio().compareTo(dtm.getDataFine()) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data fine", ": non deve essere precedente alla data inizio"));
		checkCondition(!model.getListaGiustificativo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Giustificativi"));
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
		InserisceRichiestaEconomale request = model.creaRequestInserisceRichiestaEconomale();
		logServiceRequest(request);
		InserisceRichiestaEconomaleResponse response = richiestaEconomaleService.inserisceRichiestaEconomaleAnticipoSpesePerMissione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceRichiestaEconomale.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Inserita richiesta economale con uid " + response.getRichiestaEconomale().getUid());
		
		// Imposto i dati nel model
		model.setRichiestaEconomale(response.getRichiestaEconomale());
		
		caricaClassificatoriDaLista();
		
		impostaMessaggioSuccessoPerStep3();
		cleanImpegnoFromSession();

		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkNotNull(model.getRichiestaEconomale(), "Movimento", true);
		checkNotNull(model.getRichiestaEconomale().getMovimento(), "Movimento", true);
		// nel caso sia una cssa non mista il select non passerebbe il valore perche' disabilitato quindi metto il valore forzato
		
		if (!TipoDiCassa.MISTA.equals(model.getCassaEconomale().getTipoDiCassa())){
			for (ModalitaPagamentoCassa mpc : model.getListaModalitaPagamentoCassa()) {
				if (model.getCassaEconomale().getTipoDiCassa().equals(mpc.getTipoDiCassa())) {
					model.getRichiestaEconomale().getMovimento().setModalitaPagamentoCassa(mpc);
				}
			}
			
		}
		Movimento m = model.getRichiestaEconomale().getMovimento();
		
		checkNotNull(m.getDataMovimento(), "Data operazione");
		checkNotNullNorInvalidUid(m.getModalitaPagamentoCassa(), "Modalita' di pagamento");
		checkNotNullNorInvalidUid(m.getModalitaPagamentoDipendente(), "Modalita' di pagamento del dipendente");
		checkDettaglioPagamento(m);
		
		checkMovimentoGestione(model.getMovimentoGestione(), model.getSubMovimentoGestione());
		// Se non ho specificato alcunche' ma Struts2 ha impostato il campo
		if(m.getModalitaPagamentoSoggetto() != null && m.getModalitaPagamentoSoggetto().getUid() == 0) {
			m.setModalitaPagamentoSoggetto(null);
		}
	}
	
	/**
	 * Pulisce il form dello step 1 riportandolo allo stato iniziale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1(){
		model.setRichiestaEconomale(null);
		model.setListaGiustificativo(null);
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
		model.getRichiestaEconomale().getMovimento().setModalitaPagamentoCassa(null);
		model.getRichiestaEconomale().getMovimento().setModalitaPagamentoDipendente(null);
		model.getRichiestaEconomale().getMovimento().setDettaglioPagamento(null);
		model.getRichiestaEconomale().getMovimento().setBic(null);
		model.getRichiestaEconomale().getMovimento().setContoCorrente(null);
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #caricaDettaglioRichiestaEconomaleEsterna()}.
	 */
	public void prepareCaricaDettaglioRichiestaEconomaleEsterna() {
		model.setIdMissioneEsterna(null);
	}
	
	/**
	 * Carica il dettaglio della richiesta economale da esterno e imposta i campi corrispondenti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaDettaglioRichiestaEconomaleEsterna() {
		final String methodName = "caricaDettaglioRichiestaEconomaleEsterna";
		
		RicercaDettaglioRichiestaAnticipoMissioneNonErogata request = model.creaRequestRicercaDettaglioRichiestaAnticipoMissioneNonErogata();
		logServiceRequest(request);
		RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse response = richiestaEconomaleService.ricercaDettaglioRichiesaAnticipoMissioneNonErogata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioRichiestaAnticipoMissioneNonErogata.class, response));
			return INPUT;
		}
		
		if(response.getRichiestaEconomale() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Richiesta economale", model.getIdMissioneEsterna()));
			log.info(methodName, "Nessuna richiesta economale trovata per id " + model.getIdMissioneEsterna());
		}
		// Separo i dati
		List<Giustificativo> listaGiustificativo = response.getRichiestaEconomale().getGiustificativi();
		response.getRichiestaEconomale().setGiustificativi(null);
		
		model.setRichiestaEconomale(response.getRichiestaEconomale());
		model.setListaGiustificativo(listaGiustificativo);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #caricaDettaglioRichiestaEconomaleEsterna()}.
	 */
	public void validataCaricaDettaglioRichiestaEconomaleEsterna() {
		checkNotNullNorEmpty(model.getIdMissioneEsterna(), "Missione da caricamento esterno");
	}
	
	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_INSERISCI, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_ABILITA};
	}
}
