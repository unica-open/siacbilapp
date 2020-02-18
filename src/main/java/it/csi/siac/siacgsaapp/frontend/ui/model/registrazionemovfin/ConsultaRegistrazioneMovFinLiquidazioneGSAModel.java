/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinLiquidazioneBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione della Liquidazione. Modulo GEN
 * 
 * @author Valentina Triolo
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.1.0 - 07/10/2015 - gestioen GEN/GSA
 *
 */
public class ConsultaRegistrazioneMovFinLiquidazioneGSAModel extends ConsultaRegistrazioneMovFinLiquidazioneBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6731629619495081477L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinLiquidazioneGSAModel() {
		setTitolo("Consulta Movimento - Liquidazione");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
