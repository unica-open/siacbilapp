/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.TipoAvviso;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

/**
 * Classe di model per l'aggiornamento del documento.
 * 
 * @author Marchino Alessandro, Osorio Alessandra
 * @version 1.0.0 - 12/03/2014
 *
 */
public class AggiornaDocumentoModel extends GenericDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5642500162717912789L;
	
	private Integer uidDocumentoDaAggiornare;
	private Integer uidQuota;
	
	private TipoAvviso tipoAvviso;
	private NoteTesoriere noteTesoriere;
	
	private Causale770 causale770;
	private CodiceSommaNonSoggetta tipoSommaNonSoggetta;
	
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	
	// le liste comuni ad entrata e spesa
	private List<CommissioniDocumento> listaCommissioniDocumento = new ArrayList<CommissioniDocumento>();
	private List<Causale770> listaCausale770 = new ArrayList<Causale770>();
	private List<TipoDocumento> listaTipoDocumentoNote = new ArrayList<TipoDocumento>();
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	
	
	private BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
	private BigDecimal totaleDaPagareQuoteSenzaOrdinativo = BigDecimal.ZERO;
	private BigDecimal totaleQuote = BigDecimal.ZERO;
//	private BigDecimal noteCreditoDocumento = BigDecimal.ZERO;
	private BigDecimal importoDaDedurreSuFattura;

	private BigDecimal totaleNoteCredito = BigDecimal.ZERO;
	private BigDecimal importoDaAttribuire = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaDedurreSuFattura = BigDecimal.ZERO;
	
	//per l'azione di consultaQuote a partire dalle note credito
	private BigDecimal totaleQuoteNotaCredito = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaDedurre = BigDecimal.ZERO;
	
	private Boolean flagNoteCreditoAccessibile = Boolean.TRUE;
	private Boolean flagDatiIvaAccessibile = Boolean.TRUE;
	
	private Boolean ingressoTabQuote = Boolean.FALSE;
	private Boolean figlioDiDocumentoEntrata = Boolean.FALSE;
	private Boolean capitoloRilevanteIvaVisibile = Boolean.FALSE;
	private Boolean importoQuotaDisabilitato = Boolean.FALSE;
	private Boolean impegnoQuotaDisabilitato = Boolean.FALSE;
	private Boolean provvedimentoQuotaDisabilitato = Boolean.FALSE;
	private Boolean capitoloQuotaDisabilitato = Boolean.FALSE;
	
	
	private Integer rigaDaEliminare;
	private Integer uidNotaCredito;
	
	private Integer progressivoNumeroSubdocumento;
	
	
	
	// Workaround per la reimpostazione del netto. Brutta cosa
	private BigDecimal oldNetto = BigDecimal.ZERO;
	private BigDecimal oldArrotondamento = BigDecimal.ZERO;
	
	// Per l'IVA
	private String tipoInserimentoDatiIva;
	private Integer uidSubdocumentoIva;
	private Boolean documentoIvaLegatoDocumentoPresente = Boolean.FALSE;
	private Boolean datiIvaAccessibileQuota = Boolean.FALSE;
	private Boolean azioneIvaGestibile = Boolean.FALSE;
	
	//Lotto O
	private Integer uidDocumentoDaCollegare;
	private Integer uidDocumentoDaScollegare;
	
	//SIAC 3931 se ci sono 2 provvedimenti uno con sac e l'altro senza rimando al cliente la possibilità di scegliere quale utilizzare
	private Boolean proseguireConElaborazione = Boolean.FALSE;
	private Boolean proseguireConElaborazioneAttoAmministrativo = Boolean.FALSE;
	
	// SIAC-4391
	private String flagConvalidaManualeQuota;
	
	//SIAC-5333
	private Boolean validazionePrimaNotaDaDocumento;
	private int uidDaCompletare;
	
	//SIAC-8153
	private StrutturaAmministrativoContabile strutturaCompetenteQuota;
	
	/** Costruttore vuoto di default */
	public AggiornaDocumentoModel() {
		setTitolo("Aggiorna Documenti");
	}

	/**
	 * @return the uidDocumentoDaAggiornare
	 */
	public Integer getUidDocumentoDaAggiornare() {
		return uidDocumentoDaAggiornare;
	}

	/**
	 * @param uidDocumentoDaAggiornare the uidDocumentoDaAggiornare to set
	 */
	public void setUidDocumentoDaAggiornare(Integer uidDocumentoDaAggiornare) {
		this.uidDocumentoDaAggiornare = uidDocumentoDaAggiornare;
	}

	/**
	 * @return the uidQuota
	 */
	public Integer getUidQuota() {
		return uidQuota;
	}

	/**
	 * @param uidQuota the uidQuota to set
	 */
	public void setUidQuota(Integer uidQuota) {
		this.uidQuota = uidQuota;
	}

	/**
	 * @return the tipoAvviso
	 */
	public TipoAvviso getTipoAvviso() {
		return tipoAvviso;
	}

	/**
	 * @param tipoAvviso the tipoAvviso to set
	 */
	public void setTipoAvviso(TipoAvviso tipoAvviso) {
		this.tipoAvviso = tipoAvviso;
	}

	/**
	 * @return the noteTesoriere
	 */
	public NoteTesoriere getNoteTesoriere() {
		return noteTesoriere;
	}

	/**
	 * @param noteTesoriere the noteTesoriere to set
	 */
	public void setNoteTesoriere(NoteTesoriere noteTesoriere) {
		this.noteTesoriere = noteTesoriere;
	}

	/**
	 * @return the causale770
	 */
	public Causale770 getCausale770() {
		return causale770;
	}

	/**
	 * @param causale770 the causale770 to set
	 */
	public void setCausale770(Causale770 causale770) {
		this.causale770 = causale770;
	}
	
	/**
	 * @return the tipoSommaNonSoggetta
	 */
	public CodiceSommaNonSoggetta getTipoSommaNonSoggetta() {
		return tipoSommaNonSoggetta;
	}

	/**
	 * @param tipoSommaNonSoggetta the tipoSommaNonSoggetta to set
	 */
	public void setTipoSommaNonSoggetta(CodiceSommaNonSoggetta tipoSommaNonSoggetta) {
		this.tipoSommaNonSoggetta = tipoSommaNonSoggetta;
	}

	/**
	 * @return the sedeSecondariaSoggetto
	 */
	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	/**
	 * @param sedeSecondariaSoggetto the sedeSecondariaSoggetto to set
	 */
	public void setSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
	}
	/**
	 * @return the listaCommissioniDocumento
	 */
	public List<CommissioniDocumento> getListaCommissioniDocumento() {
		return listaCommissioniDocumento;
	}
	
	/**
	 * @param listaCommissioniDocumento the listaCommissioniDocumento to set
	 */
	public void setListaCommissioniDocumento(List<CommissioniDocumento> listaCommissioniDocumento) {
		this.listaCommissioniDocumento = (listaCommissioniDocumento == null ? new ArrayList<CommissioniDocumento>() : listaCommissioniDocumento);
	}
	
	/**
	 * @return the listaCausale770
	 */
	public List<Causale770> getListaCausale770() {
		return listaCausale770;
	}
	
	/**
	 * @param listaCausale770 the listaCausale770 to set
	 */
	public void setListaCausale770(List<Causale770> listaCausale770) {
		this.listaCausale770 = (listaCausale770 == null ? new ArrayList<Causale770>() : listaCausale770);
	}
	
	/**
	 * @return the listaTipoDocumentoNote
	 */
	public List<TipoDocumento> getListaTipoDocumentoNote() {
		return listaTipoDocumentoNote;
	}

	/**
	 * @param listaTipoDocumentoNote the listaTipoDocumentoNote to set
	 */
	public void setListaTipoDocumentoNote(List<TipoDocumento> listaTipoDocumentoNote) {
		this.listaTipoDocumentoNote = listaTipoDocumentoNote != null ? listaTipoDocumentoNote : new ArrayList<TipoDocumento>();
	}

	/**
	 * @return the listaContoTesoreria
	 */
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria;
	}

	/**
	 * @param listaContoTesoreria the listaContoTesoreria to set
	 */
	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria != null ? listaContoTesoreria : new ArrayList<ContoTesoreria>();
	}
	/**
	 * @return the listaSedeSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}
	
	/**
	 * @param listaSedeSecondariaSoggetto the listaSedeSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = listaSedeSecondariaSoggetto != null ? listaSedeSecondariaSoggetto : new ArrayList<SedeSecondariaSoggetto>();
	}
	
	/**
	 * @return le sedi secondarie in stato VALIDO
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggettoValide() {
		List<SedeSecondariaSoggetto> lista = new ArrayList<SedeSecondariaSoggetto>();
		for(SedeSecondariaSoggetto sss : listaSedeSecondariaSoggetto) {
			if(StatoOperativoSedeSecondaria.VALIDO.equals(sss.getStatoOperativoSedeSecondaria())) {
				lista.add(sss);
			}
		}
		return lista;
	}
	/**
	 * @return the totaleDaPagareQuote
	 */
	public BigDecimal getTotaleDaPagareQuote() {
		return totaleDaPagareQuote;
	}

	/**
	 * @param totaleDaPagareQuote the totaleDaPagareQuote to set
	 */
	public void setTotaleDaPagareQuote(BigDecimal totaleDaPagareQuote) {
		this.totaleDaPagareQuote = totaleDaPagareQuote;
	}

	
	/**
	 * @return the totaleDaPagareQuoteSenzaOrdinativo
	 */
	public BigDecimal getTotaleDaPagareQuoteSenzaOrdinativo() {
		return totaleDaPagareQuoteSenzaOrdinativo;
	}

	/**
	 * @param totaleDaPagareQuoteSenzaOrdinativo the totaleDaPagareQuoteSenzaOrdinativo to set
	 */
	public void setTotaleDaPagareQuoteSenzaOrdinativo(
			BigDecimal totaleDaPagareQuoteSenzaOrdinativo) {
		this.totaleDaPagareQuoteSenzaOrdinativo = totaleDaPagareQuoteSenzaOrdinativo;
	}

	/**
	 * @return the totaleQuote
	 */
	public BigDecimal getTotaleQuote() {
		return totaleQuote;
	}

	/**
	 * @param totaleQuote the totaleQuote to set
	 */
	public void setTotaleQuote(BigDecimal totaleQuote) {
		this.totaleQuote = totaleQuote;
	}

