/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.attivitaiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva.RisultatiRicercaStampaIvaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva.ElementoStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.StampaIva;

/**
 * Action per i risultati di ricerca della StampaIva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/01/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStampaIvaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoStampaIva,
	RisultatiRicercaStampaIvaAjaxModel, StampaIva, RicercaSinteticaStampaIva, RicercaSinteticaStampaIvaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONE_DOWNLOAD_SINGOLO = "<a href='risultatiRicercaStampaIva_download.do?uidFile=%uid%' class='btn btn-secondary'>"
			+ "Download &nbsp;<i class='icon-download-alt icon-1x'></i>"
			+ "</a>";

	@Autowired private transient StampaIvaService stampaIvaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaStampaIvaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_STAMPA_IVA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_STAMPA_IVA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaStampaIva request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaStampaIva request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoStampaIva ottieniIstanza(StampaIva e) throws FrontEndBusinessException {
		return new ElementoStampaIva(e);
	}
	
	@Override
	protected RicercaSinteticaStampaIvaResponse ottieniResponse(RicercaSinteticaStampaIva request) {
		return stampaIvaService.ricercaSinteticaStampaIva(request);
	}
	
	@Override
	protected ListaPaginata<StampaIva> ottieniListaRisultati(RicercaSinteticaStampaIvaResponse response) {
		return response.getStampeIva();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoStampaIva instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		// Gestione delle azioni consentite
		Integer uidFile = instance.getUidFile();
		String uid = uidFile != null ? uidFile.toString() : "";
		String azioni = AZIONE_DOWNLOAD_SINGOLO.replace("%uid%", uid);
		instance.setAzioni(azioni);
	}
	
}
