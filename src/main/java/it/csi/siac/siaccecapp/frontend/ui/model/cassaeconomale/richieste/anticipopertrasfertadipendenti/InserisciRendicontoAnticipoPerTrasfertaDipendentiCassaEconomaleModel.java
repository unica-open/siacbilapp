/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;

/**
 * Classe di model per l'inserimento del rendiconto per l'anticipo spese per trasferta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/02/2015
 *
 */
public class InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel extends BaseInserisciAggiornaRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8480637006365938416L;

	/** Costruttore vuoto di default */
	public InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel() {
		setTitolo("Anticipo per trasferta dipendenti - Inserimento Rendiconto");
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomale";
	}

	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceRendicontoRichiesta}.
	 * 
	 * @return la request creata
	 */
	public InserisceRendicontoRichiesta creaRequestInserisceRendicontoRichiesta() {
		InserisceRendicontoRichiesta request = creaRequest(InserisceRendicontoRichiesta.class);
		
		// Popolamento rendiconto
		getRendicontoRichiesta().setEnte(getEnte());
		getRendicontoRichiesta().setImportoIntegrato(getImportoDaIntegrare());
		getRendicontoRichiesta().setImportoRestituito(getImportoDaRestituire());
		
		getRendicontoRichiesta().setGiustificativi(getListaGiustificativo());
		
		request.setRendicontoRichiesta(getRendicontoRichiesta());
		
		return request;
	}

}
