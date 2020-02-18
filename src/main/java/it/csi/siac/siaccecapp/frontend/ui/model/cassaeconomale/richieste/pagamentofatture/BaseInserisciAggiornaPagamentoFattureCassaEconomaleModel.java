/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.Function;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.Reductor;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe base di model per l'inserimento e l'aggiornamento del pagamento fatture.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
public abstract class BaseInserisciAggiornaPagamentoFattureCassaEconomaleModel extends BaseInserisciAggiornaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7040804546331729686L;
	
	// Step 1

	private Integer uidValutaEuro;
		
	private List<Valuta> listaValuta = new ArrayList<Valuta>();
	
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	//Stato Operativo Documento
	private List<StatoOperativoDocumento> listaStatoOperativoDocumento = new ArrayList<StatoOperativoDocumento>();

	private DocumentoSpesa documentoSpesa;
	private Soggetto soggetto;
	private String descrizioneDocumento;
	
	private ModalitaPagamentoSoggetto mpsDaQuota;
	
	

	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	// Lotto M
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	
	private BigDecimal importoPagato = BigDecimal.ZERO;
	private BigDecimal importoSplitReverse = BigDecimal.ZERO;


	/**
	 * @return the uidValutaEuro
	 */
	public Integer getUidValutaEuro() {
		return uidValutaEuro;
	}

	
	@Override
	public Boolean getMaySearchHR() {
		return Boolean.FALSE;
	}
	/**
	 * @param uidValutaEuro the uidValutaEuro to set
	 */
	public void setUidValutaEuro(Integer uidValutaEuro) {
		this.uidValutaEuro = uidValutaEuro;
	}

	
	/**
	 * @return the listaValuta
	 */
	public List<Valuta> getListaValuta() {
		return listaValuta;
	}

	/**
	 * @param listaValuta the listaValuta to set
	 */
	public void setListaValuta(List<Valuta> listaValuta) {
		this.listaValuta = listaValuta != null ? listaValuta : new ArrayList<Valuta>();
	}


	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}
	
	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}
	
	/**
	 * @return the listaStatoOperativoDocumento
	 */
	public List<StatoOperativoDocumento> getListaStatoOperativoDocumento() {
		return listaStatoOperativoDocumento;
	}

	/**
	 * @param listaStatoOperativoDocumento the listaStatoOperativoDocumento to set
	 */
	public void setListaStatoOperativoDocumento(
			List<StatoOperativoDocumento> listaStatoOperativoDocumento) {
		this.listaStatoOperativoDocumento = listaStatoOperativoDocumento;
	}

	/**
	 * @return the documentoSpesa
	 */
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}

	/**
	 * @param documentoSpesa the documentoSpesa to set
	 */
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}


	
	/**
	 * @return the descrizioneDocumento
	 */
	public String getDescrizioneDocumento() {
		setDescrizioneDocumento( computaDescrizioneDocumento(getDocumentoSpesa()));
		return descrizioneDocumento;
	}

	/**
	 * @param descrizioneDocumento the descrizioneDocumento to set
	 */
	public void setDescrizioneDocumento(String descrizioneDocumento) {
		this.descrizioneDocumento = descrizioneDocumento;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}

	/**
	 * @return the listaSubdocumentoSpesa
	 */
	public List<SubdocumentoSpesa> getListaSubdocumentoSpesa() {
		return listaSubdocumentoSpesa;
	}

	/**
	 * @param listaSubdocumentoSpesa the listaSubdocumentoSpesa to set
	 */
	public void setListaSubdocumentoSpesa(List<SubdocumentoSpesa> listaSubdocumentoSpesa) {
		this.listaSubdocumentoSpesa = listaSubdocumentoSpesa != null ? listaSubdocumentoSpesa : new ArrayList<SubdocumentoSpesa>();
	}
	
	
	/**
	 * @return the importoPagato
	 */
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}


	/**
	 * @param importoPagato the importoPagato to set
	 */
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}


	/**
	 * @return the importoSplitReverse
	 */
	public BigDecimal getImportoSplitReverse() {
		return importoSplitReverse;
	}


	/**
	 * @param importoSplitReverse the importoSplitReverse to set
	 */
	public void setImportoSplitReverse(BigDecimal importoSplitReverse) {
		this.importoSplitReverse = importoSplitReverse;
	}


	/**
	 * @return descrizioneDellaSpesaProposta
	 */
	public String getDescrizioneDellaSpesaProposta() {
		if(getDocumentoSpesa() == null || getDocumentoSpesa().getUid() == 0 || getListaSubdocumentoSpesa().isEmpty()) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		
		sb.append("Pagamento " )
			.append(getDocumentoSpesa().getTipoDocumento().getDescrizione())
			.append(" Numero ")
			.append(getDocumentoSpesa().getNumero());
		if (getDocumentoSpesa().getTipoDocumento().isFattura()) {
			sb.append(" ")
			.append(computeStringaQuote(getListaSubdocumentoSpesa()));
		}
		sb.append(" ")
			.append(" Beneficiario ")
			.append(getDocumentoSpesa().getSoggetto().getCodiceSoggetto())
			.append(" ")
			.append(getDocumentoSpesa().getSoggetto().getDenominazione());
		if (getDocumentoSpesa().getTipoDocumento().isNotaCredito() && getDocumentoSpesa().getListaDocumentiSpesaPadre()!=null && !getDocumentoSpesa().getListaDocumentiSpesaPadre().isEmpty()) {
			
			sb.append(" ");
			sb.append("Fatture di riferimento ");
			for(DocumentoSpesa fatPadre : getDocumentoSpesa().getListaDocumentiSpesaPadre()){
				sb.append(fatPadre.getNumero());
				sb.append("  ");
			}
		}
		return sb.toString();
	}
	

	/**
	 * Calcola la stringa delle quote.
	 * 
	 * @param quote le quote per cui calcolare la stringa
	 * 
	 * @return la stringa dei numeri delle quote, separati da virgola
	 */
	private String computeStringaQuote(List<SubdocumentoSpesa> quote) {
		final StringBuilder sb = new StringBuilder()
			.append(getListaSubdocumentoSpesa().size() == 1 ? "Quota" : "Quote")
			.append(" ")
			.append(joinNumeroQuote(quote));
		return sb.toString();
	}
	
	/**
	 * Effettua il join delle quote prendendo il numero.
	 *
	 * @param quote le quote per cui effettuare il join.
	 * @return i numeri delle quote, separati da virgola
	 */
	private String joinNumeroQuote(List<SubdocumentoSpesa> quote) {
		// Completamente inefficiente. Ma per una volta tanto volevo provare a programmare in maniera funzionale
		Collection<String> listaNumeroQuote = CollectionUtil.map(quote, new Function<SubdocumentoSpesa, String>() {
			@Override
			public String map(SubdocumentoSpesa source) {
				return source.getNumero().toString();
			}
		});
		return CollectionUtil.reduce(listaNumeroQuote, new Reductor<String, String>() {
			@Override
			public String reduce(String accumulator, String currentValue, int index, Collection<String> collection) {
				return accumulator + ", " + currentValue;
			}
		});
	}
	
	/**
	 * @return the descrizioneQuoteAssociate
	 */
	public String getDescrizioneQuoteAssociate() {
		return joinNumeroQuote(getListaSubdocumentoSpesa());
	}
	
	/**
	 * @return the rilevanteIVA
	 */
	public boolean isRilevanteIVA() {
		for(SubdocumentoSpesa ss : getListaSubdocumentoSpesa()) {
			if(Boolean.TRUE.equals(ss.getFlagRilevanteIVA())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Produice la corretta String da mostrare per la descrizione documento
	 * 
	 * @param documentoSpesa il documento di cui creare la descrizione
	 * @return la stringa che rappresenta la descrizione completa del documento di spesa
	 */
	protected String computaDescrizioneDocumento(DocumentoSpesa documentoSpesa) {
		if(documentoSpesa == null) {
			return "";
		}
		
		final String separator = "/";
		final StringBuilder sb = new StringBuilder();
		sb.append(documentoSpesa.getAnno())
			.append(separator)
			.append(documentoSpesa.getTipoDocumento().getCodice())
			.append(separator)
			.append(documentoSpesa.getNumero())
			.append(separator)
			.append(documentoSpesa.getSoggetto().getCodiceSoggetto())
			.append(separator)
			.append(documentoSpesa.getSoggetto().getDenominazione())
			.append(separator)
			.append(FormatUtils.formatDate(documentoSpesa.getDataEmissione()))
			.append(" - ")
			.append(computeStringaQuote(getListaSubdocumentoSpesa()));
		return sb.toString();
	}
	
	@Override
	public String getDenominazioneSoggetto() {
		if(getDocumentoSpesa() == null || getDocumentoSpesa().getSoggetto() == null) {
			return "";
		}
		Soggetto s = getDocumentoSpesa().getSoggetto();
		List<String> chunks = new ArrayList<String>();
		if(StringUtils.isNotBlank(s.getCodiceFiscale())) {
			chunks.add("CF: " + s.getCodiceFiscale());
		}
		if(StringUtils.isNotBlank(s.getPartitaIva())) {
			chunks.add("P.IVA: " + s.getPartitaIva());
		}
		return StringUtils.join(chunks, " - ");
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaValuta}.
	 * 
	 * @return la request creata
	 */
	public RicercaValuta creaRequestRicercaValuta() {
		RicercaValuta request = creaRequest(RicercaValuta.class);
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @param tipoFamigliaDocumento la famiglia del documento (Entrata / Spesa)
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) {
		RicercaTipoDocumento request = new RicercaTipoDocumento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setTipoFamDoc(tipoFamigliaDocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDocumentiCollegatiByDocumentoSpesa}.
	 * 
	 * @param uidDocumento l'uid del documento
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDocumentiCollegatiByDocumentoSpesa creaRequestRicercaDocumentiCollegatiByDocumentoSpesa(Integer uidDocumento) {
		RicercaDocumentiCollegatiByDocumentoSpesa request = new RicercaDocumentiCollegatiByDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(uidDocumento);
		request.setDocumentoSpesa(ds);
		return request;
	}
	
	/**
	 * 
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 * @param docSpesa il documento per cui creare la request
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoSpesa creaRequestRicercaQuoteByDocumentoSpesa(DocumentoSpesa docSpesa) {
		RicercaQuoteByDocumentoSpesa request = creaRequest(RicercaQuoteByDocumentoSpesa.class);
		
		request.setDocumentoSpesa(docSpesa);
		
		return request;
	}


	/**
	 * @return the mpsDaQuota
	 */
	public ModalitaPagamentoSoggetto getMpsDaQuota() {
		return mpsDaQuota;
	}


	/**
	 * @param mpsDaQuota the mpsDaQuota to set
	 */
	public void setMpsDaQuota(ModalitaPagamentoSoggetto mpsDaQuota) {
		this.mpsDaQuota = mpsDaQuota;
	}


}
