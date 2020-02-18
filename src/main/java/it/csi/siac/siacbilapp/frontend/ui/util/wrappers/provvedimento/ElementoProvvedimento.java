/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.provvedimento;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per i Provvedimenti, utile per la gestione sulla lista paginata degli stessi.
 * 
 * @author Alessandro Marchino
 *
 */
public class ElementoProvvedimento implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7882717488445303117L;
	
	private int uid;
	private String anno;
	private String numero;
	private String tipo;
	private String oggetto;
	private String strutturaAmministrativoContabile;
	private String stato;
	private int uidTipo;
	private int uidStrutturaAmministrativoContabile;
	//SIAC-4853
	private String codiceTipo;
	
	
	/** Costruttore vuoto di default */
	public ElementoProvvedimento() {
		super();
	}

	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the anno
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(String anno) {
		this.anno = anno;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the oggetto
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto the oggetto to set
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public String getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(
			String strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	/**
	 * @return the uidTipo
	 */
	public int getUidTipo() {
		return uidTipo;
	}

	/**
	 * @param uidTipo the uidTipo to set
	 */
	public void setUidTipo(int uidTipo) {
		this.uidTipo = uidTipo;
	}

	/**
	 * @return the uidStrutturaAmministrativoContabile
	 */
	public int getUidStrutturaAmministrativoContabile() {
		return uidStrutturaAmministrativoContabile;
	}

	/**
	 * @param uidStrutturaAmministrativoContabile the uidStrutturaAmministrativoContabile to set
	 */
	public void setUidStrutturaAmministrativoContabile(int uidStrutturaAmministrativoContabile) {
		this.uidStrutturaAmministrativoContabile = uidStrutturaAmministrativoContabile;
	}

	/**
	 * @return the codice tipo
	 */
	public String getCodiceTipo() {
		return codiceTipo;
	}
	/**
	 * 
	 * @param codiceTipo the codice tipo to set
	 */
	public void setCodiceTipo(String codiceTipo) {
		this.codiceTipo = codiceTipo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementoProvvedimento)) {
			return false;
		}
		ElementoProvvedimento other = (ElementoProvvedimento) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(uid, other.uid)
			.append(anno, other.anno)
			.append(numero, other.numero)
			.append(tipo, other.tipo)
			.append(oggetto, other.oggetto)
			.append(strutturaAmministrativoContabile, other.strutturaAmministrativoContabile)
			.append(stato, other.stato)
			.append(uidTipo, other.uidTipo)
			.append(uidStrutturaAmministrativoContabile, other.uidStrutturaAmministrativoContabile);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(uid)
			.append(anno)
			.append(numero)
			.append(tipo)
			.append(oggetto)
			.append(strutturaAmministrativoContabile)
			.append(stato)
			.append(uidTipo)
			.append(uidStrutturaAmministrativoContabile);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("uid", uid)
			.append("anno", anno)
			.append("numero", numero)
			.append("tipo", tipo)
			.append("oggetto", oggetto)
			.append("struttura amministativo contabile", strutturaAmministrativoContabile)
			.append("stato", stato)
			.append("uid tipo", uidTipo)
			.append("uid strutturaAmministrativoContabile", uidStrutturaAmministrativoContabile);
		return toStringBuilder.toString();
	}
	
	
}
