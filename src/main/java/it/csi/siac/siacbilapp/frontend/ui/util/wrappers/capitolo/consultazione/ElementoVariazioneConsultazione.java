/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;

/**
 * Wrapper per i campi delle variazioni del capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/11/2013
 *
 */
public class ElementoVariazioneConsultazione implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3903186920288726585L;
	
	private long variazioniInAumentoBozzaNum0 = 0L;
	private long variazioniInAumentoDefinitivaNum0 = 0L;
	private long variazioniInAumentoPreDefinitivaNum0 = 0L;
	private long variazioniInAumentoGiuntaConsiglioNum0 = 0L;
	private long variazioniInAumentoAnnullataNum0 = 0L;
	private long variazioniInAumentoBozzaNum1 = 0L;
	private long variazioniInAumentoDefinitivaNum1 = 0L;
	private long variazioniInAumentoPreDefinitivaNum1 = 0L;
	private long variazioniInAumentoGiuntaConsiglioNum1 = 0L;
	private long variazioniInAumentoAnnullataNum1 = 0L;
	private long variazioniInAumentoBozzaNum2 = 0L;
	private long variazioniInAumentoDefinitivaNum2 = 0L;
	private long variazioniInAumentoPreDefinitivaNum2 = 0L;
	private long variazioniInAumentoGiuntaConsiglioNum2 = 0L;
	private long variazioniInAumentoAnnullataNum2 = 0L;
	
	private BigDecimal variazioniInAumentoBozzaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInAumentoDefinitivaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInAumentoPreDefinitivaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInAumentoGiuntaConsiglioCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInAumentoAnnullataCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataCassa2 = BigDecimal.ZERO;
	
	private long variazioniInDiminuzioneBozzaNum0 = 0L;
	private long variazioniInDiminuzioneDefinitivaNum0 = 0L;
	private long variazioniInDiminuzionePreDefinitivaNum0 = 0L;
	private long variazioniInDiminuzioneGiuntaConsiglioNum0 = 0L;
	private long variazioniInDiminuzioneAnnullataNum0 = 0L;
	private long variazioniInDiminuzioneBozzaNum1 = 0L;
	private long variazioniInDiminuzioneDefinitivaNum1 = 0L;
	private long variazioniInDiminuzionePreDefinitivaNum1 = 0L;
	private long variazioniInDiminuzioneGiuntaConsiglioNum1 = 0L;
	private long variazioniInDiminuzioneAnnullataNum1 = 0L;
	private long variazioniInDiminuzioneBozzaNum2 = 0L;
	private long variazioniInDiminuzioneDefinitivaNum2 = 0L;
	private long variazioniInDiminuzionePreDefinitivaNum2 = 0L;
	private long variazioniInDiminuzioneGiuntaConsiglioNum2 = 0L;
	private long variazioniInDiminuzioneAnnullataNum2 = 0L;
	
	private BigDecimal variazioniInDiminuzioneBozzaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInDiminuzioneDefinitivaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInDiminuzionePreDefinitivaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInDiminuzioneAnnullataCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataCassa2 = BigDecimal.ZERO;
	
	/* Capitolo equivalente */
	private long variazioniInAumentoBozzaEquivalenteNum = 0L;
	private long variazioniInAumentoDefinitivaEquivalenteNum = 0L;
	private long variazioniInAumentoPreDefinitivaEquivalenteNum = 0L;
	private long variazioniInAumentoGiuntaConsiglioEquivalenteNum = 0L;
	private long variazioniInAumentoAnnullataEquivalenteNum = 0L;
	
	private BigDecimal variazioniInAumentoBozzaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoBozzaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoDefinitivaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoPreDefinitivaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoGiuntaConsiglioEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInAumentoAnnullataEquivalenteCassa = BigDecimal.ZERO;
	
	private long variazioniInDiminuzioneBozzaEquivalenteNum = 0L;
	private long variazioniInDiminuzioneDefinitivaEquivalenteNum = 0L;
	private long variazioniInDiminuzionePreDefinitivaEquivalenteNum = 0L;
	private long variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum = 0L;
	private long variazioniInDiminuzioneAnnullataEquivalenteNum = 0L;
	
	private BigDecimal variazioniInDiminuzioneBozzaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneBozzaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneDefinitivaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzionePreDefinitivaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInDiminuzioneAnnullataEquivalenteCassa = BigDecimal.ZERO;
	
	
	
	// CONTABILIA-285 INIZIO
	private long variazioniInNeutreBozzaNum0 = 0L;
	private long variazioniInNeutreDefinitivaNum0 = 0L;
	private long variazioniInNeutrePreDefinitivaNum0 = 0L;
	private long variazioniInNeutreGiuntaConsiglioNum0 = 0L;
	private long variazioniInNeutreAnnullataNum0 = 0L;
	private long variazioniInNeutreBozzaNum1 = 0L;
	private long variazioniInNeutreDefinitivaNum1 = 0L;
	private long variazioniInNeutrePreDefinitivaNum1 = 0L;
	private long variazioniInNeutreGiuntaConsiglioNum1 = 0L;
	private long variazioniInNeutreAnnullataNum1 = 0L;
	private long variazioniInNeutreBozzaNum2 = 0L;
	private long variazioniInNeutreDefinitivaNum2 = 0L;
	private long variazioniInNeutrePreDefinitivaNum2 = 0L;
	private long variazioniInNeutreGiuntaConsiglioNum2 = 0L;
	private long variazioniInNeutreAnnullataNum2 = 0L;
	
	private BigDecimal variazioniInNeutreBozzaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInNeutreDefinitivaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInNeutrePreDefinitivaCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInNeutreGiuntaConsiglioCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioCassa2 = BigDecimal.ZERO;
	
	private BigDecimal variazioniInNeutreAnnullataCompetenza0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataResiduo0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataCassa0 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataCompetenza1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataResiduo1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataCassa1 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataCompetenza2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataResiduo2 = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataCassa2 = BigDecimal.ZERO;

	/* Capitolo equivalente */
	private long variazioniInNeutreBozzaEquivalenteNum = 0L;
	private long variazioniInNeutreDefinitivaEquivalenteNum = 0L;
	private long variazioniInNeutrePreDefinitivaEquivalenteNum = 0L;
	private long variazioniInNeutreGiuntaConsiglioEquivalenteNum = 0L;
	private long variazioniInNeutreAnnullataEquivalenteNum = 0L;
	
	private BigDecimal variazioniInNeutreBozzaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreBozzaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreDefinitivaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutrePreDefinitivaEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreGiuntaConsiglioEquivalenteCassa = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataEquivalenteCompetenza = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataEquivalenteResiduo = BigDecimal.ZERO;
	private BigDecimal variazioniInNeutreAnnullataEquivalenteCassa = BigDecimal.ZERO;
	// CONTABILIA-285 FINE
	
	/**
	 * @return the variazioniInNeutreBozzaNum0
	 */
	public long getVariazioniInNeutreBozzaNum0() {
		return variazioniInNeutreBozzaNum0;
	}

	/**
	 * @param variazioniInNeutreBozzaNum0 the variazioniInNeutreBozzaNum0 to set
	 */
	public void setVariazioniInNeutreBozzaNum0(long variazioniInNeutreBozzaNum0) {
		this.variazioniInNeutreBozzaNum0 = variazioniInNeutreBozzaNum0;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaNum0
	 */
	public long getVariazioniInNeutreDefinitivaNum0() {
		return variazioniInNeutreDefinitivaNum0;
	}

	/**
	 * @param variazioniInNeutreDefinitivaNum0 the variazioniInNeutreDefinitivaNum0 to set
	 */
	public void setVariazioniInNeutreDefinitivaNum0(long variazioniInNeutreDefinitivaNum0) {
		this.variazioniInNeutreDefinitivaNum0 = variazioniInNeutreDefinitivaNum0;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaNum0
	 */
	public long getVariazioniInNeutrePreDefinitivaNum0() {
		return variazioniInNeutrePreDefinitivaNum0;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaNum0 the variazioniInNeutrePreDefinitivaNum0 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaNum0(long variazioniInNeutrePreDefinitivaNum0) {
		this.variazioniInNeutrePreDefinitivaNum0 = variazioniInNeutrePreDefinitivaNum0;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioNum0
	 */
	public long getVariazioniInNeutreGiuntaConsiglioNum0() {
		return variazioniInNeutreGiuntaConsiglioNum0;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioNum0 the variazioniInNeutreGiuntaConsiglioNum0 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioNum0(long variazioniInNeutreGiuntaConsiglioNum0) {
		this.variazioniInNeutreGiuntaConsiglioNum0 = variazioniInNeutreGiuntaConsiglioNum0;
	}

	/**
	 * @return the variazioniInNeutreAnnullataNum0
	 */
	public long getVariazioniInNeutreAnnullataNum0() {
		return variazioniInNeutreAnnullataNum0;
	}

	/**
	 * @param variazioniInNeutreAnnullataNum0 the variazioniInNeutreAnnullataNum0 to set
	 */
	public void setVariazioniInNeutreAnnullataNum0(long variazioniInNeutreAnnullataNum0) {
		this.variazioniInNeutreAnnullataNum0 = variazioniInNeutreAnnullataNum0;
	}

	/**
	 * @return the variazioniInNeutreBozzaNum1
	 */
	public long getVariazioniInNeutreBozzaNum1() {
		return variazioniInNeutreBozzaNum1;
	}

	/**
	 * @param variazioniInNeutreBozzaNum1 the variazioniInNeutreBozzaNum1 to set
	 */
	public void setVariazioniInNeutreBozzaNum1(long variazioniInNeutreBozzaNum1) {
		this.variazioniInNeutreBozzaNum1 = variazioniInNeutreBozzaNum1;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaNum1
	 */
	public long getVariazioniInNeutreDefinitivaNum1() {
		return variazioniInNeutreDefinitivaNum1;
	}

	/**
	 * @param variazioniInNeutreDefinitivaNum1 the variazioniInNeutreDefinitivaNum1 to set
	 */
	public void setVariazioniInNeutreDefinitivaNum1(long variazioniInNeutreDefinitivaNum1) {
		this.variazioniInNeutreDefinitivaNum1 = variazioniInNeutreDefinitivaNum1;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaNum1
	 */
	public long getVariazioniInNeutrePreDefinitivaNum1() {
		return variazioniInNeutrePreDefinitivaNum1;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaNum1 the variazioniInNeutrePreDefinitivaNum1 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaNum1(long variazioniInNeutrePreDefinitivaNum1) {
		this.variazioniInNeutrePreDefinitivaNum1 = variazioniInNeutrePreDefinitivaNum1;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioNum1
	 */
	public long getVariazioniInNeutreGiuntaConsiglioNum1() {
		return variazioniInNeutreGiuntaConsiglioNum1;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioNum1 the variazioniInNeutreGiuntaConsiglioNum1 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioNum1(long variazioniInNeutreGiuntaConsiglioNum1) {
		this.variazioniInNeutreGiuntaConsiglioNum1 = variazioniInNeutreGiuntaConsiglioNum1;
	}

	/**
	 * @return the variazioniInNeutreAnnullataNum1
	 */
	public long getVariazioniInNeutreAnnullataNum1() {
		return variazioniInNeutreAnnullataNum1;
	}

	/**
	 * @param variazioniInNeutreAnnullataNum1 the variazioniInNeutreAnnullataNum1 to set
	 */
	public void setVariazioniInNeutreAnnullataNum1(long variazioniInNeutreAnnullataNum1) {
		this.variazioniInNeutreAnnullataNum1 = variazioniInNeutreAnnullataNum1;
	}

	/**
	 * @return the variazioniInNeutreBozzaNum2
	 */
	public long getVariazioniInNeutreBozzaNum2() {
		return variazioniInNeutreBozzaNum2;
	}

	/**
	 * @param variazioniInNeutreBozzaNum2 the variazioniInNeutreBozzaNum2 to set
	 */
	public void setVariazioniInNeutreBozzaNum2(long variazioniInNeutreBozzaNum2) {
		this.variazioniInNeutreBozzaNum2 = variazioniInNeutreBozzaNum2;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaNum2
	 */
	public long getVariazioniInNeutreDefinitivaNum2() {
		return variazioniInNeutreDefinitivaNum2;
	}

	/**
	 * @param variazioniInNeutreDefinitivaNum2 the variazioniInNeutreDefinitivaNum2 to set
	 */
	public void setVariazioniInNeutreDefinitivaNum2(long variazioniInNeutreDefinitivaNum2) {
		this.variazioniInNeutreDefinitivaNum2 = variazioniInNeutreDefinitivaNum2;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaNum2
	 */
	public long getVariazioniInNeutrePreDefinitivaNum2() {
		return variazioniInNeutrePreDefinitivaNum2;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaNum2 the variazioniInNeutrePreDefinitivaNum2 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaNum2(long variazioniInNeutrePreDefinitivaNum2) {
		this.variazioniInNeutrePreDefinitivaNum2 = variazioniInNeutrePreDefinitivaNum2;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioNum2
	 */
	public long getVariazioniInNeutreGiuntaConsiglioNum2() {
		return variazioniInNeutreGiuntaConsiglioNum2;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioNum2 the variazioniInNeutreGiuntaConsiglioNum2 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioNum2(long variazioniInNeutreGiuntaConsiglioNum2) {
		this.variazioniInNeutreGiuntaConsiglioNum2 = variazioniInNeutreGiuntaConsiglioNum2;
	}

	/**
	 * @return the variazioniInNeutreAnnullataNum2
	 */
	public long getVariazioniInNeutreAnnullataNum2() {
		return variazioniInNeutreAnnullataNum2;
	}

	/**
	 * @param variazioniInNeutreAnnullataNum2 the variazioniInNeutreAnnullataNum2 to set
	 */
	public void setVariazioniInNeutreAnnullataNum2(long variazioniInNeutreAnnullataNum2) {
		this.variazioniInNeutreAnnullataNum2 = variazioniInNeutreAnnullataNum2;
	}

	/**
	 * @return the variazioniInNeutreBozzaCompetenza0
	 */
	public BigDecimal getVariazioniInNeutreBozzaCompetenza0() {
		return variazioniInNeutreBozzaCompetenza0;
	}

	/**
	 * @param variazioniInNeutreBozzaCompetenza0 the variazioniInNeutreBozzaCompetenza0 to set
	 */
	public void setVariazioniInNeutreBozzaCompetenza0(BigDecimal variazioniInNeutreBozzaCompetenza0) {
		this.variazioniInNeutreBozzaCompetenza0 = variazioniInNeutreBozzaCompetenza0;
	}

	/**
	 * @return the variazioniInNeutreBozzaResiduo0
	 */
	public BigDecimal getVariazioniInNeutreBozzaResiduo0() {
		return variazioniInNeutreBozzaResiduo0;
	}

	/**
	 * @param variazioniInNeutreBozzaResiduo0 the variazioniInNeutreBozzaResiduo0 to set
	 */
	public void setVariazioniInNeutreBozzaResiduo0(BigDecimal variazioniInNeutreBozzaResiduo0) {
		this.variazioniInNeutreBozzaResiduo0 = variazioniInNeutreBozzaResiduo0;
	}

	/**
	 * @return the variazioniInNeutreBozzaCassa0
	 */
	public BigDecimal getVariazioniInNeutreBozzaCassa0() {
		return variazioniInNeutreBozzaCassa0;
	}

	/**
	 * @param variazioniInNeutreBozzaCassa0 the variazioniInNeutreBozzaCassa0 to set
	 */
	public void setVariazioniInNeutreBozzaCassa0(BigDecimal variazioniInNeutreBozzaCassa0) {
		this.variazioniInNeutreBozzaCassa0 = variazioniInNeutreBozzaCassa0;
	}

	/**
	 * @return the variazioniInNeutreBozzaCompetenza1
	 */
	public BigDecimal getVariazioniInNeutreBozzaCompetenza1() {
		return variazioniInNeutreBozzaCompetenza1;
	}

	/**
	 * @param variazioniInNeutreBozzaCompetenza1 the variazioniInNeutreBozzaCompetenza1 to set
	 */
	public void setVariazioniInNeutreBozzaCompetenza1(BigDecimal variazioniInNeutreBozzaCompetenza1) {
		this.variazioniInNeutreBozzaCompetenza1 = variazioniInNeutreBozzaCompetenza1;
	}

	/**
	 * @return the variazioniInNeutreBozzaResiduo1
	 */
	public BigDecimal getVariazioniInNeutreBozzaResiduo1() {
		return variazioniInNeutreBozzaResiduo1;
	}

	/**
	 * @param variazioniInNeutreBozzaResiduo1 the variazioniInNeutreBozzaResiduo1 to set
	 */
	public void setVariazioniInNeutreBozzaResiduo1(BigDecimal variazioniInNeutreBozzaResiduo1) {
		this.variazioniInNeutreBozzaResiduo1 = variazioniInNeutreBozzaResiduo1;
	}

	/**
	 * @return the variazioniInNeutreBozzaCassa1
	 */
	public BigDecimal getVariazioniInNeutreBozzaCassa1() {
		return variazioniInNeutreBozzaCassa1;
	}

	/**
	 * @param variazioniInNeutreBozzaCassa1 the variazioniInNeutreBozzaCassa1 to set
	 */
	public void setVariazioniInNeutreBozzaCassa1(BigDecimal variazioniInNeutreBozzaCassa1) {
		this.variazioniInNeutreBozzaCassa1 = variazioniInNeutreBozzaCassa1;
	}

	/**
	 * @return the variazioniInNeutreBozzaCompetenza2
	 */
	public BigDecimal getVariazioniInNeutreBozzaCompetenza2() {
		return variazioniInNeutreBozzaCompetenza2;
	}

	/**
	 * @param variazioniInNeutreBozzaCompetenza2 the variazioniInNeutreBozzaCompetenza2 to set
	 */
	public void setVariazioniInNeutreBozzaCompetenza2(BigDecimal variazioniInNeutreBozzaCompetenza2) {
		this.variazioniInNeutreBozzaCompetenza2 = variazioniInNeutreBozzaCompetenza2;
	}

	/**
	 * @return the variazioniInNeutreBozzaResiduo2
	 */
	public BigDecimal getVariazioniInNeutreBozzaResiduo2() {
		return variazioniInNeutreBozzaResiduo2;
	}

	/**
	 * @param variazioniInNeutreBozzaResiduo2 the variazioniInNeutreBozzaResiduo2 to set
	 */
	public void setVariazioniInNeutreBozzaResiduo2(BigDecimal variazioniInNeutreBozzaResiduo2) {
		this.variazioniInNeutreBozzaResiduo2 = variazioniInNeutreBozzaResiduo2;
	}

	/**
	 * @return the variazioniInNeutreBozzaCassa2
	 */
	public BigDecimal getVariazioniInNeutreBozzaCassa2() {
		return variazioniInNeutreBozzaCassa2;
	}

	/**
	 * @param variazioniInNeutreBozzaCassa2 the variazioniInNeutreBozzaCassa2 to set
	 */
	public void setVariazioniInNeutreBozzaCassa2(BigDecimal variazioniInNeutreBozzaCassa2) {
		this.variazioniInNeutreBozzaCassa2 = variazioniInNeutreBozzaCassa2;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaCompetenza0
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaCompetenza0() {
		return variazioniInNeutreDefinitivaCompetenza0;
	}

	/**
	 * @param variazioniInNeutreDefinitivaCompetenza0 the variazioniInNeutreDefinitivaCompetenza0 to set
	 */
	public void setVariazioniInNeutreDefinitivaCompetenza0(BigDecimal variazioniInNeutreDefinitivaCompetenza0) {
		this.variazioniInNeutreDefinitivaCompetenza0 = variazioniInNeutreDefinitivaCompetenza0;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaResiduo0
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaResiduo0() {
		return variazioniInNeutreDefinitivaResiduo0;
	}

	/**
	 * @param variazioniInNeutreDefinitivaResiduo0 the variazioniInNeutreDefinitivaResiduo0 to set
	 */
	public void setVariazioniInNeutreDefinitivaResiduo0(BigDecimal variazioniInNeutreDefinitivaResiduo0) {
		this.variazioniInNeutreDefinitivaResiduo0 = variazioniInNeutreDefinitivaResiduo0;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaCassa0
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaCassa0() {
		return variazioniInNeutreDefinitivaCassa0;
	}

	/**
	 * @param variazioniInNeutreDefinitivaCassa0 the variazioniInNeutreDefinitivaCassa0 to set
	 */
	public void setVariazioniInNeutreDefinitivaCassa0(BigDecimal variazioniInNeutreDefinitivaCassa0) {
		this.variazioniInNeutreDefinitivaCassa0 = variazioniInNeutreDefinitivaCassa0;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaCompetenza1
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaCompetenza1() {
		return variazioniInNeutreDefinitivaCompetenza1;
	}

	/**
	 * @param variazioniInNeutreDefinitivaCompetenza1 the variazioniInNeutreDefinitivaCompetenza1 to set
	 */
	public void setVariazioniInNeutreDefinitivaCompetenza1(BigDecimal variazioniInNeutreDefinitivaCompetenza1) {
		this.variazioniInNeutreDefinitivaCompetenza1 = variazioniInNeutreDefinitivaCompetenza1;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaResiduo1
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaResiduo1() {
		return variazioniInNeutreDefinitivaResiduo1;
	}

	/**
	 * @param variazioniInNeutreDefinitivaResiduo1 the variazioniInNeutreDefinitivaResiduo1 to set
	 */
	public void setVariazioniInNeutreDefinitivaResiduo1(BigDecimal variazioniInNeutreDefinitivaResiduo1) {
		this.variazioniInNeutreDefinitivaResiduo1 = variazioniInNeutreDefinitivaResiduo1;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaCassa1
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaCassa1() {
		return variazioniInNeutreDefinitivaCassa1;
	}

	/**
	 * @param variazioniInNeutreDefinitivaCassa1 the variazioniInNeutreDefinitivaCassa1 to set
	 */
	public void setVariazioniInNeutreDefinitivaCassa1(BigDecimal variazioniInNeutreDefinitivaCassa1) {
		this.variazioniInNeutreDefinitivaCassa1 = variazioniInNeutreDefinitivaCassa1;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaCompetenza2
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaCompetenza2() {
		return variazioniInNeutreDefinitivaCompetenza2;
	}

	/**
	 * @param variazioniInNeutreDefinitivaCompetenza2 the variazioniInNeutreDefinitivaCompetenza2 to set
	 */
	public void setVariazioniInNeutreDefinitivaCompetenza2(BigDecimal variazioniInNeutreDefinitivaCompetenza2) {
		this.variazioniInNeutreDefinitivaCompetenza2 = variazioniInNeutreDefinitivaCompetenza2;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaResiduo2
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaResiduo2() {
		return variazioniInNeutreDefinitivaResiduo2;
	}

	/**
	 * @param variazioniInNeutreDefinitivaResiduo2 the variazioniInNeutreDefinitivaResiduo2 to set
	 */
	public void setVariazioniInNeutreDefinitivaResiduo2(BigDecimal variazioniInNeutreDefinitivaResiduo2) {
		this.variazioniInNeutreDefinitivaResiduo2 = variazioniInNeutreDefinitivaResiduo2;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaCassa2
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaCassa2() {
		return variazioniInNeutreDefinitivaCassa2;
	}

	/**
	 * @param variazioniInNeutreDefinitivaCassa2 the variazioniInNeutreDefinitivaCassa2 to set
	 */
	public void setVariazioniInNeutreDefinitivaCassa2(BigDecimal variazioniInNeutreDefinitivaCassa2) {
		this.variazioniInNeutreDefinitivaCassa2 = variazioniInNeutreDefinitivaCassa2;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaCompetenza0
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaCompetenza0() {
		return variazioniInNeutrePreDefinitivaCompetenza0;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaCompetenza0 the variazioniInNeutrePreDefinitivaCompetenza0 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaCompetenza0(BigDecimal variazioniInNeutrePreDefinitivaCompetenza0) {
		this.variazioniInNeutrePreDefinitivaCompetenza0 = variazioniInNeutrePreDefinitivaCompetenza0;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaResiduo0
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaResiduo0() {
		return variazioniInNeutrePreDefinitivaResiduo0;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaResiduo0 the variazioniInNeutrePreDefinitivaResiduo0 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaResiduo0(BigDecimal variazioniInNeutrePreDefinitivaResiduo0) {
		this.variazioniInNeutrePreDefinitivaResiduo0 = variazioniInNeutrePreDefinitivaResiduo0;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaCassa0
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaCassa0() {
		return variazioniInNeutrePreDefinitivaCassa0;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaCassa0 the variazioniInNeutrePreDefinitivaCassa0 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaCassa0(BigDecimal variazioniInNeutrePreDefinitivaCassa0) {
		this.variazioniInNeutrePreDefinitivaCassa0 = variazioniInNeutrePreDefinitivaCassa0;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaCompetenza1
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaCompetenza1() {
		return variazioniInNeutrePreDefinitivaCompetenza1;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaCompetenza1 the variazioniInNeutrePreDefinitivaCompetenza1 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaCompetenza1(BigDecimal variazioniInNeutrePreDefinitivaCompetenza1) {
		this.variazioniInNeutrePreDefinitivaCompetenza1 = variazioniInNeutrePreDefinitivaCompetenza1;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaResiduo1
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaResiduo1() {
		return variazioniInNeutrePreDefinitivaResiduo1;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaResiduo1 the variazioniInNeutrePreDefinitivaResiduo1 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaResiduo1(BigDecimal variazioniInNeutrePreDefinitivaResiduo1) {
		this.variazioniInNeutrePreDefinitivaResiduo1 = variazioniInNeutrePreDefinitivaResiduo1;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaCassa1
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaCassa1() {
		return variazioniInNeutrePreDefinitivaCassa1;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaCassa1 the variazioniInNeutrePreDefinitivaCassa1 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaCassa1(BigDecimal variazioniInNeutrePreDefinitivaCassa1) {
		this.variazioniInNeutrePreDefinitivaCassa1 = variazioniInNeutrePreDefinitivaCassa1;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaCompetenza2
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaCompetenza2() {
		return variazioniInNeutrePreDefinitivaCompetenza2;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaCompetenza2 the variazioniInNeutrePreDefinitivaCompetenza2 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaCompetenza2(BigDecimal variazioniInNeutrePreDefinitivaCompetenza2) {
		this.variazioniInNeutrePreDefinitivaCompetenza2 = variazioniInNeutrePreDefinitivaCompetenza2;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaResiduo2
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaResiduo2() {
		return variazioniInNeutrePreDefinitivaResiduo2;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaResiduo2 the variazioniInNeutrePreDefinitivaResiduo2 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaResiduo2(BigDecimal variazioniInNeutrePreDefinitivaResiduo2) {
		this.variazioniInNeutrePreDefinitivaResiduo2 = variazioniInNeutrePreDefinitivaResiduo2;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaCassa2
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaCassa2() {
		return variazioniInNeutrePreDefinitivaCassa2;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaCassa2 the variazioniInNeutrePreDefinitivaCassa2 to set
	 */
	public void setVariazioniInNeutrePreDefinitivaCassa2(BigDecimal variazioniInNeutrePreDefinitivaCassa2) {
		this.variazioniInNeutrePreDefinitivaCassa2 = variazioniInNeutrePreDefinitivaCassa2;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioCompetenza0
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioCompetenza0() {
		return variazioniInNeutreGiuntaConsiglioCompetenza0;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioCompetenza0 the variazioniInNeutreGiuntaConsiglioCompetenza0 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioCompetenza0(BigDecimal variazioniInNeutreGiuntaConsiglioCompetenza0) {
		this.variazioniInNeutreGiuntaConsiglioCompetenza0 = variazioniInNeutreGiuntaConsiglioCompetenza0;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioResiduo0
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioResiduo0() {
		return variazioniInNeutreGiuntaConsiglioResiduo0;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioResiduo0 the variazioniInNeutreGiuntaConsiglioResiduo0 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioResiduo0(BigDecimal variazioniInNeutreGiuntaConsiglioResiduo0) {
		this.variazioniInNeutreGiuntaConsiglioResiduo0 = variazioniInNeutreGiuntaConsiglioResiduo0;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioCassa0
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioCassa0() {
		return variazioniInNeutreGiuntaConsiglioCassa0;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioCassa0 the variazioniInNeutreGiuntaConsiglioCassa0 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioCassa0(BigDecimal variazioniInNeutreGiuntaConsiglioCassa0) {
		this.variazioniInNeutreGiuntaConsiglioCassa0 = variazioniInNeutreGiuntaConsiglioCassa0;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioCompetenza1
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioCompetenza1() {
		return variazioniInNeutreGiuntaConsiglioCompetenza1;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioCompetenza1 the variazioniInNeutreGiuntaConsiglioCompetenza1 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioCompetenza1(BigDecimal variazioniInNeutreGiuntaConsiglioCompetenza1) {
		this.variazioniInNeutreGiuntaConsiglioCompetenza1 = variazioniInNeutreGiuntaConsiglioCompetenza1;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioResiduo1
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioResiduo1() {
		return variazioniInNeutreGiuntaConsiglioResiduo1;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioResiduo1 the variazioniInNeutreGiuntaConsiglioResiduo1 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioResiduo1(BigDecimal variazioniInNeutreGiuntaConsiglioResiduo1) {
		this.variazioniInNeutreGiuntaConsiglioResiduo1 = variazioniInNeutreGiuntaConsiglioResiduo1;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioCassa1
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioCassa1() {
		return variazioniInNeutreGiuntaConsiglioCassa1;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioCassa1 the variazioniInNeutreGiuntaConsiglioCassa1 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioCassa1(BigDecimal variazioniInNeutreGiuntaConsiglioCassa1) {
		this.variazioniInNeutreGiuntaConsiglioCassa1 = variazioniInNeutreGiuntaConsiglioCassa1;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioCompetenza2
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioCompetenza2() {
		return variazioniInNeutreGiuntaConsiglioCompetenza2;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioCompetenza2 the variazioniInNeutreGiuntaConsiglioCompetenza2 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioCompetenza2(BigDecimal variazioniInNeutreGiuntaConsiglioCompetenza2) {
		this.variazioniInNeutreGiuntaConsiglioCompetenza2 = variazioniInNeutreGiuntaConsiglioCompetenza2;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioResiduo2
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioResiduo2() {
		return variazioniInNeutreGiuntaConsiglioResiduo2;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioResiduo2 the variazioniInNeutreGiuntaConsiglioResiduo2 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioResiduo2(BigDecimal variazioniInNeutreGiuntaConsiglioResiduo2) {
		this.variazioniInNeutreGiuntaConsiglioResiduo2 = variazioniInNeutreGiuntaConsiglioResiduo2;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioCassa2
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioCassa2() {
		return variazioniInNeutreGiuntaConsiglioCassa2;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioCassa2 the variazioniInNeutreGiuntaConsiglioCassa2 to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioCassa2(BigDecimal variazioniInNeutreGiuntaConsiglioCassa2) {
		this.variazioniInNeutreGiuntaConsiglioCassa2 = variazioniInNeutreGiuntaConsiglioCassa2;
	}

	/**
	 * @return the variazioniInNeutreAnnullataCompetenza0
	 */
	public BigDecimal getVariazioniInNeutreAnnullataCompetenza0() {
		return variazioniInNeutreAnnullataCompetenza0;
	}

	/**
	 * @param variazioniInNeutreAnnullataCompetenza0 the variazioniInNeutreAnnullataCompetenza0 to set
	 */
	public void setVariazioniInNeutreAnnullataCompetenza0(BigDecimal variazioniInNeutreAnnullataCompetenza0) {
		this.variazioniInNeutreAnnullataCompetenza0 = variazioniInNeutreAnnullataCompetenza0;
	}

	/**
	 * @return the variazioniInNeutreAnnullataResiduo0
	 */
	public BigDecimal getVariazioniInNeutreAnnullataResiduo0() {
		return variazioniInNeutreAnnullataResiduo0;
	}

	/**
	 * @param variazioniInNeutreAnnullataResiduo0 the variazioniInNeutreAnnullataResiduo0 to set
	 */
	public void setVariazioniInNeutreAnnullataResiduo0(BigDecimal variazioniInNeutreAnnullataResiduo0) {
		this.variazioniInNeutreAnnullataResiduo0 = variazioniInNeutreAnnullataResiduo0;
	}

	/**
	 * @return the variazioniInNeutreAnnullataCassa0
	 */
	public BigDecimal getVariazioniInNeutreAnnullataCassa0() {
		return variazioniInNeutreAnnullataCassa0;
	}

	/**
	 * @param variazioniInNeutreAnnullataCassa0 the variazioniInNeutreAnnullataCassa0 to set
	 */
	public void setVariazioniInNeutreAnnullataCassa0(BigDecimal variazioniInNeutreAnnullataCassa0) {
		this.variazioniInNeutreAnnullataCassa0 = variazioniInNeutreAnnullataCassa0;
	}

	/**
	 * @return the variazioniInNeutreAnnullataCompetenza1
	 */
	public BigDecimal getVariazioniInNeutreAnnullataCompetenza1() {
		return variazioniInNeutreAnnullataCompetenza1;
	}

	/**
	 * @param variazioniInNeutreAnnullataCompetenza1 the variazioniInNeutreAnnullataCompetenza1 to set
	 */
	public void setVariazioniInNeutreAnnullataCompetenza1(BigDecimal variazioniInNeutreAnnullataCompetenza1) {
		this.variazioniInNeutreAnnullataCompetenza1 = variazioniInNeutreAnnullataCompetenza1;
	}

	/**
	 * @return the variazioniInNeutreAnnullataResiduo1
	 */
	public BigDecimal getVariazioniInNeutreAnnullataResiduo1() {
		return variazioniInNeutreAnnullataResiduo1;
	}

	/**
	 * @param variazioniInNeutreAnnullataResiduo1 the variazioniInNeutreAnnullataResiduo1 to set
	 */
	public void setVariazioniInNeutreAnnullataResiduo1(BigDecimal variazioniInNeutreAnnullataResiduo1) {
		this.variazioniInNeutreAnnullataResiduo1 = variazioniInNeutreAnnullataResiduo1;
	}

	/**
	 * @return the variazioniInNeutreAnnullataCassa1
	 */
	public BigDecimal getVariazioniInNeutreAnnullataCassa1() {
		return variazioniInNeutreAnnullataCassa1;
	}

	/**
	 * @param variazioniInNeutreAnnullataCassa1 the variazioniInNeutreAnnullataCassa1 to set
	 */
	public void setVariazioniInNeutreAnnullataCassa1(BigDecimal variazioniInNeutreAnnullataCassa1) {
		this.variazioniInNeutreAnnullataCassa1 = variazioniInNeutreAnnullataCassa1;
	}

	/**
	 * @return the variazioniInNeutreAnnullataCompetenza2
	 */
	public BigDecimal getVariazioniInNeutreAnnullataCompetenza2() {
		return variazioniInNeutreAnnullataCompetenza2;
	}

	/**
	 * @param variazioniInNeutreAnnullataCompetenza2 the variazioniInNeutreAnnullataCompetenza2 to set
	 */
	public void setVariazioniInNeutreAnnullataCompetenza2(BigDecimal variazioniInNeutreAnnullataCompetenza2) {
		this.variazioniInNeutreAnnullataCompetenza2 = variazioniInNeutreAnnullataCompetenza2;
	}

	/**
	 * @return the variazioniInNeutreAnnullataResiduo2
	 */
	public BigDecimal getVariazioniInNeutreAnnullataResiduo2() {
		return variazioniInNeutreAnnullataResiduo2;
	}

	/**
	 * @param variazioniInNeutreAnnullataResiduo2 the variazioniInNeutreAnnullataResiduo2 to set
	 */
	public void setVariazioniInNeutreAnnullataResiduo2(BigDecimal variazioniInNeutreAnnullataResiduo2) {
		this.variazioniInNeutreAnnullataResiduo2 = variazioniInNeutreAnnullataResiduo2;
	}

	/**
	 * @return the variazioniInNeutreAnnullataCassa2
	 */
	public BigDecimal getVariazioniInNeutreAnnullataCassa2() {
		return variazioniInNeutreAnnullataCassa2;
	}

	/**
	 * @param variazioniInNeutreAnnullataCassa2 the variazioniInNeutreAnnullataCassa2 to set
	 */
	public void setVariazioniInNeutreAnnullataCassa2(BigDecimal variazioniInNeutreAnnullataCassa2) {
		this.variazioniInNeutreAnnullataCassa2 = variazioniInNeutreAnnullataCassa2;
	}

	/**
	 * @return the variazioniInNeutreBozzaEquivalenteNum
	 */
	public long getVariazioniInNeutreBozzaEquivalenteNum() {
		return variazioniInNeutreBozzaEquivalenteNum;
	}

	/**
	 * @param variazioniInNeutreBozzaEquivalenteNum the variazioniInNeutreBozzaEquivalenteNum to set
	 */
	public void setVariazioniInNeutreBozzaEquivalenteNum(long variazioniInNeutreBozzaEquivalenteNum) {
		this.variazioniInNeutreBozzaEquivalenteNum = variazioniInNeutreBozzaEquivalenteNum;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaEquivalenteNum
	 */
	public long getVariazioniInNeutreDefinitivaEquivalenteNum() {
		return variazioniInNeutreDefinitivaEquivalenteNum;
	}

	/**
	 * @param variazioniInNeutreDefinitivaEquivalenteNum the variazioniInNeutreDefinitivaEquivalenteNum to set
	 */
	public void setVariazioniInNeutreDefinitivaEquivalenteNum(long variazioniInNeutreDefinitivaEquivalenteNum) {
		this.variazioniInNeutreDefinitivaEquivalenteNum = variazioniInNeutreDefinitivaEquivalenteNum;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaEquivalenteNum
	 */
	public long getVariazioniInNeutrePreDefinitivaEquivalenteNum() {
		return variazioniInNeutrePreDefinitivaEquivalenteNum;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaEquivalenteNum the variazioniInNeutrePreDefinitivaEquivalenteNum to set
	 */
	public void setVariazioniInNeutrePreDefinitivaEquivalenteNum(long variazioniInNeutrePreDefinitivaEquivalenteNum) {
		this.variazioniInNeutrePreDefinitivaEquivalenteNum = variazioniInNeutrePreDefinitivaEquivalenteNum;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioEquivalenteNum
	 */
	public long getVariazioniInNeutreGiuntaConsiglioEquivalenteNum() {
		return variazioniInNeutreGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioEquivalenteNum the variazioniInNeutreGiuntaConsiglioEquivalenteNum to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioEquivalenteNum(long variazioniInNeutreGiuntaConsiglioEquivalenteNum) {
		this.variazioniInNeutreGiuntaConsiglioEquivalenteNum = variazioniInNeutreGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @return the variazioniInNeutreAnnullataEquivalenteNum
	 */
	public long getVariazioniInNeutreAnnullataEquivalenteNum() {
		return variazioniInNeutreAnnullataEquivalenteNum;
	}

	/**
	 * @param variazioniInNeutreAnnullataEquivalenteNum the variazioniInNeutreAnnullataEquivalenteNum to set
	 */
	public void setVariazioniInNeutreAnnullataEquivalenteNum(long variazioniInNeutreAnnullataEquivalenteNum) {
		this.variazioniInNeutreAnnullataEquivalenteNum = variazioniInNeutreAnnullataEquivalenteNum;
	}

	/**
	 * @return the variazioniInNeutreBozzaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInNeutreBozzaEquivalenteCompetenza() {
		return variazioniInNeutreBozzaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInNeutreBozzaEquivalenteCompetenza the variazioniInNeutreBozzaEquivalenteCompetenza to set
	 */
	public void setVariazioniInNeutreBozzaEquivalenteCompetenza(BigDecimal variazioniInNeutreBozzaEquivalenteCompetenza) {
		this.variazioniInNeutreBozzaEquivalenteCompetenza = variazioniInNeutreBozzaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInNeutreBozzaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInNeutreBozzaEquivalenteResiduo() {
		return variazioniInNeutreBozzaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInNeutreBozzaEquivalenteResiduo the variazioniInNeutreBozzaEquivalenteResiduo to set
	 */
	public void setVariazioniInNeutreBozzaEquivalenteResiduo(BigDecimal variazioniInNeutreBozzaEquivalenteResiduo) {
		this.variazioniInNeutreBozzaEquivalenteResiduo = variazioniInNeutreBozzaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInNeutreBozzaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInNeutreBozzaEquivalenteCassa() {
		return variazioniInNeutreBozzaEquivalenteCassa;
	}

	/**
	 * @param variazioniInNeutreBozzaEquivalenteCassa the variazioniInNeutreBozzaEquivalenteCassa to set
	 */
	public void setVariazioniInNeutreBozzaEquivalenteCassa(BigDecimal variazioniInNeutreBozzaEquivalenteCassa) {
		this.variazioniInNeutreBozzaEquivalenteCassa = variazioniInNeutreBozzaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaEquivalenteCompetenza() {
		return variazioniInNeutreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInNeutreDefinitivaEquivalenteCompetenza the variazioniInNeutreDefinitivaEquivalenteCompetenza to set
	 */
	public void setVariazioniInNeutreDefinitivaEquivalenteCompetenza(
			BigDecimal variazioniInNeutreDefinitivaEquivalenteCompetenza) {
		this.variazioniInNeutreDefinitivaEquivalenteCompetenza = variazioniInNeutreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaEquivalenteResiduo() {
		return variazioniInNeutreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInNeutreDefinitivaEquivalenteResiduo the variazioniInNeutreDefinitivaEquivalenteResiduo to set
	 */
	public void setVariazioniInNeutreDefinitivaEquivalenteResiduo(
			BigDecimal variazioniInNeutreDefinitivaEquivalenteResiduo) {
		this.variazioniInNeutreDefinitivaEquivalenteResiduo = variazioniInNeutreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInNeutreDefinitivaEquivalenteCassa() {
		return variazioniInNeutreDefinitivaEquivalenteCassa;
	}

	/**
	 * @param variazioniInNeutreDefinitivaEquivalenteCassa the variazioniInNeutreDefinitivaEquivalenteCassa to set
	 */
	public void setVariazioniInNeutreDefinitivaEquivalenteCassa(BigDecimal variazioniInNeutreDefinitivaEquivalenteCassa) {
		this.variazioniInNeutreDefinitivaEquivalenteCassa = variazioniInNeutreDefinitivaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaEquivalenteCompetenza() {
		return variazioniInNeutrePreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaEquivalenteCompetenza the variazioniInNeutrePreDefinitivaEquivalenteCompetenza to set
	 */
	public void setVariazioniInNeutrePreDefinitivaEquivalenteCompetenza(
			BigDecimal variazioniInNeutrePreDefinitivaEquivalenteCompetenza) {
		this.variazioniInNeutrePreDefinitivaEquivalenteCompetenza = variazioniInNeutrePreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaEquivalenteResiduo() {
		return variazioniInNeutrePreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaEquivalenteResiduo the variazioniInNeutrePreDefinitivaEquivalenteResiduo to set
	 */
	public void setVariazioniInNeutrePreDefinitivaEquivalenteResiduo(
			BigDecimal variazioniInNeutrePreDefinitivaEquivalenteResiduo) {
		this.variazioniInNeutrePreDefinitivaEquivalenteResiduo = variazioniInNeutrePreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInNeutrePreDefinitivaEquivalenteCassa() {
		return variazioniInNeutrePreDefinitivaEquivalenteCassa;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaEquivalenteCassa the variazioniInNeutrePreDefinitivaEquivalenteCassa to set
	 */
	public void setVariazioniInNeutrePreDefinitivaEquivalenteCassa(
			BigDecimal variazioniInNeutrePreDefinitivaEquivalenteCassa) {
		this.variazioniInNeutrePreDefinitivaEquivalenteCassa = variazioniInNeutrePreDefinitivaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioEquivalenteCompetenza() {
		return variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza the variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioEquivalenteCompetenza(
			BigDecimal variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza) {
		this.variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza = variazioniInNeutreGiuntaConsiglioEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioEquivalenteResiduo() {
		return variazioniInNeutreGiuntaConsiglioEquivalenteResiduo;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioEquivalenteResiduo the variazioniInNeutreGiuntaConsiglioEquivalenteResiduo to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioEquivalenteResiduo(
			BigDecimal variazioniInNeutreGiuntaConsiglioEquivalenteResiduo) {
		this.variazioniInNeutreGiuntaConsiglioEquivalenteResiduo = variazioniInNeutreGiuntaConsiglioEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInNeutreGiuntaConsiglioEquivalenteCassa
	 */
	public BigDecimal getVariazioniInNeutreGiuntaConsiglioEquivalenteCassa() {
		return variazioniInNeutreGiuntaConsiglioEquivalenteCassa;
	}

	/**
	 * @param variazioniInNeutreGiuntaConsiglioEquivalenteCassa the variazioniInNeutreGiuntaConsiglioEquivalenteCassa to set
	 */
	public void setVariazioniInNeutreGiuntaConsiglioEquivalenteCassa(
			BigDecimal variazioniInNeutreGiuntaConsiglioEquivalenteCassa) {
		this.variazioniInNeutreGiuntaConsiglioEquivalenteCassa = variazioniInNeutreGiuntaConsiglioEquivalenteCassa;
	}

	/**
	 * @return the variazioniInNeutreAnnullataEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInNeutreAnnullataEquivalenteCompetenza() {
		return variazioniInNeutreAnnullataEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInNeutreAnnullataEquivalenteCompetenza the variazioniInNeutreAnnullataEquivalenteCompetenza to set
	 */
	public void setVariazioniInNeutreAnnullataEquivalenteCompetenza(
			BigDecimal variazioniInNeutreAnnullataEquivalenteCompetenza) {
		this.variazioniInNeutreAnnullataEquivalenteCompetenza = variazioniInNeutreAnnullataEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInNeutreAnnullataEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInNeutreAnnullataEquivalenteResiduo() {
		return variazioniInNeutreAnnullataEquivalenteResiduo;
	}

	/**
	 * @param variazioniInNeutreAnnullataEquivalenteResiduo the variazioniInNeutreAnnullataEquivalenteResiduo to set
	 */
	public void setVariazioniInNeutreAnnullataEquivalenteResiduo(BigDecimal variazioniInNeutreAnnullataEquivalenteResiduo) {
		this.variazioniInNeutreAnnullataEquivalenteResiduo = variazioniInNeutreAnnullataEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInNeutreAnnullataEquivalenteCassa
	 */
	public BigDecimal getVariazioniInNeutreAnnullataEquivalenteCassa() {
		return variazioniInNeutreAnnullataEquivalenteCassa;
	}

	/**
	 * @param variazioniInNeutreAnnullataEquivalenteCassa the variazioniInNeutreAnnullataEquivalenteCassa to set
	 */
	public void setVariazioniInNeutreAnnullataEquivalenteCassa(BigDecimal variazioniInNeutreAnnullataEquivalenteCassa) {
		this.variazioniInNeutreAnnullataEquivalenteCassa = variazioniInNeutreAnnullataEquivalenteCassa;
	}

	/* Liste */
	private List<VariazioneImportoCapitolo> listaVariazioneInAumento = new ArrayList<VariazioneImportoCapitolo>();
	private List<VariazioneImportoCapitolo> listaVariazioneInDiminuzione = new ArrayList<VariazioneImportoCapitolo>();
	// CONTABILIA-285
	private List<VariazioneImportoCapitolo> listaVariazioneInNeutre = new ArrayList<VariazioneImportoCapitolo>();

	/** Costruttore vuoto di default */
	public ElementoVariazioneConsultazione() {
		super();
	}

	/**
	 * @return the variazioniInAumentoBozzaNum0
	 */
	public long getVariazioniInAumentoBozzaNum0() {
		return this.variazioniInAumentoBozzaNum0;
	}

	/**
	 * @param variazioniInAumentoBozzaNum0 the variazioniInAumentoBozzaNum0 to set
	 */
	public void setVariazioniInAumentoBozzaNum0(long variazioniInAumentoBozzaNum0) {
		this.variazioniInAumentoBozzaNum0 = variazioniInAumentoBozzaNum0;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaNum0
	 */
	public long getVariazioniInAumentoDefinitivaNum0() {
		return this.variazioniInAumentoDefinitivaNum0;
	}

	/**
	 * @param variazioniInAumentoDefinitivaNum0 the variazioniInAumentoDefinitivaNum0 to set
	 */
	public void setVariazioniInAumentoDefinitivaNum0(long variazioniInAumentoDefinitivaNum0) {
		this.variazioniInAumentoDefinitivaNum0 = variazioniInAumentoDefinitivaNum0;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaNum0
	 */
	public long getVariazioniInAumentoPreDefinitivaNum0() {
		return this.variazioniInAumentoPreDefinitivaNum0;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaNum0 the variazioniInAumentoPreDefinitivaNum0 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaNum0(long variazioniInAumentoPreDefinitivaNum0) {
		this.variazioniInAumentoPreDefinitivaNum0 = variazioniInAumentoPreDefinitivaNum0;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioNum0
	 */
	public long getVariazioniInAumentoGiuntaConsiglioNum0() {
		return this.variazioniInAumentoGiuntaConsiglioNum0;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioNum0 the variazioniInAumentoGiuntaConsiglioNum0 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioNum0(long variazioniInAumentoGiuntaConsiglioNum0) {
		this.variazioniInAumentoGiuntaConsiglioNum0 = variazioniInAumentoGiuntaConsiglioNum0;
	}

	/**
	 * @return the variazioniInAumentoAnnullataNum0
	 */
	public long getVariazioniInAumentoAnnullataNum0() {
		return this.variazioniInAumentoAnnullataNum0;
	}

	/**
	 * @param variazioniInAumentoAnnullataNum0 the variazioniInAumentoAnnullataNum0 to set
	 */
	public void setVariazioniInAumentoAnnullataNum0(long variazioniInAumentoAnnullataNum0) {
		this.variazioniInAumentoAnnullataNum0 = variazioniInAumentoAnnullataNum0;
	}

	/**
	 * @return the variazioniInAumentoBozzaNum1
	 */
	public long getVariazioniInAumentoBozzaNum1() {
		return this.variazioniInAumentoBozzaNum1;
	}

	/**
	 * @param variazioniInAumentoBozzaNum1 the variazioniInAumentoBozzaNum1 to set
	 */
	public void setVariazioniInAumentoBozzaNum1(long variazioniInAumentoBozzaNum1) {
		this.variazioniInAumentoBozzaNum1 = variazioniInAumentoBozzaNum1;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaNum1
	 */
	public long getVariazioniInAumentoDefinitivaNum1() {
		return this.variazioniInAumentoDefinitivaNum1;
	}

	/**
	 * @param variazioniInAumentoDefinitivaNum1 the variazioniInAumentoDefinitivaNum1 to set
	 */
	public void setVariazioniInAumentoDefinitivaNum1(long variazioniInAumentoDefinitivaNum1) {
		this.variazioniInAumentoDefinitivaNum1 = variazioniInAumentoDefinitivaNum1;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaNum1
	 */
	public long getVariazioniInAumentoPreDefinitivaNum1() {
		return this.variazioniInAumentoPreDefinitivaNum1;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaNum1 the variazioniInAumentoPreDefinitivaNum1 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaNum1(long variazioniInAumentoPreDefinitivaNum1) {
		this.variazioniInAumentoPreDefinitivaNum1 = variazioniInAumentoPreDefinitivaNum1;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioNum1
	 */
	public long getVariazioniInAumentoGiuntaConsiglioNum1() {
		return this.variazioniInAumentoGiuntaConsiglioNum1;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioNum1 the variazioniInAumentoGiuntaConsiglioNum1 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioNum1(long variazioniInAumentoGiuntaConsiglioNum1) {
		this.variazioniInAumentoGiuntaConsiglioNum1 = variazioniInAumentoGiuntaConsiglioNum1;
	}

	/**
	 * @return the variazioniInAumentoAnnullataNum1
	 */
	public long getVariazioniInAumentoAnnullataNum1() {
		return this.variazioniInAumentoAnnullataNum1;
	}

	/**
	 * @param variazioniInAumentoAnnullataNum1 the variazioniInAumentoAnnullataNum1 to set
	 */
	public void setVariazioniInAumentoAnnullataNum1(long variazioniInAumentoAnnullataNum1) {
		this.variazioniInAumentoAnnullataNum1 = variazioniInAumentoAnnullataNum1;
	}

	/**
	 * @return the variazioniInAumentoBozzaNum2
	 */
	public long getVariazioniInAumentoBozzaNum2() {
		return this.variazioniInAumentoBozzaNum2;
	}

	/**
	 * @param variazioniInAumentoBozzaNum2 the variazioniInAumentoBozzaNum2 to set
	 */
	public void setVariazioniInAumentoBozzaNum2(long variazioniInAumentoBozzaNum2) {
		this.variazioniInAumentoBozzaNum2 = variazioniInAumentoBozzaNum2;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaNum2
	 */
	public long getVariazioniInAumentoDefinitivaNum2() {
		return this.variazioniInAumentoDefinitivaNum2;
	}

	/**
	 * @param variazioniInAumentoDefinitivaNum2 the variazioniInAumentoDefinitivaNum2 to set
	 */
	public void setVariazioniInAumentoDefinitivaNum2(long variazioniInAumentoDefinitivaNum2) {
		this.variazioniInAumentoDefinitivaNum2 = variazioniInAumentoDefinitivaNum2;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaNum2
	 */
	public long getVariazioniInAumentoPreDefinitivaNum2() {
		return this.variazioniInAumentoPreDefinitivaNum2;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaNum2 the variazioniInAumentoPreDefinitivaNum2 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaNum2(long variazioniInAumentoPreDefinitivaNum2) {
		this.variazioniInAumentoPreDefinitivaNum2 = variazioniInAumentoPreDefinitivaNum2;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioNum2
	 */
	public long getVariazioniInAumentoGiuntaConsiglioNum2() {
		return this.variazioniInAumentoGiuntaConsiglioNum2;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioNum2 the variazioniInAumentoGiuntaConsiglioNum2 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioNum2(long variazioniInAumentoGiuntaConsiglioNum2) {
		this.variazioniInAumentoGiuntaConsiglioNum2 = variazioniInAumentoGiuntaConsiglioNum2;
	}

	/**
	 * @return the variazioniInAumentoAnnullataNum2
	 */
	public long getVariazioniInAumentoAnnullataNum2() {
		return this.variazioniInAumentoAnnullataNum2;
	}

	/**
	 * @param variazioniInAumentoAnnullataNum2 the variazioniInAumentoAnnullataNum2 to set
	 */
	public void setVariazioniInAumentoAnnullataNum2(long variazioniInAumentoAnnullataNum2) {
		this.variazioniInAumentoAnnullataNum2 = variazioniInAumentoAnnullataNum2;
	}

	/**
	 * @return the variazioniInAumentoBozzaCompetenza0
	 */
	public BigDecimal getVariazioniInAumentoBozzaCompetenza0() {
		return this.variazioniInAumentoBozzaCompetenza0;
	}

	/**
	 * @param variazioniInAumentoBozzaCompetenza0 the variazioniInAumentoBozzaCompetenza0 to set
	 */
	public void setVariazioniInAumentoBozzaCompetenza0(BigDecimal variazioniInAumentoBozzaCompetenza0) {
		this.variazioniInAumentoBozzaCompetenza0 = variazioniInAumentoBozzaCompetenza0;
	}

	/**
	 * @return the variazioniInAumentoBozzaResiduo0
	 */
	public BigDecimal getVariazioniInAumentoBozzaResiduo0() {
		return this.variazioniInAumentoBozzaResiduo0;
	}

	/**
	 * @param variazioniInAumentoBozzaResiduo0 the variazioniInAumentoBozzaResiduo0 to set
	 */
	public void setVariazioniInAumentoBozzaResiduo0(BigDecimal variazioniInAumentoBozzaResiduo0) {
		this.variazioniInAumentoBozzaResiduo0 = variazioniInAumentoBozzaResiduo0;
	}

	/**
	 * @return the variazioniInAumentoBozzaCassa0
	 */
	public BigDecimal getVariazioniInAumentoBozzaCassa0() {
		return this.variazioniInAumentoBozzaCassa0;
	}

	/**
	 * @param variazioniInAumentoBozzaCassa0 the variazioniInAumentoBozzaCassa0 to set
	 */
	public void setVariazioniInAumentoBozzaCassa0(BigDecimal variazioniInAumentoBozzaCassa0) {
		this.variazioniInAumentoBozzaCassa0 = variazioniInAumentoBozzaCassa0;
	}

	/**
	 * @return the variazioniInAumentoBozzaCompetenza1
	 */
	public BigDecimal getVariazioniInAumentoBozzaCompetenza1() {
		return this.variazioniInAumentoBozzaCompetenza1;
	}

	/**
	 * @param variazioniInAumentoBozzaCompetenza1 the variazioniInAumentoBozzaCompetenza1 to set
	 */
	public void setVariazioniInAumentoBozzaCompetenza1(BigDecimal variazioniInAumentoBozzaCompetenza1) {
		this.variazioniInAumentoBozzaCompetenza1 = variazioniInAumentoBozzaCompetenza1;
	}

	/**
	 * @return the variazioniInAumentoBozzaResiduo1
	 */
	public BigDecimal getVariazioniInAumentoBozzaResiduo1() {
		return this.variazioniInAumentoBozzaResiduo1;
	}

	/**
	 * @param variazioniInAumentoBozzaResiduo1 the variazioniInAumentoBozzaResiduo1 to set
	 */
	public void setVariazioniInAumentoBozzaResiduo1(BigDecimal variazioniInAumentoBozzaResiduo1) {
		this.variazioniInAumentoBozzaResiduo1 = variazioniInAumentoBozzaResiduo1;
	}

	/**
	 * @return the variazioniInAumentoBozzaCassa1
	 */
	public BigDecimal getVariazioniInAumentoBozzaCassa1() {
		return this.variazioniInAumentoBozzaCassa1;
	}

	/**
	 * @param variazioniInAumentoBozzaCassa1 the variazioniInAumentoBozzaCassa1 to set
	 */
	public void setVariazioniInAumentoBozzaCassa1(BigDecimal variazioniInAumentoBozzaCassa1) {
		this.variazioniInAumentoBozzaCassa1 = variazioniInAumentoBozzaCassa1;
	}

	/**
	 * @return the variazioniInAumentoBozzaCompetenza2
	 */
	public BigDecimal getVariazioniInAumentoBozzaCompetenza2() {
		return this.variazioniInAumentoBozzaCompetenza2;
	}

	/**
	 * @param variazioniInAumentoBozzaCompetenza2 the variazioniInAumentoBozzaCompetenza2 to set
	 */
	public void setVariazioniInAumentoBozzaCompetenza2(BigDecimal variazioniInAumentoBozzaCompetenza2) {
		this.variazioniInAumentoBozzaCompetenza2 = variazioniInAumentoBozzaCompetenza2;
	}

	/**
	 * @return the variazioniInAumentoBozzaResiduo2
	 */
	public BigDecimal getVariazioniInAumentoBozzaResiduo2() {
		return this.variazioniInAumentoBozzaResiduo2;
	}

	/**
	 * @param variazioniInAumentoBozzaResiduo2 the variazioniInAumentoBozzaResiduo2 to set
	 */
	public void setVariazioniInAumentoBozzaResiduo2(BigDecimal variazioniInAumentoBozzaResiduo2) {
		this.variazioniInAumentoBozzaResiduo2 = variazioniInAumentoBozzaResiduo2;
	}

	/**
	 * @return the variazioniInAumentoBozzaCassa2
	 */
	public BigDecimal getVariazioniInAumentoBozzaCassa2() {
		return this.variazioniInAumentoBozzaCassa2;
	}

	/**
	 * @param variazioniInAumentoBozzaCassa2 the variazioniInAumentoBozzaCassa2 to set
	 */
	public void setVariazioniInAumentoBozzaCassa2(BigDecimal variazioniInAumentoBozzaCassa2) {
		this.variazioniInAumentoBozzaCassa2 = variazioniInAumentoBozzaCassa2;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaCompetenza0
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaCompetenza0() {
		return this.variazioniInAumentoDefinitivaCompetenza0;
	}

	/**
	 * @param variazioniInAumentoDefinitivaCompetenza0 the variazioniInAumentoDefinitivaCompetenza0 to set
	 */
	public void setVariazioniInAumentoDefinitivaCompetenza0(BigDecimal variazioniInAumentoDefinitivaCompetenza0) {
		this.variazioniInAumentoDefinitivaCompetenza0 = variazioniInAumentoDefinitivaCompetenza0;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaResiduo0
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaResiduo0() {
		return this.variazioniInAumentoDefinitivaResiduo0;
	}

	/**
	 * @param variazioniInAumentoDefinitivaResiduo0 the variazioniInAumentoDefinitivaResiduo0 to set
	 */
	public void setVariazioniInAumentoDefinitivaResiduo0(BigDecimal variazioniInAumentoDefinitivaResiduo0) {
		this.variazioniInAumentoDefinitivaResiduo0 = variazioniInAumentoDefinitivaResiduo0;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaCassa0
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaCassa0() {
		return this.variazioniInAumentoDefinitivaCassa0;
	}

	/**
	 * @param variazioniInAumentoDefinitivaCassa0 the variazioniInAumentoDefinitivaCassa0 to set
	 */
	public void setVariazioniInAumentoDefinitivaCassa0(BigDecimal variazioniInAumentoDefinitivaCassa0) {
		this.variazioniInAumentoDefinitivaCassa0 = variazioniInAumentoDefinitivaCassa0;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaCompetenza1
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaCompetenza1() {
		return this.variazioniInAumentoDefinitivaCompetenza1;
	}

	/**
	 * @param variazioniInAumentoDefinitivaCompetenza1 the variazioniInAumentoDefinitivaCompetenza1 to set
	 */
	public void setVariazioniInAumentoDefinitivaCompetenza1(BigDecimal variazioniInAumentoDefinitivaCompetenza1) {
		this.variazioniInAumentoDefinitivaCompetenza1 = variazioniInAumentoDefinitivaCompetenza1;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaResiduo1
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaResiduo1() {
		return this.variazioniInAumentoDefinitivaResiduo1;
	}

	/**
	 * @param variazioniInAumentoDefinitivaResiduo1 the variazioniInAumentoDefinitivaResiduo1 to set
	 */
	public void setVariazioniInAumentoDefinitivaResiduo1(BigDecimal variazioniInAumentoDefinitivaResiduo1) {
		this.variazioniInAumentoDefinitivaResiduo1 = variazioniInAumentoDefinitivaResiduo1;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaCassa1
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaCassa1() {
		return this.variazioniInAumentoDefinitivaCassa1;
	}

	/**
	 * @param variazioniInAumentoDefinitivaCassa1 the variazioniInAumentoDefinitivaCassa1 to set
	 */
	public void setVariazioniInAumentoDefinitivaCassa1(BigDecimal variazioniInAumentoDefinitivaCassa1) {
		this.variazioniInAumentoDefinitivaCassa1 = variazioniInAumentoDefinitivaCassa1;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaCompetenza2
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaCompetenza2() {
		return this.variazioniInAumentoDefinitivaCompetenza2;
	}

	/**
	 * @param variazioniInAumentoDefinitivaCompetenza2 the variazioniInAumentoDefinitivaCompetenza2 to set
	 */
	public void setVariazioniInAumentoDefinitivaCompetenza2(BigDecimal variazioniInAumentoDefinitivaCompetenza2) {
		this.variazioniInAumentoDefinitivaCompetenza2 = variazioniInAumentoDefinitivaCompetenza2;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaResiduo2
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaResiduo2() {
		return this.variazioniInAumentoDefinitivaResiduo2;
	}

	/**
	 * @param variazioniInAumentoDefinitivaResiduo2 the variazioniInAumentoDefinitivaResiduo2 to set
	 */
	public void setVariazioniInAumentoDefinitivaResiduo2(BigDecimal variazioniInAumentoDefinitivaResiduo2) {
		this.variazioniInAumentoDefinitivaResiduo2 = variazioniInAumentoDefinitivaResiduo2;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaCassa2
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaCassa2() {
		return this.variazioniInAumentoDefinitivaCassa2;
	}

	/**
	 * @param variazioniInAumentoDefinitivaCassa2 the variazioniInAumentoDefinitivaCassa2 to set
	 */
	public void setVariazioniInAumentoDefinitivaCassa2(BigDecimal variazioniInAumentoDefinitivaCassa2) {
		this.variazioniInAumentoDefinitivaCassa2 = variazioniInAumentoDefinitivaCassa2;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaCompetenza0
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaCompetenza0() {
		return this.variazioniInAumentoPreDefinitivaCompetenza0;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaCompetenza0 the variazioniInAumentoPreDefinitivaCompetenza0 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaCompetenza0(BigDecimal variazioniInAumentoPreDefinitivaCompetenza0) {
		this.variazioniInAumentoPreDefinitivaCompetenza0 = variazioniInAumentoPreDefinitivaCompetenza0;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaResiduo0
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaResiduo0() {
		return this.variazioniInAumentoPreDefinitivaResiduo0;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaResiduo0 the variazioniInAumentoPreDefinitivaResiduo0 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaResiduo0(BigDecimal variazioniInAumentoPreDefinitivaResiduo0) {
		this.variazioniInAumentoPreDefinitivaResiduo0 = variazioniInAumentoPreDefinitivaResiduo0;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaCassa0
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaCassa0() {
		return this.variazioniInAumentoPreDefinitivaCassa0;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaCassa0 the variazioniInAumentoPreDefinitivaCassa0 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaCassa0(BigDecimal variazioniInAumentoPreDefinitivaCassa0) {
		this.variazioniInAumentoPreDefinitivaCassa0 = variazioniInAumentoPreDefinitivaCassa0;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaCompetenza1
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaCompetenza1() {
		return this.variazioniInAumentoPreDefinitivaCompetenza1;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaCompetenza1 the variazioniInAumentoPreDefinitivaCompetenza1 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaCompetenza1(BigDecimal variazioniInAumentoPreDefinitivaCompetenza1) {
		this.variazioniInAumentoPreDefinitivaCompetenza1 = variazioniInAumentoPreDefinitivaCompetenza1;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaResiduo1
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaResiduo1() {
		return this.variazioniInAumentoPreDefinitivaResiduo1;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaResiduo1 the variazioniInAumentoPreDefinitivaResiduo1 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaResiduo1(BigDecimal variazioniInAumentoPreDefinitivaResiduo1) {
		this.variazioniInAumentoPreDefinitivaResiduo1 = variazioniInAumentoPreDefinitivaResiduo1;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaCassa1
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaCassa1() {
		return this.variazioniInAumentoPreDefinitivaCassa1;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaCassa1 the variazioniInAumentoPreDefinitivaCassa1 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaCassa1(BigDecimal variazioniInAumentoPreDefinitivaCassa1) {
		this.variazioniInAumentoPreDefinitivaCassa1 = variazioniInAumentoPreDefinitivaCassa1;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaCompetenza2
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaCompetenza2() {
		return this.variazioniInAumentoPreDefinitivaCompetenza2;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaCompetenza2 the variazioniInAumentoPreDefinitivaCompetenza2 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaCompetenza2(BigDecimal variazioniInAumentoPreDefinitivaCompetenza2) {
		this.variazioniInAumentoPreDefinitivaCompetenza2 = variazioniInAumentoPreDefinitivaCompetenza2;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaResiduo2
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaResiduo2() {
		return this.variazioniInAumentoPreDefinitivaResiduo2;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaResiduo2 the variazioniInAumentoPreDefinitivaResiduo2 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaResiduo2(BigDecimal variazioniInAumentoPreDefinitivaResiduo2) {
		this.variazioniInAumentoPreDefinitivaResiduo2 = variazioniInAumentoPreDefinitivaResiduo2;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaCassa2
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaCassa2() {
		return this.variazioniInAumentoPreDefinitivaCassa2;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaCassa2 the variazioniInAumentoPreDefinitivaCassa2 to set
	 */
	public void setVariazioniInAumentoPreDefinitivaCassa2(BigDecimal variazioniInAumentoPreDefinitivaCassa2) {
		this.variazioniInAumentoPreDefinitivaCassa2 = variazioniInAumentoPreDefinitivaCassa2;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioCompetenza0
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioCompetenza0() {
		return this.variazioniInAumentoGiuntaConsiglioCompetenza0;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioCompetenza0 the variazioniInAumentoGiuntaConsiglioCompetenza0 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioCompetenza0(BigDecimal variazioniInAumentoGiuntaConsiglioCompetenza0) {
		this.variazioniInAumentoGiuntaConsiglioCompetenza0 = variazioniInAumentoGiuntaConsiglioCompetenza0;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioResiduo0
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioResiduo0() {
		return this.variazioniInAumentoGiuntaConsiglioResiduo0;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioResiduo0 the variazioniInAumentoGiuntaConsiglioResiduo0 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioResiduo0(BigDecimal variazioniInAumentoGiuntaConsiglioResiduo0) {
		this.variazioniInAumentoGiuntaConsiglioResiduo0 = variazioniInAumentoGiuntaConsiglioResiduo0;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioCassa0
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioCassa0() {
		return this.variazioniInAumentoGiuntaConsiglioCassa0;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioCassa0 the variazioniInAumentoGiuntaConsiglioCassa0 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioCassa0(BigDecimal variazioniInAumentoGiuntaConsiglioCassa0) {
		this.variazioniInAumentoGiuntaConsiglioCassa0 = variazioniInAumentoGiuntaConsiglioCassa0;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioCompetenza1
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioCompetenza1() {
		return this.variazioniInAumentoGiuntaConsiglioCompetenza1;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioCompetenza1 the variazioniInAumentoGiuntaConsiglioCompetenza1 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioCompetenza1(BigDecimal variazioniInAumentoGiuntaConsiglioCompetenza1) {
		this.variazioniInAumentoGiuntaConsiglioCompetenza1 = variazioniInAumentoGiuntaConsiglioCompetenza1;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioResiduo1
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioResiduo1() {
		return this.variazioniInAumentoGiuntaConsiglioResiduo1;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioResiduo1 the variazioniInAumentoGiuntaConsiglioResiduo1 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioResiduo1(BigDecimal variazioniInAumentoGiuntaConsiglioResiduo1) {
		this.variazioniInAumentoGiuntaConsiglioResiduo1 = variazioniInAumentoGiuntaConsiglioResiduo1;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioCassa1
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioCassa1() {
		return this.variazioniInAumentoGiuntaConsiglioCassa1;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioCassa1 the variazioniInAumentoGiuntaConsiglioCassa1 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioCassa1(BigDecimal variazioniInAumentoGiuntaConsiglioCassa1) {
		this.variazioniInAumentoGiuntaConsiglioCassa1 = variazioniInAumentoGiuntaConsiglioCassa1;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioCompetenza2
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioCompetenza2() {
		return this.variazioniInAumentoGiuntaConsiglioCompetenza2;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioCompetenza2 the variazioniInAumentoGiuntaConsiglioCompetenza2 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioCompetenza2(BigDecimal variazioniInAumentoGiuntaConsiglioCompetenza2) {
		this.variazioniInAumentoGiuntaConsiglioCompetenza2 = variazioniInAumentoGiuntaConsiglioCompetenza2;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioResiduo2
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioResiduo2() {
		return this.variazioniInAumentoGiuntaConsiglioResiduo2;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioResiduo2 the variazioniInAumentoGiuntaConsiglioResiduo2 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioResiduo2(BigDecimal variazioniInAumentoGiuntaConsiglioResiduo2) {
		this.variazioniInAumentoGiuntaConsiglioResiduo2 = variazioniInAumentoGiuntaConsiglioResiduo2;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioCassa2
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioCassa2() {
		return this.variazioniInAumentoGiuntaConsiglioCassa2;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioCassa2 the variazioniInAumentoGiuntaConsiglioCassa2 to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioCassa2(BigDecimal variazioniInAumentoGiuntaConsiglioCassa2) {
		this.variazioniInAumentoGiuntaConsiglioCassa2 = variazioniInAumentoGiuntaConsiglioCassa2;
	}

	/**
	 * @return the variazioniInAumentoAnnullataCompetenza0
	 */
	public BigDecimal getVariazioniInAumentoAnnullataCompetenza0() {
		return this.variazioniInAumentoAnnullataCompetenza0;
	}

	/**
	 * @param variazioniInAumentoAnnullataCompetenza0 the variazioniInAumentoAnnullataCompetenza0 to set
	 */
	public void setVariazioniInAumentoAnnullataCompetenza0(BigDecimal variazioniInAumentoAnnullataCompetenza0) {
		this.variazioniInAumentoAnnullataCompetenza0 = variazioniInAumentoAnnullataCompetenza0;
	}

	/**
	 * @return the variazioniInAumentoAnnullataResiduo0
	 */
	public BigDecimal getVariazioniInAumentoAnnullataResiduo0() {
		return this.variazioniInAumentoAnnullataResiduo0;
	}

	/**
	 * @param variazioniInAumentoAnnullataResiduo0 the variazioniInAumentoAnnullataResiduo0 to set
	 */
	public void setVariazioniInAumentoAnnullataResiduo0(BigDecimal variazioniInAumentoAnnullataResiduo0) {
		this.variazioniInAumentoAnnullataResiduo0 = variazioniInAumentoAnnullataResiduo0;
	}

	/**
	 * @return the variazioniInAumentoAnnullataCassa0
	 */
	public BigDecimal getVariazioniInAumentoAnnullataCassa0() {
		return this.variazioniInAumentoAnnullataCassa0;
	}

	/**
	 * @param variazioniInAumentoAnnullataCassa0 the variazioniInAumentoAnnullataCassa0 to set
	 */
	public void setVariazioniInAumentoAnnullataCassa0(BigDecimal variazioniInAumentoAnnullataCassa0) {
		this.variazioniInAumentoAnnullataCassa0 = variazioniInAumentoAnnullataCassa0;
	}

	/**
	 * @return the variazioniInAumentoAnnullataCompetenza1
	 */
	public BigDecimal getVariazioniInAumentoAnnullataCompetenza1() {
		return this.variazioniInAumentoAnnullataCompetenza1;
	}

	/**
	 * @param variazioniInAumentoAnnullataCompetenza1 the variazioniInAumentoAnnullataCompetenza1 to set
	 */
	public void setVariazioniInAumentoAnnullataCompetenza1(BigDecimal variazioniInAumentoAnnullataCompetenza1) {
		this.variazioniInAumentoAnnullataCompetenza1 = variazioniInAumentoAnnullataCompetenza1;
	}

	/**
	 * @return the variazioniInAumentoAnnullataResiduo1
	 */
	public BigDecimal getVariazioniInAumentoAnnullataResiduo1() {
		return this.variazioniInAumentoAnnullataResiduo1;
	}

	/**
	 * @param variazioniInAumentoAnnullataResiduo1 the variazioniInAumentoAnnullataResiduo1 to set
	 */
	public void setVariazioniInAumentoAnnullataResiduo1(BigDecimal variazioniInAumentoAnnullataResiduo1) {
		this.variazioniInAumentoAnnullataResiduo1 = variazioniInAumentoAnnullataResiduo1;
	}

	/**
	 * @return the variazioniInAumentoAnnullataCassa1
	 */
	public BigDecimal getVariazioniInAumentoAnnullataCassa1() {
		return this.variazioniInAumentoAnnullataCassa1;
	}

	/**
	 * @param variazioniInAumentoAnnullataCassa1 the variazioniInAumentoAnnullataCassa1 to set
	 */
	public void setVariazioniInAumentoAnnullataCassa1(BigDecimal variazioniInAumentoAnnullataCassa1) {
		this.variazioniInAumentoAnnullataCassa1 = variazioniInAumentoAnnullataCassa1;
	}

	/**
	 * @return the variazioniInAumentoAnnullataCompetenza2
	 */
	public BigDecimal getVariazioniInAumentoAnnullataCompetenza2() {
		return this.variazioniInAumentoAnnullataCompetenza2;
	}

	/**
	 * @param variazioniInAumentoAnnullataCompetenza2 the variazioniInAumentoAnnullataCompetenza2 to set
	 */
	public void setVariazioniInAumentoAnnullataCompetenza2(BigDecimal variazioniInAumentoAnnullataCompetenza2) {
		this.variazioniInAumentoAnnullataCompetenza2 = variazioniInAumentoAnnullataCompetenza2;
	}

	/**
	 * @return the variazioniInAumentoAnnullataResiduo2
	 */
	public BigDecimal getVariazioniInAumentoAnnullataResiduo2() {
		return this.variazioniInAumentoAnnullataResiduo2;
	}

	/**
	 * @param variazioniInAumentoAnnullataResiduo2 the variazioniInAumentoAnnullataResiduo2 to set
	 */
	public void setVariazioniInAumentoAnnullataResiduo2(BigDecimal variazioniInAumentoAnnullataResiduo2) {
		this.variazioniInAumentoAnnullataResiduo2 = variazioniInAumentoAnnullataResiduo2;
	}

	/**
	 * @return the variazioniInAumentoAnnullataCassa2
	 */
	public BigDecimal getVariazioniInAumentoAnnullataCassa2() {
		return this.variazioniInAumentoAnnullataCassa2;
	}

	/**
	 * @param variazioniInAumentoAnnullataCassa2 the variazioniInAumentoAnnullataCassa2 to set
	 */
	public void setVariazioniInAumentoAnnullataCassa2(BigDecimal variazioniInAumentoAnnullataCassa2) {
		this.variazioniInAumentoAnnullataCassa2 = variazioniInAumentoAnnullataCassa2;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaNum0
	 */
	public long getVariazioniInDiminuzioneBozzaNum0() {
		return this.variazioniInDiminuzioneBozzaNum0;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaNum0 the variazioniInDiminuzioneBozzaNum0 to set
	 */
	public void setVariazioniInDiminuzioneBozzaNum0(long variazioniInDiminuzioneBozzaNum0) {
		this.variazioniInDiminuzioneBozzaNum0 = variazioniInDiminuzioneBozzaNum0;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaNum0
	 */
	public long getVariazioniInDiminuzioneDefinitivaNum0() {
		return this.variazioniInDiminuzioneDefinitivaNum0;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaNum0 the variazioniInDiminuzioneDefinitivaNum0 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaNum0(long variazioniInDiminuzioneDefinitivaNum0) {
		this.variazioniInDiminuzioneDefinitivaNum0 = variazioniInDiminuzioneDefinitivaNum0;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaNum0
	 */
	public long getVariazioniInDiminuzionePreDefinitivaNum0() {
		return this.variazioniInDiminuzionePreDefinitivaNum0;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaNum0 the variazioniInDiminuzionePreDefinitivaNum0 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaNum0(long variazioniInDiminuzionePreDefinitivaNum0) {
		this.variazioniInDiminuzionePreDefinitivaNum0 = variazioniInDiminuzionePreDefinitivaNum0;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioNum0
	 */
	public long getVariazioniInDiminuzioneGiuntaConsiglioNum0() {
		return this.variazioniInDiminuzioneGiuntaConsiglioNum0;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioNum0 the variazioniInDiminuzioneGiuntaConsiglioNum0 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioNum0(long variazioniInDiminuzioneGiuntaConsiglioNum0) {
		this.variazioniInDiminuzioneGiuntaConsiglioNum0 = variazioniInDiminuzioneGiuntaConsiglioNum0;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataNum0
	 */
	public long getVariazioniInDiminuzioneAnnullataNum0() {
		return this.variazioniInDiminuzioneAnnullataNum0;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataNum0 the variazioniInDiminuzioneAnnullataNum0 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataNum0(long variazioniInDiminuzioneAnnullataNum0) {
		this.variazioniInDiminuzioneAnnullataNum0 = variazioniInDiminuzioneAnnullataNum0;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaNum1
	 */
	public long getVariazioniInDiminuzioneBozzaNum1() {
		return this.variazioniInDiminuzioneBozzaNum1;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaNum1 the variazioniInDiminuzioneBozzaNum1 to set
	 */
	public void setVariazioniInDiminuzioneBozzaNum1(long variazioniInDiminuzioneBozzaNum1) {
		this.variazioniInDiminuzioneBozzaNum1 = variazioniInDiminuzioneBozzaNum1;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaNum1
	 */
	public long getVariazioniInDiminuzioneDefinitivaNum1() {
		return this.variazioniInDiminuzioneDefinitivaNum1;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaNum1 the variazioniInDiminuzioneDefinitivaNum1 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaNum1(long variazioniInDiminuzioneDefinitivaNum1) {
		this.variazioniInDiminuzioneDefinitivaNum1 = variazioniInDiminuzioneDefinitivaNum1;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaNum1
	 */
	public long getVariazioniInDiminuzionePreDefinitivaNum1() {
		return this.variazioniInDiminuzionePreDefinitivaNum1;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaNum1 the variazioniInDiminuzionePreDefinitivaNum1 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaNum1(long variazioniInDiminuzionePreDefinitivaNum1) {
		this.variazioniInDiminuzionePreDefinitivaNum1 = variazioniInDiminuzionePreDefinitivaNum1;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioNum1
	 */
	public long getVariazioniInDiminuzioneGiuntaConsiglioNum1() {
		return this.variazioniInDiminuzioneGiuntaConsiglioNum1;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioNum1 the variazioniInDiminuzioneGiuntaConsiglioNum1 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioNum1(long variazioniInDiminuzioneGiuntaConsiglioNum1) {
		this.variazioniInDiminuzioneGiuntaConsiglioNum1 = variazioniInDiminuzioneGiuntaConsiglioNum1;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataNum1
	 */
	public long getVariazioniInDiminuzioneAnnullataNum1() {
		return this.variazioniInDiminuzioneAnnullataNum1;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataNum1 the variazioniInDiminuzioneAnnullataNum1 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataNum1(long variazioniInDiminuzioneAnnullataNum1) {
		this.variazioniInDiminuzioneAnnullataNum1 = variazioniInDiminuzioneAnnullataNum1;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaNum2
	 */
	public long getVariazioniInDiminuzioneBozzaNum2() {
		return this.variazioniInDiminuzioneBozzaNum2;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaNum2 the variazioniInDiminuzioneBozzaNum2 to set
	 */
	public void setVariazioniInDiminuzioneBozzaNum2(long variazioniInDiminuzioneBozzaNum2) {
		this.variazioniInDiminuzioneBozzaNum2 = variazioniInDiminuzioneBozzaNum2;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaNum2
	 */
	public long getVariazioniInDiminuzioneDefinitivaNum2() {
		return this.variazioniInDiminuzioneDefinitivaNum2;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaNum2 the variazioniInDiminuzioneDefinitivaNum2 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaNum2(long variazioniInDiminuzioneDefinitivaNum2) {
		this.variazioniInDiminuzioneDefinitivaNum2 = variazioniInDiminuzioneDefinitivaNum2;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaNum2
	 */
	public long getVariazioniInDiminuzionePreDefinitivaNum2() {
		return this.variazioniInDiminuzionePreDefinitivaNum2;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaNum2 the variazioniInDiminuzionePreDefinitivaNum2 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaNum2(long variazioniInDiminuzionePreDefinitivaNum2) {
		this.variazioniInDiminuzionePreDefinitivaNum2 = variazioniInDiminuzionePreDefinitivaNum2;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioNum2
	 */
	public long getVariazioniInDiminuzioneGiuntaConsiglioNum2() {
		return this.variazioniInDiminuzioneGiuntaConsiglioNum2;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioNum2 the variazioniInDiminuzioneGiuntaConsiglioNum2 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioNum2(long variazioniInDiminuzioneGiuntaConsiglioNum2) {
		this.variazioniInDiminuzioneGiuntaConsiglioNum2 = variazioniInDiminuzioneGiuntaConsiglioNum2;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataNum2
	 */
	public long getVariazioniInDiminuzioneAnnullataNum2() {
		return this.variazioniInDiminuzioneAnnullataNum2;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataNum2 the variazioniInDiminuzioneAnnullataNum2 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataNum2(long variazioniInDiminuzioneAnnullataNum2) {
		this.variazioniInDiminuzioneAnnullataNum2 = variazioniInDiminuzioneAnnullataNum2;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaCompetenza0
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaCompetenza0() {
		return this.variazioniInDiminuzioneBozzaCompetenza0;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaCompetenza0 the variazioniInDiminuzioneBozzaCompetenza0 to set
	 */
	public void setVariazioniInDiminuzioneBozzaCompetenza0(BigDecimal variazioniInDiminuzioneBozzaCompetenza0) {
		this.variazioniInDiminuzioneBozzaCompetenza0 = variazioniInDiminuzioneBozzaCompetenza0;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaResiduo0
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaResiduo0() {
		return this.variazioniInDiminuzioneBozzaResiduo0;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaResiduo0 the variazioniInDiminuzioneBozzaResiduo0 to set
	 */
	public void setVariazioniInDiminuzioneBozzaResiduo0(BigDecimal variazioniInDiminuzioneBozzaResiduo0) {
		this.variazioniInDiminuzioneBozzaResiduo0 = variazioniInDiminuzioneBozzaResiduo0;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaCassa0
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaCassa0() {
		return this.variazioniInDiminuzioneBozzaCassa0;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaCassa0 the variazioniInDiminuzioneBozzaCassa0 to set
	 */
	public void setVariazioniInDiminuzioneBozzaCassa0(BigDecimal variazioniInDiminuzioneBozzaCassa0) {
		this.variazioniInDiminuzioneBozzaCassa0 = variazioniInDiminuzioneBozzaCassa0;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaCompetenza1
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaCompetenza1() {
		return this.variazioniInDiminuzioneBozzaCompetenza1;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaCompetenza1 the variazioniInDiminuzioneBozzaCompetenza1 to set
	 */
	public void setVariazioniInDiminuzioneBozzaCompetenza1(BigDecimal variazioniInDiminuzioneBozzaCompetenza1) {
		this.variazioniInDiminuzioneBozzaCompetenza1 = variazioniInDiminuzioneBozzaCompetenza1;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaResiduo1
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaResiduo1() {
		return this.variazioniInDiminuzioneBozzaResiduo1;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaResiduo1 the variazioniInDiminuzioneBozzaResiduo1 to set
	 */
	public void setVariazioniInDiminuzioneBozzaResiduo1(BigDecimal variazioniInDiminuzioneBozzaResiduo1) {
		this.variazioniInDiminuzioneBozzaResiduo1 = variazioniInDiminuzioneBozzaResiduo1;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaCassa1
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaCassa1() {
		return this.variazioniInDiminuzioneBozzaCassa1;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaCassa1 the variazioniInDiminuzioneBozzaCassa1 to set
	 */
	public void setVariazioniInDiminuzioneBozzaCassa1(BigDecimal variazioniInDiminuzioneBozzaCassa1) {
		this.variazioniInDiminuzioneBozzaCassa1 = variazioniInDiminuzioneBozzaCassa1;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaCompetenza2
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaCompetenza2() {
		return this.variazioniInDiminuzioneBozzaCompetenza2;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaCompetenza2 the variazioniInDiminuzioneBozzaCompetenza2 to set
	 */
	public void setVariazioniInDiminuzioneBozzaCompetenza2(BigDecimal variazioniInDiminuzioneBozzaCompetenza2) {
		this.variazioniInDiminuzioneBozzaCompetenza2 = variazioniInDiminuzioneBozzaCompetenza2;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaResiduo2
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaResiduo2() {
		return this.variazioniInDiminuzioneBozzaResiduo2;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaResiduo2 the variazioniInDiminuzioneBozzaResiduo2 to set
	 */
	public void setVariazioniInDiminuzioneBozzaResiduo2(BigDecimal variazioniInDiminuzioneBozzaResiduo2) {
		this.variazioniInDiminuzioneBozzaResiduo2 = variazioniInDiminuzioneBozzaResiduo2;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaCassa2
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaCassa2() {
		return this.variazioniInDiminuzioneBozzaCassa2;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaCassa2 the variazioniInDiminuzioneBozzaCassa2 to set
	 */
	public void setVariazioniInDiminuzioneBozzaCassa2(BigDecimal variazioniInDiminuzioneBozzaCassa2) {
		this.variazioniInDiminuzioneBozzaCassa2 = variazioniInDiminuzioneBozzaCassa2;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaCompetenza0
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaCompetenza0() {
		return this.variazioniInDiminuzioneDefinitivaCompetenza0;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaCompetenza0 the variazioniInDiminuzioneDefinitivaCompetenza0 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaCompetenza0(BigDecimal variazioniInDiminuzioneDefinitivaCompetenza0) {
		this.variazioniInDiminuzioneDefinitivaCompetenza0 = variazioniInDiminuzioneDefinitivaCompetenza0;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaResiduo0
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaResiduo0() {
		return this.variazioniInDiminuzioneDefinitivaResiduo0;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaResiduo0 the variazioniInDiminuzioneDefinitivaResiduo0 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaResiduo0(BigDecimal variazioniInDiminuzioneDefinitivaResiduo0) {
		this.variazioniInDiminuzioneDefinitivaResiduo0 = variazioniInDiminuzioneDefinitivaResiduo0;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaCassa0
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaCassa0() {
		return this.variazioniInDiminuzioneDefinitivaCassa0;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaCassa0 the variazioniInDiminuzioneDefinitivaCassa0 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaCassa0(BigDecimal variazioniInDiminuzioneDefinitivaCassa0) {
		this.variazioniInDiminuzioneDefinitivaCassa0 = variazioniInDiminuzioneDefinitivaCassa0;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaCompetenza1
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaCompetenza1() {
		return this.variazioniInDiminuzioneDefinitivaCompetenza1;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaCompetenza1 the variazioniInDiminuzioneDefinitivaCompetenza1 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaCompetenza1(BigDecimal variazioniInDiminuzioneDefinitivaCompetenza1) {
		this.variazioniInDiminuzioneDefinitivaCompetenza1 = variazioniInDiminuzioneDefinitivaCompetenza1;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaResiduo1
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaResiduo1() {
		return this.variazioniInDiminuzioneDefinitivaResiduo1;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaResiduo1 the variazioniInDiminuzioneDefinitivaResiduo1 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaResiduo1(BigDecimal variazioniInDiminuzioneDefinitivaResiduo1) {
		this.variazioniInDiminuzioneDefinitivaResiduo1 = variazioniInDiminuzioneDefinitivaResiduo1;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaCassa1
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaCassa1() {
		return this.variazioniInDiminuzioneDefinitivaCassa1;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaCassa1 the variazioniInDiminuzioneDefinitivaCassa1 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaCassa1(BigDecimal variazioniInDiminuzioneDefinitivaCassa1) {
		this.variazioniInDiminuzioneDefinitivaCassa1 = variazioniInDiminuzioneDefinitivaCassa1;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaCompetenza2
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaCompetenza2() {
		return this.variazioniInDiminuzioneDefinitivaCompetenza2;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaCompetenza2 the variazioniInDiminuzioneDefinitivaCompetenza2 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaCompetenza2(BigDecimal variazioniInDiminuzioneDefinitivaCompetenza2) {
		this.variazioniInDiminuzioneDefinitivaCompetenza2 = variazioniInDiminuzioneDefinitivaCompetenza2;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaResiduo2
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaResiduo2() {
		return this.variazioniInDiminuzioneDefinitivaResiduo2;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaResiduo2 the variazioniInDiminuzioneDefinitivaResiduo2 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaResiduo2(BigDecimal variazioniInDiminuzioneDefinitivaResiduo2) {
		this.variazioniInDiminuzioneDefinitivaResiduo2 = variazioniInDiminuzioneDefinitivaResiduo2;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaCassa2
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaCassa2() {
		return this.variazioniInDiminuzioneDefinitivaCassa2;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaCassa2 the variazioniInDiminuzioneDefinitivaCassa2 to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaCassa2(BigDecimal variazioniInDiminuzioneDefinitivaCassa2) {
		this.variazioniInDiminuzioneDefinitivaCassa2 = variazioniInDiminuzioneDefinitivaCassa2;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaCompetenza0
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaCompetenza0() {
		return this.variazioniInDiminuzionePreDefinitivaCompetenza0;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaCompetenza0 the variazioniInDiminuzionePreDefinitivaCompetenza0 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaCompetenza0(
			BigDecimal variazioniInDiminuzionePreDefinitivaCompetenza0) {
		this.variazioniInDiminuzionePreDefinitivaCompetenza0 = variazioniInDiminuzionePreDefinitivaCompetenza0;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaResiduo0
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaResiduo0() {
		return this.variazioniInDiminuzionePreDefinitivaResiduo0;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaResiduo0 the variazioniInDiminuzionePreDefinitivaResiduo0 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaResiduo0(BigDecimal variazioniInDiminuzionePreDefinitivaResiduo0) {
		this.variazioniInDiminuzionePreDefinitivaResiduo0 = variazioniInDiminuzionePreDefinitivaResiduo0;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaCassa0
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaCassa0() {
		return this.variazioniInDiminuzionePreDefinitivaCassa0;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaCassa0 the variazioniInDiminuzionePreDefinitivaCassa0 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaCassa0(BigDecimal variazioniInDiminuzionePreDefinitivaCassa0) {
		this.variazioniInDiminuzionePreDefinitivaCassa0 = variazioniInDiminuzionePreDefinitivaCassa0;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaCompetenza1
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaCompetenza1() {
		return this.variazioniInDiminuzionePreDefinitivaCompetenza1;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaCompetenza1 the variazioniInDiminuzionePreDefinitivaCompetenza1 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaCompetenza1(
			BigDecimal variazioniInDiminuzionePreDefinitivaCompetenza1) {
		this.variazioniInDiminuzionePreDefinitivaCompetenza1 = variazioniInDiminuzionePreDefinitivaCompetenza1;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaResiduo1
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaResiduo1() {
		return this.variazioniInDiminuzionePreDefinitivaResiduo1;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaResiduo1 the variazioniInDiminuzionePreDefinitivaResiduo1 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaResiduo1(BigDecimal variazioniInDiminuzionePreDefinitivaResiduo1) {
		this.variazioniInDiminuzionePreDefinitivaResiduo1 = variazioniInDiminuzionePreDefinitivaResiduo1;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaCassa1
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaCassa1() {
		return this.variazioniInDiminuzionePreDefinitivaCassa1;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaCassa1 the variazioniInDiminuzionePreDefinitivaCassa1 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaCassa1(BigDecimal variazioniInDiminuzionePreDefinitivaCassa1) {
		this.variazioniInDiminuzionePreDefinitivaCassa1 = variazioniInDiminuzionePreDefinitivaCassa1;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaCompetenza2
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaCompetenza2() {
		return this.variazioniInDiminuzionePreDefinitivaCompetenza2;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaCompetenza2 the variazioniInDiminuzionePreDefinitivaCompetenza2 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaCompetenza2(
			BigDecimal variazioniInDiminuzionePreDefinitivaCompetenza2) {
		this.variazioniInDiminuzionePreDefinitivaCompetenza2 = variazioniInDiminuzionePreDefinitivaCompetenza2;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaResiduo2
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaResiduo2() {
		return this.variazioniInDiminuzionePreDefinitivaResiduo2;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaResiduo2 the variazioniInDiminuzionePreDefinitivaResiduo2 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaResiduo2(BigDecimal variazioniInDiminuzionePreDefinitivaResiduo2) {
		this.variazioniInDiminuzionePreDefinitivaResiduo2 = variazioniInDiminuzionePreDefinitivaResiduo2;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaCassa2
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaCassa2() {
		return this.variazioniInDiminuzionePreDefinitivaCassa2;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaCassa2 the variazioniInDiminuzionePreDefinitivaCassa2 to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaCassa2(BigDecimal variazioniInDiminuzionePreDefinitivaCassa2) {
		this.variazioniInDiminuzionePreDefinitivaCassa2 = variazioniInDiminuzionePreDefinitivaCassa2;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioCompetenza0
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioCompetenza0() {
		return this.variazioniInDiminuzioneGiuntaConsiglioCompetenza0;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioCompetenza0 the variazioniInDiminuzioneGiuntaConsiglioCompetenza0 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioCompetenza0(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioCompetenza0) {
		this.variazioniInDiminuzioneGiuntaConsiglioCompetenza0 = variazioniInDiminuzioneGiuntaConsiglioCompetenza0;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioResiduo0
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioResiduo0() {
		return this.variazioniInDiminuzioneGiuntaConsiglioResiduo0;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioResiduo0 the variazioniInDiminuzioneGiuntaConsiglioResiduo0 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioResiduo0(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioResiduo0) {
		this.variazioniInDiminuzioneGiuntaConsiglioResiduo0 = variazioniInDiminuzioneGiuntaConsiglioResiduo0;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioCassa0
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioCassa0() {
		return this.variazioniInDiminuzioneGiuntaConsiglioCassa0;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioCassa0 the variazioniInDiminuzioneGiuntaConsiglioCassa0 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioCassa0(BigDecimal variazioniInDiminuzioneGiuntaConsiglioCassa0) {
		this.variazioniInDiminuzioneGiuntaConsiglioCassa0 = variazioniInDiminuzioneGiuntaConsiglioCassa0;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioCompetenza1
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioCompetenza1() {
		return this.variazioniInDiminuzioneGiuntaConsiglioCompetenza1;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioCompetenza1 the variazioniInDiminuzioneGiuntaConsiglioCompetenza1 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioCompetenza1(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioCompetenza1) {
		this.variazioniInDiminuzioneGiuntaConsiglioCompetenza1 = variazioniInDiminuzioneGiuntaConsiglioCompetenza1;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioResiduo1
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioResiduo1() {
		return this.variazioniInDiminuzioneGiuntaConsiglioResiduo1;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioResiduo1 the variazioniInDiminuzioneGiuntaConsiglioResiduo1 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioResiduo1(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioResiduo1) {
		this.variazioniInDiminuzioneGiuntaConsiglioResiduo1 = variazioniInDiminuzioneGiuntaConsiglioResiduo1;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioCassa1
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioCassa1() {
		return this.variazioniInDiminuzioneGiuntaConsiglioCassa1;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioCassa1 the variazioniInDiminuzioneGiuntaConsiglioCassa1 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioCassa1(BigDecimal variazioniInDiminuzioneGiuntaConsiglioCassa1) {
		this.variazioniInDiminuzioneGiuntaConsiglioCassa1 = variazioniInDiminuzioneGiuntaConsiglioCassa1;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioCompetenza2
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioCompetenza2() {
		return this.variazioniInDiminuzioneGiuntaConsiglioCompetenza2;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioCompetenza2 the variazioniInDiminuzioneGiuntaConsiglioCompetenza2 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioCompetenza2(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioCompetenza2) {
		this.variazioniInDiminuzioneGiuntaConsiglioCompetenza2 = variazioniInDiminuzioneGiuntaConsiglioCompetenza2;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioResiduo2
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioResiduo2() {
		return this.variazioniInDiminuzioneGiuntaConsiglioResiduo2;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioResiduo2 the variazioniInDiminuzioneGiuntaConsiglioResiduo2 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioResiduo2(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioResiduo2) {
		this.variazioniInDiminuzioneGiuntaConsiglioResiduo2 = variazioniInDiminuzioneGiuntaConsiglioResiduo2;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioCassa2
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioCassa2() {
		return this.variazioniInDiminuzioneGiuntaConsiglioCassa2;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioCassa2 the variazioniInDiminuzioneGiuntaConsiglioCassa2 to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioCassa2(BigDecimal variazioniInDiminuzioneGiuntaConsiglioCassa2) {
		this.variazioniInDiminuzioneGiuntaConsiglioCassa2 = variazioniInDiminuzioneGiuntaConsiglioCassa2;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataCompetenza0
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataCompetenza0() {
		return this.variazioniInDiminuzioneAnnullataCompetenza0;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataCompetenza0 the variazioniInDiminuzioneAnnullataCompetenza0 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataCompetenza0(BigDecimal variazioniInDiminuzioneAnnullataCompetenza0) {
		this.variazioniInDiminuzioneAnnullataCompetenza0 = variazioniInDiminuzioneAnnullataCompetenza0;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataResiduo0
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataResiduo0() {
		return this.variazioniInDiminuzioneAnnullataResiduo0;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataResiduo0 the variazioniInDiminuzioneAnnullataResiduo0 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataResiduo0(BigDecimal variazioniInDiminuzioneAnnullataResiduo0) {
		this.variazioniInDiminuzioneAnnullataResiduo0 = variazioniInDiminuzioneAnnullataResiduo0;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataCassa0
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataCassa0() {
		return this.variazioniInDiminuzioneAnnullataCassa0;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataCassa0 the variazioniInDiminuzioneAnnullataCassa0 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataCassa0(BigDecimal variazioniInDiminuzioneAnnullataCassa0) {
		this.variazioniInDiminuzioneAnnullataCassa0 = variazioniInDiminuzioneAnnullataCassa0;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataCompetenza1
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataCompetenza1() {
		return this.variazioniInDiminuzioneAnnullataCompetenza1;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataCompetenza1 the variazioniInDiminuzioneAnnullataCompetenza1 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataCompetenza1(BigDecimal variazioniInDiminuzioneAnnullataCompetenza1) {
		this.variazioniInDiminuzioneAnnullataCompetenza1 = variazioniInDiminuzioneAnnullataCompetenza1;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataResiduo1
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataResiduo1() {
		return this.variazioniInDiminuzioneAnnullataResiduo1;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataResiduo1 the variazioniInDiminuzioneAnnullataResiduo1 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataResiduo1(BigDecimal variazioniInDiminuzioneAnnullataResiduo1) {
		this.variazioniInDiminuzioneAnnullataResiduo1 = variazioniInDiminuzioneAnnullataResiduo1;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataCassa1
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataCassa1() {
		return this.variazioniInDiminuzioneAnnullataCassa1;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataCassa1 the variazioniInDiminuzioneAnnullataCassa1 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataCassa1(BigDecimal variazioniInDiminuzioneAnnullataCassa1) {
		this.variazioniInDiminuzioneAnnullataCassa1 = variazioniInDiminuzioneAnnullataCassa1;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataCompetenza2
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataCompetenza2() {
		return this.variazioniInDiminuzioneAnnullataCompetenza2;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataCompetenza2 the variazioniInDiminuzioneAnnullataCompetenza2 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataCompetenza2(BigDecimal variazioniInDiminuzioneAnnullataCompetenza2) {
		this.variazioniInDiminuzioneAnnullataCompetenza2 = variazioniInDiminuzioneAnnullataCompetenza2;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataResiduo2
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataResiduo2() {
		return this.variazioniInDiminuzioneAnnullataResiduo2;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataResiduo2 the variazioniInDiminuzioneAnnullataResiduo2 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataResiduo2(BigDecimal variazioniInDiminuzioneAnnullataResiduo2) {
		this.variazioniInDiminuzioneAnnullataResiduo2 = variazioniInDiminuzioneAnnullataResiduo2;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataCassa2
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataCassa2() {
		return this.variazioniInDiminuzioneAnnullataCassa2;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataCassa2 the variazioniInDiminuzioneAnnullataCassa2 to set
	 */
	public void setVariazioniInDiminuzioneAnnullataCassa2(BigDecimal variazioniInDiminuzioneAnnullataCassa2) {
		this.variazioniInDiminuzioneAnnullataCassa2 = variazioniInDiminuzioneAnnullataCassa2;
	}

	/**
	 * @return the variazioniInAumentoBozzaEquivalenteNum
	 */
	public long getVariazioniInAumentoBozzaEquivalenteNum() {
		return this.variazioniInAumentoBozzaEquivalenteNum;
	}

	/**
	 * @param variazioniInAumentoBozzaEquivalenteNum the variazioniInAumentoBozzaEquivalenteNum to set
	 */
	public void setVariazioniInAumentoBozzaEquivalenteNum(long variazioniInAumentoBozzaEquivalenteNum) {
		this.variazioniInAumentoBozzaEquivalenteNum = variazioniInAumentoBozzaEquivalenteNum;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaEquivalenteNum
	 */
	public long getVariazioniInAumentoDefinitivaEquivalenteNum() {
		return this.variazioniInAumentoDefinitivaEquivalenteNum;
	}

	/**
	 * @param variazioniInAumentoDefinitivaEquivalenteNum the variazioniInAumentoDefinitivaEquivalenteNum to set
	 */
	public void setVariazioniInAumentoDefinitivaEquivalenteNum(long variazioniInAumentoDefinitivaEquivalenteNum) {
		this.variazioniInAumentoDefinitivaEquivalenteNum = variazioniInAumentoDefinitivaEquivalenteNum;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaEquivalenteNum
	 */
	public long getVariazioniInAumentoPreDefinitivaEquivalenteNum() {
		return this.variazioniInAumentoPreDefinitivaEquivalenteNum;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaEquivalenteNum the variazioniInAumentoPreDefinitivaEquivalenteNum to set
	 */
	public void setVariazioniInAumentoPreDefinitivaEquivalenteNum(long variazioniInAumentoPreDefinitivaEquivalenteNum) {
		this.variazioniInAumentoPreDefinitivaEquivalenteNum = variazioniInAumentoPreDefinitivaEquivalenteNum;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioEquivalenteNum
	 */
	public long getVariazioniInAumentoGiuntaConsiglioEquivalenteNum() {
		return this.variazioniInAumentoGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioEquivalenteNum the variazioniInAumentoGiuntaConsiglioEquivalenteNum to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioEquivalenteNum(long variazioniInAumentoGiuntaConsiglioEquivalenteNum) {
		this.variazioniInAumentoGiuntaConsiglioEquivalenteNum = variazioniInAumentoGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @return the variazioniInAumentoAnnullataEquivalenteNum
	 */
	public long getVariazioniInAumentoAnnullataEquivalenteNum() {
		return this.variazioniInAumentoAnnullataEquivalenteNum;
	}

	/**
	 * @param variazioniInAumentoAnnullataEquivalenteNum the variazioniInAumentoAnnullataEquivalenteNum to set
	 */
	public void setVariazioniInAumentoAnnullataEquivalenteNum(long variazioniInAumentoAnnullataEquivalenteNum) {
		this.variazioniInAumentoAnnullataEquivalenteNum = variazioniInAumentoAnnullataEquivalenteNum;
	}

	/**
	 * @return the variazioniInAumentoBozzaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInAumentoBozzaEquivalenteCompetenza() {
		return this.variazioniInAumentoBozzaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInAumentoBozzaEquivalenteCompetenza the variazioniInAumentoBozzaEquivalenteCompetenza to set
	 */
	public void setVariazioniInAumentoBozzaEquivalenteCompetenza(BigDecimal variazioniInAumentoBozzaEquivalenteCompetenza) {
		this.variazioniInAumentoBozzaEquivalenteCompetenza = variazioniInAumentoBozzaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInAumentoBozzaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInAumentoBozzaEquivalenteResiduo() {
		return this.variazioniInAumentoBozzaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInAumentoBozzaEquivalenteResiduo the variazioniInAumentoBozzaEquivalenteResiduo to set
	 */
	public void setVariazioniInAumentoBozzaEquivalenteResiduo(BigDecimal variazioniInAumentoBozzaEquivalenteResiduo) {
		this.variazioniInAumentoBozzaEquivalenteResiduo = variazioniInAumentoBozzaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInAumentoBozzaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInAumentoBozzaEquivalenteCassa() {
		return this.variazioniInAumentoBozzaEquivalenteCassa;
	}

	/**
	 * @param variazioniInAumentoBozzaEquivalenteCassa the variazioniInAumentoBozzaEquivalenteCassa to set
	 */
	public void setVariazioniInAumentoBozzaEquivalenteCassa(BigDecimal variazioniInAumentoBozzaEquivalenteCassa) {
		this.variazioniInAumentoBozzaEquivalenteCassa = variazioniInAumentoBozzaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaEquivalenteCompetenza() {
		return this.variazioniInAumentoDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInAumentoDefinitivaEquivalenteCompetenza the variazioniInAumentoDefinitivaEquivalenteCompetenza to set
	 */
	public void setVariazioniInAumentoDefinitivaEquivalenteCompetenza(
			BigDecimal variazioniInAumentoDefinitivaEquivalenteCompetenza) {
		this.variazioniInAumentoDefinitivaEquivalenteCompetenza = variazioniInAumentoDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaEquivalenteResiduo() {
		return this.variazioniInAumentoDefinitivaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInAumentoDefinitivaEquivalenteResiduo the variazioniInAumentoDefinitivaEquivalenteResiduo to set
	 */
	public void setVariazioniInAumentoDefinitivaEquivalenteResiduo(
			BigDecimal variazioniInAumentoDefinitivaEquivalenteResiduo) {
		this.variazioniInAumentoDefinitivaEquivalenteResiduo = variazioniInAumentoDefinitivaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInAumentoDefinitivaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInAumentoDefinitivaEquivalenteCassa() {
		return this.variazioniInAumentoDefinitivaEquivalenteCassa;
	}

	/**
	 * @param variazioniInAumentoDefinitivaEquivalenteCassa the variazioniInAumentoDefinitivaEquivalenteCassa to set
	 */
	public void setVariazioniInAumentoDefinitivaEquivalenteCassa(BigDecimal variazioniInAumentoDefinitivaEquivalenteCassa) {
		this.variazioniInAumentoDefinitivaEquivalenteCassa = variazioniInAumentoDefinitivaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaEquivalenteCompetenza() {
		return this.variazioniInAumentoPreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaEquivalenteCompetenza the variazioniInAumentoPreDefinitivaEquivalenteCompetenza to set
	 */
	public void setVariazioniInAumentoPreDefinitivaEquivalenteCompetenza(
			BigDecimal variazioniInAumentoPreDefinitivaEquivalenteCompetenza) {
		this.variazioniInAumentoPreDefinitivaEquivalenteCompetenza = variazioniInAumentoPreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaEquivalenteResiduo() {
		return this.variazioniInAumentoPreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaEquivalenteResiduo the variazioniInAumentoPreDefinitivaEquivalenteResiduo to set
	 */
	public void setVariazioniInAumentoPreDefinitivaEquivalenteResiduo(
			BigDecimal variazioniInAumentoPreDefinitivaEquivalenteResiduo) {
		this.variazioniInAumentoPreDefinitivaEquivalenteResiduo = variazioniInAumentoPreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInAumentoPreDefinitivaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInAumentoPreDefinitivaEquivalenteCassa() {
		return this.variazioniInAumentoPreDefinitivaEquivalenteCassa;
	}

	/**
	 * @param variazioniInAumentoPreDefinitivaEquivalenteCassa the variazioniInAumentoPreDefinitivaEquivalenteCassa to set
	 */
	public void setVariazioniInAumentoPreDefinitivaEquivalenteCassa(
			BigDecimal variazioniInAumentoPreDefinitivaEquivalenteCassa) {
		this.variazioniInAumentoPreDefinitivaEquivalenteCassa = variazioniInAumentoPreDefinitivaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioEquivalenteCompetenza() {
		return this.variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza the variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioEquivalenteCompetenza(
			BigDecimal variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza) {
		this.variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza = variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioEquivalenteResiduo() {
		return this.variazioniInAumentoGiuntaConsiglioEquivalenteResiduo;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioEquivalenteResiduo the variazioniInAumentoGiuntaConsiglioEquivalenteResiduo to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioEquivalenteResiduo(
			BigDecimal variazioniInAumentoGiuntaConsiglioEquivalenteResiduo) {
		this.variazioniInAumentoGiuntaConsiglioEquivalenteResiduo = variazioniInAumentoGiuntaConsiglioEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInAumentoGiuntaConsiglioEquivalenteCassa
	 */
	public BigDecimal getVariazioniInAumentoGiuntaConsiglioEquivalenteCassa() {
		return this.variazioniInAumentoGiuntaConsiglioEquivalenteCassa;
	}

	/**
	 * @param variazioniInAumentoGiuntaConsiglioEquivalenteCassa the variazioniInAumentoGiuntaConsiglioEquivalenteCassa to set
	 */
	public void setVariazioniInAumentoGiuntaConsiglioEquivalenteCassa(
			BigDecimal variazioniInAumentoGiuntaConsiglioEquivalenteCassa) {
		this.variazioniInAumentoGiuntaConsiglioEquivalenteCassa = variazioniInAumentoGiuntaConsiglioEquivalenteCassa;
	}

	/**
	 * @return the variazioniInAumentoAnnullataEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInAumentoAnnullataEquivalenteCompetenza() {
		return this.variazioniInAumentoAnnullataEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInAumentoAnnullataEquivalenteCompetenza the variazioniInAumentoAnnullataEquivalenteCompetenza to set
	 */
	public void setVariazioniInAumentoAnnullataEquivalenteCompetenza(
			BigDecimal variazioniInAumentoAnnullataEquivalenteCompetenza) {
		this.variazioniInAumentoAnnullataEquivalenteCompetenza = variazioniInAumentoAnnullataEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInAumentoAnnullataEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInAumentoAnnullataEquivalenteResiduo() {
		return this.variazioniInAumentoAnnullataEquivalenteResiduo;
	}

	/**
	 * @param variazioniInAumentoAnnullataEquivalenteResiduo the variazioniInAumentoAnnullataEquivalenteResiduo to set
	 */
	public void setVariazioniInAumentoAnnullataEquivalenteResiduo(
			BigDecimal variazioniInAumentoAnnullataEquivalenteResiduo) {
		this.variazioniInAumentoAnnullataEquivalenteResiduo = variazioniInAumentoAnnullataEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInAumentoAnnullataEquivalenteCassa
	 */
	public BigDecimal getVariazioniInAumentoAnnullataEquivalenteCassa() {
		return this.variazioniInAumentoAnnullataEquivalenteCassa;
	}

	/**
	 * @param variazioniInAumentoAnnullataEquivalenteCassa the variazioniInAumentoAnnullataEquivalenteCassa to set
	 */
	public void setVariazioniInAumentoAnnullataEquivalenteCassa(BigDecimal variazioniInAumentoAnnullataEquivalenteCassa) {
		this.variazioniInAumentoAnnullataEquivalenteCassa = variazioniInAumentoAnnullataEquivalenteCassa;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaEquivalenteNum
	 */
	public long getVariazioniInDiminuzioneBozzaEquivalenteNum() {
		return this.variazioniInDiminuzioneBozzaEquivalenteNum;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaEquivalenteNum the variazioniInDiminuzioneBozzaEquivalenteNum to set
	 */
	public void setVariazioniInDiminuzioneBozzaEquivalenteNum(long variazioniInDiminuzioneBozzaEquivalenteNum) {
		this.variazioniInDiminuzioneBozzaEquivalenteNum = variazioniInDiminuzioneBozzaEquivalenteNum;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaEquivalenteNum
	 */
	public long getVariazioniInDiminuzioneDefinitivaEquivalenteNum() {
		return this.variazioniInDiminuzioneDefinitivaEquivalenteNum;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaEquivalenteNum the variazioniInDiminuzioneDefinitivaEquivalenteNum to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaEquivalenteNum(long variazioniInDiminuzioneDefinitivaEquivalenteNum) {
		this.variazioniInDiminuzioneDefinitivaEquivalenteNum = variazioniInDiminuzioneDefinitivaEquivalenteNum;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaEquivalenteNum
	 */
	public long getVariazioniInDiminuzionePreDefinitivaEquivalenteNum() {
		return this.variazioniInDiminuzionePreDefinitivaEquivalenteNum;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaEquivalenteNum the variazioniInDiminuzionePreDefinitivaEquivalenteNum to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaEquivalenteNum(
			long variazioniInDiminuzionePreDefinitivaEquivalenteNum) {
		this.variazioniInDiminuzionePreDefinitivaEquivalenteNum = variazioniInDiminuzionePreDefinitivaEquivalenteNum;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum
	 */
	public long getVariazioniInDiminuzioneGiuntaConsiglioEquivalenteNum() {
		return this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum the variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioEquivalenteNum(
			long variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum) {
		this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum = variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataEquivalenteNum
	 */
	public long getVariazioniInDiminuzioneAnnullataEquivalenteNum() {
		return this.variazioniInDiminuzioneAnnullataEquivalenteNum;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataEquivalenteNum the variazioniInDiminuzioneAnnullataEquivalenteNum to set
	 */
	public void setVariazioniInDiminuzioneAnnullataEquivalenteNum(long variazioniInDiminuzioneAnnullataEquivalenteNum) {
		this.variazioniInDiminuzioneAnnullataEquivalenteNum = variazioniInDiminuzioneAnnullataEquivalenteNum;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaEquivalenteCompetenza() {
		return this.variazioniInDiminuzioneBozzaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaEquivalenteCompetenza the variazioniInDiminuzioneBozzaEquivalenteCompetenza to set
	 */
	public void setVariazioniInDiminuzioneBozzaEquivalenteCompetenza(
			BigDecimal variazioniInDiminuzioneBozzaEquivalenteCompetenza) {
		this.variazioniInDiminuzioneBozzaEquivalenteCompetenza = variazioniInDiminuzioneBozzaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaEquivalenteResiduo() {
		return this.variazioniInDiminuzioneBozzaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaEquivalenteResiduo the variazioniInDiminuzioneBozzaEquivalenteResiduo to set
	 */
	public void setVariazioniInDiminuzioneBozzaEquivalenteResiduo(
			BigDecimal variazioniInDiminuzioneBozzaEquivalenteResiduo) {
		this.variazioniInDiminuzioneBozzaEquivalenteResiduo = variazioniInDiminuzioneBozzaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInDiminuzioneBozzaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInDiminuzioneBozzaEquivalenteCassa() {
		return this.variazioniInDiminuzioneBozzaEquivalenteCassa;
	}

	/**
	 * @param variazioniInDiminuzioneBozzaEquivalenteCassa the variazioniInDiminuzioneBozzaEquivalenteCassa to set
	 */
	public void setVariazioniInDiminuzioneBozzaEquivalenteCassa(BigDecimal variazioniInDiminuzioneBozzaEquivalenteCassa) {
		this.variazioniInDiminuzioneBozzaEquivalenteCassa = variazioniInDiminuzioneBozzaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaEquivalenteCompetenza() {
		return this.variazioniInDiminuzioneDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaEquivalenteCompetenza the variazioniInDiminuzioneDefinitivaEquivalenteCompetenza to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaEquivalenteCompetenza(
			BigDecimal variazioniInDiminuzioneDefinitivaEquivalenteCompetenza) {
		this.variazioniInDiminuzioneDefinitivaEquivalenteCompetenza = variazioniInDiminuzioneDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaEquivalenteResiduo() {
		return this.variazioniInDiminuzioneDefinitivaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaEquivalenteResiduo the variazioniInDiminuzioneDefinitivaEquivalenteResiduo to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaEquivalenteResiduo(
			BigDecimal variazioniInDiminuzioneDefinitivaEquivalenteResiduo) {
		this.variazioniInDiminuzioneDefinitivaEquivalenteResiduo = variazioniInDiminuzioneDefinitivaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInDiminuzioneDefinitivaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInDiminuzioneDefinitivaEquivalenteCassa() {
		return this.variazioniInDiminuzioneDefinitivaEquivalenteCassa;
	}

	/**
	 * @param variazioniInDiminuzioneDefinitivaEquivalenteCassa the variazioniInDiminuzioneDefinitivaEquivalenteCassa to set
	 */
	public void setVariazioniInDiminuzioneDefinitivaEquivalenteCassa(
			BigDecimal variazioniInDiminuzioneDefinitivaEquivalenteCassa) {
		this.variazioniInDiminuzioneDefinitivaEquivalenteCassa = variazioniInDiminuzioneDefinitivaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaEquivalenteCompetenza() {
		return this.variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza the variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaEquivalenteCompetenza(
			BigDecimal variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza) {
		this.variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza = variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaEquivalenteResiduo() {
		return this.variazioniInDiminuzionePreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaEquivalenteResiduo the variazioniInDiminuzionePreDefinitivaEquivalenteResiduo to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaEquivalenteResiduo(
			BigDecimal variazioniInDiminuzionePreDefinitivaEquivalenteResiduo) {
		this.variazioniInDiminuzionePreDefinitivaEquivalenteResiduo = variazioniInDiminuzionePreDefinitivaEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInDiminuzionePreDefinitivaEquivalenteCassa
	 */
	public BigDecimal getVariazioniInDiminuzionePreDefinitivaEquivalenteCassa() {
		return this.variazioniInDiminuzionePreDefinitivaEquivalenteCassa;
	}

	/**
	 * @param variazioniInDiminuzionePreDefinitivaEquivalenteCassa the variazioniInDiminuzionePreDefinitivaEquivalenteCassa to set
	 */
	public void setVariazioniInDiminuzionePreDefinitivaEquivalenteCassa(
			BigDecimal variazioniInDiminuzionePreDefinitivaEquivalenteCassa) {
		this.variazioniInDiminuzionePreDefinitivaEquivalenteCassa = variazioniInDiminuzionePreDefinitivaEquivalenteCassa;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza() {
		return this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza the variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza) {
		this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza = variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo() {
		return this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo the variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo) {
		this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo = variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa
	 */
	public BigDecimal getVariazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa() {
		return this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa;
	}

	/**
	 * @param variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa the variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa to set
	 */
	public void setVariazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa(
			BigDecimal variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa) {
		this.variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa = variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataEquivalenteCompetenza() {
		return this.variazioniInDiminuzioneAnnullataEquivalenteCompetenza;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataEquivalenteCompetenza the variazioniInDiminuzioneAnnullataEquivalenteCompetenza to set
	 */
	public void setVariazioniInDiminuzioneAnnullataEquivalenteCompetenza(
			BigDecimal variazioniInDiminuzioneAnnullataEquivalenteCompetenza) {
		this.variazioniInDiminuzioneAnnullataEquivalenteCompetenza = variazioniInDiminuzioneAnnullataEquivalenteCompetenza;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataEquivalenteResiduo() {
		return this.variazioniInDiminuzioneAnnullataEquivalenteResiduo;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataEquivalenteResiduo the variazioniInDiminuzioneAnnullataEquivalenteResiduo to set
	 */
	public void setVariazioniInDiminuzioneAnnullataEquivalenteResiduo(
			BigDecimal variazioniInDiminuzioneAnnullataEquivalenteResiduo) {
		this.variazioniInDiminuzioneAnnullataEquivalenteResiduo = variazioniInDiminuzioneAnnullataEquivalenteResiduo;
	}

	/**
	 * @return the variazioniInDiminuzioneAnnullataEquivalenteCassa
	 */
	public BigDecimal getVariazioniInDiminuzioneAnnullataEquivalenteCassa() {
		return this.variazioniInDiminuzioneAnnullataEquivalenteCassa;
	}

	/**
	 * @param variazioniInDiminuzioneAnnullataEquivalenteCassa the variazioniInDiminuzioneAnnullataEquivalenteCassa to set
	 */
	public void setVariazioniInDiminuzioneAnnullataEquivalenteCassa(
			BigDecimal variazioniInDiminuzioneAnnullataEquivalenteCassa) {
		this.variazioniInDiminuzioneAnnullataEquivalenteCassa = variazioniInDiminuzioneAnnullataEquivalenteCassa;
	}

	/**
	 * @return the listaVariazioneInAumento
	 */
	public List<VariazioneImportoCapitolo> getListaVariazioneInAumento() {
		return listaVariazioneInAumento;
	}

	/**
	 * @param listaVariazioneInAumento the listaVariazioneInAumento to set
	 */
	public void setListaVariazioneInAumento(List<VariazioneImportoCapitolo> listaVariazioneInAumento) {
		this.listaVariazioneInAumento = listaVariazioneInAumento;
	}

	/**
	 * @return the listaVariazioneInDiminuzione
	 */
	public List<VariazioneImportoCapitolo> getListaVariazioneInDiminuzione() {
		return listaVariazioneInDiminuzione;
	}

	/**
	 * @param listaVariazioneInDiminuzione the listaVariazioneInDiminuzione to set
	 */
	public void setListaVariazioneInDiminuzione(List<VariazioneImportoCapitolo> listaVariazioneInDiminuzione) {
		this.listaVariazioneInDiminuzione = listaVariazioneInDiminuzione;
	}

	/**
	 * @return the variazioniInAumentoTotaleNum0
	 */
	public long getVariazioniInAumentoTotaleNum0() {
		return variazioniInAumentoBozzaNum0 + variazioniInAumentoDefinitivaNum0 + variazioniInAumentoPreDefinitivaNum0
				+ variazioniInAumentoGiuntaConsiglioNum0;
	}

	/**
	 * @return the variazioniInAumentoTotaleNum1
	 */
	public long getVariazioniInAumentoTotaleNum1() {
		return variazioniInAumentoBozzaNum1 + variazioniInAumentoDefinitivaNum1 + variazioniInAumentoPreDefinitivaNum1
				+ variazioniInAumentoGiuntaConsiglioNum1;
	}

	/**
	 * @return the variazioniInAumentoTotaleNum2
	 */
	public long getVariazioniInAumentoTotaleNum2() {
		return variazioniInAumentoBozzaNum2 + variazioniInAumentoDefinitivaNum2 + variazioniInAumentoPreDefinitivaNum2
				+ variazioniInAumentoGiuntaConsiglioNum2;
	}

	/**
	 * @return the variazioniInAumentoTotaleCompetenza0
	 */
	public BigDecimal getVariazioniInAumentoTotaleCompetenza0() {
		return variazioniInAumentoBozzaCompetenza0
				.add(variazioniInAumentoDefinitivaCompetenza0)
				.add(variazioniInAumentoPreDefinitivaCompetenza0)
				.add(variazioniInAumentoGiuntaConsiglioCompetenza0);
	}

	/**
	 * @return the variazioniInAumentoTotaleResiduo0
	 */
	public BigDecimal getVariazioniInAumentoTotaleResiduo0() {
		return variazioniInAumentoBozzaResiduo0
				.add(variazioniInAumentoDefinitivaResiduo0)
				.add(variazioniInAumentoPreDefinitivaResiduo0)
				.add(variazioniInAumentoGiuntaConsiglioResiduo0);
	}

	/**
	 * @return the variazioniInAumentoTotaleCassa0
	 */
	public BigDecimal getVariazioniInAumentoTotaleCassa0() {
		return variazioniInAumentoBozzaCassa0
				.add(variazioniInAumentoDefinitivaCassa0)
				.add(variazioniInAumentoPreDefinitivaCassa0)
				.add(variazioniInAumentoGiuntaConsiglioCassa0);
	}

	/**
	 * @return the variazioniInAumentoTotaleCompetenza1
	 */
	public BigDecimal getVariazioniInAumentoTotaleCompetenza1() {
		return variazioniInAumentoBozzaCompetenza1
				.add(variazioniInAumentoDefinitivaCompetenza1)
				.add(variazioniInAumentoPreDefinitivaCompetenza1)
				.add(variazioniInAumentoGiuntaConsiglioCompetenza1);
	}

	/**
	 * @return the variazioniInAumentoTotaleResiduo1
	 */
	public BigDecimal getVariazioniInAumentoTotaleResiduo1() {
		return variazioniInAumentoBozzaResiduo1
				.add(variazioniInAumentoDefinitivaResiduo1)
				.add(variazioniInAumentoPreDefinitivaResiduo1)
				.add(variazioniInAumentoGiuntaConsiglioResiduo1);
	}

	/**
	 * @return the variazioniInAumentoTotaleCassa1
	 */
	public BigDecimal getVariazioniInAumentoTotaleCassa1() {
		return variazioniInAumentoBozzaCassa1
				.add(variazioniInAumentoDefinitivaCassa1)
				.add(variazioniInAumentoPreDefinitivaCassa1)
				.add(variazioniInAumentoGiuntaConsiglioCassa1);
	}

	/**
	 * @return the variazioniInAumentoTotaleCompetenza2
	 */
	public BigDecimal getVariazioniInAumentoTotaleCompetenza2() {
		return variazioniInAumentoBozzaCompetenza2
				.add(variazioniInAumentoDefinitivaCompetenza2)
				.add(variazioniInAumentoPreDefinitivaCompetenza2)
				.add(variazioniInAumentoGiuntaConsiglioCompetenza2);
	}

	/**
	 * @return the variazioniInAumentoTotaleResiduo2
	 */
	public BigDecimal getVariazioniInAumentoTotaleResiduo2() {
		return variazioniInAumentoBozzaResiduo2
				.add(variazioniInAumentoDefinitivaResiduo2)
				.add(variazioniInAumentoPreDefinitivaResiduo2)
				.add(variazioniInAumentoGiuntaConsiglioResiduo2);
	}

	/**
	 * @return the variazioniInAumentoTotaleCassa2
	 */
	public BigDecimal getVariazioniInAumentoTotaleCassa2() {
		return variazioniInAumentoBozzaCassa2
				.add(variazioniInAumentoDefinitivaCassa2)
				.add(variazioniInAumentoPreDefinitivaCassa2)
				.add(variazioniInAumentoGiuntaConsiglioCassa2);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleNum0
	 */
	public long getVariazioniInDiminuzioneTotaleNum0() {
		return variazioniInDiminuzioneBozzaNum0 + variazioniInDiminuzioneDefinitivaNum0 + variazioniInDiminuzionePreDefinitivaNum0
				+ variazioniInDiminuzioneGiuntaConsiglioNum0;
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleNum1
	 */
	public long getVariazioniInDiminuzioneTotaleNum1() {
		return variazioniInDiminuzioneBozzaNum1 + variazioniInDiminuzioneDefinitivaNum1 + variazioniInDiminuzionePreDefinitivaNum1
				+ variazioniInDiminuzioneGiuntaConsiglioNum1;
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleNum2
	 */
	public long getVariazioniInDiminuzioneTotaleNum2() {
		return variazioniInDiminuzioneBozzaNum2 + variazioniInDiminuzioneDefinitivaNum2 + variazioniInDiminuzionePreDefinitivaNum2
				+ variazioniInDiminuzioneGiuntaConsiglioNum2;
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleCompetenza0
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleCompetenza0() {
		return variazioniInDiminuzioneBozzaCompetenza0
				.add(variazioniInDiminuzioneDefinitivaCompetenza0)
				.add(variazioniInDiminuzionePreDefinitivaCompetenza0)
				.add(variazioniInDiminuzioneGiuntaConsiglioCompetenza0);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleResiduo0
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleResiduo0() {
		return variazioniInDiminuzioneBozzaResiduo0
				.add(variazioniInDiminuzioneDefinitivaResiduo0)
				.add(variazioniInDiminuzionePreDefinitivaResiduo0)
				.add(variazioniInDiminuzioneGiuntaConsiglioResiduo0);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleCassa0
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleCassa0() {
		return variazioniInDiminuzioneBozzaCassa0
				.add(variazioniInDiminuzioneDefinitivaCassa0)
				.add(variazioniInDiminuzionePreDefinitivaCassa0)
				.add(variazioniInDiminuzioneGiuntaConsiglioCassa0);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleCompetenza1
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleCompetenza1() {
		return variazioniInDiminuzioneBozzaCompetenza1
				.add(variazioniInDiminuzioneDefinitivaCompetenza1)
				.add(variazioniInDiminuzionePreDefinitivaCompetenza1)
				.add(variazioniInDiminuzioneGiuntaConsiglioCompetenza1);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleResiduo1
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleResiduo1() {
		return variazioniInDiminuzioneBozzaResiduo1
				.add(variazioniInDiminuzioneDefinitivaResiduo1)
				.add(variazioniInDiminuzionePreDefinitivaResiduo1)
				.add(variazioniInDiminuzioneGiuntaConsiglioResiduo1);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleCassa1
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleCassa1() {
		return variazioniInDiminuzioneBozzaCassa1
				.add(variazioniInDiminuzioneDefinitivaCassa1)
				.add(variazioniInDiminuzionePreDefinitivaCassa1)
				.add(variazioniInDiminuzioneGiuntaConsiglioCassa1);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleCompetenza2
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleCompetenza2() {
		return variazioniInDiminuzioneBozzaCompetenza2
				.add(variazioniInDiminuzioneDefinitivaCompetenza2)
				.add(variazioniInDiminuzionePreDefinitivaCompetenza2)
				.add(variazioniInDiminuzioneGiuntaConsiglioCompetenza2);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleResiduo2
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleResiduo2() {
		return variazioniInDiminuzioneBozzaResiduo2
				.add(variazioniInDiminuzioneDefinitivaResiduo2)
				.add(variazioniInDiminuzionePreDefinitivaResiduo2)
				.add(variazioniInDiminuzioneGiuntaConsiglioResiduo2);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleCassa2
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleCassa2() {
		return variazioniInDiminuzioneBozzaCassa2
				.add(variazioniInDiminuzioneDefinitivaCassa2)
				.add(variazioniInDiminuzionePreDefinitivaCassa2)
				.add(variazioniInDiminuzioneGiuntaConsiglioCassa2);
	}

	/**
	 * @return the variazioniInAumentoTotaleEquivalenteNum
	 */
	public long getVariazioniInAumentoTotaleEquivalenteNum() {
		return variazioniInAumentoBozzaEquivalenteNum + variazioniInAumentoDefinitivaEquivalenteNum + variazioniInAumentoPreDefinitivaEquivalenteNum
				+ variazioniInAumentoGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @return the variazioniInAumentoTotaleEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInAumentoTotaleEquivalenteCompetenza() {
		return variazioniInAumentoBozzaEquivalenteCompetenza
				.add(variazioniInAumentoDefinitivaEquivalenteCompetenza)
				.add(variazioniInAumentoPreDefinitivaEquivalenteCompetenza)
				.add(variazioniInAumentoGiuntaConsiglioEquivalenteCompetenza);
	}

	/**
	 * @return the variazioniInAumentoTotaleEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInAumentoTotaleEquivalenteResiduo() {
		return variazioniInAumentoBozzaEquivalenteResiduo
				.add(variazioniInAumentoDefinitivaEquivalenteResiduo)
				.add(variazioniInAumentoPreDefinitivaEquivalenteResiduo)
				.add(variazioniInAumentoGiuntaConsiglioEquivalenteResiduo);
	}

	/**
	 * @return the variazioniInAumentoTotaleEquivalenteCassa
	 */
	public BigDecimal getVariazioniInAumentoTotaleEquivalenteCassa() {
		return variazioniInAumentoBozzaEquivalenteCassa
				.add(variazioniInAumentoDefinitivaEquivalenteCassa)
				.add(variazioniInAumentoPreDefinitivaEquivalenteCassa)
				.add(variazioniInAumentoGiuntaConsiglioEquivalenteCassa);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleEquivalenteNum
	 */
	public long getVariazioniInDiminuzioneTotaleEquivalenteNum() {
		return variazioniInDiminuzioneBozzaEquivalenteNum + variazioniInDiminuzioneDefinitivaEquivalenteNum
				+ variazioniInDiminuzionePreDefinitivaEquivalenteNum + variazioniInDiminuzioneGiuntaConsiglioEquivalenteNum;
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleEquivalenteCompetenza
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleEquivalenteCompetenza() {
		return variazioniInDiminuzioneBozzaEquivalenteCompetenza
				.add(variazioniInDiminuzioneDefinitivaEquivalenteCompetenza)
				.add(variazioniInDiminuzionePreDefinitivaEquivalenteCompetenza)
				.add(variazioniInDiminuzioneGiuntaConsiglioEquivalenteCompetenza);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleEquivalenteResiduo
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleEquivalenteResiduo() {
		return variazioniInDiminuzioneBozzaEquivalenteResiduo
				.add(variazioniInDiminuzioneDefinitivaEquivalenteResiduo)
				.add(variazioniInDiminuzionePreDefinitivaEquivalenteResiduo)
				.add(variazioniInDiminuzioneGiuntaConsiglioEquivalenteResiduo);
	}

	/**
	 * @return the variazioniInDiminuzioneTotaleEquivalenteCassa
	 */
	public BigDecimal getVariazioniInDiminuzioneTotaleEquivalenteCassa() {
		return variazioniInDiminuzioneBozzaEquivalenteCassa
				.add(variazioniInDiminuzioneDefinitivaEquivalenteCassa)
				.add(variazioniInDiminuzionePreDefinitivaEquivalenteCassa)
				.add(variazioniInDiminuzioneGiuntaConsiglioEquivalenteCassa);
	}

	public List<VariazioneImportoCapitolo> getListaVariazioneInNeutre() {
		return listaVariazioneInNeutre;
	}

	public void setListaVariazioneInNeutre(List<VariazioneImportoCapitolo> listaVariazioneInNeutre) {
		this.listaVariazioneInNeutre = listaVariazioneInNeutre;
	}

	
	
	
	
	
	//
	/**
	 * @return the variazioniInNeutreTotaleNum0
	 */
	public long getVariazioniInNeutreTotaleNum0() {
		return variazioniInNeutreBozzaNum0 + variazioniInNeutreDefinitivaNum0 + variazioniInNeutrePreDefinitivaNum0
				+ variazioniInNeutreGiuntaConsiglioNum0;
	}

	/**
	 * @return the variazioniInNeutreTotaleNum1
	 */
	public long getVariazioniInNeutreTotaleNum1() {
		return variazioniInNeutreBozzaNum1 + variazioniInNeutreDefinitivaNum1 + variazioniInNeutrePreDefinitivaNum1
				+ variazioniInNeutreGiuntaConsiglioNum1;
	}

	/**
	 * @return the variazioniInNeutreTotaleNum2
	 */
	public long getVariazioniInNeutreTotaleNum2() {
		return variazioniInNeutreBozzaNum2 + variazioniInNeutreDefinitivaNum2 + variazioniInNeutrePreDefinitivaNum2
				+ variazioniInNeutreGiuntaConsiglioNum2;
	}

	/**
	 * @return the variazioniInNeutreTotaleCompetenza0
	 */
	public BigDecimal getVariazioniInNeutreTotaleCompetenza0() {
		return variazioniInNeutreBozzaCompetenza0
				.add(variazioniInNeutreDefinitivaCompetenza0)
				.add(variazioniInNeutrePreDefinitivaCompetenza0)
				.add(variazioniInNeutreGiuntaConsiglioCompetenza0);
	}

	/**
	 * @return the variazioniInNeutreTotaleResiduo0
	 */
	public BigDecimal getVariazioniInNeutreTotaleResiduo0() {
		return variazioniInNeutreBozzaResiduo0
				.add(variazioniInNeutreDefinitivaResiduo0)
				.add(variazioniInNeutrePreDefinitivaResiduo0)
				.add(variazioniInNeutreGiuntaConsiglioResiduo0);
	}

	/**
	 * @return the variazioniInNeutreTotaleCassa0
	 */
	public BigDecimal getVariazioniInNeutreTotaleCassa0() {
		return variazioniInNeutreBozzaCassa0
				.add(variazioniInNeutreDefinitivaCassa0)
				.add(variazioniInNeutrePreDefinitivaCassa0)
				.add(variazioniInNeutreGiuntaConsiglioCassa0);
	}

	/**
	 * @return the variazioniInNeutreTotaleCompetenza1
	 */
	public BigDecimal getVariazioniInNeutreTotaleCompetenza1() {
		return variazioniInNeutreBozzaCompetenza1
				.add(variazioniInNeutreDefinitivaCompetenza1)
				.add(variazioniInNeutrePreDefinitivaCompetenza1)
				.add(variazioniInNeutreGiuntaConsiglioCompetenza1);
	}

	/**
	 * @return the variazioniInNeutreTotaleResiduo1
	 */
	public BigDecimal getVariazioniInNeutreTotaleResiduo1() {
		return variazioniInNeutreBozzaResiduo1
				.add(variazioniInNeutreDefinitivaResiduo1)
				.add(variazioniInNeutrePreDefinitivaResiduo1)
				.add(variazioniInNeutreGiuntaConsiglioResiduo1);
	}

	/**
	 * @return the variazioniInNeutreTotaleCassa1
	 */
	public BigDecimal getVariazioniInNeutreTotaleCassa1() {
		return variazioniInNeutreBozzaCassa1
				.add(variazioniInNeutreDefinitivaCassa1)
				.add(variazioniInNeutrePreDefinitivaCassa1)
				.add(variazioniInNeutreGiuntaConsiglioCassa1);
	}

	/**
	 * @return the variazioniInNeutreTotaleCompetenza2
	 */
	public BigDecimal getVariazioniInNeutreTotaleCompetenza2() {
		return variazioniInNeutreBozzaCompetenza2
				.add(variazioniInNeutreDefinitivaCompetenza2)
				.add(variazioniInNeutrePreDefinitivaCompetenza2)
				.add(variazioniInNeutreGiuntaConsiglioCompetenza2);
	}

	/**
	 * @return the variazioniInNeutreTotaleResiduo2
	 */
	public BigDecimal getVariazioniInNeutreTotaleResiduo2() {
		return variazioniInNeutreBozzaResiduo2
				.add(variazioniInNeutreDefinitivaResiduo2)
				.add(variazioniInNeutrePreDefinitivaResiduo2)
				.add(variazioniInNeutreGiuntaConsiglioResiduo2);
	}

	/**
	 * @return the variazioniInNeutreTotaleCassa2
	 */
	public BigDecimal getVariazioniInNeutreTotaleCassa2() {
		return variazioniInNeutreBozzaCassa2
				.add(variazioniInNeutreDefinitivaCassa2)
				.add(variazioniInNeutrePreDefinitivaCassa2)
				.add(variazioniInNeutreGiuntaConsiglioCassa2);
	}
	
	
	
	/**
	 * SIAC-7735
	 */
//	public long getVariazioniInNeutreBozzaComplessiva() {
//		return variazioniInNeutreBozzaNum0 + variazioniInNeutreBozzaNum1 + variazioniInNeutreBozzaNum2;
//	}
//	
//	public long getVariazioniInNeutreDefinitivaComplessiva() {
//		return variazioniInNeutreDefinitivaNum0 + variazioniInNeutreDefinitivaNum1 + variazioniInNeutreDefinitivaNum2;
//	}
//	
//	public long getVariazioniInNeutrePreDefinitivaComplessiva() {
//		return variazioniInNeutrePreDefinitivaNum0 + variazioniInNeutrePreDefinitivaNum1 + variazioniInNeutrePreDefinitivaNum2;
//	}
//	
//	public long getVariazioniInNeutreGiuntaComplessiva() {
//		return variazioniInNeutreGiuntaConsiglioNum0 + variazioniInNeutreGiuntaConsiglioNum1 + variazioniInNeutreGiuntaConsiglioNum2;
//	}
//	
//	
//	public long getVariazioniInNeutreTotaleComplessiva() {
//		return getVariazioniInNeutreBozzaComplessiva() + getVariazioniInNeutreDefinitivaComplessiva() + getVariazioniInNeutrePreDefinitivaComplessiva()
//				+ getVariazioniInNeutreGiuntaComplessiva();
//	}
	
	
	private Integer variazioniInNeutreBozzaComplessiva;
	private Integer variazioniInNeutreDefinitivaComplessiva;
	private Integer variazioniInNeutrePreDefinitivaComplessiva;
	private Integer variazioniInNeutreGiuntaComplessiva;
	private Integer variazioniInNeutreTotaleComplessiva;

	/**
	 * @return the variazioniInNeutreBozzaComplessiva
	 */
	public Integer getVariazioniInNeutreBozzaComplessiva() {
		return variazioniInNeutreBozzaComplessiva;
	}

	/**
	 * @return the variazioniInNeutreDefinitivaComplessiva
	 */
	public Integer getVariazioniInNeutreDefinitivaComplessiva() {
		return variazioniInNeutreDefinitivaComplessiva;
	}

	/**
	 * @return the variazioniInNeutrePreDefinitivaComplessiva
	 */
	public Integer getVariazioniInNeutrePreDefinitivaComplessiva() {
		return variazioniInNeutrePreDefinitivaComplessiva;
	}

	/**
	 * @return the variazioniInNeutreGiuntaComplessiva
	 */
	public Integer getVariazioniInNeutreGiuntaComplessiva() {
		return variazioniInNeutreGiuntaComplessiva;
	}

	/**
	 * @return the variazioniInNeutreTotaleComplessiva
	 */
	public Integer getVariazioniInNeutreTotaleComplessiva() {
		return variazioniInNeutreTotaleComplessiva;
	}

	/**
	 * @param variazioniInNeutreBozzaComplessiva the variazioniInNeutreBozzaComplessiva to set
	 */
	public void setVariazioniInNeutreBozzaComplessiva(Integer variazioniInNeutreBozzaComplessiva) {
		this.variazioniInNeutreBozzaComplessiva = variazioniInNeutreBozzaComplessiva;
	}

	/**
	 * @param variazioniInNeutreDefinitivaComplessiva the variazioniInNeutreDefinitivaComplessiva to set
	 */
	public void setVariazioniInNeutreDefinitivaComplessiva(Integer variazioniInNeutreDefinitivaComplessiva) {
		this.variazioniInNeutreDefinitivaComplessiva = variazioniInNeutreDefinitivaComplessiva;
	}

	/**
	 * @param variazioniInNeutrePreDefinitivaComplessiva the variazioniInNeutrePreDefinitivaComplessiva to set
	 */
	public void setVariazioniInNeutrePreDefinitivaComplessiva(Integer variazioniInNeutrePreDefinitivaComplessiva) {
		this.variazioniInNeutrePreDefinitivaComplessiva = variazioniInNeutrePreDefinitivaComplessiva;
	}

	/**
	 * @param variazioniInNeutreGiuntaComplessiva the variazioniInNeutreGiuntaComplessiva to set
	 */
	public void setVariazioniInNeutreGiuntaComplessiva(Integer variazioniInNeutreGiuntaComplessiva) {
		this.variazioniInNeutreGiuntaComplessiva = variazioniInNeutreGiuntaComplessiva;
	}

	/**
	 * @param variazioniInNeutreTotaleComplessiva the variazioniInNeutreTotaleComplessiva to set
	 */
	public void setVariazioniInNeutreTotaleComplessiva(Integer variazioniInNeutreTotaleComplessiva) {
		this.variazioniInNeutreTotaleComplessiva = variazioniInNeutreTotaleComplessiva;
	}
	
	
	
}
