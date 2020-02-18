/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione per la Nota Ccredito di Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2016
 *
 */
public class ConsultaRegistrazioneMovFinNotaCreditoEntrataFINModel extends ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8649535655493749489L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinNotaCreditoEntrataFINModel() {
		setTitolo("Consulta Movimento - Nota Credito di Entrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
