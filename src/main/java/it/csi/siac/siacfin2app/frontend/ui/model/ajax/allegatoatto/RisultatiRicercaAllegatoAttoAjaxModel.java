/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoAllegatoAtto;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/set/2014
 *
 */
public class RisultatiRicercaAllegatoAttoAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoAllegatoAtto> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4029533272809633835L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Allegato Atto - AJAX");
	}
	
	/**
	 * Controlla se l'ente gestisce o meno l'integrazione con Atti di liquidazione.
	 * 
	 * @return <code>true</code> se l'ente gestisce l'integrazione; <code>false</code> in caso contrario
	 */
	public boolean isIntegrazioneAttiLiquidazione() {
		return getEnte() != null && getEnte().getGestioneLivelli() != null;
				// TODO: impostare il controllo corretto
				//&& BilConstants.GESTIONE_UEB.getConstant().equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO))
	}
}
