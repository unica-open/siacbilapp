/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per l'inserimento della testata del documento di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/07/2014
 *
 */
public class InserisciTestataDocumentoEntrataModel extends GenericDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1055324871786395570L;
	
	private DocumentoEntrata documento;
	private Integer uidDocumento;
	
	/** Costruttore vuoto di default */
	public InserisciTestataDocumentoEntrataModel() {
		setTitolo("Inserimento Documenti iva entrata");
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
	}

	/**
	 * @return the uidDocumento
	 */
	public Integer getUidDocumento() {
		return uidDocumento;
	}

	/**
	 * @param uidDocumento the uidDocumento to set
	 */
	public void setUidDocumento(Integer uidDocumento) {
		this.uidDocumento = uidDocumento;
	}

	/**
	 * Impostazione dei dati per l'esecuzione del metodo ripeti.
	 * 
	 * @param documento il documento precedentemente salvato
	 * @param soggetto  il soggetto precedentemento salvato
	 */
	public void impostaDatiRipeti(DocumentoEntrata documento, Soggetto soggetto) {
		setSoggetto(soggetto);
		
		// Pulisco i dati del documento
		documento.setAnno(null);
		documento.setNumero(null);
		documento.setDataEmissione(null);
		documento.setUid(0);
		setDocumento(documento);
	}

	/* Requests */
	/**
	 * Crea una request per il servizio di {@link InserisceDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public InserisceDocumentoEntrata creaRequestInserisceDocumentoEntrata() {
		InserisceDocumentoEntrata request = new InserisceDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoEntrata(creaDocumentoPerInserimento());
		request.setBilancio(getBilancio());
		
		return request;
	}

	/* Utility */
	/**
	 * Crea un Documento di entrata per l'inserimento dello stesso.
	 * 
	 * @return il documento con i dati correttamente popolati
	 */
	private DocumentoEntrata creaDocumentoPerInserimento() {
		DocumentoEntrata documentoEntrata = getDocumento();
		documentoEntrata.setEnte(getEnte());
		documentoEntrata.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		Soggetto soggetto = getSoggetto();
		
		if(soggetto != null && soggetto.getUid() != 0) {
			documentoEntrata.setSoggetto(soggetto);
		}
		
		return documentoEntrata;
	}
	
	/**
	 * Popola il model a partire dal Documento di entrata ottenuto dal servizio.
	 * 
	 * @param documentoEntrata il documento tramite cui popolare il model
	 */
	public void popolaModel(DocumentoEntrata documentoEntrata) {
		setDocumento(documentoEntrata);
	}
}
