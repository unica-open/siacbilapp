/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

public enum PeriodoTabellaComponentiImportiCapitolo {
	ANNI_PRECEDENTI(-1, "importoAnniPrecedenti", null, null, null),
//	RESIDUO_INIZIALE_ANNO_BILANCIO(0, "importoResiduoInizialeAnno0", null, null, "impegnatoResiduoIniziale"),
	RESIDUO_ANNO_BILANCIO(0, "importoResiduoAnno0", null, null, "impegnatoResiduoFinale"),
	ANNO_BILANCIO(0, "importoAnno0", "uidComponenteAnno0", null, null),
	ANNO_BILANCIO_PIU_UNO(1, "importoAnno1", "uidComponenteAnno1", null, null),
	ANNO_BILANCIO_PIU_DUE(2, "importoAnno2", "uidComponenteAnno2", null, null), 
	ANNI_SUCCESSIVI(3, "importoAnniSuccessivi", null, null, null)
	;
	
	private int deltaDaAnnoBilancio;
	private String fieldNameRiga;
	private String fieldNameUidComponente;
	private String fieldNameComponenteStanziamento;
	private String fieldNameComponenteImpegnato;
	

	
	
	private PeriodoTabellaComponentiImportiCapitolo(int deltaDaAnnoBilancio, String fieldNameRiga,  String fieldNameUidComponente, String fieldNameComponenteStanziamento, String fieldNameComponenteImpegnato) {
		this.deltaDaAnnoBilancio = deltaDaAnnoBilancio;
		this.fieldNameRiga = fieldNameRiga;
		this.fieldNameUidComponente = fieldNameUidComponente;
		this.fieldNameComponenteStanziamento = fieldNameComponenteStanziamento;
		this.fieldNameComponenteImpegnato = fieldNameComponenteImpegnato;
	}
	
	public int getDelta() {
		return this.deltaDaAnnoBilancio;
	}
	
	public String getFieldNameRiga() {
		return this.fieldNameRiga;
	}
	
	public String getFieldNameUidComponente() {
		return this.fieldNameUidComponente;
	}
	
	public String getFieldNameComponente(TipoDettaglioComponenteImportiCapitolo tp) {
		return TipoDettaglioComponenteImportiCapitolo.IMPEGNATO.equals(tp) ? this.fieldNameComponenteImpegnato :this.fieldNameComponenteStanziamento ;
	}
	
	public static PeriodoTabellaComponentiImportiCapitolo getByDeltaEscludendoResiduo(int delta) {
		for (PeriodoTabellaComponentiImportiCapitolo pd : values()) {
			if(pd.getDelta() == delta && !PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO.equals(pd)) {
				return pd;
			}
		}
		return null;
	}
	
	public static List<String> getAllNames() {
		List<String> names = new ArrayList<String>();
		for (PeriodoTabellaComponentiImportiCapitolo per : values()) {
			names.add(per.name());
		}
		return names;
	}
}
