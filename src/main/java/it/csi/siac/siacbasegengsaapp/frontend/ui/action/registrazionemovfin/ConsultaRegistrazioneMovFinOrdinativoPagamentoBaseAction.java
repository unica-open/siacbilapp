/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;

/**
 * Classe base di consultazione per l'ordinativo di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseAction<M extends ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7918565871273015428L;
	
	@Autowired
	private transient OrdinativoService ordinativoService;
	
	@Override
	public String execute() throws Exception {
		
		RicercaOrdinativoPagamentoPerChiave req = model.creaRequestRicercaOrdinativoPagamentoPerChiave();
		logServiceRequest(req);
		RicercaOrdinativoPagamentoPerChiaveResponse res = ordinativoService.ricercaOrdinativoPagamentoPerChiave(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return INPUT;
		}
		OrdinativoPagamento ordinativo = res.getOrdinativoPagamento();
		List<SubOrdinativoPagamento> listaSubOrdinativo = defaultingList(ordinativo.getElencoSubOrdinativiDiPagamento());
		
		model.setOrdinativo(ordinativo);
		model.setListaSubOrdinativo(listaSubOrdinativo);
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper(ordinativo, listaSubOrdinativo, model.isGestioneUEB()));
		return SUCCESS;
	
	}
	
}
