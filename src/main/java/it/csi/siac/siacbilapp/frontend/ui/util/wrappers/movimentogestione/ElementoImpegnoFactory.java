/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacfinser.model.Impegno;


public final class ElementoImpegnoFactory extends ElementoMovimentoGestioneFactory {

	private ElementoImpegnoFactory() {
		super();
	}


	public static ElementoImpegno getInstance(Impegno impegno) {
		ElementoImpegno result = ElementoMovimentoGestioneFactory.getInstance(ElementoImpegno.class, impegno); 
	
		fillCommonFields(result, impegno);
		
		result.setMissione(impegno.getCapitoloUscitaGestione().getMissione().getCodice());
		result.setProgramma(impegno.getCapitoloUscitaGestione().getProgramma().getCodice());
		
		result.setCig(impegno.getCig());
		result.setCup(impegno.getCup());
		result.setSub(CoreUtil.getOption(impegno.getTotaleSubImpegni() > 0, "Sì", "No"));
		result.setImportoLiquidato(NumberUtil.toImporto(impegno.getImportoLiquidato()));
		
		return result;
	}
	
	public static ElementoImpegno getInstanceDettaglio(Impegno impegno) { 
		ElementoImpegno result = ElementoMovimentoGestioneFactory.getInstance(ElementoImpegno.class, impegno); 
		
		fillDettaglioFields(result, impegno);
		
		result.setImportoLiquidato(NumberUtil.toImporto(impegno.getImportoLiquidato()));
		
		return result;
	}

//
//
//	public static List<ElementoImpegno> getInstances(List<Impegno> impegni) {
//		List<ElementoImpegno> result = new ArrayList<ElementoImpegno>();
//
//		for (Impegno impegno : impegni) {
//			result.add(getInstance(impegno));
//		}
//		
//		return result;
//	}

}
