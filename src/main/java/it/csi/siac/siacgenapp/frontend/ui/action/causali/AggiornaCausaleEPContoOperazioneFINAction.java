/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.causali;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.BaseInserisciAggiornaCausaleEPBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.BaseInserisciAggiornaCausaleEPContoOperazioneBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.causali.AggiornaCausaleEPFINModel;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di action per l'aggiornamento della causale EP, sezione dei conti operazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaCausaleEPBaseAction.MODEL_SESSION_NAME_AGGIORNAMENTO_FIN)
public class AggiornaCausaleEPContoOperazioneFINAction extends BaseInserisciAggiornaCausaleEPContoOperazioneBaseAction<AggiornaCausaleEPFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1225962822392847806L;
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassiPiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GEN;
	}
	
	@Override
	public void validateInserisciConto() {
		final String methodName = "validateInserisciConto";
		// Se non ho il conto, non posso fare alcunche'
		
		super.validateInserisciConto();
		
		checkCondition(model.getContoTipoOperazione().getConto() != null && model.getContoTipoOperazione().getConto().getPianoDeiConti() != null
				&& model.getContoTipoOperazione().getConto().getPianoDeiConti().getClassePiano() != null
				&& model.getContoTipoOperazione().getConto().getPianoDeiConti().getClassePiano().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Classe conto"));
	
		try {
			Conto contoDaValidare = model.getContoTipoOperazione().getConto();
			contoDaValidare.setAmbito(model.getAmbito());
			Conto conto = validaConto(contoDaValidare);
			model.getContoTipoOperazione().setConto(conto);
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il conto: " + pve.getMessage());
		}
		
	}

}
