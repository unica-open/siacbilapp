/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.rimborsospese;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese.InserisciRimborsoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'inserimento del rimborso spese.
 * 
 * @author Domenico Lisi
 * @autoor Ahmad Nazha
 * @version 1.0.0 - 02/02/2015
 * @version 1.0.1 - 31/03/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaRimborsoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO)
public class InserisciRimborsoSpeseCassaEconomaleAction extends BaseInserisciAggiornaRimborsoSpeseCassaEconomaleAction<InserisciRimborsoSpeseCassaEconomaleModel> {


	/** Per la serializzazione */
	private static final long serialVersionUID = -8214244927429872240L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
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
		
		//Imposto il valore di confronto per il tipo fattura scatenante obbligatorietà di Anno e numero protocollo
		TipoGiustificativo tipoGiust = ComparatorUtils.findByCodice(model.getListaTipoGiustificativo(), BilConstants.CODICE_TIPO_GIUSTIFICATIVO_FATTURA.getConstant());
		if(tipoGiust != null) {
			log.debug(methodName, "Impostazione dell'uid tipo giustificativo FATTURA : uid " + tipoGiust.getUid());
			model.setUidTipoGiustificativoFattura(tipoGiust.getUid());
		}
		
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
			checkMatricola(re.getSoggetto());
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
		InserisceRichiestaEconomale request = model.creaRequestInserisceRichiestaEconomale();
		logServiceRequest(request);
		
		InserisceRichiestaEconomaleResponse response = richiestaEconomaleService.inserisceRichiestaEconomaleRimborsoSpese(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
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
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_RIMBORSO_SPESE_INSERISCI, AzioniConsentite.CASSA_ECONOMALE_RIMBORSO_SPESE_ABILITA};
	}
}
