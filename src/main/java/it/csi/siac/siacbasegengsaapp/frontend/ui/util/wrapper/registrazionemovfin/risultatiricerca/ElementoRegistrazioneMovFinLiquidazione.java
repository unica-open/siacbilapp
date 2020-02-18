/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca;

import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Wrapper per la registrazioneMovFin per la liquidazione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/01/2016
 *
 */
public class ElementoRegistrazioneMovFinLiquidazione extends ElementoRegistrazioneMovFin {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6714703895638634414L;

	/**
	 * @see ElementoRegistrazioneMovFin#ElementoRegistrazioneMovFin(RegistrazioneMovFin)
	 */
	public ElementoRegistrazioneMovFinLiquidazione(RegistrazioneMovFin registrazioneMovFin) {
		super(registrazioneMovFin);
	}

	@Override
	public String getStringaMovimento(){
		if(registrazioneMovFin == null || registrazioneMovFin.getEvento() == null || registrazioneMovFin.getMovimento() == null) {
			return "";
		}
		Liquidazione liquidazione = (Liquidazione) registrazioneMovFin.getMovimento();
		return liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione();
	}
	
}
