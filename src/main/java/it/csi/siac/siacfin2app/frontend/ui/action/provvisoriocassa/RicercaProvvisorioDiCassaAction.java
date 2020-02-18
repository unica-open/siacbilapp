/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.provvisoriocassa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa.RicercaProvvisorioDiCassaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;

/**
 * Classe di action per le ricerche del provvisorio di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/10/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaProvvisorioDiCassaAction extends GenericBilancioAction<RicercaProvvisorioDiCassaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7245366022572914127L;
	
	@Autowired private transient ProvvisorioService provvisorioService;
	@Autowired private transient PreDocumentoSpesaService preDocumentoSpesaService;

	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		try {
			caricaListaContoTesoreria();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage(), e);
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}
	
	/**
	 * Ricerca sintetica del provvisorio.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSintetica(){
		final String methodName = "ricercaSintetica";

		// SIAC-7016
		// Eseguo il controllo della ricerca valida prima dei dati valorizzati
		// altrimenti lancia InvocationTargetException a causa di troppi valori nulli nella Reflection
		boolean ricercaValida = checkRicercaValida();
		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		checkDatiValorizzatiRicerca();

//		if(model.getErrori().size() > 0) {
		if(hasErrori()) {
			log.debug(methodName, "Errore nella validazione dei campi");
			return SUCCESS;
		}
		
		RicercaProvvisoriDiCassa request = model.creaRequestRicercaProvvisoriDiCassa();
		logServiceRequest(request);
		RicercaProvvisoriDiCassaResponse response = provvisorioService.ricercaProvvisoriDiCassa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		if(response.getNumRisultati() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		// Imposto i dati in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVISORIO_DI_CASSA, response.getElencoProvvisoriDiCassa());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROVVISORIO_DI_CASSA, request);
		// XXX: purtroppo, la lista restituida dal servizio non e' paginata. Per avere i dati di cui necessito, devo impostare qualche dato in piu' in sessione
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}
	
	/**
	 * Controlla che i parametri per la ricerca siano stati valorizzati
	 */
	private void checkDatiValorizzatiRicerca() {
		// Controllo formale sui da-a
		checkCondition(
				model.getAnnoDa() == null || model.getAnnoA() == null || model.getAnnoDa().compareTo(model.getAnnoA()) <= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("Anno Da/A", "l'anno Da non deve essere successivo l'anno A")
				);
		checkCondition(
				model.getNumeroDa() == null || model.getNumeroA() == null || model.getNumeroDa().compareTo(model.getNumeroA()) <= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("Numero Da/A", "il numero Da non deve essere successivo il numero A")
				);
		checkCondition(
				model.getDataEmissioneDa() == null || model.getDataEmissioneA() == null || model.getDataEmissioneDa().compareTo(model.getDataEmissioneA()) <= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("Data Da/A", "la data Da non deve essere successiva la data A")
				);
		checkCondition(
				model.getImportoDa() == null || model.getImportoA() == null || model.getImportoDa().compareTo(model.getImportoA()) <= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("importo Da/A", "l'importo Da non deve essere successivo l'importo A")
				);
		
	}
	
	/** 
	 * SIAC-7016
	 * Separo il metodo da checkDatiValorizzatiRicerca per creare una Reflection
	 * Controlla che alemno un campo di ricerca sia valido.
	 * @return boolean se false lancerà l'errore > "Indicare almeno un criterio di ricerca"
	 */
	public boolean checkRicercaValida() {
		boolean ricercaValida = (
				model.getAnnoDa() != null 
				|| model.getAnnoA() != null
				|| model.getNumeroDa() != null 
				|| model.getNumeroA() != null
				|| model.getDataEmissioneDa() != null
				|| model.getDataEmissioneA() != null
				|| model.getImportoDa() != null 
				|| model.getImportoA() != null
				|| Boolean.TRUE.equals(model.isFlagProvvisoriPagoPA() != null ? model.isFlagProvvisoriPagoPA() : Boolean.FALSE )
//				|| model.getDescCausale() != null
//				|| model.getDenominazioneSoggetto() != null
//				|| StringUtils.isNotBlank(model.getContoTesoreria().getCodice())
				);
		
		return ricercaValida;
	}
	
	/**
	 * Ricerca il provvisorio per chiave.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaPerChiave() {
		checkDatiValorizzatiRicercaPerChiave();
		final String methodName = "ricercaPerChiave";
		checkDatiValorizzatiRicerca();
		if(hasErrori()) {
			log.debug(methodName, "Errore nella validazione dei campi");
			return SUCCESS;
		}
		
		RicercaProvvisorioDiCassaPerChiave request = model.creaRequestRicercaProvvisorioDiCassaPerChiave();
		logServiceRequest(request);
		RicercaProvvisorioDiCassaPerChiaveResponse response = provvisorioService.ricercaProvvisorioDiCassaPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		model.setProvvisorioDiCassa(response.getProvvisorioDiCassa());
		return SUCCESS;
	}

	private void checkDatiValorizzatiRicercaPerChiave() {
		checkNotNull(model.getProvvisorioDiCassa(), "Provvisorio");
		checkCondition(model.getProvvisorioDiCassa() == null || model.getProvvisorioDiCassa().getAnno() != null,ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("anno provvisorio"));
		checkCondition(model.getProvvisorioDiCassa() == null || model.getProvvisorioDiCassa().getNumero() != null,ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("numero provvisorio"));
	}

	/**
	 * Carica la lista del Conto Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaContoTesoreria() throws WebServiceInvocationFailureException {
		List<ContoTesoreria> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CONTO_TESORERIA);
		if(listaInSessione == null) {
			LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
			logServiceRequest(request);
			LeggiContiTesoreriaResponse response = preDocumentoSpesaService.leggiContiTesoreria(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaContoTesoreria");
			}
			
			listaInSessione = response.getContiTesoreria();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, listaInSessione);
		}
		
		model.setListaContoTesoreria(listaInSessione);
	}
	
}
