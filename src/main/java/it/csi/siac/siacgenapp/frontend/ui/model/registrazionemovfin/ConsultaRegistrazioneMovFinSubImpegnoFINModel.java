/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinSubImpegnoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione del SubImpegno. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinSubImpegnoFINModel extends ConsultaRegistrazioneMovFinSubImpegnoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8059915899587017735L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinSubImpegnoFINModel() {
		setTitolo("Consulta Movimento - SubImpegno");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
