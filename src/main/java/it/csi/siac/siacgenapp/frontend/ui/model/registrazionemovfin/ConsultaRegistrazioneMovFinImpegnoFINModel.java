/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinImpegnoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione del Documento di Spesa. Modulo GEN
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 07/10/2015 - gestione GEN/GSA
 *
 */
public class ConsultaRegistrazioneMovFinImpegnoFINModel extends ConsultaRegistrazioneMovFinImpegnoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9016249167332363498L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinImpegnoFINModel() {
		setTitolo("Consulta Movimento - Impegno");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
