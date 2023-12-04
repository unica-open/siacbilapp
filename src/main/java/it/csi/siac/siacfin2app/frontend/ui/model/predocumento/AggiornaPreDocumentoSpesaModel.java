/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Classe di model per l'aggiornamento del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/04/2014
 *
 */
public class AggiornaPreDocumentoSpesaModel extends GenericPreDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5481575444709997686L;
	
	private Integer uidPreDocumentoDaAggiornare;
	
	/** Costruttore vuoto di default */
	public AggiornaPreDocumentoSpesaModel() {
		setTitolo("Aggiornamento Predisposizione di Pagamento");
		setNomeAzioneDecentrata(BilConstants.AGGIORNA_PREDOCUMENTO_SPESA_DECENTRATO.getConstant());
	}

	/**
	 * @return the uidPreDocumentoDaAggiornare
	 */
	public Integer getUidPreDocumentoDaAggiornare() {
		return uidPreDocumentoDaAggiornare;
	}

	/**
	 * @param uidPreDocumentoDaAggiornare the uidPreDocumentoDaAggiornare to set
	 */
	public void setUidPreDocumentoDaAggiornare(Integer uidPreDocumentoDaAggiornare) {
		this.uidPreDocumentoDaAggiornare = uidPreDocumentoDaAggiornare;
	}

	/* ***** Requests ***** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaPreDocumentoDiSpesa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaPreDocumentoDiSpesa creaRequestAggiornaPreDocumentoDiSpesa() {
		AggiornaPreDocumentoDiSpesa request = new AggiornaPreDocumentoDiSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoSpesa preDocumento = getPreDocumento();
		
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumento.setCausaleSpesa(getCausaleSpesa());
		preDocumento.setContoTesoreria(getContoTesoreria());
		
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		preDocumento.setModalitaPagamentoSoggetto(impostaEntitaFacoltativa(getModalitaPagamentoSoggetto()));
		preDocumento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));

		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
		preDocumento.setProvvisorioDiCassa(getProvvisorioCassa());
		request.setPreDocumentoSpesa(preDocumento);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPreDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPreDocumentoSpesa creaRequestRicercaDettaglioPreDocumentoSpesa() {
		RicercaDettaglioPreDocumentoSpesa request = new RicercaDettaglioPreDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoSpesa preDocumento = new PreDocumentoSpesa();
		preDocumento.setUid(getUidPreDocumentoDaAggiornare());
		request.setPreDocumentoSpesa(preDocumento);
		
		return request;
	}

	/**
	 * Imposta i dati del PreDocumento di Spesa all'interno del model.
	 *  
	 * @param preDocumentoSpesa il predocumento i cui dati devono essere impostati
	 */
	public void impostaPreDocumento(PreDocumentoSpesa preDocumentoSpesa) {
		setPreDocumento(preDocumentoSpesa);
		setCapitolo(preDocumentoSpesa.getCapitoloUscitaGestione());
		setSoggetto(preDocumentoSpesa.getSoggetto());
		setMovimentoGestione(preDocumentoSpesa.getImpegno());
		setSubMovimentoGestione(preDocumentoSpesa.getSubImpegno());
		
		setDatiAnagraficiPreDocumento(preDocumentoSpesa.getDatiAnagraficiPreDocumento());
		setCausaleSpesa(preDocumentoSpesa.getCausaleSpesa());
		setContoTesoreria(preDocumentoSpesa.getContoTesoreria());
		setSedeSecondariaSoggetto(preDocumentoSpesa.getSedeSecondariaSoggetto());
		setModalitaPagamentoSoggetto(preDocumentoSpesa.getModalitaPagamentoSoggetto());
		setStrutturaAmministrativoContabile(preDocumentoSpesa.getStrutturaAmministrativoContabile());
		setTipoCausale(getCausaleSpesa().getTipoCausale());
		
		impostaAttoAmministrativo(preDocumentoSpesa.getAttoAmministrativo());
	}

}
