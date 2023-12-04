/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.anagcomp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.ComponenteCapitoloModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.AmbitoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.FonteFinanziariaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImpegnabileComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MomentoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

/**
 * @author filippo
 *
 */
public class InserisciComponenteCapitoloModel extends ComponenteCapitoloModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	private TipoComponenteImportiCapitolo componenteCapitolo;
	
	private List<MacrotipoComponenteImportiCapitolo> listaMacroTipo = new ArrayList<MacrotipoComponenteImportiCapitolo>();
	private List<SottotipoComponenteImportiCapitolo> listaSottoTipo = new ArrayList<SottotipoComponenteImportiCapitolo>();
	private List<AmbitoComponenteImportiCapitolo> listaAmbito = new ArrayList<AmbitoComponenteImportiCapitolo>();
	private List<FonteFinanziariaComponenteImportiCapitolo> listaFonteFinanziamento = new ArrayList<FonteFinanziariaComponenteImportiCapitolo>();
	private List<MomentoComponenteImportiCapitolo> listaMomento = new ArrayList<MomentoComponenteImportiCapitolo>();
	private List<PropostaDefaultComponenteImportiCapitolo> listaPrevisione = new ArrayList<PropostaDefaultComponenteImportiCapitolo>();
	//SIAC-7349
	//private List<TipoGestioneComponenteImportiCapitolo> listaGestione = new ArrayList<TipoGestioneComponenteImportiCapitolo>();
	private List<ImpegnabileComponenteImportiCapitolo> listaImpegnabile = new ArrayList<ImpegnabileComponenteImportiCapitolo>();
	
	/** Costruttore vuoto di default */
	public InserisciComponenteCapitoloModel() {
		super();
		setTitolo("Inserimento Componente Capitolo");
	}
	
	/* Getter e Setter */
	
	public TipoComponenteImportiCapitolo getComponenteCapitolo() {
		return componenteCapitolo;
	}

	public void setComponenteCapitolo(TipoComponenteImportiCapitolo componenteCapitolo) {
		this.componenteCapitolo = componenteCapitolo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((componenteCapitolo == null) ? 0 : componenteCapitolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InserisciComponenteCapitoloModel other = (InserisciComponenteCapitoloModel) obj;
		if (componenteCapitolo == null) {
			if (other.componenteCapitolo != null)
				return false;
		} else if (!componenteCapitolo.equals(other.componenteCapitolo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InserisciComponenteCapitoloModel [componenteCapitolo=" + componenteCapitolo + "]";
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

	public List<AmbitoComponenteImportiCapitolo> getListaAmbito() {
		return listaAmbito;
	}

	public void setListaAmbito(List<AmbitoComponenteImportiCapitolo> listaAmbito) {
		this.listaAmbito = listaAmbito;
	}

	public List<FonteFinanziariaComponenteImportiCapitolo> getListaFonteFinanziamento() {
		return listaFonteFinanziamento;
	}

	public void setListaFonteFinanziamento(List<FonteFinanziariaComponenteImportiCapitolo> listaFonteFinanziamento) {
		this.listaFonteFinanziamento = listaFonteFinanziamento;
	}

	public List<MomentoComponenteImportiCapitolo> getListaMomento() {
		return listaMomento;
	}

	public void setListaMomento(List<MomentoComponenteImportiCapitolo> listaMomento) {
		this.listaMomento = listaMomento;
	}

	public List<PropostaDefaultComponenteImportiCapitolo> getListaPrevisione() {
		return listaPrevisione;
	}

	public void setListaPrevisione(List<PropostaDefaultComponenteImportiCapitolo> listaPrevisione) {
		this.listaPrevisione = listaPrevisione;
	}

//	public List<TipoGestioneComponenteImportiCapitolo> getListaGestione() {
//		return listaGestione;
//	}
//
//	public void setListaGestione(List<TipoGestioneComponenteImportiCapitolo> listaGestione) {
//		this.listaGestione = listaGestione;
//	}

	
	
	/**
	 * @return the listaImpegnabile
	 */
	public List<ImpegnabileComponenteImportiCapitolo> getListaImpegnabile() {
		return listaImpegnabile;
	}

	/**
	 * @param listaImpegnabile the listaImpegnabile to set
	 */
	public void setListaImpegnabile(List<ImpegnabileComponenteImportiCapitolo> listaImpegnabile) {
		this.listaImpegnabile = listaImpegnabile;
	}

	/* Requests */	
	public InserisceTipoComponenteImportiCapitolo creaRequestInserisceComponenteCapitolo() {
		InserisceTipoComponenteImportiCapitolo request = new InserisceTipoComponenteImportiCapitolo();
		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		//SIAC-7246
		setDataInizioValiditaToDefaultIfNull();
				
		//TODO : rimuovere il codice del TipoComponenteImportiCapitolo 
		//SIAC-7492
//		componenteCapitolo.setCodice("05");
		request.setTipoComponenteImportiCapitolo(componenteCapitolo);
		return request;
	}
	
	private void setDataInizioValiditaToDefaultIfNull(){
		String methodName = "creaRequestInserisceComponenteCapitolo() - setDataInizioValiditaToDefaultIfNull";
		if(componenteCapitolo.getDataInizioValidita() == null && getBilancio() != null && getBilancio().getAnno() != 0) {
			Date defaultDate = null;
			try {
				defaultDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/" + getBilancio().getAnno());
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			componenteCapitolo.setDataInizioValidita(defaultDate);
		} else if(componenteCapitolo.getDataInizioValidita() != null) {
			log.debug(methodName, "data inizio validita' corretta");
		} else {
			log.debug(methodName, "data inizio validita non pressente");
		}
	}
	
}
