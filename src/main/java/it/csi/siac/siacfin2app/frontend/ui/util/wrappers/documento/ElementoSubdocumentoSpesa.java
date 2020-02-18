/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di wrap per il Subdocumento di spesa.
 * @author Alessandro Marchino
 *
 */
public class ElementoSubdocumentoSpesa extends ElementoSubdocumento<SubdocumentoSpesa, Impegno, SubImpegno, CapitoloUscitaGestione> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8868369333580105549L;

	/**
	 * Costruttore di wrap
	 * @param subdocumento il subdocumento da wrappare
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ElementoSubdocumentoSpesa(SubdocumentoSpesa subdocumento, boolean isGestioneUEB) {
		super(subdocumento, subdocumento != null ? subdocumento.getImpegno() : null, subdocumento != null ? subdocumento.getSubImpegno() : null, isGestioneUEB);
	}
	
	/**
	 * @return the liquidazione
	 */
	public String getLiquidazione() {
		if(subdocumento == null || subdocumento.getLiquidazione() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(subdocumento.getLiquidazione().getAnnoLiquidazione());
		if(subdocumento.getLiquidazione().getNumeroLiquidazione() != null) {
			sb.append("/")
			.append(subdocumento.getLiquidazione().getNumeroLiquidazione().toPlainString());
		}
		return sb.toString();
	}

	@Override
	protected CapitoloUscitaGestione ottieniCapitolo() {
		if(movimentoGestione == null) {
			return null;
		}
		return movimentoGestione.getCapitoloUscitaGestione();
	}

}
