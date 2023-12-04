/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione;

public class ElementoImpegno extends ElementoMovimentoGestione {

	private static final long serialVersionUID = -388804950908809381L;
	
	
	private String cig;
	private String cup;
	private String sub;
	private String importoLiquidato;
	private String missione;
	private String programma;


	public ElementoImpegno() {
		super();
	}




	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}




	public String getImportoLiquidato() {
		return importoLiquidato;
	}



	public void setImportoLiquidato(String importoLiquidato) {
		this.importoLiquidato = importoLiquidato;
	}


	public String getMissione() {
		return missione;
	}

	public void setMissione(String missione) {
		this.missione = missione;
	}
	
	public String getProgramma() {
		return programma;
	}

	public void setProgramma(String programma) {
		this.programma = programma;
	}

	
	
}
