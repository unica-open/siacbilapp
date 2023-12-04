/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.DataTableAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class DataTableAjaxAction<S extends Serializable, M extends DataTableAjaxModel<S, REQ>, 
	E extends Entita, REQ extends ServiceRequest, RES extends ServiceResponse> extends BaseDataTableAjaxAction<S, M, E, REQ, RES> {
	
	private static final long serialVersionUID = -7171596552249445160L;
	private List<E> responseList;

	

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();

		log.debug(methodName, "Invocazione del metodo per l'ottenimento della lista paginata");
		
		log.debug(methodName, "Ottengo il numero del blocco dai parametri di Paginazione");
		REQ req = model.buildRequest();
		
		List<S> aaData = new ArrayList<S>();

		log.debug(methodName, "iTotalRecords = " + model.getiTotalRecords());
		log.debug(methodName, "iTotalDisplayRecords = " + model.getiTotalDisplayRecords());
		
		String risCall = callService(req);
		
		if(!risCall.equals(SUCCESS)) {
			model.setiTotalRecords(0);
			model.setiTotalDisplayRecords(model.getiTotalRecords());
		} else {
			int totrec = responseList.size();
			model.setiTotalRecords(totrec);
			model.setiTotalDisplayRecords(model.getiTotalRecords());

			AzioniConsentiteWrapper wpAzioniConsentite = getAzioniConsentiteWrapperInstance(listaAzioniConsentite);
			
			for (E elem: responseList) {
				S instance = getInstance(elem);
				handleAzioniConsentite(instance, 
					wpAzioniConsentite.isAggiornaAbilitato(),
					wpAzioniConsentite.isConsultaAbilitato(),
					wpAzioniConsentite.isEliminaAbilitato(),
					wpAzioniConsentite.isAnnullaAbilitato());
				aaData.add(instance);
			}
		}

		model.setAaData(aaData);
		
		executeEnd();
		
		return SUCCESS;
	}
	
	private String callService(REQ req) {
		
		RES res = getResponse(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		
		responseList = getResponseList(res);
	
		if(!!responseList.isEmpty()) {
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return SUCCESS;
		}
		
		executeAfterServiceCall(res);
		
		return SUCCESS;
	}
	
	
	
	protected void handleAzioniConsentite(S instance, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		super.handleAzioniConsentite(instance, false, isAggiornaAbilitato, isAnnullaAbilitato, isConsultaAbilitato, isEliminaAbilitato);
	}
	
	/**
	 * Ottiene il wrapper per le azioni consentite.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return il wrapper
	 */
	protected AzioniConsentiteWrapper getAzioniConsentiteWrapperInstance(List<AzioneConsentita> listaAzioniConsentite) {
		return new AzioniConsentiteWrapper();
	}
	
	/**
	 * Esecuzione di ulteriori operazioni sulla response del servizio.
	 * 
	 * @param response la response tramite cui effettuare ulteriori informazioni
	 */
	protected void executeAfterServiceCall(RES response) {
		// Implementazione di default vuota
	}
	
	/**
	 * Esecuzione di ulteriori operazioni a termine della chiamata.
	 */
	protected void executeEnd() {
		// Implementazione di dafault vuota
	}
	
	protected abstract List<E> getResponseList(RES response);

	protected void addIfConsentito(Appendable app, boolean consentito, String azione) {
		if(consentito) {
			try {
				app.append(azione);
			} catch (IOException e) {
				// Should NEVER happen
				throw new IllegalArgumentException("Impossibile aggiungere l'azione", e);
			}
		}
	}
}
