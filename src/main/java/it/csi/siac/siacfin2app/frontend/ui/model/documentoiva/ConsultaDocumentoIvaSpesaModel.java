/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

/**
 * Classe di model per la ricerca del Documento Iva di Spesa
 * 
 * @author Domenico
 */
public class ConsultaDocumentoIvaSpesaModel extends GenericDocumentoIvaSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3398006391781647554L;

	private Integer uidDocumentoIvaDaConsultare;
	private List<Subdocumento<?, ?>> subdocumentiPagati = new ArrayList<Subdocumento<?, ?>>();
	
	private Integer uidQuotaIvaDifferita;
	private SubdocumentoIvaSpesa quotaIvaDifferita;
	
	/** Costruttore vuoto di default */
	public ConsultaDocumentoIvaSpesaModel() {
		setTitolo("Consulta Documenti iva di spesa");
	}

	/**
	 * @return the uidDocumentoIvaDaConsultare
	 */
	public Integer getUidDocumentoIvaDaConsultare() {
		return uidDocumentoIvaDaConsultare;
	}

	/**
	 * @param uidDocumentoIvaDaConsultare the uidDocumentoIvaDaConsultare to set
	 */
	public void setUidDocumentoIvaDaConsultare(Integer uidDocumentoIvaDaConsultare) {
		this.uidDocumentoIvaDaConsultare = uidDocumentoIvaDaConsultare;
	}
	
	/**
	 * @return the subdocumentiPagati
	 */
	public List<Subdocumento<?, ?>> getSubdocumentiPagati() {
		return subdocumentiPagati;
	}

	/**
	 * @param subdocumentiPagati the subdocumentiPagati to set
	 */
	public void setSubdocumentiPagati(List<Subdocumento<?, ?>> subdocumentiPagati) {
		this.subdocumentiPagati = subdocumentiPagati;
	}
	
	/**
	 * @return the uidQuotaIvaDifferita
	 */
	public Integer getUidQuotaIvaDifferita() {
		return uidQuotaIvaDifferita;
	}

	/**
	 * @param uidQuotaIvaDifferita the uidQuotaIvaDifferita to set
	 */
	public void setUidQuotaIvaDifferita(Integer uidQuotaIvaDifferita) {
		this.uidQuotaIvaDifferita = uidQuotaIvaDifferita;
	}

	/**
	 * @return the quotaIvaDifferita
	 */
	public SubdocumentoIvaSpesa getQuotaIvaDifferita() {
		return quotaIvaDifferita;
	}

	/**
	 * @param quotaIvaDifferita the quotaIvaDifferita to set
	 */
	public void setQuotaIvaDifferita(SubdocumentoIvaSpesa quotaIvaDifferita) {
		this.quotaIvaDifferita = quotaIvaDifferita;
	}
	
	/**
	 * @return the tipoEntrataSpesa
	 */
	public String getTipoEntrataSpesa(){
		return "Spesa";
	}
	
	/**
	 * @return the imponibileTotaleMovimentiIva
	 */
	public BigDecimal getImponibileTotaleMovimentiIva(){
		BigDecimal result = BigDecimal.ZERO;
		if(getSubdocumentoIva()!=null){
			for(AliquotaSubdocumentoIva asi :getSubdocumentoIva().getListaAliquotaSubdocumentoIva()){
				result = result.add(asi.getImponibile());
			}
		}
		return result;
	}
	
	/**
	 * @return the impostaTotaleMovimentiIva
	 */
	public BigDecimal getImpostaTotaleMovimentiIva(){
		BigDecimal result = BigDecimal.ZERO;
		if(getSubdocumentoIva()!=null){
			for(AliquotaSubdocumentoIva asi :getSubdocumentoIva().getListaAliquotaSubdocumentoIva()){
				result = result.add(asi.getImposta());
			}
		}
		return result;
	}
	
	@Override
	public BigDecimal getTotaleTotaleMovimentiIva(){
		BigDecimal result = BigDecimal.ZERO;
		if(getSubdocumentoIva()!=null){
			for(AliquotaSubdocumentoIva asi :getSubdocumentoIva().getListaAliquotaSubdocumentoIva()){
				result = result.add(asi.getTotale());
			}
		}
		return result;
	}
	
	/* **** Getter di utilita' **** */
		
	/**
	 * @return the datiTipoRegistrazioneIva
	 */
	public String getDatiTipoRegistrazione() {
		if(getSubdocumentoIva() == null || getSubdocumentoIva().getTipoRegistrazioneIva() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getSubdocumentoIva().getTipoRegistrazioneIva().getCodice());
		CollectionUtil.addIfNotNullNorEmpty(res, getSubdocumentoIva().getTipoRegistrazioneIva().getDescrizione());
		return StringUtils.join(res, " - ");
	}
	
	/**
	 * @return the datiTipoRegistroIva
	 */
	public String getDatiTipoRegistroIva() {
		if(getSubdocumentoIva() == null || getSubdocumentoIva().getRegistroIva()  == null || getSubdocumentoIva().getRegistroIva().getTipoRegistroIva() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getSubdocumentoIva().getRegistroIva().getTipoRegistroIva().getCodice());
		CollectionUtil.addIfNotNullNorEmpty(res, getSubdocumentoIva().getRegistroIva().getTipoRegistroIva().getDescrizione());
		return StringUtils.join(res, " - ");
	}
	
	/**
	 * @return the datiAttivitaIva
	 */
	public String getDatiAttivita() {
		if(getSubdocumentoIva() == null || getSubdocumentoIva().getAttivitaIva()  == null ) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getSubdocumentoIva().getAttivitaIva().getCodice());
		CollectionUtil.addIfNotNullNorEmpty(res, getSubdocumentoIva().getAttivitaIva().getDescrizione());
		return StringUtils.join(res, " - ");
	}
	/* **** Requests **** */
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaSpesa creaRequestRicercaDettaglioSubdocumentoIvaSpesa() {
		return super.creaRequestRicercaDettaglioSubdocumentoIvaSpesa(uidDocumentoIvaDaConsultare);
	}

}
