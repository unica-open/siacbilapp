/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;


/**
 * Factory per il wrapping dei capitoli.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 27/08/2013
 *
 */
public final class ElementoCapitoloFactory {
	
	/** Stringa di utilit&agrave; per i campi non presenti */
	// Un tempo era "undefined"
	private static final String UNDEFINED = "";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCapitoloFactory() {
	}
	
	/* GESTIONE DEI CAPITOLI PUNTUALI */
	
	/**
	 * Wrapper per il Capitolo di Uscita Previsione.
	 *  
	 * @param capitolo    il capitolo da wrappare
	 * @param massivo     se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB se l'ente gestisce le UEB
	 * 
	 * @return il wrapper
	 */
	public static ElementoCapitolo getInstance(CapitoloUscitaPrevisione capitolo, boolean massivo, boolean gestioneUEB) {
		// Fallback in caso di null
		if(capitolo == null) {
			return null;
		}
		
		ElementoCapitolo wrapper = new ElementoCapitolo();
		
		// Carica i dati comuni
		popolaCampiComuni(wrapper, capitolo, massivo, gestioneUEB);
		
		// Popola i dati specifici
		
		// Campi da injettare
		StringBuilder classificazione = new StringBuilder();
		StringBuilder strutturaAmministrativoResponsabile = new StringBuilder();
		StringBuilder pianoDeiContiFinanziario =  new StringBuilder();
		StringBuilder pianoDeiContiVoce =  new StringBuilder();
		
		// Campi di utilita'
		Missione missione;
		Programma programma;
		TitoloSpesa titoloSpesa;
		Macroaggregato macroaggregato;
		StrutturaAmministrativoContabile strutturaAmministrativoContabile;
		ElementoPianoDeiConti elementoPianoDeiConti;
						
		// Classificazione 
		missione = capitolo.getMissione();
		programma = capitolo.getProgramma();
		titoloSpesa = capitolo.getTitoloSpesa();
		macroaggregato = capitolo.getMacroaggregato();
		
		classificazione.append(codificaToString(programma))
			.append("-")
			.append(codificaToString(macroaggregato));
		
		impostaImportiCapitolo(wrapper, capitolo.getImportiCapitoloUP(), capitolo.getListaImportiCapitoloUP());
		
		// Struttura amministrativo Contabile
		strutturaAmministrativoContabile = capitolo.getStrutturaAmministrativoContabile();
		strutturaAmministrativoResponsabile.append(strutturaAmministrativoContabile == null
				? UNDEFINED
				: strutturaAmministrativoContabile.getCodice() + "-" + strutturaAmministrativoContabile.getDescrizione());
		
		// Piano dei conti
		elementoPianoDeiConti = capitolo.getElementoPianoDeiConti();
		if (elementoPianoDeiConti != null) {
			pianoDeiContiFinanziario.append(elementoPianoDeiConti.getCodice());
			pianoDeiContiVoce.append(elementoPianoDeiConti.getDescrizione());
		} else {
			pianoDeiContiFinanziario.append(UNDEFINED);
			pianoDeiContiVoce.append(UNDEFINED);
		}
		
		// Injezione dei dati nel wrapper
		wrapper.setClassificazione(classificazione.toString());
		wrapper.setStruttAmmResp(strutturaAmministrativoResponsabile.toString());
		wrapper.setPdcFinanziario(pianoDeiContiFinanziario.toString());
		wrapper.setPdcVoce(pianoDeiContiVoce.toString());
		
		wrapper.setMissione(missione);
		wrapper.setProgramma(programma);
		wrapper.setTitoloSpesa(titoloSpesa);
		
		return wrapper;
	}
	
