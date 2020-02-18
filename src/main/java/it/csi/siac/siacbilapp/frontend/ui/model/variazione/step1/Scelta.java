/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step1;

import it.csi.siac.siaccorser.model.AzioneConsentita;

/** L'Enum delle scelte possibili */
public enum Scelta {
	/** Scelta per proseguire nella variazione di importi */
	IMPORTI("OP-GESC001-insVarImporti"),
	/** Scelta per proseguire nella variazione di codifiche */
	CODIFICHE("OP-GESC001-insVarCodifiche");
	
	private String nomeAzione;

	private Scelta(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	
	/**
	 * Controlla se la scelta &eacute; consentita in base alla profilazione dell'utente.
	 * 
	 * @param azioniConsentite le azioni consentite all'utente
	 * @return <code>true</code> se la scelta &eacute; consentita; <code>false</code> altrimenti
	 */
	public boolean isConsentita(Iterable<AzioneConsentita> azioniConsentite){
		for(AzioneConsentita ac : azioniConsentite){
			if(nomeAzione.equals(ac.getAzione().getNome())){
				return true;
			}
		}
		return false;
	}
}