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
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtil;
import it.csi.siac.siacfin2app.frontend.ui.model.soggetto.RicercaSoggettoPerMatricolaModel;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action per il caricamento dei dati del soggetto per matricola.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaSoggettoPerMatricolaAction extends GenericBilancioAction<RicercaSoggettoPerMatricolaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3073980931265784653L;
	
	@Autowired private transient SoggettoService soggettoService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		if(model.getSoggetto() == null || StringUtils.isBlank(model.getSoggetto().getMatricola())) {
			log.debug(methodName, "Matricola non fornita");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("matricola soggetto"));
			return SUCCESS;
		}
		
		// Controllo prima di non averlo in sessione
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		if(!isSoggettoGiaPresente(soggetto, listaSedeSecondariaSoggetto, listaModalitaPagamentoSoggetto)) {
			// Non ho ancora il soggetto: lo carico
			RicercaSoggettoPerChiaveResponse response;
			try {
				response = caricaSoggetto();
			} catch(WebServiceInvocationFailureException wsife) {
				// Ho un errore, ma i log sono gia' a posto
				return SUCCESS;
			}
			
			soggetto = response.getSoggetto();
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			if (model.isGestioneHR() && model.getMaySearchHR() ) {
				listaModalitaPagamentoSoggetto = defaultingList(response.getSoggetto().getModalitaPagamentoList());
			
			} else {
				
				listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			}
			//listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			
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
	 * Controlla se il soggetto sia gi&agrave; presente per i dati forniti in input.
	 * 
	 * @param soggetto                       il soggetto da controllare
	 * @param listaSedeSecondariaSoggetto    la lista delle sedi secondarie
	 * @param listaModalitaPagamentoSoggetto la lista delle modalita di pagamento
	 * 
	 * @return <code>true</code> se il soggetto &eacute; gi&agrave; presente; <code>false</code> altrimenti
	 */
	private boolean isSoggettoGiaPresente(Soggetto soggetto, List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto, List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		return soggetto != null
				&& listaSedeSecondariaSoggetto != null
				&& listaModalitaPagamentoSoggetto != null
				&& model.getSoggetto().getMatricola().equals(soggetto.getMatricola());
	}
	
	/**
	 * Carica il soggetto.
	 * 
	 * @return la response della ricerca del soggetto
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private RicercaSoggettoPerChiaveResponse caricaSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "caricaSoggetto";
		Soggetto soggetto = model.getSoggetto();
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response);
			log.debug(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(response.getSoggetto() == null) {
			String codiceRicercato = StringUtils.isNotBlank(soggetto.getCodiceSoggetto())
				? "con codice " + soggetto.getCodiceSoggetto()
				: StringUtils.isNotBlank(soggetto.getMatricola())
					? "con matricola " + soggetto.getMatricola()
					: "";
			String errorMsg = "Nessun soggetto corrispondente al codice " + codiceRicercato + " trovato";
			log.debug(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", codiceRicercato));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Ho il dettaglio: lo restituisco
		return response;
	}
	
	/**
	 * Filtra le modalita di pagamento del soggetto a partire dalla sede secondaria selezionata.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String filterModalitaPagamento() {
		final String methodName = "filterModalitaPagamento";
		SedeSecondariaSoggetto sss = new SedeSecondariaSoggetto();
		sss.setUid(model.getUidSedeSecondariaSoggetto() != null ? model.getUidSedeSecondariaSoggetto() : 0);
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
