/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import it.csi.siac.siacbilapp.BaseJUnit4TestCase;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siaccommon.util.ReflectionUtil;


/**
 * @author Marchino Alessandro
 *
 */
public class ReflectionUtilTest extends BaseJUnit4TestCase {
	
	/**
	 * Test
	 */
	@Test
	public void setAndGet() {
		final String methodName = "setAndGet";
		ElementoCapitolo elementoCapitolo = new ElementoCapitolo();
		
		log.info(methodName, ToStringBuilder.reflectionToString(elementoCapitolo, ToStringStyle.MULTI_LINE_STYLE));
		
		String descrizione = "Capitolo di prova";
		ReflectionUtil.setField(elementoCapitolo, "descrizione", descrizione);
		assertNotNull("Il campo non e' stato injettato correttamente", ReflectionUtil.getField(elementoCapitolo, "descrizione", String.class));
		
		log.info(methodName, ToStringBuilder.reflectionToString(elementoCapitolo, ToStringStyle.MULTI_LINE_STYLE));
		
	}
	
}
