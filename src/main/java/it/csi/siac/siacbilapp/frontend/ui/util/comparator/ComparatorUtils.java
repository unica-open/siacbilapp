/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.comparator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe di utilit&agrave; per le comparazioni.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 * 
 */
public final class ComparatorUtils {

	private static final LogUtil LOG = new LogUtil(ComparatorUtils.class);

	/** Non permettere l'instanziazione */
	private ComparatorUtils() {
	}

	/**
	 * Ordina una lista a partire dal codice.
	 * @param <T> la tipizzazione della codifica
	 * @param list la lista da ordinare
	 */
	public static <T extends Codifica> void sortByCodice(List<T> list) {
		if (list != null) {
			Collections.sort(list, ComparatorCodifica.INSTANCE);
		} else {
			LOG.debug("sortByCodice", "La lista fornita in input e' null");
		}
	}

	/**
	 * Ordina una lista a partire dal codice.
	 * @param <T> la tipizzazione della codifica fin
	 * @param list la lista da ordinare
	 */
	public static <T extends CodificaFin> void sortByCodiceFin(List<T> list) {
		if (list != null) {
			Collections.sort(list, ComparatorCodificaFin.INSTANCE);
		}
	}

	/**
	 * Ordina una lista in profondit&agrave; a partire dal codice.
	 * @param <T> la tipizzazione della codifica
	 * @param list la lista da ordinare
	 */
	public static <T extends Codifica> void sortDeepByCodice(List<T> list) {
		if (list == null || list.isEmpty()) {
			LOG.debug("sortDeepByCodice", "La lista fornita in input e' null o vuota");
			return;
		}
		T primoElemento = list.get(0);
		String metodoDaChiamarePerOttenereIFigliDelClassificatore = "";

		// La lista esiste, e pertanto posso effettuare l'ordinamento
		if (primoElemento instanceof ElementoPianoDeiConti) {
			metodoDaChiamarePerOttenereIFigliDelClassificatore = "getElemPdc";
		} else if (primoElemento instanceof StrutturaAmministrativoContabile) {
			metodoDaChiamarePerOttenereIFigliDelClassificatore = "getSubStrutture";
		} else if (primoElemento instanceof SiopeEntrata || primoElemento instanceof SiopeSpesa) {
			metodoDaChiamarePerOttenereIFigliDelClassificatore = "getFigli";
		} else {
			throw new UnsupportedOperationException("Il metodo non e' attualmente implementato");
		}
		try {
			sortDeepByCodice(list, metodoDaChiamarePerOttenereIFigliDelClassificatore);
		} catch (Exception e) {
			// Non dovrebbe succedere
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Ordina una lista in profondit&agrave; a partire dal codice.
	 * @param <T> la tipizzazione della codifica
	 * @param list       la lista da ordinare
	 * @param methodName il nome del metodo da invocare
	 * 
	 * @throws IllegalAccessException in caso di errore di accesso al metodo
	 * @throws InvocationTargetException in caso di errore di invocazione del metodo
	 * @throws NoSuchMethodException in caso di non esistenza del metodo
	 */
	private static <T extends Codifica> void sortDeepByCodice(List<T> list, String methodName) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Collections.sort(list, ComparatorCodifica.INSTANCE);
		for (T t : list) {
			@SuppressWarnings("unchecked")
			List<T> subList = (List<T>) t.getClass().getMethod(methodName).invoke(t);
			if (subList != null && !subList.isEmpty()) {
				sortDeepByCodice(subList, methodName);
			}
		}
	}

	/**
	 * Ordina una lista a partire dall'ordine.
	 * @param <T> la tipizzazione della codifica
	 * @param list la lista da ordinare
	 */
	public static <T extends ClassificatoreGerarchico> void sortByOrdine(List<T> list) {
		if (list != null) {
			Collections.sort(list, ComparatorClassificatoreGerarchico.INSTANCE);
		} else {
			LOG.debug("sortByCodice", "La lista fornita in input e' null");
		}
	}

	/**
	 * Ordina una lista in profondit&agrave; a partire dal codice.
	 * @param <T> la tipizzazione della codifica
	 * @param list la lista da ordinare
	 */
	public static <T extends ClassificatoreGerarchico> void sortDeepByOrdine(List<T> list) {
		if (list == null || list.isEmpty()) {
			LOG.debug("sortDeepByCodice", "La lista fornita in input e' null o vuota");
			return;
		}
		try {
			if (list.get(0) instanceof ElementoPianoDeiConti) {
				sortDeepByOrdine(list, "getElemPdc");
			} else if (list.get(0) instanceof StrutturaAmministrativoContabile) {
				sortDeepByOrdine(list, "getSubStrutture");
			} else {
				throw new UnsupportedOperationException("Il metodo non e' attualmente implementato");
			}
		} catch (Exception e) {
			// Non dovrebbe succedere
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Ordina una lista in profondit&agrave; a partire dal codice.
	 * @param <T> la tipizzazione della codifica
	 * @param list       la lista da ordinare
	 * @param methodName il nome del metodo da invocare
	 * 
	 * @throws IllegalAccessException in caso di errore di accesso al metodo
	 * @throws InvocationTargetException in caso di errore di invocazione del metodo
	 * @throws NoSuchMethodException in caso di non esistenza del metodo
	 */
	private static <T extends ClassificatoreGerarchico> void sortDeepByOrdine(List<T> list, String methodName) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Collections.sort(list, ComparatorClassificatoreGerarchico.INSTANCE);
		for (T t : list) {
			@SuppressWarnings("unchecked")
			List<T> subList = (List<T>) t.getClass().getMethod(methodName).invoke(t);
			if (subList != null && !subList.isEmpty()) {
				sortDeepByOrdine(subList, methodName);
			}
		}
	}

	/**
	 * Ordina una lista a partire dall'uid.
	 * @param <T> la tipizzazione dell'entit&agrave;
	 * @param list la lista da ordinare
	 */
	public static <T extends Entita> void sortByUid(List<T> list) {
		if (list != null) {
			Collections.sort(list, ComparatorEntita.INSTANCE);
		} else {
			LOG.debug("sortByCodice", "La lista fornita in input e' null");
		}
	}

	/**
	 * Effettua una ricerca all'interno della lista tramite l'utilizzo del comparatore. La complessit&agrave; computazionale &eacute; pari a <code>O(n)</code>.
	 * @param <T> la tipizzazione dell'entit&agrave;
	 * @param list       la lista in cui cercare il risultato
	 * @param entita     l'entit&agrave; da ricercare nella lista
	 * @param comparator il comparatore per effettuare il confronto
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	private static <T extends Entita> T search(List<T> list, T entita, Comparator<? super T> comparator) {
		final String methodName = "search";
		T result = entita;
		if (entita == null || entita.getUid() == 0) {
			LOG.debug(methodName, "Oggetto da cercare non valido");
			return result;
		}
		if (list == null) {
			LOG.debug(methodName, "Lista non inizializzata");
			return result;
		}

		boolean found = false;
		// Se non ho la lista, esco subito

		/* Complessita' computazionale: O(n) */
		for (int i = 0; i < list.size() && !found; i++) {
			if (comparator.compare(entita, list.get(i)) == 0) {
				LOG.debug(methodName, entita.getClass().getSimpleName() + " trovato in posizione " + i);
				found = true;
				try {
					result = list.get(i);
				} catch (ClassCastException e) {
					LOG.debug(methodName, "Errore nel cast dell'oggetto: " + e.getMessage());
					result = entita;
				}
			}
		}

		return result;
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid.
	 * @param <T> la tipizzazione dell'entit&agrave;
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	public static <T extends Entita> T searchByUid(List<T> list, T entita) {
		return search(list, entita, ComparatorEntita.INSTANCE);
	}

	/**
	 * Effettua una ricerca all'interno della lista tramite l'utilizzo del comparatore. La complessit&agrave; computazionale &eacute; pari a <code>O(n)</code>.
	 * @param <T> la tipizzazione dell'entit&agrave;
	 * @param list       la lista in cui cercare il risultato
	 * @param entita     l'entit&agrave; da ricercare nella lista
	 * @param comparator il comparatore per effettuare il confronto
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; <code>null</code> in caso tale elemento non sia reperito
	 */
	private static <T extends Entita> T searchEventuallyNull(List<T> list, T entita, Comparator<? super T> comparator) {
		final String methodName = "searchEventuallyNull";
		if (entita == null || entita.getUid() == 0) {
			LOG.debug(methodName, "Oggetto da cercare non valido");
			return null;
		}
		if (list == null) {
			LOG.debug(methodName, "Lista non inizializzata");
			return null;
		}

		T result = null;
		boolean found = false;
		// Se non ho la lista, esco subito

		/* Complessita' computazionale: O(n) */
		for (int i = 0; i < list.size() && !found; i++) {
			if (comparator.compare(entita, list.get(i)) == 0) {
				LOG.debug(methodName, "Oggetto trovato in posizione " + i);
				found = true;
				try {
					result = list.get(i);
				} catch (ClassCastException e) {
					LOG.debug(methodName, "Errore nel cast dell'oggetto: " + e.getMessage());
					result = entita;
				}
			}
		}

		return result;
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid.
	 * 
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * @param <T>    la classe estendente {@link Entita} dell'oggetto da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; <code>null</code> in caso tale elemento non sia reperito
	 */
	public static <T extends Entita> T searchByUidEventuallyNull(List<T> list, T entita) {
		return searchEventuallyNull(list, entita, ComparatorEntita.INSTANCE);
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid.
	 * 
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	public static ElementoCapitoloVariazione searchByUid(List<ElementoCapitoloVariazione> list, ElementoCapitoloVariazione entita) {
		if (entita == null || entita.getUid() == 0) {
			return null;
		}
		Comparator<ModelWrapper> comparator = ComparatorModelWrapper.INSTANCE;

		boolean found = false;
		ElementoCapitoloVariazione result = null;
		if (list != null) {
			/* Complessita' computazionale: O(n) */
			for (int i = 0; i < list.size() && !found; i++) {
				if (comparator.compare(entita, list.get(i)) == 0) {
					found = true;
					result = list.get(i);
				}
			}
		}
		return result;
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid.
	 * 
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	public static ElementoCapitoloVariazione searchDeepByUid(List<ElementoCapitoloVariazione> list, ElementoCapitoloVariazione entita) {
		if (entita == null || entita.getUid() == 0) {
			return null;
		}
		Comparator<ModelWrapper> comparator = ComparatorModelWrapper.INSTANCE;

		boolean found = false;
		ElementoCapitoloVariazione result = null;
		if (list != null) {
			/* Complessita' computazionale: O(n) */
			for (int i = 0; i < list.size() && !found; i++) {
				ElementoCapitoloVariazione e = list.get(i);
				if (comparator.compare(entita, e) == 0) {
					result = list.get(i);
				} else {
					result = searchDeepByUid(e.getListaSottoElementi(), entita);
				}
				if (result != null) {
					found = true;
				}
			}
		}
		return result;
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid. Questo metodo cerca anche nei figli della lista fornita.
	 * @param <T> la tipizzazione del classificatore
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	public static <T extends ClassificatoreGerarchico> T searchByUidWithChildren(List<T> list, T entita) {
		T result = null;
		try {
			if (entita instanceof ElementoPianoDeiConti) {
				result = searchWithChildren(list, entita, ComparatorEntita.INSTANCE, true, "getElemPdc");
			} else if (entita instanceof StrutturaAmministrativoContabile) {
				result = searchWithChildren(list, entita, ComparatorEntita.INSTANCE, true, "getSubStrutture");
			} else if (entita instanceof SiopeSpesa || entita instanceof SiopeEntrata) {
				result = searchWithChildren(list, entita, ComparatorEntita.INSTANCE, true, "getFigli");
			}
		} catch (Exception e) {
			LOG.error("searchByUidWithChildren", "Errore: " + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid. Questo metodo cerca anche nei figli della lista fornita.
	 * 
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	public static ElementoPianoDeiConti searchByUidWithChildren(List<ElementoPianoDeiConti> list, ElementoPianoDeiConti entita) {
		ElementoPianoDeiConti elementoPianoDeiConti = null;
		try {
			elementoPianoDeiConti = searchWithChildren(list, entita, ComparatorEntita.INSTANCE, true, "getElemPdc");
		} catch (Exception e) {
			LOG.error("searchByUidWithChildren", "Errore: " + e.getMessage(), e);
		}
		return elementoPianoDeiConti;
	}

	/**
	 * Effettua una ricerca all'interno della lista a partire dall'uid. Questo metodo cerca anche nei figli della lista fornita.
	 * 
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 */
	public static StrutturaAmministrativoContabile searchByUidWithChildren(List<StrutturaAmministrativoContabile> list, StrutturaAmministrativoContabile entita) {
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = null;
		try {
			strutturaAmministrativoContabile = searchWithChildren(list, entita, ComparatorEntita.INSTANCE, true, "getSubStrutture");
		} catch (Exception e) {
			LOG.error("searchByUidWithChildren", "Errore: " + e.getMessage(), e);
		}
		return strutturaAmministrativoContabile;
	}

	/**
	 * Effettua una ricerca all'interno della lista di Elementi del Piano dei Conti tramite l'utilizzo del comparatore. Tale metodo effettua una ricerca anche
	 * all'interno delle sottoliste degli elementi presenti nella lista.
	 * 
	 * @param list             la lista in cui cercare il risultato
	 * @param entita           l'entit&agrave; da ricercare nella lista
	 * @param comparator       il comparatore per effettuare il confronto
	 * @param first            definisce la prima iterazione
	 * @param reflectionMethod il nome del metodo da utilizzare nella Reflection
	 * @param <T>              la classe estendente {@link ClassificatoreGerarchico}
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; l'entit&agrave; fornita come parametro in caso tale elemento non sia reperito
	 * 
	 * @throws NoSuchMethodException     nel caso in cui non esista il metodo considerato
	 * @throws InvocationTargetException nel caso in cui non si possa invocare il metodo sul tagret indicato
	 * @throws IllegalAccessException    nel caso in cui vi sia un accesso a un campo privato
	 * @throws SecurityException         nel caso di eccezione di sicurezza
	 * @throws IllegalArgumentException  nel caso in cui l'argomento della chiamata non sia legale
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ClassificatoreGerarchico> T searchWithChildren(List<T> list, T entita, Comparator<? super T> comparator, boolean first,
			String reflectionMethod) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		final String methodName = "searchWithChildren";
		if (entita == null || entita.getUid() == 0) {
			LOG.debug(methodName, "Oggetto da cercare non valido");
			return null;
		}
		if (list == null) {
			LOG.debug(methodName, "Lista in cui cercare l'oggetto nulla");
			return entita;
		}

		T result = null;
		boolean found = false;
		for (int i = 0; i < list.size() && !found; i++) {
			if (comparator.compare(entita, list.get(i)) == 0) {
				LOG.debug(methodName, "Oggetto trovato in posizione " + i);
				result = list.get(i);
			} else {
				// Ricerca ricorsivamente sulla sottolista
				result = searchWithChildren((List<T>) list.get(i).getClass().getMethod(reflectionMethod).invoke(list.get(i)), entita, comparator, false,
						reflectionMethod);
			}
			if (result != null) {
				found = true;
			}
		}

		// Effettuo un log per sapere il risultato dell'invocazione
		if (first) {
			if (!found) {
				result = entita;
			}
			LOG.debug(methodName, "Oggetto trovato? " + found);
		}
		return result;
	}

	
	@SuppressWarnings("unchecked")
	private static <T extends ClassificatoreGerarchico> T searchWithChildrenByCodice(List<T> list, T entita, Comparator<? super T> comparator, boolean first,
			String reflectionMethod) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		final String methodName = "searchWithChildren";
		if (entita == null || StringUtils.isBlank(entita.getCodice())) {
			LOG.debug(methodName, "Oggetto da cercare non valido");
			return null;
		}
		if (list == null) {
			LOG.debug(methodName, "Lista in cui cercare l'oggetto nulla");
			return entita;
		}

		T result = null;
		boolean found = false;
		for (int i = 0; i < list.size() && !found; i++) {
			if (comparator.compare(entita, list.get(i)) == 0) {
				LOG.debug(methodName, "Oggetto trovato in posizione " + i);
				result = list.get(i);
			} else {
				// Ricerca ricorsivamente sulla sottolista
				result = searchWithChildrenByCodice((List<T>) list.get(i).getClass().getMethod(reflectionMethod).invoke(list.get(i)), entita, comparator, false,
						reflectionMethod);
			}
			if (result != null) {
				found = true;
			}
		}

		// Effettuo un log per sapere il risultato dell'invocazione
		if (first) {
			if (!found) {
				result = entita;
			}
			LOG.debug(methodName, "Oggetto trovato? " + found);
		}
		return result;
	}

	/**
	 * Effettua una ricerca all'interno della lista di SAC. Tale metodo effettua una ricerca anche all'interno delle sottoliste degli elementi presenti nella lista.
	 * <br/>
	 * Pu&ograve; restituire <code>null</code>.
	 * 
	 * @param list   la lista in cui cercare il risultato
	 * @param entita l'entit&agrave; da ricercare nella lista
	 * 
	 * @return l'entit&agrave; corrispondente a partire dalla lista, se presente; <code>null</code> in caso tale elemento non sia reperito
	 */
	public static StrutturaAmministrativoContabile searchWithChildrenPossiblyNull(List<StrutturaAmministrativoContabile> list,
			StrutturaAmministrativoContabile entita) {
		final String methodName = "searchWithChildrenPossiblyNull";
		if (entita == null || entita.getUid() == 0) {
			LOG.debug(methodName, "Oggetto da cercare non valido");
			return null;
		}
		if (list == null) {
			LOG.debug(methodName, "Lista in cui cercare l'oggetto nulla");
			return entita;
		}

		StrutturaAmministrativoContabile result = null;
		boolean found = false;
		for (int i = 0; i < list.size() && !found; i++) {
			if (ComparatorEntita.INSTANCE.compare(entita, list.get(i)) == 0) {
				LOG.debug(methodName, "Oggetto trovato in posizione " + i);
				result = list.get(i);
			} else {
				// Ricerca ricorsivamente sulla sottolista
				result = searchWithChildrenPossiblyNull(list.get(i).getSubStrutture(), entita);
			}
			if (result != null) {
				found = true;
			}
		}
		return result;
	}

	/**
	 * Restituisce la Codifica avente codice pari a quello fornito in input.
	 * @param <C> la tipizzazione della codifica
	 * 
	 * @param list   la lista in cui cercare la codifica
	 * @param codice il codice da ricercare
	 * 
	 * @return la codifica con dato codice, se presente; <code>null</code> in caso contrario
	 */
	public static <C extends Codifica> C findByCodice(List<C> list, String codice) {
		// Se non ho la lista o la codifica, allora ritorno subito null
		if (list == null || codice == null) {
			return null;
		}
		C result = null;
		for (C c : list) {
			if (codice.equals(c.getCodice())) {
				// Ho trovato la codifica di pari codice: esco
				result = c;
				break;
			}
		}
		return result;
	}
	 public static <T extends ClassificatoreGerarchico> T findByCodiceWithChildren(List<T> list, T entita) {
			T result = null;
			try {
				if (entita instanceof ElementoPianoDeiConti) {
					result = searchWithChildrenByCodice(list, entita, ComparatorCodifica.INSTANCE, true, "getElemPdc");
				} else if (entita instanceof StrutturaAmministrativoContabile) {
					result = searchWithChildrenByCodice(list, entita, ComparatorCodifica.INSTANCE, true, "getSubStrutture");
				} else if (entita instanceof SiopeSpesa || entita instanceof SiopeEntrata) {
					result = searchWithChildrenByCodice(list, entita, ComparatorCodifica.INSTANCE, true, "getFigli");
				}
			} catch (Exception e) {
				LOG.error("searchByUidWithChildren", "Errore: " + e.getMessage(), e);
			}
			return result;
		}

	/**
	 * Restituisce l'ImportoCapitolo avente anno pari a quanto passato in input.
	 * @param <T> la tipizzazione degli importi del capitolo
	 * 
	 * @param listaImportiCapitolo la lista da cui ottenere l'importo
	 * @param anno                 l'anno dell'importo da ottenere
	 * 
	 * @return l'importo corrispondente all'anno, se presente; <code>null</code> altrimenti
	 */
	public static <T extends ImportiCapitolo> T searchByAnno(List<T> listaImportiCapitolo, Integer anno) {
		// Fallback per la nullità della lista
		if (listaImportiCapitolo == null || anno == null) {
			return null;
		}

		// Inizializzo a null il risultato
		T result = null;

		for (T t : listaImportiCapitolo) {
			// Controllo l'anno di competenza
			if (t != null && anno.equals(t.getAnnoCompetenza())) {
				// Importo trovato: lo imposto nel risultato
				result = t;
				break;
			}
		}

		return result;
	}

	/**
	 * Restituisce l'ImportoCapitolo avente anno pari a quanto passato in input.
	 * 
	 * @param listaImportiCapitolo la lista da cui ottenere l'importo
	 * @param anno                 l'anno dell'importo da ottenere
	 * 
	 * @return l'importo corrispondente all'anno, se presente; <code>null</code> altrimenti
	 * @deprecated da rimuovere con le UEB
	 */
	@Deprecated
	public static DettaglioVariazioneImportoCapitolo searchByAnnoDettaglio(List<DettaglioVariazioneImportoCapitolo> listaImportiCapitolo, Integer anno) {
		// Fallback per la nullità della lista
		if (listaImportiCapitolo == null) {
			return null;
		}

		// Inizializzo a null il risultato
		DettaglioVariazioneImportoCapitolo result = null;

		// SIAC-6883
//		for (DettaglioVariazioneImportoCapitolo t : listaImportiCapitolo) {
//			// Controllo l'anno di competenza
//			if (t.getAnnoCompetenza().equals(anno)) {
//				// Importo trovato: lo imposto nel risultato
//				result = t;
//				break;
//			}
//		}

		return result;
	}

	/**
	 * Ottiene il massimo valore delle UEB all'interno di una lista.
	 * @param <C> la tipizzazione del capitolo
	 * @param lista la lista da cui ottenere il massimo valore delle UEB
	 * 
	 * @return il valore massimo, nel caso tale valore esista; 1 in caso contrario
	 */
	public static <C extends Capitolo<?, ?>> Integer getMaxUEB(List<C> lista) {
		Integer result = 1;
		// Fallback in caso in cui la lista sia null, vuota, o contenga elementi che non hanno numero UEB.
		if (lista == null || lista.isEmpty()) {
			return result;
		}

		for (C c : lista) {
			Integer i = c.getNumeroUEB();
			if (result.compareTo(i) < 0) {
				result = i;
			}
		}

		return result;
	}

	/**
	 * Trova gli elementi appartenenti ad una determinata lista per numero capitolo, numero articolo e tipo di capitolo
	 * 
	 * @param lista    la lista in cui cercare gli elementi
	 * @param elemento l'elemento da cui estrarre numero del capitolo, numero dell'articolo e tipo di capitolo
	 * 
	 * @return la lista degli elementi corrispondenti
	 */
	public static List<ElementoCapitoloVariazione> findByNumeroCapitoloNumeroArticoloAndTipoCapitolo(List<ElementoCapitoloVariazione> lista,
			ElementoCapitoloVariazione elemento) {
		List<ElementoCapitoloVariazione> result = new ArrayList<ElementoCapitoloVariazione>();

		Integer numeroCapitolo = elemento.getNumeroCapitolo();
		Integer numeroArticolo = elemento.getNumeroArticolo();
		TipoCapitolo tipoCapitolo = elemento.getTipoCapitolo();

		// Ciclo sulla lista in input
		for (ElementoCapitoloVariazione e : lista) {
			if (numeroCapitolo.equals(e.getNumeroCapitolo()) && numeroArticolo.equals(e.getNumeroArticolo()) && tipoCapitolo.equals(e.getTipoCapitolo())) {
				// I valori corrispondono: aggiungo l'elemento alla lista dei capitoli da eliminare
				result.add(e);
			}
		}

		return result;
	}
	/**
	  * Trova gli elementi appartenenti ad una determinata lista per numero capitolo, numero articolo e tipo di capitolo
	  * 
	  * @param lista    la lista in cui cercare gli elementi
	  * @param elemento l'elemento da cui estrarre numero del capitolo, numero dell'articolo e tipo di capitolo
	  * 
	  * @return la lista degli elementi corrispondenti
	  */
	 public static List<ElementoCapitoloCodifiche> findByNumeroCapitoloNumeroArticoloAndTipoCapitolo(List<ElementoCapitoloCodifiche> lista, ElementoCapitoloCodifiche elemento) {
		 List<ElementoCapitoloCodifiche> result = new ArrayList<ElementoCapitoloCodifiche>();
		 
		 Integer numeroCapitolo = elemento.getNumeroCapitolo();
		 Integer numeroArticolo = elemento.getNumeroArticolo();
		 TipoCapitolo tipoCapitolo = elemento.getTipoCapitolo();
		 
		 // Ciclo sulla lista in input
		 for(ElementoCapitoloCodifiche e : lista) {
			 if(numeroCapitolo.equals(e.getNumeroCapitolo()) && numeroArticolo.equals(e.getNumeroArticolo()) && tipoCapitolo.equals(e.getTipoCapitolo())) {
				 // I valori corrispondono: aggiungo l'elemento alla lista dei capitoli da eliminare
				 result.add(e);
			 }
		 }
		 
		 return result;
	 }

	/**
	 * Trova gli elementi appartenenti ad una determinata lista per numero capitolo, numero articolo e tipo di capitolo
	 * @param <C> la tipizzazione del capitolo
	 * @param lista    la lista in cui cercare gli elementi
	 * @param elemento l'elemento da cui estrarre numero del capitolo, numero dell'articolo e tipo di capitolo
	 * 
	 * @return la lista degli elementi corrispondenti
	 */
	public static <C extends Capitolo<?, ?>> List<C> findByNumeroCapitoloNumeroArticoloAndTipoCapitolo(List<C> lista, C elemento) {
		List<C> result = new ArrayList<C>();
		Integer numeroCapitolo = elemento.getNumeroCapitolo();
		Integer numeroArticolo = elemento.getNumeroArticolo();
		TipoCapitolo tipoCapitolo = elemento.getTipoCapitolo();
		for (C e : lista) {
			if (numeroCapitolo.equals(e.getNumeroCapitolo()) && numeroArticolo.equals(e.getNumeroArticolo()) && tipoCapitolo.equals(e.getTipoCapitolo())) {
				result.add(e);
			}
		}
		return result;
	}

	/**
	 * Trova il dettaglio della variazione appartenenti ad una determinata lista per numero capitolo, numero articolo e tipo di capitolo
	 * 
	 * @param lista    la lista in cui cercare gli elementi
	 * @param elemento l'elemento da cui estrarre numero del capitolo, numero dell'articolo e tipo di capitolo
	 * 
	 * @return l'elemento corrispondente
	 */
	public static DettaglioVariazioneImportoCapitolo findCapitoloByNumeroCapitoloNumeroArticoloNumeroUEBAndTipoCapitolo(
			List<DettaglioVariazioneImportoCapitolo> lista, Capitolo<?, ?> elemento) {
		DettaglioVariazioneImportoCapitolo result = null;
		Integer numeroCapitolo = elemento.getNumeroCapitolo();
		Integer numeroArticolo = elemento.getNumeroArticolo();
		Integer numeroUEB = elemento.getNumeroUEB();
		TipoCapitolo tipoCapitolo = elemento.getTipoCapitolo();
		for (DettaglioVariazioneImportoCapitolo e : lista) {
			if (numeroCapitolo.equals(e.getCapitolo().getNumeroCapitolo()) && numeroArticolo.equals(e.getCapitolo().getNumeroArticolo())
					&& numeroUEB.equals(e.getCapitolo().getNumeroUEB()) && tipoCapitolo.equals(e.getCapitolo().getTipoCapitolo())) {
				result = e;
				break;
			}
		}
		return result;
	}
	
	/**
	  * Trova il dettaglio della variazione appartenenti ad una determinata lista per uid
	  * 
	  * @param lista    la lista in cui cercare gli elementi
	  * @param elemento l'elemento da cui estrarre numero del capitolo, numero dell'articolo e tipo di capitolo
	  * 
	  * @return l'elemento corrispondente
	  */
	public static DettaglioVariazioneImportoCapitolo findCapitoloByUid(List<DettaglioVariazioneImportoCapitolo> lista, Capitolo<?, ?> elemento) {
		DettaglioVariazioneImportoCapitolo result = null;
		int uid = elemento.getUid();
		for (DettaglioVariazioneImportoCapitolo e : lista) {
			if (uid == e.getCapitolo().getUid()) {
				result = e;
				break;
			}
		}
		return result;
	}

	/**
	 * Ricerca il movimento di gestione per numero.
	 * @param <T> la tipizzazione del movimento di gestione
	 * @param list      la lista dei movimento
	 * @param movimento il movimento da cercare
	 * 
	 * @return il movimento di dato numero, se presente; <code>null</code> in caso contrario
	 */
	public static <T extends MovimentoGestione> T findByNumeroMovimentoGestione(List<T> list, T movimento) {
		if (list == null || list.isEmpty() || movimento == null) {
			return null;
		}
		T result = null;
		for(T mg : list) {
			if(mg.getNumeroBigDecimal().equals(movimento.getNumeroBigDecimal())) {
				result = mg;
				break;
			}
		}
		return result;
	}

	/**
	 * Ottiene l'azione consentita di dato nome a partire dalla lista delle azioni consentite.
	 * 
	 * @param lista la lista delle azioni
	 * @param nome  il nome dell'azione da cercare
	 * 
	 * @return l'azione consentita di dato nome, se presente; <code>null</code> in caso contrario
	 */
	public static AzioneConsentita findAzioneConsentitaByNomeAzione(List<AzioneConsentita> lista, String nome) {
		// Se non ho impostato il nome, restituisco null
		if (StringUtils.isBlank(nome)) {
			return null;
		}

		AzioneConsentita result = null;

		for (AzioneConsentita ac : lista) {
			if (ac != null && ac.getAzione() != null && nome.equals(ac.getAzione().getNome())) {
				result = ac;
				break;
			}
		}

		return result;
	}
	

	/**
	 * Restituisce la lista degli indici corrispondenti aigli elementi presenti nella lista con dati numero capitolo, numero articolo e tipo di capitolo.
	 * 
	 * @param lista    la lista in cui cercare gli elementi
	 * @param elemento l'elemento da cui estrarre numero del capitolo, numero dell'articolo e tipo di capitolo
	 * 
	 * @return la lista degli elementi corrispondenti
	 */
	public static List<Integer> getIndexByNumeroCapitoloNumeroArticoloAndTipoCapitolo(List<ElementoCapitoloVariazione> lista,
			ElementoCapitoloVariazione elemento) {
		List<Integer> result = new ArrayList<Integer>();

		Integer numeroCapitolo = elemento.getNumeroCapitolo();
		Integer numeroArticolo = elemento.getNumeroArticolo();
		TipoCapitolo tipoCapitolo = elemento.getTipoCapitolo();

		// Ciclo sulla lista in input
		for (int i = 0; i < lista.size(); i++) {
			ElementoCapitoloVariazione e = lista.get(i);
			if (numeroCapitolo.equals(e.getNumeroCapitolo()) && numeroArticolo.equals(e.getNumeroArticolo()) && tipoCapitolo.equals(e.getTipoCapitolo())) {
				// I valori corrispondono: aggiungo l'elemento alla lista dei capitoli da eliminare
				result.add(Integer.valueOf(i));
			}
		}

		return result;
	}

	/**
	 * Ottiene l'indice dell'elemento ricercato all'interno di una lista, ricercando l'elemento stesso a partire dall'uid.
	 * @param <T> la tipizzazione dell'entit&agrave;
	 * @param list   la lista in cui ricercare l'oggetto
	 * @param entita l'oggetto da ricercare
	 * 
	 * @return l'indice dell'elemento nella lista, se presente; <code>-1</code> in caso contrario
	 */
	public static <T extends Entita> int getIndexByUid(List<T> list, T entita) {
		int result = -1;
		// Fallback
		if (entita == null || list == null) {
			return result;
		}

		for (int i = 0; i < list.size() && result == -1; i++) {
			if (list.get(i) != null && (list.get(i).getUid() == entita.getUid())) {
				result = i;
			}
		}
		return result;
	}

	/**
	 * Ottiene l'indice dell'elemento ricercato all'interno di una lista, ricercando l'elemento stesso a partire dall'uid.
	 * @param <T> la tipizzazione del model wrapper
	 * @param list         la lista in cui ricercare l'oggetto
	 * @param modelWrapper l'oggetto da ricercare
	 * 
	 * @return l'indice dell'elemento nella lista, se presente; <code>-1</code> in caso contrario
	 */
	public static <T extends ModelWrapper> int getIndexByUid(List<T> list, T modelWrapper) {
		int result = -1;
		// Fallback
		if (modelWrapper == null || list == null) {
			return result;
		}

		for (int i = 0; i < list.size() && result == -1; i++) {
			if (list.get(i) != null && (list.get(i).getUid() == modelWrapper.getUid())) {
				result = i;
			}
		}
		return result;
	}

}
