/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.progetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuo;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoAmbito;

public class RicercaProgettoMutuoModel extends BaseMutuoModel {	

	private static final long serialVersionUID = 5386544276802554960L;

	private Progetto progetto;
	
	protected List<TipoAmbito> listaTipoAmbito = new ArrayList<TipoAmbito>();
	
	public RicercaProgettoMutuoModel() {
		super();
		setTitolo("Ricerca progetti");
	}

	public RicercaProgettiAssociabiliMutuo creaRequestRicercaProgettiAssociabiliMutuo() {
		RicercaProgettiAssociabiliMutuo request = creaRequest(RicercaProgettiAssociabiliMutuo.class);
		
		getMutuo().setEnte(getEnte());
		
		request.setProgetto(progetto);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	public Progetto getProgetto() {
		return progetto;
	}

	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

	public List<TipoAmbito> getListaTipoAmbito() {
		return listaTipoAmbito;
	}

	public void setListaTipoAmbito(List<TipoAmbito> listaTipoAmbito) {
		this.listaTipoAmbito = listaTipoAmbito;
	}
	

}
