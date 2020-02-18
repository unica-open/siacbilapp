/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileWrapper;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaEntitaConsultabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di model base per la gestione dell'elenco delle entitaconsultabili
 * @author Elisa Chiari
 * @version 1.0.0 - 29/02/2016
 */
public class RicercaEntitaConsultabileBaseAjaxModel extends GenericBilancioModel {
	
	
	/** per la serializzazione */
	private static final long serialVersionUID = -8584918616876330797L;
	
	private String sEcho;
	private int iTotalRecords;
	private int iTotalDisplayRecords;
	private int iDisplayStart;
	private int iDisplayLength;
	private List<EntitaConsultabileWrapper> aaData = new ArrayList<EntitaConsultabileWrapper>();
	
	//Per il download excel
	private Boolean isXlsx;
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	
	/**
	 * @return the sEcho
	 */
	public String getsEcho() {
		return sEcho;
	}
	
	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	
	/**
	 * @return the iTotalRecords
	 */
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	
	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	
	/**
	 * @return the iTotalDisplayRecords
	 */
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	
	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	
	/**
	 * @return the iDisplayStart
	 */
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	
	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	
	/**
	 * @return the iDisplayLength
	 */
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	
	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	
	/**
	 * @return the aaData
	 */
	public List<EntitaConsultabileWrapper> getAaData() {
		return aaData;
	}
	
	/**
	 * @param aaData the aaData to set
	 */
	public void setAaData(List<EntitaConsultabileWrapper> aaData) {
		this.aaData = aaData;
	}
	
	/**
	 * @return the isXlsx
	 */
	public Boolean getIsXlsx() {
		return isXlsx;
	}

	/**
	 * @param isXlsx the isXlsx to set
	 */
	public void setIsXlsx(Boolean isXlsx) {
		this.isXlsx = isXlsx;
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

	@Override
	protected ParametriPaginazione creaParametriPaginazione() {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		
		int numeroPagina = getiDisplayStart() / ELEMENTI_PER_PAGINA_RICERCA;
		parametriPaginazione.setNumeroPagina(numeroPagina);
		
		return parametriPaginazione;
	}
	
	/**
	 * Crea una request per il servizio {@link RicercaSinteticaEntitaConsultabile}
	 * @param prec i parametri di ricerca
	 * @return la request creata
	 */
	protected RicercaSinteticaEntitaConsultabile creaRequestRicercaSinteticaEntitaConsultabile (ParametriRicercaEntitaConsultabile prec) {
		RicercaSinteticaEntitaConsultabile request = new RicercaSinteticaEntitaConsultabile();
		request.setParametriRicercaEntitaConsultabile(prec);
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
}
