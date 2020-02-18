/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * Classe generica di model per il Documento Iva Spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/06/2014
 *
 */
public class GenericDocumentoIvaSpesaModel extends GenericDocumentoIvaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9193244828455206340L;
	
	private Integer uidDocumentoCollegato;
	private Integer uidQuotaDocumentoCollegato;
	
	private DocumentoSpesa documento;
	private SubdocumentoSpesa subdocumento;
	private SubdocumentoIvaSpesa subdocumentoIva;
	
	/** Costruttore vuoto di default */
	public GenericDocumentoIvaSpesaModel() {
		super();
	}
	
	/**
	 * @return the tipoSubdocumentoIvaTitolo
	 */
	public String getTipoSubdocumentoIva() {
		return "Spesa";
	}
	
	/**
	 * @return the tipoSubdocumentoIvaTitolo
	 */
	public String getTipoSubdocumentoIvaTitolo() {
		return "spesa";
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
	public DocumentoSpesa getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoSpesa documento) {
		this.documento = documento;
	}

	/**
	 * @return the subdocumento
	 */
	public SubdocumentoSpesa getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(SubdocumentoSpesa subdocumento) {
		this.subdocumento = subdocumento;
	}

	/**
	 * @return the subdocumentoIva
	 */
	public SubdocumentoIvaSpesa getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @param subdocumentoIva the subdocumentoIva to set
	 */
	public void setSubdocumentoIva(SubdocumentoIvaSpesa subdocumentoIva) {
		this.subdocumentoIva = subdocumentoIva;
	}

	/**
	 * Ottiene l'importo del documento non rilevante per l'IVA.
	 * 
	 * @return l'importo non rilevante per l'IVA
	 */
	public BigDecimal getImportoNonRilevanteIva() {
		BigDecimal result = BigDecimal.ZERO;
		DocumentoSpesa doc = getDocumento();
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
		DocumentoSpesa doc = getDocumento();
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
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaSpesa}.
	 * 
	 * @param uid l'uid del subdocumento iva da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaSpesa creaRequestRicercaDettaglioSubdocumentoIvaSpesa(Integer uid) {
		RicercaDettaglioSubdocumentoIvaSpesa request = creaRequest(RicercaDettaglioSubdocumentoIvaSpesa.class);
		request.setSubdocumentoIvaSpesa(creaSubdocumentoIvaSpesa(uid));
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoSpesa}.
	 * 
	 * @param uid l'uid del documento da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioDocumentoSpesa creaRequestRicercaDettaglioDocumentoSpesa(Integer uid) {
		RicercaDettaglioDocumentoSpesa request = creaRequest(RicercaDettaglioDocumentoSpesa.class);
		request.setDocumentoSpesa(creaDocumentoSpesa(uid));
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @param impegno l'impegno da cui ricavare la request
	 * 
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'impegno sia <code>null</code>
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno impegno) {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		
		request.setEnte(getEnte());
		request.setCaricaSub(false);
		request.setSubPaginati(true);
		
		if(impegno != null) {
			RicercaImpegnoK parametroRicercaImpegnoK = new RicercaImpegnoK();
			parametroRicercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
			parametroRicercaImpegnoK.setNumeroImpegno(impegno.getNumero());
			parametroRicercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
			request.setpRicercaImpegnoK(parametroRicercaImpegnoK);
		}
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);

		return request;
	}
	
	
	/**
	 * Crea il subdocumento Iva spesa.
	 * 
	 * @param uid l'uid del subdocumento iva da creare
	 * 
	 * @return il subdocumento creato
	 */
	protected SubdocumentoIvaSpesa creaSubdocumentoIvaSpesa(Integer uid) {
		SubdocumentoIvaSpesa sis = new SubdocumentoIvaSpesa();
		sis.setUid(uid);
		return sis;
	}
	
	/**
	 * Crea un documento di spesa per la ricerca di dettaglio.
	 * 
	 * @param uid l'uid del documento
	 * 
	 * @return il documento avente uid assegnato
	 */
	private DocumentoSpesa creaDocumentoSpesa(Integer uid) {
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(uid);
		return ds;
	}
	
	/**
	 * Imposta il subdocumento Iva per l'injezione del documento intracomunitario.
	 * 
	 * @param subdocumentoIvaSpesa         il subdocumento in cui injettare l'intracomunitario
	 * @param subdocumentoIntracomunitario il subdocumento da injettare
	 */
	protected void impostaSubdocumentoIvaPerIntracomunitario(SubdocumentoIvaSpesa subdocumentoIvaSpesa, SubdocumentoIvaEntrata subdocumentoIntracomunitario) {
		// Imposto il tipo di registro
		getRegistroIvaIntracomunitarioDocumento().setTipoRegistroIva(getTipoRegistroIvaIntracomunitarioDocumento());
		
		int uid = getUidIntracomunitarioDocumento() != null ? getUidIntracomunitarioDocumento() : 0;
		subdocumentoIntracomunitario.setUid(uid);
		subdocumentoIntracomunitario.setRegistroIva(getRegistroIvaIntracomunitarioDocumento());
		subdocumentoIntracomunitario.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIvaIntracomunitarioDocumento()));
		subdocumentoIntracomunitario.setTipoRegistrazioneIva(subdocumentoIvaSpesa.getTipoRegistrazioneIva());
		
		Date dpd = subdocumentoIvaSpesa.getDataProtocolloDefinitivo() != null ?
				subdocumentoIvaSpesa.getDataProtocolloDefinitivo() :
					subdocumentoIvaSpesa.getDataProtocolloProvvisorio();
		subdocumentoIntracomunitario.setDataProtocolloDefinitivo(dpd);
		
		// Imposto il campo come figlio del documento iva
		subdocumentoIvaSpesa.setSubdocumentoIvaEntrata(subdocumentoIntracomunitario);
	}
	
}
