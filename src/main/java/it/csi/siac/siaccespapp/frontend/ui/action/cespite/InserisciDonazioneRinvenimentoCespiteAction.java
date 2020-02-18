/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccespapp.frontend.ui.model.cespite.InserisciDonazioneRinvenimentoCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;

/**
 * The Class InserisciDonazioneRinvenimentoCespiteAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciDonazioneRinvenimentoCespiteAction extends BaseInserisciCespiteAction<InserisciDonazioneRinvenimentoCespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3360000666801446924L;
	
	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		InserisciCespiteResponse res = inserisciCespite();
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		model.setCespite(res.getCespite());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	

}