	/**
	 * Wrapper per il Capitolo di Uscita Gestione.
	 *  
	 * @param capitolo il capitolo da wrappare
	 * @param massivo  se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB se l'ente gestisce le UEB
	 * 
	 * @return il wrapper
	 */
	public static ElementoCapitolo getInstance(CapitoloUscitaGestione capitolo, boolean massivo, boolean gestioneUEB) {
		// Fallback in caso di null
		if(capitolo == null) {
			return null;
		}
				
		ElementoCapitolo wrapper = new ElementoCapitolo();
		
		// Carica i dati comuni
		popolaCampiComuni(wrapper, capitolo, massivo, gestioneUEB);
		
		// Popola i dati specifici
		
		// Campi da injettare
		StringBuilder classificazione = new StringBuilder();
		String strutturaAmministrativoResponsabile = "";
		String pianoDeiContiFinanziario = "";
		String pianoDeiContiVoce = "";
		
		// Campi di utilita'
		Missione missione;
		Programma programma;
		TitoloSpesa titoloSpesa;
		Macroaggregato macroaggregato;
		StrutturaAmministrativoContabile strutturaAmministrativoContabile;
		ElementoPianoDeiConti elementoPianoDeiConti;
						
		// Classificazione 
		missione = capitolo.getMissione();
		programma = capitolo.getProgramma();
		titoloSpesa = capitolo.getTitoloSpesa();
		macroaggregato = capitolo.getMacroaggregato();
		
		classificazione.append(codificaToString(programma));
		classificazione.append("-");
		classificazione.append(codificaToString(macroaggregato));
		
		impostaImportiCapitolo(wrapper, capitolo.getImportiCapitoloUG(), capitolo.getListaImportiCapitoloUG());
		impostaDisponibilita(wrapper, capitolo.getImportiCapitoloUG());
		
		// Struttura amministrativo Contabile
		strutturaAmministrativoContabile = capitolo.getStrutturaAmministrativoContabile();
		strutturaAmministrativoResponsabile = ((strutturaAmministrativoContabile == null) ? UNDEFINED : 
				(strutturaAmministrativoContabile.getCodice() + "-" + strutturaAmministrativoContabile.getDescrizione()));
		
		// Piano dei conti
		elementoPianoDeiConti = capitolo.getElementoPianoDeiConti();
		if (elementoPianoDeiConti != null) {
			pianoDeiContiFinanziario = elementoPianoDeiConti.getCodice();
			pianoDeiContiVoce = elementoPianoDeiConti.getDescrizione();
		} else {
			pianoDeiContiFinanziario = UNDEFINED;
			pianoDeiContiVoce = UNDEFINED;
		}
		
		// Injezione dei dati nel wrapper
		wrapper.setClassificazione(classificazione.toString());
		wrapper.setStruttAmmResp(strutturaAmministrativoResponsabile);
		wrapper.setPdcFinanziario(pianoDeiContiFinanziario);
		wrapper.setPdcVoce(pianoDeiContiVoce);
		
		wrapper.setMissione(missione);
		wrapper.setProgramma(programma);
		wrapper.setTitoloSpesa(titoloSpesa);
		
		return wrapper;
	}
	
	/**
	 * Imposta i campi della disponibilit&agrave;
	 * 
	 * @param wrapper il wrapper da popolare
	 * @param importi gli importi da cui ottenere i dati
	 */
	private static void impostaDisponibilita(ElementoCapitolo wrapper, ImportiCapitoloUG importi) {
		BigDecimal disponibilita0 = BigDecimal.ZERO;
		BigDecimal disponibilita1 = BigDecimal.ZERO;
		BigDecimal disponibilita2 = BigDecimal.ZERO;
		BigDecimal disponibilitaVariare0 = BigDecimal.ZERO;
		BigDecimal disponibilitaVariare1 = BigDecimal.ZERO;
		BigDecimal disponibilitaVariare2 = BigDecimal.ZERO;
		
		if(importi != null) {
			disponibilita0 = importi.getDisponibilitaImpegnareAnno1();
			disponibilita1 = importi.getDisponibilitaImpegnareAnno2();
			disponibilita2 = importi.getDisponibilitaImpegnareAnno3();
			disponibilitaVariare0 = importi.getDisponibilitaVariareAnno1();
			disponibilitaVariare1 = importi.getDisponibilitaVariareAnno2();
			disponibilitaVariare2 = importi.getDisponibilitaVariareAnno3();
		}
		
		wrapper.setDisponibileAnno0(disponibilita0);
		wrapper.setDisponibileAnno1(disponibilita1);
		wrapper.setDisponibileAnno2(disponibilita2);
		wrapper.setDisponibileVariareAnno0(disponibilitaVariare0);
		wrapper.setDisponibileVariareAnno1(disponibilitaVariare1);
		wrapper.setDisponibileVariareAnno2(disponibilitaVariare2);
	}

