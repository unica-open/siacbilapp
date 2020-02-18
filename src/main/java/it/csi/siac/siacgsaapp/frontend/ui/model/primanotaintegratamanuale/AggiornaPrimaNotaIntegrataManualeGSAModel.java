/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.AggiornaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di model per l'aggiornamento della prima nota libera. Modulo GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public class AggiornaPrimaNotaIntegrataManualeGSAModel extends AggiornaPrimaNotaIntegrataManualeBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8519725262207849414L;

	/** Costruttore vuoto di default */
	public AggiornaPrimaNotaIntegrataManualeGSAModel(){
		setTitolo("Aggiorna Prima Nota Integrata Manuale - GSA");
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
	
}
