/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RicercaTipoEventoByEntrataSpesaModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di action per la ricerca della lista di causaliEP in base all'evento passato
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 - 13/05/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaTipoEventoByEntrataSpesaAction extends GenericBilancioAction<RicercaTipoEventoByEntrataSpesaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2823133468965243546L;

	@Autowired private transient CodificheService codificheService;

	@Override
	public String execute() {
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		
		// Se la lista delle causali non e' valorizzata, la inizializzo
		if (listaTipoEvento == null) {
			try {
				listaTipoEvento = ricercaTipoEvento();
			} catch(WebServiceInvocationFailureException wsife) {
				// Ho gia' catturato gli errori. Esco
				return INPUT;
			}
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
		}
		
		List<TipoEvento> filtered = new ArrayList<TipoEvento>(listaTipoEvento);
		// Filtro per entrata e spesa
		filtraPerEntrata(listaTipoEvento, filtered);
		filtraPerSpesa(listaTipoEvento, filtered);
		filtraPerTipoCausale(listaTipoEvento, filtered);
		filtraTipiEventoExtr(listaTipoEvento, filtered);
		// Cespiti
		filtraTipiEventoCogeInv(filtered);
		
		model.setListaTipoEvento(filtered);
		return SUCCESS;
	}

	/**
	 * Filtra per l'entrata.
	 * 
	 * @param listaTipoEvento la lista dei tipi eventi
	 * @param filtered        la lista filtrata
	 */
	private void filtraPerEntrata(List<TipoEvento> listaTipoEvento, List<TipoEvento> filtered) {
		if(!model.isEntrata()) {
			// Se non ho passato entrata, non filtro
			return;
		}
		for (TipoEvento tipoEvento : listaTipoEvento) {
			if(!tipoEvento.isTipoEntrata()) {
				filtered.remove(tipoEvento);
			}
		}
	}

	/**
	 * Filtra per la spesa.
	 * 
	 * @param listaTipoEvento la lista dei tipi eventi
	 * @param filtered        la lista filtrata
	 */
	private void filtraPerSpesa(List<TipoEvento> listaTipoEvento, List<TipoEvento> filtered) {
		if(!model.isSpesa()) {
			// Se non ho passato spesa, non filtro
			return;
		}
		for (TipoEvento tipoEvento : listaTipoEvento) {
			if(!tipoEvento.isTipoSpesa()) {
				filtered.remove(tipoEvento);
			}
		}
	}
	
	/**
	 * Filtra per il tipo di causale
	 * @param listaTipoEvento la lista dei tipi eventi
	 * @param filtered        la lista filtrata
	 */
	private void filtraPerTipoCausale(List<TipoEvento> listaTipoEvento, List<TipoEvento> filtered) {
		if(model.getTipoCausale() == null) {
			// Se non ho il tipo di causale, non filtro
			return;
		}
		for (TipoEvento tipoEvento : listaTipoEvento) {
			if(tipoEvento.getListaTipoCausaleEP() != null && !tipoEvento.getListaTipoCausaleEP().contains(model.getTipoCausale())) {
				filtered.remove(tipoEvento);
			}
		}
	}

	/**
	 * Ricerca il tipo evento dal servizio.
	 * 
	 * @return la lista dei tipi di evento
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<TipoEvento> ricercaTipoEvento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaTipoEvento";
		
		RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class);
		logServiceRequest(request);
		RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			String errorMsg = createErrorInServiceInvocationString(request, response);
			log.debug(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}

		return response.getCodifiche(TipoEvento.class);
	}
	
	
	/**
	 * Filtro per i tipi evento EXTR.
	 *
	 * @param listaTipoEvento the lista tipo evento
	 * @param filtered the filtered
	 */
	private void filtraTipiEventoExtr(List<TipoEvento> listaTipoEvento, List<TipoEvento> filtered) {
		//SIAC-5334
		for (TipoEvento te : listaTipoEvento) {
			if(te == null || BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(te.getCodice())) {
				filtered.remove(te);
			}
		}
	}
	
	/**
	 * Filtro sui tipi evento collegati a COGE-INV
	 * @param filtered gli eventi gi&agrave; filtrati
	 */
	private void filtraTipiEventoCogeInv(List<TipoEvento> filtered) {
		if(!model.isFiltroTipiEventoCogeInv()) {
			return;
		}
		// Tipi evento possibili: DS, L, DE
		List<String> codiciPossibili = Arrays.asList(
				BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_ENTRATA.getConstant(),
				BilConstants.CODICE_TIPO_EVENTO_DOCUMENTO_SPESA.getConstant(),
				BilConstants.CODICE_TIPO_EVENTO_LIQUIDAZIONE.getConstant());
		for(Iterator<TipoEvento> it = filtered.iterator(); it.hasNext();) {
			TipoEvento te = it.next();
			if(te == null || te.getCodice() == null || !codiciPossibili.contains(te.getCodice())) {
				it.remove();
			}
		}
	}
}
