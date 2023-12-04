/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di wrap per la consultazione dell'impegno alla prima nota integrata
 * @author Alessandro Marchino
 */
public class ElementoMovimentoConsultazionePrimaNotaIntegrataSubImpegno extends ElementoMovimentoConsultazionePrimaNotaIntegrata<SubImpegno> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4981977017208427152L;

	/**
	 * Costruttore di wrap
	 * @param subImpegno il subimpegno da wrappare
	 */
	public ElementoMovimentoConsultazionePrimaNotaIntegrataSubImpegno(SubImpegno subImpegno) {
		super(subImpegno);
	}
	
	@Override
	public String getAnno() {
		return movimento != null ? Integer.toString(movimento.getAnnoMovimento()) : "";
	}
	
	@Override
	public String getNumero() {
		BigDecimal numeroPadre = movimento.getNumeroImpegnoPadre();
		BigDecimal numero = movimento.getNumeroBigDecimal();
		List<String> components = new ArrayList<String>();
		
		if(numeroPadre != null) {
			components.add(numeroPadre.toPlainString());
		}
		if(numero != null) {
			components.add(numero.toPlainString());
		}
		return StringUtils.join(components, " - ");
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
