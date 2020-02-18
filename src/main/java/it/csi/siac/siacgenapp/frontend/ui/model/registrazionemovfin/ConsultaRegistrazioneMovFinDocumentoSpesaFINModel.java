/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoSpesaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione per il Documento di Spesa.
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 06/10/2015 - gestione GEN/GSA
 *
 */
public class ConsultaRegistrazioneMovFinDocumentoSpesaFINModel extends ConsultaRegistrazioneMovFinDocumentoSpesaBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -197452792674878680L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinDocumentoSpesaFINModel() {
		setTitolo("Consulta Movimento - Documento di Spesa");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
