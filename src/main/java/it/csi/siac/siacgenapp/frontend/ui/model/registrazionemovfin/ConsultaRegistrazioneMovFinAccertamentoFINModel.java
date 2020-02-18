/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinAccertamentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione dell'Accertamento. Modulo GEN
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 07/10/2015 - gestione GEN/GSA
 *
 */
public class ConsultaRegistrazioneMovFinAccertamentoFINModel extends ConsultaRegistrazioneMovFinAccertamentoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4794188359426688393L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinAccertamentoFINModel() {
		setTitolo("Consulta Movimento - Accertamento");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
