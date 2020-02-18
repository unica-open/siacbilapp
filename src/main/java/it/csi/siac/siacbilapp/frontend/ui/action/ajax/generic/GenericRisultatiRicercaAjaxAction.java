/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.GenericTypeResolver;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.WrapperAzioniConsentite;
import it.csi.siac.siaccommonapp.handler.session.SessionParameter;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe generica di action per la gestione delle interazioni AJAX dei risultati di ricerca.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/01/2014
 *
 * @param <S>   la parametrizzazione del Wrapper
 * @param <M>   la parametrizzazione del Model
 * @param <E>   la parametrizzazione dell'Entita ottenuta dalla ricerca
 * @param <REQ> la parametrizzazione della Request
 * @param <RES> la parametrizzazione della Response
 */
public abstract class GenericRisultatiRicercaAjaxAction<S extends Serializable, M extends GenericRisultatiRicercaAjaxModel<S>, 
	E extends Entita, REQ extends ServiceRequest, RES extends ServiceResponse> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 33835391696744127L;
	
	/** Lista di utilit&agrave;. */
	private ListaPaginata<E> listaRisultati;
	private SessionParameter parametroSessioneLista;
	private SessionParameter parametroSessioneRequest;
	private Class<REQ> requestClass;

	/** &Eacute; il numero di elementi che vengono richiesti al servizio ad ogni chiamata. */
	protected static final int MAX_ELEMENTI_RICERCA_SERVICE_DEFAULT = GenericBilancioModel.ELEMENTI_PER_PAGINA_RICERCA;
	/** Il numero di pagina iniziale */
	protected int numeroPaginaIniziale = 0;
	
	/**
	 * @param parametroSessioneLista the parametroSessioneLista to set
	 */
	public void setParametroSessioneLista(SessionParameter parametroSessioneLista) {
		this.parametroSessioneLista = parametroSessioneLista;
	}

	/**
	 * @param parametroSessioneRequest the parametroSessioneRequest to set
	 */
	public void setParametroSessioneRequest(SessionParameter parametroSessioneRequest) {
		this.parametroSessioneRequest = parametroSessioneRequest;
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
		
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		WrapperAzioniConsentite wpAzioniConsentite = ottieniWrapperAzioniConsentite(listaAzioniConsentite);
		
		boolean isAggiornaAbilitato = wpAzioniConsentite.isAggiornaAbilitato();
		boolean isConsultaAbilitato = wpAzioniConsentite.isConsultaAbilitato();
		boolean isEliminaAbilitato = wpAzioniConsentite.isEliminaAbilitato();
		boolean isAnnullaAbilitato = wpAzioniConsentite.isAnnullaAbilitato();
		
		log.debug(methodName, "Invocazione del metodo per l'ottenimento della lista paginata");
		
		boolean daRientro = controllaDaRientro();
		log.debug(methodName, "Da rientro? " + daRientro);
		
		log.debug(methodName, "Carico la lista dalla sessione");
		listaRisultati = ottieniListaRisultatiDallaSessione();
		
		log.debug(methodName, "Carico la request dalla sessione");
		// Workaround per la non-serializzabilita' delle requests
		REQ req = ottieniRequestDallaSessione();
		
		log.debug(methodName, "Ottengo il numero del blocco dai parametri di Paginazione");
		ParametriPaginazione parametriPaginazione = ottieniParametriDiPaginazione(req);
		int bloccoNumero = ottieniBloccoNumero(parametriPaginazione);
		
		// Valori di default
		final int elementiPerPagina = model.getiDisplayLength() != 0 ? model.getiDisplayLength() : getElementiPerPaginaDefault();
		final int maxElementiRicercaService = parametriPaginazione.getElementiPerPagina() != 0 ? parametriPaginazione.getElementiPerPagina() : MAX_ELEMENTI_RICERCA_SERVICE_DEFAULT;
		
		List<S> aaData = new ArrayList<S>();

		log.debug(methodName, "iTotalRecords = " + model.getiTotalRecords());
		log.debug(methodName, "iTotalDisplayRecords = " + model.getiTotalDisplayRecords());
		log.debug(methodName, "iDisplayLength() = " + model.getiDisplayLength());
		log.debug(methodName, "iDisplayStart() = " + model.getiDisplayStart());
		log.debug(methodName, "bloccoNumero = " + bloccoNumero);
		
		int inizio = ottieniValoreInizio(daRientro);
		int fine = elementiPerPagina;
		
		inizio = model.getiDisplayStart();
		fine = inizio + model.getiDisplayLength();
		
		log.debug(methodName, "inizio = " + inizio);
		log.debug(methodName, "fine = " + fine);
		
		String risCall = SUCCESS;
		
		boolean richiamareServizio = daRientro;
		
		int nuovoBlocco = inizio / maxElementiRicercaService;
		if(nuovoBlocco != bloccoNumero){
			bloccoNumero = nuovoBlocco;
			richiamareServizio = true;
		}
		
		if(listaRisultati == null) {
			richiamareServizio = true;
		}
		
		if(model.isForceRefresh() || richiamareServizio) {
			log.debug(methodName, "Richiamo il metodo per ottenere il nuovo blocco. bloccoNumero = " + bloccoNumero);
			risCall = getBlocco(bloccoNumero, req, parametriPaginazione, maxElementiRicercaService);
			log.debug(methodName, "risCall() = " + risCall);
		}
		
		if(!risCall.equals(SUCCESS)) {
			// L'invocazione non ha avuto esito positivo. Impostare a zero il numero dei risultati totali
			model.setiTotalRecords(0);
			model.setiTotalDisplayRecords(model.getiTotalRecords());
		} else {
			int totrec = ottieniTotaleRecords(listaRisultati);
			model.setiTotalRecords(totrec);
			model.setiTotalDisplayRecords(model.getiTotalRecords());
			
			int fromIndex = ottieniFromIndex(bloccoNumero, inizio, daRientro, maxElementiRicercaService);
			int toIndex = ottieniToIndex(bloccoNumero, fine, daRientro, maxElementiRicercaService);
			
			log.debug(methodName, "fromIndex() = " + fromIndex + ", toIndex() = " + toIndex);
			
			if(toIndex > listaRisultati.size()) {
				toIndex = fromIndex + elementiPerPagina;
				if (toIndex > listaRisultati.size()) {
					toIndex = listaRisultati.size();
				}

				log.debug(methodName, "fromIndex() = " + fromIndex + ", toIndex() = " + toIndex);
			}
				
			log.debug(methodName, "Carica aaData : blocco = " + bloccoNumero + ", dal record " + (fromIndex + 1) + " al record " + toIndex);
			for(int k = fromIndex; k < toIndex && k < totrec; k++) {
				S instance = ottieniIstanza(listaRisultati.get(k));
				gestisciAzioniConsentite(instance, daRientro, isAggiornaAbilitato, isAnnullaAbilitato, isConsultaAbilitato, isEliminaAbilitato);
				aaData.add(instance);
			}
			
			Integer startPosition = Integer.valueOf(inizio);
			if(isAggiornareRientroPosizioneStart()) {
				log.debug(methodName, "Salvo startPosition = " + startPosition);
				sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, startPosition);
			}

		}
		log.debug(methodName, "iTotalRecords = " + model.getiTotalRecords());
		log.debug(methodName, "iTotalDisplayRecords = " + model.getiTotalDisplayRecords());
		log.debug(methodName, "iDisplayLength = " + model.getiDisplayLength());
		log.debug(methodName, "iDisplayStart = " + model.getiDisplayStart());

		log.debug(methodName, "aaData.size() = " + aaData.size());
		model.setAaData(aaData);
		
		eseguiOperazioniUlteriori();
		
		return SUCCESS;
	}
	
	/**
	 * Chiama il servizio per recuperare il blocco ennesimo (quello successivo o il precedente).
	 * Il risultato lo rimette in sessione nel model.
	 * 
	 * @param numeroBlocco              numero del blocco
	 * @param req                       la request da invocare
	 * @param parametriPaginazione      i parametri di paginazione
	 * @param maxElementiRicercaService il numero massimo di elementi del servizio
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 * 
	 * @author LG, AM, DL
	 *
	 */
	private String getBlocco(int numeroBlocco, REQ req, ParametriPaginazione parametriPaginazione, final int maxElementiRicercaService) {
		final String methodName = "getBlocco";
		log.debugStart(methodName, "Numero blocco: " + numeroBlocco);
		
		parametriPaginazione.setNumeroPagina(numeroBlocco+numeroPaginaIniziale);
		parametriPaginazione.setElementiPerPagina(maxElementiRicercaService);
		
		impostaParametriPaginazione(req, parametriPaginazione);
		
		impostaLaPaginaRemote(req);
		
		/* Richiamo il servizio di Ricerca Sintetica */		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RES response = ottieniResponse(req);
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		listaRisultati = ottieniListaRisultati(response);
		

		boolean controlloDatiReperiti = controllaDatiReperiti(listaRisultati);
		
		if(!controlloDatiReperiti) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return SUCCESS;
		}
		
		// Injetto il wrapper in sessione
		impostaLaRequestInSessione(req);
		impostaIRisultatiDiRicercaInSessione(listaRisultati);
		
		eseguiOperazioniUlterioriSuResponse(response);
		
		return SUCCESS;
	}
	
	/**
	 * Imposta la classe di request a partire dal Generics.
	 */
	@SuppressWarnings("unchecked")
	private void impostaRequestClass() {
		Class<?>[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), GenericRisultatiRicercaAjaxAction.class);
		Class<REQ> clazz = (Class<REQ>) genericTypeArguments[3];
		this.requestClass = clazz;
	}
	
	/**
	 * Ottiene la lista dei Risultati della ricerca dalla sessione.
	 * 
	 * @return la lista dei risultati di ricerca
	 */
	protected ListaPaginata<E> ottieniListaRisultatiDallaSessione() {
		return sessionHandler.getParametro(parametroSessioneLista);
	}
	
	/**
	 * Ottiene la request della ricerca dalla sessione.
	 * 
	 * @return la request
	 */
	protected REQ ottieniRequestDallaSessione() {
		return sessionHandler.getParametroXmlType(parametroSessioneRequest, requestClass);
	}
	
	/**
	 * Imposta la request per la ricerca in sessione.
	 * 
	 * @param req la request da impostare
	 */
	protected void impostaLaRequestInSessione(REQ req) {
		sessionHandler.setParametroXmlType(parametroSessioneRequest, req);
	}
	
	/**
	 * Imposta i risultati della ricerca in sessione
	 * 
	 * @param listaRisultatiDaInjettare i risultati da injettare
	 */
	protected void impostaIRisultatiDiRicercaInSessione(ListaPaginata<E> listaRisultatiDaInjettare) {
		sessionHandler.setParametro(parametroSessioneLista, listaRisultatiDaInjettare);
	}
	
	/**
	 * Ottiene il numero del blocco.
	 * 
	 * @param parametriPaginazione i parametri di paginazione
	 * 
	 * @return il numero del blocco
	 */
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return parametriPaginazione.getNumeroPagina();
	}

	/**
	 * Ottiene il valore di inizio degli elementi
	 * 
	 * @param daRientro se si stia arrivando dal rientro
	 * 
	 * @return il valore iniziale
	 */
	protected int ottieniValoreInizio(boolean daRientro) {
		return 1;
	}
	
	/**
	 * Ottiene il numero totale dei records nella lista.
	 * 
	 * @param listaRisultatiDaCuiOttenereIlTotale la lista da cui ottenere il numero dei records.
	 * 
	 * @return il numero totale dei records
	 */
	protected int ottieniTotaleRecords(ListaPaginata<E> listaRisultatiDaCuiOttenereIlTotale) {
		return listaRisultatiDaCuiOttenereIlTotale.getTotaleElementi();
	}
	
	/**
	 * Impostazione della pagina remota.
	 * 
	 * @param req la request in cui impostare il dato
	 */
	protected void impostaLaPaginaRemote(REQ req) {
		// Implementazione vuota, da overridare se necessario
	}
	
	/**
	 * Controlla se si sia arrivati alla Action da un rientro da altra pagina.
	 * 
	 * @return <code>true</code> se si &eacute; rientrati nella pagina; <code>false</code> altrimenti
	 */
	protected boolean controllaDaRientro() {
		return false;
	}

	/**
	 * Gestisci l'elenco delle azioni consentite, se applicabile.
	 * 
	 * @param instance            l'istanza rispetto cui gestire le azioni consentite
	 * @param daRientro           se si stia arrivando dal rientro
	 * @param isAggiornaAbilitato se l'aggiornamento sia abilitato
	 * @param isAnnullaAbilitato  se l'annullamento sia abilitato
	 * @param isConsultaAbilitato se la consultazione sia abilitata
	 * @param isEliminaAbilitato  se l'eliminazione sia abilitata
	 */
	protected void gestisciAzioniConsentite(S instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, 
			boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		// Implementazione vuota, da overridare se necessario
	}
	
	/**
	 * Controlla che i dati siano stati reperiti.
	 * 
	 * @param lista la lista dei risultati
	 * 
	 * @return <code>true</code> se vi sono dei dati, false in caso contrario
	 */
	protected boolean controllaDatiReperiti(ListaPaginata<E> lista) {
		return !listaRisultati.isEmpty();
	}
	
	/**
	 * Ottiene l'indice da cui partire.
	 * 
	 * @param bloccoNumero              il numero del blocco
	 * @param inizio                    il primo elemento
	 * @param daRientro                 se si stia arrivando dal rientro
	 * @param maxElementiRicercaService il numero massimo di elementi del servizio
	 * 
	 * @return l'indice di partenza
	 */
	protected int ottieniFromIndex(int bloccoNumero, int inizio, boolean daRientro, final int maxElementiRicercaService) {
		return inizio - ((bloccoNumero) * maxElementiRicercaService);
	}
	
	/**
	 * Ottiene l'indice a cui arrivare.
	 * 
	 * @param bloccoNumero              il numero del blocco
	 * @param fine                      l'ultimo elemento
	 * @param daRientro                 se si stia arrivando dal rientro
	 * @param maxElementiRicercaService il numero massimo di elementi del servizio
	 * 
	 * @return l'indice di arrivo
	 */
	protected int ottieniToIndex(int bloccoNumero, int fine, boolean daRientro, final int maxElementiRicercaService) {
		return fine - ((bloccoNumero) * maxElementiRicercaService);
	}
	
	/**
	 * Ottiene il wrapper per le azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return il wrapper
	 */
	protected WrapperAzioniConsentite ottieniWrapperAzioniConsentite(List<AzioneConsentita> listaAzioniConsentite) {
		return new WrapperAzioniConsentite();
	}
	
	/**
	 * Esecuzione di ulteriori operazioni sulla response del servizio.
	 * 
	 * @param response la response tramite cui effettuare ulteriori informazioni
	 */
	protected void eseguiOperazioniUlterioriSuResponse(RES response) {
		// Implementazione di default vuota
	}
	
	/**
	 * Esecuzione di ulteriori operazioni a termine della chiamata.
	 */
	protected void eseguiOperazioniUlteriori() {
		// Implementazione di dafault vuota
	}
	
	/* Metodi da implementare */
	
	/**
	 * Ottiene i parametri di paginazione dalla request.
	 * 
	 * @param req la request da cui ottenere i parametri
	 * 
	 * @return i parametri di paginazione
	 */
	protected abstract ParametriPaginazione ottieniParametriDiPaginazione(REQ req);
	
	/**
	 * Imposta i parametri di paginazione.
	 * 
	 * @param req              la request in cui injettare i parametri
	 * @param parametriPaginazione i parametri da injettare
	 */
	protected abstract void impostaParametriPaginazione(REQ req, ParametriPaginazione parametriPaginazione);
	
	/**
	 * Ottiene un'istanza del wrapper dall'Entita.
	 * 
	 * @param e l'Entita
	 * 
	 * @return il wrapper
	 * @throws FrontEndBusinessException nel caso in cui non sia possibile creare l'istanza
	 */
	protected abstract S ottieniIstanza(E e) throws FrontEndBusinessException;
	
	/**
	 * Ottiene la response di ricerca sintetica dal servizio.
	 * 
	 * @param req la request da utilizzare
	 * 
	 * @return la response
	 */
	protected abstract RES ottieniResponse(REQ req);
	
	/**
	 * Ottiene la lista dei risultati dalla response del servizio di ricerca sintetica.
	 * 
	 * @param response la response
	 * 
	 * @return la lista dei risultati
	 */
	protected abstract ListaPaginata<E> ottieniListaRisultati(RES response);
	
	/* Utilities */
	
	/**
	 * Ottiene una lista paginata a partire da una lista normale, dal numero della pagina e dal numero dei risultati totali.
	 * @param <T> il tipo
	 * 
	 * @param list            la lista da paginare
	 * @param numeroPagina    il numero della pagina
	 * @param numeroRisultati il numero dei risultati
	 * 
	 * @return il wrapper come lista paginata
	 */
	protected <T> ListaPaginata<T> toListaPaginata(List<T> list, int numeroPagina, int numeroRisultati) {
		ListaPaginataImpl<T> listaPaginata = new ListaPaginataImpl<T>();
		if(list != null) {
			listaPaginata.addAll(list);
		}
		
		// Basta?
		listaPaginata.setTotaleElementi(numeroRisultati);
		
		return listaPaginata;
	}
	
	/**
	 * Controlla se la fase di bilancio appartiene alle fasi fornite.
	 * 
	 * @param faseBilancio la fase da validare
	 * @param fasiBilancio le fasi da controllare
	 * 
	 * @return <code>true</code> se la fase fa parte dell'elenco; <code>false</code> altrimenti
	 */
	protected boolean faseBilancioInValues(FaseBilancio faseBilancio, FaseBilancio... fasiBilancio) {
		boolean result = false;
		
		for (FaseBilancio fb : fasiBilancio) {
			result = result || fb.equals(faseBilancio);
		}
		
		return result;
	}
	
	/**
	 * Appende un dato all'<code>Appendable</code> se la condizione &eacute; verificata.
	 * 
	 * @param appendable   l'<code>Appendable</code> da popolare
	 * @param condition    la condizione da controllare
	 * @param charSequence il dato da appendere
	 */
	protected void appendIfTrue(Appendable appendable, boolean condition, CharSequence charSequence) {
		final String methodName = "appendIfTrue";
		if(condition) {
			try {
				appendable.append(charSequence);
			} catch(IOException ioe) {
				log.error(methodName, "IOException in appending '" + charSequence + "': " + ioe.getMessage());
			}
		}
	}
	
	/* **** getter **** */
	
	/**
	 * @return the parametroSessioneLista
	 */
	protected SessionParameter getParametroSessioneLista() {
		return parametroSessioneLista;
	}

	/**
	 * @return the parametroSessioneRequest
	 */
	protected SessionParameter getParametroSessioneRequest() {
		return parametroSessioneRequest;
	}
	
	/**
	 * &Eacute; il numero di elementi che vengono visualizzati in una pagina di risultati. 
	 * &Eacute; anche il numero di elementi che passano dal server al client.
	 * 
	 * @return gli elementi per pagina di default
	 */
	protected int getElementiPerPaginaDefault() {
		return 10;
	}
	
	/**
	 * Se sia da aggiornare la posizione di start per il rientro
	 * @return <code>true</code> se la posizione &eacute; da aggiornare; <code>false</code> altrimenti
	 */
	protected boolean isAggiornareRientroPosizioneStart() {
		return true;
	}
	
	/**
	 * Aggiunge l'azione se consentita
	 * @param app l'appendable
	 * @param consentito se l'azione sia consentita
	 * @param azione l'azione
	 */
	protected void addIfConsentito(Appendable app, boolean consentito, String azione) {
		if(consentito) {
			try {
				app.append(azione);
			} catch (IOException e) {
				// Should NEVER happen
				throw new IllegalArgumentException("Impossibile aggiungere l'azione", e);
			}
		}
	}
}
