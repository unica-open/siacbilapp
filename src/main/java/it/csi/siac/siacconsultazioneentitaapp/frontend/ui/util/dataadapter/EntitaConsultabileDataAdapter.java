/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileDataWrapper;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;

/**
 * Base per gli adapter che aggregano i dati delle colonne da mostrare in maschera a partire dai dati che arrivano dalle {@link EntitaConsultabile}
 * 
 * @author Domenico Lisi
 *
 */
public abstract class EntitaConsultabileDataAdapter {
	
	private List<DataInfo<?>> columns;
	private List<DataInfo<?>> columnsExport;
	private List<DataInfo<?>> datas;
	
	
	/**
	 * Costruttore.
	 * 
	 * @param columns dati inerenti le colonne di una {@link EntitaConsultabile}
	 * @param columnsExport dati relativi alle colonne da esportare
	 * @param datas dati generici (escluso le colonne) inerenti una {@link EntitaConsultabile}
	 */
	public EntitaConsultabileDataAdapter(List<DataInfo<?>> columns, List<DataInfo<?>> columnsExport, List<DataInfo<?>> datas) {
		this.columns = columns;
		this.columnsExport = columnsExport;
		this.datas = datas;
	}
	
	
	/**
	 * Ottiene la lista delle colonne. Ovvero delle chiavi da utilizzare nella mappa restituita dal metodo {@link #getListaIntestazioneColonneTabella()}
	 * 
	 * @return lista delle colonne
	 */
	public List<EntitaConsultabileDataWrapper> getListaIntestazioneColonneTabella() {
		
		List<EntitaConsultabileDataWrapper> result = new ArrayList<EntitaConsultabileDataWrapper>();
		
		for (DataInfo<?> column : columns) {
			result.add(new EntitaConsultabileDataWrapper(column.getName(), column.isSummableImporto()));
		}
		
		return result;
	}

	/**
	 * Elaborazione dei dati delle colonne.
	 * 
	 * @param campi mappa dei campi (non aggregati) che arriva dal servizio 
	 * @return mappa delle colonne da utilizzare nella view (campi aggregati con eventuale HTML).
	 */
	public Map<String, String> elaboraDatiColonne(Map<String, Object> campi) {
		
		Map<String, String> m = new HashMap<String, String>();
		
		for (DataInfo<?> column : columns) {
			m.put(column.getName(), column.getData(campi).toString());
		}
		
		return m;
	}
	
	/**
	 * Ottiene la lista delle colonne per l'export in Excel. Ovvero delle chiavi da utilizzare nella mappa restituita dal metodo {@link #getListaIntestazioneColonneTabella()}
	 * 
	 * @return lista delle colonne
	 */
	public List<String> getListaIntestazioneColonneExportTabella() {
		
		List<String> result = new ArrayList<String>();
		
		for (DataInfo<?> column : columnsExport) {
			result.add(column.getName());
		}
		
		return result;
	}

	/**
	 * Elaborazione dei dati delle colonne per l'export in Excel.
	 * 
	 * @param campi mappa dei campi (non aggregati) che arriva dal servizio 
	 * @return mappa delle colonne da utilizzare nella view (campi aggregati con eventuale HTML).
	 */
	public Map<String, Object> elaboraDatiColonneExport(Map<String, Object> campi) {
		
		Map<String, Object> m = new HashMap<String, Object>();
		
		for (DataInfo<?> column : columnsExport) {
			Object data = column.getData(campi);
			if(column.mayUseFallback() && (data == null || (data instanceof CharSequence && StringUtils.isBlank((CharSequence) data)))) {
				data = column.getFallbackData(campi);
			}
			
			m.put(column.getName(), data);
		}
		
		return m;
	}
	
	
	/**
	 * Ottiene la lista dei dati. Ovvero delle chiavi da utilizzare nella mappa restituita dal metodo {@link #getListaIntestazioneDatiAccessori()}
	 * 
	 * @return lista delle colonne
	 */
	public List<String> getListaIntestazioneDatiAccessori() {
		
		List<String> result = new ArrayList<String>();
		
		for (DataInfo<?> data : datas) {
			result.add(data.getName());
		}
		
		return result;
	}

	/**
	 * Elaborazione dei dati.
	 * 
	 * @param campi mappa dei campi (non aggregati) che arriva dal servizio 
	 * @return mappa delle colonne da utilizzare nella view (campi aggregati con eventuale HTML).
	 */
	public Map<String, String> elaboraDatiAccessori(Map<String, Object> campi) {
		
		Map<String, String> m = new HashMap<String, String>();
		
		for (DataInfo<?> data : datas) {
			m.put(data.getName(), data.getData(campi).toString());
		}
		
		return m;
	}
	
	/**
	 * Ottiene una List a partire da un Array di DataInfo.
	 * 
	 * @param dataInfos le informazioni
	 * @return lista di DataInfo
	 */
	protected static List<DataInfo<?>> asList(DataInfo<?>... dataInfos){
		return Arrays.asList(dataInfos);
	}
	
	
	
}
