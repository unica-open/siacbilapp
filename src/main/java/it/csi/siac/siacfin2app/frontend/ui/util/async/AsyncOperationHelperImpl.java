/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.async;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsincResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Implementazione dell'helper per le azioni asincrone.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/05/2014
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AsyncOperationHelperImpl implements AsyncOperationHelper {
	
	private final LogUtil log = new LogUtil(getClass());
	
	@Autowired private transient OperazioneAsincronaService operazioneAsincronaService;
	
	@Override
	public InserisciOperazioneAsincResponse inserisciOperazioneAsincrona(Account account, AzioneRichiesta azioneRichiesta, Ente ente, Richiedente richiedente)
			throws WebServiceInvocationFailureException {
		
		InserisciOperazioneAsinc request = costruisciRequestInserisciOperazioneAsinc(account, azioneRichiesta, ente, richiedente);
		log.logXmlTypeObject(request, "Request");
		
		InserisciOperazioneAsincResponse response;
		try {
			 response = operazioneAsincronaService.inserisciOperazioneAsinc(request);
		} catch(Exception e) {
			throw new WebServiceInvocationFailureException(e);
		}
		
		log.logXmlTypeObject(response, "Response");
		
		return response;
	}

	@Override
	public GetOperazioneAsincResponse getOperazioneAsinc(Integer idAzioneAsync, Richiedente richiedente) throws WebServiceInvocationFailureException {
		GetOperazioneAsinc request = costruisciRequestGetAzioneAsinc(idAzioneAsync, richiedente);
		log.logXmlTypeObject(request, "Request");
		
		GetOperazioneAsincResponse response;
		try {
			 response = operazioneAsincronaService.getOperazioneAsinc(request);
		} catch(Exception e) {
			throw new WebServiceInvocationFailureException(e);
		}
		
		log.logXmlTypeObject(response, "Response");
		
		return response;
	}
	
	
	
	
	
	/**
	 * Costruisce la request per il servizio di {@link InserisciOperazioneAsinc}.
	 * 
	 * @param account         l'account relativo alla richiesta
	 * @param azioneRichiesta l'azione richiesta
	 * @param ente            l'ente
	 * @param richiedente     il richiedente
	 * 
	 * @return la request creata
	 */
	private InserisciOperazioneAsinc costruisciRequestInserisciOperazioneAsinc(Account account, AzioneRichiesta azioneRichiesta, Ente ente, Richiedente richiedente) {
		InserisciOperazioneAsinc request = new InserisciOperazioneAsinc();
		
		request.setAccount(account);
		request.setAzioneRichiesta(azioneRichiesta);
		request.setDataOra(new Date());
		request.setEnte(ente);
		request.setRichiedente(richiedente);
		
		return request;
	}
	
	/**
	 * Costruisce la request per il servizio di {@link GetOperazioneAsinc}.
	 * 
	 * @param idAzioneAsync l'id dell'azione asincrona
	 * @param richiedente   il richiedente
	 * 
	 * @return la request creata
	 */
	private GetOperazioneAsinc costruisciRequestGetAzioneAsinc(Integer idAzioneAsync, Richiedente richiedente) {
		GetOperazioneAsinc request = new GetOperazioneAsinc();
		
		request.setDataOra(new Date());
		request.setIdOperazione(idAzioneAsync);
		request.setRichiedente(richiedente);
		
		return request;
	}
	
}
