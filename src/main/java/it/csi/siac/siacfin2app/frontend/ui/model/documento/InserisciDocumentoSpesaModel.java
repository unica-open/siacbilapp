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

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Ordine;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.sirfelser.model.OrdineAcquistoFEL;

/**
 * Classe di model per l'inserimento del documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/03/2014
 *
 */
public class InserisciDocumentoSpesaModel extends GenericDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7606443361330348840L;
	
	private DocumentoEntrata documentoEntrataCollegato;
	private DocumentoSpesa documentoSpesaCollegato;
	private String classeDocumentoCollegato;

	private Integer uidDocumento;
	
	private Integer uidDocumentoCollegato;
	
	private Boolean flagSubordinato = Boolean.FALSE;
	private Boolean flagRegolarizzazione = Boolean.FALSE;
	
	private List<Integer> listaUidProvvisori = new ArrayList<Integer>();
	private BigDecimal totaleProvvisori;
	
	// SIAC-4680
	private String nomeAzioneSAC;
	
	//SIAC-5346
	private boolean inibisciModificaDatiImportatiFEL;

	//SIAC-6584
	private int numeroOrdinativoSenzaNumero;
	

	/** Costruttore vuoto di default */
	public InserisciDocumentoSpesaModel() {
		setTitolo("Inserimento Documenti di Spesa");
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
	public void setDocumentoEntrataCollegato(DocumentoEntrata documentoEntrataCollegato) {
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
	 * @param classeDocumentoCollegato the classeDocumentoCollegato to set (Entrata / Spesa)
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
	
	/**
	 * @return the nomeAzioneSAC
	 */
	public String getNomeAzioneSAC() {
		return nomeAzioneSAC;
	}

	/**
	 * @param nomeAzioneSAC the nomeAzioneSAC to set
	 */
	public void setNomeAzioneSAC(String nomeAzioneSAC) {
		this.nomeAzioneSAC = nomeAzioneSAC;
	}

	/**
	 * Checks if is dati fel disabilitati.
	 *
	 * @return the datiFelDisabilitati
	 */
	public boolean isInibisciModificaDatiImportatiFEL() {
		return inibisciModificaDatiImportatiFEL;
	}

	/**
	 * Sets the dati fel disabilitati.
	 *
	 * @param inibisciModificaDatiImportatiFEL the new inibisci modifica dati importati FEL
	 */
	public void setInibisciModificaDatiImportatiFEL(boolean inibisciModificaDatiImportatiFEL) {
		this.inibisciModificaDatiImportatiFEL = inibisciModificaDatiImportatiFEL;
	}
	
	@Override
	public BigDecimal getNetto() {
		DocumentoSpesa documento = getDocumento();
		if(documento == null) {
			return super.getNetto();
		}
		BigDecimal netto = documento.getImporto();
		BigDecimal arrotondamento = documento.getArrotondamento();
		
		if(arrotondamento != null) {
			netto = netto.add(arrotondamento);
		}
		
		return netto;
	}
	
	/**
	 * @return the codicePccObbligatorio
	 */
	public boolean isCodicePccObbligatorio() {
		return getDocumento() != null && getDocumento().getTipoDocumento() != null && Boolean.TRUE.equals(getDocumento().getTipoDocumento().getFlagComunicaPCC());
	}
	
	/**
	 * @return the tipoDocumentoSiopeAnalogico
	 */
	public boolean isTipoDocumentoSiopeAnalogico() {
		// Deve avere codice 'A'
		return getDocumento() != null
				&& getDocumento().getSiopeDocumentoTipo() != null
				&& BilConstants.CODICE_SIOPE_DOCUMENTO_TIPO_ANALOGICO.getConstant().equals(getDocumento().getSiopeDocumentoTipo().getCodice());
	}
	
	/**
	 * Checks if is importo documento maggiore di zero.
	 *
	 * @return true, if is importo documento maggiore di zero
	 */
	public boolean isImportoDocumentoMinoreUgualeZero() {
		return getDocumento() == null || getDocumento().getImporto() == null || BigDecimal.ZERO.compareTo(getDocumento().getImporto()) <0;
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link InserisceDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public InserisceDocumentoSpesa creaRequestInserisceDocumentoSpesa() {
		InserisceDocumentoSpesa request = new InserisceDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoSpesa(creaDocumentoPerInserimento());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea un Documento di spesa per l'inserimento dello stesso.
	 * 
	 * @return il documento con i dati correttamente popolati
	 */
	private DocumentoSpesa creaDocumentoPerInserimento() {
		DocumentoSpesa documento = getDocumento();
		documento.setSoggetto(getSoggetto());
		documento.setEnte(getEnte());
		documento.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		// SIAC-4679: non passo la SAC se l'uid non e' valorizzato (il controllo DEVE essere presente anche su backend)
		documento.setTipoImpresa(impostaEntitaFacoltativa(documento.getTipoImpresa()));
		documento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(documento.getStrutturaAmministrativoContabile()));
		
		if(uidDocumentoCollegato != null) {
			// É presente un id di un documento. Effettuo il collegamento
			DocumentoEntrata padre = new DocumentoEntrata();
			padre.setUid(uidDocumentoCollegato.intValue());
			documento.setListaDocumentiEntrataPadre(Arrays.asList(padre));
		}
		
		if(getFatturaFEL() != null && getFatturaFEL().getIdFattura() != null) {
			documento.setFatturaFEL(getFatturaFEL());
			impostaOrdini(documento, getFatturaFEL().getOrdiniAcquisti());
		}
		
		return documento;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceDocumentoPerProvvisoriSpesa}.
	 * 
	 * @return la request creata
	 */
	public InserisceDocumentoPerProvvisoriSpesa creaRequestInserisceDocumentoPerProvvisoriSpesa() {
		InserisceDocumentoPerProvvisoriSpesa request = new InserisceDocumentoPerProvvisoriSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoSpesa(creaDocumentoPerInserimento());
		request.setListaQuote(creaSubdocumenti());
		request.setBilancio(getBilancio());
		
		return request;
	}

	
	/**
	 * Impostazione degli ordini per il documento a partire dalla fattura FEL.
	 * 
	 * @param documento         il documento da popolare
	 * @param ordiniAcquistoFEL la lista degli ordini FEL
	 */
	private void impostaOrdini(DocumentoSpesa documento, List<OrdineAcquistoFEL> ordiniAcquistoFEL) {
		// Se non ho ordini, esco
		numeroOrdinativoSenzaNumero = 0;
		if(ordiniAcquistoFEL == null || ordiniAcquistoFEL.isEmpty()) {
			return;
		}
		List<Ordine> ordini = new ArrayList<Ordine>();
		// Per ogni ordine
		for(OrdineAcquistoFEL ordineAcquistoFEL : ordiniAcquistoFEL) {
			
			String numdoc = ordineAcquistoFEL.getNumeroDocumento();
			if(StringUtils.isNotBlank(numdoc)){
				// Creo il numero di ordine come il numero documento
				Ordine ordine = new Ordine();
				ordine.setNumeroOrdine(numdoc);
				ordini.add(ordine);
			}else{
				numeroOrdinativoSenzaNumero++;
			}
			
			
		}
		
		documento.setOrdini(ordini);
	}

	/**
	 * Impostazione dei dati per l'esecuzione del metodo ripeti.
	 * 
	 * @param documento il documento precedentemente salvato
	 * @param soggetto  il soggetto precedentemento salvato
	 */
	public void impostaDatiRipeti(DocumentoSpesa documento, Soggetto soggetto) {
		setSoggetto(soggetto);
		
		// Pulisco i dati del documento
		documento.setAnno(null);
		documento.setNumero(null);
		documento.setDataEmissione(null);
		documento.setDataRicezionePortale(null);
		documento.setUid(0);
		
		//SIAC-6840
		documento.setCodAvvisoPagoPA(null);
		documento.setStrutturaAmministrativoContabile(null);
		setDocumento(documento);
	}

	/**
	 * Popola il model a partire dal Documento di spesa ottenuto dal serivizio.
	 * 
	 * @param documentoSpesa il documento tramite cui popolare il model
	 */
	public void popolaModel(DocumentoSpesa documentoSpesa) {
		setDocumento(documentoSpesa);
	}
	
	/**
	 * Crea una lista di subdocumenti di entrata da associare al documento
	 * 
	 * @return lista di quote con i dati correttamente popolati
	 */
	private List<SubdocumentoSpesa> creaSubdocumenti() {
		List<SubdocumentoSpesa> listaQuote = new ArrayList<SubdocumentoSpesa>();
		for(Integer uid : getListaUidProvvisori()){
			ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
			provvisorio.setUid(uid);
			SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
			subdoc.setProvvisorioCassa(provvisorio);
			listaQuote.add(subdoc);
		}
		return listaQuote;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaCodifiche}.
	 * 
	 * @param codifiche le codifiche da cercare
	 * @return la request creata
	 */
	public RicercaCodifiche creaRequestRicercaCodifiche(Class<? extends Codifica>... codifiche) {
		RicercaCodifiche req = creaRequest(RicercaCodifiche.class);
		req.addTipiCodifica(codifiche);
		return req;
	}

	/**
	 * @return the numeroOrdinativoSenzaNumero
	 */
	public int getNumeroOrdinativoSenzaNumero() {
		return numeroOrdinativoSenzaNumero;
	}

	/**
	 * @param numeroOrdinativoSenzaNumero the numeroOrdinativoSenzaNumero to set
	 */
	public void setNumeroOrdinativoSenzaNumero(int numeroOrdinativoSenzaNumero) {
		this.numeroOrdinativoSenzaNumero = numeroOrdinativoSenzaNumero;
	}
	
	
}
