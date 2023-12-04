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
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAnticipoSpeseCassaEconomaleAjaxAction extends RisultatiRicercaRichiestaEconomaleAjaxAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1869966435003492015L;

	@Override
	protected boolean gestisciAggiornamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return isStatoOperativoPrenotataEvasaOrDaRendicontare(instance)
				&& !instance.hasRendiconto()
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}

	@Override
	protected boolean gestisciAnnullamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return isStatoOperativoPrenotataEvasaOrDaRendicontare(instance)
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}

	@Override
	protected boolean gestisciConsultazione(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// SIAC-5623: aggiunto controllo abilitazione
//		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_RICERCA, listaAzioniConsentite);
		return AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_RICERCA, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}
	
	@Override
	protected boolean gestisciRendicontazione(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return instance.isStatoOperativoDaRendicontare()
				&& !instance.hasRendiconto()
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_INSERISCI_RENDICONTO, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_INSERISCI_RENDICONTO, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}
	
	@Override
	protected boolean gestisciAggiornamentoRendiconto(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return instance.hasRendiconto()
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA_RENDICONTO, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_AGGIORNA_RENDICONTO, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}
	
	@Override
	protected boolean gestisciConsultazioneRendiconto(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return instance.hasRendiconto()
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_SPESE_CONSULTA_RENDICONTO, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_CONSULTA_RENDICONTO, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}
	
	@Override
	protected boolean gestisciStampaRicevutaRendiconto(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return instance.hasRendiconto()
				// SIAC-5623: aggiunto controllo abilitazione
//				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_STAMPA_RICEVUTA, listaAzioniConsentite);
				&& AzioniConsentiteFactory.isConsentitoAll(listaAzioniConsentite, AzioneConsentitaEnum.CASSA_ECONOMALE_STAMPA_RICEVUTA, AzioneConsentitaEnum.CASSA_ECONOMALE_ANTICIPO_SPESE_ABILITA);
	}
	
	@Override
	protected String ottieniBaseActionUrl() {
		return "risultatiRicercaAnticipoSpeseCassaEconomale";
	}

}
