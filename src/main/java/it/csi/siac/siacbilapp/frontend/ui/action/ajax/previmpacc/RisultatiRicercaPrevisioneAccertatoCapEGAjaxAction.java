/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.previmpacc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaCapitoloAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrevisioneAccertatoCapEGAjaxAction extends GenericRisultatiRicercaCapitoloAjaxAction<CapitoloEntrataGestione, 
RicercaSinteticaCapitoloEntrataGestione, RicercaSinteticaCapitoloEntrataGestioneResponse>{

	/** per la serializzazione */
	private static final long serialVersionUID = 2605935395943435970L;


	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	

	private static final String AZIONI_CONSENTITE_PREVISIONE_IMPEGNATO_ACCERTATO=
			"<li><a href='#' class='gestionePrevisioneImpegnatoAccertato' data-uid='%UID%' data-chiave-logica='%CHIAVE%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA_PREVISIONE_IMPEGNATO_ACCERTATO=
			"<li><a href='risultatiRicercaCapEGPrevisioneImpegnatoAccertato_consulta.do?uidCapitolo=%UID%'>consulta</a></li>";
			//"<button type='button' class='btn btn-secondary gestionePrevisioneImpegnatoAccertato' data-uid='%UID%'> previsione accertato</button>";
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrevisioneAccertatoCapEGAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO);
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_ENTRATA_GESTIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCapitoloEntrataGestione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCapitoloEntrataGestione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaCapitoloEntrataGestioneResponse getResponse(RicercaSinteticaCapitoloEntrataGestione request) {
		return capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataGestione> ottieniListaRisultati(RicercaSinteticaCapitoloEntrataGestioneResponse response) {
		return response.getCapitoli();
	}
	
	/**
	 * Controllo quale sia l'azione presa in causa, se non ci sono corrispondenze ritorno al giro normale,
	 * altrimenti ritorno come unica azione quella di gestione
	 */
	@Override
	protected void handleAzioniConsentite(ElementoCapitolo instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		boolean gestionePrevisioneImpegnatoAccertato = 
				AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREVISIONE_IMPEGNATO_ACCERTATO, sessionHandler.getAzioniConsentite());
		
				StringBuilder strAzioniSB = new StringBuilder().append(AZIONI_CONSENTITE_BEGIN);
		if (gestionePrevisioneImpegnatoAccertato) {
			String azioneConsulta = AZIONI_CONSENTITE_PREVISIONE_IMPEGNATO_ACCERTATO;
			azioneConsulta = gestisciAzioneConsulta(azioneConsulta, instance);
			strAzioniSB.append(azioneConsulta);
		}
		strAzioniSB.append(AZIONI_CONSENTITE_CONSULTA_PREVISIONE_IMPEGNATO_ACCERTATO);
		strAzioniSB.append(AZIONI_CONSENTITE_END);
		String strAzioni = strAzioniSB.toString(); //.replaceAll("%actionName%", nomeAzione);
		strAzioni = strAzioni.replaceAll("%UID%", Integer.toString(instance.getUid()));
		String chiaveLogicaCapitolo = instance.getAnnoCapitolo() + " / " + instance.getNumeroCapitolo()+ " / " + instance.getNumeroArticolo();
		strAzioni = strAzioni.replaceAll("%CHIAVE%", chiaveLogicaCapitolo);
		instance.setAzioni(strAzioni);
	}

}
