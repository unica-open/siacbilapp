/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.componenteimporticapitolo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;

public class ConsultaComponenteImportiCapitoloModel extends CapitoloUscitaPrevisioneModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2653471479823874609L;
	
	public ConsultaComponenteImportiCapitoloModel() {
		super();
		setTitolo("Consulto Stanziamenti");
	}
	
	private int uidCapitolo;
	private List<ImportiCapitolo> listaImportiCapitolo = new ArrayList<ImportiCapitolo>();
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	

	/**
	 * @return the importiComponentiCapitolo
	 */
	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	/**
	 * @param importiComponentiCapitolo the importiComponentiCapitolo to set
	 */
	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		this.importiComponentiCapitolo = importiComponentiCapitolo;
	}

	/**
	 * @return the listaImportiCapitolo
	 */
	public List<ImportiCapitolo> getListaImportiCapitolo() {
		return listaImportiCapitolo;
	}

	/**
	 * @param listaImportiCapitolo the listaImportiCapitolo to set
	 */
	public void setListaImportiCapitolo(List<ImportiCapitolo> listaImportiCapitolo) {
		this.listaImportiCapitolo = listaImportiCapitolo;
	}

	/**
	 * @return the uidCapitolo
	 */
	public int getUidCapitolo() {
		return uidCapitolo;
	}

	/**
	 * @param uidCapitolo the uidCapitolo to set
	 */
	public void setUidCapitolo(int uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}
	
	
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		return request;
	}

	
}
