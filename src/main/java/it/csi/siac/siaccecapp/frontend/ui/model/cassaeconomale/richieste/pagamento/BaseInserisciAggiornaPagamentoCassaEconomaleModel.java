/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe base di model per l'inserimento e l'aggiornamento del pagamento
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/01/2016
 *
 */
public abstract class BaseInserisciAggiornaPagamentoCassaEconomaleModel extends BaseInserisciAggiornaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4564513931435646248L;

	// Step 1
	private List<Giustificativo> listaGiustificativo = new ArrayList<Giustificativo>();

	private Integer uidValutaEuro;
	private Giustificativo giustificativo;
	private Integer rowNumber;
	
	private List<TipoGiustificativo> listaTipoGiustificativo = new ArrayList<TipoGiustificativo>();
	private List<Valuta> listaValuta = new ArrayList<Valuta>();

	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	private DocumentoSpesa documentoSpesa;
	/**
	 * @return the listaGiustificativo
	 */
	public List<Giustificativo> getListaGiustificativo() {
		return listaGiustificativo;
	}

	/**
	 * @param listaGiustificativo the listaGiustificativo to set
	 */
	public void setListaGiustificativo(List<Giustificativo> listaGiustificativo) {
		this.listaGiustificativo = listaGiustificativo != null ? listaGiustificativo : new ArrayList<Giustificativo>();
	}

	/**
	 * @return the uidValutaEuro
	 */
	public Integer getUidValutaEuro() {
		return uidValutaEuro;
	}

	/**
	 * @param uidValutaEuro the uidValutaEuro to set
	 */
	public void setUidValutaEuro(Integer uidValutaEuro) {
		this.uidValutaEuro = uidValutaEuro;
	}
	@Override
	public Boolean getMaySearchHR() {
		return Boolean.FALSE;
	}
	/**
	 * @return the giustificativo
	 */
	public Giustificativo getGiustificativo() {
		return giustificativo;
	}

	/**
	 * @param giustificativo the giustificativo to set
	 */
	public void setGiustificativo(Giustificativo giustificativo) {
		this.giustificativo = giustificativo;
	}

	/**
	 * @return the rowNumber
	 */
	public Integer getRowNumber() {
		return rowNumber;
	}

	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}


	/**
	 * @return the listaTipoGiustificativo
	 */
	public List<TipoGiustificativo> getListaTipoGiustificativo() {
		return listaTipoGiustificativo;
	}

	/**
	 * @param listaTipoGiustificativo the listaTipoGiustificativo to set
	 */
	public void setListaTipoGiustificativo(List<TipoGiustificativo> listaTipoGiustificativo) {
		this.listaTipoGiustificativo = listaTipoGiustificativo != null ? listaTipoGiustificativo : new ArrayList<TipoGiustificativo>();
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
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
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
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}
	
	/**
	 * @return the descrizioneFatturaRiferimento
	 */
	public String getDescrizioneFatturaRiferimento(){
		final StringBuilder sb = new StringBuilder();
		if (getRichiestaEconomale()!= null 
				&& getRichiestaEconomale().getSubdocumenti()!= null 
				&& !getRichiestaEconomale().getSubdocumenti().isEmpty()){
			//recupero i dati del padre
			DocumentoSpesa fatPadre = getRichiestaEconomale().getSubdocumenti().get(0).getDocumento();
			
			sb.append("Anno ");
			sb.append(fatPadre.getAnno());
			sb.append(" Numero  ");
			sb.append(fatPadre.getNumero());
			sb.append(" Quota ");
			sb.append(getRichiestaEconomale().getSubdocumenti().get(0).getNumero());
			
			}
		return sb.toString();
	}
	
	/**
	 * @return the descrizioneFatturaRiferimentoPerRiepilogo
	 */
	public String getDescrizioneFatturaRiferimentoPerRiepilogo(){
		final StringBuilder sb = new StringBuilder();
		if (getRichiestaEconomale()!= null 
				&& getRichiestaEconomale().getSubdocumenti()!= null 
				&& !getRichiestaEconomale().getSubdocumenti().isEmpty()){
			//recupero i dati del padre
			DocumentoSpesa fatPadre = getRichiestaEconomale().getSubdocumenti().get(0).getDocumento();
			
			sb.append("Anno ");
			sb.append(fatPadre.getAnno());
			sb.append(" Numero  ");
			sb.append(fatPadre.getNumero());
			sb.append(" Quota ");
			sb.append(getRichiestaEconomale().getSubdocumenti().get(0).getNumero());
			sb.append(" Soggetto ");
			sb.append(fatPadre.getSoggetto().getDenominazione());
			
			
			}
		return sb.toString();
	}
	
	/**
	 * @return the datiSoggetto
	 */
	public String getDatiSoggetto() {
		
		if(getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getCodiceSoggetto());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		//CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCodiceBeneficiario());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getNome());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCognome());
		return StringUtils.join(chunks, " - ");
	}
	/**
	 * @return the totaleImportiGiustificativi
	 */
	public BigDecimal getTotaleImportiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo g : getListaGiustificativo()) {
			if(g.getImportoGiustificativo() != null) {
				totale = totale.add(g.getImportoGiustificativo());
			}
		}
		return totale;
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
	 * Crea una request per il servizio di {@link RicercaTipoGiustificativo}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo() {
		//TODO mod con PAGAMENTO
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.PAGAMENTO);
	}
	
}
