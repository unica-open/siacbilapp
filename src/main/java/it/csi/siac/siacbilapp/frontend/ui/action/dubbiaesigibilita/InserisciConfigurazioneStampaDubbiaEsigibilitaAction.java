/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.dubbiaesigibilita;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiaveResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * 
 * @author Alessio Romanato
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/10/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciConfigurazioneStampaDubbiaEsigibilitaAction extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<InserisciConfigurazioneStampaDubbiaEsigibilitaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1048555718689347461L;
	
	@Override
	protected void executeOtherLoading() throws WebServiceInvocationFailureException {
		RicercaAzionePerChiave req = model.creaRequestRicercaAzionePerChiave(AzioniConsentite.REPORTISTICA_PREVISIONE.getNomeAzione());
		RicercaAzionePerChiaveResponse res = azioneService.ricercaAzionePerChiave(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		model.setAzioneReportistica(res.getAzione());
		model.setGruppoAzioniReportistica(res.getGruppoAzioni());
	}

	@Override
	public String caricaListaAccantonamentoFondi() {
		final String methodName = "caricaListaAccantonamentoFondi";
		RicercaSinteticaFondiDubbiaEsigibilita req = model.creaRequestRicercaSinteticaFondiDubbiaEsigibilita();
		logServiceRequest(req);
		
		RicercaSinteticaFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.ricercaSinteticaFondiDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Imposto in sessione la request.");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA, req);
		log.debug(methodName, "Imposto in sessione i risultati.");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA, res.getAccantonamentiFondiDubbiaEsigibilita());
		
		return SUCCESS;
	}

	@Override
	public String confermaCapitoli() {
		List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp = model.getListaAccantonamentoFondiDubbiaEsigibilitaTemp();
	
		for (CapitoloEntrataPrevisione c : model.getListaCapitoloEntrataPrevisione()) {
			addCapitoloIfNotPresent(c, new AccantonamentoFondiDubbiaEsigibilita(), listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		}
		
		model.setListaCapitoloEntrataPrevisione(new ArrayList<CapitoloEntrataPrevisione>());
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #salvaCapitoli()}
	 */
	public void prepareSalvaCapitoli() {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaSelezionati(null);
	}
	
	@Override
	public String salvaCapitoli() {
		final String methodName = "salvaCapitoli";
	
		InserisceFondiDubbiaEsigibilita req = model.creaRequestInserisceFondiDubbiaEsigibilita();
		logServiceRequest(req);
	
		InserisceFondiDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.inserisceFondiDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio.");
			return INPUT;
		}
		
		model.rimuoviCapitoliDaTemp();
		
		// SIAC-5481: aggiunta del messaggio di successo
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo<br/>Per la stampa del \"Report di controllo - Simulazione FCDE\" - Menu "
				+ model.getGruppoAzioniReportistica().getTitolo() + ", azione " + model.getAzioneReportistica().getTitolo()));
	
		return SUCCESS;
	}
	
	@Override
	public void validateSalvaCapitoli() {
		List<AccantonamentoFondiDubbiaEsigibilita> listaNelModel = model.getListaAccantonamentoFondiDubbiaEsigibilitaSelezionati();
		checkCondition(!listaNelModel.isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("fondi a dubbia e difficile esazione"));
		for(AccantonamentoFondiDubbiaEsigibilita accantonamento : listaNelModel){
			checkNotNullNorInvalidUid(accantonamento.getCapitolo(), "capitolo entrata previsione");
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi(), "percentuale accantonamento fondi per capitolo con uid: " + accantonamento.getCapitolo().getUid());
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi1(), "percentuale accantonamento fondi per capitolo con uid: " + accantonamento.getCapitolo().getUid());
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi2(), "percentuale accantonamento fondi per capitolo con uid: " + accantonamento.getCapitolo().getUid());
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi3(), "percentuale accantonamento fondi per capitolo con uid: " + accantonamento.getCapitolo().getUid());
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi4(), "percentuale accantonamento fondi per capitolo con uid: " + accantonamento.getCapitolo().getUid());
			checkNotNull(accantonamento.getPercentualeDelta(), "percentuale delta per capitolo con uid: " + accantonamento.getCapitolo().getUid());
		}
	}
	

	@Override
	public String eliminaAccantonamento() {
		final String methodName = "eliminaAccantonamento";
	
		EliminaFondoDubbiaEsigibilita req = model.creaRequestEliminaFondoDubbiaEsigibilita();
		logServiceRequest(req);
	
		EliminaFondoDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.eliminaFondoDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		return SUCCESS;
	}

	@Override
	public String aggiornaAccantonamento() {
		final String methodName = "aggiornaAccantonamento";
	
		AggiornaFondoDubbiaEsigibilita req = model.creaRequestAggiornaFondoDubbiaEsigibilita();
		logServiceRequest(req);
	
		AggiornaFondoDubbiaEsigibilitaResponse res = fondiDubbiaEsigibilitaService.aggiornaFondoDubbiaEsigibilita(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return SUCCESS;
		}
		
		return SUCCESS;
	}

	@Override
	protected void caricaDatiAnnoPrecedente() throws WebServiceInvocationFailureException {
		final String methodName = "caricaDatiAnnoPrecedente";
		if(!model.isAttributiBilancioPresenti()) {
			log.debug(methodName, "Gli attributi non sono presenti: non permetto il caricamento");
			model.setDatiAnnoPrecedentePresenti(false);
			return;
		}
		
		ControllaFondiDubbiaEsigibilitaAnnoPrecedente req = model.creaRequestControllaFondiDubbiaEsigibilitaAnnoPrecedente();
		ControllaFondiDubbiaEsigibilitaAnnoPrecedenteResponse res = fondiDubbiaEsigibilitaService.controllaFondiDubbiaEsigibilitaAnnoPrecedente(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		Long numeroFondi = res.getNumeroFondiAnnoPrecedente();
		Long numeroFondiGestione = res.getNumeroFondiGestioneAnnoPrecedente();
		log.debug(methodName, "Numero fondi anno precedente: " + numeroFondi + ", numero fondi di gestione: " + numeroFondiGestione);
		model.setDatiAnnoPrecedentePresenti(numeroFondi != null && numeroFondi.longValue() > 0L);
		model.setDatiAnnoPrecedenteGestionePresenti(numeroFondiGestione != null && numeroFondiGestione.longValue() > 0L);
	}

	/**
	 * Preparazione per il metodo {@link #popolaDaAnnoPrecedente()}
	 */
	public void preparePopolaDaAnnoPrecedente() {
		model.setIdOperazioneAsincrona(null);
	}

	@Override
	public String popolaDaAnnoPrecedente() {
		final String methodName = "popolaDaAnnoPrecedente";
		
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente req = model.creaRequestPopolaFondiDubbiaEsigibilitaDaAnnoPrecedente();
		AsyncServiceResponse res = fondiDubbiaEsigibilitaService.popolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsync(wrapRequestToAsync(req, model.getAzioneRichiesta()));
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		log.debug(methodName, "Inizializzata operazione asincrona con id " + res.getIdOperazioneAsincrona());
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());
		
		return SUCCESS;
	}
	
	/**
	 * Popolamento dei fondi a partire dai capitoli di gestione dell'anno precedente
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String popolaDaAnnoPrecedenteGestione() {
		final String methodName = "popolaDaAnnoPrecedenteGestione";
		
		PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente req = model.creaRequestPopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente();
		AsyncServiceResponse res = fondiDubbiaEsigibilitaService.popolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteAsync(wrapRequestToAsync(req, model.getAzioneRichiesta()));
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		log.debug(methodName, "Inizializzata operazione asincrona con id " + res.getIdOperazioneAsincrona());
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());
		
		return SUCCESS;
	}
	
	/**
	 * Annullamento dei dati in pagina
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaSelezionati(null);
		model.setListaAccantonamentoFondiDubbiaEsigibilitaTemp(null);
		return SUCCESS;
	}
}
