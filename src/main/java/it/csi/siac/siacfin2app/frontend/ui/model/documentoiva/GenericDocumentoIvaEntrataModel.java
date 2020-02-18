/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import java.math.BigDecimal;
import java.util.EnumSet;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
 * Classe generica di model per il Documento Iva Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/06/2014
 *
 */
public class GenericDocumentoIvaEntrataModel extends GenericDocumentoIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4927862081458623419L;
	
	private Integer uidDocumentoCollegato;
	private Integer uidQuotaDocumentoCollegato;
	
	private DocumentoEntrata documento;
	private SubdocumentoEntrata subdocumento;
	private SubdocumentoIvaEntrata subdocumentoIva;
	
	/** Costruttore vuoto di default */
	public GenericDocumentoIvaEntrataModel() {
		super();
	}
	
	/**
	 * @return the tipoSubdocumentoIvaTitolo
	 */
	public String getTipoSubdocumentoIva() {
		return "Entrata";
	}
	
	/**
	 * @return the tipoSubdocumentoIvaTitolo
	 */
	public String getTipoSubdocumentoIvaTitolo() {
		return "entrata";
	}
	

	/**
	 * @return the uidDocumentoCollegato
	 */
	public Integer getUidDocumentoCollegato() {
		return uidDocumentoCollegato;
	}

	/**
	 * @param uidDocumentoCollegato the uidDocumentoCollegato to set
	 */
	public void setUidDocumentoCollegato(Integer uidDocumentoCollegato) {
		this.uidDocumentoCollegato = uidDocumentoCollegato;
	}

	/**
	 * @return the uidQuotaDocumentoCollegato
	 */
	public Integer getUidQuotaDocumentoCollegato() {
		return uidQuotaDocumentoCollegato;
	}

	/**
	 * @param uidQuotaDocumentoCollegato the uidQuotaDocumentoCollegato to set
	 */
	public void setUidQuotaDocumentoCollegato(Integer uidQuotaDocumentoCollegato) {
		this.uidQuotaDocumentoCollegato = uidQuotaDocumentoCollegato;
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
	}

	/**
	 * @return the subdocumento
	 */
	public SubdocumentoEntrata getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(SubdocumentoEntrata subdocumento) {
		this.subdocumento = subdocumento;
	}

	/**
	 * @return the subdocumentoIva
	 */
	public SubdocumentoIvaEntrata getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @param subdocumentoIva the subdocumentoIva to set
	 */
	public void setSubdocumentoIva(SubdocumentoIvaEntrata subdocumentoIva) {
		this.subdocumentoIva = subdocumentoIva;
	}

	/**
	 * Ottiene l'importo del documento non rilevante per l'IVA.
	 * 
	 * @return l'importo non rilevante per l'IVA
	 */
	public BigDecimal getImportoNonRilevanteIva() {
		BigDecimal result = BigDecimal.ZERO;
		DocumentoEntrata doc = getDocumento();
		if(doc != null) {
			result = doc.getImporto().subtract(doc.calcolaImportoTotaleRilevanteIVASubdoumenti());
		}
		return result;
	}
	
	/**
	 * Ottiene l'importo del documento rilevante per l'IVA.
	 * 
	 * @return l'importo rilevante per l'IVA
	 */
	public BigDecimal getImportoRilevanteIva() {
		BigDecimal result = BigDecimal.ZERO;
		DocumentoEntrata doc = getDocumento();
		if(doc != null) {
			result = doc.calcolaImportoTotaleRilevanteIVASubdoumenti();
		}
		return result;
	}
	
	/**
	 * @return the datiRegistroIva
	 */
	public String getDatiRegistroIva() {
		if(getSubdocumentoIva() == null || getSubdocumentoIva().getRegistroIva() == null) {
			return "";
		}
		return getSubdocumentoIva().getRegistroIva().getCodice() + " - " + getSubdocumentoIva().getRegistroIva().getDescrizione();
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaEntrata}.
	 * 
	 * @param uid l'uid del subdocumento iva da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaEntrata creaRequestRicercaDettaglioSubdocumentoIvaEntrata(Integer uid) {
		RicercaDettaglioSubdocumentoIvaEntrata request = creaRequest(RicercaDettaglioSubdocumentoIvaEntrata.class);
		request.setSubdocumentoIvaEntrata(creaSubdocumentoIvaEntrata(uid));
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoEntrata}.
	 * 
	 * @param uid l'uid del documento da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioDocumentoEntrata creaRequestRicercaDettaglioDocumentoEntrata(Integer uid) {
		RicercaDettaglioDocumentoEntrata request = creaRequest(RicercaDettaglioDocumentoEntrata.class);
		request.setDocumentoEntrata(creaDocumentoEntrata(uid));
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @param accertamento l'impegno da cui ricavare la request
	 * 
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'impegno sia <code>null</code>
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento) {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		if(accertamento != null) {
			RicercaAccertamentoK parametroRicercaAccertamentoK = new RicercaAccertamentoK();
			parametroRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
			parametroRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumero());
			parametroRicercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
			request.setpRicercaAccertamentoK(parametroRicercaAccertamentoK);
		}
		
		request.setEnte(getEnte());
		request.setCaricaSub(false);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 
		
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		return request;
	}
	
	/**
	 * Crea il subdocumento Iva entrata.
	 * 
	 * @param uid l'uid del subdocumento iva da creare
	 * 
	 * @return il subdocumento creato
	 */
	protected SubdocumentoIvaEntrata creaSubdocumentoIvaEntrata(Integer uid) {
		SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
		sie.setUid(uid);
		return sie;
	}
	
	/**
	 * Crea un documento di entrata per la ricerca di dettaglio.
	 * 
	 * @param uid l'uid del documento
	 * 
	 * @return il documento avente uid assegnato
	 */
	private DocumentoEntrata creaDocumentoEntrata(Integer uid) {
		DocumentoEntrata de = new DocumentoEntrata();
		de.setUid(uid);
		return de;
	}
	
}
