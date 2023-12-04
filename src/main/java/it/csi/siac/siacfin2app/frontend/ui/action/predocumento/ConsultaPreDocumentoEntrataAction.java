/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.ConsultaPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;

/**
 * Classe di action per la consultazione del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/04/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPreDocumentoEntrataAction extends GenericPreDocumentoEntrataAction<ConsultaPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5903151887055459172L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Ricerca di dettaglio del PreDocumento
		RicercaDettaglioPreDocumentoEntrata request = model.creaRequestRicercaDettaglioPreDocumentoEntrata();
		logServiceRequest(request);
		RicercaDettaglioPreDocumentoEntrataResponse response = preDocumentoEntrataService.ricercaDettaglioPreDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio preDocumento entrata");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "PreDocumento caricato. Imposto i dati nel model");
		model.impostaPreDocumento(response.getPreDocumentoEntrata());
		
		AttoAmministrativo attoAmministrativo = caricaAttoAmministrativoSePresente();
		if(attoAmministrativo != null) {
			model.impostaAttoAmministrativo(attoAmministrativo);
		}
		
		return SUCCESS;
	}
	
}
