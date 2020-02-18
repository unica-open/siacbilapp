/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaClassificatoriERestituisciSeSingolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Superclasse per il model del Capitolo.
 * <br>
 * Si occupa di definire i campi e i metodi comuni per le actions del Capitolo, con attenzione a:
 * <ul>
 *   <li>aggiornamento</li>
 *   <li>inserimento</li>
 *   <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 20/08/2013
 *
 */
public abstract class CapitoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8402159471354010337L;
	
	/* Dati principali */
	private ElementoPianoDeiConti            elementoPianoDeiConti;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	/* Altri dati */
	private TipoFinanziamento tipoFinanziamento;
	private TipoFondo         tipoFondo;
	private TipoAtto          tipoAtto;
	
	private ClassificatoreGenerico classificatoreGenerico1;
	private ClassificatoreGenerico classificatoreGenerico2;
	private ClassificatoreGenerico classificatoreGenerico3;
	private ClassificatoreGenerico classificatoreGenerico4;
	private ClassificatoreGenerico classificatoreGenerico5;
	private ClassificatoreGenerico classificatoreGenerico6;
	private ClassificatoreGenerico classificatoreGenerico7;
	private ClassificatoreGenerico classificatoreGenerico8;
	private ClassificatoreGenerico classificatoreGenerico9;
	private ClassificatoreGenerico classificatoreGenerico10;
	private ClassificatoreGenerico classificatoreGenerico11;
	private ClassificatoreGenerico classificatoreGenerico12;
	private ClassificatoreGenerico classificatoreGenerico13;
	private ClassificatoreGenerico classificatoreGenerico14;
	private ClassificatoreGenerico classificatoreGenerico15;
	
	/* Liste */
	private List<TipoFinanziamento>      listaTipoFinanziamento        = new ArrayList<TipoFinanziamento>();
	private List<TipoFondo>              listaTipoFondo                = new ArrayList<TipoFondo>();
	private List<TipoAtto>               listaTipoAtto                 = new ArrayList<TipoAtto>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico1  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico2  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico3  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico4  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico5  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico6  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico7  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico8  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico9  = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico10 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico11 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico12 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico13 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico14 = new ArrayList<ClassificatoreGenerico>();
	private List<ClassificatoreGenerico> listaClassificatoreGenerico15 = new ArrayList<ClassificatoreGenerico>();
	
	private List<CategoriaCapitolo>      listaCategoriaCapitolo        = new ArrayList<CategoriaCapitolo>();
	
	/* Liste derivate */
	private List<ElementoPianoDeiConti>            listaElementoPianoDeiConti            = new ArrayList<ElementoPianoDeiConti>();
	private List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = new ArrayList<StrutturaAmministrativoContabile>();
	
	/* Labels */
	private String labelClassificatoreGenerico1;
	private String labelClassificatoreGenerico2;
	private String labelClassificatoreGenerico3;
	private String labelClassificatoreGenerico4;
	private String labelClassificatoreGenerico5;
	private String labelClassificatoreGenerico6;
	private String labelClassificatoreGenerico7;
	private String labelClassificatoreGenerico8;
	private String labelClassificatoreGenerico9;
	private String labelClassificatoreGenerico10;
	private String labelClassificatoreGenerico11;
	private String labelClassificatoreGenerico12;
	private String labelClassificatoreGenerico13;
	private String labelClassificatoreGenerico14;
	private String labelClassificatoreGenerico15;
	
	/* Stringhe di utilita' per il post-aggiornamento */
	private String pdcFinanziario;
	private String strutturaAmministrativoResponsabile;
	
	/* Booleani rappresentanti i campi, per la editabilita' */
	private boolean elementoPianoDeiContiEditabile;
	private boolean strutturaAmministrativoContabileEditabile;
	private boolean tipoFinanziamentoEditabile;
	private boolean tipoFondoEditabile;
	private boolean classificatoreGenerico1Editabile;
	private boolean classificatoreGenerico2Editabile;
	private boolean classificatoreGenerico3Editabile;
	private boolean classificatoreGenerico4Editabile;
	private boolean classificatoreGenerico5Editabile;
	private boolean classificatoreGenerico6Editabile;
	private boolean classificatoreGenerico7Editabile;
	private boolean classificatoreGenerico8Editabile;
	private boolean classificatoreGenerico9Editabile;
	private boolean classificatoreGenerico10Editabile;
	private boolean classificatoreGenerico11Editabile;
	private boolean classificatoreGenerico12Editabile;
	private boolean classificatoreGenerico13Editabile;
	private boolean classificatoreGenerico14Editabile;
	private boolean classificatoreGenerico15Editabile;
	
	private boolean categoriaCapitoloEditabile;
	
	private boolean descrizioneEditabile;
	private boolean descrizioneArticoloEditabile;
	private boolean flagRilevanteIvaEditabile;
	private boolean noteEditabile;
	private boolean flagImpegnabileEditabile;
	
	/* Per il ritorno all'aggiornamento */
	private boolean daAggiornamento = false;
	private Integer annoImporti;
	
	/* dati aggiuntivi per l'inserimento del capitolo da variazione*/
	private int uidVariazioneCapitolo;
	private Boolean daVariazione;
	/* Getter e setter */
	
	/**
	 * @return the elementoPianoDeiConti
	 */
	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}

	/**
	 * @param elementoPianoDeiConti the elementoPianoDeiConti to set
	 */
	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the tipoFinanziamento
	 */
	public TipoFinanziamento getTipoFinanziamento() {
		return tipoFinanziamento;
	}

	/**
	 * @param tipoFinanziamento the tipoFinanziamento to set
	 */
	public void setTipoFinanziamento(TipoFinanziamento tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}

	/**
	 * @return the tipoFondo
	 */
	public TipoFondo getTipoFondo() {
		return tipoFondo;
	}

	/**
	 * @param tipoFondo the tipoFondo to set
	 */
	public void setTipoFondo(TipoFondo tipoFondo) {
		this.tipoFondo = tipoFondo;
	}

	/**
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}

	/**
	 * @return the classificatoreGenerico1
	 */
	public ClassificatoreGenerico getClassificatoreGenerico1() {
		return classificatoreGenerico1;
	}

	/**
	 * @param classificatoreGenerico1 the classificatoreGenerico1 to set
	 */
	public void setClassificatoreGenerico1(ClassificatoreGenerico classificatoreGenerico1) {
		this.classificatoreGenerico1 = classificatoreGenerico1;
	}

	/**
	 * @return the classificatoreGenerico2
	 */
	public ClassificatoreGenerico getClassificatoreGenerico2() {
		return classificatoreGenerico2;
	}

	/**
	 * @param classificatoreGenerico2 the classificatoreGenerico2 to set
	 */
	public void setClassificatoreGenerico2(ClassificatoreGenerico classificatoreGenerico2) {
		this.classificatoreGenerico2 = classificatoreGenerico2;
	}

	/**
	 * @return the classificatoreGenerico3
	 */
	public ClassificatoreGenerico getClassificatoreGenerico3() {
		return classificatoreGenerico3;
	}

	/**
	 * @param classificatoreGenerico3 the classificatoreGenerico3 to set
	 */
	public void setClassificatoreGenerico3(ClassificatoreGenerico classificatoreGenerico3) {
		this.classificatoreGenerico3 = classificatoreGenerico3;
	}

	/**
	 * @return the classificatoreGenerico4
	 */
	public ClassificatoreGenerico getClassificatoreGenerico4() {
		return classificatoreGenerico4;
	}

	/**
	 * @param classificatoreGenerico4 the classificatoreGenerico4 to set
	 */
	public void setClassificatoreGenerico4(ClassificatoreGenerico classificatoreGenerico4) {
		this.classificatoreGenerico4 = classificatoreGenerico4;
	}

	/**
	 * @return the classificatoreGenerico5
	 */
	public ClassificatoreGenerico getClassificatoreGenerico5() {
		return classificatoreGenerico5;
	}

	/**
	 * @param classificatoreGenerico5 the classificatoreGenerico5 to set
	 */
	public void setClassificatoreGenerico5(ClassificatoreGenerico classificatoreGenerico5) {
		this.classificatoreGenerico5 = classificatoreGenerico5;
	}

	/**
	 * @return the classificatoreGenerico6
	 */
	public ClassificatoreGenerico getClassificatoreGenerico6() {
		return classificatoreGenerico6;
	}

	/**
	 * @param classificatoreGenerico6 the classificatoreGenerico6 to set
	 */
	public void setClassificatoreGenerico6(ClassificatoreGenerico classificatoreGenerico6) {
		this.classificatoreGenerico6 = classificatoreGenerico6;
	}

	/**
	 * @return the classificatoreGenerico7
	 */
	public ClassificatoreGenerico getClassificatoreGenerico7() {
		return classificatoreGenerico7;
	}

	/**
	 * @param classificatoreGenerico7 the classificatoreGenerico7 to set
	 */
	public void setClassificatoreGenerico7(ClassificatoreGenerico classificatoreGenerico7) {
		this.classificatoreGenerico7 = classificatoreGenerico7;
	}

	/**
	 * @return the classificatoreGenerico8
	 */
	public ClassificatoreGenerico getClassificatoreGenerico8() {
		return classificatoreGenerico8;
	}

	/**
	 * @param classificatoreGenerico8 the classificatoreGenerico8 to set
	 */
	public void setClassificatoreGenerico8(ClassificatoreGenerico classificatoreGenerico8) {
		this.classificatoreGenerico8 = classificatoreGenerico8;
	}

	/**
	 * @return the classificatoreGenerico9
	 */
	public ClassificatoreGenerico getClassificatoreGenerico9() {
		return classificatoreGenerico9;
	}

	/**
	 * @param classificatoreGenerico9 the classificatoreGenerico9 to set
	 */
	public void setClassificatoreGenerico9(ClassificatoreGenerico classificatoreGenerico9) {
		this.classificatoreGenerico9 = classificatoreGenerico9;
	}

	/**
	 * @return the classificatoreGenerico10
	 */
	public ClassificatoreGenerico getClassificatoreGenerico10() {
		return classificatoreGenerico10;
	}

	/**
	 * @param classificatoreGenerico10 the classificatoreGenerico10 to set
	 */
	public void setClassificatoreGenerico10(ClassificatoreGenerico classificatoreGenerico10) {
		this.classificatoreGenerico10 = classificatoreGenerico10;
	}

	/**
	 * @return the classificatoreGenerico11
	 */
	public ClassificatoreGenerico getClassificatoreGenerico11() {
		return classificatoreGenerico11;
	}

	/**
	 * @param classificatoreGenerico11 the classificatoreGenerico11 to set
	 */
	public void setClassificatoreGenerico11(ClassificatoreGenerico classificatoreGenerico11) {
		this.classificatoreGenerico11 = classificatoreGenerico11;
	}

	/**
	 * @return the classificatoreGenerico12
	 */
	public ClassificatoreGenerico getClassificatoreGenerico12() {
		return classificatoreGenerico12;
	}

	/**
	 * @param classificatoreGenerico12 the classificatoreGenerico12 to set
	 */
	public void setClassificatoreGenerico12(ClassificatoreGenerico classificatoreGenerico12) {
		this.classificatoreGenerico12 = classificatoreGenerico12;
	}

	/**
	 * @return the classificatoreGenerico13
	 */
	public ClassificatoreGenerico getClassificatoreGenerico13() {
		return classificatoreGenerico13;
	}

	/**
	 * @param classificatoreGenerico13 the classificatoreGenerico13 to set
	 */
	public void setClassificatoreGenerico13(ClassificatoreGenerico classificatoreGenerico13) {
		this.classificatoreGenerico13 = classificatoreGenerico13;
	}

	/**
	 * @return the classificatoreGenerico14
	 */
	public ClassificatoreGenerico getClassificatoreGenerico14() {
		return classificatoreGenerico14;
	}

	/**
	 * @param classificatoreGenerico14 the classificatoreGenerico14 to set
	 */
	public void setClassificatoreGenerico14(ClassificatoreGenerico classificatoreGenerico14) {
		this.classificatoreGenerico14 = classificatoreGenerico14;
	}

	/**
	 * @return the classificatoreGenerico15
	 */
	public ClassificatoreGenerico getClassificatoreGenerico15() {
		return classificatoreGenerico15;
	}

	/**
	 * @param classificatoreGenerico15 the classificatoreGenerico15 to set
	 */
	public void setClassificatoreGenerico15(ClassificatoreGenerico classificatoreGenerico15) {
		this.classificatoreGenerico15 = classificatoreGenerico15;
	}

	/**
	 * @return the listaTipoFinanziamento
	 */
	public List<TipoFinanziamento> getListaTipoFinanziamento() {
		return listaTipoFinanziamento;
	}

	/**
	 * @param listaTipoFinanziamento the listaTipoFinanziamento to set
	 */
	public void setListaTipoFinanziamento(List<TipoFinanziamento> listaTipoFinanziamento) {
		this.listaTipoFinanziamento = listaTipoFinanziamento;
	}

	/**
	 * @return the listaTipoFondo
	 */
	public List<TipoFondo> getListaTipoFondo() {
		return listaTipoFondo;
	}

	/**
	 * @param listaTipoFondo the listaTipoFondo to set
	 */
	public void setListaTipoFondo(List<TipoFondo> listaTipoFondo) {
		this.listaTipoFondo = listaTipoFondo;
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}

	/**
	 * @return the listaClassificatoreGenerico1
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico1() {
		return listaClassificatoreGenerico1;
	}

	/**
	 * @param listaClassificatoreGenerico1 the listaClassificatoreGenerico1 to set
	 */
	public void setListaClassificatoreGenerico1(List<ClassificatoreGenerico> listaClassificatoreGenerico1) {
		this.listaClassificatoreGenerico1 = listaClassificatoreGenerico1;
	}

	/**
	 * @return the listaClassificatoreGenerico2
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico2() {
		return listaClassificatoreGenerico2;
	}

	/**
	 * @param listaClassificatoreGenerico2 the listaClassificatoreGenerico2 to set
	 */
	public void setListaClassificatoreGenerico2(List<ClassificatoreGenerico> listaClassificatoreGenerico2) {
		this.listaClassificatoreGenerico2 = listaClassificatoreGenerico2;
	}

	/**
	 * @return the listaClassificatoreGenerico3
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico3() {
		return listaClassificatoreGenerico3;
	}

	/**
	 * @param listaClassificatoreGenerico3 the listaClassificatoreGenerico3 to set
	 */
	public void setListaClassificatoreGenerico3(List<ClassificatoreGenerico> listaClassificatoreGenerico3) {
		this.listaClassificatoreGenerico3 = listaClassificatoreGenerico3;
	}

	/**
	 * @return the listaClassificatoreGenerico4
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico4() {
		return listaClassificatoreGenerico4;
	}

	/**
	 * @param listaClassificatoreGenerico4 the listaClassificatoreGenerico4 to set
	 */
	public void setListaClassificatoreGenerico4(List<ClassificatoreGenerico> listaClassificatoreGenerico4) {
		this.listaClassificatoreGenerico4 = listaClassificatoreGenerico4;
	}

	/**
	 * @return the listaClassificatoreGenerico5
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico5() {
		return listaClassificatoreGenerico5;
	}

	/**
	 * @param listaClassificatoreGenerico5 the listaClassificatoreGenerico5 to set
	 */
	public void setListaClassificatoreGenerico5(List<ClassificatoreGenerico> listaClassificatoreGenerico5) {
		this.listaClassificatoreGenerico5 = listaClassificatoreGenerico5;
	}

	/**
	 * @return the listaClassificatoreGenerico6
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico6() {
		return listaClassificatoreGenerico6;
	}

	/**
	 * @param listaClassificatoreGenerico6 the listaClassificatoreGenerico6 to set
	 */
	public void setListaClassificatoreGenerico6(List<ClassificatoreGenerico> listaClassificatoreGenerico6) {
		this.listaClassificatoreGenerico6 = listaClassificatoreGenerico6;
	}

	/**
	 * @return the listaClassificatoreGenerico7
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico7() {
		return listaClassificatoreGenerico7;
	}

	/**
	 * @param listaClassificatoreGenerico7 the listaClassificatoreGenerico7 to set
	 */
	public void setListaClassificatoreGenerico7(List<ClassificatoreGenerico> listaClassificatoreGenerico7) {
		this.listaClassificatoreGenerico7 = listaClassificatoreGenerico7;
	}

	/**
	 * @return the listaClassificatoreGenerico8
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico8() {
		return listaClassificatoreGenerico8;
	}

	/**
	 * @param listaClassificatoreGenerico8 the listaClassificatoreGenerico8 to set
	 */
	public void setListaClassificatoreGenerico8(List<ClassificatoreGenerico> listaClassificatoreGenerico8) {
		this.listaClassificatoreGenerico8 = listaClassificatoreGenerico8;
	}

	/**
	 * @return the listaClassificatoreGenerico9
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico9() {
		return listaClassificatoreGenerico9;
	}

	/**
	 * @param listaClassificatoreGenerico9 the listaClassificatoreGenerico9 to set
	 */
	public void setListaClassificatoreGenerico9(List<ClassificatoreGenerico> listaClassificatoreGenerico9) {
		this.listaClassificatoreGenerico9 = listaClassificatoreGenerico9;
	}

	/**
	 * @return the listaClassificatoreGenerico10
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico10() {
		return listaClassificatoreGenerico10;
	}

	/**
	 * @param listaClassificatoreGenerico10 the listaClassificatoreGenerico10 to set
	 */
	public void setListaClassificatoreGenerico10(List<ClassificatoreGenerico> listaClassificatoreGenerico10) {
		this.listaClassificatoreGenerico10 = listaClassificatoreGenerico10;
	}

	/**
	 * @return the listaClassificatoreGenerico11
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico11() {
		return listaClassificatoreGenerico11;
	}

	/**
	 * @param listaClassificatoreGenerico11 the listaClassificatoreGenerico11 to set
	 */
	public void setListaClassificatoreGenerico11(List<ClassificatoreGenerico> listaClassificatoreGenerico11) {
		this.listaClassificatoreGenerico11 = listaClassificatoreGenerico11;
	}

	/**
	 * @return the listaClassificatoreGenerico12
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico12() {
		return listaClassificatoreGenerico12;
	}

	/**
	 * @param listaClassificatoreGenerico12 the listaClassificatoreGenerico12 to set
	 */
	public void setListaClassificatoreGenerico12(List<ClassificatoreGenerico> listaClassificatoreGenerico12) {
		this.listaClassificatoreGenerico12 = listaClassificatoreGenerico12;
	}

	/**
	 * @return the listaClassificatoreGenerico13
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico13() {
		return listaClassificatoreGenerico13;
	}

	/**
	 * @param listaClassificatoreGenerico13 the listaClassificatoreGenerico13 to set
	 */
	public void setListaClassificatoreGenerico13(List<ClassificatoreGenerico> listaClassificatoreGenerico13) {
		this.listaClassificatoreGenerico13 = listaClassificatoreGenerico13;
	}

	/**
	 * @return the listaClassificatoreGenerico14
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico14() {
		return listaClassificatoreGenerico14;
	}

	/**
	 * @param listaClassificatoreGenerico14 the listaClassificatoreGenerico14 to set
	 */
	public void setListaClassificatoreGenerico14(List<ClassificatoreGenerico> listaClassificatoreGenerico14) {
		this.listaClassificatoreGenerico14 = listaClassificatoreGenerico14;
	}

	/**
	 * @return the listaClassificatoreGenerico15
	 */
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico15() {
		return listaClassificatoreGenerico15;
	}

	/**
	 * @param listaClassificatoreGenerico15 the listaClassificatoreGenerico15 to set
	 */
	public void setListaClassificatoreGenerico15(List<ClassificatoreGenerico> listaClassificatoreGenerico15) {
		this.listaClassificatoreGenerico15 = listaClassificatoreGenerico15;
	}

	/**
	 * @return the listaCategoriaCapitolo
	 */
	public List<CategoriaCapitolo> getListaCategoriaCapitolo() {
		return listaCategoriaCapitolo;
	}

	/**
	 * @param listaCategoriaCapitolo the listaCategoriaCapitolo to set
	 */
	public void setListaCategoriaCapitolo(List<CategoriaCapitolo> listaCategoriaCapitolo) {
		this.listaCategoriaCapitolo = listaCategoriaCapitolo;
	}

	/**
	 * @return the listaElementoPianoDeiConti
	 */
	public List<ElementoPianoDeiConti> getListaElementoPianoDeiConti() {
		return listaElementoPianoDeiConti;
	}

	/**
	 * @param listaElementoPianoDeiConti the listaElementoPianoDeiConti to set
	 */
	public void setListaElementoPianoDeiConti(List<ElementoPianoDeiConti> listaElementoPianoDeiConti) {
		this.listaElementoPianoDeiConti = listaElementoPianoDeiConti;
	}

	/**
	 * @return the listaStrutturaAmministrativoContabile
	 */
	public List<StrutturaAmministrativoContabile> getListaStrutturaAmministrativoContabile() {
		return listaStrutturaAmministrativoContabile;
	}

	/**
	 * @param listaStrutturaAmministrativoContabile the listaStrutturaAmministrativoContabile to set
	 */
	public void setListaStrutturaAmministrativoContabile(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		this.listaStrutturaAmministrativoContabile = listaStrutturaAmministrativoContabile;
	}

	/**
	 * @return the labelClassificatoreGenerico1
	 */
	public String getLabelClassificatoreGenerico1() {
		return labelClassificatoreGenerico1;
	}

	/**
	 * @param labelClassificatoreGenerico1 the labelClassificatoreGenerico1 to set
	 */
	public void setLabelClassificatoreGenerico1(String labelClassificatoreGenerico1) {
		this.labelClassificatoreGenerico1 = labelClassificatoreGenerico1;
	}

	/**
	 * @return the labelClassificatoreGenerico2
	 */
	public String getLabelClassificatoreGenerico2() {
		return labelClassificatoreGenerico2;
	}

	/**
	 * @param labelClassificatoreGenerico2 the labelClassificatoreGenerico2 to set
	 */
	public void setLabelClassificatoreGenerico2(String labelClassificatoreGenerico2) {
		this.labelClassificatoreGenerico2 = labelClassificatoreGenerico2;
	}

	/**
	 * @return the labelClassificatoreGenerico3
	 */
	public String getLabelClassificatoreGenerico3() {
		return labelClassificatoreGenerico3;
	}

	/**
	 * @param labelClassificatoreGenerico3 the labelClassificatoreGenerico3 to set
	 */
	public void setLabelClassificatoreGenerico3(String labelClassificatoreGenerico3) {
		this.labelClassificatoreGenerico3 = labelClassificatoreGenerico3;
	}

	/**
	 * @return the labelClassificatoreGenerico4
	 */
	public String getLabelClassificatoreGenerico4() {
		return labelClassificatoreGenerico4;
	}

	/**
	 * @param labelClassificatoreGenerico4 the labelClassificatoreGenerico4 to set
	 */
	public void setLabelClassificatoreGenerico4(String labelClassificatoreGenerico4) {
		this.labelClassificatoreGenerico4 = labelClassificatoreGenerico4;
	}

	/**
	 * @return the labelClassificatoreGenerico5
	 */
	public String getLabelClassificatoreGenerico5() {
		return labelClassificatoreGenerico5;
	}

	/**
	 * @param labelClassificatoreGenerico5 the labelClassificatoreGenerico5 to set
	 */
	public void setLabelClassificatoreGenerico5(String labelClassificatoreGenerico5) {
		this.labelClassificatoreGenerico5 = labelClassificatoreGenerico5;
	}

	/**
	 * @return the labelClassificatoreGenerico6
	 */
	public String getLabelClassificatoreGenerico6() {
		return labelClassificatoreGenerico6;
	}

	/**
	 * @param labelClassificatoreGenerico6 the labelClassificatoreGenerico6 to set
	 */
	public void setLabelClassificatoreGenerico6(String labelClassificatoreGenerico6) {
		this.labelClassificatoreGenerico6 = labelClassificatoreGenerico6;
	}

	/**
	 * @return the labelClassificatoreGenerico7
	 */
	public String getLabelClassificatoreGenerico7() {
		return labelClassificatoreGenerico7;
	}

	/**
	 * @param labelClassificatoreGenerico7 the labelClassificatoreGenerico7 to set
	 */
	public void setLabelClassificatoreGenerico7(String labelClassificatoreGenerico7) {
		this.labelClassificatoreGenerico7 = labelClassificatoreGenerico7;
	}

	/**
	 * @return the labelClassificatoreGenerico8
	 */
	public String getLabelClassificatoreGenerico8() {
		return labelClassificatoreGenerico8;
	}

	/**
	 * @param labelClassificatoreGenerico8 the labelClassificatoreGenerico8 to set
	 */
	public void setLabelClassificatoreGenerico8(String labelClassificatoreGenerico8) {
		this.labelClassificatoreGenerico8 = labelClassificatoreGenerico8;
	}

	/**
	 * @return the labelClassificatoreGenerico9
	 */
	public String getLabelClassificatoreGenerico9() {
		return labelClassificatoreGenerico9;
	}

	/**
	 * @param labelClassificatoreGenerico9 the labelClassificatoreGenerico9 to set
	 */
	public void setLabelClassificatoreGenerico9(String labelClassificatoreGenerico9) {
		this.labelClassificatoreGenerico9 = labelClassificatoreGenerico9;
	}

	/**
	 * @return the labelClassificatoreGenerico10
	 */
	public String getLabelClassificatoreGenerico10() {
		return labelClassificatoreGenerico10;
	}

	/**
	 * @param labelClassificatoreGenerico10 the labelClassificatoreGenerico10 to set
	 */
	public void setLabelClassificatoreGenerico10(String labelClassificatoreGenerico10) {
		this.labelClassificatoreGenerico10 = labelClassificatoreGenerico10;
	}

	/**
	 * @return the labelClassificatoreGenerico11
	 */
	public String getLabelClassificatoreGenerico11() {
		return labelClassificatoreGenerico11;
	}

	/**
	 * @param labelClassificatoreGenerico11 the labelClassificatoreGenerico11 to set
	 */
	public void setLabelClassificatoreGenerico11(String labelClassificatoreGenerico11) {
		this.labelClassificatoreGenerico11 = labelClassificatoreGenerico11;
	}

	/**
	 * @return the labelClassificatoreGenerico12
	 */
	public String getLabelClassificatoreGenerico12() {
		return labelClassificatoreGenerico12;
	}

	/**
	 * @param labelClassificatoreGenerico12 the labelClassificatoreGenerico12 to set
	 */
	public void setLabelClassificatoreGenerico12(String labelClassificatoreGenerico12) {
		this.labelClassificatoreGenerico12 = labelClassificatoreGenerico12;
	}

	/**
	 * @return the labelClassificatoreGenerico13
	 */
	public String getLabelClassificatoreGenerico13() {
		return labelClassificatoreGenerico13;
	}

	/**
	 * @param labelClassificatoreGenerico13 the labelClassificatoreGenerico13 to set
	 */
	public void setLabelClassificatoreGenerico13(String labelClassificatoreGenerico13) {
		this.labelClassificatoreGenerico13 = labelClassificatoreGenerico13;
	}

	/**
	 * @return the labelClassificatoreGenerico14
	 */
	public String getLabelClassificatoreGenerico14() {
		return labelClassificatoreGenerico14;
	}

	/**
	 * @param labelClassificatoreGenerico14 the labelClassificatoreGenerico14 to set
	 */
	public void setLabelClassificatoreGenerico14(String labelClassificatoreGenerico14) {
		this.labelClassificatoreGenerico14 = labelClassificatoreGenerico14;
	}

	/**
	 * @return the labelClassificatoreGenerico15
	 */
	public String getLabelClassificatoreGenerico15() {
		return labelClassificatoreGenerico15;
	}

	/**
	 * @param labelClassificatoreGenerico15 the labelClassificatoreGenerico15 to set
	 */
	public void setLabelClassificatoreGenerico15(String labelClassificatoreGenerico15) {
		this.labelClassificatoreGenerico15 = labelClassificatoreGenerico15;
	}

	/**
	 * @return the pdcFinanziario
	 */
	public String getPdcFinanziario() {
		return pdcFinanziario;
	}

	/**
	 * @param pdcFinanziario the pdcFinanziario to set
	 */
	public void setPdcFinanziario(String pdcFinanziario) {
		this.pdcFinanziario = pdcFinanziario;
	}

	/**
	 * @return the strutturaAmministrativoResponsabile
	 */
	public String getStrutturaAmministrativoResponsabile() {
		return strutturaAmministrativoResponsabile;
	}

	/**
	 * @param strutturaAmministrativoResponsabile the strutturaAmministrativoResponsabile to set
	 */
	public void setStrutturaAmministrativoResponsabile(String strutturaAmministrativoResponsabile) {
		this.strutturaAmministrativoResponsabile = strutturaAmministrativoResponsabile;
	}

	/**
	 * @return the elementoPianoDeiContiEditabile
	 */
	public boolean isElementoPianoDeiContiEditabile() {
		return elementoPianoDeiContiEditabile;
	}

	/**
	 * @param elementoPianoDeiContiEditabile the elementoPianoDeiContiEditabile to set
	 */
	public void setElementoPianoDeiContiEditabile(boolean elementoPianoDeiContiEditabile) {
		this.elementoPianoDeiContiEditabile = elementoPianoDeiContiEditabile;
	}

	/**
	 * @return the strutturaAmministrativoContabileEditabile
	 */
	public boolean isStrutturaAmministrativoContabileEditabile() {
		return strutturaAmministrativoContabileEditabile;
	}

	/**
	 * @param strutturaAmministrativoContabileEditabile the strutturaAmministrativoContabileEditabile to set
	 */
	public void setStrutturaAmministrativoContabileEditabile(boolean strutturaAmministrativoContabileEditabile) {
		this.strutturaAmministrativoContabileEditabile = strutturaAmministrativoContabileEditabile;
	}

	/**
	 * @return the tipoFinanziamentoEditabile
	 */
	public boolean isTipoFinanziamentoEditabile() {
		return tipoFinanziamentoEditabile;
	}

	/**
	 * @param tipoFinanziamentoEditabile the tipoFinanziamentoEditabile to set
	 */
	public void setTipoFinanziamentoEditabile(boolean tipoFinanziamentoEditabile) {
		this.tipoFinanziamentoEditabile = tipoFinanziamentoEditabile;
	}

	/**
	 * @return the tipoFondoEditabile
	 */
	public boolean isTipoFondoEditabile() {
		return tipoFondoEditabile;
	}

	/**
	 * @param tipoFondoEditabile the tipoFondoEditabile to set
	 */
	public void setTipoFondoEditabile(boolean tipoFondoEditabile) {
		this.tipoFondoEditabile = tipoFondoEditabile;
	}

	/**
	 * @return the classificatoreGenerico1Editabile
	 */
	public boolean isClassificatoreGenerico1Editabile() {
		return classificatoreGenerico1Editabile;
	}

	/**
	 * @param classificatoreGenerico1Editabile the classificatoreGenerico1Editabile to set
	 */
	public void setClassificatoreGenerico1Editabile(boolean classificatoreGenerico1Editabile) {
		this.classificatoreGenerico1Editabile = classificatoreGenerico1Editabile;
	}

	/**
	 * @return the classificatoreGenerico2Editabile
	 */
	public boolean isClassificatoreGenerico2Editabile() {
		return classificatoreGenerico2Editabile;
	}

	/**
	 * @param classificatoreGenerico2Editabile the classificatoreGenerico2Editabile to set
	 */
	public void setClassificatoreGenerico2Editabile(boolean classificatoreGenerico2Editabile) {
		this.classificatoreGenerico2Editabile = classificatoreGenerico2Editabile;
	}

	/**
	 * @return the classificatoreGenerico3Editabile
	 */
	public boolean isClassificatoreGenerico3Editabile() {
		return classificatoreGenerico3Editabile;
	}

	/**
	 * @param classificatoreGenerico3Editabile the classificatoreGenerico3Editabile to set
	 */
	public void setClassificatoreGenerico3Editabile(boolean classificatoreGenerico3Editabile) {
		this.classificatoreGenerico3Editabile = classificatoreGenerico3Editabile;
	}

	/**
	 * @return the classificatoreGenerico4Editabile
	 */
	public boolean isClassificatoreGenerico4Editabile() {
		return classificatoreGenerico4Editabile;
	}

	/**
	 * @param classificatoreGenerico4Editabile the classificatoreGenerico4Editabile to set
	 */
	public void setClassificatoreGenerico4Editabile(boolean classificatoreGenerico4Editabile) {
		this.classificatoreGenerico4Editabile = classificatoreGenerico4Editabile;
	}

	/**
	 * @return the classificatoreGenerico5Editabile
	 */
	public boolean isClassificatoreGenerico5Editabile() {
		return classificatoreGenerico5Editabile;
	}

	/**
	 * @param classificatoreGenerico5Editabile the classificatoreGenerico5Editabile to set
	 */
	public void setClassificatoreGenerico5Editabile(boolean classificatoreGenerico5Editabile) {
		this.classificatoreGenerico5Editabile = classificatoreGenerico5Editabile;
	}

	/**
	 * @return the classificatoreGenerico6Editabile
	 */
	public boolean isClassificatoreGenerico6Editabile() {
		return classificatoreGenerico6Editabile;
	}

	/**
	 * @param classificatoreGenerico6Editabile the classificatoreGenerico6Editabile to set
	 */
	public void setClassificatoreGenerico6Editabile(boolean classificatoreGenerico6Editabile) {
		this.classificatoreGenerico6Editabile = classificatoreGenerico6Editabile;
	}

	/**
	 * @return the classificatoreGenerico7Editabile
	 */
	public boolean isClassificatoreGenerico7Editabile() {
		return classificatoreGenerico7Editabile;
	}

	/**
	 * @param classificatoreGenerico7Editabile the classificatoreGenerico7Editabile to set
	 */
	public void setClassificatoreGenerico7Editabile(boolean classificatoreGenerico7Editabile) {
		this.classificatoreGenerico7Editabile = classificatoreGenerico7Editabile;
	}

	/**
	 * @return the classificatoreGenerico8Editabile
	 */
	public boolean isClassificatoreGenerico8Editabile() {
		return classificatoreGenerico8Editabile;
	}

	/**
	 * @param classificatoreGenerico8Editabile the classificatoreGenerico8Editabile to set
	 */
	public void setClassificatoreGenerico8Editabile(boolean classificatoreGenerico8Editabile) {
		this.classificatoreGenerico8Editabile = classificatoreGenerico8Editabile;
	}

	/**
	 * @return the classificatoreGenerico9Editabile
	 */
	public boolean isClassificatoreGenerico9Editabile() {
		return classificatoreGenerico9Editabile;
	}

	/**
	 * @param classificatoreGenerico9Editabile the classificatoreGenerico9Editabile to set
	 */
	public void setClassificatoreGenerico9Editabile(boolean classificatoreGenerico9Editabile) {
		this.classificatoreGenerico9Editabile = classificatoreGenerico9Editabile;
	}

	/**
	 * @return the classificatoreGenerico10Editabile
	 */
	public boolean isClassificatoreGenerico10Editabile() {
		return classificatoreGenerico10Editabile;
	}

	/**
	 * @param classificatoreGenerico10Editabile the classificatoreGenerico10Editabile to set
	 */
	public void setClassificatoreGenerico10Editabile(boolean classificatoreGenerico10Editabile) {
		this.classificatoreGenerico10Editabile = classificatoreGenerico10Editabile;
	}

	/**
	 * @return the classificatoreGenerico11Editabile
	 */
	public boolean isClassificatoreGenerico11Editabile() {
		return classificatoreGenerico11Editabile;
	}

	/**
	 * @param classificatoreGenerico11Editabile the classificatoreGenerico11Editabile to set
	 */
	public void setClassificatoreGenerico11Editabile(boolean classificatoreGenerico11Editabile) {
		this.classificatoreGenerico11Editabile = classificatoreGenerico11Editabile;
	}

	/**
	 * @return the classificatoreGenerico12Editabile
	 */
	public boolean isClassificatoreGenerico12Editabile() {
		return classificatoreGenerico12Editabile;
	}

	/**
	 * @param classificatoreGenerico12Editabile the classificatoreGenerico12Editabile to set
	 */
	public void setClassificatoreGenerico12Editabile(boolean classificatoreGenerico12Editabile) {
		this.classificatoreGenerico12Editabile = classificatoreGenerico12Editabile;
	}

	/**
	 * @return the classificatoreGenerico13Editabile
	 */
	public boolean isClassificatoreGenerico13Editabile() {
		return classificatoreGenerico13Editabile;
	}

	/**
	 * @param classificatoreGenerico13Editabile the classificatoreGenerico13Editabile to set
	 */
	public void setClassificatoreGenerico13Editabile(boolean classificatoreGenerico13Editabile) {
		this.classificatoreGenerico13Editabile = classificatoreGenerico13Editabile;
	}

	/**
	 * @return the classificatoreGenerico14Editabile
	 */
	public boolean isClassificatoreGenerico14Editabile() {
		return classificatoreGenerico14Editabile;
	}

	/**
	 * @param classificatoreGenerico14Editabile the classificatoreGenerico14Editabile to set
	 */
	public void setClassificatoreGenerico14Editabile(boolean classificatoreGenerico14Editabile) {
		this.classificatoreGenerico14Editabile = classificatoreGenerico14Editabile;
	}

	/**
	 * @return the classificatoreGenerico15Editabile
	 */
	public boolean isClassificatoreGenerico15Editabile() {
		return classificatoreGenerico15Editabile;
	}

	/**
	 * @param classificatoreGenerico15Editabile the classificatoreGenerico15Editabile to set
	 */
	public void setClassificatoreGenerico15Editabile(boolean classificatoreGenerico15Editabile) {
		this.classificatoreGenerico15Editabile = classificatoreGenerico15Editabile;
	}

	/**
	 * @return the categoriaCapitoloEditabile
	 */
	public boolean isCategoriaCapitoloEditabile() {
		return categoriaCapitoloEditabile;
	}

	/**
	 * @param categoriaCapitoloEditabile the categoriaCapitoloEditabile to set
	 */
	public void setCategoriaCapitoloEditabile(boolean categoriaCapitoloEditabile) {
		this.categoriaCapitoloEditabile = categoriaCapitoloEditabile;
	}

	/**
	 * @return the descrizioneEditabile
	 */
	public boolean isDescrizioneEditabile() {
		return descrizioneEditabile;
	}

	/**
	 * @param descrizioneEditabile the descrizioneEditabile to set
	 */
	public void setDescrizioneEditabile(boolean descrizioneEditabile) {
		this.descrizioneEditabile = descrizioneEditabile;
	}

	/**
	 * @return the descrizioneArticoloEditabile
	 */
	public boolean isDescrizioneArticoloEditabile() {
		return descrizioneArticoloEditabile;
	}

	/**
	 * @param descrizioneArticoloEditabile the descrizioneArticoloEditabile to set
	 */
	public void setDescrizioneArticoloEditabile(boolean descrizioneArticoloEditabile) {
		this.descrizioneArticoloEditabile = descrizioneArticoloEditabile;
	}

	/**
	 * @return the flagRilevanteIvaEditabile
	 */
	public boolean isFlagRilevanteIvaEditabile() {
		return flagRilevanteIvaEditabile;
	}

	/**
	 * @param flagRilevanteIvaEditabile the flagRilevanteIvaEditabile to set
	 */
	public void setFlagRilevanteIvaEditabile(boolean flagRilevanteIvaEditabile) {
		this.flagRilevanteIvaEditabile = flagRilevanteIvaEditabile;
	}

	/**
	 * @return the noteEditabile
	 */
	public boolean isNoteEditabile() {
		return noteEditabile;
	}

	/**
	 * @param noteEditabile the noteEditabile to set
	 */
	public void setNoteEditabile(boolean noteEditabile) {
		this.noteEditabile = noteEditabile;
	}

	/**
	 * @return the flagImpegnabileEditabile
	 */
	public boolean isFlagImpegnabileEditabile() {
		return flagImpegnabileEditabile;
	}

	/**
	 * @param flagImpegnabileEditabile the flagImpegnabileEditabile to set
	 */
	public void setFlagImpegnabileEditabile(boolean flagImpegnabileEditabile) {
		this.flagImpegnabileEditabile = flagImpegnabileEditabile;
	}

	/**
	 * @return the daAggiornamento
	 */
	public boolean isDaAggiornamento() {
		return daAggiornamento;
	}

	/**
	 * @param daAggiornamento the daAggiornamento to set
	 */
	public void setDaAggiornamento(boolean daAggiornamento) {
		this.daAggiornamento = daAggiornamento;
	}

	/**
	 * @return the annoImporti
	 */
	public Integer getAnnoImporti() {
		return annoImporti;
	}

	/**
	 * @param annoImporti the annoImporti to set
	 */
	public void setAnnoImporti(Integer annoImporti) {
		this.annoImporti = annoImporti;
	}
	
	/**
	 * @return the uidVariazioneCapitolo
	 */
	public int getUidVariazioneCapitolo() {
		return uidVariazioneCapitolo;
	}

	/**
	 * @param uidVariazioneCapitolo the uidVariazioneCapitolo to set
	 */
	public void setUidVariazioneCapitolo(int uidVariazioneCapitolo) {
		this.uidVariazioneCapitolo = uidVariazioneCapitolo;
	}
	
	/**
	 * @return the daVariazione
	 */
	public Boolean getDaVariazione() {
		return daVariazione;
	}

	/**
	 * @param daVariazione the daVariazione to set
	 */
	public void setDaVariazione(Boolean daVariazione) {
		this.daVariazione = daVariazione;
	}
	
	/**
	 * @return the placeholderAnnoExCapitolo
	 */
	public String getPlaceholderAnnoExCapitolo() {
		return Integer.toString(getAnnoEsercizioInt().intValue() - 1);
	}
	
	/* **** REQUESTS **** */
	
	/**
	 * Restituisce una Request di tipo {@link ControllaClassificatoriModificabiliCapitolo} a partire dal Model.
	 * 
	 * @param capitolo il capitolo da inserire nella request 
	 * @param tipo     il tipo del capitolo
	 * 
	 * @return la Request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo(Capitolo<?, ?> capitolo, TipoCapitolo tipo) {
		ControllaClassificatoriModificabiliCapitolo request = creaRequest(ControllaClassificatoriModificabiliCapitolo.class);
		
		request.setBilancio(getBilancio());
		
		request.setEnte(getEnte());
		// Il capitolo può essere nullo, nel caso di ingresso nella ricerca
		if(capitolo != null) {
			request.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			request.setNumeroArticolo(capitolo.getNumeroArticolo());
			request.setNumeroUEB(capitolo.getNumeroUEB());
		}
		request.setTipoCapitolo(tipo);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link ControllaAttributiModificabiliCapitolo} a partire dal Model.
	 * 
	 * @param capitolo il capitolo da inserire nella request 
	 * @param tipo     il tipo del capitolo
	 * 
	 * @return la Request creata
	 */
	public ControllaAttributiModificabiliCapitolo creaRequestControllaAttributiModificabiliCapitolo(Capitolo<?, ?> capitolo, TipoCapitolo tipo) {
		ControllaAttributiModificabiliCapitolo request = creaRequest(ControllaAttributiModificabiliCapitolo.class);
		
		request.setBilancio(getBilancio());
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		// Il capitolo può essere nullo, nel caso di ingresso nella ricerca
		if(capitolo != null) {
			request.setNumeroArticolo(capitolo.getNumeroArticolo());
			request.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			request.setNumeroUEB(capitolo.getNumeroUEB());
		}
		request.setTipoCapitolo(tipo);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloPerAggiornamentoCapitolo} a partire dal Model.
	 * 
	 * @param uid l'uid del capitolo per cui efffettuare la richiesta
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloPerAggiornamentoCapitolo creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(Integer uid) {
		RicercaVariazioniCapitoloPerAggiornamentoCapitolo request = creaRequest(RicercaVariazioniCapitoloPerAggiornamentoCapitolo.class);
		request.setUidCapitolo(uid);
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link ContaClassificatoriERestituisciSeSingolo} a partire dal Model.
	 * 
	 * @param tipologiaClassificatore la tipologia di classificatore
	 * @return la Request creata
	 */
	public ContaClassificatoriERestituisciSeSingolo creaRequestContaClassificatoriERestituisciSeSingolo(TipologiaClassificatore tipologiaClassificatore) {
		ContaClassificatoriERestituisciSeSingolo request = creaRequest(ContaClassificatoriERestituisciSeSingolo.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setTipologiaClassificatore(tipologiaClassificatore);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno}.
	 * @param classificatoreGerarchico il classificatore da cercare
	 * @param tipologiaClassificatore la tipologia del classificatore
	 * @return la request creata
	 */
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(ClassificatoreGerarchico classificatoreGerarchico, TipologiaClassificatore tipologiaClassificatore) {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno req = creaRequest(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno.class);
		
		req.setAnno(getAnnoEsercizioInt());
		req.setClassificatore(classificatoreGerarchico);
		req.setTipologiaClassificatore(tipologiaClassificatore);
		
		return req;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Ottiene codice e descrizione dalla codifica.
	 * 
	 * @param codifica la codifica
	 * b
	 * @return il codice e la descrizione
	 */
	protected String getCodiceEDescrizione(Codifica codifica) {
		return codifica == null ? "" : (codifica.getCodice() + " - " + codifica.getDescrizione());
	}
	
	/**
	 * Costruisce la lista dei Classificatori Generici a partire dagli importi del Model.
	 * 
	 * @return la lista creata
	 */
	protected List<ClassificatoreGenerico> getListaClassificatoriGenerici() {
		List<ClassificatoreGenerico> lista = new ArrayList<ClassificatoreGenerico>();
		
		addClassificatoreGenericoALista(lista, classificatoreGenerico1);
		addClassificatoreGenericoALista(lista, classificatoreGenerico2);
		addClassificatoreGenericoALista(lista, classificatoreGenerico3);
		addClassificatoreGenericoALista(lista, classificatoreGenerico4);
		addClassificatoreGenericoALista(lista, classificatoreGenerico5);
		addClassificatoreGenericoALista(lista, classificatoreGenerico6);
		addClassificatoreGenericoALista(lista, classificatoreGenerico7);
		addClassificatoreGenericoALista(lista, classificatoreGenerico8);
		addClassificatoreGenericoALista(lista, classificatoreGenerico9);
		addClassificatoreGenericoALista(lista, classificatoreGenerico10);
		addClassificatoreGenericoALista(lista, classificatoreGenerico11);
		addClassificatoreGenericoALista(lista, classificatoreGenerico12);
		addClassificatoreGenericoALista(lista, classificatoreGenerico13);
		addClassificatoreGenericoALista(lista, classificatoreGenerico14);
		addClassificatoreGenericoALista(lista, classificatoreGenerico15);
		
		return lista;
	}
	
	/**
	 * Costruisce la lista dei Classificatori Generici a partire dagli importi del Model.
	 * 
	 * @param classificatoreAggiornamento il wrapper per l'aggiornamento
	 * 
	 * @return la lista creata
	 */
	protected List<ClassificatoreGenerico> getListaClassificatoriGenericiAggiornamento(ClassificatoreAggiornamento classificatoreAggiornamento) {
		List<ClassificatoreGenerico> lista = new ArrayList<ClassificatoreGenerico>();
		
		addClassificatoreGenericoALista(lista, classificatoreGenerico1,  classificatoreAggiornamento.getClassificatoreGenerico1(),  classificatoreGenerico1Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico2,  classificatoreAggiornamento.getClassificatoreGenerico2(),  classificatoreGenerico2Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico3,  classificatoreAggiornamento.getClassificatoreGenerico3(),  classificatoreGenerico3Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico4,  classificatoreAggiornamento.getClassificatoreGenerico4(),  classificatoreGenerico4Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico5,  classificatoreAggiornamento.getClassificatoreGenerico5(),  classificatoreGenerico5Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico6,  classificatoreAggiornamento.getClassificatoreGenerico6(),  classificatoreGenerico6Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico7,  classificatoreAggiornamento.getClassificatoreGenerico7(),  classificatoreGenerico7Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico8,  classificatoreAggiornamento.getClassificatoreGenerico8(),  classificatoreGenerico8Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico9,  classificatoreAggiornamento.getClassificatoreGenerico9(),  classificatoreGenerico9Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico10, classificatoreAggiornamento.getClassificatoreGenerico10(), classificatoreGenerico10Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico11, classificatoreAggiornamento.getClassificatoreGenerico11(), classificatoreGenerico11Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico12, classificatoreAggiornamento.getClassificatoreGenerico12(), classificatoreGenerico12Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico13, classificatoreAggiornamento.getClassificatoreGenerico13(), classificatoreGenerico13Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico14, classificatoreAggiornamento.getClassificatoreGenerico14(), classificatoreGenerico14Editabile);
		addClassificatoreGenericoALista(lista, classificatoreGenerico15, classificatoreAggiornamento.getClassificatoreGenerico15(), classificatoreGenerico15Editabile);
		
		return lista;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'inserimento dei classificatori generici se e solo se essi sono
	 * <ul>
	 *  <li>non-<code>null</code>;</li>
	 *  <li>con UID valorizzato.</li>
	 * </ul>
	 * 
	 * @param lista						la lista di classificatori cui apporre il classificatore
	 * @param classificatoreGenerico	il classificatore da apporre
	 */
	protected void addClassificatoreGenericoALista(List<ClassificatoreGenerico> lista, ClassificatoreGenerico classificatoreGenerico) {
		if(classificatoreGenerico != null && classificatoreGenerico.getUid() != 0) {
			lista.add(classificatoreGenerico);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per l'inserimento dei classificatori generici se e solo se essi sono
	 * <ul>
	 *  <li>non-<code>null</code>;</li>
	 *  <li>con UID valorizzato.</li>
	 * </ul>
	 * 
	 * @param lista							   la lista di classificatori cui apporre il classificatore
	 * @param classificatoreGenericoDaFrontEnd il classificatore da apporre
	 * @param classificatoreGenericoInSessione il classificatore gi&agrave; in sessione
	 * @param editabile						   definisce se il classificatore sia editabile
	 */
	protected void addClassificatoreGenericoALista(List<ClassificatoreGenerico> lista, ClassificatoreGenerico classificatoreGenericoDaFrontEnd,
			ClassificatoreGenerico classificatoreGenericoInSessione, boolean editabile) {
		lista.add(valutaInserimento(classificatoreGenericoDaFrontEnd, classificatoreGenericoInSessione, editabile));
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle informazioni relative ai classificatori a partire dalle liste in sessione.
	 * 
	 * @param sessionHandler l'handler per la sessione
	 */
	public void caricaClassificatoriDaSessione(SessionHandler sessionHandler) {
		elementoPianoDeiConti = caricaClassificatoriDaSessione(sessionHandler, elementoPianoDeiConti, BilSessionParameter.LISTA_ELEMENTO_PIANO_DEI_CONTI);
		strutturaAmministrativoContabile = caricaClassificatoriDaSessione(sessionHandler, strutturaAmministrativoContabile,
				BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		// Prima di poter compilare le stringhe di utilita', serve che gli elementi da cui le costruisco siano non-null
		if(elementoPianoDeiConti != null && StringUtils.isBlank(pdcFinanziario)) {
			pdcFinanziario = getCodiceEDescrizione(getElementoPianoDeiConti());
		}
		if(strutturaAmministrativoContabile != null && StringUtils.isBlank(strutturaAmministrativoResponsabile)) {
			strutturaAmministrativoResponsabile = getCodiceEDescrizione(getStrutturaAmministrativoContabile());
		}
		
		tipoFinanziamento = caricaClassificatoriDaSessione(sessionHandler, tipoFinanziamento, BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		tipoFondo = caricaClassificatoriDaSessione(sessionHandler, tipoFondo, BilSessionParameter.LISTA_TIPO_FONDO);
		
		// Caricamento dei classificatori generici
		caricaClassificatoriGenericiDaSessione(sessionHandler);
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle informazioni relative ai classificatori a partire dalle liste in sessione.
	 * @param <T> la tipizzazione della codifica
	 * 
	 * @param sessionHandler     l'handler per la sessione
	 * @param classificatore     il classificatore da ricercare
	 * @param nomeClassificatore il nome del classificatore in sessione
	 * 
	 * @return il classificatore valorizzato come da lista in sessione
	 */
	protected <T extends Codifica> T caricaClassificatoriDaSessione(SessionHandler sessionHandler, T classificatore, BilSessionParameter nomeClassificatore) {
		List<T> lista = sessionHandler.getParametro(nomeClassificatore);
		T classif = ComparatorUtils.searchByUid(lista, classificatore);
		if(classif != null) {
			classif.setDataFineValidita(null);
		}
		return classif;
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
	private ElementoPianoDeiConti caricaClassificatoriDaSessione(SessionHandler sessionHandler, ElementoPianoDeiConti classificatore, BilSessionParameter nomeClassificatore) {
		List<ElementoPianoDeiConti> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
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
	private StrutturaAmministrativoContabile caricaClassificatoriDaSessione(SessionHandler sessionHandler, StrutturaAmministrativoContabile classificatore, BilSessionParameter nomeClassificatore) {
		List<StrutturaAmministrativoContabile> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
	}
	
	/**
	 * Carica i classificatori generici a partire dalla sessione
	 * 
	 * @param sessionHandler l'handler per la sessione
	 */
	protected abstract void caricaClassificatoriGenericiDaSessione(SessionHandler sessionHandler);
	
	/**
	 * Metodo di utilit&agrave; per l'inserimento degli importi dei Capitolo di entrata previsione.
	 * <br>
	 * Il presente metodo controlla che sia valorizzato il campo {@code annoCompetenza}. In caso constrario, esso viene
	 * valorizzato tramite l'utilizzo del parametro {@code anno}.
	 * @param <T> la tipizzazione degli importi
	 * 
	 * @param lista			  la lista degli importi
	 * @param importiCapitolo l'importo da apporre alla lista
	 * @param anno			  l'anno cui l'importo si riferisce
	 */
	protected <T extends ImportiCapitolo> void addImportoCapitoloALista(List<T> lista, T importiCapitolo, Integer anno) {
		if(importiCapitolo.getAnnoCompetenza() == null || importiCapitolo.getAnnoCompetenza() == 0) {
			importiCapitolo.setAnnoCompetenza(anno);
		}
		lista.add(importiCapitolo);
	}
	
	/**
	 * Metodo di utilit&agrave; per la valutazione dell'inserimento di una data Entita nella request di aggiornamento.
	 * @param <T> la tipizzazione dell'entita
	 * 
	 * @param daFrontEnd l'entita ottenuta da frontEnd
	 * @param inSessione l'entita gi&agrave; presente in sessione
	 * @param editabile  definisce se il campo sia editabile
	 * 
	 * @return l'oggetto da inserire nella request
	 */
	protected <T extends Entita> T valutaInserimento(T daFrontEnd, T inSessione, boolean editabile) {
		T entita = null;
		
		if(!editabile) {
			entita = inSessione;
		} else if(inSessione != null && (daFrontEnd == null || daFrontEnd.getUid() == 0)) {
			entita = inSessione;
			entita.setDataFineValidita(new Date());
			
		} else if(daFrontEnd != null && daFrontEnd.getUid() != 0) {
			entita = daFrontEnd;
			entita.setDataFineValidita(null);
		}
		
		return entita;
	}
	
	/**
	 * Metodo di utilit&agrave; per la valutazione dell'inserimento di una data Entita nella request di aggiornamento massivo.
	 * @param <T> la tipizzazione dell'entita
	 * 
	 * @param daFrontEnd l'entita ottenuta da frontEnd
	 * @param inSessione l'entita gi&agrave; presente in sessione
	 * @param editabile  definisce se il campo sia editabile
	 * 
	 * @return l'oggetto da inserire nella request
	 */
	protected <T extends Entita> T valutaInserimentoMassivo(T daFrontEnd, T inSessione, boolean editabile) {
		T entita = null;
		
		// Se non ho l'editabilità, restitisco il null
		if(!editabile) {
			return entita;
		}
		
		if(inSessione != null && (daFrontEnd == null || daFrontEnd.getUid() == 0)) {
			entita = inSessione;
			entita.setDataFineValidita(new Date());
		} else if(daFrontEnd != null && daFrontEnd.getUid() != 0) {
			entita = daFrontEnd;
			entita.setDataFineValidita(null);
		}
		
		return entita;
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle stringhe di utilit&agrave;.
	 */
	protected void valorizzaStringheUtilita() {
		pdcFinanziario = getElementoPianoDeiConti() != null
				? getCodiceEDescrizione(getElementoPianoDeiConti())
				: "Nessun P.d.C. finanziario selezionato";
		strutturaAmministrativoResponsabile = getStrutturaAmministrativoContabile() != null
				? getCodiceEDescrizione(getStrutturaAmministrativoContabile())
				: "Nessuna Struttura Amministrativa Responsabile selezionata";
	}
	
	/**
	 * Metodo di utilit&agrave; per la valutazione della modificabilit&agrave; dei classificatori per l'aggiornamento.
	 *  
	 * @param response  la response da cui ottenere la modificabilt&agrave; dei campi
	 * @param isMassivo definisce le la valutazione si riferisce al caso massivo
	 */
	public void valutaModificabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response, boolean isMassivo) {
		// Controllo ciascun classificatore per la modificabilità
		
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		boolean unico = stessoNumCapArt <= 1L;
		
		// Elemento del piano dei conti
		elementoPianoDeiContiEditabile = isEditabile(unico, isMassivo, response,
				TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III,
				TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V);
		// Struttura amministrativa contabile
		strutturaAmministrativoContabileEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR);
		
		tipoFinanziamentoEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TIPO_FINANZIAMENTO);
		tipoFondoEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TIPO_FONDO);
		
		categoriaCapitoloEditabile = !isMassivo || unico;
		// Classificatori generici
		isClassificatoriGenericiEditabile(unico, isMassivo, response);
	}
	
	/**
	 * Controlla se il classificatore di dato tipo sia editabile.
	 * 
	 * @param unico     se l'elemento di getBilancio() sia unico
	 * @param isMassivo se il caso d'uso sia massivo
	 * @param response  la response del servizio di controllo modificabilit&agrave;
	 * @param tipologie le tipologie di classificatori da controllare
	 * 
	 * @return <code>true</code> se il classificatore &eacute; editabile; <code>false</code> in caso contrario.
	 */
	protected boolean isEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response, TipologiaClassificatore... tipologie) {
		// Null-safety
		if(tipologie == null || tipologie.length == 0) {
			return false;
		}
		
		
		boolean modificabileClassificatore = false;
		for(TipologiaClassificatore tipologia : tipologie) {
			modificabileClassificatore = modificabileClassificatore || response.isModificabile(tipologia);
		}
		
		//CR 3591
		boolean modificabileClassificatoreMG = false;
		for(TipologiaClassificatore tipologia : tipologie) {
			modificabileClassificatoreMG = modificabileClassificatoreMG || response.isModificabilePerMovimentoGestione(tipologia);
		}
		
		return unico || (modificabileClassificatore ^ (isMassivo && modificabileClassificatoreMG));
		
		//Tabella casi possibili:
		//             false                 false        false                            -> false (ok)
		//             false                 true         false                            -> false (ok)
		//             false                 true         true                             -> true  (ok)
		                                          
        //             true                  false        true                             -> true  (ok)
		//             true                  true         true                             -> false (ok)

	}
	
	/**
	 * Controlla se i classificatori generici siano editabili.
	 * 
	 * @param unico     se l'elemento di getBilancio() sia unico
	 * @param isMassivo se il caso d'uso sia massivo
	 * @param response  la response del servizio di controllo modificabilit&agrave;
	 */
	protected abstract void isClassificatoriGenericiEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response);
	
	/**
	 * Metodo di utilit&agrave; per la valutazione della modificabilit&agrave; degli attributi per l'aggiornamento.
	 *  
	 * @param response  la response da cui ottenere la modificabilt&agrave; dei campi
	 * @param isMassivo definisce le la valutazione si riferisce al caso massivo
	 */
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		// Controllo ciascun attributo per la modificabilita'
		
		// Controllo se il classificatore è unico: in tal caso, ogni classificatore sarà modificabile
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		long stessoNumCap = response.getStessoNumCap() != null ? response.getStessoNumCap().longValue() : 0L;
		boolean unicoArticolo = stessoNumCapArt <= 1L;
		boolean unicoCapitolo = stessoNumCap <= 1L;
		
		// Se il numero di elementi con lo stesso capitolo è pari al numero di elementi con lo stesso articolo, allora esiste solo l'articolo in questione
		boolean soloCapitoliDiDatoArticolo = stessoNumCap == stessoNumCapArt;
		
		// Descrizione
		descrizioneEditabile = unicoCapitolo || (soloCapitoliDiDatoArticolo && isMassivo);
		descrizioneArticoloEditabile = isMassivo || unicoArticolo || soloCapitoliDiDatoArticolo;
		
		// Flag Rilevante IVA
		flagRilevanteIvaEditabile = isEditabile(unicoArticolo, isMassivo, response, TipologiaAttributo.FLAG_RILEVANTE_IVA);
		// Note
		noteEditabile = isEditabile(unicoArticolo, isMassivo, response, TipologiaAttributo.NOTE);
		// Flag Impegnabile
		flagImpegnabileEditabile = unicoArticolo || !isMassivo;
	}
	
	/**
	 * Controlla se l'attributo di dato tipo sia editabile.
	 * 
	 * @param unico     se l'elemento di getBilancio() sia unico
	 * @param isMassivo se il caso d'uso sia massivo
	 * @param response  la response del servizio di controllo modificabilit&agrave;
	 * @param tipologie le tipologie di classificatori da controllare
	 * 
	 * @return <code>true</code> se il classificatore &eacute; editabile; <code>false</code> in caso contrario.
	 */
	protected boolean isEditabile(boolean unico, boolean isMassivo, ControllaAttributiModificabiliCapitoloResponse response, TipologiaAttributo... tipologie) {
		// Null-safety
		if(tipologie == null || tipologie.length == 0) {
			return false;
		}
		boolean modificabileClassificatore = false;
		for(TipologiaAttributo tipologia : tipologie) {
			modificabileClassificatore = modificabileClassificatore || response.isModificabile(tipologia);
		}
		return unico || modificabileClassificatore ^ isMassivo;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'estrazione del label del classificatore generico a partire dalla lista.
	 * 
	 * @param listaClassificatoriGenerici	la lista del Classificatore da cui estrarre il label
	 * 
	 * @return il label estratto
	 */
	protected String ottieniLabelClassificatoreGenerico(List<ClassificatoreGenerico> listaClassificatoriGenerici) {
		if(listaClassificatoriGenerici != null && !listaClassificatoriGenerici.isEmpty()) {
			return listaClassificatoriGenerici.get(0).getTipoClassificatore().getDescrizione();
		}
		return null;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'estrazione del label del classificatore generico a partire dal classificatore.
	 * @param classificatoreGenerici il classificatore
	 * @return il label estratto
	 */
	protected String ottieniLabelClassificatoreGenerico(ClassificatoreGenerico classificatoreGenerici) {
		return classificatoreGenerici == null || classificatoreGenerici.getTipoClassificatore() == null
			? null
			: classificatoreGenerici.getTipoClassificatore().getDescrizione();
	}
	
	/**
	 * Reimposta i dati disabilitati causa non editabilit&agrave; nel model.
	 * 
	 * @param classificatoreAggiornamento i classificatori originali in sessione
	 */
	protected void setParametriDisabilitati(ClassificatoreAggiornamento classificatoreAggiornamento) {
		elementoPianoDeiConti = impostaIlDato(elementoPianoDeiContiEditabile, elementoPianoDeiConti, classificatoreAggiornamento.getElementoPianoDeiConti());
		strutturaAmministrativoContabile = impostaIlDato(strutturaAmministrativoContabileEditabile, strutturaAmministrativoContabile, classificatoreAggiornamento.getStrutturaAmministrativoContabile());
		tipoFinanziamento = impostaIlDato(tipoFinanziamentoEditabile, tipoFinanziamento, classificatoreAggiornamento.getTipoFinanziamento());
		tipoFondo = impostaIlDato(tipoFondoEditabile, tipoFondo, classificatoreAggiornamento.getTipoFondo());
		classificatoreGenerico1 = impostaIlDato(classificatoreGenerico1Editabile, classificatoreGenerico1, classificatoreAggiornamento.getClassificatoreGenerico1());
		classificatoreGenerico2 = impostaIlDato(classificatoreGenerico2Editabile, classificatoreGenerico2, classificatoreAggiornamento.getClassificatoreGenerico2());
		classificatoreGenerico3 = impostaIlDato(classificatoreGenerico3Editabile, classificatoreGenerico3, classificatoreAggiornamento.getClassificatoreGenerico3());
		classificatoreGenerico4 = impostaIlDato(classificatoreGenerico4Editabile, classificatoreGenerico4, classificatoreAggiornamento.getClassificatoreGenerico4());
		classificatoreGenerico5 = impostaIlDato(classificatoreGenerico5Editabile, classificatoreGenerico5, classificatoreAggiornamento.getClassificatoreGenerico5());
		classificatoreGenerico6 = impostaIlDato(classificatoreGenerico6Editabile, classificatoreGenerico6, classificatoreAggiornamento.getClassificatoreGenerico6());
		classificatoreGenerico7 = impostaIlDato(classificatoreGenerico7Editabile, classificatoreGenerico7, classificatoreAggiornamento.getClassificatoreGenerico7());
		classificatoreGenerico8 = impostaIlDato(classificatoreGenerico8Editabile, classificatoreGenerico8, classificatoreAggiornamento.getClassificatoreGenerico8());
		classificatoreGenerico9 = impostaIlDato(classificatoreGenerico9Editabile, classificatoreGenerico9, classificatoreAggiornamento.getClassificatoreGenerico9());
		classificatoreGenerico10 = impostaIlDato(classificatoreGenerico10Editabile, classificatoreGenerico10, classificatoreAggiornamento.getClassificatoreGenerico10());
		classificatoreGenerico11 = impostaIlDato(classificatoreGenerico11Editabile, classificatoreGenerico11, classificatoreAggiornamento.getClassificatoreGenerico11());
		classificatoreGenerico12 = impostaIlDato(classificatoreGenerico12Editabile, classificatoreGenerico12, classificatoreAggiornamento.getClassificatoreGenerico12());
		classificatoreGenerico13 = impostaIlDato(classificatoreGenerico13Editabile, classificatoreGenerico13, classificatoreAggiornamento.getClassificatoreGenerico13());
		classificatoreGenerico14 = impostaIlDato(classificatoreGenerico14Editabile, classificatoreGenerico14, classificatoreAggiornamento.getClassificatoreGenerico14());
		classificatoreGenerico15 = impostaIlDato(classificatoreGenerico15Editabile, classificatoreGenerico15, classificatoreAggiornamento.getClassificatoreGenerico15());
	}
	
	/**
	 * Imposta un dato tra due campi dato il booleano.
	 * @param <T> il tipo
	 * 
	 * @param editabile se il campo sia editabile o meno
	 * @param campo     il campo da popolare
	 * @param vecchio   il campo con il valore da ottenere
	 * 
	 * @return il campo impostato
	 */
	protected <T> T impostaIlDato(boolean editabile, T campo, T vecchio) {
		T result = campo;
		if(!editabile) {
			result = vecchio;
		}
		return result;
	}
	
	/**
	 * Imposta l'entit&agrave; selezionata all'interno del capitolo.
	 * <br>
	 * <strong>ATTENZIONE!</strong> Non funziona con il TipoAtto.
	 * @param <C> la tipizzazione del capitolo
	 * @param <E> la tipizzazione dell'entita
	 * 
	 * @param capitolo  il capitolo in cui injettare il dato
	 * @param entita    l'entit&agrave; da injettare
	 * 
	 * @throws IllegalArgumentException nel caso in cui l'invocazione non vada a buon fine
	 */
	protected <C extends Capitolo<?, ?>, E extends Entita> void impostaIlParametroNelCapitolo(C capitolo, E entita) {
		if(entita == null || entita.getUid() == 0) {
			return;
		}
		// Fallback in caso di entita null
		Class<? extends Entita> clazz = entita.getClass();
		String nomeSetter = "set" + StringUtils.capitalize(clazz.getSimpleName());
		try {
			Method method = ReflectionUtil.silentlyFindMethod(capitolo.getClass(), nomeSetter, clazz);
			ReflectionUtil.silentlyInvokeMethod(method, capitolo, entita);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("IllegalArgumentException nell'impostazione del parametro. Invocazione del metodo " + nomeSetter, e);
		}
	}
	
	/**
	 * Injetta il codice della codifica nella request di ricerca nel caso in cui tale codifica sia valida.
	 * 
	 * @param request   la request in cui injettare il codice
	 * @param codifica  la codifica da cui ottenere il codice
	 * @param nomeCampo il nome del campo in cui injettare la codifica
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaCodiceCodificaNellaRicercaSeValida(Serializable request, Codifica codifica, String nomeCampo) {
		// Se la codifica non è valorizzata correttamente, esco subito dal metodo
		if(codifica == null || StringUtils.isBlank(codifica.getCodice())) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, nomeCampo, String.class, codifica.getCodice());
	}
	
	/**
	 * Injetta l'atto di legge nella request di ricerca nel caso in cui tale codifica sia valida.
	 * 
	 * @param request     la request in cui injettare il codice
	 * @param attoDiLegge l'atto di legge da cui ottenere i dati
	 */
	protected void injettaAttoDiLeggeNellaRicercaSeValido(Serializable request, AttoDiLegge attoDiLegge) {
		// Se l'atto di legge è null esco immediatamente
		if(attoDiLegge == null) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, "setAnnoAttoDilegge", Integer.class, attoDiLegge.getAnno());
		ReflectionUtil.invokeSetterMethod(request, "setArticoloAttoDilegge", String.class, attoDiLegge.getArticolo());
		ReflectionUtil.invokeSetterMethod(request, "setCommaAttoDilegge", String.class, attoDiLegge.getComma());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroAttoDilegge", Integer.class, attoDiLegge.getNumero());
		ReflectionUtil.invokeSetterMethod(request, "setPuntoAttoDilegge", String.class, attoDiLegge.getPunto());
		
		injettaCodiceCodificaNellaRicercaSeValida(request, attoDiLegge.getTipoAtto(), "setTipoAttoDilegge");
	}
	
	/**
	 * Injetta il codice della codifica nella request di ricerca nel caso in cui tale codifica sia valida.
	 * 
	 * @param request   la request in cui injettare il codice
	 * @param stringa   la stringa da injettare
	 * @param nomeCampo il nome del campo in cui injettare la codifica
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaStringaRicercaSeValida(Serializable request, String stringa, String nomeCampo) {
		// Se la stringa è vuota o null esco immediatamente
		if(StringUtils.isBlank(stringa)) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, nomeCampo, String.class, StringUtils.trim(stringa));
	}
	
	/**
	 * Injetta il capitolo nella request di ricerca nel caso in cui tale entit&agrave; sia valida.
	 * 
	 * @param request  la request in cui injettare il codice
	 * @param capitolo il capitolo da cui ottenere i dati
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaCapitoloNellaRicercaSeValido(Serializable request, Capitolo<?, ?> capitolo) {
		// Se il capitolo non è valorizzato esco immediatamente
		if(capitolo == null) {
			return;
		}
		
		ReflectionUtil.invokeSetterMethod(request, "setAnnoCapitolo", Integer.class, capitolo.getAnnoCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroCapitolo", Integer.class, capitolo.getNumeroCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroArticolo", Integer.class, capitolo.getNumeroArticolo());
		ReflectionUtil.invokeSetterMethod(request, "setNumeroUEB", Integer.class, capitolo.getNumeroUEB());
		
		// Stringhe
		injettaStringaRicercaSeValida(request, capitolo.getDescrizione(), "setDescrizioneCapitolo");
		injettaStringaRicercaSeValida(request, capitolo.getDescrizioneArticolo(), "setDescrizioneArticolo");
		
		// Campi ex
		ReflectionUtil.invokeSetterMethod(request, "setExAnnoCapitolo", Integer.class, capitolo.getExAnnoCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setExNumeroArticolo", Integer.class, capitolo.getExArticolo());
		ReflectionUtil.invokeSetterMethod(request, "setExNumeroCapitolo", Integer.class, capitolo.getExCapitolo());
		ReflectionUtil.invokeSetterMethod(request, "setExNumeroUEB", Integer.class, capitolo.getExUEB());
	}

	/**
	 * Crea una request per il servizio di {@link InserisciDettaglioVariazioneImportoCapitolo}.
	 * @param uidCapitolo l'uid del capitolo da utilizzare
	 * @return la request creata
	 */
	public InserisciDettaglioVariazioneImportoCapitolo creaRequestInserisciDettaglioVariazioneImportoCapitolo(int uidCapitolo){
		InserisciDettaglioVariazioneImportoCapitolo request = creaRequest(InserisciDettaglioVariazioneImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = new DettaglioVariazioneImportoCapitolo();
		
		Capitolo<?,?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setUid(uidCapitolo);
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		dettaglioVariazioneImportoCapitolo.setFlagNuovoCapitolo(Boolean.TRUE);
		VariazioneImportoCapitolo variazioneImportoCapitolo = new VariazioneImportoCapitolo();
		variazioneImportoCapitolo.setUid(uidVariazioneCapitolo);
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		request.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		return request;
	}
	
	/**
	 * Injetta la Struttura Amministrativo Contabile nella request di ricerca nel caso in cui tale entit&agrave; sia valida.
	 * <br>
	 * Il metodo &eacute; a parte in quanto si richiede anche l'injezione del codice del tipo di Struttura.
	 * 
	 * @param request                          la request in cui injettare il codice
	 * @param sac la Struttura da cui ottenere i dati
	 * @param stringaUtilita                   la stringa di utilit&agrave; da cui ottenere il codice del tipo
	 * 
	 * @throws IllegalArgumentException nel caso in cui il nome del campo sia non corretto
	 */
	protected void injettaStrutturaAmministrativoContabileNellaRicercaSeValido(Serializable request, StrutturaAmministrativoContabile sac, 
			String stringaUtilita) {
		// Se la struttura non è valorizzata correttamente esco immediatamente
		if(sac == null || sac.getUid() == 0 || StringUtils.isBlank(stringaUtilita)) {
			return;
		}
		ReflectionUtil.invokeSetterMethod(request, "setCodiceStrutturaAmmCont", String.class, sac.getCodice());
		
		String codiceTipoStrutturaAmmCont = Pattern.compile("\\d{3} -").matcher(strutturaAmministrativoResponsabile).find() ? 
				BilConstants.CODICE_CDC.getConstant() : BilConstants.CODICE_CDR.getConstant();
		ReflectionUtil.invokeSetterMethod(request, "setCodiceTipoStrutturaAmmCont", String.class, codiceTipoStrutturaAmmCont);
	}
	
	/**
	 * Controlla gli importi del capitolo equivalente, e nel caso non siano settati li inizializza.
	 * @param <IEQ> la tipizzaizone degli importi
	 * @param <CAP> la tipizzazione del capitolo
	 * 
	 * @param capitolo     il capitolo da controllare e popolare
	 * @param importiClazz la classe degli importi
	 */
	protected <IEQ extends ImportiCapitolo, CAP extends Capitolo<?, IEQ>> void controllaImportiCapitoloEquivalente(CAP capitolo, Class<IEQ> importiClazz) {
		// Check dei campi per gli importi del capitolo equivalente
		if(capitolo.getImportiCapitoloEquivalente() == null) {
			try {
				capitolo.setImportiCapitoloEquivalente(importiClazz.newInstance());
			} catch (Exception e) {
				// Ignoro l'errore: impossibile inizializzare gli importi
			}
			
		}
	}
	
	/**
	 * Crea un'Utilit&agrave; per i Parametri di Paginazione come dato aggiuntivo.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	protected ParametriPaginazione creaParametriPaginazioneComeDatoAggiuntivo() {
		return new ParametriPaginazione(0, 10000);
	}

}
