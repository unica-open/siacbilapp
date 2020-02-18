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

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaDocumentoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Action per i risultati di ricerca della testata del documento di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/07/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTestataDocumentoSpesaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoDocumento,
	RisultatiRicercaDocumentoAjaxModel, DocumentoSpesa, RicercaSinteticaDocumentoSpesa, RicercaSinteticaDocumentoSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7921025105905617890L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaTestataDocumentoSpesaAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTestataDocumentoSpesaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TESTATA_DOCUMENTI_SPESA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TESTATA_DOCUMENTI_SPESA);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaDocumentoSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaDocumentoSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoDocumentoSpesa ottieniIstanza(DocumentoSpesa e) throws FrontEndBusinessException {
		return ElementoDocumentoFactory.getInstanceSpesa(e);
	}
	
	@Override
	protected RicercaSinteticaDocumentoSpesaResponse ottieniResponse(RicercaSinteticaDocumentoSpesa request) {
		return documentoSpesaService.ricercaSinteticaTestataDocumentoSpesa(request);
	}
	
	@Override
	protected ListaPaginata<DocumentoSpesa> ottieniListaRisultati(RicercaSinteticaDocumentoSpesaResponse response) {
		return response.getDocumenti();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoDocumento instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isAggiornaConsentitoDocumentoSpesa(listaAzioniConsentite)
			|| AzioniConsentiteFactory.isAggiornaConsentitoDocumentoSpesaQuietanza(listaAzioniConsentite);
		
		boolean statoOperativoEmesso = instance.checkStatoOperativoEmesso();
		boolean statoOperativoAnnullato = instance.checkStatoOperativoAnnullato();
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita) && !statoOperativoEmesso && !statoOperativoAnnullato) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
}
