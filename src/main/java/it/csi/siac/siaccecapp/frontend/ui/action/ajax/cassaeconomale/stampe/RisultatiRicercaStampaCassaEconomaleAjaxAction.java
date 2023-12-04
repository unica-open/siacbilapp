/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.ajax.cassaeconomale.stampe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.stampe.RisultatiRicercaStampaCassaEconomaleAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe.ElementoStampeCassaFile;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca delle StampeCassaFile.
 * 
 * @author Valentina Triolo
 * @version 1.0.0 - 01/04/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStampaCassaEconomaleAjaxAction extends PagedDataTableAjaxAction<ElementoStampeCassaFile,
 RisultatiRicercaStampaCassaEconomaleAjaxModel, StampeCassaFile, RicercaStampeCassaEconomale, RicercaStampeCassaEconomaleResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONE_DOWNLOAD_SINGOLO = "<a href='risultatiRicercaStampeCassaEconomale_download.do?uidFile=%uid%' class='btn btn-secondary'>"
			+ "Download &nbsp;<i class='icon-download-alt icon-1x'></i>"
			+ "</a>";

	@Autowired private transient StampaCassaEconomaleService stampaCassaEconomaleService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaStampaCassaEconomaleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_STAMPE_CEC);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_STAMPE_CEC);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaStampeCassaEconomale request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaStampeCassaEconomale request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoStampeCassaFile getInstance(StampeCassaFile e) throws FrontEndBusinessException {
		return new ElementoStampeCassaFile(e);
	}
	
	@Override
	protected RicercaStampeCassaEconomaleResponse getResponse(RicercaStampeCassaEconomale request) {
		return stampaCassaEconomaleService.ricercaStampeCassaEconomale(request);
	}
	
	@Override
	protected ListaPaginata<StampeCassaFile> ottieniListaRisultati(RicercaStampeCassaEconomaleResponse response) {
		return response.getListaStampe();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoStampeCassaFile instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		// Gestione delle azioni consentite
		Integer uidFile = instance.getUidFile();
		String uid = uidFile != null ? uidFile.toString() : "";
		String azioni = AZIONE_DOWNLOAD_SINGOLO.replace("%uid%", uid);
		instance.setAzioni(azioni);
	}
	
}
