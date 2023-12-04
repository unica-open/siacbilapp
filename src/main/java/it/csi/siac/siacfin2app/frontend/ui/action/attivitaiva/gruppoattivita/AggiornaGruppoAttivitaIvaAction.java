/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.gruppoattivita;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita.AggiornaGruppoAttivitaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

/**
 * Classe di action per l'aggiornamento del Gruppo Attivita Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaGruppoAttivitaIvaAction extends GenericGruppoAttivitaIvaAction<AggiornaGruppoAttivitaIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoChiusura();
		caricaListaTipoAttivita();
		caricaListaAttivitaIva();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() {
		final String methodName = "execute";
		// Controllo se il CDU sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		
		RicercaDettaglioGruppoAttivitaIva request = model.creaRequestRicercaDettaglioGruppoAttivitaIva();
		logServiceRequest(request);
		RicercaDettaglioGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaDettaglioGruppoAttivitaIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaGruppoAttivitaIva");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricercato gruppo con uid " + model.getUidGruppoAttivitaIva());
		model.impostaDati(response.getGruppoAttivitaIva());
		
		// SIAC-4360
		try {
			checkDatiAnnoInCorso();
		} catch (WebServiceInvocationFailureException wsife) {
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * Se il gruppo non ha dati annualizzati per l'anno in corso, richiedo il gruppo dell'anno precedente
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void checkDatiAnnoInCorso() throws WebServiceInvocationFailureException {
		final String methodName = "checkDatiAnnoInCorso";
		if(model.getGruppoAttivitaIva() != null && !model.getGruppoAttivitaIva().getListaProRataEChiusuraGruppoIva().isEmpty()) {
			return;
		}
		log.debug(methodName, "Gruppo senza dati per l'anno in corso. Prendo i dati del precedente anno...");
		
		RicercaDettaglioGruppoAttivitaIva request = model.creaRequestRicercaDettaglioGruppoAttivitaIvaPrecedente();
		RicercaDettaglioGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaDettaglioGruppoAttivitaIva(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaGruppoAttivitaIva per l'anno di bilancio precedente");
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioGruppoAttivitaIva.class, response));
		}
		
		if(response.getGruppoAttivitaIva() == null || response.getGruppoAttivitaIva().getListaProRataEChiusuraGruppoIva() == null || response.getGruppoAttivitaIva().getListaProRataEChiusuraGruppoIva().isEmpty()) {
			addMessaggio(MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio("Nessun dato annualizzato presente per l'anno di esercizio in corso n&eacute; per l'anno precedente (" + (model.getAnnoEsercizioInt() - 1) + ")"));
			return;
		}
		ProRataEChiusuraGruppoIva oldproRata = response.getGruppoAttivitaIva().getListaProRataEChiusuraGruppoIva().get(0);
		
		ProRataEChiusuraGruppoIva newProRata = new ProRataEChiusuraGruppoIva();
		newProRata.setPercentualeProRata(oldproRata.getPercentualeProRata());
		newProRata.setUltimoMeseDefinito(oldproRata.getUltimoMeseDefinito());
		newProRata.setEnte(oldproRata.getEnte());
		newProRata.setAnnoEsercizio(model.getAnnoEsercizioInt());
		newProRata.setGruppoAttivitaIva(model.getGruppoAttivitaIva());
		newProRata.setIvaPrecedente(oldproRata.getIvaPrecedente());
		
		addMessaggio(MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio("Dati annualizzati copiati dall'anno di esercizio precedente (" + (model.getAnnoEsercizioInt() - 1) + ")"));
		model.setTipoChiusura(response.getGruppoAttivitaIva().getTipoChiusura());
		model.setProRataEChiusuraGruppoIva(newProRata);
	}

	/**
	 * Aggiorna il Gruppo Attivita Iva
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		
		AggiornaGruppoAttivitaIvaEProrata request = model.creaRequestAggiornaGruppoAttivitaIvaEProrata();
		logServiceRequest(request);
		AggiornaGruppoAttivitaIvaEProrataResponse response = gruppoAttivitaIvaService.aggiornaGruppoAttivitaIvaEProrata(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Fornisco l'errore a video ed esco
			log.info(methodName, "Errore nell'invocazione del servizio di AggiornaGruppoAttivitaIvaEProrata");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Aggiornamento Gruppo Attivita Iva andato a buon fine");
		GruppoAttivitaIva gai = response.getGruppoAttivitaIva();
		
		model.impostaDati(gai);
		// Carico il messaggio di successo dell'servizio
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca delle associazioni con le Attivita Iva siano validi.
	 */
	public void validateAggiornamento() {
		checkNotNull(model.getGruppoAttivitaIva(), "Gruppo attivita Iva", true);
		
		GruppoAttivitaIva gai = model.getGruppoAttivitaIva();
		checkNotNullNorEmpty(gai.getCodice(), "Codice");
		checkNotNullNorEmpty(gai.getDescrizione(), "Descrizione");
		
		checkNotNull(model.getTipoAttivita(), "Tipo attivita'");
		checkCondition(!gai.getListaAttivitaIva().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Attivita'"));
		
		// Deve esserci almeno un'attivita compilata
		for(AttivitaIva attivitaIva : gai.getListaAttivitaIva()) {
			checkNotNullNorInvalidUid(attivitaIva, "Attivita'");
		}
		
		checkNotNull(model.getTipoChiusura(), "Tipo chiusura");
		
		// La pro-rata e' obbligatoria
		checkNotNull(model.getProRataEChiusuraGruppoIva(), "% Pro-rata", true);
		ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva = model.getProRataEChiusuraGruppoIva();
		
		checkNotNull(proRataEChiusuraGruppoIva.getPercentualeProRata(), "% Pro-rata");
		
		// L'iva a credito deve essere zero o negativa
		checkCondition(proRataEChiusuraGruppoIva.getIvaPrecedente() == null || proRataEChiusuraGruppoIva.getIvaPrecedente().signum() <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Iva a credito precedente", ": non deve essere positiva"));
	}
	
}
