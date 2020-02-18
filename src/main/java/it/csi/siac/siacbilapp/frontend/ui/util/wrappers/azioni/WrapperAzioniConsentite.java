/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni;

import java.util.List;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Classe di utilit&agrave; per la gestione delle azioni consentite all'utente. Wrappa i var&icirc; booleani rappresentati l'ideoneit&agrave; all'esecuzione delle azioni.
 * 
 * @author Luciano Gallo
 * @author Marchino Alessandro
 * 
 * @version 1.0.0 23/09/2013
 * @version 1.0.0 23/09/2014 - tolto il default ai campi
 *
 */
public class WrapperAzioniConsentite {
	
	private boolean aggiornaAbilitato;
	private boolean annullaAbilitato;
	private boolean consultaAbilitato;
	private boolean eliminaAbilitato;
	
	/** Nome della action per l'uscita previsione */
	public static final String ACTION_NAME_USCITA_PREVISIONE = "UscitaPrevisione";
	/** Nome della action per l'uscita gestione */
	public static final String ACTION_NAME_USCITA_GESTIONE = "UscitaGestione";
	/** Nome della action per l'entrata previsione */
	public static final String ACTION_NAME_ENTRATA_PREVISIONE = "EntrataPrevisione";
	/** Nome della action per l'entrata gestione */
	public static final String ACTION_NAME_ENTRATA_GESTIONE = "EntrataGestione";
	
	/** Costruttore vuoto di default. Rende disponibile solo la consultazione */
	public WrapperAzioniConsentite() {
		super();
		this.consultaAbilitato = true;
	}
	
	/**
	 * Costruttore a partire dalle azioni consentite.
	 * 
	 * @param actionName            il nome dell'azione in cui controllare la profilazione
	 * @param listaAzioniConsentite la lista delle azioni consentite all'utente
	 */
	public WrapperAzioniConsentite(String actionName, List<AzioneConsentita> listaAzioniConsentite) {
		this();
		this.aggiornaAbilitato = controlloAggiornaAbilitato(actionName, listaAzioniConsentite);
		this.annullaAbilitato = controlloAnnullaAbilitato(actionName, listaAzioniConsentite);
		this.eliminaAbilitato = controlloEliminaAbilitato(actionName, listaAzioniConsentite);
	}

	/**
	 * @return the aggiornaAbilitato
	 */
	public boolean isAggiornaAbilitato() {
		return aggiornaAbilitato;
	}

	/**
	 * @param aggiornaAbilitato the aggiornaAbilitato to set
	 */
	public void setAggiornaAbilitato(boolean aggiornaAbilitato) {
		this.aggiornaAbilitato = aggiornaAbilitato;
	}

	/**
	 * @return the annullaAbilitato
	 */
	public boolean isAnnullaAbilitato() {
		return annullaAbilitato;
	}

	/**
	 * @param annullaAbilitato the annullaAbilitato to set
	 */
	public void setAnnullaAbilitato(boolean annullaAbilitato) {
		this.annullaAbilitato = annullaAbilitato;
	}

	/**
	 * @return the consultaAbilitato
	 */
	public boolean isConsultaAbilitato() {
		return consultaAbilitato;
	}

	/**
	 * @param consultaAbilitato the consultaAbilitato to set
	 */
	public void setConsultaAbilitato(boolean consultaAbilitato) {
		this.consultaAbilitato = consultaAbilitato;
	}

	/**
	 * @return the eliminaAbilitato
	 */
	public boolean isEliminaAbilitato() {
		return eliminaAbilitato;
	}

	/**
	 * @param eliminaAbilitato the eliminaAbilitato to set
	 */
	public void setEliminaAbilitato(boolean eliminaAbilitato) {
		this.eliminaAbilitato = eliminaAbilitato;
	}
	
	/* Metodi di utilita' interni */
	
