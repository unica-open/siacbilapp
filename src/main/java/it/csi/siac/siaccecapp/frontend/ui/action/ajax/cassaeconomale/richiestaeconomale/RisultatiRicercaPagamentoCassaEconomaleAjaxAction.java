/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.ajax.cassaeconomale.richiestaeconomale;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.richiestaeconomale.ElementoRichiestaEconomale;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Classe di action per i risultati di ricerca per il pagamento, gestione dell'AJAX.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 01/02/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPagamentoCassaEconomaleAjaxAction extends RisultatiRicercaRichiestaEconomaleAjaxAction {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -8880702118181892231L;

	@Override
	protected boolean gestisciAggiornamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return (instance.isStatoOperativoPrenotata()
				|| instance.isStatoOperativoEvasa()
				|| instance.isStatoOperativoDaRendicontare())
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_AGGIORNA, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_AGGIORNA, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_ABILITA);
	}

	@Override
	protected boolean gestisciAnnullamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// SIAC-5623: aggiunto controllo abilitazione
//		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_AGGIORNA, listaAzioniConsentite);
		return AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_AGGIORNA, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_ABILITA);
	}

	@Override
	protected boolean gestisciConsultazione(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// SIAC-5623: aggiunto controllo abilitazione
//		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_RICERCA, listaAzioniConsentite);
		return AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_RICERCA, AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_ABILITA);
	}
	
	

	@Override
	protected String ottieniBaseActionUrl() {
		return "risultatiRicercaPagamentoCassaEconomale";
	}

}
