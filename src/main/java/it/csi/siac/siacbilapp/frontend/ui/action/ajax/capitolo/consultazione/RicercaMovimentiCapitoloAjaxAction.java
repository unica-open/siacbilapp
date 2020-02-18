/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.consultazione;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione.RicercaMovimentiCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoVariazioneConsultazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoVariazioneConsultazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.VincoloCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;

/**
 * Ricerca dei movimenti per il capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMovimentiCapitoloAjaxAction extends GenericBilancioAction<RicercaMovimentiCapitoloAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7282161038065141710L;
	
	@Autowired private transient VincoloCapitoloService vincoloCapitoloService;
	@Autowired private transient CapitoloService capitoloService;
	
	/**
	 * Validazione per la ricerca.
	 */
	private void validateRicerca() {
		checkNotNull(model.getUidCapitolo(), "capitolo");
	}

	// Vincoli
	/**
	 * Ottiene i vincoli collegati al capitolo di uscita previsione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniVincoliCapitoloUscitaPrevisione() {
		return ottieniVincoliCapitolo(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
	}
	/**
	 * Ottiene i vincoli collegati al capitolo di uscita gestione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniVincoliCapitoloUscitaGestione() {
		return ottieniVincoliCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
	}
	/**
	 * Ottiene i vincoli collegati al capitolo di entrata previsione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniVincoliCapitoloEntrataPrevisione() {
		return ottieniVincoliCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
	}
	/**
	 * Ottiene i vincoli collegati al capitolo di entrata gstione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniVincoliCapitoloEntrataGestione() {
		return ottieniVincoliCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
	}
	
	/**
	 * Ottiene i vincoli collegati al capitolo
	 * @param tipoCapitolo il tipo di capitolo
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	private String ottieniVincoliCapitolo(TipoCapitolo tipoCapitolo) {
		validateRicerca();
		if(hasErrori()) {
			return INPUT;
		}
		
		RicercaVincolo request = model.creaRequestRicercaVincolo(tipoCapitolo);
		RicercaVincoloResponse response = vincoloCapitoloService.ricercaVincolo(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		model.setListaVincoloCapitoli(response.getVincoloCapitoli());
		return SUCCESS;
	}
	
	// Variazioni
	
	/**
	 * Ottiene le variazioni per il capitolo di uscita gestione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniVariazioniCapitolo() {
		validateRicerca();
		if(hasErrori()) {
			return INPUT;
		}
		
		RicercaVariazioniCapitolo request = model.creaRequestRicercaVariazioniCapitolo();
		RicercaVariazioniCapitoloResponse response = capitoloService.ricercaVariazioniCapitolo(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		ElementoVariazioneConsultazione movimento = ElementoVariazioneConsultazioneFactory.getInstance(
			response.getRiepilogoDatiVariazioneImportiCapitoloPerAnnoPositive().getDatiVariazioneImportiCapitoloPerAnno(),
			response.getRiepilogoDatiVariazioneImportiCapitoloPerAnnoNegative().getDatiVariazioneImportiCapitoloPerAnno(),
			model.getAnnoEsercizioInt());
		
		caricaEtichetteGiuntaConsiglio();
		
		model.setElementoVariazioneConsultazione(movimento);
		return SUCCESS;
	}
	
	/**
	 * Carica etichette giunta consiglio.
	 */
	private void caricaEtichetteGiuntaConsiglio() {
		Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
		String organoAmministrativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_AMM, model.getEtichettaGiunta());
		String organoLegislativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_LEG, model.getEtichettaConsiglio());
		model.setEtichettaConsiglio(organoLegislativo);
		model.setEtichettaGiunta(organoAmministrativo);
	}
	
	/**
	 * Ottieni etichetta by configurazione ente.
	 *
	 * @param gestioneLivelli the gestione livelli
	 * @param tipologiaGestioneLivelli the tipologia gestione livelli
	 * @param etichettaDefault the etichetta default
	 * @return the string
	 */
	private static String ottieniEtichettaByConfigurazioneEnte(Map<TipologiaGestioneLivelli, String> gestioneLivelli, TipologiaGestioneLivelli tipologiaGestioneLivelli, String etichettaDefault) {
		String parametroEnteOrganoAmm = gestioneLivelli.get(tipologiaGestioneLivelli);
		String organoAmministrativo = StringUtils.isNotBlank(parametroEnteOrganoAmm)? parametroEnteOrganoAmm : etichettaDefault;
		return organoAmministrativo;
	}
	
	/**
	 * Ottiene le variazioni per il capitolo di uscita gestione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniVariazioniCodificheCapitolo() {
		final String methodName = "ottieniVariazioniCodificheCapitolo";
		validateRicerca();
		if(hasErrori()) {
			return INPUT;
		}
		
		RicercaStoricoVariazioniCodificheCapitolo request = model.creaRequestRicercaStoricoVariazioniCodificheCapitolo();
		RicercaStoricoVariazioniCodificheCapitoloResponse response = capitoloService.ricercaStoricoVariazioniCodificheCapitolo(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori.");
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_STORICO_VARIAZIONI_CODIFICHE_CAPITOLO, request);
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_STORICO_VARIAZIONI_CODIFICHE_CAPITOLO,response.getDatiStoricoVariazioniCodificheCapitolo());
		return SUCCESS;
	}

}
