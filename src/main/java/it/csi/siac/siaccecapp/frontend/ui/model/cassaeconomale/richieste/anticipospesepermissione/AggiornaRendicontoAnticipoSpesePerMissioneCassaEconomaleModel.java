/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import java.util.Date;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;

/**
 * Classe di model per l'inserimento del rendiconto per l'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2015
 *
 */
public class AggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel extends BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8480637006365938416L;
	
	// SIAC-5192
	private boolean originariamenteRestituzione;
	
	/** Costruttore vuoto di default */
	public AggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Anticipo spese per missione - Aggiornamento Rendiconto");
	}
	
	/**
	 * @return the originariamenteRestituzione
	 */
	public boolean isOriginariamenteRestituzione() {
		return originariamenteRestituzione;
	}

	/**
	 * @param originariamenteRestituzione the originariamenteRestituzione to set
	 */
	public void setOriginariamenteRestituzione(boolean originariamenteRestituzione) {
		this.originariamenteRestituzione = originariamenteRestituzione;
	}

	@Override
	public boolean getIsAggiornamento() {
		return true;
	}
	
	@Override
	public String getBaseUrl() {
		return "aggiornaRendicontoAnticipoSpesePerMissioneCassaEconomale";
	}

	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaRendicontoRichiesta}.
	 * 
	 * @return la request creata
	 */
	public AggiornaRendicontoRichiesta creaRequestAggiornaRendicontoRichiesta() {
		AggiornaRendicontoRichiesta request = creaRequest(AggiornaRendicontoRichiesta.class);
		
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

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRendicontoRichiesta}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRendicontoRichiesta creRequestRicercaDettaglioRendicontoRichiesta() {
		RicercaDettaglioRendicontoRichiesta request = creaRequest(RicercaDettaglioRendicontoRichiesta.class);
		request.setRendicontoRichiesta(getRendicontoRichiesta());
		return request;
	}
	
}
