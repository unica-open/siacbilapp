/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe base di consultazione per la richiesta economale. Modulo GEN
 * 
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2015
 * @version 1.1.0 - 08/10/2015 - gestione GEN/GSA
 */
public class ConsultaRegistrazioneMovFinRichiestaEconomaleFINModel extends ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3597837964160950629L;

	/** Costruttore vuoto di default */
	public ConsultaRegistrazioneMovFinRichiestaEconomaleFINModel() {
		super();
		setTitolo("Consulta Richiesta Economale");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
