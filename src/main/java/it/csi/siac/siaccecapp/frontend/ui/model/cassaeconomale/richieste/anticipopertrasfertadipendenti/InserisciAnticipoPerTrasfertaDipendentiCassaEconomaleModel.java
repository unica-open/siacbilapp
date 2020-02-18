/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;


/**
 * Classe di model per l'inserimento dell'anticipo spese per trasferta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
public class InserisciAnticipoPerTrasfertaDipendentiCassaEconomaleModel extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9166088136062012378L;
	
	

	/** Costruttore vuoto di default */
	public InserisciAnticipoPerTrasfertaDipendentiCassaEconomaleModel() {
		setTitolo("Anticipo per trasferta dipendenti");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciAnticipoPerTrasfertaDipendentiCassaEconomale";
	}
	
	@Override
	public boolean isDisabledImpegnoFields() {
		return false;
	}
	

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public InserisceRichiestaEconomale creaRequestInserisceRichiestaEconomale() {
		InserisceRichiestaEconomale request = creaRequest(InserisceRichiestaEconomale.class);
		
		// Popolo la richiesta economale
		getRichiestaEconomale().setBilancio(getBilancio());
		getRichiestaEconomale().setEnte(getEnte());
		getRichiestaEconomale().setCassaEconomale(getCassaEconomale());
		getRichiestaEconomale().setGiustificativi(getListaGiustificativo());
		getRichiestaEconomale().setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
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
		
		// Lotto P
		popolaDatiHR();
		
		request.setRichiestaEconomale(getRichiestaEconomale());
		
		return request;
	}

}
