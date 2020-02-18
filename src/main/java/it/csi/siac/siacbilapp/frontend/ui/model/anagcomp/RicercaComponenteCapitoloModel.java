/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.anagcomp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.ComponenteCapitoloModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;


/**
 * Classe di model per la ricerca del componente. Contiene una mappatura dei campi della ricerca.
 * 
 * @author Jacopo Bongiovanni
 * @version 1.0.0 - 17/09/2019
 *
 */
public class RicercaComponenteCapitoloModel  extends ComponenteCapitoloModel {

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 610669889736596618L;

	private TipoComponenteImportiCapitolo componenteCapitolo;
	
	private MacrotipoComponenteImportiCapitolo macroComponenteCapitolo;
	private SottotipoComponenteImportiCapitolo sottoComponenteCapitolo;
	
 
	
	private List<MacrotipoComponenteImportiCapitolo> listaMacroTipo = new ArrayList<MacrotipoComponenteImportiCapitolo>();
	private List<SottotipoComponenteImportiCapitolo> listaSottoTipo = new ArrayList<SottotipoComponenteImportiCapitolo>();
	

	

	/** Costruttore vuoto di default */
	public RicercaComponenteCapitoloModel() {
		super();
		setTitolo("Ricerca Componente Capitolo");
	}
	
	/* Getter e Setter */
	
	public TipoComponenteImportiCapitolo getComponenteCapitolo() {
		return componenteCapitolo;
	}

	public void setComponenteCapitolo(TipoComponenteImportiCapitolo componenteCapitolo) {
		this.componenteCapitolo = componenteCapitolo;
	}

	/* Requests */	
	public InserisceTipoComponenteImportiCapitolo creaRequestInserisceComponenteCapitolo(StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {

		InserisceTipoComponenteImportiCapitolo request = new InserisceTipoComponenteImportiCapitolo();
		
		componenteCapitolo.setAnno(getBilancio().getAnno());
		
		/* aggiungere gli altri? */
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setTipoComponenteImportiCapitolo(componenteCapitolo);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoComponenteImportiCapitolo} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaTipoComponenteImportiCapitolo creaRequestComponenteCapitolo() {
		RicercaSinteticaTipoComponenteImportiCapitolo request = creaRequest(RicercaSinteticaTipoComponenteImportiCapitolo.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
				
		// Injezione della utility di ricerca
		request.setTipoComponenteImportiCapitolo(creaUtilityRicercaComponenteCapitolo());
		
		return request;
	}

	
	/**
	 * Metodo di utilit&agrave; per la ricerca del provvedimento.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private TipoComponenteImportiCapitolo creaUtilityRicercaComponenteCapitolo() {
		RicercaTipoComponenteImportiCapitolo utility = new RicercaTipoComponenteImportiCapitolo();
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
		 
		
		utility.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		// Controllo che il provvedimento sia stato inizializzato
		if(componenteCapitolo != null) {
			// Injetto la descrizione se sono state inizializzate
			utility.getTipoComponenteImportiCapitolo().setDescrizione(StringUtils.isNotBlank(componenteCapitolo.getDescrizione()) ? componenteCapitolo.getDescrizione() : null);
			utility.getTipoComponenteImportiCapitolo().setMacrotipoComponenteImportiCapitolo(componenteCapitolo.getMacrotipoComponenteImportiCapitolo());
			utility.getTipoComponenteImportiCapitolo().setSottotipoComponenteImportiCapitolo(componenteCapitolo.getSottotipoComponenteImportiCapitolo());
		}
		
		

		
		return utility.getTipoComponenteImportiCapitolo();
	}
	
	  

	/**
	 * @return the macroComponenteCapitolo
	 */
	public MacrotipoComponenteImportiCapitolo getMacroComponenteCapitolo() {
		return macroComponenteCapitolo;
	}

	/**
	 * @param macroComponenteCapitolo the macroComponenteCapitolo to set
	 */
	public void setMacroComponenteCapitolo(MacrotipoComponenteImportiCapitolo macroComponenteCapitolo) {
		this.macroComponenteCapitolo = macroComponenteCapitolo;
	}

	/**
	 * @return the sottoComponenteCapitolo
	 */
	public SottotipoComponenteImportiCapitolo getSottoComponenteCapitolo() {
		return sottoComponenteCapitolo;
	}

	/**
	 * @param sottoComponenteCapitolo the sottoComponenteCapitolo to set
	 */
	public void setSottoComponenteCapitolo(SottotipoComponenteImportiCapitolo sottoComponenteCapitolo) {
		this.sottoComponenteCapitolo = sottoComponenteCapitolo;
	}

	/**
	 * @return the listaMacroTipo
	 */
	public List<MacrotipoComponenteImportiCapitolo> getListaMacroTipo() {
		return listaMacroTipo;
	}

	/**
	 * @param listaMacroTipo the listaMacroTipo to set
	 */
	public void setListaMacroTipo(List<MacrotipoComponenteImportiCapitolo> listaMacroTipo) {
		this.listaMacroTipo = listaMacroTipo;
	}

	/**
	 * @return the listaSottoTipo
	 */
	public List<SottotipoComponenteImportiCapitolo> getListaSottoTipo() {
		return listaSottoTipo;
	}

	/**
	 * @param listaSottoTipo the listaSottoTipo to set
	 */
	public void setListaSottoTipo(List<SottotipoComponenteImportiCapitolo> listaSottoTipo) {
		this.listaSottoTipo = listaSottoTipo;
	}
 
	 
	
	
	
	
	
}
