/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.attodilegge;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Model per l'inserimento dell'Atto di Legge. Mappa i campi del form di inserimento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/09/2013
 *
 */
public class InserisciAttoDiLeggeModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1966676985334930039L;
	
	private AttoDiLegge attoDiLegge;
	
	/** Costruttore vuoto di default */
	public InserisciAttoDiLeggeModel() {
		super();
	}
	
	/* Getter e Setter */

	/**
	 * @return the attoDiLegge
	 */
	public AttoDiLegge getAttoDiLegge() {
		return attoDiLegge;
	}

	/**
	 * @param attoDiLegge the attoDiLegge to set
	 */
	public void setAttoDiLegge(AttoDiLegge attoDiLegge) {
		this.attoDiLegge = attoDiLegge;
	}
	
	/* ==== Override di equals, hashCode, ToString */
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof InserisciAttoDiLeggeModel)) {
			return false;
		}
		InserisciAttoDiLeggeModel other = (InserisciAttoDiLeggeModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(attoDiLegge, other.attoDiLegge);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(attoDiLegge);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("atto di legge", attoDiLegge);
		
		return toStringBuilder.toString();
	}
	
	/* Requests */
	
	/**
	 * Crea una request per la inserimento di un Atto di Legge.
	 * 
	 * @return la request creata
	 */
	public InserisceAttoDiLegge creaRequestInserisceAttoDiLegge() {
		InserisceAttoDiLegge request = new InserisceAttoDiLegge();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento dell'atto di legge
		request.setAttoDiLegge(attoDiLegge);
		
		return request;
	}
	
}
