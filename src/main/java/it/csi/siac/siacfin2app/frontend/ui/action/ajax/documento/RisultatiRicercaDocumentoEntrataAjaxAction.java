/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RisultatiRicercaDocumentoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

/**
 * Action per i risultati di ricerca del documento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDocumentoEntrataAjaxAction extends PagedDataTableAjaxAction<ElementoDocumento, 
	RisultatiRicercaDocumentoAjaxModel, DocumentoEntrata, RicercaSinteticaModulareDocumentoEntrata, RicercaSinteticaModulareDocumentoEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaDocumentoEntrataAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='risultatiRicercaDocumentoEntrataConsulta.do?uidDaConsultare=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_CONSULTA_QUOTE = 
			"<li><a href='#' class='dettaglioQuoteDocumento'>dettaglio quote</a></li>";
	
	private static final String AZIONI_CONSENTITE_ATTIVA_REGISTRAZIONI_CONTABILI= 
			"<li><a class='attivaRegistrazioniContabili'>attiva registrazioni contabili</a></li>";

	private static final String AZIONI_CONSENTITE_EMETTI_FATTURA_FEL = 
			"<li><a href='risultatiRicercaDocumentoEntrataEmettiFatturaFel.do?uidVersoFel=%UID%'>emetti fattura</a></li>";
	
	private static final String AZIONI_CONSENTITE_GESTIONE_ORDINI= 
			"<li><a href='#' class='gestioneOrdini'>gestione ordini</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	//SIAC-7557
	@Autowired
	private transient TipoDocumentoFELService tipoDocumentoServiceFEL;
	
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_ENTRATA);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaModulareDocumentoEntrata request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaModulareDocumentoEntrata request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoDocumentoEntrata getInstance(DocumentoEntrata e) throws FrontEndBusinessException {
		return ElementoDocumentoFactory.getInstanceEntrata(e);
	}
	
	@Override
	protected RicercaSinteticaModulareDocumentoEntrataResponse getResponse(RicercaSinteticaModulareDocumentoEntrata request) {	
		return documentoEntrataService.ricercaSinteticaModulareDocumentoEntrata(request);
	}
	
	@Override
	protected ListaPaginata<DocumentoEntrata> ottieniListaRisultati(RicercaSinteticaModulareDocumentoEntrataResponse response) {
		return response.getDocumenti();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoDocumento instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAggiornaConsentita = AzioniConsentiteFactory.isAggiornaConsentitoDocumentoEntrata(listaAzioniConsentite)
									   || AzioniConsentiteFactory.isAggiornaConsentitoDocumentoEntrataQuietanza(listaAzioniConsentite);
		Boolean isAnnullaConsentita = AzioniConsentiteFactory.isAnnullaConsentitoDocumentoEntrata(listaAzioniConsentite);
		Boolean isConsultaConsentita = AzioniConsentiteFactory.isConsultaConsentitoDocumentoEntrata(listaAzioniConsentite);
		
		// lo facciamo vedere sempre
		Boolean isConsultaQuoteConsentita = Boolean.TRUE;
		//LOTTO M - Documenti Bozze
		Boolean isAttivaRegistrazioniContabili = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_ENTRATA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_ENTRATA_AGGIORNA_QUIENTANZA, listaAzioniConsentite);
		
		//SIAC-7557
		Boolean isAttivaGestioneOrdini = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_ENTRATA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_ENTRATA_AGGIORNA_QUIENTANZA, listaAzioniConsentite);
		
		boolean statoOperativoValido = instance.checkStatoOperativoValido();
		boolean statoOperativoIncompleto = instance.checkStatoOperativoIncompleto();
		boolean statoOperativoEmesso = instance.checkStatoOperativoEmesso();
		boolean statoOperativoAnnullato = instance.checkStatoOperativoAnnullato();
		boolean isNotaCredito = instance.checkNotaCredito();
		boolean isIntrastat = instance.checkIntrastat();
		boolean tipoALG = instance.checkAllegatoAtto();
		boolean isContabilizzaGENPCC = instance.isContabilizzaGenPCC();
		boolean isFlagAttivaGEN = instance.isFlagAttivaGen();
		
		boolean isStatoSDIValido = instance.checkStatoSDIValido(); 
		
		//SIAC-6988 FL Verifico se lo stato SDI "Inviata a FEL,"Accettata/Consegnata","In decorrenza termini"
		boolean isStatoSDIValidoFEL = instance.checkStatoSDIFEL(); 
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		//SIAC-6988 FL 
		if( (Boolean.TRUE.equals(isAggiornaConsentita) && !statoOperativoEmesso && !statoOperativoAnnullato && !isIntrastat && !tipoALG && isStatoSDIValido ) || 
		(Boolean.TRUE.equals(isAggiornaConsentita) &&   !statoOperativoAnnullato && !isIntrastat && !tipoALG && isStatoSDIValidoFEL   && "FTV".equalsIgnoreCase(instance.getTipoDocumentoCode()) ) ){
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(Boolean.TRUE.equals(isAnnullaConsentita) &&  ((statoOperativoValido && !isNotaCredito) || statoOperativoIncompleto)  && !isIntrastat) {
			azioniBuilder.append(AZIONI_CONSENTITE_ANNULLA);
		}
		if(Boolean.TRUE.equals(isConsultaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if(Boolean.TRUE.equals(isConsultaQuoteConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_QUOTE);
		}
		//LOTTO M - Documenti Bozze
		if(Boolean.TRUE.equals(isAttivaRegistrazioniContabili) && !statoOperativoIncompleto && !statoOperativoAnnullato && !statoOperativoEmesso && !isContabilizzaGENPCC && isFlagAttivaGEN) {
			azioniBuilder.append(AZIONI_CONSENTITE_ATTIVA_REGISTRAZIONI_CONTABILI);
		}
		// SIAC-6565
		if( (Boolean.TRUE.equals(isStatoSDIValido)) 
				//SIAC-7557 Inzio
				//&& ( ("FTV".equalsIgnoreCase(instance.getTipoDocumentoCode())) || ("NCV".equalsIgnoreCase(instance.getTipoDocumentoCode())) )
				&& isEsisteMappingFEL(instance.getIdTipoDocumento()) 
				//SIAC-7557 Fine
				&& ("V".equalsIgnoreCase(instance.getStatoOperativoDocumentoCode())) ) {
			azioniBuilder.append(AZIONI_CONSENTITE_EMETTI_FATTURA_FEL);
		}
		
		//SIAC-7557
		if(Boolean.TRUE.equals(isAttivaGestioneOrdini) && !statoOperativoAnnullato && !statoOperativoEmesso) {
			azioniBuilder.append(AZIONI_CONSENTITE_GESTIONE_ORDINI);
		}
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
	@Override
	protected void eseguiOperazioniUlterioriSuResponse(RicercaSinteticaModulareDocumentoEntrataResponse response) {
			sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
	}
	
	@Override
	protected void executeEnd() {
		model.addMoreData("importoTotale", sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA));
	}
	
	//SIAC-7557
	protected Boolean isEsisteMappingFEL(Integer idTipoDocumento) {
		
		RicercaSinteticaTipoDocumentoFEL request = creaRequest(idTipoDocumento);
		
		RicercaSinteticaTipoDocumentoFELResponse res = tipoDocumentoServiceFEL.ricercaSinteticaTipoDocumentoFEL(request);
		
		if(res.hasErrori() || res.getTotaleElementi() == 0 || res.getListaTipoDocFEL().isEmpty() ) {
			 return false;
		}		
		 
		return  res.getTotaleElementi()==1 ;
	}
	//SIAC-7557

	private RicercaSinteticaTipoDocumentoFEL creaRequest(Integer idTipoDocumento)
	{
		RicercaSinteticaTipoDocumentoFEL request = new RicercaSinteticaTipoDocumentoFEL();
		
		request.setDataOra(new Date());
		request.setRichiedente(model.getRichiedente());
		request.setAnnoBilancio(model.getBilancio().getAnno());
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		
		parametriPaginazione.setElementiPerPagina(10);
		parametriPaginazione.setNumeroPagina(0);
		
		request.setParametriPaginazione(parametriPaginazione);

		TipoDocFEL tipoDocumentiFEL = new TipoDocFEL();
		 
		TipoDocumento tipoDocContabiliaEntrata = new  TipoDocumento();
			
		tipoDocContabiliaEntrata.setUid(idTipoDocumento);
		tipoDocumentiFEL.setTipoDocContabiliaEntrata(tipoDocContabiliaEntrata);
	
		// Injezione della utility di ricerca
		request.setTipoDocFEL(tipoDocumentiFEL);
		return request;
	}
	
}
