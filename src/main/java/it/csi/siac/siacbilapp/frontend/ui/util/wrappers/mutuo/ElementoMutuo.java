/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.mutuo;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

public class ElementoMutuo implements Serializable, ModelWrapper {

	private static final long serialVersionUID = -3670882370902349245L;
	
	
	private int uid;
	private String numero;
	private String tipoTasso;
	private String tassoInteresse;
	private String euribor;
	private String spread;
	private String provvedimento;
	private String tipo;
	private String strutturaAmministrativa;
	private String soggetto;
	private String sommaMutuataIniziale;
	private String codiceStatoMutuo;
	private String descrizioneStatoMutuo;
	private String periodoRimborso;

	public String getDescrizioneStatoMutuo() {
		return descrizioneStatoMutuo;
	}

	public void setDescrizioneStatoMutuo(String descrizioneStatoMutuo) {
		this.descrizioneStatoMutuo = descrizioneStatoMutuo;
	}

	public String getPeriodoRimborso() {
		return periodoRimborso;
	}

	public void setPeriodoRimborso(String periodoRimborso) {
		this.periodoRimborso = periodoRimborso;
	}

	private String azioni;

	/** Costruttore vuoto di default */
	public ElementoMutuo() {
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipoTasso() {
		return tipoTasso;
	}

	public void setTipoTasso(String tipoTasso) {
		this.tipoTasso = tipoTasso;
	}

	public String getTassoInteresse() {
		return tassoInteresse;
	}

	public void setTassoInteresse(String tassoInteresse) {
		this.tassoInteresse = tassoInteresse;
	}

	public String getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(String provvedimento) {
		this.provvedimento = provvedimento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getStrutturaAmministrativa() {
		return strutturaAmministrativa;
	}

	public void setStrutturaAmministrativa(String strutturaAmministrativa) {
		this.strutturaAmministrativa = strutturaAmministrativa;
	}

	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	public String getSommaMutuataIniziale() {
		return sommaMutuataIniziale;
	}

	public void setSommaMutuataIniziale(String sommaMutuataIniziale) {
		this.sommaMutuataIniziale = sommaMutuataIniziale;
	}
	
	public String getEuribor() {
		return euribor;
	}

	public void setEuribor(String euribor) {
		this.euribor = euribor;
	}

	public String getSpread() {
		return spread;
	}

	public void setSpread(String spread) {
		this.spread = spread;
	}

	public String getCodiceStatoMutuo() {
		return codiceStatoMutuo;
	}

	public void setCodiceStatoMutuo(String codiceStatoMutuo) {
		this.codiceStatoMutuo = codiceStatoMutuo;
	}


}
