/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.documento.spesa;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa.CompletaDocumentoSpesaInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della prima nota integrata collegata al documento. Per la spesa. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public class CompletaDocumentoSpesaInsPrimaNotaIntegrataGSAModel extends CompletaDocumentoSpesaInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3311869266328845315L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
