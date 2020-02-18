/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.util.wrapper;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;

/**
 * Wrapper per la gestione della fattura FEL-
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 *
 */
public class ElementoFatturaFEL implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5172876738949679539L;
	
	private final FatturaFEL fatturaFEL;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param fatturaFEL la fattura da wrappare
	 */
	public ElementoFatturaFEL(FatturaFEL fatturaFEL) {
		this.fatturaFEL = fatturaFEL;
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
	
	/**
	 * @return the fornitore
	 */
	public String getFornitore() {
		StringBuilder sb = new StringBuilder();
		
		if(fatturaFEL != null && fatturaFEL.getPrestatore() != null) {
			sb.append(fatturaFEL.getPrestatore().getCodicePrestatore());
			sb.append(" - ");
			if(StringUtils.isNotBlank(fatturaFEL.getPrestatore().getDenominazionePrestatore())){
				sb.append(fatturaFEL.getPrestatore().getDenominazionePrestatore());
			} else {
				String nomePrestatore = fatturaFEL.getPrestatore().getNomePrestatore();
				if(StringUtils.isNotBlank(nomePrestatore)){
					sb.append(nomePrestatore).append(" ");
				}
				String cognomePrestatore = fatturaFEL.getPrestatore().getCognomePrestatore();
				if(StringUtils.isNotBlank(cognomePrestatore)){
					sb.append(cognomePrestatore);
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the dataEmissione
	 */
	public String getDataEmissione() {
		return fatturaFEL != null ? FormatUtils.formatDate(fatturaFEL.getData()) : "";
	}
	
	/**
	 * @return the dataRicezione
	 */
	public String getDataRicezione() {
		return fatturaFEL != null && fatturaFEL.getPortaleFattureFEL() != null ? FormatUtils.formatDate(fatturaFEL.getPortaleFattureFEL().getDataRicezione()) : "";
	}
	
	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return fatturaFEL != null ? fatturaFEL.getNumero() : "";
	}
	
	/**
	 * @return the tipoDocumentoFEL
	 */
	public String getTipoDocumentoFEL() {
		return fatturaFEL != null && fatturaFEL.getTipoDocumentoFEL() != null ? fatturaFEL.getTipoDocumentoFEL().getCodice() : "";
	}
	
	/**
	 * @return the dataAcquisizione
	 */
	public String getDataAcquisizione() {
		return fatturaFEL != null ? FormatUtils.formatDate(fatturaFEL.getDataCaricamento()) : "";
	}
	
	/**
	 * @return the statoAcquisizione
	 */
	public String getStatoAcquisizione() {
		return fatturaFEL != null && fatturaFEL.getStatoAcquisizioneFEL() != null ? fatturaFEL.getStatoAcquisizioneFEL().getDescrizione() : "";
	}
	
	/**
	 * @return the importoLordo
	 */
	public String getImportoLordo() {
		return fatturaFEL != null ? FormatUtils.formatCurrency(fatturaFEL.getImportoTotaleDocumento()) : "";
	}
	
	/**
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return getCodicePrestatoreByLength(16);
	}
	
	/**
	 * @return the partitaIva
	 */
	public String getPartitaIva() {
		return getCodicePrestatoreByLength(11);
	}
	
	/**
	 * Ottiene il codice del prestatore a seconda della lunghezza richiesta
	 * 
	 * @param length la lunghezza richiesta
	 * @return il codice corrispondente dalla lunghezza fornita
	 */
	private String getCodicePrestatoreByLength(int length) {
		return fatturaFEL != null
				&& fatturaFEL.getPrestatore() != null
				&& StringUtils.isNotBlank(fatturaFEL.getPrestatore().getCodicePrestatore())
				&& fatturaFEL.getPrestatore().getCodicePrestatore().length() == length
					? fatturaFEL.getPrestatore().getCodicePrestatore()
					: "";
	}
	
	/**
	 * @return the statoOperativoDaImportare
	 */
	public boolean isStatoOperativoDaImportare() {
		return fatturaFEL != null && StatoAcquisizioneFEL.DA_ACQUISIRE.equals(fatturaFEL.getStatoAcquisizioneFEL());
	}
	
	/**
	 * @return the statoOperativoImportata
	 */
	public boolean isStatoOperativoImportata() {
		return fatturaFEL != null && StatoAcquisizioneFEL.IMPORTATA.equals(fatturaFEL.getStatoAcquisizioneFEL());
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return fatturaFEL != null && StringUtils.isNotBlank(fatturaFEL.getNote()) ? fatturaFEL.getNote() : "";
	}

	@Override
	public int getUid() {
		return fatturaFEL != null && fatturaFEL.getIdFattura() != null ? fatturaFEL.getIdFattura().intValue() : 0;
	}
	
}
