/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Model di base per la visualizzazione dei risultati di ricerca per la RegistrazioneMovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 * 
 */
public abstract class RisultatiRicercaRegistrazioneMovFinBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8241900113760082439L;
	
	// Properties necessarie per pilotare la dataTable con il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private int uidDaAnnullare;
	private int uidDaConsultare;
	private int uidDaCompletare;
	private int uidDaValidare;
	private int uidPrimaNotaRegistrazioneDaConsultare;
	
	// Per separare la gestione prima nota integrata dei DOC 
	private String nomeAzione;
	private boolean isValidazione;
	private Integer numeroMovimentoDaConsultare;
	private Integer numeroSubMovimentoDaConsultare;
	private Integer annoMovimentoDaConsultare;

	private String riepilogoRicerca;
	
	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the uidDaAnnullare
	 */
	public int getUidDaAnnullare() {
		return uidDaAnnullare;
	}

	/**
	 * @param uidDaAnnullare the uidDaAnnullare to set
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		this.uidDaAnnullare = uidDaAnnullare;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public int getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}

	/**
	 * @return the uidDaCompletare
	 */
	public int getUidDaCompletare() {
		return uidDaCompletare;
	}

	/**
	 * @param uidDaCompletare the uidDaCompletare to set
	 */
	public void setUidDaCompletare(int uidDaCompletare) {
		this.uidDaCompletare = uidDaCompletare;
	}

	/**
	 * @return the uidDaValidare
	 */
	public int getUidDaValidare() {
		return uidDaValidare;
	}

	/**
	 * @param uidDaValidare the uidDaValidare to set
	 */
	public void setUidDaValidare(int uidDaValidare) {
		this.uidDaValidare = uidDaValidare;
	}
	
	/**
	 * @return the uidPrimaNotaRegistrazioneDaConsultare
	 */
	public int getUidPrimaNotaRegistrazioneDaConsultare() {
		return uidPrimaNotaRegistrazioneDaConsultare;
	}

	/**
	 * @param uidPrimaNotaRegistrazioneDaConsultare the uidPrimaNotaRegistrazioneDaConsultare to set
	 */
	public void setUidPrimaNotaRegistrazioneDaConsultare(
			int uidPrimaNotaRegistrazioneDaConsultare) {
		this.uidPrimaNotaRegistrazioneDaConsultare = uidPrimaNotaRegistrazioneDaConsultare;
	}

	/**
	 * @return the nomeAzione
	 */
	public String getNomeAzione() {
		return nomeAzione;
	}

	/**
	 * @param nomeAzione the nomeAzione to set
	 */
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}

	/**
	 * @return the isValidazione
	 */
	public boolean isValidazione() {
		return isValidazione;
	}

	/**
	 * @param isValidazione the isValidazione to set
	 */
	public void setValidazione(boolean isValidazione) {
		this.isValidazione = isValidazione;
	}

	/**
	 * @return the numeroMovimentoDaConsultare
	 */
	public Integer getNumeroMovimentoDaConsultare() {
		return numeroMovimentoDaConsultare;
	}

	/**
	 * @param numeroMovimentoDaConsultare the numeroMovimentoDaConsultare to set
	 */
	public void setNumeroMovimentoDaConsultare(Integer numeroMovimentoDaConsultare) {
		this.numeroMovimentoDaConsultare = numeroMovimentoDaConsultare;
	}

	/**
	 * @return the numeroSubMovimentoDaConsultare
	 */
	public Integer getNumeroSubMovimentoDaConsultare() {
		return numeroSubMovimentoDaConsultare;
	}

	/**
	 * @param numeroSubMovimentoDaConsultare the numeroSubMovimentoDaConsultare to set
	 */
	public void setNumeroSubMovimentoDaConsultare(
			Integer numeroSubMovimentoDaConsultare) {
		this.numeroSubMovimentoDaConsultare = numeroSubMovimentoDaConsultare;
	}

	/**
	 * @return the annoMovimentoDaConsultare
	 */
	public Integer getAnnoMovimentoDaConsultare() {
		return annoMovimentoDaConsultare;
	}

	/**
	 * @param annoMovimentoDaConsultare the annoMovimentoDaConsultare to set
	 */
	public void setAnnoMovimentoDaConsultare(Integer annoMovimentoDaConsultare) {
		this.annoMovimentoDaConsultare = annoMovimentoDaConsultare;
	}

	/**
	 * @return the riepilogoRicerca
	 */
	public String getRiepilogoRicerca() {
		return riepilogoRicerca;
	}

	/**
	 * @param riepilogoRicerca the riepilogoRicerca to set
	 */
	public void setRiepilogoRicerca(String riepilogoRicerca) {
		this.riepilogoRicerca = riepilogoRicerca;
	}

	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();

	/* Requests */

	/**
	 * Crea una request per il servizio di {@link AssegnaContoEPRegistrazioneMovFin}.
	 * 
	 * @return la request creata
	 */
	public AssegnaContoEPRegistrazioneMovFin creaRequestAssegnaContoEPRegistrazioneMovFin() {
		AssegnaContoEPRegistrazioneMovFin request = creaRequest(AssegnaContoEPRegistrazioneMovFin.class);
		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(getUidDaValidare());
		registrazioneMovFin.setAmbito(getAmbito());
		request.setRegistrazioneMovFin(registrazioneMovFin);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RegistraMassivaPrimaNotaIntegrata}.
	 * @param r la request di ricerca sintetica
	 * @return la request creata
	 */
	public RegistraMassivaPrimaNotaIntegrata creaRequestRegistraMassivaPrimaNotaIntegrata(RicercaSinteticaRegistrazioneMovFin r) {
		RegistraMassivaPrimaNotaIntegrata req = creaRequest(RegistraMassivaPrimaNotaIntegrata.class);
		
		// Clono dalla ricerca sintetica
		req.setRegistrazioneMovFin(r.getRegistrazioneMovFin());
		req.setTipoEvento(r.getTipoEvento());
		req.setEvento(r.getEvento());
		req.setEventoRegistrazioneIniziale(r.getEventoRegistrazioneIniziale());
		req.setAnnoMovimento(r.getAnnoMovimento());
		req.setNumeroMovimento(r.getNumeroMovimento());
		req.setNumeroSubmovimento(r.getNumeroSubmovimento());
		req.setDataRegistrazioneDa(r.getDataRegistrazioneDa());
		req.setDataRegistrazioneA(r.getDataRegistrazioneA());
		req.setIdDocumento(r.getIdDocumento());
		//SIAC-6248
		req.setCapitolo(r.getCapitolo());
		req.setAttoAmministrativo(r.getAttoAmministrativo());
		req.setSoggetto(r.getSoggetto());
		
		return req;
	}
}
