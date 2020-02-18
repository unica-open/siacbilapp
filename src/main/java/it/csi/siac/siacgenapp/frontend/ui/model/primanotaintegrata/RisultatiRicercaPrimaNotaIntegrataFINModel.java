/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la ricerca della prima nota integrata. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.1.0 - 08/10/2015 - gestione GEN/GSA
 */
public class RisultatiRicercaPrimaNotaIntegrataFINModel extends RisultatiRicercaPrimaNotaIntegrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8785495670182558416L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaIntegrataFINModel() {
		setTitolo("Risultati ricerca prima nota integrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

	@Override
	public String getBaseUrl() {
		return "risultatiRicercaPrimaNotaIntegrataFIN";
	}
}
