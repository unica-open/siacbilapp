/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagcomp;

import java.util.Arrays;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.ComponenteCapitoloAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.anagcomp.RicercaComponenteCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Classe di Action per la ricerca del TipoComponenteImportiCapitolo
 * 
 * @author Lobue Filippo
 * @version 1.0.0 - 20/set/2019
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaComponenteCapitoloAction  extends ComponenteCapitoloAction<RicercaComponenteCapitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 117393494833285781L;

	TipoComponenteImportiCapitolo cc = new TipoComponenteImportiCapitolo();
	
	@Autowired private transient TipoComponenteImportiCapitoloService componenteCapitoloService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaClassificatori();
		
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	
	/**
	 * Ricerca con operazioni il TipoComponenteImportiCapitolo.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaComponenteCapitolo() {
		final String methodName = "ricercaComponenteCapitolo";

		// Popolo le codifiche
		popolaCodificheDaSessione();
		
		log.debug(methodName, "Creazione della request");
		RicercaSinteticaTipoComponenteImportiCapitolo req = model.creaRequestComponenteCapitolo();
		logServiceRequest(req);
		RicercaSinteticaTipoComponenteImportiCapitoloResponse res = componenteCapitoloService.ricercaSinteticaTipoComponenteImportiCapitolo(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio RicercaComponenteCapitolo");
			addErrori(res);
			return INPUT;
		}
		
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato di ricerca per i dati forniti");
			impostaMessaggioNessunDatoTrovato();
			return INPUT;
		}
		//rimuovere
		if(res.getListaTipoComponenteImportiCapitolo().isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreBil.COMPONENTE_INESISTENTE.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + res.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_COMPONENTE_CAPITOLO, req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_COMPONENTE_CAPITOLO, res.getListaTipoComponenteImportiCapitolo());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);

		return SUCCESS;
	}
	
	/**
	 * 
	 */
	protected void impostaMessaggioNessunDatoTrovato() {
		addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste dei MacroTipo  e SottoTipo
	 */
	private void caricaClassificatori() {
		 List<MacrotipoComponenteImportiCapitolo> macrotipoList = Arrays.asList(MacrotipoComponenteImportiCapitolo.values());
		 model.setListaMacroTipo(macrotipoList);
	       
	    List<SottotipoComponenteImportiCapitolo> sottoTipoList  = Arrays.asList(SottotipoComponenteImportiCapitolo.values());
	    model.setListaSottoTipo(sottoTipoList); 
	 
	}
	
	/**
	 * Popola le codifiche a partire dalla lista caricata in sessione.
	 */
	private void popolaCodificheDaSessione() {
		List<TipoComponenteImportiCapitolo> listaTipoComponenteImportiCapitoloInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_COMPONENTE_CAPITOLO);
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = ComparatorUtils.searchByUid(listaTipoComponenteImportiCapitoloInSessione, model.getComponenteCapitolo());
		// Imposto il tipoAtto nel model
		model.setComponenteCapitolo(tipoComponenteImportiCapitolo);
		
	 
	}
	
	
	

}
