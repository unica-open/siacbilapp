/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public class RigaDettaglioComponenteTabellaImportiCapitolo extends RigaDettaglioTabellaImportiCapitolo {

	/** Per la serializzazione  */
	private static final long serialVersionUID = -2655108201908361174L;
	
	private int uidComponenteAnno0;
	private int uidComponenteAnno1;
	private int uidComponenteAnno2;
	
	private TipoCapitolo tipoCapitolo;
	
	
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

	public TipoCapitolo getTipoCapitolo() {
		return tipoCapitolo;
	}

	public void setTipoCapitolo(TipoCapitolo tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	public boolean isStanziamento() {
		return TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO.getDescrizione().equals(getTipoDettaglioComponenteDesc());
	}
	
	@Override
	public boolean isTipoDettaglioEditabile() {
		return isStanziamento();
	}
	
	@Override
	public boolean isTipoDettaglioEliminabile() {
		return isStanziamento();
	}

	@Override
	public String getTrCssClass() {
		String cssClass="componentiCompetenzaRow";
		if(isStanziamento()) {
			cssClass += " previsione-default-stanziamento";
		}
		return cssClass;
	}

	
	@Override
	public String getFormattedImportoInizialeAnniPrecedenti() {
		return "";
	}
	
	@Override
	public String getFormattedImportoResiduoInizialeAnno0() {
		return this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloPrevisione(tipoCapitolo)? "" : super.getFormattedImportoResiduoInizialeAnno0() ;
	}
	
	@Override
	public String getFormattedImportoInizialeAnno1() {
		return "";
	}
	
	public String getFormattedImportoInizialeAnno2() {
		return "";
	}
	
	@Override
	public String getFormattedImportoInizialeAnniSuccessivi() {
		return "";
	}
	
	@Override
	public String getFormattedImportoAnniSuccessivi() {
		//SIAC-8469
		return isStanziamento()?  "" : super.getFormattedImportoAnniSuccessivi();
	}
	
	
	@Override
	public String getFormattedImportoInizialeAnno0() {
		return "";
	}
	
}



