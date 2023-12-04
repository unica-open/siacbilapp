/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionBilUtil;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Classe di factory per l'ElementoCapitoloVariazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public final class ElementoCapitoloVariazioneFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCapitoloVariazioneFactory() {
	}
	
	/**
	 * Costruisce un'istanza di un ElementoCapitoloVariazione a partire da un Capitolo.
	 * 
	 * @param capitolo            il capitolo da wrappare
	 * @param gestioneUEB         se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti         l'anno degli importi
	 * @param impostazioneImporti se gli importi debbano essere importati
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVariazioneUscitaPrevisione getInstance(CapitoloUscitaPrevisione capitolo, Boolean gestioneUEB, Boolean impostazioneImporti) {
		ElementoCapitoloVariazioneUscitaPrevisione wrapper = new ElementoCapitoloVariazioneUscitaPrevisione();
		impostaCampi(wrapper, capitolo, gestioneUEB, impostazioneImporti);
		
		return wrapper;
	}
	
	/**
	 * Costruisce un'istanza di un ElementoCapitoloVariazione a partire da un Capitolo.
	 * 
	 * @param capitolo            il capitolo da wrappare
	 * @param gestioneUEB         se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti         l'anno degli importi
	 * @param impostazioneImporti se gli importi debbano essere importati
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVariazioneUscitaGestione getInstance(CapitoloUscitaGestione capitolo, Boolean gestioneUEB, Boolean impostazioneImporti) {
		ElementoCapitoloVariazioneUscitaGestione wrapper = new ElementoCapitoloVariazioneUscitaGestione();
		
		impostaCampi(wrapper, capitolo, gestioneUEB, impostazioneImporti);
		
		return wrapper;
	}
	
	/**
	 * Costruisce un'istanza di un ElementoCapitoloVariazione a partire da un Capitolo.
	 * 
	 * @param capitolo            il capitolo da wrappare
	 * @param gestioneUEB         se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti         l'anno degli importi
	 * @param impostazioneImporti se gli importi debbano essere importati
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVariazioneEntrataPrevisione getInstance(CapitoloEntrataPrevisione capitolo, Boolean gestioneUEB, Boolean impostazioneImporti) {
		ElementoCapitoloVariazioneEntrataPrevisione wrapper = new ElementoCapitoloVariazioneEntrataPrevisione();
		
		impostaCampi(wrapper, capitolo, gestioneUEB, impostazioneImporti);
		
		return wrapper;
	}
	
	/**
	 * Costruisce un'istanza di un ElementoCapitoloVariazione a partire da un Capitolo.
	 * 
	 * @param capitolo            il capitolo da wrappare
	 * @param gestioneUEB         se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti         l'anno degli importi
	 * @param impostazioneImporti se gli importi debbano essere importati
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVariazioneEntrataGestione getInstance(CapitoloEntrataGestione capitolo, Boolean gestioneUEB, Boolean impostazioneImporti) {
		ElementoCapitoloVariazioneEntrataGestione wrapper = new ElementoCapitoloVariazioneEntrataGestione();
		
		impostaCampi(wrapper, capitolo, gestioneUEB, impostazioneImporti);
		
		return wrapper;
	}
	
	/**
	 * Costruisce un'istanza di un ElementoCapitoloVariazione a partire da un Capitolo.
	 * 
	 * @param capitolo            il capitolo da wrappare
	 * @param gestioneUEB         se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti         l'anno degli importi
	 * @param impostazioneImporti se gli importi debbano essere importati
	 * 
	 * @param <V> la tipizzazione del wrapper
	 * @param <C> la tipizzazione del capitolo
	 * 
	 * @return il wrapper creato
	 */
	@SuppressWarnings("unchecked")
	public static <V extends ElementoCapitoloVariazione, C extends Capitolo<?, ?>> V getInstance(C capitolo, Boolean gestioneUEB, Boolean impostazioneImporti) {
		// Fallback nel caso in cui i dati non siano correttamente settati
		if(capitolo == null || capitolo.getTipoCapitolo() == null) {
			return null;
		}
		
		V result = null;
		// L'istanza del capitolo dalla quale ricavare il wrapper
		Capitolo<?, ?> nuovaIstanza = capitolo;
		try {
			// Controllo se la classe del capitolo fornito in input sia effettivamente quella della superclasse, o sia quella di una sottoclasse
			if(Capitolo.class.equals(capitolo.getClass())) {
				// Sono nella superclasse. Innanzitutto, devo creare una nuova istanza del capitolo sì da non avere problemi di invocazione circolare
				nuovaIstanza = ReflectionBilUtil.getInstanceFromCapitoloBaseClass(capitolo);
			}
			// Ho un'istanza di una sottoclasse del capitolo
			Method method = ElementoCapitoloVariazioneFactory.class.getMethod("getInstance", nuovaIstanza.getClass(), Boolean.class, Boolean.class);
			result = (V) method.invoke(null, nuovaIstanza, gestioneUEB, impostazioneImporti);
		} catch(Exception e) {
			new LogUtil(ElementoCapitoloVariazioneFactory.class).error("getInstance", "", e);
		}
		
		return result;
	}
	
	/**
	 * Costruisce una lista di istanze di un ElementoCapitoloVariazione a partire da una lista di Capitoli
	 * 
	 * @param capitoli    la lista di capitoli
	 * @param gestioneUEB se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti l'anno degli importi
	 * 
	 * @param <V> la tipizzazione del wrapper
	 * @param <C> la tipizzazione del capitolo
	 * 
	 * @return la lista di wrapper
	 */
	public static <V extends ElementoCapitoloVariazione, C extends Capitolo<?, ?>> List<V> getInstances(List<C> capitoli, Boolean gestioneUEB) {
		List<V> lista = new ArrayList<V>();
		for(C capitolo : capitoli) {
			V instance = getInstance(capitolo, gestioneUEB, Boolean.TRUE);
			lista.add(instance);
		}
		return lista;
	}
	
	/**
	 * Costruisce una lista di istanze di un ElementoCapitoloVariazione a partire da una lista di Capitoli
	 * 
	 * @param capitoli    la lista di capitoli
	 * @param gestioneUEB se la gestioneUEB &eacute; attiva o meno
	 * 
	 * @param <V> la tipizzazione del wrapper
	 * 
	 * @return la lista di wrapper
	 */
	public static <V extends ElementoCapitoloVariazione> List<V> getInstancesFromDettaglio(List<DettaglioVariazioneImportoCapitolo> capitoli, Boolean gestioneUEB) {
		List<V> lista = new ArrayList<V>();
		for(DettaglioVariazioneImportoCapitolo capitolo : capitoli) {
			V instance = getInstance(capitolo.getCapitolo(), gestioneUEB, Boolean.FALSE);
			instance.setCompetenza(capitolo.getStanziamento());
			instance.setResiduo(capitolo.getStanziamentoResiduo());
			instance.setCassa(capitolo.getStanziamentoCassa());
			instance.setFondoPluriennaleVincolato(capitolo.getFondoPluriennaleVinc());
			instance.setDaAnnullare(capitolo.getFlagAnnullaCapitolo());
			instance.setDaInserire(capitolo.getFlagNuovoCapitolo());
			instance.setCassaOriginale(capitolo.getCapitolo().getImportiCapitolo().getStanziamentoCassa());
			instance.setCompetenzaOriginale(capitolo.getCapitolo().getImportiCapitolo().getStanziamento());
			instance.setResiduoOriginale(capitolo.getCapitolo().getImportiCapitolo().getStanziamentoResiduo());
			
			impostaDatiAccessorii(instance, capitolo.getFlagAnnullaCapitolo(), capitolo.getFlagNuovoCapitolo());
			
			lista.add(instance);
		}
		return lista;
	}
	
	/**
	 * Costruisce una istanzea di un ElementoCapitoloVariazione a partire da un dettaglio variazione Capitolo
	 * 
	 * @param capitolo    la lista di capitoli
	 * @param gestioneUEB se la gestioneUEB &eacute; attiva o meno
	 * 
	 * @param <V> la tipizzazione del wrapper
	 * 
	 * @return il wrapper
	 */
	public static <V extends ElementoCapitoloVariazione> V getInstanceFromSingoloDettaglio(DettaglioVariazioneImportoCapitolo capitolo, Boolean gestioneUEB) {
		
		V instance = getInstance(capitolo.getCapitolo(), gestioneUEB, gestioneUEB);
			instance.setCompetenza(capitolo.getStanziamento());
			instance.setResiduo(capitolo.getStanziamentoResiduo());
			instance.setCassa(capitolo.getStanziamentoCassa());
			instance.setFondoPluriennaleVincolato(capitolo.getFondoPluriennaleVinc());
			instance.setDaAnnullare(capitolo.getFlagAnnullaCapitolo());
			instance.setDaInserire(capitolo.getFlagNuovoCapitolo());
			instance.setCassaOriginale(capitolo.getCapitolo().getImportiCapitolo().getStanziamentoCassa());
			instance.setCompetenzaOriginale(capitolo.getCapitolo().getImportiCapitolo().getStanziamento());
			instance.setResiduoOriginale(capitolo.getCapitolo().getImportiCapitolo().getStanziamentoResiduo());
			//SIAC-6883
			instance.setCompetenza1(capitolo.getStanziamento1());
			instance.setCompetenzaOriginale1(capitolo.getCapitolo().getImportiCapitolo().getStanziamento());
			instance.setCompetenza2(capitolo.getStanziamento2());
			instance.setCompetenzaOriginale2(capitolo.getCapitolo().getImportiCapitolo().getStanziamento());
			
			// SIAC-8003
			instance.setCodiceCategoriaCapitolo(getCodiceCategoriaCapitolo(capitolo));
			
			
			impostaDatiAccessorii(instance, capitolo.getFlagAnnullaCapitolo(), capitolo.getFlagNuovoCapitolo());
			
			return instance;
	}

	private static String getCodiceCategoriaCapitolo(DettaglioVariazioneImportoCapitolo capitolo) {
		try {
			return capitolo.getCapitolo().getCategoriaCapitolo().getCodice();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Imposta i campi comuni tra il wrapper e il capitolo originale.
	 * 
	 * @param wrapper             il wrapper da popolare
	 * @param capitolo            il capitolo da cui ottenere i dati
	 * @param gestioneUEB         se la gestioneUEB &eacute; attiva o meno
	 * @param annoImporti         l'anno degli importi
	 * @param impostazioneImporti se gli importi siano da impostare o meno
	 */
	private static void impostaCampi(ElementoCapitoloVariazione wrapper, Capitolo<?, ?> capitolo, Boolean gestioneUEB, Boolean impostazioneImporti) {
		
		wrapper.setUid(capitolo.getUid());
		wrapper.setAnnoCapitolo(capitolo.getAnnoCapitolo());
		wrapper.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		wrapper.setNumeroArticolo(capitolo.getNumeroArticolo());
		wrapper.setNumeroUEB(capitolo.getNumeroUEB());
		wrapper.setTipoCapitolo(capitolo.getTipoCapitolo());
		wrapper.setGestioneUEB(gestioneUEB);
		wrapper.setStatoOperativoElementoDiBilancio(capitolo.getStatoOperativoElementoDiBilancio());
		
		
		StringBuilder sbDescrizione = new StringBuilder()
			.append(capitolo.getDescrizione());
		if(StringUtils.isNotBlank(capitolo.getDescrizioneArticolo())) {
			sbDescrizione.append(" - ")
				.append(capitolo.getDescrizioneArticolo());
		}
		wrapper.setDescrizione(sbDescrizione.toString());
		
		ImportiCapitolo importi = null;
		
		if(Boolean.TRUE.equals(impostazioneImporti)) {
			// FIXME: SIAC-6883
//			importi = ComparatorUtil.searchByAnno(capitolo.getListaImportiCapitolo(), annoImporti);
			// Fallback nel caso in cui la lista sia vuota
			if(importi == null) {
				importi = capitolo.getImportiCapitolo();
			}
		}
		
		if(importi != null) {
			wrapper.setCompetenza(importi.getStanziamento());
			wrapper.setResiduo(importi.getStanziamentoResiduo());
			wrapper.setCassa(importi.getStanziamentoCassa());
			wrapper.setFondoPluriennaleVincolato(importi.getFondoPluriennaleVinc());
		}
		
		// Creazione della denominazione
		StringBuilder denominazione = new StringBuilder()
				.append((wrapper.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE || wrapper.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE) ? "E" : "S")
				.append("-")
				.append(wrapper.getAnnoCapitolo())
				.append("/")
				.append(wrapper.getNumeroCapitolo())
				.append("/")
				.append(wrapper.getNumeroArticolo());
		if(Boolean.TRUE.equals(wrapper.getGestioneUEB())) {
			denominazione.append("/")
				.append(wrapper.getNumeroUEB());
		}
		wrapper.setDenominazione(denominazione.toString());
		
		// Impostazione dei dati accessorii
		impostaDatiAccessorii(wrapper, Boolean.FALSE, StatoOperativoElementoDiBilancio.PROVVISORIO.equals(capitolo.getStatoOperativoElementoDiBilancio()));
	}
	
	/**
	 * Imposta i dati accessorii nel wrapper.
	 * 
	 * @param wrapper     il wrapper in cui injettare i dati accessorii
	 * @param daAnnullare se il capitolo rappresentato dal wrapper sia da annullare
	 * @param daInserire  se il capitolo rappresentato dal wrapper sia da inserire
	 */
	private static void impostaDatiAccessorii(ElementoCapitoloVariazione wrapper, Boolean daAnnullare, Boolean daInserire) {
		String datiAccessorii = "";
		wrapper.setDaAnnullare(daAnnullare);
		wrapper.setDaInserire(daInserire);
		if(Boolean.TRUE.equals(daAnnullare)) {
			datiAccessorii = "DA ANNULLARE";
		} else if(Boolean.TRUE.equals(daInserire)) {
			datiAccessorii = "NUOVO";
		}
		wrapper.setDatiAccessorii(StringUtils.trimToNull(datiAccessorii));
	}
	
	/**
	 * Trasforma il wrapper in un Dettaglio per la Variazione di Importo per il Capitolo.
	 * 
	 * @param elementoDaConvertire il wrapper da convertire
	 * 
	 * @return il wrapper convertito
	 */
	public static DettaglioVariazioneImportoCapitolo transform(ElementoCapitoloVariazione elementoDaConvertire) {
		DettaglioVariazioneImportoCapitolo result = new DettaglioVariazioneImportoCapitolo();

		result.setCapitolo(elementoDaConvertire.unwrap());
		
		result.setStanziamento(elementoDaConvertire.getCompetenza());
		result.setStanziamentoResiduo(elementoDaConvertire.getResiduo());
		result.setStanziamentoCassa(elementoDaConvertire.getCassa());
		result.setFondoPluriennaleVinc(elementoDaConvertire.getFondoPluriennaleVincolato());
		
		result.setFlagAnnullaCapitolo(elementoDaConvertire.getDaAnnullare());
		result.setFlagNuovoCapitolo(elementoDaConvertire.getDaInserire());
		
		return result;
	}
	
	/**
	 * Collassa una lista di ElementoCapitoloVariazione su di un unico wrapper.
	 * @param <V> la tipizzazione del wrapper
	 * @param lista la lista da collassare
	 * 
	 * @return l'elemento collassato
	 */
	@SuppressWarnings("unchecked")
	public static <V extends ElementoCapitoloVariazione> V collapse (List<V> lista) {
		if(lista == null || lista.isEmpty()) {
			return null;
		}
		V result;
		try {
			result = (V) lista.get(0).clone();
		} catch (CloneNotSupportedException e) {
			// Non dovrebbe mai succedere. Per sicurezza è comunque quivi riportato
			result = lista.get(0);
		}
		
		// Tolgo il numero della UEB
		result.setNumeroUEB(null);
		String datiAccessorii = result.getDatiAccessorii();
		result.getListaSottoElementi().add(lista.get(0));
		
		for(int i = 1; i < lista.size(); i++) {
			V temp = lista.get(i);
			if(temp != null) {
				result.setCompetenza(result.getCompetenza().add(temp.getCompetenza()));
				result.setResiduo(result.getResiduo().add(temp.getResiduo()));
				result.setCassa(result.getCassa().add(temp.getCassa()));
				
				// Injetto i dati accessorii, ma solo i primi che trovo
				if(temp.getDatiAccessorii() != null && datiAccessorii == null) {
					datiAccessorii = temp.getDatiAccessorii();
				}
				
				// Aggiungo l'elemento temporaneo nella lista dei sotto-elementi
				result.getListaSottoElementi().add(temp);
			}
		}
		
		result.setDatiAccessorii(datiAccessorii);
		
		String denominazione = ((result.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE || result.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE) ? "E" : "S")
				+ "-" + result.getAnnoCapitolo() + "/" + result.getNumeroCapitolo() + "/" + result.getNumeroArticolo();
		result.setDenominazione(denominazione);
		
		return result;
	}
	
	/**
	 * Collassa una lista di Dettaglio Variazione Importo Capitolo su di una lista di wrappers.
	 * 
	 * @param lista       la lista da collassare
	 * @param gestioneUEB se la gestione delle UEB &eacute; attiva
	 * 
	 * @return la lista degli elementi collassati
	 */
	public static List<ElementoCapitoloVariazione> collapseToList (List<DettaglioVariazioneImportoCapitolo> lista, Boolean gestioneUEB) {
		List<ElementoCapitoloVariazione> result = new ArrayList<ElementoCapitoloVariazione>();
		if(lista == null || lista.isEmpty()) {
			return result;
		}
		
		Map<String, List<ElementoCapitoloVariazione>> mappaElementi = new HashMap<String, List<ElementoCapitoloVariazione>>();
		for(DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo : lista) {
			Capitolo<?, ?> capitolo = dettaglioVariazioneImportoCapitolo.getCapitolo();
			ElementoCapitoloVariazione elementoCapitolo = getInstance(capitolo, gestioneUEB, Boolean.FALSE);
			// Impostazione degli stanziamenti
			elementoCapitolo.setCompetenza(dettaglioVariazioneImportoCapitolo.getStanziamento());
			elementoCapitolo.setCassa(dettaglioVariazioneImportoCapitolo.getStanziamentoCassa());
			elementoCapitolo.setResiduo(dettaglioVariazioneImportoCapitolo.getStanziamentoResiduo());
			elementoCapitolo.setCompetenzaOriginale(capitolo.getImportiCapitolo().getStanziamento());
			elementoCapitolo.setCassaOriginale(capitolo.getImportiCapitolo().getStanziamentoCassa());
			elementoCapitolo.setResiduoOriginale(capitolo.getImportiCapitolo().getStanziamentoResiduo());
			elementoCapitolo.setFondoPluriennaleVincolato(dettaglioVariazioneImportoCapitolo.getFondoPluriennaleVinc());
			// Impostazione dei flags
			impostaDatiAccessorii(elementoCapitolo, dettaglioVariazioneImportoCapitolo.getFlagAnnullaCapitolo(), dettaglioVariazioneImportoCapitolo.getFlagNuovoCapitolo());
			
			String key = dettaglioVariazioneImportoCapitolo.getCapitolo().getTipoCapitolo() + "_" + capitolo.getNumeroCapitolo() + "_" + capitolo.getNumeroArticolo();
			List<ElementoCapitoloVariazione> list = mappaElementi.get(key);
			if(list == null) {
				list = new ArrayList<ElementoCapitoloVariazione>();
			}
			list.add(elementoCapitolo);
			mappaElementi.put(key, list);
		}
		
		// Collasso nella lista
		for(Map.Entry<String, List<ElementoCapitoloVariazione>> entry : mappaElementi.entrySet()) {
			result.add(collapse(entry.getValue()));
		}
		
		return result;
	}
	
}
