/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.documento.spesa;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa.CompletaDocumentoSpesaInsPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;

/**
 * Classe di model per l'inserimento della prima nota integrata collegata al documento. Per la spesa. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 */
public class CompletaDocumentoSpesaInsPrimaNotaIntegrataFINModel extends CompletaDocumentoSpesaInsPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3311869266328845315L;

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
}