	/**
	 * Controlla che l'azione di aggiornamento sia consentita all'utente.
	 * 
	 * @param actionName            il nome dell'azione su cui effettuare la profilazione
	 * @param listaAzioniConsentite la lista delle azioni consentite all'utente
	 * 
	 * @return <code>true</code> se all'utente &eacute; consentito aggiornare; <code>false</code> altrimenti 
	 */
	private final boolean controlloAggiornaAbilitato(String actionName, List<AzioneConsentita> listaAzioniConsentite) {
		if(listaAzioniConsentite == null) {
			return false;
		}
		for (AzioneConsentita az: listaAzioniConsentite) {
			if(controlloAzione(actionName, az, "RelazioneAttoCapitolo", AzioniConsentite.RELAZIONE_ATTO_CAPITOLO_AGGIORNA)
					|| controlloAzione(actionName, az, ACTION_NAME_USCITA_PREVISIONE, AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_AGGIORNA)
					|| controlloAzione(actionName, az, ACTION_NAME_USCITA_GESTIONE, AzioniConsentite.CAPITOLO_USCITA_GESTIONE_AGGIORNA)
					|| controlloAzione(actionName, az, ACTION_NAME_ENTRATA_PREVISIONE, AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_AGGIORNA)
					|| controlloAzione(actionName, az, ACTION_NAME_ENTRATA_GESTIONE, AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_AGGIORNA)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se l'azione rispetta i criteri forniti.
	 * 
	 * @param actionName              il nome dell'azione
	 * @param azioneConsentita        l'azione consentita
	 * @param actionNameToCheck       l'azione da controllare
	 * @param azioniConsentiteToCheck l'azione consentita da controllare
	 * 
	 * @return <code>true</code> se l'azione &eacute; consentita; <code>false</code> in caso contrario
	 */
	private boolean controlloAzione(String actionName, AzioneConsentita azioneConsentita, String actionNameToCheck, AzioniConsentite azioniConsentiteToCheck) {
		return actionNameToCheck.equals(actionName) && azioneConsentita.getAzione().getNome().equalsIgnoreCase(azioniConsentiteToCheck.getNomeAzione());
	}
	
	/**
	 * Controlla che l'azione di annullamento sia consentita all'utente.
	 * 
	 * @param actionName            il nome dell'azione su cui effettuare la profilazione
	 * @param listaAzioniConsentite la lista delle azioni consentite all'utente
	 * 
	 * @return <code>true</code> se all'utente &eacute; consentito aggiornare; <code>false</code> altrimenti 
	 */
	private final boolean controlloAnnullaAbilitato(String actionName, List<AzioneConsentita> listaAzioniConsentite) {
		if(listaAzioniConsentite == null) {
			return false;
		}
		for (AzioneConsentita az: listaAzioniConsentite) {
			if(controlloAzione(actionName, az, "RelazioneAttoCapitolo", AzioniConsentite.RELAZIONE_ATTO_CAPITOLO_ANNULLA)
					|| controlloAzione(actionName, az, ACTION_NAME_USCITA_PREVISIONE, AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_ANNULLA)
					|| controlloAzione(actionName, az, ACTION_NAME_USCITA_GESTIONE, AzioniConsentite.CAPITOLO_USCITA_GESTIONE_ANNULLA)
					|| controlloAzione(actionName, az, ACTION_NAME_ENTRATA_PREVISIONE, AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_ANNULLA)
					|| controlloAzione(actionName, az, ACTION_NAME_ENTRATA_GESTIONE, AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_ANNULLA)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla che l'azione di eliminazione sia consentita all'utente.
	 * 
	 * @param actionName            il nome dell'azione su cui effettuare la profilazione
	 * @param listaAzioniConsentite la lista delle azioni consentite all'utente
	 * 
	 * @return <code>true</code> se all'utente &eacute; consentito aggiornare; <code>false</code> altrimenti 
	 */
	private final boolean controlloEliminaAbilitato(String actionName, List<AzioneConsentita> listaAzioniConsentite) {
		if(listaAzioniConsentite == null) {
			return false;
		}
		for (AzioneConsentita az: listaAzioniConsentite) {
			if(controlloAzione(actionName, az, ACTION_NAME_USCITA_PREVISIONE, AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_ELIMINA)
					|| controlloAzione(actionName, az, ACTION_NAME_USCITA_GESTIONE, AzioniConsentite.CAPITOLO_USCITA_GESTIONE_ELIMINA)
					|| controlloAzione(actionName, az, ACTION_NAME_ENTRATA_PREVISIONE, AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_ELIMINA)
					|| controlloAzione(actionName, az, ACTION_NAME_ENTRATA_GESTIONE, AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_ELIMINA)) {
				return true;
			}
		}
		return false;
	}
	
}
