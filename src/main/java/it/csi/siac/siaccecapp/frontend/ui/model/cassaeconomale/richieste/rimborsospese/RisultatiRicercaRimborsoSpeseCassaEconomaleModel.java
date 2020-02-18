/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;


/**
 * Classe di model per i risultati della ricerca del rimborso spese.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class RisultatiRicercaRimborsoSpeseCassaEconomaleModel extends BaseRisultatiRicercaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 8202388889732737831L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRimborsoSpeseCassaEconomaleModel() {
		setTitolo("Risultati Ricerca rimborso spese");
	}

	@Override
	public String getDenominazioneRisultatiRicerca() {
		return "Risultati rimborso spese";
	}

	@Override
	/**
	 * Metodo necessario per poter avere la corretta navigazione tra action
	 * @return the pathTipoRichiestaEconomale
	 */
	public String getPathTipoRichiestaEconomale() {
		return "rimborsoSpese";
	}

}
