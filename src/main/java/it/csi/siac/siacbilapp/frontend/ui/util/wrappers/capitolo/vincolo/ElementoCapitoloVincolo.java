/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per il Capitolo all'interno di un Vincolo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 02/01/2013
 *
 */
public class ElementoCapitoloVincolo implements Serializable, Comparable<ElementoCapitoloVincolo>, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5572161356567979937L;
	
	private int uid;
	private String capitolo;
	private String descrizione;
	private String classificazione;
	private BigDecimal competenzaAnno0 = BigDecimal.ZERO;
	private BigDecimal competenzaAnno1 = BigDecimal.ZERO;
	private BigDecimal competenzaAnno2 = BigDecimal.ZERO;
	private String strutturaAmministrativoContabile;
	
	/** Costruttore vuoto di default */
	public ElementoCapitoloVincolo() {
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
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the classificazione
	 */
	public String getClassificazione() {
		return classificazione;
	}

	/**
	 * @param classificazione the classificazione to set
	 */
	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}

	/**
	 * @return the competenzaAnno0
	 */
	public BigDecimal getCompetenzaAnno0() {
		return competenzaAnno0;
	}

	/**
	 * @param competenzaAnno0 the competenzaAnno0 to set
	 */
	public void setCompetenzaAnno0(BigDecimal competenzaAnno0) {
		this.competenzaAnno0 = competenzaAnno0;
	}

	/**
	 * @return the competenzaAnno1
	 */
	public BigDecimal getCompetenzaAnno1() {
		return competenzaAnno1;
	}

	/**
	 * @param competenzaAnno1 the competenzaAnno1 to set
	 */
	public void setCompetenzaAnno1(BigDecimal competenzaAnno1) {
		this.competenzaAnno1 = competenzaAnno1;
	}

	/**
	 * @return the competenzaAnno2
	 */
	public BigDecimal getCompetenzaAnno2() {
		return competenzaAnno2;
	}

	/**
	 * @param competenzaAnno2 the competenzaAnno2 to set
	 */
	public void setCompetenzaAnno2(BigDecimal competenzaAnno2) {
		this.competenzaAnno2 = competenzaAnno2;
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
	public void setStrutturaAmministrativoContabile(String strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoCapitoloVincolo)) {
			return false;
		}
		ElementoCapitoloVincolo o = (ElementoCapitoloVincolo) obj;
		return this.uid == o.uid;
	}
	
	@Override
	public int hashCode() {
		return uid;
	}
	
	@Override
	public int compareTo(ElementoCapitoloVincolo o) {
		return o == null ? -1 : o.uid - this.uid;
	}
	
}
