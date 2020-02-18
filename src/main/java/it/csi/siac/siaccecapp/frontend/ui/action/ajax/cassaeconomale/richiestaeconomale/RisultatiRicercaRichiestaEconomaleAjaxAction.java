/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.ajax.cassaeconomale.richiestaeconomale;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.richiestaeconomale.RisultatiRicercaModulareRichiestaEconomaleAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.richiestaeconomale.ElementoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di action per i risultati di ricerca modulare per la richiesta economale, gestione dell'AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/03/2017
 *
 */
public abstract class RisultatiRicercaRichiestaEconomaleAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoRichiestaEconomale,
		RisultatiRicercaModulareRichiestaEconomaleAjaxModel, RichiestaEconomale, RicercaSinteticaModulareRichiestaEconomale, RicercaSinteticaModulareRichiestaEconomaleResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5288371197838531329L;
	
	/** Il pattern per la sostituzione */
	private static final Pattern PATTERN = Pattern.compile("%(prenotata|source\\.uid|baseActionUrl|rendiconto\\.uid)%");
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN =
			"<div class='btn-group'>" +
				"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
				"<ul class='dropdown-menu pull-right'>";
	private static final String AZIONI_CONSENTITE_AGGIORNA =
			"<li><a class='aggiornaRichiestaEconomale' data-href='%baseActionUrl%_aggiorna.do?uidRichiesta=%source.uid%'>aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA =
			"<li><a class='annullaRichiestaEconomale' data-prenotata='%prenotata%' data-href='%baseActionUrl%_annulla.do?uidRichiesta=%source.uid%'>annulla</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href='%baseActionUrl%_consulta.do?uidRichiesta=%source.uid%'>consulta</a></li>";
	private static final String AZIONI_CONSENTITE_RENDICONTA =
			"<li><a href='%baseActionUrl%_rendiconta.do?uidRichiesta=%source.uid%'>rendiconto</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA_RENDICONTO =
			"<li><a href='%baseActionUrl%_aggiornaRendiconto.do?uidRichiesta=%rendiconto.uid%'>aggiorna rendiconto</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA_RENDICONTO =
			"<li><a href='%baseActionUrl%_consultaRendiconto.do?uidRichiesta=%rendiconto.uid%'>consulta rendiconto</a></li>";
	
	private static final String AZIONI_CONSENTITE_STAMPA_RICEVUTA =
			"<li><a class='stampaRicevutaRichiesta' data-href='%baseActionUrl%_stampaRicevuta.do?uidRichiestaDaStampare=%source.uid%'>stampa ricevuta</a></li>";
	private static final String AZIONI_CONSENTITE_STAMPA_RICEVUTA_RENDICONTO =
			"<li><a class='stampaRicevutaRendiconto' data-href='%baseActionUrl%_stampaRicevutaRendiconto.do'>stampa ricevuta rendiconto</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";

	@Autowired private transient RichiestaEconomaleService richiestaEconomaleService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaRichiestaEconomaleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_MODULARE_RICHIESTA_ECONOMALE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_MODULARE_RICHIESTA_ECONOMALE);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaModulareRichiestaEconomale request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaModulareRichiestaEconomale request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoRichiestaEconomale ottieniIstanza(RichiestaEconomale e) throws FrontEndBusinessException {
		return new ElementoRichiestaEconomale(e);
	}

	@Override
	protected RicercaSinteticaModulareRichiestaEconomaleResponse ottieniResponse(RicercaSinteticaModulareRichiestaEconomale request) {
		return richiestaEconomaleService.ricercaSinteticaModulareRichiestaEconomale(request);
	}

	@Override
	protected ListaPaginata<RichiestaEconomale> ottieniListaRisultati(RicercaSinteticaModulareRichiestaEconomaleResponse response) {
		return response.getRichiesteEconomali();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoRichiestaEconomale instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		final boolean gestioneAggiorna = gestisciAggiornamento(instance, listaAzioniConsentite);
		final boolean gestioneAnnulla = gestisciAnnullamento(instance, listaAzioniConsentite);
		final boolean gestioneConsulta = gestisciConsultazione(instance, listaAzioniConsentite);
		final boolean gestioneStampaRicevuta = gestisciStampaRicevuta(instance, listaAzioniConsentite);
		final boolean gestioneStampaRicevutaRendiconto = gestisciStampaRicevutaRendiconto(instance, listaAzioniConsentite);
		final boolean gestioneRendiconta = gestisciRendicontazione(instance, listaAzioniConsentite);
		final boolean gestioneAggiornaRendiconto = gestisciAggiornamentoRendiconto(instance, listaAzioniConsentite);
		final boolean gestioneConsultaRendiconto = gestisciConsultazioneRendiconto(instance, listaAzioniConsentite);
		
		final String baseActionUrl = ottieniBaseActionUrl();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("%baseActionUrl%", baseActionUrl);
		map.put("%source.uid%", Integer.toString(instance.getUid()));
		map.put("%rendiconto.uid%", Integer.toString(instance.getUidRendiconto()));
		map.put("%prenotata%", Boolean.toString(instance.isStatoOperativoPrenotata()));

		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
		
		if (gestioneAggiorna) {
			strAzioni.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if (gestioneAnnulla) {
			strAzioni.append(AZIONI_CONSENTITE_ANNULLA);
		}
		if (gestioneConsulta) {
			strAzioni.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if (gestioneRendiconta) {
			strAzioni.append(AZIONI_CONSENTITE_RENDICONTA);
		}
		if (gestioneAggiornaRendiconto) {
			strAzioni.append(AZIONI_CONSENTITE_AGGIORNA_RENDICONTO);
		}
		if (gestioneConsultaRendiconto) {
			strAzioni.append(AZIONI_CONSENTITE_CONSULTA_RENDICONTO);
		}
		if (gestioneStampaRicevuta) {
			strAzioni.append(AZIONI_CONSENTITE_STAMPA_RICEVUTA);
		}
		if (gestioneStampaRicevutaRendiconto) {
			strAzioni.append(AZIONI_CONSENTITE_STAMPA_RICEVUTA_RENDICONTO);
		}
		strAzioni.append(AZIONI_CONSENTITE_END);
		
		// Sostituzione dei placeholder
		String azioni = StringUtilities.replacePlaceholders(PATTERN, strAzioni.toString(), map, false);
		instance.setAzioni(azioni);
	}

	/**
	 * Gestione dell'aggiornamento: controlla se sia possibile aggiornare la richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la richiesta &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	protected abstract boolean gestisciAggiornamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite);

	/**
	 * Gestione dell'annullamento: controlla se sia possibile annullare la richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la richiesta &eacute; annullabile; <code>false</code> in caso contrario
	 */
	protected abstract boolean gestisciAnnullamento(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite);
	/**
	 * Gestione della stampa ricevuta: controlla se sia possibile stampare la richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la richiesta &eacute; stampabile; <code>false</code> in caso contrario
	 */
	protected boolean gestisciStampaRicevuta(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.CASSA_ECONOMALE_STAMPA_RICEVUTA, listaAzioniConsentite);
	}
	/**
	 * Gestione della stampa ricevuta del rendiconto: controlla se sia possibile stampare la richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la richiesta &eacute; stampabile; <code>false</code> in caso contrario
	 */
	protected boolean gestisciStampaRicevutaRendiconto(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// SIAC-4713
		// Il valore di default e' FALSE
		return false;
	}

	/**
	 * Gestione della consultazione: controlla se sia possibile consultare la richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la richiesta &eacute; consultabile; <code>false</code> in caso contrario
	 */
	protected abstract boolean gestisciConsultazione(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite);
	
	/**
	 * Gestione della rendicontazione: controlla se sia possibile rendicontare la richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la richiesta &eacute; rendicontabile; <code>false</code> in caso contrario
	 */
	protected boolean gestisciRendicontazione(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// Il valore di default e' FALSE
		return false;
	}
	
	/**
	 * Gestione della rendicontazione: controlla se sia possibile aggiornare il rendiconto della richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se il rendiconto &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	protected boolean gestisciAggiornamentoRendiconto(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// Il valore di default e' FALSE
		return false;
	}
	
	/**
	 * Gestione della rendicontazione: controlla se sia possibile consultare il rendiconto della richiesta.
	 * 
	 * @param instance              l'istanza del wrapper
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se il rendiconto &eacute; consultabile; <code>false</code> in caso contrario
	 */
	protected boolean gestisciConsultazioneRendiconto(ElementoRichiestaEconomale instance, List<AzioneConsentita> listaAzioniConsentite) {
		// Il valore di default e' FALSE
		return false;
	}
	
	/**
	 * Restituisce l'URL di base per le azioni dei risultati della ricerca.
	 * 
	 * @return l'URL di base
	 */
	protected abstract String ottieniBaseActionUrl();
	
	@Override
	protected void eseguiOperazioniUlterioriSuResponse(RicercaSinteticaModulareRichiestaEconomaleResponse response) {
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getTotaleImporti());
	}
	
	@Override
	protected void eseguiOperazioniUlteriori() {
		BigDecimal totale = sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA);
		model.setTotale(totale);
	}
	
	/**
	 * Controlla se lo stato operativo sia PRENOTATA, EVASA o DA_RENDICONTARE.
	 * 
	 * @param instance il wrapper della richiesta
	 * 
	 * @return <code>true</code> se lo stato operativo &eacute; PRENOTATA, EVASA o DA_RENDICONTARE; <code>false</code> altrimenti
	 */
	protected boolean isStatoOperativoPrenotataEvasaOrDaRendicontare(ElementoRichiestaEconomale instance) {
		return instance.isStatoOperativoPrenotata()
				|| instance.isStatoOperativoEvasa()
				|| instance.isStatoOperativoDaRendicontare();
	}
	
}
