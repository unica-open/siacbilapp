/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Wrapper per la StampaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/01/2015
 *
 */
public class ElementoStampaIva implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5260223277214449517L;
	
	private StampaIva stampaIva;
	private String azioni;
	
	/**
	 * Costruttore a partire dalla stampa iva.
	 * 
	 * @param stampaIva la stampa iva da wrappare
	 */
	public ElementoStampaIva(StampaIva stampaIva) {
		this.stampaIva = stampaIva;
	}

	/**
	 * @return the stampaIva
	 */
	public StampaIva getStampaIva() {
		return stampaIva;
	}

	/**
	 * @param stampaIva the stampaIva to set
	 */
	public void setStampaIva(StampaIva stampaIva) {
		this.stampaIva = stampaIva;
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

	@Override
	public int getUid() {
		return stampaIva != null ? stampaIva.getUid() : 0;
	}
	
	/**
	 * @return the annoEsercizio
	 */
	public String getAnnoEsercizio() {
		return stampaIva != null && stampaIva.getAnnoEsercizio() != null ? stampaIva.getAnnoEsercizio().toString() : "";
	}
	
	/**
	 * @return the gruppoAttivitaIva
	 */
	public String getGruppoAttivitaIva() {
		StringBuilder sb = new StringBuilder();
		RegistroIva ri = ottieniPrimoRegistroIva();
		if(ri != null && ri.getGruppoAttivitaIva() != null) {
			sb.append(ri.getGruppoAttivitaIva().getCodice())
				.append(" - ")
				.append(ri.getGruppoAttivitaIva().getDescrizione());
		}
		return sb.toString();
	}
	
	/**
	 * @return the registroIva
	 */
	public String getRegistroIva() {
		StringBuilder sb = new StringBuilder();
		RegistroIva ri = ottieniPrimoRegistroIva();
		if(ri != null) {
			TipoRegistroIva tri = ri.getTipoRegistroIva();
			if(tri != null) {
				sb.append("<a data-original-title='Tipo registro iva' data-trigger='hover' rel='popover' data-content='")
					.append(FormatUtils.formatHtmlAttributeString(ri.getTipoRegistroIva().getDescrizione()))
					.append("'>");
			}
			
			sb.append(ri.getCodice())
				.append(" - ")
				.append(ri.getDescrizione());
			if(tri != null) {
				sb.append("</a>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return the periodo
	 */
	public String getPeriodo() {
		return stampaIva != null && stampaIva.getPeriodo() != null ? stampaIva.getPeriodo().getDescrizione() : "";
	}
	
	/**
	 * @return the periodo
	 */
	public String getTipoStampa() {
		return stampaIva != null && stampaIva.getTipoStampa() != null ? stampaIva.getTipoStampa().getDescrizione().toUpperCase() : "";
	}
	
	/**
	 * @return the periodo
	 */
	public String getPeriodoTipoStampa() {
		
		return getPeriodo() + " - " + getTipoStampa();
	}
	
	/**
	 * @return the nomeFile
	 */
	public String getNomeFile() {
		File file = ottieniPrimoFile();
		if(file != null) {
			// Prendo il primo file
			return file.getNome();
		}
		return "";
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
		if(stampaIva != null && stampaIva.getDataCreazione() != null) {
			return FormatUtils.formatDateTime(stampaIva.getDataCreazione());
		}
		return "";
	}
	
	/**
	 * Controlla che alla stampa sia legato un unico file.
	 * 
	 * @return the fileSingolo
	 */
	public boolean isFileSingolo() {
		return stampaIva != null && stampaIva.getFiles().size() == 1;
	}
	
	/**
	 * Ottiene il primo registro iva associato alla stampa.
	 * 
	 * @return il primo registroIva associato
	 */
	private RegistroIva ottieniPrimoRegistroIva() {
		if(stampaIva != null && !stampaIva.getListaRegistroIva().isEmpty()) {
			// Prendo lo 0-simo registro
			return stampaIva.getListaRegistroIva().get(0);
		}
		return null;
	}
	
	/**
	 * Ottiene il primo file associato alla stampa.
	 * 
	 * @return il primo file associato
	 */
	private File ottieniPrimoFile() {
		if(stampaIva != null && !stampaIva.getFiles().isEmpty()) {
			// Prendo lo 0-simo registro
			return stampaIva.getFiles().get(0);
		}
		return null;
	}

}
