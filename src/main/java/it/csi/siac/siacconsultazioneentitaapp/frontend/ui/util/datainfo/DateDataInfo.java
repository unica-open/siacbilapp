/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con un solo dato di tipo  {@link java.util.Date}.
 * 
 * @author Domenico
 *
 */
public class DateDataInfo extends BaseDataInfo {
	
	/**
	 * Costruttore con formato data di defalut dd/MM/yyyy.
	 * 
	 * @param name nome della colonna
	 * @param campoKey nome del campo della mappa dei campi restituita dal servizio.
	 */
	public DateDataInfo(String name, String campoKey) {
		this(name, campoKey, "dd/MM/yyyy");
	}
	
	/**
	 * Costruttore con formato data personalizabile.
	 * 
	 * @param name nome della colonna
	 * @param campoKey nome del campo della mappa dei campi restituita dal servizio.
	 * @param datePattern patter della data: ad esempio: "dd - MM - yyyy"
	 */
	public DateDataInfo(String name, String campoKey, String datePattern) {
		super(name, "{0,date," + datePattern + "}", campoKey);
	}

}