/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.InserisciPrimaNotaIntegrataNotaCreditoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per l'inserimento della prima nota integrata collegata alla nota di credito. Per la spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/02/2016
 */
public abstract class InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseModel extends InserisciPrimaNotaIntegrataNotaCreditoBaseModel<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa,
		ElementoQuotaSpesaRegistrazioneMovFin, DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 5220874714637527174L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "NotaCreditoSpesa";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoSpesa}.
	 * 
	 * @return una request creata.
	 */
	public RicercaDettaglioDocumentoSpesa creaRequestRicercaDettaglioDocumentoSpesa() {
		RicercaDettaglioDocumentoSpesa request = creaRequest(RicercaDettaglioDocumentoSpesa.class);
		
		request.setDocumentoSpesa(getDocumento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoSpesa}.
	 * @param documentoSpesa il documento di spesa
	 * 
	 * @return una request creata.
	 */
	public RicercaDettaglioDocumentoSpesa creaRequestRicercaDettaglioDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		RicercaDettaglioDocumentoSpesa request = creaRequest(RicercaDettaglioDocumentoSpesa.class);
		
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioQuotaSpesa}.
	 * 
	 * @return una request creata.
	 */
	public RicercaDettaglioQuotaSpesa creaRequestRicercaDettaglioQuotaSpesa() {
		RicercaDettaglioQuotaSpesa request = creaRequest(RicercaDettaglioQuotaSpesa.class);
		
		request.setSubdocumentoSpesa(getSubdocumento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la classeDiConciliazione per cui ricercare i conti di conciliazione
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getSubdocumento().getImpegno().getCapitoloUscitaGestione());
		req.setRichiedente(getRichiedente());
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 * @param ds il documento
	 *
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoSpesa creaRequestRicercaQuoteByDocumentoSpesa(DocumentoSpesa ds) {
		RicercaQuoteByDocumentoSpesa request = creaRequest(RicercaQuoteByDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(ds.getUid());
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNoteCreditoIvaDocumentoSpesa}.
	 * @param ds il documento
	 * @return la request creata
	 */
	public RicercaNoteCreditoIvaDocumentoSpesa creaRequestRicercaNoteCreditoIvaDocumentoSpesa(DocumentoSpesa ds) {
		RicercaNoteCreditoIvaDocumentoSpesa request = creaRequest(RicercaNoteCreditoIvaDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(ds.getUid());
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
}
