/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Entita;

public class ReflectionBilUtil {

	/** Per la conversione della classe padre nelle sottoclassi */
	private static BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
	
	private static final LogUtil LOG = new LogUtil(ReflectionUtil.class);
	
	/** Inizializza in modo statico l'istanza del BeanUtilsBean per la conversione. */
	static {
		beanUtilsBean.getConvertUtils().register(false, true, 0);
	}
	
	
	/** Non instanziare la classe */
	private ReflectionBilUtil() {
	}

	/**
	 * Crea un'istanza di una sottoclasse del capitolo a partire da un'istanza della classe padre e dal tipo di capitolo.
	 * @param <C> la tipizzazione del capitolo
	 * @param capitolo il capitolo da cui ricavare la sottoclasse
	 * @return la sottoclasse del capitolo relativa al tipo di capitolo, nel caso sia inizializzato; <code>null</code> in caso contrario
	 * @throws FrontEndBusinessException nel caso in cui non sia possibile ottenere l'istanza a partire dal capitolo originale
	 */
	@SuppressWarnings("unchecked")
	public static <C extends Capitolo<?, ?>> C getInstanceFromCapitoloBaseClass(C capitolo) throws FrontEndBusinessException {
		final String methodName = "getInstanceFromCapitoloBaseClass";
		TipoCapitolo tipoCapitolo = capitolo.getTipoCapitolo();
		ImportiCapitolo importiCapitolo = capitolo.getImportiCapitolo();
		
		if(importiCapitolo == null) {
			importiCapitolo = tipoCapitolo.newImportiCapitoloInstance();
		}
		
		Capitolo<?, ?> result = tipoCapitolo.newCapitoloInstance();
		ImportiCapitolo ic = tipoCapitolo.newImportiCapitoloInstance();
		
		try {
			beanUtilsBean.copyProperties(ic, importiCapitolo);
		} catch (IllegalAccessException e) {
			LOG.debug(methodName, "IllegalAccessException. Impossibile copiare gli importi: " + e.getMessage());
			throw new FrontEndBusinessException("Impossibile copiare gli importi", e);
		} catch (InvocationTargetException e) {
			LOG.debug(methodName, "InvocationTargetException. Impossibile copiare gli importi: " + e.getMessage());
			throw new FrontEndBusinessException("Impossibile copiare gli importi", e);
		}
		importiCapitolo = null;
		try {
			beanUtilsBean.copyProperties(result, capitolo);
		} catch (IllegalAccessException e) {
			LOG.debug(methodName, "IllegalAccessException. Impossibile copiare il capitolo: " + e.getMessage());
			throw new FrontEndBusinessException("Impossibile copiare il capitolo", e);
		} catch (InvocationTargetException e) {
			LOG.debug(methodName, "InvocationTargetException. Impossibile copiare il capitolo: " + e.getMessage());
			throw new FrontEndBusinessException("Impossibile copiare il capitolo", e);
		}
		try {
			beanUtilsBean.setProperty(result, "importiCapitolo", ic);
		} catch (IllegalAccessException e) {
			LOG.debug(methodName, "IllegalAccessException. Impossibile impostare gli importi nel capitolo: " + e.getMessage());
			throw new FrontEndBusinessException("Impossibile impostare gli importi nel capitolo", e);
		} catch (InvocationTargetException e) {
			LOG.debug(methodName, "InvocationTargetException. Impossibile impostare gli importi nel capitolo: " + e.getMessage());
			throw new FrontEndBusinessException("Impossibile impostare gli importi nel capitolo", e);
		}
		return (C)result;
	}
	
	
	/**
	 * Crea una istanza della classe fornita copiando solo l'uid fornito.
	 * @param <T> la tipizzazione dell'entit&agrave;
	 * @param original l'oggetto originale
	 * @return l'istanza dell'oggetto con solo l'uid valorizzato
	 */
	public static <T extends Entita> T createWithOnlyUid(T original) {
		if(original == null || original.getUid() == 0) {
			return null;
		}
		final String methodName = "createWithOnlyUid";
		try {
			@SuppressWarnings("unchecked")
			T newInstance = (T) original.getClass().getConstructor().newInstance();
			newInstance.setUid(original.getUid());
			return newInstance;
		} catch (IllegalArgumentException e) {
			LOG.error(methodName, "IllegalArgumentException in entity creation", e);
		} catch (SecurityException e) {
			LOG.error(methodName, "SecurityException in entity creation", e);
		} catch (InstantiationException e) {
			LOG.error(methodName, "InstantiationException in entity creation", e);
		} catch (IllegalAccessException e) {
			LOG.error(methodName, "IllegalAccessException in entity creation", e);
		} catch (InvocationTargetException e) {
			LOG.error(methodName, "InvocationTargetException in entity creation", e);
		} catch (NoSuchMethodException e) {
			LOG.error(methodName, "NoSuchMethodException in entity creation", e);
		}
		return null;
	}
	
}
