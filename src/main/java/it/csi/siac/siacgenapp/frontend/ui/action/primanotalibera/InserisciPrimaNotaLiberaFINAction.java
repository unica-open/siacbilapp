/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.InserisciPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.InserisciPrimaNotaLiberaFINModel;

/**
 *  Classe base di action per l'inserimento e l'aggiornamento della prima nota libera
 *   
 * @author Paggio Simona
 * @version 1.0.0 - 14/04/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_INSERIMENTO_FIN)
public class InserisciPrimaNotaLiberaFINAction extends InserisciPrimaNotaLiberaBaseAction<InserisciPrimaNotaLiberaFINModel>{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 3511175804739831324L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN;
	}

	/**
	 * SIAC-8134
	 * Caricamento dell'azione per la gestione della SAC
	 */
	@Override
	public void caricaAzionePerSAC() {
		model.setNomeAzioneSAC(AzioneConsentitaEnum.PRIMANOTALIBERA_GEN_GESTIONE.getNomeAzione());
	}

}
