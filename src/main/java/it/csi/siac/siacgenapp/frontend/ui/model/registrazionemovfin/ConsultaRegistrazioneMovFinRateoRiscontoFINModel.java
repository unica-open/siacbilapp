/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRateoRiscontoBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe base di consultazione per ratei e risconti. Modulo GEN
 * 
 * @author Valentina
 * @version 1.1.0 - 25/07/2016
 */
public class ConsultaRegistrazioneMovFinRateoRiscontoFINModel extends ConsultaRegistrazioneMovFinRateoRiscontoBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3597837964160950629L;

	/** Costruttore vuoto di default */
	public ConsultaRegistrazioneMovFinRateoRiscontoFINModel() {
		super();
		setTitolo("Consulta Ratei e risconti");
	}

	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}

	
}
