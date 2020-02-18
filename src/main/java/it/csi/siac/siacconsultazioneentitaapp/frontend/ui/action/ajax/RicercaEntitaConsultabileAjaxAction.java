/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax;

import org.apache.struts2.json.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.RicercaEntitaConsultabileAjaxModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.ConsultazioneEntitaService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per la ricerca del capitolo (cruscottino)
 * @author Elisa Chiari
 * @version 1.0.0 - 23/02/2016
 * 
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaEntitaConsultabileAjaxAction extends RicercaEntitaConsultabileBaseAjaxAction<RicercaEntitaConsultabileAjaxModel, RicercaFigliEntitaConsultabile, RicercaFigliEntitaConsultabileResponse> {

	/**Per la serializzazione */
	private static final long serialVersionUID = -5639414847540854050L;

	@Autowired
	private transient ConsultazioneEntitaService consultazioneEntitaService;
	

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaFigliEntitaConsultabile req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected RicercaFigliEntitaConsultabile creaRequest() { 
		return model.creaRequestRicercaFigliEntitaConsultabili();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaFigliEntitaConsultabile req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaFigliEntitaConsultabileResponse ottieniResponse(RicercaFigliEntitaConsultabile req) {
		return consultazioneEntitaService.ricercaFigliEntitaConsultabile(req);
	}

	@Override
	protected ListaPaginata<EntitaConsultabile> ottieniListaRisultati(RicercaFigliEntitaConsultabileResponse response) {
		return response.getEntitaConsultabili();
	}
	
	@Override
	protected void eseguiOperazioniUlterioriSuResponse(RicercaFigliEntitaConsultabileResponse response) {
		model.setImportoEntitaConsultabili(response.getImportoEntitaConsultabili());
	}
	
	/**
	 * Result per il datatables delle entit&agrave; consultabili
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 28/06/2018
	 *
	 */
	public static class EntitaConsultabileDataTablesResult extends JSONResult {
		/** For serialization purpose */
		private static final long serialVersionUID = 5007655031236727633L;
		private static final String INCLUDE_PROPERTIES = "errori.*,importoEntitaConsultabili,sEcho,iTotalRecords,iTotalDisplayRecords,iDisplayStart,iDisplayLength,aaData.*";

		/** Empty default constructor */
		public EntitaConsultabileDataTablesResult() {
			super();
			setIgnoreHierarchy(false);
			setEnumAsBean(true);
			setExcludeNullProperties(true);
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
