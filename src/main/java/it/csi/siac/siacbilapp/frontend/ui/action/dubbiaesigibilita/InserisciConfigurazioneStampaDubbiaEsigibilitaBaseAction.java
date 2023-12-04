/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.dubbiaesigibilita;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.CookieProvider;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.frontend.webservice.AzioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.InserisceAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipologiaEstrazioniFogliDiCalcolo;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * Classe base di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * @author Marchino Alessandro
 * @param <M> la tipizzazione del model
 */
public abstract class InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<M extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel> extends GenericBilancioAction<M> implements CookieProvider {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5365271862455391015L;
	// Servizi
	/** Servizio per i fondi a dubbia esigibilit&agrave; */
	@Autowired protected transient FondiDubbiaEsigibilitaService fondiDubbiaEsigibilitaService;
	/** Servizio per l'azione */
	@Autowired protected transient AzioneService azioneService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;

	@Override
	public void prepare() {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		final String methodName = "execute";
		try {
			// FIXME: decommentare quando sara' possibile verificare con fasi di bilancio
			checkCasoDUsoApplicabile();
			caricaListaTitoloEntrata();
			caricaAttributiBilancio();
			
			// se abbiamo almeno una versione ne cerchiamo i dati
			if (model.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio() != null) {
				caricaAttributiPerCopia();
				caricaListaAccantonamentoFondi();
			} else {
				// se non abbiamo almeno una versione per la tipologia ricercata ne creiamo una di default su cui far 
				// lavorare l'utente
				initAttributiBilancio();
			}

			// SIAC-5481
			executeOtherLoading();
		} catch(WebServiceInvocationFailureException wsife){
			log.info(methodName, wsife.getMessage());
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = checkCompatibilitaFaseBilancio(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}

	/**
	 * Controllo della compatibilit&agrave; della fase di bilancio
	 * @param faseBilancio
	 * @return
	 */
	protected abstract boolean checkCompatibilitaFaseBilancio(FaseBilancio faseBilancio);

	/**
	 * Esecuzione di ulteriori caricamenti
	 * @throws WebServiceInvocationFailureException in caso di fallimento nei caricamenti
	 */
	protected void executeOtherLoading() throws WebServiceInvocationFailureException {
		// To be implemented a necessary
	}

	public abstract boolean isConsentitoTornaInBozza();
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Metodo chiamato al load della pagina
	 * @return il risultato dell'invocazione
	 */
	public String enterPage() {
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		return SUCCESS;
	}

	/**
	 * Salva gli attributi del bilancio
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String salvaAttributiBilancio() {
		// Aggiornamento
		AggiornaAttributiBilancio req = model.creaRequestAggiornaAttributiBilancio();
		AggiornaAttributiBilancioResponse response = bilancioService.aggiornaAttributiBilancio(req);
	
		if (response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Validazione del metodo {@link #salvaAttributiBilancio()}
	 */
	public void validateSalvaAttributiBilancio() {
		checkNotNull(model.getAttributiBilancio().getAccantonamentoGraduale(), "Accantonamento enti locali");
		
		checkNotNull(model.getAttributiBilancio().getPercentualeAccantonamentoAnno(), String.format("Percentuale accantonamento anno %d", model.getAnnoEsercizioInt()));
		checkNotNull(model.getAttributiBilancio().getPercentualeAccantonamentoAnno1(), String.format("Percentuale accantonamento anno %d", Integer.valueOf(model.getAnnoEsercizioInt().intValue() + 1)));
		checkNotNull(model.getAttributiBilancio().getPercentualeAccantonamentoAnno2(), String.format("Percentuale accantonamento anno %d", Integer.valueOf(model.getAnnoEsercizioInt().intValue() + 2)));
	
		checkNotNull(model.getAttributiBilancio().getRiscossioneVirtuosa(), "Riscossione virtuosa");
		checkNotNull(model.getAttributiBilancio().getMediaApplicata(), "Tipo media prescelta");
		checkNotNull(model.getAttributiBilancio().getUltimoAnnoApprovato(), "Ultimo bilancio approvato");
	}

	/**
	 * Aggiorna gli attributi di bilancio
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaAttributiBilancio() {
		return salvaAttributiBilancio();
	}
	
	
	/**
	 * Validazione del metodo {@link #aggiornaAttributiBilancio()}
	 */
	public void validateAggiornaAttributiBilancio() {
		validateSalvaAttributiBilancio();
	}

	/**
	 * Caricamento degli attributi del bilancio
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione dei servizi
	 */
	private void caricaAttributiBilancio() throws WebServiceInvocationFailureException {
		RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestRicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);

		RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.ricercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, res));
		}
		
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
	}
	
	/**
	 * Caricamento degli attributi di bilancio per la copia dei dati da interfaccia
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione dei servizi
	 */
	protected void caricaAttributiPerCopia() throws WebServiceInvocationFailureException {
		RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestRicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);
		
		RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.ricercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
		
		if (res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, res));
		}
		
		model.setListaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDistinctVersion(res.getListaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
	}
	
	/**
	 * Metodo che consente di ottenere la lista paginata degli accantonamenti presenti su database
	 * @return il risultato dell'invocazione
	 */
	public abstract String caricaListaAccantonamentoFondi();

	/**
	 * Metodo che consente di ottenere una versione di default per gli fcde
	 * @throws WebServiceInvocationFailureException 
	 */
	public void initAttributiBilancio() throws WebServiceInvocationFailureException {
		final String methodName = "initAttributiBilancio";
		ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestInizializzaioneAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);
		
		ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.initDefaultDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
		
		if (res.hasErrori()) {
			addErrori(res);
			log.debug(methodName, "ERRORI DURANTE l'INIZIALIZZAIONE DEGLI ATTRIBUTI FCDE DI DEFAULT");
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, res));
		}
		
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
	}

	/**
	 * Caricamento delle liste di entrata.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTitoloEntrata() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoListeEntrata";
		List<TitoloEntrata> list = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
	
		if (list == null) {
			// Carico da servizio
			log.debug(methodName, "Necessario ottenere le liste da servizio");
			LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
			//SIAC-8397 per FCDE sono amessi i titoli dall'1 al 5
			list = filtraListaTitoliEntrata(response.getClassificatoriTitoloEntrata());
	
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, list);
		}
	
		model.setListaTitoloEntrata(list);
	}
	
	/**
	 * SIAC-8397 per FCDE sono amessi i titoli dall'1 al 5
	 */
	private List<TitoloEntrata> filtraListaTitoliEntrata(List<TitoloEntrata> list) {
		List<TitoloEntrata> listaFiltrati = new ArrayList<TitoloEntrata>();

		if(list != null) {
			for (TitoloEntrata titoloEntrata : list) {
				if(StringUtils.isNotBlank(titoloEntrata.getCodice()) && new Integer(titoloEntrata.getCodice()) <= 5) {
					listaFiltrati.add(titoloEntrata);
				}
			}
		}
		
		return listaFiltrati;
	}

	/**
	 * Ottiene la response per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice rispetto cui ottenere la response
	 * 
	 * @return la response ottenuta
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice.getConstant());
		logServiceRequest(req);
		
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		
		if (res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(LeggiClassificatoriByTipoElementoBil.class, res));
		}
		
		return res;
	}

	/**
	 * Metodo che imposta nel model la lista di accantonamenti selezionati dall'utente.
	 * @return il risultato dell'invocazione
	 */
	public abstract String confermaCapitoli();
	
	/**
	 * Metodo per la persistenza dell'accantonamento tramite il servizio {@link InserisceAccantonamentoFondiDubbiaEsigibilita}
	 * @return il risultato dell'invocazione
	 */
	public abstract String salvaCapitoli();
	
	/**
	 * Eliminazione dell'accantonamento
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String eliminaAccantonamento();

	/**
	 * Ripristino dell'accantonamento
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String ripristinaAccantonamento();

	/**
	 * Permette di ottenere i dati salvati nel model.
	 * @return il risultato dell'invocazione
	 */
	public String leggiListaTemp(){
		return SUCCESS;
	}
	
	public void prepareSelezionaVersione() {
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
	}
	
	public void resetAccantonamentoGradualePerNuovaVersione() {
		model.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().setAccantonamentoGraduale(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
	}
	
	public String selezionaVersione() {
		final String methodName = "selezionaVersione";
		RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestRicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(model.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa());
		logServiceRequest(req);
		RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.ricercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
		if(res.hasErrori()) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			addErrori(res);
			return INPUT;
		}
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio());
		model.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(null);
		try {
			caricaAttributiPerCopia();
			caricaListaAccantonamentoFondi();
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio: " + wsife.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}
	
	public void validateSelezionaVersione() {
		checkNotNullNorInvalidUid(model.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa(), "attributi bilancio");
	}
	
	/**
	 * Aggiunge il capitolo solo se non presente nella lista
	 * @param <C> la tipizzazione del capitolo
	 * @param <A> la tipizzazione della lista
	 * @param capitolo il capitolo
	 * @param template il template dell'accantonamento
	 * @param lista la lista da popolare
	 */
	protected <C extends Capitolo<?, ?>, A extends AccantonamentoFondiDubbiaEsigibilitaBase<C>> void addCapitoloIfNotPresent(C capitolo, A template, List<A> lista) {
		if(capitolo == null) {
			return;
		}
		for(A accantonamento : lista) {
			C cap = accantonamento.ottieniCapitolo();
			if(cap != null && cap.getUid() == capitolo.getUid()) {
				// Capitolo gia' presente
				return;
			}
		}
		
		template.impostaCapitolo(capitolo);
		lista.add(template);
	}
	
	/**
	 * Metodo per la creazione di un cookie per la gestione degli eventi DOM
	 * sugli iframe sui progetti basati su Chromium.
	 * @param res 
	 * 
	 * @return javax.servlet.http.Cookie
	 */
	protected void impostaDatiStampa(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse res, TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		impostaDatiStampa(res, tipo.getDescrizione());
		
	}

	/**
	 * Metodo per la creazione di un cookie per la gestione degli eventi DOM
	 * sugli iframe sui progetti basati su Chromium.
	 * @param res 
	 * 
	 * @return javax.servlet.http.Cookie
	 */
	private void impostaDatiStampa(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse res, String appendToFileName) {
		Cookie cookie = createPrintCookie();
		
		Set<Cookie> cookieSet = new HashSet<Cookie>();
		cookieSet.add(cookie);
		model.setCookies(cookieSet);
		
		model.setContentType(res.getContentType() == null ? null : res.getContentType().getMimeType()); 
		model.setContentLength(Long.valueOf(res.getReport().length));
		model.setFileName("FondoCreditiDubbiaEsigibilita." + appendToFileName + "." + res.getExtension());
		
		InputStream inputStream = new ByteArrayInputStream(res.getReport());
		model.setInputStream(inputStream);
		
	}
	
	//SIAC-7858 CM 04/06/2021 Inizio
	public String modificaStatoInDefinitivo() {
		return modificaStato(StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA);
	}
	
	public String modificaStatoInBozza() {
		return modificaStato(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA);
	}
	//SIAC-7858 CM 04/06/2021 Fine

	private String modificaStato(StatoAccantonamentoFondiDubbiaEsigibilita stato) {
		final String methodName = "modificaStato";
		
		ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio req = model.creaRequestModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(stato);
		logServiceRequest(req);
		
		ModificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse res = fondiDubbiaEsigibilitaService.modificaStatoAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(req);
		logServiceResponse(res);
	
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio di modifica stato in " + stato);
			return INPUT;
		}
		
		// Aggiorno lo stato delle elaborazioni nella select
		try {
			caricaAttributiPerCopia();
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio: " + wsife.getMessage());
			return INPUT;
		}
		
		// Aggiorno lo stato dell'accantonamento
		model.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio()
			.setStatoAccantonamentoFondiDubbiaEsigibilita(
				res.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio()
					.getStatoAccantonamentoFondiDubbiaEsigibilita());
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'estrazione del folgio di calcolo tramie servizio
	 * @return il risultato dell'invocazione
	 */
	public abstract String estraiInFoglioDiCalcolo();
	
	//SIAC-7858 CM 21/06/2021 Inizio
	protected String estraiInFoglioDiCalcolo(TipoAccantonamentoFondiDubbiaEsigibilita tipo) {
		final String methodName = "estraiInFoglioDiCalcolo";
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport req = model.creaRequestStampaExcelAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio();
		logServiceRequest(req);
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse res = fondiDubbiaEsigibilitaService.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		impostaDatiStampa(res, tipo);
		
		return SUCCESS;
	}
	
	protected String estraiInFoglioDiCalcoloSecondari(TipologiaEstrazioniFogliDiCalcolo tipologiaEstrazione) {
		final String methodName = "estraiInFoglioDiCalcolo";
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport req = model.creaRequestStampaExcelAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(tipologiaEstrazione);
		logServiceRequest(req);
		
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse res = fondiDubbiaEsigibilitaService.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "Si sono verificati errori nell'invocazione del servizio");
			return INPUT;
		}
		
		impostaDatiStampa(res, tipologiaEstrazione.getTipoAccantonamento().getDescrizione() + "-" + tipologiaEstrazione.getDescrizione());

		return SUCCESS;
	}
	//SIAC-7858 CM 21/06/2021 Fine

	@Override
	public Set<Cookie> getCookies() {
		return model.getCookies();
	}
}
