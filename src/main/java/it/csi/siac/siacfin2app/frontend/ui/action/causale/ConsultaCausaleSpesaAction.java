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
import it.csi.siac.siacfin2app.frontend.ui.model.causale.ConsultaCausaleSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesaResponse;

/**
 * Classe di Action per la gestione della consultazione della Causale .
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 29/04/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCausaleSpesaAction extends GenericCausaleSpesaAction<ConsultaCausaleSpesaModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepareExecute() throws Exception {
		super.prepare();
		caricaListeCodifiche();
	}
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		log.debug(methodName, "Ricerco la causale");
		DettaglioStoricoCausaleSpesa request = model.creaRequestDettaglioStoricoCausaleSpesa();
		logServiceRequest(request);
		
		// cambiare con lo storico delle variazioni sulle causali
		DettaglioStoricoCausaleSpesaResponse response = preDocumentoSpesaService.dettaglioStoricoCausaleSpesa(request);
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
	
		model.setListaStoricoCausale(response.getCausaliSpesa(), listaTipoAtto, listaStrutturaAmministrativoContabile);
		
		return SUCCESS;
	}
	
}
