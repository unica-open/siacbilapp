/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRateoRiscontoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRateoRiscontoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RateoRisconto;

/**
 * Consultazione base della registrazione movfin legata a ratei o risocnti.
 * 
  * @author Valentina
 * @version 1.0.0 - 25/07/2016
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinRateoRiscontoBaseAction<M extends ConsultaRegistrazioneMovFinRateoRiscontoBaseModel> extends GenericBilancioAction<M>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 8309546579399417290L;
	
	@Autowired
	private transient PrimaNotaService primaNotaService;

	@Override
	public String execute() throws Exception {
		RicercaDettaglioPrimaNota req = model.creaRequestRicercaDettaglioPrimaNota();
		logServiceRequest(req);
		RicercaDettaglioPrimaNotaResponse response = primaNotaService.ricercaDettaglioPrimaNota(req);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		PrimaNota primaNota = response.getPrimaNota();
		RateoRisconto rateoRisconto = retrieveRateoORisconto(primaNota);
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinRateoRiscontoHelper(rateoRisconto, primaNota, model.isGestioneUEB()));
		return SUCCESS;
	}

	/**
	 * Impostazione del rateo o del risconto
	 * @param primaNota la prima nota
	 * @return il rateo o il risconto
	 */
	protected abstract RateoRisconto retrieveRateoORisconto(PrimaNota primaNota);

}
