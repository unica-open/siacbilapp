/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinSubAccertamentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinSubAccertamentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Consultazione base per il subAccertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinSubAccertamentoBaseAction<M extends ConsultaRegistrazioneMovFinSubAccertamentoBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7529261463205242692L;
	
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
		SubAccertamento subAccertamento = findSubAccertamento(accertamento.getElencoSubAccertamenti());
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinSubAccertamentoHelper(accertamento, subAccertamento, model.isGestioneUEB()));
		
		sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, res.getAccertamento());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, CollectionUtil.toListaPaginata(res.getAccertamento().getElencoSubAccertamenti(), res.getNumPagina(),res.getNumeroTotaleSub()));

		return SUCCESS;

	}
	
	/**
	 * Cerca il subaccertamento nella lista dell'accertamento
	 * @param listaSubAccertamento la lista dei sub
	 * @return il subaccertamento
	 */
	private SubAccertamento findSubAccertamento(List<SubAccertamento> listaSubAccertamento) {
		if(listaSubAccertamento == null) {
			return null;
		}
		for(SubAccertamento sa : listaSubAccertamento) {
			// Imposto solo la riga del singolo subimpegno ricercato
			if(sa != null && sa.getNumero() != null && model.getNumeroSub().compareTo(sa.getNumero()) == 0) {
				return sa;
			}
		}
		return null;
	}
	
}
