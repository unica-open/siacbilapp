/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Consultazione della registrazione del documento di entrata. Modulo GEN
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 07/10/2015 - gestione GEN/GSA
 *
 */
public class ConsultaRegistrazioneMovFinDocumentoEntrataFINModel extends ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2105363946077354608L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinDocumentoEntrataFINModel() {
		setTitolo("Consulta Movimento - Documento di Entrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
