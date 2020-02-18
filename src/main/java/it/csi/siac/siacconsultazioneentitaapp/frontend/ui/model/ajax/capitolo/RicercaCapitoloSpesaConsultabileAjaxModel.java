/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo;

import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloSpesaConsultabile;

/**
 * Classe di model per la ricerca del capitolo spesa  consultabile (cruscottino) come entita' di partenza
 * @author Elisa Chiari
 * @version 1.0.0 - 02/03/2016
 *
 */
public class RicercaCapitoloSpesaConsultabileAjaxModel extends RicercaCapitoloConsultabileAjaxModel {
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -2280928464676040060L;
	
	private TitoloSpesa titoloSpesa;
	private Macroaggregato macroaggregato;

	
	/** Costruttore */
	public RicercaCapitoloSpesaConsultabileAjaxModel() {
		super();
		setTitolo("Ricerca Capitolo Spesa Consultabile - AJAX");
	}

	/**
	 * @return the request ottieniElencoEntitaDiPartenzaCapitoloEntrataConsultabile
	 */
	public RicercaSinteticaEntitaConsultabile creaRequestRicercaSinteticaCapitoloSpesaConsultabile() {
		ParametriRicercaCapitoloSpesaConsultabile prcsc = new ParametriRicercaCapitoloSpesaConsultabile();		
		prcsc.setTipoCapitolo("Spesa");
		prcsc.setFaseBilancio(getFaseBilancio());
		prcsc.setAnnoCapitolo(getAnnoCapitolo());
		prcsc.setNumeroCapitolo(getNumeroCapitolo());
		prcsc.setNumeroArticolo(getNumeroArticolo());
		prcsc.setNumeroUEB(getNumeroUEB());
		prcsc.setDescrizione(getDescrizione());
		prcsc.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		prcsc.setTitoloSpesa(getTitoloSpesa());
		prcsc.setMacroaggregato(getMacroaggregato());		
		return creaRequestRicercaSinteticaEntitaConsultabile(prcsc);
	}
	

	/**
	 * @return the titoloSpesa
	 */
	public TitoloSpesa getTitoloSpesa() {
		return titoloSpesa;
	}

	/**
	 * @param titoloSpesa the titoloSpesa to set
	 */
	public void setTitoloSpesa(TitoloSpesa titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}

	/**
	 * @return the macroaggregato
	 */
	public Macroaggregato getMacroaggregato() {
		return macroaggregato;
	}

	/**
	 * @param macroaggregato the macroaggregato to set
	 */
	public void setMacroaggregato(Macroaggregato macroaggregato) {
		this.macroaggregato = macroaggregato;
	}

}
