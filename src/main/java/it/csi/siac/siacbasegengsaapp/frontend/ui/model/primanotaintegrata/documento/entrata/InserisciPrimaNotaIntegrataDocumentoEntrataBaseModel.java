/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.InserisciPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoEntrataHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per l'inserimento della prima nota integrata collegata al documento. Per l'entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public abstract class InserisciPrimaNotaIntegrataDocumentoEntrataBaseModel extends InserisciPrimaNotaIntegrataDocumentoBaseModel<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata,
		ElementoQuotaEntrataRegistrazioneMovFin, DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ConsultaRegistrazioneMovFinDocumentoEntrataHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 2060100614843634276L;

	@Override
	public String getConsultazioneSubpath() {
		return "DocumentoEntrata";
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
		req.setCapitolo(getSubdocumento().getAccertamento().getCapitoloEntrataGestione());
		req.setSoggetto(getDocumento().getSoggetto());
		req.setRichiedente(getRichiedente());
		return req;
	}

}
