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
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.richiestaeconomale.ElementoRichiestaEconomale;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di action per i risultati di ricerca per l'anticipo spese , gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPagamentoFattureCassaEconomaleAjaxAction extends RisultatiRicercaRichiestaEconomaleAjaxAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 550353640266691944L;

	@Override
	protected boolean gestisciAggiornamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return (instance.isStatoOperativoPrenotata()
				|| instance.isStatoOperativoEvasa()
				|| instance.isStatoOperativoDaRendicontare())
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_FATTURE_AGGIORNA, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_ABILITA);
	}

	@Override
	protected boolean gestisciAnnullamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// SIAC-5623: aggiunto controllo abilitazione
//		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_FATTURE_AGGIORNA, listaAzioniConsentite);
		return AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_ABILITA);
	}

	@Override
	protected boolean gestisciConsultazione(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// SIAC-5623: aggiunto controllo abilitazione
//		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_PAGAMENTO_FATTURE_RICERCA, listaAzioniConsentite);
		return AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_ABILITA);
	}
	
	@Override
	protected String ottieniBaseActionUrl() {
		return "risultatiRicercaPagamentoFattureCassaEconomale";
	}

}
