/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;

/**
 * Classe di model per l'aggiornamento del RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class RecuperaProtocolloRegistroIvaModel extends AggiornaRegistroIvaBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5916854908189509644L;
	private Integer numeroProtocolloDefinitivo;
	private Integer numeroProtocolloProvvisorio;
	private boolean isProtocolloProvvisorioModificabile;

	/** Costruttore vuoto di default */
	public RecuperaProtocolloRegistroIvaModel() {
		setTitolo("Recupera protocollo registro iva");
	}

	/**
	 * @return the numeroProtocolloDefinitivo
	 */
	public Integer getNumeroProtocolloDefinitivo() {
		return numeroProtocolloDefinitivo;
	}

	/**
	 * @return the numeroProtocolloProvvisorio
	 */
	public Integer getNumeroProtocolloProvvisorio() {
		return numeroProtocolloProvvisorio;
	}

	/**
	 * @param numeroProtocolloDefinitivo the numeroProtocolloDefinitivo to set
	 */
	public void setNumeroProtocolloDefinitivo(Integer numeroProtocolloDefinitivo) {
		this.numeroProtocolloDefinitivo = numeroProtocolloDefinitivo;
	}

	/**
	 * @param numeroProtocolloProvvisorio the numeroProtocolloProvvisorio to set
	 */
	public void setNumeroProtocolloProvvisorio(Integer numeroProtocolloProvvisorio) {
		this.numeroProtocolloProvvisorio = numeroProtocolloProvvisorio;
	}
	
	/**
	 * @return the isProtocolloProvvisorioModificabile
	 */
	public boolean isProtocolloProvvisorioModificabile() {
		return isProtocolloProvvisorioModificabile;
	}

	/**
	 * @param isProtocolloProvvisorioModificabile the isProtocolloProvvisorioModificabile to set
	 */
	public void setProtocolloProvvisorioModificabile(boolean isProtocolloProvvisorioModificabile) {
		this.isProtocolloProvvisorioModificabile = isProtocolloProvvisorioModificabile;
	}

	
	
	/* **** Requests **** */


	/**
	 * crea la request per il servizio di {@link RecuperaProtocolloRegistroIva}
	 * @return  la request creata
	 */
	public RecuperaProtocolloRegistroIva creaRequestRecuperaProtocolloRegistroIva() {
		RecuperaProtocolloRegistroIva request = creaRequest(RecuperaProtocolloRegistroIva.class);
		//TODO: controlla che sia giusto l'uid che viene passato
		request.setRegistroIva(creaRegistroIva());
		request.setNumeroProtocolloDefinitivo(getNumeroProtocolloDefinitivo());
		request.setNumeroProtocolloProvvisorio(getNumeroProtocolloProvvisorio());
		request.setBilancio(getBilancio());
		return request;
	}

	/**
	 * 
	 * @param registroIva the registroIva da cui prendere i dati per impostare il model
	 */
	public void impostaDatiRecuperaProtocollo(RegistroIva registroIva) {
		if(registroIva == null){
			return;
		}
		impostaDati(registroIva);
		setProtocolloProvvisorioModificabile(TipoEsigibilitaIva.DIFFERITA.equals(registroIva.getTipoRegistroIva().getTipoEsigibilitaIva()));		
	}




}
