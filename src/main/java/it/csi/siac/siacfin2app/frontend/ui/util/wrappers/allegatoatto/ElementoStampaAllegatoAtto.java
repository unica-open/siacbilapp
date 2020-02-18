/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.io.Serializable;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;


/**
 * Wrapper per la StampaAllegatoAtto
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 30/12/2015
 *
 */
public class ElementoStampaAllegatoAtto implements ModelWrapper, Serializable {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -1547506300688552937L;
	private final AllegatoAttoStampa allegatoAttoStampa;
	private String azioni;
	
	/**
	 * Costruttore a partire dal file
	 * 
	 * @param allegatoAttoStampa il file il file da wrappare
	 */	
	public ElementoStampaAllegatoAtto (AllegatoAttoStampa allegatoAttoStampa){
		this.allegatoAttoStampa = allegatoAttoStampa;
	}
	
	/**
	 * @return the stringaAttoAmministrativo
	 */
	public String getStringaAttoAmministrativo() {
		final String delim = "/";
		if(allegatoAttoStampa.getAllegatoAtto() == null || allegatoAttoStampa.getAllegatoAtto().getAttoAmministrativo() == null) {
			return "";
		}
		final AttoAmministrativo atto = allegatoAttoStampa.getAllegatoAtto().getAttoAmministrativo();
		StringBuilder result = new StringBuilder();
		// Anno provvedimento + numero del provvedimento + codice tipo atto + codice struttura amministrativo contabile separati dal carattere '/'.
		result.append(atto.getAnno())
			.append(delim)
			.append(atto.getNumero())
			.append(delim)
			.append(atto.getTipoAtto().getCodice());
		// La SAC non e' obbligatoria
		if(atto.getStrutturaAmmContabile() != null) {
			result.append(delim)
				.append(atto.getStrutturaAmmContabile().getCodice());
		}
		return result.toString();
		//return "";
	}
	
	/**
	 * @return the tipoStampa
	 */
	public String getTipoStampa() {
		return allegatoAttoStampa != null && allegatoAttoStampa.getTipoStampa() != null ? allegatoAttoStampa.getTipoStampa().getDescrizione().toUpperCase() : "";
	}


	
	@Override
	public int getUid() {
		return allegatoAttoStampa != null ? allegatoAttoStampa.getUid() : 0;
	}
	
	/**
	 * @return the uidFile
	 */
	public Integer getUidFile() {
		File file = ottieniPrimoFile();
		if(file != null) {
			// Prendo il primo file
			return file.getUid();
		}
		return null;
	}

	/**
	 * @return the dataCreazione
	 */
	public String getDataCreazione() {
		if(allegatoAttoStampa != null && allegatoAttoStampa.getDataCreazione() != null) {
			return FormatUtils.formatDateTime(allegatoAttoStampa.getDataCreazione());
		}
		return "";
	}
	
	/**
	 * @return the versione
	 */
	public String getVersione() {
		return allegatoAttoStampa.getVersioneInvioFirma()!=null? allegatoAttoStampa.getVersioneInvioFirma().toString() : "" ;
	}
	
	/**
	 * Ottiene il primo file associato alla stampa.
	 * 
	 * @return il primo file associato
	 */
	private File ottieniPrimoFile() {
		if(allegatoAttoStampa != null && !allegatoAttoStampa.getFiles().isEmpty()) {
			// Prendo lo 0-simo registro
			return allegatoAttoStampa.getFiles().get(0);
		}
		return null;
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
	 * @return the allegatoAttoStampa
	 */
	public AllegatoAttoStampa getAllegatoAttoStampa() {
		return allegatoAttoStampa;
	}

}
