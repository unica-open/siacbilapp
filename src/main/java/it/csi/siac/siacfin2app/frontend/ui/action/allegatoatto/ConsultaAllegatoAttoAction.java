/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.ConsultaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe di Action per la consultazione dell'Allegato Atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/set/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaAllegatoAttoAction extends GenericBilancioAction<ConsultaAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7648423908243115522L;
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	@Autowired private transient SoggettoService soggettoService;

	
	@Override
	public void prepareExecute() throws Exception {
		// Annullo il model, e quindi lo ricreo ex novo
		setModel(null);
		super.prepare();
		checkAndObtainListaClassiSoggetto();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		RicercaDettaglioAllegatoAtto request = model.creaRequestRicercaDettaglioAllegatoAtto();
		logServiceRequest(request);
		RicercaDettaglioAllegatoAttoResponse response = allegatoAttoService.ricercaDettaglioAllegatoAtto(request);
		logServiceResponse(response);
		
		// Se ho un errore, lancio un'eccezione
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAllegatoAtto.class, response));
			throwExceptionFromErrori(response.getErrori());
		}
		AllegatoAtto allegatoAtto = response.getAllegatoAtto();
		model.setAllegatoAtto(allegatoAtto);
		model.setListaElencoDocumentiAllegato(allegatoAtto.getElenchiDocumentiAllegato());
		
		sessionHandler.setParametro(BilSessionParameter.LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO, model.getAllegatoAtto().getDatiSoggettiAllegati());
		return SUCCESS;
	}
	
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 */
	protected void checkAndObtainListaClassiSoggetto() {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			//la lista non e' presente in sessione, la devo ricaricare da servizio
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				listaClassiSoggetto = response.getListaClasseSoggetto();
				//ordino la lista per codice
				ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
				//setto la lista ordinata in sessione per successivi utilizzi
				sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
			}
		}
		//setto la lista in sessione, sia che io l'abbia ottenuta da servizio o da sessione
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}

}
