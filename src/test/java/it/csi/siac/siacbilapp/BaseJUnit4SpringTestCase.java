/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.csi.siac.siaccommon.util.log.LogUtil;
import junit.framework.TestCase;

/**
 * @author Marchino Alessandro
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { 
//	"/spring/applicationContext-test.xml",
//	"/spring/serviceClientContext-test.xml"
//})
public abstract class BaseJUnit4SpringTestCase extends TestCase {

	/** Il logger */
	protected final LogUtil log = new LogUtil(this.getClass());

	/** L'application context */
	@Autowired protected ApplicationContext applicationContext;

}
