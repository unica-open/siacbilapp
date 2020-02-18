/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.progetto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaProgettoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgetto;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgettoFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del progetto.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 05/02/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaProgettoAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoProgetto, 
	RisultatiRicercaProgettoAjaxModel, Progetto, RicercaSinteticaProgetto, RicercaSinteticaProgettoResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	private static final Map<String, TipoProgetto> MAPPA_FASE_BILANCIO_TIPO_PROGETTO;
	
	static {
		Map<String, TipoProgetto> tmp = new HashMap<String, TipoProgetto>();
		tmp.put("G", TipoProgetto.GESTIONE);
		tmp.put("P",  TipoProgetto.PREVISIONE);
		tmp.put("E",  TipoProgetto.PREVISIONE);
		
		MAPPA_FASE_BILANCIO_TIPO_PROGETTO = Collections.unmodifiableMap(tmp);
	}
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaProgettoAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = 
			"<li><a href='risultatiRicercaProgettoConsulta.do?uidDaConsultare=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_RIATTIVA = 
			"<li><a href='#msgRiattiva' data-toggle='modal'>riattiva</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient ProgettoService progettoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaProgettoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PROGETTO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PROGETTO);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaProgetto request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaProgetto request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoProgetto ottieniIstanza(Progetto e) throws FrontEndBusinessException {
		return ElementoProgettoFactory.getInstance(e);
	}

	@Override
	protected RicercaSinteticaProgettoResponse ottieniResponse(RicercaSinteticaProgetto request) {
		return progettoService.ricercaSinteticaProgetto(request);
	}

	@Override
	protected ListaPaginata<Progetto> ottieniListaRisultati(RicercaSinteticaProgettoResponse response) {
		return response.getProgetti();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoProgetto instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		appendIfTrue(azioniBuilder, true, AZIONI_CONSENTITE_BEGIN);
		appendIfTrue(azioniBuilder, isAggiornaConsentito(listaAzioniConsentite, instance), AZIONI_CONSENTITE_AGGIORNA);
		appendIfTrue(azioniBuilder, isAnnullaConsentito(listaAzioniConsentite, instance), AZIONI_CONSENTITE_ANNULLA);
		appendIfTrue(azioniBuilder, isRiattivaConsentito(listaAzioniConsentite, instance), AZIONI_CONSENTITE_RIATTIVA);
		appendIfTrue(azioniBuilder, isConsultaConsentito(listaAzioniConsentite), AZIONI_CONSENTITE_CONSULTA);
		appendIfTrue(azioniBuilder, true, AZIONI_CONSENTITE_END);
		
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
	/**
	 * Controlla se l'aggiornamento sia consentito
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance l'istanza
	 * @return se l'aggiornamento sia consentito
	 */
	private boolean isAggiornaConsentito(List<AzioneConsentita> listaAzioniConsentite, ElementoProgetto instance) {
		return (AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROGETTO_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROGETTO_AGGIORNA_DECENTRATO, listaAzioniConsentite))
			&& isTipoProgettoCoerenteConFaseBilancio(instance)
				&& instance.checkStatoOperativoValido();
	}
	
	/**
	 * Checks if is tipo progetto coerente con fase bilancio.
	 *
	 * @param instance the instance
	 * @return true, if is tipo progetto coerente con fase bilancio
	 */
	//SIAC-6255
	public boolean isTipoProgettoCoerenteConFaseBilancio(ElementoProgetto instance) {
		TipoProgetto tipoProgetto = MAPPA_FASE_BILANCIO_TIPO_PROGETTO.get(sessionHandler.getFaseBilancio());
		return tipoProgetto != null && tipoProgetto.getCodice().equalsIgnoreCase(instance.getCodiceTipoProgetto());
	}
	
	/**
	 * Controlla se l'annullamento sia consentito
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance l'istanza
	 * @return se l'annullamento sia consentito
	 */
	private boolean isAnnullaConsentito(List<AzioneConsentita> listaAzioniConsentite, ElementoProgetto instance) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROGETTO_AGGIORNA, listaAzioniConsentite)
				&& instance.checkStatoOperativoValido();
	}

	/**
	 * Controlla se la consultazione sia consentita
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance l'istanza
	 * @return se la consultazione sia consentita
	 */
	private boolean isConsultaConsentito(List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROGETTO_CONSULTA, listaAzioniConsentite);
	}

	/**
	 * Controlla se la riattivazione sia consentita
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance l'istanza
	 * @return se la riattivazione sia consentita
	 */
	private boolean isRiattivaConsentito(List<AzioneConsentita> listaAzioniConsentite, ElementoProgetto instance) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROGETTO_AGGIORNA, listaAzioniConsentite)
				&& instance.checkStatoOperativoAnnullato();
	}
}
