/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori.ricercasintetica;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action base per i risultati di ricerca dei classificatori gerarchici.
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/12/2015
 * @param <CG> la tipizzazione del classificatore
 * @param <M> la tipizzazione del model
 *
 */
public abstract class RisultatiRicercaClassificatoreGerachicoAjaxAction<CG extends ClassificatoreGerarchico, M extends GenericRisultatiRicercaAjaxModel<CG>> extends GenericRisultatiRicercaAjaxAction<CG,
	M, Codifica, RicercaSinteticaClassificatore, RicercaSinteticaClassificatoreResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaClassificatore request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaClassificatore request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaClassificatoreResponse ottieniResponse(RicercaSinteticaClassificatore request) {
		return classificatoreBilService.ricercaSinteticaClassificatore(request);
	}

	@Override
	protected ListaPaginata<Codifica> ottieniListaRisultati(RicercaSinteticaClassificatoreResponse response) {
		return response.getCodifiche();
	}
	
}
