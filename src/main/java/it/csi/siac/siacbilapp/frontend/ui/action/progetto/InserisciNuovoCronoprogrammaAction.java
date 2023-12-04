/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.progetto.InserisciCronoprogrammaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

/**
 * Classe di Action per l'inserimento del Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @autor Nazha Ahmad
 * @version 1.1.0 - 13/02/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciCronoprogrammaAction.MODEL_NAME_INSERIMENTO_SENZA_PROGETTO)
public class InserisciNuovoCronoprogrammaAction extends BaseInserisciCronoprogrammaAction<InserisciCronoprogrammaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5621668075903832652L;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	@SkipValidation
	public String execute() throws Exception {
		final String methodName = "execute";
		checkCasoDUsoApplicabile();
		//task-170
		caricaListaTipiProgetto();
		
		try {
			effettuaCaricamentoDettaglioProgetto();
//			effettuaCaricamentoCronoprogrammiCollegati();
			//task-170
			model.setTipoProgettoRicerca(model.getProgetto().getTipoProgetto());
			effettuaCaricamentoListeClassificatori();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		model.setCronoAggiornabile(true);
		
		return SUCCESS;
	}
	
	@Override
	public String inserisciCDU() {
		final String methodName = "inserisciCDU";
		
		try {
			// Non vi sono cronoprogrammi già associati
			inserisceCronoprogramma();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}
	
}
