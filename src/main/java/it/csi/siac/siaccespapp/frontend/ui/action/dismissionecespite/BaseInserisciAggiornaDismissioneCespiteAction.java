/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.dismissionecespite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.BaseInserisciAggiornaDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class InserisciTipoBeneAction.
 *
 * @param <M> the la classe di model
 */
public class BaseInserisciAggiornaDismissioneCespiteAction<M extends BaseInserisciAggiornaDismissioneCespiteModel> extends GenericDismissioneCespiteAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1450211848540040317L;

	@Autowired private transient ClassificazioneCespiteService  classificazioneCespiteService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
		
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// In ingresso della pagina, pulisco il model e richiamo il prepare originale
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Enter inserisci anagrafica.
	 *
	 * @return the string
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	public String enterStep1Anagrafica() throws WebServiceInvocationFailureException {
		caricaListe();
		return SUCCESS;
	}
	
	/**
	 * Enter step 2 collega cespite.
	 *
	 * @return the string
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	public String enterCollegaCespite() throws WebServiceInvocationFailureException {
		caricaTipoBene();
		caricaCespitiCollegati();
		return SUCCESS;
	}
	
	
	/**
	 * Carica cespiti collegati.
	 */
	private void caricaCespitiCollegati(){
		final String methodName = "caricaCespitiCollegati";
		RicercaSinteticaCespite req = model.creaRequestRicercaSinteticaCespite();
		RicercaSinteticaCespiteResponse res = cespiteService.ricercaSinteticaCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			return;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(req, res);
		
	}
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaCespite request, RicercaSinteticaCespiteResponse response) {
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE_DA_DISMISSIONE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CESPITE_DA_DISMISSIONE, response.getListaCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}

	/**
	 * Validate inserisci anagrafica.
	 */
	public void validateDismissione() {
		DismissioneCespite dismissioneCespite = model.getDismissioneCespite();
		checkNotNull(dismissioneCespite, "dismissione cespite");
		checkNotNullNorEmpty(dismissioneCespite.getDescrizione(), "descrizione");
		checkAttoAmministrativo();
		checkCondition(idEntitaPresente(dismissioneCespite.getCausaleEP()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("causale dismissione"));
		checkNotNull(dismissioneCespite.getDataCessazione(), "data cessazione");
	}
	
	/**
	 * Carica tipo bene.
	 */
	private void caricaTipoBene() {
		final String methodName = "caricaTipoBene";
		RicercaSinteticaTipoBeneCespite request = model.creaRequestRicercaSinteticaTipoBene();
		RicercaSinteticaTipoBeneCespiteResponse response = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(request);
		if(response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			model.setListaTipoBeneCespite(new ArrayList<TipoBeneCespite>());
			return;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun tipo bene cespite");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			model.setListaTipoBeneCespite(new ArrayList<TipoBeneCespite>());
		}
		List<TipoBeneCespite> listaTipoBeneCespite = filtraTipiBeneValidi(response.getListaTipoBeneCespite());
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
	 * Back to step 1.
	 *
	 * @return the string
	 */
	public String backToStep1() {
		return SUCCESS;
	}
	
	/**
	 * Validate collega cespite.
	 */
	public void validateCollegaCespite() {
		checkNotNullNorInvalidUid(model.getDismissioneCespite(), "dismissione");
		checkCondition(model.getUidsCespitiDaCollegare() != null && !model.getUidsCespitiDaCollegare().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cespiti da collegare"));
	}
	
	/**
	 * Collega cespite.
	 *
	 * @return the string
	 */
	public String collegaCespite() {
		CollegaCespiteDismissioneCespiteResponse res = chiamaServizioCollegaCespiti();
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}

	/**
	 * Invocazione del servizio do collegamente cespiti
	 * @return la response del servizio
	 */
	protected CollegaCespiteDismissioneCespiteResponse chiamaServizioCollegaCespiti() {
		CollegaCespiteDismissioneCespite req = model.creaRequestCollegaCespiteDismissioneCespite();
		CollegaCespiteDismissioneCespiteResponse res = cespiteService.collegaCespiteDismissioneCespite(req);
		return res;
	}
	
	/**
	 * Validate collega cespite.
	 */
	public void validateScollegaCespite() {
		checkNotNullNorInvalidUid(model.getDismissioneCespite(), "dismissione");
		checkCondition(model.getUidCespiteDaScollegare() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cespiti da collegare"));
	}
	
	/**
	 * Collega cespite.
	 *
	 * @return the string
	 */
	public String scollegaCespite() {
		ScollegaCespiteDismissioneCespite req = model.creaRequestScollegaCespiteDismissioneCespite();
		ScollegaCespiteDismissioneCespiteResponse res = cespiteService.scollegaCespiteDismissioneCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validate effettua scritture.
	 */
	public void validateEffettuaScritture() {
		checkNotNullNorInvalidUid(model.getDismissioneCespite(), "dismissione");
	}
	
	/**
	 * Effettua scritture.
	 *
	 * @return the string
	 */
	public String effettuaScritture() {
		
		InserisciPrimeNoteDismissioneCespite req = model.creaRequestEffettuaScrittureDismissioneCespite();
		InserisciPrimeNoteDismissioneCespiteResponse res = cespiteService.inserisciPrimeNoteDismissioneCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
}
