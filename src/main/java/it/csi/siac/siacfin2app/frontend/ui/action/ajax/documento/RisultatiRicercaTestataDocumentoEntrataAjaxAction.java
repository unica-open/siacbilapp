/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaDocumentoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * Action per i risultati di ricerca della testata del documento di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/07/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTestataDocumentoEntrataAjaxAction extends PagedDataTableAjaxAction<ElementoDocumento,
	RisultatiRicercaDocumentoAjaxModel, DocumentoEntrata, RicercaSinteticaDocumentoEntrata, RicercaSinteticaDocumentoEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3401669639506724634L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaTestataDocumentoEntrataAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTestataDocumentoEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TESTATA_DOCUMENTI_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TESTATA_DOCUMENTI_ENTRATA);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaDocumentoEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaDocumentoEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoDocumentoEntrata getInstance(DocumentoEntrata e) throws FrontEndBusinessException {
		return ElementoDocumentoFactory.getInstanceEntrata(e);
	}
	
	@Override
	protected RicercaSinteticaDocumentoEntrataResponse getResponse(RicercaSinteticaDocumentoEntrata request) {
		return documentoEntrataService.ricercaSinteticaTestataDocumentoEntrata(request);
	}
	
	@Override
	protected ListaPaginata<DocumentoEntrata> ottieniListaRisultati(RicercaSinteticaDocumentoEntrataResponse response) {
		return response.getDocumenti();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoDocumento instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isAggiornaConsentitoDocumentoEntrata(listaAzioniConsentite)
			|| AzioniConsentiteFactory.isAggiornaConsentitoDocumentoEntrataQuietanza(listaAzioniConsentite);
		
		boolean statoOperativoEmesso = instance.checkStatoOperativoEmesso();
		boolean statoOperativoAnnullato = instance.checkStatoOperativoAnnullato();
		boolean isIntrastat = instance.checkIntrastat();
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita) && !statoOperativoEmesso && !statoOperativoAnnullato && !isIntrastat) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
}
