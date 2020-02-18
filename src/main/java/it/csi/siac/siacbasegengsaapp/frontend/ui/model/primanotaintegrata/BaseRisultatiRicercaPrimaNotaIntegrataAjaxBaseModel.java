/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;

/**
 * Classe base di model per i risultati della ricerca delle prime note integrate per la validazione, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 * @version 1.0.1 - 08/10/2015 - gestione GEN/GSA
 */
public abstract class BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel extends GenericRisultatiRicercaAjaxModel<ElementoPrimaNotaIntegrata> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4495177568238856337L;

}
