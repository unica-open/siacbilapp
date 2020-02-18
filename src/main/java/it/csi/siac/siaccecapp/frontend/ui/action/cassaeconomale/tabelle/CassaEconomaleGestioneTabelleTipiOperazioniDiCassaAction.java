/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.tabelle;

import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle.CassaEconomaleGestioneTabelleTipiOperazioniDiCassaModel;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.tabelle.OperazioneTipiOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoCassaEconomale;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipologiaOperazioneCassa;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la gestione della tabella dei tipi operazioni di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CassaEconomaleGestioneTabelleTipiOperazioniDiCassaAction extends GenericBilancioAction<CassaEconomaleGestioneTabelleTipiOperazioniDiCassaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3326992020040126030L;
	
	@Autowired private transient CassaEconomaleService cassaEconomaleService;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		setCassaEconomaleInModelFromSession();
		
		boolean cassaInStatoValido = StatoOperativoCassaEconomale.VALIDA.equals(model.getCassaEconomale().getStatoOperativoCassaEconomale());
		model.setCassaInStatoValido(Boolean.valueOf(cassaInStatoValido));
		// Pulsante attivo solo se la cassa e' in stato 'Valida'
		Boolean inserimentoAbilitato = Boolean.valueOf(cassaInStatoValido);
		model.setInserimentoAbilitato(inserimentoAbilitato);
		
		return SUCCESS;
	}

	/**
	 * Imposta la cassa economale nel model.
	 */
	private void setCassaEconomaleInModelFromSession() {
		CassaEconomale cassaEconomale = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE);
		model.setCassaEconomale(cassaEconomale);
	}
	
	/**
	 * Caricamento della lista delle tipologie dell'operazione di cassa.
	 */
	private void caricaListaTipologiaOperazioneCassa() {
		model.setListaTipologiaOperazioneCassa(Arrays.asList(TipologiaOperazioneCassa.values()));
	}

	/**
	 * Ricerca i tipi di operazione di cassa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		setCassaEconomaleInModelFromSession();
		
		RicercaSinteticaTipoOperazioneDiCassa request = model.creaRequestRicercaSinteticaTipoOperazioneDiCassa();
		logServiceRequest(request);
		RicercaSinteticaTipoOperazioneDiCassaResponse response = cassaEconomaleService.ricercaSinteticaTipoOperazioneDiCassa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica del tipo di operazione di cassa");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		// Dati trovati. Imposto in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_OPERAZIONE_DI_CASSA, response.getTipiOperazione());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_TIPO_OPERAZIONE_DI_CASSA, request);
		return SUCCESS;
	}
	
	/**
	 * Inizio dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserimento() {
		model.setOperazioneTipiOperazioneDiCassa(OperazioneTipiOperazioneDiCassa.INSERIMENTO);
		
		// Impostazione dei default
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setInclusoInGiornale(Boolean.TRUE);
		tipoOperazioneCassa.setInclusoInRendiconto(Boolean.TRUE);
		model.setTipoOperazioneCassa(tipoOperazioneCassa);
		caricaListaTipologiaOperazioneCassa();
		
		return SUCCESS;
	}
	
	/**
	 * Inserimento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		setCassaEconomaleInModelFromSession();
		
		InserisceTipoOperazioneDiCassa request = model.creaRequestInserisceTipoOperazioneDiCassa();
		logServiceRequest(request);
		InserisceTipoOperazioneDiCassaResponse response = cassaEconomaleService.inserisceTipoOperazioneDiCassa(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		TipoOperazioneCassa tipoOperazioneCassa = response.getTipoOperazioneCassa();
		log.debug(methodName, "Inserito TipoOperazioneCassa con uid " + tipoOperazioneCassa.getUid());
		
		// Inserimento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		impostaTipoOperazioneCassaRicercaSeNonPresente();
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Validazione per il metodo {@link #inserimento()}.
	 */
	public void validateInserimento() {
		TipoOperazioneCassa tipoOperazioneCassa = model.getTipoOperazioneCassa();
		checkNotNull(tipoOperazioneCassa, "Tipo operazione da inserire", true);
		
		checkNotNullNorEmpty(tipoOperazioneCassa.getCodice(), "Codice");
		checkNotNull(tipoOperazioneCassa.getTipologiaOperazioneCassa(), "Tipo");
		checkNotNullNorEmpty(tipoOperazioneCassa.getDescrizione(), "Descrizione");
	}
	
	/**
	 * Inizio dell'aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornamento() {
		// Cerco il tipo per uid
		List<TipoOperazioneCassa> listaTipi = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_OPERAZIONE_DI_CASSA);
		TipoOperazioneCassa tipoOperazioneCassa = model.getTipoOperazioneCassa();
		TipoOperazioneCassa tipoTrovato = ComparatorUtils.searchByUid(listaTipi, tipoOperazioneCassa);
		model.setTipoOperazioneCassa(tipoTrovato);
		caricaListaTipologiaOperazioneCassa();
		
		// Imposto il tipo di operazione
		model.setOperazioneTipiOperazioneDiCassa(OperazioneTipiOperazioneDiCassa.AGGIORNAMENTO);
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		setCassaEconomaleInModelFromSession();
		
		AggiornaTipoOperazioneDiCassa request = model.creaRequestAggiornaTipoOperazioneDiCassa();
		logServiceRequest(request);
		AggiornaTipoOperazioneDiCassaResponse response = cassaEconomaleService.aggiornaTipoOperazioneDiCassa(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		TipoOperazioneCassa tipoOperazioneCassa = response.getTipoOperazioneCassa();
		log.debug(methodName, "Aggiornato TipoOperazioneCassa con uid " + tipoOperazioneCassa.getUid());
		
		// Aggiornamento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		impostaTipoOperazioneCassaRicercaSeNonPresente();
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		TipoOperazioneCassa tipoOperazioneCassa = model.getTipoOperazioneCassa();
		checkNotNull(tipoOperazioneCassa, "Tipo operazione da aggiornare", true);
		
		checkNotNullNorEmpty(tipoOperazioneCassa.getDescrizione(), "Descrizione");
	}
	
	/**
	 * Annullamento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullamento() {
		final String methodName = "annullamento";
		AnnullaTipoOperazioneDiCassa request = model.creaRequestAnnullaTipoOperazioneDiCassa();
		logServiceRequest(request);
		AnnullaTipoOperazioneDiCassaResponse response = cassaEconomaleService.annullaTipoOperazioneDiCassa(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		TipoOperazioneCassa tipoOperazioneCassa = response.getTipoOperazioneCassa();
		log.debug(methodName, "Annullato TipoOperazioneCassa con uid " + tipoOperazioneCassa.getUid());
		
		// Aggiornamento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Imposta il tipo di operazione di cassa per la ricerca qualora non sia gi&agrave; presente nel model.
	 */
	private void impostaTipoOperazioneCassaRicercaSeNonPresente() {
		if(model.getTipoOperazioneCassaRicerca() == null) {
			model.setTipoOperazioneCassaRicerca(new TipoOperazioneCassa());
		}
	}
	
}
