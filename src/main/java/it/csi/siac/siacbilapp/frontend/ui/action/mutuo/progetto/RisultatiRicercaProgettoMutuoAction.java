/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.progetto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.progetto.RisultatiRicercaProgettoMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneProgettiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneProgettiMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaProgettoMutuoAction extends BaseMutuoAction<RisultatiRicercaProgettoMutuoModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2745593032590721433L;

	protected ListaPaginata<Progetto> ricercaProgetti() throws WebServiceInvocationFailureException {
		RicercaProgettiAssociabiliMutuoResponse res = 
				mutuoService.ricercaProgettiAssociabiliMutuo(sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROGETTI_MUTUO, RicercaProgettiAssociabiliMutuo.class));
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaImpegniAssociabiliMutuo.class, res));
		}

		return res.getProgetti();
	}
	
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";

		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_PROGETTI_MUTUO);
		if(posizioneStart == null) {
			posizioneStart = Integer.valueOf(0);
		}
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
	}


	@Override
	public String enterPage() throws Exception {
		super.enterPage();
		final String methodName = "enterPage";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_PROGETTI_MUTUO);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "StartPosition = " + startPosition);
		
		return SUCCESS;
	}
	
	public void validateProgettiSelezionati() {
		checkCondition(CollectionUtil.isNotEmpty(model.getElencoIdProgetti()), ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore());
	}
	
	public String associaProgettiSelezionati() {
		InserisciAssociazioneProgettiMutuo req = model.creaRequestInserisciAssociazioneProgettiMutuo();
		req.setMutuo(new Mutuo());
		req.getMutuo().setUid(getIdMutuoFromSession());
		
		InserisciAssociazioneProgettiMutuoResponse response = mutuoService.inserisciAssociazioneProgettiMutuo(req);
		
		if(response.hasErrori()) {
			log.info("associaProgettiSelezionati", createErrorInServiceInvocationString(InserisciAssociazioneProgettiMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		try {
			sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROGETTI_MUTUO, ricercaProgetti());
		}
		catch (WebServiceInvocationFailureException e) {
			addErroreDiSistema("associaProgettiSelezionati", e);
			return INPUT;
		}
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		return SUCCESS;
	}

}
