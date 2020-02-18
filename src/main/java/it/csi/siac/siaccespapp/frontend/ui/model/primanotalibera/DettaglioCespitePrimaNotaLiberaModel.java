/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite.ElementoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;


/**
 * Classe di model per la ricerca del dettaglio dei movimenti della prima nota libera
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class DettaglioCespitePrimaNotaLiberaModel extends GenericBilancioModel {
	
	/** Per la serializzazione	 **/
	private static final long serialVersionUID = 7750526781111645201L;
	
	private PrimaNota primaNota;

	private TipoEvento tipoEvento;

	private List<ElementoCespite> listaCespite = new ArrayList<ElementoCespite>();
	
	/** Costruttore vuoto di default */
	public DettaglioCespitePrimaNotaLiberaModel() {
		setTitolo("Risultati ricerca Cespite associati a prima nota");
	}


	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}
	/**
	 * @return the listaCespite
	 */
	public List<ElementoCespite> getListaCespite() {
		return listaCespite;
	}

	/**
	 * @param listaCespite the listaCespite to set
	 */
	public void setListaCespite(List<ElementoCespite> listaCespite) {
		this.listaCespite = listaCespite != null ? listaCespite : new ArrayList<ElementoCespite>();
	}

	/* **** Requests **** */

	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}


	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}


	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaCespiteDaPrimaNota creaRequestRicercaCespiteDaPrimaNota() {
		RicercaCespiteDaPrimaNota request = creaRequest(RicercaCespiteDaPrimaNota.class);		
		request.setPrimaNota(getPrimaNota());	
		request.setTipoEvento(getTipoEvento());			
		return request;
	}
}