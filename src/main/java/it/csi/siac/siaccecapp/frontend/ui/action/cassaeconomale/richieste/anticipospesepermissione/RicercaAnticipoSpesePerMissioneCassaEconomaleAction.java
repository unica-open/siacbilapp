/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseRicercaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.RicercaAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;

/**
 * Classe di action per la ricerca dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 * @version 1.0.1 - 02/02/2015 - rifattorizzazione
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaAnticipoSpesePerMissioneCassaEconomaleAction extends BaseRicercaRichiestaEconomaleAction<RicercaAnticipoSpesePerMissioneCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -438215972404250966L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Aggiungo il breadcrumb
		return super.execute();
	}

	@Override
	protected TipoRichiestaEconomale findTipoRichiestaEconomale(List<TipoRichiestaEconomale> lista) {
		return ComparatorUtils.findByCodice(lista, BilConstants.CODICE_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE.getConstant());
	}

	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_RICERCA, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_PER_MISSIONE_ABILITA};
	}
}
