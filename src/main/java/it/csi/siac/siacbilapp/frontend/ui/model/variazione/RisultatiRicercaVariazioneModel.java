/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Capitolo di Entrata Previsione.
 * 
 * @author Daniele Argiolas
 * 
 */
public class RisultatiRicercaVariazioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8675359642254632588L;
	
	private int savedDisplayStart;
	private Integer idAzioneReportVariazioni;
	
	// Per le azioni da delegare all'esterno
	private int uidVariazioneDaConsultare;
	private Boolean tipoVariazioneCodifica;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaVariazioneModel() {
		super();
		setTitolo("Risultati di ricerca Variazione");
	}


	/* Getter e Setter */
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
	 * @return the uidVariazioneDaConsultare
	 */
	public int getUidVariazioneDaConsultare() {
		return uidVariazioneDaConsultare;
	}


	/**
	 * @param uidVariazioneDaConsultare the uidVariazioneDaConsultare to set
	 */
	public void setUidVariazioneDaConsultare(int uidVariazioneDaConsultare) {
		this.uidVariazioneDaConsultare = uidVariazioneDaConsultare;
	}


	/**
	 * @return the tipoVariazioneCodifica
	 */
	public Boolean getTipoVariazioneCodifica() {
		return tipoVariazioneCodifica;
	}


	/**
	 * @param tipoVariazioneCodifica the tipoVariazioneCodifica to set
	 */
	public void setTipoVariazioneCodifica(Boolean tipoVariazioneCodifica) {
		this.tipoVariazioneCodifica = tipoVariazioneCodifica;
	}


	/**
	 * @return the idAzioneReportVariazioni
	 */
	public Integer getIdAzioneReportVariazioni() {
		return this.idAzioneReportVariazioni;
	}


	/**
	 * @param idAzioneReportVariazioni the idAzioneReportVariazioni to set
	 */
	public void setIdAzioneReportVariazioni(Integer idAzioneReportVariazioni) {
		this.idAzioneReportVariazioni = idAzioneReportVariazioni;
	}

}