	/**
	 * Wrapper per il Capitolo di Entrata Previsione.
	 *  
	 * @param capitolo il capitolo da wrappare
	 * @param massivo  se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB se l'ente gestisce le UEB
	 * 
	 * @return il wrapper
	 */
	public static ElementoCapitolo getInstance(CapitoloEntrataPrevisione capitolo, boolean massivo, boolean gestioneUEB) {
		// Fallback in caso di null
		if(capitolo == null) {
			return null;
		}
				
		ElementoCapitolo wrapper = new ElementoCapitolo();
		
		// Carica i dati comuni
		popolaCampiComuni(wrapper, capitolo, massivo, gestioneUEB);
		
		// Popola i dati specifici
		
		// Campi da injettare
		String classificazione;
		String strutturaAmministrativoResponsabile = "";
		String pianoDeiContiFinanziario = "";
		String pianoDeiContiVoce = "";
		
		// Campi di utilita'
		TitoloEntrata titoloEntrata;
		TipologiaTitolo tipologiaTitolo;
		CategoriaTipologiaTitolo categoriaTipologiaTitolo;
		StrutturaAmministrativoContabile strutturaAmministrativoContabile;
		ElementoPianoDeiConti elementoPianoDeiConti;
		
		// Classificazione
		titoloEntrata = capitolo.getTitoloEntrata();
		tipologiaTitolo = capitolo.getTipologiaTitolo();
		categoriaTipologiaTitolo = capitolo.getCategoriaTipologiaTitolo();
		
		classificazione = codificaToString(categoriaTipologiaTitolo);
		
		impostaImportiCapitolo(wrapper, capitolo.getImportiCapitoloEP(), capitolo.getListaImportiCapitoloEP());
		
		// Struttura amministrativo Contabile
		strutturaAmministrativoContabile = capitolo.getStrutturaAmministrativoContabile();
		strutturaAmministrativoResponsabile = ((strutturaAmministrativoContabile == null) ? UNDEFINED : 
				(strutturaAmministrativoContabile.getCodice() + "-" + strutturaAmministrativoContabile.getDescrizione()));
		
		// Piano dei conti
		elementoPianoDeiConti = capitolo.getElementoPianoDeiConti();
		if (elementoPianoDeiConti != null) {
			pianoDeiContiFinanziario = elementoPianoDeiConti.getCodice();
			pianoDeiContiVoce = elementoPianoDeiConti.getDescrizione();
		} else {
			pianoDeiContiFinanziario = UNDEFINED;
			pianoDeiContiVoce = UNDEFINED;
		}
		
		// Injezione dei dati nel wrapper
		wrapper.setClassificazione(classificazione);
		wrapper.setStruttAmmResp(strutturaAmministrativoResponsabile);
		wrapper.setPdcFinanziario(pianoDeiContiFinanziario);
		wrapper.setPdcVoce(pianoDeiContiVoce);
		
		wrapper.setTitoloEntrata(titoloEntrata);
		wrapper.setTipologiaTitolo(tipologiaTitolo);
		
		return wrapper;
	}
	
