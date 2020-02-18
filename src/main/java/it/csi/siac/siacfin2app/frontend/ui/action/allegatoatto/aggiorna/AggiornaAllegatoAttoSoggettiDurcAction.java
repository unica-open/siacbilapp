/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.aggiorna;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggettoResponse;

/**
 * The Class AggiornaAllegatoAttoSoggettiDurcAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AggiornaAllegatoAttoSoggettiDurcAction extends AggiornaAllegatoAttoBaseAction {

    private static final long serialVersionUID = -5809029023931383653L;

    /**
     * Ottieni lista soggetti durc.
     *
     * @return the string
     */
    public String ottieniListaSoggettiDurc() {
    	return SUCCESS;
    }
    
    
    /**
     * Salva durc.
     *
     * @return the string
     */
    public String salvaDurc() {
	
		AggiornaDatiDurcSoggettoResponse res = soggettoService.aggiornaDatiDurcSoggetto(model.creaRequestAggiornaDatiDurcSoggetto());
		
		if (res.isFallimento()) {
		    addErrori(res);
		}else {
		    addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		}
		
		setInformazioniMessaggiErroriInSessionePerActionSuccessiva();
		
		return SUCCESS;
    }

}
