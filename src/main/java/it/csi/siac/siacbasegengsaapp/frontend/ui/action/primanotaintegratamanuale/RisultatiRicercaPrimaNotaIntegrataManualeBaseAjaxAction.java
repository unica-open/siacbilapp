/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.RisultatiRicercaPrimaNotaIntegrataManualeBaseAjaxModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di Action per i risultati ricerca della prima nota integrata manuale (comune per ambito FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public abstract class RisultatiRicercaPrimaNotaIntegrataManualeBaseAjaxAction extends PagedDataTableAjaxAction<ElementoPrimaNotaLibera, RisultatiRicercaPrimaNotaIntegrataManualeBaseAjaxModel,
PrimaNota, RicercaSinteticaPrimaNota, RicercaSinteticaPrimaNotaResponse> {


	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7775372158496507839L;

	private static final Pattern PATTERN = Pattern.compile("%(TYPE|UID)%");

	@Autowired private transient PrimaNotaService primaNotaService;
	
	// Azioni
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\">"
			+ "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>"
			+ "<ul class=\"dropdown-menu pull-right\">";
	private static final String AZIONI_CONSENTITE_VALIDA = "<li><a class=\"validaPrimaNotaLibera\" data-href=\"risultatiRicercaPrimaNotaIntegrataManuale%TYPE%_valida.do?primaNotaLibera.uid=%UID%\">valida</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a class=\"aggiornaPrimaNotaLibera\" data-href=\"risultatiRicercaPrimaNotaIntegrataManuale%TYPE%_aggiorna.do?primaNotaLibera.uid=%UID%\">aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a class=\"consultaPrimaNotaLibera\" data-href=\"risultatiRicercaPrimaNotaIntegrataManuale%TYPE%_consulta.do?primaNotaLibera.uid=%UID%\">consulta</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a class=\"annullaPrimaNotaLibera\" data-href=\"risultatiRicercaPrimaNotaIntegrataManuale%TYPE%_annulla.do?primaNotaLibera.uid=%UID%\">annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul>"
			+ "</div>";
		
	protected FaseBilancio faseBilancio;


	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaPrimaNota req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaPrimaNota req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoPrimaNotaLibera getInstance(PrimaNota pn) throws FrontEndBusinessException {
		return new ElementoPrimaNotaLibera(pn);
	}

	/*
	protected ElementoPrimaNotaLibera ottieniIstanza(PrimaNota pn, Cespite ces) throws FrontEndBusinessException {
		return new ElementoPrimaNotaLibera(pn , ces);
	}
	*/
	
	@Override
	protected RicercaSinteticaPrimaNotaResponse getResponse(RicercaSinteticaPrimaNota req) {
		return primaNotaService.ricercaSinteticaPrimaNota(req);
	}

	@Override
	protected ListaPaginata<PrimaNota> ottieniListaRisultati(RicercaSinteticaPrimaNotaResponse response) {
		return response.getPrimeNote();
	}
	
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
	
	/**
	 * @return la stringa identificativa dell'ambito (pu&ograve essere FIN o GSA)
	 * */
	protected abstract String getCodiceAmbito();
	
	/**
	 * @return azione consentita corrispondente alla gestione della prima nota libera
	 * */
	protected abstract AzioneConsentitaEnum getAzioneConsentitaGestionePrimaNotaLibera();
	
	/**
	 * @return azione consentita corrispondente alla validazione della prima nota libera
	 * */
	protected abstract AzioneConsentitaEnum getAzioneConsentitaValidazionePrimaNotaLibera();
	
	/**
	 * @return azione consentita corrispondente alla validazione della prima nota libera
	 * */
	protected abstract AzioneConsentitaEnum getAzioneConsentitaRicercaPrimaNotaLibera();
	
	@Override
	protected void handleAzioniConsentite(ElementoPrimaNotaLibera instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		
		final boolean gestioneAggiorna = gestisciAggiornamento(listaAzioniConsentite, instance);
		final boolean gestioneAnnulla = gestisciAnnullamento(listaAzioniConsentite, instance);
		final boolean gestioneConsulta = gestisciConsultazione(listaAzioniConsentite);
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
		
		strAzioni.append(AZIONI_CONSENTITE_END);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("%TYPE%", getCodiceAmbito());
		map.put("%UID%", instance.getUid() + "");

		String azioni = StringUtilities.replacePlaceholders(PATTERN, strAzioni.toString(), map, false);

		instance.setAzioni(azioni);
	}
	

	
	/**
	 * Controlla che la validazione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se la validazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciValidazione(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaLibera instance) {
		return isFaseBilancioCoerenteConValidazione()
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaValidazionePrimaNotaLibera(), listaAzioniConsentite)
				&& StatoOperativoPrimaNota.PROVVISORIO.equals(instance.getStatoOperativoPrimaNota());
	}

	/**
	 * Controlla che la consultazione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciConsultazione(List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRicercaPrimaNotaLibera(), listaAzioniConsentite);

	}

	/**
	 * Controlla che l'annullamento sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciAnnullamento(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaLibera instance) {
		return isFaseBilancioCoerenteConAnnullamento()
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestionePrimaNotaLibera(), listaAzioniConsentite)
				&& !StatoOperativoPrimaNota.ANNULLATO.equals(instance.getStatoOperativoPrimaNota());

	}

	//SIAC-8323
	protected abstract boolean isFaseBilancioCoerenteConAnnullamento();
	//SIAC-8323
	protected abstract  boolean isFaseBilancioCoerenteConValidazione();
	//SIAC-8323
	protected abstract boolean isFaseBilancioCoerenteConAggiornamento();

	/**
	 * Controlla che l'aggiornamento sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciAggiornamento(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaLibera instance) {
		return isFaseBilancioCoerenteConAggiornamento()
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestionePrimaNotaLibera(), listaAzioniConsentite)
				&& isStatoOperativoCoerenteConAggiornamento(instance);
	}

	/**
	 * Checks if is stato operativo coerente con aggiornamento.
	 *
	 * @param instance the instance
	 * @return true, if is stato operativo coerente con aggiornamentoreturn StatoOperativoPrimaNota.PROVVISORIO.equals(instance.getStatoOperativoPrimaNota());
	 */
	protected abstract boolean isStatoOperativoCoerenteConAggiornamento(ElementoPrimaNotaLibera instance);
	
	
}
