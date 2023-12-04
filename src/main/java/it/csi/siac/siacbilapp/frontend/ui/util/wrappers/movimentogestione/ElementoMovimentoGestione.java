/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

public abstract class ElementoMovimentoGestione implements Serializable, ModelWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7419181915728411361L;

	private int uid;
	private String anno;
	private String numero;
	private String stato;
	private String capitolo;
	private String importo;
	private String altriMutui;
	private String annoBilancio;
	private String importoIniziale;
	private String importoAttuale;
	private String provvedimento;
	private String soggetto;
	private String strutturaAmministrativa;
	private String tipoAtto;
	private String tipoFinanziamento;
	private String componenteBilancio;

	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getImporto() {
		return importo;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public String getAltriMutui() {
		return altriMutui;
	}

	public void setAltriMutui(String altriMutui) {
		this.altriMutui = altriMutui;
	}

	public String getAnnoBilancio() {
		return annoBilancio;
	}

	public void setAnnoBilancio(String annoBilancio) {
		this.annoBilancio = annoBilancio;
	}

	public String getImportoIniziale() {
		return importoIniziale;
	}

	public void setImportoIniziale(String importoIniziale) {
		this.importoIniziale = importoIniziale;
	}

	public String getImportoAttuale() {
		return importoAttuale;
	}

	public void setImportoAttuale(String importoAttuale) {
		this.importoAttuale = importoAttuale;
	}

	public String getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(String provvedimento) {
		this.provvedimento = provvedimento;
	}

	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	public String getStrutturaAmministrativa() {
		return strutturaAmministrativa;
	}

	public void setStrutturaAmministrativa(String strutturaAmministrativa) {
		this.strutturaAmministrativa = strutturaAmministrativa;
	}

	public String getTipoAtto() {
		return tipoAtto;
	}

	public void setTipoAtto(String tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	public String getTipoFinanziamento() {
		return tipoFinanziamento;
	}

	public void setTipoFinanziamento(String tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}

	public String getComponenteBilancio() {
		return componenteBilancio;
	}

	public void setComponenteBilancio(String componenteBilancio) {
		this.componenteBilancio = componenteBilancio;
	}

}
