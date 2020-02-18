/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.Date;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Consultazione della registrazione per la nota credito di Entrata. Classe base
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2016
 *
 */
public abstract class ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel extends ConsultaRegistrazioneMovFinDocumentoBaseModel<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 3902603889965386684L;
	
	private DocumentoEntrata documentoOriginale;
	
	/**
	 * @return the documentoOriginale
	 */
	public DocumentoEntrata getDocumentoOriginale() {
		return documentoOriginale;
	}

	/**
	 * @param documentoOriginale the documentoOriginale to set
	 */
	public void setDocumentoOriginale(DocumentoEntrata documentoOriginale) {
		this.documentoOriginale = documentoOriginale;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "NotaCreditoEntrata";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoEntrata}.
	 * @param uidDocumentoEntrata  l'uid del documento di entrata
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoEntrata creaRequestRicercaQuoteByDocumentoEntrata(int uidDocumentoEntrata) {
		RicercaQuoteByDocumentoEntrata request = creaRequest(RicercaQuoteByDocumentoEntrata.class);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumentoEntrata);
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoEntrata}.
	 * 
	 * @param uidDocumentoEntrata l'uid del documento di spesa
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDettaglioDocumentoEntrata creaRequestRicercaDettaglioDocumentoEntrata(int uidDocumentoEntrata) {
		RicercaDettaglioDocumentoEntrata request = new RicercaDettaglioDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumentoEntrata);
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNoteCreditoIvaDocumentoEntrata}.
	 * @param uidDocumentoEntrata l'uid del documento di spesa
	 * @return la request creata
	 */
	public RicercaNoteCreditoIvaDocumentoEntrata creaRequestRicercaNoteCreditoIvaDocumentoEntrata(int uidDocumentoEntrata) {
		RicercaNoteCreditoIvaDocumentoEntrata request = creaRequest(RicercaNoteCreditoIvaDocumentoEntrata.class);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumentoEntrata);
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}

}
