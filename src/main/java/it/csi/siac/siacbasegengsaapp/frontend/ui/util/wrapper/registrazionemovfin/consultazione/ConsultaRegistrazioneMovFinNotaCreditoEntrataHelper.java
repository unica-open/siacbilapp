/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Helper per la consultazione dei dati della nota di credito di spesa
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper extends ConsultaRegistrazioneMovFinDocumentoEntrataHelper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -466647951631491868L;
	
	private final DocumentoEntrata documentoOriginale;
	
	/**
	 * Costruttore di wrap
	 * @param documento il documento
	 * @param documentoOriginale il documento originale
	 * @param listaQuote la lista delle quote
	 * @param listaIva la lista dei dati iva
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper(DocumentoEntrata documento, DocumentoEntrata documentoOriginale,
			List<SubdocumentoEntrata> listaQuote, List<SubdocumentoIvaEntrata> listaIva, boolean isGestioneUEB) {
		// Le quote sono quelle collegate
		super(documento, new ArrayList<SubdocumentoEntrata>(), isGestioneUEB);
		this.documentoOriginale = documentoOriginale;
		
		this.listaQuoteDocCollegato.addAll(listaQuote);
		impostaDatiIvaNellaListaWrapper(listaIva, listaMovimentiIvaDocCollegato);
		
	}

	/**
	 * @return the documentoOriginale
	 */
	public DocumentoEntrata getDocumentoOriginale() {
		return this.documentoOriginale;
	}

	/**
	 * @return the numeroDocumentoOriginale
	 */
	public String getNumeroDocumentoOriginale() {
		return documentoOriginale != null ? documentoOriginale.getNumero() : "";
	}
	
	/**
	 * @return the listaQuoteDaDedurre
	 */
	public List<SubdocumentoEntrata> getListaQuoteDaDedurre() {
		List<SubdocumentoEntrata> res = new ArrayList<SubdocumentoEntrata>();
		for(SubdocumentoEntrata se : getListaQuoteDocCollegato()) {
			if(se.getImportoDaDedurreNotNull().signum() != 0) {
				res.add(se);
			}
		}
		return res;
	}
	
	/**
	 * @return the totaleRilevanteIvaDocCollegato
	 */
	public BigDecimal getTotaleRilevanteIvaDocCollegato(){
		BigDecimal totale = BigDecimal.ZERO;
		for (SubdocumentoEntrata quota : getListaQuoteDocCollegato()) {
			if(quota != null && quota.getImporto() != null && Boolean.TRUE.equals(quota.getFlagRilevanteIVA())) {
				// TODO: importo da dedurre?
				totale = totale.add(quota.getImporto());
			}
		}
		return totale;
	}
	
	/**
	 * @return the totaleNonRilevanteIvaDocCollegato
	 */
	public BigDecimal getTotaleNonRilevanteIvaDocCollegato(){
		if(documentoOriginale == null || documentoOriginale.getImporto() == null){
			return BigDecimal.ZERO;
		}
		return documentoOriginale.getImporto().subtract(getTotaleRilevanteIvaDocCollegato());
	}
	
	@Override
	public String getDatiImporti() {
		return FormatUtils.formatCurrency(documento.getImporto());
	}
	
	@Override
	protected BigDecimal initTotaleImportoDocumentiCollegati() {
		return calcolaTotaleDocumentiDiEntrata();
	}

	@Override
	public BigDecimal getTotaleNote() {
		return BigDecimal.ZERO;
	}
	
	@Override
	protected boolean initDocCollegato() {
		return true;
	}

	@Override
	protected boolean isCodiceNotaCredito() {
		return true;
	}

}
