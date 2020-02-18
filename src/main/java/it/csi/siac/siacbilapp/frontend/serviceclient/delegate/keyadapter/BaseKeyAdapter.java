/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.ServiceRequest;

/**
 * Base for key adapters for {@link ServiceRequest}s.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/10/2014
 * 
 * @param <REQ> the request class
 *
 */
public abstract class BaseKeyAdapter<REQ extends ServiceRequest> implements KeyAdapter<REQ> {
	
	/** The dafault value to subtitute for null */
	protected static final String DEFAULT_NULL_VALUE = "null";
	/** The default separator between tokens */
	protected static final String DEFAULT_SEPARATOR = "_";
	
	@Override
	public String computeKey(REQ o) {
		return o == null ? DEFAULT_NULL_VALUE : computeNotNullKey(o);
	}
	
	/**
	 * Computes the logical key for the input object, knowing the input object not to be <code>null</code>.
	 * 
	 * @param req the given object
	 * @return the corresponding logical key
	 * @see #computeKey(ServiceRequest)
	 */
	protected abstract String computeNotNullKey(REQ req);

	/**
	 * Computes the default key for an {@link Entita}.
	 * 
	 * @param entita the entita whose key is to be computed.
	 * 
	 * @return the key corresponding to the entita
	 */
	protected String computeEntitaKey(Entita entita) {
		return entita != null ? "" + entita.getUid() : DEFAULT_NULL_VALUE;
	}
	
	/**
	 * Computes the default key for a possibly <code>null</code> object.
	 * 
	 * @param obj the object whose key is to be computed.
	 * 
	 * @return the key corresponding to the object
	 */
	protected String computeNullableKey(Object obj) {
		return obj != null ? obj.toString() : DEFAULT_NULL_VALUE;
	}
	
	/**
	 * Computes the default key for a set of possible <code>null</code> objects.
	 * 
	 * @param objs the objects whose key is to be computed
	 * @return the key corresponding to the objects
	 */
	protected String computeNullableKeys(Object... objs) {
		List<String> list = new ArrayList<String>();
		for(Object obj : objs) {
			list.add(computeNullableKey(obj));
		}
		return StringUtils.join(list, DEFAULT_SEPARATOR);
	}

}
