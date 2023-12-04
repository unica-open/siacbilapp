/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuo;

public class RicercaMutuoModel extends BaseMutuoModel {

	private static final long serialVersionUID = 6272373819945865929L;

	public RicercaMutuoModel() {
		super();
		setTitolo("Ricerca Mutuo");
	}
	

	public RicercaSinteticaMutuo creaRequestRicercaSinteticaMutuo() {
		RicercaSinteticaMutuo request = creaRequest(RicercaSinteticaMutuo.class);
		
		getMutuo().setEnte(getEnte());
		
		request.setMutuo(getMutuo());		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	
}
