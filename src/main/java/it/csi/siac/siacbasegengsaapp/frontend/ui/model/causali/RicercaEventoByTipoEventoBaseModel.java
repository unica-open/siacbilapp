/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;


import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorEvento;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model per la ricerca degli eventi a partire dal tipo di evento.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 30/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
public class RicercaEventoByTipoEventoBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6003008090944506059L;
	
	private TipoEvento tipoEvento;
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	/** Costruttore vuoto di default */
	public RicercaEventoByTipoEventoBaseModel() {
		setTitolo("Ricerca evento per tipo evento");
	}
	
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
	 * @return the listaEvento
	 */
	public List<Evento> getListaEvento() {
		// SIAC-3528 ordinamento in base al codice (ordine alfabetico)
		return new SortedSetList<Evento>(listaEvento != null ? listaEvento : new ArrayList<Evento>(), ComparatorEvento.INSTANCE);
	}

	/**
	 * @param listaEvento the listaEvento to set
	 */
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento != null ? listaEvento : new ArrayList<Evento>();
	}
	
	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaEventiPerTipo}.
	 * 
	 * @return la request creata
	 */
	public RicercaEventiPerTipo creaRequestRicercaEventiPerTipo() {
		RicercaEventiPerTipo request = creaRequest(RicercaEventiPerTipo.class);
		
		request.setTipoEvento(getTipoEvento());
		
		return request;
	}
	
}
