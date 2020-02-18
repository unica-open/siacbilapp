/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomaleModelDetail;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;


/**
 * Classe base di model per la ricerca della richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2015
 */
public class BaseRicercaRichiestaEconomaleModel extends BaseRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8524399161949239798L;

	// SIAC-4497
	private Date dataCreazioneDa;
	private Date dataCreazioneA;
	// SIAC-4452
	private Date dataMovimentoDa;
	private Date dataMovimentoA;
	
	private List<StatoOperativoRichiestaEconomale> listaStatoOperativoRichiestaEconomale = new ArrayList<StatoOperativoRichiestaEconomale>();
	
	/**
	 * @return the dataCreazioneDa
	 */
	public Date getDataCreazioneDa() {
		return dataCreazioneDa != null ? new Date(dataCreazioneDa.getTime()) : null;
	}

	/**
	 * @param dataCreazioneDa the dataCreazioneDa to set
	 */
	public void setDataCreazioneDa(Date dataCreazioneDa) {
		this.dataCreazioneDa = dataCreazioneDa != null ? new Date(dataCreazioneDa.getTime()) : null;
	}

	/**
	 * @return the dataCreazioneA
	 */
	public Date getDataCreazioneA() {
		return dataCreazioneA != null ? new Date(dataCreazioneA.getTime()) : null;
	}

	/**
	 * @param dataCreazioneA the dataCreazioneA to set
	 */
	public void setDataCreazioneA(Date dataCreazioneA) {
		this.dataCreazioneA = dataCreazioneA != null ? new Date(dataCreazioneA.getTime()) : null;
	}

	/**
	 * @return the dataMovimentoDa
	 */
	public Date getDataMovimentoDa() {
		return dataMovimentoDa != null ? new Date(dataMovimentoDa.getTime()) : null;
	}

	/**
	 * @param dataMovimentoDa the dataMovimentoDa to set
	 */
	public void setDataMovimentoDa(Date dataMovimentoDa) {
		this.dataMovimentoDa = dataMovimentoDa != null ? new Date(dataMovimentoDa.getTime()) : null;
	}

	/**
	 * @return the dataMovimentoA
	 */
	public Date getDataMovimentoA() {
		return dataMovimentoA != null ? new Date(dataMovimentoA.getTime()) : null;
	}

	/**
	 * @param dataMovimentoA the dataMovimentoA to set
	 */
	public void setDataMovimentoA(Date dataMovimentoA) {
		this.dataMovimentoA = dataMovimentoA != null ? new Date(dataMovimentoA.getTime()) : null;
	}

	/**
	 * @return the listaStatoOperativoRichiestaEconomale
	 */
	public List<StatoOperativoRichiestaEconomale> getListaStatoOperativoRichiestaEconomale() {
		return listaStatoOperativoRichiestaEconomale;
	}

	/**
	 * @param listaStatoOperativoRichiestaEconomale the listaStatoOperativoRichiestaEconomale to set
	 */
	public void setListaStatoOperativoRichiestaEconomale(List<StatoOperativoRichiestaEconomale> listaStatoOperativoRichiestaEconomale) {
		this.listaStatoOperativoRichiestaEconomale = listaStatoOperativoRichiestaEconomale != null ? listaStatoOperativoRichiestaEconomale : new ArrayList<StatoOperativoRichiestaEconomale>();
	}
	
	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareRichiestaEconomale creaRequestRicercaSinteticaModulareRichiestaEconomale() {
		RicercaSinteticaModulareRichiestaEconomale req = creaRequest(RicercaSinteticaModulareRichiestaEconomale.class);
		
		// Classificatori
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico1());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico2());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico3());
		
		// Tipo di richiesta economale
		getRichiestaEconomale().setBilancio(getBilancio());
		if (isGestioneHR() && getRichiestaEconomale().getSoggetto() != null && getRichiestaEconomale().getSoggetto().getMatricola() != null) {
				//questo dato che la matricolaHR non e'di competenza soggetto fin
				getRichiestaEconomale().setMatricola(getRichiestaEconomale().getSoggetto().getMatricola()); 
				getRichiestaEconomale().setSoggetto(null); 
		}
		getRichiestaEconomale().setEnte(getEnte());
		getRichiestaEconomale().setTipoRichiestaEconomale(getTipoRichiestaEconomale());
		getRichiestaEconomale().setCassaEconomale(getCassaEconomale());
		
		req.setRichiestaEconomale(getRichiestaEconomale());
		req.setParametriPaginazione(creaParametriPaginazione());
		
		// SIAC-4497
		req.setDataCreazioneDa(getDataCreazioneDa());
		req.setDataCreazioneA(getDataCreazioneA());
		// SAIC-4552
		req.setDataMovimentoDa(getDataMovimentoDa());
		req.setDataMovimentoA(getDataMovimentoA());
		
		req.setRichiestaEconomaleModelDetails(getRichiestaEconomaleModelDetails());
		
		return req;
	}

	/**
	 * @return i model details
	 */
	protected RichiestaEconomaleModelDetail[] getRichiestaEconomaleModelDetails() {
		return new RichiestaEconomaleModelDetail[] {
				RichiestaEconomaleModelDetail.StatoOperativo,
				RichiestaEconomaleModelDetail.Rendiconto,
				RichiestaEconomaleModelDetail.Movimenti,
				RichiestaEconomaleModelDetail.Sospeso,
				RichiestaEconomaleModelDetail.Soggetto};
	}
	
}
