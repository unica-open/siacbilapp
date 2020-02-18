/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Classe statica per l'ottenimento delle informazioni sull'azione consentita.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 14/11/2013
 *
 */
public final class AzioniConsentiteFactory {
	
	private static final Map<String, List<AzioniConsentite>> MAPPATURA_AZIONI_CONSENTITE = new HashMap<String, List<AzioniConsentite>>();
	// Inizializzazione della mappatura
	static {
		List<AzioniConsentite> listaStringheUscitaPrevisione = Arrays.asList(AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_AGGIORNA, 
				AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_ANNULLA, AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_ELIMINA, 
				AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_INSERISCI);
		List<AzioniConsentite> listaStringheUscitaGestione = Arrays.asList(AzioniConsentite.CAPITOLO_USCITA_GESTIONE_AGGIORNA, 
				AzioniConsentite.CAPITOLO_USCITA_GESTIONE_ANNULLA, AzioniConsentite.CAPITOLO_USCITA_GESTIONE_ELIMINA, 
				AzioniConsentite.CAPITOLO_USCITA_GESTIONE_INSERISCI);
		List<AzioniConsentite> listaStringheEntrataPrevisione = Arrays.asList(AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_AGGIORNA, 
				AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_ANNULLA, AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_ELIMINA, 
				AzioniConsentite.CAPITOLO_ENTRATA_PREVISIONE_INSERISCI);
		List<AzioniConsentite> listaStringheEntrataGestione = Arrays.asList(AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_AGGIORNA, 
				AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_ANNULLA, AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_ELIMINA, 
				AzioniConsentite.CAPITOLO_ENTRATA_GESTIONE_INSERISCI);
		
		List<AzioniConsentite> listaStringheVincolo = Arrays.asList(AzioniConsentite.VINCOLO_CONSULTA, AzioniConsentite.VINCOLO_GESTISCI);
		
		List<AzioniConsentite> listaStringheCronoProgrammaNelProgetto = Arrays.asList(AzioniConsentite.CRONO_NEL_PROGETTO_CONSULTA, 
				AzioniConsentite.CRONO_NEL_PROGETTO_AGGIORNA);
		
		List<AzioniConsentite> listaStringheDocumentoSpesa = Arrays.asList(AzioniConsentite.DOCUMENTO_SPESA_CONSULTA, 
				AzioniConsentite.DOCUMENTO_SPESA_AGGIORNA, AzioniConsentite.DOCUMENTO_SPESA_AGGIORNA_QUIENTANZA, AzioniConsentite.DOCUMENTO_SPESA_ANNULLA, 
				AzioniConsentite.DOCUMENTO_SPESA_GESTIONE_ACQUISTI, AzioniConsentite.DOCUMENTO_SPESA_DECENTRATO, AzioniConsentite.DOCUMENTO_SPESA_LIMITA_DATI_FEL);
		List<AzioniConsentite> listaStringheDocumentoEntrata = Arrays.asList(AzioniConsentite.DOCUMENTO_ENTRATA_CONSULTA, 
				AzioniConsentite.DOCUMENTO_ENTRATA_AGGIORNA, AzioniConsentite.DOCUMENTO_ENTRATA_AGGIORNA_QUIENTANZA, AzioniConsentite.DOCUMENTO_ENTRATA_ANNULLA,
				AzioniConsentite.DOCUMENTO_ENTRATA_DECENTRATO);

		List<AzioniConsentite> listaStringheCausaleSpesa = Arrays.asList(AzioniConsentite.CAUSALE_SPESA_CONSULTA, AzioniConsentite.CAUSALE_SPESA_GESTISCI);
		List<AzioniConsentite> listaStringheCausaleEntrata = Arrays.asList(AzioniConsentite.CAUSALE_ENTRATA_CONSULTA, AzioniConsentite.CAUSALE_ENTRATA_GESTISCI);
		
		List<AzioniConsentite> listaStringhePreDocumentoSpesa = Arrays.asList(AzioniConsentite.PREDOCUMENTO_SPESA_INSERISCI, 
				AzioniConsentite.PREDOCUMENTO_SPESA_INSERISCI_DECENTRATO, AzioniConsentite.PREDOCUMENTO_SPESA_AGGIORNA, 
				AzioniConsentite.PREDOCUMENTO_SPESA_AGGIORNA_DECENTRATO, AzioniConsentite.PREDOCUMENTO_SPESA_CONSULTA);
		List<AzioniConsentite> listaStringhePreDocumentoEntrata = Arrays.asList(AzioniConsentite.PREDOCUMENTO_ENTRATA_INSERISCI, 
				AzioniConsentite.PREDOCUMENTO_ENTRATA_INSERISCI_DECENTRATO, AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA, 
				AzioniConsentite.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, AzioniConsentite.PREDOCUMENTO_ENTRATA_CONSULTA);
		

		MAPPATURA_AZIONI_CONSENTITE.put(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.name(), listaStringheUscitaPrevisione);
		MAPPATURA_AZIONI_CONSENTITE.put(TipoCapitolo.CAPITOLO_USCITA_GESTIONE.name(), listaStringheUscitaGestione);
		MAPPATURA_AZIONI_CONSENTITE.put(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.name(), listaStringheEntrataPrevisione);
		MAPPATURA_AZIONI_CONSENTITE.put(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.name(), listaStringheEntrataGestione);
		
		MAPPATURA_AZIONI_CONSENTITE.put("Vincolo", listaStringheVincolo);
		
		MAPPATURA_AZIONI_CONSENTITE.put("CronoprogrammaNelProgetto", listaStringheCronoProgrammaNelProgetto);
		
		MAPPATURA_AZIONI_CONSENTITE.put("DocumentoSpesa", listaStringheDocumentoSpesa);
		MAPPATURA_AZIONI_CONSENTITE.put("DocumentoEntrata", listaStringheDocumentoEntrata);
		
		MAPPATURA_AZIONI_CONSENTITE.put("CausaleSpesa", listaStringheCausaleSpesa);
		MAPPATURA_AZIONI_CONSENTITE.put("CausaleEntrata", listaStringheCausaleEntrata);
		
		MAPPATURA_AZIONI_CONSENTITE.put("PreDocumentoSpesa", listaStringhePreDocumentoSpesa);
		MAPPATURA_AZIONI_CONSENTITE.put("PreDocumentoEntrata", listaStringhePreDocumentoEntrata);
	}
	
