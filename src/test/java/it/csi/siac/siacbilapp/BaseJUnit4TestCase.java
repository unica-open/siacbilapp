/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import junit.framework.TestCase;

/**
 * @author Marchino Alessandro
 */
@RunWith(JUnit4.class)
public abstract class BaseJUnit4TestCase extends TestCase {

	/** Il logger */
	protected final LogUtil log = new LogUtil(this.getClass());
	
	/** Le properties dell'endpoint */
	protected Properties endpointProperties;
	private Properties accountProperties;
	
	/**
	 * Inizializzazione
	 */
	@Before
	public void init() {
		accountProperties = readProperties("./account.properties");
		endpointProperties = readProperties("./endpoint.properties");
	}
	
	/**
	 * Lettura delle propertied da file
	 * @param filename il file da leggere
	 * @return le properties del file
	 */
	private Properties readProperties(String filename) {
		Properties properties = new Properties();
		
		InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
		if(is == null) {
			throw new IllegalArgumentException("Properties non lette");
		}
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new IllegalArgumentException("Errore nella lettura delle properties", e);
		}
		return properties;
	}

	/**
	 * Crea un'entit&agrave; con l'uid fornito
	 * @param <T> la tipizzazione dell'entita
	 * @param clazz la classe dell'entit&agrave:
	 * @param uid l'uid dell'istanza
	 * @return l'entit&agrave; creata
	 */
	protected <T extends Entita> T create(Class<T> clazz, int uid) {
		T obj;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossibile creare un'istanza per la classe " + clazz + " con uid " + uid, e);
		}
		obj.setUid(uid);
		return obj;
	}
	
	/**
	 * Ottiene dei parametri di paginazione di test: la prima pagina, con 100 elementi per pagina
	 * 
	 * @return i parametri di paginazione
	 */
	protected ParametriPaginazione getParametriPaginazioneTest() {
		return getParametriPaginazioneTest(100, 0);
	}
	
	/**
	 * Ottiene dei parametri di paginazione di test: la prima pagina, con 100 elementi per pagina
	 * 
	 * @param elementiPerPagina il numero di elementi per pagina
	 * @param numeroPagina      il numero di pagina
	 * 
	 * @return i parametri di paginazione
	 */
	protected ParametriPaginazione getParametriPaginazioneTest(int elementiPerPagina, int numeroPagina) {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(elementiPerPagina);
		parametriPaginazione.setNumeroPagina(numeroPagina);
		return parametriPaginazione;
	}
	
	/**
	 * Ottiene un richiedente per il test.
	 * 
	 * @param codiceFiscale il codice fiscale dell'operatore
	 * @param uidAccount    l'uid dell'account
	 * @param uidEnte       l'uid dell'ente
	 * 
	 * @return il richiedente
	 */
	protected Richiedente getRichiedenteTest(String codiceFiscale, int uidAccount, int uidEnte) {
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale(codiceFiscale);
		richiedente.setOperatore(operatore);
		richiedente.setAccount(getAccountTest(uidAccount, uidEnte));
		return richiedente;
	}

	/**
	 * Ottiene un richiedente per il test.
	 * 
	 * @return il richiedente
	 */
	protected Richiedente getRichiedenteTest() {
		return getRichiedenteTest("AAAAAA00A11B000J", 1, 1);
	}
	
	/**
	 * Ottiene un richiedente per il test: imposta anche l'account perch&eacute; i servizi di fin recuperano
	 * l'ente dall'account. In caso contrario null pointer
	 * 
	 * @param ente l'ente
	 * 
	 * @return il richiedente
	 */
	protected Richiedente getRichiedenteTest(Ente ente) {
		return getRichiedenteTest("AAAAAA00A11B000J", 1, ente.getUid());
	}
	
	/**
	 * Ottiene un ente per il test.
	 * 
	 * @return l'ente
	 */
	protected Ente getEnteTest() {
		return getEnteTest(1);
	}
	
	/**
	 * Ottiene un ente per il test.
	 * 
	 * @param uid l'uid dell'ente
	 * 
	 * @return l'ente
	 */
	protected Ente getEnteTest(int uid) {
		return create(Ente.class, uid);
	}

	/**
	 * Ottiene il bilancio per uid e anno
	 * @param uid l'uid del bilancio
	 * @param anno l'anno di bilancio
	 * @return il bilancio
	 */
	protected Bilancio getBilancio(int uid, int anno) {
		Bilancio b = create(Bilancio.class, uid);
		b.setAnno(anno);
		return b;
	}
	
	/**
	 * Ottiene l'account di test.
	 * 
	 * @param ente l'ente dell'account
	 * 
	 * @return l'account
	 */
	protected Account getAccountTest(Ente ente) {
		return getAccountTest(1, ente.getUid());
	}
	
	/**
	 * Ottiene l'account di test.
	 * 
	 * @param uidAccount l'uid dell'account
	 * @param uidEnte    l'uid dell'ente
	 * 
	 * @return l'account
	 */
	protected Account getAccountTest(int uidAccount, int uidEnte) {
		Account account = create(Account.class, uidAccount);
		account.setEnte(getEnteTest(uidEnte));
		return account;
	}
	
	/**
	 * Ottiene un richiedente di test dal file di properties.
	 *
	 * @param ambiente l'ambiente da usare (forn2, coll, ...)
	 * @param codiceEnte il codice dell'ente (coto, regp, crp, edisu...)
	 * 
	 * @return the richiedente test
	 */
	protected Richiedente getRichiedenteByProperties(String ambiente, String codiceEnte) {
		String codiceFiscale = accountProperties.getProperty(ambiente + "." + codiceEnte + ".codicefiscale");
		int uidAccount = Integer.parseInt(accountProperties.getProperty(ambiente + "." + codiceEnte + ".accountid"));
		int uidEnte = Integer.parseInt(accountProperties.getProperty(ambiente + "." + codiceEnte + ".enteproprietarioid"));
		
		return getRichiedenteTest(codiceFiscale, uidAccount, uidEnte);
	}
	
	/**
	 * Wrappa la request per l'invocazione asincrona.
	 * @param <REQ> la tipizzazione della request
	 * 
	 * @param request la request
	 * @return il wrapper
	 */
	protected <REQ extends ServiceRequest> AsyncServiceRequestWrapper<REQ> wrapRequestToAsync(REQ request) {
		AsyncServiceRequestWrapper<REQ> result = new AsyncServiceRequestWrapper<REQ>();
		
		Ente ente = getEnteTest();
		Account account = getAccountTest(ente);
		
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		azioneRichiesta.setAccount(account);
		Azione azione = new Azione();
		azioneRichiesta.setAzione(azione);
		
		// Mappatura dei campi
		result.setAzioneRichiesta(azioneRichiesta);
		result.setDataOra(request.getDataOra());
		result.setEnte(ente);
		result.setRequest(request);
		result.setRichiedente(request.getRichiedente());
		result.setAccount(account);
		
		return result;
	}
	
	/**
	 * Logga la request
	 * @param <R> la tipizzazione della request
	 * 
	 * @param request la request da loggare
	 */
	protected <R extends ServiceRequest> void logRequest(R request) {
		log.logXmlTypeObject(request, "REQUEST");
	}
	
	/**
	 * Logga la response
	 * @param <R> la tipizzazione della response
	 * 
	 * @param response la request da loggare
	 */
	protected <R extends ServiceResponse> void logResponse(R response) {
		log.logXmlTypeObject(response, "RESPONSE");
	}
	
	/**
	 * Formattazione della data
	 * @param dateString la data da convertire
	 * @return la data
	 */
	protected Date formatDate(String dateString) {
		return formatDate("dd/MM/yyyy", dateString);
	}
	
	/**
	 * Formattazione della data
	 * @param format il formato
	 * @param dateString la data da convertire
	 * @return la data
	 */
	protected Date formatDate(String format, String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Illegal date string " + dateString, e);
		}
	}
	
	/**
	 * Gets the test file bytes.
	 *
	 * @param fileName the file name
	 * @return the test file bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static byte[] getTestFileBytes(String fileName) throws IOException {
		
		byte[] byteArray;
		java.io.File f = new java.io.File(fileName);
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		 
		try {
			baos = new ByteArrayOutputStream();
			fis = new FileInputStream(f);
			int b;
			while ((b = fis.read()) != -1){
				baos.write(b);
			}
			
			byteArray = baos.toByteArray();
			
		} finally {
			try {
				if(fis!=null) {
					fis.close();
				}
				fis = null;
				f = null;
				if(baos!=null){
					baos.close();
				}
				baos = null;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return byteArray;
	}
}
