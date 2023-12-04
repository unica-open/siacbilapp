/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.soggetto.helper;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.BaseActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class SoggettoActionHelper extends BaseActionHelper {

	@Autowired protected transient SoggettoService soggettoService;

	public SoggettoActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
	}

	public void findSoggetto(Soggetto soggetto) {
		final String methodName = "checkSoggetto";
		
		if (soggetto == null || soggetto.getUid() > 0) {
			return;
		}
		
		if (StringUtils.isEmpty(soggetto.getCodiceSoggetto())) {
			return;
		}
		
		action.checkCondition(StringUtils.isNotBlank(soggetto.getCodiceSoggetto()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice soggetto"), true);

		RicercaSoggettoPerChiave request = creaRequestRicercaSoggettoPerChiave(soggetto);
		action.logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		action.logServiceResponse(response);
		
		if(response.hasErrori()) {
			log.info(methodName, action.createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			action.addErrori(response);
			action.throwExceptionFromErrori(response.getErrori());
		}
		
		if(response.getSoggetto() == null) {
			log.debug(methodName, "Nessun soggetto disponibile per codice " + request.getParametroSoggettoK().getCodice());
			action.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + request.getParametroSoggettoK().getCodice()));
			return;
		}

		log.debug(methodName, "Soggetto con codice " + soggetto.getCodiceSoggetto() + " trovato con uid " + soggetto.getUid()
				+ ". StatoOperativoAnagrafica: " + soggetto.getStatoOperativo());

		soggetto.setUid(response.getSoggetto().getUid());
	}
	
	public List<CodificaFin> caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> list = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(list == null) {
			ListeGestioneSoggetto request = action.getModel().creaRequestListeGestioneSoggetto();
			action.logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			action.logServiceResponse(response);

			if(response.hasErrori()) {
				action.addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaClasseSoggetto");
			}
			
			list = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(list);
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, list);
		}

		return list;
	}
	
	private RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto soggetto) {
		RicercaSoggettoPerChiave request = action.getModel().creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(action.getModel().getEnte());
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(soggetto.getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}


}
