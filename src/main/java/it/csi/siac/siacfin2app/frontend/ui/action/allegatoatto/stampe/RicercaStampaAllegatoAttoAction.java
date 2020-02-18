/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.stampe;

import java.util.Arrays;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.stampe.RicercaStampaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;

/**
 * Classe di action per la ricerca della stampa allegato action.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 28/12/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaStampaAllegatoAttoAction extends GenericAllegatoAttoAction<RicercaStampaAllegatoAttoModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 3880132958220976769L;

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		super.prepare();
		try {
			log.debug(methodName, "Caricamento liste classificatori");
			caricaListaTipoAtto();
			caricaListaTipoStampa();
		}catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());		
		}
		
	}

	/**
	 * 
	 * Carica la lista Tipo stampa
	 * 
	 */

//
	private void caricaListaTipoStampa() {
		//attualmente e' possibile la ricerca solo per allegato Atto, verr&agrave resa dinamica
		model.setListaTipoStampa(Arrays.asList(TipoStampaAllegatoAtto.values()));
	}



	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	
	/**
	 * Ricerca la stampa allegato atto
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		
		RicercaSinteticaStampaAllegatoAtto request = model.creaRequestRicercaStampaAllegatoAtto();
		logServiceRequest(request);
		RicercaSinteticaStampaAllegatoAttoResponse response = allegatoAttoService.ricercaSinteticaStampaAllegatoAtto(request); ////
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio RicercaStampaAllegatoatto");
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato a fronte della ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio RicercaStampaAllegatoAtto avvenuta con successo");

		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_STAMPA_ALLEGATO_ATTO, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_STAMPA_ALLEGATO_ATTO, response.getListaAllegatoAttoStampa());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricerca()}.
	 */
	public void validateRicerca() {
		
		checkAttoAmministrativo();
				
		checkCondition(model.getAllegatoAttoStampa().getTipoStampa() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo stampa"));
	
		
	}
	/**
	 * * Controllo per il provvedimento.
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; valido per la ricerca; <code>false</code> altrimenti
	 * 
	 * 	 
	 */
	private boolean checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		checkCondition(aa == null || !(aa.getAnno() != 0 ^ aa.getNumero() != 0), ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e numero atto", "devono essere entrambi valorizzati o entrambi non valorizzati"));
		
		if(aa == null || aa.getAnno() == 0 || aa.getNumero() == 0) {
			return false;
		}
		
		if(model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0) {
			model.getAttoAmministrativo().setStrutturaAmmContabile(model.getStrutturaAmministrativoContabile());
		}
		log.debug(methodName, "Ricerca provvedimento con anno " + aa.getAnno() + " e numero " + aa.getNumero());
		// Se ho i dati dell'atto amministrativo, controllo che siano corretti
		try {
			controlloEsistenzaEUnicitaAttoAmministrativo();
			return true;
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione dell'Atto Amministrativo: " + pve.getMessage());
		}
		return false;
	}

	

}

