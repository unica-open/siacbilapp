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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * SIAC-8291
 * 
 * Action per i risultati di ricerca del capitolo di spesa gestione per la previsione accertato.
 * 
 * @author Alesssandro Todesco
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrevisioneImpegnatoCapUGAjaxAction extends GenericRisultatiRicercaCapitoloAjaxAction<CapitoloUscitaGestione, 
RicercaSinteticaCapitoloUscitaGestione, RicercaSinteticaCapitoloUscitaGestioneResponse> {

	/** per la serializzazione */
	private static final long serialVersionUID = -3139453568727368769L;


	
	private static final String AZIONI_CONSENTITE_PREVISIONE_IMPEGNATO_ACCERTATO=
			"<li><a href='#' class='gestionePrevisioneImpegnatoAccertato' data-uid='%UID%' data-chiave-logica='%CHIAVE%' >aggiorna</a></li>";
			//"<button type='button' class='btn btn-secondary gestionePrevisioneImpegnatoAccertato' data-uid='%UID%'> previsione impegnato</button>";
	private static final String AZIONI_CONSENTITE_CONSULTA_PREVISIONE_IMPEGNATO_ACCERTATO=
			"<li><a href='risultatiRicercaPrevisioneImpegnatoCapUG_consulta.do?uidCapitolo=%UID%'>consulta</a></li>";
	
	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrevisioneImpegnatoCapUGAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO);
		//vedere cosa bisogna mettere
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_USCITA_GESTIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCapitoloUscitaGestione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCapitoloUscitaGestione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaCapitoloUscitaGestioneResponse getResponse(RicercaSinteticaCapitoloUscitaGestione request) {
		return capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaGestione> ottieniListaRisultati(RicercaSinteticaCapitoloUscitaGestioneResponse response) {
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
		String chiaveLogicaCapitolo = instance.getAnnoCapitolo() + " / " + instance.getNumeroCapitolo() + " / " + instance.getNumeroArticolo();
		strAzioni = strAzioni.replaceAll("%CHIAVE%", chiaveLogicaCapitolo);
		instance.setAzioni(strAzioni);
	}
	
}
