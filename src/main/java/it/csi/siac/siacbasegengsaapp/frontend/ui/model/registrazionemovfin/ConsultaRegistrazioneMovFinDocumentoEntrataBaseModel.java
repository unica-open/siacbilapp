/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.Date;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoEntrataHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Consultazione della registrazione per il Documento di Entrata. Classe base
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel extends ConsultaRegistrazioneMovFinDocumentoBaseModel<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ConsultaRegistrazioneMovFinDocumentoEntrataHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4099404008095767723L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "DocumentoEntrata";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoEntrata}.
	 * 
	 * @param uidDocumentoEntrata l'uid del documento di entrata
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDettaglioDocumentoEntrata creaRequestRicercaDettaglioDocumentoEntrata(Integer uidDocumentoEntrata) {
		RicercaDettaglioDocumentoEntrata request = new RicercaDettaglioDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumentoEntrata);
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoEntrata creaRequestRicercaQuoteByDocumentoEntrata() {
		RicercaQuoteByDocumentoEntrata request = new RicercaQuoteByDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setDocumentoEntrata(getDocumento());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
}
