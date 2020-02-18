/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.InserisciPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoSpesaHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per l'inserimento della prima nota integrata collegata al documento. Per la spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public abstract class InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel extends InserisciPrimaNotaIntegrataDocumentoBaseModel<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa,
		ElementoQuotaSpesaRegistrazioneMovFin, DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ConsultaRegistrazioneMovFinDocumentoSpesaHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 5220874714637527174L;
	
	@Override
	public String getConsultazioneSubpath() {
		return "DocumentoSpesa";
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
	 *
	 * @return la request creata
	 */
	public RicercaOnereByDocumentoSpesa creaRequestRicercaOnereByDocumentoSpesa() {
		RicercaOnereByDocumentoSpesa request = creaRequest(RicercaOnereByDocumentoSpesa.class);
		request.setDocumentoSpesa(getDocumento());
		return request;
	}
	
}
