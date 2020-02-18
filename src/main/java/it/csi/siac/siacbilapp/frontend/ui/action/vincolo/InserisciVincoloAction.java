/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.vincolo;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.model.vincolo.InserisciVincoloModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoVincoloCapitoli;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione dell'inserimento del Vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/12/2013
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciVincoloAction extends GenericVincoloAction<InserisciVincoloModel> {
	
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
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		log.debug(methodName, "Ricerco il bilancio");
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		log.debug(methodName, "Fase del bilancio: " + faseBilancio);
		
		checkCasoDUsoApplicabile(faseBilancio);
		
		// Impostazione dei valori di default
		if(model.getVincolo() == null) {
			model.setVincolo(new Vincolo());
		}
		
		if(FaseBilancio.PREVISIONE.equals(faseBilancio)) {
			model.setBilancioGestioneAbilitato(Boolean.FALSE);
			model.getVincolo().setTipoVincoloCapitoli(TipoVincoloCapitoli.PREVISIONE);
		} else if(FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)) {
			// Lascio entrambi i valori per il radio editabili
		} else {
			model.setBilancioPrevisioneAbilitato(Boolean.FALSE);
			model.getVincolo().setTipoVincoloCapitoli(TipoVincoloCapitoli.GESTIONE);
		}
		
		
		
		model.getVincolo().setFlagTrasferimentiVincolati(Boolean.FALSE);
		
		return SUCCESS;
	}
	
	/**
	 * Inserimento del vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciCDU() {
		final String methodName = "inserisciCDU";
		
		log.debug(methodName, "Controllo che non vi sia un vincolo già inserito");
		RicercaVincolo requestRicerca = model.creaRequestRicercaVincolo();
		logServiceRequest(requestRicerca);
		
		RicercaVincoloResponse responseRicerca = vincoloCapitoloService.ricercaVincolo(requestRicerca);
		logServiceResponse(responseRicerca);
		
		if(responseRicerca.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(requestRicerca, responseRicerca));
			addErrori(responseRicerca);
			return INPUT;
		}
		if(esisteAlmenoUnVincoloValido(responseRicerca)) {
			log.info(methodName, "Vi è già un vincolo in stato valido con la stessa anagrafica");
			addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Vincolo", model.getVincolo().getCodice()));
			return INPUT;
		}
		
		log.debug(methodName, "Nessun vincolo già presente");
		InserisceAnagraficaVincolo requestInserimento = model.creaRequestInserisceAnagraficaVincolo();
		logServiceRequest(requestInserimento);
		InserisceAnagraficaVincoloResponse responseInserimento = vincoloCapitoloService.inserisceAnagraficaVincolo(requestInserimento);
		logServiceResponse(responseInserimento);
		
		if(responseInserimento.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(requestInserimento, responseInserimento));
			addErrori(responseInserimento);
			return INPUT;
		}
		
		Vincolo vincolo = responseInserimento.getVincolo();
		
		// Injetto il vincolo ottenuto dalla response nel model
		model.setVincolo(vincolo);
		model.setIdVincolo(vincolo.getUid());
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		Vincolo vincolo = model.getVincolo();
		
		if(vincolo == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Bilancio"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
		} else {
			checkNotNull(vincolo.getTipoVincoloCapitoli(), "Bilancio");
			checkNotNullNorEmpty(vincolo.getCodice(), "codice");
		}
	}
	
	/**
	 * Controlla l'esistenza di almeno un vincolo in stato valido nella lista dei vincoli.
	 * 
	 * @param responseRicerca la response del servizio di ricerca del vincolo
	 * 
	 * @return <code>true</code> se vi &eacute; almeno un capitolo in stato valido; <code>false</code> altrimenti
	 */
	private boolean esisteAlmenoUnVincoloValido(RicercaVincoloResponse responseRicerca) {
		List<VincoloCapitoli> listaVincoli = responseRicerca.getVincoloCapitoli();
		
		if(listaVincoli.isEmpty()) {
			return false;
		}
		
		for(VincoloCapitoli v : listaVincoli) {
			if(StatoOperativo.VALIDO.equals(v.getStatoOperativo())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controlla che il caso d'uso sia applicabile.
	 * @param faseBilancio la fase di bilancio
	 * 
	 * @throws GenericFrontEndMessagesException nel caso in cui il caso d'uso non sia applicabile
	 */
	private void checkCasoDUsoApplicabile(FaseBilancio faseBilancio) {
		final String methodName = "checkCasoDUsoApplicabile";
		
		// Controllo che la fase non sia CHIUSO né PREDISPOSIZIONE_CONSUNTIVO
		boolean faseNonValida = FaseBilancio.CHIUSO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		log.debug(methodName, "La fase di bilancio è valida? " + !faseNonValida);
		if(faseNonValida) {
			Errore errore = ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("inserimento vincolo");
			throw new GenericFrontEndMessagesException(errore.getTesto(), GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
}
