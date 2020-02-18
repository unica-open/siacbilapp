/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.codifica;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Factory per il wrapping degli elementi di codifica.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 28/08/2013
 *
 */
public final class ElementoCodificaFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCodificaFactory() {
	}
	
	/**
	 * Wrapper per l'Elemento del Piano dei Conti
	 *  
	 * @param elementoPianoDeiConti l'elemento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoCodifica getInstance(ElementoPianoDeiConti elementoPianoDeiConti) {
		// Fallback in caso di null
		if(elementoPianoDeiConti == null) {
			return null;
		}
		
		ElementoCodifica wrapper = new ElementoCodifica();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, elementoPianoDeiConti);
		
		// Popolamento dei sotto-elementi
		List<ElementoCodifica> sottoElementi = new ArrayList<ElementoCodifica>();
		
		// Ottengo i sotto-elementi del piano dei conti dalla codifica
		List<ElementoPianoDeiConti> subElements = elementoPianoDeiConti.getElemPdc();
		if(subElements != null && !subElements.isEmpty()) {
			List<ElementoCodifica> subList = new ArrayList<ElementoCodifica>();
			for(ElementoPianoDeiConti subElement : subElements) {
				// Pongo i sotto-elementi nella lista
				subList.add(ElementoCodificaFactory.getInstance(subElement));
			}
			// Aggiungo tutti i sotto-elementi
			sottoElementi.addAll(subList);
		}
		
		// Injetto i sotto-elementi nel wrapper
		wrapper.setSottoElementi(sottoElementi);
		
		return wrapper;
	}
	
	/**
	 * Wrapper per la Struttura Amministrativo Contabile
	 *  
	 * @param strutturaAmministrativoContabile l'elemento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoCodifica getInstance(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		// Fallback in caso di null
		if(strutturaAmministrativoContabile == null) {
			return null;
		}
		
		ElementoCodifica wrapper = new ElementoCodifica();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, strutturaAmministrativoContabile);
		
		// Popolamento dei sotto-elementi
		List<ElementoCodifica> sottoElementi = new ArrayList<ElementoCodifica>();
		
		// Ottengo i sotto-elementi della struttura amministrativo contabile dalla codifica
		List<StrutturaAmministrativoContabile> subElements = strutturaAmministrativoContabile.getSubStrutture();
		if(subElements != null && !subElements.isEmpty()) {
			List<ElementoCodifica> subList = new ArrayList<ElementoCodifica>();
			for(StrutturaAmministrativoContabile subElement : subElements) {
				// Pongo i sotto-elementi nella lista
				subList.add(ElementoCodificaFactory.getInstance(subElement));
			}
			// Aggiungo tutti i sotto-elementi
			sottoElementi.addAll(subList);
		}
		
		// Injetto i sotto-elementi nel wrapper
		wrapper.setSottoElementi(sottoElementi);
		
		return wrapper;
	}
	
	/**
	 * Wrapper per il SIOPE di entrata.
	 *  
	 * @param siopeEntrata l'elemento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoCodifica getInstance(SiopeEntrata siopeEntrata) {
		// Fallback in caso di null
		if(siopeEntrata == null) {
			return null;
		}
		
		ElementoCodifica wrapper = new ElementoCodifica();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, siopeEntrata);
		
		// Popolamento dei sotto-elementi
		List<ElementoCodifica> sottoElementi = new ArrayList<ElementoCodifica>();
		
		// Ottengo i sotto-elementi del piano dei conti dalla codifica
		List<SiopeEntrata> subElements = siopeEntrata.getFigli();
		if(subElements != null && !subElements.isEmpty()) {
			List<ElementoCodifica> subList = new ArrayList<ElementoCodifica>();
			for(SiopeEntrata subElement : subElements) {
				// Pongo i sotto-elementi nella lista
				subList.add(ElementoCodificaFactory.getInstance(subElement));
			}
			// Aggiungo tutti i sotto-elementi
			sottoElementi.addAll(subList);
		}
		
		// Injetto i sotto-elementi nel wrapper
		wrapper.setSottoElementi(sottoElementi);
		
		return wrapper;
	}
	
	/**
	 * Wrapper per il SIOPE di spesa.
	 *  
	 * @param siopeSpesa l'elemento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoCodifica getInstance(SiopeSpesa siopeSpesa) {
		// Fallback in caso di null
		if(siopeSpesa == null) {
			return null;
		}
		
		ElementoCodifica wrapper = new ElementoCodifica();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, siopeSpesa);
		
		// Popolamento dei sotto-elementi
		List<ElementoCodifica> sottoElementi = new ArrayList<ElementoCodifica>();
		
		// Ottengo i sotto-elementi del piano dei conti dalla codifica
		List<SiopeSpesa> subElements = siopeSpesa.getFigli();
		if(subElements != null && !subElements.isEmpty()) {
			List<ElementoCodifica> subList = new ArrayList<ElementoCodifica>();
			for(SiopeSpesa subElement : subElements) {
				// Pongo i sotto-elementi nella lista
				subList.add(ElementoCodificaFactory.getInstance(subElement));
			}
			// Aggiungo tutti i sotto-elementi
			sottoElementi.addAll(subList);
		}
		
		// Injetto i sotto-elementi nel wrapper
		wrapper.setSottoElementi(sottoElementi);
		
		return wrapper;
	}
	
	/**
	 * Trasforma una lista di codifiche in una lista di wrappers.
	 * @param <T> la tipizzazione del classificatore
	 * 
	 * @param listaCodifica la lista di elementi da wrappare
	 * 
	 * @return la lista dei wrappers
	 * 
	 * @throws UnsupportedOperationException nel caso in cui la lista contenga elementi non wrappabili
	 */
	public static <T extends ClassificatoreGerarchico> List<ElementoCodifica> getInstances(List<T> listaCodifica) {
		if(listaCodifica == null) {
			return new ArrayList<ElementoCodifica>();
		}
		List<ElementoCodifica> result = new ArrayList<ElementoCodifica>();
		for(T codifica : listaCodifica) {
			if(codifica instanceof ElementoPianoDeiConti) {
				result.add(ElementoCodificaFactory.getInstance((ElementoPianoDeiConti)codifica));
			} else if (codifica instanceof StrutturaAmministrativoContabile) {
				result.add(ElementoCodificaFactory.getInstance((StrutturaAmministrativoContabile)codifica));
			} else if(codifica instanceof SiopeEntrata) {
				result.add(ElementoCodificaFactory.getInstance((SiopeEntrata)codifica));
			} else if(codifica instanceof SiopeSpesa) {
				result.add(ElementoCodificaFactory.getInstance((SiopeSpesa)codifica));
			} else {
				throw new UnsupportedOperationException("Non e' possibile convertire la lista");
			}
		}
		return result;
	}
	
	/**
	 * Metodo di utilit&agrave; per il wrapping dei campi comuni.
	 * 
	 * @param wrapper il wrapper da popolare
	 * @param classif la codifica da wrappare
	 */
	private static void popolaCampiComuni(ElementoCodifica wrapper, ClassificatoreGerarchico classif) {
		// Wrappo i campi comuni
		wrapper.setCodice(classif.getCodice());
		wrapper.setDescrizione(classif.getDescrizione());
		wrapper.setUid(classif.getUid());
		wrapper.setCodiceTipo(classif.getTipoClassificatore().getCodice());
	}

}
