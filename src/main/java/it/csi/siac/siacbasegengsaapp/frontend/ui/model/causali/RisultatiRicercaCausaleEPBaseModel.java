/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausale;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Classe di model per i risultati di ricerca della causale EP.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 31/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public abstract class RisultatiRicercaCausaleEPBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3868206634026160055L;
	
	private CausaleEP causaleEP;
	private Integer savedDisplayStart;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCausaleEPBaseModel() {
		setTitolo("Risultati ricerca Causali");
	}
	
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();

	
	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}

	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link AnnullaCausale}.
	 * 
	 * @return la request creata
	 */
	public AnnullaCausale creaRequestAnnullaCausale() {
		AnnullaCausale request = creaRequest(AnnullaCausale.class);
		
		request.setCausaleEP(getCausaleEP());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaCausale}.
	 * 
	 * @return la request creata
	 */
	public EliminaCausale creaRequestEliminaCausale() {
		EliminaCausale request = creaRequest(EliminaCausale.class);
		
		request.setCausaleEP(getCausaleEP());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ValidaCausale}.
	 * 
	 * @return la request creata
	 */
	public ValidaCausale creaRequestValidaCausale() {
		ValidaCausale request = creaRequest(ValidaCausale.class);
		getCausaleEP().setAmbito(getAmbito());
		request.setCausaleEP(getCausaleEP());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
}
