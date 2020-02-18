/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;

/**
 * Classe di action per l'aggiornamento del Documento di entrata, sezione Documenti Collegati.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataDocumentiCollegatiAction extends AggiornaDocumentoEntrataBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	/**
	 * Redirezione verso l'inserimento di un documento di spesa.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneInserimentoDocumentoSpesa() {
		final String methodName = "redirezioneInserimentoDocumentoSpesa";
		log.info(methodName, "Redirezione verso l'inserimento di un documento di spesa per Documenti Collegati ");
		return SUCCESS;
	}

	/**
	 * Restituisce la lista dei documenti collegati al documento (SOLO ENTRATA).
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaDocumentiCollegati() {
		return SUCCESS;
	}

}
