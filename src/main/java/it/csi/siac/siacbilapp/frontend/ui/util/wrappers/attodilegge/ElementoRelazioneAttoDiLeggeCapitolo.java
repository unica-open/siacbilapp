/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.attodilegge;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per il la relazione tra Atto di Legge e Capitolo.
 * 
 * @author Luciano Gallo
 *
 */
public class ElementoRelazioneAttoDiLeggeCapitolo implements Serializable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = 372671294828403933L;

	private int uid;
	private String tipoAtto;
	private String anno;
	private String numero;
	private String articolo;
	private String comma;
	private String punto;
	private String gerarchia;
	private String descrizione;
	private String dataInizioFinanziamento;
	private String dataFineFinanziamento;
	private String azioni;
	private int uidAttoDiLegge;

	/** Costruttore vuoto di default */
	public ElementoRelazioneAttoDiLeggeCapitolo() {
		super();
	}

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
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the tipoAtto
	 */
	public String getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(String tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	/**
	 * @return the anno
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(String anno) {
		this.anno = anno;
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
	 * @return the gerarchia
	 */
	public String getGerarchia() {
		return gerarchia;
	}

	/**
	 * @param gerarchia the gerarchia to set
	 */
	public void setGerarchia(String gerarchia) {
		this.gerarchia = gerarchia;
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
	 * @return the dataInizioFinanziamento
	 */
	public String getDataInizioFinanziamento() {
		return dataInizioFinanziamento;
	}

	/**
	 * @param dataInizioFinanziamento the dataInizioFinanziamento to set
	 */
	public void setDataInizioFinanziamento(String dataInizioFinanziamento) {
		this.dataInizioFinanziamento = dataInizioFinanziamento;
	}

	/**
	 * @return the dataFineFinanziamento
	 */
	public String getDataFineFinanziamento() {
		return dataFineFinanziamento;
	}

	/**
	 * @param dataFineFinanziamento the dataFineFinanziamento to set
	 */
	public void setDataFineFinanziamento(String dataFineFinanziamento) {
		this.dataFineFinanziamento = dataFineFinanziamento;
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
	 * @return the uidAttoDiLegge
	 */
	public int getUidAttoDiLegge() {
		return uidAttoDiLegge;
	}

	/**
	 * @param uidAttoDiLegge the uidAttoDiLegge to set
	 */
	public void setUidAttoDiLegge(int uidAttoDiLegge) {
		this.uidAttoDiLegge = uidAttoDiLegge;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementoRelazioneAttoDiLeggeCapitolo)) {
			return false;
		}
		ElementoRelazioneAttoDiLeggeCapitolo other = (ElementoRelazioneAttoDiLeggeCapitolo) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder
				.append(this.uid, other.uid)
				.append(this.tipoAtto, other.tipoAtto)
				.append(this.anno, other.anno)
				.append(this.numero, other.descrizione)
				.append(this.articolo, articolo)
				.append(this.comma, other.comma)
				.append(this.punto, other.punto)
				.append(this.gerarchia, other.gerarchia)
				.append(this.descrizione, other.descrizione)
				.append(this.dataInizioFinanziamento, other.dataInizioFinanziamento)
				.append(this.dataFineFinanziamento, other.dataFineFinanziamento);
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(uid).append(tipoAtto).append(anno)
				.append(numero).append(articolo).append(comma).append(punto)
				.append(gerarchia).append(descrizione)
				.append(dataInizioFinanziamento).append(dataFineFinanziamento);
		return hashCodeBuilder.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.append("uid", uid).append("tipoAtto", tipoAtto)
				.append("anno", anno).append("numero", numero)
				.append("articolo", articolo).append("comma", comma)
				.append("punto", punto).append("gerarchi", gerarchia)
				.append("descrizione", descrizione)
				.append("data Inizio Finanziamento", dataInizioFinanziamento)
				.append("data Fine Finanziamento", dataFineFinanziamento);
		return toStringBuilder.toString();
	}

}
