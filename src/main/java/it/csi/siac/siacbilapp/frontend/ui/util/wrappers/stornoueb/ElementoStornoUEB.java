/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.stornoueb;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per gli Storni UEB, utile per la gestione sulla lista paginata degli stessi.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 19/09/2013
 *
 */
public class ElementoStornoUEB implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4160691894707878241L;
	
	private int uid;
	private String numeroStorno;
	private String capitoloSorgente;
	private String capitoloDestinazione;
	private String provvedimento;
	
	/** Costruttore vuoto di default */
	public ElementoStornoUEB() {
		super();
	}
	
	/* Getter e Setter */

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
	 * @return the numeroStorno
	 */
	public String getNumeroStorno() {
		return numeroStorno;
	}

	/**
	 * @param numeroStorno the numeroStorno to set
	 */
	public void setNumeroStorno(String numeroStorno) {
		this.numeroStorno = numeroStorno;
	}

	/**
	 * @return the capitoloSorgente
	 */
	public String getCapitoloSorgente() {
		return capitoloSorgente;
	}

	/**
	 * @param capitoloSorgente the capitoloSorgente to set
	 */
	public void setCapitoloSorgente(String capitoloSorgente) {
		this.capitoloSorgente = capitoloSorgente;
	}

	/**
	 * @return the capitoloDestinazione
	 */
	public String getCapitoloDestinazione() {
		return capitoloDestinazione;
	}

	/**
	 * @param capitoloDestinazione the capitoloDestinazione to set
	 */
	public void setCapitoloDestinazione(String capitoloDestinazione) {
		this.capitoloDestinazione = capitoloDestinazione;
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
	
	/* Override di equals, hashCode, toString */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementoStornoUEB)) {
			return false;
		}
		ElementoStornoUEB other = (ElementoStornoUEB) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(uid, other.uid)
			.append(numeroStorno, other.numeroStorno)
			.append(capitoloSorgente, other.capitoloSorgente)
			.append(capitoloDestinazione, other.capitoloDestinazione)
			.append(provvedimento, other.provvedimento);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(uid)
			.append(numeroStorno)
			.append(capitoloSorgente)
			.append(capitoloDestinazione)
			.append(provvedimento);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("uid", uid)
			.append("numero storno", numeroStorno)
			.append("capitolo sorgente", capitoloSorgente)
			.append("capitolo destinazione", capitoloDestinazione)
			.append("provvedimento", provvedimento);
		return toStringBuilder.toString();
	}
	
}
