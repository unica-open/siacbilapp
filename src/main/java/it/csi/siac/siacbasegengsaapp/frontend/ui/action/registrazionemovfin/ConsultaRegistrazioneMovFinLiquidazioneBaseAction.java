/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinLiquidazioneBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinLiquidazioneHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;

/**
 * Classe base di consultazione per la registrazione MovFin sulla liquidazione.
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinLiquidazioneBaseAction<M extends ConsultaRegistrazioneMovFinLiquidazioneBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -984074120854030406L;
	
	@Autowired
	private transient LiquidazioneService liquidazioneService;
	
	@Override
	public String execute() throws Exception {
		RicercaLiquidazionePerChiave req = model.creaRequestRicercaLiquidazionePerChiave();
		logServiceRequest(req);
		RicercaLiquidazionePerChiaveResponse res = liquidazioneService.ricercaLiquidazionePerChiave(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return INPUT;
		}
		model.setLiquidazione(res.getLiquidazione());
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinLiquidazioneHelper(res.getLiquidazione(), model.isGestioneUEB()));
		return SUCCESS;
	
	}
	
}
