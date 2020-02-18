/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RisultatiRicercaAllegatoAttoOperazioniMultipleModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolatiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 15/set/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAllegatoAttoOperazioniMultipleAction extends RisultatiRicercaAllegatoAttoBaseAction<RisultatiRicercaAllegatoAttoOperazioniMultipleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3015528792486862396L;
	

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Ottengo i messaggi
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		return SUCCESS;
	}
	
	/**
	 * Validate completa multiplo.
	 */
	public void validateCompletaSelezionati() {
		checkCondition(model.getUidsAllegatiAtto() != null && !model.getUidsAllegatiAtto().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista allegati da completare"));
	}
	
	/**
	 * Completa multiplo.
	 *
	 * @return the string
	 */
	public String completaSelezionati() {
		CompletaAllegatoAttoMultiplo request = model.creaRequestCompletaAllegatoAttoMultiploPerSelezionati();
		return completaMultiplo(request);
	}
	
	/**
	 * Validate completa multiplo.
	 */
	public void validateCompletaTutti() {
		
		RicercaAllegatoAtto reqInterna = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO_MULT, RicercaAllegatoAtto.class);
		checkNotNull(reqInterna, "parametri ricerca per selezione");
	}
	
	/**
	 * Completa multiplo.
	 *
	 * @return the string
	 */
	public String completaTutti() {
		RicercaAllegatoAtto reqInterna = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO_MULT, RicercaAllegatoAtto.class);
		CompletaAllegatoAttoMultiplo request = model.creaRequestCompletaAllegatoAttoMultiploTutti(reqInterna);
		return completaMultiplo(request);
	}
	
	/**
	 * Completa multiplo.
	 *
	 * @param req the req
	 * @return the string
	 */
	private String completaMultiplo(CompletaAllegatoAttoMultiplo req) {
		final String methodName = "completaMultiplo";
		AzioneRichiesta azioneRichiesta = AzioniConsentite.ALLEGATO_ATTO_COMPLETA_MULTIPLO.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = allegatoAttoService.completaAllegatoAttoMultiploAsync(wrapRequestToAsync(req, azioneRichiesta));
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nella chiamata al servizio.");
			addErrori(response);
			return SUCCESS;
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata correttamente avviata: il risultato sar&agrave; disponibile da cruscotto."));
		return SUCCESS;
	}
	
	/**
	 * Validate completa multiplo.
	 */
	public void validateConvalidaSelezionati() {
		checkCondition(model.getUidsAllegatiAtto() != null && !model.getUidsAllegatiAtto().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista allegati da completare"));
	}
	
	/**
	 * Convalida multiplo.
	 *
	 * @return the string
	 */
	public String convalidaSelezionati() {
		ConvalidaAllegatoAttoPerElenchiMultiplo request = model.creaRequestConvalidaAllegatoAttoMultiploPerSelezionati();
		return convalidaMultiplo(request);
	}
	
	/**
	 * Validate completa multiplo.
	 */
	public void validateConvalidaTutti() {
		
		RicercaAllegatoAtto reqInterna = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO_MULT, RicercaAllegatoAtto.class);
		checkNotNull(reqInterna, "parametri ricerca per selezione");
	}
	
	/**
	 * Convalida multiplo.
	 *
	 * @return the string
	 */
	public String convalidaTutti() {
		RicercaAllegatoAtto reqInterna = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO_MULT, RicercaAllegatoAtto.class);
		ConvalidaAllegatoAttoPerElenchiMultiplo request = model.creaRequestConvalidaAllegatoAttoPerElenchiMultiploTutti(reqInterna);
		return convalidaMultiplo(request);
	}
	
	/**
	 * Convalida multiplo.
	 *
	 * @param req the req
	 * @return the string
	 */
	private String convalidaMultiplo(ConvalidaAllegatoAttoPerElenchiMultiplo req) {
		final String methodName = "completaMultiplo";
		AzioneRichiesta azioneRichiesta = AzioniConsentite.ALLEGATO_ATTO_CONVALIDA_MULTIPLO.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = allegatoAttoService.convalidaAllegatoAttoPerElenchiMultiploAsync(wrapRequestToAsync(req, azioneRichiesta));
		if(response.hasErrori()) {
			log.debug(methodName, "si sono verificati errori nella chiamata al servizio.");
			addErrori(response);
			return SUCCESS;
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata correttamente avviata: il risultato sar&agrave; disponibile da cruscotto."));
		return SUCCESS;
	}
	
	/**
	 * Controllo importi impegni vincolati.
	 *
	 * @return the string
	 */
	//SIAC-6688
	public String controlloImportiImpegniVincolati() {
		final String methodName = "controlloImportiImpegniVincolati";
		model.resetMessaggi();
		model.resetInformazioni();
		
		// Creo la request per l'annullamento dell'allegato atto
		ControlloImportiImpegniVincolati req = model.creaRequestControlloImportiImpegniVincolati();
		logServiceRequest(req);
		
		if(req.getListaAllegatoAttoId()==null || req.getListaAllegatoAttoId().size() == 0 ){
			return SUCCESS;
		}
				
		// Invocazione del servizio
		ControlloImportiImpegniVincolatiResponse response = allegatoAttoService.controlloImportiImpegniVincolati(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, response));
			addErrori(response);
			return INPUT;
		}
		
		if(response.getMessaggi() == null || response.getMessaggi().size() == 0){
			addInformazione(new Informazione("CRU_CON_2001", "Controllo Importi avvenuto con successo."));						
		}else{
			model.addMessaggi(response.getMessaggi());
		}
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
}
