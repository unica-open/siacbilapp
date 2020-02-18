/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

import java.util.Map;

/**
 * Descrizione informazioni di una Colonna con un solo dato di tipo  {@link java.util.Date}.
 * 
 * @author Domenico
 * @author Alessandro Marchino
 * @version 1.1.0 - gestione della tipizzazione del risultato
 *
 */
public class BooleanStringDataInfo extends BaseDataInfo {
	
	private final String trueOriginalValue;
	private final String trueValue;
	private final String falseOriginalValue;
	private final String falseValue;
	
	/**
	 * Costruttore con formato del booleano personalizabile.
	 * 
	 * @param name nome della colonna
	 * @param pattern il pattern
	 * @param campoKey nome del campo della mappa dei campi restituita dal servizio.
	 * @param trueOriginalValue il valore da sostituire al <code>true</code>
	 * @param trueValue il valore da sostituire al <code>true</code>
	 * @param falseOriginalValue il valore da sostituire al <code>true</code>
	 * @param falseValue il valore da sostituire al <code>false</code>
	 */
	public BooleanStringDataInfo(String name, String pattern, String campoKey, String trueOriginalValue, String trueValue, String falseOriginalValue, String falseValue) {
		super(name, pattern, campoKey);
		this.trueOriginalValue = trueOriginalValue;
		this.trueValue = trueValue;
		this.falseOriginalValue = falseOriginalValue;
		this.falseValue = falseValue;
	}
	
	@Override
	protected Object preProcessValue(Map<String, Object> campi, String key) {
		Object obj = super.preProcessValue(campi, key);
		if(!(obj instanceof String)) {
			return obj;
		}
		String str = (String) obj;
		return trueOriginalValue.equalsIgnoreCase(str)
			? trueValue
			: falseOriginalValue.equalsIgnoreCase(str)
				? falseValue
				: str;
	}
	

}