/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.attodilegge;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaLeggi;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;

/**
 * Classe di model per la ricerca dell'Atto di Legge. La classe mappa il form di ricerca.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/09/2013
 *
 */
public class RicercaAttoDiLeggeModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1874393116181044366L;
	
	private Integer annoLegge;
	private Integer numeroLegge;
	private TipoAtto tipoAtto;
	private String articolo;
	private String comma;
	private String punto;
	
	/* Per la cancellazione */
	private Integer uidAttoDiLegge;
	
	/* Lista dei risultati */
	private List<AttoDiLegge> listaAttoDiLegge = new ArrayList<AttoDiLegge>();
	
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	private List<AttoDiLegge> aaData = new ArrayList<AttoDiLegge>();
	
	/** Costruttore vuoto di default */
	public RicercaAttoDiLeggeModel() {
		super();
	}
	
	/* Getter e Setter */
	
	/**
	 * @return the annoLegge
	 */
	public Integer getAnnoLegge() {
		return annoLegge;
	}

	/**
	 * @param annoLegge the annoLegge to set
	 */
	public void setAnnoLegge(Integer annoLegge) {
		this.annoLegge = annoLegge;
	}

	/**
	 * @return the numeroLegge
	 */
	public Integer getNumeroLegge() {
		return numeroLegge;
	}

	/**
	 * @param numeroLegge the numeroLegge to set
	 */
	public void setNumeroLegge(Integer numeroLegge) {
		this.numeroLegge = numeroLegge;
	}

	/**
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	/**
	 * @return the articolo
	 */
	public String getArticolo() {
		return articolo;
	}

	/**
	 * @param articolo the articolo to set
	 */
	public void setArticolo(String articolo) {
		this.articolo = articolo;
	}

	/**
	 * @return the comma
	 */
	public String getComma() {
		return comma;
	}

	/**
	 * @param comma the comma to set
	 */
	public void setComma(String comma) {
		this.comma = comma;
	}

	/**
	 * @return the punto
	 */
	public String getPunto() {
		return punto;
	}

	/**
	 * @param punto the punto to set
	 */
	public void setPunto(String punto) {
		this.punto = punto;
	}

	/**
	 * @return the uidAttoDiLegge
	 */
	public Integer getUidAttoDiLegge() {
		return uidAttoDiLegge;
	}

	/**
	 * @param uidAttoDiLegge the uidAttoDiLegge to set
	 */
	public void setUidAttoDiLegge(Integer uidAttoDiLegge) {
		this.uidAttoDiLegge = uidAttoDiLegge;
	}

	/**
	 * @return the listaAttoDiLegge
	 */
	public List<AttoDiLegge> getListaAttoDiLegge() {
		return listaAttoDiLegge;
	}

	/**
	 * @param listaAttoDiLegge the listaAttoDiLegge to set
	 */
	public void setListaAttoDiLegge(List<AttoDiLegge> listaAttoDiLegge) {
		this.listaAttoDiLegge = listaAttoDiLegge;
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
	public List<AttoDiLegge> getAaData() {
		return aaData;
	}

	/**
	 * @param aaData the aaData to set
	 */
	public void setAaData(List<AttoDiLegge> aaData) {
		this.aaData = aaData;
	}
	
	/* Override di equals, hashCode, toString */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RicercaAttoDiLeggeModel)) {
			return false;
		}
		RicercaAttoDiLeggeModel other = (RicercaAttoDiLeggeModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(annoLegge, other.annoLegge)
			.append(numeroLegge, other.numeroLegge)
			.append(tipoAtto, other.tipoAtto)
			.append(articolo, other.articolo)
			.append(comma, other.comma)
			.append(punto, other.punto);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(annoLegge)
			.append(numeroLegge)
			.append(tipoAtto)
			.append(articolo)
			.append(comma)
			.append(punto);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("anno legge", annoLegge)
			.append("numero legge", numeroLegge)
			.append("tipo atto", tipoAtto)
			.append("articolo", articolo)
			.append("comma", comma)
			.append("punto", punto);
		return toStringBuilder.toString();
	}
	
	/* Requests */
	
	/**
	 * Crea una request per la ricerca di un Atto di Legge.
	 * 
	 * @return la request creata
	 */
	public RicercaAttoDiLegge creaRequestRicercaAttoDiLegge() {
		RicercaAttoDiLegge request = new RicercaAttoDiLegge();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento dell'atto di legge
		request.setAttoDiLegge(creaUtilityRicercaLeggi());
		
		return request;
	}
	
	/**
	 * Crea una request per la cancellazione di un Atto di Legge.
	 * 
	 * @return la request creata
	 */
	public CancellaAttoDiLegge creaRequestCancellaAttoDiLegge() {
		CancellaAttoDiLegge request = new CancellaAttoDiLegge();
		
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		// Popolamento dell'atto di legge
		request.setAttoDiLegge(creaAttoDiLeggeDaCancellare());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un'<em>utility</em> per la ricerca dell'Atto di Legge.
	 * 
	 * @return l'utility creata
	 */
	private RicercaLeggi creaUtilityRicercaLeggi() {
		RicercaLeggi utility = new RicercaLeggi();
		if (uidAttoDiLegge == null || uidAttoDiLegge == 0) {
			// Popolamento dei campi
			utility.setAnno(annoLegge);
			utility.setNumero(numeroLegge);
			utility.setArticolo(articolo);
			utility.setComma(comma);
			utility.setPunto(punto);
			utility.setStato(StatoEntita.VALIDO);
			utility.setTipoAtto(tipoAtto);
		} else {
			utility.setUid(uidAttoDiLegge);
		}
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un Atto di Legge per la cancellazione.
	 * 
	 * @return l'Atto da cancellare
	 */
	private AttoDiLegge creaAttoDiLeggeDaCancellare() {
		AttoDiLegge attoDiLegge = new AttoDiLegge();
		if(uidAttoDiLegge != null) {
			attoDiLegge.setUid(uidAttoDiLegge);
		} else {
			attoDiLegge.setUid(0);
		}
		
		return attoDiLegge;
	}
	
}
