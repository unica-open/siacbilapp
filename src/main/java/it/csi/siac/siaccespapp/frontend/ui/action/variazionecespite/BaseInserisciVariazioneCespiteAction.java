/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.BaseInserisciVariazioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Inserimento della variazione di un cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciVariazioneCespiteAction<M extends BaseInserisciVariazioneCespiteModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8392759793663948564L;
	
	@Autowired private CespiteService cespiteService;
	@Autowired private ClassificazioneCespiteService classificazioneCespiteService;
	@Autowired private ClassificatoreBilService classificatoreBilService;
	@Autowired private CodificheService codificheService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile();
		caricaTipoBene();
		caricaListaClassificazioneGiuridica();
		caricaListaTitoli();
		caricaListaClassePiano();
		return SUCCESS;
	}
	
	private void caricaListaClassePiano() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
	
		if(listaEvento == null || listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche("ClassePiano" + "_" + Ambito.AMBITO_FIN.getSuffix());
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN, listaClassePiano);
		}
			
		model.setListaClassi(listaClassePiano);
		
	}
	
	/**
	 * Caricamento del tipo bene
	 */
	private void caricaTipoBene() {
		final String methodName = "caricaTipoBene";
		RicercaSinteticaTipoBeneCespite req = model.creaRequestRicercaSinteticaTipoBene();
		RicercaSinteticaTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			model.setListaTipoBeneCespite(new ArrayList<TipoBeneCespite>());
			return;
		}
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun tipo bene cespite");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			model.setListaTipoBeneCespite(new ArrayList<TipoBeneCespite>());
			return;
		}
		model.setListaTipoBeneCespite(res.getListaTipoBeneCespite());
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	private void caricaListaTitoli() {
		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		List<TitoloEntrata> listaTE = response.getClassificatoriTitoloEntrata();
		
		if(!listaTE.isEmpty()){
			model.setListaTitoloEntrata(listaTE);
		}else{
			model.setListaTitoloEntrata(new ArrayList<TitoloEntrata> ());
		}
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		
		if(!listaTS.isEmpty()){
			model.setListaTitoloSpesa(listaTS);
		}else{
			model.setListaTitoloSpesa(new ArrayList<TitoloSpesa> ());
		}
	}
	
	/**
	 * Ottiene la response per il servizio di lettura classificatori per tipo elemento bilancio
	 * @param codice il codice dell'elemento di bilancio
	 * @return la response
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(response);
		return response;
	}
	
	/**
	 * Caricamento della classificazione giuridica
	 */
	private void caricaListaClassificazioneGiuridica() {
		model.setListaClassificazioneGiuridicaCespite(Arrays.asList(ClassificazioneGiuridicaCespite.values()));
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Step 1
	 * @return sempre SUCCESS
	 */
	public String step1() {
		return SUCCESS;
	}
	
	
	
	/**
	 * Preparazione per il metodo {@link #enterStep2()}.
	 * Pulizia dei campi
	 */
	public void prepareEnterStep2() {
		model.setCespite(null);
		model.setFlagSoggettoTutelaBeniCulturali(null);
		model.setFlgDonazioneRinvenimento(null);
	}
	
	/**
	 * Ingresso nello step2
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep2() {
		final String methodName = "enterStep2";
		RicercaSinteticaCespite req = model.creaRequestRicercaSinteticaCespite();
		RicercaSinteticaCespiteResponse res = cespiteService.ricercaSinteticaCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			return INPUT;
		}
		
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CESPITE, res.getListaCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #enterStep2()}
	 */
	public void validateEnterStep2() {
		Cespite cespite = model.getCespite();
		if(cespite == null) {
			cespite = new Cespite();
			model.setCespite(cespite);
		}
		
		boolean indicatoUnCriterioDiRicerca =
			StringUtils.isNotBlank(cespite.getCodice())
			|| StringUtils.isNotBlank(cespite.getDescrizione())
			|| idEntitaPresente(cespite.getTipoBeneCespite())
			|| (cespite.getTipoBeneCespite() != null && cespite.getTipoBeneCespite().getContoPatrimoniale() != null && StringUtils.isNotBlank(cespite.getTipoBeneCespite().getContoPatrimoniale().getCodice()))
			|| cespite.getClassificazioneGiuridicaCespite() != null
			|| StringUtils.isNotBlank(model.getFlagSoggettoTutelaBeniCulturali())
			|| StringUtils.isNotBlank(model.getFlgDonazioneRinvenimento())
			|| StringUtils.isNotBlank(cespite.getNumeroInventario())
			|| cespite.getDataAccessoInventario() != null;

		checkCondition(indicatoUnCriterioDiRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
	/**
	 * Step 2
	 * @return sempre SUCCESS
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Torna allo step 1
	 * @return sempre SUCCESS
	 */
	public String backToStep1() {
		model.setCespite(null);
		model.setFlagSoggettoTutelaBeniCulturali(null);
		model.setFlgDonazioneRinvenimento(null);
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #enterStep3()}
	 */
	public void prepareEnterStep3() {
		model.setCespite(null);
	}
	
	
	/**
	 * Prepare enter step 3 directly.
	 *
	 * @throws Exception the exception
	 */
	public void prepareEnterStep3Directly() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Enter step 3 directly.
	 *
	 * @return the string
	 */
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String enterStep3Directly() {
		model.impostaCespiteDaUid();
		model.setBackUrl("backToRicerca");
		return enterStep3();
	}
	
	/**
	 * Back to ricerca.
	 *
	 * @return the string
	 */
	public String backToRicerca() {
		model.setCespite(null);
		model.setFlagSoggettoTutelaBeniCulturali(null);
		model.setFlgDonazioneRinvenimento(null);
		return SUCCESS;
	}
	
	/**
	 * Ingresso nello step 3
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3() {
		final String methodName = "enterStep3";
		try {
			// Caricamento cespite
			loadCespite();
			// Caricamento lista variazioni
			loadVariazioniCespite();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		return SUCCESS;
	}

	/**
	 * Caricamento del cespite
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void loadCespite() throws WebServiceInvocationFailureException {
		RicercaDettaglioCespite req = model.creaRequestRicercaDettaglioCespite();
		RicercaDettaglioCespiteResponse res = cespiteService.ricercaDettaglioCespite(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		model.setCespite(res.getCespite());
	}
	
	/**
	 * Caricamento delle variazioni del cespite
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void loadVariazioniCespite() throws WebServiceInvocationFailureException {
		RicercaSinteticaVariazioneCespite req = model.creaRequestRicercaSinteticaVariazioneCespite();
		RicercaSinteticaVariazioneCespiteResponse res = cespiteService.ricercaSinteticaVariazioneCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		// Imposto i dati in sessione
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONE_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_VARIAZIONE_CESPITE, res.getListaVariazioneCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
	
	/**
	 * Validazione per il metodo {@link #enterStep3()}.
	 */
	public void validateEnterStep3() {
		checkNotNullNorInvalidUid(model.getCespite(), "cespite");
	}
	
	/**
	 * Step 3
	 * @return sempre SUCCESS
	 */
	public String step3() {
		return SUCCESS;
	}
	
	/**
	 * Torna allo step 2
	 * @return sempre SUCCESS
	 */
	public String backToStep2() {
		model.setCespite(null);
		return SUCCESS;
	}

	/**
	 * Preparazione per il metodo {@link #inserisciVariazione()}
	 */
	public void prepareInserisciVariazione() {
		model.setVariazioneCespite(null);
	}
	/**
	 * Inserimento della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciVariazione() {
		final String methodName = "inserisciVariazione";
		InserisciVariazioneCespite req = model.creaRequestInserisciVariazioneCespite();
		InserisciVariazioneCespiteResponse res = cespiteService.inserisciVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.info(methodName, "Inserimento effettuato");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccesso();
		model.setVariazioneCespite(res.getVariazioneCespite());
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #inserisciVariazione()}
	 */
	public void validateInserisciVariazione() {
		checkNotNull(model.getVariazioneCespite(), "Variazione", true);
		
		checkNotNullNorEmpty(model.getVariazioneCespite().getAnnoVariazione(), "Anno");
		checkNotNull(model.getVariazioneCespite().getDataVariazione(), "Data inserimento");
		checkNotNullNorEmpty(model.getVariazioneCespite().getDescrizione(), "Descrizione");
		checkNotNull(model.getVariazioneCespite().getImporto(), "Importo");
		
		checkCondition(model.getVariazioneCespite().getImporto() == null || model.getVariazioneCespite().getImporto().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo", "deve essere positivo"));
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaVariazione()}
	 */
	public void prepareAggiornaVariazione() {
		model.setVariazioneCespite(null);
	}
	/**
	 * Aggiornamento della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaVariazione() {
		final String methodName = "aggiornaVariazione";
		AggiornaVariazioneCespite req = model.creaRequestAggiornaVariazioneCespite();
		AggiornaVariazioneCespiteResponse res = cespiteService.aggiornaVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.info(methodName, "Aggiornamento effettuato");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccesso();
		model.setVariazioneCespite(res.getVariazioneCespite());
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #aggiornaVariazione()}
	 */
	public void validateAggiornaVariazione() {
		checkNotNullNorInvalidUid(model.getVariazioneCespite(), "Variazione", true);
		
		checkNotNullNorEmpty(model.getVariazioneCespite().getAnnoVariazione(), "Anno");
		checkNotNull(model.getVariazioneCespite().getDataVariazione(), "Data inserimento");
		checkNotNullNorEmpty(model.getVariazioneCespite().getDescrizione(), "Descrizione");
		checkNotNull(model.getVariazioneCespite().getImporto(), "Importo");
		
		checkCondition(model.getVariazioneCespite().getImporto() == null || model.getVariazioneCespite().getImporto().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo", "deve essere positivo"));
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaVariazione()}
	 */
	public void prepareEliminaVariazione() {
		model.setVariazioneCespite(null);
	}
	/**
	 * Eliminazione della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaVariazione() {
		final String methodName = "eliminaVariazione";
		EliminaVariazioneCespite req = model.creaRequestEliminaVariazioneCespite();
		EliminaVariazioneCespiteResponse res = cespiteService.eliminaVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.info(methodName, "Eliminazione effettuata");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		model.setVariazioneCespite(res.getVariazioneCespite());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #eliminaVariazione()}
	 */
	public void validateEliminaVariazione() {
		checkNotNull(model.getVariazioneCespite(), "Variazione");
	}
	
	/**
	 * Preparazione per il metodo {@link #caricaVariazione()}
	 */
	public void prepareCaricaVariazione() {
		model.setVariazioneCespite(null);
	}
	/**
	 * Caricamento dei dati della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaVariazione() {
		final String methodName = "caricaVariazione";
		RicercaDettaglioVariazioneCespite req = model.creaRequestRicercaDettaglioVariazioneCespite();
		RicercaDettaglioVariazioneCespiteResponse res = cespiteService.ricercaDettaglioVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		model.setVariazioneCespite(res.getVariazioneCespite());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #caricaVariazione()}
	 */
	public void validateCaricaVariazione() {
		checkNotNull(model.getVariazioneCespite(), "Variazione");
	}
	
	/**
	 * Result JSON per la variazione del cespite
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 10/08/2018
	 *
	 */
	public static class VariazioneCespiteJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = -4596708620841614068L;

		/** Costruttore vuoto di dfault */
		public VariazioneCespiteJSONResult() {
			setIncludeProperties("errori.*,messaggi.*,informazioni.*,variazioneCespite.*");
		}
	}
}
