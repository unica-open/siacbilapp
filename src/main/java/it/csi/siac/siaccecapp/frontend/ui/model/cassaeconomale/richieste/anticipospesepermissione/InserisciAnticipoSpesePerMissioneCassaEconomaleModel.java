/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogata;


/**
 * Classe di model per l'inserimento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/01/2015
 * @version 1.0.1 - 27/01/2015 - rifattorizzazione del model
 *
 */
public class InserisciAnticipoSpesePerMissioneCassaEconomaleModel extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7866088136062012378L;
	
	private String idMissioneEsterna;

	/** Costruttore vuoto di default */
	public InserisciAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Anticipo spese per missione");
	}
	
	/**
	 * @return the idMissioneEsterna
	 */
	public String getIdMissioneEsterna() {
		return idMissioneEsterna;
	}

	/**
	 * @param idMissioneEsterna the idMissioneEsterna to set
	 */
	public void setIdMissioneEsterna(String idMissioneEsterna) {
		this.idMissioneEsterna = idMissioneEsterna;
	}

	@Override
	public boolean getIsAggiornamento() {
		return false;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciAnticipoSpesePerMissioneCassaEconomale";
	}
	
	@Override
	public boolean isDisabledImpegnoFields() {
		return false;
	}
	
	/**
	 * @return fromMissioneEsterna
	 */
	public boolean isFromMissioneEsterna() {
		return getRichiestaEconomale() != null && StringUtils.isNotBlank(getRichiestaEconomale().getIdMissioneEsterna());
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
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRichiestaAnticipoMissioneNonErogata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRichiestaAnticipoMissioneNonErogata creaRequestRicercaDettaglioRichiestaAnticipoMissioneNonErogata() {
		RicercaDettaglioRichiestaAnticipoMissioneNonErogata request = creaRequest(RicercaDettaglioRichiestaAnticipoMissioneNonErogata.class);
		request.setCassaEconomale(getCassaEconomale());
		request.setIdMissioneEsterna(getIdMissioneEsterna());
		return request;
	}

}
