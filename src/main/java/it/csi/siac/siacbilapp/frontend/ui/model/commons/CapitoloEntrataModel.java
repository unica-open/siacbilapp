/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Superclasse per il model del Capitolo di Entrata.
 * <br>
 * Si occupa di definire i campi e i metodi comuni per le actions del Capitolo di Entrata, con attenzione a:
 * <ul>
 *   <li>aggiornamento</li>
 *   <li>inserimento</li>
 *   <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 03/12/2013
 *
 */
public abstract class CapitoloEntrataModel extends CapitoloModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1393414257243302252L;
	
	
	/* Prima maschera: dati di base */
	private TitoloEntrata            titoloEntrata;
	private TipologiaTitolo          tipologiaTitolo;
	private CategoriaTipologiaTitolo categoriaTipologiaTitolo;
	
	private Integer codiceTitolo;
	
	/* Liste */
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	
	/* Liste derivate */
	private List<TipologiaTitolo>          listaTipologiaTitolo          = new ArrayList<TipologiaTitolo>();
	private List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = new ArrayList<CategoriaTipologiaTitolo>();
	
	/* Booleani rappresentanti i campi, per la editabilita' */
	private boolean titoloEntrataEditabile;
	private boolean tipologiaTitoloEditabile;
	private boolean categoriaTipologiaTitoloEditabile;
	
	// Classificatori fase 4
	private SiopeEntrata siopeEntrata;
	private RicorrenteEntrata ricorrenteEntrata;
	private PerimetroSanitarioEntrata perimetroSanitarioEntrata;
	private TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata;
	
	private String siopeInserito;
	
	private List<SiopeEntrata> listaSiopeEntrata = new ArrayList<SiopeEntrata>();
	private List<RicorrenteEntrata> listaRicorrenteEntrata = new ArrayList<RicorrenteEntrata>();
	private List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata = new ArrayList<PerimetroSanitarioEntrata>();
	private List<TransazioneUnioneEuropeaEntrata> listaTransazioneUnioneEuropeaEntrata = new ArrayList<TransazioneUnioneEuropeaEntrata>();
	
	private boolean siopeEntrataEditabile;
	private boolean ricorrenteEntrataEditabile;
	private boolean perimetroSanitarioEntrataEditabile;
	private boolean transazioneUnioneEuropeaEntrataEditabile;
	
	private boolean flagEntrateRicorrenti;
	
	//SIAC-8517
	private ClassificatoreGenerico classificatoreGenerico36;
	private ClassificatoreGenerico classificatoreGenerico37;
	private ClassificatoreGenerico classificatoreGenerico38;
	private ClassificatoreGenerico classificatoreGenerico39;
	private ClassificatoreGenerico classificatoreGenerico40;
	private ClassificatoreGenerico classificatoreGenerico41;
	private ClassificatoreGenerico classificatoreGenerico42;
	private ClassificatoreGenerico classificatoreGenerico43;
	private ClassificatoreGenerico classificatoreGenerico44;
	private ClassificatoreGenerico classificatoreGenerico45;
	private ClassificatoreGenerico classificatoreGenerico46;
	private ClassificatoreGenerico classificatoreGenerico47;
	private ClassificatoreGenerico classificatoreGenerico48;
	private ClassificatoreGenerico classificatoreGenerico49;
	private ClassificatoreGenerico classificatoreGenerico50;
	private List<ClassificatoreGenerico> listaClassificatoreGenerico36 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico37 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico38 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico39 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico40 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico41 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico42 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico43 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico44 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico45 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico46 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico47 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico48 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico49 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico50 = new ArrayList<ClassificatoreGenerico>();
	private String labelClassificatoreGenerico36;
	private String labelClassificatoreGenerico37;
	private String labelClassificatoreGenerico38;
	private String labelClassificatoreGenerico39;
	private String labelClassificatoreGenerico40;
	private String labelClassificatoreGenerico41;
	private String labelClassificatoreGenerico42;
	private String labelClassificatoreGenerico43;
	private String labelClassificatoreGenerico44;
	private String labelClassificatoreGenerico45;
	private String labelClassificatoreGenerico46;
	private String labelClassificatoreGenerico47;
	private String labelClassificatoreGenerico48;
	private String labelClassificatoreGenerico49;
	private String labelClassificatoreGenerico50;
	private boolean classificatoreGenerico36Editabile;
	private boolean classificatoreGenerico37Editabile;
	private boolean classificatoreGenerico38Editabile;
	private boolean classificatoreGenerico39Editabile;
	private boolean classificatoreGenerico40Editabile;
	private boolean classificatoreGenerico41Editabile;
	private boolean classificatoreGenerico42Editabile;
	private boolean classificatoreGenerico43Editabile;
	private boolean classificatoreGenerico44Editabile;
	private boolean classificatoreGenerico45Editabile;
	private boolean classificatoreGenerico46Editabile;
	private boolean classificatoreGenerico47Editabile;
	private boolean classificatoreGenerico48Editabile;
	private boolean classificatoreGenerico49Editabile;
	private boolean classificatoreGenerico50Editabile;
	
	
	
	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}
	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}
	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}
	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}
	/**
	 * @return the categoriaTipologiaTitolo
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitolo() {
		return categoriaTipologiaTitolo;
	}
	/**
	 * @param categoriaTipologiaTitolo the categoriaTipologiaTitolo to set
	 */
	public void setCategoriaTipologiaTitolo(CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		this.categoriaTipologiaTitolo = categoriaTipologiaTitolo;
	}
	/**
	 * @return the codiceTitolo
	 */
	public Integer getCodiceTitolo() {
		return codiceTitolo;
	}
	/**
	 * @param codiceTitolo the codiceTitolo to set
	 */
	public void setCodiceTitolo(Integer codiceTitolo) {
		this.codiceTitolo = codiceTitolo;
	}
	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}
	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
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
		this.listaTipologiaTitolo = listaTipologiaTitolo;
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
		this.listaCategoriaTipologiaTitolo = listaCategoriaTipologiaTitolo;
	}
	/**
	 * @return the titoloEntrataEditabile
	 */
	public boolean isTitoloEntrataEditabile() {
		return titoloEntrataEditabile;
	}
	/**
	 * @param titoloEntrataEditabile the titoloEntrataEditabile to set
	 */
	public void setTitoloEntrataEditabile(boolean titoloEntrataEditabile) {
		this.titoloEntrataEditabile = titoloEntrataEditabile;
	}
	/**
	 * @return the tipologiaTitoloEditabile
	 */
	public boolean isTipologiaTitoloEditabile() {
		return tipologiaTitoloEditabile;
	}
	/**
	 * @param tipologiaTitoloEditabile the tipologiaTitoloEditabile to set
	 */
	public void setTipologiaTitoloEditabile(boolean tipologiaTitoloEditabile) {
		this.tipologiaTitoloEditabile = tipologiaTitoloEditabile;
	}
	/**
	 * @return the categoriaTipologiaTitoloEditabile
	 */
	public boolean isCategoriaTipologiaTitoloEditabile() {
		return categoriaTipologiaTitoloEditabile;
	}
	/**
	 * @param categoriaTipologiaTitoloEditabile the categoriaTipologiaTitoloEditabile to set
	 */
	public void setCategoriaTipologiaTitoloEditabile(boolean categoriaTipologiaTitoloEditabile) {
		this.categoriaTipologiaTitoloEditabile = categoriaTipologiaTitoloEditabile;
	}
	
	/**
	 * @return the siopeEntrata
	 */
	public SiopeEntrata getSiopeEntrata() {
		return siopeEntrata;
	}
	/**
	 * @param siopeEntrata the siopeEntrata to set
	 */
	public void setSiopeEntrata(SiopeEntrata siopeEntrata) {
		this.siopeEntrata = siopeEntrata;
	}
	/**
	 * @return the ricorrenteEntrata
	 */
	public RicorrenteEntrata getRicorrenteEntrata() {
		return ricorrenteEntrata;
	}
	/**
	 * @param ricorrenteEntrata the ricorrenteEntrata to set
	 */
	public void setRicorrenteEntrata(RicorrenteEntrata ricorrenteEntrata) {
		this.ricorrenteEntrata = ricorrenteEntrata;
	}
	/**
	 * @return the perimetroSanitarioEntrata
	 */
	public PerimetroSanitarioEntrata getPerimetroSanitarioEntrata() {
		return perimetroSanitarioEntrata;
	}
	/**
	 * @param perimetroSanitarioEntrata the perimetroSanitarioEntrata to set
	 */
	public void setPerimetroSanitarioEntrata(
			PerimetroSanitarioEntrata perimetroSanitarioEntrata) {
		this.perimetroSanitarioEntrata = perimetroSanitarioEntrata;
	}
	/**
	 * @return the transazioneUnioneEuropeaEntrata
	 */
	public TransazioneUnioneEuropeaEntrata getTransazioneUnioneEuropeaEntrata() {
		return transazioneUnioneEuropeaEntrata;
	}
	/**
	 * @param transazioneUnioneEuropeaEntrata the transazioneUnioneEuropeaEntrata to set
	 */
	public void setTransazioneUnioneEuropeaEntrata(TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata) {
		this.transazioneUnioneEuropeaEntrata = transazioneUnioneEuropeaEntrata;
	}
	/**
	 * @return the siopeInserito
	 */
	public String getSiopeInserito() {
		return siopeInserito;
	}
	/**
	 * @param siopeInserito the siopeInserito to set
	 */
	public void setSiopeInserito(String siopeInserito) {
		this.siopeInserito = siopeInserito;
	}
	/**
	 * @return the listaSiopeEntrata
	 */
	public List<SiopeEntrata> getListaSiopeEntrata() {
		return listaSiopeEntrata;
	}
	/**
	 * @param listaSiopeEntrata the listaSiopeEntrata to set
	 */
	public void setListaSiopeEntrata(List<SiopeEntrata> listaSiopeEntrata) {
		this.listaSiopeEntrata = listaSiopeEntrata;
	}
	/**
	 * @return the listaRicorrenteEntrata
	 */
	public List<RicorrenteEntrata> getListaRicorrenteEntrata() {
		return listaRicorrenteEntrata;
	}
	/**
	 * @param listaRicorrenteEntrata the listaRicorrenteEntrata to set
	 */
	public void setListaRicorrenteEntrata(
			List<RicorrenteEntrata> listaRicorrenteEntrata) {
		this.listaRicorrenteEntrata = listaRicorrenteEntrata;
	}
	/**
	 * @return the listaPerimetroSanitarioEntrata
	 */
	public List<PerimetroSanitarioEntrata> getListaPerimetroSanitarioEntrata() {
		return listaPerimetroSanitarioEntrata;
	}
	/**
	 * @param listaPerimetroSanitarioEntrata the listaPerimetroSanitarioEntrata to set
	 */
	public void setListaPerimetroSanitarioEntrata(List<PerimetroSanitarioEntrata> listaPerimetroSanitarioEntrata) {
		this.listaPerimetroSanitarioEntrata = listaPerimetroSanitarioEntrata;
	}
	/**
	 * @return the listaTransazioneUnioneEuropeaEntrata
	 */
	public List<TransazioneUnioneEuropeaEntrata> getListaTransazioneUnioneEuropeaEntrata() {
		return listaTransazioneUnioneEuropeaEntrata;
	}
	/**
	 * @param listaTransazioneUnioneEuropeaEntrata the listaTransazioneUnioneEuropeaEntrata to set
	 */
	public void setListaTransazioneUnioneEuropeaEntrata(List<TransazioneUnioneEuropeaEntrata> listaTransazioneUnioneEuropeaEntrata) {
		this.listaTransazioneUnioneEuropeaEntrata = listaTransazioneUnioneEuropeaEntrata;
	}
	/**
	 * @return the siopeEntrataEditabile
	 */
	public boolean isSiopeEntrataEditabile() {
		return siopeEntrataEditabile;
	}
	/**
	 * @param siopeEntrataEditabile the siopeEntrataEditabile to set
	 */
	public void setSiopeEntrataEditabile(boolean siopeEntrataEditabile) {
		this.siopeEntrataEditabile = siopeEntrataEditabile;
	}
	/**
	 * @return the ricorrenteEntrataEditabile
	 */
	public boolean isRicorrenteEntrataEditabile() {
		return ricorrenteEntrataEditabile;
	}
	/**
	 * @param ricorrenteEntrataEditabile the ricorrenteEntrataEditabile to set
	 */
	public void setRicorrenteEntrataEditabile(boolean ricorrenteEntrataEditabile) {
		this.ricorrenteEntrataEditabile = ricorrenteEntrataEditabile;
	}
	/**
	 * @return the perimetroSanitarioEntrataEditabile
	 */
	public boolean isPerimetroSanitarioEntrataEditabile() {
		return perimetroSanitarioEntrataEditabile;
	}
	/**
	 * @param perimetroSanitarioEntrataEditabile the perimetroSanitarioEntrataEditabile to set
	 */
	public void setPerimetroSanitarioEntrataEditabile(boolean perimetroSanitarioEntrataEditabile) {
		this.perimetroSanitarioEntrataEditabile = perimetroSanitarioEntrataEditabile;
	}
	/**
	 * @return the transazioneUnioneEuropeaEntrataEditabile
	 */
	public boolean isTransazioneUnioneEuropeaEntrataEditabile() {
		return transazioneUnioneEuropeaEntrataEditabile;
	}
	/**
	 * @param transazioneUnioneEuropeaEntrataEditabile the transazioneUnioneEuropeaEntrataEditabile to set
	 */
	public void setTransazioneUnioneEuropeaEntrataEditabile(boolean transazioneUnioneEuropeaEntrataEditabile) {
		this.transazioneUnioneEuropeaEntrataEditabile = transazioneUnioneEuropeaEntrataEditabile;
	}
	
	/**
	 * @return the flagEntrateRicorrenti
	 */
	public boolean isFlagEntrateRicorrenti() {
		return flagEntrateRicorrenti;
	}
	/**
	 * @param flagEntrateRicorrenti the flagEntrateRicorrenti to set
	 */
	public void setFlagEntrateRicorrenti(boolean flagEntrateRicorrenti) {
		this.flagEntrateRicorrenti = flagEntrateRicorrenti;
	}
	
	
	public ClassificatoreGenerico getClassificatoreGenerico36() {
		return classificatoreGenerico36;
	}
	public void setClassificatoreGenerico36(ClassificatoreGenerico classificatoreGenerico36) {
		this.classificatoreGenerico36 = classificatoreGenerico36;
	}
	public ClassificatoreGenerico getClassificatoreGenerico37() {
		return classificatoreGenerico37;
	}
	public void setClassificatoreGenerico37(ClassificatoreGenerico classificatoreGenerico37) {
		this.classificatoreGenerico37 = classificatoreGenerico37;
	}
	public ClassificatoreGenerico getClassificatoreGenerico38() {
		return classificatoreGenerico38;
	}
	public void setClassificatoreGenerico38(ClassificatoreGenerico classificatoreGenerico38) {
		this.classificatoreGenerico38 = classificatoreGenerico38;
	}
	public ClassificatoreGenerico getClassificatoreGenerico39() {
		return classificatoreGenerico39;
	}
	public void setClassificatoreGenerico39(ClassificatoreGenerico classificatoreGenerico39) {
		this.classificatoreGenerico39 = classificatoreGenerico39;
	}
	public ClassificatoreGenerico getClassificatoreGenerico40() {
		return classificatoreGenerico40;
	}
	public void setClassificatoreGenerico40(ClassificatoreGenerico classificatoreGenerico40) {
		this.classificatoreGenerico40 = classificatoreGenerico40;
	}
	public ClassificatoreGenerico getClassificatoreGenerico41() {
		return classificatoreGenerico41;
	}
	public void setClassificatoreGenerico41(ClassificatoreGenerico classificatoreGenerico41) {
		this.classificatoreGenerico41 = classificatoreGenerico41;
	}
	public ClassificatoreGenerico getClassificatoreGenerico42() {
		return classificatoreGenerico42;
	}
	public void setClassificatoreGenerico42(ClassificatoreGenerico classificatoreGenerico42) {
		this.classificatoreGenerico42 = classificatoreGenerico42;
	}
	public ClassificatoreGenerico getClassificatoreGenerico43() {
		return classificatoreGenerico43;
	}
	public void setClassificatoreGenerico43(ClassificatoreGenerico classificatoreGenerico43) {
		this.classificatoreGenerico43 = classificatoreGenerico43;
	}
	public ClassificatoreGenerico getClassificatoreGenerico44() {
		return classificatoreGenerico44;
	}
	public void setClassificatoreGenerico44(ClassificatoreGenerico classificatoreGenerico44) {
		this.classificatoreGenerico44 = classificatoreGenerico44;
	}
	public ClassificatoreGenerico getClassificatoreGenerico45() {
		return classificatoreGenerico45;
	}
	public void setClassificatoreGenerico45(ClassificatoreGenerico classificatoreGenerico45) {
		this.classificatoreGenerico45 = classificatoreGenerico45;
	}
	public ClassificatoreGenerico getClassificatoreGenerico46() {
		return classificatoreGenerico46;
	}
	public void setClassificatoreGenerico46(ClassificatoreGenerico classificatoreGenerico46) {
		this.classificatoreGenerico46 = classificatoreGenerico46;
	}
	public ClassificatoreGenerico getClassificatoreGenerico47() {
		return classificatoreGenerico47;
	}
	public void setClassificatoreGenerico47(ClassificatoreGenerico classificatoreGenerico47) {
		this.classificatoreGenerico47 = classificatoreGenerico47;
	}
	public ClassificatoreGenerico getClassificatoreGenerico48() {
		return classificatoreGenerico48;
	}
	public void setClassificatoreGenerico48(ClassificatoreGenerico classificatoreGenerico48) {
		this.classificatoreGenerico48 = classificatoreGenerico48;
	}
	public ClassificatoreGenerico getClassificatoreGenerico49() {
		return classificatoreGenerico49;
	}
	public void setClassificatoreGenerico49(ClassificatoreGenerico classificatoreGenerico49) {
		this.classificatoreGenerico49 = classificatoreGenerico49;
	}
	public ClassificatoreGenerico getClassificatoreGenerico50() {
		return classificatoreGenerico50;
	}
	public void setClassificatoreGenerico50(ClassificatoreGenerico classificatoreGenerico50) {
		this.classificatoreGenerico50 = classificatoreGenerico50;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico36() {
		return listaClassificatoreGenerico36;
	}
	public void setListaClassificatoreGenerico36(List<ClassificatoreGenerico> listaClassificatoreGenerico36) {
		this.listaClassificatoreGenerico36 = listaClassificatoreGenerico36;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico37() {
		return listaClassificatoreGenerico37;
	}
	public void setListaClassificatoreGenerico37(List<ClassificatoreGenerico> listaClassificatoreGenerico37) {
		this.listaClassificatoreGenerico37 = listaClassificatoreGenerico37;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico38() {
		return listaClassificatoreGenerico38;
	}
	public void setListaClassificatoreGenerico38(List<ClassificatoreGenerico> listaClassificatoreGenerico38) {
		this.listaClassificatoreGenerico38 = listaClassificatoreGenerico38;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico39() {
		return listaClassificatoreGenerico39;
	}
	public void setListaClassificatoreGenerico39(List<ClassificatoreGenerico> listaClassificatoreGenerico39) {
		this.listaClassificatoreGenerico39 = listaClassificatoreGenerico39;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico40() {
		return listaClassificatoreGenerico40;
	}
	public void setListaClassificatoreGenerico40(List<ClassificatoreGenerico> listaClassificatoreGenerico40) {
		this.listaClassificatoreGenerico40 = listaClassificatoreGenerico40;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico41() {
		return listaClassificatoreGenerico41;
	}
	public void setListaClassificatoreGenerico41(List<ClassificatoreGenerico> listaClassificatoreGenerico41) {
		this.listaClassificatoreGenerico41 = listaClassificatoreGenerico41;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico42() {
		return listaClassificatoreGenerico42;
	}
	public void setListaClassificatoreGenerico42(List<ClassificatoreGenerico> listaClassificatoreGenerico42) {
		this.listaClassificatoreGenerico42 = listaClassificatoreGenerico42;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico43() {
		return listaClassificatoreGenerico43;
	}
	public void setListaClassificatoreGenerico43(List<ClassificatoreGenerico> listaClassificatoreGenerico43) {
		this.listaClassificatoreGenerico43 = listaClassificatoreGenerico43;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico44() {
		return listaClassificatoreGenerico44;
	}
	public void setListaClassificatoreGenerico44(List<ClassificatoreGenerico> listaClassificatoreGenerico44) {
		this.listaClassificatoreGenerico44 = listaClassificatoreGenerico44;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico45() {
		return listaClassificatoreGenerico45;
	}
	public void setListaClassificatoreGenerico45(List<ClassificatoreGenerico> listaClassificatoreGenerico45) {
		this.listaClassificatoreGenerico45 = listaClassificatoreGenerico45;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico46() {
		return listaClassificatoreGenerico46;
	}
	public void setListaClassificatoreGenerico46(List<ClassificatoreGenerico> listaClassificatoreGenerico46) {
		this.listaClassificatoreGenerico46 = listaClassificatoreGenerico46;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico47() {
		return listaClassificatoreGenerico47;
	}
	public void setListaClassificatoreGenerico47(List<ClassificatoreGenerico> listaClassificatoreGenerico47) {
		this.listaClassificatoreGenerico47 = listaClassificatoreGenerico47;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico48() {
		return listaClassificatoreGenerico48;
	}
	public void setListaClassificatoreGenerico48(List<ClassificatoreGenerico> listaClassificatoreGenerico48) {
		this.listaClassificatoreGenerico48 = listaClassificatoreGenerico48;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico49() {
		return listaClassificatoreGenerico49;
	}
	public void setListaClassificatoreGenerico49(List<ClassificatoreGenerico> listaClassificatoreGenerico49) {
		this.listaClassificatoreGenerico49 = listaClassificatoreGenerico49;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico50() {
		return listaClassificatoreGenerico50;
	}
	public void setListaClassificatoreGenerico50(List<ClassificatoreGenerico> listaClassificatoreGenerico50) {
		this.listaClassificatoreGenerico50 = listaClassificatoreGenerico50;
	}
	public String getLabelClassificatoreGenerico36() {
		return labelClassificatoreGenerico36;
	}
	public void setLabelClassificatoreGenerico36(String labelClassificatoreGenerico36) {
		this.labelClassificatoreGenerico36 = labelClassificatoreGenerico36;
	}
	public String getLabelClassificatoreGenerico37() {
		return labelClassificatoreGenerico37;
	}
	public void setLabelClassificatoreGenerico37(String labelClassificatoreGenerico37) {
		this.labelClassificatoreGenerico37 = labelClassificatoreGenerico37;
	}
	public String getLabelClassificatoreGenerico38() {
		return labelClassificatoreGenerico38;
	}
	public void setLabelClassificatoreGenerico38(String labelClassificatoreGenerico38) {
		this.labelClassificatoreGenerico38 = labelClassificatoreGenerico38;
	}
	public String getLabelClassificatoreGenerico39() {
		return labelClassificatoreGenerico39;
	}
	public void setLabelClassificatoreGenerico39(String labelClassificatoreGenerico39) {
		this.labelClassificatoreGenerico39 = labelClassificatoreGenerico39;
	}
	public String getLabelClassificatoreGenerico40() {
		return labelClassificatoreGenerico40;
	}
	public void setLabelClassificatoreGenerico40(String labelClassificatoreGenerico40) {
		this.labelClassificatoreGenerico40 = labelClassificatoreGenerico40;
	}
	public String getLabelClassificatoreGenerico41() {
		return labelClassificatoreGenerico41;
	}
	public void setLabelClassificatoreGenerico41(String labelClassificatoreGenerico41) {
		this.labelClassificatoreGenerico41 = labelClassificatoreGenerico41;
	}
	public String getLabelClassificatoreGenerico42() {
		return labelClassificatoreGenerico42;
	}
	public void setLabelClassificatoreGenerico42(String labelClassificatoreGenerico42) {
		this.labelClassificatoreGenerico42 = labelClassificatoreGenerico42;
	}
	public String getLabelClassificatoreGenerico43() {
		return labelClassificatoreGenerico43;
	}
	public void setLabelClassificatoreGenerico43(String labelClassificatoreGenerico43) {
		this.labelClassificatoreGenerico43 = labelClassificatoreGenerico43;
	}
	public String getLabelClassificatoreGenerico44() {
		return labelClassificatoreGenerico44;
	}
	public void setLabelClassificatoreGenerico44(String labelClassificatoreGenerico44) {
		this.labelClassificatoreGenerico44 = labelClassificatoreGenerico44;
	}
	public String getLabelClassificatoreGenerico45() {
		return labelClassificatoreGenerico45;
	}
	public void setLabelClassificatoreGenerico45(String labelClassificatoreGenerico45) {
		this.labelClassificatoreGenerico45 = labelClassificatoreGenerico45;
	}
	public String getLabelClassificatoreGenerico46() {
		return labelClassificatoreGenerico46;
	}
	public void setLabelClassificatoreGenerico46(String labelClassificatoreGenerico46) {
		this.labelClassificatoreGenerico46 = labelClassificatoreGenerico46;
	}
	public String getLabelClassificatoreGenerico47() {
		return labelClassificatoreGenerico47;
	}
	public void setLabelClassificatoreGenerico47(String labelClassificatoreGenerico47) {
		this.labelClassificatoreGenerico47 = labelClassificatoreGenerico47;
	}
	public String getLabelClassificatoreGenerico48() {
		return labelClassificatoreGenerico48;
	}
	public void setLabelClassificatoreGenerico48(String labelClassificatoreGenerico48) {
		this.labelClassificatoreGenerico48 = labelClassificatoreGenerico48;
	}
	public String getLabelClassificatoreGenerico49() {
		return labelClassificatoreGenerico49;
	}
	public void setLabelClassificatoreGenerico49(String labelClassificatoreGenerico49) {
		this.labelClassificatoreGenerico49 = labelClassificatoreGenerico49;
	}
	public String getLabelClassificatoreGenerico50() {
		return labelClassificatoreGenerico50;
	}
	public void setLabelClassificatoreGenerico50(String labelClassificatoreGenerico50) {
		this.labelClassificatoreGenerico50 = labelClassificatoreGenerico50;
	}
	
	
	
	public boolean isClassificatoreGenerico36Editabile() {
		return classificatoreGenerico36Editabile;
	}
	public void setClassificatoreGenerico36Editabile(boolean classificatoreGenerico36Editabile) {
		this.classificatoreGenerico36Editabile = classificatoreGenerico36Editabile;
	}
	public boolean isClassificatoreGenerico37Editabile() {
		return classificatoreGenerico37Editabile;
	}
	public void setClassificatoreGenerico37Editabile(boolean classificatoreGenerico37Editabile) {
		this.classificatoreGenerico37Editabile = classificatoreGenerico37Editabile;
	}
	public boolean isClassificatoreGenerico38Editabile() {
		return classificatoreGenerico38Editabile;
	}
	public void setClassificatoreGenerico38Editabile(boolean classificatoreGenerico38Editabile) {
		this.classificatoreGenerico38Editabile = classificatoreGenerico38Editabile;
	}
	public boolean isClassificatoreGenerico39Editabile() {
		return classificatoreGenerico39Editabile;
	}
	public void setClassificatoreGenerico39Editabile(boolean classificatoreGenerico39Editabile) {
		this.classificatoreGenerico39Editabile = classificatoreGenerico39Editabile;
	}
	public boolean isClassificatoreGenerico40Editabile() {
		return classificatoreGenerico40Editabile;
	}
	public void setClassificatoreGenerico40Editabile(boolean classificatoreGenerico40Editabile) {
		this.classificatoreGenerico40Editabile = classificatoreGenerico40Editabile;
	}
	public boolean isClassificatoreGenerico41Editabile() {
		return classificatoreGenerico41Editabile;
	}
	public void setClassificatoreGenerico41Editabile(boolean classificatoreGenerico41Editabile) {
		this.classificatoreGenerico41Editabile = classificatoreGenerico41Editabile;
	}
	public boolean isClassificatoreGenerico42Editabile() {
		return classificatoreGenerico42Editabile;
	}
	public void setClassificatoreGenerico42Editabile(boolean classificatoreGenerico42Editabile) {
		this.classificatoreGenerico42Editabile = classificatoreGenerico42Editabile;
	}
	public boolean isClassificatoreGenerico43Editabile() {
		return classificatoreGenerico43Editabile;
	}
	public void setClassificatoreGenerico43Editabile(boolean classificatoreGenerico43Editabile) {
		this.classificatoreGenerico43Editabile = classificatoreGenerico43Editabile;
	}
	public boolean isClassificatoreGenerico44Editabile() {
		return classificatoreGenerico44Editabile;
	}
	public void setClassificatoreGenerico44Editabile(boolean classificatoreGenerico44Editabile) {
		this.classificatoreGenerico44Editabile = classificatoreGenerico44Editabile;
	}
	public boolean isClassificatoreGenerico45Editabile() {
		return classificatoreGenerico45Editabile;
	}
	public void setClassificatoreGenerico45Editabile(boolean classificatoreGenerico45Editabile) {
		this.classificatoreGenerico45Editabile = classificatoreGenerico45Editabile;
	}
	public boolean isClassificatoreGenerico46Editabile() {
		return classificatoreGenerico46Editabile;
	}
	public void setClassificatoreGenerico46Editabile(boolean classificatoreGenerico46Editabile) {
		this.classificatoreGenerico46Editabile = classificatoreGenerico46Editabile;
	}
	public boolean isClassificatoreGenerico47Editabile() {
		return classificatoreGenerico47Editabile;
	}
	public void setClassificatoreGenerico47Editabile(boolean classificatoreGenerico47Editabile) {
		this.classificatoreGenerico47Editabile = classificatoreGenerico47Editabile;
	}
	public boolean isClassificatoreGenerico48Editabile() {
		return classificatoreGenerico48Editabile;
	}
	public void setClassificatoreGenerico48Editabile(boolean classificatoreGenerico48Editabile) {
		this.classificatoreGenerico48Editabile = classificatoreGenerico48Editabile;
	}
	public boolean isClassificatoreGenerico49Editabile() {
		return classificatoreGenerico49Editabile;
	}
	public void setClassificatoreGenerico49Editabile(boolean classificatoreGenerico49Editabile) {
		this.classificatoreGenerico49Editabile = classificatoreGenerico49Editabile;
	}
	public boolean isClassificatoreGenerico50Editabile() {
		return classificatoreGenerico50Editabile;
	}
	public void setClassificatoreGenerico50Editabile(boolean classificatoreGenerico50Editabile) {
		this.classificatoreGenerico50Editabile = classificatoreGenerico50Editabile;
	}
	/**
	 * @return the numeroClassificatoriGenerici
	 */
	public int getNumeroClassificatoriGenerici() {
		return NUMERO_CLASSIFICATORI_GENERICI;
	}
	
	/**
	 * @return the numeroClassificatoriGenerici
	 */
	public int getLastIndexClassificatoriGenerici() {
		return 36 + NUMERO_CLASSIFICATORI_GENERICI;
	}
	
	/**
	 * @return the flagAccertatoPerCassaVisibile
	 */
	public boolean isFlagAccertatoPerCassaVisibile() {
		String codiceTitoloEntrataOK = "1";
		Collection<String> codiceTipologiaTitoloOK = Arrays.asList("1010100", "1010200", "1010300");
		
		return titoloEntrata != null
				&& tipologiaTitolo != null
				&& codiceTitoloEntrataOK.equals(titoloEntrata.getCodice())
				&& codiceTipologiaTitoloOK.contains(tipologiaTitolo.getCodice());
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea una Request per il servizio di {@link RicercaTipoClassificatoreGenerico}.
	 * @param tipoCapitolo il tipo di capitolo
	 * @return la request creata
	 */
	public RicercaTipoClassificatoreGenerico creaRequestRicercaTipoClassificatoreGenerico(String tipoCapitolo) {
		RicercaTipoClassificatoreGenerico req = creaRequest(RicercaTipoClassificatoreGenerico.class);
		
		req.setAnno(getAnnoEsercizioInt());
		req.setTipologieClassificatore(Arrays.asList(
				TipologiaClassificatore.CLASSIFICATORE_36,
				TipologiaClassificatore.CLASSIFICATORE_37,
				TipologiaClassificatore.CLASSIFICATORE_38,
				TipologiaClassificatore.CLASSIFICATORE_39,
				TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41,
				TipologiaClassificatore.CLASSIFICATORE_42,
				TipologiaClassificatore.CLASSIFICATORE_43,
				TipologiaClassificatore.CLASSIFICATORE_44,
				TipologiaClassificatore.CLASSIFICATORE_45,
				TipologiaClassificatore.CLASSIFICATORE_46,
				TipologiaClassificatore.CLASSIFICATORE_47,
				TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49,
				TipologiaClassificatore.CLASSIFICATORE_50));
		req.setTipoElementoBilancio(tipoCapitolo);
		
		return req;
	}
	
	@Override
	public void caricaClassificatoriDaSessione(SessionHandler sessionHandler) {
		// Carico i classificatori comuni
		super.caricaClassificatoriDaSessione(sessionHandler);
		
		titoloEntrata = caricaClassificatoriDaSessione(sessionHandler, titoloEntrata, BilSessionParameter.LISTA_TITOLO_ENTRATA);
		tipologiaTitolo = caricaClassificatoriDaSessione(sessionHandler, tipologiaTitolo, BilSessionParameter.LISTA_TIPOLOGIA_TITOLO);
		categoriaTipologiaTitolo = caricaClassificatoriDaSessione(sessionHandler, categoriaTipologiaTitolo, BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO);
		
		ricorrenteEntrata = caricaClassificatoriDaSessione(sessionHandler, ricorrenteEntrata, BilSessionParameter.LISTA_RICORRENTE_ENTRATA);
		perimetroSanitarioEntrata = caricaClassificatoriDaSessione(sessionHandler, perimetroSanitarioEntrata, BilSessionParameter.LISTA_PERIMETRO_SANITARIO_ENTRATA);
		transazioneUnioneEuropeaEntrata = caricaClassificatoriDaSessione(sessionHandler, transazioneUnioneEuropeaEntrata, BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_ENTRATA);
		
		siopeEntrata = caricaClassificatoriDaSessione(sessionHandler, siopeEntrata, BilSessionParameter.LISTA_SIOPE_SPESA);
		if(siopeEntrata != null && siopeEntrata.getUid() != 0 && (siopeInserito == null || siopeInserito.isEmpty())) {
			siopeInserito = getCodiceEDescrizione(siopeEntrata);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle informazioni relative ai classificatori a partire dalle liste in sessione.
	 * 
	 * @param sessionHandler     l'handler per la sessione
	 * @param classificatore     il classificatore da ricercare
	 * @param nomeClassificatore il nome del classificatore in sessione
	 * 
	 * @return il classificatore valorizzato come da lista in sessione
	 */
	protected SiopeEntrata caricaClassificatoriDaSessione(SessionHandler sessionHandler, SiopeEntrata classificatore, BilSessionParameter nomeClassificatore) {
		List<SiopeEntrata> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
	}
	
	@Override
	protected void caricaClassificatoriGenericiDaSessione(SessionHandler sessionHandler) {
		setClassificatoreGenerico36(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico36(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36));
		setClassificatoreGenerico37(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico37(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37));
		setClassificatoreGenerico38(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico38(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38));
		setClassificatoreGenerico39(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico39(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39));
		setClassificatoreGenerico40(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico40(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40));
		setClassificatoreGenerico41(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico41(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41));
		setClassificatoreGenerico42(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico42(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42));
		setClassificatoreGenerico43(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico43(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43));
		setClassificatoreGenerico44(caricaClassificatoriDaSessione(sessionHandler,  getClassificatoreGenerico44(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44));
		setClassificatoreGenerico45(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico45(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45));
		setClassificatoreGenerico46(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico46(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46));
		setClassificatoreGenerico47(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico47(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47));
		setClassificatoreGenerico48(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico48(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48));
		setClassificatoreGenerico49(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico49(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49));
		setClassificatoreGenerico50(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico50(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50));
	}
	
	@Override
	public void valutaModificabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaClassificatori(response, isMassivo);
		
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		long stessoNumCap = response.getStessoNumCap() != null ? response.getStessoNumCap().longValue() : 0L;
		boolean unico =false;
		if (isGestioneUEB()){
		 unico = stessoNumCapArt <= 1L;
		}else{
			unico = stessoNumCap <= 1L;
		}
		
		
		// Controllo i classificatori definiti per il capitolo di entrata
		titoloEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TITOLO_ENTRATA);
		tipologiaTitoloEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TIPOLOGIA);
		categoriaTipologiaTitoloEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.CATEGORIA);
		
		siopeEntrataEditabile = isEditabile(unico, isMassivo, response,TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I,TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III);
		ricorrenteEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.RICORRENTE_ENTRATA);
		perimetroSanitarioEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA);
		transazioneUnioneEuropeaEntrataEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA);
	}
	
	@Override
	protected void isClassificatoriGenericiEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response) {
		setClassificatoreGenerico36Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_36));
		setClassificatoreGenerico37Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_37));
		setClassificatoreGenerico38Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_38));
		setClassificatoreGenerico39Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_39));
		setClassificatoreGenerico40Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_40));
		setClassificatoreGenerico41Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_41));
		setClassificatoreGenerico42Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_42));
		setClassificatoreGenerico43Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_43));
		setClassificatoreGenerico44Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_44));
		setClassificatoreGenerico45Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_45));
		setClassificatoreGenerico46Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_46));
		setClassificatoreGenerico47Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_47));
		setClassificatoreGenerico48Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_48));
		setClassificatoreGenerico49Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_49));
		setClassificatoreGenerico50Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_50));
	}
	
	/**
	 * Reimposta i dati disabilitati causa non editabilit&agrave; nel model.
	 * 
	 * @param classificatoreAggiornamento i classificatori originali in sessione
	 */
	public void setParametriDisabilitati(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		super.setParametriDisabilitati(classificatoreAggiornamento);
		titoloEntrata = impostaIlDato(titoloEntrataEditabile, titoloEntrata, classificatoreAggiornamento.getTitoloEntrata());
		tipologiaTitolo = impostaIlDato(tipologiaTitoloEditabile, tipologiaTitolo, classificatoreAggiornamento.getTipologiaTitolo());
		categoriaTipologiaTitolo = impostaIlDato(categoriaTipologiaTitoloEditabile, categoriaTipologiaTitolo, classificatoreAggiornamento.getCategoriaTipologiaTitolo());
		
		siopeEntrata = impostaIlDato(siopeEntrataEditabile, siopeEntrata, classificatoreAggiornamento.getSiopeEntrata());
		ricorrenteEntrata = impostaIlDato(ricorrenteEntrataEditabile, ricorrenteEntrata, classificatoreAggiornamento.getRicorrenteEntrata());
		perimetroSanitarioEntrata = impostaIlDato(perimetroSanitarioEntrataEditabile, perimetroSanitarioEntrata, classificatoreAggiornamento.getPerimetroSanitarioEntrata());
		transazioneUnioneEuropeaEntrata = impostaIlDato(transazioneUnioneEuropeaEntrataEditabile, transazioneUnioneEuropeaEntrata, classificatoreAggiornamento.getTransazioneUnioneEuropeaEntrata());
	}
	
	@Override
	protected void valorizzaStringheUtilita() {
		super.valorizzaStringheUtilita();
		if(siopeEntrata != null) {
			siopeInserito = getCodiceEDescrizione(siopeEntrata);
		} else {
			// Valore di default
			siopeInserito = "Nessun SIOPE selezionato";
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per impostare i classificatori generici a partire da una lista.
	 * 
	 * @param listaClassificatoriGenerici la lista di classificatori generici da cui popolare il model
	 */
	protected void impostaClassificatoriGenericiDaLista(List<ClassificatoreGenerico> listaClassificatoriGenerici) {
		setClassificatoreGenerico36(null);
		setClassificatoreGenerico37(null);
		setClassificatoreGenerico38(null);
		setClassificatoreGenerico39(null);
		setClassificatoreGenerico40(null);
		setClassificatoreGenerico41(null);
		setClassificatoreGenerico42(null);
		setClassificatoreGenerico43(null);
		setClassificatoreGenerico44(null);
		setClassificatoreGenerico45(null);
		setClassificatoreGenerico46(null);
		setClassificatoreGenerico47(null);
		setClassificatoreGenerico48(null);
		setClassificatoreGenerico49(null);
		setClassificatoreGenerico50(null);
		
		for(ClassificatoreGenerico classificatoreGenerico : listaClassificatoriGenerici) {
			TipoClassificatore tipoClassificatore = classificatoreGenerico.getTipoClassificatore();
			
			if(tipoClassificatore == null || tipoClassificatore.getCodice() == null) {
				// Il tipo di classificatore per il Classificatore Generico non e' valido
				continue;
			}
			
			if(BilConstants.CODICE_CLASSIFICATORE36.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico36(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE37.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico37(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE38.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico38(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE39.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico39(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE40.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico40(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE41.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico41(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE42.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico42(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE43.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico43(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE44.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico44(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE45.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico45(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE46.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico46(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE47.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico47(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE48.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico48(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE49.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico49(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE50.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico50(classificatoreGenerico);
			}
		}
	}
	
	/**
	 * Imposta i labels per i classificatori generici a partire dalle liste caricate in sessione.
	 * 
	 * @param sessionHandler l'handler della sessione
	 */
	@SuppressWarnings("unchecked")
	protected void impostaLabelDaSessione(SessionHandler sessionHandler) {
		setLabelClassificatoreGenerico36(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_36)));
		setLabelClassificatoreGenerico37(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_37)));
		setLabelClassificatoreGenerico38(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_38)));
		setLabelClassificatoreGenerico39(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_39)));
		setLabelClassificatoreGenerico40(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_40)));
		setLabelClassificatoreGenerico41(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_41)));
		setLabelClassificatoreGenerico42(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_42)));
		setLabelClassificatoreGenerico43(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_43)));
		setLabelClassificatoreGenerico44(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_44)));
		setLabelClassificatoreGenerico45(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_45)));
		setLabelClassificatoreGenerico46(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_46)));
		setLabelClassificatoreGenerico47(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_47)));
		setLabelClassificatoreGenerico48(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_48)));
		setLabelClassificatoreGenerico49(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_49)));
		setLabelClassificatoreGenerico50(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_50)));
	}
	protected List<ClassificatoreGenerico> getListaClassificatoriGenerici() {
		List<ClassificatoreGenerico> lista = new ArrayList<ClassificatoreGenerico>();
		
		addClassificatoreGenericoALista(lista, classificatoreGenerico36);
		addClassificatoreGenericoALista(lista, classificatoreGenerico37);
		addClassificatoreGenericoALista(lista, classificatoreGenerico38);
		addClassificatoreGenericoALista(lista, classificatoreGenerico39);
		addClassificatoreGenericoALista(lista, classificatoreGenerico40);
		addClassificatoreGenericoALista(lista, classificatoreGenerico41);
		addClassificatoreGenericoALista(lista, classificatoreGenerico42);
		addClassificatoreGenericoALista(lista, classificatoreGenerico43);
		addClassificatoreGenericoALista(lista, classificatoreGenerico44);
		addClassificatoreGenericoALista(lista, classificatoreGenerico45);
		addClassificatoreGenericoALista(lista, classificatoreGenerico46);
		addClassificatoreGenericoALista(lista, classificatoreGenerico47);
		addClassificatoreGenericoALista(lista, classificatoreGenerico48);
		addClassificatoreGenericoALista(lista, classificatoreGenerico49);
		addClassificatoreGenericoALista(lista, classificatoreGenerico50);
		
		return lista;
	}
	
	/**
	 * Costruisce la lista dei Classificatori Generici a partire dagli importi del Model.
	 * 
	 * @param classificatoreAggiornamento il wrapper per l'aggiornamento
	 * 
	 * @return la lista creata
	 */
	protected List<ClassificatoreGenerico> getListaClassificatoriGenericiAggiornamento(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		List<ClassificatoreGenerico> lista = new ArrayList<ClassificatoreGenerico>();
		
		addClassificatoreGenericoALista(lista, classificatoreGenerico36,  classificatoreAggiornamento.getClassificatoreGenerico36(), classificatoreGenerico36Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico37,  classificatoreAggiornamento.getClassificatoreGenerico37(), classificatoreGenerico37Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico38,  classificatoreAggiornamento.getClassificatoreGenerico38(), classificatoreGenerico38Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico39,  classificatoreAggiornamento.getClassificatoreGenerico39(), classificatoreGenerico39Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico40,  classificatoreAggiornamento.getClassificatoreGenerico40(), classificatoreGenerico40Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico41,  classificatoreAggiornamento.getClassificatoreGenerico41(), classificatoreGenerico41Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico42,  classificatoreAggiornamento.getClassificatoreGenerico42(), classificatoreGenerico42Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico43,  classificatoreAggiornamento.getClassificatoreGenerico43(), classificatoreGenerico43Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico44,  classificatoreAggiornamento.getClassificatoreGenerico44(), classificatoreGenerico44Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico45,  classificatoreAggiornamento.getClassificatoreGenerico45(), classificatoreGenerico45Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico46,  classificatoreAggiornamento.getClassificatoreGenerico46(), classificatoreGenerico46Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico47,  classificatoreAggiornamento.getClassificatoreGenerico47(), classificatoreGenerico47Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico48,  classificatoreAggiornamento.getClassificatoreGenerico48(), classificatoreGenerico48Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico49,  classificatoreAggiornamento.getClassificatoreGenerico49(), classificatoreGenerico49Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico50,  classificatoreAggiornamento.getClassificatoreGenerico50(), classificatoreGenerico50Editabile);
		
		return lista;
	}
	
}
