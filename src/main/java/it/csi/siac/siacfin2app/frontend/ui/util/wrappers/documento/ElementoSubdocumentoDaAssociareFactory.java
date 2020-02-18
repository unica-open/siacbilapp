/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Factory per il wrapping dei SubDocumenti da associare all'Allegato Atto.
 */
public final class ElementoSubdocumentoDaAssociareFactory extends BaseFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoSubdocumentoDaAssociareFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal Subdocumento.
	 * 
	 * @param s il subdocumento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoSubdocumentoDaAssociare getInstance(Subdocumento<?,?> s) {
		ElementoSubdocumentoDaAssociare result = new ElementoSubdocumentoDaAssociare();
		
		Integer uid = s.getUid();
		BigDecimal importo = s.getImportoNotNull();
		BigDecimal importoDaDedurre = s.getImportoDaDedurreNotNull();
		String numero = s.getNumero().toString();
		String codiceStato = s.getDocumento().getStatoOperativoDocumento() != null ? s.getDocumento().getStatoOperativoDocumento().getCodice() : "";
		String descrizioneStato = s.getDocumento().getStatoOperativoDocumento() != null ? s.getDocumento().getStatoOperativoDocumento().getDescrizione() : "";
		String descrizioneDocumentoPadre = s.getDocumento().getDescrizione();
		Date dataEmissione = s.getDocumento().getDataEmissione();
		
		
		String movimento = ottieniStringaMovimento(s);
		String dettagliNote = ottieniStringaDettagliNote(s);
		String documentoPadre = ottieniStringaDocumentoPadre(s.getDocumento());
		String ivaSplitReverse = ottieniStringaIva(s);
		String note = ottieniStringaNote(s);
		String soggetto = ottieniStringaSoggetto(s);
		String famiglia = ottieniStringaFamiglia(s.getDocumento());
		BigDecimal importoSplitReverse = ottieniImportoSplitReverse(s);
		//CR-4440
		boolean associatoAMovgestResiduo = isAssociatoAMovgestResiduo(s);

		String provvisorioCassa = ottieniStringaProvvisorioCassa(s.getProvvisorioCassa());

		
		result.setUid(uid);
		result.setImporto(importo);
		result.setImportoDaDedurre(importoDaDedurre);
		result.setNumero(numero);
		result.setMovimento(movimento);
		result.setDettagliNote(dettagliNote);
		result.setDocumentoPadre(documentoPadre);
		result.setDescrizioneDocumentoPadre(descrizioneDocumentoPadre);
		result.setIvaSplitReverse(ivaSplitReverse);
		result.setSoggetto(soggetto);
		result.setCodiceStato(codiceStato);
		result.setDescrizioneStato(descrizioneStato);
		result.setNote(note);
		result.setDataEmissione(dataEmissione);
		result.setFamiglia(famiglia);
		result.setImportoIvaSplitReverse(importoSplitReverse);
		result.setAssociatoAMovgestResiduo(associatoAMovgestResiduo);
		result.setProvvisorioCassa(provvisorioCassa);
		
		return result;
	}

	private static String ottieniStringaProvvisorioCassa(ProvvisorioDiCassa provvisorioCassa) {
	    return provvisorioCassa != null ? String.format("%d/%d", provvisorioCassa.getAnno(), provvisorioCassa.getNumero()) : "";
	}

	/**
	 * Controlla se il subdoc sia associato a un residuo
	 * @param s il subdoc da controllare
	 * @return se l'associazione sia a un movimento residuo
	 */
	private static boolean isAssociatoAMovgestResiduo(Subdocumento<?, ?> s) {
		int annoMovimento = 0;
		int annoBilancio = 0;
		if(s instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)s;
			Impegno impegnoAssociato = subdoc.getImpegno();
			if(impegnoAssociato  == null || impegnoAssociato.getCapitoloUscitaGestione() == null) {
				return false;
			}
			annoBilancio = impegnoAssociato.getCapitoloUscitaGestione().getBilancio().getAnno();
			annoMovimento = impegnoAssociato.getAnnoMovimento();
			return annoBilancio > annoMovimento;
		}
		if(s instanceof SubdocumentoEntrata){
			SubdocumentoEntrata subdoc = (SubdocumentoEntrata)s;
			Accertamento accertamentoAssociato = subdoc.getAccertamento();
			if(accertamentoAssociato  == null || accertamentoAssociato.getCapitoloEntrataGestione() == null) {
				return false;
			}
			annoBilancio = accertamentoAssociato.getCapitoloEntrataGestione().getBilancio().getAnno();
			annoMovimento = accertamentoAssociato.getAnnoMovimento();
			return annoBilancio > annoMovimento;
		}
		return false;
	}

	/**
	 * Calcola la stringa del soggetto.
	 * 
	 * @param s il subdocumento
	 * @return la stringa del soggetto
	 */
	private static String ottieniStringaSoggetto(Subdocumento<?,?> s) {
		if(s.getDocumento().getSoggetto() != null){
			return s.getDocumento().getSoggetto().getCodiceSoggetto() + " - " + s.getDocumento().getSoggetto().getDenominazione();
		}
		return "";
	}
	
	/**
	 * Calcola la stringa delle note.
	 * 
	 * @param s il subdocumento
	 * @return la stringa delle note
	 */
	private static String ottieniStringaNote(Subdocumento<?,?> s) {
		if(Boolean.TRUE.equals(s.getHasSubordinati()) || Boolean.TRUE.equals(s.getIsSubordinato())){
			return "*";
		}
		if(s instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)s;
			if(Boolean.TRUE.equals(subdoc.getHasRitenute())){
				return "*";
			}
		}
		return "";
	}
	
	/**
	 * Calcola la stringa di dettaglio delle note.
	 * 
	 * @param s il subdocumento
	 * @return la stringa delle note con il dettaglio
	 */
	private static String ottieniStringaDettagliNote(Subdocumento<?,?> s) {
		List<String> list = new ArrayList<String>();
		if(s instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)s;
			if(Boolean.TRUE.equals(subdoc.getHasRitenute())){
				list.add("RIT");
			}
		}
		if(Boolean.TRUE.equals(s.getHasSubordinati())){
			list.add("CON SUB");
		}
		if(Boolean.TRUE.equals(s.getIsSubordinato())){
			list.add("DOC PADRE: " + ottieniStringaDocCollegatoPadre(s.getDocumento()));
		}
		return StringUtils.join(list, " - ");
	}

	/**
	 * Calcola la stringa del documento collegato padre.
	 * 
	 * @param d il documento
	 * @return la stringa del documento padre
	 */
	private static String ottieniStringaDocCollegatoPadre(Documento<?,?> d) {
		if(d.getListaDocumentiSpesaPadre() != null && !d.getListaDocumentiSpesaPadre().isEmpty()){
			return ottieniStringaDocumentoPadre(d.getListaDocumentiSpesaPadre().get(0));
		}
		if(d.getListaDocumentiEntrataPadre() != null && !d.getListaDocumentiEntrataPadre().isEmpty()){
			return ottieniStringaDocumentoPadre(d.getListaDocumentiEntrataPadre().get(0));
		}
		return "";
	}

	/**
	 * Calcola la stringa dell'IVA.
	 * 
	 * @param s il subdocumento
	 * @return la stringa dell'IVA
	 */
	private static String ottieniStringaIva(Subdocumento<?,?> s) {
		if(s instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)s;
			return subdoc.getTipoIvaSplitReverse() != null ? subdoc.getTipoIvaSplitReverse().getDescrizione() : "";
		}
		return "";
	}

	
	/**
	 * Calcola l'importo IVA Split Reverse.
	 * 
	 * @param s il subdocumento
	 * @return l'importo dell'IVA
	 */
	private static BigDecimal ottieniImportoSplitReverse(Subdocumento<?,?> s) {
		if(s instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)s;
			return subdoc.getImportoSplitReverseNotNull();
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * Calcola la stringa del documento padre.
	 * 
	 * @param d il documento
	 * @return la stringa del documento padre
	 */
	private static String ottieniStringaDocumentoPadre(Documento<?,?> d) {
		StringBuilder result = new StringBuilder();
		if(d instanceof DocumentoSpesa){
			result.append("S-");
		}else{
			result.append("E-");
		}
		result.append(d.getAnno())
			.append("/")
			.append(d.getTipoDocumento().getCodice())
			.append("/")
			.append(d.getNumero());
		return result.toString();
	}

	
	

	/**
	 * Ottiene la stringa dell'impegno.
	 * 
	 * @param s il subdocumento
	 * @return la stringa del movimento gestione, se presente
	 */
	private static String ottieniStringaMovimento(Subdocumento<?,?> s) {
		String result = "";
		if(s instanceof SubdocumentoSpesa){
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)s;
			if(subdoc.getImpegno() != null) {
				StringBuilder sb = new StringBuilder();
				if(subdoc.getImpegno().getCapitoloUscitaGestione() != null){
					sb.append(subdoc.getImpegno().getCapitoloUscitaGestione().getBilancio().getAnno())
					.append("/");
				}
					sb.append(subdoc.getImpegno().getAnnoMovimento())
					.append("/")
					.append(subdoc.getImpegno().getNumero().toPlainString());
				if(subdoc.getSubImpegno() != null) {
					sb.append("/")
						.append(subdoc.getSubImpegno().getNumero().toPlainString());
				}
				result = sb.toString();
			}
		}else if(s instanceof SubdocumentoEntrata){
			SubdocumentoEntrata subdoc = (SubdocumentoEntrata)s;
			if(subdoc.getAccertamento() != null) {
				StringBuilder sb = new StringBuilder();
				if(subdoc.getAccertamento().getCapitoloEntrataGestione() != null){
					sb.append(subdoc.getAccertamento().getCapitoloEntrataGestione().getBilancio().getAnno())
					.append("/");
				}
					sb.append(subdoc.getAccertamento().getAnnoMovimento())
					.append("/")
					.append(subdoc.getAccertamento().getNumero().toPlainString());
				if(subdoc.getSubAccertamento() != null) {
					sb.append("/")
						.append(subdoc.getSubAccertamento().getNumero().toPlainString());
				}
				result = sb.toString();
			}
		}
		return result;
	}

	/**
	 * Calcola la stringa della famiglia.
	 * 
	 * @param documento il documento
	 * @return la stringa della famiglia
	 */
	private static String ottieniStringaFamiglia(Documento<?, ?> documento) {
		if(documento instanceof DocumentoSpesa){
			return "S";
		}
		return "E";
	}

}
