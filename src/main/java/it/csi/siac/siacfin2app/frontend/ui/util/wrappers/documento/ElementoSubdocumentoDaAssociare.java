/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per il Subdocumento Da Associare all'allegato atto.
 */
public class ElementoSubdocumentoDaAssociare implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	
	private Integer uid;
	private String documentoPadre;
	private String descrizioneDocumentoPadre;
	private String codiceStato;
	private String descrizioneStato;
	private String soggetto;
	private String numero;
	private String movimento;
	private String ivaSplitReverse;
	private String note;
	private String dettagliNote;
	private Date dataEmissione;
	private BigDecimal importo;
	private BigDecimal importoDaDedurre;
	private BigDecimal importoIvaSplitReverse;
	private String famiglia;
	//SIAC-4440
	private boolean associatoAMovgestResiduo;
	
	// SIAC-5949
	private String provvisorioCassa;
	
	/** Costruttore vuoto di default */
	public ElementoSubdocumentoDaAssociare() {
		super();
	}
	
	@Override
	public int getUid() {
		return uid != null ? uid : 0;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	/**
	 * @return the documentoPadre
	 */
	public String getDocumentoPadre() {
		return documentoPadre;
	}
	/**
	 * @param documentoPadre the documentoPadre to set
	 */
	public void setDocumentoPadre(String documentoPadre) {
		this.documentoPadre = documentoPadre;
	}
	
	/**
	 * @return the descrizioneDocumentoPadre
	 */
	public String getDescrizioneDocumentoPadre() {
		return descrizioneDocumentoPadre;
	}

	/**
	 * @param descrizioneDocumentoPadre the descrizioneDocumentoPadre to set
	 */
	public void setDescrizioneDocumentoPadre(String descrizioneDocumentoPadre) {
		this.descrizioneDocumentoPadre = descrizioneDocumentoPadre;
	}

	/**
	 * @return the codiceStato
	 */
	public String getCodiceStato() {
		return codiceStato;
	}
	/**
	 * @param codiceStato the codiceStato to set
	 */
	public void setCodiceStato(String codiceStato) {
		this.codiceStato = codiceStato;
	}
	/**
	 * @return the descrizioneStato
	 */
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	/**
	 * @param descrizioneStato the descrizioneStato to set
	 */
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
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
	 * @return the movimento
	 */
	public String getMovimento() {
		return movimento;
	}
	/**
	 * @param movimento the movimento to set
	 */
	public void setMovimento(String movimento) {
		this.movimento = movimento;
	}
	/**
	 * @return the ivaSplitReverse
	 */
	public String getIvaSplitReverse() {
		return ivaSplitReverse;
	}
	/**
	 * @param ivaSplitReverse the ivaSplitReverse to set
	 */
	public void setIvaSplitReverse(String ivaSplitReverse) {
		this.ivaSplitReverse = ivaSplitReverse;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the dettagliNote
	 */
	public String getDettagliNote() {
		return dettagliNote;
	}
	/**
	 * @param dettagliNote the dettagliNote to set
	 */
	public void setDettagliNote(String dettagliNote) {
		this.dettagliNote = dettagliNote;
	}
	/**
	 * @return the dataEmissione
	 */
	public Date getDataEmissione() {
		return dataEmissione != null ? new Date(dataEmissione.getTime()) : null;
	}
	/**
	 * @param dataEmissione the dataEmissione to set
	 */
	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione != null ? new Date(dataEmissione.getTime()) : null;
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
	 * @return the importoDaDedurre
	 */
	public BigDecimal getImportoDaDedurre() {
		return importoDaDedurre;
	}

	/**
	 * @param importoDaDedurre the importoDaDedurre to set
	 */
	public void setImportoDaDedurre(BigDecimal importoDaDedurre) {
		this.importoDaDedurre = importoDaDedurre;
	}

	/**
	 * @return the importoIvaSplitReverse
	 */
	public BigDecimal getImportoIvaSplitReverse() {
		return importoIvaSplitReverse;
	}

	/**
	 * @param importoIvaSplitReverse the importoIvaSplitReverse to set
	 */
	public void setImportoIvaSplitReverse(BigDecimal importoIvaSplitReverse) {
		this.importoIvaSplitReverse = importoIvaSplitReverse;
	}

	/**
	 * @return the famiglia
	 */
	public String getFamiglia() {
		return famiglia;
	}

	/**
	 * @param famiglia the famiglia to set
	 */
	public void setFamiglia(String famiglia) {
		this.famiglia = famiglia;
	}

	/**
	 * @return the associatoAMovgestResiduo
	 */
	public boolean isAssociatoAMovgestResiduo() {
		return associatoAMovgestResiduo;
	}

	/**
	 * @param associatoAMovgestResiduo the associatoAMovgestResiduo to set
	 */
	public void setAssociatoAMovgestResiduo(boolean associatoAMovgestResiduo) {
		this.associatoAMovgestResiduo = associatoAMovgestResiduo;
	}

	/**
	 * @return the provvisorioCassa
	 */
	public String getProvvisorioCassa() {
		return this.provvisorioCassa;
	}

	/**
	 * @param provvisorioCassa the provvisorioCassa to set
	 */
	public void setProvvisorioCassa(String provvisorioCassa) {
		this.provvisorioCassa = provvisorioCassa;
	}


}
