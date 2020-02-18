/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.async;

import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetOperazioneAsincResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Helper per le azioni asincrone.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/05/2014
 *
 */
public interface AsyncOperationHelper {
	
	/**
	 * Inserisce l'operazione asincrona e restituisce il risultato dell'invocazione.
	 * 
	 * @param account         l'account relativo alla richiesta
	 * @param azioneRichiesta l'azione richiesta
	 * @param ente            l'ente
	 * @param richiedente     il richiedente
	 * 
	 * @return la response del servizio di {@link InserisciOperazioneAsinc}
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del WebService restituisca un'eccezione
	 */
	InserisciOperazioneAsincResponse inserisciOperazioneAsincrona(Account account, AzioneRichiesta azioneRichiesta, Ente ente, Richiedente richiedente)
		throws WebServiceInvocationFailureException;
	
	/**
	 * Effettua il polling dell'operazione asincrona.
	 * 
	 * @param idAzioneAsync l'id dell'azione asincrona
	 * @param richiedente   il richiedente
	 * 
	 * @return la response del servizio di {@link GetOperazioneAsincResponse}
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del WebService restituisca un'eccezione
	 */
	GetOperazioneAsincResponse getOperazioneAsinc(Integer idAzioneAsync, Richiedente richiedente) throws WebServiceInvocationFailureException;
	
}
