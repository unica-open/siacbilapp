/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Classe di wrap per il capitolo durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 11/12/2013
 * 
 */
public class ElementoDettaglioVariazioneCodificaCapitolo implements Serializable, ModelWrapper {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -5023444094707092284L;

	private Integer uid;
	private String descrizioneCapitoloPreVariazione;
	private String descrizioneArticoloPreVariazione;
	private String strutturaAmministrativaContabileCapitoloPreVariazione;
	private Integer numeroVariazione;
	private String provvedimentoVariazione;
	private String dataVariazione;
//	private Capitolo<?,?> capitoloPreVariazione;
//	private VariazioneCodificaCapitolo variazione;
	
	
	@Override
	public int getUid() {
		return uid != null ? uid.intValue() : 0;
	}


	/**
	 * @return the descrizioneCapitoloPreVariazione
	 */
	public String getDescrizioneCapitoloPreVariazione() {
		return descrizioneCapitoloPreVariazione;
	}


	/**
	 * @return the descrizioneArticoloPreVariazione
	 */
	public String getDescrizioneArticoloPreVariazione() {
		return descrizioneArticoloPreVariazione;
	}


	/**
	 * @return the strutturaAmministrativaContabileCapitoloPreVariazione
	 */
	public String getStrutturaAmministrativaContabileCapitoloPreVariazione() {
		return strutturaAmministrativaContabileCapitoloPreVariazione;
	}


	/**
	 * @return the numeroVariazione
	 */
	public Integer getNumeroVariazione() {
		return numeroVariazione;
	}


	/**
	 * @return the provvedimentoVariazione
	 */
	public String getProvvedimentoVariazione() {
		return provvedimentoVariazione;
	}


	/**
	 * @return the dataVariazione
	 */
	public String getDataVariazione() {
		return dataVariazione;
	}


	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}


	/**
	 * @param descrizioneCapitoloPreVariazione the descrizioneCapitoloPreVariazione to set
	 */
	public void setDescrizioneCapitoloPreVariazione(String descrizioneCapitoloPreVariazione) {
		this.descrizioneCapitoloPreVariazione = descrizioneCapitoloPreVariazione;
	}


	/**
	 * @param descrizioneArticoloPreVariazione the descrizioneArticoloPreVariazione to set
	 */
	public void setDescrizioneArticoloPreVariazione(String descrizioneArticoloPreVariazione) {
		this.descrizioneArticoloPreVariazione = descrizioneArticoloPreVariazione;
	}


	/**
	 * @param strutturaAmministrativaContabileCapitoloPreVariazione the strutturaAmministrativaContabileCapitoloPreVariazione to set
	 */
	public void setStrutturaAmministrativaContabileCapitoloPreVariazione(
			String strutturaAmministrativaContabileCapitoloPreVariazione) {
		this.strutturaAmministrativaContabileCapitoloPreVariazione = strutturaAmministrativaContabileCapitoloPreVariazione;
	}


	/**
	 * @param numeroVariazione the numeroVariazione to set
	 */
	public void setNumeroVariazione(Integer numeroVariazione) {
		this.numeroVariazione = numeroVariazione;
	}


	/**
	 * @param provvedimentoVariazione the provvedimentoVariazione to set
	 */
	public void setProvvedimentoVariazione(String provvedimentoVariazione) {
		this.provvedimentoVariazione = provvedimentoVariazione;
	}


	/**
	 * @param dataVariazione the dataVariazione to set
	 */
	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}
	
//	public String getDescrizioneCapitolo() {
//		return getCapitoloPreVariazione() != null && getCapitoloPreVariazione().getDescrizione() != null?
//				getCapitoloPreVariazione().getDescrizione()
//						: ""; 		
//	}
//	
//	public String getDescrizioneArticolo() {
//		return getCapitoloPreVariazione() != null && getCapitoloPreVariazione().getDescrizioneArticolo() != null?
//				getCapitoloPreVariazione().getDescrizione()
//						: ""; 		
//	}
//	
//	public String getStrutturaAmministrativaCapitolo(){
//		 
//		return getCapitoloPreVariazione() != null && getCapitoloPreVariazione().getStrutturaAmministrativoContabile() != null?
//					getCapitoloPreVariazione().getStrutturaAmministrativoContabile().getCodice() + '-' + getCapitoloPreVariazione().getStrutturaAmministrativoContabile().getDescrizione()
//					: "";
//	}
//
//
//	public Capitolo<?,?> getCapitoloPreVariazione() {
//		return capitoloPreVariazione;
//	}
//
//	public void setCapitoloPreVariazione(Capitolo<?,?> capitoloPreVariazione) {
//		this.capitoloPreVariazione = capitoloPreVariazione;
//	}
//
//	public VariazioneCodificaCapitolo getVariazione() {
//		return variazione;
//	}
//
//	public void setVariazione(VariazioneCodificaCapitolo variazione) {
//		this.variazione = variazione;
//	}
	
	
	
	
	
	
}
