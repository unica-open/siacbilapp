/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RisultatiRicercaTestataDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;

/**
 * Action per i risultati di ricerca della testata del documento di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/07/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTestataDocumentoSpesaAction extends GenericBilancioAction<RisultatiRicercaTestataDocumentoSpesaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1322988916623994822L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		if(posizioneStart == null) {
			//setto la posizione start a zero
			posizioneStart = Integer.valueOf(0);
		}
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		//carico la start position da sessione
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			//parto dal valore ottenuto dalla sessione, se presente
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		
		//ottengo i dati dalla sessione
		model.setRiepilogoRicerca((String) sessionHandler.getParametro(BilSessionParameter.RIEPILOGO_RICERCA_TESTATA_DOCUMENTO));
		
		model.setImportoTotale((BigDecimal) sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA));
		log.debug(methodName, "StartPosition = " + startPosition);
		
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid del documento da aggiornare: " + model.getUidDaAggiornare());
		// imposto in sessione il parametro che mi dice che ho effettuato una redirezione
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid del documento da consultare: " + model.getUidDaConsultare());
		//la redirezione DEVE essere effettuata da struts
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di consultazione quote.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	
	public String ottieniListaQuoteSpesa(){
		final String methodName = "ottieniListaQuoteSpesa";
		log.debug(methodName, "Uid del documento di cui consultare le quote: " + model.getUidDaConsultare());
		
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = model.creaRicercaSinteticaQuoteByDocumentoSpesa();
		logServiceRequest(request);
		
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse response = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(request);
		logServiceResponse(response);
		
		if(response.isFallimento()) {
			// ci sono stati errori nel servizio
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuote(response.getTotale());
		//setto in sessione la request e la lista per l'ajax action
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, response.getSubdocumentiSpesa());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ottieniListaQuoteSpesa()}.
	 */
	public void validateOttieniListaQuoteSpesa() {
		//devo avere un uid con cui cercare la quota
		checkCondition(model.getUidDaConsultare() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Documento"));
	}
		
}
