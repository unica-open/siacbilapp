/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.functor;

/**
 * Defines a binary operation between two models.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/06/2014
 *
 * @param <L> the left-side type
 * @param <R> the right-side type
 * @param <T> the result type
 */
public interface BinaryOperator<L, R, T> {
	
	/**
	 * Performs the operation.
	 * 
	 * @param left  left-side operator
	 * @param right right-side operator
	 *
	 * @return the result of the operation
	 */
	T performOperation(L left, R right);
	
}
