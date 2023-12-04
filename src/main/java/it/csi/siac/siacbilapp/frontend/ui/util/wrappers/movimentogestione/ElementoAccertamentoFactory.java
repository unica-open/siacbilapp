/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacfinser.model.Accertamento;


public final class ElementoAccertamentoFactory extends ElementoMovimentoGestioneFactory {

	private ElementoAccertamentoFactory() {
		super();
	}

	public static ElementoAccertamento getInstance(Accertamento accertamento) {
		ElementoAccertamento result = ElementoMovimentoGestioneFactory.getInstance(ElementoAccertamento.class, accertamento); 
	
		fillCommonFields(result, accertamento);
		
		result.setTitolo(accertamento.getCapitoloEntrataGestione().getTitoloEntrata().getCodice());

		result.setSub(CoreUtil.getOption(accertamento.getTotaleSubAccertamenti() > 0, "Sì", "No"));
		result.setImportoIncassato(NumberUtil.toImporto(accertamento.getImportoIncassato()));
		
		return result;
	}
	
	public static ElementoAccertamento getInstanceDettaglio(Accertamento accertamento) { 
		ElementoAccertamento result = ElementoMovimentoGestioneFactory.getInstance(ElementoAccertamento.class, accertamento); 
		
		fillDettaglioFields(result, accertamento);
		
		result.setImportoIncassato(NumberUtil.toImporto(accertamento.getImportoIncassato()));
		
		return result;
	}
}
