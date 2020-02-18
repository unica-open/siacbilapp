/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.RicercaProgettoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione della ricerca del Progetto.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaProgettoAction extends GenericProgettoAction<RicercaProgettoModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		
		super.prepare();
		caricaListeCodifiche();
		caricaListaStati();
		
		log.debugEnd(methodName, "");
	}

	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	
	@Override
	public void validate() {
		final String methodName = "validate";
		log.debugStart(methodName, "Verifica campi");
		
		Progetto progetto = model.getProgetto();
		
		log.info(methodName, "anno progetto " + progetto.getBilancio().getAnno());
		
		boolean formValido =
			checkStringaValorizzata(progetto.getCodice(), "Codice Progetto") ||
			checkPresenzaIdEntita(progetto.getTipoAmbito()) ||
			checkStringaValorizzata(model.getFlagFondoPluriennaleVincolato(), "Rilevante a FPV") ||
			checkCampoValorizzato(progetto.getStatoOperativoProgetto(), "Stato") ||
			checkCampoValorizzato(progetto.getBilancio().getAnno(), "Anno Bilancio") ||
			checkStringaValorizzata(progetto.getDescrizione(), "Descrizione") ||
			checkCampoValorizzato(progetto.getDataAggiudicazioneGara(), "Data Aggiudicazione Gara")||
			checkCampoValorizzato(progetto.getDataIndizioneGara(), "Data Indizione Gara") ||
			checkCampoValorizzato(progetto.getInvestimentoInCorsoDiDefinizione(), "Stato") ||
			checkStringaValorizzata(model.getFlagInvestimentoInCorsoDiDefinizione(), "Investimento In Corso Di Definizione") ||
			checkPresenzaIdEntita(progetto.getAttoAmministrativo());
		
		
		// Check sul provvedimento
		if(!formValido && model.getUidProvvedimento() != null && model.getUidProvvedimento() != 0) {
			log.debug(methodName, "Provvedimento valido");
			formValido = true;
		}
	
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	
	/**
	 * Ricerca del progetto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaProgetto() {
		final String methodName = "ricercaProgetto";
		log.debugStart(methodName, "Effettua la ricerca nel caso non sia stato specificato un codice progetto");

		RicercaSinteticaProgetto request = model.creaRequestRicercaSinteticaProgetto();
		logServiceRequest(request);
		
		RicercaSinteticaProgettoResponse response = progettoService.ricercaSinteticaProgetto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		if(response.getProgetti().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		log.debug(methodName, "Totale: "+response.getProgetti().getTotaleElementi());
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PROGETTO, request);
					
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PROGETTO, response.getProgetti());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
				
		return SUCCESS;
	}
	
		
	/**
	 * Carica la lista stati necessaria alla ricerca 
	 * 
	 */
	private void caricaListaStati() {
		final String methodName = "caricaListaStati";
		log.debug(methodName, "Caricamento della lista degli stati");
		
		
		List<StatoOperativoProgetto> listaStato = Arrays.asList(StatoOperativoProgetto.values());
		model.setListaStatoProgetto(listaStato);
	}
	
}



