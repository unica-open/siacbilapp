/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.progetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuo;

public class ProgettiAssociatiMutuoModel extends BaseMutuoModel {	
	private static final long serialVersionUID = -5441109272144149595L;
	
	private List<Integer> elencoIdProgetti = new ArrayList<Integer>();
	
	public ProgettiAssociatiMutuoModel() {
		super();
		setTitolo("Progetti");
	}

	public EliminaAssociazioneProgettiMutuo creaRequestEliminaAssociazioneProgettiMutuo() {
		EliminaAssociazioneProgettiMutuo request = creaRequest(EliminaAssociazioneProgettiMutuo.class);
		
		request.setMutuo(getMutuo());
		request.setElencoIdProgetti(getElencoIdProgetti());
		
		return request;
	}

	public List<Integer> getElencoIdProgetti() {
		return elencoIdProgetti;
	}

	public void setElencoIdProgetti(List<Integer> elencoIdProgetti) {
		this.elencoIdProgetti = elencoIdProgetti;
	}
	
}
