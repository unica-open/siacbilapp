/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RicercaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Ricerca della prima nota integrata. Modulo GEN.
 * 
 * @author paggio
 * @author Marchino Alessandro
 *
 * @version 1.0.0 12/05/2015
 * @version 1.0.1 13/05/2015
 * @version 1.1.0 08/10/2015 - gestione GEN/GSA
 */
public class RicercaPrimaNotaIntegrataFINModel extends RicercaPrimaNotaIntegrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3162955709178608330L;
	
	private RegistrazioneMovFin registrazioneMovFin;

	/** Costruttore vuoto di default */
	public RicercaPrimaNotaIntegrataFINModel() {
		setTitolo("Ricerca prima nota integrata");
	}

	/**
	 * @return the registrazioneMovFin
	 */
	public RegistrazioneMovFin getRegistrazioneMovFin() {
		return registrazioneMovFin;
	}

	/**
	 * @param registrazioneMovFin the registrazioneMovFin to set
	 */
	public void setRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_FIN;
	}
	
	@Override
	protected void injettaRegistrazioneMovFinSePresente(RicercaSinteticaPrimaNotaIntegrata request) {
		if(getRegistrazioneMovFin() != null && getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato() != null && getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getUid() != 0) {
			request.setRegistrazioneMovFin(getRegistrazioneMovFin());
		}
	}
}
