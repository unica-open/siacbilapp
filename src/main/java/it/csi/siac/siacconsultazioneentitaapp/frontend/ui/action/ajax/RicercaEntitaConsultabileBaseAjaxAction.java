/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.dispatcher.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.RicercaEntitaConsultabileBaseAjaxModel;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.DataAdapterFactory;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.EntitaConsultabileDataAdapter;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.stampa.StampaEntitaConsultabili;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileWrapper;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileWrapperFactory;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe base per la gestione dell'elenco paginato (server-side) di entita consultabili
 *
 * @param <M>   la parametrizzazione del Model
 * @param <REQ> la parametrizzazione della Request
 * @param <RES> la parametrizzazione della Response
 * 
 * @author Elisa Chiari 
 * @version 1.0.0 - 29/02/2016
 *
 */
public abstract class RicercaEntitaConsultabileBaseAjaxAction<M extends RicercaEntitaConsultabileBaseAjaxModel, REQ extends ServiceRequest, RES extends ServiceResponse> extends GenericBilancioAction<M> {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -2009647350994898300L;
	
	/** Lista di utilit&agrave;. */
	private ListaPaginata<EntitaConsultabile> listaRisultati;
	private Class<REQ> requestClass;

	/** 
	 * &Eacute; il numero di elementi che vengono visualizzati in una pagina di risultati.
	 * &Eacute; anche il numero di elementi che passano dal server al client.
	 * Equivale al numero di elementi che vengono richiesti al servizio ad ogni chiamata
	 * 
	 */
	protected static final int ELEMENTI_PER_PAGINA_DEFAULT = 10;
	
	/** 
	 * &Eacute; il numero di elementi che vengono scaricati nell'export.
	 * Equivale al numero di elementi che vengono richiesti al servizio per il download
	 * 
	 */
	protected static final int MAX_ELEMENTI_DOWNLOAD_ENTITA_CONSULTABILI = 10000;

	/** Factory dei DataAdapter */
	@Autowired
	protected DataAdapterFactory dataAdapterFactory;
	
	
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
		
		log.debug(methodName, "Invocazione del metodo per l'ottenimento della lista paginata");
		
		//overridato nella classe figlia: prendo dal sessionhandler i valore _RIENTRO e poi lo setto a null
		boolean daRientro = controllaDaRientro();
		log.debug(methodName, "Da rientro? " + daRientro);
		
		log.debug(methodName, "Creo la request di ricerca dell'entita' consultabile");
		//TODO: devo gestire il throws
		REQ req = creaRequest();
		//logServiceRequest(request);
		
		// Valori di default
		final int elementiPerPagina = model.getiDisplayLength() != 0 ? model.getiDisplayLength() : ELEMENTI_PER_PAGINA_DEFAULT;
		
		
		List<EntitaConsultabileWrapper> aaData = new ArrayList<EntitaConsultabileWrapper>();

		//qui
		log.debug(methodName, "iTotalRecords = " + model.getiTotalRecords());
		log.debug(methodName, "iTotalDisplayRecords = " + model.getiTotalDisplayRecords());
		log.debug(methodName, "iDisplayLength() = " + model.getiDisplayLength());
		log.debug(methodName, "iDisplayStart() = " + model.getiDisplayStart());
		
		//anche questi sono valori di default: 1 per l'inizio e il numero di elementi per pagina per la fine
		int inizio = ottieniValoreInizio(daRientro);
		//TODO cruscottino caso in cui ho meno risultati dell'elenco
		int fine = elementiPerPagina;
		
		//vado a modificare il calore dell'inizio e della fine in base alla info che ho da datatable
		inizio = Integer.valueOf(model.getiDisplayStart());
		fine = inizio + Integer.valueOf(model.getiDisplayLength());
		
		log.debug(methodName, "inizio = " + inizio);
		log.debug(methodName, "fine = " + fine);

