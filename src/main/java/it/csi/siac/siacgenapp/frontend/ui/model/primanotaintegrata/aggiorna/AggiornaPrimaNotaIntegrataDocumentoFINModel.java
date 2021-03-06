/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.aggiorna;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
/**
 * Classe di model per la consultazione della prima nota integrata. Modulo GEN
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
public class AggiornaPrimaNotaIntegrataDocumentoFINModel extends AggiornaPrimaNotaIntegrataDocumentoBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5236891507426388580L;

	/** Costruttore vuoto di default */
	public AggiornaPrimaNotaIntegrataDocumentoFINModel() {
		setTitolo("Aggiorna prima nota integrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
