/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra.TipologiaRichiestaCassaEconomaleCassa;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra.TipologiaRichiestaCassaEconomaleGestioneTabelle;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra.TipologiaRichiestaCassaEconomaleOperazioniCassa;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard.TipologiaRichiestaCassaEconomaleAnticipoPerTrasfertaDipendenti;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard.TipologiaRichiestaCassaEconomaleAnticipoSpese;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard.TipologiaRichiestaCassaEconomaleAnticipoSpesePerMissione;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard.TipologiaRichiestaCassaEconomalePagamento;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard.TipologiaRichiestaCassaEconomalePagamentoFatture;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.standard.TipologiaRichiestaCassaEconomaleRimborsoSpese;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Factory per la tipologia di richiesta per la cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/12/2014
 *
 */
public final class TipologiaRichiestaCassaEconomaleFactory {
	
	/** Non instanziare la classe */
	private TipologiaRichiestaCassaEconomaleFactory() {
	}

	/**
	 * Creazione delle operazioni extra per le richieste della cassa economale.
	 * 
	 * @param azioniConsentite le azioni consentite all'utente
	 * 
	 * @return la lista delle operazioni extra per l'utente
	 */
	public static List<TipologiaRichiestaCassaEconomale> creaOperazioniExtra(List<AzioneConsentita> azioniConsentite) {
		List<TipologiaRichiestaCassaEconomale> result = new ArrayList<TipologiaRichiestaCassaEconomale>();
		// Creo le operazioni
		TipologiaRichiestaCassaEconomale cassa = new TipologiaRichiestaCassaEconomaleCassa(azioniConsentite);
		TipologiaRichiestaCassaEconomale operazioniCassa = new TipologiaRichiestaCassaEconomaleOperazioniCassa(azioniConsentite);
		// Non piu' presenti liquidazioni per jira 1985
		TipologiaRichiestaCassaEconomale gestioneTabelle = new TipologiaRichiestaCassaEconomaleGestioneTabelle(azioniConsentite);
		
		// Aggiungo le operazioni
		result.add(cassa);
		result.add(operazioniCassa);
		result.add(gestioneTabelle);
		
		return result;
	}

	/**
	 * Creazione delle tipologie di operazione per la cassa economale
	 * 
	 * @param azioniConsentite le azioni consentite all'utente
	 * 
	 * @return la lista delle operazioni fake per l'utente
	 */
	public static List<TipologiaRichiestaCassaEconomale> creaTipologieOperazione(List<AzioneConsentita> azioniConsentite) {
		List<TipologiaRichiestaCassaEconomale> result = new ArrayList<TipologiaRichiestaCassaEconomale>();
		
		// Creo le operazioni
		TipologiaRichiestaCassaEconomale rimborsoSpese = new TipologiaRichiestaCassaEconomaleRimborsoSpese(azioniConsentite);
		TipologiaRichiestaCassaEconomale pagamentoFatture = new TipologiaRichiestaCassaEconomalePagamentoFatture(azioniConsentite);
		TipologiaRichiestaCassaEconomale anticipoSpese = new TipologiaRichiestaCassaEconomaleAnticipoSpese(azioniConsentite);
		TipologiaRichiestaCassaEconomale anticipoPerTrasfertaDipendenti = new TipologiaRichiestaCassaEconomaleAnticipoPerTrasfertaDipendenti(azioniConsentite);
		TipologiaRichiestaCassaEconomale anticipoSpesePerMissione = new TipologiaRichiestaCassaEconomaleAnticipoSpesePerMissione(azioniConsentite);
		TipologiaRichiestaCassaEconomale pagamento = new TipologiaRichiestaCassaEconomalePagamento(azioniConsentite);
		
		// Aggiungo le operazioni
		result.add(rimborsoSpese);
		result.add(pagamentoFatture);
		result.add(anticipoSpese);
		result.add(anticipoPerTrasfertaDipendenti);
		result.add(anticipoSpesePerMissione);
		result.add(pagamento);
		
		return result;
	}

}
