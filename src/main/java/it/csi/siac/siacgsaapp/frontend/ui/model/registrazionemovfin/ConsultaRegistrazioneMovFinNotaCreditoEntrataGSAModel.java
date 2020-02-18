/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione per la Nota Ccredito di Entrata.
 * 
 * @author Marchino Alessandro
 */
public class ConsultaRegistrazioneMovFinNotaCreditoEntrataGSAModel extends ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -6484542491464770955L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinNotaCreditoEntrataGSAModel() {
		setTitolo("Consulta Movimento - Nota Credito di Entrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
