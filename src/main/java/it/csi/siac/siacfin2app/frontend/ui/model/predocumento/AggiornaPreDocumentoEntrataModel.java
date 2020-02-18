/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Classe di model per l'aggiornamento del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/04/2014
 *
 */
public class AggiornaPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6695121258199378514L;
	
	private Integer uidPreDocumentoDaAggiornare;
	private boolean utenteDecentrato;
	
	/** Costruttore vuoto di default */
	public AggiornaPreDocumentoEntrataModel() {
		setTitolo("Aggiornamento Predisposizione di Incasso");
		setNomeAzioneDecentrata(BilConstants.AGGIORNA_PREDOCUMENTO_ENTRATA_DECENTRATO.getConstant());
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

	/**
	 * @return the utenteDecentrato
	 */
	public boolean isUtenteDecentrato() {
		return utenteDecentrato;
	}
	
	/**
	 * @param utenteDecentrato the utenteDecentrato to set
	 */
	public void setUtenteDecentrato(boolean utenteDecentrato) {
		this.utenteDecentrato = utenteDecentrato;
	}
	
	/* ***** Requests ***** */

	/**
	 * Crea una request per il servizio di {@link AggiornaPreDocumentoDiEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaPreDocumentoDiEntrata creaRequestAggiornaPreDocumentoDiEntrata() {
		AggiornaPreDocumentoDiEntrata request = new AggiornaPreDocumentoDiEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = getPreDocumento();
		
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumento.setCausaleEntrata(getCausaleEntrata());
		
		preDocumento.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		preDocumento.setProvvisorioDiCassa(getProvvisorioCassa());
		
		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
		request.setPreDocumentoEntrata(preDocumento);
		request.setBilancio(getBilancio());
		request.setGestisciModificaImportoAccertamento(getForzaDisponibilitaAccertamento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPreDocumentoEntrata creaRequestRicercaDettaglioPreDocumentoEntrata() {
		RicercaDettaglioPreDocumentoEntrata request = new RicercaDettaglioPreDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = new PreDocumentoEntrata();
		preDocumento.setUid(getUidPreDocumentoDaAggiornare());
		request.setPreDocumentoEntrata(preDocumento);
		
		return request;
	}

	/**
	 * Imposta i dati del PreDocumento di Spesa all'interno del model.
	 *  
	 * @param preDocumentoSpesa il predocumento i cui dati devono essere impostati
	 */
	public void impostaPreDocumento(PreDocumentoEntrata preDocumentoSpesa) {
		setPreDocumento(preDocumentoSpesa);
		setCapitolo(preDocumentoSpesa.getCapitoloEntrataGestione());
		setSoggetto(preDocumentoSpesa.getSoggetto());
		setMovimentoGestione(preDocumentoSpesa.getAccertamento());
		setSubMovimentoGestione(preDocumentoSpesa.getSubAccertamento());
		
		setDatiAnagraficiPreDocumento(preDocumentoSpesa.getDatiAnagraficiPreDocumento());
		setCausaleEntrata(preDocumentoSpesa.getCausaleEntrata());
		setContoCorrente(preDocumentoSpesa.getContoCorrente());
		setStrutturaAmministrativoContabile(preDocumentoSpesa.getStrutturaAmministrativoContabile());
		setTipoCausale(getCausaleEntrata().getTipoCausale());
		
		impostaAttoAmministrativo(preDocumentoSpesa.getAttoAmministrativo());
		
		// SIAC-4492
		setCausaleOriginale(preDocumentoSpesa.getCausaleEntrata());
	}

}
