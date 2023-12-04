/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.stampa;

import java.util.Arrays;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa.RicercaStampaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * Classe di action per la ricerca della stampa iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/01/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaStampaIvaAction extends GenericBilancioAction<RicercaStampaIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2090534180054254871L;
	
	@Autowired private transient GruppoAttivitaIvaService gruppoAttivitaIvaService;
	@Autowired private transient RegistroIvaService registroIvaService;
	@Autowired private transient StampaIvaService stampaIvaService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricamentoListe();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	/**
	 * Ricerca la stampa iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		RicercaSinteticaStampaIva request = model.creaRequestRicercaSinteticaStampaIva();
		logServiceRequest(request);
		RicercaSinteticaStampaIvaResponse response = stampaIvaService.ricercaSinteticaStampaIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio RicercaSinteticaStampaIva");
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato a fronte della ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio RicercaSinteticaStampaIva avvenuta con successo");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_STAMPA_IVA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_STAMPA_IVA, response.getStampeIva());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricerca()}.
	 */
	public void validateRicerca() {
		StampaIva si = model.getStampaIva();
		checkNotNull(si, "Stampa iva", true);
		checkNotNull(si.getTipoStampaIva(), "Tipo stampa iva", true);
		checkNotNull(si.getAnnoEsercizio(), "Anno esercizio");
		if(TipoStampaIva.LIQUIDAZIONE_IVA.equals(si.getTipoStampaIva())){
			checkNotNull(si.getPeriodo(), "Periodo");
		}
		
	}
	
	/**
	 * Carica le varie liste da servizio.
	 */
	private void caricamentoListe() {
		caricaListaTipoStampaIva();
		caricaListaGruppoAttivitaIva();
		caricaListaTipoRegistroIva();
		caricaListaRegistroIva();
		caricaListaPeriodo();
	}
	
	/**
	 * Caricamento della lista del tipoStampaIva.
	 */
	private void caricaListaTipoStampaIva() {
		model.setListaTipoStampaIva(Arrays.asList(TipoStampaIva.values()));
	}
	
	/**
	 * Carica la lista del GruppoAttivitaIva a partire dal servizio.
	 */
	protected void caricaListaGruppoAttivitaIva() {
		List<GruppoAttivitaIva> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_GRUPPO_ATTIVITA_IVA);
		if(lista == null) {
			// Non ho ancora la lista in sessione: la carico dal servizio
			RicercaGruppoAttivitaIva request = model.creaRequestRicercaGruppoAttivitaIva();
			logServiceRequest(request);
			RicercaGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaGruppoAttivitaIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			lista = response.getListaGruppoAttivitaIva();
			sessionHandler.setParametro(BilSessionParameter.LISTA_GRUPPO_ATTIVITA_IVA, lista);
		}
		model.setListaGruppoAttivitaIva(lista);
	}
	
	/**
	 * Caricamento della lista del tipo registro iva.
	 */
	private void caricaListaTipoRegistroIva() {
		model.setListaTipoRegistroIva(Arrays.asList(TipoRegistroIva.values()));
	}
	
	/**
	 * Carica la lista del RegistroIva a partire dal servizio.
	 */
	private void caricaListaRegistroIva() {
		RegistroIva ri = model.getRegistroIva();
		if(ri == null) {
			// Non ho nessun dato utile. Esco
			return;
		}
		GruppoAttivitaIva gai = ri.getGruppoAttivitaIva();
		TipoRegistroIva tri = ri.getTipoRegistroIva();
		if(gai != null && gai.getUid() != 0 && tri != null) {
			RicercaRegistroIva request = model.creaRequestRicercaRegistroIva(gai, tri);
			logServiceRequest(request);
			RicercaRegistroIvaResponse response = registroIvaService.ricercaRegistroIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			model.setListaRegistroIva(response.getListaRegistroIva());
		}
	}
	
	/**
	 * Carica la lista del periodo.
	 */
	protected void caricaListaPeriodo() {
		TipoChiusura tc = model.getTipoChiusura();
		// Se non ho una chiusura, non posso avere i periodi
		if(tc != null) {
			model.setListaPeriodo(tc.getPeriodi());
		}
	}

}
