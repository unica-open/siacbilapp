/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.causali.ElementoCausaleEP;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;

/**
 * Classe di model per i risultati della ricerca delle causali EP, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 31/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public class RisultatiRicercaCausaleEPBaseAjaxModel extends PagedDataTableAjaxModel<ElementoCausaleEP> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3909218711844686533L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleEPBaseAjaxModel() {
		setTitolo("Risultati ricerca causali EP - AJAX");
	}

}
