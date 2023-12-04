/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.consultazione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione.RicercaMovimentiCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.ConsultaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.ConsultaCapitoloUscitaPrevisioneModel;
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
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneStatoIdVariazione;
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
	
	private static final String CODICE_BOZZA = "B";
	private static final String CODICE_DEFINITIVA = "D";
	private static final String CODICE_GIUNTA = "G";
	private static final String CODICE_CONSIGLIO = "C";
	private static final String CODICE_PRE_DEFINITIVA = "P";
	
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
			//CONTABILIA-285
			response.getRiepilogoDatiVariazioneImportiCapitoloPerAnnoNeutre().getDatiVariazioneImportiCapitoloPerAnno(), 
			model.getAnnoEsercizioInt());
		
		caricaEtichetteGiuntaConsiglio();
		//SIAC-7735
		boolean isUscita = false;
		if(model.getUidCapitolo() != null){
			if(sessionHandler.getParametro(BilSessionParameter.MODEL_CONSULTA_CAPITOLO) instanceof ConsultaCapitoloUscitaGestioneModel ||
					sessionHandler.getParametro(BilSessionParameter.MODEL_CONSULTA_CAPITOLO) instanceof ConsultaCapitoloUscitaPrevisioneModel){
				isUscita = true;
			}
		}
		model.setCapitoloUscitaSaldoZero(isUscita);
		buildVariazioniNeutreTotali(response.getRiepilogoDatiVariazioneStatoIdVariazioneList(), movimento);
		model.setElementoVariazioneConsultazione(movimento);
		return SUCCESS;
	}
	
	
	
	/**
	 * Compone i campi per le variazioni a saldo zero
	 */
	private void buildVariazioniNeutreTotali(List<RiepilogoDatiVariazioneStatoIdVariazione> riepilogoDatiVar,
			ElementoVariazioneConsultazione movimento){
		
		int countBozza = 0;
		int countPredef = 0;
		int countGiunta =0;
		int countDefi =0;
		if(riepilogoDatiVar != null &&	!riepilogoDatiVar.isEmpty()){
			HashMap<Integer, RiepilogoDatiVariazioneStatoIdVariazione> idVarMap = new HashMap<Integer, RiepilogoDatiVariazioneStatoIdVariazione>();
			for(RiepilogoDatiVariazioneStatoIdVariazione riepilogoVar :riepilogoDatiVar){
				
				if(!idVarMap.containsKey(riepilogoVar.getIdVariazione())){
					if(CODICE_BOZZA.equals(riepilogoVar.getTipo())){
						countBozza++;
					}else if(CODICE_DEFINITIVA.equals(riepilogoVar.getTipo())){
						countDefi++;
					}else if(CODICE_PRE_DEFINITIVA.equals(riepilogoVar.getTipo())){
						countPredef++;
					} else if(CODICE_GIUNTA.equals(riepilogoVar.getTipo()) || CODICE_CONSIGLIO.equals(riepilogoVar.getTipo())){
						countGiunta++;
					} 
					
					idVarMap.put(riepilogoVar.getIdVariazione(),riepilogoVar);
				}
			}
		}
		
		int totale = countBozza + countDefi + countGiunta + countPredef;
		movimento.setVariazioniInNeutreBozzaComplessiva(countBozza);
		movimento.setVariazioniInNeutreDefinitivaComplessiva(countDefi);
		movimento.setVariazioniInNeutrePreDefinitivaComplessiva(countPredef);
		movimento.setVariazioniInNeutreGiuntaComplessiva(countGiunta);
		movimento.setVariazioniInNeutreTotaleComplessiva(totale);
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
