/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva.ElementoMovimentoIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Helper per la consultazione dei dati del dcumento di entrata
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinDocumentoEntrataHelper extends ConsultaRegistrazioneMovFinDocumentoHelper<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	
	/**
	 * Costruttore di wrap
	 * @param documento il documento
	 * @param listaQuote la lista delle quote
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinDocumentoEntrataHelper(DocumentoEntrata documento, List<SubdocumentoEntrata> listaQuote, boolean isGestioneUEB) {
		super(documento, listaQuote, isGestioneUEB);
	}

	@Override
	protected BigDecimal initTotaleImportoDocumentiCollegati() {
		return calcolaTotaleDocumentiDiSpesa();
	}

	@Override
	public BigDecimal getTotaleNote() {
		BigDecimal totale = BigDecimal.ZERO;
		for(DocumentoEntrata de : documento.getListaDocumentiEntrataFiglio()) {
			if(de != null && de.getTipoDocumento() != null && BilConstants.CODICE_NOTE_ACCREDITO.getConstant().equalsIgnoreCase(de.getTipoDocumento().getCodiceGruppo())
				&& !StatoOperativoDocumento.ANNULLATO.equals(de.getStatoOperativoDocumento()) ) {
				totale = totale.add(de.getImportoDaDedurreSuFattura());
			}
		}
		return totale;
	}

	@Override
	protected boolean initDocCollegato() {
		return false;
	}

	@Override
	protected ElementoMovimentoIva<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata> istanziaWrapperMovimentoIva(SubdocumentoIvaEntrata subdocumentoIva, AliquotaSubdocumentoIva aliquotaIva) {
		return new ElementoMovimentoIva<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata>(subdocumentoIva, aliquotaIva);
	}
	
	@Override
	protected boolean isCodiceNotaCredito() {
		return BilConstants.CODICE_NOTE_ACCREDITO.getConstant().equals(documento.getTipoDocumento().getCodice());
	}

}
