/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaAllegatoAttoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAllegatoAttoAjaxAction extends PagedDataTableAjaxAction<ElementoAllegatoAtto, 
		RisultatiRicercaAllegatoAttoAjaxModel, AllegatoAtto, RicercaAllegatoAtto, RicercaAllegatoAttoResponse> {
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = 6070112707026461016L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN =
			"<div class='btn-group'> " +
				"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
				"<ul class='dropdown-menu pull-right' >";
	private static final String AZIONI_CONSENTITE_COMPLETA_CON_VERIFICHE = 
			"<li><a href='#modaleCompletaAllegatoAtto' class='completaAllegatoAtto'>completa</a></li>";
	//SIAC-6775
	private static final String AZIONI_CONSENTITE_COMPLETA_BYPASS_VERIFICHE = 
			"<li><a href='#modaleCompletaAllegatoAtto' class='completaAllegatoAttoByPassVerifiche'>completa</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicercaAllegatoAtto_aggiorna.do?uidAllegatoAtto=%source.uid%'>aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#modaleAnnullaAllegatoAtto' class='annullaAllegatoAtto'>annulla</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href='risultatiRicercaAllegatoAtto_consulta.do?uidAllegatoAtto=%source.uid%'>consulta</a></li>";
	private static final String AZIONI_CONSENTITE_STAMPA =
			"<li><a href='risultatiRicercaAllegatoAtto_stampa.do?uidAllegatoAtto=%source.uid%'>stampa atto</a></li>";
	private static final String AZIONI_CONSENTITE_INVIA =
			"<li><a href='#' class='inviaAllegatoAtto'>invia per firma</a></li>";
	// SIAC-5170
	private static final String AZIONI_CONSENTITE_CONVALIDA =
			"<li><a href='risultatiRicercaAllegatoAtto_convalida.do?uidAllegatoAtto=%source.uid%' class='convalidaAllegatoAtto'>convalida</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ALLEGATO_ATTO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaAllegatoAtto request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaAllegatoAtto request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoAllegatoAtto getInstance(AllegatoAtto e) throws FrontEndBusinessException {
		return new ElementoAllegatoAtto(e);
	}
	
	@Override
	protected RicercaAllegatoAttoResponse getResponse(RicercaAllegatoAtto request) {
		return allegatoAttoService.ricercaAllegatoAtto(request);
	}
	
	@Override
	protected ListaPaginata<AllegatoAtto> ottieniListaRisultati(RicercaAllegatoAttoResponse response) {
		return response.getAllegatoAtto();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoAllegatoAtto instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		//SIAC-6775
		boolean inibisciVerificheSulCompletamento = gestisciCompletamento(instance, AzioneConsentitaEnum.ALLEGATO_ATTO_COMPLETA_SENZA_CONTROLLI);
		boolean gestioneCompletaStandard = gestisciCompletamento(instance, AzioneConsentitaEnum.ALLEGATO_ATTO_COMPLETA);
		boolean gestioneAggiorna = gestisciAggiornamento(instance);
		boolean gestioneAnnulla = gestisciAnnullamento(instance);
		boolean gestioneConsulta = gestisciConsultazione();
		
		// Lotto L
		boolean gestioneStampa = gestisciStampa(instance);
		boolean gestioneInvia = gestisciInvia(instance);
		// SIAC-5170
		boolean gestioneConvalida = gestisciConvalida(instance);
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
		
		//SIAC-6775: inibisco le veriche solo a quegli utenti che possono completare
		appendIfTrue(strAzioni, !inibisciVerificheSulCompletamento && gestioneCompletaStandard, AZIONI_CONSENTITE_COMPLETA_CON_VERIFICHE);
		appendIfTrue(strAzioni, inibisciVerificheSulCompletamento && gestioneCompletaStandard, AZIONI_CONSENTITE_COMPLETA_BYPASS_VERIFICHE);
		
		appendIfTrue(strAzioni, gestioneAggiorna, AZIONI_CONSENTITE_AGGIORNA);
		appendIfTrue(strAzioni, gestioneAnnulla, AZIONI_CONSENTITE_ANNULLA);
		appendIfTrue(strAzioni, gestioneConsulta, AZIONI_CONSENTITE_CONSULTA);
		//SIAC-8804
		appendIfTrue(strAzioni, gestioneStampa, AZIONI_CONSENTITE_STAMPA);
		appendIfTrue(strAzioni, gestioneInvia, AZIONI_CONSENTITE_INVIA);
		appendIfTrue(strAzioni, gestioneConvalida, AZIONI_CONSENTITE_CONVALIDA);
		
		strAzioni.append(AZIONI_CONSENTITE_END);
		String azioni = strAzioni.toString().replaceAll("%source.uid%", Integer.toString(instance.getUid()));
		instance.setAzioni(azioni);
	}
	
	/**
	 * Gestione del completamento: controlla se sia possibile completare l'allegato.
	 * @param instance l'istanza
	 * 
	 * @return <code>true</code> se l'allegato &eacute; completabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciCompletamento(ElementoAllegatoAtto instance, AzioneConsentitaEnum azioneCompletamento) {
		return AzioniConsentiteFactory.isConsentito(azioneCompletamento, sessionHandler.getAzioniConsentite())
				&& (StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(instance.getStatoOperativoAllegatoAtto()))
				&& instance.getIsAssociatoAdAlmenoUnDocumento();
	}

	/**
	 * Gestione dell'aggiornamento: controlla se sia possibile aggiornare l'allegato.
	 * 
	 * @param instance l'istanza del wrapper
	 * 
	 * @return <code>true</code> se l'allegato &eacute; aggiornabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciAggiornamento(ElementoAllegatoAtto instance) {
		// Abilitato per l’azione OP-COM-aggAttoAllegatoDec  e per allegati in stato 'DA COMPLETARE'
		boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_AGGIORNA_DECENTRATO, sessionHandler.getAzioniConsentite())
				&& StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(instance.getStatoOperativoAllegatoAtto());
		// Abilitato per l’azione OP-COM- aggAttoAllegatoCen e per allegati in stato diverso da 'ANNULLATO', 'RIFIUTATO', 'DA COMPLETARE', 
		boolean centrale = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_AGGIORNA_CENTRALE, sessionHandler.getAzioniConsentite())
				&& !StatoOperativoAllegatoAtto.ANNULLATO.equals(instance.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.RIFIUTATO.equals(instance.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(instance.getStatoOperativoAllegatoAtto());
		
		return decentrato || centrale;
	}

	/**
	 * Gestione dell'annullamento: controlla se sia possibile annullare l'allegato.
	 * 
	 * @param instance l'istanza del wrapper
	 * 
	 * @return <code>true</code> se l'allegato &eacute; annullabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciAnnullamento(ElementoAllegatoAtto instance) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_ANNULLA, sessionHandler.getAzioniConsentite())
			&& (StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(instance.getStatoOperativoAllegatoAtto()));
	}

	/**
	 * Gestione della consultazione: controlla se sia possibile consultare l'allegato.
	 * 
	 * @return <code>true</code> se l'allegato &eacute; consultabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciConsultazione() {
		// Posso sempre consultare
		return true;
	}
	
	/**
	 * Gestione della stampa: controlla se sia possibile stampare l'allegato.
	 * @param instance l'istanza del wrapper
	 * @return <code>true</code> se l'allegato &eacute; stampabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciStampa(ElementoAllegatoAtto instance) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_RICERCA, sessionHandler.getAzioniConsentite()) 
				&& instance.getIsAssociatoAdAlmenoUnaQuotaSpesa();
	}
	
	/**
	 * Gestione dell'invio: controlla se sia possibile inviare l'allegato per la firma.
	 * 
	 * @param instance l'istanza del wrapper
	 * @return <code>true</code> se l'allegato &eacute; stampabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciInvia(ElementoAllegatoAtto instance) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_INVIA, sessionHandler.getAzioniConsentite())
			&& (StatoOperativoAllegatoAtto.COMPLETATO.equals(instance.getStatoOperativoAllegatoAtto()) || StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(instance.getStatoOperativoAllegatoAtto()) || StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(instance.getStatoOperativoAllegatoAtto()))
			&& model.isIntegrazioneAttiLiquidazione() 
			&& instance.getIsAssociatoAdAlmenoUnaQuotaSpesa();
	}
	
	/**
	 * Gestione dell'invio: controlla se sia possibile convalidare l'allegato.
	 * <br/>
	 * Condizioni per l'attivazione:
	 * <br/>
	 * <ul>
	 *     <li>quando l'utente ha OP-COM-convDet</li>
	 *     <li>l'atto &eacute; in stato <strong>COMPLETATO</strong> o <strong>PARZIALMENTE CONVALIDATO</strong></li>
	 * </ul>
	 * 
	 * @param instance l'istanza del wrapper
	 * @return <code>true</code> se l'allegato &eacute; convalidabile; <code>false</code> in caso contrario
	 */
	private boolean gestisciConvalida(ElementoAllegatoAtto instance) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_CONVALIDA, sessionHandler.getAzioniConsentite())
				&& (StatoOperativoAllegatoAtto.COMPLETATO.equals(instance.getStatoOperativoAllegatoAtto()) || StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(instance.getStatoOperativoAllegatoAtto()));
	}
	
}