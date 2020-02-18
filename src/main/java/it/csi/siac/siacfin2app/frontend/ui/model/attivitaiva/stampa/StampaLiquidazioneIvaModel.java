/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di model per la stampa della liquidazione iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/07/2014
 *
 */
public class StampaLiquidazioneIvaModel extends GenericStampaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7305872582067401739L;
	
	/** Costruttore vuoto di default */
	public StampaLiquidazioneIvaModel() {
		super();
		setTitolo("Stampa liquidazione iva");
		setTemplateConferma(ErroreFin.CONFERMA_STAMPA_LIQUIDAZIONE_IVA.getErrore("___").getTesto());
	}
	
	@Override
	public String getAzioneIndietro() {
		return "stampaLiquidazioneIva";
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link StampaLiquidazioneIva}.
	 * 
	 * @return la request creata
	 */
	public StampaLiquidazioneIva creaRequestStampaLiquidazioneIva() {
		StampaLiquidazioneIva request = creaRequest(StampaLiquidazioneIva.class);
		
		request.setBilancio(getBilancio());
		request.setGruppoAttivitaIva(getGruppoAttivitaIva());
		request.setEnte(getEnte());
		request.setPeriodo(getPeriodo());
		request.setTipoStampa(getTipoStampa());
		
		getGruppoAttivitaIva().setTipoChiusura(getTipoChiusura());
		
		ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva = new ProRataEChiusuraGruppoIva();
		proRataEChiusuraGruppoIva.setIvaPrecedente(getIvaACredito());
		
		List<ProRataEChiusuraGruppoIva> listaProRataEChiusuraGruppoIva = new ArrayList<ProRataEChiusuraGruppoIva>();
		listaProRataEChiusuraGruppoIva.add(proRataEChiusuraGruppoIva);
		
		getGruppoAttivitaIva().setListaProRataEChiusuraGruppoIva(listaProRataEChiusuraGruppoIva);
		
		return request;
	}

}
