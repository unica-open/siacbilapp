/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Factory per Elemento delle scritture per lo step 2 della PrimaNotaLibera
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 *
 */
public final class ElementoConsultazioneScritturaPrimaNotaLiberaFactory {
	/** Non instanziare la classe */
	private ElementoConsultazioneScritturaPrimaNotaLiberaFactory() {
	}
	
	/**
	 * Crea elemento consultazione scrittura prima nota libera.
	 *
	 * @param primaNotaLibera the prima nota libera
	 * @param clazz the clazz
	 * @return the elemento consultazione scrittura prima nota libera
	 */
	public static final ElementoConsultazioneScritturaPrimaNotaLibera creaElementoConsultazioneScritturaPrimaNotaLibera(PrimaNota primaNotaLibera, Class<?> clazz) {
		ElementoConsultazioneScritturaPrimaNotaLibera instance = new ElementoConsultazioneScritturaPrimaNotaLibera(primaNotaLibera, clazz);
		List<ElementoScritturaPrimaNotaLibera> listaScritture = primaNotaLibera != null? ElementoScritturaPrimaNotaLiberaFactory.creaListaScrittureDaPrimaNota(primaNotaLibera, false) : new ArrayList<ElementoScritturaPrimaNotaLibera>();
		instance.setListaElementoScritturaPrimaNotaLibera(listaScritture);
		return instance;
	}

	
	/**
	 * Crea un lista di elementi di consultazione scrittura prima nota libera, richiamando il metodo {@link #creaElementoConsultazioneScritturaPrimaNotaLibera(PrimaNota, Class)}.
	 *
	 * @param primeNoteLibere the prime note libere
	 * @param clazz the clazz
	 * @return the list
	 */
	public static final List<ElementoConsultazioneScritturaPrimaNotaLibera> creaElementoConsultazioneScritturaPrimaNotaLibera(List<PrimaNota> primeNoteLibere, Class<?> clazz) {
		List<ElementoConsultazioneScritturaPrimaNotaLibera> instances = new ArrayList<ElementoConsultazioneScritturaPrimaNotaLibera>();
		if(primeNoteLibere == null) {
			return instances;
		}
		for (PrimaNota primaNota : primeNoteLibere) {
			ElementoConsultazioneScritturaPrimaNotaLibera singleInstance = creaElementoConsultazioneScritturaPrimaNotaLibera(primaNota, clazz);
			instances.add(singleInstance);
		}
		return instances;
	}
	
	

}
