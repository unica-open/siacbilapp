/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an action whose invocation is subordinated to be valid for the given CDUs.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/05/2015
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnabledForCDU {
	
	/**
	 * Name of the CDU codes to validate for.
	 */
	String[] value();
}
