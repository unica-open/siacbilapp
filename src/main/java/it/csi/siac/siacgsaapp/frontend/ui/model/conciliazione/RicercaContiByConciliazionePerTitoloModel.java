/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitolo;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di model per la ricerca dei conti collegati alla conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/11/2015
 *
 */
public class RicercaContiByConciliazionePerTitoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2441018425899652384L;
	
	private ConciliazionePerTitolo conciliazionePerTitolo;
	private List<Conto> listaConto = new ArrayList<Conto>();
	
	/** Costruttore vuoto di default */
	public RicercaContiByConciliazionePerTitoloModel() {
		setTitolo("Ricerca conti per conciliazione per titolo");
	}
	/**
	 * @return the conciliazionePerTitolo
	 */
	public ConciliazionePerTitolo getConciliazionePerTitolo() {
		return conciliazionePerTitolo;
	}
	/**
	 * @param conciliazionePerTitolo the conciliazionePerTitolo to set
	 */
	public void setConciliazionePerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		this.conciliazionePerTitolo = conciliazionePerTitolo;
	}
	/**
	 * @return the listaConto
	 */
	public List<Conto> getListaConto() {
		return listaConto;
	}
	/**
	 * @param listaConto the listaConto to set
	 */
	public void setListaConto(List<Conto> listaConto) {
		this.listaConto = listaConto != null ? listaConto : new ArrayList<Conto>();
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerTitolo}.
	 * 
	 * @return la request creata
	 */
	public RicercaContiConciliazionePerTitolo creaRequestRicercaContiConciliazionePerTitolo() {
		RicercaContiConciliazionePerTitolo request = creaRequest(RicercaContiConciliazionePerTitolo.class);
		
		getConciliazionePerTitolo().setEnte(getEnte());
		request.setConciliazionePerTitolo(getConciliazionePerTitolo());
		
		return request;
	}
	
}
