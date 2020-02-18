/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.documento.entrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.entrata.CompletaDocumentoEntrataInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della prima nota integrata collegata al documento. Per l'entrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 */
public class CompletaDocumentoEntrataInsPrimaNotaIntegrataGSAModel extends CompletaDocumentoEntrataInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 8012180554604043623L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
}
