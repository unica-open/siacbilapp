/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera.DettaglioCespitePrimaNotaLiberaModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite.ElementoCespite;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNota;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNotaResponse;
import it.csi.siac.siaccespser.model.Cespite;
/**
 * Classe di action per il dettaglio dei movimenti della prima nota integrata-
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/05/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioCespitePrimaNotaLiberaAction extends GenericBilancioAction <DettaglioCespitePrimaNotaLiberaModel> {

	/** Per la serializzazione	 **/
	private static final long serialVersionUID = -6733967364481507901L;
	
	@Autowired private transient PrimaNotaCespiteService primaNotaCespiteService;
	
	/**
	 * Caricamento dei movimenti per la prima nota libera.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaCespitiDaPrimaNota() {
		final String methodName = "caricaCespiti";
		
		RicercaCespiteDaPrimaNota request = model.creaRequestRicercaCespiteDaPrimaNota();
		logServiceRequest(request);
		RicercaCespiteDaPrimaNotaResponse response = primaNotaCespiteService.ricercaCespiteDaPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
	
		List<ElementoCespite> listaCespite = new ArrayList<ElementoCespite>();
		for (Cespite ces : response.getListaCespite()){
			listaCespite.add(new ElementoCespite(ces));
		}
		
		model.setListaCespite(listaCespite);
		return SUCCESS;
	}


}
