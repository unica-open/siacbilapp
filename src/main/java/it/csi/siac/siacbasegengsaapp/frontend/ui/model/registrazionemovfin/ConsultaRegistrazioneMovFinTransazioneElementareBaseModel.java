/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinTransazioneElementareHelper;
import it.csi.siac.siacfinser.model.TransazioneElementare;

/**
 * Classe base di model per la consultazione della transazione elementare.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <T> la tipizzazione della transazione elementare
 * @param <H> la tipizzazione dell'helper
 *
 */
public abstract class ConsultaRegistrazioneMovFinTransazioneElementareBaseModel<T extends TransazioneElementare, H extends ConsultaRegistrazioneMovFinTransazioneElementareHelper<T>>
		extends ConsultaRegistrazioneMovFinBaseModel<T, H>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1660649239795042903L;
	
	@Override
	public String getDatiCreazioneModifica() {
		return consultazioneHelper != null ? consultazioneHelper.getDatiCreazioneModifica() : "";
	}

}
