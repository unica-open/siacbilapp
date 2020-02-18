/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoIncassoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione dell'Ordinativo di incasso. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinOrdinativoIncassoGSAModel extends ConsultaRegistrazioneMovFinOrdinativoIncassoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5405350937001901651L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinOrdinativoIncassoGSAModel() {
		setTitolo("Consulta Movimento - Ordinativo di Incasso");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
