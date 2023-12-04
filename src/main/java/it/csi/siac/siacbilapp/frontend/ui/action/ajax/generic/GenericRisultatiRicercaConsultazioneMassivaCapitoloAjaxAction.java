/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.GenericTypeResolver;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaConsultazioneMassivaCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Action di base per i risultati di ricerca del capitolo, per la consultazione massiva.
 * 
 * @author LG, AM
 * 
 * @param <CAP> la parametrizzazione del Capitolo 
 * @param <CM>  la parametrizzazione del Capitolo Massivo
 * @param <REQ> la parametrizzazione della Request
 * @param <RES> la parametrizzazione della Response
 * 
 */
public abstract class GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction<CAP extends Capitolo<?, ?>, CM extends CAP, REQ extends ServiceRequest, RES extends ServiceResponse> extends 
	GenericBilancioAction<RisultatiRicercaConsultazioneMassivaCapitoloAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4981255654657737076L;
	
	/** 
	 * &Eacute; il numero di elementi che vengono visualizzati in una pagina di risultati. 
	 * &Eacute; anche il numero di elementi che passano dal server al client.
	 */
	protected static final int ELEMENTI_PER_PAGINA = 10;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> "+
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_AGGIORNA =
			"<li><a href='aggiornaCap%actionName%.do?uidDaAggiornare=%source.uid%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA =
			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = 
			"<li><a href='#msgElimina' data-toggle='modal'>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a href='consultaCap%actionName%.do?uidDaConsultare=%source.uid%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	/** Lista di utilit&agrave;. */
	private List<CAP> listaRisultati;
	
	private String nomeAzione;
	private Class<REQ> requestClass;
	
	/**
	 * @param nomeAzione the nomeAzione to set
	 */
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		if(requestClass == null) {
			impostaRequestClass();
		}
	}
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Valuta la lista delle azioni consentite per
		// costruire il pannello di operazioni utente sul capitolo 
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		AzioniConsentiteWrapper wpAzioniConsentite = ottieniWrapperAzioniConsentite(listaAzioniConsentite);
		
		boolean isAggiornaAbilitato = wpAzioniConsentite.isAggiornaAbilitato();
		// Consulta è abilitato sempre!
		boolean isConsultaAbilitato = wpAzioniConsentite.isConsultaAbilitato();
		boolean isEliminaAbilitato = wpAzioniConsentite.isEliminaAbilitato();
		boolean isAnnullaAbilitato = wpAzioniConsentite.isAnnullaAbilitato();
		
		// Gestione eventuale rientro da Elimina, Annulla, Aggiorna
		boolean bIsRientro = false;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO) != null && "S".equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO))) {
			bIsRientro = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO,null);
		}
		
		log.debug(methodName, "Rientro da ANNULLA, ELIMINA o AGGIORNA? " + bIsRientro);
		log.debug(methodName, "Carico la lista dalla sessione");
		listaRisultati = sessionHandler.getParametro(BilSessionParameter.LISTA_UEB_COLLEGATE);
		
		log.debug(methodName, "Carico la request dalla sessione");
		REQ request = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DETTAGLIO_CAPITOLO_MASSIVO, requestClass);
		List<ElementoCapitolo> aaData = new ArrayList<ElementoCapitolo>();
		// Comincio da 1 nel caso in cui non sia da rientro
		int inizio = bIsRientro ? 0 : 1;
		int fine = ELEMENTI_PER_PAGINA;
		
		inizio = Integer.valueOf(model.getiDisplayStart());
		fine = inizio + Integer.valueOf(model.getiDisplayLength());
			
		log.debug(methodName, "inizio = " + inizio);
		log.debug(methodName, "fine = " + fine);
		
		// Nel caso del rientro, devo invocare il servizio
		int totRec = 0;
		if(bIsRientro) {
			RES response = ottieniResponse(request);
			CM capitoloMassivo = ottieniCapitoloMassivo(response);
			listaRisultati = ottieniListaRisultatiDaMassivo(capitoloMassivo);
			if(!response.hasErrori()) {
				Integer startPosition = Integer.valueOf(inizio);
				sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START_CONSULTAZIONE, startPosition);
				log.debug(methodName,"Salvo startPosition = " + startPosition);
			}
		}
		
		totRec = listaRisultati.size();
		model.setiTotalRecords(totRec);
		model.setiTotalDisplayRecords(model.getiTotalRecords());
		
		log.debug(methodName, "Carica aaData");
		caricaLista(aaData, inizio, fine, totRec, isAggiornaAbilitato, isAnnullaAbilitato, isEliminaAbilitato, isConsultaAbilitato);

		log.debug(methodName, "iTotalRecords = " + model.getiTotalRecords());
		log.debug(methodName, "iTotalDisplayRecords = " + model.getiTotalDisplayRecords());
		log.debug(methodName, "iDisplayLength = " + model.getiDisplayLength());
		log.debug(methodName, "iDisplayStart = " + model.getiDisplayStart());

		log.debug(methodName, "aaData.size() = " + aaData.size());
		model.setAaData(aaData);
			
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento della lista. 
	 * 
	 * @param listaDaCaricare   la lista da caricare con i dati
	 * @param inizio            la posizione di inizio
	 * @param fine              la posizione di fine
	 * @param totaleRecord      il totale dei record nella lista
	 * @param aggiornaAbilitato se l'aggioramento &eacute; abilitato
	 * @param annullaAbilitato  se l'annullamento &eacute; abilitato
	 * @param eliminaAbilitato  se l'eliminazione &eacute; abilitata
	 * @param consultaAbilitato se la consultazione &eacute; abilitata
	 * 
	 */
	private void caricaLista(List<ElementoCapitolo> listaDaCaricare, int inizio, int fine, int totaleRecord, 
			boolean aggiornaAbilitato, boolean annullaAbilitato, boolean eliminaAbilitato, boolean consultaAbilitato) {
		for(int k = inizio; k < fine && k < totaleRecord; k++) {
			ElementoCapitolo el = ElementoCapitoloFactory.getInstance(listaRisultati.get(k), false, model.isGestioneUEB());
			
			// Caricamento delle azioni consentite
			StringBuilder strAzioniSB = new StringBuilder().append(AZIONI_CONSENTITE_BEGIN);
			if (aggiornaAbilitato) {
				strAzioniSB.append(AZIONI_CONSENTITE_AGGIORNA);
			}
			if (annullaAbilitato && !"ANNULLATO".equals(el.getStato())) {
				strAzioniSB.append(AZIONI_CONSENTITE_ANNULLA);
			}
			if (eliminaAbilitato) {
				strAzioniSB.append(AZIONI_CONSENTITE_ELIMINA);
			}
			if (consultaAbilitato) {
				strAzioniSB.append(AZIONI_CONSENTITE_CONSULTA);
			}
			strAzioniSB.append(AZIONI_CONSENTITE_END);
			
			// Sostituisco i valori delle azioni e dell'uid
			String strAzioni = rimpiazzaNomeAzione(strAzioniSB.toString());
			strAzioni = strAzioni.replaceAll("%source.uid%", Integer.toString(el.getUid()));

			el.setAzioni(strAzioni);
			
			// Carico i valori in lista
			listaDaCaricare.add(el);
		}
	}
	
	/**
	 * Imposta la classe di request a partire dal Generics.
	 */
	@SuppressWarnings("unchecked")
	private void impostaRequestClass() {
		Class<?>[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction.class);
		Class<REQ> clazz = (Class<REQ>) genericTypeArguments[2];
		this.requestClass = clazz;
	}
	
	/**
	 * Ottiene il wrapper delle azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista da wrappare
	 * 
	 * @return il wrapper
	 */
	protected AzioniConsentiteWrapper ottieniWrapperAzioniConsentite(List<AzioneConsentita> listaAzioniConsentite) {
		return new AzioniConsentiteWrapper(nomeAzione, listaAzioniConsentite);
	}
	
	/**
	 * Rimpiazza il nome dell'azione con il nome concreto.
	 * 
	 * @param strAzioni la stringa in cui effettuare la sostituzione
	 * 
	 * @return la stringa con il valore sostituito
	 */
	protected String rimpiazzaNomeAzione(String strAzioni) {
		return strAzioni.replaceAll("%actionName%", nomeAzione);
	}
	
	/**
	 * Ottiene la response del servizio di ricerca sintetica.
	 * 
	 * @param request la request tramite cui ottenere la response 
	 * 
	 * @return la response creata
	 */
	protected abstract RES ottieniResponse(REQ request);
	
	/**
	 * Ottiene il capitolo massivo.
	 * 
	 * @param response la response da cui ottenere il capitolo massivo
	 * 
	 * @return il capitolo massivo
	 */
	protected abstract CM ottieniCapitoloMassivo(RES response);
	
	/**
	 * Ottiene la lista dei risultati dal capitolo massivo.
	 * 
	 * @param capitoloMassivo il capitolo massivo da cui ottenere la lista
	 * 
	 * @return la lista
	 */
	protected abstract List<CAP> ottieniListaRisultatiDaMassivo(CM capitoloMassivo);
	
}
