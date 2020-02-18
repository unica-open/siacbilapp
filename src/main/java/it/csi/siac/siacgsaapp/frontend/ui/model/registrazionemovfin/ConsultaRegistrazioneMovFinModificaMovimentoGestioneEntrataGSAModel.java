/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per la consultazione della modifica del movimento di gestione di entrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/11/2015
 *
 */
public class ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataGSAModel extends ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataBaseModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8774816568717104760L;

	/** Costruttore vuoto di dafault */
	public ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataGSAModel() {
		setTitolo("Consulta Movimento - Modifica Movimento di Gestione di Entrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

}
