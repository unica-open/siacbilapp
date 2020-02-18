/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Classe di wrap per la consultazione dell'accertamento alla prima nota integrata
 * @author Alessandro Marchino
 */
public class ElementoMovimentoConsultazionePrimaNotaIntegrataAccertamento extends ElementoMovimentoConsultazionePrimaNotaIntegrata<Accertamento> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7510208756536271618L;

	/**
	 * Costruttore di wrap
	 * @param accertamento l'accertamento da wrappare
	 */
	public ElementoMovimentoConsultazionePrimaNotaIntegrataAccertamento(Accertamento accertamento) {
		super(accertamento);
	}
	
	@Override
	public String getAnno() {
		return movimento != null ? Integer.toString(movimento.getAnnoMovimento()) : "";
	}
	
	@Override
	public String getNumero() {
		return movimento != null && movimento.getNumero() != null ? movimento.getNumero().toPlainString() : "";
	}
	
	@Override
	public String getDescrizione() {
		return movimento != null ? movimento.getDescrizione() : "";
	}
	
	@Override
	public String getSoggetto() {
		if(movimento == null || movimento.getSoggetto() == null) {
			return "";
		}
		return new StringBuilder()
				.append(movimento.getSoggetto().getCodiceSoggetto())
				.append(" - ")
				.append(movimento.getSoggetto().getDenominazione())
				.toString();
	}
	
	@Override
	public String getPianoDeiConti() {
		if(StringUtils.isBlank(movimento.getCodPdc()) || StringUtils.isBlank(movimento.getDescPdc())) {
			return "";
		}
		return new StringBuilder()
				.append(movimento.getCodPdc())
				.append(" - ")
				.append(movimento.getDescPdc())
				.toString();
	}
	
}
