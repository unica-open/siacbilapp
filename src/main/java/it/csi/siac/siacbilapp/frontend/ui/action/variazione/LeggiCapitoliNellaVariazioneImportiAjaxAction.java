/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.LeggiCapitoliNellaVariazioneImportiAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per il popolamento della
 * 
 * @author Elisa Chiari
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class LeggiCapitoliNellaVariazioneImportiAjaxAction extends
		GenericRisultatiRicercaAjaxAction<ElementoCapitoloVariazione, LeggiCapitoliNellaVariazioneImportiAjaxModel, DettaglioVariazioneImportoCapitolo, RicercaDettagliVariazioneImportoCapitoloNellaVariazione, RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -1947379243402495508L;
	/** Serviz&icirc; della variazione di bilancio */
	@Autowired
	protected transient VariazioneDiBilancioService variazioneDiBilancioService;

	/** Costruttore vuoto di default */
	public LeggiCapitoliNellaVariazioneImportiAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(
			RicercaDettagliVariazioneImportoCapitoloNellaVariazione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaDettagliVariazioneImportoCapitoloNellaVariazione request,
			ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoCapitoloVariazione ottieniIstanza(DettaglioVariazioneImportoCapitolo e)
			throws FrontEndBusinessException {
		Boolean gestioneUEB = Boolean.valueOf(model.isGestioneUEB());
		ElementoCapitoloVariazione istanza = ElementoCapitoloVariazioneFactory.getInstanceFromSingoloDettaglio(e, gestioneUEB);
		return istanza;
	}

	@Override
	protected RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ottieniResponse(
			RicercaDettagliVariazioneImportoCapitoloNellaVariazione request) {
		return variazioneDiBilancioService.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(request);
	}

	@Override
	protected ListaPaginata<DettaglioVariazioneImportoCapitolo> ottieniListaRisultati(
			RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse response) {
		return response.getListaDettaglioVariazioneImportoCapitolo();
	}

}
