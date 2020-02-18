/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.importi.ElementoImportiVariazione;

/**
 * Classe di model per la specificazione della variazione degli importi nel caso della gestione delle UEB. Contiene una mappatura del model.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 15/10/2013
 *
 */
public class SpecificaVariazioneImportoUEBModel extends SpecificaVariazioneImportoBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7357638058514734678L;

	private String tipoCapitolo;
	private Integer numeroUEB;
	private ElementoCapitoloVariazione elementoCapitoloVariazione;
	private Boolean rientroDaInserimentoNuovaUEB;
	
	private List<ElementoCapitoloVariazione> listaUEBDaInserireNellaVariazione = new ArrayList<ElementoCapitoloVariazione>();
	private List<ElementoCapitoloVariazione> listaUEBNellaVariazioneCollassate = new ArrayList<ElementoCapitoloVariazione>();
	private List<ElementoCapitoloVariazione> listaUEBNellaVariazione = new ArrayList<ElementoCapitoloVariazione>();
	private List<ElementoCapitoloVariazione> listaUEBDaAnnullare = new ArrayList<ElementoCapitoloVariazione>();
	
	private ElementoImportiVariazione elementoImportiVariazione;
	
	/** Costruttore vuoto di default */
	public SpecificaVariazioneImportoUEBModel() {
		super();
	}

	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}
	
	/**
	 * @return the numeroUEB
	 */
	public Integer getNumeroUEB() {
		return numeroUEB;
	}

	/**
	 * @param numeroUEB the numeroUEB to set
	 */
	public void setNumeroUEB(Integer numeroUEB) {
		this.numeroUEB = numeroUEB;
	}

	/**
	 * @return the elementoCapitoloVariazione
	 */
	public ElementoCapitoloVariazione getElementoCapitoloVariazione() {
		return elementoCapitoloVariazione;
	}

	/**
	 * @param elementoCapitoloVariazione the elementoCapitoloVariazione to set
	 */
	public void setElementoCapitoloVariazione(ElementoCapitoloVariazione elementoCapitoloVariazione) {
		this.elementoCapitoloVariazione = elementoCapitoloVariazione;
	}

	/**
	 * @return the rientroDaInserimentoNuovaUEB
	 */
	public Boolean getRientroDaInserimentoNuovaUEB() {
		return rientroDaInserimentoNuovaUEB;
	}

	/**
	 * @param rientroDaInserimentoNuovaUEB the rientroDaInserimentoNuovaUEB to set
	 */
	public void setRientroDaInserimentoNuovaUEB(Boolean rientroDaInserimentoNuovaUEB) {
		this.rientroDaInserimentoNuovaUEB = rientroDaInserimentoNuovaUEB;
	}

	/**
	 * @return the listaUEBDaInserireNellaVariazione
	 */
	public List<ElementoCapitoloVariazione> getListaUEBDaInserireNellaVariazione() {
		return listaUEBDaInserireNellaVariazione;
	}

	/**
	 * @param listaUEBDaInserireNellaVariazione the listaUEBDaInserireNellaVariazione to set
	 */
	public void setListaUEBDaInserireNellaVariazione(List<ElementoCapitoloVariazione> listaUEBDaInserireNellaVariazione) {
		this.listaUEBDaInserireNellaVariazione = listaUEBDaInserireNellaVariazione;
	}

	/**
	 * @return the listaUEBNellaVariazioneCollassate
	 */
	public List<ElementoCapitoloVariazione> getListaUEBNellaVariazioneCollassate() {
		return listaUEBNellaVariazioneCollassate;
	}

	/**
	 * @param listaUEBNellaVariazioneCollassate the listaUEBNellaVariazioneCollassate to set
	 */
	public void setListaUEBNellaVariazioneCollassate(List<ElementoCapitoloVariazione> listaUEBNellaVariazioneCollassate) {
		this.listaUEBNellaVariazioneCollassate = listaUEBNellaVariazioneCollassate;
	}

	/**
	 * @return the listaUEBNellaVariazione
	 */
	public List<ElementoCapitoloVariazione> getListaUEBNellaVariazione() {
		return listaUEBNellaVariazione;
	}

	/**
	 * @param listaUEBNellaVariazione the listaUEBNellaVariazione to set
	 */
	public void setListaUEBNellaVariazione(List<ElementoCapitoloVariazione> listaUEBNellaVariazione) {
		this.listaUEBNellaVariazione = listaUEBNellaVariazione;
	}
	
	/**
	 * @return the listaUEBDaAnnullare
	 */
	public List<ElementoCapitoloVariazione> getListaUEBDaAnnullare() {
		return listaUEBDaAnnullare;
	}

	/**
	 * @param listaUEBDaAnnullare the listaUEBDaAnnullare to set
	 */
	public void setListaUEBDaAnnullare(List<ElementoCapitoloVariazione> listaUEBDaAnnullare) {
		this.listaUEBDaAnnullare = listaUEBDaAnnullare;
	}

	/**
	 * @return the elementoImportiVariazione
	 */
	public ElementoImportiVariazione getElementoImportiVariazione() {
		return elementoImportiVariazione;
	}

	/**
	 * @param elementoImportiVariazione the elementoImportiVariazione to set
	 */
	public void setElementoImportiVariazione(ElementoImportiVariazione elementoImportiVariazione) {
		this.elementoImportiVariazione = elementoImportiVariazione;
	}
	
}
