/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe base di consultazione per la richiesta economale. Modulo GEN
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 12/10/2015
 */
public class ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleFINModel extends ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel{
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 2588330700775184920L;

	/** Costruttore vuoto di default */
	public ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleFINModel() {
		super();
		setTitolo("Consulta Rendiconto Richiesta");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

}
