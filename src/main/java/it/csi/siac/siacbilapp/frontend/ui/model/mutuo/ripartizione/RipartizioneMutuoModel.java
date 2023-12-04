/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.ripartizione;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoRipartizioneMutuo;

public class RipartizioneMutuoModel extends BaseMutuoModel {	
	private static final long serialVersionUID = -5441109272144149595L;
	
	private TipoRipartizioneMutuo[] listaTipoRipartizioneMutuo = TipoRipartizioneMutuo.values();
	
	private RipartizioneMutuo ripartizioneMutuo;
	
	public RipartizioneMutuoModel() {
		super();
		setTitolo("Ripartizione capitoli");
	}
	
	public AggiornaRipartizioneMutuo creaRequestAggiornaRipartizioneMutuo() {
		AggiornaRipartizioneMutuo request = creaRequest(AggiornaRipartizioneMutuo.class);
		
		request.setMutuo(getMutuo());
		
		return request;
	}
	
	public AnnullaRipartizioneMutuo creaRequestAnnullaRipartizioneMutuo() {
		AnnullaRipartizioneMutuo request = creaRequest(AnnullaRipartizioneMutuo.class);
		
		request.setMutuo(getMutuo());
		
		return request;
	}

	public TipoRipartizioneMutuo[] getListaTipoRipartizioneMutuo() {
		return listaTipoRipartizioneMutuo;
	}

	public void setListaTipoRipartizioneMutuo(TipoRipartizioneMutuo[] listaTipoRipartizioneMutuo) {
		this.listaTipoRipartizioneMutuo = listaTipoRipartizioneMutuo;
	}

	public RipartizioneMutuo getRipartizioneMutuo() {
		return ripartizioneMutuo;
	}

	public void setRipartizioneMutuo(RipartizioneMutuo ripartizioneMutuo) {
		this.ripartizioneMutuo = ripartizioneMutuo;
	}
	
	
}
