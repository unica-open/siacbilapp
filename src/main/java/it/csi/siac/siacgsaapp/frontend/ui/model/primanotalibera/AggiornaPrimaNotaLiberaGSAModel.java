/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.AggiornaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di model per l'aggiornamento della prima nota libera. Modulo GSA
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 *
 */
public class AggiornaPrimaNotaLiberaGSAModel extends AggiornaPrimaNotaLiberaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8519725262207849414L;
	
	/** Costruttore vuoto di default */
	public AggiornaPrimaNotaLiberaGSAModel(){
		setTitolo("Aggiorna Prima Nota Libera");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

	@Override
	public String getAmbitoSuffix() {
		return "GSA";
	}
	
	@Override
	public boolean isValidazione() {
		return StatoOperativoPrimaNota.PROVVISORIO.equals(getPrimaNotaLibera().getStatoOperativoPrimaNota());
	}
	
	//SIAC-8134 per ora la GSA non può inserire e aggiornare prime note con le strutture
	@Override
	protected void popolaStrutturaCompetente() {
		// nothing to do
	}
	
}
