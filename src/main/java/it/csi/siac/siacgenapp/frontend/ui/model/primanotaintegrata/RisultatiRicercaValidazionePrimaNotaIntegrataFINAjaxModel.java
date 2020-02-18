/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel;


/**
 * Classe di model per i risultati della ricerca delle prime note integrate per la validazione, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 */
public class RisultatiRicercaValidazionePrimaNotaIntegrataFINAjaxModel extends BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = 4615508697395550207L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaValidazionePrimaNotaIntegrataFINAjaxModel() {
		setTitolo("Risultati ricerca valida prima nota integrata - AJAX");
	}

}
