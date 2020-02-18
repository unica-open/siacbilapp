/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoIncassoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoIncassoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;

/**
 * Classe base di consultazione per l'ordinativo di incasso.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinOrdinativoIncassoBaseAction<M extends ConsultaRegistrazioneMovFinOrdinativoIncassoBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8307069688239857127L;
	
	@Autowired
	private transient OrdinativoService ordinativoService;
	
	@Override
	public String execute() throws Exception {
		
		RicercaOrdinativoIncassoPerChiave req = model.creaRequestRicercaOrdinativoIncassoPerChiave();
		logServiceRequest(req);
		RicercaOrdinativoIncassoPerChiaveResponse res = ordinativoService.ricercaOrdinativoIncassoPerChiave(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return INPUT;
		}
		OrdinativoIncasso ordinativo = res.getOrdinativoIncasso();
		List<SubOrdinativoIncasso> listaSubOrdinativo = defaultingList(ordinativo.getElencoSubOrdinativiDiIncasso());
		
		model.setOrdinativo(ordinativo);
		model.setListaSubOrdinativo(listaSubOrdinativo);
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinOrdinativoIncassoHelper(ordinativo, listaSubOrdinativo, model.isGestioneUEB()));
		return SUCCESS;
	
	}
	
}
