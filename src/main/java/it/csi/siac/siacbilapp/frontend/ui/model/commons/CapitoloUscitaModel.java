/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamento;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.RisorsaAccantonata;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Superclasse per il model del Capitolo di Uscita.
 * <br>
 * Si occupa di definire i campi e i metodi comuni per le actions del Capitolo di Uscita, con attenzione a:
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
public abstract class CapitoloUscitaModel extends CapitoloModel {
	
	/** Per la serializzazione  */
	private static final long serialVersionUID = 5795279638816969800L;

	//SIAC-7192
	private static final String CODICE_MISSIONE_FONDI = "20";
	
	/* Prima maschera: dati di base */
	private Missione             missione;
	private Programma            programma;
	private ClassificazioneCofog classificazioneCofog;
	private TitoloSpesa          titoloSpesa;
	private Macroaggregato       macroaggregato;
	
	/* Liste */
	private List<Missione> 	listaMissione    = new ArrayList<Missione>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	/* Liste derivate */
	private List<Programma> 			 listaProgramma            = new ArrayList<Programma>();
	private List<ClassificazioneCofog> listaClassificazioneCofog = new ArrayList<ClassificazioneCofog>();
	private List<Macroaggregato> 		 listaMacroaggregato       = new ArrayList<Macroaggregato>();
	
	/* Booleani rappresentanti i campi, per la editabilita' */
	private boolean missioneEditabile;
	private boolean programmaEditabile;
	private boolean classificazioneCofogEditabile;
	private boolean titoloSpesaEditabile;
	private boolean macroaggregatoEditabile;
	
	// Classificatori fase 4
	private SiopeSpesa                    siopeSpesa;
	private RicorrenteSpesa               ricorrenteSpesa;
	private PerimetroSanitarioSpesa       perimetroSanitarioSpesa;
	private TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesa;
	private PoliticheRegionaliUnitarie    politicheRegionaliUnitarie;
	
	private String siopeInserito;
	
	private List<SiopeSpesa>                    listaSiopeSpesa                    = new ArrayList<SiopeSpesa>();
	private List<RicorrenteSpesa>               listaRicorrenteSpesa               = new ArrayList<RicorrenteSpesa>();
	private List<PerimetroSanitarioSpesa>       listaPerimetroSanitarioSpesa       = new ArrayList<PerimetroSanitarioSpesa>();
	private List<TransazioneUnioneEuropeaSpesa> listaTransazioneUnioneEuropeaSpesa = new ArrayList<TransazioneUnioneEuropeaSpesa>();
	private List<PoliticheRegionaliUnitarie>    listaPoliticheRegionaliUnitarie    = new ArrayList<PoliticheRegionaliUnitarie>();
	
	private boolean siopeSpesaEditabile;
	private boolean ricorrenteSpesaEditabile;
	private boolean perimetroSanitarioSpesaEditabile;
	private boolean transazioneUnioneEuropeaSpesaEditabile;
	private boolean politicheRegionaliUnitarieEditabile;
	private boolean flagFunzioniDelegateRegioneEditabile;
	
	//SIAC-7192
	private List<RisorsaAccantonata> listaRisorsaAccantonata = new ArrayList<RisorsaAccantonata>();
	private RisorsaAccantonata risorsaAccantonata;
	//SIAC-8214
	//SIAC-XXXX
	private List<RigaComponenteTabellaImportiCapitolo> righeComponentiTabellaImportiCapitolo = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	private List<RigaImportoTabellaImportiCapitolo> righeImportiTabellaImportiCapitolo = new ArrayList<RigaImportoTabellaImportiCapitolo>();
	private List<RigaComponenteTabellaImportiCapitolo> righeDisponibilitaImpegnareComponenti = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	private List<RigaComponenteTabellaImportiCapitolo> righeDisponibilitaVariareComponenti = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	
	//SIAC-8517
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
	
	
	/**
	 * @return the missione
	 */
	public Missione getMissione() {
		return missione;
	}
	/**
	 * @param missione the missione to set
	 */
	public void setMissione(Missione missione) {
		this.missione = missione;
	}
	/**
	 * @return the programma
	 */
	public Programma getProgramma() {
		return programma;
	}
	/**
	 * @param programma the programma to set
	 */
	public void setProgramma(Programma programma) {
		this.programma = programma;
	}
	/**
	 * @return the classificazioneCofog
	 */
	public ClassificazioneCofog getClassificazioneCofog() {
		return classificazioneCofog;
	}
	/**
	 * @param classificazioneCofog the classificazioneCofog to set
	 */
	public void setClassificazioneCofog(ClassificazioneCofog classificazioneCofog) {
		this.classificazioneCofog = classificazioneCofog;
	}
	/**
	 * @return the titoloSpesa
	 */
	public TitoloSpesa getTitoloSpesa() {
		return titoloSpesa;
	}
	/**
	 * @param titoloSpesa the titoloSpesa to set
	 */
	public void setTitoloSpesa(TitoloSpesa titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}
	/**
	 * @return the macroaggregato
	 */
	public Macroaggregato getMacroaggregato() {
		return macroaggregato;
	}
	/**
	 * @param macroaggregato the macroaggregato to set
	 */
	public void setMacroaggregato(Macroaggregato macroaggregato) {
		this.macroaggregato = macroaggregato;
	}
	/**
	 * @return the listaMissione
	 */
	public List<Missione> getListaMissione() {
		return listaMissione;
	}
	/**
	 * @param listaMissione the listaMissione to set
	 */
	public void setListaMissione(List<Missione> listaMissione) {
		this.listaMissione = listaMissione;
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
		this.listaTitoloSpesa = listaTitoloSpesa;
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
		this.listaProgramma = listaProgramma;
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
		this.listaClassificazioneCofog = listaClassificazioneCofog;
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
		this.listaMacroaggregato = listaMacroaggregato;
	}
	/**
	 * @return the missioneEditabile
	 */
	public boolean isMissioneEditabile() {
		return missioneEditabile;
	}
	/**
	 * @param missioneEditabile the missioneEditabile to set
	 */
	public void setMissioneEditabile(boolean missioneEditabile) {
		this.missioneEditabile = missioneEditabile;
	}
	/**
	 * @return the programmaEditabile
	 */
	public boolean isProgrammaEditabile() {
		return programmaEditabile;
	}
	/**
	 * @param programmaEditabile the programmaEditabile to set
	 */
	public void setProgrammaEditabile(boolean programmaEditabile) {
		this.programmaEditabile = programmaEditabile;
	}
	/**
	 * @return the classificazioneCofogEditabile
	 */
	public boolean isClassificazioneCofogEditabile() {
		return classificazioneCofogEditabile;
	}
	/**
	 * @param classificazioneCofogEditabile the classificazioneCofogEditabile to set
	 */
	public void setClassificazioneCofogEditabile(
			boolean classificazioneCofogEditabile) {
		this.classificazioneCofogEditabile = classificazioneCofogEditabile;
	}
	/**
	 * @return the titoloSpesaEditabile
	 */
	public boolean isTitoloSpesaEditabile() {
		return titoloSpesaEditabile;
	}
	/**
	 * @param titoloSpesaEditabile the titoloSpesaEditabile to set
	 */
	public void setTitoloSpesaEditabile(boolean titoloSpesaEditabile) {
		this.titoloSpesaEditabile = titoloSpesaEditabile;
	}
	/**
	 * @return the macroaggregatoEditabile
	 */
	public boolean isMacroaggregatoEditabile() {
		return macroaggregatoEditabile;
	}
	/**
	 * @param macroaggregatoEditabile the macroaggregatoEditabile to set
	 */
	public void setMacroaggregatoEditabile(boolean macroaggregatoEditabile) {
		this.macroaggregatoEditabile = macroaggregatoEditabile;
	}
	
