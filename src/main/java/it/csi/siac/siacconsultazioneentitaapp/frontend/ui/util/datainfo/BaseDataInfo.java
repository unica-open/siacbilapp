/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Descrizione informazioni di un Dato da mostrare a front-end.
 * Tale dato pu&oacute; essere il valore di una colonna oppure una generica etichetta da mostrare.
 * 
 * @author Domenico Lisi
 * @author Alessandro Marchino
 * @version 1.0.0
 * @version 1.1.0 - implementazione dei risultati tipizzati 
 *
 */
public class BaseDataInfo implements DataInfo<String> {
	
	/** Logger */
	protected final LogUtil log = new LogUtil(this.getClass()); 
	
	private final String name;
	private final String pattern;
	private final String[] campiKeys;
	
	/** Fallback per il pattern */
	protected String fallbackPattern;
	/** Fallback per le chiavi dei campi */
	protected String[] fallbackCampiKeys;
	
	private boolean escape;
	
	
	/**
	 * Costruttore dei dati di una colonna.
	 * <br>
	 * Esempio per una colonna con dati separati da trattini:
	 * <br>
	 * <code> new DataInfo("nome colonna con due dati separati da trattino", "{0} - {1}", "dato1", "dato2") </code>
	 * 
	 * @param name nome della colonna
	 * @param pattern message pattern compatibile con {@link MessageFormat}
	 * @param campiKeys nome dei campi della mappa dei campi restituita dal servizio.
	 */
	public BaseDataInfo(String name, String pattern, String... campiKeys) {
		this.name = name;
		this.pattern = pattern;
		this.campiKeys = campiKeys;
		this.escape = true;
		
		this.fallbackPattern = "";
		this.fallbackCampiKeys = new String[0];
	}
	
	/**
	 * Forza il divieto dell'escape
	 * @return il dataInfo su cui si &eacute; effettuata l'invocazione
	 */
	public DataInfo<String> forceNoEscape() {
		this.escape = false;
		return this;
	}
	
	@Override
	public String getName() {
		return name;
	}
	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @return the campiKeys
	 */
	public String[] getCampiKeys() {
		return campiKeys != null ? campiKeys.clone() : null;
	}
	@Override
	public String getData(Map<String, Object> campi) {
		return innerGetData(this.pattern, this.campiKeys, campi);
	}

	private String innerGetData(String pattern, String[] campiKeys, Map<String, Object> campi) {
		String methodName = "innerGetData";
		
		List<Object> args = new ArrayList<Object>();
		for(String campoKey : campiKeys){
			Object value = preProcessValue(campi, campoKey);
			args.add(value);
		}
		if(emptyArguments(args)) {
			return "";
		}
		
		Object[] argumentsObject = args.toArray();
		String data;
		try {
			String patternNew = preProcessPattern(pattern, argumentsObject);
			MessageFormat mf = new MessageFormat(patternNew, Locale.ITALY);
			data = mf.format(argumentsObject);
			data = postProcessData(data);
		}catch(IllegalArgumentException iae) {
			StringBuilder sb = new StringBuilder()
				.append("Errore formattazione del dato ")
				.append(this.getName())
				.append(" [")
				.append(this.getClass().getSimpleName())
				.append("]. Pattern: ")
				.append(pattern)
				.append(". Arguments: ")
				.append(ToStringBuilder.reflectionToString(argumentsObject, ToStringStyle.MULTI_LINE_STYLE));
			
			for(Object arg : argumentsObject){
				sb.append(" ")
					.append(ToStringBuilder.reflectionToString(arg));
			}
			sb.append(".");
			String errorMessage = sb.toString();
			log.error(methodName, errorMessage, iae );
			data = handleMessageFormatException(iae, errorMessage);
		}
		return data;
	}
	
	/**
	 * Consente di modificare il pattern poco prima di essere utilizzato dal formattatore dei messaggi.
	 * 
	 * @param pattern il pattern da usare
	 * @param argumentsObject gli argomenti da utilizzare per popolare il dato
	 * @return il formato con la sostituzione degli argomenti <code>null</code>
	 */
	protected String preProcessPattern(String pattern, Object[] argumentsObject) {
		String patternNew = pattern;
		for (int i = 0; i < argumentsObject.length; i++) {
			Object o = argumentsObject[i];
			if(o==null){
				patternNew = patternNew.replaceAll("\\{"+i+"(.*?)\\}", getDefaultForNullValues());
			}
		}
		return patternNew;
		
	}

	/**
	 * Pre-processazione dei campi.
	 * 
	 * @param campi i campi
	 * @param key la chiave
	 * @return il valore
	 */
	protected Object preProcessValue(Map<String, Object> campi, String key) {
		Object value = campi.get(key);
		boolean mayEscape = escape();
		if (value instanceof String && mayEscape) {
			//Se e' una stringa faccio escape dei caratteri per l'HTML
			value = StringEscapeUtils.escapeHtml4((String)value);
			campi.put(key, value);
		}
		return value;
	}

	/**
	 * Controlla se effettuare l'escape o meno
	 * @return se effettuare l'escape o meno
	 */
	protected boolean escape() {
		return escape;
	}

	/**
	 * Controlla se gli argomenti siano <code>null</code> o vuoti
	 * @param args i dati da controllare
	 * @return <code>true</code> se tutti gli argomenti sono <code>null</code> o pari a una stringa vuota; <code>false</code> altrimenti
	 */
	private boolean emptyArguments(List<Object> args) {
		for(Object obj : args) {
			if(obj != null && (!(obj instanceof String) || !((String)obj).isEmpty())) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Funzione a termine della processazione dei dati
	 * @param data il dato da processare
	 * @return il dato processato
	 */
	protected String postProcessData(String data) {
		// Lascio intenzionalmente i placeholder di tipo {0} a video in modo che
		// sia visibile il fatto che un campo non sia stato impostato dal DataAdapter.
		return data;
	}

	/**
	 * Gestione dell'eccezione del message format
	 * @param iae l'eccezione
	 * @param errorMessage il messaggio di errore
	 * @return la valore da utilizzare in caso di errore
	 */
	protected String handleMessageFormatException(IllegalArgumentException iae, String errorMessage) {
		throw new IllegalArgumentException(errorMessage, iae);
	}
	


	/**
	 * Imposta il default per i valori <code>null</code>.
	 * Se necessario fare override per specificare un default diverso e/o configurabile.
	 * @return il valore di default per i <code>null</code>
	 */
	protected String getDefaultForNullValues() {
		return "";
	}
	
	@Override
	public boolean mayUseFallback() {
		return false;
	}
	
	@Override
	public String getFallbackData(Map<String, Object> campi) {
		return innerGetData(this.fallbackPattern, this.fallbackCampiKeys, campi);
	}
	
	@Override
	public boolean isSummableImporto() {
		// Implementazione di default
		return false;
	}
	
}