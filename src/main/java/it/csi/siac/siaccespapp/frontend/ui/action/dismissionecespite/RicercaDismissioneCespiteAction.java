/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.dismissionecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.RicercaDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespiteResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaDismissioneCespiteAction.
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDismissioneCespiteAction extends GenericDismissioneCespiteAction<RicercaDismissioneCespiteModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = 3520467190714760466L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// SIAC-6423: caricate le liste nella prepare per evitare problemi in fase di ricalcolo della pagina
		caricaListe();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setDismissioneCespite(null);
		return SUCCESS;
	}
	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateEffettuaRicerca() {
		AttoAmministrativo attoAmministrativo = model.getAttoAmministrativo();
		if(attoAmministrativo != null && (attoAmministrativo.getUid() != 0 || attoAmministrativo.getAnno() != 0)) {
			checkAttoAmministrativo();
		}
	}
	
	/**
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String effettuaRicerca(){
		final String methodName = "effettuaRicerca";
		RicercaSinteticaDismissioneCespite req = model.creaRequestRicercaSinteticaDismissioneCespite();
		RicercaSinteticaDismissioneCespiteResponse res = cespiteService.ricercaSinteticaDismissioneCespite(req);
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
	private void impostaParametriInSessione(RicercaSinteticaDismissioneCespite request, RicercaSinteticaDismissioneCespiteResponse response) {
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DISMISSIONE_CESPITE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DISMISSIONE_CESPITE, response.getListaDismissioneCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
}
