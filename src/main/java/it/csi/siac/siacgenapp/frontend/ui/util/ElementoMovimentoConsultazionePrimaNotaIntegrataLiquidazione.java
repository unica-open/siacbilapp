/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * Classe di wrap per la consultazione della liquidazione alla prima nota integrata
 * @author Alessandro Marchino
 */
public class ElementoMovimentoConsultazionePrimaNotaIntegrataLiquidazione extends ElementoMovimentoConsultazionePrimaNotaIntegrata<Liquidazione> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7510208756536271618L;

	/**
	 * Costruttore di wrap
	 * @param liquidazione la liquidazione da wrappare
	 */
	public ElementoMovimentoConsultazionePrimaNotaIntegrataLiquidazione(Liquidazione liquidazione) {
		super(liquidazione);
	}
	
	@Override
	public String getAnno() {
		return movimento != null && movimento.getAnnoLiquidazione() != null ? movimento.getAnnoLiquidazione().toString() : "";
	}
	
	@Override
	public String getNumero() {
		return movimento != null && movimento.getNumeroLiquidazione() != null ? movimento.getNumeroLiquidazione().toPlainString() : "";
	}
	
	@Override
	public String getDescrizione() {
		return movimento != null ? movimento.getDescrizioneLiquidazione() : "";
	}
	
	@Override
	public String getImporto() {
		return movimento != null ? FormatUtils.formatCurrency(movimento.getImportoLiquidazione()) : "";
	}
	
	@Override
	public String getSoggetto() {
		if(movimento == null || movimento.getSoggettoLiquidazione() == null) {
			return "";
		}
		return new StringBuilder()
				.append(movimento.getSoggettoLiquidazione().getCodiceSoggetto())
				.append(" - ")
				.append(movimento.getSoggettoLiquidazione().getDenominazione())
				.toString();
	}
	
	@Override
	public String getMovimentoGestione() {
		if(movimento == null || movimento.getImpegno() == null) {
			return "";
		}
		return new StringBuilder()
				.append(movimento.getImpegno().getAnnoMovimento())
				.append(" - ")
				.append(movimento.getImpegno().getNumero().toPlainString())
				.toString();
	}
	
	@Override
	public String getPianoDeiConti() {
		if(movimento == null || (StringUtils.isBlank(movimento.getCodPdc()) || StringUtils.isBlank(movimento.getDescPdc()))){
			return "";
		}
		return new StringBuilder()
				.append(movimento.getCodPdc())
				.append(" - ")
				.append(movimento.getDescPdc())
				.toString();
	}
	
}
