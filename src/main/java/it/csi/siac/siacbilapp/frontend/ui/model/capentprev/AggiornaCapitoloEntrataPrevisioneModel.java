/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Entrata Previsione. Contiene una mappatura dei form per l'inserimento del
 * Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
public class AggiornaCapitoloEntrataPrevisioneModel extends CapitoloEntrataPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5944107128924905793L;
	
	/* Terza maschera: importi */
	// Anno in corso - 1
	private ImportiCapitoloEG importiEx;
	// Anno in corso
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione0;
	// Anno in corso + 1
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione1;
	// Anno in corso + 2
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	private Boolean decentrato;
	
	// SIAC-4308
	private boolean exAnnoEditabile;
	private boolean exCapitoloEditabile;
	private boolean exArticoloEditabile;
	private boolean exUEBEditabile;
	
	// SIAC-5582
	private boolean flagAccertatoPerCassaEditabile;
	
	/** Costruttore vuoto di default */
	public AggiornaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Entrata Previsione");
	}
	
	/**
	 * @return the importiEx
	 */
	public ImportiCapitoloEG getImportiEx() {
		return importiEx;
	}

	/**
	 * @param importiEx the importiEx to set
	 */
	public void setImportiEx(ImportiCapitoloEG importiEx) {
		this.importiEx = importiEx;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione0
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione0() {
		return importiCapitoloEntrataPrevisione0;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione0 the importiCapitoloEntrataPrevisione0 to set
	 */
	public void setImportiCapitoloEntrataPrevisione0(ImportiCapitoloEP importiCapitoloEntrataPrevisione0) {
		this.importiCapitoloEntrataPrevisione0 = importiCapitoloEntrataPrevisione0;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione1
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione1() {
		return importiCapitoloEntrataPrevisione1;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione1 the importiCapitoloEntrataPrevisione1 to set
	 */
	public void setImportiCapitoloEntrataPrevisione1(ImportiCapitoloEP importiCapitoloEntrataPrevisione1) {
		this.importiCapitoloEntrataPrevisione1 = importiCapitoloEntrataPrevisione1;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione2
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione2() {
		return importiCapitoloEntrataPrevisione2;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione2 the importiCapitoloEntrataPrevisione2 to set
	 */
	public void setImportiCapitoloEntrataPrevisione2(ImportiCapitoloEP importiCapitoloEntrataPrevisione2) {
		this.importiCapitoloEntrataPrevisione2 = importiCapitoloEntrataPrevisione2;
	}

	/**
	 * @return the capitoloEntrataPrevisione
	 */
	public CapitoloEntrataPrevisione getCapitoloEntrataPrevisione() {
		return capitoloEntrataPrevisione;
	}

	/**
	 * @param capitoloEntrataPrevisione the capitoloEntrataPrevisione to set
	 */
	public void setCapitoloEntrataPrevisione(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		this.capitoloEntrataPrevisione = capitoloEntrataPrevisione;
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
	 * @return the flagAccertatoPerCassaEditabile
	 */
	public boolean isFlagAccertatoPerCassaEditabile() {
		return this.flagAccertatoPerCassaEditabile;
	}

	/**
	 * @param flagAccertatoPerCassaEditabile the flagAccertatoPerCassaEditabile to set
	 */
	public void setFlagAccertatoPerCassaEditabile(boolean flagAccertatoPerCassaEditabile) {
		this.flagAccertatoPerCassaEditabile = flagAccertatoPerCassaEditabile;
	}

	/* Uid per l'aggiornamento */

	/**
	 * Restituisce l'uid per l'aggiornamento.
	 * 
	 * @return	l'uid per l'aggiornamento
	 */
	public int getUidDaAggiornare() {
		return capitoloEntrataPrevisione == null ? 0 : capitoloEntrataPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid per l'aggiornamento.
	 * 
	 * @param uidDaAggiornare l'uid da impostare
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setUid(uidDaAggiornare);
	}

	/* ************ Requests ************ */
	
	/**
	 * Restituisce una Request di tipo {@link AggiornaCapitoloDiEntrataPrevisione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaCapitoloDiEntrataPrevisione creaRequestAggiornaCapitoloDiEntrataPrevisione
			(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		AggiornaCapitoloDiEntrataPrevisione request = new AggiornaCapitoloDiEntrataPrevisione();
		
		capitoloEntrataPrevisione.setAnnoCapitolo(getBilancio().getAnno());
		
		// I seguenti dati sono i dati di base obbligatori
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		// Modularizzazione dei campi
		TitoloEntrata titoloEntrataDaInjettare = valutaInserimento(getTitoloEntrata(),classificatoreAggiornamento.getTitoloEntrata(),isTitoloEntrataEditabile());
		TipologiaTitolo tipologiaTitoloDaInjettare = valutaInserimento(getTipologiaTitolo(),classificatoreAggiornamento.getTipologiaTitolo(),isTipologiaTitoloEditabile());
		
		CategoriaTipologiaTitolo categoriaTipologiaTitoloDaInjettare = valutaInserimento(getCategoriaTipologiaTitolo(), classificatoreAggiornamento.getCategoriaTipologiaTitolo(), isCategoriaTipologiaTitoloEditabile());
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = valutaInserimento(getElementoPianoDeiConti(), classificatoreAggiornamento.getElementoPianoDeiConti(),isElementoPianoDeiContiEditabile());
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = valutaInserimento(getStrutturaAmministrativoContabile(), classificatoreAggiornamento.getStrutturaAmministrativoContabile(), isStrutturaAmministrativoContabileEditabile());
		// Controllo che i classificatori siano cambiati
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimento(getTipoFinanziamento(), classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimento(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(), isTipoFondoEditabile());
		
		SiopeEntrata siopeEntrataDaInjettare = valutaInserimento(getSiopeEntrata(), classificatoreAggiornamento.getSiopeEntrata(), isSiopeEntrataEditabile());
		RicorrenteEntrata ricorrenteEntrataDaInjettare = valutaInserimento(getRicorrenteEntrata(), classificatoreAggiornamento.getRicorrenteEntrata(), isRicorrenteEntrataEditabile());
		PerimetroSanitarioEntrata perimetroSanitarioEntrataDaInjettare = valutaInserimento(getPerimetroSanitarioEntrata(), classificatoreAggiornamento.getPerimetroSanitarioEntrata(), isPerimetroSanitarioEntrataEditabile());
		TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrataDaInjettare = valutaInserimento(getTransazioneUnioneEuropeaEntrata(), classificatoreAggiornamento.getTransazioneUnioneEuropeaEntrata(), isTransazioneUnioneEuropeaEntrataEditabile());
		
		// Injezione dei classificatori nella request
		capitoloEntrataPrevisione.setEnte(getEnte());
		capitoloEntrataPrevisione.setBilancio(getBilancio());
		capitoloEntrataPrevisione.setTitoloEntrata(titoloEntrataDaInjettare);
		capitoloEntrataPrevisione.setTipologiaTitolo(tipologiaTitoloDaInjettare);
		capitoloEntrataPrevisione.setCategoriaTipologiaTitolo(categoriaTipologiaTitoloDaInjettare);
		capitoloEntrataPrevisione.setElementoPianoDeiConti(elementoPianoDeiContiDaInjettare);
		capitoloEntrataPrevisione.setStrutturaAmministrativoContabile(strutturaAmministrativoContabileDaInjettare);
		capitoloEntrataPrevisione.setTipoFinanziamento(tipoFinanziamentoDaInjettare);
		capitoloEntrataPrevisione.setTipoFondo(tipoFondoDaInjettare);
		
		capitoloEntrataPrevisione.setSiopeEntrata(siopeEntrataDaInjettare);
		capitoloEntrataPrevisione.setRicorrenteEntrata(ricorrenteEntrataDaInjettare);
		capitoloEntrataPrevisione.setPerimetroSanitarioEntrata(perimetroSanitarioEntrataDaInjettare);
		capitoloEntrataPrevisione.setTransazioneUnioneEuropeaEntrata(transazioneUnioneEuropeaEntrataDaInjettare);
		
		// Gli importi sono obbligatori
		capitoloEntrataPrevisione.setListaImportiCapitoloEP(getListaImportiCapitolo());
		
		/* Gestione della lista dei classificatori generici */
		capitoloEntrataPrevisione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloEntrataPrevisione(capitoloEntrataPrevisione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloEntrataPrevisione creaRequestRicercaVariazioniCapitoloEntrataPrevisione() {
		RicercaVariazioniCapitoloEntrataPrevisione request = creaRequest(RicercaVariazioniCapitoloEntrataPrevisione.class);
		
		capitoloEntrataPrevisione.setBilancio(getBilancio());
		capitoloEntrataPrevisione.setEnte(getEnte());
		
		request.setCapitoloEntrataPrev(capitoloEntrataPrevisione);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione() {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEPrev(getRicercaDettaglioCapitoloEPrev(capitoloEntrataPrevisione.getUid()));
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link ControllaClassificatoriModificabiliCapitolo} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo() {
		ControllaClassificatoriModificabiliCapitolo request = creaRequest(ControllaClassificatoriModificabiliCapitolo.class);
		
		request.setBilancio(getBilancio());
		
		request.setEnte(getEnte());
		request.setNumeroArticolo(capitoloEntrataPrevisione.getNumeroArticolo());
		request.setNumeroCapitolo(capitoloEntrataPrevisione.getNumeroCapitolo());
		request.setNumeroUEB(capitoloEntrataPrevisione.getNumeroUEB());
		
		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * 
	 * @param response	la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloEntrataPrevisioneResponse response) {
		capitoloEntrataPrevisione = response.getCapitoloEntrataPrevisione();
		
		setBilancio(response.getBilancio());
		setTipoFinanziamento(response.getTipoFinanziamento());
		setTipoFondo(response.getTipoFondo());
		setCategoriaTipologiaTitolo(capitoloEntrataPrevisione.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(capitoloEntrataPrevisione.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(capitoloEntrataPrevisione.getStrutturaAmministrativoContabile());
		setTipologiaTitolo(capitoloEntrataPrevisione.getTipologiaTitolo());
		setTitoloEntrata(capitoloEntrataPrevisione.getTitoloEntrata());
		
		setSiopeEntrata(capitoloEntrataPrevisione.getSiopeEntrata());
		setRicorrenteEntrata(capitoloEntrataPrevisione.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitoloEntrataPrevisione.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitoloEntrataPrevisione.getTransazioneUnioneEuropeaEntrata());
		
		if(capitoloEntrataPrevisione.getListaImportiCapitoloEP() != null) {
			// Dovrebbe essere sempre non-null
			try {
				importiCapitoloEntrataPrevisione0 = capitoloEntrataPrevisione.getListaImportiCapitoloEP().get(0);
				importiCapitoloEntrataPrevisione1 = capitoloEntrataPrevisione.getListaImportiCapitoloEP().get(1);
				importiCapitoloEntrataPrevisione2 = capitoloEntrataPrevisione.getListaImportiCapitoloEP().get(2);
			} catch(IndexOutOfBoundsException e) {
				// Ignoro l'eccezione
			}
		}
		
		importiEx = capitoloEntrataPrevisione.getImportiCapitoloEquivalente();
		
		impostaClassificatoriGenericiDaLista(capitoloEntrataPrevisione.getClassificatoriGenerici());
		
		if(getTitoloEntrata() != null) {
			setCodiceTitolo(Integer.parseInt(getTitoloEntrata().getCodice()));
		}
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */ 
		valorizzaStringheUtilita();
	}
	
	/**
	 * Costruisce la lista degli Importi Capitolo Uscita Previsione a partire dagli importi del Model.
	 * 
	 * @return la lista creata
	 */
	private List<ImportiCapitoloEP> getListaImportiCapitolo() {
		List<ImportiCapitoloEP> lista = new ArrayList<ImportiCapitoloEP>();
		addImportoCapitoloALista(lista, importiCapitoloEntrataPrevisione0, getAnnoEsercizioInt() + 0);
		addImportoCapitoloALista(lista, importiCapitoloEntrataPrevisione1, getAnnoEsercizioInt() + 1);
		addImportoCapitoloALista(lista, importiCapitoloEntrataPrevisione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * @param chiaveCapitolo la chiave univoca del capitolo
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev getRicercaDettaglioCapitoloEPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	@Override
	public void valutaModificabilitaClassificatori(ControllaClassificatoriModificabiliCapitoloResponse response, boolean isMassivo) {
		// SIAC-4308: controllo modificabilita' per il decentrato
		if(Boolean.TRUE.equals(getDecentrato())) {
			// Non abilitare nulla
			
			setTitoloEntrataEditabile(false);
			setTipologiaTitoloEditabile(false);
			setCategoriaTipologiaTitoloEditabile(false);
			setElementoPianoDeiContiEditabile(false);
			setSiopeEntrataEditabile(false);
			setStrutturaAmministrativoContabileEditabile(false);
			setTipoFinanziamentoEditabile(false);
			setTipoFondoEditabile(false);
			setRicorrenteEntrataEditabile(false);
			setPerimetroSanitarioEntrataEditabile(false);
			setTransazioneUnioneEuropeaEntrataEditabile(false);
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
			setFlagImpegnabileEditabile(false);
			setFlagPerMemoriaEditabile(false);
			setNoteEditabile(false);
		} else {
			super.valutaModificabilitaAttributi(response, isMassivo);
			
			setExAnnoEditabile(true);
			setExCapitoloEditabile(true);
			setExArticoloEditabile(true);
			setExUEBEditabile(true);
		}
		setFlagAccertatoPerCassaEditabile(true);
	}
}
