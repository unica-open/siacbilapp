/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoVariazioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.VariazioneMutuo;

public abstract class BaseVariazioneMutuoModel extends BaseMutuoModel {

	private static final long serialVersionUID = -6959390014601563283L;

	private VariazioneMutuo variazioneMutuo;
	
	private List<Integer> listaNumeriRataAnno = new ArrayList<Integer>();
	
	public BaseVariazioneMutuoModel() {
		super();
		setTitolo(getTipoVariazioneMutuo().getDescrizione() + " Mutuo");
	}
	
	public VariazioneMutuo getVariazioneMutuo() {
		return variazioneMutuo;
	}

	public void setVariazioneMutuo(VariazioneMutuo variazioneMutuo) {
		this.variazioneMutuo = variazioneMutuo;
	}

	public InserisciVariazioneMutuo creaRequestInserisciVariazioneMutuo() {
		InserisciVariazioneMutuo req = creaRequest(InserisciVariazioneMutuo.class);
		
		getVariazioneMutuo().setTipoVariazioneMutuo(getTipoVariazioneMutuo());
		getVariazioneMutuo().setMutuo(getMutuo());

		req.setVariazioneMutuo(getVariazioneMutuo());
		
		return req;
	}

	protected abstract TipoVariazioneMutuo getTipoVariazioneMutuo();
	
	public void initListaNumeriRataAnno() {
		for (int i = 1; i <= MutuoUtil.calcNumeroRateAnnue(getMutuo().getPeriodoRimborso()); i++) {
			listaNumeriRataAnno.add(i);
		}
	}

	public List<Integer> getListaNumeriRataAnno() {
		return listaNumeriRataAnno;
	}

	public void setListaNumeriRataAnno(List<Integer> listaNumeriRataAnno) {
		this.listaNumeriRataAnno = listaNumeriRataAnno;
	}
}
