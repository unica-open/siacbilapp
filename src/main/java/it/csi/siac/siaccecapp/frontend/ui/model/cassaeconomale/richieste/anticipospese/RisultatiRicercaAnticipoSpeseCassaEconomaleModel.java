/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;


/**
 * Classe di model per i risultati della ricerca dell'anticipo spese.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class RisultatiRicercaAnticipoSpeseCassaEconomaleModel extends BaseRisultatiRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7934506106204532464L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAnticipoSpeseCassaEconomaleModel() {
		setTitolo("Risultati Ricerca anticipo spese");
	}
	
	@Override
	public String getDenominazioneRisultatiRicerca() {
		return "Risultati anticipo spese";
	}

	@Override
	public String getPathTipoRichiestaEconomale() {
		return "anticipoSpese";
	}
	
	// SIAC-4618
	@Override
	public boolean isHasNumeroMovimentoRendiconto() {
		return true;
	}
}
