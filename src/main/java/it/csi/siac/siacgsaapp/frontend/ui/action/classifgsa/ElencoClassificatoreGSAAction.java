/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.classifgsa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacgenser.frontend.webservice.ClassificatoreGSAService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValido;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValidoResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgsaapp.frontend.ui.model.classifgsa.ElencoClassificatoreGSAModel;

/**
 * Classe di action per l'elenco dei classificatori GSA.
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoClassificatoreGSAAction extends GenericBilancioAction <ElencoClassificatoreGSAModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3684752948745638585L;
	
	@Autowired private transient ClassificatoreGSAService classificatoreGSAService;
	
	@Override
	public String execute() {
		final String methodName = "execute";
		
		try {
			caricaClassificatoriGSA();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		return SUCCESS;
	}

	/**
	 * Caricamento dei classificatori GSA
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaClassificatoriGSA() throws WebServiceInvocationFailureException {
		final String methodName = "caricaClassificatoriGSA";
		
		List<ClassificatoreGSA> listaClassificatoreGSA = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GSA);
		
		if(listaClassificatoreGSA == null) {
			log.debug(methodName, "Ricerca dei classificatori GSA da servizio");
			RicercaClassificatoreGSAValido req = model.creaRequestRicercaClassificatoreGSAValido();
			RicercaClassificatoreGSAValidoResponse res = classificatoreGSAService.ricercaClassificatoreGSAValido(req);
			
			// Se ho errori esco subito
			if(res.hasErrori()) {
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			listaClassificatoreGSA = res.getClassificatoriGSA();
			log.debug(methodName, "Impostazione della lista dei risultati in sessione");
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GSA, listaClassificatoreGSA);
		}
		model.setListaClassificatoreGSA(listaClassificatoreGSA);
	}
	
}
