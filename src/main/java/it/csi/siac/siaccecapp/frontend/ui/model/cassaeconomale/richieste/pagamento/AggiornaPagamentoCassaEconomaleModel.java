/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento;

import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;


/**
 * Classe di model per l'aggiornamento del pagamento
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/01/2016
 *
 */
public class AggiornaPagamentoCassaEconomaleModel extends BaseInserisciAggiornaPagamentoCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3660732095139610801L;
	
	
	private boolean automatica= false;
	/** Costruttore vuoto di default */
	public AggiornaPagamentoCassaEconomaleModel() {
		setTitolo("Pagamento");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return true;
	}
	
	
	/**
	 * @return the automatica
	 */
	public boolean isAutomatica() {
		return automatica;
	}

	
	/**
	 * @param automatica the automatica to set
	 */
	public void setAutomatica(boolean automatica) {
		this.automatica = automatica;
	}
	@Override
	public String getBaseUrl() {
		return "aggiornaPagamentoCassaEconomale";
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
		getRichiestaEconomale().setSubdocumenti(getListaSubdocumentoSpesa());
		// Popolo i classificatori
		getRichiestaEconomale().getClassificatoriGenerici().clear();
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico1());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico2());
		addClassificatoreIfSelected(getRichiestaEconomale(), getClassificatoreGenerico3());
		
		request.setRichiestaEconomale(getRichiestaEconomale());
		
		return request;
	}

	

}
