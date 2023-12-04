/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documentoiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documentoiva.RisultatiRicercaDocumentoIvaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva.ElementoDocumentoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Action per i risultati di ricerca del SubdocumentoIvaEntrata.
 * 
 * @author Domenico
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDocumentoIvaEntrataAjaxAction extends PagedDataTableAjaxAction<
		ElementoDocumentoIva<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata>, 
		RisultatiRicercaDocumentoIvaAjaxModel<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata>, SubdocumentoIvaEntrata,
		RicercaSinteticaSubdocumentoIvaEntrata, RicercaSinteticaSubdocumentoIvaEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right dropdown-menu-datatable'>";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='#' class='aggiornaDocumentoIvaEntrata'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='#' class='consultaDocumentoIvaEntrata'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient DocumentoIvaEntrataService documentoIvaEntrataService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoIvaEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_IVA_SPESA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_IVA_SPESA);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaSubdocumentoIvaEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaSubdocumentoIvaEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoDocumentoIva<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata> getInstance(SubdocumentoIvaEntrata e) {
		return new ElementoDocumentoIva<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata>(e);
	}
	
	@Override
	protected RicercaSinteticaSubdocumentoIvaEntrataResponse getResponse(RicercaSinteticaSubdocumentoIvaEntrata request) {
		return documentoIvaEntrataService.ricercaSinteticaSubdocumentoIvaEntrata(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoIvaEntrata> ottieniListaRisultati(RicercaSinteticaSubdocumentoIvaEntrataResponse response) {
		return response.getListaSubdocumentoIvaEntrata();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoDocumentoIva<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata> instance,
			boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_IVA_ENTRATA_AGGIORNA, listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_IVA_ENTRATA_CONSULTA, listaAzioniConsentite);
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isConsultaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		instance.setAzioni(azioniBuilder.toString());
	}
	
}
