/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RicercaEventoByTipoEventoBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipoResponse;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di action per la ricerca dell'evento a partire dal tipo di evento.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 06/10/2015
 * @param <M> la tipizzazione del model
 *
 */

public class RicercaEventoByTipoEventoBaseAction <M extends RicercaEventoByTipoEventoBaseModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4238986980038232318L;
	
	@Autowired private transient CausaleService causaleService;
	
	@Override
	public String execute() {
		final String methodName = "execute";
		
		try {
			validazioneInvocazione();
		} catch(ParamValidationException pve) {
			// Fallimento nella validazione
			return SUCCESS;
		}
		
		Integer uidTipoEvento = sessionHandler.getParametro(BilSessionParameter.ULTIMO_UID_TIPO_EVENTO_RICERCATO);
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		if(uidTipoEvento == null || (uidTipoEvento.intValue() != model.getTipoEvento().getUid()) || listaEvento == null) {
			RicercaEventiPerTipo request = model.creaRequestRicercaEventiPerTipo();
			logServiceRequest(request);
			RicercaEventiPerTipoResponse response = causaleService.ricercaEventiPerTipo(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.debug(methodName, createErrorInServiceInvocationString(RicercaEventiPerTipo.class, response));
				addErrori(response);
				return SUCCESS;
			}
			
			listaEvento = response.getEventi();
			
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, listaEvento);
			sessionHandler.setParametro(BilSessionParameter.ULTIMO_UID_TIPO_EVENTO_RICERCATO, model.getTipoEvento().getUid());
		}
		
		model.setListaEvento(listaEvento);
		return SUCCESS;
	}

	/**
	 * Validazione dell'invocazione al servizio.
	 */
	private void validazioneInvocazione() {
		checkNotNullNorInvalidUid(model.getTipoEvento(), "Tipo evento", true);
	}
	
}