		String risCall = getNuovoElenco(req);
		log.debug(methodName, "risCall() = " + risCall);

		
		if(!risCall.equals(SUCCESS) || listaRisultati == null) {
			// L'invocazione non ha avuto esito positivo. Impostare a zero il numero dei risultati totali
			model.setiTotalRecords(0);
			model.setiTotalDisplayRecords(model.getiTotalRecords());
		} else {
			//l'invocazione ha avuto esito positivo: quanti sono i risultati? li setto
			int totrec = ottieniTotaleRecords(listaRisultati);
			model.setiTotalRecords(totrec);
			model.setiTotalDisplayRecords(model.getiTotalRecords());
			
			Integer startPosition = Integer.valueOf(inizio);
			log.debug(methodName, "Salvo startPosition = " + startPosition);
			sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, startPosition);

		}
		log.debug(methodName, "iTotalRecords = " + model.getiTotalRecords());
		log.debug(methodName, "iTotalDisplayRecords = " + model.getiTotalDisplayRecords());
		log.debug(methodName, "iDisplayLength = " + model.getiDisplayLength());
		log.debug(methodName, "iDisplayStart = " + model.getiDisplayStart());

		
		
		//setto la lista nel model
		model.setAaData(toAaData());
		log.debug(methodName, "aaData.size() = " + aaData.size());
		
		eseguiOperazioniUlteriori();
		
		return SUCCESS;
	}
	

	/**
	 * Ottiene la lista dei risultati Wrappata.
	 * 
	 * @param listaRisultati la lista dei risultati
	 * @return la lista dei risultati
	 */
	private List<EntitaConsultabileWrapper> toAaData() {
		List<EntitaConsultabileWrapper> result = new ArrayList<EntitaConsultabileWrapper>();
		if(listaRisultati==null || listaRisultati.isEmpty()){
			return result; 
		}
		EntitaConsultabileDataAdapter dataAdapter = dataAdapterFactory.init(listaRisultati.get(0).getTipoEntitaConsultabile());
		return EntitaConsultabileWrapperFactory.init(listaRisultati, dataAdapter);
	}

	/**
	 * Chiama il servizio per recuperare l'ennesima pagina.
	 * 
	 * @param req la request da invocare
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	private String getNuovoElenco(REQ req) {
		final String methodName = "getNuovoElenco";
		
		/* Richiamo il servizio di Ricerca Sintetica */
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RES response = ottieniResponse(req);
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		listaRisultati = ottieniListaRisultati(response);
		eseguiOperazioniUlterioriSuResponse(response);

		boolean controlloDatiReperiti = controllaDatiReperiti(listaRisultati);
		if(!controlloDatiReperiti) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return SUCCESS;
		}
		
		return SUCCESS;
	}
	
	/**
	 * Imposta la classe di request a partire dal Generics.
	 */
	@SuppressWarnings("unchecked")
	private void impostaRequestClass() {
		Class<?>[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), RicercaEntitaConsultabileBaseAjaxAction.class);
		Class<REQ> clazz = (Class<REQ>) genericTypeArguments[1];
		this.requestClass = clazz;
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
	protected int ottieniTotaleRecords(ListaPaginata<EntitaConsultabile> listaRisultatiDaCuiOttenereIlTotale) {
		return listaRisultatiDaCuiOttenereIlTotale.getTotaleElementi();
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
	 * Controlla che i dati siano stati reperiti.
	 * 
	 * @param lista la lista dei risultati
	 * 
	 * @return <code>true</code> se vi sono dei dati, false in caso contrario
	 */
	protected boolean controllaDatiReperiti(ListaPaginata<EntitaConsultabile> lista) {
		return !listaRisultati.isEmpty();
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
	 * Ottiene la request valorizzata.
	 * 
	 * @return la request
	 */
	protected abstract REQ creaRequest();
	
	/**
	 * Imposta i parametri di paginazione.
	 * 
	 * @param req                  la request in cui injettare i parametri
	 * @param parametriPaginazione i parametri da injettare
	 */
	protected abstract void impostaParametriPaginazione(REQ req, ParametriPaginazione parametriPaginazione);
	
	
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
	 * @param res la response
	 * 
	 * @return la lista dei risultati
	 */
	//TODO: forse questa si puo' togliere
	protected abstract ListaPaginata<EntitaConsultabile> ottieniListaRisultati(RES res);
	
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

	@Override
	protected void logServiceResponse(ServiceResponse res){
		log.logXmlTypeObject(res, "Service Response param");
	}
	
	@Override
	protected void logServiceRequest(ServiceRequest req){
		log.logXmlTypeObject(req, "Service Request param");
	}
	
	
	
	
	/**
	 *  Chiama il servizio per il download dei risultati di ricerca
	 *  @return il risultato dell'invocazione
	 */
	public String download() {
		String methodName = "download";
		REQ req = creaRequest();
		impostaParametriPaginazione(req,new ParametriPaginazione(0, MAX_ELEMENTI_DOWNLOAD_ENTITA_CONSULTABILI));
		
		RES response = ottieniResponse(req);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT; 
		}
		
		ListaPaginata<EntitaConsultabile> entitaConsultabili = ottieniListaRisultati(response);
		if(entitaConsultabili.isEmpty()){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT; 
		}
		
		EntitaConsultabileDataAdapter dataAdapter = dataAdapterFactory.init(entitaConsultabili.get(0).getTipoEntitaConsultabile());
		
		StampaEntitaConsultabili sec = new StampaEntitaConsultabili(dataAdapter, Boolean.TRUE.equals(model.getIsXlsx()));
		sec.init();
		sec.addRows(entitaConsultabili);
		
		byte[] excel;
		try {
			excel = sec.toBytes(entitaConsultabili.getTotalePagine() > 1);
		} catch (IOException e) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile generare il report. " + e.getMessage()));
			return INPUT; 
		}
		log.info(methodName, "Excel entita consultabili generato correttamente. Size: " + excel.length + " Bytes");
		
		//per l'xlsx invece usare: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
		model.setContentType(sec.getContentType()); 
		model.setContentLength(Long.valueOf(excel.length));
		model.setFileName("consultazioneEntita." + sec.getExtension());
		
		InputStream inputStream = new ByteArrayInputStream(excel);
		model.setInputStream(inputStream);
		
		return SUCCESS;
	}
	
	/**
	 * Stream result per le entit&agrave; consultabili
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 28/06/2018
	 *
	 */
	public static class EntitaConsultabileStreamResult extends StreamResult {

		/** Per la serializzazione */
		private static final long serialVersionUID = -6442717248516202406L;

		/** Costruttore vuoto di default */
		public EntitaConsultabileStreamResult() {
			this.contentType = "${contentType}";
			this.contentLength = "${contentLength}";
			this.inputName = "inputStream";
			this.contentDisposition = "filename=\"${fileName}\"";
			this.bufferSize = 1024;
		}
	}
}
