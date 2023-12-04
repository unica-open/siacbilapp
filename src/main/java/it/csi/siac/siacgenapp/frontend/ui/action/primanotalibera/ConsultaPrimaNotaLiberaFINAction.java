/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.ConsultaPrimaNotaLiberaBaseAction;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.ConsultaPrimaNotaLiberaFINModel;
/**
 * Classe di action per la consultazione della prima nota libera FIN
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 06/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 08/10/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPrimaNotaLiberaFINAction extends ConsultaPrimaNotaLiberaBaseAction <ConsultaPrimaNotaLiberaFINModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7014277979603667010L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
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
