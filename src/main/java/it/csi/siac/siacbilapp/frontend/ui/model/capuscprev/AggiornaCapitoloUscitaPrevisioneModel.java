/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.RisorsaAccantonata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Uscita Previsione. Contiene una mappatura dei form per l'aggiornamento del
 * Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.2.0 17/09/2013 - Aggiunta la modularit&agrave; a livello di campo
 *
 */
public class AggiornaCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5535104743908839717L;
			
	/* Terza maschera: importi */
	private ImportiCapitoloUG importiEx;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione0;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione1;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	private Boolean decentrato;
	
	// SIAC-4308
	private boolean exAnnoEditabile;
	private boolean exCapitoloEditabile;
	private boolean exArticoloEditabile;
	private boolean exUEBEditabile;
	
	
	//SIAC-6884 Per Discriminare le componenti di default
	private boolean capitoloFondino = false;
	//SIAC-8256
	private boolean stanziamentiNegativiPresenti;
	private boolean presentiComponentiNonFresco;
	private List<TipoComponenteImportiCapitolo> listaTipoComponentiDefault = new ArrayList<TipoComponenteImportiCapitolo>();

	

	

	/** Costruttore vuoto di default */
	public AggiornaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Spesa Previsione");
	}

	/**
	 * @return the importiEx
	 */
	public ImportiCapitoloUG getImportiEx() {
		return importiEx;
	}

	/**
	 * @param importiEx the importiEx to set
	 */
	public void setImportiEx(ImportiCapitoloUG importiEx) {
		this.importiEx = importiEx;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione0
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione0() {
		return importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione0 the importiCapitoloUscitaPrevisione0 to set
	 */
	public void setImportiCapitoloUscitaPrevisione0(ImportiCapitoloUP importiCapitoloUscitaPrevisione0) {
		this.importiCapitoloUscitaPrevisione0 = importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione1
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione1() {
		return importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione1 the importiCapitoloUscitaPrevisione1 to set
	 */
	public void setImportiCapitoloUscitaPrevisione1(ImportiCapitoloUP importiCapitoloUscitaPrevisione1) {
		this.importiCapitoloUscitaPrevisione1 = importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione2
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione2() {
		return importiCapitoloUscitaPrevisione2;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione2 the importiCapitoloUscitaPrevisione2 to set
	 */
	public void setImportiCapitoloUscitaPrevisione2(ImportiCapitoloUP importiCapitoloUscitaPrevisione2) {
		this.importiCapitoloUscitaPrevisione2 = importiCapitoloUscitaPrevisione2;
	}

	/**
	 * @return the capitoloUscitaPrevisione
	 */
	public CapitoloUscitaPrevisione getCapitoloUscitaPrevisione() {
		return capitoloUscitaPrevisione;
	}

	/**
	 * @param capitoloUscitaPrevisione the capitoloUscitaPrevisione to set
	 */
	public void setCapitoloUscitaPrevisione(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		this.capitoloUscitaPrevisione = capitoloUscitaPrevisione;
	}
	
	/**
	 * @return the decentrato
	 */
	public Boolean getDecentrato() {
		return decentrato;
	}

	/**
	 * @param decentrato the decentrato to set
	 */
	public void setDecentrato(Boolean decentrato) {
		this.decentrato = decentrato;
	}

	/**
	 * @return the exAnnoEditabile
	 */
	public boolean isExAnnoEditabile() {
		return exAnnoEditabile;
	}

	/**
	 * @param exAnnoEditabile the exAnnoEditabile to set
	 */
	public void setExAnnoEditabile(boolean exAnnoEditabile) {
		this.exAnnoEditabile = exAnnoEditabile;
	}

	/**
	 * @return the exCapitoloEditabile
	 */
	public boolean isExCapitoloEditabile() {
		return exCapitoloEditabile;
	}

	/**
	 * @param exCapitoloEditabile the exCapitoloEditabile to set
	 */
	public void setExCapitoloEditabile(boolean exCapitoloEditabile) {
		this.exCapitoloEditabile = exCapitoloEditabile;
	}

	/**
	 * @return the exArticoloEditabile
	 */
	public boolean isExArticoloEditabile() {
		return exArticoloEditabile;
	}

	/**
	 * @param exArticoloEditabile the exArticoloEditabile to set
	 */
	public void setExArticoloEditabile(boolean exArticoloEditabile) {
		this.exArticoloEditabile = exArticoloEditabile;
	}

	/**
	 * @return the exUEBEditabile
	 */
	public boolean isExUEBEditabile() {
		return exUEBEditabile;
	}

	/**
	 * @param exUEBEditabile the exUEBEditabile to set
	 */
	public void setExUEBEditabile(boolean exUEBEditabile) {
		this.exUEBEditabile = exUEBEditabile;
	}

	/**
	 * Ottiene l'uid del capitolo da aggiornare.
	 * 
	 * @return l'uid del Capitolo di Uscita Previsione da aggiornare
	 */
	public int getUidDaAggiornare() {
		return capitoloUscitaPrevisione == null ? 0 : capitoloUscitaPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Uscita Previsione da Aggiornare.
	 * 
	 * @param uidDaAggiornare l'uid da impostare
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		if(capitoloUscitaPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setUid(uidDaAggiornare);
	}
	

	//SIAC-6884
	public boolean isCapitoloFondino() {
		return capitoloFondino;
	}

	public void setCapitoloFondino(boolean capitoloFondino) {
		this.capitoloFondino = capitoloFondino;
	}
	
	
	
	public boolean isStanziamentiNegativiPresenti() {
		return stanziamentiNegativiPresenti;
	}

	public void setStanziamentiNegativiPresenti(boolean stanziamentiNegativiPresenti) {
		this.stanziamentiNegativiPresenti = stanziamentiNegativiPresenti;
	}

	public boolean isPresentiComponentiNonFresco() {
		return presentiComponentiNonFresco;
	}

	public void setPresentiComponentiNonFresco(boolean presentiComponentiNonFresco) {
		this.presentiComponentiNonFresco = presentiComponentiNonFresco;
	}

	
	public List<TipoComponenteImportiCapitolo> getListaTipoComponentiDefault() {
		return listaTipoComponentiDefault;
	}

	public void setListaTipoComponentiDefault(List<TipoComponenteImportiCapitolo> listaTipoComponentiDefault) {
		this.listaTipoComponentiDefault = listaTipoComponentiDefault;
	}

	public RicercaTipoComponenteImportiCapitoloPerCapitolo creaRicercaTipoComponenteImportiCapitoloPerCapitolo() {
		RicercaTipoComponenteImportiCapitoloPerCapitolo request = creaRequest(RicercaTipoComponenteImportiCapitoloPerCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(getUidDaAggiornare());
		request.setSoloValidiPerBilancio(true);
		return request;
	}
	/* Requests */
	/**
	 * Restituisce una Request di tipo {@link AggiornaCapitoloDiUscitaPrevisione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaCapitoloDiUscitaPrevisione creaRequestAggiornaCapitoloDiUscitaPrevisione(ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento) {
		AggiornaCapitoloDiUscitaPrevisione request = creaRequest(AggiornaCapitoloDiUscitaPrevisione.class);

		capitoloUscitaPrevisione.setAnnoCapitolo(getBilancio().getAnno());
		
		// Le seguenti entita sono le entita di base obbligatorie
		
		// Modularizzazione dei campi
		Missione missioneDaInjettare = valutaInserimento(getMissione(), classificatoreAggiornamento.getMissione(), isMissioneEditabile());
		Programma programmaDaInjettare = valutaInserimento(getProgramma(), classificatoreAggiornamento.getProgramma(), isProgrammaEditabile());
		TitoloSpesa titoloSpesaDaInjettare = valutaInserimento(getTitoloSpesa(), classificatoreAggiornamento.getTitoloSpesa(), isTitoloSpesaEditabile());
		Macroaggregato macroaggregatoDaInjettare = valutaInserimento(getMacroaggregato(), classificatoreAggiornamento.getMacroaggregato(), isMacroaggregatoEditabile());
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = valutaInserimento(getElementoPianoDeiConti(), classificatoreAggiornamento.getElementoPianoDeiConti(), isElementoPianoDeiContiEditabile());
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = valutaInserimento(getStrutturaAmministrativoContabile(), classificatoreAggiornamento.getStrutturaAmministrativoContabile(), isStrutturaAmministrativoContabileEditabile());
		// Controllo che i classificatori siano cambiati
		ClassificazioneCofog classificazioneCofogDaInjettare = valutaInserimento(getClassificazioneCofog(), classificatoreAggiornamento.getClassificazioneCofog(), isClassificazioneCofogEditabile());
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimento(getTipoFinanziamento(), classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimento(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(), isTipoFondoEditabile());
		
		SiopeSpesa siopeSpesaDaInjettare = valutaInserimento(getSiopeSpesa(), classificatoreAggiornamento.getSiopeSpesa(), isSiopeSpesaEditabile());
		RicorrenteSpesa ricorrenteSpesaDaInjettare = valutaInserimento(getRicorrenteSpesa(), classificatoreAggiornamento.getRicorrenteSpesa(), isRicorrenteSpesaEditabile());
		PerimetroSanitarioSpesa perimetroSanitarioSpesaDaInjettare = valutaInserimento(getPerimetroSanitarioSpesa(), classificatoreAggiornamento.getPerimetroSanitarioSpesa(), isPerimetroSanitarioSpesaEditabile());
		TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesaDaInjettare = valutaInserimento(getTransazioneUnioneEuropeaSpesa(), classificatoreAggiornamento.getTransazioneUnioneEuropeaSpesa(), isTransazioneUnioneEuropeaSpesaEditabile());
		PoliticheRegionaliUnitarie politicheRegionaliUnitarieDaInjettare = valutaInserimento(getPoliticheRegionaliUnitarie(), classificatoreAggiornamento.getPoliticheRegionaliUnitarie(), isPoliticheRegionaliUnitarieEditabile());
		RisorsaAccantonata risorsaAccantonataDaInjettare = valutaInserimento(getRisorsaAccantonata(), classificatoreAggiornamento.getRisorsaAccantonata(), isMissioneEditabile());
		
		
		// Injezione dei classificatori
		capitoloUscitaPrevisione.setBilancio(getBilancio());
		capitoloUscitaPrevisione.setEnte(getEnte());
		capitoloUscitaPrevisione.setMissione(missioneDaInjettare);
		capitoloUscitaPrevisione.setProgramma(programmaDaInjettare);
		capitoloUscitaPrevisione.setTitoloSpesa(titoloSpesaDaInjettare);
		capitoloUscitaPrevisione.setMacroaggregato(macroaggregatoDaInjettare);
		capitoloUscitaPrevisione.setElementoPianoDeiConti(elementoPianoDeiContiDaInjettare);
		capitoloUscitaPrevisione.setStrutturaAmministrativoContabile(strutturaAmministrativoContabileDaInjettare);
		capitoloUscitaPrevisione.setClassificazioneCofogProgramma(getClassificazioneCofogProgramma(classificazioneCofogDaInjettare));
		capitoloUscitaPrevisione.setTipoFinanziamento(tipoFinanziamentoDaInjettare);
		capitoloUscitaPrevisione.setTipoFondo(tipoFondoDaInjettare);
		capitoloUscitaPrevisione.setSiopeSpesa(siopeSpesaDaInjettare);
		capitoloUscitaPrevisione.setRicorrenteSpesa(ricorrenteSpesaDaInjettare);
		capitoloUscitaPrevisione.setPerimetroSanitarioSpesa(perimetroSanitarioSpesaDaInjettare);
		capitoloUscitaPrevisione.setTransazioneUnioneEuropeaSpesa(transazioneUnioneEuropeaSpesaDaInjettare);
		capitoloUscitaPrevisione.setPoliticheRegionaliUnitarie(politicheRegionaliUnitarieDaInjettare);
		
		capitoloUscitaPrevisione.setRisorsaAccantonata(risorsaAccantonataDaInjettare);
		
		capitoloUscitaPrevisione.setListaImportiCapitoloUP(getListaImportiCapitolo());
		capitoloUscitaPrevisione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloUscitaPrevisione creaRequestRicercaVariazioniCapitoloUscitaPrevisione() {
		RicercaVariazioniCapitoloUscitaPrevisione request = creaRequest(RicercaVariazioniCapitoloUscitaPrevisione.class);
		
		capitoloUscitaPrevisione.setBilancio(getBilancio());
		capitoloUscitaPrevisione.setEnte(getEnte());
		
		request.setCapitoloUscitaPrev(capitoloUscitaPrevisione);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione() {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUPrev(getRicercaDettaglioCapitoloUPrev(capitoloUscitaPrevisione.getUid()));
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @param response	la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloUscitaPrevisioneResponse response) {
		
		/* Ottenuti dalla response */
		capitoloUscitaPrevisione = response.getCapitoloUscitaPrevisione();
		
		setBilancio(capitoloUscitaPrevisione.getBilancio());
		setTipoFinanziamento(capitoloUscitaPrevisione.getTipoFinanziamento());
		setTipoFondo(capitoloUscitaPrevisione.getTipoFondo());
		
		if(capitoloUscitaPrevisione.getListaImportiCapitoloUP() != null) {
			// Dovrebbe essere sempre non-null
			try {
				importiCapitoloUscitaPrevisione0 = capitoloUscitaPrevisione.getListaImportiCapitoloUP().get(0);
				importiCapitoloUscitaPrevisione1 = capitoloUscitaPrevisione.getListaImportiCapitoloUP().get(1);
				importiCapitoloUscitaPrevisione2 = capitoloUscitaPrevisione.getListaImportiCapitoloUP().get(2);
			} catch(IndexOutOfBoundsException e) {
				// Ignoro l'eccezione
			}
		}
		importiEx = capitoloUscitaPrevisione.getImportiCapitoloEquivalente();
		
		impostaClassificatoriGenericiDaLista(response.getListaClassificatori());
		
		/* Ottenuti dalla response, injettati nel capitoloUscitaPrevisione */
		setClassificazioneCofog(capitoloUscitaPrevisione.getClassificazioneCofog());
		setElementoPianoDeiConti(capitoloUscitaPrevisione.getElementoPianoDeiConti());
		setMacroaggregato(capitoloUscitaPrevisione.getMacroaggregato());
		setMissione(capitoloUscitaPrevisione.getMissione());
		setProgramma(capitoloUscitaPrevisione.getProgramma());
		setStrutturaAmministrativoContabile(capitoloUscitaPrevisione.getStrutturaAmministrativoContabile());
		setTitoloSpesa(capitoloUscitaPrevisione.getTitoloSpesa());
		
		setSiopeSpesa(capitoloUscitaPrevisione.getSiopeSpesa());
		setRicorrenteSpesa(capitoloUscitaPrevisione.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitoloUscitaPrevisione.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitoloUscitaPrevisione.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitoloUscitaPrevisione.getPoliticheRegionaliUnitarie());
		//SIAC-7192
		setRisorsaAccantonata(capitoloUscitaPrevisione.getRisorsaAccantonata());
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */
		valorizzaStringheUtilita();
	}
	
	/**
	 * Costruisce la lista degli Importi Capitolo Uscita Previsione a partire dagli importi del Model.
	 * 
	 * @return la lista creata
	 */
	private List<ImportiCapitoloUP> getListaImportiCapitolo() {
		List<ImportiCapitoloUP> lista = new ArrayList<ImportiCapitoloUP>();
		
		// Carica gli importi di ogni anno
//		addImportoCapitoloALista(lista, importiCapitoloUscitaPrevisione0, getAnnoEsercizioInt() + 0);
//		addImportoCapitoloALista(lista, importiCapitoloUscitaPrevisione1, getAnnoEsercizioInt() + 1);
//		addImportoCapitoloALista(lista, importiCapitoloUscitaPrevisione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
		
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @param chiaveCapitolo la chiave univoca del capitolo
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev getRicercaDettaglioCapitoloUPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	@Override
	public void valutaModificabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response, boolean isMassivo) {
		// SIAC-4308: controllo modificabilita' per il decentrato
		if(Boolean.TRUE.equals(getDecentrato())) {
			// Non abilitare nulla
			
			setMissioneEditabile(false);
			setProgrammaEditabile(false);
			setClassificazioneCofogEditabile(false);
			setTitoloSpesaEditabile(false);
			setMacroaggregatoEditabile(false);
			setElementoPianoDeiContiEditabile(false);
			setSiopeSpesaEditabile(false);
			setStrutturaAmministrativoContabileEditabile(false);
			setTipoFinanziamentoEditabile(false);
			setTipoFondoEditabile(false);
			setRicorrenteSpesaEditabile(false);
			setPerimetroSanitarioSpesaEditabile(false);
			setTransazioneUnioneEuropeaSpesaEditabile(false);
			setPoliticheRegionaliUnitarieEditabile(false);
			setClassificatoreGenerico1Editabile(false);
			setClassificatoreGenerico2Editabile(false);
			setClassificatoreGenerico3Editabile(false);
			setClassificatoreGenerico4Editabile(false);
			setClassificatoreGenerico5Editabile(false);
			setClassificatoreGenerico6Editabile(false);
			setClassificatoreGenerico7Editabile(false);
			setClassificatoreGenerico8Editabile(false);
			setClassificatoreGenerico9Editabile(false);
			setClassificatoreGenerico10Editabile(false);
			setClassificatoreGenerico11Editabile(false);
			setClassificatoreGenerico12Editabile(false);
			setClassificatoreGenerico13Editabile(false);
			setClassificatoreGenerico14Editabile(false);
			setClassificatoreGenerico15Editabile(false);
		} else {
			super.valutaModificabilitaClassificatori(response, isMassivo);
		}
	}
	
	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		// SIAC-4308: controllo modificabilita' per il decentrato
		if(Boolean.TRUE.equals(getDecentrato())) {
			// Non abilitare nulla
			setCategoriaCapitoloEditabile(false);
			setDescrizioneEditabile(false);
			setDescrizioneArticoloEditabile(false);
			setFlagRilevanteIvaEditabile(false);
			setNoteEditabile(false);
			setFlagImpegnabileEditabile(false);
			setFlagFunzioniDelegateRegioneEditabile(false);
			setFlagPerMemoriaEditabile(false);
		} else {
			super.valutaModificabilitaAttributi(response, isMassivo);
			
			setExAnnoEditabile(true);
			setExCapitoloEditabile(true);
			setExArticoloEditabile(true);
			setExUEBEditabile(true);
		}
	}

	//SIAC-8256
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(capitoloUscitaPrevisione.getUid());
		return request;
	}
	
	

}
