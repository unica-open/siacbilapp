/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Wrapper per l'Elenco Documenti Allegato
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2014
 * 
 */
public class ElementoElencoDocumentiAllegatoSpesa extends ElementoElencoDocumentiAllegato<Impegno, SubImpegno, CapitoloUscitaGestione, DocumentoSpesa, SubdocumentoSpesa> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7651734923264061189L;
	
	/**
	 * Costruttore a partire dall'elenco, dai dati del soggetto e dal subdocumento.
	 * 
	 * @param elencoDocumentiAllegato l'elenco da wrappare
	 * @param datiSoggettoAllegato    i dati del soggetto
	 * @param isGestioneUEB           se la gestione delle UEB sia attiva
	 * @param subdocumentoSpesa       il subdocumento
	 */
	public ElementoElencoDocumentiAllegatoSpesa(ElencoDocumentiAllegato elencoDocumentiAllegato, DatiSoggettoAllegato datiSoggettoAllegato, Boolean isGestioneUEB,
			SubdocumentoSpesa subdocumentoSpesa) {
		super(elencoDocumentiAllegato,
				subdocumentoSpesa,
				subdocumentoSpesa.getDocumento(),
				subdocumentoSpesa.getImpegno(),
				subdocumentoSpesa.getSubImpegno(),
				datiSoggettoAllegato,
				subdocumentoSpesa.getAttoAmministrativo(),
				subdocumentoSpesa.getTipoIvaSplitReverse() != null ? subdocumentoSpesa.getTipoIvaSplitReverse().getDescrizione() : "",
				subdocumentoSpesa.getImportoSplitReverseNotNull(),
				CODICE_TIPO_DOCUMENTO_SPESA,
				CODICE_TIPO_MOVIMENTO_GESTIONE_SPESA,
				isGestioneUEB);
	}
	
	@Override
	protected CapitoloUscitaGestione ottieniDatiCapitolo() {
		if(subdocumento.getImpegno() != null) {
			return subdocumento.getImpegno().getCapitoloUscitaGestione();
		}
		return null;
	}
	
	@Override
	protected String[] ottieniDatiNote() {
		List<String> dettaglio = new ArrayList<String>();
		String asterisk = "";
		
		if(Boolean.TRUE.equals(subdocumento.getHasRitenuteDiverseSplit())){
			dettaglio.add("RIT");
			asterisk = "*";
		}
		// In esclusione
		if(Boolean.TRUE.equals(subdocumento.getHasSubordinatiPNL())) {
			dettaglio.add("CON PNL");
			asterisk = "*";
		} else if(Boolean.TRUE.equals(subdocumento.getHasSubordinati())){
			dettaglio.add("CON SUB");
			asterisk = "*";
		}
		
		if(Boolean.TRUE.equals(subdocumento.getIsSubordinato())){
			dettaglio.add("DOC PADRE: " + ottieniStringaDocCollegatoPadre());
			asterisk = "*";
		}
		if(subdocumento.getProvvisorioCassa() != null && subdocumento.getProvvisorioCassa().getNumero() != null && subdocumento.getProvvisorioCassa().getAnno() != null){
			dettaglio.add("PROVV. DI CASSA: " + ottieniStringaProvvisorio());
			asterisk = "*";
		}
		return new String[] {asterisk, StringUtils.join(dettaglio, " - ")};
	}
	
	@Override
	public String getDomStringLiquidazione() {
		if(subdocumento == null || subdocumento.getLiquidazione() == null) {
			return "";
		}
		return new StringBuilder()
				.append(subdocumento.getLiquidazione().getAnnoLiquidazione())
				.append("/")
				.append(subdocumento.getLiquidazione().getNumeroLiquidazione().toPlainString())
				.toString();
	}
	
	@Override
	public String getDomStringDistinta() {
		if(subdocumento == null || subdocumento.getLiquidazione() == null || subdocumento.getLiquidazione().getDistinta() == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a data-original-title=\"Info\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(subdocumento.getLiquidazione().getDistinta().getDescrizione()))
				.append("\">")
				.append(subdocumento.getLiquidazione().getDistinta().getCodice())
				.append("</a>")
				.toString();
	}
	
	@Override
	public String getDomStringContoTesoreria() {
		if(subdocumento == null || subdocumento.getContoTesoreria() == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a data-original-title=\"Info\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(subdocumento.getContoTesoreria().getDescrizione()))
				.append("\">")
				.append(subdocumento.getContoTesoreria().getCodice())
				.append("</a>")
				.toString();
	}
	
	@Override
	public String getDomStringModalitaPagamentoSoggetto() {
		if(subdocumento == null || subdocumento.getModalitaPagamentoSoggetto() == null || subdocumento.getModalitaPagamentoSoggetto().getModalitaAccreditoSoggetto() == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(subdocumento.getModalitaPagamentoSoggetto().getDescrizione()))
				.append("\">")
				.append(subdocumento.getModalitaPagamentoSoggetto().getModalitaAccreditoSoggetto().getCodice())
				.append("</a>")
				.toString();
	}
	
	@Override
	public String getImportoSpesa() {
		if(subdocumento == null) {
			return "-";
		}
		return FormatUtils.formatCurrency(subdocumento.getImportoDaPagare());
	}
	
	@Override
	public String getSubdocumentoCig() {
		if(subdocumento == null) {
			return "";
		}
		return subdocumento.getCig();
	}
	
	@Override
	public String getSubdocumentoCup() {
		if(subdocumento == null) {
			return "";
		}
		return subdocumento.getCup();
	}
	
	@Override
	public String getSubdocumentoNumeroMutuo() {
		if(subdocumento == null || subdocumento.getVoceMutuo() == null) {
			return "";
		}
		return subdocumento.getVoceMutuo().getNumeroMutuo();
	}
	
	@Override
	protected Date getDataFineValiditaDurc() {
		if(subdocumento == null){
			return null;
		}
		// Dai requisiti: In caso di cessione si deve considerare il DURC del ricevente (beneficiario amministrativo)
		if(subdocumento.getModalitaPagamentoSoggetto() != null && subdocumento.getModalitaPagamentoSoggetto().getSoggettoCessione()!= null && StringUtils.isNotBlank(subdocumento.getModalitaPagamentoSoggetto().getSoggettoCessione().getCodiceSoggetto())) {
			return subdocumento.getModalitaPagamentoSoggetto().getSoggettoCessione().getDataFineValiditaDurc();
		}
		return subdocumento.getDocumento() != null && subdocumento.getDocumento().getSoggetto() != null? 
				subdocumento.getDocumento().getSoggetto().getDataFineValiditaDurc() : null;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder()
			.append("SPESA")
			.appendSuper(super.hashCode());
		return hcb.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoElencoDocumentiAllegatoSpesa)) {
			return false;
		}
		return super.equals(obj);
	}
	
	@Override
	public int compareTo(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> other) {
		int comparison = super.compareTo(other);
		if(comparison != 0){
			return comparison;
		}
		if(other.documento instanceof DocumentoEntrata) {
			return -1;
			
		}
		return this.getUid() - other.getUid();
	}
	
	
	
}
