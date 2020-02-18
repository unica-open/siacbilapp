/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

/**
 * Factory per i wrap dei movimenti collegati alla prima nota integrata
 * @author Alessandro Marchino
 */
public final class ElementoMovimentoConsultazionePrimaNotaIntegrataFactory extends BaseFactory {

	/** Non instanziare */
	private ElementoMovimentoConsultazionePrimaNotaIntegrataFactory() {
	}
	
	/**
	 * Ottiene i wrapper
	 * @param entita le entita da wrappare 
	 * @return i wrapper
	 */
	public static List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> getInstances(List<Entita> entita) {
		List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> res = new ArrayList<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>>();
		for(Entita e : entita) {
			ElementoMovimentoConsultazionePrimaNotaIntegrata<?> wrapper = getInstance(e);
			res.add(wrapper);
		}
		return res;
	}
	
	/**
	 * Ottiene il wrapper
	 * @param entita l'entita da wrappare
	 * @return il wrapper
	 */
	public static ElementoMovimentoConsultazionePrimaNotaIntegrata<?> getInstance(Entita entita) {
		if(entita instanceof SubImpegno) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataSubImpegno((SubImpegno)entita);
		}
		if(entita instanceof Impegno) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataImpegno((Impegno)entita);
		}
		if(entita instanceof SubAccertamento) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataSubAccertamento((SubAccertamento)entita);
		}
		if(entita instanceof Accertamento) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataAccertamento((Accertamento)entita);
		}
		
		// SIAC-4467
		if(entita instanceof ModificaMovimentoGestioneSpesa) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataModificaMovimentoGestioneSpesa((ModificaMovimentoGestioneSpesa)entita);
		}
		if(entita instanceof ModificaMovimentoGestioneEntrata) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataModificaMovimentoGestioneEntrata((ModificaMovimentoGestioneEntrata)entita);
		}
		
		if(entita instanceof Liquidazione) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataLiquidazione((Liquidazione)entita);
		}
		if(entita instanceof SubdocumentoSpesa) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataSubdocumentoSpesa((SubdocumentoSpesa)entita);
		}
		if(entita instanceof SubdocumentoEntrata) {
			return new ElementoMovimentoConsultazionePrimaNotaIntegrataSubdocumentoEntrata((SubdocumentoEntrata)entita);
		}
		
		return null;
	}
}
