/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.Date;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoSpesaHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Consultazione della registrazione per il Documento di Spesa. Classe base
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinDocumentoSpesaBaseModel extends ConsultaRegistrazioneMovFinDocumentoBaseModel<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ConsultaRegistrazioneMovFinDocumentoSpesaHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 3902603889965386684L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "DocumentoSpesa";
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 *
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoSpesa creaRequestRicercaQuoteByDocumentoSpesa() {
		RicercaQuoteByDocumentoSpesa request = creaRequest(RicercaQuoteByDocumentoSpesa.class);
		request.setDocumentoSpesa(getDocumento());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 *
	 * @return la request creata
	 */
	public RicercaOnereByDocumentoSpesa creaRequestRicercaOnereByDocumentoSpesa() {
		RicercaOnereByDocumentoSpesa request = creaRequest(RicercaOnereByDocumentoSpesa.class);
		request.setDocumentoSpesa(getDocumento());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoSpesa}.
	 * 
	 * @param uidDocumentoSpesa l'uid del documento di spesa
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDettaglioDocumentoSpesa creaRequestRicercaDettaglioDocumentoSpesa(Integer uidDocumentoSpesa) {
		RicercaDettaglioDocumentoSpesa request = new RicercaDettaglioDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumentoSpesa);
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}

}
