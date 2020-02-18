/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.operazionicassa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;

/**
 * Classe di model per la gestione delle operazioni di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/12/2014
 *
 */
public class CassaEconomaleOperazioniCassaGestioneModel extends CassaEconomaleBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4250060604147774835L;
	
	private OperazioneCassa operazioneCassaRicerca;
	private TipoOperazioneCassa tipoOperazioneCassaRicerca;
	
	private OperazioneCassa operazioneCassa;
	private Boolean inserimentoAbilitato;
	private Boolean azioniAbilitate;
	private OperazioneOperazioneCassa operazioneOperazioneCassa;
	private AttoAmministrativo attoAmministrativo;
	
	private List<TipoOperazioneCassa> listaTipoOperazioneCassa = new ArrayList<TipoOperazioneCassa>();
	private List<StatoOperativoOperazioneCassa> listaStatoOperativoOperazioneCassa = new ArrayList<StatoOperativoOperazioneCassa>();
	private List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa = new ArrayList<ModalitaPagamentoCassa>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	/** Costruttore vuoto di default */
	public CassaEconomaleOperazioniCassaGestioneModel() {
		setTitolo("Operazioni cassa");
	}

	/**
	 * @return the operazioneCassaRicerca
	 */
	public OperazioneCassa getOperazioneCassaRicerca() {
		return operazioneCassaRicerca;
	}

	/**
	 * @param operazioneCassaRicerca the operazioneCassaRicerca to set
	 */
	public void setOperazioneCassaRicerca(OperazioneCassa operazioneCassaRicerca) {
		this.operazioneCassaRicerca = operazioneCassaRicerca;
	}

	/**
	 * @return the tipoOperazioneCassaRicerca
	 */
	public TipoOperazioneCassa getTipoOperazioneCassaRicerca() {
		return tipoOperazioneCassaRicerca;
	}

	/**
	 * @param tipoOperazioneCassaRicerca the tipoOperazioneCassaRicerca to set
	 */
	public void setTipoOperazioneCassaRicerca(
			TipoOperazioneCassa tipoOperazioneCassaRicerca) {
		this.tipoOperazioneCassaRicerca = tipoOperazioneCassaRicerca;
	}

	/**
	 * @return the operazioneCassa
	 */
	public OperazioneCassa getOperazioneCassa() {
		return operazioneCassa;
	}

	/**
	 * @param operazioneCassa the operazioneCassa to set
	 */
	public void setOperazioneCassa(OperazioneCassa operazioneCassa) {
		this.operazioneCassa = operazioneCassa;
	}

	/**
	 * @return the inserimentoAbilitato
	 */
	public Boolean getInserimentoAbilitato() {
		return inserimentoAbilitato;
	}

	/**
	 * @param inserimentoAbilitato the inserimentoAbilitato to set
	 */
	public void setInserimentoAbilitato(Boolean inserimentoAbilitato) {
		this.inserimentoAbilitato = inserimentoAbilitato;
	}

	/**
	 * @return the azioniAbilitate
	 */
	public Boolean getAzioniAbilitate() {
		return azioniAbilitate;
	}

	/**
	 * @param azioniAbilitate the azioniAbilitate to set
	 */
	public void setAzioniAbilitate(Boolean azioniAbilitate) {
		this.azioniAbilitate = azioniAbilitate;
	}

	/**
	 * @return the operazioneOperazioneCassa
	 */
	public OperazioneOperazioneCassa getOperazioneOperazioneCassa() {
		return operazioneOperazioneCassa;
	}

	/**
	 * @param operazioneOperazioneCassa the operazioneOperazioneCassa to set
	 */
	public void setOperazioneOperazioneCassa(OperazioneOperazioneCassa operazioneOperazioneCassa) {
		this.operazioneOperazioneCassa = operazioneOperazioneCassa;
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	/**
	 * @return the listaTipoOperazioneCassa
	 */
	public List<TipoOperazioneCassa> getListaTipoOperazioneCassa() {
		return listaTipoOperazioneCassa;
	}

	/**
	 * @param listaTipoOperazioneCassa the listaTipoOperazioneCassa to set
	 */
	public void setListaTipoOperazioneCassa(List<TipoOperazioneCassa> listaTipoOperazioneCassa) {
		this.listaTipoOperazioneCassa = listaTipoOperazioneCassa;
	}

	/**
	 * @return the listaStatoOperativoOperazioneCassa
	 */
	public List<StatoOperativoOperazioneCassa> getListaStatoOperativoOperazioneCassa() {
		return listaStatoOperativoOperazioneCassa;
	}

	/**
	 * @param listaStatoOperativoOperazioneCassa the listaStatoOperativoOperazioneCassa to set
	 */
	public void setListaStatoOperativoOperazioneCassa(List<StatoOperativoOperazioneCassa> listaStatoOperativoOperazioneCassa) {
		this.listaStatoOperativoOperazioneCassa = listaStatoOperativoOperazioneCassa;
	}

	/**
	 * @return the listaModalitaPagamentoCassa
	 */
	public List<ModalitaPagamentoCassa> getListaModalitaPagamentoCassa() {
		return listaModalitaPagamentoCassa;
	}

	/**
	 * @param listaModalitaPagamentoCassa the listaModalitaPagamentoCassa to set
	 */
	public void setListaModalitaPagamentoCassa(List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa) {
		this.listaModalitaPagamentoCassa = listaModalitaPagamentoCassa;
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}
	
	/**
	 * @return the cassaMista
	 */
	public boolean isCassaMista() {
		return getCassaEconomale() != null && TipoDiCassa.MISTA.equals(getCassaEconomale().getTipoDiCassa());
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoOperazioneDiCassa creaRequestRicercaTipoOperazioneDiCassa() {
		RicercaTipoOperazioneDiCassa request = creaRequest(RicercaTipoOperazioneDiCassa.class);
		request.setCassaEconomale(getCassaEconomale());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaModalitaPagamentoCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaModalitaPagamentoCassa creaRequestRicercaModalitaPagamentoCassa() {
		return creaRequest(RicercaModalitaPagamentoCassa.class);
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaOperazioneDiCassa creaRequestRicercaSinteticaOperazioneDiCassa() {
		RicercaSinteticaOperazioneDiCassa request = creaRequest(RicercaSinteticaOperazioneDiCassa.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		getOperazioneCassaRicerca().setEnte(getEnte());
		getOperazioneCassaRicerca().setBilancio(getBilancio());
		getOperazioneCassaRicerca().setCassaEconomale(getCassaEconomale());
		getOperazioneCassaRicerca().setTipoOperazioneCassa(impostaEntitaFacoltativa(getTipoOperazioneCassaRicerca()));
		request.setOperazioneCassa(getOperazioneCassaRicerca());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(getAttoAmministrativo().getAnno());
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero());
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getAttoAmministrativo().getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getAttoAmministrativo().getStrutturaAmmContabile()));
		
		request.setRicercaAtti(ricercaAtti);
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link InserisceOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public InserisceOperazioneDiCassa creaRequestInserisceOperazioneDiCassa() {
		InserisceOperazioneDiCassa request = creaRequest(InserisceOperazioneDiCassa.class);
		
		getOperazioneCassa().setBilancio(getBilancio());
		getOperazioneCassa().setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		getOperazioneCassa().setCassaEconomale(getCassaEconomale());
		request.setOperazioneCassa(getOperazioneCassa());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaOperazioneDiCassa creaRequestAggiornaOperazioneDiCassa() {
		AggiornaOperazioneDiCassa request = creaRequest(AggiornaOperazioneDiCassa.class);
		
		getOperazioneCassa().setBilancio(getBilancio());
		getOperazioneCassa().setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		getOperazioneCassa().setCassaEconomale(getCassaEconomale());
		request.setOperazioneCassa(getOperazioneCassa());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AnnullaOperazioneDiCassa}.
	 * 
	 * @return la request creata
	 */
	public AnnullaOperazioneDiCassa creaRequestAnnullaOperazioneDiCassa() {
		AnnullaOperazioneDiCassa request = creaRequest(AnnullaOperazioneDiCassa.class);
		
		request.setOperazioneCassa(getOperazioneCassa());
		
		return request;
	}

}
