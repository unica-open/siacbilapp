/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione dell'Ordinativo di Pagamento. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ConsultaRegistrazioneMovFinOrdinativoPagamentoGSAModel extends ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2439090459486819900L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinOrdinativoPagamentoGSAModel() {
		setTitolo("Consulta Movimento - Ordinativo di Pagamento");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
