/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;


/**
 * Classe di model per l'aggiornamento dell'anticipo per trasferta dipendenti.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/02/2015
 *
 */
public class AggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6139838264238706295L;
	
	// Serve solo per impostare i campi che potrebbero andar persi durante le pulizie e le validazioni inter-step
	private RichiestaEconomale richiestaEconomaleTemporary;

	/** Costruttore vuoto di default */
	public AggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel() {
		setTitolo("Anticipo spese per trasferta dipendenti");
	}
	
	/**
	 * @return the richiestaEconomaleTemporary
	 */
	public RichiestaEconomale getRichiestaEconomaleTemporary() {
		return richiestaEconomaleTemporary;
	}

	/**
	 * @param richiestaEconomaleTemporary the richiestaEconomaleTemporary to set
	 */
	public void setRichiestaEconomaleTemporary(
			RichiestaEconomale richiestaEconomaleTemporary) {
		this.richiestaEconomaleTemporary = richiestaEconomaleTemporary;
	}

	@Override
	public boolean getIsAggiornamento() {
		return true;
	}
	
	@Override
	public String getBaseUrl() {
		return "aggiornaAnticipoPerTrasfertaDipendentiCassaEconomale";
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
