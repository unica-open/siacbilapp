/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;


/**
 * Classe di model per i risultati della ricerca del pagamento
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/01/2016
 */
public class RisultatiRicercaPagamentoCassaEconomaleModel extends BaseRisultatiRicercaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 8202388889732737831L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPagamentoCassaEconomaleModel() {
		setTitolo("Risultati Ricerca pagamento");
	}

	@Override
	public String getDenominazioneRisultatiRicerca() {
		return "Risultati pagamento";
	}

	@Override
	/**
	 * Metodo necessario per poter avere la corretta navigazione tra action
	 * @return the pathTipoRichiestaEconomale
	 */
	public String getPathTipoRichiestaEconomale() {
		return "pagamento";
	}

}
