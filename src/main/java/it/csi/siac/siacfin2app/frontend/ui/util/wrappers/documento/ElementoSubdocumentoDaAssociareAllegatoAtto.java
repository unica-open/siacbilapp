/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.TipoGruppoDocumento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Classe di wrap per il Subdocumento Da Associare all'allegato atto.
 * @param <MG> la tipizzazione del movimento di gestione
 * @param <SMG> la tipizzazione del submovimento di gestione
 * @param <D> la tipizzazione del documento
 * @param <SD> la tipizzazione del subdocumento
 * @param <C> la tipizzazione del capitolo
 */
public abstract class ElementoSubdocumentoDaAssociareAllegatoAtto<MG extends MovimentoGestione, SMG extends MG, D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>, C extends Capitolo<?, ?>>
		implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4533278250656859592L;
	
	/** Codice per il tipo di documento di entrata */
	protected static final String CODICE_TIPO_DOCUMENTO_ENTRATA = "E";
	/** Codice per il tipo di documento di spesa */
	protected static final String CODICE_TIPO_DOCUMENTO_SPESA = "S";
	
	/** Pattern per il provvisorio di cassa */
	private static final Pattern PATTERN_PROVVISORI_CASSA = Pattern.compile("^PROVV\\. DI CASSA: \\d{4}\\/\\d+$");
	/** Il subdocumento */
	protected final SD subdocumento;
	/** Il movimento di gestione */
	protected final MG movimentoGestione;
	private final D documento;
	private final SMG submovimentoGestione;
	private final C capitolo;
	private final String note;
	private final String dettagliNote;
	// INNER
	/** Il codice del tipo di documento */
	protected final String codiceTipoDocumento;

	/**
	 * Costruttore a partire dal subdoc
	 * @param subdocumento         il subdocumento da wrappare
	 * @param documento            il documento da wrappare
	 * @param movimentoGestione    il movimento di gestione
	 * @param submovimentoGestione il submovimento di gestione
	 * @param codiceTipoDocumento  il codice del tipo documento
	 */
	protected ElementoSubdocumentoDaAssociareAllegatoAtto(SD subdocumento, D documento, MG movimentoGestione, SMG submovimentoGestione, String codiceTipoDocumento) {
		
		this.subdocumento = subdocumento;
		this.documento = documento;
		this.movimentoGestione = movimentoGestione;
		this.submovimentoGestione = submovimentoGestione;
		this.codiceTipoDocumento = codiceTipoDocumento;

		this.capitolo = ottieniDatiCapitolo();
		
		String[] datiNote = ottieniDatiNote();
		this.note = datiNote[0];
		this.dettagliNote = datiNote[1];
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the dettagliNote
	 */
	public String getDettagliNote() {
		return dettagliNote;
	}

	/**
	 * @return the domStringDocumento
	 */
	public String getDomStringDocumento() {
		if(documento == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a data-original-title='Descrizione' href='#' data-trigger='hover' rel='popover' data-content='")
				.append(FormatUtils.formatHtmlAttributeString(documento.getDescrizione()))
				.append("'>")
				.append(creaStringaSingoloDocumento(documento, codiceTipoDocumento))
//				.append(codiceTipoDocumento)
//				.append("-")
//				.append(documento.getTipoDocumento().getCodice())
//				.append("/")
//				.append(documento.getAnno())
//				.append("/")
//				.append(documento.getNumero())
				.append("</a>")
				.toString();
	}
	
	/**
	 * @return the dataEmissione
	 */
	public Date getDataEmissione() {
		return documento != null ? documento.getDataEmissione() : null;
	}
	
	/**
	 * @return the domStringStato
	 */
	public String getDomStringStato() {
		if(documento == null || documento.getStatoOperativoDocumento() == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a data-original-title='Stato' href='#' data-trigger='hover' rel='popover' data-content='")
				.append(FormatUtils.formatHtmlAttributeString(documento.getStatoOperativoDocumento().getDescrizione()))
				.append("'>")
				.append(documento.getStatoOperativoDocumento().getCodice())
				.append("</a>")
				.toString();
	}
	
	/**
	 * @return the domStringSoggetto
	 */
	public String getDomStringSoggetto() {
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
	 * @return the numeroQuota
	 */
	public Integer getNumeroQuota() {
		return subdocumento != null ? subdocumento.getNumero() : null;
	}
	
	/**
	 * @return the domStringMovimento
	 */
	public String getDomStringMovimento() {
		if(movimentoGestione == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if(capitolo != null){
			sb.append(capitolo.getBilancio().getAnno())
				.append("/");
		}
		sb.append(movimentoGestione.getAnnoMovimento());
		if(movimentoGestione.getNumeroBigDecimal() != null) {
			sb.append("/")
				.append(movimentoGestione.getNumeroBigDecimal().toPlainString());
		}
		if(submovimentoGestione != null && submovimentoGestione.getNumeroBigDecimal() != null) {
			sb.append("-")
				.append(submovimentoGestione.getNumeroBigDecimal().toPlainString());
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the domStringIva
	 */
	public String getDomStringIva() {
		return "";
	}
	
	/**
	 * @return the domStringAnnotazioni
	 */
	public String getDomStringAnnotazioni() {
		StringBuilder sbHTML = new StringBuilder();
		String popoverPrefix = "<a data-original-title=\"Dettagli\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"";
		if(!PATTERN_PROVVISORI_CASSA.matcher(dettagliNote).matches()) {
			sbHTML.append(popoverPrefix)
				.append(FormatUtils.formatHtmlAttributeString(dettagliNote))
				.append("\" data-trigger=\"hover\">")
				.append(note)
				.append( "</a>");
		}
		if(subdocumento != null && subdocumento.getImportoDaDedurre() != null && subdocumento.getImportoDaDedurre().signum() != 0) {
			List<DocumentoEntrata> documentiEntrataFigli = subdocumento.getDocumento().getListaDocumentiEntrataFiglioByTipoGruppo(TipoGruppoDocumento.NOTA_DI_CREDITO);
			List<DocumentoSpesa> documentiSpesaFigli = subdocumento.getDocumento().getListaDocumentiSpesaFiglioByTipoGruppo(TipoGruppoDocumento.NOTA_DI_CREDITO);
			sbHTML.append(popoverPrefix)
				.append("Importo da dedurre:")
				.append(FormatUtils.formatCurrency(subdocumento.getImportoDaDedurre()))
				.append(" <br/>  Documenti:  ")
				.append(ottieniStringaDocCollegatiFiglio(documentiEntrataFigli, documentiSpesaFigli))
				.append(" \" data-html=\"true\" \">NCD")
				.append("</a>");
		}
		return sbHTML.toString();
	}
	
	/**
	 * @return the importoQuota
	 */
	public BigDecimal getImportoQuota() {
		return subdocumento != null ? subdocumento.getImporto() : null;
	}
	
	/**
	 * @return the famiglia
	 */
	public String getFamiglia() {
		return codiceTipoDocumento;
	}
	
	@Override
	public int getUid() {
		return subdocumento != null ? subdocumento.getUid() : -1;
	}
	
	/**
	 * Ottiene i dati del capitolo
	 * @return i dati del capitolo
	 */
	protected abstract C ottieniDatiCapitolo();
	/**
	 * Ottiene i dati delle note
	 * @return i dati delle note
	 */
	protected abstract String[] ottieniDatiNote();
	
	/**
	 * Popola una stringa con gli estremi di un eventuale documento padre del documento passat in input
	 * @return la stringa con gli estremi del documento
	 */
	protected String ottieniStringaDocCollegatoPadre() {
		D doc = subdocumento.getDocumento();
		if(doc.getListaDocumentiSpesaPadre() != null && !doc.getListaDocumentiSpesaPadre().isEmpty()){
			DocumentoSpesa docSpesa = doc.getListaDocumentiSpesaPadre().get(0);
			return creaStringaSingoloDocumento(docSpesa, "S");
		}
		if(doc.getListaDocumentiEntrataPadre() != null && !doc.getListaDocumentiEntrataPadre().isEmpty()){
			DocumentoEntrata docEntrata = doc.getListaDocumentiEntrataPadre().get(0);
			return creaStringaSingoloDocumento(docEntrata,"E");
		}
		return "";
	}
	//SIAC-5308
	/**
	 * Crea stringa di descrizione relativa ad un singolo documento con i segenti dati: prefisso-annoDoc-codiceTipoDoc-numerodoc
	 *
	 * @param doc il documento di cui si vuole la stringa
	 * @param prefix il prefisso da pre-pendere alla stringa
	 * @return la stringa creata
	 */
	private String creaStringaSingoloDocumento(Documento<?,?> doc, String prefix) {
		StringBuilder sbd = new StringBuilder();
		sbd.append(prefix != null? prefix : "")
			.append("-")
			.append(doc.getAnno())
			.append("/")
			.append(doc.getTipoDocumento().getCodice())
			.append("/")
			.append(doc.getNumero());
		return FormatUtils.formatHtmlAttributeString(sbd.toString());
	}
	
	/**
	 * Ottieni stringa doc collegati figlio.
	 *
	 * @param documentiEntrata the documenti entrata
	 * @param documentiSpesa the documenti spesa
	 * @return the string
	 */
	protected String ottieniStringaDocCollegatiFiglio(List<DocumentoEntrata> documentiEntrata, List<DocumentoSpesa> documentiSpesa) {
		//SIAC-5308
		if(documentiSpesa != null && !documentiSpesa.isEmpty()){
			return ottieniStringaDocumentiSpesa(documentiSpesa);
		}
		if(documentiEntrata != null && !documentiEntrata.isEmpty()){
			return ottieniStringaDocumentiEntrata(documentiEntrata);
		}
		return "";
	}

	
	/**
	 * Ottiene una stringa del tipo S-anno/codiceTipoDoc/numero per ogni documento spesa presente nella lista, separandoli tramite &lt;br/&gt;
	 *
	 * @param listaDocumentiSpesa  la lista dei documenti spesa di cui si vuole la descrizione formattata
	 * @return la stringa creata
	 */
	private String ottieniStringaDocumentiSpesa(List<DocumentoSpesa> listaDocumentiSpesa) {
		List<String> chunks = new ArrayList<String>();
		for(DocumentoSpesa doc : listaDocumentiSpesa) {
			chunks.add(creaStringaSingoloDocumento(doc, "S"));
		}
		return StringUtils.join(chunks, "<br/>");
	}

	
	/**
	 *  Ottiene una stringa del tipo E-anno/codiceTipoDoc/numero per ogni documento spesa presente nella lista, separandoli tramite &lt;br/&gt;
	 *
	 * @param listaDocumentiEntrata la lista dei documenti entrata di cui si vuole la descrizione formattata
	 * @return la stringa creata
	 */
	private String ottieniStringaDocumentiEntrata(List<DocumentoEntrata> listaDocumentiEntrata) {
		StringBuilder sb = new StringBuilder();
		for(DocumentoEntrata doc : listaDocumentiEntrata) {
			sb.append(creaStringaSingoloDocumento(doc,"E"))
			.append("<br/>");
		}
		return sb.toString();
	}

	/**
	 * popola una stringa con Anno/numero provvisorioDicassa
	 * @return la stringa con gli estremi del provvisorio
	 */
	protected String ottieniStringaProvvisorio(){
		ProvvisorioDiCassa provvisorioDiCassa = subdocumento.getProvvisorioCassa();
		return provvisorioDiCassa.getAnno() + "/" + provvisorioDiCassa.getNumero();
	}
	
	/**
	 * Ottiene i dati di dettaglio per il subordinato.
	 * <br/>
	 * Nel tooltip  concatenare:
	 * <ul>
	 *     <li>SE esitono ritenute diverse da SPLIT: &ldquo;RIT&rdquo;</li>
	 *     <li>SE CollegamentoSubordinato come padre con doc di tipo PNL &ldquo;CON PNL&rdquo;</li>
	 *     <li>SE CollegamentoSubordinato come padre &ldquo;CON SUB&rdquo;</li>
	 *     <li>SE CollegamentoSubordinato come figlio estremi documento padre</li>
	 * </ul>
	 * @return il dettaglio
	 */
	protected String ottieniDettaglioSubordinato() {
		// SIAC-4765
		if(padreHasPNL()) {
			return "PNL";
		}
		
		return "DOC PADRE: " + ottieniStringaDocCollegatoPadre();
	}
	
	/**
	 * Controlla se il padre ha dei documenti di tipo PNL associati
	 * @return se il padre ha documenti di tipo PNL associati
	 */
	protected boolean padreHasPNL() {
		D doc = subdocumento.getDocumento();
		if(doc.getListaDocumentiSpesaFiglio() != null && !doc.getListaDocumentiSpesaFiglio().isEmpty()){
			DocumentoSpesa docSpesa = doc.getListaDocumentiSpesaFiglio().get(0);
			return "PNL".equals(docSpesa.getTipoDocumento().getCodice());
		}
		if(doc.getListaDocumentiEntrataFiglio() != null && !doc.getListaDocumentiEntrataFiglio().isEmpty()){
			DocumentoEntrata docEntrata = doc.getListaDocumentiEntrataFiglio().get(0);
			return "PNL".equals(docEntrata.getTipoDocumento().getCodice());
		}
		return false;
	}
	
}
