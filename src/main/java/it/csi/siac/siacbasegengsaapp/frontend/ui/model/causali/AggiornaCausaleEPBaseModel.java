/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreEP;

/**
 * Classe di model per l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
public abstract class AggiornaCausaleEPBaseModel extends BaseInserisciAggiornaCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 568572254744064120L;
	
	private CausaleEP causaleEPOriginale;
	
	/**
	 * @return the causaleEPOriginale
	 */
	public CausaleEP getCausaleEPOriginale() {
		return causaleEPOriginale;
	}

	/**
	 * @param causaleEPOriginale the causaleEPOriginale to set
	 */
	public void setCausaleEPOriginale(CausaleEP causaleEPOriginale) {
		this.causaleEPOriginale = causaleEPOriginale;
	}

	
	@Override
	public String getDenominazioneWizard() {
		return "Aggiorna Causale";
	}
	
	@Override
	public boolean isAggiornamento() {
		return true;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaCausale}.
	 * 
	 * @return la request creata
	 */
	public AggiornaCausale creaRequestAggiornaCausale() {
		AggiornaCausale request = creaRequest(AggiornaCausale.class);
		
		// Popolo la causale correttamente
		getCausaleEP().setContiTipoOperazione(getListaContoTipoOperazione());
		getCausaleEP().setEnte(getEnte());
		getCausaleEP().setElementoPianoDeiConti(impostaEntitaFacoltativa(getElementoPianoDeiConti()));
		getCausaleEP().setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		getCausaleEP().setAmbito(getAmbito());
		// Impostazione dei classificatori
		List<ClassificatoreEP> listEPs = new ArrayList<ClassificatoreEP>();
		addIfNotNullNorInvalidUid(listEPs, getClassificatoreEP1());
		addIfNotNullNorInvalidUid(listEPs, getClassificatoreEP2());
		addIfNotNullNorInvalidUid(listEPs, getClassificatoreEP3());
		addIfNotNullNorInvalidUid(listEPs, getClassificatoreEP4());
		addIfNotNullNorInvalidUid(listEPs, getClassificatoreEP5());
		addIfNotNullNorInvalidUid(listEPs, getClassificatoreEP6());
		getCausaleEP().setClassificatoriEP(listEPs);
		
		request.setCausaleEP(getCausaleEP());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	
	
}
