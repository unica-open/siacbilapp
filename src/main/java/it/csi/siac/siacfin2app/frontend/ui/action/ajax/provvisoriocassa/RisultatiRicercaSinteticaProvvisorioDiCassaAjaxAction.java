/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.provvisoriocassa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.provvisoriocassa.RisultatiRicercaSinteticaProvvisorioDiCassaAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.provvisoriodicassa.ElementoProvvisorioDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Action per i risultati di ricerca sintetica del ProvvisorioDiCassa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 11/03/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaProvvisorioDiCassaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoProvvisorioDiCassa,
		RisultatiRicercaSinteticaProvvisorioDiCassaAjaxModel, ProvvisorioDiCassa, RicercaProvvisoriDiCassa, RicercaProvvisoriDiCassaResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;
	
	@Autowired private transient ProvvisorioService provvisorioService;
	
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right dropdown-menu-datatable'>";

	private static final String AZIONI_CONSENTITE_ASSOCIA_DOCUMENTO =  "<li><a href='risultatiRicercaSinteticaProvvisorioDiCassa_associaDocumento%TIPO%.do?uidDaAssociare=%UID%'>associa documento</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaProvvisorioDiCassaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PROVVISORIO_DI_CASSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PROVVISORIO_DI_CASSA);
		numeroPaginaIniziale = 1;
	}
	
	@Override
	protected ListaPaginata<ProvvisorioDiCassa> ottieniListaRisultatiDallaSessione() {
		List<ProvvisorioDiCassa> listaNonPaginata = sessionHandler.getParametro(getParametroSessioneLista());
		// Parametri di cui necessito per la paginazione
		Integer numeroPagina = sessionHandler.getParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN);
		Integer numeroRisultati = sessionHandler.getParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN);
		
		return toListaPaginata(listaNonPaginata, numeroPagina, numeroRisultati);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaProvvisoriDiCassa request) {
		return new ParametriPaginazione(request.getNumPagina(), request.getNumRisultatiPerPagina());
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaProvvisoriDiCassa request, ParametriPaginazione parametriPaginazione) {
		request.setNumPagina(parametriPaginazione.getNumeroPagina());
		request.setNumRisultatiPerPagina(parametriPaginazione.getElementiPerPagina());
	}
	
	@Override
	protected ElementoProvvisorioDiCassa ottieniIstanza(ProvvisorioDiCassa e) throws FrontEndBusinessException {
		return new ElementoProvvisorioDiCassa(e);
	}
	
	@Override
	protected RicercaProvvisoriDiCassaResponse ottieniResponse(RicercaProvvisoriDiCassa request) {
		return provvisorioService.ricercaProvvisoriDiCassa(request);
	}
	
	@Override
	protected boolean controllaDatiReperiti(ListaPaginata<ProvvisorioDiCassa> lista) {
		return lista != null && !lista.isEmpty();
	}
	
	@Override
	protected ListaPaginata<ProvvisorioDiCassa> ottieniListaRisultati(RicercaProvvisoriDiCassaResponse response) {
		// Rimetto in sessione i dati
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		
		return toListaPaginata(response.getElencoProvvisoriDiCassa(), response.getNumPagina(), response.getNumRisultati());
	}
	
	@Override
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return super.ottieniBloccoNumero(parametriPaginazione) - 1;
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoProvvisorioDiCassa instance,
			boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean isAssociaConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROVVISORIO_DI_CASSA_GESTISCI, listaAzioniConsentite);
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);
		if(Boolean.TRUE.equals(isAssociaConsentita)) {
			azioniBuilder.append(AZIONI_CONSENTITE_ASSOCIA_DOCUMENTO);
		}
		azioniBuilder.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		//attualmente la funzionalita' e' disponibile solo per i doc di entrata...ma non si sa mai!
		azioni = azioni.replaceAll("%TIPO%", instance.getTipo());
		instance.setAzioni(azioni);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		Boolean daRientro = sessionHandler.getParametro(BilSessionParameter.RIENTRO);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return Boolean.TRUE.equals(daRientro);
	}
	
}