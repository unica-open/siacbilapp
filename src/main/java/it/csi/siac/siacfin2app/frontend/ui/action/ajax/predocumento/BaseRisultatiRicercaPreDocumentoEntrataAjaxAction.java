/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.predocumento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.predocumento.RisultatiRicercaPreDocumentoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.predocumento.ElementoPreDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.predocumento.ElementoPreDocumentoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * Action per i risultati di ricerca del predocumento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 24/04/2014
 * 
 */
public abstract class BaseRisultatiRicercaPreDocumentoEntrataAjaxAction extends PagedDataTableAjaxAction<ElementoPreDocumento, 
	RisultatiRicercaPreDocumentoAjaxModel, PreDocumentoEntrata, RicercaSinteticaPreDocumentoEntrata, RicercaSinteticaPreDocumentoEntrataResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8131379785308656816L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right dropdown-menu-datatable' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA =
			"<li><a href='risultatiRicercaPreDocumentoEntrataAggiorna.do?uidDaAggiornare=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA =
			"<li><a href='#modaleAnnulla' data-toggle='modal' class='annullaPreDocumento'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA =
			"<li><a href='risultatiRicercaPreDocumentoEntrataConsulta.do?uidDaConsultare=%UID%'>consulta</a></li>";
	
	// SIAC-4675
	private static final String AZIONI_CONSENTITE_RIPETI =
			"<li><a href='risultatiRicercaPreDocumentoEntrataRipeti.do?uidDaAggiornare=%UID%'>ripeti</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient PreDocumentoEntrataService preDocumentoEntrataService;
	private boolean faseBilancioCoerente;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		inizializzazioneCoerenzaFaseBilancio();
	}

	/**
	 * La ricerca pu&ograve; abilitare i tasti funzione Aggiorna, e Annulla se il Bilancio &eacute; in una di queste fasi:
	 * <ul>
	 *     <li>Provvisorio</li>
	 *     <li>Gestione</li>
	 *     <li>Assestamento</li>
	 *     <li>Predisposizione a consuntivo</li>
	 * </ul>
	 */
	private void inizializzazioneCoerenzaFaseBilancio() {
		FaseBilancio faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		
		/*
		 * La ricerca puo' abilitare i tasti funzione Aggiorna, e Annulla se il Bilancio e' in una di queste fasi:
		 *     Provvisorio
		 *     Gestione
		 *     Assestamento
		 *     Predisposizione a consuntivo
		 */
		faseBilancioCoerente = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaPreDocumentoEntrata req) {
		return req.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaPreDocumentoEntrata req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
		
	}
	
	@Override
	protected ElementoPreDocumento getInstance(PreDocumentoEntrata e) throws FrontEndBusinessException {
		return ElementoPreDocumentoFactory.getInstance(e);
	}
	
	@Override
	protected RicercaSinteticaPreDocumentoEntrataResponse getResponse(RicercaSinteticaPreDocumentoEntrata req) {
		return preDocumentoEntrataService.ricercaSinteticaPreDocumentoEntrata(req);
	}
	
	@Override
	protected ListaPaginata<PreDocumentoEntrata> ottieniListaRisultati(RicercaSinteticaPreDocumentoEntrataResponse response) {
		return response.getPreDocumenti();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoPreDocumento instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		boolean isAggiornaConsentita = isAggiornaConsentita(listaAzioniConsentite, instance);
		boolean isAnnullaConsentita = isAnnullaConsentita(listaAzioniConsentite, instance);
		boolean isConsultaConsentita = isConsultaConsentita(listaAzioniConsentite);
		
		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		addIfConsentito(azioniBuilder, true, AZIONI_CONSENTITE_BEGIN);
		addIfConsentito(azioniBuilder, isAggiornaConsentita, AZIONI_CONSENTITE_AGGIORNA);
		addIfConsentito(azioniBuilder, isAnnullaConsentita, AZIONI_CONSENTITE_ANNULLA);
		addIfConsentito(azioniBuilder, isConsultaConsentita, AZIONI_CONSENTITE_CONSULTA);
		// SIAC-4675
		addIfConsentito(azioniBuilder, isRipetiConsentito(listaAzioniConsentite), AZIONI_CONSENTITE_RIPETI);
		addIfConsentito(azioniBuilder, true, AZIONI_CONSENTITE_END);
		
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
	/**
	 * Controlla se l'aggiornamento sia consentito
	 * @param listaAzioniConsentite le azioni consentite
	 * @param instance l'istanza da controllare
	 * @return se l'aggiornamento &eacute; consentito
	 */
	private boolean isAggiornaConsentita(List<AzioneConsentita> listaAzioniConsentite, ElementoPreDocumento instance) {
		return (AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_AGGIORNA, listaAzioniConsentite)
				|| AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite))
			&& !instance.isStatoOperativoAnnullato()
			&& !instance.isStatoOperativoDefinito()
			&& faseBilancioCoerente;
	}
	
	/**
	 * Controlla se l'annullamento sia consentito
	 * @param listaAzioniConsentite le azioni consentite
	 * @param instance l'istanza da controllare
	 * @return se l'annullamento &eacute; consentito
	 */
	private boolean isAnnullaConsentita(List<AzioneConsentita> listaAzioniConsentite, ElementoPreDocumento instance) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_AGGIORNA, listaAzioniConsentite)
			&& !AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_AGGIORNA_DECENTRATO, listaAzioniConsentite)
			&& !instance.isStatoOperativoAnnullato()
			&& !instance.isStatoOperativoDefinito()
			&& faseBilancioCoerente;
	}
	
	/**
	 * Controlla se la consultazione sia consentita
	 * @param listaAzioniConsentite le azioni consentite
	 * @return se la consultazione &eacute; consentita
	 */
	private boolean isConsultaConsentita(List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_CONSULTA, listaAzioniConsentite);
	}
	
	/**
	 * Controlla se la ripetizione sia consentita
	 * @param listaAzioniConsentite le azioni consentite
	 * @return se la ripetizione &eacute; consentita
	 */
	private boolean isRipetiConsentito(List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_INSERISCI, listaAzioniConsentite);
	}
}
