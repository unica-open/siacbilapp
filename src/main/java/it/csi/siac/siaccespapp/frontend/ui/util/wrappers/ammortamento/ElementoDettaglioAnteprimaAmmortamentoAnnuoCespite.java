/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.ammortamento;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;

/**
 * The Class ElementoCespite.
 * @author elisa
 * @version 1.0.0 - 12-06-2018
 */
public class ElementoDettaglioAnteprimaAmmortamentoAnnuoCespite implements Serializable, ModelWrapper {

	/**
	 * PER LA SERIALIZZAZIONE
	 */
	private static final long serialVersionUID = 3141152990024668954L;

	//FIELDS
	private final DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAnteprimaAmmortamentoAnnuoCespite;
	
	private static final String DEFAULT_NULL_STRING = "";
	
	
	/**
	 * Instantiates a new elemento categoria cespiti.
	 *
	 * @param dettaglioAmmortamentoAnnuoCespite the dettaglio ammortamento annuo cespite
	 */
	public ElementoDettaglioAnteprimaAmmortamentoAnnuoCespite(DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite) {
		this.dettaglioAnteprimaAmmortamentoAnnuoCespite = dettaglioAmmortamentoAnnuoCespite;
	}



	@Override
	public int getUid() {
		if(dettaglioAnteprimaAmmortamentoAnnuoCespite == null) {
			return 0;
		}
		return dettaglioAnteprimaAmmortamentoAnnuoCespite.getUid();
	}
	
	/**
	 * Gets the conto.
	 *
	 * @return the conto
	 */
	public String getConto() {
		
		return new StringBuilder().append(getCodiceConto())
				.append(" - ")
				.append(getDescrizioneConto())
				.toString();
	}
	
	/**
	 * Gets the codice conto.
	 *
	 * @return the codice conto
	 */
	public String getCodiceConto() {
		if(dettaglioAnteprimaAmmortamentoAnnuoCespite == null || dettaglioAnteprimaAmmortamentoAnnuoCespite.getConto() == null) {
			return "";
		}
		return new StringBuilder(StringUtils.defaultString(dettaglioAnteprimaAmmortamentoAnnuoCespite.getConto().getCodice(), DEFAULT_NULL_STRING)).toString();
	}
	
	/**
	 * Gets the codice conto.
	 *
	 * @return the codice conto
	 */
	public String getDescrizioneConto() {
		if(dettaglioAnteprimaAmmortamentoAnnuoCespite == null || dettaglioAnteprimaAmmortamentoAnnuoCespite.getConto() == null) {
			return "";
		}
		return new StringBuilder(StringUtils.defaultString(dettaglioAnteprimaAmmortamentoAnnuoCespite.getConto().getDescrizione(), DEFAULT_NULL_STRING)).toString();
	}
	
	/**
	 * Gets the importo dare.
	 *
	 * @return the importo dare
	 */
	public String getImportoDare() {
		if(dettaglioAnteprimaAmmortamentoAnnuoCespite == null || !OperazioneSegnoConto.DARE.equals(dettaglioAnteprimaAmmortamentoAnnuoCespite.getSegno())) {
			return "";
		}
		return FormatUtils.formatCurrency(dettaglioAnteprimaAmmortamentoAnnuoCespite.getImporto());
	}
	
	/**
	 * Gets the importo avere.
	 *
	 * @return the importo avere
	 */
	public String getImportoAvere() {
		if(dettaglioAnteprimaAmmortamentoAnnuoCespite == null || !OperazioneSegnoConto.AVERE.equals(dettaglioAnteprimaAmmortamentoAnnuoCespite.getSegno())) {
			return "";
		}
		return FormatUtils.formatCurrency(dettaglioAnteprimaAmmortamentoAnnuoCespite.getImporto());
	}
	
	/**
	 * Gets the numero cespiti.
	 *
	 * @return the numero cespiti
	 */
	public String getNumeroCespiti() {
		if(dettaglioAnteprimaAmmortamentoAnnuoCespite == null || dettaglioAnteprimaAmmortamentoAnnuoCespite.getNumeroCespiti() == null || !OperazioneSegnoConto.DARE.equals(dettaglioAnteprimaAmmortamentoAnnuoCespite.getSegno())) {
			return "";
		}
		return  dettaglioAnteprimaAmmortamentoAnnuoCespite.getNumeroCespiti().toString();
	}
	
	
	/**
	 * Gets the segno.
	 *
	 * @return the segno
	 */
	public OperazioneSegnoConto getSegno() {
		if( dettaglioAnteprimaAmmortamentoAnnuoCespite == null) {
			return null;
		}
		return dettaglioAnteprimaAmmortamentoAnnuoCespite.getSegno();
	}
	
}
