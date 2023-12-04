/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.TipoGruppoDocumento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Wrapper per l'Elenco Documenti Allegato
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 08/10/2014 - aggiunti generics
 * 
 * @param <MG>  la tipizzazione del movimenti di gestione
 * @param <SMG> la tipizzazione del submovimento di gestione
 * @param <C>   la tipizzazione del capitolo
 * @param <D>   la tipizzazione del documento
 * @param <SD>  la tipizzazione del subdocumento
 *
 */
public abstract class ElementoElencoDocumentiAllegato<MG extends MovimentoGestione, SMG extends MG, C extends Capitolo<?, ?>,
		D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> implements ModelWrapper, Serializable, Comparable<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2881317137044375095L;
	
	/** Codice per il tipo di documento di entrata */
	protected static final String CODICE_TIPO_DOCUMENTO_ENTRATA = "E";
	/** Codice per il tipo di documento di spesa */
	protected static final String CODICE_TIPO_DOCUMENTO_SPESA = "S";
	/** Codice per il tipo di movimento di gestione di entrata */
	protected static final String CODICE_TIPO_MOVIMENTO_GESTIONE_ENTRATA = "E";
	/** Codice per il tipo di movimento di gestione di spesa */
	protected static final String CODICE_TIPO_MOVIMENTO_GESTIONE_SPESA = "S";

	/** Il subdocumento */
	protected final SD subdocumento;
	/** Il documento */
	protected final D documento;
	private final ElencoDocumentiAllegato elencoDocumentiAllegato;
	private final MG movimentoGestione;
	private final SMG submovimentoGestione;
	private final Soggetto soggetto;
	private final String causaleSospensioneSoggetto;
	private final Date dataSospensioneSoggetto;
	private final Date dataRiattivazioneSoggetto;
	private final Integer uidDatiSoggetto;
	private final C capitolo;
	private final AttoAmministrativo attoAmministrativo;
	private final String note;
	private final String dettagliNote;
	private final String ivaSplitReverse;
	private final BigDecimal importoIvaSplitReverse;
	
	// INNER
	/** Il codice del tipo di documento */
	protected final String codiceTipoDocumento;
	/** Il codice del tipo di movimento di gestione */
	protected final String codiceTipoMovimentoGestione;
	/** Se siano gestite le informazioni della UEB */
	protected final Boolean isGestioneUEB;
	
	/**
	 * Costruttore a partire da elenco e dati soggetto.
	 * @param elencoDocumentiAllegato     l'elenco da wrappare
	 * @param subdocumento                il subdocumento da wrappare
	 * @param documento                   il documento da wrappare
	 * @param movimentoGestione           il movimento di gestione
	 * @param submovimentoGestione        il submovimento di gestione
	 * @param datiSospensioneSoggetto     i dati del soggetto
	 * @param attoAmministrativo          l'atto amministrativo
	 * @param ivaSplitReverse             l'iva split/reverse
	 * @param importoIvaSplitReverse      l'importo iva split/reverse
	 * @param codiceTipoDocumento         il codice del tipo documento
	 * @param codiceTipoMovimentoGestione il codice del tipo movimento di gestione
	 * @param isGestioneUEB               se la gestione delle UEB sia attiva
	 * 
	 */
	protected ElementoElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato, SD subdocumento, D documento, MG movimentoGestione, SMG submovimentoGestione,
			DatiSoggettoAllegato datiSospensioneSoggetto, AttoAmministrativo attoAmministrativo,
			String ivaSplitReverse, BigDecimal importoIvaSplitReverse, String codiceTipoDocumento, String codiceTipoMovimentoGestione, Boolean isGestioneUEB) {
		
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
		this.subdocumento = subdocumento;
		this.documento = documento;
		this.movimentoGestione = movimentoGestione;
		this.submovimentoGestione = submovimentoGestione;
		// soggetto
		this.causaleSospensioneSoggetto = datiSospensioneSoggetto == null ? null : datiSospensioneSoggetto.getCausaleSospensione();
		this.dataSospensioneSoggetto = datiSospensioneSoggetto == null || datiSospensioneSoggetto.getDataSospensione() == null ? null : new Date(datiSospensioneSoggetto.getDataSospensione().getTime());
		this.dataRiattivazioneSoggetto = datiSospensioneSoggetto == null || datiSospensioneSoggetto.getDataRiattivazione() == null ? null : new Date(datiSospensioneSoggetto.getDataRiattivazione().getTime());
		this.uidDatiSoggetto = datiSospensioneSoggetto == null || datiSospensioneSoggetto.getUid() == 0 ? null : datiSospensioneSoggetto.getUid();
		// capitolo
		this.attoAmministrativo = attoAmministrativo;
		// note
		// dettagli note
		this.ivaSplitReverse = ivaSplitReverse;
		this.importoIvaSplitReverse = importoIvaSplitReverse;
		this.codiceTipoDocumento = codiceTipoDocumento;
		this.codiceTipoMovimentoGestione = codiceTipoMovimentoGestione;
		this.isGestioneUEB = isGestioneUEB;
		
		this.capitolo = ottieniDatiCapitolo();
		this.soggetto = ottieniDatiSoggetto();
		
		String[] datiNote = ottieniDatiNote();
		this.note = datiNote[0];
		this.dettagliNote = datiNote[1];
	}
	
	/**
	 * Ottenimento dati del capitolo.
	 * 
	 * @return il capitolo
	 */
	protected abstract C ottieniDatiCapitolo();

	/**
	 * Ottiene i dati delle note
	 * @return i dati delle note
	 */
	protected abstract String[] ottieniDatiNote();
	
	/**
	 * @return the causaleSospensioneSoggetto
	 */
	public String getCausaleSospensioneSoggetto() {
		return causaleSospensioneSoggetto;
	}

	/**
	 * @return the dataSospensioneSoggetto
	 */
	public Date getDataSospensioneSoggetto() {
		return dataSospensioneSoggetto != null ? new Date(dataSospensioneSoggetto.getTime()) : null;
	}

	/**
	 * @return the dataRiattivazioneSoggetto
	 */
	public Date getDataRiattivazioneSoggetto() {
		return dataRiattivazioneSoggetto != null ? new Date(dataRiattivazioneSoggetto.getTime()) : null;
	}

	/**
	 * @return the uidDatiSoggetto
	 */
	public Integer getUidDatiSoggetto() {
		return uidDatiSoggetto;
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
	 * @return the importoIvaSplitReverse
	 */
	public BigDecimal getImportoIvaSplitReverse() {
		return importoIvaSplitReverse;
	}

	/**
	 * @return the ivaSplitReverse
	 */
	public String getIvaSplitReverse() {
		return ivaSplitReverse;
	}

	/**
	 * @return the isSubdocumentoSpesa
	 */
	public boolean getIsSubdocumentoSpesa() {
		return CODICE_TIPO_DOCUMENTO_SPESA.equals(codiceTipoDocumento);
	}
	/**
	 * @return the isSubdocumentoEntrata
	 */
	public boolean getIsSubdocumentoEntrata() {
		return CODICE_TIPO_DOCUMENTO_ENTRATA.equals(codiceTipoDocumento);
	}
	/**
	 * @return the domStringElenco
	 */
	public String getDomStringElenco() {
		final StringBuilder sb = new StringBuilder();
		final String separator = "/";
		
		// Anno e numero elenco separati dal carattere '/'.
		if(elencoDocumentiAllegato != null) {
			sb.append(elencoDocumentiAllegato.getAnno())
				.append(separator)
				.append(elencoDocumentiAllegato.getNumero());
		}
		return sb.toString();
	}
	/**
	 * @return the domStringDocumento
	 */
	public String getDomStringDocumento() {
		final StringBuilder sb = new StringBuilder();
		final String separator = "/";
		
		// Tipo Entrata-Spesa + anno documento + tipo documento + numero documento + quota documento separati dal carattere '/'.
		// Nel tooltip visualizza Descrizione documento.
		if(documento != null && documento.getTipoDocumento() != null && subdocumento != null) {
			sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(documento.getDescrizione()))
				.append("\">")
				.append(creaStringaSingoloDocumento(documento, codiceTipoDocumento))
				.append(separator)
				.append(subdocumento.getNumero())
				.append("</a>");
		}
		return sb.toString();
	}
	/**
	 * @return the domStringSoggetto
	 */
	public String getDomStringSoggetto() {
		final StringBuilder sb = new StringBuilder();
		final String separator = "-";
		// Codice.
		// Nel tooltip visualizza Denominazione
		if(soggetto != null) {
			sb.append("<a data-original-title=\"Denominazione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(soggetto.getDenominazione()))
				.append("\">")
				.append(soggetto.getCodiceSoggetto())
				.append("</a>");
			if(checkIsSoggettoSospeso()) {
				sb.append("&nbsp;<a data-original-title=\"Sospensione pagamento\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
					.append(FormatUtils.formatDate(dataSospensioneSoggetto))
					.append(separator)
					.append(causaleSospensioneSoggetto);
				if(dataRiattivazioneSoggetto != null) {
					sb.append(separator)
						.append(FormatUtils.formatDate(dataRiattivazioneSoggetto));
				}
				sb.append("\"><i class=\"icon-info-sign");
				if(!checkIsSoggettoRiattivato()) {
					sb.append(" icon-red");
				}
				sb.append("\"></i></a>");
			}
		}
		return sb.toString();
	}
	/**
	 * @return the domStringMovimento
	 */
	public String getDomStringMovimento() {
		final StringBuilder sb = new StringBuilder();
		// Tipo Entrata-Spesa + anno movimento + numero movimento + numero subimpegno se presente.
		
		if(movimentoGestione != null) {
			sb.append(codiceTipoMovimentoGestione)
				.append("-")
				.append(movimentoGestione.getAnnoMovimento())
				.append("/")
				.append(movimentoGestione.getNumeroBigDecimal().toPlainString());
			if(submovimentoGestione != null) {
				sb.append("-")
					.append(submovimentoGestione.getNumeroBigDecimal().toPlainString());
			}
		}
		return sb.toString();
	}
	/**
	 * @return the domStringCapitolo
	 */
	public String getDomStringCapitolo() {
		final StringBuilder sb = new StringBuilder();
		final String separator = "/";
		// Anno capitolo + numero capitolo + numero articolo + numero ueb se presente
		
		if(capitolo != null) {
			sb.append(capitolo.getAnnoCapitolo())
				.append(separator)
				.append(capitolo.getNumeroCapitolo())
				.append(separator)
				.append(capitolo.getNumeroArticolo());
			if(Boolean.TRUE.equals(isGestioneUEB)) {
				sb.append(separator)
					.append(capitolo.getNumeroUEB());
			}
		}
		return sb.toString();
	}
	/**
	 * @return the domStringAttoAmministrativo
	 */
	public String getDomStringAttoAmministrativo() {
		return computeStringAttoAmministrativo(attoAmministrativo);
	}
	
	/**
	 * @return the domStringAttoAmministrativoMovimento
	 */
	public String getDomStringAttoAmministrativoMovimento() {
		AttoAmministrativo aa = submovimentoGestione != null
				? submovimentoGestione.getAttoAmministrativo()
				: movimentoGestione != null
					? movimentoGestione.getAttoAmministrativo()
					: null;
		return computeStringAttoAmministrativo(aa);
	}
	
	/**
	 * Calcola la stringa dell'atto amministrativo.
	 * 
	 * @param attoAmministrativo l'atto
	 * @return la stringa DOM dell'atto
	 */
	private String computeStringAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		final StringBuilder sb = new StringBuilder();
		final String separator = "/";
		// Anno provvedimento + numero del provvedimento + codice tipo atto + codice struttura amministrativo contabile separati dal carattere '/'.
		// Con un tooltip visualizzare Oggetto
		
		if(attoAmministrativo != null) {
			sb.append("<a data-original-title=\"Oggetto\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(attoAmministrativo.getOggetto()))
				.append("\">")
				.append(attoAmministrativo.getAnno())
				.append(separator)
				.append(attoAmministrativo.getNumero());
			if(attoAmministrativo.getTipoAtto() != null) {
				sb.append(separator)
					.append(attoAmministrativo.getTipoAtto().getCodice());
			}
			if(attoAmministrativo.getStrutturaAmmContabile() != null) {
				sb.append(separator)
				.append(attoAmministrativo.getStrutturaAmmContabile().getCodice());
			}
			sb.append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringAzioni
	 */
	public String getDomStringAzioni() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"btn-group\">")
			.append("<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>")
			.append("<ul class=\"dropdown-menu pull-right\">");
		if(isAggiornaAbilitato()) {
			sb.append("<li><a class=\"aggiornaQuotaElenco\" href=\"#\">aggiorna</a></li>");
		}
		if(isEliminaAbilitato()) {
			sb.append("<li><a class=\"eliminaQuotaElenco\" href=\"#\">elimina</a></li>");
		}
		if(isSospendiPagamentoSoggettoAbilitato()) {
			sb.append("<li><a class=\"sospendiPagamentoSoggetto\" href=\"#\">sospendi pagamento soggetto</a></li>");
		}
		if(isRiattivaPagamentoSoggettoAbilitato()) {
			sb.append("<li><a class=\"riattivaPagamentoSoggetto\" href=\"#\">riattiva pagamento soggetto</a></li>");
		}
		if(isSpezzaAbilitato()) {
			sb.append("<li><a class=\"spezzaQuotaElenco\" href=\"#\">spezza</a></li>");
		}
		sb.append("</ul>")
			.append("</div>");
		return sb.toString();
	}
	/**
	 * @return the importoInAtto
	 */
	public BigDecimal getImportoInAtto() {
		BigDecimal result = BigDecimal.ZERO;
		
		if(subdocumento != null) {
			//JIRA -3039
			//result = subdocumento.getImporto();
			result = subdocumento.getImportoNotNull().subtract(subdocumento.getImportoDaDedurreNotNull());
		}
		
		return result;
	}
	
	/**
	 * @return the domStringConvalida
	 */
	public String getDomStringConvalida() {
		return subdocumento != null && Boolean.TRUE.equals(subdocumento.getFlagConvalidaManuale()) ? "MAN" : "AUT";
	}
	
	/**
	 * @return the domStringQuoteACopertura
	 */
	public String getDomStringQuoteACopertura() {
		if(subdocumento == null || !Boolean.TRUE.equals(subdocumento.getFlagACopertura())) {
			return "";
		}
		return "<a data-original-title=\"Info\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"Contiene quote a copertura\"><i class=\"icon-asterisk icon-2x\">&nbsp;</i></a>";
	}
	
	/**
	 * @return the domStringLiquidazione
	 */
	public String getDomStringLiquidazione() {
		return "";
	}
	
	/**
	 * @return the domStringDistinta
	 */
	public String getDomStringDistinta() {
		return "";
	}
	
	/**
	 * @return the domStringContoTesoreria
	 */
	public String getDomStringContoTesoreria() {
		return "";
	}
	
	/**
	 * @return the domStringImportoSplitReverse
	 */
	public String getDomStringImportoSplitReverse() {
		return new StringBuilder()
				.append("<a data-original-title=\"Info\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"importo Iva: ")
				.append(FormatUtils.formatCurrency(getImportoIvaSplitReverse()))
				.append("\" >")
				.append(getIvaSplitReverse())
				.append("</a>")
				.toString();
	}
	
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		if(subdocumento == null) {
			return BigDecimal.ZERO;
		}
		return subdocumento.getImporto();
	}
	
	/**
	 * @return the uidElencoDocumentiAllegato
	 */
	public Integer getUidElencoDocumentiAllegato() {
		if(elencoDocumentiAllegato == null) {
			return null;
		}
		return Integer.valueOf(elencoDocumentiAllegato.getUid());
	}
	
	/**
	 * @return the importoDaDedurre
	 */
	public BigDecimal getImportoDaDedurre() {
		if(subdocumento == null) {
			return BigDecimal.ZERO;
		}
		return subdocumento.getImportoDaDedurreNotNull();
	}
	
	/**
	 * @return the importoSpesa
	 */
	public String getImportoSpesa() {
		return "-";
	}
	
	/**
	 * @return the importoEntrata
	 */
	public String getImportoEntrata() {
		return "-";
	}
	
	/**
	 * @return the subdocumentoCig
	 */
	public String getSubdocumentoCig() {
		return "";
	}
	
	/**
	 * @return the subdocumentoCig
	 */
	public String getSubdocumentoCup() {
		return "";
	}
	
	
	/**
	 * @return the uidDocumento
	 */
	public Integer getUidDocumento() {
		if(documento == null) {
			return null;
		}
		return Integer.valueOf(documento.getUid());
	}
	
	/**
	 * @return the uidSoggetto
	 */
	public Integer getUidSoggetto() {
		if(soggetto == null) {
			return null;
		}
		return Integer.valueOf(soggetto.getUid());
	}
	
	/**
	 * @return the codiceSoggetto
	 */
	public String getCodiceSoggetto() {
		if(soggetto == null) {
			return "";
		}
		return soggetto.getCodiceSoggetto();
	}
	
	// SIAC-4675
	/**
	 * @return the domStringAnnotazioni
	 */
	public String getDomStringAnnotazioni() {
		StringBuilder sbHTML = new StringBuilder();
		String popoverPrefix = "<a href=\"#\" rel=\"popover\" data-trigger=\"hover\" data-original-title=\"Dettagli\" data-content=\"";
		sbHTML.append(popoverPrefix)
			.append(FormatUtils.formatHtmlAttributeString(dettagliNote))
			.append("\">")
			.append(note)
			.append("</a>");
		if(subdocumento != null && subdocumento.getImportoDaDedurre() != null && subdocumento.getImportoDaDedurre().signum() != 0) {
			List<DocumentoEntrata> documentiEntrataFigli = subdocumento.getDocumento().getListaDocumentiEntrataFiglioByTipoGruppo(TipoGruppoDocumento.NOTA_DI_CREDITO);
			List<DocumentoSpesa> documentiSpesaFigli = subdocumento.getDocumento().getListaDocumentiSpesaFiglioByTipoGruppo(TipoGruppoDocumento.NOTA_DI_CREDITO);
			sbHTML.append(popoverPrefix)
				.append("Importo da dedurre:")
				.append(FormatUtils.formatCurrency(subdocumento.getImportoDaDedurre()))
				.append(" <br/>  Documenti:  ")
				.append(ottieniStringaDocCollegatiFiglio(documentiEntrataFigli, documentiSpesaFigli))
				.append(" \" data-html=\"true\" \">NCD</a>");
		}
		return sbHTML.toString();
	}
	
	// SIAC-5021
	/**
	 * @return the domStringModalitaPagamentoSoggetto
	 */
	public String getDomStringModalitaPagamentoSoggetto() {
		return "";
	}
	
	/**
	 * @return the tipoDocumentoAllegatoAtto
	 */
	public boolean isTipoDocumentoAllegatoAtto() {
		return documento != null && documento.getTipoDocumento() != null
				&& BilConstants.TIPO_DOCUMENTO_ALLEGATO_ATTO.getConstant().equals(documento.getTipoDocumento().getCodice());
	}

	// SIAC-5043
	/**
	 * @return the domStringOrdinativo
	 */
	public String getDomStringOrdinativo() {
		if(subdocumento == null || subdocumento.getOrdinativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-placement=\"left\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(subdocumento.getOrdinativo().getDescrizione()))
			.append("\">")
			.append(subdocumento.getOrdinativo().getAnno())
			.append("/")
			.append(subdocumento.getOrdinativo().getNumero())
			.append("</a>")
			.toString();
	}
	
	@Override
	public int getUid() {
		return subdocumento != null ? subdocumento.getUid() : 0;
	}
	
	// SIAC-5171
	
	/**
	 * @return the domStringOrdinativoReduced
	 */
	public String getDomStringOrdinativoReduced() {
		if(subdocumento == null || subdocumento.getOrdinativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append(subdocumento.getOrdinativo().getAnno())
			.append("/")
			.append(subdocumento.getOrdinativo().getNumero())
			.toString();
	}
	
	/**
	 * @return the domStringStatoOrdinativo
	 */
	public String getDomStringStatoOrdinativo() {
		return subdocumento == null || subdocumento.getOrdinativo() == null
			? ""
			: subdocumento.getOrdinativo().getCodStatoOperativoOrdinativo();
	}
	
	/**
	 * Estrae il subdocumento.
	 * @return il subdocumento
	 */
	public SD extractSubdocumento() {
		return subdocumento;
	}
	
	/**
	 * Controlla se il soggetto sia sospeso alla data odierna.
	 * 
	 * @return <code>true</code> se il soggetto &eacute; sospeso; <code>false</code> in caso contrario
	 */
	private boolean checkIsSoggettoSospeso() {
		return dataSospensioneSoggetto != null && now().after(dataSospensioneSoggetto);
	}
	
	/**
	 * Controlla se il soggetto sia riattivato alla data odierna.
	 * 
	 * @return <code>true</code> se il soggetto &eacute; riattivato; <code>false</code> in caso contrario
	 */
	private boolean checkIsSoggettoRiattivato() {
		return dataRiattivazioneSoggetto != null && now().after(dataRiattivazioneSoggetto);
	}
	
	/**
	 * Controlla se l'aggiornamento sia abilitato.
	 * 
	 * @return the aggiornaAbilitato
	 */
	private boolean isAggiornaAbilitato() {
		// Presente solamente se la quota selezionata appartiene ad un documento di tipo ALLEGATO ATTO
		// e se la quota non e' ancora stata convalidata (ovvero l'attributo 'TipoConvalida' e' valorizzato).
		return documento != null && documento.getTipoDocumento() != null
			&& BilConstants.TIPO_DOCUMENTO_ALLEGATO_ATTO.getConstant().equals(documento.getTipoDocumento().getCodice())
			&& subdocumento != null && subdocumento.getFlagConvalidaManuale() == null;
	}
	
	/**
	 * Controlla se l'eliminazione sia abilitata.
	 * 
	 * @return the eliminaAbilitato
	 */
	private boolean isEliminaAbilitato() {
		// Presente se la quota non e' ancora stata convalidata (ovvero l'attributo 'TipoConvalida' non e' valorizzato)
		return subdocumento != null && subdocumento.getFlagConvalidaManuale() == null;
	}
	
	/**
	 * Controlla se la sospensione del pagamento al soggetto sia abilitata.
	 * 
	 * @return the sospendiPagamentoSoggettoAbilitato
	 */
	private boolean isSospendiPagamentoSoggettoAbilitato() {
		// Sempre presente
		return true;
	}
	
	/**
	 * Controlla se la riattivazione del pagamento al soggetto sia abilitata.
	 * 
	 * @return the riattivaPagamentoSoggettoAbilitato
	 */
	private boolean isRiattivaPagamentoSoggettoAbilitato() {
		// Presente se e' attiva una sospensione per il soggetto collegato al documento della quota presente sulla riga
		return checkIsSoggettoSospeso() && !checkIsSoggettoRiattivato();
	}
	
	/**
	 * Controlla se lo spezzamento sia abilitato.
	 * 
	 * @return the spezzaAbilitato
	 */
	private boolean isSpezzaAbilitato() {
		return subdocumento != null
			// Tipo documento ALG
			// SIAC-5301: il tipo di documento non e' piu' un filtro
			// Senza ordinativo
			&& (subdocumento.getOrdinativo() == null || subdocumento.getOrdinativo().getUid() == 0)
			// Senza dati IVA
			&& StringUtils.isBlank(subdocumento.getNumeroRegistrazioneIVA());
	}
	
	/**
	 * Popola i dati del soggetto.
	 * 
	 * @return il soggetto
	 */
	private Soggetto ottieniDatiSoggetto() {
		if(documento != null) {
			return documento.getSoggetto();
		}
		return null;
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
	 * Popola una stringa con gli estremi di un eventuale documento padre del documento passat in input
	 * @return la stringa con gli estremi del documento
	 */
	protected String ottieniStringaDocCollegatoPadre() {
		D doc = subdocumento.getDocumento();
		if(doc.getListaDocumentiSpesaPadre() != null && !doc.getListaDocumentiSpesaPadre().isEmpty()){
			DocumentoSpesa docSpesa = doc.getListaDocumentiSpesaPadre().get(0);
			return "S-" + docSpesa.getAnno() + "/" + docSpesa.getTipoDocumento().getCodice() + "/" + docSpesa.getNumero();
		}
		if(doc.getListaDocumentiEntrataPadre() != null && !doc.getListaDocumentiEntrataPadre().isEmpty()){
			DocumentoEntrata docEntrata = doc.getListaDocumentiEntrataPadre().get(0);
			return "E-" + docEntrata.getAnno() + "/" + docEntrata.getTipoDocumento().getCodice() + "/" + docEntrata.getNumero();
		}
		return "";
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
	/**
	 * popola una stringa con Anno/numero provvisorioDicassa
	 * @return la stringa con gli estremi del provvisorio
	 */
	protected String ottieniStringaProvvisorio(){
		ProvvisorioDiCassa provvisorioDiCassa = subdocumento.getProvvisorioCassa();
		return provvisorioDiCassa.getAnno() + "/" + provvisorioDiCassa.getNumero();
	}
	
	/**
	 * Ottiene la data attuale.
	 * 
	 * @return la data attuale
	 */
	private Date now() {
		return new Date();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		if(elencoDocumentiAllegato != null) {
			hcb.append(elencoDocumentiAllegato.getNumero());
		}
		if(documento != null) {
			hcb.append(documento.getAnno())
				.append(documento.getNumero());
		}
		if(subdocumento != null) {
			hcb.append(subdocumento.getNumero());
		}
		if(movimentoGestione != null) {
			hcb.append(movimentoGestione.getAnnoMovimento())
				.append(movimentoGestione.getNumeroBigDecimal());
		}
		return hcb.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return false;
		}
		if(!(obj instanceof ElementoElencoDocumentiAllegato)) {
			return false;
		}
		ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> other = (ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>) obj;
		EqualsBuilder eb = new EqualsBuilder();
		
		eb.append(this.elencoDocumentiAllegato != null, other.elencoDocumentiAllegato != null)
			.append(this.documento != null, other.documento != null)
			.append(this.documento != null && this.documento.getTipoDocumento() != null, other.documento != null && other.documento.getTipoDocumento() != null)
			.append(this.subdocumento != null, other.subdocumento != null)
			.append(this.movimentoGestione != null, other.movimentoGestione != null);
		
		if(this.elencoDocumentiAllegato != null && other.elencoDocumentiAllegato != null) {
			eb.append(this.elencoDocumentiAllegato.getNumero(), other.elencoDocumentiAllegato.getNumero());
		}
		if(this.documento != null && other.documento != null) {
			eb.append(this.documento.getAnno(), other.documento.getAnno())
				.append(this.documento.getNumero(), other.documento.getNumero());
			
			if(this.documento.getTipoDocumento() != null && other.documento.getTipoDocumento() != null) {
				eb.append(this.documento.getTipoDocumento().getCodice(), other.documento.getTipoDocumento().getCodice());
			}
		}
		if(this.subdocumento != null && other.subdocumento != null) {
			eb.append(this.subdocumento.getNumero(), other.subdocumento.getNumero());
		}
		
		if(this.movimentoGestione != null && other.movimentoGestione != null) {
			eb.append(this.movimentoGestione.getAnnoMovimento(), other.movimentoGestione.getAnnoMovimento())
				.append(this.movimentoGestione.getNumeroBigDecimal(), other.movimentoGestione.getNumeroBigDecimal());
		}
		eb.append(this.getUid(), other.getUid());
		
		return eb.isEquals();
	}

	@Override
	public int compareTo(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> other) {
		if(this == other){
			return 0;
		}
		if(other == null){
			return -1;
		}
		if(this.documento == other.documento){
			return 0;
		}
		if(this.documento == null){ 
			return 1;
		}
		if(other.documento == null){
			return -1;
		}
		if(this.documento.getTipoDocumento() == null){ 
			return 1;
		}
		if(other.documento.getTipoDocumento() == null){
			return -1;
		}
		if(this.subdocumento == null){ 
			return 1;
		}
		if(other.subdocumento == null){
			return -1;
		}
		return new CompareToBuilder()
			.append(this.documento.getTipoDocumento().getCodice(), other.documento.getTipoDocumento().getCodice())
			.append(this.documento.getNumero(), other.documento.getNumero())
			.append(this.subdocumento.getNumero(), other.subdocumento.getNumero())
			.toComparison();
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
	 * Gets the inibisci selezione.
	 *
	 * @return the inibisci selezione
	 */
	public boolean getInibisciSelezione() {
		return isMovimentoGestioneDurc() && isDurcScadutoONull();
	}
	
	private boolean isDurcScadutoONull() {
		Date dataFineValiditaDurc = getDataFineValiditaDurc();
		if(dataFineValiditaDurc == null) {
			return true;
		}
		Date now = new Date();
		return now.compareTo(dataFineValiditaDurc)>0;
	}
	
	/**
	 * Gets the data fine validita durc.
	 *
	 * @return the data fine validita durc
	 */
	protected Date getDataFineValiditaDurc() {
		return new Date();
	}
	
	/**
	 * Controlla se il movimento di gestione sia soggetto a DURC
	 * @return se sia DURC
	 */
	protected boolean isMovimentoGestioneDurc() {
		return movimentoGestione != null && movimentoGestione.isFlagSoggettoDurc();
	}
	
	/**
	 * Gets the dom string dati durc.
	 *
	 * @return the dom string dati durc
	 */
	public String getDomStringDatiDurc() {
		StringBuilder sb = new StringBuilder();
		if(!isMovimentoGestioneDurc() || !isDurcScadutoONull() ) {
			return sb.toString();
		}
		Date dataFineValiditaDurc = getDataFineValiditaDurc();
		sb.append("<a data-original-title='Dati Durc' data-html='true' data-trigger='hover' rel='popover' data-content='")
		.append("Rich. DURC:" )
		.append(FormatUtils.formatBoolean(isMovimentoGestioneDurc(), "S&igrave;", "No"))
		.append("<br/>")
		.append("Data fine validita DURC: ")
		.append(dataFineValiditaDurc != null? FormatUtils.formatDate(dataFineValiditaDurc) : "Durc da richiedere");
		sb.append("'>")
			.append("*");
		sb.append("</a>");
		
		return sb.toString();
	}
	
}
