/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.modificabilita;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Wrapper per il controllo dell'editabilit&agrave; dei classificatori.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 08/10/2013
 *
 */
public class ElementoControlloClassificatoriEditabili implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4661210184441377032L;
	
	private Map<TipologiaClassificatore, Integer> classificatoriNonModificabili = new HashMap<TipologiaClassificatore, Integer>();
	
	/** Costruttore vuoto di default */
	public ElementoControlloClassificatoriEditabili() {
		super();
	}

	/**
	 * @return the classificatoriNonModificabili
	 */
	public Map<TipologiaClassificatore, Integer> getClassificatoriNonModificabili() {
		return classificatoriNonModificabili;
	}

	/**
	 * @param classificatoriNonModificabili the classificatoriNonModificabili to set
	 */
	public void setClassificatoriNonModificabili(
			Map<TipologiaClassificatore, Integer> classificatoriNonModificabili) {
		this.classificatoriNonModificabili = classificatoriNonModificabili;
	}
	
	/**
	 * Ricrea la response a partire dal wrapper.
	 * 
	 * @return la response creata
	 */
	public ControllaClassificatoriModificabiliCapitoloResponse ottieniResponseControllaClassificatoriModificabiliCapitoloResponse() {
		ControllaClassificatoriModificabiliCapitoloResponse response = new ControllaClassificatoriModificabiliCapitoloResponse();
		
		response.setClassificatoriNonModificabili(classificatoriNonModificabili);
		
		return response;
	}
	
}
