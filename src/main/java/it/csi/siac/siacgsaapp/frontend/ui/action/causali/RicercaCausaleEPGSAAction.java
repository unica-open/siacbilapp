/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.causali;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.RicercaCausaleEPBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgsaapp.frontend.ui.model.causali.RicercaCausaleEPGSAModel;

/**
 * Classe di action per la ricerca della causale EP.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 07/05/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCausaleEPGSAAction extends RicercaCausaleEPBaseAction<RicercaCausaleEPGSAModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4556780831053124063L;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient SoggettoService soggettoService;

	@Override
	protected void caricaListe() {
		caricaListaTipoCausale();
		caricaListaStatoOperativoCausaleEP();
		caricaListaTipoEventoDaSessione();
		caricaListaEvento();
		caricaListaClassiDaSessione();
		caricaListaTitoliDaSessione();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}
	

	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_CAUSALE_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_CAUSALE_GSA;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassePiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GEN;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaClasseSoggetto() {
		return BilSessionParameter.LISTA_CLASSI_SOGGETTO;
	}
	
	
	/**
	 * Caricamento della lista delle classi.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassi() {
		final String methodName = "caricaListaClassi";
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassePiano());
		if(listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaClassi();
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, createErrorInServiceInvocationString(request, response));
				addErrori(response);
				return INPUT;
			}
			
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(getBilSessionParameterListaClassePiano(), listaClassePiano);
		}
		
		model.setListaClassi(listaClassePiano);
		return SUCCESS;
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaTitoli() {
		final String methodName = "caricaListaTitoli";
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata == null || listaTitoloSpesa == null) {
			LeggiClassificatoriByTipoElementoBilResponse responseEntrata;
			LeggiClassificatoriByTipoElementoBilResponse responseSpesa;
			
			try {
				responseEntrata = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
				responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			} catch(WebServiceInvocationFailureException wsife) {
				log.info(methodName, wsife.getMessage());
				return INPUT;
			}
			
			listaTitoloEntrata = responseEntrata.getClassificatoriTitoloEntrata();
			listaTitoloSpesa = responseSpesa.getClassificatoriTitoloSpesa();
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, listaTitoloEntrata);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, listaTitoloSpesa);
		}
		
		model.setListaTitoloEntrata(listaTitoloEntrata);
		model.setListaTitoloSpesa(listaTitoloSpesa);
		return SUCCESS;
	}
	
	/**
	 * Caricamento della lista del tipo evento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaTipoEvento() {
		final String methodName = "caricaListaTipoEvento";
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		
		if(listaTipoEvento == null) {
			// Carico i dati da servizio
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, errorMsg);
				addErrori(response);
				return INPUT;
			}
			
			listaTipoEvento = response.getCodifiche(TipoEvento.class);
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
		}
		
		model.setListaTipoEvento(listaTipoEvento);
		return SUCCESS;
	}

	/**
	 * caricamento della lista classe soggetto
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClasseSoggetto(){
		final String methodName = "caricaListaClasseSoggetto";
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(getBilSessionParameterListaClasseSoggetto());
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.info(methodName, errorMsg);
				addErrori(response);
				return INPUT;
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(getBilSessionParameterListaClasseSoggetto(), listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);

		return SUCCESS;
	}
	
	
	/**
	 * @return <code>true</code> se la ricerca &egrave; valida, <code>false</code> altrimenti.
	 */
	@Override
	protected boolean isRicercaValida() {
		return super.isRicercaValida() ||  checkCampoValorizzato(model.getClasseDiConciliazione(), "Classe di conciliazione");
	}

}
