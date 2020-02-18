/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe base di consultazione per la richiesta economale(rendiconto). Modulo GSA
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 12/10/2015
 */
public class ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleGSAModel extends ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2148410054214178777L;

	/** Costruttore vuoto di default */
	public ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleGSAModel() {
		super();
		setTitolo("Consulta Rendiconto Richiesta");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
