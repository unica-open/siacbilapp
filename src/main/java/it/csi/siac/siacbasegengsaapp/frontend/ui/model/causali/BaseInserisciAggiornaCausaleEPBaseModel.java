/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccommon.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorEvento;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEP;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoConto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe base di model per l'inserimento e l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 30/03/2015
 * @version 1.1.0 - 07/10/2015 GSA/GEN
 *   
 */
public abstract class BaseInserisciAggiornaCausaleEPBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1742490947751985513L;
	
	private CausaleEP causaleEP;
	private TipoEvento tipoEvento;
	private ContoTipoOperazione contoTipoOperazione;
	private ElementoPianoDeiConti elementoPianoDeiConti;
	private Soggetto soggetto;
	
	private List<ContoTipoOperazione> listaContoTipoOperazione = new ArrayList<ContoTipoOperazione>();
	
	private List<TipoCausale> listaTipoCausale = new ArrayList<TipoCausale>();
	private List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	//listaTipoEvento filtrata per iltipodi causale scelto dall'utente
	private List<TipoEvento> listaTipoEventoFiltrata = new ArrayList<TipoEvento>();
	
	private Integer indiceConto;
	private List<ClassePiano> listaClassePiano = new ArrayList<ClassePiano>();
	private List<OperazioneSegnoConto> listaOperazioneSegnoConto = new ArrayList<OperazioneSegnoConto>();
	private List<OperazioneUtilizzoConto> listaOperazioneUtilizzoConto = new ArrayList<OperazioneUtilizzoConto>();
	private List<OperazioneUtilizzoImporto> listaOperazioneUtilizzoImporto = new ArrayList<OperazioneUtilizzoImporto>();
	private List<OperazioneTipoImporto> listaOperazioneTipoImporto = new ArrayList<OperazioneTipoImporto>();
	
	// Classificatori generici
	private String labelClassificatoreEP1;
	private String labelClassificatoreEP2;
	private String labelClassificatoreEP3;
	private String labelClassificatoreEP4;
	private String labelClassificatoreEP5;
	private String labelClassificatoreEP6;
	
	private List<ClassificatoreEP> listaClassificatoreEP1 = new ArrayList<ClassificatoreEP>();
	private List<ClassificatoreEP> listaClassificatoreEP2 = new ArrayList<ClassificatoreEP>();
	private List<ClassificatoreEP> listaClassificatoreEP3 = new ArrayList<ClassificatoreEP>();
	private List<ClassificatoreEP> listaClassificatoreEP4 = new ArrayList<ClassificatoreEP>();
	private List<ClassificatoreEP> listaClassificatoreEP5 = new ArrayList<ClassificatoreEP>();
	private List<ClassificatoreEP> listaClassificatoreEP6 = new ArrayList<ClassificatoreEP>();
	
	private ClassificatoreEP classificatoreEP1;
	private ClassificatoreEP classificatoreEP2;
	private ClassificatoreEP classificatoreEP3;
	private ClassificatoreEP classificatoreEP4;
	private ClassificatoreEP classificatoreEP5;
	private ClassificatoreEP classificatoreEP6;
	
	/* modale compilazione guidata conto*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private Ambito ambito;
	
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */

	public Ambito getAmbito(){
		return this.ambito;
	}
	
	/**
	 * @param ambito the ambito to set
	 */
	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}

	
	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}

	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
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
	 * @return the contoTipoOperazione
	 */
	public ContoTipoOperazione getContoTipoOperazione() {
		return contoTipoOperazione;
	}

	/**
	 * @param contoTipoOperazione the contoTipoOperazione to set
	 */
	public void setContoTipoOperazione(ContoTipoOperazione contoTipoOperazione) {
		this.contoTipoOperazione = contoTipoOperazione;
	}

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
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the listaContoTipoOperazione
	 */
	public List<ContoTipoOperazione> getListaContoTipoOperazione() {
		return listaContoTipoOperazione;
	}

	/**
	 * @param listaContoTipoOperazione the listaContoTipoOperazione to set
	 */
	public void setListaContoTipoOperazione(List<ContoTipoOperazione> listaContoTipoOperazione) {
		this.listaContoTipoOperazione = listaContoTipoOperazione != null ? listaContoTipoOperazione : new ArrayList<ContoTipoOperazione>();
	}

	/**
	 * @return the listaTipoCausale
	 */
	public List<TipoCausale> getListaTipoCausale() {
		return listaTipoCausale;
	}

	/**
	 * @param listaTipoCausale the listaTipoCausale to set
	 */
	public void setListaTipoCausale(List<TipoCausale> listaTipoCausale) {
		this.listaTipoCausale = listaTipoCausale != null ? listaTipoCausale : new ArrayList<TipoCausale>();
	}

	/**
	 * @return the listaTipoEvento
	 */
	public List<TipoEvento> getListaTipoEvento() {
		return listaTipoEvento;
	}

	/**
	 * @param listaTipoEvento the listaTipoEvento to set
	 */
	public void setListaTipoEvento(List<TipoEvento> listaTipoEvento) {
		this.listaTipoEvento = listaTipoEvento != null ? listaTipoEvento : new ArrayList<TipoEvento>();
	}

	/**
	 * @return the listaTipoEventoFiltrata
	 */
	public final List<TipoEvento> getListaTipoEventoFiltrata() {
		return listaTipoEventoFiltrata;
	}

	/**
	 * @param listaTipoEventoFiltrata the listaTipoEventoFiltrata to set
	 */
	public final void setListaTipoEventoFiltrata(
			List<TipoEvento> listaTipoEventoFiltrata) {
		this.listaTipoEventoFiltrata = listaTipoEventoFiltrata != null ? listaTipoEventoFiltrata : new ArrayList<TipoEvento>();
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

	/**
	 * @return the indiceConto
	 */
	public Integer getIndiceConto() {
		return indiceConto;
	}

	/**
	 * @param indiceConto the indiceConto to set
	 */
	public void setIndiceConto(Integer indiceConto) {
		this.indiceConto = indiceConto;
	}

	/**
	 * @return the listaClassePiano
	 */
	public List<ClassePiano> getListaClassePiano() {
		return listaClassePiano;
	}

	/**
	 * @param listaClassePiano the listaClassePiano to set
	 */
	public void setListaClassePiano(List<ClassePiano> listaClassePiano) {
		this.listaClassePiano = listaClassePiano != null ? listaClassePiano : new ArrayList<ClassePiano>();
	}

	/**
	 * @return the listaOperazioneSegnoConto
	 */
	public List<OperazioneSegnoConto> getListaOperazioneSegnoConto() {
		return listaOperazioneSegnoConto;
	}

	/**
	 * @param listaOperazioneSegnoConto the listaOperazioneSegnoConto to set
	 */
	public void setListaOperazioneSegnoConto(List<OperazioneSegnoConto> listaOperazioneSegnoConto) {
		this.listaOperazioneSegnoConto = listaOperazioneSegnoConto != null ? listaOperazioneSegnoConto : new ArrayList<OperazioneSegnoConto>();
	}

	/**
	 * @return the listaOperazioneUtilizzoConto
	 */
	public List<OperazioneUtilizzoConto> getListaOperazioneUtilizzoConto() {
		return listaOperazioneUtilizzoConto;
	}

	/**
	 * @param listaOperazioneUtilizzoConto the listaOperazioneUtilizzoConto to set
	 */
	public void setListaOperazioneUtilizzoConto(List<OperazioneUtilizzoConto> listaOperazioneUtilizzoConto) {
		this.listaOperazioneUtilizzoConto = listaOperazioneUtilizzoConto != null ? listaOperazioneUtilizzoConto : new ArrayList<OperazioneUtilizzoConto>();
	}

	/**
	 * @return the listaOperazioneUtilizzoImporto
	 */
	public List<OperazioneUtilizzoImporto> getListaOperazioneUtilizzoImporto() {
		return listaOperazioneUtilizzoImporto;
	}

	/**
	 * @param listaOperazioneUtilizzoImporto the listaOperazioneUtilizzoImporto to set
	 */
	public void setListaOperazioneUtilizzoImporto(List<OperazioneUtilizzoImporto> listaOperazioneUtilizzoImporto) {
		this.listaOperazioneUtilizzoImporto = listaOperazioneUtilizzoImporto != null ? listaOperazioneUtilizzoImporto : new ArrayList<OperazioneUtilizzoImporto>();
	}

	/**
	 * @return the listaOperazioneTipoImporto
	 */
	public List<OperazioneTipoImporto> getListaOperazioneTipoImporto() {
		return listaOperazioneTipoImporto;
	}

	/**
	 * @param listaOperazioneTipoImporto the listaOperazioneTipoImporto to set
	 */
	public void setListaOperazioneTipoImporto(List<OperazioneTipoImporto> listaOperazioneTipoImporto) {
		this.listaOperazioneTipoImporto = listaOperazioneTipoImporto != null ? listaOperazioneTipoImporto : new ArrayList<OperazioneTipoImporto>();
	}
	
	/**
	 * @return the labelClassificatoreEP1
	 */
	public String getLabelClassificatoreEP1() {
		return labelClassificatoreEP1;
	}

	/**
	 * @param labelClassificatoreEP1 the labelClassificatoreEP1 to set
	 */
	public void setLabelClassificatoreEP1(String labelClassificatoreEP1) {
		this.labelClassificatoreEP1 = labelClassificatoreEP1;
	}

	/**
	 * @return the labelClassificatoreEP2
	 */
	public String getLabelClassificatoreEP2() {
		return labelClassificatoreEP2;
	}

	/**
	 * @param labelClassificatoreEP2 the labelClassificatoreEP2 to set
	 */
	public void setLabelClassificatoreEP2(String labelClassificatoreEP2) {
		this.labelClassificatoreEP2 = labelClassificatoreEP2;
	}

	/**
	 * @return the labelClassificatoreEP3
	 */
	public String getLabelClassificatoreEP3() {
		return labelClassificatoreEP3;
	}

	/**
	 * @param labelClassificatoreEP3 the labelClassificatoreEP3 to set
	 */
	public void setLabelClassificatoreEP3(String labelClassificatoreEP3) {
		this.labelClassificatoreEP3 = labelClassificatoreEP3;
	}

	/**
	 * @return the labelClassificatoreEP4
	 */
	public String getLabelClassificatoreEP4() {
		return labelClassificatoreEP4;
	}

	/**
	 * @param labelClassificatoreEP4 the labelClassificatoreEP4 to set
	 */
	public void setLabelClassificatoreEP4(String labelClassificatoreEP4) {
		this.labelClassificatoreEP4 = labelClassificatoreEP4;
	}

	/**
	 * @return the labelClassificatoreEP5
	 */
	public String getLabelClassificatoreEP5() {
		return labelClassificatoreEP5;
	}

	/**
	 * @param labelClassificatoreEP5 the labelClassificatoreEP5 to set
	 */
	public void setLabelClassificatoreEP5(String labelClassificatoreEP5) {
		this.labelClassificatoreEP5 = labelClassificatoreEP5;
	}

	/**
	 * @return the labelClassificatoreEP6
	 */
	public String getLabelClassificatoreEP6() {
		return labelClassificatoreEP6;
	}

	/**
	 * @param labelClassificatoreEP6 the labelClassificatoreEP6 to set
	 */
	public void setLabelClassificatoreEP6(String labelClassificatoreEP6) {
		this.labelClassificatoreEP6 = labelClassificatoreEP6;
	}

	/**
	 * @return the listaClassificatoreEP1
	 */
	public List<ClassificatoreEP> getListaClassificatoreEP1() {
		return listaClassificatoreEP1;
	}

	/**
	 * @param listaClassificatoreEP1 the listaClassificatoreEP1 to set
	 */
	public void setListaClassificatoreEP1(List<ClassificatoreEP> listaClassificatoreEP1) {
		this.listaClassificatoreEP1 = listaClassificatoreEP1 != null ? listaClassificatoreEP1 : new ArrayList<ClassificatoreEP>();
	}

	/**
	 * @return the listaClassificatoreEP2
	 */
	public List<ClassificatoreEP> getListaClassificatoreEP2() {
		return listaClassificatoreEP2;
	}

	/**
	 * @param listaClassificatoreEP2 the listaClassificatoreEP2 to set
	 */
	public void setListaClassificatoreEP2(List<ClassificatoreEP> listaClassificatoreEP2) {
		this.listaClassificatoreEP2 = listaClassificatoreEP2 != null ? listaClassificatoreEP2 : new ArrayList<ClassificatoreEP>();
	}

	/**
	 * @return the listaClassificatoreEP3
	 */
	public List<ClassificatoreEP> getListaClassificatoreEP3() {
		return listaClassificatoreEP3;
	}

	/**
	 * @param listaClassificatoreEP3 the listaClassificatoreEP3 to set
	 */
	public void setListaClassificatoreEP3(List<ClassificatoreEP> listaClassificatoreEP3) {
		this.listaClassificatoreEP3 = listaClassificatoreEP3 != null ? listaClassificatoreEP3 : new ArrayList<ClassificatoreEP>();
	}

	/**
	 * @return the listaClassificatoreEP4
	 */
	public List<ClassificatoreEP> getListaClassificatoreEP4() {
		return listaClassificatoreEP4;
	}

	/**
	 * @param listaClassificatoreEP4 the listaClassificatoreEP4 to set
	 */
	public void setListaClassificatoreEP4(List<ClassificatoreEP> listaClassificatoreEP4) {
		this.listaClassificatoreEP4 = listaClassificatoreEP4 != null ? listaClassificatoreEP4 : new ArrayList<ClassificatoreEP>();
	}

	/**
	 * @return the listaClassificatoreEP5
	 */
	public List<ClassificatoreEP> getListaClassificatoreEP5() {
		return listaClassificatoreEP5;
	}

	/**
	 * @param listaClassificatoreEP5 the listaClassificatoreEP5 to set
	 */
	public void setListaClassificatoreEP5(List<ClassificatoreEP> listaClassificatoreEP5) {
		this.listaClassificatoreEP5 = listaClassificatoreEP5 != null ? listaClassificatoreEP5 : new ArrayList<ClassificatoreEP>();
	}

	/**
	 * @return the listaClassificatoreEP6
	 */
	public List<ClassificatoreEP> getListaClassificatoreEP6() {
		return listaClassificatoreEP6;
	}

	/**
	 * @param listaClassificatoreEP6 the listaClassificatoreEP6 to set
	 */
	public void setListaClassificatoreEP6(List<ClassificatoreEP> listaClassificatoreEP6) {
		this.listaClassificatoreEP6 = listaClassificatoreEP6 != null ? listaClassificatoreEP6 : new ArrayList<ClassificatoreEP>();
	}

	/**
	 * @return the classificatoreEP1
	 */
	public ClassificatoreEP getClassificatoreEP1() {
		return classificatoreEP1;
	}

	/**
	 * @param classificatoreEP1 the classificatoreEP1 to set
	 */
	public void setClassificatoreEP1(ClassificatoreEP classificatoreEP1) {
		this.classificatoreEP1 = classificatoreEP1;
	}

	/**
	 * @return the classificatoreEP2
	 */
	public ClassificatoreEP getClassificatoreEP2() {
		return classificatoreEP2;
	}

	/**
	 * @param classificatoreEP2 the classificatoreEP2 to set
	 */
	public void setClassificatoreEP2(ClassificatoreEP classificatoreEP2) {
		this.classificatoreEP2 = classificatoreEP2;
	}

	/**
	 * @return the classificatoreEP3
	 */
	public ClassificatoreEP getClassificatoreEP3() {
		return classificatoreEP3;
	}

	/**
	 * @param classificatoreEP3 the classificatoreEP3 to set
	 */
	public void setClassificatoreEP3(ClassificatoreEP classificatoreEP3) {
		this.classificatoreEP3 = classificatoreEP3;
	}

	/**
	 * @return the classificatoreEP4
	 */
	public ClassificatoreEP getClassificatoreEP4() {
		return classificatoreEP4;
	}

	/**
	 * @param classificatoreEP4 the classificatoreEP4 to set
	 */
	public void setClassificatoreEP4(ClassificatoreEP classificatoreEP4) {
		this.classificatoreEP4 = classificatoreEP4;
	}

	/**
	 * @return the classificatoreEP5
	 */
	public ClassificatoreEP getClassificatoreEP5() {
		return classificatoreEP5;
	}

	/**
	 * @param classificatoreEP5 the classificatoreEP5 to set
	 */
	public void setClassificatoreEP5(ClassificatoreEP classificatoreEP5) {
		this.classificatoreEP5 = classificatoreEP5;
	}

	/**
	 * @return the classificatoreEP6
	 */
	public ClassificatoreEP getClassificatoreEP6() {
		return classificatoreEP6;
	}

	/**
	 * @param classificatoreEP6 the classificatoreEP6 to set
	 */
	public void setClassificatoreEP6(ClassificatoreEP classificatoreEP6) {
		this.classificatoreEP6 = classificatoreEP6;
	}

	/**
	 * @return the listaClassi
	 */
	public final List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public final void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public final List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public final void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public final List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public final void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public final List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public final void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}

	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	/**
	 * @return the urlStep1
	 */
	public String getUrlStep1() {
		return getBaseUrl() + "_completeStep1";
	}
	
	/**
	 * @return the urlStep2
	 */
	public String getUrlStep2() {
		return getBaseUrl() + "_completeStep2";
	}
	
	/**
	 * @return the urlAnnullaStep1
	 */
	public String getUrlAnnullaStep1() {
		return getBaseUrl() + "_annullaStep1.do";
	}
	
	/**
	 * @return the urlAnnullaStep2
	 */
	public String getUrlAnnullaStep2() {
		return getBaseUrl() + "_annullaStep2.do";
	}
	
	/**
	 * @return the urlBackToStep1
	 */
	public String getUrlBackToStep1() {
		return getBaseUrl() + "_backToStep1.do";
	}
	
	/**
	 * @return the denominazioneWizard
	 */
	public abstract String getDenominazioneWizard();
	
	/**
	 * @return the aggiornamento
	 */
	public abstract boolean isAggiornamento();
	
	/**
	 * @return the causaleStessoAnnoBilancio
	 */
	public boolean isCausaleStessoAnnoBilancio() {
		// Se no ho il campo, allora considero 'false'
		if(getCausaleEP() == null || getCausaleEP().getDataCreazione() == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCausaleEP().getDataCreazione());
		int annoCausaleEP = cal.get(Calendar.YEAR);
		return annoCausaleEP == getAnnoEsercizioInt().intValue();
	}
	
	/**
	 * @return the tipoCausaleDiRaccordo
	 */
	public boolean isTipoCausaleDiRaccordo() {
		return getCausaleEP() != null && TipoCausale.Integrata.equals(getCausaleEP().getTipoCausale());
	}
	
	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		StringBuilder sb = new StringBuilder();
		if(getSoggetto() != null && StringUtils.isNotBlank(getSoggetto().getCodiceSoggetto())) {
			sb.append(": ")
				.append(getSoggetto().getCognome())
				.append(" ")
				.append(getSoggetto().getNome());
		}
		return sb.toString();
	}
	
	/**
	 * @return the numeroClassificatoriEP
	 */
	public int getNumeroClassificatoriEP() {
		return 6;
	}
	
	/**
	 * @return the tipoCausaleIntegrata
	 */
	public String getTipoCausaleIntegrata() {
		return TipoCausale.Integrata.name();
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param conto il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setBilancio(getBilancio());
		request.setConto(conto);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		return creaRequestRicercaSoggettoPerChiave(getSoggetto());
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @param sogg il soggetto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto sogg) {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(sogg.getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}

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
	
	/**
	 * Crea una request per il servizio di {@link LeggiElementoPianoDeiContiByCodiceAndAnno}.
	 *
	 * @param pianoDeiConti l'elemento per cui effettuare la ricerca
	 * @return la request creata
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(ElementoPianoDeiConti pianoDeiConti) {
		LeggiElementoPianoDeiContiByCodiceAndAnno request = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setElementoPianoDeiConti(pianoDeiConti);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaClassificatoriEP}.
	 *
	 * @return la request creata
	 */
	public RicercaClassificatoriEP creaRequestRicercaClassificatoriEP() {
		return creaRequest(RicercaClassificatoriEP.class);
	}
	
	/* **** Utilita **** */
	
	/**
	 * Aggiunge l'entita alla collection qualora sia non-null e abbia uid valorizzato.
	 * 
	 * @param collection la collection da popolare
	 * @param value      il valore da impostare
	 * @param <E>        la tipizzazione dell'entita
	 * @param <S>        la sottoclasse di entita
	 */
	protected <E extends Entita, S extends E> void addIfNotNullNorInvalidUid(Collection<E> collection, S value) {
		if(value != null && value.getUid() != 0) {
			collection.add(value);
		}
	}
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		return creaRequestRicercaCodifiche("ClassePiano_" + getAmbito().getSuffix());
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCausale creaRequestRicercaDettaglioCausale() {
		RicercaDettaglioCausale request = creaRequest(RicercaDettaglioCausale.class);
		
		request.setCausaleEP(getCausaleEP());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Ottiene la data del primo gennaio dell'anno fornito.
	 * 
	 * @param anno l'anno da considerare
	 * @return l'oggetto Date corrispondente al primo gennaio dell'anno da considerare
	 */
	public static Date getPrimoGennaio(Integer anno) {
		if(anno == null) {
			throw new IllegalArgumentException("Illegal Argument for anno: cannot be null");
		}
		Calendar cal = Calendar.getInstance();
		cal.set(anno.intValue(), Calendar.JANUARY, 1, 0, 0, 0);
		return cal.getTime();
	}
}
