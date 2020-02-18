/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione per la Nota Credito di Spesa.
 * 
 * @author Marchino Alessandro
 */
public class ConsultaRegistrazioneMovFinNotaCreditoSpesaGSAModel extends ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8372688082926221201L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinNotaCreditoSpesaGSAModel() {
		setTitolo("Consulta Movimento - Nota Credito di Spesa");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
