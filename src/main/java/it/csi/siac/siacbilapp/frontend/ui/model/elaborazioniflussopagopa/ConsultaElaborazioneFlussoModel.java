/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.elaborazioniflussopagopa;


import java.math.BigDecimal;

import it.csi.siac.pagopa.frontend.webservice.msg.RicercaRiconciliazioniDoc;

/**
 * Classe di model per la consultazione del Documento di Spesa.
 * 
 * @author Ahmad Nazha, Valentina Triolo
 * @version 1.0.0 - 24/03/2014
 *
 */
public class ConsultaElaborazioneFlussoModel extends GenericElaborazioniFlussoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1294948158973919518L;
	private String numeroProvvisorio;
	private String dataEmissioneProvvisorio;
	private BigDecimal importoProvvisorio;
	private String dataElaborazione;
	private String statoElaborazione;
	private Integer riconciliazioneId;
	
	private boolean tipoFattura;
	//private Integer uidElaborazioneFlusso;
	//SIAC-8046 CM Task 2.2-2.3 31/03-07/04-16/04/2021 Inizio
	private String annoAcc;
	private String numAcc;
	private boolean resRicercaAccertamento;
	private boolean resAggiornaAccertamento;
	private String descrizioneResAggiornaAccertamento;

	/**
	 * @return the resAggiornaAccertamento
	 */
	public boolean isResAggiornaAccertamento() {
		return resAggiornaAccertamento;
	}

	/**
	 * @param resAggiornaAccertamento the resAggiornaAccertamento to set
	 */
	public void setResAggiornaAccertamento(boolean resAggiornaAccertamento) {
		this.resAggiornaAccertamento = resAggiornaAccertamento;
	}

	/**
	 * @return the descrizioneResAggiornaAccertamento
	 */
	public String getDescrizioneResAggiornaAccertamento() {
		return descrizioneResAggiornaAccertamento;
	}

	/**
	 * @param descrizioneResAggiornaAccertamento the descrizioneResAggiornaAccertamento to set
	 */
	public void setDescrizioneResAggiornaAccertamento(String descrizioneResAggiornaAccertamento) {
		this.descrizioneResAggiornaAccertamento = descrizioneResAggiornaAccertamento;
	}

	/**
	 * @return the resRicercaAccertamento
	 */
	public boolean isResRicercaAccertamento() {
		return resRicercaAccertamento;
	}

	/**
	 * @param resRicercaAccertamento the resRicercaAccertamento to set
	 */
	public void setResRicercaAccertamento(boolean resRicercaAccertamento) {
		this.resRicercaAccertamento = resRicercaAccertamento;
	}

	/**
	 * @return the annoAcc
	 */
	public String getAnnoAcc() {
		return annoAcc;
	}

	/**
	 * @param annoAcc the annoAcc to set
	 */
	public void setAnnoAcc(String annoAcc) {
		this.annoAcc = annoAcc;
	}

	/**
	 * @return the numAcc
	 */
	public String getNumAcc() {
		return numAcc;
	}

	/**
	 * @param numAcc the numAcc to set
	 */
	public void setNumAcc(String numAcc) {
		this.numAcc = numAcc;
	}
	//SIAC-8046 CM Task 2.2-2.3 31/03-07/04-16/04/2021 Fine

	/**
	 * @return the tipoFattura
	 */
	public boolean isTipoFattura() {
		return tipoFattura;
	}

	/**
	 * @param tipoFattura the tipoFattura to set
	 */
	public void setTipoFattura(boolean tipoFattura) {
		this.tipoFattura = tipoFattura;
	}

	/**
	 * @return the numeroProvvisorio
	 */
	public String getNumeroProvvisorio() {
		return numeroProvvisorio;
	}

	/**
	 * @param numeroProvvisorio the numeroProvvisorio to set
	 */
	public void setNumeroProvvisorio(String numeroProvvisorio) {
		this.numeroProvvisorio = numeroProvvisorio;
	}
	
	/**
	 * @return the dataEmissioneProvvisorio
	 */
	public String getDataEmissioneProvvisorio() {
		return dataEmissioneProvvisorio;
	}

	/**
	 * @return the importoProvvisorio
	 */
	public BigDecimal getImportoProvvisorio() {
		return importoProvvisorio;
	}

	/**
	 * @return the dataElaborazione
	 */
	public String getDataElaborazione() {
		return dataElaborazione;
	}

	/**
	 * @return the statoElaborazione
	 */
	public String getStatoElaborazione() {
		return statoElaborazione;
	}

	/**
	 * @param dataEmissioneProvvisorio the dataEmissioneProvvisorio to set
	 */
	public void setDataEmissioneProvvisorio(String dataEmissioneProvvisorio) {
		this.dataEmissioneProvvisorio = dataEmissioneProvvisorio;
	}

	/**
	 * @param importoProvvisorio the importoProvvisorio to set
	 */
	public void setImportoProvvisorio(BigDecimal importoProvvisorio) {
		this.importoProvvisorio = importoProvvisorio;
	}

	/**
	 * @param dataElaborazione the dataElaborazione to set
	 */
	public void setDataElaborazione(String dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}

	/**
	 * @param statoElaborazione the statoElaborazione to set
	 */
	public void setStatoElaborazione(String statoElaborazione) {
		this.statoElaborazione = statoElaborazione;
	}

	/** Costruttore vuoto di default */
	public ConsultaElaborazioneFlussoModel() {
		super();
		setTitolo("Consulta PagoPA");
	}

	/*
	 * 
	 */
	public RicercaRiconciliazioniDoc creaRequestRicercaRiconciliazioniDoc() {
		RicercaRiconciliazioniDoc request = creaRequest(RicercaRiconciliazioniDoc.class);
		//request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	
	
	public RicercaRiconciliazioniDoc creaRequestRicercaRiconciliazioniErrore() {
		RicercaRiconciliazioniDoc request = creaRequest(RicercaRiconciliazioniDoc.class);
		//request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	//SIAC-8046 CM Task 2.2 31/03/2021 Inizio
	public RicercaRiconciliazioniDoc creaRequestAggiornaAccertamentoRiconciliazione() {
		RicercaRiconciliazioniDoc request = creaRequest(RicercaRiconciliazioniDoc.class);
		//request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	//SIAC-8046 CM Task 2.2 31/03/2021 Fine
	public Integer getRiconciliazioneId() {
		return riconciliazioneId;
	}

	public void setRiconciliazioneId(Integer riconciliazioneId) {
		this.riconciliazioneId = riconciliazioneId;
	}
	
	
}
