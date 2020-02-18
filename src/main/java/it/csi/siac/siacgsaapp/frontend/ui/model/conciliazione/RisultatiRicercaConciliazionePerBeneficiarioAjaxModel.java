/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerBeneficiario;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 03/11/2015
 *
 */
public class RisultatiRicercaConciliazionePerBeneficiarioAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoConciliazionePerBeneficiario> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3516844308895920927L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaConciliazionePerBeneficiarioAjaxModel() {
		super();
		setTitolo("Risultati di ricerca Conciliazione per beneficiario - AJAX");
	}
}
