/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.vincolo;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.vincolo.RicercaVincoloModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione della ricerca del Vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/12/2013
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaVincoloAction extends GenericVincoloAction<RicercaVincoloModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// SIAC-5076: caricamento Genere Vincolo
		caricaGenereVincolo();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Ricerca del vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaCDU() {
		final String methodName = "ricercaCDU";
		RicercaVincolo request = model.creaRequestRicercaVincolo();
		logServiceRequest(request);
		
		RicercaVincoloResponse response = vincoloCapitoloService.ricercaVincolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		if(response.getVincoloCapitoli().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VINCOLO, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_VINCOLO, response.getVincoloCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca di dettaglio dei capitoli legati al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String ricercaDettaglioCapitoli() {
		final String methodName = "ricercaDettaglioCapitoli";
		
		// Effettuo una ricerca di dettaglio per reperire i dati dei capitoli
		log.debug(methodName, "Ricerco il vincolo");
		RicercaDettaglioVincolo request = model.creaRequestRicercaDettaglioVincolo();
		logServiceRequest(request);
		
		RicercaDettaglioVincoloResponse response = vincoloCapitoloService.ricercaDettaglioVincolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "Vincolo ottenuto");
		
		List<Capitolo<?, ?>> listaCapitoliEntrata = null;
		List<Capitolo<?, ?>> listaCapitoliUscita = null;
		
		try {
			listaCapitoliEntrata = ricercaCapitoliEntrata(response);
			listaCapitoliUscita = ricercaCapitoliUscita(response);
		} catch(ApplicationException e) {
			log.error(methodName, e.getMessage());
			return SUCCESS;
		}
		
		// Imposto i dati dei capitoli
		model.impostaDati(response, listaCapitoliEntrata, listaCapitoliUscita);
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		Vincolo vincolo = model.getVincolo();
		Capitolo<?, ?> capitolo = model.getCapitolo();
		
		boolean formValido =
				// Controllo tipo di bilancio
			checkCampoValorizzato(vincolo.getTipoVincoloCapitoli(), "Bilancio") ||
				// Controllo il capitolo
			checkStringaValorizzata(model.getTipoCapitolo(), "Capitolo") ||
			checkCampoValorizzato(capitolo.getNumeroCapitolo(), "Numero Capitolo") ||
			checkCampoValorizzato(capitolo.getNumeroArticolo(), "Numero Articolo") ||
			checkCampoValorizzato(capitolo.getNumeroUEB(), "Numero UEB") ||
				// Controllo gli altri dati del vincolo
			checkStringaValorizzata(vincolo.getCodice(), "Codice") ||
			checkStringaValorizzata(vincolo.getDescrizione(), "Descrizione") ||
			checkCampoValorizzato(vincolo.getFlagTrasferimentiVincolati(), "Trasferimenti Vincolati") ||
			// SIAC-5076
			checkPresenzaIdEntita(vincolo.getGenereVincolo());
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		} else if(capitolo.getNumeroUEB() != null && (capitolo.getNumeroCapitolo() == null || capitolo.getNumeroArticolo() == null)) {
			addErrore(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il numero di capitolo e di articolo devono essere valorizzati"));
		} else if(capitolo.getNumeroArticolo() != null && capitolo.getNumeroCapitolo() == null) {
			addErrore(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il numero di capitolo deve essere valorizzato"));
		}
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
