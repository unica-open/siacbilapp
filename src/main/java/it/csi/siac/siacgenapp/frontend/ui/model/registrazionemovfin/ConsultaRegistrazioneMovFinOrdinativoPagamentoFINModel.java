/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione dell'Ordinativo di Pagamento. Modulo GEN
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 07/10/2015 - gestione GEN/GSA
 *
 */
public class ConsultaRegistrazioneMovFinOrdinativoPagamentoFINModel extends ConsultaRegistrazioneMovFinOrdinativoPagamentoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8848881228569967010L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinOrdinativoPagamentoFINModel() {
		setTitolo("Consulta Movimento - Ordinativo di Pagamento");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
