/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRichiestaEconomaleHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Consultazione base della registrazione movfin legata alla richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinRichiestaEconomaleBaseAction<M extends ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel> extends GenericBilancioAction<M>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 8309546579399417290L;
	
	@Autowired
	private transient RichiestaEconomaleService richiestaEconomaleService;

	@Override
	public String execute() throws Exception {
		RicercaDettaglioRichiestaEconomale req = model.creaRequestRicercaDettaglioRichiestaEconomale();
		logServiceRequest(req);
		RicercaDettaglioRichiestaEconomaleResponse response = richiestaEconomaleService.ricercaDettaglioRichiestaEconomale(req);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}

		RichiestaEconomale richiestaEconomale = response.getRichiestaEconomale();
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinRichiestaEconomaleHelper(richiestaEconomale, model.isGestioneUEB()));

		return SUCCESS;
	}

}
