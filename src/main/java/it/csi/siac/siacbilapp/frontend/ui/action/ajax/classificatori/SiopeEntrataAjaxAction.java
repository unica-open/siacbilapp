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
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe per il caricamento <em>AJAX</em> del SIOPE di Entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SiopeEntrataAjaxAction extends GenericClassificatoriAjaxAction<LeggiTreeSiope, LeggiTreeSiopeResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2870122129746925616L;

	/** Service per i Classificatori del modulo BIL */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	protected LeggiTreeSiope definisciRequest(Integer id) {
		return model.creaRequestLeggiTreeSiope(id);
	}

	@Override
	protected LeggiTreeSiopeResponse ottieniResponse(LeggiTreeSiope request) {
		return classificatoreBilService.leggiTreeSiopeEntrata(request);
	}

	@Override
	protected void impostaLaResponseInSessione(LeggiTreeSiopeResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_SIOPE_ENTRATA, response.getTreeSiopeEntrata());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(LeggiTreeSiopeResponse response) {
		injettaInVariazione(TipologiaClassificatore.SIOPE_ENTRATA, response.getTreeSiopeEntrata());
	}

	@Override
	protected void injettaResponseNelModel(LeggiTreeSiopeResponse response) {
		model.setListaElementoCodifica(ElementoCodificaFactory.getInstances(response.getTreeSiopeEntrata()));
	}
	
	@Override
	protected void sortDeiRisultati(LeggiTreeSiopeResponse response) {
		final String methodName = "sortDeiRisultati";
		try {
			ComparatorUtils.sortDeepByCodice(response.getTreeSiopeEntrata());
		} catch (Exception e) {
			log.debug(methodName, "Errore nel sorting dei dati: " + e.getMessage());
		}
	}

}
