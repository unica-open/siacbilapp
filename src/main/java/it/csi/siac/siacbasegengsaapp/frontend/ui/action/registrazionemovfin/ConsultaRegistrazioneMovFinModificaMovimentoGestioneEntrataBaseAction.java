/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.paginazione.PaginazioneUtil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

/**
 * Consultazione base per la modifica del movimento di gestione di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataBaseAction<M extends ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 975563045921409841L;
	
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
		
		Accertamento accertamento = res.getAccertamento();
		SubAccertamento subAccertamento = findSubAccertamento(res.getAccertamento());
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = findModificaMovimentoGestioneEntrata(subAccertamento != null ? subAccertamento : accertamento);
		
		// SIAC-5294
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper(accertamento, subAccertamento, modificaMovimentoGestioneEntrata, model.isGestioneUEB()));
		
		sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, PaginazioneUtil.toListaPaginata(res.getAccertamento().getElencoSubAccertamenti(), res.getNumPagina(),res.getNumeroTotaleSub()));

		return SUCCESS;
		
	}
	
	/**
	 * Trova il subaccertamento.
	 * @param accertamento l'accertamento
	 * @return il sub
	 */
	private SubAccertamento findSubAccertamento(Accertamento accertamento) {
		final String methodName = "findSubAccertamento";
		if(accertamento == null || accertamento.getElencoSubAccertamenti() == null) {
			return null;
		}
		BigDecimal numeroSub = model.getNumeroSub();
		if(numeroSub == null) {
			log.debug(methodName, "Modifica non relativa al subaccertamento: non tiro su il dato");
			return null;
		}
		for(SubAccertamento si : accertamento.getElencoSubAccertamenti()) {
			if(si != null && si.getNumeroBigDecimal() != null && si.getNumeroBigDecimal().compareTo(numeroSub) == 0) {
				return si;
			}
		}
		return null;
	}
	
	/**
	 * Trova la modifica dall'accertamento restituito dal servizio di ricerca.
	 * 
	 * @param accertamento l'accertamento del servizio di ricerca
	 * @return la modifica
	 */
	private ModificaMovimentoGestioneEntrata findModificaMovimentoGestioneEntrata(Accertamento accertamento) {
		if(accertamento == null || accertamento.getListaModificheMovimentoGestioneEntrata() == null) {
			return null;
		}
		int uid = model.getUid().intValue();
		for(ModificaMovimentoGestioneEntrata mmgs : accertamento.getListaModificheMovimentoGestioneEntrata()) {
			if(mmgs != null && mmgs.getUid() == uid) {
				return mmgs;
			}
		}
		return null;
	}

}
