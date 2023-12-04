/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per l'inserimento del documento di entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 25/03/2014
 *
 */
public class InserisciDocumentoEntrataModel extends GenericDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7327741641707476224L;
	
	private DocumentoEntrata documento;
	
	private DocumentoEntrata documentoEntrataCollegato;
	private DocumentoSpesa documentoSpesaCollegato;
	private String classeDocumentoCollegato;
	
	private Integer uidDocumento;
	private Integer uidDocumentoCollegato;
	
	private Boolean flagSubordinato = Boolean.FALSE;
	private Boolean flagRegolarizzazione = Boolean.FALSE;
	private List<Integer> listaUidProvvisori = new ArrayList<Integer>();
	private BigDecimal totaleProvvisori;
	
	
	/** Costruttore vuoto di default */
	public InserisciDocumentoEntrataModel() {
		setTitolo("Inserimento Documenti entrata");
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
	}

	/**
	 * @return the documentoEntrataCollegato
	 */
	public DocumentoEntrata getDocumentoEntrataCollegato() {
		return documentoEntrataCollegato;
	}

	/**
	 * @param documentoEntrataCollegato the documentoEntrataCollegato to set
	 */
	public void setDocumentoEntrataCollegato(
			DocumentoEntrata documentoEntrataCollegato) {
		this.documentoEntrataCollegato = documentoEntrataCollegato;
	}

	/**
	 * @return the documentoSpesaCollegato
	 */
	public DocumentoSpesa getDocumentoSpesaCollegato() {
		return documentoSpesaCollegato;
	}

	/**
	 * @param documentoSpesaCollegato the documentoSpesaCollegato to set
	 */
	public void setDocumentoSpesaCollegato(DocumentoSpesa documentoSpesaCollegato) {
		this.documentoSpesaCollegato = documentoSpesaCollegato;
	}

	/**
	 * @return the classeDocumentoCollegato
	 */
	public String getClasseDocumentoCollegato() {
		return classeDocumentoCollegato;
	}

	/**
	 * @param classeDocumentoCollegato the classeDocumentoCollegato to set
	 */
	public void setClasseDocumentoCollegato(String classeDocumentoCollegato) {
		this.classeDocumentoCollegato = classeDocumentoCollegato;
	}

	/**
	 * @return the uidDocumento
	 */
	public Integer getUidDocumento() {
		return uidDocumento;
	}

	/**
	 * @param uidDocumento the uidDocumento to set
	 */
	public void setUidDocumento(Integer uidDocumento) {
		this.uidDocumento = uidDocumento;
	}
	
	/**
	 * @return the uidDocumentoCollegato
	 */
	public Integer getUidDocumentoCollegato() {
		return uidDocumentoCollegato;
	}

	/**
	 * @param uidDocumentoCollegato the uidDocumentoCollegato to set
	 */
	public void setUidDocumentoCollegato(Integer uidDocumentoCollegato) {
		this.uidDocumentoCollegato = uidDocumentoCollegato;
	}

	/**
	 * @return the flagSubordinato
	 */
	public Boolean getFlagSubordinato() {
		return flagSubordinato;
	}

	/**
	 * @param flagSubordinato the flagSubordinato to set
	 */
	public void setFlagSubordinato(Boolean flagSubordinato) {
		this.flagSubordinato = flagSubordinato;
	}

	/**
	 * @return the flagRegolarizzazione
	 */
	public Boolean getFlagRegolarizzazione() {
		return flagRegolarizzazione;
	}

	/**
	 * @param flagRegolarizzazione the flagRegolarizzazione to set
	 */
	public void setFlagRegolarizzazione(Boolean flagRegolarizzazione) {
		this.flagRegolarizzazione = flagRegolarizzazione;
	}
	
	/**
	 * @return the listaUidProvvisori
	 */
	public List<Integer> getListaUidProvvisori() {
		return listaUidProvvisori;
	}

	/**
	 * @param listaUidProvvisori the listaUidProvvisori to set
	 */
	public void setListaUidProvvisori(List<Integer> listaUidProvvisori) {
		this.listaUidProvvisori = listaUidProvvisori;
	}

	
	/**
	 * @return the totaleProvvisori
	 */
	public BigDecimal getTotaleProvvisori() {
		return totaleProvvisori;
	}

	/**
	 * @param totaleProvvisori the totaleProvvisori to set
	 */
	public void setTotaleProvvisori(BigDecimal totaleProvvisori) {
		this.totaleProvvisori = totaleProvvisori;
	}

	@Override
	public BigDecimal getNetto() {
		DocumentoEntrata documentoEntrata = getDocumento();
		if(documentoEntrata == null) {
			return super.getNetto();
		}
		BigDecimal netto = documentoEntrata.getImporto();
		BigDecimal arrotondamento = documentoEntrata.getArrotondamento();
		
		if(arrotondamento != null) {
			netto = netto.add(arrotondamento);
		}
		
		return netto;
	}

	/**
	 * Impostazione dei dati per l'esecuzione del metodo ripeti.
	 * 
	 * @param documento il documento precedentemente salvato
	 * @param soggetto  il soggetto precedentemento salvato
	 */
	public void impostaDatiRipeti(DocumentoEntrata documento, Soggetto soggetto) {
		setSoggetto(soggetto);
		
		// Pulisco i dati del documento
		documento.setAnno(null);
		documento.setNumero(null);
		documento.setDataEmissione(null);
		documento.setUid(0);
		//SIAC 6677
		documento.setIuv(null);
		//SIAC-7567
		documento.setCig(null);
		documento.setCup(null);
		//
		documento.setIuv(null);
		documento.setDataOperazione(null);
		setDocumento(documento);
	}

	/* Requests */
	/**
	 * Crea una request per il servizio di {@link InserisceDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public InserisceDocumentoEntrata creaRequestInserisceDocumentoEntrata() {
		InserisceDocumentoEntrata request = new InserisceDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoEntrata(creaDocumentoPerInserimento());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceDocumentoPerProvvisoriEntrata}.
	 * 
	 * @return la request creata
	 */
	public InserisceDocumentoPerProvvisoriEntrata creaRequestInserisceDocumentoPerProvvisoriEntrata() {
		InserisceDocumentoPerProvvisoriEntrata request = new InserisceDocumentoPerProvvisoriEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoEntrata(creaDocumentoPerInserimento());
		request.setListaQuote(creaSubdocumenti());
		request.setBilancio(getBilancio());
		
		return request;
	}


	/* Utility */
	/**
	 * Crea un Documento di entrata per l'inserimento dello stesso.
	 * 
	 * @return il documento con i dati correttamente popolati
	 */
	private DocumentoEntrata creaDocumentoPerInserimento() {
		DocumentoEntrata documentoEntrata = getDocumento();
		documentoEntrata.setEnte(getEnte());
		documentoEntrata.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		if(uidDocumentoCollegato != null) {
			// É presente un id di un documento. Effettuo il collegamento
			DocumentoSpesa padre = new DocumentoSpesa();
			padre.setUid(uidDocumentoCollegato.intValue());
			documentoEntrata.setListaDocumentiSpesaPadre(Arrays.asList(padre));
		}
		
		Soggetto soggetto = getSoggetto();
		
		if(soggetto != null && soggetto.getUid() != 0) {
			documentoEntrata.setSoggetto(soggetto);
		}
		
		//SIAC-7717 passo la FatturaFel
		if(getFatturaFEL() != null) {
			documentoEntrata.setFatturaFEL(getFatturaFEL());
		}
		
		return documentoEntrata;
	}
	
	/**
	 * Popola il model a partire dal Documento di entrata ottenuto dal servizio.
	 * 
	 * @param documentoEntrata il documento tramite cui popolare il model
	 */
	public void popolaModel(DocumentoEntrata documentoEntrata) {
		setDocumento(documentoEntrata);
	}
	
	/**
	 * setta i parametri di default come e' stato richiesta dalla CR-2831
	 * Si richiede nello step 2 dell'inserisci documento di pre-impostare 
	 * il campo Termine di pagamento con 60 giorni e di conseguenza calcolare la data scadenza 
	 */
	public void popolaParametriDiDefaultStep2() {
		
		documento.setTerminePagamento(BilConstants.TERMINE_PAGAMENTO.getId());
		//utilizzo la data emissione documento
		calcolaDataScadenzaDefault();	
	}
	
	/**
	 * CR-2831-2885: calcola la data di scadenza rispetto al valore di default per il termine di pagamento
	 */
	public void calcolaDataScadenzaDefault() {
		//valore di default per il termine di pagamento
		Integer termineScadenza = BilConstants.TERMINE_PAGAMENTO.getId();
		Date dataDiPartenza = getDocumento().getDataRicezionePortale() != null ? getDocumento().getDataRicezionePortale() :
			getDocumento().getDataRepertorio() != null ?  getDocumento().getDataRepertorio() : getDocumento().getDataEmissione();
		Date dataScadenza =  DateUtils.addDays(dataDiPartenza, termineScadenza.intValue());
		getDocumento().setDataScadenza(dataScadenza);
	}

	/**
	 * Crea una lista di subdocumenti di entrata da associare al documento
	 * 
	 * @return lista di quote con i dati correttamente popolati
	 */
	private List<SubdocumentoEntrata> creaSubdocumenti() {
		List<SubdocumentoEntrata> listaQuote = new ArrayList<SubdocumentoEntrata>();
		for(Integer uid : getListaUidProvvisori()){
			ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
			provvisorio.setUid(uid);
			SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
			subdoc.setProvvisorioCassa(provvisorio);
			listaQuote.add(subdoc);
		}
		return listaQuote;
	}


}
