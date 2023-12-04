/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.AmbitoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.FonteFinanziariaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImpegnabileComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MomentoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;

public abstract class ComponenteCapitoloModel extends GenericBilancioModel{

	
	private Integer uidComponenteCapitolo;
	/** Per la serializzazione  */
	private static final long serialVersionUID = 6644937276040430390L;
	
	private MacrotipoComponenteImportiCapitolo tipologiaMacroComponenteCapitolo;
	private SottotipoComponenteImportiCapitolo tipologiaSottoComponenteCapitolo;
	private String descrizione;
	private Integer anno;
	private AmbitoComponenteImportiCapitolo tipologiaAmbitoComponenteCapitolo;
	private FonteFinanziariaComponenteImportiCapitolo tipologiaFonteFinanziamentoComponenteCapitolo;
	private MomentoComponenteImportiCapitolo tipologiaMomentoComponenteCapitolo;
	private PropostaDefaultComponenteImportiCapitolo tipologiaPrevisioneComponenteCapitolo;
	//SIAC-7349
	//private TipoGestioneComponenteImportiCapitolo tipologiaGestioneComponenteCapitolo;
	private ImpegnabileComponenteImportiCapitolo tipologiaImpegnabileComponenteCapitolo;
	
	private Integer annoInizioAttivita;
	private Integer annoFineAttivita;
	

	/* Booleani rappresentanti i campi, per la editabilita' */
	private boolean macroComponenteCapitoloEditabile;
	private boolean sottoComponenteCapitoloEditabile;
	private boolean ambitoComponenteCapitoloEditabile;
	private boolean fonteFinanziamentoComponenteCapitoloEditabile;
	private boolean momentoComponenteCapitoloEditabile;
	private boolean previsioneComponenteCapitoloEditabile;
	//SIAC-7349
	//private boolean gestioneComponenteCapitoloEditabile;
	private boolean impegnabileComponenteCapitoloEditabile;
	
	


	/**
	 * @return the uidComponenteCapitolo
	 */
	public Integer getUidComponenteCapitolo() {
		return uidComponenteCapitolo;
	}

	/**
	 * @param uidComponenteCapitolo the uidComponenteCapitolo to set
	 */
	public void setUidComponenteCapitolo(Integer uidComponenteCapitolo) {
		this.uidComponenteCapitolo = uidComponenteCapitolo;
	}
	
