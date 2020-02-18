/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di model per la ricerca della prima nota integrata manuale
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public abstract class RisultatiRicercaPrimaNotaIntegrataManualeBaseModel extends GenericBilancioModel{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 4041369692043209903L;
	private PrimaNota primaNotaLibera;
	private Integer savedDisplayStart;
	
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * Ottiene il suffisso dell'ambito corrispondente: pu&oacute; essere "FIN" o "GSA"
	 * 
	 * @return la stringa con il suffisso dell'ambito
	 */
	public abstract String getAmbitoSuffix();

	/**
	 * @return the primaNotaLibera
	 */
	public PrimaNota getPrimaNotaLibera() {
		return primaNotaLibera;
	}



	/**
	 * @param primaNotaLibera the primaNotaLibera to set
	 */
	public void setPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
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
	 * Crea una request per il servizio di {@link AnnullaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public AnnullaPrimaNota creaRequestAnnullaPrimaNota() {
		AnnullaPrimaNota request = creaRequest(AnnullaPrimaNota.class);
		
		request.setPrimaNota(getPrimaNotaLibera());
		request.getPrimaNota().setAmbito(getAmbito());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ValidaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public ValidaPrimaNota creaRequestValidaPrimaNota() {
		ValidaPrimaNota request = creaRequest(ValidaPrimaNota.class);
		getPrimaNotaLibera().setBilancio(getBilancio());
		getPrimaNotaLibera().setAmbito(getAmbito());
		request.setPrimaNota(getPrimaNotaLibera());
		
		return request;
	}
}
