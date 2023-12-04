/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.movimentogestione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneMovimentiGestioneMutuo;

public class MovimentiGestioneAssociatiMutuoModel extends BaseMutuoModel {	
	private static final long serialVersionUID = -5441109272144149595L;
	
	private List<Integer> elencoIdMovimentiGestione = new ArrayList<Integer>();
	
	public MovimentiGestioneAssociatiMutuoModel() {
		super();
		setTitolo("Movimenti contabili");
	}

	public EliminaAssociazioneMovimentiGestioneMutuo creaRequestEliminaAssociazioneMovimentiGestioneMutuo() {
		EliminaAssociazioneMovimentiGestioneMutuo request = creaRequest(EliminaAssociazioneMovimentiGestioneMutuo.class);
		
		request.setMutuo(getMutuo());
		request.setElencoIdMovimentiGestione(getElencoIdMovimentiGestione());
		
		return request;
	}

	public List<Integer> getElencoIdMovimentiGestione() {
		return elencoIdMovimentiGestione;
	}

	public void setElencoIdMovimentiGestione(List<Integer> elencoIdMovimentiGestione) {
		this.elencoIdMovimentiGestione = elencoIdMovimentiGestione;
	}
}
