/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;

public class RisultatiRicercaMutuoModel extends BaseMutuoModel {


	private static final long serialVersionUID = 8715297787571710350L;
	
	
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	private List<Integer> elencoIdMutui = new ArrayList<Integer>();
	private BigDecimal tassoInteresseEuribor;
	
	private int savedDisplayStart;
	

	public RisultatiRicercaMutuoModel() {
		super();
		setTitolo("Risultati di ricerca Mutui");
	}

	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	public List<Integer> getElencoIdMutui() {
		return elencoIdMutui;
	}

	public void setElencoIdMutui(List<Integer> elencoIdMutui) {
		this.elencoIdMutui = ObjectUtils.defaultIfNull(elencoIdMutui, new ArrayList<Integer>());
	}
	
	public BigDecimal getTassoInteresseEuribor() {
		return tassoInteresseEuribor;
	}

	public void setTassoInteresseEuribor(BigDecimal tassoInteresseEuribor) {
		this.tassoInteresseEuribor = tassoInteresseEuribor;
	}
	
	public AnnullaMutuo creaRequestAnnullaMutuo() {
		AnnullaMutuo request = creaRequest(AnnullaMutuo.class);
		
		Mutuo mutuo = new Mutuo();
		mutuo.setUid(getMutuo().getUid());
		mutuo.setEnte(getEnte());
		
		request.setMutuo(mutuo);
		
		return request;
	}
	
	public InserisciVariazioneMassivaTassoMutuo creaRequestVariazioneMassivaTassoMutuo() {
		InserisciVariazioneMassivaTassoMutuo request = creaRequest(InserisciVariazioneMassivaTassoMutuo.class);
		
		request.setTassoInteresseEuribor(tassoInteresseEuribor);
		request.setElencoIdMutui(elencoIdMutui);
		
		return request;
	}

	
}


