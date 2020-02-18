/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;


/**
 * Classe di model per l'aggiornamento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2015
 *
 */
public class AggiornaAnticipoSpesePerMissioneCassaEconomaleModel extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6139838264238706295L;
	
	/** Costruttore vuoto di default */
	public AggiornaAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Anticipo spese per missione");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return true;
	}
	
	@Override
	public String getBaseUrl() {
		return "aggiornaAnticipoSpesePerMissioneCassaEconomale";
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
		
		// Imposto i mezzi di trasporto
		getRichiestaEconomale().getDatiTrasfertaMissione().setMezziDiTrasporto(getMezziDiTrasportoSelezionati());
		
		// TODO: controllare come trattarlo. Al momento e' forzato per evitare il campo NOT NULL su database
		getRichiestaEconomale().getDatiTrasfertaMissione().setCodice("");
		
		// Ripristino dei vecchi valori
		getRichiestaEconomale().setSospeso(getRichiestaEconomaleCopia().getSospeso());
		
		request.setRichiestaEconomale(getRichiestaEconomale());
		
		return request;
	}

}
