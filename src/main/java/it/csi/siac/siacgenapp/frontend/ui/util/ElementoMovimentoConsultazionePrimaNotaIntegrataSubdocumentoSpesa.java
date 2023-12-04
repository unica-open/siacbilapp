/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di wrap per la consultazione del subdocumento di spesa alla prima nota integrata
 * @author Alessandro Marchino
 */
public class ElementoMovimentoConsultazionePrimaNotaIntegrataSubdocumentoSpesa extends ElementoMovimentoConsultazionePrimaNotaIntegrata<SubdocumentoSpesa> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7510208756536271618L;

	/**
	 * Costruttore di wrap
	 * @param subdocumentoSpesa il subdocumento di spesa da wrappare
	 */
	public ElementoMovimentoConsultazionePrimaNotaIntegrataSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		super(subdocumentoSpesa);
	}
	
	@Override
	public String getNumero() {
		return movimento != null && movimento.getNumero() != null ? movimento.getNumero().toString() : "";
	}
	
	@Override
	public String getImporto() {
		return movimento != null ? FormatUtils.formatCurrency(movimento.getImporto()) : "";
	}
	
	@Override
	public String getMovimentoGestione() {
		if(movimento == null || movimento.getImpegno() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(movimento.getImpegno().getAnnoMovimento())
				.append("/")
				.append(movimento.getImpegno().getNumeroBigDecimal().toPlainString());
		if(movimento.getSubImpegno() != null) {
			sb.append("-")
				.append(movimento.getSubImpegno().getNumeroBigDecimal().toPlainString());
		}
		
		return sb.toString();
	}
	
	@Override
	public String getPianoDeiConti() {
		if(movimento == null) {
			return "";
		}
		if(isValorizzatoPdc(movimento.getSubImpegno())) {
			return extractPianoDeiConti(movimento.getSubImpegno());
		}
		if(isValorizzatoPdc(movimento.getImpegno())) {
			return extractPianoDeiConti(movimento.getImpegno());
		}
		return "";
	}
	
	@Override
	public String getLiquidazione() {
		if(movimento == null || movimento.getLiquidazione() == null) {
			return "";
		}
		return new StringBuilder()
				.append(movimento.getLiquidazione().getAnnoLiquidazione())
				.append(" - ")
				.append(movimento.getLiquidazione().getNumeroLiquidazione().toPlainString())
				.toString();
	}
	
	@Override
	public String getOrdinativo() {
		if(movimento == null || movimento.getOrdinativo() == null) {
			return "";
		}
		return new StringBuilder()
				.append(movimento.getOrdinativo().getAnno())
				.append(" - ")
				.append(movimento.getOrdinativo().getNumero())
				.toString();
	}
	
	@Override
	public String getDatiAccessorii(){
		DocumentoSpesa documento = movimento.getDocumento();
		if(documento==null){
			return "";
		}
		return  new StringBuilder()
				.append(obtainStringTipoDocumento(documento.getTipoDocumento()))
				.append(" - ")
				.append(movimento.getDocumento().getAnno())
				.append(" - ")
				.append(movimento.getDocumento().getNumero())
				.append(" - ")
				.append(FormatUtils.formatDate(movimento.getDocumento().getDataEmissione()))
				.append(" - ")		
				.append(obtainStringSoggetto(documento.getSoggetto()))
				.toString();		
	}
	
	private String obtainStringTipoDocumento(TipoDocumento tipoDocumento){
		if(tipoDocumento == null){
			return "";
		}
		return tipoDocumento.getCodice() + "-" + tipoDocumento.getDescrizione();		
	}
	
	private String obtainStringSoggetto(Soggetto soggetto){
		if(soggetto == null){
			return "";
		}
		return soggetto.getCodiceSoggetto() + "-" + soggetto.getDenominazione();		
	}
	
}
