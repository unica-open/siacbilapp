/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.DettaglioPagamentoFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PagamentoFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

/**
 * Classe di model per la consultazione della fattura elettronica.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
public class ConsultaFatturaElettronicaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2514321410628964213L;
	
	private FatturaFEL fatturaFEL;
	private CausaleFEL causaleFEL;
	private DettaglioPagamentoFEL dettaglioPagamentoFEL;
	
	/** Costruttore vuoto di default */
	public ConsultaFatturaElettronicaModel() {
		setTitolo("Consulta FEL");
	}

	/**
	 * @return the fatturaFEL
	 */
	public FatturaFEL getFatturaFEL() {
		return fatturaFEL;
	}

	/**
	 * @param fatturaFEL the fatturaFEL to set
	 */
	public void setFatturaFEL(FatturaFEL fatturaFEL) {
		this.fatturaFEL = fatturaFEL;
	}

	/**
	 * @return the causaleFEL
	 */
	public CausaleFEL getCausaleFEL() {
		return causaleFEL;
	}

	/**
	 * @param causaleFEL the causaleFEL to set
	 */
	public void setCausaleFEL(CausaleFEL causaleFEL) {
		this.causaleFEL = causaleFEL;
	}

	/**
	 * @return the dettaglioPagamentoFEL
	 */
	public DettaglioPagamentoFEL getDettaglioPagamentoFEL() {
		return dettaglioPagamentoFEL;
	}

	/**
	 * @param dettaglioPagamentoFEL the dettaglioPagamentoFEL to set
	 */
	public void setDettaglioPagamentoFEL(DettaglioPagamentoFEL dettaglioPagamentoFEL) {
		this.dettaglioPagamentoFEL = dettaglioPagamentoFEL;
	}
	
	/* **** Getter di utilita' **** */
	
	/**
	 * @return the estremiDellaFatturaTipo
	 */
	public String getEstremiDellaFatturaTipo() {
		if(getFatturaFEL() == null || getFatturaFEL().getTipoDocumentoFEL() == null) {
			return "";
		}
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getTipoDocumentoFEL().getCodice());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getTipoDocumentoFEL().getDescrizione());
		
		return StringUtils.join(res, ", ");
	}
	
	/**
	 * @return the estremiDellaFatturaAnnoNumeroDataProtocollo
	 */
	public String getEstremiDellaFatturaAnnoNumeroDataProtocollo() {
		if(getFatturaFEL() == null || getFatturaFEL().getProtocolloFEL() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getProtocolloFEL().getAnnoProtocollo());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getProtocolloFEL().getNumeroProtocollo());
		// Passo intermedio
		String intermediate = StringUtils.join(res, "/");
		// Pulisco e ricomincio
		res.clear();
		CollectionUtil.addIfNotNullNorEmpty(res, intermediate);
		CollectionUtil.addIfNotNullNorEmpty(res, FormatUtils.formatDate(getFatturaFEL().getProtocolloFEL().getDataRegProtocollo()));
		
		return StringUtils.join(res, " - ");
	}
	
	/**
	 * @return the estremiDellaFatturaCausale
	 */
	public String getEstremiDellaFatturaCausale() {
		if(getCausaleFEL() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		
		// Il campo e' numerico
		if(getCausaleFEL().getProgressivo() != null) {
			CollectionUtil.addIfNotNullNorEmpty(res, getCausaleFEL().getProgressivo().toString());
		}
		CollectionUtil.addIfNotNullNorEmpty(res, getCausaleFEL().getCausale());
		
		return StringUtils.join(res, " / ");
	}
	
	/**
	 * @return the datiFornitoreDenominazione
	 */
	public String getDatiFornitoreDenominazione() {
		if(getFatturaFEL() == null || getFatturaFEL().getPrestatore() == null) {
			return "";
		}
		
		if(StringUtils.isNotBlank(getFatturaFEL().getPrestatore().getDenominazionePrestatore())) {
			return getFatturaFEL().getPrestatore().getDenominazionePrestatore();
		}
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getCognomePrestatore());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getNomePrestatore());
		return StringUtils.join(res, " ");
	}
	
	/**
	 * @return the datiFornitoreIndirizzo
	 */
	public String getDatiFornitoreIndirizzo() {
		if(getFatturaFEL() == null || getFatturaFEL().getPrestatore() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getIndirizzoPrestatore());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getNumeroCivicoPrestatore());
		return StringUtils.join(res, ", ");
	}
	
	/**
	 * @return the datiFornitoreCAPComuneProvincia
	 */
	public String getDatiFornitoreCAPComuneProvincia() {
		if(getFatturaFEL() == null || getFatturaFEL().getPrestatore() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getCapPrestatore());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getComunePrestatore());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getProvinciaPrestatore());
		return StringUtils.join(res, ", ");
	}
	
	/**
	 * @return the datiFornitoreTelefonoFax
	 */
	public String getDatiFornitoreTelefonoFax() {
		if(getFatturaFEL() == null || getFatturaFEL().getPrestatore() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getTelefonoPrestatore());
		CollectionUtil.addIfNotNullNorEmpty(res, getFatturaFEL().getPrestatore().getFaxPrestatore());
		return StringUtils.join(res, ", ");
	}
	
	/**
	 * @return the datiModalitaDiPagamentoCodiceModalitaDiPagamento
	 */
	public String getDatiModalitaDiPagamentoCodiceModalitaDiPagamento() {
		if(getDettaglioPagamentoFEL() == null || getDettaglioPagamentoFEL().getModalitaPagamentoFEL() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, getDettaglioPagamentoFEL().getModalitaPagamentoFEL().getCodice());
		CollectionUtil.addIfNotNullNorEmpty(res, getDettaglioPagamentoFEL().getModalitaPagamentoFEL().getDescrizione());
		return StringUtils.join(res, " - ");
	}
	
	/**
	 * @return the datiModalitaDiPagamentoDataRiferimentoGiorni
	 */
	public String getDatiModalitaDiPagamentoDataRiferimentoGiorni() {
		if(getDettaglioPagamentoFEL() == null) {
			return "";
		}
		
		List<String> res = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(res, FormatUtils.formatDate(getDettaglioPagamentoFEL().getDataRifTerminiPagamento()));
		if(getDettaglioPagamentoFEL().getGiorniTerminiPagamento() != null) {
			CollectionUtil.addIfNotNullNorEmpty(res, getDettaglioPagamentoFEL().getGiorniTerminiPagamento().toString());
		}
		return StringUtils.join(res, ", ");
	}
	
	/**
	 * @return the datiFornitoreCodiceFiscale
	 */
	public String getDatiFornitoreCodiceFiscale() {
		String codice16 = getCodicePrestatoreByLength(16);
		return codice16 != null ? codice16 : "";
	}
	
	/**
	 * @return the datiFornitorePartitaIva
	 */
	public String getDatiFornitorePartitaIva() {
		String codice11 = getCodicePrestatoreByLength(11);
		return codice11 != null ? codice11 : "";
	}
	
	/**
	 * Ottiene il codice prestatore se di data lunghezza.
	 * 
	 * @param length la lunghezza del codice da ottenere
	 * 
	 * @return il codice se di data lunghezza; <code>null</code> in caso contrario
	 */
	private String getCodicePrestatoreByLength(int length) {
		return getFatturaFEL() != null
				&& getFatturaFEL().getPrestatore() != null
				&& getFatturaFEL().getPrestatore().getCodicePrestatore() != null
				&& getFatturaFEL().getPrestatore().getCodicePrestatore().length() == length
					? getFatturaFEL().getPrestatore().getCodicePrestatore()
					: null;
	}
	
	/**
	 * @return the conMoltepliciDettaglioPagamentoFel
	 */
	public boolean isConMoltepliciDettaglioPagamentoFel() {
		// Caso limite
		if(getFatturaFEL() == null || getFatturaFEL().getPagamenti().isEmpty()) {
			return false;
		}
		
		// Se ho vari pagamenti, allora ho piu' di un dettaglio
		if(getFatturaFEL().getPagamenti() != null && getFatturaFEL().getPagamenti().size() > 1) {
			return true;
		}
		// Ho un unico pagamento
		PagamentoFEL pagamentoFEL = getFatturaFEL().getPagamenti().get(0);
		
		// Caso limite
		if(pagamentoFEL.getElencoDettagliPagamento().isEmpty()) {
			return false;
		}
		
		return pagamentoFEL.getElencoDettagliPagamento().size() > 1;
	}
	
	/**
	 * @return the totaleImponibile
	 */
	public BigDecimal getTotaleImponibile() {
		// Caso limite
		if(getFatturaFEL() == null) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal result = BigDecimal.ZERO;
		
		for(RiepilogoBeniFEL riepilogoBeniFEL : getFatturaFEL().getRiepiloghiBeni()) {
			result = result.add(riepilogoBeniFEL.getImponibileImportoNotNull());
		}
		
		return result;
	}
	
	/**
	 * @return the totaleImposta
	 */
	public BigDecimal getTotaleImposta() {
		// Caso limite
		if(getFatturaFEL() == null) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal result = BigDecimal.ZERO;
		
		for(RiepilogoBeniFEL riepilogoBeniFEL : getFatturaFEL().getRiepiloghiBeni()) {
			result = result.add(riepilogoBeniFEL.getImpostaNotNull());
		}
		
		return result;
	}
	
	/**
	 * @return the totaleArrotondamenti
	 */
	public BigDecimal getTotaleArrotondamenti() {
		// Caso limite
		if(getFatturaFEL() == null) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal result = BigDecimal.ZERO;
		
		for(RiepilogoBeniFEL riepilogoBeniFEL : getFatturaFEL().getRiepiloghiBeni()) {
			result = result.add(riepilogoBeniFEL.getArrotondamentoNotNull());
		}
		
		return result;
	}
	
	/**
	 * @return the totaleSpeseAccessorie
	 */
	public BigDecimal getTotaleSpeseAccessorie() {
		// Caso limite
		if(getFatturaFEL() == null) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal result = BigDecimal.ZERO;
		
		for(RiepilogoBeniFEL riepilogoBeniFEL : getFatturaFEL().getRiepiloghiBeni()) {
			result = result.add(riepilogoBeniFEL.getSpeseAccessorieNotNull());
		}
		
		return result;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioFatturaElettronica}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioFatturaElettronica creaRequestRicercaDettaglioFatturaElettronica() {
		RicercaDettaglioFatturaElettronica request = creaRequest(RicercaDettaglioFatturaElettronica.class);
		
		getFatturaFEL().setEnte(getEnte());
		request.setFatturaFEL(getFatturaFEL());
		
		return request;
	}

}
