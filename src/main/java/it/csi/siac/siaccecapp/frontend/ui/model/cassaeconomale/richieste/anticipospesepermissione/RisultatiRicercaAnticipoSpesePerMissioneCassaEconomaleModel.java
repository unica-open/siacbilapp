/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;


/**
 * Classe di model per i risultati della ricerca dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 */
public class RisultatiRicercaAnticipoSpesePerMissioneCassaEconomaleModel extends BaseRisultatiRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8698143148121094447L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Risultati Ricerca anticipo spese per missione");
	}

	@Override
	public String getDenominazioneRisultatiRicerca() {
		return "Risultati anticipo spese per missione";
	}

	@Override
	public String getPathTipoRichiestaEconomale() {
		return "anticipoSpesePerMissione";
	}
	
	// SIAC-4618
	@Override
	public boolean isHasNumeroMovimentoRendiconto() {
		return true;
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
