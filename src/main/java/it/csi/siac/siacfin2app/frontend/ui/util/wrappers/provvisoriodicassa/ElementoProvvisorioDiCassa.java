/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.provvisoriodicassa;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Classe di wrap per il provvisorio di cassa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 11/03/2016
 *
 */
public class ElementoProvvisorioDiCassa implements Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8460444358532385533L;
	
	private final ProvvisorioDiCassa provvisorioDiCassa;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param provvisorioDiCassa il provvisorio da wrappare
	 * @throws IllegalArgumentException nel caso in cui il provvisorio sia <code>null</code>
	 */
	public ElementoProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa) {
		if(provvisorioDiCassa == null) {
			throw new IllegalArgumentException("Il provvisorio di cassa da wrappare deve essere impostato");
		}
		this.provvisorioDiCassa = provvisorioDiCassa;
	}
	
	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}
	
	/**
	 * 
	 * @return uid uid del provvisorio
	 */
	public Integer getUid(){
		return provvisorioDiCassa.getUid();
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return defaultString(provvisorioDiCassa.getDescTipoProvvisorioDiCassa());
	}
	
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return provvisorioDiCassa.getNumero() != null ? provvisorioDiCassa.getNumero().toString() : "";
	}
	
	/**
	 * @return the dataEmissione
	 */
	public String getDataEmissione() {
		return FormatUtils.formatDate(provvisorioDiCassa.getDataEmissione());
	}
	
	/**
	 * @return the dataAnnullamento
	 */
	public String getDataAnnullamento() {
		return FormatUtils.formatDate(provvisorioDiCassa.getDataAnnullamento());
	}
	
	/**
	 * @return the descrizioneVersante
	 */
	public String getDescrizioneVersante() {
		return defaultString(provvisorioDiCassa.getDenominazioneSoggetto());
	}
	
	/**
	 * @return the descrizioneCausale
	 */
	public String getDescrizioneCausale() {
		if(StringUtils.isBlank(provvisorioDiCassa.getCausale())) {
			return "";
		}
		return new StringBuilder()
			.append("<a href=\"#\" data-trigger=\"hover\" rel=\"popover\" title=\"Descrizione sub causale\" data-content=\"")
			.append(provvisorioDiCassa.getSubCausale() != null ? FormatUtils.formatHtmlAttributeString(provvisorioDiCassa.getSubCausale()) : "")
			.append("\">")
			.append(provvisorioDiCassa.getCausale())
			.append("</a>")
			.toString();
	}
	
	/**
	 * @return the importo
	 */
	public String getImporto() {
		return FormatUtils.formatCurrency(provvisorioDiCassa.getImporto());
	}
	
	/**
	 * @return the importoDaRegolarizzare
	 */
	public String getImportoDaRegolarizzare() {
		return FormatUtils.formatCurrency(provvisorioDiCassa.getImportoDaRegolarizzare());
	}
	
	/**
	 * @return the importoDaRegolarizzare
	 */
	public BigDecimal getImportoDaRegolarizzareNumeric() {
		return provvisorioDiCassa.getImportoDaRegolarizzare();
	}
	
	/**
	 * @return the importoDaEmettere
	 */
	public String getImportoDaEmettere() {
		return FormatUtils.formatCurrency(provvisorioDiCassa.getImportoDaEmettere());
	}

	/**
	 * Fornisce un default alla stringa nel caso in cui sia null.
	 * @param str la stringa da defaultare
	 * @return la stringa se valorizzata o il default
	 */
	private String defaultString(String str) {
		return defaultString(str, "");
	}
	
	/**
	 * Fornisce un default alla stringa nel caso in cui sia null.
	 * @param str la stringa da defaultare
	 * @param defaultValue il default da applicare
	 * @return la stringa se valorizzata o il default
	 */
	private String defaultString(String str, String defaultValue) {
		return str != null ? str : defaultValue;
	}
}
