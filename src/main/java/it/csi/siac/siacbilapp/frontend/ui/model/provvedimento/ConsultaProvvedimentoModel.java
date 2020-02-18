/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.provvedimento;

import java.util.Date;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model per la consultazione di un Provvedimento. Contiene una mappatura del model.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 26/09/2013
 *
 */
public class ConsultaProvvedimentoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 598802889235100200L;
	
	private Integer uidDaConsultare;
	private AttoAmministrativo attoAmministrativo;
	
	/** Costruttore vuoto di default */
	public ConsultaProvvedimentoModel() {
		super();
		setTitolo("Consulta provvedimento");
	}
	
	/* Getter e setter */

	/**
	 * @return the uidDaConsultare
	 */
	public Integer getUidDaConsultare() {
		return uidDaConsultare;
	}
	
	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(Integer uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
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
	
	/* Request */
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento} a partire dal Model.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = new RicercaProvvedimento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		request.setRicercaAtti(creaUtilityRicercaAtti());
		
		return request;
	}
	
	/**
	 * Crea un'utilit&agrave; per la Ricerca degli atti.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	public RicercaAtti creaUtilityRicercaAtti() {
		RicercaAtti utility = new RicercaAtti();
		
		utility.setUid(getUidDaConsultare());
		
		return utility;
	}
	
	
}
