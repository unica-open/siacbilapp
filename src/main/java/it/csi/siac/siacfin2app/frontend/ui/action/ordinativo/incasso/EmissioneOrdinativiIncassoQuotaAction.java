/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.incasso;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.GenericEmissioneOrdinativiAction;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di action per l'emissione di ordinativi di incasso, gestione della quota.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericEmissioneOrdinativiAction.SESSION_MODEL_NAME_INCASSO)
public class EmissioneOrdinativiIncassoQuotaAction extends EmissioneOrdinativiIncassoAction {  
	
	/** Per la serializazzione */
	private static final long serialVersionUID = 8373777286033962625L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	public void prepareCompleteStep1() {
		// Svuoto i campi
		// Atto amministrativo
		model.setAttoAmministrativo(null);
		model.setTipoAtto(null);
		model.setStrutturaAmministrativoContabile(null);		
		// Elenco
		model.setElencoDocumentiAllegato(null);
		model.setNumeroElencoDa(null);
		model.setNumeroElencoA(null);		
		// Capitolo
		model.setCapitolo(null);		
		// Soggetto
		model.setSoggetto(null);
		// Provvisorio Di Cassa
		model.setListProvvisorioDiCassa(null);		
	}
	
	@Override
	public String completeStep1() {
		final String methodName = "completeStep1";
		RicercaQuoteDaEmettereEntrata request = model.creaRequestRicercaQuoteDaEmettereEntrata();
		logServiceRequest(request);
		RicercaQuoteDaEmettereEntrataResponse response = documentoEntrataService.ricercaQuoteDaEmettereEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Creazione parametri di ricerca
		model.creaParametriRicerca();
		log.debug(methodName, "Parametri di ricerca per le quote: " + model.getParametriRicerca());
		
		// Annullo la distinta
		model.setDistinta(null);
		
		// Impostazione dei totali
		model.setNumeroQuote(response.getTotaleElementi());
		model.setTotaleQuote(response.getTotaleImporti());
		
		// Imposto i parametri in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_EMETTERE_ENTRATA, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_EMETTERE_ENTRATA, response.getListaSubdocumenti());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		checkCondition(model.getTipoEmissioneOrdinativo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo emissione"), true);
		// SIAC-5133: popolo la SAC
		popolaStrutturaAmministrativoContabile();
		// Indicatore della presenza di almeno un criterio di ricerca
		boolean formValido = 
				   validazioneAttoAmministrativo()
				|| validazioneElenco()
				|| validazioneCapitolo()
				|| validazioneSoggetto()
				|| validazioneAltriDati()
				;
		
		checkCondition(formValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}

	/**
	 * Validazione per l'atto amministrativo.
	 * 
	 * @return <code>true</code> in caso la validazione per l'atto sia partita; <code>false</code> in caso contrario
	 */
	private boolean validazioneAttoAmministrativo() {
		final String methodName = "validazioneAttoAmministrativo";
		AttoAmministrativo attoAmministrativo = model.getAttoAmministrativo();
		TipoAtto tipoAtto = model.getTipoAtto();
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = model.getStrutturaAmministrativoContabile();
		boolean presenzaAttoAmministrativo = (attoAmministrativo != null && (attoAmministrativo.getAnno() != 0 || attoAmministrativo.getNumero() != 0))
				|| (tipoAtto != null && tipoAtto.getUid() != 0)
				|| (strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0);
		if(presenzaAttoAmministrativo) {
			// Controllo sull'atto amministrativo
			try {
				checkAttoAmministrativo();
				// Effettuo ricerca provvedimento
			} catch(ParamValidationException pve) {
				// Ignoro l'errore
				log.debug(methodName, "Errore di validazione: " + pve.getMessage());
			}
		}
		return presenzaAttoAmministrativo;
	}
	
	/**
	 * Validazione per l'elenco.
	 * 
	 * @return <code>true</code> in caso la validazione per l'elenco sia partita; <code>false</code> in caso contrario
	 */
	private boolean validazioneElenco() {
		final String methodName = "validazioneElenco";
		ElencoDocumentiAllegato elencoDocumentiAllegato = model.getElencoDocumentiAllegato();
		boolean presenzaElenco = (elencoDocumentiAllegato != null && elencoDocumentiAllegato.getAnno() != null)
				|| model.getNumeroElencoDa() != null
				|| model.getNumeroElencoA() != null;
		if(presenzaElenco) {
			try {
				checkElenco();
			} catch(ParamValidationException pve) {
				// Ignoro l'errore
				log.debug(methodName, "Errore di validazione: " + pve.getMessage());
			}
		}
		return presenzaElenco;
	}
	
	/**
	 * Validazione per il capitolo.
	 * 
	 * @return <code>true</code> in caso la validazione per il capitolo sia partita; <code>false</code> in caso contrario
	 */
	private boolean validazioneCapitolo() {
		final String methodName = "validazioneCapitolo";
		CapitoloEntrataGestione capitoloEntrataGestione = model.getCapitolo();
		boolean presenzaCapitolo = capitoloEntrataGestione != null
				&& (capitoloEntrataGestione.getNumeroCapitolo() != null || capitoloEntrataGestione.getNumeroArticolo() != null);
		if(presenzaCapitolo) {
			try {
				checkCapitolo(capitoloEntrataGestione);
			} catch(ParamValidationException pve) {
				// Ignoro l'errore
				log.debug(methodName, "Errore di validazione: " + pve.getMessage());
			}
		}
		return presenzaCapitolo;
	}
	
	/**
	 * Validazione per il soggetto.
	 * 
	 * @return <code>true</code> in caso la validazione per il soggetto sia partita; <code>false</code> in caso contrario
	 */
	private boolean validazioneSoggetto() {
		final String methodName = "validazioneSoggetto";
		Soggetto s = model.getSoggetto();
		boolean presenzaSoggetto = s != null && StringUtils.isNotBlank(s.getCodiceSoggetto());
		if(presenzaSoggetto && s.getUid() != 0) {
			try {
				checkSoggetto();
			} catch(ParamValidationException pve) {
				// Ignoro l'errore
				log.debug(methodName, "Errore di validazione: " + pve.getMessage());
			}
		}
		return presenzaSoggetto;
	}
	
	/**
	 * Validazione per gli altri dati
	 * 
	 * @return <code>true</code> in caso la validazione per gli altri dati sia partita; <code>false</code> in caso contrario
	 */
	private boolean validazioneAltriDati() {
		//distinta criterio ricerca
		boolean presenzaAltriDati = model.getDistintaDaCercare() != null && model.getDistintaDaCercare().getUid() != 0;
		if(presenzaAltriDati) {
			checkDistinta();
		}
		return presenzaAltriDati;
	}

	/**
	 * Ingresso nello step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = GenericEmissioneOrdinativiAction.ANCHOR_VALUE_INCASSO, name = "Emissione Ordinativi Incasso Quota STEP2")
	public String step2() {
		final String methodName = "step2";
		// Caricamento lista conto tesoreria
		try {
			caricaListaContoTesoreria();
			caricaListaBollo();
			caricaDataScadenza();
			caricaListaClassificatoreStipendi();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento delle liste: " + wsife.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		model.setDistinta(null);
		model.setContoTesoreria(null);
		model.setListSubdocumenti(null);
		model.setNota(null);
		model.setCodiceBollo(null);

	}
	
	/**
	 * Completamento dello step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		EmetteOrdinativiDiIncassoDaElenco request = model.creaRequestEmetteOrdinativiDiIncassoDaElencoByQuota();
		logServiceRequest(request);
		AsyncServiceResponse response = emissioneOrdinativiService.emetteOrdinativiDiIncassoDaElenco(wrapRequestToAsync(request));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione asincrona attivata con uid " + response.getIdOperazioneAsincrona());
		model.setIdOperazioneAsincrona(response.getIdOperazioneAsincrona());
		model.popolaIdsSubdocElaborati();
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Emissione ordinativi di incasso", ""));
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkCondition(model.getListSubdocumenti() != null && !model.getListSubdocumenti().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quota"));
		checkDataScadenza("data scadenza", "Nessuna Data");
	}
	
}
