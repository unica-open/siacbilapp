/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.ajax;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.PagedDataTableAjaxModel;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;

/**
 * Classe di model per la gestione della ricerca sintetica dell'accantonamento fondi dubbia esigibilit&agrave;, rendiconto
 * @author Marchino Alessandro
 */
public class RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxModel extends PagedDataTableAjaxModel<AccantonamentoFondiDubbiaEsigibilitaRendiconto> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2389606397010465216L;

	/** Costruttore di default */
	public RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxModel(){
		super();
		setTitolo("Ajax Model");
	}
}
