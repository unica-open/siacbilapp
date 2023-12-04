/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

public abstract class BasePrimaNotaLiberaModel extends GenericBilancioModel {

	/** per la serializzazione */
	private static final long serialVersionUID = 2481856279092393598L;

	//SIAC-8134
	private StrutturaAmministrativoContabile strutturaCompetentePrimaNotaLibera;
	private String nomeAzioneSAC;
	
	/**
	 * @return the strutturaCompetentePrimaNotaLibera
	 */
	public StrutturaAmministrativoContabile getStrutturaCompetentePrimaNotaLibera() {
		return strutturaCompetentePrimaNotaLibera;
	}

	/**
	 * @param strutturaCompetentePrimaNotaLibera the strutturaCompetentePrimaNotaLibera to set
	 */
	public void setStrutturaCompetentePrimaNotaLibera(StrutturaAmministrativoContabile strutturaCompetentePrimaNotaLibera) {
		this.strutturaCompetentePrimaNotaLibera = strutturaCompetentePrimaNotaLibera;
	}

	/**
	 * @return the nomeAzioneSAC
	 */
	public String getNomeAzioneSAC() {
		return nomeAzioneSAC;
	}

	/**
	 * @param nomeAzioneSAC the nomeAzioneSAC to set
	 */
	public void setNomeAzioneSAC(String nomeAzioneSAC) {
		this.nomeAzioneSAC = nomeAzioneSAC;
	}

}
