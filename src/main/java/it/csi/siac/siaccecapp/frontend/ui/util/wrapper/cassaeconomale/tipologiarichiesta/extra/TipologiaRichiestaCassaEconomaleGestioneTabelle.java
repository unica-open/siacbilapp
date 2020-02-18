/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.extra;

import java.util.List;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Tipologia di richiesta per la cassa economale: azioni extra della gestione tabelle.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/12/2014
 *
 */
public class TipologiaRichiestaCassaEconomaleGestioneTabelle extends TipologiaRichiestaCassaEconomaleExtra {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5823618854058762978L;

	/**
	 * Costruttore a partire dalle azioni consentite all'utente.
	 * 
	 * @param azioniConsentite le azioni consentite
	 */
	public TipologiaRichiestaCassaEconomaleGestioneTabelle(List<AzioneConsentita> azioniConsentite) {
		super();
		
		populateTipoRichiestaEconomale("Gestione tabelle");
		addAbilitazioneRichiestaCassaEconomale("tabella tipi giustificativi", "cassaEconomaleGestioneTabelleTipiGiustificativi.do", azioniConsentite,
				AzioniConsentite.CASSA_ECONOMALE_GESTIONE_TABELLA_TIPI_GIUSTIFICATIVI);
		addAbilitazioneRichiestaCassaEconomale("tabella operazioni di cassa", "cassaEconomaleGestioneTabelleTipiOperazioniDiCassa.do", azioniConsentite,
				AzioniConsentite.CASSA_ECONOMALE_GESTIONE_TABELLA_OPERAZIONI_CASSA);
	}
	
}
