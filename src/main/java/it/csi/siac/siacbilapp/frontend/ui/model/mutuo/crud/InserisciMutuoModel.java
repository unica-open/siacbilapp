/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento.BaseInserisciAggiornaMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;

public class InserisciMutuoModel extends BaseInserisciAggiornaMutuoModel {

	private static final long serialVersionUID = -1519348688672576493L;

	public InserisciMutuoModel() {
		super();
		setTitolo("Inserisci Mutuo");
	}

	public InserisciMutuo creaRequestInserisciMutuo() {

		InserisciMutuo req = creaRequest(InserisciMutuo.class);

		Mutuo mutuo = getMutuo();

		mutuo.setStatoMutuo(StatoMutuo.Bozza);

		req.setMutuo(getMutuo());

		return req;
	}
}
