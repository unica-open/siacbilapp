/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.io.Serializable;
import java.util.Date;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

/**
 * Wrapper per l'Allegato Atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/set/2014
 *
 */
public class ElementoAllegatoAtto implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2835230601000217374L;
	
	private final AllegatoAtto allegatoAtto;
	private String azioni;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoAllegatoAtto(AllegatoAtto allegatoAtto) {
		this.allegatoAtto = allegatoAtto;
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
		return allegatoAtto == null ? 0 : allegatoAtto.getUid();
	}
	
	// Utilita' per il Javascript
	
	/**
	 * @return the stringaAttoAmministrativo
	 */
	public String getStringaAttoAmministrativo() {
		final String delim = "/";
		// Se non ho i dati, esco
		if(allegatoAtto == null || allegatoAtto.getAttoAmministrativo() == null) {
			return "";
		}
		
		final AttoAmministrativo atto = allegatoAtto.getAttoAmministrativo();
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
	}
	
	/**
	 * @return the stringaRitenute
	 */
	public String getStringaRitenute() {
		return allegatoAtto != null && Boolean.TRUE.equals(allegatoAtto.getFlagRitenute()) ? "RIT" : "";
	}
	
	/**
	 * @return the stringaCausale
	 */
	public String getStringaCausale() {
		return allegatoAtto != null ? allegatoAtto.getCausale() : "";
	}
	
	/**
	 * @return the stringaDataScadenza
	 */
	public String getStringaDataScadenza() {
		return allegatoAtto != null ? FormatUtils.formatDate(allegatoAtto.getDataScadenza()) : "";
	}
	
	/**
	 * @return the stringaDataStatoOperativo
	 */
	public String getStringaStatoOperativo() {
		return allegatoAtto != null && allegatoAtto.getStatoOperativoAllegatoAtto() != null
			? allegatoAtto.getStatoOperativoAllegatoAtto().getDescrizione()
			: "";
	}
	
	/**
	 * @return the stringaOrdinativi
	 */
	public String getStringaOrdinativi() {
		return allegatoAtto != null && Boolean.TRUE.equals(allegatoAtto.getIsAssociatoAdUnSubdocumentoConOrdinativo()) ? "S&Igrave;" : "NO";
	}
	
	/**
	 * @return the versioneInvioFirma
	 */
	public Integer getVersioneInvioFirma() {
		return allegatoAtto != null ? allegatoAtto.getVersioneInvioFirmaNotNull() : Integer.valueOf(0);
	}
	
	/**
	 * @return the stringaSospensione
	 */
	public String getStringaSospensione() {
		return allegatoAtto != null && Boolean.TRUE.equals(allegatoAtto.getIsAssociatoAdUnSubdocumentoSospeso()) ? "SOS" : "";
	}
	
	// Utilita' per la action
	/**
	 * @return the statoOperativoAllegatoAtto
	 */
	public StatoOperativoAllegatoAtto getStatoOperativoAllegatoAtto() {
		return allegatoAtto != null ? allegatoAtto.getStatoOperativoAllegatoAtto() : null;
	}

	/**
	 * @return the isAssociatoAdAlmenoUnDocumento
	 */
	public boolean getIsAssociatoAdAlmenoUnDocumento() {
		return allegatoAtto != null && allegatoAtto.getIsAssociatoAdAlmenoUnDocumento() != null && allegatoAtto.getIsAssociatoAdAlmenoUnDocumento().booleanValue();
	}
	
	/**
	 * @return the isAssociatoAdAlmenoUnaQuotaSpesa
	 */
	public boolean getIsAssociatoAdAlmenoUnaQuotaSpesa() {
		return allegatoAtto != null && allegatoAtto.getIsAssociatoAdAlmenoUnaQuotaSpesa() != null && allegatoAtto.getIsAssociatoAdAlmenoUnaQuotaSpesa().booleanValue();
	}
	
	/**
	 * Gets the checks for impegno conferma durc.
	 *
	 * @return the checks for impegno conferma durc
	 */
	public boolean getHasImpegnoConfermaDurc() {
		return allegatoAtto != null && Boolean.TRUE.equals(allegatoAtto.getHasImpegnoConfermaDurc());
	}
	
	/**
	 * Inibisci selezione.
	 *
	 * @return true, if successful
	 */
	public boolean getInibisciSelezione() {
		if(!getIsAssociatoAdAlmenoUnDocumento()) {
			return true;
		}		
		return getHasImpegnoConfermaDurc() && isDurcScadutoONull();
	}
	
	
	/**
	 * Checks if is durc scaduto O null.
	 *
	 * @return true, if is durc scaduto O null
	 */
	private boolean isDurcScadutoONull() {
		Date dataFineValiditaDurc = this.allegatoAtto.getDataFineValiditaDurc();
		if(dataFineValiditaDurc == null) {
			return true;
		}
		Date now = new Date();
		return now.compareTo(dataFineValiditaDurc)>0;
	}
	/**
	 * Gets the string dom rich durc.
	 *
	 * @return the string dom rich durc
	 */
	public String getStringaDomRichDurc() {
		return FormatUtils.formatBoolean(getHasImpegnoConfermaDurc(), "S&igrave;", "No");
	}
	
	/**
	 * Gets the data fine validita durc.
	 *
	 * @return the data fine validita durc
	 */
	public String getStringaDataFineValiditaDurc() {
		if(!getHasImpegnoConfermaDurc()) {
			return "";
		}
		return this.allegatoAtto.getDataFineValiditaDurc() != null? FormatUtils.formatDate(this.allegatoAtto.getDataFineValiditaDurc()) : "Durc da richiedere";
	}
}
