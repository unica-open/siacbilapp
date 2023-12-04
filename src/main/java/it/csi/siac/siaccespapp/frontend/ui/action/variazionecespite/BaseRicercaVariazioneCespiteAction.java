/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.BaseRicercaVariazioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseRicercaVariazioneCespiteAction<M extends BaseRicercaVariazioneCespiteModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1605712222479788525L;
	
	@Autowired private ClassificazioneCespiteService classificazioneCespiteService;
	@Autowired private CespiteService cespiteService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaTipoBene();
		caricaListaClassificazioneGiuridica();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;
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
	 * Caricamento della classificazione giuridica
	 */
	private void caricaListaClassificazioneGiuridica() {
		model.setListaClassificazioneGiuridicaCespite(Arrays.asList(ClassificazioneGiuridicaCespite.values()));
	}

	/**
	 * Ricerca della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		RicercaSinteticaVariazioneCespite req = model.creaRequestRicercaSinteticaVariazioneCespite();
		RicercaSinteticaVariazioneCespiteResponse res = cespiteService.ricercaSinteticaVariazioneCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaVariazioneCespite.class, res));
			addErrori(res);
			return INPUT;
		}
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		// Ricerca effettuata con successo: imposto i dati in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONE_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_VARIAZIONE_CESPITE, res.getListaVariazioneCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}
	 */
	public void validateEffettuaRicerca() {
		Cespite cespite = model.getCespite();
		if(cespite == null) {
			cespite = new Cespite();
			model.setCespite(cespite);
		}
		VariazioneCespite variazioneCespite = model.getVariazioneCespite();
		if(variazioneCespite == null) {
			variazioneCespite = new VariazioneCespite();
			model.setVariazioneCespite(variazioneCespite);
		}
		
		boolean indicatoUnCriterioDiRicerca =
			// Variazione
			StringUtils.isNotBlank(variazioneCespite.getAnnoVariazione())
			|| variazioneCespite.getDataVariazione() != null
			|| StringUtils.isNotBlank(variazioneCespite.getDescrizione())
			// Cespite
			|| StringUtils.isNotBlank(cespite.getCodice())
			|| StringUtils.isNotBlank(cespite.getDescrizione())
			|| idEntitaPresente(cespite.getTipoBeneCespite())
			|| cespite.getClassificazioneGiuridicaCespite() != null
			|| StringUtils.isNotBlank(model.getFlagSoggettoTutelaBeniCulturali())
			|| StringUtils.isNotBlank(model.getFlgDonazioneRinvenimento())
			|| StringUtils.isNotBlank(cespite.getNumeroInventario())
			|| cespite.getDataAccessoInventario() != null;

		checkCondition(indicatoUnCriterioDiRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
}
