/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseAjaxModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe base di action per i risultati di ricerca della prima nota integrata, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.0.1 - 16/06/2015
 * @version 1.1.0 - 08/10/2015 - gestione GEN/GSA
 * @author Elisa Chiari
 * @version 1.20.0 - 16/03/2017 --gestipone aggiornamento prima nota
 */
public abstract class RisultatiRicercaPrimaNotaIntegrataBaseAjaxAction extends BaseRisultatiRicercaPrimaNotaIntegrataBaseAjaxAction<RisultatiRicercaPrimaNotaIntegrataBaseAjaxModel,
		RicercaSinteticaPrimaNotaIntegrata, RicercaSinteticaPrimaNotaIntegrataResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2085403225367653701L;

	private static final Pattern PATTERN = Pattern.compile("%(TYPE|UID|UID_DOCUMENTO)%");
	
	// Azioni
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\">"
			+ "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>"
			+ "<ul class=\"dropdown-menu pull-right\">";
	private static final String AZIONI_CONSENTITE_VALIDA = "<li><a class=\"validaPrimaNota\" data-href=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_valida.do?primaNota.uid=%UID%\">valida</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a class=\"aggiornaPrimaNota\" data-href=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_aggiornaPrimaNota.do?primaNota.uid=%UID%\">aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA_EVENTO_DOCUMENTO = "<li><a class=\"aggiornaPrimaNota\" data-href=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_aggiornaPrimaNotaDocumento.do?primaNota.uid=%UID%\">aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a class=\"consultaPrimaNota\" data-href=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_consulta.do?primaNota.uid=%UID%\">consulta</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a class=\"annullaPrimaNota\" data-href=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_annulla.do?primaNota.uid=%UID%\">annulla</a></li>";
	private static final String AZIONI_CONSENTITE_COLLEGA = "<li><a class=\"collegaPrimaNota\">collega</a></li>";
	private static final String AZIONI_CONSENTITE_RATEI = "<li><a class=\"ratei\">rateo</a></li>";
	private static final String AZIONI_CONSENTITE_RISCONTI = "<li><a class=\"gestioneRisconti\">risconti</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA_RATEI = "<li><a class=\"rateiAggiornamento\">aggiorna rateo</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA_RISCONTI = "<li><a class=\"gestioneRisconti\">aggiorna risconti</a></li>";
	private static final String AZIONI_CONSENTITE_RATEI_DOCUMENTO = "<li><a class=\"ratei\" data-href-documento=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_gestioneRateiDocumento.do?primaNota.uid=%UID%\" data-uid-documento=\"%UID_DOCUMENTO%\">rateo</a></li>";
	private static final String AZIONI_CONSENTITE_RISCONTI_DOCUMENTO = "<li><a class=\"gestioneRisconti\" data-href-documento=\"risultatiRicercaPrimaNotaIntegrata%TYPE%_gestioneRiscontiDocumento.do?primaNota.uid=%UID%\" data-uid-documento=\"%UID_DOCUMENTO%\" >risconti</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul>"
			+ "</div>";
	
	/** La fase di bilancio */
	protected FaseBilancio faseBilancio;
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaPrimaNotaIntegrata req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaPrimaNotaIntegrata req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaPrimaNotaIntegrataResponse ottieniResponse(RicercaSinteticaPrimaNotaIntegrata req) {
		return primaNotaService.ricercaSinteticaPrimaNotaIntegrata(req);
	}

	@Override
	protected ListaPaginata<PrimaNota> ottieniListaRisultati(RicercaSinteticaPrimaNotaIntegrataResponse response) {
		return response.getPrimeNote();
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Lo imposto qui e non nel costruttore in quanto non ho ancora il wiring del sessionHandler
		faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoPrimaNotaIntegrata instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		
		final boolean gestioneAggiorna = gestisciAggiornamento(listaAzioniConsentite, instance);
		final boolean gestioneAnnulla = gestisciAnnullamento(listaAzioniConsentite, instance);
		final boolean gestioneCollega = gestisciCollegamento();
		final boolean gestioneConsulta = gestisciConsultazione(listaAzioniConsentite);
		final boolean gestioneValida = gestisciValidazione(listaAzioniConsentite, instance);
		final boolean gestioneInserisciRateo = gestisciInserisciRateo(listaAzioniConsentite, instance);
		final boolean gestioneInserisciRisconti = gestisciInserisciRisconti(listaAzioniConsentite, instance);
		final boolean gestioneAggiornaRateo = gestisciAggiornaRateo(listaAzioniConsentite, instance);
		final boolean gestioneAggiornaRisconti = gestisciAggiornaRisconti(listaAzioniConsentite, instance);
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);

		appendIfTrue(strAzioni, gestioneAggiorna, ottieniAzioneAggiornamento(instance));
		appendIfTrue(strAzioni, gestioneValida, AZIONI_CONSENTITE_VALIDA);
		appendIfTrue(strAzioni, gestioneConsulta, AZIONI_CONSENTITE_CONSULTA);
		appendIfTrue(strAzioni, gestioneAnnulla, AZIONI_CONSENTITE_ANNULLA);
		appendIfTrue(strAzioni, gestioneCollega, AZIONI_CONSENTITE_COLLEGA);
		appendIfTrue(strAzioni, gestioneInserisciRateo, ottieniAzioneRatei(instance));
		appendIfTrue(strAzioni, gestioneInserisciRisconti, ottieniAzioneRisconti(instance));
		appendIfTrue(strAzioni, gestioneAggiornaRateo, ottieniAzioneAggiornaRateo(instance));
		appendIfTrue(strAzioni, gestioneAggiornaRisconti, ottieniAzioneAggiornaRisconti(instance));
		appendIfTrue(strAzioni, true, AZIONI_CONSENTITE_END);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("%TYPE%", getCodiceAmbito());
		map.put("%UID%", instance.getUid() + "");
		map.put("%UID_DOCUMENTO%", ottieniIdDocumento(instance));

		String azioni = StringUtilities.replacePlaceholders(PATTERN, strAzioni.toString(), map, false);
		instance.setAzioni(azioni);
	}

	private String ottieniIdDocumento(ElementoPrimaNotaIntegrata instance) {
		Entita entita = instance.ottieniMovimentoAssociatoAPrimaNota();
		if(!isTipoEventoDocumento(instance) || ! (entita instanceof Subdocumento)) {
			return "";
		}
		Subdocumento<?, ?> subdocumento = (Subdocumento<?, ?>)entita;
		
		
		return subdocumento.getDocumento() != null?  subdocumento.getDocumento().getUid() + "" : "";
	}

	private String ottieniAzioneAggiornaRisconti(ElementoPrimaNotaIntegrata instance) {
		return isTipoEventoDocumento(instance)? AZIONI_CONSENTITE_RISCONTI_DOCUMENTO : AZIONI_CONSENTITE_AGGIORNA_RISCONTI;
	}

	private String ottieniAzioneRatei(ElementoPrimaNotaIntegrata instance) {
		return isTipoEventoDocumento(instance)? AZIONI_CONSENTITE_RATEI_DOCUMENTO : AZIONI_CONSENTITE_RATEI;
	}

	private String ottieniAzioneAggiornaRateo(ElementoPrimaNotaIntegrata instance) {
		return isTipoEventoDocumento(instance)? AZIONI_CONSENTITE_RATEI_DOCUMENTO : AZIONI_CONSENTITE_AGGIORNA_RATEI;
	}

	private String ottieniAzioneRisconti(ElementoPrimaNotaIntegrata instance) {
		return isTipoEventoDocumento(instance)? AZIONI_CONSENTITE_RISCONTI_DOCUMENTO : AZIONI_CONSENTITE_RISCONTI;
	}

	/**
	 * Ottiene il nome dell'azione per l'aggiornamento
	 * @param instance l'istanza da controllare
	 * @return il nome dell'azione
	 */
	protected String ottieniAzioneAggiornamento(ElementoPrimaNotaIntegrata instance) {
		
		return isTipoEventoDocumento(instance)? 
				AZIONI_CONSENTITE_AGGIORNA_EVENTO_DOCUMENTO
				: AZIONI_CONSENTITE_AGGIORNA;
	}

	/**
	 * @param instance
	 * @return
	 */
	private boolean isTipoEventoDocumento(ElementoPrimaNotaIntegrata instance) {
		String codiceTipoEvento = instance.getCodiceTipoEvento();
		return BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_ENTRATA.getConstant().equals(codiceTipoEvento) || BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_SPESA.getConstant().equals(codiceTipoEvento);
	}
	
	/**
	 * Controlla che la validazione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se la validazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciValidazione(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.CHIUSO)
			&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaValidaPrimaNotaIntegrata(), listaAzioniConsentite)
			&& StatoOperativoPrimaNota.PROVVISORIO.equals(instance.getStatoOperativoPrimaNota());
	}

	/**
	 * Controlla che la consultazione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciConsultazione(List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRicercaPrimaNotaIntegrata(), listaAzioniConsentite);

	}

	/**
	 * Controlla che l'annullamento sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciAnnullamento(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestisciPrimaNotaIntegrata(), listaAzioniConsentite)
				&& StatoOperativoPrimaNota.PROVVISORIO.equals(instance.getStatoOperativoPrimaNota());

	}
	
	/**
	 * Controlla che il collegamento sia eseguibile.
	 * 
	 * @return <code>true</code> se il collegamento &eacute; consentito; <code>false</code> altrimenti
	 */
	private boolean gestisciCollegamento() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.CHIUSO);

	}

	/**
	 * Controlla che l'aggiornamento sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciAggiornamento(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaAggiornaPrimaNotaIntegrata(), listaAzioniConsentite)
				&& isStatoCoerenteConAggiornamento(instance);
	}

	/**
	 * Controlla se lo stato sia coerente con l'aggiornamento
	 * @param instance l'istanza
	 * @return se lo stato &eacute; coerente
	 */
	protected abstract boolean isStatoCoerenteConAggiornamento(ElementoPrimaNotaIntegrata instance);
	
	/**
	 * Controlla che l'aggiornamento dei risconti sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> altrimenti
	 */
	protected abstract boolean gestisciAggiornaRisconti(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance);

	/**
	 * Controlla che l'aggiornamento del rateo sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> altrimenti
	 */
	protected abstract boolean gestisciAggiornaRateo(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) ;

	/**
	 * Controlla che l'inserimento di un risconto sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'inserimento &eacute; consentito; <code>false</code> altrimenti
	 */
	protected abstract boolean gestisciInserisciRisconti(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance);
	/**
	 * Controlla che l'inserimento di un rateo sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'inserimento &eacute; consentito; <code>false</code> altrimenti
	 */
	protected abstract boolean gestisciInserisciRateo(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance);

	/**
	 * @return il codice ambito dell'azione (FIN/GSA)
	 */
	protected abstract String getCodiceAmbito();
	/**
	 * @return l'azione consentita corrispondente alla validazione della prima nota integrata
	 */
	protected abstract AzioniConsentite getAzioneConsentitaValidaPrimaNotaIntegrata();
	/**
	 * @return l'azione consentita corrispondente alla ricerca della prima nota integrata
	 */
	protected abstract AzioniConsentite getAzioneConsentitaRicercaPrimaNotaIntegrata();
	/**
	 * @return l'azione consentita corrispondente alla gestione della prima nota integrata
	 */
	protected abstract AzioniConsentite getAzioneConsentitaGestisciPrimaNotaIntegrata();
	/**
	 * @return l'azione consentita corrispondente alla gestione di retei e risconti della prima nota integrata
	 */
	protected abstract AzioniConsentite getAzioneConsentitaRateiRisconti();
	// SIAC-4524
	/**
	 * @return l'azione consentita corrispondente all'aggiornamento della prima nota integrata
	 */
	protected abstract AzioniConsentite getAzioneConsentitaAggiornaPrimaNotaIntegrata();	
	
}
