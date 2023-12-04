/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.movimentogestione;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ComponenteBilancioImpegno;

public class RicercaMovimentoGestioneMutuoModel extends BaseMutuoModel {	

	private static final long serialVersionUID = 5386544276802554960L;

	private MovimentoGestione movimentoGestione;
	
	public RicercaMovimentoGestioneMutuoModel() {
		super();
		setTitolo("Ricerca movimenti contabili");
	}

	public RicercaImpegniAssociabiliMutuo creaRequestRicercaImpegniAssociabiliMutuo() {
		RicercaImpegniAssociabiliMutuo request = creaRequest(RicercaImpegniAssociabiliMutuo.class);
		
		getMutuo().setEnte(getEnte());
		
		request.setMovimentoGestione(movimentoGestione);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	public RicercaAccertamentiAssociabiliMutuo creaRequestRicercaAccertamentiAssociabiliMutuo() {
		RicercaAccertamentiAssociabiliMutuo request = creaRequest(RicercaAccertamentiAssociabiliMutuo.class);
		
		getMutuo().setEnte(getEnte());
		
		request.setMovimentoGestione(movimentoGestione);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	public MovimentoGestione getMovimentoGestione() {
		return movimentoGestione;
	}

	public void setMovimentoGestione(MovimentoGestione movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}
}
