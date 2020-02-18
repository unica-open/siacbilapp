/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.soggetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.soggetto.SelezionaSoggettoModel;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di Action per la gestione dei Soggetti.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SelezionaSoggettoAction extends GenericBilancioAction<SelezionaSoggettoModel> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Autowired private transient SoggettoService soggettoService;
	
	/**
	 * Validazione per il metodo {@link SelezionaSoggettoAction#ricercaSoggetti()}.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	private boolean checkIsValidaRicercaSoggetti() {
		final String methodName = "checkIsValidaRicercaSoggetti";
		log.debugStart(methodName, "Verifica campi");
		
		checkCondition(model.getSoggetto() != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());

		Soggetto soggetto = model.getSoggetto();
		boolean formValido = 	
				checkStringaValorizzata(soggetto.getCodiceSoggetto(), "Codice Soggetto") ||
				checkStringaValorizzata(soggetto.getMatricola(), "Matricola") ||
				checkStringaValorizzata(soggetto.getCodiceFiscale(), "Codice Fiscale") ||
				checkStringaValorizzata(soggetto.getPartitaIva(), "Stato") ||
				//SIAC-6565-CR1215
				checkStringaValorizzata(soggetto.getDenominazione(), "Descrizione") ||
				checkStringaValorizzata(soggetto.getEmailPec(), "Email PEC") ||
				checkStringaValorizzata(soggetto.getCodDestinatario(), "Codice Destinatario") ||
				checkPresenzaIdEntita(model.getClasseSoggetto());
		
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
		log.debugEnd(methodName, "");
		
		return !hasErrori();
	}
	
	
	/**
	 * Ricerca dei soggetti sulla base dei criteri impostati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSoggetti() {
		final String methodName = "ricercaSoggetti";
		
		if(!checkIsValidaRicercaSoggetti()) {
			log.debug(methodName, "Validazione fallita");
			return SUCCESS;
		}
		
		log.debug(methodName, "Effettua la ricerca in base ai parametri impostati");
		
		RicercaSoggetti request = model.creaRequestRicercaSoggetti();
		logServiceRequest(request);
		
		RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return SUCCESS;
		}
		
		List<Soggetto> listaSoggetti = response.getSoggetti();
		
		if(listaSoggetti == null || listaSoggetti.isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Totale: " + listaSoggetti.size());
		log.debug(methodName, "Ricerca effettuata con successo");
		
		model.setListaSoggetti(listaSoggetti);
		
		return SUCCESS;
	}
	
}
