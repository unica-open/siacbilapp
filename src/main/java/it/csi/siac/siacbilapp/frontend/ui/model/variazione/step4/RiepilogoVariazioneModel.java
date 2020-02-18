/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step4;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoVariazione;

/**
 * Classe di model per la specificazione della variazione degli importi nel caso della gestione delle UEB. Contiene una mappatura del model.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 15/10/2013
 *
 */
public class RiepilogoVariazioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7524801026683580575L;
	
	private Integer numeroVariazione;
	private StatoOperativoVariazioneDiBilancio statoVariazione;
	private String applicazioneVariazione;
	private String descrizioneVariazione;
	private TipoVariazione tipoVariazione;
	private Integer annoVariazione;
	private String noteVariazione;
	
	/** Costruttore vuoto di default */
	public RiepilogoVariazioneModel() {
		super();
	}

	/**
	 * @return the numeroVariazione
	 */
	public Integer getNumeroVariazione() {
		return numeroVariazione;
	}

	/**
	 * @param numeroVariazione the numeroVariazione to set
	 */
	public void setNumeroVariazione(Integer numeroVariazione) {
		this.numeroVariazione = numeroVariazione;
	}

	/**
	 * @return the statoVariazione
	 */
	public StatoOperativoVariazioneDiBilancio getStatoVariazione() {
		return statoVariazione;
	}

	/**
	 * @param statoVariazione the statoVariazione to set
	 */
	public void setStatoVariazione(
			StatoOperativoVariazioneDiBilancio statoVariazione) {
		this.statoVariazione = statoVariazione;
	}

	/**
	 * @return the applicazioneVariazione
	 */
	public String getApplicazioneVariazione() {
		return applicazioneVariazione;
	}

	/**
	 * @param applicazioneVariazione the applicazioneVariazione to set
	 */
	public void setApplicazioneVariazione(String applicazioneVariazione) {
		this.applicazioneVariazione = applicazioneVariazione;
	}

	/**
	 * @return the descrizioneVariazione
	 */
	public String getDescrizioneVariazione() {
		return descrizioneVariazione;
	}

	/**
	 * @param descrizioneVariazione the descrizioneVariazione to set
	 */
	public void setDescrizioneVariazione(String descrizioneVariazione) {
		this.descrizioneVariazione = descrizioneVariazione;
	}

	/**
	 * @return the tipoVariazione
	 */
	public TipoVariazione getTipoVariazione() {
		return tipoVariazione;
	}

	/**
	 * @param tipoVariazione the tipoVariazione to set
	 */
	public void setTipoVariazione(TipoVariazione tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}

	/**
	 * @return the annoVariazione
	 */
	public Integer getAnnoVariazione() {
		return annoVariazione;
	}

	/**
	 * @param annoVariazione the annoVariazione to set
	 */
	public void setAnnoVariazione(Integer annoVariazione) {
		this.annoVariazione = annoVariazione;
	}

	/**
	 * @return the noteVariazione
	 */
	public String getNoteVariazione() {
		return noteVariazione;
	}

	/**
	 * @param noteVariazione the noteVariazione to set
	 */
	public void setNoteVariazione(String noteVariazione) {
		this.noteVariazione = noteVariazione;
	}

}
