/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.ConsultaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione della prima nota libera.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 06/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class ConsultaPrimaNotaLiberaFINModel extends ConsultaPrimaNotaLiberaBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -4555496844989703043L;

	/** Costruttore di default*/
	public ConsultaPrimaNotaLiberaFINModel() {
		setTitolo("Consulta prima nota libera");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
