/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinImpegnoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione del Documento di Spesa. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinImpegnoGSAModel extends ConsultaRegistrazioneMovFinImpegnoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9016249167332363498L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinImpegnoGSAModel() {
		setTitolo("Consulta Movimento - Impegno");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
