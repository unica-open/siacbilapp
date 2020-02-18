/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;

/**
 * Classe di wrap per i Capitoli, utile per la gestione sulla lista paginata degli stessi.
 * 
 * @author Luciano Gallo, Alessandro Marchino
 *
 */
public class ElementoCapitolo implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6538068876539631495L;
	
	private Integer uid;
	private String capitolo;
	private String stato;
	private String descrizione;
	private String classificazione;
	
	private BigDecimal stanziamentoCompetenza = BigDecimal.ZERO;
	private BigDecimal stanziamentoResiduo = BigDecimal.ZERO;
	private BigDecimal stanziamentoCassa = BigDecimal.ZERO;
	
	private BigDecimal stanziamentoCompetenza1 = BigDecimal.ZERO;
	private BigDecimal stanziamentoCompetenza2 = BigDecimal.ZERO;
	private BigDecimal stanziamentoResiduo1 = BigDecimal.ZERO;
	private BigDecimal stanziamentoResiduo2 = BigDecimal.ZERO;
	private BigDecimal stanziamentoCassa1 = BigDecimal.ZERO;
	private BigDecimal stanziamentoCassa2 = BigDecimal.ZERO;
	
	private BigDecimal disponibileAnno0 = BigDecimal.ZERO;
	private BigDecimal disponibileAnno1 = BigDecimal.ZERO;
	private BigDecimal disponibileAnno2 = BigDecimal.ZERO;
	
	private BigDecimal disponibileVariareAnno0 = BigDecimal.ZERO;
	private BigDecimal disponibileVariareAnno1 = BigDecimal.ZERO;
	private BigDecimal disponibileVariareAnno2 = BigDecimal.ZERO;
	
	private Boolean rilevanteIva = Boolean.FALSE;
	
	private String struttAmmResp;
	private String pdcFinanziario;
	private String pdcVoce;
	private String azioni;
	private String annoCapitolo;
	private String numeroCapitolo;
	private String numeroArticolo;
	private String numeroUEB;
	
	private Missione missione;
	private Programma programma;
	private TitoloSpesa titoloSpesa;
	
	private TitoloEntrata titoloEntrata;
	private TipologiaTitolo tipologiaTitolo;
	
	private CategoriaCapitolo categoriaCapitolo;
	
	/** Costruttore vuoto di default */
	public ElementoCapitolo() {
		super();
	}

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
	 * @return the stanziamentoCompetenza
	 */
	public BigDecimal getStanziamentoCompetenza() {
		return stanziamentoCompetenza;
	}

	/**
	 * @param stanziamentoCompetenza the stanziamentoCompetenza to set
	 */
	public void setStanziamentoCompetenza(BigDecimal stanziamentoCompetenza) {
		this.stanziamentoCompetenza = stanziamentoCompetenza != null ? stanziamentoCompetenza : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoResiduo
	 */
	public BigDecimal getStanziamentoResiduo() {
		return stanziamentoResiduo;
	}

	/**
	 * @param stanziamentoResiduo the stanziamentoResiduo to set
	 */
	public void setStanziamentoResiduo(BigDecimal stanziamentoResiduo) {
		this.stanziamentoResiduo = stanziamentoResiduo != null ? stanziamentoResiduo : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoCassa
	 */
	public BigDecimal getStanziamentoCassa() {
		return stanziamentoCassa;
	}

	/**
	 * @param stanziamentoCassa the stanziamentoCassa to set
	 */
	public void setStanziamentoCassa(BigDecimal stanziamentoCassa) {
		this.stanziamentoCassa = stanziamentoCassa != null ? stanziamentoCassa : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoCompetenza1
	 */
	public BigDecimal getStanziamentoCompetenza1() {
		return stanziamentoCompetenza1;
	}

	/**
	 * @param stanziamentoCompetenza1 the stanziamentoCompetenza1 to set
	 */
	public void setStanziamentoCompetenza1(BigDecimal stanziamentoCompetenza1) {
		this.stanziamentoCompetenza1 = stanziamentoCompetenza1 != null ? stanziamentoCompetenza1 : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoCompetenza2
	 */
	public BigDecimal getStanziamentoCompetenza2() {
		return stanziamentoCompetenza2;
	}

	/**
	 * @param stanziamentoCompetenza2 the stanziamentoCompetenza2 to set
	 */
	public void setStanziamentoCompetenza2(BigDecimal stanziamentoCompetenza2) {
		this.stanziamentoCompetenza2 = stanziamentoCompetenza2 != null ? stanziamentoCompetenza2 : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoResiduo1
	 */
	public BigDecimal getStanziamentoResiduo1() {
		return stanziamentoResiduo1;
	}

	/**
	 * @param stanziamentoResiduo1 the stanziamentoResiduo1 to set
	 */
	public void setStanziamentoResiduo1(BigDecimal stanziamentoResiduo1) {
		this.stanziamentoResiduo1 = stanziamentoResiduo1 != null ? stanziamentoResiduo1 : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoResiduo2
	 */
	public BigDecimal getStanziamentoResiduo2() {
		return stanziamentoResiduo2;
	}

	/**
	 * @param stanziamentoResiduo2 the stanziamentoResiduo2 to set
	 */
	public void setStanziamentoResiduo2(BigDecimal stanziamentoResiduo2) {
		this.stanziamentoResiduo2 = stanziamentoResiduo2 != null ? stanziamentoResiduo2 : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoCassa1
	 */
	public BigDecimal getStanziamentoCassa1() {
		return stanziamentoCassa1;
	}

	/**
	 * @param stanziamentoCassa1 the stanziamentoCassa1 to set
	 */
	public void setStanziamentoCassa1(BigDecimal stanziamentoCassa1) {
		this.stanziamentoCassa1 = stanziamentoCassa1 != null ? stanziamentoCassa1 : BigDecimal.ZERO;
	}

	/**
	 * @return the stanziamentoCassa2
	 */
	public BigDecimal getStanziamentoCassa2() {
		return stanziamentoCassa2;
	}

	/**
	 * @param stanziamentoCassa2 the stanziamentoCassa2 to set
	 */
	public void setStanziamentoCassa2(BigDecimal stanziamentoCassa2) {
		this.stanziamentoCassa2 = stanziamentoCassa2 != null ? stanziamentoCassa2 : BigDecimal.ZERO;
	}

	/**
	 * @return the disponibileAnno0
	 */
	public BigDecimal getDisponibileAnno0() {
		return disponibileAnno0;
	}

	/**
	 * @param disponibileAnno0 the disponibileAnno0 to set
	 */
	public void setDisponibileAnno0(BigDecimal disponibileAnno0) {
		this.disponibileAnno0 = disponibileAnno0 != null ? disponibileAnno0 : BigDecimal.ZERO;
	}

	/**
	 * @return the disponibileAnno1
	 */
	public BigDecimal getDisponibileAnno1() {
		return disponibileAnno1;
	}

	/**
	 * @param disponibileAnno1 the disponibileAnno1 to set
	 */
	public void setDisponibileAnno1(BigDecimal disponibileAnno1) {
		this.disponibileAnno1 = disponibileAnno1 != null ? disponibileAnno1 : BigDecimal.ZERO;
	}

	/**
	 * @return the disponibileAnno2
	 */
	public BigDecimal getDisponibileAnno2() {
		return disponibileAnno2;
	}

	/**
	 * @param disponibileAnno2 the disponibileAnno2 to set
	 */
	public void setDisponibileAnno2(BigDecimal disponibileAnno2) {
		this.disponibileAnno2 = disponibileAnno2 != null ? disponibileAnno2 : BigDecimal.ZERO;
	}

	/**
	 * @return the disponibileVariareAnno0
	 */
	public final BigDecimal getDisponibileVariareAnno0() {
		return disponibileVariareAnno0;
	}

	/**
	 * @param disponibileVariareAnno0 the disponibileVariareAnno0 to set
	 */
	public final void setDisponibileVariareAnno0(BigDecimal disponibileVariareAnno0) {
		this.disponibileVariareAnno0 = disponibileVariareAnno0 != null ? disponibileVariareAnno0 : BigDecimal.ZERO;
	}

	/**
	 * @return the disponibileVariareAnno1
	 */
	public final BigDecimal getDisponibileVariareAnno1() {
		return disponibileVariareAnno1;
	}

	/**
	 * @param disponibileVariareAnno1 the disponibileVariareAnno1 to set
	 */
	public final void setDisponibileVariareAnno1(BigDecimal disponibileVariareAnno1) {
		this.disponibileVariareAnno1 = disponibileVariareAnno1 != null ? disponibileVariareAnno1 : BigDecimal.ZERO;
	}

	/**
	 * @return the disponibileVariareAnno2
	 */
	public final BigDecimal getDisponibileVariareAnno2() {
		return disponibileVariareAnno2;
	}

	/**
	 * @param disponibileVariareAnno2 the disponibileVariareAnno2 to set
	 */
	public final void setDisponibileVariareAnno2(BigDecimal disponibileVariareAnno2) {
		this.disponibileVariareAnno2 = disponibileVariareAnno2 != null ? disponibileVariareAnno2 : BigDecimal.ZERO;
	}

	/**
	 * @return the rilevanteIva
	 */
	public Boolean getRilevanteIva() {
		return rilevanteIva;
	}

	/**
	 * @param rilevanteIva the rilevanteIva to set
	 */
	public void setRilevanteIva(Boolean rilevanteIva) {
		this.rilevanteIva = rilevanteIva != null ? rilevanteIva : Boolean.FALSE;
	}

	/**
	 * @return the struttAmmResp
	 */
	public String getStruttAmmResp() {
		return struttAmmResp;
	}

	/**
	 * @param struttAmmResp the struttAmmResp to set
	 */
	public void setStruttAmmResp(String struttAmmResp) {
		this.struttAmmResp = struttAmmResp;
	}

	/**
	 * @return the pdcFinanziario
	 */
	public String getPdcFinanziario() {
		return pdcFinanziario;
	}

	/**
	 * @param pdcFinanziario the pdcFinanziario to set
	 */
	public void setPdcFinanziario(String pdcFinanziario) {
		this.pdcFinanziario = pdcFinanziario;
	}

	/**
	 * @return the pdcVoce
	 */
	public String getPdcVoce() {
		return pdcVoce;
	}

	/**
	 * @param pdcVoce the pdcVoce to set
	 */
	public void setPdcVoce(String pdcVoce) {
		this.pdcVoce = pdcVoce;
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
	 * @return the annoCapitolo
	 */
	public String getAnnoCapitolo() {
		return annoCapitolo;
	}

	/**
	 * @param annoCapitolo the annoCapitolo to set
	 */
	public void setAnnoCapitolo(String annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	/**
	 * @return the numeroCapitolo
	 */
	public String getNumeroCapitolo() {
		return numeroCapitolo;
	}

	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(String numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	/**
	 * @return the numeroArticolo
	 */
	public String getNumeroArticolo() {
		return numeroArticolo;
	}

	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(String numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	/**
	 * @return the numeroUEB
	 */
	public String getNumeroUEB() {
		return numeroUEB;
	}

	/**
	 * @param numeroUEB the numeroUEB to set
	 */
	public void setNumeroUEB(String numeroUEB) {
		this.numeroUEB = numeroUEB;
	}

	/**
	 * @return the missione
	 */
	public Missione getMissione() {
		return missione;
	}

	/**
	 * @param missione the missione to set
	 */
	public void setMissione(Missione missione) {
		this.missione = missione;
	}

	/**
	 * @return the programma
	 */
	public Programma getProgramma() {
		return programma;
	}

	/**
	 * @param programma the programma to set
	 */
	public void setProgramma(Programma programma) {
		this.programma = programma;
	}

	/**
	 * @return the titoloSpesa
	 */
	public TitoloSpesa getTitoloSpesa() {
		return titoloSpesa;
	}

	/**
	 * @param titoloSpesa the titoloSpesa to set
	 */
	public void setTitoloSpesa(TitoloSpesa titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}

	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}

	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}

	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}

	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}

	/**
	 * @return the categoriaCapitolo
	 */
	public CategoriaCapitolo getCategoriaCapitolo() {
		return categoriaCapitolo;
	}

	/**
	 * @param categoriaCapitolo the categoriaCapitolo to set
	 */
	public void setCategoriaCapitolo(CategoriaCapitolo categoriaCapitolo) {
		this.categoriaCapitolo = categoriaCapitolo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementoCapitolo)) {
			return false;
		}
		ElementoCapitolo other = (ElementoCapitolo) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(this.uid, other.uid)
			.append(this.capitolo, other.capitolo)
			.append(this.stato, other.stato)
			.append(this.descrizione, other.descrizione)
			.append(this.classificazione, other.classificazione)
			.append(this.stanziamentoCompetenza, other.stanziamentoCompetenza)
			.append(this.stanziamentoResiduo, other.stanziamentoResiduo)
			.append(this.stanziamentoCassa, other.stanziamentoCassa)
			.append(this.struttAmmResp, other.struttAmmResp)
			.append(this.pdcFinanziario, other.pdcFinanziario)
			.append(this.pdcVoce, other.pdcVoce);
		return equalsBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(uid)
			.append(capitolo)
			.append(stato)
			.append(descrizione)
			.append(classificazione)
			.append(stanziamentoCompetenza)
			.append(stanziamentoResiduo)
			.append(stanziamentoCassa)
			.append(struttAmmResp)
			.append(pdcFinanziario)
			.append(pdcVoce);
		return hashCodeBuilder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("uid", uid)
			.append("capitolo", capitolo)
			.append("stato", stato)
			.append("descrizione", descrizione)
			.append("classificazione", classificazione)
			.append("stanziamento di competenza", stanziamentoCompetenza)
			.append("stanziamento residuo", stanziamentoResiduo)
			.append("stanziamento cassa", stanziamentoCassa)
			.append("struttura amministrativo responsabile", struttAmmResp)
			.append("piano dei conti finanziario", pdcFinanziario)
			.append("voce del piano dei conti", pdcVoce);
		return toStringBuilder.toString();
	}
	
}
