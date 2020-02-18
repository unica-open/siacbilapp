/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.stornoueb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.stornoueb.RicercaStornoUEBModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEBResponse;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la ricerca dello storno UEB.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaStornoUEBAction extends GenericBilancioAction<RicercaStornoUEBModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9096744698157930495L;
	
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		super.prepare();
		log.debug(methodName, "Caricamento della lista dei tipi di atto");
		
		// Caricamento lista dei tipi di atto
		caricaListeCodifiche();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Ricerca con operazioni lo storno UEB.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName = "ricercaConOperazioniCDU";
		
		log.debug(methodName, "Creazione della request");
		RicercaStornoUEB request = model.creaRequestRicercaStornoUEB();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca");
		RicercaStornoUEBResponse response = variazioneDiBilancioService.ricercaStornoUEB(request);
		log.debug(methodName, "Richiamato il WebService di ricerca");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato ottenuto dalla ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		
		log.debug(methodName, "Pulisco la sessione");
		// Ricordare pulire correttamente la sessione 
		sessionHandler.cleanAllSafely();

		// Mette in sessione :
		// La lista dei risultati : per poter visualizzare i dati trovati nella action di risultatiRicerca 
		// La request usata per chiamare il servizio di ricerca : per poterla riusare allo scopo di reperire un nuovo blocco di risultati
		log.debug(methodName, "Imposto in sessione la request");
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_STORNO, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_STORNO, response.getStorniUEB());
		log.debug(methodName, "Lista caricata in sessione: " + response.getStorniUEB());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		super.validate();
		
		final String methodName = "validate";
		log.debugStart(methodName, "Chiamata alla validate()");
		
		boolean formValido = false;
		
		// Check sul numero dello storno
		if(model.getNumeroStorno() != null && model.getNumeroStorno() != 0) {
			log.debug(methodName, "Il numero dello storno è valido");
			formValido = true;
		}
		
		// Check sul tipo di capitolo
		if(!formValido && model.getTipoCapitolo() != null) {
			log.debug(methodName, "Almeno un capitolo valido");
			formValido = true;
		}
		
		// Check sul provvedimento
		if(!formValido && model.getUidProvvedimento() != null && model.getUidProvvedimento() != 0) {
			log.debug(methodName, "Provvedimento valido");
			formValido = true;
		}

		// Controllo della validità del form
		if(!formValido) {
			// Nessun criterio di ricerca è stato apposto
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}
		
		log.debugEnd(methodName, "Fine validate() - formValido = " + formValido);
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste delle codifiche.
	 */
	private void caricaListeCodifiche() {
		final String methodName = "caricaListeCodifiche";
		log.debug(methodName, "Caricamento della lista dei tipi di atto");
		
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
		
		// Caricamento degli stati operativi
		List<StatoOperativoElementoDiBilancio> listaStatoOperativoElementoDiBilancio = 
				Arrays.asList(StatoOperativoElementoDiBilancio.values());
		model.setListaStatoOperativoElementoDiBilancio(listaStatoOperativoElementoDiBilancio);
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		if(!model.isGestioneUEB()) {
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("l'ente non gestisce l'informazione UEB").getTesto(), 
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
}