	public MacrotipoComponenteImportiCapitolo getTipologiaMacroComponenteCapitolo() {
		return tipologiaMacroComponenteCapitolo;
	}
	public void setTipologiaMacroComponenteCapitolo(MacrotipoComponenteImportiCapitolo tipologiaMacroComponenteCapitolo) {
		this.tipologiaMacroComponenteCapitolo = tipologiaMacroComponenteCapitolo;
	}
	public SottotipoComponenteImportiCapitolo getTipologiaSottoComponenteCapitolo() {
		return tipologiaSottoComponenteCapitolo;
	}
	public void setTipologiaSottoComponenteCapitolo(SottotipoComponenteImportiCapitolo tipologiaSottoComponenteCapitolo) {
		this.tipologiaSottoComponenteCapitolo = tipologiaSottoComponenteCapitolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public AmbitoComponenteImportiCapitolo getTipologiaAmbitoComponenteCapitolo() {
		return tipologiaAmbitoComponenteCapitolo;
	}
	public void setTipologiaAmbitoComponenteCapitolo(AmbitoComponenteImportiCapitolo tipologiaAmbitoComponenteCapitolo) {
		this.tipologiaAmbitoComponenteCapitolo = tipologiaAmbitoComponenteCapitolo;
	}
	public FonteFinanziariaComponenteImportiCapitolo getTipologiaFonteFinanziamentoComponenteCapitolo() {
		return tipologiaFonteFinanziamentoComponenteCapitolo;
	}
	public void setTipologiaFonteFinanziamentoComponenteCapitolo(
			FonteFinanziariaComponenteImportiCapitolo tipologiaFonteFinanziamentoComponenteCapitolo) {
		this.tipologiaFonteFinanziamentoComponenteCapitolo = tipologiaFonteFinanziamentoComponenteCapitolo;
	}
	public MomentoComponenteImportiCapitolo getTipologiaMomentoComponenteCapitolo() {
		return tipologiaMomentoComponenteCapitolo;
	}
	public void setTipologiaMomentoComponenteCapitolo(
			MomentoComponenteImportiCapitolo tipologiaMomentoComponenteCapitolo) {
		this.tipologiaMomentoComponenteCapitolo = tipologiaMomentoComponenteCapitolo;
	}
	public PropostaDefaultComponenteImportiCapitolo getTipologiaPrevisioneComponenteCapitolo() {
		return tipologiaPrevisioneComponenteCapitolo;
	}
	public void setTipologiaPrevisioneComponenteCapitolo(
			PropostaDefaultComponenteImportiCapitolo tipologiaPrevisioneComponenteCapitolo) {
		this.tipologiaPrevisioneComponenteCapitolo = tipologiaPrevisioneComponenteCapitolo;
	}
//	public TipoGestioneComponenteImportiCapitolo getTipologiaGestioneComponenteCapitolo() {
//		return tipologiaGestioneComponenteCapitolo;
//	}
//	public void setTipologiaGestioneComponenteCapitolo(
//			TipoGestioneComponenteImportiCapitolo tipologiaGestioneComponenteCapitolo) {
//		this.tipologiaGestioneComponenteCapitolo = tipologiaGestioneComponenteCapitolo;
//	}
	public Integer getAnnoInizioAttivita() {
		return annoInizioAttivita;
	}
	public void setAnnoInizioAttivita(Integer annoInizioAttivita) {
		this.annoInizioAttivita = annoInizioAttivita;
	}
	public Integer getAnnoFineAttivita() {
		return annoFineAttivita;
	}
	public void setAnnoFineAttivita(Integer annoFineAttivita) {
		this.annoFineAttivita = annoFineAttivita;
	}
	public boolean isMacroComponenteCapitoloEditabile() {
		return macroComponenteCapitoloEditabile;
	}
	public void setMacroComponenteCapitoloEditabile(boolean macroComponenteCapitoloEditabile) {
		this.macroComponenteCapitoloEditabile = macroComponenteCapitoloEditabile;
	}
	public boolean isSottoComponenteCapitoloEditabile() {
		return sottoComponenteCapitoloEditabile;
	}
	public void setSottoComponenteCapitoloEditabile(boolean sottoComponenteCapitoloEditabile) {
		this.sottoComponenteCapitoloEditabile = sottoComponenteCapitoloEditabile;
	}
	public boolean isAmbitoComponenteCapitoloEditabile() {
		return ambitoComponenteCapitoloEditabile;
	}
	public void setAmbitoComponenteCapitoloEditabile(boolean ambitoComponenteCapitoloEditabile) {
		this.ambitoComponenteCapitoloEditabile = ambitoComponenteCapitoloEditabile;
	}
	public boolean isFonteFinanziamentoComponenteCapitoloEditabile() {
		return fonteFinanziamentoComponenteCapitoloEditabile;
	}
	public void setFonteFinanziamentoComponenteCapitoloEditabile(boolean fonteFinanziamentoComponenteCapitoloEditabile) {
		this.fonteFinanziamentoComponenteCapitoloEditabile = fonteFinanziamentoComponenteCapitoloEditabile;
	}
	public boolean isMomentoComponenteCapitoloEditabile() {
		return momentoComponenteCapitoloEditabile;
	}
	public void setMomentoComponenteCapitoloEditabile(boolean momentoComponenteCapitoloEditabile) {
		this.momentoComponenteCapitoloEditabile = momentoComponenteCapitoloEditabile;
	}
	public boolean isPrevisioneComponenteCapitoloEditabile() {
		return previsioneComponenteCapitoloEditabile;
	}
	public void setPrevisioneComponenteCapitoloEditabile(boolean previsioneComponenteCapitoloEditabile) {
		this.previsioneComponenteCapitoloEditabile = previsioneComponenteCapitoloEditabile;
	}
//	public boolean isGestioneComponenteCapitoloEditabile() {
//		return gestioneComponenteCapitoloEditabile;
//	}
//	public void setGestioneComponenteCapitoloEditabile(boolean gestioneComponenteCapitoloEditabile) {
//		this.gestioneComponenteCapitoloEditabile = gestioneComponenteCapitoloEditabile;
//	}

	/**
	 * @return the tipologiaImpegnabileComponenteCapitolo
	 */
	public ImpegnabileComponenteImportiCapitolo getTipologiaImpegnabileComponenteCapitolo() {
		return tipologiaImpegnabileComponenteCapitolo;
	}

	/**
	 * @param tipologiaImpegnabileComponenteCapitolo the tipologiaImpegnabileComponenteCapitolo to set
	 */
	public void setTipologiaImpegnabileComponenteCapitolo(
			ImpegnabileComponenteImportiCapitolo tipologiaImpegnabileComponenteCapitolo) {
		this.tipologiaImpegnabileComponenteCapitolo = tipologiaImpegnabileComponenteCapitolo;
	}

	/**
	 * @return the impegnabileComponenteCapitoloEditabile
	 */
	public boolean isImpegnabileComponenteCapitoloEditabile() {
		return impegnabileComponenteCapitoloEditabile;
	}

	/**
	 * @param impegnabileComponenteCapitoloEditabile the impegnabileComponenteCapitoloEditabile to set
	 */
	public void setImpegnabileComponenteCapitoloEditabile(boolean impegnabileComponenteCapitoloEditabile) {
		this.impegnabileComponenteCapitoloEditabile = impegnabileComponenteCapitoloEditabile;
	}
	
}
