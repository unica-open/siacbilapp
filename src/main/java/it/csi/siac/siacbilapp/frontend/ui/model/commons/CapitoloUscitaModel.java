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
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
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

	/** Il numero dei classificatori generici */
	private static final int NUMERO_CLASSIFICATORI_GENERICI = 15;
	
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
