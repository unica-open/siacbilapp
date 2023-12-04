/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conti;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.ConsultaPianoDeiContiModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.model.ContoModelDetail;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 * @param <M> la tipizzazione del model
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPianoDeiContiGSAAction<M extends ConsultaPianoDeiContiModel> extends GenericBilancioAction<M> {

	private static final long serialVersionUID = 3156633852266783402L;
	
	@Autowired private transient ContoService contoService;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		RicercaDettaglioConto request = model.creaRequestRicercaDettaglioConto();
		logServiceRequest(request);
		
		request.setContoModelDetails(ContoModelDetail.Attr, ContoModelDetail.ClassifExtended, ContoModelDetail.Soggetto, ContoModelDetail.ContoCollegato);
		
		RicercaDettaglioContoResponse response = contoService.ricercaDettaglioConto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioConto.class, response));
			addErrori(response);
			sessionHandler.setParametro(BilSessionParameter.ERRORI_AZIONE_PRECEDENTE, model.getErrori());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca del conto con uid " + model.getUidConto() + " avvenuta con successo");
		model.setConto(response.getConto());
		
		return SUCCESS;
	}
	

	/**
	 * Validazione del metodo {@link #execute()}.
	 */
	public void validateExecute() {
		checkCondition(model.getUidConto() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("uid conto"), true);
	}
	
	
	

}
