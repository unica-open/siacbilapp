/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

import java.util.Map;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * 
 * Consente di specificare un DataInfo che rappresenta un valore con un solo Tipo.
 * @author Domenico Lisi
 * @author Elisa Chiari
 *
 * @param <S> la tipizzazione del dato
 */
public class TypedDataInfo<S> implements DataInfo<S> {
	/** Il logger */
	protected final LogUtil log = new LogUtil(this.getClass());
	
	private String name;
	private String campoKey;
	
	
	/**
	 * Costruttore
	 * @param name il nome
	 * @param campoKey la chiave del campo
	 */
	public TypedDataInfo(String name, String campoKey) {
		this.name = name;
		this.campoKey = campoKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public S getData(Map<String, Object> campi) {
		final String methodName = "getData";
		
		Object o = campi.get(campoKey);
		
		try {
			return (S) o;
		} catch (ClassCastException cce){
			log.error(methodName, "Campo corrispondente alla chiave " + campoKey + " e' di tipo "+(o!=null?o.getClass().getSimpleName():"null")+ ". Restituisco null.", cce);
			return null;
		}
		
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean mayUseFallback() {
		return false;
	}

	@Override
	public S getFallbackData(Map<String, Object> campi) {
		return null;
	}

	@Override
	public boolean isSummableImporto() {
		return false;
	}
}
