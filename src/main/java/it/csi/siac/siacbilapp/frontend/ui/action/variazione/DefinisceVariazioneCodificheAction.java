/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.DefinisceVariazioneCodificheModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Classe di Action per la gestione della consultazione della Variazione Importi.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class DefinisceVariazioneCodificheAction extends DefinisceVariazioneBaseAction<DefinisceVariazioneCodificheModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	
	@Override
	public void prepareExecute() throws Exception{
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		log.debug(methodName, "Ottengo l'azione richiesta dalla sessione");
		AzioneRichiesta azioneRichiesta = sessionHandler.getAzioneRichiesta();
		log.debug(methodName, "Injetto le variabili del processo");
		model.impostaDatiNelModel(azioneRichiesta);
//		model.injettaVariabiliProcesso(azioneRichiesta);

		log.debug(methodName, "Creo la request per la ricerca del capitolo");
		if(model.getUidVariazione() == null) {
			log.debug(methodName, "Errore: non presente uid variazione da consultare");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		
		RicercaDettaglioVariazioneCodifiche request = model.creaRequestRicercaVariazioneCodifiche();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
	
		RicercaDettaglioVariazioneCodificheResponse response = variazioneDiBilancioService.ricercaDettaglioVariazioneCodifiche(request);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio della variazione di bilancio");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		RicercaProvvedimento requestProvvedimento = model.creaRequestRicercaProvvedimento(response);
		RicercaProvvedimentoResponse responseProvvedimento = provvedimentoService.ricercaProvvedimento(requestProvvedimento);
		log.debug(methodName, "Richiamato il WebService per la ricerca del Provvedimento");
		logServiceResponse(responseProvvedimento);
		
		if(responseProvvedimento.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
		}
		
		//SIAC-7555
		controllaStatoOperativoVariazione(response.getVariazioneCodificaCapitolo());
		//
		
		log.debug(methodName, "Impostazione dei dati nel model e in sessione");
		model.impostaDatiDaResponseESessione(response, responseProvvedimento, sessionHandler);
		log.debug(methodName, "Dati impostati nel model e nella sessione");
		
		return SUCCESS;
	}
	
	/**
	 * Definisci Variazione
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String definisciVariazione() {
		final String methodName = "definisciVariazione";
		
		// Se non vi è coerenza, ritorno subito indietro
		if(!isControllaCoerenzaApplicazioneFase(model.getApplicazioneVariazione())) {
			log.debug(methodName, "Mancanza di coerenza tra fase di bilancio e applicazione della variazione");
			addErrore(ErroreBil.DEFINIZIONE_NON_POSSIBILE_PERCHE_FASE_DI_BILANCIO_INCONGRUENTE.getErrore("Definizione"));
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		
		VariazioneCodificaCapitolo variazione = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONI, VariazioneCodificaCapitolo.class);
		
		model.setVariazioneCodificaCapitolo(variazione);
		log.debug(methodName, "Creazione request per la definizione");
		DefinisceVariazioneCodifiche request = model.creaRequestDefinisceVariazioneCodifiche();
		logServiceRequest(request);
		
		log.debug(methodName, "Invocazione del WebService");
		DefinisceVariazioneCodificheResponse response = variazioneDiBilancioService.definisceVariazioneCodifiche(request);
		log.debug(methodName, "Invocato il WebService di definizione");
		logServiceResponse(response);
	
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}

		addInformazione(new Informazione("CRU_CON_2001", "L'operazione è stata completata con successo"));
		
		model.setDefinizioneEseguita(Boolean.TRUE);
		model.setStatoVariazione(StatoOperativoVariazioneBilancio.DEFINITIVA);
		
		// Imposto i dati nel model
		model.injettaDatiPostDefinizione(response);
		return SUCCESS;
	}
	
}
