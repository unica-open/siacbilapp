/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import java.util.Date;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * Classe di model per la ricerca della prima nota integrata per la validazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 */
public class RisultatiRicercaValidazionePrimaNotaIntegrataGSAModel extends RisultatiRicercaValidazionePrimaNotaIntegrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2976079971928591303L;
	
	private ClassificatoreGSA classificatoreGSA;
	private Date dataRegistrazioneLibroGiornale;

	/** Costruttore vuoto di default */
	public RisultatiRicercaValidazionePrimaNotaIntegrataGSAModel() {
		setTitolo("Risultati ricerca validazione massiva prime note integrate");
	}

	/**
	 * @return the classificatoreGSA
	 */
	public ClassificatoreGSA getClassificatoreGSA() {
		return this.classificatoreGSA;
	}

	/**
	 * @param classificatoreGSA the classificatoreGSA to set
	 */
	public void setClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		this.classificatoreGSA = classificatoreGSA;
	}
	
	/**
	 * @return the dataRegistrazioneLibroGiornale
	 */
	public Date getDataRegistrazioneLibroGiornale() {
		return this.dataRegistrazioneLibroGiornale;
	}

	/**
	 * @param dataRegistrazioneLibroGiornale the dataRegistrazioneLibroGiornale to set
	 */
	public void setDataRegistrazioneLibroGiornale(Date dataRegistrazioneLibroGiornale) {
		this.dataRegistrazioneLibroGiornale = dataRegistrazioneLibroGiornale;
	}

	@Override
	public ValidazioneMassivaPrimaNotaIntegrata creaRequestValidazioneMassivaPrimaNotaIntegrata(RicercaSinteticaPrimaNotaIntegrataValidabile ricercaSinteticaPrimaNotaIntegrataValidabile) {
		ValidazioneMassivaPrimaNotaIntegrata req = super.creaRequestValidazioneMassivaPrimaNotaIntegrata(ricercaSinteticaPrimaNotaIntegrataValidabile);
		req.setClassificatoreGSA(impostaEntitaFacoltativa(getClassificatoreGSA()));
		req.setDataRegistrazioneLibroGiornale(getDataRegistrazioneLibroGiornale());
		
		return req;
	}

}
