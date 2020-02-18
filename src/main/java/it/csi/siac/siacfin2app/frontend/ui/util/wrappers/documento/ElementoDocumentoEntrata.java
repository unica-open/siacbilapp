/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * Classe di wrap per il Documento di entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 *
 */
public class ElementoDocumentoEntrata extends ElementoDocumento {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3473987446232495758L;
	
	private DocumentoEntrata documentoEntrata;
	
	/** Costruttore vuoto di default */
	public ElementoDocumentoEntrata() {
		super();
	}

	/**
	 * @return the documentoEntrata
	 */
	public DocumentoEntrata getDocumentoEntrata() {
		return documentoEntrata;
	}

	/**
	 * @param documentoEntrata the documentoEntrata to set
	 */
	public void setDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		this.documentoEntrata = documentoEntrata;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoDocumentoEntrata)) {
			return false;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.appendSuper(super.hashCode())
			.append("ElementoDocumentoEntrata")
			.toHashCode();
	}

}
