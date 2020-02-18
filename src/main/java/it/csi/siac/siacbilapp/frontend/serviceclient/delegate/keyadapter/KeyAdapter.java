/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter;

/**
 * Obtains the logical key for an object.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 * @param <T> the object type
 */
public interface KeyAdapter<T> {
	
	/**
	 * Computes the logical key for the input object.
	 * 
	 * @param o the given object
	 * @return the corresponding logical key
	 */
	String computeKey(T o);

}
