/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.crud;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud.AggiornaMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siaccorser.model.Informazione;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaMutuoAction extends BaseInserisciAggiornaMutuoAction<AggiornaMutuoModel>{

	private static final long serialVersionUID = 2020318842439985171L;
		
	public String enterPage() throws Exception {
		final String methodName = "enterPage";
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo();
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		model.setMutuoFromResponse(response);
		
		return SUCCESS;
	}
	
	public String salva() {
		beforeSalva();
		
		AggiornaMutuo req = model.creaRequestAggiornaMutuo();
		logServiceRequest(req);
		
		AggiornaMutuoResponse res = mutuoService.aggiornaMutuo(req);
		logServiceResponse(res);

		if (res.hasErrori()) {
			addErrori(res);
			log.debug("salva", "Si sono verificati errori nell'invocazione del servizio");
			prepareEnterPage();
			return INPUT;
		}
		
		impostaInformazioneSuccesso();
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}
	
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Annullamento del mutuo avente uid=" + model.getMutuo().getUid());
		
		log.debug(methodName, "Creazione della request ed invocazione del servizio");
		AnnullaMutuo request = model.creaRequestAnnullaMutuo();
		AnnullaMutuoResponse response = mutuoService.annullaMutuo(request);
		
		if(response.hasErrori()) {
			log.info(methodName, "Errori nell'invocazione del servizio di annullamento mutuo");
			addErrori(response);
			prepareEnterPage();
			return INPUT;
		}
		
		log.debug(methodName, "Mutuo annullato: imposto il valore TRUE al parametro RIENTRO e torno alla pagina");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
}
