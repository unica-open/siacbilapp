/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public class RigaDettaglioImportoTabellaImportiCapitolo extends RigaDettaglioTabellaImportiCapitolo {

	/** Per la serializzazione  */
	private static final long serialVersionUID = -2655108201908361174L;
	
	public boolean isStanziamento() {
		return TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO.equals(getTipoDettaglioComponenteImportiCapitolo());
	}
	
	public boolean isPresunto() {
		return TipoDettaglioComponenteImportiCapitolo.PRESUNTO.equals(getTipoDettaglioComponenteImportiCapitolo());
	}
	
	@Override
	public boolean isTipoDettaglioEditabile() {
		return isStanziamento() || isPresunto();
	}
	
	@Override
	public boolean isTipoDettaglioEliminabile() {
		return false;
	}
	
	@Override
	public String getTrCssClass() {
		if(isStanziamento() || isPresunto()) {
			return "componentiRowFirst";
		}
		return "componentiRowOther";
	}
	
	public boolean isAlmenoUnImportoTriennioMinoreDiZero() {
		return BigDecimal.ZERO.compareTo(getImportoAnno0()) > 0 ||  BigDecimal.ZERO.compareTo(getImportoAnno1()) > 0 || BigDecimal.ZERO.compareTo(getImportoAnno2()) >  0;
	}
	
//	@Override
//	public String getFormattedImportoAnniPrecedenti() {
//		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI.name())? super.getFormattedImportoAnniPrecedenti() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoInizialeAnniPrecedenti() {
//		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI.name())? super.getFormattedImportoInizialeAnniPrecedenti() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoResiduoAnno0() {
//		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO.name())? super.getFormattedImportoResiduoAnno0() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoResiduoInizialeAnno0() {
//		return !isImpegnatoODisponibilitaImpegnare()? super.getFormattedImportoResiduoInizialeAnno0() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoInizialeAnno0() {
//		return !isImpegnatoODisponibilitaImpegnare()? super.getFormattedImportoInizialeAnno0() : "";
//	}
//	
//	
//	@Override
//	public String getFormattedImportoInizialeAnno1() {
//		return !isImpegnatoODisponibilitaImpegnare() && !isRigaResiduo() && !isRigaCassa()? super.getFormattedImportoInizialeAnno1() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoAnno1() {
//		return !isRigaResiduo() && !isRigaCassa()? super.getFormattedImportoInizialeAnno1() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoInizialeAnno2() {
//		return !isImpegnatoODisponibilitaImpegnare() && !isRigaCassa()? super.getFormattedImportoInizialeAnno2() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoAnno2() {
//		return !isRigaCassa()? super.getFormattedImportoAnno2() : "";
//	}
//
//	
//	@Override
//	public String getFormattedImportoAnniSuccessivi() {
//		return !isDisponibilitaImpegnare() && !isRigaCassa()? super.getFormattedImportoAnniSuccessivi() : "";
//	}
//	
//	@Override
//	public String getFormattedImportoInizialeAnniSuccessivi() {
//		return !isImpegnatoODisponibilitaImpegnare() && !isRigaCassa()? super.getFormattedImportoInizialeAnniSuccessivi() : "";
//	}
	
}

