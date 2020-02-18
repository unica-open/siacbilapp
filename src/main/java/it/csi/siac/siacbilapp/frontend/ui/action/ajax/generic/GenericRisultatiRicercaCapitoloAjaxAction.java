/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.WrapperAzioniConsentite;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Action generica per i risultati di ricerca del capitolo
 * 
 * @author LG, AM
 * 
 * @param <CAP> la parametrizzazione del Capitolo 
 * @param <REQ> la parametrizzazione della Request
 * @param <RES> la parametrizzazione della Response
 * 
 */
public abstract class GenericRisultatiRicercaCapitoloAjaxAction<CAP extends Capitolo<?, ?>, REQ extends ServiceRequest, RES extends ServiceResponse> extends GenericRisultatiRicercaAjaxAction<ElementoCapitolo, RisultatiRicercaCapitoloAjaxModel, CAP, REQ, RES> {	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1423646045701404359L;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> "+
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA = 
			"<li><a href='risultatiRicerca%Massiva%Cap%actionName%Aggiorna.do?uidDaAggiornare=%source.uid%%altriParametri%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = 
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = 
			"<li><a href='#msgElimina' data-toggle='modal'>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a href='risultatiRicerca%Massiva%Cap%actionName%Consulta.do?uidDaConsultare=%source.uid%%altriParametri%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";

	private String nomeAzione;
	
	/**
	 * @param nomeAzione the nomeAzione to set
	 */
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}

	/* ********** Esecuzione del servizio ********** */
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if ("S".equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}
	
	@Override
	protected int ottieniValoreInizio(boolean daRientro) {
		// Se sono rientrato, inizio da 1 e non da 0
		return daRientro ? 0 : 1;
	}
	
	@Override
	protected int ottieniFromIndex(int bloccoNumero, int inizio, boolean daRientro, final int maxElementiRicercaService) {
		return daRientro ? inizio : (inizio - (bloccoNumero) * maxElementiRicercaService);
	}
	
	@Override
	protected int ottieniToIndex(int bloccoNumero, int fine, boolean daRientro, final int maxElementiRicercaService) {
		return daRientro ? fine : (fine - (bloccoNumero) * maxElementiRicercaService);
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoCapitolo instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		boolean gestioneAggiorna = gestisciAggiornamento(isAggiornaAbilitato);
		boolean gestioneAnnulla = gestisciAnnullamento(daRientro, isAnnullaAbilitato, instance);
		boolean gestioneElimina = gestisciEliminazione(isEliminaAbilitato);
		boolean gestioneConsulta = gestisciConsultazione(isConsultaAbilitato);
		StringBuilder strAzioniSB = new StringBuilder().append(AZIONI_CONSENTITE_BEGIN);
		if (gestioneConsulta) {
			String azioneConsulta = AZIONI_CONSENTITE_CONSULTA;
			azioneConsulta = gestisciAzioneConsulta(azioneConsulta, instance);
			strAzioniSB.append(azioneConsulta);
		}		
		if (gestioneAggiorna) {
			String azioneAggiorna = AZIONI_CONSENTITE_AGGIORNA;
			azioneAggiorna = gestisciAzioneAggiorna(azioneAggiorna, instance);
			strAzioniSB.append(azioneAggiorna);
		}
		if (gestioneElimina) {
			strAzioniSB.append(AZIONI_CONSENTITE_ELIMINA);
		}
		if (gestioneAnnulla) {
			strAzioniSB.append(AZIONI_CONSENTITE_ANNULLA);
		}
		strAzioniSB.append(AZIONI_CONSENTITE_END);
		String strAzioni = strAzioniSB.toString().replaceAll("%actionName%", nomeAzione);
		strAzioni = strAzioni.replaceAll("%source.uid%", Integer.toString(instance.getUid()));
		instance.setAzioni(strAzioni);
	}
	
	/**
	 * Costruisce il wrapper delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista da wrappare
	 * 
	 * @return il wrapper creato
	 */
	@Override
	protected WrapperAzioniConsentite ottieniWrapperAzioniConsentite(List<AzioneConsentita> listaAzioniConsentite) {
		return new WrapperAzioniConsentite(nomeAzione, listaAzioniConsentite);
	}
	
	@Override
	protected ElementoCapitolo ottieniIstanza(CAP cap) throws FrontEndBusinessException {
		return ElementoCapitoloFactory.getInstance(cap, false, model.isGestioneUEB());
	}
	
	/**
	 * Definisce se gestire l'aggiornamento.
	 * 
	 * @param isAggiornaAbilitato se l'aggiornamento sia abilitato
	 *  
	 * @return <code>true</code> se l'azione di aggiornamento &eacute; da gestire; <code>false</code> in caso contrario
	 */
	private boolean gestisciAggiornamento(boolean isAggiornaAbilitato) {
		return isAggiornaAbilitato;
	}
	
	/**
	 * Definisce se gestire l'annullamento.
	 * <br>
	 * Da overridare nel caso massivo.
	 * 
	 * @param bIsRientro         se si &eacute; rientrati nella pagina
	 * @param isAnnullaAbilitato se l'annullamento sia abilitato
	 * @param el                 l'elemento da gestire
	 * 
	 * @return <code>true</code> se l'azione di annullamento &eacute; da gestire; <code>false</code> in caso contrario
	 */
	protected boolean gestisciAnnullamento(boolean bIsRientro, boolean isAnnullaAbilitato, ElementoCapitolo el) {
		return (bIsRientro || isAnnullaAbilitato) && !"ANNULLATO".equals(el.getStato());
	}
	
	/**
	 * Definisce se gestire l'eliminazione.
	 * <br>
	 * Da overridare nel caso massivo.
	 * 
	 * @param isEliminaAbilitato se l'eliminazione sia abilitata
	 * 
	 * @return <code>true</code> se l'azione di eliminazione &eacute; da gestire; <code>false</code> in caso contrario
	 */
	protected boolean gestisciEliminazione(boolean isEliminaAbilitato) {
		return isEliminaAbilitato;
	}
	
	/**
	 * Definisce se gestire la consultazione.
	 * 
	 * @param isConsultaAbilitato se la consultazione sia abilitata
	 *  
	 * @return <code>true</code> se l'azione di consultazione &eacute; da gestire; <code>false</code> in caso contrario
	 */
	protected boolean gestisciConsultazione(boolean isConsultaAbilitato) {
		return isConsultaAbilitato;
	}
	
	/**
	 * Ottiene l'azione di aggiornamento.
	 * <br>
	 * Da overridare nel caso dei risultati della ricerca massiva.
	 * 
	 * @param azioneAggiorna l'azione da gestire
	 * @param el             il wrapper del capitolo
	 * 
	 * @return l'azione gestita
	 */
	protected String gestisciAzioneAggiorna(String azioneAggiorna, ElementoCapitolo el) {
		return azioneAggiorna.replaceAll("%altriParametri%", "")
			.replace("%Massiva%", "");
	}
	
	/**
	 * Ottiene l'azione di consultazione.
	 * <br>
	 * Da overridare nel caso dei risultati della ricerca massiva.
	 * 
	 * @param azioneConsulta l'azione da gestire
	 * @param el             il wrapper del capitolo
	 * 
	 * @return l'azione gestita
	 */
	protected String gestisciAzioneConsulta(String azioneConsulta, ElementoCapitolo el) {
		return azioneConsulta.replaceAll("%altriParametri%", "")
			.replace("%Massiva%", "");
	}
	
	@Override
	protected boolean controllaDatiReperiti(ListaPaginata<CAP> lista) {
		return lista.getTotaleElementi() > 0;
	}
	
}