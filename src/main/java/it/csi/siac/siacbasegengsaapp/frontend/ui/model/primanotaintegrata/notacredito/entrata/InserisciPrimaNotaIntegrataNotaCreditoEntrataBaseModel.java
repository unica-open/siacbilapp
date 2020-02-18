/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.InserisciPrimaNotaIntegrataNotaCreditoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per l'inserimento della prima nota integrata collegata alla nota di credito. Per l'entrata.
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 */
public abstract class InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseModel extends InserisciPrimaNotaIntegrataNotaCreditoBaseModel<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata,
		ElementoQuotaEntrataRegistrazioneMovFin, DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 5220874714637527174L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "NotaCreditoEntrata";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoEntrata}.
	 * 
	 * @return una request creata.
	 */
	public RicercaDettaglioDocumentoEntrata creaRequestRicercaDettaglioDocumentoEntrata() {
		RicercaDettaglioDocumentoEntrata request = creaRequest(RicercaDettaglioDocumentoEntrata.class);
		
		request.setDocumentoEntrata(getDocumento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoEntrata}.
	 * @param documentoEntrata il documento di entrata
	 * 
	 * @return una request creata.
	 */
	public RicercaDettaglioDocumentoEntrata creaRequestRicercaDettaglioDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		RicercaDettaglioDocumentoEntrata request = creaRequest(RicercaDettaglioDocumentoEntrata.class);
		
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioQuotaEntrata}.
	 * 
	 * @return una request creata.
	 */
	public RicercaDettaglioQuotaEntrata creaRequestRicercaDettaglioQuotaEntrata() {
		RicercaDettaglioQuotaEntrata request = creaRequest(RicercaDettaglioQuotaEntrata.class);
		
		request.setSubdocumentoEntrata(getSubdocumento());
		
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
		req.setSoggetto(getDocumento().getSoggetto());
		req.setCapitolo(getSubdocumento().getAccertamento().getCapitoloEntrataGestione());
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoEntrata}.
	 * @param de il documento
	 *
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoEntrata creaRequestRicercaQuoteByDocumentoEntrata(DocumentoEntrata de) {
		RicercaQuoteByDocumentoEntrata request = creaRequest(RicercaQuoteByDocumentoEntrata.class);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(de.getUid());
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNoteCreditoIvaDocumentoEntrata}.
	 * @param de il documento
	 * @return la request creata
	 */
	public RicercaNoteCreditoIvaDocumentoEntrata creaRequestRicercaNoteCreditoIvaDocumentoEntrata(DocumentoEntrata de) {
		RicercaNoteCreditoIvaDocumentoEntrata request = creaRequest(RicercaNoteCreditoIvaDocumentoEntrata.class);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(de.getUid());
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}
	
}
