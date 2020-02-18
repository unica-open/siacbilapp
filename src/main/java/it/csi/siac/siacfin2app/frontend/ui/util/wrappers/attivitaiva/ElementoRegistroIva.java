/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Wrapper per il RegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/06/2014
 *
 */
public class ElementoRegistroIva implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5373837455635805301L;
	
	private final RegistroIva registroIva;
	
	private String azioni;
	
	/**
	 * Costruttore di wrap
	 * 
	 * @param registroIva il registro da wrappare
	 */
	public ElementoRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}
	
	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return registroIva != null ? registroIva.getCodice() : "";
	}
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		// SIAC-6214
		return registroIva != null ? registroIva.getDescrizione() : "";
	}
	
	/**
	 * @return the gruppoAttivitaIva
	 */
	public String getGruppoAttivitaIva() {
		if(registroIva == null || registroIva.getGruppoAttivitaIva() == null) {
			return "";
		}
		GruppoAttivitaIva gai = registroIva.getGruppoAttivitaIva();
		return gai.getCodice() + " - " + gai.getDescrizione();
	}
	
	/**
	 * @return the gruppoAttivitaIva
	 */
	public String getTipoRegistroIva() {
		if(registroIva == null || registroIva.getTipoRegistroIva() == null) {
			return "";
		}
		TipoRegistroIva tri = registroIva.getTipoRegistroIva();
		return tri.getCodice() + " - " + tri.getDescrizione();
	}
	
	/**
	 * @return the descrizioneTipoRegistroIva
	 */
	public String getDescrizioneTipoRegistroIva() {
		return registroIva.getTipoRegistroIva() != null ? registroIva.getTipoRegistroIva().getDescrizione() : "";
	}
	
	/**
	 * @return the flagLiquidazioneIva
	 */
	public String getIsFlagLiquidazioneIva() {
		// SIAC-6276 CR-1179B
		return Boolean.TRUE.equals(registroIva.getFlagLiquidazioneIva())? "S&igrave;" : "No";
	}
	
	/**
	 * @return la stringa corrispondente ("Si" se il registro &egrave; bloccato, "No" altrimenti)
	 */
	public String getIsRegistroBloccatoStringa(){
		if(registroIva == null) {
			return "";
		}
		return Boolean.TRUE.equals(registroIva.getFlagBloccato())? "S&igrave;" : "No";
	}
	
	/**
	 * @return the flagBloccato
	 */
	public Boolean getFlagBloccato(){
		return registroIva != null ? registroIva.getFlagBloccato() : null;
	}

	@Override
	public int getUid() {
		return registroIva != null ? registroIva.getUid() : 0;
	}
	
}
