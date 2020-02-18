/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Classe di wrap per il Documento di spesa.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 *
 */
public class ElementoDocumentoSpesa extends ElementoDocumento {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9112573740765713903L;
	
	//SIAC-5346 e SIAC-5617
	private static final Collection<String> TIPI_DOCUMENTO_IMPORTABILI_DA_FEL = Arrays.asList("FAT", "FPR", "NCD", "NTE");
	
	private DocumentoSpesa documentoSpesa;
	/** Costruttore vuoto di default */
	public ElementoDocumentoSpesa() {
		super();
	}
	/**
	 * @return the documentoSpesa
	 */
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}
	/**
	 * @param documentoSpesa the documentoSpesa to set
	 */
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}
	
	/**
	 * Checks if is importabile da FEL.
	 *
	 * @return true, if is importabile da FEL
	 */
	public boolean isImportabileDaFEL() {
		return TIPI_DOCUMENTO_IMPORTABILI_DA_FEL.contains(getTipoDocumentoCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoDocumentoSpesa)) {
			return false;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.appendSuper(super.hashCode())
			.append("ElementoDocumentoSpesa")
			.toHashCode();
	}

}
