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
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiaveResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoAction extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1048555718689347461L;

	@Override
	public String caricaListaAccantonamentoFondi() {
		final String methodName = "caricaListaAccantonamentoFondi";
		RicercaSinteticaFondiDubbiaEsigibilitaRendiconto request = model.creaRequestRicercaSinteticaFondiDubbiaEsigibilitaRendiconto();
		logServiceRequest(request);
		
		RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse response = fondiDubbiaEsigibilitaService.ricercaSinteticaFondiDubbiaEsigibilitaRendiconto(request);
		logServiceResponse(response);
	
		if (response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Imposto in sessione la request.");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO, request);
		log.debug(methodName, "Imposto in sessione i risultati.");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO, response.getAccantonamentiFondiDubbiaEsigibilitaRendiconto());
		
		return SUCCESS;
	}

	@Override
	public String confermaCapitoli() {
		List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaTemp = model.getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp();
	
		for (CapitoloEntrataGestione c : model.getListaCapitoloEntrataGestione()) {
			addCapitoloIfNotPresent(c, new AccantonamentoFondiDubbiaEsigibilitaRendiconto(), listaAccantonamentoFondiDubbiaEsigibilitaTemp);
		}
		
		model.setListaCapitoloEntrataGestione(new ArrayList<CapitoloEntrataGestione>());
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #salvaCapitoli()}
	 */
	public void prepareSalvaCapitoli() {
		model.setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati(null);
	}
	
	@Override
	public String salvaCapitoli() {
		final String methodName = "salvaCapitoli";
	
		InserisceFondiDubbiaEsigibilitaRendiconto request = model.creaRequestInserisceFondiDubbiaEsigibilitaRendiconto();
		logServiceRequest(request);
	
		InserisceFondiDubbiaEsigibilitaRendicontoResponse response = fondiDubbiaEsigibilitaService.inserisceFondiDubbiaEsigibilitaRendiconto(request);
		logServiceResponse(response);
	
		if (response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio.");
			return INPUT;
		}
		
		model.rimuoviCapitoliDaTemp();
		
		// SIAC-5304
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo<br/>Per la stampa del \"Report di controllo - Simulazione FCDE\" - Menu 7 - Report di Utilit&agrave; "));
	
		return SUCCESS;
	}
	
	@Override
	public void validateSalvaCapitoli() {
		List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaNelModel = model.getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati();
		checkCondition(!listaNelModel.isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("fondi a dubbia e difficile esazione"));
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamento : listaNelModel){
			checkNotNullNorInvalidUid(accantonamento.getCapitolo(), "capitolo entrata gestione");
			String keyCapitolo = model.isGestioneUEB() ? accantonamento.getCapitolo().getAnnoNumeroArticoloUEB() : accantonamento.getCapitolo().getAnnoNumeroArticolo();
			int anno = model.getAttributiBilancio().getUltimoAnnoApprovato().intValue();
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi(), "percentuale accantonamento fondi anno " + anno + " per capitolo: " + keyCapitolo);
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi1(), "percentuale accantonamento fondi anno " + (anno - 1) + " per capitolo: " + keyCapitolo);
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi2(), "percentuale accantonamento fondi anno " + (anno - 2) + " per capitolo: " + keyCapitolo);
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi3(), "percentuale accantonamento fondi anno " + (anno - 3) + " per capitolo: " + keyCapitolo);
			checkNotNull(accantonamento.getPercentualeAccantonamentoFondi4(), "percentuale accantonamento fondi anno " + (anno - 4) + " per capitolo: " + keyCapitolo);
		}
	}

	@Override
	public String eliminaAccantonamento() {
		final String methodName = "eliminaAccantonamento";
	
		EliminaFondoDubbiaEsigibilitaRendiconto request = model.creaRequestEliminaFondoDubbiaEsigibilitaRendiconto();
		logServiceRequest(request);
	
		EliminaFondoDubbiaEsigibilitaRendicontoResponse response = fondiDubbiaEsigibilitaService.eliminaFondoDubbiaEsigibilitaRendiconto(request);
		logServiceResponse(response);
	
		if (response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		return SUCCESS;
	}

	@Override
	public String aggiornaAccantonamento() {
		final String methodName = "aggiornaAccantonamento";
	
		AggiornaFondoDubbiaEsigibilitaRendiconto request = model.creaRequestAggiornaFondoDubbiaEsigibilitaRendiconto();
		logServiceRequest(request);
	
		AggiornaFondoDubbiaEsigibilitaRendicontoResponse response = fondiDubbiaEsigibilitaService.aggiornaFondoDubbiaEsigibilitaRendiconto(request);
		logServiceResponse(response);
	
		if (response.hasErrori()) {
			addErrori(response);
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
		
		ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente req = model.creaRequestControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente();
		ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse res = fondiDubbiaEsigibilitaService.controllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		Long numeroFondi = res.getNumeroFondiAnnoCorrente();
		log.debug(methodName, "Numero fondi anno precedente: " + numeroFondi);
		model.setDatiAnnoPrecedentePresenti(numeroFondi != null && numeroFondi.longValue() > 0L);
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
		
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente req = model.creaRequestPopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente();
		AsyncServiceResponse res = fondiDubbiaEsigibilitaService.popolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteAsync(wrapRequestToAsync(req, model.getAzioneRichiesta()));
		
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
	
	// SIAC-5304
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
	
}
