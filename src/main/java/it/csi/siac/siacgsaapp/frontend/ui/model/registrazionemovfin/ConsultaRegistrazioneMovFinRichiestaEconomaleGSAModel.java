/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe base di consultazione per la richiesta economale. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
public class ConsultaRegistrazioneMovFinRichiestaEconomaleGSAModel extends ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1740475366359450673L;

	/** Costruttore vuoto di default */
	public ConsultaRegistrazioneMovFinRichiestaEconomaleGSAModel() {
		super();
		setTitolo("Consulta Richiesta Economale");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
