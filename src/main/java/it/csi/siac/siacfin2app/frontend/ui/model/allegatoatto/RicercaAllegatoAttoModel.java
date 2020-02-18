/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

/**
 * Classe di model per la ricerca dell'allegato atto.
 * 
 * @author elisa
 * @version 1.0.0 - 26/feb/2018
 *
 */
public class RicercaAllegatoAttoModel extends RicercaAllegatoAttoBaseModel {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8851490141438411826L;

	/** Costruttore vuoto di default */
	public RicercaAllegatoAttoModel() {
		super();
		setTitolo("Ricerca allegato atto");
		setNomeAzioneRicerca("effettuaRicercaAllegatoAtto");
	}
	
}
