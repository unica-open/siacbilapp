/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva;

import java.util.Date;

import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * Wrapper per il SubdocumentoIva.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 17/06/2014
 * 
 * @param <D>   il tipo del documento
 * @param <SD>  il tipo del subdocumento
 * @param <SDI> il tipo del subdocumento iva
 *
 */
public class ElementoDocumentoIva<D extends Documento<SD, SDI>, SD extends Subdocumento<D, SDI>, SDI extends SubdocumentoIva<D, SD, ?>>
		extends SubdocumentoIva<D, SD, SDI> implements ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5373837455635805301L;
	
	private String azioni;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param subdocumentoIva la superclasse
	 */
	public ElementoDocumentoIva(SubdocumentoIva<D, SD, SDI> subdocumentoIva) {
		ReflectionUtil.downcastByReflection(subdocumentoIva, this);
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
	 * @return la descrizione del documento collegato
	 */
	public String getDescrizioneDocumento() {
		Documento<?, ?> doc = getDocumentoCollegato();
		if(doc == null){
			return "";
		}
		return doc.getDescAnnoNumeroTipoDoc();
		
	}
	
	/**
	 * @return la data di emissione del documento collegato
	 */
	public Date getDataEmissioneDocumento() {
		Documento<?, ?> doc = getDocumentoCollegato();
		if(doc == null){
			return null;
		}
		return doc.getDataEmissione();
		
	}
	
	/**
	 * @return la descrizione del soggetto
	 */
	public String getDescrizioneSoggetto() {
		Documento<?, ?> doc = getDocumentoCollegato();
		if(doc == null || doc.getSoggetto() == null){
			return "";
		}
		
		return doc.getSoggetto().getCodiceSoggetto()  + "-" + doc.getSoggetto().getDenominazione();
		
	}
	

}
