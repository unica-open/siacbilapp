/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.registroa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa.RisultatiRicercaRegistroACespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoPrimaNotaRegistroA;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespiteResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di Action per i risultati ricerca del registro A (prime note verso inventario contabile)del cespite, gestione AJAX.
 * @author elisa
 * @version 1.0.0 - 29-11-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaScrittureRegistroAByCespiteAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoPrimaNotaRegistroA, RisultatiRicercaRegistroACespiteAjaxModel,
		PrimaNota, RicercaSinteticaScrittureRegistroAByCespite, RicercaSinteticaScrittureRegistroAByCespiteResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7594573476324698068L;

	@Autowired private transient CespiteService cespiteService;
	
//	private static final String AZIONE_CONSULTA_DATI_CONTABILI = "<button type=\"button\" class=\" btn btn-secondary apriDatiContabili\"> dati contabili</button>";

	/** Costruttore vuoto di default */
	public RisultatiRicercaScrittureRegistroAByCespiteAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SCRITTURE_REGISTROA_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SCRITTURE_REGISTROA_CESPITE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaScrittureRegistroAByCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaScrittureRegistroAByCespite req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoPrimaNotaRegistroA ottieniIstanza(PrimaNota e) throws FrontEndBusinessException {
		return new ElementoPrimaNotaRegistroA(e);
	}

	@Override
	protected RicercaSinteticaScrittureRegistroAByCespiteResponse ottieniResponse(RicercaSinteticaScrittureRegistroAByCespite req) {
		return cespiteService.ricercaSinteticaScrittureRegistroAByCespite(req);
	}

	@Override
	protected ListaPaginata<PrimaNota> ottieniListaRisultati(RicercaSinteticaScrittureRegistroAByCespiteResponse response) {
		return response.getListaPrimaNota();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	
//	@Override
//	protected void gestisciAzioniConsentite(ElementoPrimaNotaRegistroA instance, boolean daRientro, boolean isAggiornaAbilitato,
//			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
//		
//		if(TipoCausale.Integrata.equals(instance.unwrap().getTipoCausale())) {
//			instance.setAzioni(AZIONE_CONSULTA_DATI_CONTABILI);
//		}
//		
//
//		
//	}
}
