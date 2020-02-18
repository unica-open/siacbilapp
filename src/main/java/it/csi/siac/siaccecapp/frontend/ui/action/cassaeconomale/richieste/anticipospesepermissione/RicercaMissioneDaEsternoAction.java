/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.RicercaMissioneDaEsternoModel;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogate;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogateResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la ricerac della missione da caricamento esterno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/12/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMissioneDaEsternoAction extends GenericBilancioAction<RicercaMissioneDaEsternoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2230276169662027025L;
	
	@Autowired private transient RichiestaEconomaleService richiestaEconomaleService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		List<RichiestaEconomale> listaRichiestaEconomale = sessionHandler.getParametro(BilSessionParameter.LISTA_MISSIONE_CARICAMENTO_ESTERNO);
		if(listaRichiestaEconomale == null) {
			RicercaRichiesteAnticipoMissioniNonErogate request = model.creaRequestRicercaRichiesteAnticipoMissioniNonErogate(false);
			logServiceRequest(request);
			RicercaRichiesteAnticipoMissioniNonErogateResponse response = richiestaEconomaleService.ricercaRichiesteAnticipoMissioniNonErogate(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				log.info(methodName, createErrorInServiceInvocationString(request, response));
				return INPUT;
			}
			log.debug(methodName, "Caricate " + response.getCardinalitaComplessiva() + " missioni da esterno");
			listaRichiestaEconomale = response.getRichiesteEconomali();
		}
		
		model.setListaRichiestaEconomale(listaRichiestaEconomale);
		return SUCCESS;
	}

	/**
	 * Caricamento del dettaglio per la missione esterna.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaDettaglio() {
		final String methodName = "caricaDettaglio";
		
		RicercaRichiesteAnticipoMissioniNonErogate request = model.creaRequestRicercaRichiesteAnticipoMissioniNonErogate(true);
		logServiceRequest(request);
		RicercaRichiesteAnticipoMissioniNonErogateResponse response = richiestaEconomaleService.ricercaRichiesteAnticipoMissioniNonErogate(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			return INPUT;
		}
		log.debug(methodName, "Caricate " + response.getCardinalitaComplessiva() + " missioni da esterno");
		
		RichiestaEconomale richiestaEconomale = findRichiestaEconomaleEsternaById(response.getRichiesteEconomali());
		if(richiestaEconomale == null) {
			log.info(methodName, "Nessuna richiesta economale con id pari a " + model.getRichiestaEconomale().getIdMissioneEsterna() + " reperita");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Anticipo per missione", model.getRichiestaEconomale().getIdMissioneEsterna()));
			return INPUT;
		}
		
		model.setRichiestaEconomale(richiestaEconomale);
		return SUCCESS;
	}
	
	/**
	 * Ottiene la richiesta economale di dato id.
	 * 
	 * @param richiesteEconomali le richieste da cui ottenere quella desiderata.
	 * @return la richiesta desiderata
	 */
	private RichiestaEconomale findRichiestaEconomaleEsternaById(List<RichiestaEconomale> richiesteEconomali) {
		for(RichiestaEconomale re : richiesteEconomali) {
			if(re != null && model.getRichiestaEconomale().getIdMissioneEsterna().equals(re.getIdMissioneEsterna())) {
				return re;
			}
		}
		return null;
	}

	/**
	 * Validazione per il metodo {@link #caricaDettaglio()}.
	 */
	public void validateCaricaDettaglio() {
		checkCondition(model.getRichiestaEconomale() != null && StringUtils.isNotBlank(model.getRichiestaEconomale().getIdMissioneEsterna()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Id missione esterna"));
	}
}
