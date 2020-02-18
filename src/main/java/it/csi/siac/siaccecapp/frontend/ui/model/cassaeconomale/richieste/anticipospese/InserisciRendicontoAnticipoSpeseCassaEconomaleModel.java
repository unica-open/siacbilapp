/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import java.util.Date;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;

/**
 * Classe di model per l'inserimento del rendiconto per l'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 *
 */
public class InserisciRendicontoAnticipoSpeseCassaEconomaleModel extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8480637006365938416L;

	/** Costruttore vuoto di default */
	public InserisciRendicontoAnticipoSpeseCassaEconomaleModel() {
		setTitolo("Anticipo spese - Inserimento Rendiconto");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return false;
	}

	
	@Override
	public String getBaseUrl() {
		return "inserisciRendicontoAnticipoSpeseCassaEconomale";
	}

	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceRendicontoRichiesta}.
	 * 
	 * @return la request creata
	 */
	public InserisceRendicontoRichiesta creaRequestInserisceRendicontoRichiesta() {
		InserisceRendicontoRichiesta request = creaRequest(InserisceRendicontoRichiesta.class);
		
		// Imposto la data
		getRendicontoRichiesta().setDataRendiconto(new Date());
		
		// Popolamento rendiconto
		getRendicontoRichiesta().setEnte(getEnte());
		getRendicontoRichiesta().setImportoIntegrato(getImportoDaIntegrare());
		getRendicontoRichiesta().setImportoRestituito(getImportoDaRestituire());
		getRendicontoRichiesta().setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		getRendicontoRichiesta().setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		getRendicontoRichiesta().setGiustificativi(getListaGiustificativo());
		
		request.setRendicontoRichiesta(getRendicontoRichiesta());
		
		return request;
	}

}