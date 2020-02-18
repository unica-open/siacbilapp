/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe;

import java.io.Serializable;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccorser.model.file.File;

/**
 * Wrapper per la StampaIva.
 * 
 * @author Valentina Triolo
 * @version 1.0.0 - 01/04/2015
 *
 */
public class ElementoStampeCassaFile implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5260223277214449517L;
	
	private StampeCassaFile stampeCassaFile;
	private String azioni;
	
	/**
	 * Costruttore a partire dalla stampa iva.
	 * 
	 * @param stampeCassaFile la stampa iva da wrappare
	 */
	public ElementoStampeCassaFile(StampeCassaFile stampeCassaFile) {
		this.stampeCassaFile = stampeCassaFile;
	}

	/**
	 * @return the stampeCassaFile
	 */
	public StampeCassaFile getStampeCassaFile() {
		return stampeCassaFile;
	}

	/**
	 * @param stampeCassaFile the stampeCassaFile to set
	 */
	public void setStampeCassaFile(StampeCassaFile stampeCassaFile) {
		this.stampeCassaFile = stampeCassaFile;
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
		return stampeCassaFile != null ? stampeCassaFile.getUid() : 0;
	}


	/**
	 * @return the tipo
	 */
	public String getTipoStampa() {
		return stampeCassaFile != null && stampeCassaFile.getTipoStampa() != null ? stampeCassaFile.getTipoStampa().getDescrizione() : "";
	}

	/**
	 * @return the periodo
	 */
	public String getPeriodoDa() {
		if(TipoDocumento.GIORNALE_CASSA.equals(stampeCassaFile.getTipoDocumento())){
			return (stampeCassaFile.getStampaGiornale() != null && stampeCassaFile.getStampaGiornale().getDataUltimaStampa() != null) ?
					FormatUtils.formatDate(stampeCassaFile.getStampaGiornale().getDataUltimaStampa()) : "";
		}
		if(TipoDocumento.RENDICONTO.equals(stampeCassaFile.getTipoDocumento())){
			return (stampeCassaFile.getStampaRendiconto() != null && stampeCassaFile.getStampaRendiconto().getPeriodoDataInizio() != null) ?
					FormatUtils.formatDate(stampeCassaFile.getStampaRendiconto().getPeriodoDataInizio()) : "";
		}
		return "";
	}
	
	/**
	 * @return the periodoA
	 */
	public String getPeriodoA() {
		if(TipoDocumento.RENDICONTO.equals(stampeCassaFile.getTipoDocumento())){
			return (stampeCassaFile.getStampaRendiconto() != null && stampeCassaFile.getStampaRendiconto().getPeriodoDataFine() != null) ?
					FormatUtils.formatDate(stampeCassaFile.getStampaRendiconto().getPeriodoDataFine()) : "";
		}
		return "";
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
		if(stampeCassaFile != null && stampeCassaFile.getDataCreazioneStampeCassaFile() != null) {
			return FormatUtils.formatDate(stampeCassaFile.getDataCreazioneStampeCassaFile(), "dd/MM/yyyy HH:mm");
		}
		return "";
	}
	
	/**
	 * Controlla che alla stampa sia legato un unico file.
	 * 
	 * @return the fileSingolo
	 */
	public boolean isFileSingolo() {
		return stampeCassaFile != null && stampeCassaFile.getFiles().size() == 1;
	}
	
	/**
	 * @return the allegatoAtto
	 */
	public String getAllegatoAtto() {
		if(stampeCassaFile.getAllegatoAtto() == null || stampeCassaFile.getAllegatoAtto().getAttoAmministrativo() == null) {
			return "";
		}
		AttoAmministrativo aa = stampeCassaFile.getAllegatoAtto().getAttoAmministrativo();
		StringBuilder sb = new StringBuilder()
				.append(aa.getAnno())
				.append("/")
				.append(aa.getNumero());
		if(aa.getTipoAtto() != null) {
			sb.append("/")
				.append(aa.getTipoAtto().getCodice());
		}
		if(aa.getStrutturaAmmContabile() != null) {
			sb.append("/")
				.append(aa.getStrutturaAmmContabile().getCodice());
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Ottiene il primo file associato alla stampa.
	 * 
	 * @return il primo file associato
	 */
	private File ottieniPrimoFile() {
		if(stampeCassaFile != null && !stampeCassaFile.getFiles().isEmpty()) {
			// Prendo lo 0-simo registro
			return stampeCassaFile.getFiles().get(0);
		}
		return null;
	}

}
