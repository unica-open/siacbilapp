/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausale;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;

/**
 * Classe di model per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 *
 */
public abstract class InserisciCausaleEPBaseModel extends BaseInserisciAggiornaCausaleEPBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8266888891757997525L;


	@Override
	public String getDenominazioneWizard() {
		return "Inserisci Causale";
	}
	
	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link InserisceCausale}.
	 * 
	 * @return la request creata
	 */
	public InserisceCausale creaRequestInserisceCausale() {
		InserisceCausale request = creaRequest(InserisceCausale.class);
		
		// Popolo la causale correttamente
		getCausaleEP().setContiTipoOperazione(getListaContoTipoOperazione());
		getCausaleEP().setEnte(getEnte());
		getCausaleEP().setStatoOperativoCausaleEP(StatoOperativoCausaleEP.PROVVISORIO);
		getCausaleEP().setDataInizioValidita(getPrimoGennaio(getAnnoEsercizioInt()));
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
