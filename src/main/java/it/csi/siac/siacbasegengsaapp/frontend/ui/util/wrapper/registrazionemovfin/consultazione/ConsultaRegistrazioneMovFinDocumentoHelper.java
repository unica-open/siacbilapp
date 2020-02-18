/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegatoFactory;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva.ElementoMovimentoIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * Helper per la consultazione dei dati del movimento di gestione
 * @author Marchino Alessandro
 * @param <D> la tipizzazione del documento
 * @param <S> la tipizzazione del subdocumento
 * @param <SI> la tipizzazione del subdocumento iva
 *
 */
public abstract class ConsultaRegistrazioneMovFinDocumentoHelper<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>>
		extends ConsultaRegistrazioneMovFinBaseHelper<D> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	/** Il documento */
	protected final D documento;

	private final BigDecimal netto;
	private final BigDecimal totaleQuote;
	private final BigDecimal totaleImportoDaDedurre;
	private final BigDecimal totaleDaPagareQuote;
	private final BigDecimal totaleImportoDaAttribuire;
	private final BigDecimal totaleImportoDocumentiCollegati;
	
	private final List<S> listaQuote;
	
	private final boolean docCollegato;
	
	/** La lista delle quote del documento collegato */
	protected final List<S> listaQuoteDocCollegato = new ArrayList<S>();
	/** La lista dei documenti collegati */
	protected final List<ElementoDocumentoCollegato> listaDocumentiCollegati = new ArrayList<ElementoDocumentoCollegato>();
	/** La lista dei movimenti iva */
	protected final List<ElementoMovimentoIva<D, S, SI>> listaMovimentiIva = new ArrayList<ElementoMovimentoIva<D, S, SI>>();
	/** La lista dei movimenti iva del documento collegato */
	protected final List<ElementoMovimentoIva<D, S, SI>> listaMovimentiIvaDocCollegato = new ArrayList<ElementoMovimentoIva<D, S, SI>>();
	
	/**
	 * Costruttore di wrap
	 * @param documento il documento
	 * @param listaQuote la lista delle quote
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	protected ConsultaRegistrazioneMovFinDocumentoHelper(D documento, List<S> listaQuote, boolean isGestioneUEB) {
		super(isGestioneUEB);
		this.documento = documento;
		this.listaQuote = listaQuote;
		
		this.netto = initNetto();
		this.totaleQuote = initTotaleQuote();
		this.totaleImportoDaDedurre = initTotaleImportoDaDedurre();
		this.totaleDaPagareQuote = initTotaleDaPagareQuote();
		this.totaleImportoDaAttribuire = initTotaleImportoDaAttribuire();
		this.totaleImportoDocumentiCollegati = initTotaleImportoDocumentiCollegati();
		this.docCollegato = initDocCollegato();
		
		initListaDocumentiCollegati();
		initDatiIva();
		
		documento.setImportoTotaleDaDedurreSuFatturaNoteCollegate(getTotaleNote());
	}
	
	/**
	 * @return the documento
	 */
	public D getDocumento() {
		return this.documento;
	}

	/**
	 * @return the netto
	 */
	public BigDecimal getNetto() {
		return this.netto;
	}

	/**
	 * @return the totaleQuote
	 */
	public BigDecimal getTotaleQuote() {
		return this.totaleQuote;
	}

	/**
	 * @return the totaleImportoDaDedurre
	 */
	public BigDecimal getTotaleImportoDaDedurre() {
		return this.totaleImportoDaDedurre;
	}

	/**
	 * @return the totaleDaPagareQuote
	 */
	public BigDecimal getTotaleDaPagareQuote() {
		return this.totaleDaPagareQuote;
	}

	/**
	 * @return the totaleImportoDaAttribuire
	 */
	public BigDecimal getTotaleImportoDaAttribuire() {
		return this.totaleImportoDaAttribuire;
	}

	/**
	 * @return the totaleImportoDocumentiCollegati
	 */
	public BigDecimal getTotaleImportoDocumentiCollegati() {
		return this.totaleImportoDocumentiCollegati;
	}

	/**
	 * @return the listaQuote
	 */
	public List<S> getListaQuote() {
		return this.listaQuote;
	}

	/**
	 * @return the listaQuoteDocCollegato
	 */
	public List<S> getListaQuoteDocCollegato() {
		return this.listaQuoteDocCollegato;
	}

	/**
	 * @return the listaDocumentiCollegati
	 */
	public List<ElementoDocumentoCollegato> getListaDocumentiCollegati() {
		return this.listaDocumentiCollegati;
	}

	/**
	 * @return the listaMovimentiIva
	 */
	public List<ElementoMovimentoIva<D, S, SI>> getListaMovimentiIva() {
		return this.listaMovimentiIva;
	}

	/**
	 * @return the listaMovimentiIvaDocCollegato
	 */
	public List<ElementoMovimentoIva<D, S, SI>> getListaMovimentiIvaDocCollegato() {
		return this.listaMovimentiIvaDocCollegato;
	}
	
	@Override
	public String getDatiCreazioneModifica() {
		return calcolaDatiCreazioneModifica(documento.getDataCreazione(), documento.getLoginCreazione(), documento.getDataModifica(), documento.getLoginModifica());
	}
	
	/**
	 * @return the datiSoggetto
	 */
	public String getDatiSoggetto() {
		if(documento == null || documento.getSoggetto() == null) {
			return "";
		}
		return calcolaDatiSoggetto(documento.getSoggetto());
	}
	
	/**
	 * @return the datiImporti
	 */
	public String getDatiImporti() {
		List<String> chunks = new ArrayList<String>();
		
		chunks.add(FormatUtils.formatCurrency(documento.getImporto()));
		chunks.add("Arrotondamento: " + FormatUtils.formatCurrency(documento.getArrotondamento()));
		chunks.add("Netto: " + FormatUtils.formatCurrency(netto));
		chunks.add("Totale quote: " + FormatUtils.formatCurrency(totaleQuote));
		chunks.add("Importo da attribuire: " + FormatUtils.formatCurrency(totaleImportoDaAttribuire));
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the dataNumeroProtocollo
	 */
	public String getDataNumeroProtocollo() {
		List<String> chunks = new ArrayList<String>();
		if(documento.getDataRepertorio() != null) {
			chunks.add(FormatUtils.formatDate(documento.getDataRepertorio()));
		}
		if(documento.getNumeroRepertorio() != null) {
			chunks.add(documento.getNumeroRepertorio());
		}
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * Controlla se vi sono movimenti IVA valorizzati
	 * @return <code>true</code> se vi sono movimenti IVA, <code>false</code> altrimenti
	 */
	public boolean isConMovimentiIvaValorizzati() {
		return !listaMovimentiIva.isEmpty();
	}
	
	/**
	 * Controlla se vi sono movimenti IVA per il documento collegato valorizzati
	 * @return <code>true</code> se vi sono movimenti IVA per il documento collegato, <code>false</code> altrimenti
	 */
	public boolean isConMovimentiIvaDocCollegatoValorizzati() {
		return !listaMovimentiIvaDocCollegato.isEmpty();
	}
	
	/**
	 * Ottiene il numero delle quote associate al documento.
	 *
	 * @return il numero
	 */
	public int getNumeroQuote(){
		return listaQuote.size();
	}
	
	/**
	 * @return the totaleRilevanteIva
	 */
	public BigDecimal getTotaleRilevanteIva(){
		BigDecimal totale = BigDecimal.ZERO;
		for (S quota : listaQuote) {
			if(quota != null && quota.getImporto() != null && Boolean.TRUE.equals(quota.getFlagRilevanteIVA())) {
				totale = totale.add(defaultImporto(quota.getImporto()));
			}
		}
		return totale;
	}
	
	/**
	 * @return the totaleNonRilevanteIva
	 */
	public BigDecimal getTotaleNonRilevanteIva(){
		if(documento == null || documento.getImporto() == null){
			return BigDecimal.ZERO;
		}
		return documento.getImporto().subtract(getTotaleRilevanteIva());
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @return the totale netto
	 */
	public abstract BigDecimal getTotaleNote();
	/**
	 * Restitusice il default per l'importo
	 * @param importo l'importo per cui ottenere il default
	 * @return l'importo
	 */
	protected BigDecimal defaultImporto(BigDecimal importo) {
		return importo != null ? importo : BigDecimal.ZERO;
	}
	
	/**
	 * Inizializzazione del totale importo documenti collegati
	 * @return il totale importo documenti collegati
	 */
	protected abstract BigDecimal initTotaleImportoDocumentiCollegati();
	/**
	 * Inizializzazione dell'informazione di collegamento
	 * @return se &eacute; collegato
	 */
	protected abstract boolean initDocCollegato();
	/**
	 * Istanziazione del wrapper per il movimento iva.
	 * 
	 * @param subdocumentoIva il subdocumento iva da wrappare
	 * @param aliquotaIva     l'aliquota iva da wrappare
	 * @return il wrapper
	 */
	protected abstract ElementoMovimentoIva<D, S, SI> istanziaWrapperMovimentoIva(SI subdocumentoIva, AliquotaSubdocumentoIva aliquotaIva);
	
	/**
	 * Inizializzazione del netto
	 * @return il netto
	 */
	private BigDecimal initNetto() {
		BigDecimal importo = defaultImporto(documento.getImporto());
		BigDecimal arrotondamento = defaultImporto(documento.getArrotondamento());
		BigDecimal totaleNote = getTotaleNote();

		return importo.add(arrotondamento).subtract(totaleNote);
	
	}
	/**
	 * Inizializzazione dei totali delle quote
	 * @return il totale quote
	 */
	private BigDecimal initTotaleQuote() {
		BigDecimal totale = BigDecimal.ZERO;
		for(S s: listaQuote){
			totale = totale.add(defaultImporto(s.getImporto()));
		}
		return totale;
	}
	/**
	 * Inizializzazione del totale importo da dedurre
	 * @return il totale importo da dedurre
	 */
	private BigDecimal initTotaleImportoDaDedurre() {
		BigDecimal totale = BigDecimal.ZERO;
		for(S s: getListaQuote()){
			totale = totale.add(defaultImporto(s.getImportoDaDedurre()));
		}
		return totale;
	}
	/**
	 * Inizializzazione del totale da pagare
	 * @return il totale da pagare
	 */
	private BigDecimal initTotaleDaPagareQuote() {
		return totaleQuote.subtract(totaleImportoDaDedurre);
	}
	/**
	 * Inizializzazione del totale importo da attribuire
	 * @return il totale da attribuire
	 */
	private BigDecimal initTotaleImportoDaAttribuire() {
		BigDecimal importo = defaultImporto(documento.getImporto());
		BigDecimal arrotondamento = defaultImporto(documento.getArrotondamento());
		
		return importo.add(arrotondamento).subtract(totaleQuote);
	}
	/**
	 * Inizializzazione della lista dei documenti collegati
	 */
	private void initListaDocumentiCollegati() {
		listaDocumentiCollegati.addAll(ElementoDocumentoCollegatoFactory.getInstances(documento.getListaDocumentiSpesaFiglio()));
		listaDocumentiCollegati.addAll(ElementoDocumentoCollegatoFactory.getInstances(documento.getListaDocumentiEntrataFiglio()));
		//jira SIAC-1919
		if(isCodiceNotaCredito()) {
			listaDocumentiCollegati.addAll(ElementoDocumentoCollegatoFactory.getInstances(documento.getListaDocumentiSpesaPadre()));
			listaDocumentiCollegati.addAll(ElementoDocumentoCollegatoFactory.getInstances(documento.getListaDocumentiEntrataPadre()));
		}
	}
	/**
	 * @return se il codice del documento sia nota di credito/accredito
	 */
	protected abstract boolean isCodiceNotaCredito();

	/**
	 * Calcola il totale dei documenti di entrata.
	 *
	 * @return il totale
	 */
	protected BigDecimal calcolaTotaleDocumentiDiEntrata() {
		BigDecimal totale = BigDecimal.ZERO;
		for(DocumentoEntrata s: documento.getListaDocumentiEntrataFiglio()){
			if(s != null && !StatoOperativoDocumento.ANNULLATO.equals(s.getStatoOperativoDocumento())){
				totale = totale.add(defaultImporto(s.getImporto()));
			}
		}
		return totale;
	}

	/**
	 * Calcola il totale dei documenti di spesa.
	 *
	 * @return il totale
	 */
	protected BigDecimal calcolaTotaleDocumentiDiSpesa() {
		BigDecimal totale = BigDecimal.ZERO;
		for(DocumentoSpesa s: documento.getListaDocumentiSpesaFiglio()){
			if(s != null && !StatoOperativoDocumento.ANNULLATO.equals(s.getStatoOperativoDocumento())){
				totale = totale.add(defaultImporto(s.getImporto()));
			}
		}
		return totale;
	}
	
	/**
	 * Impostazione dei dati iva.
	 */
	private void initDatiIva() {
		List<SI> listaSubdocumentoIvaQuote = new ArrayList<SI>();
		for(S quota : listaQuote) {
			if(quota.getSubdocumentoIva() != null) {
				listaSubdocumentoIvaQuote.add(quota.getSubdocumentoIva());
			}
		}
		
		// Unisco i dati del documento a quelli delle quote
		List<SI> listaSubdocIva = new ArrayList<SI>();
		listaSubdocIva.addAll(documento.getListaSubdocumentoIva());
		listaSubdocIva.addAll(listaSubdocumentoIvaQuote);
		
		List<ElementoMovimentoIva<D, S, SI>> listaDaPopolare = docCollegato ? listaMovimentiIvaDocCollegato : listaMovimentiIva;
		impostaDatiIvaNellaListaWrapper(listaSubdocIva, listaDaPopolare);
	}
	
	/**
	 * Impostazione dei dati IVA nei wrapper.
	 * @param listaSubdocIva la lista dei subdoc iva
	 * @param listaWrapper la lista dei wrapper in cui impostare i dati
	 */
	protected void impostaDatiIvaNellaListaWrapper(List<SI> listaSubdocIva, List<ElementoMovimentoIva<D, S, SI>> listaWrapper) {
		if(listaSubdocIva == null) {
			return;
		}
		for(SI sIva : listaSubdocIva){
			for(AliquotaSubdocumentoIva aIva : sIva.getListaAliquotaSubdocumentoIva()){
				ElementoMovimentoIva<D, S, SI> mov = istanziaWrapperMovimentoIva(sIva, aIva);
				listaWrapper.add(mov);
			}
		}
	}
	

}
