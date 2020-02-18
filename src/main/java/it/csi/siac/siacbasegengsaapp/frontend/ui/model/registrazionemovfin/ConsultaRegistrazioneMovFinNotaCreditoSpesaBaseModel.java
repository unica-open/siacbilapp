/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Consultazione della registrazione per la nota credito di Spesa. Classe base
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2016
 *
 */
public abstract class ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel extends ConsultaRegistrazioneMovFinDocumentoBaseModel<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 3902603889965386684L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "NotaCreditoSpesa";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 * @param uidDocumentoSpesa l'uid del documento
	 *
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoSpesa creaRequestRicercaQuoteByDocumentoSpesa(int uidDocumentoSpesa) {
		RicercaQuoteByDocumentoSpesa request = creaRequest(RicercaQuoteByDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumentoSpesa);
		request.setDocumentoSpesa(documentoSpesa);
		
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
	public RicercaDettaglioDocumentoSpesa creaRequestRicercaDettaglioDocumentoSpesa(int uidDocumentoSpesa) {
		RicercaDettaglioDocumentoSpesa request = creaRequest(RicercaDettaglioDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumentoSpesa);
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNoteCreditoIvaDocumentoSpesa}.
	 * @param uidDocumentoSpesa l'uid del documento
	 * @return la request creata
	 */
	public RicercaNoteCreditoIvaDocumentoSpesa creaRequestRicercaNoteCreditoIvaDocumentoSpesa(int uidDocumentoSpesa) {
		RicercaNoteCreditoIvaDocumentoSpesa request = creaRequest(RicercaNoteCreditoIvaDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumentoSpesa);
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}

}
