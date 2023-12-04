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
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.ConsultaPreDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;

/**
 * Classe di action per la consultazione del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/04/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPreDocumentoSpesaAction extends GenericPreDocumentoSpesaAction<ConsultaPreDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3410995502977493215L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Ricerca di dettaglio del PreDocumento
		RicercaDettaglioPreDocumentoSpesa request = model.creaRequestRicercaDettaglioPreDocumentoSpesa();
		logServiceRequest(request);
		RicercaDettaglioPreDocumentoSpesaResponse response = preDocumentoSpesaService.ricercaDettaglioPreDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio preDocumento spesa");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "PreDocumento caricato. Imposto i dati nel model");
		model.impostaPreDocumento(response.getPreDocumentoSpesa());
		
		AttoAmministrativo attoAmministrativo = caricaAttoAmministrativoSePresente();
		if(attoAmministrativo != null) {
			model.impostaAttoAmministrativo(attoAmministrativo);
		}
		
		return SUCCESS;
	}
	
}
