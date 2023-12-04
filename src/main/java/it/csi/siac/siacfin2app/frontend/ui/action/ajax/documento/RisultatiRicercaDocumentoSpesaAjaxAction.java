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
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaDocumentoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Action per i risultati di ricerca del documento.
 * 
 * @author Valentina
 * @version 1.0.0 - 04/05/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDocumentoSpesaAjaxAction extends PagedDataTableAjaxAction<ElementoDocumento, 
	RisultatiRicercaDocumentoAjaxModel, DocumentoSpesa, RicercaSinteticaModulareDocumentoSpesa, RicercaSinteticaModulareDocumentoSpesaResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA =
			"<li><a href='risultatiRicercaDocumentoSpesaAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA =
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href='risultatiRicercaDocumentoSpesaConsulta.do?uidDaConsultare=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_CONSULTA_QUOTE =
			"<li><a href='#' class='dettaglioQuoteDocumento'>dettaglio quote</a></li>";
	private static final String AZIONI_CONSENTITE_ATTIVA_REGISTRAZIONI_CONTABILI=
			"<li><a href='#' class='attivaRegistrazioniContabili'>attiva registrazioni contabili</a></li>";
	private static final String AZIONI_CONSENTITE_GESTIONE_ORDINI= 
			"<li><a href='#' class='gestioneOrdini'>gestione ordini</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoSpesaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_SPESA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_SPESA);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaModulareDocumentoSpesa request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaModulareDocumentoSpesa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoDocumentoSpesa getInstance(DocumentoSpesa e) throws FrontEndBusinessException {
		return ElementoDocumentoFactory.getInstanceSpesa(e);
	}
	
	@Override
	protected RicercaSinteticaModulareDocumentoSpesaResponse getResponse(RicercaSinteticaModulareDocumentoSpesa request) {	
		return documentoSpesaService.ricercaSinteticaModulareDocumentoSpesa(request);
	}
	
	@Override
	protected ListaPaginata<DocumentoSpesa> ottieniListaRisultati(RicercaSinteticaModulareDocumentoSpesaResponse response) {
		return response.getDocumenti();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoDocumento instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isAggiornaConsentitoDocumentoSpesa(listaAzioniConsentite)
				|| AzioniConsentiteFactory.isAggiornaConsentitoDocumentoSpesaQuietanza(listaAzioniConsentite);
		Boolean isAnnullaConsentita = AzioniConsentiteFactory.isAnnullaConsentitoDocumentoSpesa(listaAzioniConsentite) && isAnnullaDocumentoFELConsentita((ElementoDocumentoSpesa)instance, listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsultaConsentitoDocumentoSpesa(listaAzioniConsentite);
		
		// lo facciamo vedere sempre
		Boolean isConsultaQuoteConsentita = Boolean.TRUE;
		
		//LOTTO M - Documenti Bozze
		Boolean isAttivaRegistrazioniContabili = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_AGGIORNA_QUIENTANZA, listaAzioniConsentite);
		Boolean isAttivaGestioneOrdini = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_AGGIORNA_QUIENTANZA, listaAzioniConsentite);
		
		boolean statoOperativoValido = instance.checkStatoOperativoValido();
		boolean statoOperativoIncompleto = instance.checkStatoOperativoIncompleto();
		boolean statoOperativoEmesso = instance.checkStatoOperativoEmesso();
		boolean statoOperativoAnnullato = instance.checkStatoOperativoAnnullato();
		boolean isNotaCredito = instance.checkNotaCredito();
		boolean tipoALG = instance.checkAllegatoAtto();
		boolean isContabilizzaGENPCC = instance.isContabilizzaGenPCC();
		boolean isFlagAttivaGEN = instance.isFlagAttivaGen();
		boolean isFlagComunicaPCC = instance.isFlagComunicaPCC();
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAggiornaConsentita) && !statoOperativoEmesso && !statoOperativoAnnullato && !tipoALG) {
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isAnnullaConsentita) &&  ((statoOperativoValido && !isNotaCredito) || statoOperativoIncompleto) ) {
			azioniBuilder.append(AZIONI_CONSENTITE_ANNULLA);
		}
		if(Boolean.TRUE.equals(isConsultaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if(Boolean.TRUE.equals(isConsultaQuoteConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_QUOTE);
		}
		if(Boolean.TRUE.equals(isAttivaRegistrazioniContabili) && !statoOperativoIncompleto && !statoOperativoAnnullato && !statoOperativoEmesso && !isContabilizzaGENPCC && (isFlagAttivaGEN || isFlagComunicaPCC)) {
			azioniBuilder.append(AZIONI_CONSENTITE_ATTIVA_REGISTRAZIONI_CONTABILI);
		}
		if(Boolean.TRUE.equals(isAttivaGestioneOrdini) && !statoOperativoAnnullato && !statoOperativoEmesso) {
			azioniBuilder.append(AZIONI_CONSENTITE_GESTIONE_ORDINI);
		}
			
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}

	/**
	 * Checks if is annulla documento FEL consentita.
	 *
	 * @param instance the instance
	 * @param listaAzioniConsentite the lista azioni consentite
	 * @return true, if is annulla documento FEL consentita
	 */
	private boolean isAnnullaDocumentoFELConsentita(ElementoDocumentoSpesa instance, List<AzioneConsentita> listaAzioniConsentite) {
		
		return !AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_LIMITA_DATI_FEL, listaAzioniConsentite)
				|| !instance.isImportabileDaFEL();
	}
	
	@Override
	protected void eseguiOperazioniUlterioriSuResponse(RicercaSinteticaModulareDocumentoSpesaResponse response) {
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
	}
	
	@Override
	protected void executeEnd() {
		model.addMoreData("importoTotale", sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA));
	}

}
