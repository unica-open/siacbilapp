/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionBilUtil;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Codifica;

/**
 * Classe di factory per l'ElementoCapitoloVariazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public final class ElementoCapitoloVincoloFactory {
	
	private static final String UNDEFINED = "--";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCapitoloVincoloFactory() {
	}
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un capitolo.
	 * 
	 * @param capitolo    il capitolo da wrappare
	 * @param gestioneUEB se l'ente gestisce le UEB o meno
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVincolo getInstance(CapitoloUscitaPrevisione capitolo, Boolean gestioneUEB) {
		if(capitolo == null || capitolo.getTipoCapitolo() == null) {
			return null;
		}
		
		ElementoCapitoloVincolo result = new ElementoCapitoloVincolo();
		
		popolaCampiComuni(result, capitolo, gestioneUEB);
		
		popolaClassificazione(result, capitolo.getProgramma(), capitolo.getMacroaggregato());
		//SIAC-8042
		List<ImportiCapitoloUP> listaImporti = CoreUtil.checkList(capitolo.getListaImportiCapitolo());
		popolaImporti(result, listaImporti, capitolo.getAnnoCapitolo());
		
		return result;
	}
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un capitolo.
	 * 
	 * @param capitolo    il capitolo da wrappare
	 * @param gestioneUEB se l'ente gestisce le UEB o meno
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVincolo getInstance(CapitoloUscitaGestione capitolo, Boolean gestioneUEB) {
		if(capitolo == null || capitolo.getTipoCapitolo() == null) {
			return null;
		}
		
		ElementoCapitoloVincolo result = new ElementoCapitoloVincolo();
		
		popolaCampiComuni(result, capitolo, gestioneUEB);
		
		popolaClassificazione(result, capitolo.getProgramma(), capitolo.getMacroaggregato());
		//SIAC-8042
		List<ImportiCapitoloUG> listaImporti = CoreUtil.checkList(capitolo.getListaImportiCapitolo());
		popolaImporti(result, listaImporti, capitolo.getAnnoCapitolo());
		
		return result;
	}
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un capitolo.
	 * 
	 * @param capitolo    il capitolo da wrappare
	 * @param gestioneUEB se l'ente gestisce le UEB o meno
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVincolo getInstance(CapitoloEntrataPrevisione capitolo, Boolean gestioneUEB) {
		if(capitolo == null || capitolo.getTipoCapitolo() == null) {
			return null;
		}
		
		ElementoCapitoloVincolo result = new ElementoCapitoloVincolo();
		
		popolaCampiComuni(result, capitolo, gestioneUEB);
		
		popolaClassificazione(result, capitolo.getCategoriaTipologiaTitolo());
		//SIAC-8042
		List<ImportiCapitoloEP> listaImporti = CoreUtil.checkList(capitolo.getListaImportiCapitolo());
		popolaImporti(result, listaImporti, capitolo.getAnnoCapitolo());
		
		return result;
	}
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un capitolo.
	 * 
	 * @param capitolo    il capitolo da wrappare
	 * @param gestioneUEB se l'ente gestisce le UEB o meno
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoCapitoloVincolo getInstance(CapitoloEntrataGestione capitolo, Boolean gestioneUEB) {
		if(capitolo == null || capitolo.getTipoCapitolo() == null) {
			return null;
		}
		
		ElementoCapitoloVincolo result = new ElementoCapitoloVincolo();
		
		popolaCampiComuni(result, capitolo, gestioneUEB);
		
		popolaClassificazione(result, capitolo.getCategoriaTipologiaTitolo());
		//SIAC-8042
		List<ImportiCapitoloEG> listaImporti = CoreUtil.checkList(capitolo.getListaImportiCapitolo());
		popolaImporti(result, listaImporti, capitolo.getAnnoCapitolo());
		
		return result;
	}
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un capitolo.
	 * @param <C> la tipizzazione del capitolo
	 * @param capitolo    il capitolo da wrappare
	 * @param gestioneUEB se l'ente gestisce le UEB o meno
	 * 
	 * @return il wrapper creato
	 */
	public static <C extends Capitolo<?, ?>> ElementoCapitoloVincolo getInstance(C capitolo, Boolean gestioneUEB) {
		if(capitolo == null || capitolo.getTipoCapitolo() == null) {
			return null;
		}
		
		ElementoCapitoloVincolo result = null;
		Capitolo<?, ?> nuovaIstanza = capitolo;
		
		try {
			// Controllo se la classe del capitolo fornito in input sia effettivamente quella della superclasse, o sia quella di una sottoclasse
			if(Capitolo.class.equals(capitolo.getClass())) {
				// Sono nella superclasse. Innanzitutto, devo creare una nuova istanza del capitolo sì da non avere problemi di invocazione circolare
				nuovaIstanza =  ReflectionBilUtil.getInstanceFromCapitoloBaseClass(capitolo);
			}
			// Ho un'istanza di una sottoclasse del capitolo
			Method method = ElementoCapitoloVincoloFactory.class.getMethod("getInstance", nuovaIstanza.getClass(), Boolean.class);
			result = (ElementoCapitoloVincolo) method.invoke(null, nuovaIstanza, gestioneUEB);
		} catch(Exception e) {
			new LogUtil(ElementoCapitoloVincoloFactory.class).error("getInstance", "", e);
		}
		
		return result;
	}
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un capitolo.
	 * @param <C> la tipizzazione del capitolo
	 * @param capitoli    i capitoli da wrappare
	 * @param gestioneUEB se l'ente gestisce le UEB o meno
	 * 
	 * @return il wrapper creato
	 */
	public static <C extends Capitolo<?, ?>> List<ElementoCapitoloVincolo> getInstances(List<C> capitoli, Boolean gestioneUEB) {
		List<ElementoCapitoloVincolo> result = new ArrayList<ElementoCapitoloVincolo>();
		
		for(C capitolo : capitoli) {
			result.add(getInstance(capitolo, gestioneUEB));
		}
		
		return result;
	}
	
	/**
	 * Imposta i campi comuni all'interno del wrapper.
	 * 
	 * @param wrapper     il wrapper da popolare
	 * @param capitolo    il capitolo tramite cui popolare il wrapper
	 * @param gestioneUEB se l'ente gestisce la UEB o meno
	 */
	private static <C extends Capitolo<?, ?>> void popolaCampiComuni(ElementoCapitoloVincolo wrapper, C capitolo, Boolean gestioneUEB) {
		int uid = capitolo.getUid();
		StringBuilder anagraficaCapitolo = new StringBuilder();
		anagraficaCapitolo.append(capitolo.getNumeroCapitolo())
			.append("/")
			.append(capitolo.getNumeroArticolo());
		if(Boolean.TRUE.equals(gestioneUEB)) {
			anagraficaCapitolo.append("/")
			.append(capitolo.getNumeroUEB());
		}
		
		StringBuilder sbDescrizione = new StringBuilder()
			.append(capitolo.getDescrizione());
		if(StringUtils.isNotBlank(capitolo.getDescrizioneArticolo())) {
			sbDescrizione.append(" - ")
				.append(capitolo.getDescrizioneArticolo());
		}
		String descrizione = sbDescrizione.toString();
		String strutturaAmministrativoContabile = capitolo.getStrutturaAmministrativoContabile().getCodice();
		
		wrapper.setUid(uid);
		wrapper.setCapitolo(anagraficaCapitolo.toString());
		wrapper.setDescrizione(descrizione);
		wrapper.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
	}
	
	/**
	 * Popola la classificazione del wrapper a partire da un elenco di codifiche.
	 * 
	 * @param wrapper   il wrapper da popolare
	 * @param codifiche l'elenco di codifiche da cui ottenere la classificazione
	 */
	private static void popolaClassificazione(ElementoCapitoloVincolo wrapper, Codifica... codifiche) {
		StringBuilder result = new StringBuilder();
		for(Codifica c : codifiche) {
			result.append((c == null || c.getCodice() == null) ? UNDEFINED : c.getCodice())
				.append("-");
		}
		String str = result.toString();
		wrapper.setClassificazione(StringUtils.substringBeforeLast(str, "-"));
	}
	
	/**
	 * Popola gli importi per il wrapper.
	 * 
	 * @param wrapper      il wrapper da popolare
	 * @param listaImporti la lista da cui ottenere gli importi da popolare
	 * @param annoCapitolo l'anno del capitolo
	 */
	private static <I extends ImportiCapitolo> void popolaImporti(ElementoCapitoloVincolo wrapper, List<I> listaImporti, Integer annoCapitolo) {
		int annoCapitoloInt = annoCapitolo.intValue();
		I importoAnno0 = cercaImportoPerAnno(listaImporti, annoCapitoloInt + 0);
		I importoAnno1 = cercaImportoPerAnno(listaImporti, annoCapitoloInt + 1);
		I importoAnno2 = cercaImportoPerAnno(listaImporti, annoCapitoloInt + 2);
		
		//SIAC-8042
		wrapper.setCompetenzaAnno0(importoAnno0 != null && importoAnno0.getStanziamento() != null ? importoAnno0.getStanziamento() : BigDecimal.ZERO);
		wrapper.setCompetenzaAnno1(importoAnno1 != null && importoAnno1.getStanziamento() != null ? importoAnno1.getStanziamento() : BigDecimal.ZERO);
		wrapper.setCompetenzaAnno2(importoAnno2 != null && importoAnno2.getStanziamento() != null ? importoAnno2.getStanziamento() : BigDecimal.ZERO);
		//
	}
	
	/**
	 * Cerca l'importo del capitolo a partire dall'anno.
	 * 
	 * @param listaImporti la lista in cui cercare l'importo
	 * @param anno         l'anno dell'importo da cercare
	 * 
	 * @return l'importo corrispondente all'anno, se presente; <code>null</code> se tale importo non esiste
	 */
	private static <I extends ImportiCapitolo> I cercaImportoPerAnno(List<I> listaImporti, int anno) {
		I result = null;
		
		for(int i = 0; i < listaImporti.size() && result == null; i++) {
			I importo = listaImporti.get(i);
			if(anno == importo.getAnnoCompetenza().intValue()) {
				result = importo;
			}
		}
		
		return result;
	}
	
	
}
