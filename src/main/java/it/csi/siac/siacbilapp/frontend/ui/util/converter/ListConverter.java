/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
//import com.opensymphony.xwork2.util.XWorkList;
import com.opensymphony.xwork2.conversion.impl.XWorkList;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Classe di conversione per le {@link List} evitando le problematiche dell'implementazione standard di Struts2..
 * 
 * @author Marchino Alessandro
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ListConverter extends DefaultTypeConverter {
	
	// Campi d'istanza
	/** Logger */
	private final LogUtil log = new LogUtil(getClass());
	
	@Override
	public Object convertValue(Map<String, Object> context, Object target, Member member, String propertyName, Object value, Class toType) {
		final String methodName = "methodName";
		
		if (String.class.equals(toType)) {
			return this.convertToString(value);
		}
		Class<?> genericClass = obtainGenericClassInfo(member, target, propertyName);
		
		if (value instanceof String[]) {
			return this.convertFromString((String[])value, genericClass);
		}
		if (value instanceof String) {
			return this.convertFromString(new String[] {(String)value}, genericClass);
		}
		log.debug(methodName, "Performing fallback conversion");
		return super.convertValue(value, toType);
	}

	/**
	 * Ottiene le informazioni sul tipo generico da applicare alla lista
	 * @param member il membro su cui &eacute; stato invocato il converter
	 * @param target il target dell'invocazione
	 * @param propertyName il nome della propriet&agrave; su cui &eacute; invocato il metodo
	 * @return la classe che risolve il generic
	 */
	private Class<?> obtainGenericClassInfo(Member member, Object target, String propertyName) {
		final String methodName = "obtainGenericClassInfo";
		if(member instanceof Method) {
			log.debug(methodName, "Obtaining info by Method " + member.getName());
			return obtainGenericClassInfoByMethod((Method)member);
		}
		if(member instanceof Field) {
			log.debug(methodName, "Obtaining info by Field " + member.getName());
			return obtainGenericClassInfoByField((Field)member);
		}
		// Fallback
		log.debug(methodName, "Fallback: obtaining by locating Field in Class by name");
		Class<?> clazz = target.getClass();
		Field field = obtainField(clazz, propertyName);
		return obtainGenericClassInfoByField(field);
	}

	/**
	 * Ottiene le informazioni sul tipo generico da applicare alla lista a partire dal tipo Field.
	 * @param field il campo da cui ottenere le informazioni
	 * @return la classe che risolve il generic
	 */
	private Class<?> obtainGenericClassInfoByField(Field field) {
		final String methodName = "obtainGenericClassInfoByField";
		if(field == null) {
			log.debug(methodName, "Null field - fallback over Object");
			return Object.class;
		}
		return getGenericTypeByType(field.getGenericType());
	}

	/**
	 * Ottiene le informazioni sul tipo generico da applicare alla lista a partire dal tipo Method.
	 * @param method il metodo da cui ottenere le informazioni
	 * @return la classe che risolve il generic
	 */
	private Class<?> obtainGenericClassInfoByMethod(Method method) {
		final String methodName = "obtainGenericClassInfoByMethod";
		if(method == null) {
			log.debug(methodName, "Null method - fallback over Object");
			return Object.class;
		}
		Type[] types = method.getGenericParameterTypes();
		// Deve essere un setter
		if(types.length != 1) {
			log.debug(methodName, "Number of types in method signature: " + types.length + " - cannot property identify the required one: fallback over Object");
			// Fallback
			return Object.class;
		}
		return getGenericTypeByType(types[0]);
	}
	
	/**
	 * Ottiene le informazioni sul tipo generico da applicare alla lista a partire dal tipo Type.
	 * @param type il tipo da cui ottenere le informazioni
	 * @return la classe che risolve il generic
	 */
	private Class<?> getGenericTypeByType(Type type) {
		final String methodName = "getGenericTypeByType";
		if(type == null) {
			log.debug(methodName, "The type is null: fallback over Object");
			return Object.class;
		}
		if(!(type instanceof ParameterizedType)) {
			log.debug(methodName, "The type " + type.toString() + " is not Parameterized: fallback over Object");
			return Object.class;
		}
		ParameterizedType pType = (ParameterizedType)type;
		Type[] acttualTypeArguments = pType.getActualTypeArguments();
		if(acttualTypeArguments == null || acttualTypeArguments.length == 0) {
			log.debug(methodName, "Not enough actual types to identify the required one: fallback over Object");
			return Object.class;
		}
		
		return (Class<?>) acttualTypeArguments[0];
	}
	
	/**
	 * Ottiene il campo dalla classe.
	 * @param clazz la classe da cui ottenere il campo
	 * @param fieldName il nome del campo
	 * @return il campo corrispondente al nome nella classe
	 */
	private Field obtainField(Class<?> clazz, String fieldName) {
		final String methodName = "obtainField";
		Class<?> tmp = clazz;
		do {
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields) {
				if(field.getName().equals(fieldName)) {
					log.debug(methodName, "Found field " + fieldName + " in class " + clazz.getName());
					return field;
				}
			}
			log.debug(methodName, "Field " + fieldName + " not found in class " + clazz.getName() + " - iterating over superclass");
			tmp = tmp.getSuperclass();
		} while(tmp != null);
		
		log.debug(methodName, "Field not found neither in class nor in any of its superclasses");
		return null;
	}

	/**
	 * Conversione in stringa
	 * @param value il valore da convertire
	 * @return la stringa corrispondente al risultato
	 */
	private String convertToString(Object value) {
		return value == null ? null : value.toString();
	}
	
	/**
	 * Conversione da stringa
	 * @param context il contesto OGNL
	 * @param values i valori da impostare nel risultato
	 * @param genericType il tipo generico da utilizzare
	 * @return la lista popolata con i dati forniti
	 */
	private <T> List<T> convertFromString(String[] values, Class<T> genericType) {
		// Workaround for Struts 2.3.32
		XWorkList tmp = new XWorkList(genericType);
		for(String value : values) {
			tmp.add(value);
		}
		
		List<T> result = new ArrayList<T>();
		result.addAll(tmp);
		return result;
	}
	
}
