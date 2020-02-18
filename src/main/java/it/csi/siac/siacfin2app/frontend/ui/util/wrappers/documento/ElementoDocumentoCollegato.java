/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per il Documento collegato.
 * 
 * @author Ahmad Nazha, Valentina Triolo
 *
 */
public class ElementoDocumentoCollegato implements Serializable, ModelWrapper{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3245825236798462375L;

	private Integer uid;
	private String tipo;
	private String documento;
	private String data;
	private String statoOperativoDocumentoCode;
	private String statoOperativoDocumentoDesc;
	private String soggetto;
	private String loginModifica;
	private String tipoDocumento;
	private BigDecimal importo = BigDecimal.ZERO;
	private BigDecimal importoDaDedurreSuFattura = BigDecimal.ZERO;
	
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
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the statoOperativoDocumentoCode
	 */
	public String getStatoOperativoDocumentoCode() {
		return statoOperativoDocumentoCode;
	}
	/**
	 * @param statoOperativoDocumentoCode the statoOperativoDocumentoCode to set
	 */
	public void setStatoOperativoDocumentoCode(String statoOperativoDocumentoCode) {
		this.statoOperativoDocumentoCode = statoOperativoDocumentoCode;
	}
	/**
	 * @return the statoOperativoDocumentoDesc
	 */
	public String getStatoOperativoDocumentoDesc() {
		return statoOperativoDocumentoDesc;
	}
	
	/**
	 * @param statoOperativoDocumentoDesc the statoOperativoDocumento
	 */
	public void setStatoOperativoDocumentoDesc(String statoOperativoDocumentoDesc) {
		this.statoOperativoDocumentoDesc = statoOperativoDocumentoDesc;
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
	 * @return the loginModifica
	 */
	public String getLoginModifica() {
		return loginModifica;
	}
	/**
	 * @param loginModifica the loginModifica to set
	 */
	public void setLoginModifica(String loginModifica) {
		this.loginModifica = loginModifica;
	}
	
	

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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
	 * @return the importoDaDedurreSuFattura
	 */
	public BigDecimal getImportoDaDedurreSuFattura() {
		return importoDaDedurreSuFattura;
	}

	/**
	 * @param importoDaDedurreSuFattura the importoDaDedurreSuFattura to set
	 */
	public void setImportoDaDedurreSuFattura(BigDecimal importoDaDedurreSuFattura) {
		this.importoDaDedurreSuFattura = importoDaDedurreSuFattura;
	}
	

}
