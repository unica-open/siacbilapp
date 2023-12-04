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
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RisultatiRicercaTestataDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrataResponse;

/**
 * Action per i risultati di ricerca del documento
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/07/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTestataDocumentoEntrataAction extends GenericBilancioAction<RisultatiRicercaTestataDocumentoEntrataModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8141703648786610701L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		if(posizioneStart == null) {
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
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
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
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di consultazione quote.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	
	public String ottieniListaQuoteEntrata(){
		final String methodName = "ottieniListaQuoteSpesa";
		log.debug(methodName, "Uid del documento di cui consultare le quote: " + model.getUidDaConsultare());
	
		RicercaSinteticaModulareQuoteByDocumentoEntrata request = model.creaRequestRicercaSinteticaQuoteByDocumentoEntrata();
		logServiceRequest(request);
		
		RicercaSinteticaModulareQuoteByDocumentoEntrataResponse response = documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(request);
		logServiceResponse(response);
		
		if(response.isFallimento()) {
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuote(response.getTotale());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA, response.getSubdocumentiEntrata());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ottieniListaQuoteEntrata()}.
	 */
	public void validateOttieniListaQuoteEntrata() {
		checkCondition(model.getUidDaConsultare() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Documento"));
	}
	
}
