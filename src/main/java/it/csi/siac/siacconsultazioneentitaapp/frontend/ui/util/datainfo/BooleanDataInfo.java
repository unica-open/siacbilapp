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
public class BooleanDataInfo extends BaseDataInfo {
	
	private final String trueValue;
	private final String falseValue;
	
	/**
	 * Costruttore con formato del booleano personalizabile.
	 * 
	 * @param name nome della colonna
	 * @param campoKey nome del campo della mappa dei campi restituita dal servizio.
	 * @param trueValue il valore da sostituire al <code>true</code>
	 * @param falseValue il valore da sostituire al <code>false</code>
	 */
	public BooleanDataInfo(String name, String campoKey, String trueValue, String falseValue) {
		super(name, "{0}", campoKey);
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}
	
	@Override
	protected Object preProcessValue(Map<String, Object> campi, String key) {
		Object obj = super.preProcessValue(campi, key);
		if(!(obj instanceof Boolean)) {
			return obj;
		}
		return Boolean.TRUE.equals(obj) ? trueValue : falseValue;
	}

}