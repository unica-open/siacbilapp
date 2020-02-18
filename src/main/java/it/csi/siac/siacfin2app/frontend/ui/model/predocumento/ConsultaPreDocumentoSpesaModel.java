/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

/**
 * Classe di model per la consultazione del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/04/2014
 *
 */
public class ConsultaPreDocumentoSpesaModel extends GenericPreDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5481575444709997686L;
	
	private Integer uidPreDocumentoDaConsultare;
	private Documento<?, ?> documento;
	
	/** Costruttore vuoto di default */
	public ConsultaPreDocumentoSpesaModel() {
		setTitolo("Consultazione Predisposizione di Pagamento");
	}

	/**
	 * @return the uidPreDocumentoDaConsultare
	 */
	public Integer getUidPreDocumentoDaConsultare() {
		return uidPreDocumentoDaConsultare;
	}

	/**
	 * @param uidPreDocumentoDaConsultare the uidPreDocumentoDaConsultare to set
	 */
	public void setUidPreDocumentoDaConsultare(Integer uidPreDocumentoDaConsultare) {
		this.uidPreDocumentoDaConsultare = uidPreDocumentoDaConsultare;
	}
	
	/**
	 * @return the documento
	 */
	public Documento<?, ?> getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(Documento<?, ?> documento) {
		this.documento = documento;
	}
	
	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return computeDenominazionePredocumento(getPreDocumento());
	}
	
	/**
	 * @return the elencoPreDocumento
	 */
	public String getElencoPreDocumento() {
		if(getPreDocumento() == null || getPreDocumento().getElencoDocumentiAllegato() == null) {
			return "";
		}
		return new StringBuilder()
				.append(getPreDocumento().getElencoDocumentiAllegato().getAnno())
				.append("/")
				.append(getPreDocumento().getElencoDocumentiAllegato().getNumero())
				.toString();
	}

	/* **** Requests **** */

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
		preDocumento.setUid(getUidPreDocumentoDaConsultare());
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
		
		impostaAttoAmministrativo(preDocumentoSpesa.getAttoAmministrativo());
		if(preDocumentoSpesa.getSubDocumento() != null) {
			Ordinativo ord = preDocumentoSpesa.getSubDocumento().getOrdinativo();
			//SIAC-6428
			setDocumento(preDocumentoSpesa.getSubDocumento().getDocumento());
			
			//log.info("impostaPreDocumento", "setto l'ordinativo " + preDocumentoSpesa.getSubDocumento().getOrdinativo() );
			
			if (ord!=null){
				setOrdinativoSubCollegato(ord);					
				setCapitolo( preDocumentoSpesa.getSubDocumento().getImpegno().getCapitoloUscitaGestione());
				setMovimentoGestioneOrdinativo(preDocumentoSpesa.getSubDocumento().getImpegno());		
				setSoggettoOrdinativo(ord.getSoggetto());
			}			
		}
	}

}