	/**
	 * @return the siopeSpesa
	 */
	public SiopeSpesa getSiopeSpesa() {
		return siopeSpesa;
	}
	/**
	 * @param siopeSpesa the siopeSpesa to set
	 */
	public void setSiopeSpesa(SiopeSpesa siopeSpesa) {
		this.siopeSpesa = siopeSpesa;
	}
	/**
	 * @return the ricorrenteSpesa
	 */
	public RicorrenteSpesa getRicorrenteSpesa() {
		return ricorrenteSpesa;
	}
	/**
	 * @param ricorrenteSpesa the ricorrenteSpesa to set
	 */
	public void setRicorrenteSpesa(RicorrenteSpesa ricorrenteSpesa) {
		this.ricorrenteSpesa = ricorrenteSpesa;
	}
	/**
	 * @return the perimetroSanitarioSpesa
	 */
	public PerimetroSanitarioSpesa getPerimetroSanitarioSpesa() {
		return perimetroSanitarioSpesa;
	}
	/**
	 * @param perimetroSanitarioSpesa the perimetroSanitarioSpesa to set
	 */
	public void setPerimetroSanitarioSpesa(
			PerimetroSanitarioSpesa perimetroSanitarioSpesa) {
		this.perimetroSanitarioSpesa = perimetroSanitarioSpesa;
	}
	/**
	 * @return the transazioneUnioneEuropeaSpesa
	 */
	public TransazioneUnioneEuropeaSpesa getTransazioneUnioneEuropeaSpesa() {
		return transazioneUnioneEuropeaSpesa;
	}
	/**
	 * @param transazioneUnioneEuropeaSpesa the transazioneUnioneEuropeaSpesa to set
	 */
	public void setTransazioneUnioneEuropeaSpesa(
			TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesa) {
		this.transazioneUnioneEuropeaSpesa = transazioneUnioneEuropeaSpesa;
	}
	/**
	 * @return the politicheRegionaliUnitarie
	 */
	public PoliticheRegionaliUnitarie getPoliticheRegionaliUnitarie() {
		return politicheRegionaliUnitarie;
	}
	/**
	 * @param politicheRegionaliUnitarie the politicheRegionaliUnitarie to set
	 */
	public void setPoliticheRegionaliUnitarie(
			PoliticheRegionaliUnitarie politicheRegionaliUnitarie) {
		this.politicheRegionaliUnitarie = politicheRegionaliUnitarie;
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
	 * @return the listaSiopeSpesa
	 */
	public List<SiopeSpesa> getListaSiopeSpesa() {
		return listaSiopeSpesa;
	}
	/**
	 * @param listaSiopeSpesa the listaSiopeSpesa to set
	 */
	public void setListaSiopeSpesa(List<SiopeSpesa> listaSiopeSpesa) {
		this.listaSiopeSpesa = listaSiopeSpesa;
	}
	/**
	 * @return the listaRicorrenteSpesa
	 */
	public List<RicorrenteSpesa> getListaRicorrenteSpesa() {
		return listaRicorrenteSpesa;
	}
	/**
	 * @param listaRicorrenteSpesa the listaRicorrenteSpesa to set
	 */
	public void setListaRicorrenteSpesa(List<RicorrenteSpesa> listaRicorrenteSpesa) {
		this.listaRicorrenteSpesa = listaRicorrenteSpesa;
	}
	/**
	 * @return the listaPerimetroSanitarioSpesa
	 */
	public List<PerimetroSanitarioSpesa> getListaPerimetroSanitarioSpesa() {
		return listaPerimetroSanitarioSpesa;
	}
	/**
	 * @param listaPerimetroSanitarioSpesa the listaPerimetroSanitarioSpesa to set
	 */
	public void setListaPerimetroSanitarioSpesa(
			List<PerimetroSanitarioSpesa> listaPerimetroSanitarioSpesa) {
		this.listaPerimetroSanitarioSpesa = listaPerimetroSanitarioSpesa;
	}
	/**
	 * @return the listaTransazioneUnioneEuropeaSpesa
	 */
	public List<TransazioneUnioneEuropeaSpesa> getListaTransazioneUnioneEuropeaSpesa() {
		return listaTransazioneUnioneEuropeaSpesa;
	}
	/**
	 * @param listaTransazioneUnioneEuropeaSpesa the listaTransazioneUnioneEuropeaSpesa to set
	 */
	public void setListaTransazioneUnioneEuropeaSpesa(
			List<TransazioneUnioneEuropeaSpesa> listaTransazioneUnioneEuropeaSpesa) {
		this.listaTransazioneUnioneEuropeaSpesa = listaTransazioneUnioneEuropeaSpesa;
	}
	/**
	 * @return the listaPoliticheRegionaliUnitarie
	 */
	public List<PoliticheRegionaliUnitarie> getListaPoliticheRegionaliUnitarie() {
		return listaPoliticheRegionaliUnitarie;
	}
	/**
	 * @param listaPoliticheRegionaliUnitarie the listaPoliticheRegionaliUnitarie to set
	 */
	public void setListaPoliticheRegionaliUnitarie(
			List<PoliticheRegionaliUnitarie> listaPoliticheRegionaliUnitarie) {
		this.listaPoliticheRegionaliUnitarie = listaPoliticheRegionaliUnitarie;
	}
	/**
	 * @return the siopeSpesaEditabile
	 */
	public boolean isSiopeSpesaEditabile() {
		return siopeSpesaEditabile;
	}
	/**
	 * @param siopeSpesaEditabile the siopeSpesaEditabile to set
	 */
	public void setSiopeSpesaEditabile(boolean siopeSpesaEditabile) {
		this.siopeSpesaEditabile = siopeSpesaEditabile;
	}
	/**
	 * @return the ricorrenteSpesaEditabile
	 */
	public boolean isRicorrenteSpesaEditabile() {
		return ricorrenteSpesaEditabile;
	}
	/**
	 * @param ricorrenteSpesaEditabile the ricorrenteSpesaEditabile to set
	 */
	public void setRicorrenteSpesaEditabile(boolean ricorrenteSpesaEditabile) {
		this.ricorrenteSpesaEditabile = ricorrenteSpesaEditabile;
	}
	/**
	 * @return the perimetroSanitarioSpesaEditabile
	 */
	public boolean isPerimetroSanitarioSpesaEditabile() {
		return perimetroSanitarioSpesaEditabile;
	}
	/**
	 * @param perimetroSanitarioSpesaEditabile the perimetroSanitarioSpesaEditabile to set
	 */
	public void setPerimetroSanitarioSpesaEditabile(
			boolean perimetroSanitarioSpesaEditabile) {
		this.perimetroSanitarioSpesaEditabile = perimetroSanitarioSpesaEditabile;
	}
	/**
	 * @return the transazioneUnioneEuropeaSpesaEditabile
	 */
	public boolean isTransazioneUnioneEuropeaSpesaEditabile() {
		return transazioneUnioneEuropeaSpesaEditabile;
	}
	/**
	 * @param transazioneUnioneEuropeaSpesaEditabile the transazioneUnioneEuropeaSpesaEditabile to set
	 */
	public void setTransazioneUnioneEuropeaSpesaEditabile(
			boolean transazioneUnioneEuropeaSpesaEditabile) {
		this.transazioneUnioneEuropeaSpesaEditabile = transazioneUnioneEuropeaSpesaEditabile;
	}
	/**
	 * @return the politicheRegionaliUnitarieEditabile
	 */
	public boolean isPoliticheRegionaliUnitarieEditabile() {
		return politicheRegionaliUnitarieEditabile;
	}
	/**
	 * @param politicheRegionaliUnitarieEditabile the politicheRegionaliUnitarieEditabile to set
	 */
	public void setPoliticheRegionaliUnitarieEditabile(
			boolean politicheRegionaliUnitarieEditabile) {
		this.politicheRegionaliUnitarieEditabile = politicheRegionaliUnitarieEditabile;
	}
	/**
	 * @return the flagFunzioniDelegateRegioneEditabile
	 */
	public boolean isFlagFunzioniDelegateRegioneEditabile() {
		return flagFunzioniDelegateRegioneEditabile;
	}
	/**
	 * @param flagFunzioniDelegateRegioneEditabile the flagFunzioniDelegateRegioneEditabile to set
	 */
	public void setFlagFunzioniDelegateRegioneEditabile(
			boolean flagFunzioniDelegateRegioneEditabile) {
		this.flagFunzioniDelegateRegioneEditabile = flagFunzioniDelegateRegioneEditabile;
	}
	
	
	
	public ClassificatoreGenerico getClassificatoreGenerico1() {
		return classificatoreGenerico1;
	}
	public void setClassificatoreGenerico1(ClassificatoreGenerico classificatoreGenerico1) {
		this.classificatoreGenerico1 = classificatoreGenerico1;
	}
	public ClassificatoreGenerico getClassificatoreGenerico2() {
		return classificatoreGenerico2;
	}
	public void setClassificatoreGenerico2(ClassificatoreGenerico classificatoreGenerico2) {
		this.classificatoreGenerico2 = classificatoreGenerico2;
	}
	public ClassificatoreGenerico getClassificatoreGenerico3() {
		return classificatoreGenerico3;
	}
	public void setClassificatoreGenerico3(ClassificatoreGenerico classificatoreGenerico3) {
		this.classificatoreGenerico3 = classificatoreGenerico3;
	}
	public ClassificatoreGenerico getClassificatoreGenerico4() {
		return classificatoreGenerico4;
	}
	public void setClassificatoreGenerico4(ClassificatoreGenerico classificatoreGenerico4) {
		this.classificatoreGenerico4 = classificatoreGenerico4;
	}
	public ClassificatoreGenerico getClassificatoreGenerico5() {
		return classificatoreGenerico5;
	}
	public void setClassificatoreGenerico5(ClassificatoreGenerico classificatoreGenerico5) {
		this.classificatoreGenerico5 = classificatoreGenerico5;
	}
	public ClassificatoreGenerico getClassificatoreGenerico6() {
		return classificatoreGenerico6;
	}
	public void setClassificatoreGenerico6(ClassificatoreGenerico classificatoreGenerico6) {
		this.classificatoreGenerico6 = classificatoreGenerico6;
	}
	public ClassificatoreGenerico getClassificatoreGenerico7() {
		return classificatoreGenerico7;
	}
	public void setClassificatoreGenerico7(ClassificatoreGenerico classificatoreGenerico7) {
		this.classificatoreGenerico7 = classificatoreGenerico7;
	}
	public ClassificatoreGenerico getClassificatoreGenerico8() {
		return classificatoreGenerico8;
	}
	public void setClassificatoreGenerico8(ClassificatoreGenerico classificatoreGenerico8) {
		this.classificatoreGenerico8 = classificatoreGenerico8;
	}
	public ClassificatoreGenerico getClassificatoreGenerico9() {
		return classificatoreGenerico9;
	}
	public void setClassificatoreGenerico9(ClassificatoreGenerico classificatoreGenerico9) {
		this.classificatoreGenerico9 = classificatoreGenerico9;
	}
	public ClassificatoreGenerico getClassificatoreGenerico10() {
		return classificatoreGenerico10;
	}
	public void setClassificatoreGenerico10(ClassificatoreGenerico classificatoreGenerico10) {
		this.classificatoreGenerico10 = classificatoreGenerico10;
	}
	public ClassificatoreGenerico getClassificatoreGenerico11() {
		return classificatoreGenerico11;
	}
	public void setClassificatoreGenerico11(ClassificatoreGenerico classificatoreGenerico11) {
		this.classificatoreGenerico11 = classificatoreGenerico11;
	}
	public ClassificatoreGenerico getClassificatoreGenerico12() {
		return classificatoreGenerico12;
	}
	public void setClassificatoreGenerico12(ClassificatoreGenerico classificatoreGenerico12) {
		this.classificatoreGenerico12 = classificatoreGenerico12;
	}
	public ClassificatoreGenerico getClassificatoreGenerico13() {
		return classificatoreGenerico13;
	}
	public void setClassificatoreGenerico13(ClassificatoreGenerico classificatoreGenerico13) {
		this.classificatoreGenerico13 = classificatoreGenerico13;
	}
	public ClassificatoreGenerico getClassificatoreGenerico14() {
		return classificatoreGenerico14;
	}
	public void setClassificatoreGenerico14(ClassificatoreGenerico classificatoreGenerico14) {
		this.classificatoreGenerico14 = classificatoreGenerico14;
	}
	public ClassificatoreGenerico getClassificatoreGenerico15() {
		return classificatoreGenerico15;
	}
	public void setClassificatoreGenerico15(ClassificatoreGenerico classificatoreGenerico15) {
		this.classificatoreGenerico15 = classificatoreGenerico15;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico1() {
		return listaClassificatoreGenerico1;
	}
	public void setListaClassificatoreGenerico1(List<ClassificatoreGenerico> listaClassificatoreGenerico1) {
		this.listaClassificatoreGenerico1 = listaClassificatoreGenerico1;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico2() {
		return listaClassificatoreGenerico2;
	}
	public void setListaClassificatoreGenerico2(List<ClassificatoreGenerico> listaClassificatoreGenerico2) {
		this.listaClassificatoreGenerico2 = listaClassificatoreGenerico2;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico3() {
		return listaClassificatoreGenerico3;
	}
	public void setListaClassificatoreGenerico3(List<ClassificatoreGenerico> listaClassificatoreGenerico3) {
		this.listaClassificatoreGenerico3 = listaClassificatoreGenerico3;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico4() {
		return listaClassificatoreGenerico4;
	}
	public void setListaClassificatoreGenerico4(List<ClassificatoreGenerico> listaClassificatoreGenerico4) {
		this.listaClassificatoreGenerico4 = listaClassificatoreGenerico4;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico5() {
		return listaClassificatoreGenerico5;
	}
	public void setListaClassificatoreGenerico5(List<ClassificatoreGenerico> listaClassificatoreGenerico5) {
		this.listaClassificatoreGenerico5 = listaClassificatoreGenerico5;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico6() {
		return listaClassificatoreGenerico6;
	}
	public void setListaClassificatoreGenerico6(List<ClassificatoreGenerico> listaClassificatoreGenerico6) {
		this.listaClassificatoreGenerico6 = listaClassificatoreGenerico6;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico7() {
		return listaClassificatoreGenerico7;
	}
	public void setListaClassificatoreGenerico7(List<ClassificatoreGenerico> listaClassificatoreGenerico7) {
		this.listaClassificatoreGenerico7 = listaClassificatoreGenerico7;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico8() {
		return listaClassificatoreGenerico8;
	}
	public void setListaClassificatoreGenerico8(List<ClassificatoreGenerico> listaClassificatoreGenerico8) {
		this.listaClassificatoreGenerico8 = listaClassificatoreGenerico8;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico9() {
		return listaClassificatoreGenerico9;
	}
	public void setListaClassificatoreGenerico9(List<ClassificatoreGenerico> listaClassificatoreGenerico9) {
		this.listaClassificatoreGenerico9 = listaClassificatoreGenerico9;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico10() {
		return listaClassificatoreGenerico10;
	}
	public void setListaClassificatoreGenerico10(List<ClassificatoreGenerico> listaClassificatoreGenerico10) {
		this.listaClassificatoreGenerico10 = listaClassificatoreGenerico10;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico11() {
		return listaClassificatoreGenerico11;
	}
	public void setListaClassificatoreGenerico11(List<ClassificatoreGenerico> listaClassificatoreGenerico11) {
		this.listaClassificatoreGenerico11 = listaClassificatoreGenerico11;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico12() {
		return listaClassificatoreGenerico12;
	}
	public void setListaClassificatoreGenerico12(List<ClassificatoreGenerico> listaClassificatoreGenerico12) {
		this.listaClassificatoreGenerico12 = listaClassificatoreGenerico12;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico13() {
		return listaClassificatoreGenerico13;
	}
	public void setListaClassificatoreGenerico13(List<ClassificatoreGenerico> listaClassificatoreGenerico13) {
		this.listaClassificatoreGenerico13 = listaClassificatoreGenerico13;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico14() {
		return listaClassificatoreGenerico14;
	}
	public void setListaClassificatoreGenerico14(List<ClassificatoreGenerico> listaClassificatoreGenerico14) {
		this.listaClassificatoreGenerico14 = listaClassificatoreGenerico14;
	}
	public List<ClassificatoreGenerico> getListaClassificatoreGenerico15() {
		return listaClassificatoreGenerico15;
	}
	public void setListaClassificatoreGenerico15(List<ClassificatoreGenerico> listaClassificatoreGenerico15) {
		this.listaClassificatoreGenerico15 = listaClassificatoreGenerico15;
	}
	public String getLabelClassificatoreGenerico1() {
		return labelClassificatoreGenerico1;
	}
	public void setLabelClassificatoreGenerico1(String labelClassificatoreGenerico1) {
		this.labelClassificatoreGenerico1 = labelClassificatoreGenerico1;
	}
	public String getLabelClassificatoreGenerico2() {
		return labelClassificatoreGenerico2;
	}
	public void setLabelClassificatoreGenerico2(String labelClassificatoreGenerico2) {
		this.labelClassificatoreGenerico2 = labelClassificatoreGenerico2;
	}
	public String getLabelClassificatoreGenerico3() {
		return labelClassificatoreGenerico3;
	}
	public void setLabelClassificatoreGenerico3(String labelClassificatoreGenerico3) {
		this.labelClassificatoreGenerico3 = labelClassificatoreGenerico3;
	}
	public String getLabelClassificatoreGenerico4() {
		return labelClassificatoreGenerico4;
	}
	public void setLabelClassificatoreGenerico4(String labelClassificatoreGenerico4) {
		this.labelClassificatoreGenerico4 = labelClassificatoreGenerico4;
	}
	public String getLabelClassificatoreGenerico5() {
		return labelClassificatoreGenerico5;
	}
	public void setLabelClassificatoreGenerico5(String labelClassificatoreGenerico5) {
		this.labelClassificatoreGenerico5 = labelClassificatoreGenerico5;
	}
	public String getLabelClassificatoreGenerico6() {
		return labelClassificatoreGenerico6;
	}
	public void setLabelClassificatoreGenerico6(String labelClassificatoreGenerico6) {
		this.labelClassificatoreGenerico6 = labelClassificatoreGenerico6;
	}
	public String getLabelClassificatoreGenerico7() {
		return labelClassificatoreGenerico7;
	}
	public void setLabelClassificatoreGenerico7(String labelClassificatoreGenerico7) {
		this.labelClassificatoreGenerico7 = labelClassificatoreGenerico7;
	}
	public String getLabelClassificatoreGenerico8() {
		return labelClassificatoreGenerico8;
	}
	public void setLabelClassificatoreGenerico8(String labelClassificatoreGenerico8) {
		this.labelClassificatoreGenerico8 = labelClassificatoreGenerico8;
	}
	public String getLabelClassificatoreGenerico9() {
		return labelClassificatoreGenerico9;
	}
	public void setLabelClassificatoreGenerico9(String labelClassificatoreGenerico9) {
		this.labelClassificatoreGenerico9 = labelClassificatoreGenerico9;
	}
	public String getLabelClassificatoreGenerico10() {
		return labelClassificatoreGenerico10;
	}
	public void setLabelClassificatoreGenerico10(String labelClassificatoreGenerico10) {
		this.labelClassificatoreGenerico10 = labelClassificatoreGenerico10;
	}
	public String getLabelClassificatoreGenerico11() {
		return labelClassificatoreGenerico11;
	}
	public void setLabelClassificatoreGenerico11(String labelClassificatoreGenerico11) {
		this.labelClassificatoreGenerico11 = labelClassificatoreGenerico11;
	}
	public String getLabelClassificatoreGenerico12() {
		return labelClassificatoreGenerico12;
	}
	public void setLabelClassificatoreGenerico12(String labelClassificatoreGenerico12) {
		this.labelClassificatoreGenerico12 = labelClassificatoreGenerico12;
	}
	public String getLabelClassificatoreGenerico13() {
		return labelClassificatoreGenerico13;
	}
	public void setLabelClassificatoreGenerico13(String labelClassificatoreGenerico13) {
		this.labelClassificatoreGenerico13 = labelClassificatoreGenerico13;
	}
	public String getLabelClassificatoreGenerico14() {
		return labelClassificatoreGenerico14;
	}
	public void setLabelClassificatoreGenerico14(String labelClassificatoreGenerico14) {
		this.labelClassificatoreGenerico14 = labelClassificatoreGenerico14;
	}
	public String getLabelClassificatoreGenerico15() {
		return labelClassificatoreGenerico15;
	}
	public void setLabelClassificatoreGenerico15(String labelClassificatoreGenerico15) {
		this.labelClassificatoreGenerico15 = labelClassificatoreGenerico15;
	}
	public boolean isClassificatoreGenerico1Editabile() {
		return classificatoreGenerico1Editabile;
	}
	public void setClassificatoreGenerico1Editabile(boolean classificatoreGenerico1Editabile) {
		this.classificatoreGenerico1Editabile = classificatoreGenerico1Editabile;
	}
	public boolean isClassificatoreGenerico2Editabile() {
		return classificatoreGenerico2Editabile;
	}
	public void setClassificatoreGenerico2Editabile(boolean classificatoreGenerico2Editabile) {
		this.classificatoreGenerico2Editabile = classificatoreGenerico2Editabile;
	}
	public boolean isClassificatoreGenerico3Editabile() {
		return classificatoreGenerico3Editabile;
	}
	public void setClassificatoreGenerico3Editabile(boolean classificatoreGenerico3Editabile) {
		this.classificatoreGenerico3Editabile = classificatoreGenerico3Editabile;
	}
	public boolean isClassificatoreGenerico4Editabile() {
		return classificatoreGenerico4Editabile;
	}
	public void setClassificatoreGenerico4Editabile(boolean classificatoreGenerico4Editabile) {
		this.classificatoreGenerico4Editabile = classificatoreGenerico4Editabile;
	}
	public boolean isClassificatoreGenerico5Editabile() {
		return classificatoreGenerico5Editabile;
	}
	public void setClassificatoreGenerico5Editabile(boolean classificatoreGenerico5Editabile) {
		this.classificatoreGenerico5Editabile = classificatoreGenerico5Editabile;
	}
	public boolean isClassificatoreGenerico6Editabile() {
		return classificatoreGenerico6Editabile;
	}
	public void setClassificatoreGenerico6Editabile(boolean classificatoreGenerico6Editabile) {
		this.classificatoreGenerico6Editabile = classificatoreGenerico6Editabile;
	}
	public boolean isClassificatoreGenerico7Editabile() {
		return classificatoreGenerico7Editabile;
	}
	public void setClassificatoreGenerico7Editabile(boolean classificatoreGenerico7Editabile) {
		this.classificatoreGenerico7Editabile = classificatoreGenerico7Editabile;
	}
	public boolean isClassificatoreGenerico8Editabile() {
		return classificatoreGenerico8Editabile;
	}
	public void setClassificatoreGenerico8Editabile(boolean classificatoreGenerico8Editabile) {
		this.classificatoreGenerico8Editabile = classificatoreGenerico8Editabile;
	}
	public boolean isClassificatoreGenerico9Editabile() {
		return classificatoreGenerico9Editabile;
	}
	public void setClassificatoreGenerico9Editabile(boolean classificatoreGenerico9Editabile) {
		this.classificatoreGenerico9Editabile = classificatoreGenerico9Editabile;
	}
	public boolean isClassificatoreGenerico10Editabile() {
		return classificatoreGenerico10Editabile;
	}
	public void setClassificatoreGenerico10Editabile(boolean classificatoreGenerico10Editabile) {
		this.classificatoreGenerico10Editabile = classificatoreGenerico10Editabile;
	}
	public boolean isClassificatoreGenerico11Editabile() {
		return classificatoreGenerico11Editabile;
	}
	public void setClassificatoreGenerico11Editabile(boolean classificatoreGenerico11Editabile) {
		this.classificatoreGenerico11Editabile = classificatoreGenerico11Editabile;
	}
	public boolean isClassificatoreGenerico12Editabile() {
		return classificatoreGenerico12Editabile;
	}
	public void setClassificatoreGenerico12Editabile(boolean classificatoreGenerico12Editabile) {
		this.classificatoreGenerico12Editabile = classificatoreGenerico12Editabile;
	}
	public boolean isClassificatoreGenerico13Editabile() {
		return classificatoreGenerico13Editabile;
	}
	public void setClassificatoreGenerico13Editabile(boolean classificatoreGenerico13Editabile) {
		this.classificatoreGenerico13Editabile = classificatoreGenerico13Editabile;
	}
	public boolean isClassificatoreGenerico14Editabile() {
		return classificatoreGenerico14Editabile;
	}
	public void setClassificatoreGenerico14Editabile(boolean classificatoreGenerico14Editabile) {
		this.classificatoreGenerico14Editabile = classificatoreGenerico14Editabile;
	}
	public boolean isClassificatoreGenerico15Editabile() {
		return classificatoreGenerico15Editabile;
	}
	public void setClassificatoreGenerico15Editabile(boolean classificatoreGenerico15Editabile) {
		this.classificatoreGenerico15Editabile = classificatoreGenerico15Editabile;
	}
	/**
	 * @return the listaRisorsaAccantonata
	 */
	public List<RisorsaAccantonata> getListaRisorsaAccantonata() {
		return listaRisorsaAccantonata;
	}
	/**
	 * @param listaRisorsaAccantonata the listaRisorsaAccantonata to set
	 */
	public void setListaRisorsaAccantonata(List<RisorsaAccantonata> listaRisorsaAccantonata) {
		this.listaRisorsaAccantonata = listaRisorsaAccantonata != null? listaRisorsaAccantonata : new ArrayList<RisorsaAccantonata>();
	}
	
	/**
	 * @return the risorsaAccantonata
	 */
	public RisorsaAccantonata getRisorsaAccantonata() {
		return risorsaAccantonata;
	}
	/**
	 * @param risorsaAccantonata the risorsaAccantonata to set
	 */
	public void setRisorsaAccantonata(RisorsaAccantonata risorsaAccantonata) {
		this.risorsaAccantonata = risorsaAccantonata;
	}
	
	public List<RigaComponenteTabellaImportiCapitolo> getRigheComponentiTabellaImportiCapitolo() {
		return righeComponentiTabellaImportiCapitolo;
	}

	public void setRigheComponentiTabellaImportiCapitolo(
			List<RigaComponenteTabellaImportiCapitolo> righeComponentiTabellaImportiCapitolo) {
		this.righeComponentiTabellaImportiCapitolo = righeComponentiTabellaImportiCapitolo;
	}

	public List<RigaImportoTabellaImportiCapitolo> getRigheImportiTabellaImportiCapitolo() {
		return righeImportiTabellaImportiCapitolo;
	}

	public void setRigheImportiTabellaImportiCapitolo(
			List<RigaImportoTabellaImportiCapitolo> righeImportiTabellaImportiCapitolo) {
		this.righeImportiTabellaImportiCapitolo = righeImportiTabellaImportiCapitolo;
	}
	
	
	public List<RigaComponenteTabellaImportiCapitolo> getRigheDisponibilitaImpegnareComponenti() {
		return righeDisponibilitaImpegnareComponenti;
	}
	public void setRigheDisponibilitaImpegnareComponenti(List<RigaComponenteTabellaImportiCapitolo> righeDisponibilitaImpegnare) {
		this.righeDisponibilitaImpegnareComponenti = righeDisponibilitaImpegnare;
	}
	public List<RigaComponenteTabellaImportiCapitolo> getRigheDisponibilitaVariareComponenti() {
		return righeDisponibilitaVariareComponenti;
	}
	public void setRigheDisponibilitaVariareComponenti(List<RigaComponenteTabellaImportiCapitolo> righeDisponibilitaVariare) {
		this.righeDisponibilitaVariareComponenti = righeDisponibilitaVariare;
	}
	/**
	 * Checks if is visualizza disavanzo da debito.
	 *
	 * @return true, if is visualizza disavanzo da debito
	 */
	public boolean isMissioneFondi() {
		if(missione == null || missione.getUid() == 0) {
			return false;
		}
		String codiceMissione = missione.getCodice();
		if(StringUtils.isEmpty(codiceMissione)) {
			Missione missioneConDati = ComparatorUtils.searchByUid(getListaMissione(), missione);
			codiceMissione = missioneConDati != null? missioneConDati.getCodice() : "";
		}
		return CODICE_MISSIONE_FONDI.equals(codiceMissione);
	}
	/**
	 * @return the numeroClassificatoriGenerici
	 */
	public int getNumeroClassificatoriGenerici() {
		return NUMERO_CLASSIFICATORI_GENERICI;
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
				TipologiaClassificatore.CLASSIFICATORE_1,
				TipologiaClassificatore.CLASSIFICATORE_2,
				TipologiaClassificatore.CLASSIFICATORE_3,
				TipologiaClassificatore.CLASSIFICATORE_4,
				TipologiaClassificatore.CLASSIFICATORE_5,
				TipologiaClassificatore.CLASSIFICATORE_6,
				TipologiaClassificatore.CLASSIFICATORE_7,
				TipologiaClassificatore.CLASSIFICATORE_8,
				TipologiaClassificatore.CLASSIFICATORE_9,
				TipologiaClassificatore.CLASSIFICATORE_10,
				TipologiaClassificatore.CLASSIFICATORE_31,
				TipologiaClassificatore.CLASSIFICATORE_32,
				TipologiaClassificatore.CLASSIFICATORE_33,
				TipologiaClassificatore.CLASSIFICATORE_34,
				TipologiaClassificatore.CLASSIFICATORE_35));
		req.setTipoElementoBilancio(tipoCapitolo);
		
		return req;
	}
	
	/**
	 * Crea una nuova Classificazione Cofog Programma a partire dalla Classificazione Cofog presente nel Model.
	 * 
	 * @param classificazioneCofog la classificazione Cofog
	 * 
	 * @return la Classificazione creata
	 */
	protected ClassificazioneCofogProgramma getClassificazioneCofogProgramma(ClassificazioneCofog classificazioneCofog) {
		if(classificazioneCofog == null || classificazioneCofog.getUid() == 0) {
			return null;
		}
		
		// Wrapper per la Classificazione Cofog
		ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
		
		classificazioneCofogProgramma.setCodice(classificazioneCofog.getCodice());
		classificazioneCofogProgramma.setDescrizione(classificazioneCofog.getDescrizione());
		classificazioneCofogProgramma.setStato(classificazioneCofog.getStato());
		classificazioneCofogProgramma.setUid(classificazioneCofog.getUid());
		// Per la cancellazione
		classificazioneCofogProgramma.setDataFineValidita(classificazioneCofog.getDataFineValidita());
		
		return classificazioneCofogProgramma;
	}
	
	@Override
	public void caricaClassificatoriDaSessione(SessionHandler sessionHandler) {
		// Richiamo il padre per i classificatori standard
		super.caricaClassificatoriDaSessione(sessionHandler);
		
		missione = caricaClassificatoriDaSessione(sessionHandler, missione, BilSessionParameter.LISTA_MISSIONE);
		programma = caricaClassificatoriDaSessione(sessionHandler, programma, BilSessionParameter.LISTA_PROGRAMMA);
		classificazioneCofog = caricaClassificatoriDaSessione(sessionHandler, classificazioneCofog, BilSessionParameter.LISTA_CLASSIFICAZIONE_COFOG);
		titoloSpesa = caricaClassificatoriDaSessione(sessionHandler, titoloSpesa, BilSessionParameter.LISTA_TITOLO_SPESA_ORIGINALE);
		macroaggregato = caricaClassificatoriDaSessione(sessionHandler, macroaggregato, BilSessionParameter.LISTA_MACROAGGREGATO);
		
		// Nuovi classificatori
		ricorrenteSpesa = caricaClassificatoriDaSessione(sessionHandler, ricorrenteSpesa, BilSessionParameter.LISTA_RICORRENTE_SPESA);
		perimetroSanitarioSpesa = caricaClassificatoriDaSessione(sessionHandler, perimetroSanitarioSpesa, BilSessionParameter.LISTA_PERIMETRO_SANITARIO_SPESA);
		transazioneUnioneEuropeaSpesa = caricaClassificatoriDaSessione(sessionHandler, transazioneUnioneEuropeaSpesa, BilSessionParameter.LISTA_TRANSAZIONE_UNIONE_EUROPEA_SPESA);
		politicheRegionaliUnitarie = caricaClassificatoriDaSessione(sessionHandler, politicheRegionaliUnitarie, BilSessionParameter.LISTA_POLITICHE_REGIONALI_UNITARIE);
		
		//SIAC-7192
		risorsaAccantonata = caricaClassificatoriDaSessione(sessionHandler, risorsaAccantonata, BilSessionParameter.LISTA_RISORSA_ACCANTONATA);
		
		siopeSpesa = caricaClassificatoriDaSessione(sessionHandler, siopeSpesa, BilSessionParameter.LISTA_SIOPE_SPESA);
		if(siopeSpesa != null && siopeSpesa.getUid() != 0 && StringUtils.isBlank(siopeInserito)) {
			siopeInserito = getCodiceEDescrizione(siopeSpesa);
		}
	}
	
	@Override
	protected void caricaClassificatoriGenericiDaSessione(SessionHandler sessionHandler) {
		setClassificatoreGenerico1(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico1(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_1));
		setClassificatoreGenerico2(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico2(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_2));
		setClassificatoreGenerico3(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico3(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_3));
		setClassificatoreGenerico4(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico4(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_4));
		setClassificatoreGenerico5(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico5(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_5));
		setClassificatoreGenerico6(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico6(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_6));
		setClassificatoreGenerico7(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico7(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_7));
		setClassificatoreGenerico8(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico8(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_8));
		setClassificatoreGenerico9(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico9(),  BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_9));
		setClassificatoreGenerico10(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico10(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_10));
		setClassificatoreGenerico11(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico11(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_31));
		setClassificatoreGenerico12(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico12(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_32));
		setClassificatoreGenerico13(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico13(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_33));
		setClassificatoreGenerico14(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico14(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_34));
		setClassificatoreGenerico15(caricaClassificatoriDaSessione(sessionHandler, getClassificatoreGenerico15(), BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_35));
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
	protected SiopeSpesa caricaClassificatoriDaSessione(SessionHandler sessionHandler, SiopeSpesa classificatore, BilSessionParameter nomeClassificatore) {
		List<SiopeSpesa> lista = sessionHandler.getParametro(nomeClassificatore);
		return ComparatorUtils.searchByUidWithChildren(lista, classificatore);
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
		
		// Controllo i classificatori definiti per il capitolo di uscita
		missioneEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.MISSIONE);
		programmaEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.PROGRAMMA);
		classificazioneCofogEditabile = isEditabile(unico, isMassivo, response,TipologiaClassificatore.CLASSE_COFOG, TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.GRUPPO_COFOG);
		titoloSpesaEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TITOLO_SPESA);
		macroaggregatoEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.MACROAGGREGATO);
		
		siopeSpesaEditabile = isEditabile(unico, isMassivo, response,TipologiaClassificatore.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I,TipologiaClassificatore.SIOPE_SPESA_II, TipologiaClassificatore.SIOPE_SPESA_III);
		ricorrenteSpesaEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.RICORRENTE_SPESA);
		perimetroSanitarioSpesaEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA);
		transazioneUnioneEuropeaSpesaEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.TRANSAZIONE_UE_SPESA);
		politicheRegionaliUnitarieEditabile = isEditabile(unico, isMassivo, response, TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE);
		//SIAC-7192
	}
	
	@Override
	protected void isClassificatoriGenericiEditabile(boolean unico, boolean isMassivo, ControllaClassificatoriModificabiliCapitoloResponse response) {
		setClassificatoreGenerico1Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_1));
		setClassificatoreGenerico2Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_2));
		setClassificatoreGenerico3Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_3));
		setClassificatoreGenerico4Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_4));
		setClassificatoreGenerico5Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_5));
		setClassificatoreGenerico6Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_6));
		setClassificatoreGenerico7Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_7));
		setClassificatoreGenerico8Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_8));
		setClassificatoreGenerico9Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_9));
		setClassificatoreGenerico10Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_10));
		setClassificatoreGenerico11Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_31));
		setClassificatoreGenerico12Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_32));
		setClassificatoreGenerico13Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_33));
		setClassificatoreGenerico14Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_34));
		setClassificatoreGenerico15Editabile(isEditabile(unico, isMassivo, response, TipologiaClassificatore.CLASSIFICATORE_35));
	}
	
	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		
		long stessoNumCapArt = response.getStessoNumCapArt() != null ? response.getStessoNumCapArt().longValue() : 0L;
		boolean unicoArticolo = stessoNumCapArt <= 1L;
		
		flagFunzioniDelegateRegioneEditabile = isEditabile(unicoArticolo, isMassivo, response, TipologiaAttributo.FLAG_FUNZIONI_DELEGATE);
	}
	
	/**
	 * Reimposta i dati disabilitati causa non editabilit&agrave; nel model.
	 * 
	 * @param classificatoreAggiornamento i classificatori originali in sessione
	 */
	public void setParametriDisabilitati(ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento) {
		super.setParametriDisabilitati(classificatoreAggiornamento);
		
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
		
		missione = impostaIlDato(missioneEditabile, missione, classificatoreAggiornamento.getMissione());
		programma = impostaIlDato(programmaEditabile, programma, classificatoreAggiornamento.getProgramma());
		classificazioneCofog = impostaIlDato(classificazioneCofogEditabile, classificazioneCofog, classificatoreAggiornamento.getClassificazioneCofog());
		titoloSpesa = impostaIlDato(titoloSpesaEditabile, titoloSpesa, classificatoreAggiornamento.getTitoloSpesa());
		macroaggregato = impostaIlDato(macroaggregatoEditabile, macroaggregato, classificatoreAggiornamento.getMacroaggregato());
		
		siopeSpesa = impostaIlDato(siopeSpesaEditabile, siopeSpesa, classificatoreAggiornamento.getSiopeSpesa());
		ricorrenteSpesa = impostaIlDato(ricorrenteSpesaEditabile, ricorrenteSpesa, classificatoreAggiornamento.getRicorrenteSpesa());
		perimetroSanitarioSpesa = impostaIlDato(perimetroSanitarioSpesaEditabile, perimetroSanitarioSpesa, classificatoreAggiornamento.getPerimetroSanitarioSpesa());
		transazioneUnioneEuropeaSpesa = impostaIlDato(transazioneUnioneEuropeaSpesaEditabile, transazioneUnioneEuropeaSpesa, classificatoreAggiornamento.getTransazioneUnioneEuropeaSpesa());
		politicheRegionaliUnitarie = impostaIlDato(politicheRegionaliUnitarieEditabile, politicheRegionaliUnitarie, classificatoreAggiornamento.getPoliticheRegionaliUnitarie());
	}
	
	@Override
	protected void valorizzaStringheUtilita() {
		super.valorizzaStringheUtilita();
		if(siopeSpesa != null) {
			siopeInserito = getCodiceEDescrizione(siopeSpesa);
		} else {
			// Valore di default
			siopeInserito = "Nessun SIOPE selezionato";
		}
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
	protected List<ClassificatoreGenerico> getListaClassificatoriGenericiAggiornamento(ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento) {
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
	 * Metodo di utilit&agrave; per impostare i classificatori generici a partire da una lista.
	 * 
	 * @param listaClassificatoriGenerici la lista di classificatori generici da cui popolare il model
	 */
	protected void impostaClassificatoriGenericiDaLista(List<ClassificatoreGenerico> listaClassificatoriGenerici) {
		setClassificatoreGenerico1(null);
		setClassificatoreGenerico2(null);
		setClassificatoreGenerico3(null);
		setClassificatoreGenerico4(null);
		setClassificatoreGenerico5(null);
		setClassificatoreGenerico6(null);
		setClassificatoreGenerico7(null);
		setClassificatoreGenerico8(null);
		setClassificatoreGenerico9(null);
		setClassificatoreGenerico10(null);
		setClassificatoreGenerico11(null);
		setClassificatoreGenerico12(null);
		setClassificatoreGenerico13(null);
		setClassificatoreGenerico14(null);
		setClassificatoreGenerico15(null);
		
		for(ClassificatoreGenerico classificatoreGenerico : listaClassificatoriGenerici) {
			TipoClassificatore tipoClassificatore = classificatoreGenerico.getTipoClassificatore();
			
			if(tipoClassificatore == null || tipoClassificatore.getCodice() == null) {
				// Il tipo di classificatore per il Classificatore Generico non e' valido
				continue;
			}
			
			if(BilConstants.CODICE_CLASSIFICATORE1.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico1(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE2.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico2(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE3.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico3(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE4.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico4(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE5.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico5(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE6.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico6(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE7.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico7(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE8.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico8(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE9.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico9(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE10.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico10(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE31.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico11(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE32.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico12(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE33.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico13(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE34.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico14(classificatoreGenerico);
			} else if(BilConstants.CODICE_CLASSIFICATORE35.getConstant().equals(tipoClassificatore.getCodice())) {
				setClassificatoreGenerico15(classificatoreGenerico);
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
		setLabelClassificatoreGenerico1(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_1)));
		setLabelClassificatoreGenerico2(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_2)));
		setLabelClassificatoreGenerico3(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_3)));
		setLabelClassificatoreGenerico4(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_4)));
		setLabelClassificatoreGenerico5(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_5)));
		setLabelClassificatoreGenerico6(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_6)));
		setLabelClassificatoreGenerico7(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_7)));
		setLabelClassificatoreGenerico8(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_8)));
		setLabelClassificatoreGenerico9(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_9)));
		setLabelClassificatoreGenerico10(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_10)));
		setLabelClassificatoreGenerico11(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_31)));
		setLabelClassificatoreGenerico12(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_32)));
		setLabelClassificatoreGenerico13(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_33)));
		setLabelClassificatoreGenerico14(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_34)));
		setLabelClassificatoreGenerico15(ottieniLabelClassificatoreGenerico((List<ClassificatoreGenerico>) sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_GENERICO_35)));
	}
	
}
