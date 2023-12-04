/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.InserisciPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;

/**
 * Classe di action per l'inserimento del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciPreDocumentoEntrataAction extends BaseInserimentoPreDocumentoEntrataAction<InserisciPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1021580050844953921L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() {
		// Caricamento valori default
		checkCasoDUsoApplicabile("Inserimento preDocumento di entrata");
		
		// Inizializzo l'anagrafica
		DatiAnagraficiPreDocumento datiAnagraficiPreDocumento = new DatiAnagraficiPreDocumento();
		datiAnagraficiPreDocumento.setNazioneNascita(BilConstants.DESCRIZIONE_ITALIA.getConstant());
		datiAnagraficiPreDocumento.setNazioneIndirizzo(BilConstants.DESCRIZIONE_ITALIA.getConstant());
		
		model.setDatiAnagraficiPreDocumento(datiAnagraficiPreDocumento);
		
		return SUCCESS;
	}

}
