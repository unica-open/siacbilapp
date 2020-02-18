/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.movimentogestione;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.movimentogestione.RicercaMovimentoGestioneModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di action per le chiamate Ajax di ricerca del movimento di gestione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/03/2014
 * @author Elisa Chiari
 * .@version 2.0.0 - 07/03/2017
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMovimentoGestioneAction extends GenericBilancioAction<RicercaMovimentoGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5221678780111453110L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/**
	 * Cerca l'accertamento per chiave.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 * @deprecated use {@link #cercaAccertamentoOttimizzato()} invece
	 */
	@Deprecated
	public String cercaAccertamento() {
		final String methodName = "cercaAccertamento";
		
		Accertamento accertamento = sessionHandler.getParametro(BilSessionParameter.ACCERTAMENTO);
		if(!ValidationUtil.isValidMovimentoGestioneFromSession(accertamento, model.getAccertamento())) {
			RicercaAccertamentoPerChiave request = model.creaRequestRicercaAccertamentoPerChiave();
			logServiceRequest(request);
			
			RicercaAccertamentoPerChiaveResponse response = movimentoGestioneService.ricercaAccertamentoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaAccertamentoPerChiave");
				addErrori(response);
				return SUCCESS;
			}
			if(response.isFallimento()) {
				log.info(methodName, "Risultato ottenuto dal servizio RicercaAccertamentoPerChiave: FALLIMENTO");
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				return SUCCESS;
			}
			
			accertamento = response.getAccertamento();
			accertamento.setElencoSubAccertamenti(filtraSubAccertamentiDefinitivi(accertamento.getElencoSubAccertamenti()));
			
			sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);
		}
		
		model.setAccertamento(accertamento);
		
		return SUCCESS;
	}
	
	/**
	 * Cerca l'accertamento per chiave.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String cercaAccertamentoOttimizzato() {
		final String methodName = "cercaAccertamentoOttimizzato";
		
		Accertamento accertamento = sessionHandler.getParametro(BilSessionParameter.ACCERTAMENTO);
		if(model.getCaricaSub() || !ValidationUtil.isValidMovimentoGestioneFromSession(accertamento, model.getAccertamento())) {
			RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
			logServiceRequest(request);
			
			RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaAccertamentoPerChiave");
				addErrori(response);
				return SUCCESS;
			}
			if(response.isFallimento()) {
				log.info(methodName, "Risultato ottenuto dal servizio RicercaAccertamentoPerChiave: FALLIMENTO");
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				return SUCCESS;
			}
			
			accertamento = response.getAccertamento();
			accertamento.setElencoSubAccertamenti(defaultingList(accertamento.getElencoSubAccertamenti()));
			
			sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);
			sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, request);
			sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, CollectionUtil.toListaPaginata(response.getAccertamento().getElencoSubAccertamenti(), response.getNumPagina(),response.getNumeroTotaleSub()));

		}
		
		model.setAccertamento(accertamento);
		
		return SUCCESS;
	}
	
	/**
	 * Cerca l'accertamento per chiave caricando un solo sub per chiave annoMovimento/ numeroMovimento/numeroASubmovimento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String cercaAccertamentoSubAccertamento() {
		final String methodName = "cercaAccertamentoSubAccertamento";
		//SIAC-4560
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoSubAccertamentoPerChiaveOttimizzato();
		logServiceRequest(request);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaAccertamentoPerChiave");
			addErrori(response);
			return SUCCESS;
		}
		if(response.isFallimento()) {
			log.info(methodName, "Risultato ottenuto dal servizio RicercaAccertamentoPerChiave: FALLIMENTO");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		Accertamento accertamento = response.getAccertamento();
		accertamento.setElencoSubAccertamenti(defaultingList(accertamento.getElencoSubAccertamenti()));
		
		sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);

		model.setAccertamento(accertamento);
		
		return SUCCESS;
	}
	
	private List<SubAccertamento> filtraSubAccertamentiDefinitivi(List<SubAccertamento> elencoSubAccertamenti) {
		if(elencoSubAccertamenti == null || elencoSubAccertamenti.isEmpty()){
			return new ArrayList<SubAccertamento>();
		}
		List<SubAccertamento> result = new ArrayList<SubAccertamento>();
		for(SubAccertamento s : elencoSubAccertamenti){
			if(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(s.getStatoOperativoMovimentoGestioneEntrata())){
				result.add(s);
			}
		}
		return result;
	}
	
	/**
	 * Cerca l'impegno per chiave.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 * @deprecated use {@link #cercaImpegnoOttimizzato()} instead
	 */
	@Deprecated
	public String cercaImpegno() {
		final String methodName = "cercaImpegno";

		Impegno impegno = sessionHandler.getParametro(BilSessionParameter.IMPEGNO);
		if(!ValidationUtil.isValidMovimentoGestioneFromSession(impegno, model.getImpegno())) {
			RicercaImpegnoPerChiave request = model.creaRequestRicercaImpegnoPerChiave();
			logServiceRequest(request);
			
			RicercaImpegnoPerChiaveResponse response = movimentoGestioneService.ricercaImpegnoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaImpegnoPerChiave");
				addErrori(response);
				return SUCCESS;
			}
			if(response.isFallimento()) {
				log.info(methodName, "Risultato ottenuto dal servizio RicercaImpegnoPerChiave: FALLIMENTO");
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				return SUCCESS;
			}
			
			impegno = response.getImpegno();
			// Inizializzazione subimpegni
			impegno.setElencoSubImpegni(filtraSubImpegniDefinitivi(impegno.getElencoSubImpegni()));
			impegno.setListaVociMutuo(defaultingList(impegno.getListaVociMutuo()));
			// Inizializzazione mutui sui subimpegni
			for(SubImpegno si : impegno.getElencoSubImpegni()) {
				si.setListaVociMutuo(defaultingList(si.getListaVociMutuo()));
			}
			if(impegno.getCapitoloUscitaGestione() == null) {
				// Se il capitolo non e' stato impostato dal servizio, lo imposto io
				impegno.setCapitoloUscitaGestione(response.getCapitoloUscitaGestione());
			}
			sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegno);	
		}
		
		model.setImpegno(impegno);
		
		return SUCCESS;
	}

	/**
	 * Cerca l'impegno per chiave ottenendo i subimpegni paginati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String cercaImpegnoOttimizzato() {
		final String methodName = "cercaImpegnoOttimizzato";

		Impegno impegno = sessionHandler.getParametro(BilSessionParameter.IMPEGNO);
		//se io sono nel caso in cui metto un impegno digitandolo, metto in sessione un impegno che e' senza subimpegni 
		if(model.getCaricaSub() || !ValidationUtil.isValidMovimentoGestioneFromSession(impegno, model.getImpegno())) {
			
			RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
				
			RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
					
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaImpegnoPerChiaveOttimizzato");
				addErrori(response);
				return SUCCESS;
			}
			if(response.isFallimento()) {
				log.info(methodName, "Risultato ottenuto dal servizio RicercaImpegnoPerChiaveOttimizzato: FALLIMENTO");
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				return SUCCESS;
			}
			
			impegno = popolaDatiImpegno(response);
			sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI, request);

		}
		
		model.setImpegno(impegno);
		
		//setto in sessione la request e la response
		return SUCCESS;
	}
	
	
	/**
	 * Cerca il subimpegno di un impegno per chiave annoImpegno/numeroImpegno/numeroSub. Popola nel model i dati dell'impegno e la lista di subimpegni con un solo impegno(quello cercato)
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String cercaImpegnoSubImpegno() {
		final String methodName = "cercaImpegnoOttimizzato";
		//SIAC-4560
		//se io sono nel caso in cui metto un impegno digitandolo, metto in sessione un impegno che e' senza subimpegni 
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoSubimpegnoPerChiaveOttimizzato();
			
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
				
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaImpegnoPerChiaveOttimizzato");
			addErrori(response);
			return SUCCESS;
		}
		if(response.isFallimento()) {
			log.info(methodName, "Risultato ottenuto dal servizio RicercaImpegnoPerChiaveOttimizzato: FALLIMENTO");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		model.setImpegno(popolaDatiImpegno(response));
		
		return SUCCESS;
	}
	
	
	private Impegno popolaDatiImpegno(RicercaImpegnoPerChiaveOttimizzatoResponse response) {
		Impegno impegno;
		impegno = response.getImpegno();
		
		// Inizializzazione subimpegni 
		//TODO: il servizio dovra' restituire i sub gia' filtrati per stato per evitare di sballare la paginazione
		//impegno.setElencoSubImpegni(filtraSubImpegniDefinitivi(impegno.getElencoSubImpegni()));
		impegno.setElencoSubImpegni(defaultingList(impegno.getElencoSubImpegni()));
		impegno.setListaVociMutuo(defaultingList(impegno.getListaVociMutuo()));
		// Inizializzazione mutui sui subimpegni
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			si.setListaVociMutuo(defaultingList(si.getListaVociMutuo()));
		}
		if(impegno.getCapitoloUscitaGestione() == null) {
			// Se il capitolo non e' stato impostato dal servizio, lo imposto io
			impegno.setCapitoloUscitaGestione(response.getCapitoloUscitaGestione());
		}
		sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegno);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI, CollectionUtil.toListaPaginata(response.getImpegno().getElencoSubImpegni(), response.getNumPagina(),response.getNumeroTotaleSub()));
		return impegno;
	}

	
	/**
	 * Filtra i subImpegni definitivi a partire da una lista di subimpegni.
	 * 
	 * @param elencoSubImpegni la lista di subImpegni da filtrare
	 * @return result la lista dei subimpegni in stato definitivo
	 * */
	private List<SubImpegno> filtraSubImpegniDefinitivi(List<SubImpegno> elencoSubImpegni) {
		if(elencoSubImpegni == null || elencoSubImpegni.isEmpty()){
			return new ArrayList<SubImpegno>();
		}
		List<SubImpegno> result = new ArrayList<SubImpegno>();
		for(SubImpegno s : elencoSubImpegni){
			if(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(s.getStatoOperativoMovimentoGestioneSpesa())){
				result.add(s);
			}
		}
		return result;
	}

	/**
	 * ricerca gli impegni tramite la ricerca sintetica 
	 * viene scatenata solo se l'utente ha inserito i dati del provvedimento dalla modale di ricerca impegni
	 * o se l'utente ha inserito solo l'anno del movimento
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSinteticaImpegniSubimpegni(){
		String methodName = "ricercaSinteticaImpegniSubimpegni";
		ricercaTipoAtto();
		
		RicercaSinteticaImpegniSubImpegni request = model.creaRequestRicercaSinteticaImpegniSubImpegni();
		RicercaSinteticaImpegniSubimpegniResponse response = movimentoGestioneService.ricercaSinteticaImpegniSubimpegni(request);

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaSinteticaImpegniSubImpegni");
			addErrori(response);
			return SUCCESS;
		}
		if(model.isMostraErroreNessunDatoTrovato() && response.isFallimento()) {
			log.info(methodName, "Risultato ottenuto dal servizio RicercaSinteticaImpegniSubimpegni: FALLIMENTO");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}

		List<Impegno> listaImpegni = response.getListaImpegni() != null? response.getListaImpegni() : new ArrayList<Impegno>();

		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_IMPEGNI_SUBIMPEGNI, listaImpegni);
		sessionHandler.setParametro(BilSessionParameter.ATTO_AMMINISTRATIVO, model.getAttoAmministrativo());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_IMPEGNI_SUBIMPEGNI, request);
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		//Non serve settarla nel model ...
		model.setListaImpegni(listaImpegni);

		return SUCCESS;
	
	}

	/**
	 * Ricerca sintetica degli accertamenti e subaccertamenti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaSinteticaAccertamentiSubAccertamenti(){
		String methodName = "ricercaSinteticaAccertamentiSubAccertamenti";
		log.debug(methodName, "INIZIO");
		
		ricercaTipoAtto();
		
		RicercaSinteticaAccertamentiSubAccertamenti request = model.creaRequestRicercaSinteticaAccertamentiSubAccertamenti();
		//parametri di paginazione li setto qua perche devo ciclare --> cambiano da una chiamata all'altra 
		RicercaSinteticaAccertamentiSubAccertamentiResponse response = movimentoGestioneService.ricercaSinteticaAccertamentiSubAccertamenti(request);

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaSinteticaAccertamentiSubAccertamenti");
			addErrori(response);
			return SUCCESS;
		}

		if(response.isFallimento()) {
			log.info(methodName, "Risultato ottenuto dal servizio RicercaSinteticaAccertamentiSubAccertamenti: FALLIMENTO");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}

		List<Accertamento> listaAccertamenti = response.getListaAccertamenti();

		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI, listaAccertamenti);
		sessionHandler.setParametro(BilSessionParameter.ATTO_AMMINISTRATIVO, model.getAttoAmministrativo());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI, request);
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		
		//model.setListaAccertamenti(listaAccertamenti);
		log.debug(methodName, "FINE");

		return SUCCESS;
	}

	/**
	 * Ricerca e popola il tipo atto.
	 */
	private void ricercaTipoAtto() {
		if(model.getAttoAmministrativo() == null || model.getAttoAmministrativo().getTipoAtto() == null || model.getAttoAmministrativo().getTipoAtto().getUid() == 0) {
			// Nulla da popolare
			return;
		}
		List<TipoAtto> listaTipoAttoSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		TipoAtto tipoAtto = ComparatorUtils.searchByUid(listaTipoAttoSessione, model.getAttoAmministrativo().getTipoAtto());
		model.getAttoAmministrativo().setTipoAtto(tipoAtto);
	}
	
}