	/**
	 * Wrapper per il Capitolo di Entrata Gestione.
	 *  
	 * @param capitolo il capitolo da wrappare
	 * @param massivo  se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB se l'ente gestisce le UEB
	 * 
	 * @return il wrapper
	 */
	public static ElementoCapitolo getInstance(CapitoloEntrataGestione capitolo, boolean massivo, boolean gestioneUEB) {
		// Fallback in caso di null
		if(capitolo == null) {
			return null;
		}
				
		ElementoCapitolo wrapper = new ElementoCapitolo();
		
		// Carica i dati comuni
		popolaCampiComuni(wrapper, capitolo, massivo, gestioneUEB);
		
		// Popola i dati specifici
		
		// Campi da injettare
		String classificazione;
		String strutturaAmministrativoResponsabile = "";
		String pianoDeiContiFinanziario = "";
		String pianoDeiContiVoce = "";
		
		// Campi di utilita'
		TitoloEntrata titoloEntrata;
		TipologiaTitolo tipologiaTitolo;
		CategoriaTipologiaTitolo categoriaTipologiaTitolo;
		StrutturaAmministrativoContabile strutturaAmministrativoContabile;
		ElementoPianoDeiConti elementoPianoDeiConti;
						
		// Classificazione
		titoloEntrata = capitolo.getTitoloEntrata();
		tipologiaTitolo = capitolo.getTipologiaTitolo();
		categoriaTipologiaTitolo = capitolo.getCategoriaTipologiaTitolo();
		
		classificazione = codificaToString(categoriaTipologiaTitolo);
		
		impostaImportiCapitolo(wrapper, capitolo.getImportiCapitoloEG(), capitolo.getListaImportiCapitoloEG());
		impostaDisponibilita(wrapper, capitolo.getImportiCapitoloEG());
		
		// Struttura amministrativo Contabile
		strutturaAmministrativoContabile = capitolo.getStrutturaAmministrativoContabile();
		strutturaAmministrativoResponsabile = ((strutturaAmministrativoContabile == null) ? UNDEFINED : 
				(strutturaAmministrativoContabile.getCodice() + "-" + strutturaAmministrativoContabile.getDescrizione()));
		
		// Piano dei conti
		elementoPianoDeiConti = capitolo.getElementoPianoDeiConti();
		if (elementoPianoDeiConti != null) {
			pianoDeiContiFinanziario = elementoPianoDeiConti.getCodice();
			pianoDeiContiVoce = elementoPianoDeiConti.getDescrizione();
		} else {
			pianoDeiContiFinanziario = UNDEFINED;
			pianoDeiContiVoce = UNDEFINED;
		}
		
		// Injezione dei dati nel wrapper
		wrapper.setClassificazione(classificazione);
		wrapper.setStruttAmmResp(strutturaAmministrativoResponsabile);
		wrapper.setPdcFinanziario(pianoDeiContiFinanziario);
		wrapper.setPdcVoce(pianoDeiContiVoce);
		
		wrapper.setTitoloEntrata(titoloEntrata);
		wrapper.setTipologiaTitolo(tipologiaTitolo);
		
		return wrapper;
	}
	
