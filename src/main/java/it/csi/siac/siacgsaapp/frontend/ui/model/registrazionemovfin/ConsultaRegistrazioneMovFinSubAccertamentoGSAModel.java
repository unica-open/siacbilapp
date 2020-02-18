/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinSubAccertamentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione del SubAccertamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinSubAccertamentoGSAModel extends ConsultaRegistrazioneMovFinSubAccertamentoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9183052827395172841L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinSubAccertamentoGSAModel() {
		setTitolo("Consulta Movimento - SubAccertamento");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
