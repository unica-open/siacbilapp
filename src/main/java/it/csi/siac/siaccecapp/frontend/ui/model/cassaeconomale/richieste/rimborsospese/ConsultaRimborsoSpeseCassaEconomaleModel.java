/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;


/**
 * Classe di model per la consultazione del rimborso spese.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class ConsultaRimborsoSpeseCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 3734224877809805503L;

	/** Costruttore vuoto di default */
	public ConsultaRimborsoSpeseCassaEconomaleModel() {
		setTitolo("Consultazione rimborso spese");
	}

}
