/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;

/**
 * Classe di model per i risultati della ricerca delle Prime note libera, gestione dell'AJAX.
 * 
 * @author Valentina
 * @version 1.0.1 - 22/06/20216
 *
 */
public class RisultatiRicercaPrimeNoteAjaxModel extends PagedDataTableAjaxModel<ElementoPrimaNotaIntegrata> {
		
	
	private static final long serialVersionUID = -1260253097469485281L;

		/** Costruttore vuoto di default */
		public RisultatiRicercaPrimeNoteAjaxModel() {
			setTitolo("Risultati ricerca prime note - AJAX");
		}

	}
