/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import it.csi.siac.siacbilapp.BaseJUnit4TestCase;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;

/**
 * @author Marchino Alessandro
 *
 */
public class ComparatorUtilsTest extends BaseJUnit4TestCase {
	
	/**
	 * Test per il sort e la ricerca profonda.
	 */
	@Test
	public void sortAndSearchDeep() {
		Random r = new Random();
		List<ElementoPianoDeiConti> list = new ArrayList<ElementoPianoDeiConti>();
		int uid = r.nextInt();
		for(int i = 0; i < 2; i++) {
			ElementoPianoDeiConti m = new ElementoPianoDeiConti();
			int valore = r.nextInt();
			m.setUid(valore);
			m.setCodice(valore + "");
			m.setDescrizione(valore + "");
			m.setDataCreazione(new Date(r.nextLong()));
			for(int j = 0; j < 2; j++) {
				ElementoPianoDeiConti e = new ElementoPianoDeiConti();
				int val = r.nextInt();
				long l = r.nextLong();
				e.setUid(val);
				e.setCodice(val + "");
				e.setDescrizione(val + "");
				e.setDataCreazione(new Date(l));
				m.getElemPdc().add(e);
			}
			list.add(m);
		}
		
		ElementoPianoDeiConti m = new ElementoPianoDeiConti();
		m.setUid(uid);
		m.setCodice(uid + "");
		m.setDescrizione(uid + "");
		m.setDataCreazione(new Date(r.nextLong()));
		list.get(list.size() - 1).getElemPdc().add(m);
		//list.add(m);
		
		ComparatorUtils.sortDeepByCodice(list);
		
		ElementoPianoDeiConti m1 = new ElementoPianoDeiConti();
		m1.setUid(uid);
		
		log.debug("sortAndSearchDeepTest", list + "\n\n\n");
		ComparatorUtils.searchByUidWithChildren(list, m1);
		//searchByUid(list, m1);
	}
	
}
