/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per l'inserimento della testata del documento (Iva) di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/07/2014
 *
 */
public class InserisciTestataDocumentoSpesaModel extends GenericDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7338758031964477388L;
	
	private Integer uidDocumento;
	
	/** Costruttore vuoto di default */
	public InserisciTestataDocumentoSpesaModel() {
		setTitolo("Inserimento Documenti iva di Spesa");
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

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public InserisceDocumentoSpesa creaRequestInserisceDocumentoSpesa() {
		InserisceDocumentoSpesa request = new InserisceDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoSpesa(creaDocumentoPerInserimento());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea un Documento di spesa per l'inserimento dello stesso.
	 * 
	 * @return il documento con i dati correttamente popolati
	 */
	private DocumentoSpesa creaDocumentoPerInserimento() {
		DocumentoSpesa documento = getDocumento();
		documento.setSoggetto(getSoggetto());
		documento.setEnte(getEnte());
		documento.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		// SIAC-5188
		documento.setTipoImpresa(impostaEntitaFacoltativa(documento.getTipoImpresa()));
		
		return documento;
	}

	/**
	 * Impostazione dei dati per l'esecuzione del metodo ripeti.
	 * 
	 * @param documento il documento precedentemente salvato
	 * @param soggetto  il soggetto precedentemento salvato
	 */
	public void impostaDatiRipeti(DocumentoSpesa documento, Soggetto soggetto) {
		setSoggetto(soggetto);
		
		// Pulisco i dati del documento
		documento.setAnno(null);
		documento.setNumero(null);
		documento.setDataEmissione(null);
		documento.setUid(0);
		setDocumento(documento);
	}

	/**
	 * Popola il model a partire dal Documento di spesa ottenuto dal serivizio.
	 * 
	 * @param documentoSpesa il documento tramite cui popolare il model
	 */
	public void popolaModel(DocumentoSpesa documentoSpesa) {
		setDocumento(documentoSpesa);
	}

}
