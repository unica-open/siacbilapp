/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.ajax;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;

/**
 * Classe di model per la gestione della ricerca sintetica dell'accantonamento fondi dubbia esigibilita
 * @author Elisa Chiari
 * @version 1.0.0 - 02/11/2016
 *
 */
public class RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxModel extends GenericRisultatiRicercaAjaxModel<AccantonamentoFondiDubbiaEsigibilita> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4377698056456187771L;

	/** Costruttore di default */
	public RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxModel(){
		setTitolo("Ajax Model");
	}
}
