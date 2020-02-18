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

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model per l'aggiornamento dell'Atto di Legge. Contiene una mappatura dei campi del form.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/09/2013
 *
 */
public class AggiornaAttoDiLeggeModel extends GenericBilancioModel {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = 5034821499008092856L;
	
	private AttoDiLegge attoDiLegge;
	
	/** Costruttore vuoto di default */
	public AggiornaAttoDiLeggeModel() {
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
		if (!(obj instanceof AggiornaAttoDiLeggeModel)) {
			return false;
		}
		AggiornaAttoDiLeggeModel other = (AggiornaAttoDiLeggeModel) obj;
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
	 * Crea una request per l'aggiornamento di un Atto di Legge.
	 * 
	 * @return la request creata
	 */
	public AggiornaAttoDiLegge creaRequestAggiornaAttoDiLegge() {
		AggiornaAttoDiLegge request = new AggiornaAttoDiLegge();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento dell'atto di legge
		request.setAttoDiLegge(attoDiLegge);
		
		return request;
	}
	
}
