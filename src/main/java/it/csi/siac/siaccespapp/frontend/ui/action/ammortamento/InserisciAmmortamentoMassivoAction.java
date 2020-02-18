/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ammortamento;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccespapp.frontend.ui.model.ammortamento.InserisciAmmortamentoMassivoModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.model.ClassePiano;

/**
 * The Class InserisciAmmortamentoMassivoAction.
 * @author elisa
 * @version 1.0.0 - 21-09-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciAmmortamentoMassivoAction extends GenericBilancioAction<InserisciAmmortamentoMassivoModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = -6380695904774057316L;
	
	@Autowired private transient CespiteService cespiteService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ClassificazioneCespiteService classificazioneCespiteService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaTipoBene(false);
		caricaListaTitoli();
		caricaCodifiche();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		model.setCespite(null);
		model.setAbilitaPulsantiAmmortamento(true);
		return SUCCESS;
	}
	
	private void caricaTipoBene(boolean excludeAnnullati) {
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
		List<TipoBeneCespite> listaTipoBeneCespite = excludeAnnullati ? filtraTipiBeneValidi(res.getListaTipoBeneCespite()) : res.getListaTipoBeneCespite();
		model.setListaTipoBeneCespite(listaTipoBeneCespite);
	}
	
	/**
	 * Filtra tipi bene validi.
	 *
	 * @param listaTipoBeneDaFiltrare the lista tipo bene cespite
	 * @return the list
	 */
	private List<TipoBeneCespite> filtraTipiBeneValidi(ListaPaginata<TipoBeneCespite> listaTipoBeneDaFiltrare) {
		List<TipoBeneCespite> filtrati = new ArrayList<TipoBeneCespite>();
		for (TipoBeneCespite tipoBeneCespite : listaTipoBeneDaFiltrare) {
			if(!Boolean.TRUE.equals(tipoBeneCespite.getAnnullato())) {
				filtrati.add(tipoBeneCespite);
			}
		}
		return filtrati;
	}
		
	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateRicercaCespitiDaAmmortare() {
		checkCondition(model.getUltimoAnnoAmmortamento() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ultimo anno di ammortamento"));
	}
	
	/**
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String ricercaCespitiDaAmmortare(){
		final String methodName = "effettuaRicerca";
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
		impostaParametriInSessione(req, res);
		
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaCespite request, RicercaSinteticaCespiteResponse response) {
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CESPITE, response.getListaCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
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
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		return response;
	}
	
	/**
	 * Caricamento delle codifiche
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void caricaCodifiche() throws WebServiceInvocationFailureException {
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
	
		if(listaClassePiano == null) {
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
	 * Validate effettua ammortamento selezionati.
	 */
	public void validateEffettuaAmmortamentoSelezionati() {
		checkCondition(model.getListaIdCespiti() != null && !model.getListaIdCespiti().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cespiti da ammortare."));
	}
	
	/**
	 * Effettua ammortamento selezionati.
	 *
	 * @return the string
	 */
	public String effettuaAmmortamentoSelezionati() {
		InserisciAmmortamentoMassivoCespite req = model.creaRequestInserisciAmmortamentoMassivoCespiteSelezionati();
		
		// Devo modificare l'azione richiesta (JIRA SIAC-1944)
		AzioneRichiesta azioneRichiesta = AzioniConsentite.AMMORTAMENTO_MASSIVO_INSERISCI.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		// Invoco il servizio asincrono
		AsyncServiceResponse response = cespiteService.inserisciAmmortamentoMassivoCespiteAsync(wrapRequestToAsync(req, azioneRichiesta));
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Ammortamento massivo cespiti", ""));
		model.setAbilitaPulsantiAmmortamento(false);
		return SUCCESS;
	}
	
	
	/**
	 * Validate effettua ammortamento selezionati.
	 */
	public void validateEffettuaAmmortamentoTutti() {
		RicercaSinteticaCespite requestRicerca = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE, RicercaSinteticaCespite.class);
		checkNotNull(requestRicerca, "parametri ricerca cespiti con piano ammortamento mancante");
	}
	
	
	/**
	 * Effettua ammortamento tutti.
	 *
	 * @return the string
	 */
	public String effettuaAmmortamentoTutti() {
		RicercaSinteticaCespite requestRicerca = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE, RicercaSinteticaCespite.class);
		InserisciAmmortamentoMassivoCespite req = model.creaRequestInserisciAmmortamentoMassivoCespiteTutti(requestRicerca);
		
		// Devo modificare l'azione richiesta (JIRA SIAC-1944)
		AzioneRichiesta azioneRichiesta = AzioniConsentite.AMMORTAMENTO_MASSIVO_INSERISCI.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		// Invoco il servizio asincrono
		AsyncServiceResponse response = cespiteService.inserisciAmmortamentoMassivoCespiteAsync(wrapRequestToAsync(req, azioneRichiesta));
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Ammortamento massivo cespiti", ""));
		model.setAbilitaPulsantiAmmortamento(false);
		return SUCCESS;
	}
	
}
