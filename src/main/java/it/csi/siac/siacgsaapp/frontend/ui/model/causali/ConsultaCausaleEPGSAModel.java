/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.causali;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.ConsultaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
public class ConsultaCausaleEPGSAModel extends ConsultaCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6773153203342788633L;
	
	
	/** Costruttore vuoto di default */
	public ConsultaCausaleEPGSAModel() {
		setTitolo("Consulta Causali");
	}
	
	

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	
}
