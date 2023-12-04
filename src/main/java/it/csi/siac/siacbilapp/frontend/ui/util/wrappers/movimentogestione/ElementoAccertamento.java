/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione;

public class ElementoAccertamento extends ElementoMovimentoGestione {

	private static final long serialVersionUID = -388804950908809381L;
	
	private String sub;
	private String importoIncassato;
	private String titolo;

	public ElementoAccertamento() {
		super();
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getImportoIncassato() {
		return importoIncassato;
	}

	public void setImportoIncassato(String importoIncassato) {
		this.importoIncassato = importoIncassato;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	
}
