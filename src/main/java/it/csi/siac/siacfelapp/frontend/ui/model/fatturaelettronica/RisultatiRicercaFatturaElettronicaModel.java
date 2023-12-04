/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronica;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Classe di model per i risultati della ricerca della fattura elettronica.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
public class RisultatiRicercaFatturaElettronicaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4795271517473720066L;
	
	private FatturaFEL fatturaFEL;
	private Soggetto soggetto;
	private Integer savedDisplayStart;
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	//SIAC-7571
	private String sceltaUtente;
	//SIAC-8273
	private String adeguaImportoNonValorizzato;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaFatturaElettronicaModel() {
		setTitolo("Risultati ricerca FEL");
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
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
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
	 * @return the sceltaUtente
	 */
	public String getSceltaUtente() {
		return sceltaUtente;
	}

	/**
	 * @param sceltaUtente the sceltaUtente to set
	 */
	public void setSceltaUtente(String sceltaUtente) {
		this.sceltaUtente = sceltaUtente;
	}
	
	/**
	 * @return the adeguaImportoNonValorizzato
	 */
	public String getAdeguaImportoNonValorizzato() {
		return adeguaImportoNonValorizzato;
	}

	/**
	 * @param adeguaImportoNonValorizzato the adeguaImportoNonValorizzato to set
	 */
	public void setAdeguaImportoNonValorizzato(String adeguaImportoNonValorizzato) {
		this.adeguaImportoNonValorizzato = adeguaImportoNonValorizzato;
	}

	
	/* **** Requests **** */
	

	/**
	 * Crea una request per il servizio di {@link SospendiFatturaElettronica}.
	 * 
	 * @return la request creata
	 */
	public SospendiFatturaElettronica creaRequestSospendiFatturaElettronica() {
		SospendiFatturaElettronica request = creaRequest(SospendiFatturaElettronica.class);
		
		request.setFatturaFEL(getFatturaFEL());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}

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
