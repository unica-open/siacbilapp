/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public class RigaImportoTabellaImportiCapitolo extends RigaTabellaImportiCapitolo {

	/** Per la serializzazione  */
	private static final long serialVersionUID = -2655108201908361174L;
	
	private TipologiaImportoTabellaImportoCapitolo tipoImportiCapitoloTabella;
	private List<RigaDettaglioImportoTabellaImportiCapitolo> sottoRighe;
	
	
	@Override
	public String getDescrizioneImporto() {
		return tipoImportiCapitoloTabella !=null? tipoImportiCapitoloTabella.getDescrizioneCella() : "N.D.";
	}
	
	@Override
	public int getSottoRigheSize() {
		return sottoRighe != null? sottoRighe.size() : 0;
	}
	
	@Override
	public boolean getImportoEditabileCellaResiduo() {
		//se si tratta di residuo
		return tipoImportiCapitoloTabella != null && TipologiaImportoTabellaImportoCapitolo.RESIDUO.equals(tipoImportiCapitoloTabella);
	}

	@Override
	public boolean getImportoEditabileCellaAnnoBilancio() {
		//se si tratta di cassa
		return tipoImportiCapitoloTabella != null && TipologiaImportoTabellaImportoCapitolo.CASSA.equals(tipoImportiCapitoloTabella);
	}

	@Override
	public boolean getImportoEditabileCelleAnnoBilancioPiuUnoPiuDue() {
		return false;
	}
	
	@Override
	public boolean getRigaModificabilePerTipoImporto() {
		return tipoImportiCapitoloTabella != null && !TipologiaImportoTabellaImportoCapitolo.COMPETENZA.equals(tipoImportiCapitoloTabella);
	}
	
	@Override
	public boolean getRigaEliminabile() {
		return false;
	}
	
	@Override
	public String getFirstCellString() {
		return "<th class=\"stanziamenti-titoli\" rowspan=\" " + getSottoRigheSize() + "\">" + getDescrizioneImporto() + "</th>";
	}

	@Override
	public int getRowspanEdit() {
		return getSottoRigheSize();
	}
	
	
	public String getThClass() {
		return "stanziamenti-titoli";
	}
	
	public void addSottoRiga(RigaDettaglioImportoTabellaImportiCapitolo e) {
		if(getSottoRighe() == null) {
			setSottoRighe(new ArrayList<RigaDettaglioImportoTabellaImportiCapitolo>());
		}
		getSottoRighe().add(e);
	}


	public List<RigaDettaglioImportoTabellaImportiCapitolo> getSottoRighe() {
		return sottoRighe;
	}


	public void setSottoRighe(List<RigaDettaglioImportoTabellaImportiCapitolo> sottoRighe) {
		this.sottoRighe = sottoRighe;
	}

	public TipologiaImportoTabellaImportoCapitolo getTipoImportiCapitoloTabella() {
		return tipoImportiCapitoloTabella;
	}

	public void setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo tipoImportiCapitoloTabella) {
		this.tipoImportiCapitoloTabella = tipoImportiCapitoloTabella;
	}

	@Override
	public String getLastSottoRigaCssClass() {
		return tipoImportiCapitoloTabella !=null? 
				tipoImportiCapitoloTabella.getCssClasseUltimaSottoRiga() 
				: "nullClass";
	}

}
