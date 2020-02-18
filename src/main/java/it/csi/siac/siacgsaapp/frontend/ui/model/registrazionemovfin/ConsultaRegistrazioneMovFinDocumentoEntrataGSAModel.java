/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione del documento di entrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinDocumentoEntrataGSAModel extends ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 770067097803009856L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinDocumentoEntrataGSAModel() {
		setTitolo("Consulta Movimento - Documento di Entrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
