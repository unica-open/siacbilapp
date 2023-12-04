/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.categoriacespiti;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti.InserisciCategoriaCespitiModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespitiResponse;

/**
 * The Class GenericTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCategoriaCespitiAction extends GenericCategoriaCespitiAction<InserisciCategoriaCespitiModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -3359815219657200661L;
	

	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaListaTipoCalcolo();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		model.setCategoriaCespiti(null);
		return SUCCESS;		
	}
	
	/**
	 * Validate salva.
	 */
	public void validateSalva() {
		validaCampiCategoriaCespiti();
	}
	
	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		final String methodName = "salva";
		//creo la request da passare al servizio
		InserisciCategoriaCespiti req = model.creaRequestInserisciCategoriaCespiti();
		//chiamo il servizio di inserimento
		InserisciCategoriaCespitiResponse response = classificazioneCespiteService.inserisciCategoriaCespiti(req);
		//controllo se si siano verificati error
		if(response.hasErrori()) {
			//Si sono verificati errori 
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'inserimento della categoria.");
			return INPUT;
		}
		//popolo il model con i dati restituiti dalla response
		model.setCategoriaCespiti(response.getCategoriaCespiti());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}

}
