/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.crud;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud.RisultatiRicercaMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaMutuoAction extends BaseMutuoAction<RisultatiRicercaMutuoModel>{

	private static final long serialVersionUID = -3921425797818215853L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";

		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
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
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "StartPosition = " + startPosition);
		
		return SUCCESS;
	}


	public String aggiorna() {
		saveIdMutuoIntoSession(model.getMutuo().getUid());
		return SUCCESS;
	}


	public String annulla() {		
		mutuoActionHelper.checkFaseBilancio();

		AnnullaMutuo request = model.creaRequestAnnullaMutuo();
		AnnullaMutuoResponse response = mutuoService.annullaMutuo(request);
		
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		
		return SUCCESS;
	}
	
	public void validateAnnulla() {
		mutuoActionHelper.checkFaseBilancio();
	}

	public void validateAggiorna() {
		mutuoActionHelper.checkFaseBilancio();
	}

	public String consulta() {
		saveIdMutuoIntoSession(model.getMutuo().getUid());
		return SUCCESS;
	}

	public void validateEffettuaVariazioneTassoMutuiSelezionati() {
		mutuoActionHelper.checkFaseBilancio();
		checkCondition(CollectionUtil.isNotEmpty(model.getElencoIdMutui()), ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore());
		checkNotNull(model.getTassoInteresseEuribor(), "Euribor", true);
		checkCondition(NumberUtil.isValidAndGreaterThanZero(model.getTassoInteresseEuribor()), ErroreCore.VALORE_NON_CONSENTITO.getErrore("Euribor"));
	}
	
	public String effettuaVariazioneTassoMutuiSelezionati() {
		
		InserisciVariazioneMassivaTassoMutuo req = model.creaRequestVariazioneMassivaTassoMutuo();
		InserisciVariazioneMassivaTassoMutuoResponse response = mutuoService.inserisciVariazioneMassivaTassoMutuo(req);
		
		if(response.hasErrori()) {
			log.info("effettuaVariazioneTassoMutuiSelezionati", createErrorInServiceInvocationString(InserisciVariazioneMassivaTassoMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MUTUO, 
				mutuoService.ricercaSinteticaMutuo(sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MUTUO, RicercaSinteticaMutuo.class)).getMutui());
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		
		return SUCCESS;
	}

	public boolean isAbilitaVariazioneTasso() {
		return isAzioneConsentita(AzioneConsentitaEnum.OP_MUT_gestisciMutuo) && mutuoActionHelper.isValidFaseBilancio();
	}
	
	
	public boolean isValidFaseBilancio() {
		return mutuoActionHelper.isValidFaseBilancio();
	}

}
