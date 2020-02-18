/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RicercaElencoDocumentiAllegatoAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Action per i risultati di ricerca dell'ElencoDocumentiAllegato.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaElencoDocumentiAllegatoAjaxAction extends GenericBilancioAction<RicercaElencoDocumentiAllegatoAjaxModel> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -2105982047738484846L;
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		// Validazione del dato
		validazione();
		
		if(hasErrori()) {
			// Errore di validazione
			log.debug(methodName, "Errore nella validazione dei dati forniti in input");
			return SUCCESS;
		}
		// Ricerco l'elenco
		RicercaElenco request = model.creaRequestRicercaElenco();
		logServiceRequest(request);
		RicercaElencoResponse response = allegatoAttoService.ricercaElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Ho un errore nel servizio: fornisco l'errore ed esco
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		// Dati trovati. Imposto i dati in sessione
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO, response.getElenchiDocumentiAllegato());

		// Esco
		return SUCCESS;
	}
	
	/**
	 * Validazione del dato.
	 */
	private void validazione() {
		ElencoDocumentiAllegato eda = model.getElencoDocumentiAllegato();
		int sumValidParams = 0;
		if(eda != null) {
			sumValidParams = isObjectPresent(eda.getAnno())
				+ isObjectPresent(eda.getNumero())
				+ isObjectPresent(eda.getAnnoSysEsterno())
				+ isStringPresent(eda.getNumeroSysEsterno())
				+ isObjectPresent(eda.getDataTrasmissione())
				+ isObjectPresent(eda.getStatoOperativoElencoDocumenti());
		}
		// Devono essere presenti almeno DUE criteri di ricerca
		if(sumValidParams < 2) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
			return;
		}
		
		// Validazione logica
		Date now = new Date();
		Integer currentYear = computeYearFromDate(now);
		
		checkCondition(eda.getAnno() == null || eda.getAnno().compareTo(currentYear) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno", ":non deve essere successivo all'anno corrente"));
		checkCondition(eda.getNumero() == null || eda.getNumero().compareTo(Integer.valueOf(0)) > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero", ":deve essere positivo"));
		checkCondition(eda.getAnnoSysEsterno() == null || eda.getAnnoSysEsterno().compareTo(currentYear) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Fonte dati", ":non deve essere successivo all'anno di esercizio"));
		checkCondition(eda.getDataTrasmissione() == null || !eda.getDataTrasmissione().after(now),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Data trasmissione", ":non deve essere successiva alla data corrente"));
	}
	
	/**
	 * Computa l'anno corrispondente alla data fornita.
	 * 
	 * @param date la data di cui calcolare l'anno
	 * 
	 * @return l'anno della data
	 */
	private Integer computeYearFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return Integer.valueOf(cal.get(Calendar.YEAR));
	}

	/**
	 * Controlla se la stringa sia presente.
	 * 
	 * @param s la stringa da controllare
	 * 
	 * @return <code>1</code> se la stringa &eacute; presente; <code>0</code> in caso contrario
	 */
	private int isStringPresent(String s) {
		return StringUtils.isNotEmpty(s) ? 1 : 0;
	}
	
	/**
	 * Controlla se l'oggetto sia presente.
	 * 
	 * @param o l'oggetto da controllare
	 * 
	 * @return <code>1</code> se l'oggetto &eacute; presente; <code>0</code> in caso contrario
	 */
	private int isObjectPresent(Object o) {
		return o != null ? 1 : 0;
	}
	
}