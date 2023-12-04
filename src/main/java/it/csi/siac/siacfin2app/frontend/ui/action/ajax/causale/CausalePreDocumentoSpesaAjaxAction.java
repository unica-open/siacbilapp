/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.causale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.causale.CausalePreDocumentoSpesaAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;

/**
 * Classe di actoin per il caricamento della causale di spesa per il PreDocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/04/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CausalePreDocumentoSpesaAjaxAction extends GenericBilancioAction<CausalePreDocumentoSpesaAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7062074207750685611L;
	
	@Autowired private transient PreDocumentoSpesaService preDocumentoSpesaService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		Integer uidTipoCausale = model.getUidTipoCausale();
		Integer uidSAC = model.getUidStrutturaAmministrativoContabile();
		log.debug(methodName, "id tipo: " + uidTipoCausale);
		log.debug(methodName, "id sac: " + uidSAC);
		
		if(uidTipoCausale == null || uidTipoCausale.intValue() == 0) {
			log.debug(methodName, "Id non fornito");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo causale"));
			return SUCCESS;
		}
		
		// Creazione della request
		RicercaSinteticaCausaleSpesa request = model.creaRequestRicercaSinteticaCausaleSpesa();
		
		log.debug(methodName, "Richiamo il WebService");
		RicercaSinteticaCausaleSpesaResponse response = preDocumentoSpesaService.ricercaSinteticaCausaleSpesa(request);
		log.debug(methodName, "Richiamato il WebService");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errori nella response");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaCausale(response.getCausaliSpesa());
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_SPESA, response.getCausaliSpesa());
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca di dettaglio per la causale di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		
		// Creazione della request
		RicercaDettaglioCausaleSpesa request = model.creaRequestRicercaDettaglioCausaleSpesa();
		logServiceRequest(request);
		RicercaDettaglioCausaleSpesaResponse response = preDocumentoSpesaService.ricercaDettaglioCausaleSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(RicercaDettaglioCausaleSpesa.class, response));
			addErrori(response);
			return SUCCESS;
		}
		// Imposto il dato nel model
		model.setCausaleSpesa(response.getCausaleSpesa());
		
		return SUCCESS;
	}
	
}
