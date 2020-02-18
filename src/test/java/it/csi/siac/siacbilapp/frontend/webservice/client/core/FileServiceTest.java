/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.core;

import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.FileService;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile.CriteriRicercaFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFileResponse;

/**
 * Test per il CommonService.
 * 
 * @author Marchino Alessandro
 *
 */
public class FileServiceTest extends BaseProxyServiceTest<FileService> {
	
	@Override
	protected String getEndpoint() {
		return "http://dev-www.ruparpiemonte.it/siacbilser/FileService";
	}
	
	/**
	 * Test
	 */
	@Test
	public void ricercaFile() {
		RicercaFile request = new RicercaFile();
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteTest());
		request.setParametriPaginazione(getParametriPaginazioneTest());
		
		CriteriRicercaFile criteri = new CriteriRicercaFile();
		request.setCriteri(criteri);
		
		criteri.setUid(Integer.valueOf(106));
		//criteri.setNome("RegistroIvaAcquistiIvaImmediata.pdf");
		
		RicercaFileResponse response = service.ricercaFile(request);
		logResponse(response);
	}
	
}
