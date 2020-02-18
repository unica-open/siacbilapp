/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione per la Nota Ccredito di Spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2016
 *
 */
public class ConsultaRegistrazioneMovFinNotaCreditoSpesaFINModel extends ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8649535655493749489L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinNotaCreditoSpesaFINModel() {
		setTitolo("Consulta Movimento - Nota Credito di Spesa");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
