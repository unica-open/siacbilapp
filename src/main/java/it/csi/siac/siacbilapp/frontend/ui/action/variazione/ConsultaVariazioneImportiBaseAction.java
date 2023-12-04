/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.io.ByteArrayInputStream;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.ConsultaVariazioneImportiModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReportResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione della consultazione della Variazione
 * Importi.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 28/02/2017
 *
 */

public abstract class ConsultaVariazioneImportiBaseAction
		extends GenericBilancioAction<ConsultaVariazioneImportiModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;

	@Autowired
	protected transient VariazioneDiBilancioService variazioneDiBilancioService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";

		/* Effettuo la ricerca di dettaglio del Capitolo */
		log.debug(methodName, "Creazione della request");
		if (model.getUidVariazione() == null) {
			log.debug(methodName, "Errore: non presente uid variazione da consultare");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		log.debug(methodName, "Creo la request per la ricerca dell'anagrafica della variazione");

		RicercaDettaglioAnagraficaVariazioneBilancio request = model.creaRequestRicercaDettaglioAnagraficaVariazioneBilancio();
		logServiceRequest(request);
		//System.out.println(request);

		log.debug(methodName, "Invocazione del servizio di ricerca");

		RicercaDettaglioAnagraficaVariazioneBilancioResponse response = variazioneDiBilancioService.ricercaDettaglioAnagraficaVariazioneBilancio(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAnagraficaVariazioneBilancio.class, response));
			addErrori(response);
			throwExceptionFromErrori(model.getErrori());
		}
		model.impostaDatiDaResponseESessione(response);
		log.debug(methodName, "Dati impostati nel model e nella sessione");

		return SUCCESS;

		
	}

	/**
	 * Metodo per ottenere la lista dei capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliNellaVariazione() {

		final String methodName = "leggiCapitoliNellaVariazione";

		log.debug(methodName, "Richiamo il webService di ricercaDettagliVariazioneImportoCapitoloNellaVariazione");

		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = model
				.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione();
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse response = variazioneDiBilancioService
				.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(request);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class, response));
			addErrori(response);
			return SUCCESS;
		}

		log.debug(methodName,
				"Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE, request);

		log.debug(methodName, "Imposto in sessione la lista");

		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE,
				response.getListaDettaglioVariazioneImportoCapitolo());

		model.setTotaleStanziamentiCassaEntrata(response.getTotaleStanziamentiCassaEntrata());
		model.setTotaleStanziamentiCassaSpesa(response.getTotaleStanziamentiCassaSpesa());
		model.setTotaleStanziamentiEntrata(response.getTotaleStanziamentiEntrata());
		model.setTotaleStanziamentiSpesa(response.getTotaleStanziamentiSpesa());
		model.setTotaleStanziamentiResiduiEntrata(response.getTotaleStanziamentiResiduiEntrata());
		model.setTotaleStanziamentiResiduiSpesa(response.getTotaleStanziamentiResiduiSpesa());
		
		//SIAC-6883
		model.setTotaleStanziamentiEntrata1(response.getTotaleStanziamentiEntrata1());
		model.setTotaleStanziamentiEntrata2(response.getTotaleStanziamentiEntrata2());
		
		model.setTotaleStanziamentiSpesa1(response.getTotaleStanziamentiSpesa1());
		model.setTotaleStanziamentiSpesa2(response.getTotaleStanziamentiSpesa2());

		return SUCCESS;
	}

	// SIAC-5016

	/**
	 * Download dell'excel dei dati della variazione
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String download() {
		final String methodName = "download";

		VariazioneBilancioExcelReport req = model.creaRequestStampaExcelVariazioneDiBilancio();
		VariazioneBilancioExcelReportResponse res = variazioneDiBilancioService.variazioneBilancioExcelReport(req);

		// Controllo gli errori
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, "Errori nel reperimento dell'excel della variazione");
			return INPUT;
		}

		byte[] bytes = res.getReport();
		model.setContentType(res.getContentType() == null ? null : res.getContentType().getMimeType());
		model.setContentLength(Long.valueOf(bytes.length));
		//SIAC-8150 controllo su anno competenza
		model.setFileName("esportazioneVariazione" + (model.getAnnoCompetenza() != null 
				? model.getAnnoCompetenza() : (model.getAnnoEsercizio() != null 
					? model.getAnnoEsercizio() : model.getBilancio().getAnno())) + "_" + model.getNumeroVariazione() + "."
				+ res.getExtension());
		model.setInputStream(new ByteArrayInputStream(bytes));

		return SUCCESS;
	}

	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	// SIAC-5016
	public String effettuaRicercaNellaVariazioneCapUscitaPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model
				.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaPrevisione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}

	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	// SIAC-5016
	public String effettuaRicercaNellaVariazioneCapUscitaGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model
				.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaGestione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}

	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	// SIAC-5016
	public String effettuaRicercaNellaVariazioneCapEntrataPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model
				.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataPrevisione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}

	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	// SIAC-5016
	public String effettuaRicercaNellaVariazioneCapEntrataGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model
				.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataGestione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}

	/**
	 * Effettua ricerca capitolo nella variazione.
	 *
	 * @param req
	 *            la request da inviare
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	// SIAC-5016
	public String effettuaRicercaCapitoloNellaVariazione(
			RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req) {
		final String methodName = "effettuaRicercaCapitoloNellaVariazione";
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse res = variazioneDiBilancioService
				.ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(req);
		// Controllo gli errori
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			log.debug(methodName,
					"Si sono verificati errori durante l'esecuzione del servizio di ricerca singolo dettaglio variazione importo capitolo nella variazione");
			addErrori(res);
			return INPUT;
		}
		ElementoCapitoloVariazione elementoCapitoloVariazioneTrovato = ElementoCapitoloVariazioneFactory
				.getInstanceFromSingoloDettaglio(res.getDettaglioVariazioneImportoCapitolo(), model.isGestioneUEB());
		model.setElementoCapitoloVariazioneTrovatoNellaVariazione(elementoCapitoloVariazioneTrovato);
		return SUCCESS;
	}

}
