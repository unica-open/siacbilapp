/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.movimentogestione;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.movimentogestione.RisultatiRicercaMovimentoGestioneMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class RisultatiRicercaMovimentoGestioneMutuoAction<RRMGMM extends RisultatiRicercaMovimentoGestioneMutuoModel> extends BaseMutuoAction<RRMGMM>{

	private static final long serialVersionUID = 7048990952091642344L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";

		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_MOVIMENTI_GESTIONE_MUTUO);
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
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_MOVIMENTI_GESTIONE_MUTUO);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "StartPosition = " + startPosition);
		
		return SUCCESS;
	}

	
	public void validateAssociaMovimentiGestioneSelezionati() {
		checkCondition(CollectionUtil.isNotEmpty(model.getElencoIdMovimentiGestione()), ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore());
	}
	
	public String associaMovimentiGestioneSelezionati() {
		InserisciAssociazioneMovimentiGestioneMutuo req = model.creaRequestInserisciAssociazioneMovimentiGestioneMutuo();
		req.setMutuo(new Mutuo());
		req.getMutuo().setUid(getIdMutuoFromSession());
		
		InserisciAssociazioneMovimentiGestioneMutuoResponse response = mutuoService.inserisciAssociazioneMovimentiGestioneMutuo(req);
		
		if(response.hasErrori()) {
			log.info("associaMovimentiGestioneSelezionati", createErrorInServiceInvocationString(InserisciAssociazioneMovimentiGestioneMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		try {
			sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_GESTIONE_MUTUO, ricercaMovimentiGestione());
		}
		catch (WebServiceInvocationFailureException e) {
			addErroreDiSistema("associaMovimentiGestioneSelezionati", e);
			return INPUT;
		}
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		
		return SUCCESS;
	}


	protected abstract <MG extends MovimentoGestione> ListaPaginata<MG> ricercaMovimentiGestione() throws WebServiceInvocationFailureException;
	

}
