/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.handler;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Handler per gli errori nei fondi a dubbia esigibilit&agrave;: modifica il messaggio di dato non presente
 * @author Marchino Alessandro
 *
 */
public class FondiDubbiaEsigibilitaDatoNonPresenteServiceErrorHandler implements ServiceErrorHandler {

	@Override
	public <RES extends ServiceResponse, MOD extends GenericBilancioModel, ACT extends GenericBilancioAction<MOD>> void handleError(ACT action, RES response) {
		List<Errore> errori = handleErrori(response.getErrori());
		
		action.getModel().addErrori(errori);
		for(Errore err : errori) {
			action.addActionError(err.getTesto());
		}
	}

	/**
	 * Gestione degli errori.
	 * <br/>
	 * Qualora vi sia un errore del tipo <code>COR_ERR_0004</code>, viene sostituito con <code>BIL_ERR_0000</code>
	 * @param errori
	 * @return
	 */
	private List<Errore> handleErrori(List<Errore> errori) {
		List<Errore> res = new ArrayList<Errore>();
		for(Errore err : errori) {
			if(ErroreCore.ENTITA_NON_TROVATA.getCodice().equals(err.getCodice())) {
				res.add(ErroreBil.ERRORE_GENERICO.getErrore("Capitolo entrata previsione: non e' presente in archivio o e' gia' collegato al fondo"));
			} else {
				res.add(err);
			}
		}
		
		return res;
	}

}
