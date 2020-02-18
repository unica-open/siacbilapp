/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinAccertamentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione dell'Accertamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinAccertamentoGSAModel extends ConsultaRegistrazioneMovFinAccertamentoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7854477897449719060L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinAccertamentoGSAModel() {
		setTitolo("Consulta Movimento - Accertamento");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
