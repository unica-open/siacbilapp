/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

public enum TipologiaImportoTabellaImportoCapitolo {
	COMPETENZA("<a href=\"#\" id=\"competenzaCella\">Competenza</a>", "competenzaLast"),
	RESIDUO("Residuo", ""),
	CASSA("Cassa", ""),
//	DEFAULT("N.D.")
	;
	private String descrizioneCella;
	private String cssClasseUltimaSottoRiga;
	
	
	
	private TipologiaImportoTabellaImportoCapitolo(String descrizioneCella, String cssClasseUltimaSottoRiga) {
		this.descrizioneCella = descrizioneCella;
		this.cssClasseUltimaSottoRiga = cssClasseUltimaSottoRiga;
	}
	
	
	public String getDescrizioneCella(){
		return descrizioneCella;
	}


	public String getCssClasseUltimaSottoRiga() {
		return cssClasseUltimaSottoRiga;
	}
	

}
