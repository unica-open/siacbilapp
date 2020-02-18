/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.tipoonere;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Wrapper per il tipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
public class ElementoTipoOnere implements Serializable, ModelWrapper, Comparable<ElementoTipoOnere> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8933173312543571497L;
	
	private TipoOnere tipoOnere;
	private String azioni;
	
	/** Costuttore vuoto di default. */
	public ElementoTipoOnere() {
		this(null);
	}
	
	/**
	 * Costruttore a partire dall'elemento wrappato.
	 * 
	 * @param tipoOnere the tipoOnere to set
	 */
	public ElementoTipoOnere(TipoOnere tipoOnere) {
		this.tipoOnere = tipoOnere;
	}
	
	/**
	 * @return the tipoOnere
	 */
	public TipoOnere getTipoOnere() {
		return tipoOnere;
	}

	/**
	 * @param tipoOnere the tipoOnere to set
	 */
	public void setTipoOnere(TipoOnere tipoOnere) {
		this.tipoOnere = tipoOnere;
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
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		if(this.tipoOnere != null) {
			hcb.append(this.tipoOnere.getCodice())
				.append(this.tipoOnere.getDescrizione());
		}
		return hcb.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof ElementoTipoOnere)) {
			return false;
		}
		ElementoTipoOnere other = (ElementoTipoOnere)obj;
		if(this.tipoOnere == other.tipoOnere) {
			return true;
		}
		if(this.tipoOnere == null || other.tipoOnere == null) {
			return false;
		}
		EqualsBuilder eb = new EqualsBuilder()
			.append(this.tipoOnere.getCodice(), other.tipoOnere.getCodice())
			.append(this.tipoOnere.getDescrizione(), other.tipoOnere.getDescrizione());
		
		return eb.isEquals();
	}

	@Override
	public int compareTo(ElementoTipoOnere o) {
		// Null checks
		if(this == o) {
			return 0;
		}
		if(o == null) {
			return -1;
		}
		if(this.tipoOnere == o.tipoOnere) {
			return 0;
		}
		// Inutile controllare che this.tipoOnere sia null: cadremmo nel caso precedente
		// Cfr. The Java Language Specification, par. 4.1
		if(o.tipoOnere == null) {
			return -1;
		}
		if(this.tipoOnere == null) {
			return 1;
		}
		CompareToBuilder ctb = new CompareToBuilder();
		ctb.append(this.tipoOnere.getCodice(), o.tipoOnere.getCodice())
			.append(this.tipoOnere.getDescrizione(), o.tipoOnere.getDescrizione());
		
		return ctb.toComparison();
	}

	@Override
	public int getUid() {
		return tipoOnere != null ? tipoOnere.getUid() : 0;
	}
	
}
