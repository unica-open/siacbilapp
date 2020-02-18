/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.bil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siacbilser.business.utility.helper.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccommon.util.JAXBUtility;

/**
 * Classe di test per i serviz&icirc; del Bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2014
 *
 */
public class ComponenteImportiCapitoloServiceTest extends BaseProxyServiceTest<ComponenteImportiCapitoloService> {
	
	@Override
	protected String getEndpoint() {
//		return "http://localhost:8080/siacbilser/ComponenteImportiCapitoloService";
		return "http://10.136.6.151/siacbilser/ComponenteImportiCapitoloService";
	}
	
	/**
	 * Inserimento della componente importi capitolo
	 */
	@Test
	public void inserisceComponenteImportiCapitolo() {
		// TODO: impostare l'uid corretto per capitolo e tipo componente
		int uidTipoComponenteImportiCapitolo = 1;
		int uidCapitolo = 106871;
		InserisceComponenteImportiCapitolo req = new InserisceComponenteImportiCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setCapitolo(create(CapitoloUscitaPrevisione.class, uidCapitolo));
		
		// 2020
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(Integer.valueOf(2020));
		cic.setTipoComponenteImportiCapitolo(create(TipoComponenteImportiCapitolo.class, uidTipoComponenteImportiCapitolo));
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(new BigDecimal("2.33"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
		
		// 2021
		cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(Integer.valueOf(2021));
		cic.setTipoComponenteImportiCapitolo(create(TipoComponenteImportiCapitolo.class, uidTipoComponenteImportiCapitolo));
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(new BigDecimal("7"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);

		// 2022
		cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(Integer.valueOf(2019));
		cic.setTipoComponenteImportiCapitolo(create(TipoComponenteImportiCapitolo.class, uidTipoComponenteImportiCapitolo));
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(new BigDecimal("5.8"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
		
		InserisceComponenteImportiCapitoloResponse res = service.inserisceComponenteImportiCapitolo(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaCompoentiCapitolo() {
		RicercaComponenteImportiCapitolo req = new RicercaComponenteImportiCapitolo();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setCapitolo(create(CapitoloUscitaPrevisione.class, 87802));
		RicercaComponenteImportiCapitoloResponse response = service.ricercaComponenteImportiCapitolo(req);
		assertNotNull(response);
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnno(response.getListaImportiCapitolo());
		StringBuilder sb = new StringBuilder();
		
		for (ImportiCapitoloPerComponente importiCapitoloPerComponente : importiComponentiCapitolo) {
			sb.append(JAXBUtility.marshall(importiCapitoloPerComponente)).append("\n");
		}
		
		System.out.println(sb.toString());
		
	}
}
