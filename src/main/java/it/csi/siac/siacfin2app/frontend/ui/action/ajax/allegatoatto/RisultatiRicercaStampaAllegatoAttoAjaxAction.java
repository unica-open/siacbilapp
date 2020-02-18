/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * 
 */
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaStampaAllegatoAttoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;

/**
 * Action per i risultati di ricerca della Stampa allegato atto
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 30/12/2015
 * 
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStampaAllegatoAttoAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoStampaAllegatoAtto, RisultatiRicercaStampaAllegatoAttoAjaxModel, AllegatoAttoStampa, RicercaSinteticaStampaAllegatoAtto, RicercaSinteticaStampaAllegatoAttoResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9094901756200404648L;

	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONE_DOWNLOAD_SINGOLO = "<a href='risultatiRicercaStampaAllegatoAtto_download.do?uidFile=%uid%' class='btn btn-secondary'>"
			+ "Download &nbsp;<i class='icon-download-alt icon-1x'></i>"
			+ "</a>";
		
	/** Costruttore vuoto di default */
	public RisultatiRicercaStampaAllegatoAttoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_STAMPA_ALLEGATO_ATTO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_STAMPA_ALLEGATO_ATTO);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaStampaAllegatoAtto request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaStampaAllegatoAtto request,
			ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	

	@Override
	protected ElementoStampaAllegatoAtto ottieniIstanza(AllegatoAttoStampa e) throws FrontEndBusinessException {
		return new ElementoStampaAllegatoAtto(e);
	}

	@Override
	protected RicercaSinteticaStampaAllegatoAttoResponse ottieniResponse(RicercaSinteticaStampaAllegatoAtto request) {
		return allegatoAttoService.ricercaSinteticaStampaAllegatoAtto(request);
	}

	@Override
	protected ListaPaginata<AllegatoAttoStampa> ottieniListaRisultati(RicercaSinteticaStampaAllegatoAttoResponse response) {
		return response.getListaAllegatoAttoStampa();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoStampaAllegatoAtto instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		// Gestione delle azioni consentite
		Integer uidFile = instance.getUidFile();
		String uid = uidFile != null ? uidFile.toString() : "";
		String azioni = AZIONE_DOWNLOAD_SINGOLO.replace("%uid%", uid);
		instance.setAzioni(azioni);
	}

}
