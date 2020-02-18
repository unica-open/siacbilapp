/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.quadroeconomico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.quadroeconomico.ElencoQuadroEconomicoModel;
import it.csi.siac.siacbilser.frontend.webservice.QuadroEconomicoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValido;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValidoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

/**
 * Classe di action per l'elenco dei quadro_economico.
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoQuadroEconomicoAction extends GenericBilancioAction <ElencoQuadroEconomicoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3684752948745638585L;
	
	@Autowired private transient QuadroEconomicoService quadroEconomicoService;
	
	@Override
	public String execute() {
		final String methodName = "execute";
		
		try {
			caricaQuadroEconomico();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		return SUCCESS;
	}

	/**
	 * Caricamento dei quadro_economico
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaQuadroEconomico() throws WebServiceInvocationFailureException {
		final String methodName = "caricaQuadroEconomico";
		
		List<QuadroEconomico> listaQuadroEconomico = sessionHandler.getParametro(BilSessionParameter.LISTA_QUADRO_ECONOMICO);
		
		if(listaQuadroEconomico == null) {
			log.debug(methodName, "Ricerca dei quadro_economico da servizio");
			RicercaQuadroEconomicoValido req = model.creaRequestRicercaQuadroEconomicoValido();
			RicercaQuadroEconomicoValidoResponse res = quadroEconomicoService.ricercaQuadroEconomicoValido(req);
			
			// Se ho errori esco subito
			if(res.hasErrori()) {
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			listaQuadroEconomico = res.getListaQuadroEconomico();
			log.debug(methodName, "Impostazione della lista dei risultati in sessione");
			sessionHandler.setParametro(BilSessionParameter.LISTA_QUADRO_ECONOMICO, listaQuadroEconomico);
		}
		model.setListaQuadroEconomico(listaQuadroEconomico);
	}
	
}
