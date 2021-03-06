/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.ConsultaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
/**
 * Classe di model per la consultazione della prima nota libera.
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public class ConsultaPrimaNotaIntegrataManualeGSAModel extends ConsultaPrimaNotaIntegrataManualeBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -8328266468565408992L;

	/** Costruttore di default*/
	public ConsultaPrimaNotaIntegrataManualeGSAModel() {
		setTitolo("Consulta Prima Nota Integrata Manuale - GSA");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

	@Override
	public String getAmbitoSuffix() {
		return "GSA";
	}
	
	/**
	 * Gets the descrizione classificatore GSA.
	 *
	 * @return the descrizione classificatore GSA
	 */
	public String getDescrizioneClassificatoreGSA() {
		// Codice + Descrizione del classificatore
		// In caso contrario, indica l'assenza del classificatore
		return getPrimaNotaLibera() != null && getPrimaNotaLibera().getClassificatoreGSA() != null
				? getPrimaNotaLibera().getClassificatoreGSA().getCodice() + " - " + getPrimaNotaLibera().getClassificatoreGSA().getDescrizione()
				: "Nessun classificatore presente"; 
	}


}
