/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di wrap per la consultazione dell'impegno alla prima nota integrata
 * @author Alessandro Marchino
 */
public class ElementoMovimentoConsultazionePrimaNotaIntegrataModificaMovimentoGestioneEntrata extends ElementoMovimentoConsultazionePrimaNotaIntegrata<ModificaMovimentoGestioneEntrata> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4981977017208427152L;

	/**
	 * Costruttore di wrap
	 * @param modificaEntrata la modificaEntrata da wrappare
	 */
	public ElementoMovimentoConsultazionePrimaNotaIntegrataModificaMovimentoGestioneEntrata(ModificaMovimentoGestioneEntrata modificaEntrata) {
		super(modificaEntrata);
	}
	
	@Override
	public String getAnno() {
		MovimentoGestione mg = extractMovimentoGestione();
		return mg == null
			? ""
			: mg instanceof SubAccertamento
				? Integer.toString(((SubAccertamento)mg).getAnnoAccertamentoPadre())
				: Integer.toString(mg.getAnnoMovimento());
	}
	
	@Override
	public String getNumero() {
		MovimentoGestione mg = extractMovimentoGestione();
		return mg != null && mg.getNumeroBigDecimal() != null ? mg.getNumeroBigDecimal().toPlainString() : "";
	}
	
	@Override
	public String getDescrizione() {
		return movimento != null ? movimento.getDescrizioneModificaMovimentoGestione() : "";
	}
	
	@Override
	public String getSoggetto() {
		MovimentoGestione mg = extractMovimentoGestione();
		Soggetto soggetto = movimento != null && movimento.getSoggetto() != null
			? movimento.getSoggetto()
			: mg != null && mg.getSoggetto() != null
				? mg.getSoggetto()
				: null;
		
		if(soggetto == null) {
			return "";
		}
		return new StringBuilder()
				.append(soggetto.getCodiceSoggetto())
				.append(" - ")
				.append(soggetto.getDenominazione())
				.toString();
	}
	
	@Override
	public String getPianoDeiConti() {
		MovimentoGestione mg = extractMovimentoGestione();
		if(mg == null || (mg.getCodPdc() == null && mg.getCodicePdc() ==null)){
			return "";
		}
		return new StringBuilder()
				.append(mg.getCodPdc())
				.append(" - ")
				.append(mg.getDescPdc())
				.toString();
	}
	
	/**
	 * Estrae il movimento di gestione collegato
	 * @return il movimento di gestione
	 */
	private MovimentoGestione extractMovimentoGestione() {
		return movimento == null
			? null
			: movimento.getSubAccertamento() != null
				? movimento.getSubAccertamento()
				: movimento.getAccertamento();
	}
}
