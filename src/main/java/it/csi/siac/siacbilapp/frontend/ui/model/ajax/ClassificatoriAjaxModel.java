/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.codifica.ElementoCodifica;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di Model per le chiamate AJAX.
 * La presente classe serve ad immagazzinare i dati passati da <em>Struts2</em> via chiamata di tipo AJAX, e
 * ad immagazzinare i risultati dei WebServices che serviranno come risposta alla chiamata asincrona.
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 05/07/2013
 *
 */
public class ClassificatoriAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1134871134767348538L;
	
	/* GENERALE */
	
	/** Uid fornito dalla GUI */
	private Integer id;
	
	private Boolean daInjettareInserimentoVariazione = Boolean.FALSE;
	private Boolean daInjettareAggiornamentoVariazione = Boolean.FALSE;
	
	//
	private String nomeAzioneDecentrata;
	
	/** Lista per lo zTree */
	private List<ElementoCodifica> listaElementoCodifica = new ArrayList<ElementoCodifica>();
	
	/* USCITA */	
	/** Lista dei Programmi per il popolamento del tag relativo */
	private List<Programma> listaProgramma = new ArrayList<Programma>();
	/** Lista delle Classificazioni Cofog per il popolamento del tag relativo */
	private List<ClassificazioneCofog> listaClassificazioneCofog = new ArrayList<ClassificazioneCofog>();
	/** Lista dei Macroaggregati per il popolamento del tag relativo */
	private List<Macroaggregato> listaMacroaggregato = new ArrayList<Macroaggregato>();
	/** Lista dei Titoli Di Spesa **/
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();

	
	/* ENTRATA */	
	/** Lista delle Tipologie Titolo per il popolamento del tag relativo */
	private List<TipologiaTitolo> listaTipologiaTitolo = new ArrayList<TipologiaTitolo>();
	/** Lista delle Categorie della Tipologia Titolo per il popolamento del tag relativo*/
	private List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = new ArrayList<CategoriaTipologiaTitolo>();
	
	/* ONERE */
	private List<TipoOnere> listaTipoOnere = new ArrayList<TipoOnere>();
	private List<AttivitaOnere> listaAttivitaOnere = new ArrayList<AttivitaOnere>();
	private List<Causale770> listaCausale770 = new ArrayList<Causale770>();
	private List<CodiceSommaNonSoggetta> codiciSommaNonSoggetta = new ArrayList<CodiceSommaNonSoggetta>();
	
	private boolean useDefaults = false;
	
	/** Costruttore vuoto di default */
	public ClassificatoriAjaxModel() {
		super();
		setTitolo("Ajax Model");
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the daInjettareInserimentoVariazione
	 */
	public Boolean getDaInjettareInserimentoVariazione() {
		return daInjettareInserimentoVariazione;
	}

	/**
	 * @param daInjettareInserimentoVariazione the daInjettareInserimentoVariazione to set
	 */
	public void setDaInjettareInserimentoVariazione(Boolean daInjettareInserimentoVariazione) {
		this.daInjettareInserimentoVariazione = daInjettareInserimentoVariazione;
	}

	/**
	 * @return the daInjettareAggiornamentoVariazione
	 */
	public Boolean getDaInjettareAggiornamentoVariazione() {
		return daInjettareAggiornamentoVariazione;
	}

	/**
	 * @param daInjettareAggiornamentoVariazione the daInjettareAggiornamentoVariazione to set
	 */
	public void setDaInjettareAggiornamentoVariazione(Boolean daInjettareAggiornamentoVariazione) {
		this.daInjettareAggiornamentoVariazione = daInjettareAggiornamentoVariazione;
	}

	/**
	 * @return the nomeAzioneDecentrata
	 */
	public String getNomeAzioneDecentrata() {
		return nomeAzioneDecentrata;
	}

	/**
	 * @param nomeAzioneDecentrata the nomeAzioneDecentrata to set
	 */
	public void setNomeAzioneDecentrata(String nomeAzioneDecentrata) {
		this.nomeAzioneDecentrata = nomeAzioneDecentrata;
	}

	/**
	 * @return the listaElementoCodifica
	 */
	public List<ElementoCodifica> getListaElementoCodifica() {
		return listaElementoCodifica;
	}

	/**
	 * @param listaElementoCodifica the listaElementoCodifica to set
	 */
	public void setListaElementoCodifica(List<ElementoCodifica> listaElementoCodifica) {
		this.listaElementoCodifica = listaElementoCodifica != null ? listaElementoCodifica : new ArrayList<ElementoCodifica>();
	}

	/**
	 * @return the listaProgramma
	 */
	public List<Programma> getListaProgramma() {
		return listaProgramma;
	}

	/**
	 * @param listaProgramma the listaProgramma to set
	 */
	public void setListaProgramma(List<Programma> listaProgramma) {
		this.listaProgramma = listaProgramma != null ? listaProgramma : new ArrayList<Programma>();
	}

	/**
	 * @return the listaClassificazioneCofog
	 */
	public List<ClassificazioneCofog> getListaClassificazioneCofog() {
		return listaClassificazioneCofog;
	}

	/**
	 * @param listaClassificazioneCofog the listaClassificazioneCofog to set
	 */
	public void setListaClassificazioneCofog(List<ClassificazioneCofog> listaClassificazioneCofog) {
		this.listaClassificazioneCofog = listaClassificazioneCofog != null ? listaClassificazioneCofog : new ArrayList<ClassificazioneCofog>();
	}

	/**
	 * @return the listaMacroaggregato
	 */
	public List<Macroaggregato> getListaMacroaggregato() {
		return listaMacroaggregato;
	}

	/**
	 * @param listaMacroaggregato the listaMacroaggregato to set
	 */
	public void setListaMacroaggregato(List<Macroaggregato> listaMacroaggregato) {
		this.listaMacroaggregato = listaMacroaggregato != null ? listaMacroaggregato : new ArrayList<Macroaggregato>();
	}

	/**
	 * @return the listaTipologiaTitolo
	 */
	public List<TipologiaTitolo> getListaTipologiaTitolo() {
		return listaTipologiaTitolo;
	}

	/**
	 * @param listaTipologiaTitolo the listaTipologiaTitolo to set
	 */
	public void setListaTipologiaTitolo(List<TipologiaTitolo> listaTipologiaTitolo) {
		this.listaTipologiaTitolo = listaTipologiaTitolo != null ? listaTipologiaTitolo : new ArrayList<TipologiaTitolo>();
	}

	/**
	 * @return the listaCategoriaTipologiaTitolo
	 */
	public List<CategoriaTipologiaTitolo> getListaCategoriaTipologiaTitolo() {
		return listaCategoriaTipologiaTitolo;
	}

	/**
	 * @param listaCategoriaTipologiaTitolo the listaCategoriaTipologiaTitolo to set
	 */
	public void setListaCategoriaTipologiaTitolo(List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo) {
		this.listaCategoriaTipologiaTitolo = listaCategoriaTipologiaTitolo != null ? listaCategoriaTipologiaTitolo : new ArrayList<CategoriaTipologiaTitolo>();
	}

	/**
	 * @return the listaTipoOnere
	 */
	public List<TipoOnere> getListaTipoOnere() {
		return listaTipoOnere;
	}

	/**
	 * @param listaTipoOnere the listaTipoOnere to set
	 */
	public void setListaTipoOnere(List<TipoOnere> listaTipoOnere) {
		this.listaTipoOnere = listaTipoOnere != null ? listaTipoOnere : new ArrayList<TipoOnere>();
	}

	/**
	 * @return the listaAttivitaOnere
	 */
	public List<AttivitaOnere> getListaAttivitaOnere() {
		return listaAttivitaOnere;
	}

	/**
	 * @param listaAttivitaOnere the listaAttivitaOnere to set
	 */
	public void setListaAttivitaOnere(List<AttivitaOnere> listaAttivitaOnere) {
		this.listaAttivitaOnere = listaAttivitaOnere != null ? listaAttivitaOnere : new ArrayList<AttivitaOnere>();
	}

	/**
	 * @return the listaCausale770
	 */
	public List<Causale770> getListaCausale770() {
		return listaCausale770;
	}

	/**
	 * @param listaCausale770 the listaCausale770 to set
	 */
	public void setListaCausale770(List<Causale770> listaCausale770) {
		this.listaCausale770 = listaCausale770 != null ? listaCausale770 : new ArrayList<Causale770>();
	}
	

	/**
	 * @return the codiciSommaNonSoggetta
	 */
	public List<CodiceSommaNonSoggetta> getCodiciSommaNonSoggetta() {
		return codiciSommaNonSoggetta;
	}

	/**
	 * @param codiciSommaNonSoggetta the codiciSommaNonSoggetta to set
	 */
	public void setCodiciSommaNonSoggetta(List<CodiceSommaNonSoggetta> codiciSommaNonSoggetta) {
		this.codiciSommaNonSoggetta = codiciSommaNonSoggetta != null ? codiciSommaNonSoggetta : new ArrayList<CodiceSommaNonSoggetta>();
	}
	
	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}
	
	/**
	 * @return the useDefaults
	 */
	public boolean isUseDefaults() {
		return useDefaults;
	}

	/**
	 * @param useDefaults the useDefaults to set
	 */
	public void setUseDefaults(boolean useDefaults) {
		this.useDefaults = useDefaults;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof ClassificatoriAjaxModel)) {
			return false;
		}
		ClassificatoriAjaxModel other = (ClassificatoriAjaxModel)obj;
		return new EqualsBuilder()
			.append(this.id, other.id)
			.isEquals();
	}

	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(id)
			.toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("Id", id)
			.toString();
	}
	
}
