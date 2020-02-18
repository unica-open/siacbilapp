/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * 
 */
package it.csi.siac.siacbilapp.frontend.ui.action.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.DettaglioVariazioniCapitoloModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitoloResponse;



/**
 * Classe di Action per la gestione della consultazione della Variazione Importi.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioVariazioniCapitoloAction extends GenericBilancioAction<DettaglioVariazioniCapitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	
	@Autowired private transient CapitoloService capitoloService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		RicercaSinteticaVariazioniSingoloCapitolo req = model.creaRequestRicercaSinteticaVariazioniSingoloCapitolo();
		RicercaSinteticaVariazioniSingoloCapitoloResponse res = capitoloService.ricercaSinteticaVariazioniSingoloCapitolo(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, "Errore nell'invocazione del servizio");
		}
		
		model.setCapitolo(res.getCapitolo());
		
		log.debug(methodName, "Imposto in sessione i dati");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_VARIAZIONI_SINGOLO_CAPITOLO, res.getVariazioni());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_VARIAZIONI_SINGOLO_CAPITOLO, req);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #execute()}.
	 */
	public void validateExecute() {
		checkNotNullNorInvalidUid(model.getCapitolo(), "capitolo");
		checkNotNull(model.getVariazioneInAumento(), "direzione");
	}
	
}
