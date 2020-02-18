/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.stornoueb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.stornoueb.ElementoStornoUEB;
import it.csi.siac.siacbilser.model.StornoUEB;

/**
 * Classe di model per i risultati di ricerca per lo storno UEB. Contiene una mappatura dei campi del form dei risultati.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/09/2013
 *
 */
public class RisultatiRicercaStornoUEBModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7919894350778325481L;
	
	private StornoUEB stornoUEB;
	
	private List<StornoUEB> listaStornoUEB = new ArrayList<StornoUEB>();
	
	// Property necessarie per pilotare la dataTable con  il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	private List<ElementoStornoUEB> aaData = new ArrayList<ElementoStornoUEB>();
	
	private Integer uidDaAggiornare;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaStornoUEBModel() {
		super();
		setTitolo("Risultati di ricerca storni UEB");
	}
	
	/* Getter e Setter */

	/**
	 * @return the stornoUEB
	 */
	public StornoUEB getStornoUEB() {
		return stornoUEB;
	}

	/**
	 * @param stornoUEB the stornoUEB to set
	 */
	public void setStornoUEB(StornoUEB stornoUEB) {
		this.stornoUEB = stornoUEB;
	}

	/**
	 * @return the listaStornoUEB
	 */
	public List<StornoUEB> getListaStornoUEB() {
		return listaStornoUEB;
	}

	/**
	 * @param listaStornoUEB the listaStornoUEB to set
	 */
	public void setListaStornoUEB(List<StornoUEB> listaStornoUEB) {
		this.listaStornoUEB = listaStornoUEB;
	}

	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the aaData
	 */
	public List<ElementoStornoUEB> getAaData() {
		return aaData;
	}

	/**
	 * @param aaData the aaData to set
	 */
	public void setAaData(List<ElementoStornoUEB> aaData) {
		this.aaData = aaData;
	}
	
	/**
	 * @return the uidDaAggiornare
	 */
	public Integer getUidDaAggiornare() {
		return uidDaAggiornare;
	}
	
	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(Integer uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}
	
	/* Override di equals, hashCode, toString */
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RisultatiRicercaStornoUEBModel)) {
			return false;
		}
		RisultatiRicercaStornoUEBModel other = (RisultatiRicercaStornoUEBModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(uidDaAggiornare, other.uidDaAggiornare);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(uidDaAggiornare);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("uid da aggiornare", uidDaAggiornare);
		return toStringBuilder.toString();
	}
	
}
