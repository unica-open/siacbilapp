/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;


/**
 * Classe di model per i risultati della ricerca dell'anticipo spese per trasferta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 */
public class RisultatiRicercaAnticipoPerTrasfertaDipendentiCassaEconomaleModel extends BaseRisultatiRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8698143148121094447L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAnticipoPerTrasfertaDipendentiCassaEconomaleModel() {
		setTitolo("Risultati Ricerca anticipo per trasferta dipendenti");
	}
	
	@Override
	public String getDenominazioneRisultatiRicerca() {
		return "Risultati anticipo per trasferta dipendenti";
	}

	@Override
	public String getPathTipoRichiestaEconomale() {
		return "anticipoPerTrasfertaDipendenti";
	}
	
	@Override
	public String getIntestazioneImporto() {
		return "Importo spettante";
	}
	
	@Override
	public String getIntestazioneTotaleImporto() {
		return "Totale spettante";
	}

}
