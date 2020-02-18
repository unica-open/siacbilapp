/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

/**
 * Classe di model per il caricamento dei dati del soggetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/04/2014
 * @version 1.0.1 - 18/09/2014 - Aggiunta dati soggetto, sede e modalita valide; aggiunta richiesta per sede secondaria
 * @version 1.1.0 - 30/11/2015 - gestione switch HR/SIAC
 *
 */
public class CaricaDatiSoggettoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 756610203478286306L;
	
	private String codiceSoggetto;
	private Integer uidSedeSecondariaSoggetto;
	
	private Soggetto soggetto;
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	private Boolean maySearchHR;
	
	/** Costruttore vuoto di default */
	public CaricaDatiSoggettoModel() {
		super();
	}

	/**
	 * @return the codiceSoggetto
	 */
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	/**
	 * @param codiceSoggetto the codiceSoggetto to set
	 */
	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	/**
	 * @return the uidSedeSecondariaSoggetto
	 */
	public Integer getUidSedeSecondariaSoggetto() {
		return uidSedeSecondariaSoggetto;
	}

	/**
	 * @param uidSedeSecondariaSoggetto the uidSedeSecondariaSoggetto to set
	 */
	public void setUidSedeSecondariaSoggetto(Integer uidSedeSecondariaSoggetto) {
		this.uidSedeSecondariaSoggetto = uidSedeSecondariaSoggetto;
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
	 * @return the listaSedeSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}

	/**
	 * @param listaSedeSecondariaSoggetto the listaSedeSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = listaSedeSecondariaSoggetto != null ? listaSedeSecondariaSoggetto : new ArrayList<SedeSecondariaSoggetto>();
	}
	
	/**
	 * @return the listaSedeSecondariaSoggettoValide
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggettoValide() {
		List<SedeSecondariaSoggetto> result = new ArrayList<SedeSecondariaSoggetto>();
		for(SedeSecondariaSoggetto sss : listaSedeSecondariaSoggetto) {
			if(StatoOperativoSedeSecondaria.VALIDO.equals(sss.getStatoOperativoSedeSecondaria())) {
				result.add(sss);
			}
		}
		return result;
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto != null ? listaModalitaPagamentoSoggetto : new ArrayList<ModalitaPagamentoSoggetto>();
	}
	
	/**
	 * @return the maySearchHR
	 */
	public Boolean getMaySearchHR() {
		return maySearchHR;
	}

	/**
	 * @param maySearchHR the maySearchHR to set
	 */
	public void setMaySearchHR(Boolean maySearchHR) {
		this.maySearchHR = maySearchHR;
	}

	/**
	 * @return the listaModalitaPagamentoSoggettoValide
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoValide() {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>();
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamentoSoggetto) {
			if(StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getCodiceStatoModalitaPagamento())
				|| (mps.getModalitaPagamentoSoggettoCessione2() != null
					&& StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getModalitaPagamentoSoggettoCessione2().getCodiceStatoModalitaPagamento()))) {
				result.add(mps);
			}
		}
		return result;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = new RicercaSoggettoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(getCodiceSoggetto());
		request.setParametroSoggettoK(parametroRicercaSoggettoK);
		
		// Lotto P
		request.setSorgenteDatiSoggetto(Boolean.TRUE.equals(getMaySearchHR()) && isGestioneHR() ? SorgenteDatiSoggetto.HR : SorgenteDatiSoggetto.SIAC);
		
		return request;
	}
	
}
