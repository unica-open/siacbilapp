/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Classe di wrap per la Causale.

 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 *
 */
public class ElementoCausale implements Serializable, ModelWrapper, Comparable<ElementoCausale> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	
	private Integer uid;
	private String causaleCode;
	private String causaleDesc;
	private String tipoCausale;
	private String statoOperativoCausaleCode;
	private String statoOperativoCausaleDesc;
	private String dataDecorrenza;
	private String strutturaAmministrativa;
	private String capitolo;
	private String movimentoGestione;
	private String soggetto;
	private String sediSecondarie;
	private String modalitaPagamentoSoggetto;
	private String provvedimento;
	
	private String azioni;
	
	/** Costruttore vuoto di default */
	public ElementoCausale() {
		super();
	}

	/**
	 * @return the uid
	 */
	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * @return the causaleCode
	 */
	public String getCausaleCode() {
		return causaleCode;
	}

	/**
	 * @param causaleCode the causaleCode to set
	 */
	public void setCausaleCode(String causaleCode) {
		this.causaleCode = causaleCode;
	}

	/**
	 * @return the causaleDesc
	 */
	public String getCausaleDesc() {
		return causaleDesc;
	}

	/**
	 * @param causaleDesc the causaleDesc to set
	 */
	public void setCausaleDesc(String causaleDesc) {
		this.causaleDesc = causaleDesc;
	}

	/**
	 * @return the tipoCausale
	 */
	public String getTipoCausale() {
		return tipoCausale;
	}

	/**
	 * @param tipoCausale the tipoCausale to set
	 */
	public void setTipoCausale(String tipoCausale) {
		this.tipoCausale = tipoCausale;
	}

	/**
	 * @return the statoOperativoCausaleCode
	 */
	public String getStatoOperativoCausaleCode() {
		return statoOperativoCausaleCode;
	}

	/**
	 * @param statoOperativoCausaleCode the statoOperativoCausaleCode to set
	 */
	public void setStatoOperativoCausaleCode(String statoOperativoCausaleCode) {
		this.statoOperativoCausaleCode = statoOperativoCausaleCode;
	}

	/**
	 * @return the statoOperativoCausaleDesc
	 */
	public String getStatoOperativoCausaleDesc() {
		return statoOperativoCausaleDesc;
	}

	/**
	 * @param statoOperativoCausaleDesc the statoOperativoCausaleDesc to set
	 */
	public void setStatoOperativoCausaleDesc(String statoOperativoCausaleDesc) {
		this.statoOperativoCausaleDesc = statoOperativoCausaleDesc;
	}

	/**
	 * @return the dataDecorrenza
	 */
	public String getDataDecorrenza() {
		return dataDecorrenza;
	}

	/**
	 * @param dataDecorrenza the dataDecorrenza to set
	 */
	public void setDataDecorrenza(String dataDecorrenza) {
		this.dataDecorrenza = dataDecorrenza;
	}

	/**
	 * @return the strutturaAmministrativa
	 */
	public String getStrutturaAmministrativa() {
		return strutturaAmministrativa;
	}

	/**
	 * @param strutturaAmministrativa the strutturaAmministrativa to set
	 */
	public void setStrutturaAmministrativa(String strutturaAmministrativa) {
		this.strutturaAmministrativa = strutturaAmministrativa;
	}

	/**
	 * @return the capitolo
	 */
	public String getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the movimentoGestione
	 */
	public String getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(String movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the soggetto
	 */
	public String getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the sediSecondarie
	 */
	public String getSediSecondarie() {
		return sediSecondarie;
	}

	/**
	 * @param sediSecondarie the sediSecondarie to set
	 */
	public void setSediSecondarie(String sediSecondarie) {
		this.sediSecondarie = sediSecondarie;
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public String getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(String modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the provvedimento
	 */
	public String getProvvedimento() {
		return provvedimento;
	}

	/**
	 * @param provvedimento the provvedimento to set
	 */
	public void setProvvedimento(String provvedimento) {
		this.provvedimento = provvedimento;
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
	 * Controlla se il documento &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del documento &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoValido() {
		return StatoOperativoCausale.VALIDA.getCodice().equalsIgnoreCase(statoOperativoCausaleCode);
	}
	
	/**
	 * Controlla se il documento &eacute; in stato annullato.
	 * 
	 * @return <code>true</code> se lo stato operativo del documento &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoAnnullato() {
		return StatoOperativoCausale.ANNULLATA.getCodice().equalsIgnoreCase(statoOperativoCausaleCode);
	}

	@Override
	public int compareTo(ElementoCausale o) {
		return new CompareToBuilder().append(this.uid, o.uid).toComparison();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof ElementoCausale)) {
			return false;
		}
		ElementoCausale other = (ElementoCausale)obj;
		return new EqualsBuilder().append(this.uid, other.uid).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(uid).toHashCode();
	}
	
}
