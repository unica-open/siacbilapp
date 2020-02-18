/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRendicontoRichiestaHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * Consultazione base della registrazione movfin legata al rendiconto richiesta economale.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 12/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseAction<M extends ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel> extends GenericBilancioAction<M>{


	/** Per la serializzazione */
	private static final long serialVersionUID = -7053031195904419140L;
	@Autowired
	private transient RichiestaEconomaleService richiestaEconomaleService;

	@Override
	public String execute() throws Exception {
		RicercaDettaglioRendicontoRichiesta req = model.creaRequestRicercaDettaglioRendiconcontoRichiestaEconomale();
		logServiceRequest(req);
		RicercaDettaglioRendicontoRichiestaResponse response = richiestaEconomaleService.ricercaDettaglioRendicontoRichiesta(req);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		RendicontoRichiesta rendicontoRichiesta = response.getRendicontoRichiesta();
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinRendicontoRichiestaHelper(rendicontoRichiesta, model.isGestioneUEB()));

		return SUCCESS;
	}

}
