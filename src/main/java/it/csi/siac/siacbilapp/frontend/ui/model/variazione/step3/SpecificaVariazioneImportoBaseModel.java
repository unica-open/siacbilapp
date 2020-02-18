/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;

/**
 * Classe di model per la specificazione della variazione degli importi. Contiene i campi comuni alla specifica variazioneImporti con e senza UEB
 * 
 * @author Elisa Chiari
 * @version 1.0.0 29/12/2016
 *
 */
public class SpecificaVariazioneImportoBaseModel extends SpecificaVariazioneModel {
	

	/**Per la serializzazione*/
	private static final long serialVersionUID = -1390740145462869241L;
	private Boolean ignoraValidazione = Boolean.FALSE;
	//CR-4330
	private Boolean ignoraValidazioneImportiDopoDefinizione = Boolean.FALSE;
	private boolean cassaIncongruente;
	private boolean cassaIncongruenteDopoDefinizione;
	//SIAC-6705
	private StatoOperativoElementoDiBilancio statoOperativoCapitolo;
	private List<StatoOperativoElementoDiBilancio> statiOperativiElementoDiBilancio = new ArrayList<StatoOperativoElementoDiBilancio>();
	
	/** Costruttore vuoto di default */
	public SpecificaVariazioneImportoBaseModel() {
		super();
	}

	/**
	 * @return the ignoraValidazione
	 */
	public Boolean getIgnoraValidazione() {
		return ignoraValidazione;
	}

	/**
	 * @param ignoraValidazione the ignoraValidazione to set
	 */
	public void setIgnoraValidazione(Boolean ignoraValidazione) {
		this.ignoraValidazione = ignoraValidazione;
	}

	/**
	 * @return the ignoraValidazioneCapitoloDopoDefinizione
	 */
	public Boolean getIgnoraValidazioneImportiDopoDefinizione() {
		return ignoraValidazioneImportiDopoDefinizione;
	}

	/**
	 * @param ignoraValidazioneCapitoloDopoDefinizione the ignoraValidazioneCapitoloDopoDefinizione to set 
	 */
	public void setIgnoraValidazioneImportiDopoDefinizione(Boolean ignoraValidazioneCapitoloDopoDefinizione) {
		this.ignoraValidazioneImportiDopoDefinizione = ignoraValidazioneCapitoloDopoDefinizione;
	}

	/**
	 * @return the isCassaIncongruente
	 */
	public boolean isCassaIncongruente() {
		return cassaIncongruente;
	}

	/**
	 * @param isCassaIncongruente the isCassaIncongruente
	 */
	public void setCassaIncongruente(boolean isCassaIncongruente) {
		this.cassaIncongruente = isCassaIncongruente;
	}

	/**
	 * @return isCassaDopoDefinizioneIncongruente
	 */
	public boolean isCassaIncongruenteDopoDefinizione() {
		return cassaIncongruenteDopoDefinizione;
	}

	/**
	 * @param isCassaDopoDefinizioneIncongruente the isCassaDopoDefinizioneIncongruente
	 */
	public void setCassaDopoDefinizioneIncongruente(boolean isCassaDopoDefinizioneIncongruente) {
		this.cassaIncongruenteDopoDefinizione = isCassaDopoDefinizioneIncongruente;
	}

	/**
	 * @return the staoOperativoCapitolo
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativoCapitolo() {
		return statoOperativoCapitolo;
	}

	/**
	 * @param staoOperativoCapitolo the staoOperativoCapitolo to set
	 */
	public void setStatoOperativoCapitolo(StatoOperativoElementoDiBilancio staoOperativoCapitolo) {
		this.statoOperativoCapitolo = staoOperativoCapitolo;
	}
	
	/**
	 * @return the statiOperativiElementoDiBilancio
	 */
	public List<StatoOperativoElementoDiBilancio> getStatiOperativiElementoDiBilancio() {
		return statiOperativiElementoDiBilancio;
	}

	/**
	 * @param statiOperativiElementoDiBilancio the statiOperativiElementoDiBilancio to set
	 */
	public void setStatiOperativiElementoDiBilancio(List<StatoOperativoElementoDiBilancio> statiOperativiElementoDiBilancio) {
		this.statiOperativiElementoDiBilancio = statiOperativiElementoDiBilancio;
	}

}
