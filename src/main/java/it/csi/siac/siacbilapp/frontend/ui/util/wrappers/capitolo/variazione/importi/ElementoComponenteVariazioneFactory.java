/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilapp.frontend.ui.util.helper.ImportiCapitoloComponenteVariazioneModificabile;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Classe di factory per l'ElementoCapitoloVariazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public final class ElementoComponenteVariazioneFactory {
	
	private static final LogUtil LOG = new LogUtil(ElementoCapitoloCodificheFactory.class);
	
	public static final String KEY_ANNO_0 = "ANNO0";
	public static final String KEY_ANNO_1 = "ANNO1";
	public static final String KEY_ANNO_2 = "ANNO2";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoComponenteVariazioneFactory() {
		
	}

	
	/**
	 * Gets the single instance of ElementoComponenteVariazioneFactory. Utilizzato per la creazione della componente a partire dai Dati del capitolo
	 *
	 * @param tipoComponente the tipo componente
	 * @param dettaglioComponenteImportiCapitolo the dettaglio anno 0
	 * @param importoAnno0 the importo anno 0
	 * @param dettaglioComponenteImportiCapitolo2 the dettaglio anno 1
	 * @param importoAnno1 the importo anno 1
	 * @param dettaglioComponenteImportiCapitolo3 the dettaglio anno 2
	 * @param importoAnno2 the importo anno 2
	 * @param importoModificabile the importo modificabile
	 * @param flagEliminaComponenteCapitolo the flag elimina componente capitolo
	 * @param nuovaComponente the nuova componente
	 * @return single instance of ElementoComponenteVariazioneSuTreAnniFactory
	 */
	public static final ElementoComponenteVariazione getInstanceFromDatiCapitolo(TipoComponenteImportiCapitolo tipoComponente, DettaglioComponenteImportiCapitolo dettaglioComponenteImportiCapitolo, BigDecimal importoAnno0, DettaglioComponenteImportiCapitolo dettaglioComponenteImportiCapitolo1, BigDecimal importoAnno1, DettaglioComponenteImportiCapitolo dettaglioComponenteImportiCapitolo2, BigDecimal importoAnno2,
			ImportiCapitoloComponenteVariazioneModificabile importoModificabile, boolean flagEliminaComponenteCapitolo, boolean nuovaComponente) {
		
		
		TipoComponenteImportiCapitolo tp = tipoComponente != null? tipoComponente : dettaglioComponenteImportiCapitolo.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo();
		
		DettaglioVariazioneComponenteImportoCapitolo dett0 = buildDettaglioVariazioneComponenteImportoCapitolo(dettaglioComponenteImportiCapitolo.getComponenteImportiCapitolo(), tp, dettaglioComponenteImportiCapitolo.getTipoDettaglioComponenteImportiCapitolo(), importoAnno0);
		DettaglioVariazioneComponenteImportoCapitolo dett1 = buildDettaglioVariazioneComponenteImportoCapitolo(dettaglioComponenteImportiCapitolo1.getComponenteImportiCapitolo(), tp, dettaglioComponenteImportiCapitolo1.getTipoDettaglioComponenteImportiCapitolo(), importoAnno1);
		DettaglioVariazioneComponenteImportoCapitolo dett2 = buildDettaglioVariazioneComponenteImportoCapitolo(dettaglioComponenteImportiCapitolo2.getComponenteImportiCapitolo(), tp, dettaglioComponenteImportiCapitolo2.getTipoDettaglioComponenteImportiCapitolo(), importoAnno2);
		
		return getInstance(dett0, dett1, dett2, importoModificabile.getInserimentoAnno0(), importoModificabile.getInserimentoAnniSuccessivi(), dettaglioComponenteImportiCapitolo.getImporto(), dettaglioComponenteImportiCapitolo1.getImporto(), dettaglioComponenteImportiCapitolo2.getImporto(), flagEliminaComponenteCapitolo, nuovaComponente, Boolean.TRUE);
	}

	
	/**
	 * Ottiene l'entita a partire dalla componente di default
	 *
	 * @param tipoComponente the tipo componente
	 * @param importoModificabile the importo modificabile
	 * @return the instance default
	 */
	public static final ElementoComponenteVariazione getInstanceComponenteDefault(TipoComponenteImportiCapitolo tipoComponente, ImportiCapitoloComponenteVariazioneModificabile importoModificabile) {
		
		DettaglioVariazioneComponenteImportoCapitolo dett0 = buildDettaglioVariazioneComponenteImportoCapitolo(null, tipoComponente, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, BigDecimal.ZERO);
		DettaglioVariazioneComponenteImportoCapitolo dett1 = buildDettaglioVariazioneComponenteImportoCapitolo(null, tipoComponente, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, BigDecimal.ZERO);
		DettaglioVariazioneComponenteImportoCapitolo dett2 = buildDettaglioVariazioneComponenteImportoCapitolo(null, tipoComponente, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, BigDecimal.ZERO);
		return getInstance(dett0, dett1, dett2, importoModificabile.getInserimentoAnno0(), importoModificabile.getInserimentoAnniSuccessivi(), false, true);
	}

	
	/**
	 * Ottiene l'istanza a partire dai dettagli di variazione presenti su base dati
	 *
	 * @param dettAnno0 the dett anno 0
	 * @param dettAnno1 the dett anno 1
	 * @param dettAnno2 the dett anno 2
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the instances
	 */
	public static final List<ElementoComponenteVariazione> getInstancesFromListeDettagliSui3Anni(List<DettaglioVariazioneComponenteImportoCapitolo> dettAnno0, List<DettaglioVariazioneComponenteImportoCapitolo> dettAnno1, List<DettaglioVariazioneComponenteImportoCapitolo> dettAnno2, ApplicazioneVariazione applicazioneVariazione) {
		Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> mappa = new HashMap<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>>();
		
		for (DettaglioVariazioneComponenteImportoCapitolo d0 : dettAnno0) {
			Integer key = Integer.valueOf(d0.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid());
			if(mappa.get(key) == null) {
				mappa.put(key, new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>());
			}
			mappa.get(key).add(d0);
		}
		
		for (DettaglioVariazioneComponenteImportoCapitolo d1 : dettAnno1) {
			Integer key = Integer.valueOf(d1.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid());
			if(mappa.get(key) == null) {
				mappa.put(key, new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>());
			}
			mappa.get(key).add(d1);
		}
		
		for (DettaglioVariazioneComponenteImportoCapitolo d2 : dettAnno2) {
			Integer key = Integer.valueOf(d2.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid());
			if(mappa.get(key) == null) {
				mappa.put(key, new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>());
			}
			mappa.get(key).add(d2);
		}

		List<ElementoComponenteVariazione> lista = new ArrayList<ElementoComponenteVariazione>();
		for (Integer k : mappa.keySet()) {
			List<DettaglioVariazioneComponenteImportoCapitolo> list = mappa.get(k);
			if(list == null) {
				continue;
			}

			popolaListaSuTreAnnualita(list);
			
			TipoComponenteImportiCapitolo tp = list.get(0).getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo();
			ImportiCapitoloComponenteVariazioneModificabile importoModificabile = ImportiCapitoloComponenteVariazioneModificabile.getImportiCapitoloModificabile(applicazioneVariazione, tp.getMacrotipoComponenteImportiCapitolo(), tp.getSottotipoComponenteImportiCapitolo(), list.get(0).getTipoDettaglioComponenteImportiCapitolo());
			
			DettaglioVariazioneComponenteImportoCapitolo dett0 = list.get(0);
			BigDecimal importoOriginale0 = getImportoComponenteOriginaleFromDettaglio(dett0);
			
			DettaglioVariazioneComponenteImportoCapitolo dett1 = list.get(1);
			BigDecimal importoOriginale1 = getImportoComponenteOriginaleFromDettaglio(dett1);
			
			DettaglioVariazioneComponenteImportoCapitolo dett2 = list.get(2);
			BigDecimal importoOriginale2 = getImportoComponenteOriginaleFromDettaglio(dett2);
			
			ElementoComponenteVariazione el = getInstance(
					dett0,
					dett1,
					dett2,
					importoModificabile != null ? importoModificabile.getInserimentoAnno0() : null,
					importoModificabile != null ? importoModificabile.getInserimentoAnniSuccessivi() : null,
					importoOriginale0,
					importoOriginale1,
					importoOriginale2,
					dett0.isFlagEliminaComponenteCapitolo(),
					dett0.isFlagNuovaComponenteCapitolo());
			lista.add(el);
		}
		
		return lista;
	}
	
	/**
	 * Gets the single instance of ElementoComponenteVariazioneSuTreAnniFactory.
	 *
	 * @param tipoComponente the tipo componente
	 * @param dettAnno0 the dett anno 0
	 * @param dettAnno1 the dett anno 1
	 * @param dettAnno2 the dett anno 2
	 * @return single instance of ElementoComponenteVariazioneSuTreAnniFactory
	 */
	public static final ElementoComponenteVariazione getInstanceNuovaComponente(TipoComponenteImportiCapitolo tipoComponente, ElementoComponenteVariazione elementoComponente, ApplicazioneVariazione applicazioneVariazione ) {
		
		//popolo i dati del dettaglio
		DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno0 = elementoComponente.getDettaglioAnno0();
		dettaglioAnno0.getComponenteImportiCapitolo().setTipoComponenteImportiCapitolo(tipoComponente);
		
		DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno1 = elementoComponente.getDettaglioAnno1();
		
		DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno2 = elementoComponente.getDettaglioAnno2();
		
		ImportiCapitoloComponenteVariazioneModificabile importoModificabile = ImportiCapitoloComponenteVariazioneModificabile.getImportiCapitoloModificabile(applicazioneVariazione, tipoComponente.getMacrotipoComponenteImportiCapitolo(), tipoComponente.getSottotipoComponenteImportiCapitolo(), dettaglioAnno0.getTipoDettaglioComponenteImportiCapitolo());
		return getInstance(dettaglioAnno0, dettaglioAnno1, dettaglioAnno2, importoModificabile.getInserimentoAnno0(), importoModificabile.getInserimentoAnniSuccessivi(), false, true);
	}
	
	/**
	 * Unwrapall by anno.
	 *
	 * @param lista the lista
	 * @param eliminaComponentiConImportiAZero the elimina componenti con importi A zero
	 * @return the map
	 */
	public static final Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> unWrapAllByAnno(List<ElementoComponenteVariazione> lista, boolean eliminaComponentiConImportiAZero) {
		Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> map = new HashMap<String, List<DettaglioVariazioneComponenteImportoCapitolo>>();
		map.put(KEY_ANNO_0, new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>());
		map.put(KEY_ANNO_1, new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>());
		map.put(KEY_ANNO_2, new ArrayList<DettaglioVariazioneComponenteImportoCapitolo>());
		
		for (ElementoComponenteVariazione el : lista) {
			DettaglioVariazioneComponenteImportoCapitolo dettAnno0 = el.getDettaglioAnno0();
			DettaglioVariazioneComponenteImportoCapitolo dettAnno1 = el.getDettaglioAnno1();
			DettaglioVariazioneComponenteImportoCapitolo dettAnno2 = el.getDettaglioAnno2();
			//SIAC-7692
			if(eliminaComponentiConImportiAZero &&  BigDecimal.ZERO.compareTo(dettAnno0.getImporto()) ==0 && BigDecimal.ZERO.compareTo(dettAnno1.getImporto()) == 0 && BigDecimal.ZERO.compareTo(dettAnno2.getImporto()) == 0) {
				//per la gestione delle componenti di default, quando inserisco un nuovo componente, elimino quelle a zero.
				continue;
			}
			
			popolaDettagli(dettAnno0, dettAnno1, dettAnno2, el.getEliminaSuTuttiGliAnni(), el.getNuovaComponente());
			
			addDettagliSuMappa(map, dettAnno0, dettAnno1, dettAnno2);
			
			
		}
		if(map.get(KEY_ANNO_0).isEmpty()) {
			popolaMappaConValoriDefault(map, lista);
		}
		
		return map;
	}


	/**
	 * @param map
	 * @param dettAnno0
	 * @param dettAnno1
	 * @param dettAnno2
	 */
	private static void addDettagliSuMappa(Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> map,	DettaglioVariazioneComponenteImportoCapitolo dettAnno0,
			DettaglioVariazioneComponenteImportoCapitolo dettAnno1,
			DettaglioVariazioneComponenteImportoCapitolo dettAnno2) {
		map.get(KEY_ANNO_0).add(dettAnno0);
		map.get(KEY_ANNO_1).add(dettAnno1);
		map.get(KEY_ANNO_2).add(dettAnno2);
	}
	



	private static void popolaMappaConValoriDefault(Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> map, List<ElementoComponenteVariazione> lista) {
		//SIAC-7384: la gestione delle componenti cosi' come e' non va bene, ma per ora fixo cosi' in attesa di risoluzioni migliori, mi tappo il naso anche se non e' tanto la soluzione migliore
		DettaglioVariazioneComponenteImportoCapitolo dettAnno0Default = null;
		DettaglioVariazioneComponenteImportoCapitolo dettAnno1Default = null;
		DettaglioVariazioneComponenteImportoCapitolo dettAnno2Default = null;
		
		for (int i = 0; i < lista.size(); i++) {
			ElementoComponenteVariazione el = lista.get(i);
			
			if(Boolean.TRUE.equals(el.getLegataAlCapitolo())) {
				popolaDettagli(el.getDettaglioAnno0(),  el.getDettaglioAnno1(), el.getDettaglioAnno2(), el.getEliminaSuTuttiGliAnni(), el.getNuovaComponente());
				addDettagliSuMappa(map, el.getDettaglioAnno0(), el.getDettaglioAnno1(), el.getDettaglioAnno2());
				return;
			}
			
			if(i == 0) {
				dettAnno0Default = el.getDettaglioAnno0();
				dettAnno1Default = el.getDettaglioAnno1();
				dettAnno2Default = el.getDettaglioAnno2();
				popolaDettagli(dettAnno0Default, dettAnno1Default, dettAnno2Default, el.getEliminaSuTuttiGliAnni(), el.getNuovaComponente());
				addDettagliSuMappa(map, dettAnno0Default, dettAnno1Default, dettAnno2Default);
			}
			
		}
	}


	/**
	 * @param el
	 * @param dettAnno0
	 * @param dettAnno1
	 * @param dettAnno2
	 * @param nuovaComponente 
	 * @param eliminaSuTuttiGliAnni 
	 * @param tipoDettaglioComponenteImportiCapitolo
	 * @param tipoComponenteImportiCapitolo
	 */
	private static void popolaDettagli(DettaglioVariazioneComponenteImportoCapitolo dettAnno0, DettaglioVariazioneComponenteImportoCapitolo dettAnno1,DettaglioVariazioneComponenteImportoCapitolo dettAnno2, Boolean eliminaSuTuttiGliAnni, Boolean nuovaComponente) {
		TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo = dettAnno0.getTipoDettaglioComponenteImportiCapitolo() != null? dettAnno0.getTipoDettaglioComponenteImportiCapitolo() : TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO;
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = dettAnno0.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo();
		//popolo i dati mancanti dell'anno 0
		dettAnno0.setFlagEliminaComponenteCapitolo(Boolean.TRUE.equals(eliminaSuTuttiGliAnni));
		dettAnno0.setFlagNuovaComponenteCapitolo(Boolean.TRUE.equals(nuovaComponente));
		
		dettAnno0.setTipoDettaglioComponenteImportiCapitolo(tipoDettaglioComponenteImportiCapitolo);
		
		//popolo i dati mancanti sull'anno 2
		
		if(dettAnno1.getComponenteImportiCapitolo() == null ) {
			dettAnno1.setComponenteImportiCapitolo(new ComponenteImportiCapitolo());
		}
		dettAnno1.getComponenteImportiCapitolo().setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		dettAnno1.setTipoDettaglioComponenteImportiCapitolo(tipoDettaglioComponenteImportiCapitolo);
		dettAnno1.setFlagEliminaComponenteCapitolo(Boolean.TRUE.equals(eliminaSuTuttiGliAnni));
		dettAnno1.setFlagNuovaComponenteCapitolo(Boolean.TRUE.equals(nuovaComponente));
		
		//popolo i dati mancanti sull'anno 1
		
		if(dettAnno2.getComponenteImportiCapitolo() == null ) {
			dettAnno2.setComponenteImportiCapitolo(new ComponenteImportiCapitolo());
		}
		dettAnno2.getComponenteImportiCapitolo().setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		dettAnno2.setTipoDettaglioComponenteImportiCapitolo(tipoDettaglioComponenteImportiCapitolo);
		dettAnno2.setFlagEliminaComponenteCapitolo(Boolean.TRUE.equals(eliminaSuTuttiGliAnni));
		dettAnno2.setFlagNuovaComponenteCapitolo(Boolean.TRUE.equals(nuovaComponente));
	}
	
	
	private static ElementoComponenteVariazione getInstance(DettaglioVariazioneComponenteImportoCapitolo dett0,	DettaglioVariazioneComponenteImportoCapitolo dett1, DettaglioVariazioneComponenteImportoCapitolo dett2,
			Boolean modificabileAnno0, Boolean modificabileAnniSuccessivi, boolean eliminaSuTuttiGliAnni, boolean nuovaComponente) {
		return getInstance(dett0,	dett1, dett2, modificabileAnno0, modificabileAnniSuccessivi, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, eliminaSuTuttiGliAnni, nuovaComponente);
	}
	
	
	private static ElementoComponenteVariazione getInstance(DettaglioVariazioneComponenteImportoCapitolo dett0,	DettaglioVariazioneComponenteImportoCapitolo dett1, DettaglioVariazioneComponenteImportoCapitolo dett2,
			Boolean modificabileAnno0, Boolean modificabileAnniSuccessivi, BigDecimal importoComponenteOriginaleCapitoloAnno0, BigDecimal importoComponenteOriginaleCapitoloAnno1, BigDecimal importoComponenteOriginaleCapitoloAnno2,  boolean eliminaSuTuttiGliAnni, boolean nuovaComponente) {
		return getInstance( dett0,	 dett1, dett2, modificabileAnno0, 
				modificabileAnniSuccessivi,  importoComponenteOriginaleCapitoloAnno0,  importoComponenteOriginaleCapitoloAnno1,
				importoComponenteOriginaleCapitoloAnno2,   eliminaSuTuttiGliAnni, nuovaComponente, Boolean.FALSE);
	}
	
	/**
	 * @param dett02
	 * @param dett1
	 * @param dett0
	 * @param modificabileAnno0
	 * @param modificabileAnniSuccessivi
	 * @param modificabileAnno0
	 * @param modificabileAnniSuccessivi
	 * @return
	 */
	private static ElementoComponenteVariazione getInstance(DettaglioVariazioneComponenteImportoCapitolo dett0,	DettaglioVariazioneComponenteImportoCapitolo dett1, DettaglioVariazioneComponenteImportoCapitolo dett2,
			Boolean modificabileAnno0, Boolean modificabileAnniSuccessivi, BigDecimal importoComponenteOriginaleCapitoloAnno0, BigDecimal importoComponenteOriginaleCapitoloAnno1, BigDecimal importoComponenteOriginaleCapitoloAnno2,  boolean eliminaSuTuttiGliAnni, boolean nuovaComponente, Boolean legataAlCapitolo) {
		ElementoComponenteVariazione instance = new ElementoComponenteVariazione();
		instance.setDettaglioAnno0(dett0);
		instance.setImportoModificabileAnno0(modificabileAnno0);
		
		
		instance.setDettaglioAnno1(dett1);
		instance.setImportoModificabileAnno1(modificabileAnniSuccessivi);
		
		
		instance.setDettaglioAnno2(dett2);
		instance.setImportoModificabileAnno2(modificabileAnniSuccessivi);
		
		instance.setEliminaSuTuttiGliAnni(Boolean.valueOf(eliminaSuTuttiGliAnni));
		instance.setNuovaComponente(Boolean.valueOf(nuovaComponente));
		//SIAC-8198 refuso nel ternario veniva sempre usato l'importo importoComponenteOriginaleCapitoloAnno0 in tutte e tre le annualita'
		instance.setImportoComponenteOriginaleCapitoloAnno0(importoComponenteOriginaleCapitoloAnno0 != null ? importoComponenteOriginaleCapitoloAnno0.setScale(2, RoundingMode.HALF_DOWN) : BigDecimal.ZERO);
		instance.setImportoComponenteOriginaleCapitoloAnno1(importoComponenteOriginaleCapitoloAnno1 != null ? importoComponenteOriginaleCapitoloAnno1.setScale(2, RoundingMode.HALF_DOWN) : BigDecimal.ZERO);
		instance.setImportoComponenteOriginaleCapitoloAnno2(importoComponenteOriginaleCapitoloAnno2 != null ? importoComponenteOriginaleCapitoloAnno2.setScale(2, RoundingMode.HALF_DOWN) : BigDecimal.ZERO);
		
		instance.setLegataAlCapitolo(legataAlCapitolo != null? legataAlCapitolo : null);
		
		return instance;
	}


	/**
	 * Gets the importo componente originale from dettaglio.
	 *
	 * @param dettVariazione the dett variazione
	 * @return the importo componente originale from dettaglio
	 */
	private static BigDecimal getImportoComponenteOriginaleFromDettaglio(DettaglioVariazioneComponenteImportoCapitolo dettVariazione) {
		if(dettVariazione== null) {
			return BigDecimal.ZERO;
		}
		ComponenteImportiCapitolo componenteCapitolo = dettVariazione.getComponenteImportiCapitolo();
		TipoDettaglioComponenteImportiCapitolo tipoDettaglioVariazione = dettVariazione.getTipoDettaglioComponenteImportiCapitolo();
		if( componenteCapitolo == null || componenteCapitolo.getUid() == 0 || tipoDettaglioVariazione == null || componenteCapitolo.getListaDettaglioComponenteImportiCapitolo() == null || componenteCapitolo.getListaDettaglioComponenteImportiCapitolo().isEmpty()) {
			return BigDecimal.ZERO;
		}
		for (DettaglioComponenteImportiCapitolo dettaglioCapitolo : componenteCapitolo.getListaDettaglioComponenteImportiCapitolo()) {
			if(dettVariazione.getTipoDettaglioComponenteImportiCapitolo().equals(dettaglioCapitolo.getTipoDettaglioComponenteImportiCapitolo())) {
				return dettaglioCapitolo.getImporto() != null? dettaglioCapitolo.getImporto() : BigDecimal.ZERO;
			}
		}
		return BigDecimal.ZERO;
	}


	private static void popolaListaSuTreAnnualita(List<DettaglioVariazioneComponenteImportoCapitolo> list) {
		final String methodName ="getInstances";
		int listSize = list.size();
		
		if(listSize==3) {
			return;
		}
		
		LOG.warn(methodName, "Non sono presenti componenti per 3 annualita'. Popolo forzosamente gli anni successivi. Annualita' presenti: " + listSize);
		DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno0 = list.get(0);
		
		DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno1 = copiaDettaglioSuNuovaAnnualita(dettaglioAnno0);
		list.add(dettaglioAnno1);
		
		DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno2 = copiaDettaglioSuNuovaAnnualita(dettaglioAnno0);
		list.add(dettaglioAnno2);
		
	}

	
	

	private static DettaglioVariazioneComponenteImportoCapitolo copiaDettaglioSuNuovaAnnualita(DettaglioVariazioneComponenteImportoCapitolo dt) {// TODO Auto-generated method stub
		DettaglioVariazioneComponenteImportoCapitolo dtClone = new DettaglioVariazioneComponenteImportoCapitolo();
		dtClone.setDettaglioVariazioneImportoCapitolo(dt.getDettaglioVariazioneImportoCapitolo());
		dtClone.setComponenteImportiCapitolo(dt.getComponenteImportiCapitolo());
		dtClone.getComponenteImportiCapitolo().setUid(0);
		dtClone.setImporto(BigDecimal.ZERO);
		dtClone.setTipoDettaglioComponenteImportiCapitolo(dt.getTipoDettaglioComponenteImportiCapitolo());
		return dtClone;
	}


	private static  DettaglioVariazioneComponenteImportoCapitolo buildDettaglioVariazioneComponenteImportoCapitolo(ComponenteImportiCapitolo componente, TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo, TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo, BigDecimal importo) {
		DettaglioVariazioneComponenteImportoCapitolo dettaglio = new DettaglioVariazioneComponenteImportoCapitolo();
		dettaglio.setComponenteImportiCapitolo(componente != null? componente : new ComponenteImportiCapitolo());
		dettaglio.getComponenteImportiCapitolo().setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		dettaglio.setTipoDettaglioComponenteImportiCapitolo(tipoDettaglioComponenteImportiCapitolo);
		if(importo != null) {
			dettaglio.setImporto(importo);
		}
		
		return dettaglio;
	}
	
}
