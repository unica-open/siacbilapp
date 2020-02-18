/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ConsultaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
/**
 * Classe di model per la consultazione della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 *
 */
public class ConsultaPrimaNotaIntegrataGSAModel extends ConsultaPrimaNotaIntegrataBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 987058517518697747L;

	/** Costruttore vuoto di default */
	public ConsultaPrimaNotaIntegrataGSAModel() {
		setTitolo("Consulta prima nota integrata");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	/**
	 * Gets the descrizione classificatore GSA.
	 *
	 * @return the descrizione classificatore GSA
	 */
	public String getDescrizioneClassificatoreGSA() {
		return getPrimaNota() != null && getPrimaNota().getClassificatoreGSA() != null
				? getPrimaNota().getClassificatoreGSA().getCodice() + " - " + getPrimaNota().getClassificatoreGSA().getDescrizione()
				: "Nessun classificatore presente"; 
	}

}
