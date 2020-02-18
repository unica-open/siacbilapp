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

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documentoiva.RisultatiRicercaDocumentoIvaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documentoiva.ElementoDocumentoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Action per i risultati di ricerca del SubdocumentoIvaSpesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 03/06/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDocumentoIvaSpesaAjaxAction extends GenericRisultatiRicercaAjaxAction<
		ElementoDocumentoIva<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa>, 
		RisultatiRicercaDocumentoIvaAjaxModel<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa>,
		SubdocumentoIvaSpesa,
		RicercaSinteticaSubdocumentoIvaSpesa, RicercaSinteticaSubdocumentoIvaSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right dropdown-menu-datatable'>";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='#' class='aggiornaDocumentoIvaSpesa'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='#' class='consultaDocumentoIvaSpesa'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient DocumentoIvaSpesaService documentoIvaSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoIvaSpesaAjaxAction() {
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaSubdocumentoIvaSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaSubdocumentoIvaSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoDocumentoIva<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa> ottieniIstanza(SubdocumentoIvaSpesa e) {
		return new ElementoDocumentoIva<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa>(e);
	}
	
	@Override
	protected RicercaSinteticaSubdocumentoIvaSpesaResponse ottieniResponse(RicercaSinteticaSubdocumentoIvaSpesa request) {
		return documentoIvaSpesaService.ricercaSinteticaSubdocumentoIvaSpesa(request);
	}
	
	@Override
	protected ListaPaginata<SubdocumentoIvaSpesa> ottieniListaRisultati(RicercaSinteticaSubdocumentoIvaSpesaResponse response) {
		return response.getListaSubdocumentoIvaSpesa();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoDocumentoIva<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa> instance,
			boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.DOCUMENTO_IVA_SPESA_AGGIORNA, listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.DOCUMENTO_IVA_SPESA_CONSULTA, listaAzioniConsentite);
		
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
