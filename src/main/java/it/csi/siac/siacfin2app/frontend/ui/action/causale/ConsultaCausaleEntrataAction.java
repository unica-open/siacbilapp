/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.ConsultaCausaleEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrataResponse;

/**
 * Classe di Action per la gestione della consultazione della Causale .
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCausaleEntrataAction extends GenericCausaleEntrataAction<ConsultaCausaleEntrataModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = -3271382344882403694L;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		log.debug(methodName, "Ricerco la causale");
		DettaglioStoricoCausaleEntrata request = model.creaRequestDettaglioStoricoCausaleEntrata();
		logServiceRequest(request);
		
		// cambiare con lo storico delle variazioni sulle causali
		DettaglioStoricoCausaleEntrataResponse response = preDocumentoEntrataService.dettaglioStoricoCausaleEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione della response del dettaglio di storico della causale di entrata con uid " +
					model.getUidDaConsultare());
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Storico causale ottenuta");
			
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = 
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
	
		model.setListaStoricoCausale(response.getCausaliEntrata(), listaTipoAtto, listaStrutturaAmministrativoContabile);
		
		return SUCCESS;
	}
	
}
