/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Wrapper per Elaborazione Flusso.
 * 
 * @author Gambino Vincenzo
 * @version 1.0.0 - 09/07/2020
 *
 */
public class ElementoRiconciliazioneDoc implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2835230601000217374L;
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private RiconciliazioneDoc riconciliazioneDoc;
	

	private String azioni;
//	private String descrizione;
//	private String tipoOperazioneDocumento;
//	private String codiceFiscale;
//	private String iuv;
//	private String ragioneSociale;
//	private BigDecimal importo;
//	
//	private String codiceVoce;
//	private String descrizioneVoce;
//	private String codiceSottovoce;
	

	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoRiconciliazioneDoc(RiconciliazioneDoc riconciliazioneDoc) {
		this.riconciliazioneDoc = riconciliazioneDoc;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	@Override
	public int getUid() {
		return riconciliazioneDoc == null ? 0 : riconciliazioneDoc.getUid();
	}


	/**
	 * @return the riconciliazioneErrore
	 */
	public RiconciliazioneDoc getRiconciliazioneDoc() {
		return riconciliazioneDoc;
	}

	/**
	 * @param riconciliazioneErrore the riconciliazioneErrore to set
	 */
	public void setRiconciliazioneDoc(RiconciliazioneDoc riconciliazioneDoc) {
		this.riconciliazioneDoc = riconciliazioneDoc;
	}
	
	// Utilita' per il Javascript

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		if(this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getErrore()!= null &&
				this.riconciliazioneDoc.getErrore().getDescrizione()!= null){
			return this.riconciliazioneDoc.getErrore().getDescrizione();
		}
		return "";
	}
	
	
	/**
	 * @return the tipoOperazioneDocumento
	 */
	public String getTipoOperazioneDocumento() {
		if(this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getTipoDocumento()!= null){
			return this.riconciliazioneDoc.getTipoDocumento().getDescrizione();
		}
		return "";
	}

	/**
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getCodiceFiscale()!= null) ? this.riconciliazioneDoc.getCodiceFiscale() : "";
	}

	/**
	 * @return the iuv
	 */
	public String getIuv() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getIUV()!= null) ? this.riconciliazioneDoc.getIUV() : "";
	}

	/**
	 * @return the ragioneSociale
	 */
	public String getRagioneSociale() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getRagioneSociale()!= null) ? this.riconciliazioneDoc.getRagioneSociale() : "";
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getImporto()!= null) ? this.riconciliazioneDoc.getImporto() : BigDecimal.ZERO;
	}

	/**
	 * @return the codiceVoce
	 */
	public String getCodiceVoce() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getCodiceVoce()!= null) ? this.riconciliazioneDoc.getCodiceVoce() : "";
	}

	/**
	 * @return the descrizioneVoce
	 */
	public String getDescrizioneVoce() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getDescrizioneVoce()!= null) ? this.riconciliazioneDoc.getDescrizioneVoce() : "";
	}

	/**
	 * @return the codiceSottovoce
	 */
	public String getCodiceSottovoce() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getCodiceSottovoce()!= null) ? this.riconciliazioneDoc.getCodiceSottovoce() : "";
	}

	
}
