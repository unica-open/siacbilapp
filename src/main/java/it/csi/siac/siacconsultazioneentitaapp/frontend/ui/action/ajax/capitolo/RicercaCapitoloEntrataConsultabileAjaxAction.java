/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax.RicercaEntitaConsultabileBaseAjaxAction;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo.RicercaCapitoloEntrataConsultabileAjaxModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.ConsultazioneEntitaService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per la ricerca del Capitolo Spesa Consultabile (cruscottino)
 * come entita di partenza
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 02/03/2016
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCapitoloEntrataConsultabileAjaxAction extends RicercaEntitaConsultabileBaseAjaxAction<RicercaCapitoloEntrataConsultabileAjaxModel, RicercaSinteticaEntitaConsultabile, RicercaSinteticaEntitaConsultabileResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -3945550016033757499L;
	@Autowired
	private transient ConsultazioneEntitaService consultazioneEntitaService;

	/** Costruttore vuoto di default */
	public RicercaCapitoloEntrataConsultabileAjaxAction() {
		super();
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaEntitaConsultabile request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaEntitaConsultabile request,ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaEntitaConsultabileResponse ottieniResponse(RicercaSinteticaEntitaConsultabile request) {
		return consultazioneEntitaService.ricercaSinteticaEntitaConsultabile(request);
	}

	@Override
	protected ListaPaginata<EntitaConsultabile> ottieniListaRisultati(RicercaSinteticaEntitaConsultabileResponse response) {
		return response.getEntitaConsultabili();
	}

	@Override
	protected RicercaSinteticaEntitaConsultabile creaRequest() {
		return model.creaRequestRicercaSinteticaEntitaConsultabile();
	}
	
}
