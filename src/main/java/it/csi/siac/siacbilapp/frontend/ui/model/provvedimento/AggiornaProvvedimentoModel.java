/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.provvedimento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model per l'inserimento di un Provvedimento. Contiene una mappatura del model.
 * 
 * @author Pro-Logic
 * @version 1.0.0 - 09/09/2013
 *
 */
public class AggiornaProvvedimentoModel extends GenericBilancioModel {
	/** Per la serializzazione */
	private static final long serialVersionUID = -242133523450910036L;
	
	private Integer uidDaAggiornare;
	private AttoAmministrativo attoAmministrativo;
	private List<TipoAtto> tipiAtti = new ArrayList<TipoAtto>();
	private List<StatoOperativoAtti> statiOperativi = new ArrayList<StatoOperativoAtti>();
	private String codiceInc;

	private Boolean movimentoInterno;
	
	/** Costruttore vuoto di default */
	public AggiornaProvvedimentoModel() {
		super();
		setTitolo("Aggiorna Provvedimento");
	}
	
	/**
	 * @return the uidDaAggiornare
	 */
	public Integer getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(Integer uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
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
	 * @return the tipiAtti
	 */
	public List<TipoAtto> getTipiAtti() {
		return tipiAtti;
	}

	/**
	 * @param tipiAtti the tipiAtti to set
	 */
	public void setTipiAtti(List<TipoAtto> tipiAtti) {
		this.tipiAtti = tipiAtti != null ? tipiAtti : new ArrayList<TipoAtto>();
	}

	/**
	 * @return the statiOperativi
	 */
	public List<StatoOperativoAtti> getStatiOperativi() {
		return statiOperativi;
	}

	/**
	 * @param statiOperativi the statiOperativi to set
	 */
	public void setStatiOperativi(List<StatoOperativoAtti> statiOperativi) {
		this.statiOperativi = statiOperativi != null ? statiOperativi : new ArrayList<StatoOperativoAtti>();
	}
	
	/**
	 * @return the movimentoInterno
	 */
	public Boolean getMovimentoInterno() {
		return movimentoInterno;
	}

	/**
	 * @param movimentoInterno the movimentoInterno to set
	 */
	public void setMovimentoInterno(Boolean movimentoInterno) {
		this.movimentoInterno = movimentoInterno;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setUid(getUidDaAggiornare());
		request.setRicercaAtti(ricercaAtti);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public AggiornaProvvedimento creaRequestAggiornaProvvedimento() {
		AggiornaProvvedimento request = creaRequest(AggiornaProvvedimento.class);
		
		request.setEnte(getEnte());
		
		request.setAttoAmministrativo(getAttoAmministrativo());
		request.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getAttoAmministrativo().getStrutturaAmmContabile()));
		
		request.setTipoAtto(getAttoAmministrativo().getTipoAtto());
		return request;
	}

	public String getCodiceInc() {
		return codiceInc;
	}

	public void setCodiceInc(String codiceInc) {
		this.codiceInc = codiceInc;
	}


}
