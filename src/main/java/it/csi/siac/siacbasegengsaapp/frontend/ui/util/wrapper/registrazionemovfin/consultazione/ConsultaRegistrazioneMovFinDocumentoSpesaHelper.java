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
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Helper per la consultazione dei dati del dcumento di spesa
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinDocumentoSpesaHelper extends ConsultaRegistrazioneMovFinDocumentoHelper<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	
	private final List<DettaglioOnere> listaOneri;
	private final BigDecimal totaleRitenute;
	
	/**
	 * Costruttore di wrap
	 * @param documento il documento
	 * @param listaQuote la lista delle quote
	 * @param listaOneri la lista degli oneri
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinDocumentoSpesaHelper(DocumentoSpesa documento, List<SubdocumentoSpesa> listaQuote, List<DettaglioOnere> listaOneri, boolean isGestioneUEB) {
		super(documento, listaQuote, isGestioneUEB);
		this.listaOneri = listaOneri;
		
		this.totaleRitenute = initTotaleRitenute();
	}

	/**
	 * @return the listaOneri
	 */
	public List<DettaglioOnere> getListaOneri() {
		return this.listaOneri;
	}

	/**
	 * @return the totaleRitenute
	 */
	public BigDecimal getTotaleRitenute() {
		return this.totaleRitenute;
	}

	@Override
	protected BigDecimal initTotaleImportoDocumentiCollegati() {
		return calcolaTotaleDocumentiDiEntrata();
	}

	@Override
	public BigDecimal getTotaleNote() {
		BigDecimal totale = BigDecimal.ZERO;
		for(DocumentoSpesa ds : documento.getListaDocumentiSpesaFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(ds.getTipoDocumento().getCodiceGruppo())
				&& !StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento()) ) {
				totale = totale.add(defaultImporto(ds.getImportoDaDedurreSuFattura()));
			}
		}
		return totale;
	}
	/**
	 * Inizializza il totale delle ritenute
	 * @return il totale delle ritenute
	 */
	private BigDecimal initTotaleRitenute() {
		if(documento.getRitenuteDocumento() == null) {
			return BigDecimal.ZERO;
		}
		return defaultImporto(documento.getRitenuteDocumento().getImportoEsente())
			.add(defaultImporto(documento.getRitenuteDocumento().getImportoRivalsa()))
			.add(defaultImporto(documento.getRitenuteDocumento().getImportoCassaPensioni()))
			.add(defaultImporto(documento.getRitenuteDocumento().getImportoIVA()));
	}

	@Override
	protected boolean initDocCollegato() {
		return false;
	}

	@Override
	protected ElementoMovimentoIva<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa> istanziaWrapperMovimentoIva(SubdocumentoIvaSpesa subdocumentoIva, AliquotaSubdocumentoIva aliquotaIva) {
		return new ElementoMovimentoIva<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa>(subdocumentoIva, aliquotaIva);
	}

	@Override
	protected boolean isCodiceNotaCredito() {
		return BilConstants.CODICE_NOTE_CREDITO.getConstant().equals(documento.getTipoDocumento().getCodice());
	}

}
