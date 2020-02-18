/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

/**
 * Model per la visualizzazione dei risultati di ricerca per l'Allegato Atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 15/set/2014
 * 
 */
public class RisultatiRicercaAllegatoAttoBaseModel extends GenericAllegatoAttoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 806056769612045208L;


	private Integer savedDisplayStart;
	private String stringaRiepilogo;
	
	// Per le azioni da delegare all'esterno
	private Integer uidAllegatoAtto;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoBaseModel() {
		super();
		setTitolo("Risultati di ricerca allegati atto");
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
	 * @return the stringaRiepilogo
	 */
	public String getStringaRiepilogo() {
		return stringaRiepilogo;
	}

	/**
	 * @param stringaRiepilogo the stringaRiepilogo to set
	 */
	public void setStringaRiepilogo(String stringaRiepilogo) {
		this.stringaRiepilogo = stringaRiepilogo;
	}

	/**
	 * @return the uidAllegato
	 */
	public Integer getUidAllegatoAtto() {
		return uidAllegatoAtto;
	}

	/**
	 * @param uidAllegatoAtto the uidAllegatoAtto to set
	 */
	public void setUidAllegatoAtto(Integer uidAllegatoAtto) {
		this.uidAllegatoAtto = uidAllegatoAtto;
	}

}
