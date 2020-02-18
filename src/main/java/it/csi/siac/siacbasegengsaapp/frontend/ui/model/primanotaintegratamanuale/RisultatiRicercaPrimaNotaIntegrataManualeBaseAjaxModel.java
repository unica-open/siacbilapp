/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;

/**
 * Classe di model per i risultati della ricerca delle Prime note integrata manuale, gestione dell'AJAX.
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public class RisultatiRicercaPrimaNotaIntegrataManualeBaseAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoPrimaNotaLibera> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 649412662932709388L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaIntegrataManualeBaseAjaxModel() {
		setTitolo("Risultati ricerca prima nota integrata manuale - AJAX");
	}

}
