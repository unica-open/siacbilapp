/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

/**
 * Classe di wrap per il capitolo durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 24/10/2013
 * 
 */
public class ElementoCapitoloVariazione implements Serializable, Cloneable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = -935863646716334174L;

	private Integer uid;
	private Integer annoCapitolo;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer numeroUEB;
	private BigDecimal competenza = BigDecimal.ZERO;
	private BigDecimal residuo = BigDecimal.ZERO;
	private BigDecimal cassa = BigDecimal.ZERO;
	private BigDecimal competenzaOriginale = BigDecimal.ZERO;
	private BigDecimal residuoOriginale = BigDecimal.ZERO;
	private BigDecimal cassaOriginale = BigDecimal.ZERO;
	private BigDecimal fondoPluriennaleVincolato = BigDecimal.ZERO;
	private String descrizione;

	private String datiAccessorii;
	private String denominazione;
	private TipoCapitolo tipoCapitolo;

	private Boolean gestioneUEB = Boolean.TRUE;
	private Boolean daAnnullare = Boolean.FALSE;
	private Boolean daInserire = Boolean.FALSE;

	private StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio;

	// Lista dei figli
	private List<ElementoCapitoloVariazione> listaSottoElementi = new ArrayList<ElementoCapitoloVariazione>();
	
	
	//SIAC-6881
	private List<TipoComponenteImportiCapitolo> componentiCapitolo = new ArrayList<TipoComponenteImportiCapitolo>();
	
	//SIAC-6883: competenze relative all'anno= anno bilancio +2	
	private BigDecimal competenza1 = BigDecimal.ZERO;
	private BigDecimal competenzaOriginale1 = BigDecimal.ZERO;
	
	//SIAC-6883: competenze relative all'anno= anno bilancio +2
	private BigDecimal competenza2 = BigDecimal.ZERO;
	private BigDecimal competenzaOriginale2 = BigDecimal.ZERO;
	

	//
	
	
	
	/** Costruttore vuoto di default */
	public ElementoCapitoloVariazione() {
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

	
	public List<TipoComponenteImportiCapitolo> getComponentiCapitolo() {
		return componentiCapitolo;
	}
	
	public void setComponentiCapitolo(List<TipoComponenteImportiCapitolo> componentiCapitolo) {
		this.componentiCapitolo = componentiCapitolo;
	}


	//
	/**
	 * @return the annoCapitolo
	 */
	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}

	/**
	 * @param annoCapitolo the annoCapitolo to set
	 */
	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}

	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}

	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	/**
	 * @return the numeroUEB
	 */
	public Integer getNumeroUEB() {
		return numeroUEB;
	}

	/**
	 * @param numeroUEB the numeroUEB to set
	 */
	public void setNumeroUEB(Integer numeroUEB) {
		this.numeroUEB = numeroUEB;
	}

	/**
	 * @return the competenza
	 */
	public BigDecimal getCompetenza() {
		return competenza;
	}

	/**
	 * @param competenza the competenza to set
	 */
	public void setCompetenza(BigDecimal competenza) {
		this.competenza = competenza != null ? competenza : BigDecimal.ZERO;
	}

	/**
	 * @return the residuo
	 */
	public BigDecimal getResiduo() {
		return residuo;
	}

	/**
	 * @param residuo the residuo to set
	 */
	public void setResiduo(BigDecimal residuo) {
		this.residuo = residuo != null ? residuo : BigDecimal.ZERO;
	}

	/**
	 * @return the cassa
	 */
	public BigDecimal getCassa() {
		return cassa;
	}

	/**
	 * @param cassa the cassa to set
	 */
	public void setCassa(BigDecimal cassa) {
		this.cassa = cassa != null ? cassa : BigDecimal.ZERO;
	}

	/**
	 * @return the fondoPluriennaleVincolato
	 */
	public BigDecimal getFondoPluriennaleVincolato() {
		return fondoPluriennaleVincolato;
	}

	/**
	 * @param fondoPluriennaleVincolato the fondoPluriennaleVincolato to set
	 */
	public void setFondoPluriennaleVincolato(BigDecimal fondoPluriennaleVincolato) {
		this.fondoPluriennaleVincolato = fondoPluriennaleVincolato != null ? fondoPluriennaleVincolato : BigDecimal.ZERO;
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
	 * @return the datiAccessorii
	 */
	public String getDatiAccessorii() {
		return datiAccessorii;
	}

	/**
	 * @param datiAccessorii the datiAccessorii to set
	 */
	public void setDatiAccessorii(String datiAccessorii) {
		this.datiAccessorii = datiAccessorii;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @param denominazione the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @return the tipoCapitolo
	 */
	public TipoCapitolo getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(TipoCapitolo tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the gestioneUEB
	 */
	public Boolean getGestioneUEB() {
		return gestioneUEB;
	}

	/**
	 * @param gestioneUEB the gestioneUEB to set
	 */
	public void setGestioneUEB(Boolean gestioneUEB) {
		this.gestioneUEB = gestioneUEB != null ? gestioneUEB : Boolean.FALSE;
	}

	/**
	 * @return the statoOperativoElementoDiBilancio
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativoElementoDiBilancio() {
		return statoOperativoElementoDiBilancio;
	}

	/**
	 * @param statoOperativoElementoDiBilancio the statoOperativoElementoDiBilancio to set
	 */
	public void setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		this.statoOperativoElementoDiBilancio = statoOperativoElementoDiBilancio;
	}

	/**
	 * @return the daAnnullare
	 */
	public Boolean getDaAnnullare() {
		return daAnnullare;
	}

	/**
	 * @param daAnnullare the daAnnullare to set
	 */
	public void setDaAnnullare(Boolean daAnnullare) {
		this.daAnnullare = daAnnullare != null ? daAnnullare : Boolean.FALSE;
	}

	/**
	 * @return the daInserire
	 */
	public Boolean getDaInserire() {
		return daInserire;
	}

	/**
	 * @param daInserire the daInserire to set
	 */
	public void setDaInserire(Boolean daInserire) {
		this.daInserire = daInserire != null ? daInserire : Boolean.FALSE;
	}

	/**
	 * @return the listaSottoElementi
	 */
	public List<ElementoCapitoloVariazione> getListaSottoElementi() {
		return listaSottoElementi;
	}

	/**
	 * @param listaSottoElementi the listaSottoElementi to set
	 */
	public void setListaSottoElementi(List<ElementoCapitoloVariazione> listaSottoElementi) {
		this.listaSottoElementi = listaSottoElementi != null ? listaSottoElementi : new ArrayList<ElementoCapitoloVariazione>();
	}
	
	

	/**
	 * @return the competenzaOriginale
	 */
	public BigDecimal getCompetenzaOriginale() {
		return competenzaOriginale;
	}

	/**
	 * @param competenzaOriginale the competenzaOriginale to set
	 */
	public void setCompetenzaOriginale(BigDecimal competenzaOriginale) {
		this.competenzaOriginale = competenzaOriginale;
	}

	/**
	 * @return the residuoOriginale
	 */
	public BigDecimal getResiduoOriginale() {
		return residuoOriginale;
	}

	/**
	 * @param residuoOriginale the residuoOriginale to set
	 */
	public void setResiduoOriginale(BigDecimal residuoOriginale) {
		this.residuoOriginale = residuoOriginale;
	}

	/**
	 * @return the cassaOriginale
	 */
	public BigDecimal getCassaOriginale() {
		return cassaOriginale;
	}

	/**
	 * @param cassaOriginale the cassaOriginale to set
	 */
	public void setCassaOriginale(BigDecimal cassaOriginale) {
		this.cassaOriginale = cassaOriginale;
	}
	
	/**
	 * @return the competenza1
	 */
	public BigDecimal getCompetenza1() {
		return competenza1;
	}

	/**
	 * @param competenza1 the competenza1 to set
	 */
	public void setCompetenza1(BigDecimal competenza1) {
		this.competenza1 = competenza1;
	}

	/**
	 * @return the competenzaOriginale1
	 */
	public BigDecimal getCompetenzaOriginale1() {
		return competenzaOriginale1;
	}

	/**
	 * @param competenzaOriginale1 the competenzaOriginale1 to set
	 */
	public void setCompetenzaOriginale1(BigDecimal competenzaOriginale1) {
		this.competenzaOriginale1 = competenzaOriginale1;
	}

	/**
	 * @return the competenza2
	 */
	public BigDecimal getCompetenza2() {
		return competenza2;
	}

	/**
	 * @param competenza2 the competenza2 to set
	 */
	public void setCompetenza2(BigDecimal competenza2) {
		this.competenza2 = competenza2;
	}

	/**
	 * @return the competenzaOriginale2
	 */
	public BigDecimal getCompetenzaOriginale2() {
		return competenzaOriginale2;
	}

	/**
	 * @param competenzaOriginale2 the competenzaOriginale2 to set
	 */
	public void setCompetenzaOriginale2(BigDecimal competenzaOriginale2) {
		this.competenzaOriginale2 = competenzaOriginale2;
	}

	/**
	 * Unwrapping delle propriet&agrave; comuni per il wrapper.
	 * 
	 * @param capitolo il capitolo da popolare
	 */
	protected void unwrapCommons(Capitolo<?, ?> capitolo) {
		capitolo.setUid(uid);
		capitolo.setAnnoCapitolo(annoCapitolo);
		capitolo.setNumeroCapitolo(numeroCapitolo);
		capitolo.setNumeroArticolo(numeroArticolo);
		capitolo.setNumeroUEB(numeroUEB);
		capitolo.setDescrizione(descrizione);
		capitolo.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		// FIXME capitolo.setComponentiCapitolo(componentiCapitolo);
	}

	/**
	 * Restituisce il capitolo originale a partire dal wrapper
	 * @param <C> la tipizzazione del capitolo originale
	 * @return il capitolo originale
	 */
	@SuppressWarnings("unchecked")
	public <C extends Capitolo<?, ?>> C unwrap() {
		C result = null;
		TipoCapitolo tipoCapitoloPresente = this.getTipoCapitolo();
		switch (tipoCapitoloPresente) {
		case CAPITOLO_USCITA_PREVISIONE:
			result = (C) new ElementoCapitoloVariazioneUscitaPrevisione(this).unwrap();
			break;
		case CAPITOLO_USCITA_GESTIONE:
			result = (C) new ElementoCapitoloVariazioneUscitaGestione(this).unwrap();
			break;
		case CAPITOLO_ENTRATA_PREVISIONE:
			result = (C) new ElementoCapitoloVariazioneEntrataPrevisione(this).unwrap();
			break;
		case CAPITOLO_ENTRATA_GESTIONE:
			result = (C) new ElementoCapitoloVariazioneEntrataGestione(this).unwrap();
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * Aggiunge un elemento di capitolo variazione al presente wrapper.
	 * 
	 * @param other il wrapper da aggiungere
	 * 
	 * @return il wrapper con un sotto-elemento in pi&ugrave;
	 */
	public ElementoCapitoloVariazione addElementoCapitoloVariazione(ElementoCapitoloVariazione other) {
		listaSottoElementi.add(other);
		return this;
	}
	
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		super.clone();
		return SerializationUtils.clone(this);
	}

}
