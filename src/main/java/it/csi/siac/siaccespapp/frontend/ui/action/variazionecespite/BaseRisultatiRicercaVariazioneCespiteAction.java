/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.BaseRisultatiRicercaVariazioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespiteResponse;

/**
 * Classe base per i risultati di ricerca della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/08/2018
 *
 * @param <M> la tipizzazione del model
 */
public abstract class BaseRisultatiRicercaVariazioneCespiteAction<M extends BaseRisultatiRicercaVariazioneCespiteModel>
		extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2366639824070775272L;
	@Autowired private CespiteService cespiteService;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// TODO: leggere la pagina iniziale
		return SUCCESS;
	}
	
	/**
	 * Eliminazione della variazione del cespite
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "eliminaVariazione";
		EliminaVariazioneCespite req = model.creaRequestEliminaVariazioneCespite();
		EliminaVariazioneCespiteResponse res = cespiteService.eliminaVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(EliminaVariazioneCespite.class, res));
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
	 * Consultazione della variazione del cespite
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		return SUCCESS;
	}
	
	/**
	 * Consultazione della variazione del cespite
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiornaVariazione";
		AggiornaVariazioneCespite req = model.creaRequestAggiornaVariazioneCespite();
		AggiornaVariazioneCespiteResponse res = cespiteService.aggiornaVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(AggiornaVariazioneCespite.class, res));
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
	 * Aggiornamento della variazione del cespite
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaVariazione() {
		final String methodName ="aggiorna";
		RicercaDettaglioVariazioneCespite req = model.creaRequestRicercaDettaglioVariazioneCespite();
		RicercaDettaglioVariazioneCespiteResponse res = cespiteService.ricercaDettaglioVariazioneCespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(RicercaDettaglioVariazioneCespite.class, res));
			addErrori(res);
			return INPUT;
		}
		model.setVariazioneCespite(res.getVariazioneCespite());
		return SUCCESS;
	}
}
