/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamento;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;


/**
 * Classe di model per l'inserimento del pagamento
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/01/2016
 *
 */
public class InserisciPagamentoCassaEconomaleModel extends BaseInserisciAggiornaPagamentoCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7489415433686467035L;
	
	private Integer uidTipoGiustificativoFattura;

	/** Costruttore vuoto di default */
	public InserisciPagamentoCassaEconomaleModel() {
		setTitolo("Pagamento");
	}
	
	@Override
	public boolean getIsAggiornamento() {
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciPagamentoCassaEconomale";
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
		
		request.setRichiestaEconomale(getRichiestaEconomale());
		
		// Lotto P
		//popolaDatiHR();
		
		return request;
	}
	
	/**
	 * @return the uidTipoGiustificativoFattura
	 */
	public Integer getUidTipoGiustificativoFattura() {
		return uidTipoGiustificativoFattura;
	}

	/**
	 * @param uidTipoGiustificativoFattura the uidTipoGiustificativoFattura to set
	 */
	public void setUidTipoGiustificativoFattura(Integer uidTipoGiustificativoFattura) {
		this.uidTipoGiustificativoFattura = uidTipoGiustificativoFattura;
	}

}
