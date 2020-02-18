/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

/**
 * Classe di model per la consultazione del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/04/2014
 *
 */
public class ConsultaPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7034890123965943596L;
	
	private Integer uidPreDocumentoDaConsultare;
	private Documento<?, ?> documento;
	
	/** Costruttore vuoto di default */
	public ConsultaPreDocumentoEntrataModel() {
		setTitolo("Consultazione Predisposizione di Incasso");
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
	 * Crea una request per il servizio di {@link RicercaDettaglioPreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPreDocumentoEntrata creaRequestRicercaDettaglioPreDocumentoEntrata() {
		RicercaDettaglioPreDocumentoEntrata request = new RicercaDettaglioPreDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = new PreDocumentoEntrata();
		preDocumento.setUid(getUidPreDocumentoDaConsultare());
		request.setPreDocumentoEntrata(preDocumento);
		
		return request;
	}

	/**
	 * Imposta i dati del PreDocumento di Entrata all'interno del model.
	 *  
	 * @param preDocumentoEntrata il predocumento i cui dati devono essere impostati
	 */
	public void impostaPreDocumento(PreDocumentoEntrata preDocumentoEntrata) {
		setPreDocumento(preDocumentoEntrata);
		setCapitolo(preDocumentoEntrata.getCapitoloEntrataGestione());
		setSoggetto(preDocumentoEntrata.getSoggetto());
		setMovimentoGestione(preDocumentoEntrata.getAccertamento());
		setSubMovimentoGestione(preDocumentoEntrata.getSubAccertamento());
		
		impostaAttoAmministrativo(preDocumentoEntrata.getAttoAmministrativo());
		
		//log.info("impostaPreDocumento", "testo se subdoc è null " +preDocumentoEntrata.getSubDocumento() != null );
		
		if(preDocumentoEntrata.getSubDocumento() != null) {
			setDocumento(preDocumentoEntrata.getSubDocumento().getDocumento());		
			log.info("impostaPreDocumento", "setto il documento " + preDocumentoEntrata.getSubDocumento().getDocumento() );
			
			//SIAC-6428
			Ordinativo ord = preDocumentoEntrata.getSubDocumento().getOrdinativo();
			if (ord!=null){
				setOrdinativoSubCollegato(ord);
				setCapitolo( preDocumentoEntrata.getSubDocumento().getAccertamento().getCapitoloEntrataGestione());
				setMovimentoGestioneOrdinativo(preDocumentoEntrata.getSubDocumento().getAccertamento());		
				setSoggettoOrdinativo(ord.getSoggetto());
			}
			
		}
	}

}
