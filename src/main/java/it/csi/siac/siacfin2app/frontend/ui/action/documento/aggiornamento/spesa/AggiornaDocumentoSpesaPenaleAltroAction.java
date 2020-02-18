/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;

/**
 * Classe di action per l'aggiornamento del Documento di spesa, sezione Penale/Altro.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaPenaleAltroAction extends AggiornaDocumentoSpesaBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3356621434620265777L;

	/**
	 * Restituisce la lista delle penali / altro relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaPenaleAltro() {
		return SUCCESS;
	}
	
	/**
	 * Redirezione verso l'inserimento di un documento di entrata.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneInserimentoDocumentoEntrata() {
		final String methodName = "redirezioneInserimentoDocumentoEntrata";
		log.info(methodName, "Redirezione verso l'inserimento di un documento di entrata per Penale/Altro");
		return SUCCESS;
	}
	
}
