/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.registrazionemovfin;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RicercaRegistrazioneMovFinBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgsaapp.frontend.ui.model.registrazionemovfin.RicercaRegistrazioneMassivaMovFinGSAModel;

/**
 * Ricerca della registrazione massiva MovFin per l'ambito GSA.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/11/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class RicercaRegistrazioneMassivaMovFinGSAAction extends RicercaRegistrazioneMovFinBaseAction<RicercaRegistrazioneMassivaMovFinGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8308061045648954000L;

	@Override
	@BreadCrumb(MODEL_TITOLO)
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}

	@Override
	public void validateRicercaRegistrazioneMovFin() {
		checkNotNullNorInvalidUid(model.getTipoEvento(), "Tipo Evento");
		checkCondition((model.getAnnoMovimento() == null && StringUtils.isBlank(model.getNumeroMovimento()))
			|| (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero movimento", ": i campi devono essere entrambi valorizzati o entrambi non valorizzati"));
		checkCondition(model.getNumeroSubmovimento() == null || model.getNumeroSubmovimento() == 0 || (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero submovimento", ": valorizzare anche anno e numero movimento"));

		checkCondition(checkAttoAmministrativo(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());

		checkCapitolo();
		checkSoggetto();

		if (hasErrori()) {
			model.impostoDatiNelModel();
		}
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRicerca() {
		return BilSessionParameter.RIEPILOGO_RICERCA_REGISTRAZIONE_MASSIVA_GSA;
	}
	
	@Override
	protected void caricaListe() {
		caricaListaTipiEventoDaSessione();
		caricaListaStati();
	}
	
	@Override
	protected void preCreazioneRequestRicercaSintetica() {
		// Forzo lo stato a notificato
		if(model.getRegistrazioneMovFin() == null) {
			model.setRegistrazioneMovFin(new RegistrazioneMovFin());
		}
		model.getRegistrazioneMovFin().setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.NOTIFICATO);
	}
}
