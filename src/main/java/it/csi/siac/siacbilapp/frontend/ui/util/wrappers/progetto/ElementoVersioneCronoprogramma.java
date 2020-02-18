/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;

/**
 * Classe di wrap per il Progetto.

 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 05/02/2013
 *
 */
public class ElementoVersioneCronoprogramma implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	
	private int uid;
	private String versione;
	private String descrizione;
	private BigDecimal competenzaSpesaAnno0 = BigDecimal.ZERO;
	private BigDecimal competenzaSpesaAnno1 = BigDecimal.ZERO;
	private BigDecimal competenzaSpesaAnno2 = BigDecimal.ZERO;
	private BigDecimal competenzaSpesaAnnoSucc = BigDecimal.ZERO;
	
	//SIAC-6255
	private BigDecimal competenzaSpesaAnnoPrec = BigDecimal.ZERO;
	private BigDecimal competenzaEntrataAnno0 = BigDecimal.ZERO;
	private BigDecimal competenzaEntrataAnno1 = BigDecimal.ZERO;
	private BigDecimal competenzaEntrataAnno2 = BigDecimal.ZERO;
	private BigDecimal competenzaEntrataAnnoSucc = BigDecimal.ZERO;
	private BigDecimal competenzaEntrataAnnoPrec = BigDecimal.ZERO;
	private TipoProgetto tipoProgetto;
	
	private BigDecimal quadraturaEntrata = BigDecimal.ZERO;
	private BigDecimal quadraturaUscita = BigDecimal.ZERO;
	
	private String statoOperativoCronoprogramma;
	//SIAC-6255
	private String descrizioneStatoOperativoCronoprogramma;
	private Boolean usatoPerFpvProv;
	
	private String azioni;
	
	private Boolean usatoPerFpv;
	// SIAC-4246
	private Boolean daDefinire;
	
	/** Costruttore vuoto di default */
	public ElementoVersioneCronoprogramma() {
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
	 * @return the versione
	 */
	public String getVersione() {
		return versione;
	}

	/**
	 * @param versione the versione to set
	 */
	public void setVersione(String versione) {
		this.versione = versione;
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
	 * @return the competenzaAnno0
	 */
	public BigDecimal getCompetenzaSpesaAnno0() {
		return competenzaSpesaAnno0;
	}

	/**
	 * @param competenzaAnno0 the competenzaAnno0 to set
	 */
	public void setCompetenzaSpesaAnno0(BigDecimal competenzaAnno0) {
		this.competenzaSpesaAnno0 = competenzaAnno0;
	}

	/**
	 * @return the competenzaAnno1
	 */
	public BigDecimal getCompetenzaSpesaAnno1() {
		return competenzaSpesaAnno1;
	}

	/**
	 * @param competenzaAnno1 the competenzaAnno1 to set
	 */
	public void setCompetenzaSpesaAnno1(BigDecimal competenzaAnno1) {
		this.competenzaSpesaAnno1 = competenzaAnno1;
	}

	/**
	 * @return the competenzaAnno2
	 */
	public BigDecimal getCompetenzaSpesaAnno2() {
		return competenzaSpesaAnno2;
	}

	/**
	 * @param competenzaAnno2 the competenzaAnno2 to set
	 */
	public void setCompetenzaSpesaAnno2(BigDecimal competenzaAnno2) {
		this.competenzaSpesaAnno2 = competenzaAnno2;
	}

	/**
	 * @return the competenzaAnnoSucc
	 */
	public BigDecimal getCompetenzaSpesaAnnoSucc() {
		return competenzaSpesaAnnoSucc;
	}

	/**
	 * @param competenzaAnnoSucc the competenzaAnnoSucc to set
	 */
	public void setCompetenzaSpesaAnnoSucc(BigDecimal competenzaAnnoSucc) {
		this.competenzaSpesaAnnoSucc = competenzaAnnoSucc;
	}
	
	/**
	 * @return the competenzaAnnoPrecedente
	 */
	public BigDecimal getCompetenzaSpesaAnnoPrec() {
		return competenzaSpesaAnnoPrec;
	}

	/**
	 * @param competenzaAnnoPrec the competenzaAnnoPrec to set
	 */
	public void setCompetenzaSpesaAnnoPrec(BigDecimal competenzaAnnoPrec) {
		this.competenzaSpesaAnnoPrec = competenzaAnnoPrec;
	}

	/**
	 * @return the competenzaEntrataAnno0
	 */
	public BigDecimal getCompetenzaEntrataAnno0() {
		return competenzaEntrataAnno0;
	}

	/**
	 * @param competenzaEntrataAnno0 the competenzaEntrataAnno0 to set
	 */
	public void setCompetenzaEntrataAnno0(BigDecimal competenzaEntrataAnno0) {
		this.competenzaEntrataAnno0 = competenzaEntrataAnno0;
	}

	/**
	 * @return the competenzaEntrataAnno1
	 */
	public BigDecimal getCompetenzaEntrataAnno1() {
		return competenzaEntrataAnno1;
	}

	/**
	 * @param competenzaEntrataAnno1 the competenzaEntrataAnno1 to set
	 */
	public void setCompetenzaEntrataAnno1(BigDecimal competenzaEntrataAnno1) {
		this.competenzaEntrataAnno1 = competenzaEntrataAnno1;
	}

	/**
	 * @return the competenzaEntrataAnno2
	 */
	public BigDecimal getCompetenzaEntrataAnno2() {
		return competenzaEntrataAnno2;
	}

	/**
	 * @param competenzaEntrataAnno2 the competenzaEntrataAnno2 to set
	 */
	public void setCompetenzaEntrataAnno2(BigDecimal competenzaEntrataAnno2) {
		this.competenzaEntrataAnno2 = competenzaEntrataAnno2;
	}

	/**
	 * @return the competenzaEntrataAnnoSucc
	 */
	public BigDecimal getCompetenzaEntrataAnnoSucc() {
		return competenzaEntrataAnnoSucc;
	}

	/**
	 * @param competenzaEntrataAnnoSucc the competenzaEntrataAnnoSucc to set
	 */
	public void setCompetenzaEntrataAnnoSucc(BigDecimal competenzaEntrataAnnoSucc) {
		this.competenzaEntrataAnnoSucc = competenzaEntrataAnnoSucc;
	}

	/**
	 * @return the competenzaEntrataAnnoPrecedente
	 */
	public BigDecimal getCompetenzaEntrataAnnoPrec() {
		return competenzaEntrataAnnoPrec;
	}

	/**
	 * @param competenzaEntrataAnnoPrecedente the competenzaEntrataAnnoPrecedente to set
	 */
	public void setCompetenzaEntrataAnnoPrec(BigDecimal competenzaEntrataAnnoPrecedente) {
		this.competenzaEntrataAnnoPrec = competenzaEntrataAnnoPrecedente;
	}

	/**
	 * @return the tipoProgetto
	 */
	public TipoProgetto getTipoProgetto() {
		return tipoProgetto;
	}

	/**
	 * @param tipoProgetto the tipoProgetto to set
	 */
	public void setTipoProgetto(TipoProgetto tipoProgetto) {
		this.tipoProgetto = tipoProgetto;
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
	 * @return the quadraturaEntrata
	 */
	public BigDecimal getQuadraturaEntrata() {
		return quadraturaEntrata;
	}

	/**
	 * @param quadraturaEntrata the quadraturaEntrata to set
	 */
	public void setQuadraturaEntrata(BigDecimal quadraturaEntrata) {
		this.quadraturaEntrata = quadraturaEntrata;
	}

	/**
	 * @return the quadraturaUscita
	 */
	public BigDecimal getQuadraturaUscita() {
		return quadraturaUscita;
	}

	/**
	 * @param quadraturaUscita the quadraturaUscita to set
	 */
	public void setQuadraturaUscita(BigDecimal quadraturaUscita) {
		this.quadraturaUscita = quadraturaUscita;
	}

	/**
	 * @return la differenza tra la quadratura di entrata e la quadratura di spesa
	 */
	public BigDecimal getQuadraturaDifferenza() {
		return quadraturaEntrata.subtract(quadraturaUscita);
	}
	
	/**
	 * @return <code>true</code> se la quadratura &eacute; corretta; <code>false</code> in caso contrario
	 */
	public boolean getQuadraturaCorretta() {
		return BigDecimal.ZERO.compareTo(getQuadraturaDifferenza()) == 0;
	}

	/**
	 * @return the statoOperativoCronoprogramma
	 */
	public String getStatoOperativoCronoprogramma() {
		return statoOperativoCronoprogramma;
	}

	/**
	 * @param statoOperativoCronoprogramma the statoOperativoCronoprogramma to set
	 */
	public void setStatoOperativoCronoprogramma(String statoOperativoCronoprogramma) {
		this.statoOperativoCronoprogramma = statoOperativoCronoprogramma;
	}
	
	/**
	 * @return the descrizionestatoOperativoCronoptogramma
	 */
	public String getDescrizioneStatoOperativoCronoprogramma() {
		return descrizioneStatoOperativoCronoprogramma;
	}

	/**
	 * @param descrizionestatoOperativoCronoptogramma the descrizionestatoOperativoCronoptogramma to set
	 */
	public void setDescrizioneStatoOperativoCronoprogramma(String descrizionestatoOperativoCronoptogramma) {
		this.descrizioneStatoOperativoCronoprogramma = descrizionestatoOperativoCronoptogramma;
	}

	/**
	 * @return the usatoPerFpv
	 */
	public Boolean getUsatoPerFpv() {
		return usatoPerFpv;
	}

	/**
	 * @param usatoPerFpv the usatoPerFpv to set
	 */
	public void setUsatoPerFpv(Boolean usatoPerFpv) {
		this.usatoPerFpv = usatoPerFpv;
	}
	
	/**
	 * @return the usatoPerFpv
	 */
	public Boolean getUsatoPerFpvProv() {
		return usatoPerFpvProv;
	}

	/**
	 * @param usatoPerFpvProv the usatoPerFpvProv to set
	 */
	public void setUsatoPerFpvProv(Boolean usatoPerFpvProv) {
		this.usatoPerFpvProv = usatoPerFpvProv;
	}


	/**
	 * @return the daDefinire
	 */
	public Boolean getDaDefinire() {
		return daDefinire;
	}

	/**
	 * @param daDefinire the daDefinire to set
	 */
	public void setDaDefinire(Boolean daDefinire) {
		this.daDefinire = daDefinire;
	}

	/**
	 * Controlla se il progetto &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del progetto &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoValido() {
		return StatoOperativoCronoprogramma.VALIDO.name().equalsIgnoreCase(statoOperativoCronoprogramma);
	}
	
	/**
	 * Controlla se il progetto &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del progetto &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoAnnullato() {
		return StatoOperativoCronoprogramma.ANNULLATO.name().equalsIgnoreCase(statoOperativoCronoprogramma);
	}
	
	/**
	 * Gets the versione constato crono programma.
	 *
	 * @return the versione constato crono programma
	 */
	public String getVersioneConStatoCronoProgramma() {
		if(StringUtils.isBlank(getDescrizioneStatoOperativoCronoprogramma())) {
			return getVersione();
		}
		StringBuilder sb = new StringBuilder()
				.append("<a data-original-title=\"Informazioni\" data-html=\"true\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//				.append("<p> <span class='test'>waa</span></p>")
				.append("<p> <b>Tipo:&nbsp;</b> <span class='")
				.append(getClasseCssByCronoprogramma())
				.append("'>")
				.append(tipoProgetto.getCodice())
				.append("</span></p>")
				.append("<p> <b>Descrizione:&nbsp;</b>")
				.append(StringUtils.defaultString(getDescrizione(),""))
				.append("</p>")
				.append("\">")
				.append(versione)
				.append("</a>");
		
		return sb.toString();
	}
	private String getClasseCssByCronoprogramma() { //usatoPerFpv = Boolean.TRUE;
		if(isCronoprogrammaValidato()){
			return "green-text";
		}
		
		
		return isCronoprogrammaInAggiornamento()? "yellow-text" : "";
	}

	/**
	 * @return
	 */
	private boolean isCronoprogrammaInAggiornamento() {
		return StatoOperativoCronoprogramma.PROVVISORIO.getDescrizione().equals(descrizioneStatoOperativoCronoprogramma);
	}

	/**
	 * @return
	 */
	private boolean isCronoprogrammaValidato() {
		return (isCollegatoAProgettoGestione() && StatoOperativoCronoprogramma.VALIDO.getDescrizione().equals(descrizioneStatoOperativoCronoprogramma))
				|| (TipoProgetto.PREVISIONE.equals(tipoProgetto) && Boolean.TRUE.equals(usatoPerFpv));
	}
	
	/**
	 * Checks if is collegato A progetto gestione.
	 *
	 * @return true, if is collegato A progetto gestione
	 */
	public boolean isCollegatoAProgettoGestione() {
		return tipoProgetto != null && TipoProgetto.GESTIONE.equals(tipoProgetto);
	}
	
	/**
	 * Gets the html PFV.
	 *
	 * @return the html PFV
	 */
	public String getHtmlFPV() {
		if(Boolean.TRUE.equals(getUsatoPerFpv())) {
			return "<a href=\"#\" rel=\"popover\" data-content=\"Questo Cronoprogramma &eacute; stato utilizzato per il calcolo FPV e non &eacute; pi&uacute; modificabile\" data-html=\"true\" data-title=\"Usato per calcolo FPV\" data-trigger=\"hover\"><span id=\"purple\" class=\"icon-check-sign icon-large\"></span></a>";
		}
		if(Boolean.TRUE.equals(getUsatoPerFpvProv())) {
			return "<a href=\"#\" rel=\"popover\" data-content=\"Questo Cronoprogramma &eacute; stato utilizzato per simulare il calcolo FPV\" data-html=\"true\" data-title=\"Usato per calcolo FPV\" data-trigger=\"hover\"><span id=\"orange\" class=\"icon-check-sign icon-large\"></span></a>";
		}
		return "";
	}
	
	

}