	/** Non permettere l'instanziazione della classe */
	private AzioniConsentiteFactory() {
	}
	
	/**
	 * Valuta se l'aggiornamento &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param tipoCapitolo          il tipo di Capitolo
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentito(TipoCapitolo tipoCapitolo, List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get(tipoCapitolo.name()).get(0);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'annullamento &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param tipoCapitolo          il tipo di Capitolo
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentito(TipoCapitolo tipoCapitolo, List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get(tipoCapitolo.name()).get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'eliminazione &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param tipoCapitolo          il tipo di Capitolo
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'eliminazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isEliminaConsentito(TipoCapitolo tipoCapitolo, List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get(tipoCapitolo.name()).get(2);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'inserimento &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param tipoCapitolo          il tipo di Capitolo
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'inserimento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isInserisciConsentito(TipoCapitolo tipoCapitolo, List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get(tipoCapitolo.name()).get(3);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento del vincolo &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoVincolo(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("Vincolo").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'annullamento del vincolo &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentitoVincolo(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("Vincolo").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione del vincolo &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoVincolo(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azioneConsultazioneVincolo = MAPPATURA_AZIONI_CONSENTITE.get("Vincolo").get(0);
		AzioniConsentite azioneGestioneVincolo = MAPPATURA_AZIONI_CONSENTITE.get("Vincolo").get(1);
		return Boolean.valueOf(isConsentito(azioneConsultazioneVincolo, listaAzioniConsentite) ||
				isConsentito(azioneGestioneVincolo, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento del  cronoprogramma nell'aggiorna progetto &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoCronoprogrammaNelProgetto(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CronoprogrammaNelProgetto").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'annullamento  del  cronoprogramma nell'aggiorna progetto  &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentitoCronoprogrammaNelProgetto(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CronoprogrammaNelProgetto").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	
	
	/**
	 * Valuta se la consultazione  del  cronoprogramma nell'aggiorna progetto &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoCronoprogrammaNelProgetto(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azioneConsultazioneCronopNelProg = MAPPATURA_AZIONI_CONSENTITE.get("CronoprogrammaNelProgetto").get(0);
		return Boolean.valueOf(isConsentito(azioneConsultazioneCronopNelProg, listaAzioniConsentite));
	}
	
	
	/**
	 * Valuta se l'aggiornamento del documento &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoSpesa").get(1);
		AzioniConsentite azioneDecentrata = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoSpesa").get(5);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite) || isConsentito(azioneDecentrata, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento del documentoSpesa &eacute; Quietanza consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento del documentoSpesa &eacute; Quietanza &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoDocumentoSpesaQuietanza(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoSpesa").get(2);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	
	/**
	 * Valuta se l'annullamento del documentoSpesa &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentitoDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoSpesa").get(3);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione del documento spesa &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoSpesa").get(0);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la gestione delle procedure acquisti del documento spesa sia consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isGestioneProceduraAcquistiConsentitoDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoSpesa").get(4);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	
	/**
	 * Valuta se l'aggiornamento del DocumentoEntrata &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoDocumentoEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoEntrata").get(1);
		AzioniConsentite azioneDecentrata = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoEntrata").get(4);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite) || isConsentito(azioneDecentrata, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento del DocumentoEntrata &eacute; Quietanza consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento del DocumentoEntrata &eacute; Quietanza &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoDocumentoEntrataQuietanza(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoEntrata").get(2);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	
	/**
	 * Valuta se l'annullamento del DocumentoEntrata &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentitoDocumentoEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoEntrata").get(3);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione del DocumentoEntrata &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoDocumentoEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("DocumentoEntrata").get(0);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}

	
	/**
	 * Valuta se la gestione IVA sia consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la gestione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isGestioneIvaConsentito(List<AzioneConsentita> listaAzioniConsentite) {
		return Boolean.valueOf(isConsentito(AzioniConsentite.GESTIONE_IVA, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la gestione del documento decentrato sia consentita dalla lista delle azioni consentite, e in tal caso restituisce l'azione creata.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return l'Azione Consentita relativa al documento decentrato, se presente; <code>null</code> in caso contrario
	 */
	public static AzioneConsentita findAzioneConsentitaDecentratoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = AzioniConsentite.DOCUMENTO_SPESA_DECENTRATO;
		for(AzioneConsentita az : listaAzioniConsentite) {
			if(az.getAzione().getNome().equalsIgnoreCase(azione.getNomeAzione())) {
				return az;
			}
		}
		return null;
	}
	
	/**
	 * Valuta se l'aggiornamento della CausaleSpesa &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoCausaleSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CausaleSpesa").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	
	/**
	 * Valuta se l'annullamento della CausaleSpesa &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentitoCausaleSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CausaleSpesa").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione della CausaleSpesa &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoCausaleSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CausaleSpesa").get(0);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento della CausaleEntrata &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoCausaleEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CausaleEntrata").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	
	/**
	 * Valuta se l'annullamento della CausaleEntrata &eacute; consentito dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentito; <code>false</code> in caso contrario
	 */
	public static Boolean isAnnullaConsentitoCausaleEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CausaleEntrata").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione della CausaleEntrata &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoCausaleEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("CausaleEntrata").get(0);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione del PredocumentoSpesa &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoPreDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoSpesa").get(4);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'inserimento del PredocumentoSpesa &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isInserisciConsentitoPreDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoSpesa").get(0);
		AzioniConsentite azioneDec = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoSpesa").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite) || isConsentito(azioneDec, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento del PredocumentoSpesa &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoPreDocumentoSpesa(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoSpesa").get(2);
		AzioniConsentite azioneDec = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoSpesa").get(3);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite) || isConsentito(azioneDec, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se la consultazione del PredocumentoEntrata &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isConsultaConsentitoPreDocumentoEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoEntrata").get(4);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'inserimento del PredocumentoEntrata &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isInserisciConsentitoPreDocumentoEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoEntrata").get(0);
		AzioniConsentite azioneDec = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoEntrata").get(1);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite) || isConsentito(azioneDec, listaAzioniConsentite));
	}
	
	/**
	 * Valuta se l'aggiornamento del PredocumentoEntrata &eacute; consentita dalla lista delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static Boolean isAggiornaConsentitoPreDocumentoEntrata(List<AzioneConsentita> listaAzioniConsentite) {
		AzioniConsentite azione = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoEntrata").get(2);
		AzioniConsentite azioneDec = MAPPATURA_AZIONI_CONSENTITE.get("PreDocumentoEntrata").get(3);
		return Boolean.valueOf(isConsentito(azione, listaAzioniConsentite) || isConsentito(azioneDec, listaAzioniConsentite));
	}
	
	
	/* Metodi di utilita' */
	
	/**
	 * Controlla se una data azione sia consentita.
	 * 
	 * @param azione                l'azione da controllare
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'azione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static boolean isConsentito(AzioniConsentite azione, List<AzioneConsentita> listaAzioniConsentite) {
		for(AzioneConsentita az : listaAzioniConsentite) {
			if(az.getAzione().getNome().equalsIgnoreCase(azione.getNomeAzione())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla se una data azione sia consentita.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param azioni                le azioni da controllare
	 * 
	 * @return <code>true</code> se l'azione &eacute; consentita; <code>false</code> in caso contrario
	 */
	public static boolean isConsentitoAll(List<AzioneConsentita> listaAzioniConsentite, AzioniConsentite... azioni) {
		boolean consentito = !listaAzioniConsentite.isEmpty() && azioni != null && azioni.length > 0;
		for(int i = 0; consentito && i < azioni.length; i++) {
			AzioniConsentite azione = azioni[i];
			// Ripristino il booleano a false
			consentito = false;
			
			for(Iterator<AzioneConsentita> it = listaAzioniConsentite.iterator(); it.hasNext() && !consentito;) {
				AzioneConsentita az = it.next();
				if(az.getAzione().getNome().equalsIgnoreCase(azione.getNomeAzione())) {
					consentito = true;
				}
			}
		}
		return consentito;
	}

}
