/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;


/**
 * Classe di model per la consultazione del pagamento fatture.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 */
public class RisultatiRicercaPagamentoFattureCassaEconomaleModel extends BaseRisultatiRicercaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3843003120486174975L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPagamentoFattureCassaEconomaleModel() {
		setTitolo("Risultati Ricerca pagamento fatture");
	}

	@Override
	public String getDenominazioneRisultatiRicerca() {
		return "Risultati pagamento fatture";
	}

	@Override
	public String getPathTipoRichiestaEconomale() {
		return "pagamentoFatture";
	}

}
