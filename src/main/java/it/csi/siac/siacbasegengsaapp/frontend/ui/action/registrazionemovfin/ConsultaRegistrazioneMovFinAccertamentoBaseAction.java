/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinAccertamentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinAccertamentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.paginazione.PaginazioneUtil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Consultazione base della registrazione del movimento finanziario per l'accertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinAccertamentoBaseAction<M extends ConsultaRegistrazioneMovFinAccertamentoBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3886539054606146707L;
	
	@Autowired
	private transient MovimentoGestioneService movimentoGestioneService;
	
	@Override
	public String execute() throws Exception {
		String methodName = "execute";
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(req);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaAccertamentoPerChiave");
			addErrori(res);
			return INPUT;
		}
		if(res.isFallimento()) {
			log.info(methodName, "Risultato ottenuto dal servizio RicercaAccertamentoPerChiave: FALLIMENTO");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		Accertamento accertamento = popolaDatiAccertamento(res);
		
		sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, PaginazioneUtil.toListaPaginata(res.getAccertamento().getElencoSubAccertamenti(), res.getNumPagina(),res.getNumeroTotaleSub()));
		
		// SIAC-5294: creo l'helper
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinAccertamentoHelper(accertamento, model.isGestioneUEB()));

		return SUCCESS;
	}
	
	/**
	 * Popolamento dei dati dell'accertamento
	 * @param response la response del servizio
	 * @return l'impegno
	 */
	private Accertamento popolaDatiAccertamento(RicercaAccertamentoPerChiaveOttimizzatoResponse response) {
		Accertamento accertamento = response.getAccertamento();
		accertamento.setElencoSubAccertamenti(defaultingList(accertamento.getElencoSubAccertamenti()));
		if (accertamento.getCapitoloEntrataGestione() == null) {
			// Se il capitolo non e' stato impostato dal servizio, lo imposto io
			accertamento.setCapitoloEntrataGestione(response.getCapitoloEntrataGestione());
		}
		return accertamento;
	}
	
}
