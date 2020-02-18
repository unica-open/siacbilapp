/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;


/**
 * Classe di model per la consultazione della richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 */
public class BaseConsultaRichiestaEconomaleModel extends BaseRiepilogoRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3104789250929020242L;
	
	@Override
	public CapitoloUscitaGestione getCapitoloMovimento() {
		return getRichiestaEconomale() != null && getRichiestaEconomale().getImpegno() != null ? getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione() : null;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRichiestaEconomale creaRequestRicercaDettaglioRichiestaEconomale() {
		return creaRequestRicercaDettaglioRichiestaEconomale(getRichiestaEconomale());
	}
	
}
