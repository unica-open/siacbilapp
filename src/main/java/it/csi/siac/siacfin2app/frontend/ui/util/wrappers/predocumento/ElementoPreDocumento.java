/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.predocumento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Classe di wrap per il PreDocumento.

 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 24/04/2014
 *
 */
public class ElementoPreDocumento implements Serializable, ModelWrapper, Comparable<ElementoPreDocumento> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 960385277435963135L;
	
	private Integer uid;
	private String numero;
	private String descrizione;
	private Causale causale;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private ContoTesoreria contoTesoreria;
	private ContoCorrentePredocumentoEntrata contoCorrente;
	private Date dataCompetenza;
	private StatoOperativoDocumento statoOperativoDocumento;
	private String documento;
	private String soggetto;
	private StatoOperativoPreDocumento statoOperativoPreDocumento;
	private Date dataDocumento;
	private BigDecimal importo = BigDecimal.ZERO;
	private String azioni;
	//SIAC-4671
	private String ragioneSocialeCognomeNome;
	
	/**
	 * @return the uid
	 */
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
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
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
	 * @return the causale
	 */
	public Causale getCausale() {
		return causale;
	}
	/**
	 * @param causale the causale to set
	 */
	public void setCausale(Causale causale) {
		this.causale = causale;
	}
	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}
	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(
			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}
	/**
	 * @return the contoTesoreria
	 */
	public ContoTesoreria getContoTesoreria() {
		return contoTesoreria;
	}
	/**
	 * @param contoTesoreria the contoTesoreria to set
	 */
	public void setContoTesoreria(ContoTesoreria contoTesoreria) {
		this.contoTesoreria = contoTesoreria;
	}
	/**
	 * @return the contoCorrente
	 */
	public ContoCorrentePredocumentoEntrata getContoCorrente() {
		return contoCorrente;
	}
	/**
	 * @param contoCorrente the contoCorrente to set
	 */
	public void setContoCorrente(ContoCorrentePredocumentoEntrata contoCorrente) {
		this.contoCorrente = contoCorrente;
	}
	/**
	 * @return the dataCompetenza
	 */
	public Date getDataCompetenza() {
		if(dataCompetenza == null) {
			return null;
		}
		return new Date(dataCompetenza.getTime());
	}
	/**
	 * @param dataCompetenza the dataCompetenza to set
	 */
	public void setDataCompetenza(Date dataCompetenza) {
		if(dataCompetenza != null) {
			this.dataCompetenza = new Date(dataCompetenza.getTime());
		} else {
			this.dataCompetenza = null;
		}
	}
	/**
	 * @return the statoOperativoDocumento
	 */
	public StatoOperativoDocumento getStatoOperativoDocumento() {
		return statoOperativoDocumento;
	}
	/**
	 * @param statoOperativoDocumento the statoOperativoDocumento to set
	 */
	public void setStatoOperativoDocumento(
			StatoOperativoDocumento statoOperativoDocumento) {
		this.statoOperativoDocumento = statoOperativoDocumento;
	}
	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	/**
	 * @return the soggetto
	 */
	public String getSoggetto() {
		return soggetto;
	}
	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}
	/**
	 * @return the statoOperativoPreDocumento
	 */
	public StatoOperativoPreDocumento getStatoOperativoPreDocumento() {
		return statoOperativoPreDocumento;
	}
	/**
	 * @param statoOperativoPreDocumento the statoOperativoPreDocumento to set
	 */
	public void setStatoOperativoPreDocumento(
			StatoOperativoPreDocumento statoOperativoPreDocumento) {
		this.statoOperativoPreDocumento = statoOperativoPreDocumento;
	}
	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		if(dataDocumento == null) {
			return null;
		}
		return new Date(dataDocumento.getTime());
	}
	/**
	 * @param dataDocumento the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		if(dataDocumento != null) {
			this.dataDocumento = new Date(dataDocumento.getTime());
		} else {
			this.dataDocumento = null;
		}
	}
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
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
	 * @return the ragioneSocialeCognomeNome
	 */
	public String getRagioneSocialeCognomeNome() {
		return ragioneSocialeCognomeNome != null? ragioneSocialeCognomeNome : "";
	}
	/**
	 * @param ragioneSocialeCognomeNome the ragioneSocialeCognomeNome to set
	 */
	public void setRagioneSocialeCognomeNome(String ragioneSocialeCognomeNome) {
		this.ragioneSocialeCognomeNome = ragioneSocialeCognomeNome;
	}
	@Override
	public int compareTo(ElementoPreDocumento o) {
		return new CompareToBuilder().append(this.numero, o.numero).toComparison();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof ElementoPreDocumento)) {
			return false;
		}
		ElementoPreDocumento other = (ElementoPreDocumento)obj;
		return new EqualsBuilder().append(this.numero, other.numero).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(numero).toHashCode();
	}
	
	/**
	 * Controlla che lo stato operativo sia annullato.
	 * 
	 * @return <code>true</code> se lo stato operativo &eacute; ANNULLATO; <code>false</code> in caso contrario
	 */
	public boolean isStatoOperativoAnnullato() {
		return StatoOperativoPreDocumento.ANNULLATO.equals(getStatoOperativoPreDocumento());
	}
	
	/**
	 * Controlla che lo stato operativo sia definito.
	 * 
	 * @return <code>true</code> se lo stato operativo &eacute; DEFINITO; <code>false</code> in caso contrario
	 */
	public boolean isStatoOperativoDefinito() {
		return StatoOperativoPreDocumento.DEFINITO.equals(getStatoOperativoPreDocumento());
	}
	
}
