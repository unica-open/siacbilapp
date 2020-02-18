/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.incasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.GenericEmissioneOrdinativiAction;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Classe di action per l'emissione di ordinativi di incasso, gestione della quota.
 * 
 * @author Antonino
 * @version 1.0.0 - 11/12/2018
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericEmissioneOrdinativiAction.SESSION_MODEL_NAME_INCASSO)
public class EmissioneOrdinativiIncassoProvvisorioCassaAction extends EmissioneOrdinativiIncassoAction {  
	
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
		//Provvisorio Di Cassa
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
		//popolaStrutturaAmministrativoContabile();
		// Indicatore della presenza di almeno un criterio di ricerca
		boolean formValido = 
				validazioneListProvvisorioDiCassa() 
				;
		
		checkCondition(formValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}

	/**
	 * Validazione Provvisorio Di Cassa
	 * 
	 * @return <code>true</code> in caso la validazione per gli altri dati sia partita; <code>false</code> in caso contrario
	 */
	private boolean validazioneListProvvisorioDiCassa() {

		List<ProvvisorioDiCassa> listProvvisorioDiCassa  = model.getListProvvisorioDiCassa();
		List<ProvvisorioDiCassa> listProvvisorioDiCassaNew  = new ArrayList<ProvvisorioDiCassa>();
		boolean ris = false;
		if(listProvvisorioDiCassa != null && !listProvvisorioDiCassa.isEmpty()) {
			ris = true;			
			for(ProvvisorioDiCassa pdc : listProvvisorioDiCassa) {
//			for(Iterator<ProvvisorioDiCassa> it = listProvvisorioDiCassa.iterator(); it.hasNext();) {
//				ProvvisorioDiCassa pdc = it.next();
				String[] value =  pdc.getCausale().split("\\|");				
				pdc.setUid(Integer.parseInt(value[0]));
				pdc.setAnno(Integer.parseInt(value[1]));
				pdc.setNumero(Integer.parseInt(value[2]));
				pdc.setImporto(new BigDecimal(value[3]));
				pdc.setImportoDaRegolarizzare(new BigDecimal(value[4]));
				/*
					BigDecimal val1BD = pdc.getImporto()                != null ?  pdc.getImporto() : new BigDecimal(0);
					BigDecimal val2BD = pdc.getImportoDaRegolarizzare() != null ?  pdc.getImportoDaRegolarizzare() : new BigDecimal(0);
					pdc.setImportoRegolarizzato(val1BD.subtract(val2BD));
				 */

				
				ris = pdc.getUid() != 0 && ris;
				listProvvisorioDiCassaNew.add(pdc);
			}
		}
		model.setListProvvisorioDiCassa(listProvvisorioDiCassaNew);
		return ris;
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
			//caricaListaprovvisoriDiCassa(model.getListProvvisorioDiCassa());
			caricaListaContoTesoreria();
			caricaListaBollo();
			caricaDataScadenza();
			caricaListaClassificatoreStipendi();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento delle liste: " + wsife.getMessage());
		}
		return SUCCESS;
	}

	//TODO da fare
	/*
	private void caricaListaprovvisoriDiCassa(List<ProvvisorioDiCassa> listProvvisorioDiCassa) {
		List<ProvvisorioDiCassa> listaXSessione = new 	ArrayList<ProvvisorioDiCassa>();	
		
		sessionHandler.setParametro(BilSessionParameter.LISTA_PROVVISORI_CASSA_SELEZIONATI,listaXSessione);
	}
*/
	/**
	 * Preparazione per il metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		model.setDistinta(null);
		model.setContoTesoreria(null);
		model.setListSubdocumenti(null);
		model.setNota(null);
		model.setCodiceBollo(null);
		model.setListProvvisorioDiCassaSelezionati(null);
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
