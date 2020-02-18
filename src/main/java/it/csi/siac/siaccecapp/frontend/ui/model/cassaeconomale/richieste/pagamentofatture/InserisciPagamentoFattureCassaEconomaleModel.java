/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;


/**
 * Classe di model per la consultazione del pagamento fatture.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 * 
 */
public class InserisciPagamentoFattureCassaEconomaleModel extends BaseInserisciAggiornaPagamentoFattureCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2367841945198602141L;

	/** Costruttore vuoto di default */
	public InserisciPagamentoFattureCassaEconomaleModel() {
		setTitolo("Pagamento fatture");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciPagamentoFattureCassaEconomale";
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
		getRichiestaEconomale().setSubdocumenti(getListaSubdocumentoSpesa());
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
