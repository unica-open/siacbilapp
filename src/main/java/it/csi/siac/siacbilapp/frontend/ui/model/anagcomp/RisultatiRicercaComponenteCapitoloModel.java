/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.anagcomp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.ComponenteCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.model.stornoueb.RisultatiRicercaStornoUEBModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;


/**
 * Classe di model per i risultati di ricerca per il componente capitolo. Contiene una mappatura dei campi del form dei risultati.
 * 
 * @author Jacopo Bongiovanni
 * @version 1.0.0 - 17/09/2019
 *
 */
public class RisultatiRicercaComponenteCapitoloModel  extends GenericBilancioModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3989699654033404427L;
	
	
	// Per le azioni da delegare all'esterno
	private int uidDaAggiornare;
	private int uidDaAnnullare;
	
	private int uidComponenteCapitolo;
	
	private List<ComponenteCapitoloModel> listaComponenteCapitolo = new ArrayList<ComponenteCapitoloModel>();
	
	// Per il dataTable
	 
	private Integer posizioneIniziale;
	
	
	
	
	// Property necessarie per pilotare la dataTable con  il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	/**
	 * Crea una request per il servizio di {@link AnnullaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public AnnullaTipoComponenteImportiCapitolo creaRequestAnnullaTipoComponenteImportiCapitolo() {
		AnnullaTipoComponenteImportiCapitolo request = creaRequest(AnnullaTipoComponenteImportiCapitolo.class);
		
		// Creo l'atto per l'annullamento a partire dall'uid
		TipoComponenteImportiCapitolo aa = new TipoComponenteImportiCapitolo();
		aa.setUid(getUidComponenteCapitolo());
		//aa.setEnte(getEnte());
		request.setTipoComponenteImportiCapitolo(aa);
		//request.setBilancio(getBilancio());
		
		return request;
	}
	/**
	 * 
	 */
	public RisultatiRicercaComponenteCapitoloModel() {
		super();
		setTitolo("Risultati di ricerca anagrafica componenti");
	}
	/**
	 * @return the uidDaAggiornare
	 */
	public int getUidDaAggiornare() {
		return uidDaAggiornare;
	}
	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}
	/**
	 * @return the uidDaAnnullare
	 */
	public int getUidDaAnnullare() {
		return uidDaAnnullare;
	}
	/**
	 * @param uidDaAnnullare the uidDaAnnullare to set
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		this.uidDaAnnullare = uidDaAnnullare;
	}
	 
	
	
	
	/**
	 * @return the uidComponenteCapitolo
	 */
	public int getUidComponenteCapitolo() {
		return uidComponenteCapitolo;
	}
	/**
	 * @param uidComponenteCapitolo the uidComponenteCapitolo to set
	 */
	public void setUidComponenteCapitolo(int uidComponenteCapitolo) {
		this.uidComponenteCapitolo = uidComponenteCapitolo;
	}
	/**
	 * @return the listaComponenteCapitolo
	 */
	public List<ComponenteCapitoloModel> getListaComponenteCapitolo() {
		return listaComponenteCapitolo;
	}
	/**
	 * @param listaComponenteCapitolo the listaComponenteCapitolo to set
	 */
	public void setListaComponenteCapitolo(List<ComponenteCapitoloModel> listaComponenteCapitolo) {
		this.listaComponenteCapitolo = listaComponenteCapitolo;
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
	 * @return the posizioneIniziale
	 */
	public Integer getPosizioneIniziale() {
		return posizioneIniziale;
	}
	/**
	 * @param posizioneIniziale the posizioneIniziale to set
	 */
	public void setPosizioneIniziale(Integer posizioneIniziale) {
		this.posizioneIniziale = posizioneIniziale;
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
		RisultatiRicercaComponenteCapitoloModel other = (RisultatiRicercaComponenteCapitoloModel) obj;
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
