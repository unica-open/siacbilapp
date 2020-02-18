/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;

/**
 * Classe di model per l'inserimento dell'anticipo spese .
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
public class InserisciAnticipoSpeseCassaEconomaleModel extends BaseInserisciAggiornaAnticipoSpeseCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9208632295513108405L;

	/** Costruttore vuoto di default */
	public InserisciAnticipoSpeseCassaEconomaleModel() {
		setTitolo("Anticipo spese");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciAnticipoSpeseCassaEconomale";
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
		getRichiestaEconomale().setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		getRichiestaEconomale().setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		
		// Popolo i classificatori
		getRichiestaEconomale().getClassificatoriGenerici().clear();
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico1());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico2());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico3());

		// Lotto P
		popolaDatiHR();
		
		request.setRichiestaEconomale(getRichiestaEconomale());
		
		return request;
	}

}
