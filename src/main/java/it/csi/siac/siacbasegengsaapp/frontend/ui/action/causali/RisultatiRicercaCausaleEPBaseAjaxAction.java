/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RisultatiRicercaCausaleEPBaseAjaxModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.causali.ElementoCausaleEP;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;

/**
 * Classe di action per i risultati di ricerca della causale EP, gestione AJAX.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 07/10/2015
 */

public abstract class RisultatiRicercaCausaleEPBaseAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoCausaleEP,
		RisultatiRicercaCausaleEPBaseAjaxModel, CausaleEP, RicercaSinteticaCausale, RicercaSinteticaCausaleResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5851370730907939528L;
	private static final Pattern PATTERN = Pattern.compile("%(TYPE|UIDRICHIESTA)%");

	// Azioni
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\">"
			+ "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>"
			+ "<ul class=\"dropdown-menu pull-right\">";
	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a class=\"aggiornaCausaleEP\" data-href=\"risultatiRicercaCausaleEP%TYPE%_aggiorna.do?causaleEP.uid=%UIDRICHIESTA%\">aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_VALIDA = "<li><a class=\"validaCausaleEP\" data-href=\"risultatiRicercaCausaleEP%TYPE%_valida.do?causaleEP.uid=%UIDRICHIESTA%\">valida</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a class=\"consultaCausaleEP\" data-href=\"risultatiRicercaCausaleEP%TYPE%_consulta.do?causaleEP.uid=%UIDRICHIESTA%\">consulta</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a class=\"annullaCausaleEP\" data-href=\"risultatiRicercaCausaleEP%TYPE%_annulla.do?causaleEP.uid=%UIDRICHIESTA%\">annulla</a></li>";
	private static final String AZIONI_CONSENTITE_ELIMINA = "<li><a class=\"eliminaCausaleEP\" data-href=\"risultatiRicercaCausaleEP%TYPE%_elimina.do?causaleEP.uid=%UIDRICHIESTA%\">elimina</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul>"
			+ "</div>";
	
	@Autowired private transient CausaleService causaleService;
	private FaseBilancio faseBilancio;
	
		
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Lo imposto qui e non nel costruttore in quanto non ho ancora il wiring del sessionHandler
		faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCausale request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCausale request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoCausaleEP ottieniIstanza(CausaleEP e) throws FrontEndBusinessException {
		return new ElementoCausaleEP(e);
	}

	@Override
	protected RicercaSinteticaCausaleResponse ottieniResponse(RicercaSinteticaCausale request) {
		return causaleService.ricercaSinteticaCausale(request);
	}

	@Override
	protected ListaPaginata<CausaleEP> ottieniListaRisultati(RicercaSinteticaCausaleResponse response) {
		return response.getCausali();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoCausaleEP instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		final boolean gestioneAggiorna = gestisciAggiornamento(listaAzioniConsentite, instance);
		final boolean gestioneAnnulla = gestisciAnnullamento(listaAzioniConsentite, instance);
		final boolean gestioneConsulta = gestisciConsultazione(listaAzioniConsentite);
		final boolean gestioneElimina = gestisciEliminazione(listaAzioniConsentite);
		final boolean gestioneValida = gestisciValidazione(listaAzioniConsentite, instance);
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
		
		if (gestioneAggiorna) {
			strAzioni.append(AZIONI_CONSENTITE_AGGIORNA);
		}
		if(gestioneValida) {
			strAzioni.append(AZIONI_CONSENTITE_VALIDA);
		}
		if (gestioneConsulta) {
			strAzioni.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if (gestioneAnnulla) {
			strAzioni.append(AZIONI_CONSENTITE_ANNULLA);
		}
		if (gestioneElimina) {
			strAzioni.append(AZIONI_CONSENTITE_ELIMINA);
		}
		
		strAzioni.append(AZIONI_CONSENTITE_END);
		Map<String, String> map = new HashMap<String, String>();
		map.put("%TYPE%", getCodiceAmbitoForPattern());
		map.put("%UIDRICHIESTA%", instance.getUid() + "");
		
		String azioni = StringUtilities.replacePlaceholders(PATTERN, strAzioni.toString(), map, false);
		instance.setAzioni(azioni);
	}

	/**
	 * Controlla se l'aggiornamento &eacute; gestibile.
	 * 
	 * @param listaAzioniConsentite le azioni consentite all'utente
	 * @param wrapper il wrapper
	 * 
	 * @return <code>true</code> se l'utente pu&acute; gestire l'aggiornamento; <code>false</code> altrimenti
	 */
	private boolean gestisciAggiornamento(List<AzioneConsentita> listaAzioniConsentite, ElementoCausaleEP wrapper) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestioneCausaleEP(), listaAzioniConsentite)
				// SIAC-5516: lo stato non puo' essere ANNULLATO
				&& wrapper != null
				&& wrapper.retrieveStato() != null
				&& !StatoOperativoCausaleEP.ANNULLATO.equals(wrapper.retrieveStato());
	}

	/**
	 * Controlla se l'annullamento &eacute; gestibile.
	 * 
	 * @param listaAzioniConsentite le azioni consentite all'utente
	 * @param wrapper il wrapper
	 * 
	 * @return <code>true</code> se l'utente pu&acute; gestire l'annullamento; <code>false</code> altrimenti
	 */
	private boolean gestisciAnnullamento(List<AzioneConsentita> listaAzioniConsentite, ElementoCausaleEP wrapper) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestioneCausaleEP(), listaAzioniConsentite)
				// SIAC-5516: lo stato non puo' essere ANNULLATO
				&& wrapper != null
				&& wrapper.retrieveStato() != null
				&& !StatoOperativoCausaleEP.ANNULLATO.equals(wrapper.retrieveStato());
	}

	/**
	 * Controlla se la consultazione &eacute; gestibile.
	 * 
	 * @param listaAzioniConsentite le azioni consentite all'utente
	 * 
	 * @return <code>true</code> se l'utente pu&acute; gestire la consultazione; <code>false</code> altrimenti
	 */
	private boolean gestisciConsultazione(List<AzioneConsentita> listaAzioniConsentite) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRicercaCausaleEP(), listaAzioniConsentite);
	}

	/**
	 * Controlla se l'eliminazione &eacute; gestibile.
	 * 
	 * @param listaAzioniConsentite le azioni consentite all'utente
	 * 
	 * @return <code>true</code> se l'utente pu&acute; gestire l'eliminazione; <code>false</code> altrimenti
	 */
	private boolean gestisciEliminazione(List<AzioneConsentita> listaAzioniConsentite) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestioneCausaleEP(), listaAzioniConsentite);
	}

	/**
	 * Controlla se la validazione &eacute; gestibile.
	 * 
	 * @param listaAzioniConsentite le azioni consentite all'utente
	 * @param wrapper il wrapper
	 * 
	 * @return <code>true</code> se l'utente pu&acute; gestire la validazione; <code>false</code> altrimenti
	 */
	private boolean gestisciValidazione(List<AzioneConsentita> listaAzioniConsentite, ElementoCausaleEP wrapper) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestioneCausaleEP(), listaAzioniConsentite)
				// SIAC-5516: lo stato non puo' essere ANNULLATO
				&& wrapper != null
				&& wrapper.retrieveStato() != null
				&& !StatoOperativoCausaleEP.ANNULLATO.equals(wrapper.retrieveStato());
	}
	
	/**
	 * @return il codice dell'ambito di riferimento ('FIN/GSA')
	 */
	protected abstract String getCodiceAmbitoForPattern();
	
	/**
	 * @return l'azione consentita corrispondente alla gestione della causaleEP
	 */
	protected abstract AzioniConsentite getAzioneConsentitaGestioneCausaleEP();
	
	/**
	 * @return l'azione consentita corrispondente alla gestione della causaleEP
	 */
	protected abstract AzioniConsentite getAzioneConsentitaRicercaCausaleEP();
	
	
}
