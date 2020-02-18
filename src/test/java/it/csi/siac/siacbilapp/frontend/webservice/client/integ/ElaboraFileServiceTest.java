/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.webservice.client.integ;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.webservice.client.BaseProxyServiceTest;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;
import it.csi.siac.siacintegser.frontend.webservice.ElaboraFileService;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;

/**
 * Test per i servizi dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 *
 */
public class ElaboraFileServiceTest extends BaseProxyServiceTest<ElaboraFileService> {
	
	@Override
	protected String getEndpoint() {
		return "http://127.0.0.1:8080/siacbilser/ElaboraFileService";
//		return "http://tst-srv1-forn2.bilancio.csi.it/siacbilser/CausaleService";
	}
	
	/**
	 * Elaborazione del file async
	 */
	@Test
	public void elaboraFileAsync() {
		ElaboraFile req = new ElaboraFile();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAccount(req.getRichiedente().getAccount());
		req.setBilancio(getBilancio(131, 2017));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		String fileName = "M2017_01_0100S_FLUSSO PER CONTABILIA_19102017_reduced.txt";
		
		File file = new File();
		file.setTipo(new TipoFile("STIPE_ONERI"));
		file.setNome(fileName);

		byte[] contenuto;
		try {
			contenuto = getTestFileBytes("docs/test/stipendi/" + fileName);
		} catch (IOException e) {
			fail("impossibile leggere il file di test: " + e.getMessage());
			return;
		}
		file.setContenuto(contenuto);
		
		req.setFile(file);
		
		AsyncServiceRequestWrapper<ElaboraFile> wr = new AsyncServiceRequestWrapper<ElaboraFile>();
		wr.setAccount(req.getAccount());
		wr.setDataOra(req.getDataOra());
		wr.setEnte(req.getEnte());
		wr.setRichiedente(req.getRichiedente());
		wr.setRequest(req);
		wr.setAzioneRichiesta(create(AzioneRichiesta.class, 66027706));
		wr.getAzioneRichiesta().setAzione(create(Azione.class, 4321));
		
		AsyncServiceResponse res = service.elaboraFileAsync(wr);
		assertNotNull(res);
	}
}
