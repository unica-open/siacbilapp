/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe;

import java.io.InputStream;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile.CriteriRicercaFile;

/**
 * Classe di model per i risultati di ricerca delle StampeCassaFile
 * 
 * @author Valentina Triolo
 * @version 1.0.0 - 01/04/2015
 *
 */
public class RisultatiRicercaStampeCassaEconomaleModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6655584591249518245L;
	
	private Integer savedDisplayStart;
	private Integer uidFile;
	
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	
	// SIAC-4799
	private TipoDocumento tipoDocumento;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaStampeCassaEconomaleModel() {
		super();
		setTitolo("Risultati di ricerca stampe cassa economale");
	}

	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the uidFile
	 */
	public Integer getUidFile() {
		return uidFile;
	}

	/**
	 * @param uidFile the uidFile to set
	 */
	public void setUidFile(Integer uidFile) {
		this.uidFile = uidFile;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the contentLength
	 */
	public Long getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	/**
	 * @return the tipoDocumentoRendiconto
	 */
	public boolean isTipoDocumentoRendiconto() {
		return TipoDocumento.RENDICONTO.equals(tipoDocumento);
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaFile}.
	 * 
	 * @return la request creata
	 */
	public RicercaFile creaRequestRicercaFile() {
		RicercaFile request = creaRequest(RicercaFile.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione(1));
		
		CriteriRicercaFile criteri = new CriteriRicercaFile();
		criteri.setUid(getUidFile());
		request.setCriteri(criteri);
		
		return request;
	}

}
