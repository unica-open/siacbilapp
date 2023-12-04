/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento.BaseInserisciAggiornaMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuo;

public class AggiornaMutuoModel extends BaseInserisciAggiornaMutuoModel {



	private static final long serialVersionUID = -7315689113187835663L;

	public AggiornaMutuoModel() {
		super();
		setTitolo("Aggiorna Mutuo");
	}

	public AggiornaMutuo creaRequestAggiornaMutuo() {

		AggiornaMutuo req = creaRequest(AggiornaMutuo.class);
		
		req.setMutuo(getMutuo());
		
		return req;
	}		
	
	public AnnullaMutuo creaRequestAnnullaMutuo() {
		AnnullaMutuo request = creaRequest(AnnullaMutuo.class);

		request.setMutuo(getMutuo());
		
		return request;
	}
}
