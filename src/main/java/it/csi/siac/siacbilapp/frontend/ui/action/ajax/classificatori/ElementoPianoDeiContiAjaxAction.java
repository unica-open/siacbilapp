/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericClassificatoriAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.codifica.ElementoCodificaFactory;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe per il caricamento <em>AJAX</em> dell'Elemento del Piano dei Conti.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElementoPianoDeiContiAjaxAction extends GenericClassificatoriAjaxAction<LeggiTreePianoDeiConti, LeggiTreePianoDeiContiResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3555086058063704977L;
	
	/** Service per i Classificatori del modulo BIL */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	protected LeggiTreePianoDeiConti definisciRequest(Integer id) {
		return model.creaRequestLeggiTreePianoDeiConti(id);
	}

	@Override
	protected LeggiTreePianoDeiContiResponse ottieniResponse(LeggiTreePianoDeiConti request) {
		return classificatoreBilService.leggiTreePianoDeiConti(request);
	}

	@Override
	protected void impostaLaResponseInSessione(LeggiTreePianoDeiContiResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI, response.getTreeElementoPianoDeiConti());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(LeggiTreePianoDeiContiResponse response) {
		injettaInVariazione(TipologiaClassificatore.PDC, response.getTreeElementoPianoDeiConti());
	}

	@Override
	protected void injettaResponseNelModel(LeggiTreePianoDeiContiResponse response) {
		model.setListaElementoCodifica(ElementoCodificaFactory.getInstances(response.getTreeElementoPianoDeiConti()));
	}
	
	@Override
	protected void sortDeiRisultati(LeggiTreePianoDeiContiResponse response) {
		final String methodName = "sortDeiRisultati";
		try {
			ComparatorUtils.sortDeepByCodice(response.getTreeElementoPianoDeiConti());
		} catch (Exception e) {
			log.error(methodName, "Errore nel sorting dei dati: " + e.getMessage(), e);
		}
	}

}
