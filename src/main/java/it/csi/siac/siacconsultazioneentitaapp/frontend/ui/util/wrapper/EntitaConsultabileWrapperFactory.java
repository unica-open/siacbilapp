/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.EntitaConsultabileDataAdapter;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;

/**
 * Crea l'{@link EntitaConsultabileWrapper} a partire da {@link EntitaConsultabile}
 * 
 * @author Domenico
 *
 */
public final class EntitaConsultabileWrapperFactory {

	/**
	 * Costruttore
	 */
	private EntitaConsultabileWrapperFactory() {
	}
	
	/**
	 * Ottiene i wrapper di un elenco di EntitaConsultabile con il DataAdapter specificato.
	 * 
	 * @param entitaConsultabili le entit&agrave; consultabili da wrappare
	 * @param dataAdapter l'adapter
	 * @return la lista di wrapper corrispondente alle entit&agrave; in ingresso
	 */
	public static List<EntitaConsultabileWrapper> init(List<EntitaConsultabile> entitaConsultabili, EntitaConsultabileDataAdapter dataAdapter) {
		List<EntitaConsultabileWrapper> result = new ArrayList<EntitaConsultabileWrapper>();
		if(entitaConsultabili==null || entitaConsultabili.isEmpty()){
			return result; 
		}
		for (EntitaConsultabile entitaConsultabile : entitaConsultabili) {
			EntitaConsultabileWrapper ecw = init(entitaConsultabile, dataAdapter);
			result.add(ecw);
		}
		return result;
	}

	/**
	 * Ottiene il wrapper di un EntitaConsultabile con DataAdapter specificato.
	 * 
	 * @param entitaConsultabile l'entit&agrave; consultabile da wrappare
	 * @param dataAdapter l'adapter
	 * @return il wrapper corrispondente al'entit&agrave; in ingresso
	 */
	public static EntitaConsultabileWrapper init(EntitaConsultabile entitaConsultabile, EntitaConsultabileDataAdapter dataAdapter) {
		if(dataAdapter == null){
			throw new IllegalArgumentException("Occorre specificare un EntitaConsultabileDataAdapter.");
		}
		EntitaConsultabileWrapper entitaConsutabileWrapper = new EntitaConsultabileWrapper(entitaConsultabile);
		//Ottengo la lista delle intestazioni 
		List<EntitaConsultabileDataWrapper> listaIntestazioneColonneTabella = dataAdapter.getListaIntestazioneColonneTabella();
		entitaConsutabileWrapper.setListaIntestazioneCampiTabella(listaIntestazioneColonneTabella);
		
		//Ottengo la mappa dei campi delle colonne
		Map<String, String> campiColonne = dataAdapter.elaboraDatiColonne(entitaConsultabile.getCampi());
		entitaConsutabileWrapper.setCampiColonne(campiColonne);
		
		
		//Ottengo la mappa dei dati dell'etita consultabile
		Map<String, String> dati = dataAdapter.elaboraDatiAccessori(entitaConsultabile.getCampi());
		entitaConsutabileWrapper.setCampiAccessori(dati);
		return entitaConsutabileWrapper;
	}
	
}
