/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;

/**
 * Classe di model per i risultati della ricerca delle Prime note libera, gestione dell'AJAX.
 * 
 * @author Paggio Simona
 * @author Elisa Chiari
 * @version 1.0.1 -08/10/2015
 *
 */
public class RisultatiRicercaPrimaNotaLiberaBaseAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoPrimaNotaLibera> {
		
		/** Per la serializzazione */

		private static final long serialVersionUID = 649412662932709388L;

		/** Costruttore vuoto di default */
		public RisultatiRicercaPrimaNotaLiberaBaseAjaxModel() {
			setTitolo("Risultati ricerca prima nota libera - AJAX");
		}

	}
