/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.OttieniFigliEntitaConsultabileAjaxModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.ConsultazioneEntitaService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabileResponse;

/**
 * @author Elisa chiari
 * @version 1.0.0 - 17/02/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class OttieniFigliEntitaConsultabileAjaxAction extends GenericBilancioAction<OttieniFigliEntitaConsultabileAjaxModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -362506507563143787L;

	@Autowired private transient ConsultazioneEntitaService consultazioneEntitaService;
	
	@Override
	public String execute(){
		
		final String methodName = "execute";
		OttieniNavigazioneTipoEntitaConsultabile request = model.creaRequestOttieniFigliEntitaConsultabile();
		logServiceRequest(request);
		log.debug(methodName, "Richiamo il webservice di ricerca navizazione");
		OttieniNavigazioneTipoEntitaConsultabileResponse response = consultazioneEntitaService.ottieniNavigazioneTipoEntitaConsultabile(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio OttieniFigliEntitaConsultabileAjaxAction");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaFigliEntitaConsultabile(response.getEntitaConsultabili());
		model.setParent(response.isParent());
		return SUCCESS;
	}

 }

