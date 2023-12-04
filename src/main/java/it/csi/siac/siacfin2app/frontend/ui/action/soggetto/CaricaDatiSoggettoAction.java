/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtil;
import it.csi.siac.siacfin2app.frontend.ui.model.soggetto.CaricaDatiSoggettoModel;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action per il caricamento dei dati del soggetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/04/2014
 * @version 1.0.1 - 18/09/2014 - Modificata ricerca con aggiunta dati soggetto, aggiuntp filtro sulle modalita pagamento
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CaricaDatiSoggettoAction extends GenericBilancioAction<CaricaDatiSoggettoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5533727790102092949L;
	
	@Autowired private transient SoggettoService soggettoService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		if(StringUtils.isBlank(model.getCodiceSoggetto())) {
			log.debug(methodName, "Codice non fornito");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice soggetto"));
			return SUCCESS;
		}
		
		// Controllo prima di non averlo in sessione
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		if(soggetto == null || listaSedeSecondariaSoggetto == null || listaModalitaPagamentoSoggetto == null || !model.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())) {
			log.debug(methodName, "Caricamento dei dati da servizio");
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.debug(methodName, createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
				addErrori(response);
				return SUCCESS;
			}
			if(response.getSoggetto() == null) {
				log.debug(methodName, "Nessun soggetto corrispondente al codice " + model.getCodiceSoggetto() + " trovato");
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getCodiceSoggetto()));
				return SUCCESS;
			}
			
			soggetto = response.getSoggetto();
			listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			if (model.isGestioneHR() && Boolean.TRUE.equals(model.getMaySearchHR())) {
				listaModalitaPagamentoSoggetto = defaultingList(response.getSoggetto().getModalitaPagamentoList());
			
			} else {
				// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
				
				listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			}
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		model.setSoggetto(soggetto);
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		
		return SUCCESS;
	}
	
	/**
	 * Filtra le modalita di pagamento del soggetto a partire dalla sede secondaria selezionata.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String filterModalitaPagamento() {
		final String methodName = "filterModalitaPagamento";
		
		// Caso base: uid non passato
		if(model.getUidSedeSecondariaSoggetto() == null) {
			List<ModalitaPagamentoSoggetto> original = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
			model.setListaModalitaPagamentoSoggetto(original);
			return SUCCESS;
		}
		
		SedeSecondariaSoggetto sss = new SedeSecondariaSoggetto();
		sss.setUid(model.getUidSedeSecondariaSoggetto());
		log.debug(methodName, "Uid della sede da cercare: " + sss.getUid());
		List<SedeSecondariaSoggetto> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		
		sss = ComparatorUtil.searchByUid(listaInSessione, sss);
		log.debug(methodName, "Denominazione della sede per cui filtrare: " + sss.getDenominazione());
		
		// Filtro sulla base della sede
		List<ModalitaPagamentoSoggetto> filtered = filtraModalitaDaSede(sss);
		model.setListaModalitaPagamentoSoggetto(filtered);
		
		return SUCCESS;
	}

	/**
	 * Filtra le modalita di pagamento soggetto sulla base della sede fornita.
	 * 
	 * @param sss la sede fornita
	 * 
	 * @return la lista delle modalita corrispondenti alla sede
	 */
	private List<ModalitaPagamentoSoggetto> filtraModalitaDaSede(SedeSecondariaSoggetto sss) {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>();
		if(sss == null || StringUtils.isBlank(sss.getDenominazione())) {
			// Se non ho la sede, non ho nulla da filtrare. Esco
			return result;
		}
		List<ModalitaPagamentoSoggetto> original = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		String trimmedDenominazione = StringUtils.trimToEmpty(sss.getDenominazione());
		if(original != null) {
			for(ModalitaPagamentoSoggetto mps : original) {
				if(trimmedDenominazione.equalsIgnoreCase(mps.getAssociatoA())) {
					result.add(mps);
				}
			}
		}
		
		return result;
	}
	
}