	/**
	 * Imposta i campi della disponibilit&agrave;
	 * 
	 * @param wrapper il wrapper da popolare
	 * @param importi gli importi da cui ottenere i dati
	 */
	private static void impostaDisponibilita(ElementoCapitolo wrapper, ImportiCapitoloEG importi) {
		BigDecimal disponibilita0 = BigDecimal.ZERO;
		BigDecimal disponibilita1 = BigDecimal.ZERO;
		BigDecimal disponibilita2 = BigDecimal.ZERO;
		BigDecimal disponibilitaVariare0 = BigDecimal.ZERO;
		BigDecimal disponibilitaVariare1 = BigDecimal.ZERO;
		BigDecimal disponibilitaVariare2 = BigDecimal.ZERO;
		
		if(importi != null) {
			disponibilita0 = importi.getDisponibilitaAccertareAnno1();
			disponibilita1 = importi.getDisponibilitaAccertareAnno2();
			disponibilita2 = importi.getDisponibilitaAccertareAnno3();
			disponibilitaVariare0 = importi.getDisponibilitaVariareAnno1();
			disponibilitaVariare1 = importi.getDisponibilitaVariareAnno2();
			disponibilitaVariare2 = importi.getDisponibilitaVariareAnno3();
		}
		
		wrapper.setDisponibileAnno0(disponibilita0);
		wrapper.setDisponibileAnno1(disponibilita1);
		wrapper.setDisponibileAnno2(disponibilita2);
		wrapper.setDisponibileVariareAnno0(disponibilitaVariare0);
		wrapper.setDisponibileVariareAnno1(disponibilitaVariare1);
		wrapper.setDisponibileVariareAnno2(disponibilitaVariare2);
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un {@link ElementoCapitolo} a partire da un capitolo.
	 * 
	 * @param capitolo il capitolo di riferimento
	 * @param massivo  se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB se l'ente gestisce le UEB
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitolo getInstance(Capitolo<?, ?> capitolo, boolean massivo, boolean gestioneUEB) {
		ElementoCapitolo result = null;
		if(capitolo instanceof CapitoloUscitaPrevisione) {
			result = getInstance((CapitoloUscitaPrevisione)capitolo, massivo, gestioneUEB);
		} else if (capitolo instanceof CapitoloUscitaGestione) {
			result = getInstance((CapitoloUscitaGestione)capitolo, massivo, gestioneUEB);
		} else if (capitolo instanceof CapitoloEntrataPrevisione) {
			result = getInstance((CapitoloEntrataPrevisione)capitolo, massivo, gestioneUEB);
		} else if (capitolo instanceof CapitoloEntrataGestione) {
			result = getInstance((CapitoloEntrataGestione)capitolo, massivo, gestioneUEB);
		} else {
			throw new UnsupportedOperationException("Il capitolo fornito non permette il wrapping");
		}
		
		return result;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un {@link ElementoCapitolo} a partire da un capitolo.
	 * 
	 * @param capitoli i capitolo di riferimento
	 * @param massivo  se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB se l'ente gestisce le UEB
	 * 
	 * @param <T>  	   la tipizzazione del risultato
	 * 
	 * @return il wrapper creato
	 */
	public static <T extends Capitolo<?, ?>> List<ElementoCapitolo> getInstances(List<T> capitoli, boolean massivo, boolean gestioneUEB) {
		List<ElementoCapitolo> result = new ArrayList<ElementoCapitolo>();
		for(T capitolo : capitoli) {
			result.add(getInstance(capitolo, massivo, gestioneUEB));
		}
		return result;
	}
	
	/**
	 * Metodo di utilit&agrave; per il wrapping degli elementi comuni.
	 * 
	 * @param wrapper			 il wrapper in cui injettare i dati
	 * @param capitoloDaWrappare il capitolo da wrappare
	 * @param massivo            se il capitolo da wrappare &egrave; massivo
	 * @param gestioneUEB        se l'ente gestisce le UEB
	 */
	private static <CAP extends Capitolo<?, ?>>void popolaCampiComuni(ElementoCapitolo wrapper, CAP capitoloDaWrappare, boolean massivo, boolean gestioneUEB) {
		Integer uid;
		StringBuilder capitolo = new StringBuilder();
		String descrizione;
		String stato;
		String azioni;
		String annoCapitolo;
		String numeroCapitolo;
		String numeroArticolo;
		CategoriaCapitolo categoriaCapitolo;
		
		Boolean rilevanteIva = Boolean.FALSE;
				
		// Numero Capitolo
		if (capitoloDaWrappare.getNumeroCapitolo() != null && capitoloDaWrappare.getNumeroArticolo() != null && 
				capitoloDaWrappare.getNumeroUEB() != null) {
			capitolo.append(capitoloDaWrappare.getNumeroCapitolo())
				.append("/")
				.append(capitoloDaWrappare.getNumeroArticolo());
		} else {
			capitolo.append(UNDEFINED);
		}
		
		/* 
		 * Boolean per controllare se utilizzare effettivamente il campo UEB. La logica e':
		 * - l'ente deve gestire gli UEB,
		 * - il campo capitolo deve essere gia' stato compilato con numeroCapitolo e numeroArticolo 
		 * 		(ovvero, differente da UNDEFINED),
		 * - il capitolo deve essere non-massivo,
		 * - il numeroUEB deve essere non-null.
		 */
		boolean utilizzoUEB = gestioneUEB &&
				!UNDEFINED.equals(capitolo.toString()) &&
				!massivo &&
				(capitoloDaWrappare.getNumeroUEB() != null);
		
		// Se posso utilizzare le UEB, le injetto nel campo
		if(utilizzoUEB) {
			capitolo.append("/")
				.append(capitoloDaWrappare.getNumeroUEB());
			wrapper.setNumeroUEB(capitoloDaWrappare.getNumeroUEB().toString());
		}
		
		// Descrizione del Capitolo
		StringBuilder sbDescrizione = new StringBuilder()
			.append(capitoloDaWrappare.getDescrizione() == null ? UNDEFINED : capitoloDaWrappare.getDescrizione());
		if(StringUtils.isNotBlank(capitoloDaWrappare.getDescrizioneArticolo())) {
			sbDescrizione.append(" - ")
				.append(capitoloDaWrappare.getDescrizioneArticolo());
		}
		descrizione = sbDescrizione.toString();
		
		// Stato del Capitolo
		stato = ((capitoloDaWrappare.getStatoOperativoElementoDiBilancio() == null) ? 
				UNDEFINED : 
				capitoloDaWrappare.getStatoOperativoElementoDiBilancio().name());
				
		// uid
		uid = capitoloDaWrappare.getUid();
		
		// azioni
		azioni = UNDEFINED;
		
		// anno e numeri
		annoCapitolo = capitoloDaWrappare.getAnnoCapitolo().toString();
		numeroCapitolo = capitoloDaWrappare.getNumeroCapitolo().toString();
		numeroArticolo = capitoloDaWrappare.getNumeroArticolo().toString();
		
		// flags
		rilevanteIva = capitoloDaWrappare.getFlagRilevanteIva();
		
		categoriaCapitolo = capitoloDaWrappare.getCategoriaCapitolo();
		
		// Injezione dei dati nel wrapper
		wrapper.setCapitolo(capitolo.toString());
		wrapper.setDescrizione(descrizione);
		wrapper.setStato(stato);
		wrapper.setUid(uid);
		wrapper.setAzioni(azioni);
		wrapper.setAnnoCapitolo(annoCapitolo);
		wrapper.setNumeroCapitolo(numeroCapitolo);
		wrapper.setNumeroArticolo(numeroArticolo);
		wrapper.setRilevanteIva(rilevanteIva);
		wrapper.setCategoriaCapitolo(categoriaCapitolo);
	}
	
	/**
	 * Imposta gli importi del capitolo all'interno del wrapper.
	 * 
	 * @param wrapper il wrapper da popolare
	 * @param importo gli importi tramite cui popolare il wrapper
	 * @param lista   lista degli importi
	 */
	private static <I extends ImportiCapitolo> void impostaImportiCapitolo(ElementoCapitolo wrapper, I importo, List<I> lista) {
		BigDecimal stanziamentoCompetenza = BigDecimal.ZERO;
		BigDecimal stanziamentoResiduo = BigDecimal.ZERO;
		BigDecimal stanziamentoCassa = BigDecimal.ZERO;
		
		BigDecimal stanziamentoCompetenza1 = BigDecimal.ZERO;
		BigDecimal stanziamentoResiduo1 = BigDecimal.ZERO;
		BigDecimal stanziamentoCassa1 = BigDecimal.ZERO;
		BigDecimal stanziamentoCompetenza2 = BigDecimal.ZERO;
		BigDecimal stanziamentoResiduo2 = BigDecimal.ZERO;
		BigDecimal stanziamentoCassa2 = BigDecimal.ZERO;
		
		if (importo != null) {
			stanziamentoCompetenza = importo.getStanziamento();
			stanziamentoResiduo = importo.getStanziamentoResiduo();
			stanziamentoCassa = importo.getStanziamentoCassa();
			
			Integer annoImporto = importo.getAnnoCompetenza();
			I importo1 = ComparatorUtils.searchByAnno(lista, annoImporto + 1);
			I importo2 = ComparatorUtils.searchByAnno(lista, annoImporto + 2);
			
			if(importo1 != null) {
				stanziamentoCompetenza1 = importo1.getStanziamento();
				stanziamentoResiduo1 = importo1.getStanziamentoResiduo();
				stanziamentoCassa1 = importo1.getStanziamentoCassa();
			}
			if(importo2 != null) {
				stanziamentoCompetenza2 = importo2.getStanziamento();
				stanziamentoResiduo2 = importo2.getStanziamentoResiduo();
				stanziamentoCassa2 = importo2.getStanziamentoCassa();
			}
		}
		
		wrapper.setStanziamentoCompetenza(stanziamentoCompetenza);
		wrapper.setStanziamentoResiduo(stanziamentoResiduo);
		wrapper.setStanziamentoCassa(stanziamentoCassa);
		
		wrapper.setStanziamentoCompetenza1(stanziamentoCompetenza1);
		wrapper.setStanziamentoResiduo1(stanziamentoResiduo1);
		wrapper.setStanziamentoCassa1(stanziamentoCassa1);
		
		wrapper.setStanziamentoCompetenza2(stanziamentoCompetenza2);
		wrapper.setStanziamentoResiduo2(stanziamentoResiduo2);
		wrapper.setStanziamentoCassa2(stanziamentoCassa2);
	}
	
	/**
	 * Ottiene il codice dalla codifica.
	 * 
	 * @param codifica la codifica
	 * @return il codice della codifica se presente
	 */
	private static String codificaToString(Codifica codifica) {
		return codifica == null ? "--" : codifica.getCodice();
	}
	
}
