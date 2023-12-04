/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorSubdocumento;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Helper per la consultazione dei dati dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinRichiestaEconomaleHelper extends ConsultaRegistrazioneMovFinBaseHelper<RichiestaEconomale> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2676241836167738216L;

	private final RichiestaEconomale richiestaEconomale;
	private final DocumentoSpesa documento;
	/**
	 * Costruttore di wrap
	 * @param richiestaEconomale la richiesta economale
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinRichiestaEconomaleHelper(RichiestaEconomale richiestaEconomale, boolean isGestioneUEB) {
		super(isGestioneUEB);
		this.richiestaEconomale = richiestaEconomale;
		this.documento = retrieveDocumento();
	}
	/**
	 * @return the richiestaEconomale
	 */
	public RichiestaEconomale getRichiestaEconomale() {
		return this.richiestaEconomale;
	}
	/**
	 * @return the documento
	 */
	public DocumentoSpesa getDocumento() {
		return this.documento;
	}
	@Override
	public String getDatiCreazioneModifica() {
		return "";
	}
	/**
	 * @return the datiBaseCapitolo
	 */
	public String getDatiBaseCapitolo() {
		if(richiestaEconomale == null || richiestaEconomale.getImpegno() == null) {
			return "";
		}
		return calcolaDatiCapitolo(richiestaEconomale.getImpegno().getCapitoloUscitaGestione());
	}
	/**
	 * @return the numeroImpegno
	 */
	public String getNumeroImpegno() {
		return richiestaEconomale.getImpegno() != null ? FormatUtils.formatPlain(richiestaEconomale.getImpegno().getNumeroBigDecimal()) : "";
	}
	/**
	 * @return the subImpegnoPresente
	 */
	public boolean isSubImpegnoPresente() {
		return richiestaEconomale.getSubImpegno() != null && richiestaEconomale.getSubImpegno().getUid() != 0;
	}
	/**
	 * @return the numeroSubImpegno
	 */
	public String getNumeroSubImpegno() {
		return richiestaEconomale.getSubImpegno() != null ? FormatUtils.formatPlain(richiestaEconomale.getSubImpegno().getNumeroBigDecimal()) : "";
	}
	/**
	 * @return the rendicontoRichiestaPresente
	 */
	public boolean isRendicontoRichiestaPresente() {
		return richiestaEconomale.getRendicontoRichiesta() != null && richiestaEconomale.getRendicontoRichiesta().getUid() != 0;
	}
	/**
	 * @return the datiTipoDocumento
	 */
	public String getDatiTipoDocumento() {
		if(documento == null || documento.getTipoDocumento() == null) {
			return "";
		}
		return new StringBuilder()
			.append(documento.getTipoDocumento().getCodice())
			.append(" - ")
			.append(documento.getTipoDocumento().getDescrizione())
			.toString();
	}
	/**
	 * @return the datiDocumento
	 */
	public String getDatiDocumento() {
		if(documento == null) {
			return "";
		}
		return new StringBuilder()
			.append(documento.getAnno())
			.append(" - ")
			.append(documento.getNumero())
			.toString();
	}
	/**
	 * @return the datiSoggettoDocumento
	 */
	public String getDatiSoggettoDocumento() {
		if(documento == null || documento.getSoggetto() == null) {
			return "";
		}
		return new StringBuilder()
			.append(documento.getSoggetto().getCodiceSoggetto())
			.append(" - ")
			.append(documento.getSoggetto().getDenominazione())
			.toString();
	}
	/**
	 * @return the quotePagateDocumento
	 */
	public String getQuotePagateDocumento() {
		if(richiestaEconomale == null || richiestaEconomale.getSubdocumenti() == null || !isPagamentoFatture()) {
			return "";
		}
		
		List<String> chunks = new ArrayList<String>();
		
		// Ordino i subdoc
		Collections.sort(richiestaEconomale.getSubdocumenti(), ComparatorSubdocumento.INSTANCE);
		StringBuilder sb = new StringBuilder();
		for (SubdocumentoSpesa s : richiestaEconomale.getSubdocumenti()) {
			sb.setLength(0);
			
			sb.append(s.getNumero())
				.append(" - ")
				.append(Boolean.TRUE.equals(s.getFlagRilevanteIVA()) ?" Importo non Rilevante IVA: " :" Importo Rilevante IVA: ")
				.append(FormatUtils.formatCurrency(s.getImporto()));

			chunks.add(sb.toString());
		}
		
		return StringUtils.join(chunks, "<br/>");
	}
	
	/**
	 * @return the pagamentoFatture
	 */
	private boolean isPagamentoFatture() {
		return richiestaEconomale != null
				&& richiestaEconomale.getTipoRichiestaEconomale() != null
				&& BilConstants.CODICE_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO_FATTURE.getConstant().equals(richiestaEconomale.getTipoRichiestaEconomale().getCodice());
	}
	
	/**
	 * Popolamento del documento: effettuato una volta sola.
	 */
	private DocumentoSpesa retrieveDocumento() {
		if(richiestaEconomale == null || richiestaEconomale.getSubdocumenti() == null || richiestaEconomale.getSubdocumenti().isEmpty() || !isPagamentoFatture()) {
			return null;
		}
		// Ho una lista di subdocumenti --> prendo una e popolo il documento
		return richiestaEconomale.getSubdocumenti().get(0).getDocumento();
	}
	
}
