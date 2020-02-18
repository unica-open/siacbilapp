/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;


/**
 * Classe di model per l'aggiornamento del rimborso spese.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2015
 *
 */
public class AggiornaRimborsoSpeseCassaEconomaleModel extends BaseInserisciAggiornaRimborsoSpeseCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3660732095139610801L;
	
	/** Costruttore vuoto di default */
	public AggiornaRimborsoSpeseCassaEconomaleModel() {
		setTitolo("Rimborso spese");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return true;
	}
	
	@Override
	public String getBaseUrl() {
		return "aggiornaRimborsoSpeseCassaEconomale";
	}
	
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public AggiornaRichiestaEconomale creaRequestAggiornaRichiestaEconomale() {
		AggiornaRichiestaEconomale request = creaRequest(AggiornaRichiestaEconomale.class);
		
		// Popolo la richiesta economale
		getRichiestaEconomale().setBilancio(getBilancio());
		getRichiestaEconomale().setEnte(getEnte());
		getRichiestaEconomale().setCassaEconomale(getCassaEconomale());
		getRichiestaEconomale().setGiustificativi(getListaGiustificativo());
		getRichiestaEconomale().setImpegno(getMovimentoGestione());
		getRichiestaEconomale().setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		
		// Popolo i classificatori
		getRichiestaEconomale().getClassificatoriGenerici().clear();
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico1());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico2());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico3());
		
		request.setRichiestaEconomale(getRichiestaEconomale());
		
		return request;
	}

}
