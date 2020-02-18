/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.convalida.elenco;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;

/**
 * Classe di Action per la convalida dell'AllegatoAtto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/10/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(ConvalidaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class ConvalidaAllegatoAttoElenchiAction extends ConvalidaAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4561331207382251492L;

	/**
	 * Ottiene le liste degli elenchi per le invocazioni AJAX.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListeElenchi() {
		// Segnaposto per le chiamate AJAX
		impostaConvalidabiliENonConvalidabili(model.getListaElencoDocumentiAllegato());
		return SUCCESS;
	}
	
	/**
	 * Ingresso nello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		// Non faccio alcunche', entro solo nella maschera
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #convalidaElenco()}.
	 */
	public void prepareConvalidaElenco() {
		// Pulizia degli uid
		model.setListaUid(new ArrayList<Integer>());
		// Pulizia del flag di convalida
		model.setConvalidaManuale(null);
	}
	
	/**
	 * Convalida gli elenchi selezionati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String convalidaElenco() {
		final String methodName = "convalidaElenco";
		
		// Creazione della request
		ConvalidaAllegatoAttoPerElenchi req = model.creaRequestConvalidaAllegatoAttoPerElenchi();
		logServiceRequest(req);
		
		// SIAC-5575: l'operazione asincrona deve essere sotto nome 'CONVALIDA'
		AzioneRichiesta azioneRichiesta = AzioniConsentite.ALLEGATO_ATTO_CONVALIDA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		// Invocazione del servizio asincrono
		AsyncServiceResponse res = allegatoAttoService.convalidaAllegatoAttoPerElenchiAsync(wrapRequestToAsync(req, azioneRichiesta));
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione attivata per gli elenchi " + model.getListaUid());
		// Imposto l'informazione di attivazione
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Convalida elenco allegato atto", ""));
		try {
			// Ricerco il dettaglio dell'allegato e ne ricalcolo i dati
			ottieniDettaglioAllegatoEdImpostaElenchi();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			// Errore: esco
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * Ottiene il dettaglio dell'allegato ed imposta gli elenchi nel model.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ottieniDettaglioAllegatoEdImpostaElenchi() throws WebServiceInvocationFailureException {
		// Ottiene il dettaglio dell'allegato atto
		RicercaDettaglioAllegatoAttoResponse response = ricercaDettaglioAllegatoAtto(model.getAllegatoAtto());
		// Ottiene l'allegato dalla response
		AllegatoAtto aa = response.getAllegatoAtto();
		// Imposto i dati nel model
		model.setAllegatoAtto(aa);
		model.setListaElencoDocumentiAllegato(aa.getElenchiDocumentiAllegato());
		// Imposto i dati convalidabili e non convalidabili
		impostaConvalidabiliENonConvalidabili(aa.getElenchiDocumentiAllegato());
	}
	
	/**
	 * Validazione per il metodo {@link #convalidaElenco()}.
	 */
	public void validateConvalidaElenco() {
		// La lista degli uid deve essere popolata
		checkCondition(!model.getListaUid().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco da convalidare"));
	}
	
	/**
	 * Preparazione per il metodo {@link #rifiutaElenco()}.
	 */
	public void prepareRifiutaElenco() {
		// Pulisco la lista degli uid
		model.setListaUid(new ArrayList<Integer>());
	}
	
	/**
	 * Rifiuta gli elenchi selezionati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String rifiutaElenco() {
		final String methodName = "rifiutaElenco";
		
		// Creo la request del servizio
		RifiutaElenchi req = model.creaRequestRifiutaElenchi(model.getListaUid(), null);
		logServiceRequest(req);
		// Invoco il servizio asincrono
		AsyncServiceResponse res = allegatoAttoService.rifiutaElenchiAsync(wrapRequestToAsync(req));
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione attivata per gli elenchi " + model.getListaUid());
		// Aggiungo l'informazione di elaborazione attiva
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Rifiuta elenco allegato atto", ""));
		try {
			// Ricerco il dettaglio dell'allegato e ne ricalcolo i dati
			ottieniDettaglioAllegatoEdImpostaElenchi();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			// Errore: esco
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #rifiutaElenco()}.
	 */
	public void validateRifiutaElenco() {
		// La lista degli uid deve essere popolata
		checkCondition(!model.getListaUid().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco da rifiutare"));
	}
	
	/**
	 * Rifiuta l'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String rifiutaAtto() {
		final String methodName = "rifiutaAtto";
		
		// Creo la request del servizio
		RifiutaElenchi req = model.creaRequestRifiutaElenchi(null, StatoOperativoAllegatoAtto.RIFIUTATO);
		logServiceRequest(req);
		// Invoco il servizio asincrono
		AsyncServiceResponse response = allegatoAttoService.rifiutaElenchiAsync(wrapRequestToAsync(req));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione attivata per l'atto " + model.getAllegatoAtto().getUid());
		// Aggiungo il messaggio di elaborazione attivata
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Rifiuta elenchi per annullamento allegato atto", ""));
		try {
			// Ricerco il dettaglio dell'allegato e ne ricalcolo i dati
			ottieniDettaglioAllegatoEdImpostaElenchi();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			// Errore: esco
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * Popola le tabelle degli elenchi convalidabili e non convalidabili.
	 * 
	 * @param list la lista tramite cui effettuare i popolamenti
	 */
	private void impostaConvalidabiliENonConvalidabili(List<ElencoDocumentiAllegato> list) {
		// Inizializzo le liste che conterranno i dati
		List<ElencoDocumentiAllegato> convalidabili = new ArrayList<ElencoDocumentiAllegato>();
		List<ElencoDocumentiAllegato> nonConvalidabili = new ArrayList<ElencoDocumentiAllegato>();
		
		for(ElencoDocumentiAllegato eda : list) {
			if(isNonConvalidabileElenco(eda)) {
				// Non convalidabile: nella lista corretta
				nonConvalidabili.add(eda);
			} else {
				// Convalidabile: nella lista corretta
				convalidabili.add(eda);
			}
		}
		
		// Imposto nel model
		model.setListaConvalidabili(convalidabili);
		model.setListaNonConvalidabili(nonConvalidabili);
		// Computo i totali
		model.computeTotali();
	}

	/**
	 * L'elenco &eacute; convalidato ovvero non convalidabile se:
	 * <ul>
	 *     <li>
	 *         {@link ElencoDocumentiAllegato#getStatoOperativoElencoDocumenti() statoOperativoElenco}
	 *         diverso da {@link StatoOperativoElencoDocumenti#COMPLETATO COMPLETATO}
	 *     </li>
	 *     <li>
	 *         {@link ElencoDocumentiAllegato#getTotaleDaConvalidareSpesa() importoDaConvalidareSpesa} = 0
	 *         e {@link ElencoDocumentiAllegato#getTotaleDaConvalidareEntrata() importoDaConvalidareEntrata} = 0
	 *     </li>
	 * </ul>
	 * 
	 * @param eda l'elenco da controllare
	 * 
	 * @return true se l'elenco sia da apporre nella lista dei non convalidabili; <code>false</code> in caso contrario
	 */
	private boolean isNonConvalidabileElenco(ElencoDocumentiAllegato eda) {
		// Stato operativo non COMPLETATO
		return !StatoOperativoElencoDocumenti.COMPLETATO.equals(eda.getStatoOperativoElencoDocumenti())
				// Importo spesa e importo entrata nulli
				|| (eda.getTotaleDaConvalidareSpesaNotNull().signum() == 0 && eda.getTotaleDaConvalidareEntrataNotNull().signum() == 0);
	}
	
}
