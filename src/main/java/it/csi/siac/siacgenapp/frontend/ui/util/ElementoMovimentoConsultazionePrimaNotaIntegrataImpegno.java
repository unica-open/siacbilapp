/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe di wrap per la consultazione dell'impegno alla prima nota integrata
 * @author Alessandro Marchino
 */
public class ElementoMovimentoConsultazionePrimaNotaIntegrataImpegno extends ElementoMovimentoConsultazionePrimaNotaIntegrata<Impegno> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7510208756536271618L;

	/**
	 * Costruttore di wrap
	 * @param impegno l'impegno da wrappare
	 */
	public ElementoMovimentoConsultazionePrimaNotaIntegrataImpegno(Impegno impegno) {
		super(impegno);
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
		if(movimento == null || (movimento.getCodPdc() == null && movimento.getCodicePdc() ==null)){
			return "";
		}
		return new StringBuilder()
				.append(movimento.getCodPdc())
				.append(" - ")
				.append(movimento.getDescPdc())
				.toString();
	}
	
}
