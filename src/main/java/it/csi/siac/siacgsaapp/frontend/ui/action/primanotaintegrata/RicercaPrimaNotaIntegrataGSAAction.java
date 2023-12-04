/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RicercaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.RicercaPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per la ricerca della prima nota libera. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPrimaNotaIntegrataGSAAction extends RicercaPrimaNotaIntegrataBaseAction<RicercaPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8172807815409126474L;

	@Override
	protected void caricaListe() {
		final String methodName = "caricaListe";
		caricaListaTipoEventoDaSessione();
		caricaListaEvento();
		caricaListaStatoOperativoRegistrazioneMovFin();
		caricaListaClassiDaSessione();
		try {
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
			// SIAC-5292
			// Carico la lista dei tipi finanziamento
			caricaListaTipoFinanziamento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// TODO: fare qualcosa?
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Aggiungo il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListaClassePiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GSA;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRiepilogo() {
		return BilSessionParameter.RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_GSA;
	}
	
	@Override
	protected boolean checkUlterioriCampi() {
		return checkCampoValorizzato(model.getPrimaNota().getClassificatoreGSA(), "classificatore");
	}
	
}
