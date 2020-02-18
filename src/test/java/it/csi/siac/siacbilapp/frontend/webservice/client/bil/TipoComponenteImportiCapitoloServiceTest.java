/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;

/**
 * Classe di test per i serviz&icirc; del Bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2014
 *
 */
public class TipoComponenteImportiCapitoloServiceTest extends BaseProxyServiceTest<TipoComponenteImportiCapitoloService> {
	
	@Override
	protected String getEndpoint() {
//		return "http://localhost:8080/siacbilser/ComponenteImportiCapitoloService";
		return "http://10.136.6.151/siacbilser/TipoComponenteImportiCapitoloService";
	}
	
		
	@Test
	public void ricercaTipoComponentiCapitolo() {
		RicercaTipoComponenteImportiCapitoloPerCapitolo req = new RicercaTipoComponenteImportiCapitoloPerCapitolo();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setCapitolo(create(CapitoloUscitaPrevisione.class, 87802));
		RicercaTipoComponenteImportiCapitoloPerCapitoloResponse response = service.ricercaTipoComponenteImportiCapitoloPerCapitolo(req);
		assertNotNull(response);
		
	}
}
