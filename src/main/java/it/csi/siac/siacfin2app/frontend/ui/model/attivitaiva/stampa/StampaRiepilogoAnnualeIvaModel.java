/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di model per la stampa del riepilogo annuale iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/07/2014
 *
 */
public class StampaRiepilogoAnnualeIvaModel extends GenericStampaIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7500173757685319600L;

	/** Costruttore vuoto di default */
	public StampaRiepilogoAnnualeIvaModel() {
		super();
		setTitolo("Stampa riepilogo annuale iva");
		setTemplateConferma(ErroreFin.CONFERMA_STAMPA_RIEPILOGO_ANNUALE_IVA.getErrore("___").getTesto());
	}
	
	@Override
	public String getAzioneIndietro() {
		return "stampaRiepilogoAnnualeIva";
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link StampaLiquidazioneIva}.
	 * 
	 * @return la request creata
	 */
	public StampaRiepilogoAnnualeIva creaRequestStampaRiepilogoAnnualeIva() {
		StampaRiepilogoAnnualeIva request = creaRequest(StampaRiepilogoAnnualeIva.class);
		
		request.setEnte(getEnte());
		request.setBilancio(getBilancio());
		request.setGruppoAttivitaIva(getGruppoAttivitaIva());
		
		return request;
	}

}
