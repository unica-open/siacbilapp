/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleModel;


/**
 * Classe di model per la ricerca del pagamento
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/01/2016
 */
public class RicercaPagamentoCassaEconomaleModel extends BaseRicercaRichiestaEconomaleModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -4504943333215161060L;

	/** Costruttore vuoto di default */
	public RicercaPagamentoCassaEconomaleModel() {
		setTitolo("Ricerca pagamento");
	}

	/**
	 * @return the urlRicerca
	 */
	public String getUrlRicerca() {
		return "effettuaRicercaPagamentoCassaEconomale";
	}
	
	/**
	 * @return the denominazioneRicerca
	 */
	public String getDenominazioneRicerca() {
		return getTitolo();
	}
	
}
