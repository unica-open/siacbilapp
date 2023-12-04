/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public class RigaComponenteTabellaImportiCapitolo extends RigaTabellaImportiCapitolo {

	/** Per la serializzazione  */
	private static final long serialVersionUID = -2655108201908361174L;
	
	private Integer uidTipoComponenteImportiCapitolo;
	private String descrizioneTipoComponente;
	private boolean propostaDefault;
	private List<RigaDettaglioComponenteTabellaImportiCapitolo> sottoRighe;
	
	private MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo;
	
	private int uidComponenteAnno0;
	private int uidComponenteAnno1;
	private int uidComponenteAnno2;
	
	
	@Override
	public String getDescrizioneImporto() {
		return StringUtils.defaultIfBlank(getDescrizioneTipoComponente(), "N.D.");
	}
	
	@Override
	public int getSottoRigheSize() {
		return getSottoRighe() != null? getSottoRighe().size() : 0;
	}
	
	@Override
	public boolean getImportoEditabileCellaResiduo() {
		return false;
	}

	@Override
	public boolean getImportoEditabileCellaAnnoBilancio() {
		return true;
	}

	@Override
	public boolean getImportoEditabileCelleAnnoBilancioPiuUnoPiuDue() {
		return true;
	}
	
	@Override
	public boolean getRigaModificabilePerTipoImporto() {
		return true;
	}
	
	@Override
	public boolean getRigaEliminabile() {
		return hasUidComponentesuiTreAnni();
	}
	
	@Override
	public String getFirstCellString() {
		return "<td class=\"componenti-competenza\" rowspan=\" " + getSottoRigheSize() + "\">" + getDescrizioneImporto() + "</td>";
	}
	
	@Override
	public int getRowspanEdit() {
		return 1;
	}
	
	public boolean hasUidComponentesuiTreAnni() {
		return getUidComponenteAnno0() != 0 && getUidComponenteAnno1() != 0 && getUidComponenteAnno2() != 0;
	}
	
	public int getUidComponenteAnno0() {
		return uidComponenteAnno0;
	}

	public void setUidComponenteAnno0(int uidComponenteAnno0) {
		this.uidComponenteAnno0 = uidComponenteAnno0;
	}

	public int getUidComponenteAnno1() {
		return uidComponenteAnno1;
	}

	public void setUidComponenteAnno1(int uidComponenteAnno1) {
		this.uidComponenteAnno1 = uidComponenteAnno1;
	}

	public int getUidComponenteAnno2() {
		return uidComponenteAnno2;
	}

	public void setUidComponenteAnno2(int uidComponenteAnno2) {
		this.uidComponenteAnno2 = uidComponenteAnno2;
	}

	public String getDescrizioneTipoComponente() {
		return descrizioneTipoComponente;
	}


	public void setDescrizioneTipoComponente(String descrizioneTipoComponente) {
		this.descrizioneTipoComponente = descrizioneTipoComponente;
	}


	public boolean isPropostaDefault() {
		return propostaDefault;
	}

	public void setPropostaDefault(boolean propostaDefault) {
		this.propostaDefault = propostaDefault;
	}
	
	public MacrotipoComponenteImportiCapitolo getMacrotipoComponenteImportiCapitolo() {
		return macrotipoComponenteImportiCapitolo;
	}

	public void setMacrotipoComponenteImportiCapitolo(
			MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo) {
		this.macrotipoComponenteImportiCapitolo = macrotipoComponenteImportiCapitolo;
	}

	public List<RigaDettaglioComponenteTabellaImportiCapitolo> getSottoRighe() {
		return sottoRighe;
	}


	public void setSottoRighe(List<RigaDettaglioComponenteTabellaImportiCapitolo> sottoRighe) {
		this.sottoRighe = sottoRighe;
	}


	public Integer getUidTipoComponenteImportiCapitolo() {
		return uidTipoComponenteImportiCapitolo;
	}


	public void setUidTipoComponenteImportiCapitolo(Integer uidTipoComponenteImportiCapitolo) {
		this.uidTipoComponenteImportiCapitolo = uidTipoComponenteImportiCapitolo;
	}	
	
	@Override
	public String getTrCssClass() {
		if(getMacrotipoComponenteImportiCapitolo() == null || hasUidComponentesuiTreAnni() || MacrotipoComponenteImportiCapitolo.FRESCO.equals(getMacrotipoComponenteImportiCapitolo())) {
			//se ho un id componente sui 3 anni, non devo nascondere mai la riga, perche' e' associata al capitolo!!
			return "";
		}
		//se 
			//in questo caso, segnalo la riga come "da nascondere" se il capitolo e' fondino
		return " nascondi-se-fondino";
	}

	@Override
	public String getLastSottoRigaCssClass() {
		return "";
	}
	
}

