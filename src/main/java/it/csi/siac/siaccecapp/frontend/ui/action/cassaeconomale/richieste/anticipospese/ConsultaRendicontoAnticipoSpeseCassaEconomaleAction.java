/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.ConsultaRendicontoAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la consultazione del rendiconto dell'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaRendicontoAnticipoSpeseCassaEconomaleAction extends GenericBilancioAction<ConsultaRendicontoAnticipoSpeseCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1197723571321922628L;
	
	@Autowired private transient RichiestaEconomaleService richiestaEconomaleService;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// SIAC-5623: Controlla se il caso d'uso sia abilitato
		checkCasoDUsoApplicabile();
		
		RicercaDettaglioRendicontoRichiesta request = model.creaRequestRicercaDettaglioRendicontoRichiesta();
		logServiceRequest(request);
		RicercaDettaglioRendicontoRichiestaResponse response = richiestaEconomaleService.ricercaDettaglioRendicontoRichiesta(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca del rendiconto richiesta con uid " + model.getRendicontoRichiesta().getUid() + " avvenuta con successo");
		impostaRendicontoRichiesta(response.getRendicontoRichiesta());
		
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		
		boolean consentito = AzioniConsentiteFactory.isConsentitoAll(azioniConsentite, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_CONSULTA_RENDICONTO, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
		if(!consentito) {
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non si dispone dei permessi necessari per l'esecuzione").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	/**
	 * Impostazione della richiesta economale.
	 * 
	 * @param rendicontoRichiesta la richiesta da impostare nel model
	 */
	private void impostaRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		model.setRendicontoRichiesta(rendicontoRichiesta);
	}

	/**
	 * Validazione del metodo {@link #execute()}.
	 */
	public void validateExecute() {
		checkNotNullNorInvalidUid(model.getRendicontoRichiesta(), "Rendiconto richiesta");
	}
}
