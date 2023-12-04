/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.RicercaClassificatoreGerarchicoModel;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricarca del classificatore gerarchico.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/12/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaClassificatoreGerarchicoAction extends GenericBilancioAction<RicercaClassificatoreGerarchicoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3885726072832698003L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Ricerca del SIOPE di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String siopeSpesa() {
		return baseRicerca("ricercaSiopeSpesa", TipologiaClassificatore.SIOPE_SPESA_I, BilSessionParameter.REQUEST_RICERCA_SIOPE_SPESA, BilSessionParameter.RISULTATI_RICERCA_SIOPE_SPESA);
	}
	
	/**
	 * Validazione per il metodo {@link #siopeSpesa()}.
	 */
	public void validateSiopeSpesa() {
		baseValidate();
	}
	
	/**
	 * Ricerca del SIOPE di entrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String siopeEntrata() {
		return baseRicerca("ricercaSiopeEntrata", TipologiaClassificatore.SIOPE_ENTRATA_I, BilSessionParameter.REQUEST_RICERCA_SIOPE_ENTRATA, BilSessionParameter.RISULTATI_RICERCA_SIOPE_ENTRATA);
	}
	
	/**
	 * Validazione per il metodo {@link #siopeEntrata()}.
	 */
	public void validateSiopeEntrata() {
		baseValidate();
	}

	/**
	 * Ricerca di base.
	 * 
	 * @param methodName il metodo per cui effettuare i log
	 * @param tipologiaClassificatore la tipologia di classificatore
	 * @param parametroRequest il parametro per impostare in sessione la request
	 * @param parametroLista il parametro per impostare in sessione la lista
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	private String baseRicerca(final String methodName, TipologiaClassificatore tipologiaClassificatore,BilSessionParameter parametroRequest, BilSessionParameter parametroLista) {
		RicercaSinteticaClassificatore request = model.creaRequestRicercaSinteticaClassificatore(tipologiaClassificatore);
		logServiceRequest(request);
		RicercaSinteticaClassificatoreResponse response = classificatoreBilService.ricercaSinteticaClassificatore(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaClassificatore.class, response));
			return INPUT;
		}
		if(response.getCodifiche().isEmpty()) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			log.debug(methodName, "Nessun dato fornito per i criteri di ricerca");
			return INPUT;
		}
		
		log.debug(methodName, "Reperiti " + response.getTotaleElementi() + " elementi per la ricerca");
		// Imposto i dati in sessione
		sessionHandler.setParametroXmlType(parametroRequest, request);
		sessionHandler.setParametro(parametroLista, response.getCodifiche());
		
		return SUCCESS;
	}
	
	/**
	 * Implementazione base della validazione
	 */
	private void baseValidate() {
		checkNotNull(model.getClassificatore(), "Classificatore", true);
		checkCondition(StringUtils.isNotBlank(model.getClassificatore().getCodice()) || StringUtils.isNotBlank(model.getClassificatore().getDescrizione()),
				ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
}
