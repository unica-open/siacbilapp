/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.helper;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.sirfelser.model.TipoDocumentoFEL;

/**
 * Helper per la traduzione del tipo documento tra FEL e FIN
 * @author Marchino Alessandro
 *
 */
public enum TipoDocumentoFELHelper {

	/** Fattura */
	TD01(TipoDocumentoFEL.FATTURA, BilConstants.TIPO_DOCUMENTO_FAT.getConstant()),
	/** Acconto fattura */
	TD02(TipoDocumentoFEL.ACCONTO_FATTURA, BilConstants.TIPO_DOCUMENTO_FAT.getConstant()),
	/** Acconto parcella */
	TD03(TipoDocumentoFEL.ACCONTO_PARCELLA, BilConstants.TIPO_DOCUMENTO_FPR.getConstant()),
	/** Nota di credito */
	TD04(TipoDocumentoFEL.NOTA_DI_CREDITO, BilConstants.CODICE_NOTE_CREDITO.getConstant()),
	/** Nota di debito */
	TD05(TipoDocumentoFEL.NOTA_DI_DEBITO, BilConstants.CODICE_NOTE.getConstant()),
	/** Parcella */
	TD06(TipoDocumentoFEL.PARCELLA, BilConstants.TIPO_DOCUMENTO_FPR.getConstant()),
	;
	private final TipoDocumentoFEL tipoDocumentoFEL;
	private final String codiceTipoDocumento;
	
	/**
	 * Costruttore privato
	 * @param tipoDocumentoFEL il tipo di documento FEL
	 * @param codiceTipoDocumento il codice del tipo documento FIN
	 */
	private TipoDocumentoFELHelper(TipoDocumentoFEL tipoDocumentoFEL, String codiceTipoDocumento) {
		this.tipoDocumentoFEL = tipoDocumentoFEL;
		this.codiceTipoDocumento = codiceTipoDocumento;
	}

	/**
	 * @return the codiceTipoDocumento
	 */
	public String getCodiceTipoDocumento() {
		return codiceTipoDocumento;
	}
	
	/**
	 * Ottiene il record corrispondente a un dato tipo documento FEL
	 * @param tipoDocumentoFEL il tipo documento FEL per cui ricercare il record
	 * @return l'helper
	 */
	public static TipoDocumentoFELHelper byTipoDocumentoFEL(TipoDocumentoFEL tipoDocumentoFEL) {
		// Ciclo sugli helper
		for(TipoDocumentoFELHelper helper : TipoDocumentoFELHelper.values()) {
			// Se l'helper ha lo stesso tipo documento FEL, lo restituisco
			if(helper.tipoDocumentoFEL.equals(tipoDocumentoFEL)) {
				return helper;
			}
		}
		return null;
	}
	
}