//	/**
//	 * @return the noteCreditoDocumento
//	 */
//	public BigDecimal getNoteCreditoDocumento() {
//		return noteCreditoDocumento;
//	}
//
//	/**
//	 * @param noteCreditoDocumento the noteCreditoDocumento to set
//	 */
//	public void setNoteCreditoDocumento(BigDecimal noteCreditoDocumento) {
//		this.noteCreditoDocumento = noteCreditoDocumento;
//	}
	
	/**
	 * @return the importoDaDedurreSuFattura
	 */
	public BigDecimal getImportoDaDedurreSuFattura() {
		return importoDaDedurreSuFattura;
	}

	/**
	 * @param importoDaDedurreSuFattura the importoDaDedurreSuFattura to set
	 */
	public void setImportoDaDedurreSuFattura(BigDecimal importoDaDedurreSuFattura) {
		this.importoDaDedurreSuFattura = importoDaDedurreSuFattura;
	}

	/**
	 * @return the importoDaAttribuire
	 */
	public BigDecimal getImportoDaAttribuire() {
		return importoDaAttribuire;
	}

	/**
	 * @param importoDaAttribuire the importoDaAttribuire to set
	 */
	public void setImportoDaAttribuire(BigDecimal importoDaAttribuire) {
		this.importoDaAttribuire = importoDaAttribuire;
	}
	
	/**
	 * @return the totaleImportoDaDedurreSuFattura
	 */
	public BigDecimal getTotaleImportoDaDedurreSuFattura() {
		return totaleImportoDaDedurreSuFattura;
	}

	/**
	 * @param totaleImportoDaDedurreSuFattura the totaleImportoDaDedurreSuFattura to set
	 */
	public void setTotaleImportoDaDedurreSuFattura(
			BigDecimal totaleImportoDaDedurreSuFattura) {
		this.totaleImportoDaDedurreSuFattura = totaleImportoDaDedurreSuFattura;
	}

	/**
	 * @return the flagNoteCreditoAccessibile
	 */
	public Boolean getFlagNoteCreditoAccessibile() {
		return flagNoteCreditoAccessibile;
	}

	/**
	 * @param flagNoteCreditoAccessibile the flagNoteCreditoAccessibile to set
	 */
	public void setFlagNoteCreditoAccessibile(Boolean flagNoteCreditoAccessibile) {
		Boolean flagDaImpostare = flagNoteCreditoAccessibile == null ? Boolean.FALSE : flagNoteCreditoAccessibile;
		this.flagNoteCreditoAccessibile = flagDaImpostare;
	}
	
	/**
	 * @return the flagDatiIvaAccessibile
	 */
	public Boolean getFlagDatiIvaAccessibile() {
		return flagDatiIvaAccessibile;
	}

	/**
	 * @param flagDatiIvaAccessibile the flagDatiIvaAccessibile to set
	 */
	public void setFlagDatiIvaAccessibile(Boolean flagDatiIvaAccessibile) {
		this.flagDatiIvaAccessibile = flagDatiIvaAccessibile;
	}

	
	/**
	 * @return the ingressoTabQuote
	 */
	public Boolean getIngressoTabQuote() {
		return ingressoTabQuote;
	}

	/**
	 * @param ingressoTabQuote the ingressoTabQuote to set
	 */
	public void setIngressoTabQuote(Boolean ingressoTabQuote) {
		Boolean flagDaImpostare = ingressoTabQuote == null ? Boolean.FALSE : ingressoTabQuote;
		this.ingressoTabQuote = flagDaImpostare;
	}
	
	/**
	 * @return the figlioDiDocumentoEntrata
	 */
	public Boolean getFiglioDiDocumentoEntrata() {
		return figlioDiDocumentoEntrata;
	}

	/**
	 * @param figlioDiDocumentoEntrata the figlioDiDocumentoEntrata to set
	 */
	public void setFiglioDiDocumentoEntrata(Boolean figlioDiDocumentoEntrata) {
		Boolean flagDaImpostare = figlioDiDocumentoEntrata == null ? Boolean.FALSE : figlioDiDocumentoEntrata;
		this.figlioDiDocumentoEntrata = flagDaImpostare;
	}

	/**
	 * @return the capitoloRilevanteIvaVisibile
	 */
	public Boolean getCapitoloRilevanteIvaVisibile() {
		return capitoloRilevanteIvaVisibile;
	}

	/**
	 * @param capitoloRilevanteIvaVisibile the capitoloRilevanteIvaVisibile to set
	 */
	public void setCapitoloRilevanteIvaVisibile(Boolean capitoloRilevanteIvaVisibile) {
		Boolean flagDaImpostare = capitoloRilevanteIvaVisibile == null ? Boolean.FALSE : capitoloRilevanteIvaVisibile;
		this.capitoloRilevanteIvaVisibile = flagDaImpostare;
	}

	/**
	 * @return the importoQuotaDisabilitato
	 */
	public Boolean getImportoQuotaDisabilitato() {
		return importoQuotaDisabilitato;
	}

	/**
	 * @param importoQuotaDisabilitato the importoQuotaDisabilitato to set
	 */
	public void setImportoQuotaDisabilitato(Boolean importoQuotaDisabilitato) {
		Boolean flagDaImpostare = importoQuotaDisabilitato == null ? Boolean.FALSE : importoQuotaDisabilitato;
		this.importoQuotaDisabilitato = flagDaImpostare;
	}
	
	/**
	 * @return the impegnoQuotaDisabilitato
	 */
	public Boolean getImpegnoQuotaDisabilitato() {
		return impegnoQuotaDisabilitato;
	}

	/**
	 * @param impegnoQuotaDisabilitato the impegnoQuotaDisabilitato to set
	 */
	public void setImpegnoQuotaDisabilitato(Boolean impegnoQuotaDisabilitato) {
		Boolean flagDaImpostare = impegnoQuotaDisabilitato == null ? Boolean.FALSE : impegnoQuotaDisabilitato;
		this.impegnoQuotaDisabilitato = flagDaImpostare;
	}
	
	/**
	 * @return the provvedimentoQuotaDisabilitato
	 */
	public Boolean getProvvedimentoQuotaDisabilitato() {
		return provvedimentoQuotaDisabilitato;
	}

	/**
	 * @param provvedimentoQuotaDisabilitato the provvedimentoQuotaDisabilitato to set
	 */
	public void setProvvedimentoQuotaDisabilitato(Boolean provvedimentoQuotaDisabilitato) {
		Boolean flagDaImpostare = provvedimentoQuotaDisabilitato == null ? Boolean.FALSE : provvedimentoQuotaDisabilitato;
		this.provvedimentoQuotaDisabilitato = flagDaImpostare;
	}
	
	/**
	 * @return the capitoloQuotaDisabilitato
	 */
	public Boolean getCapitoloQuotaDisabilitato() {
		return capitoloQuotaDisabilitato;
	}

	/**
	 * @param capitoloQuotaDisabilitato the capitoloQuotaDisabilitato to set
	 */
	public void setCapitoloQuotaDisabilitato(Boolean capitoloQuotaDisabilitato) {
		this.capitoloQuotaDisabilitato = capitoloQuotaDisabilitato;
	}

	/**
	 * @return the rigaDaEliminare
	 */
	public Integer getRigaDaEliminare() {
		return rigaDaEliminare;
	}

	/**
	 * @param rigaDaEliminare the rigaDaEliminare to set
	 */
	public void setRigaDaEliminare(Integer rigaDaEliminare) {
		this.rigaDaEliminare = rigaDaEliminare;
	}

	/**
	 * @return the uidNotaCredito
	 */
	public Integer getUidNotaCredito() {
		return uidNotaCredito;
	}

	/**
	 * @param uidNotaCredito the uidNotaCredito to set
	 */
	public void setUidNotaCredito(Integer uidNotaCredito) {
		this.uidNotaCredito = uidNotaCredito;
	}

	/**
	 * @return the progressivoNumeroSubdocumento
	 */
	public Integer getProgressivoNumeroSubdocumento() {
		return progressivoNumeroSubdocumento;
	}

	/**
	 * @param progressivoNumeroSubdocumento the progressivoNumeroSubdocumento to set
	 */
	public void setProgressivoNumeroSubdocumento(Integer progressivoNumeroSubdocumento) {
		Integer numeroDaImpostare = progressivoNumeroSubdocumento == null ? Integer.valueOf(0) : progressivoNumeroSubdocumento;
		this.progressivoNumeroSubdocumento = numeroDaImpostare;
	}
	

	/**
	 * @return the totaleQuoteNotaCredito
	 */
	public BigDecimal getTotaleQuoteNotaCredito() {
		return totaleQuoteNotaCredito;
	}

	/**
	 * @param totaleQuoteNotaCredito the totaleQuoteNotaCredito to set
	 */
	public void setTotaleQuoteNotaCredito(BigDecimal totaleQuoteNotaCredito) {
		this.totaleQuoteNotaCredito = totaleQuoteNotaCredito;
	}

	
	/**
	 * @return the totaleNoteCredito
	 */
	public BigDecimal getTotaleNoteCredito() {
		return totaleNoteCredito;
	}

	/**
	 * @param totaleNoteCredito the totaleNoteCredito to set
	 */
	public void setTotaleNoteCredito(BigDecimal totaleNoteCredito) {
		this.totaleNoteCredito = totaleNoteCredito;
	}

	/**
	 * @return the totaleImportoDaDedurre
	 */
	public BigDecimal getTotaleImportoDaDedurre() {
		return totaleImportoDaDedurre;
	}

	/**
	 * @param totaleImportoDaDedurre the totaleImportoDaDedurre to set
	 */
	public void setTotaleImportoDaDedurre(BigDecimal totaleImportoDaDedurre) {
		this.totaleImportoDaDedurre = totaleImportoDaDedurre;
	}
	
	/**
	 * @return the oldNetto
	 */
	public BigDecimal getOldNetto() {
		return oldNetto;
	}

	/**
	 * @param oldNetto the oldNetto to set
	 */
	public void setOldNetto(BigDecimal oldNetto) {
		this.oldNetto = oldNetto != null ? oldNetto : BigDecimal.ZERO;
	}
	
	/**
	 * @return the oldArrotondamento
	 */
	public BigDecimal getOldArrotondamento() {
		return oldArrotondamento;
	}

	/**
	 * @param oldArrotondamento the oldArrotondamento to set
	 */
	public void setOldArrotondamento(BigDecimal oldArrotondamento) {
		this.oldArrotondamento = oldArrotondamento != null ? oldArrotondamento : BigDecimal.ZERO;
	}

	/**
	 * @return the tipoInserimentoDatiIva
	 */
	public String getTipoInserimentoDatiIva() {
		return tipoInserimentoDatiIva;
	}

	/**
	 * @param tipoInserimentoDatiIva the tipoInserimentoDatiIva to set
	 */
	public void setTipoInserimentoDatiIva(String tipoInserimentoDatiIva) {
		this.tipoInserimentoDatiIva = tipoInserimentoDatiIva;
	}

	/**
	 * @return the uidSubdocumentoIva
	 */
	public Integer getUidSubdocumentoIva() {
		return uidSubdocumentoIva;
	}

	/**
	 * @param uidSubdocumentoIva the uidSubdocumentoIva to set
	 */
	public void setUidSubdocumentoIva(Integer uidSubdocumentoIva) {
		this.uidSubdocumentoIva = uidSubdocumentoIva;
	}

	/**
	 * @return the documentoIvaLegatoDocumentoPresente
	 */
	public Boolean getDocumentoIvaLegatoDocumentoPresente() {
		return documentoIvaLegatoDocumentoPresente;
	}
	
	/**
	 * @return the datiIvaAccessibileQuota
	 */
	public Boolean getDatiIvaAccessibileQuota() {
		return datiIvaAccessibileQuota;
	}

	/**
	 * @param datiIvaAccessibileQuota the datiIvaAccessibileQuota to set
	 */
	public void setDatiIvaAccessibileQuota(Boolean datiIvaAccessibileQuota) {
		this.datiIvaAccessibileQuota = datiIvaAccessibileQuota;
	}

	/**
	 * @return the azioneIvaGestibile
	 */
	public Boolean getAzioneIvaGestibile() {
		return azioneIvaGestibile;
	}

	/**
	 * @param azioneIvaGestibile the azioneIvaGestibile to set
	 */
	public void setAzioneIvaGestibile(Boolean azioneIvaGestibile) {
		this.azioneIvaGestibile = azioneIvaGestibile;
	}

	/**
	 * @param documentoIvaLegatoDocumentoPresente the documentoIvaLegatoDocumentoPresente to set
	 */
	public void setDocumentoIvaLegatoDocumentoPresente(Boolean documentoIvaLegatoDocumentoPresente) {
		this.documentoIvaLegatoDocumentoPresente = documentoIvaLegatoDocumentoPresente != null ? documentoIvaLegatoDocumentoPresente : Boolean.FALSE;
	}
	
	/**
	 * Verifica se siamo in entrata o spesa.
	 * 
	 * @param tipo il tipo di famiglia del documento
	 * 
	 * @return <code>true</code> nel caso si tratti di spesa, <code>false</code> altrimenti
	 */
	public Boolean checkTipoFamigliaSpesa(TipoFamigliaDocumento tipo) {
		return TipoFamigliaDocumento.SPESA.equals(tipo);
	}
	
	/**
	 * @return the proseguireConElaborazione
	 */
	public Boolean getProseguireConElaborazione() {
		return proseguireConElaborazione;
	}

	/**
	 * @param proseguireConElaborazione the proseguireConElaborazione to set
	 */
	public void setProseguireConElaborazione(Boolean proseguireConElaborazione) {
		this.proseguireConElaborazione = proseguireConElaborazione;
	}
	
	/**
	 * @return the proseguireConElaborazioneAttoAmministrativo
	 */
	public Boolean getProseguireConElaborazioneAttoAmministrativo() {
		return proseguireConElaborazioneAttoAmministrativo;
	}

	/**
	 * @param proseguireConElaborazioneAttoAmministrativo the proseguireConElaborazioneAttoAmministrativo to set
	 */
	public void setProseguireConElaborazioneAttoAmministrativo(Boolean proseguireConElaborazioneAttoAmministrativo) {
		this.proseguireConElaborazioneAttoAmministrativo = proseguireConElaborazioneAttoAmministrativo;
	}

	/**
	 * @return the flagConvalidaManualeQuota
	 */
	public String getFlagConvalidaManualeQuota() {
		return flagConvalidaManualeQuota;
	}

	/**
	 * @param flagConvalidaManualeQuota the flagConvalidaManualeQuota to set
	 */
	public void setFlagConvalidaManualeQuota(String flagConvalidaManualeQuota) {
		this.flagConvalidaManualeQuota = flagConvalidaManualeQuota;
	}

	/**
	 * Gets the validazione prima nota da documento.
	 *
	 * @return the validazionePrimaNotaDaDocumento
	 */
	public Boolean getValidazionePrimaNotaDaDocumento() {
		return validazionePrimaNotaDaDocumento;
	}

	/**
	 * Sets the validazione prima nota da documento.
	 *
	 * @param validazionePrimaNotaDaDocumento the validazionePrimaNotaDaDocumento to set
	 */
	public void setValidazionePrimaNotaDaDocumento(Boolean validazionePrimaNotaDaDocumento) {
		this.validazionePrimaNotaDaDocumento = validazionePrimaNotaDaDocumento;
	}

	/**
	 * Imposta la lista delle note di credito.
	 */
	public void impostaListaTipoDocumentoNoteCredito() {
		List<TipoDocumento> listaNote = new ArrayList<TipoDocumento>();
		for (TipoDocumento tipo : getListaTipoDocumento()) {
			if(tipo.isNotaCredito()) {
				listaNote.add(tipo);
			}
		}
		setListaTipoDocumentoNote(listaNote);
	}
	


	/**
	 * Ottiene i totali per le coerenze delle quote.
	 * @param <S> la tipizzazione del subdocumento
	 * @param subdoc      il subdocumento da cui ricavare il totale
	 * @param listaSubdoc la lista dei subdocumenti da cui ricavare il vecchio
	 * 
	 * @return il totale delle quote per l'inserimento
	 */
	public <S extends Subdocumento<?,?>> BigDecimal getTotaleCoerenzaAggiornamentoQuota(S subdoc, List<S> listaSubdoc) {
		BigDecimal tot = getTotaleDaPagareQuote().add(subdoc.getImporto()).subtract(getNetto());
		// Tolgo dal totale delle quote quello relativo al subdoc che voglio aggiornare
		S subdocVecchio = ComparatorUtils.searchByUid(listaSubdoc, subdoc);
		return tot.subtract(subdocVecchio.getImporto());
	}
	
	/**
	 * Controlla se esista almeno una quota rilevante Iva.
	 * @param <SD> la tipizzazione del subdocumento
	 * 
	 * @param listaSubdocumenti la lista dei subdocumenti
	 * 
	 * @return <code>true</code> se esiste almeno una quota rilevante iva; <code>false</code> in caso contrario
	 */ 
	protected <SD extends Subdocumento<?, ?>> boolean esisteAlmenoUnaQuotaRilevanteIva(List<SD> listaSubdocumenti) {
		for(SD sd : listaSubdocumenti) {
			if(Boolean.TRUE.equals(sd.getFlagRilevanteIVA())) {
				return true;
			}
		}
		return false;
	}
	
	/* *************************************************************************************************************************************** */

	/**
	 * Imposta il vecchio valore per il netto.
	 */
	public void impostaNettoFromOld() {
		
		impostaNetto(getOldNetto());
	}
	
	/**
	 * Calcola gli importi. Da overridare nelle superclassi
	 */
	public void calcoloImporti() {
		// Placeholder
	}
	
	/**
	 * Imposta se l'iva sia gestibile.
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * 
	 * @param documento         il documento
	 * @param listaSubdocumento la lista dei subdocumenti
	 */
	protected <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> void impostaFlagIvaGestibile(D documento, List<SD> listaSubdocumento) {
		TipoDocumento tipoDocumento = documento.getTipoDocumento();
		Boolean ivaGestibile = Boolean.TRUE.equals(tipoDocumento.getFlagIVA()) && Boolean.TRUE.equals(getAzioneIvaGestibile());
		setDatiIvaAccessibileQuota(ivaGestibile);
		setFlagDatiIvaAccessibile(Boolean.TRUE.equals(ivaGestibile) && esisteAlmenoUnaQuotaRilevanteIva(listaSubdocumento));
	}

	/**
	 * @return the uidDocumentoDaCollegare
	 */
	public Integer getUidDocumentoDaCollegare() {
		return uidDocumentoDaCollegare;
	}

	/**
	 * @param uidDocumentoDaCollegare the uidDocumentoDaCollegare to set
	 */
	public void setUidDocumentoDaCollegare(Integer uidDocumentoDaCollegare) {
		this.uidDocumentoDaCollegare = uidDocumentoDaCollegare;
	}

	/**
	 * @return the uidDocumentoDaScollegare
	 */
	public final Integer getUidDocumentoDaScollegare() {
		return uidDocumentoDaScollegare;
	}

	/**
	 * @param uidDocumentoDaScollegare the uidDocumentoDaScollegare to set
	 */
	public final void setUidDocumentoDaScollegare(Integer uidDocumentoDaScollegare) {
		this.uidDocumentoDaScollegare = uidDocumentoDaScollegare;
	}
	
	/**
	 * Gets the abilitato prima nota da finanziaria.
	 *
	 * @return the abilitato prima nota da finanziaria
	 */
	//SIAC-5333
	public boolean getAbilitatoPrimaNotaDaFinanziaria() {
		return "TRUE".equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
	}

	/**
	 * Gets the uid da completare.
	 *
	 * @return the uidDaCompletare
	 */
	public int getUidDaCompletare() {
		return uidDaCompletare;
	}

	/**
	 * Sets the uid da completare.
	 *
	 * @param uidDaCompletare the uidDaCompletare to set
	 */
	public void setUidDaCompletare(int uidDaCompletare) {
		this.uidDaCompletare = uidDaCompletare;
	}

	/**
	 * @return the strutturaCompetenteQuota
	 */
	public StrutturaAmministrativoContabile getStrutturaCompetenteQuota() {
		return strutturaCompetenteQuota;
	}

	/**
	 * @param strutturaCompetenteQuota the strutturaCompetenteQuota to set
	 */
	public void setStrutturaCompetenteQuota(StrutturaAmministrativoContabile strutturaCompetenteQuota) {
		this.strutturaCompetenteQuota = strutturaCompetenteQuota;
	}
	
}
