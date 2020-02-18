/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.cassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;

/**
 * Classe di model per la gestione della cassa economale
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/dic/2014
 *
 */
public class CassaEconomaleCassaGestioneModel extends CassaEconomaleBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8159056047396337801L;
	
	private List<TipoDiCassa> listaTipoDiCassa = new ArrayList<TipoDiCassa>();
	
	private TipoDiCassa tipoDiCassaOriginale;
	private Boolean proseguireConElaborazione;
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	/** Costruttore vuoto di default */
	public CassaEconomaleCassaGestioneModel() {
		setTitolo("Gestione casse");
	}

	/**
	 * @return the listaTipoDiCassa
	 */
	public List<TipoDiCassa> getListaTipoDiCassa() {
		return listaTipoDiCassa;
	}

	/**
	 * @param listaTipoDiCassa the listaTipoDiCassa to set
	 */
	public void setListaTipoDiCassa(List<TipoDiCassa> listaTipoDiCassa) {
		this.listaTipoDiCassa = listaTipoDiCassa != null ? listaTipoDiCassa : new ArrayList<TipoDiCassa>();
	}
	
	/**
	 * @return the tipoDiCassaOriginale
	 */
	public TipoDiCassa getTipoDiCassaOriginale() {
		return tipoDiCassaOriginale;
	}

	/**
	 * @param tipoDiCassaOriginale the tipoDiCassaOriginale to set
	 */
	public void setTipoDiCassaOriginale(TipoDiCassa tipoDiCassaOriginale) {
		this.tipoDiCassaOriginale = tipoDiCassaOriginale;
	}

	/**
	 * @return the proseguireConElaborazione
	 */
	public Boolean getProseguireConElaborazione() {
		return proseguireConElaborazione;
	}

	/**
	 * @param proseguireConElaborazione the proseguireConElaborazione to set
	 */
	public void setProseguireConElaborazione(Boolean proseguireConElaborazione) {
		this.proseguireConElaborazione = proseguireConElaborazione;
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
	 * @return the totaleImporti
	 */
	public BigDecimal getTotaleImporti() {
		BigDecimal contanti = getCassaEconomale().getDisponibilitaCassaContantiNotNull();
		BigDecimal cc = getCassaEconomale().getDisponibilitaCassaContoCorrenteNotNull();
		return contanti.add(cc);
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaCassaEconomale}.
	 * 
	 * @return la request creata
	 */
	public AggiornaCassaEconomale creaRequestAggiornaCassaEconomale() {
		AggiornaCassaEconomale request = creaRequest(AggiornaCassaEconomale.class);
		
		getCassaEconomale().setEnte(getEnte());
		//Jira -3011
		if(getCassaEconomale().getSoggetto()!=null && getCassaEconomale().getSoggetto().getUid() == 0){
			getCassaEconomale().setSoggetto(null);
		}
		request.setCassaEconomale(getCassaEconomale());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AnnullaCassaEconomale}.
	 * 
	 * @return la request creata
	 */
	public AnnullaCassaEconomale creaRequestAnnullaCassaEconomale() {
		AnnullaCassaEconomale request = creaRequest(AnnullaCassaEconomale.class);
		
		request.setCassaEconomale(getCassaEconomale());
		
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
		parametroSoggettoK.setCodice(getCassaEconomale().getSoggetto().getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}
	
}
