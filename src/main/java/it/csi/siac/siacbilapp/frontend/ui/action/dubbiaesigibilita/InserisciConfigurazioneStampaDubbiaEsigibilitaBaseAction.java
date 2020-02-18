/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.dubbiaesigibilita;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.AzioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancioResponse;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

/**
 * Classe base di action per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * @author Marchino Alessandro
 * @param <M> la tipizzazione del model
 */
public abstract class InserisciConfigurazioneStampaDubbiaEsigibilitaBaseAction<M extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel> extends GenericBilancioAction<M> {

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
			caricaListaTitoloEntrata();
			caricaAttributiBilancio();
			// SIAC-4469
			caricaDatiAnnoPrecedente();
			// SIAC-5481
			executeOtherLoading();
		} catch(WebServiceInvocationFailureException wsife){
			log.info(methodName, wsife.getMessage());
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
		return SUCCESS;
	}

	/**
	 * Esecuzione di ulteriori caricamenti
	 * @throws WebServiceInvocationFailureException in caso di fallimento nei caricamenti
	 */
	protected void executeOtherLoading() throws WebServiceInvocationFailureException {
		// To be implemented a necessary
	}

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
		AggiornaAttributiBilancio request = model.creaRequestAggiornaAttributiBilancio();
		AggiornaAttributiBilancioResponse response = bilancioService.aggiornaAttributiBilancio(request);
	
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
		RicercaAttributiBilancio request = model.creaRequestRicercaAttributiBilancio();
		logServiceRequest(request);
		
		RicercaAttributiBilancioResponse response = bilancioService.ricercaAttributiBilancio(request);
		logServiceResponse(response);
	
		if (response.hasErrori()) {
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
	
		AttributiBilancio attributiBilancio = response.getAttributiBilancio();
		model.handleAttributiBilancio(attributiBilancio);
	}
	
	/**
	 * Carica i dati dall'anno precedente e imposta i flag corrispondenti se presenti
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected abstract void caricaDatiAnnoPrecedente() throws WebServiceInvocationFailureException;

	/**
	 * Metodo che consente di ottenere la lista paginata degli accantonamenti presenti su database
	 * @return il risultato dell'invocazione
	 */
	public abstract String caricaListaAccantonamentoFondi();

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
			list = response.getClassificatoriTitoloEntrata();
	
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, list);
		}
	
		model.setListaTitoloEntrata(list);
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
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice.getConstant());
		logServiceRequest(request);
		
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		
		if (response.hasErrori()) {
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		return response;
	}

	/**
	 * Metodo che imposta nel model la lista di accantonamenti selezionati dall'utente.
	 * @return il risultato dell'invocazione
	 */
	public abstract String confermaCapitoli();
	
	/**
	 * Metodo per la persistenza dell'accantonamento tramite il servizio {@link InserisceFondiDubbiaEsigibilita}
	 * @return il risultato dell'invocazione
	 */
	public abstract String salvaCapitoli();
	
	/**
	 * Validazione del metodo {@link #salvaCapitoli()}
	 */
	public abstract void validateSalvaCapitoli();

	/**
	 * Eliminazione dell'accantonamento
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String eliminaAccantonamento();

	/**
	 * Metodo che permette l'aggiornamento dei dati dell'accantonamento
	 * @return il risultato dell'invocazione
	 */
	public abstract String aggiornaAccantonamento();
	
	/**
	 * Popolamento dei fondi a partire dagli equivalenti nell'anno precedente
	 * @return il risultato dell'invocazione
	 */
	public abstract String popolaDaAnnoPrecedente();
	
	/**
	 * Permette di ottenere i dati salvati nel model.
	 * @return il risultato dell'invocazione
	 */
	public String leggiListaTemp(){
		return SUCCESS;
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
}
