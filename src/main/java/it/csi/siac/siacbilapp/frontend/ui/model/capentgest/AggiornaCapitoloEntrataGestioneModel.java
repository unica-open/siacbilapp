/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Entrata Gestione. Contiene una mappatura dei form per l'aggiornamento del
 * Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
public class AggiornaCapitoloEntrataGestioneModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3216137239869475666L;
	
	/* Terza maschera: importi */
	private ImportiCapitoloEG importiCapitoloEntrataGestioneM1;
	private ImportiCapitoloEG importiCapitoloEntrataGestione0;
	private ImportiCapitoloEG importiCapitoloEntrataGestione1;
	private ImportiCapitoloEG importiCapitoloEntrataGestione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloEntrataGestione	 capitoloEntrataGestione;
	
	// SIAC-5582
	private boolean flagAccertatoPerCassaEditabile;
	
	/** Costruttore vuoto di default */
	public AggiornaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Entrata Gestione");
	}

	/**
	 * @return the importiCapitoloEntrataGestioneM1
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestioneM1() {
		return importiCapitoloEntrataGestioneM1;
	}

	/**
	 * @param importiCapitoloEntrataGestioneM1 the importiCapitoloEntrataGestioneM1 to set
	 */
	public void setImportiCapitoloEntrataGestioneM1(ImportiCapitoloEG importiCapitoloEntrataGestioneM1) {
		this.importiCapitoloEntrataGestioneM1 = importiCapitoloEntrataGestioneM1;
	}

	/**
	 * @return the importiCapitoloEntrataGestione0
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione0() {
		return importiCapitoloEntrataGestione0;
	}

	/**
	 * @param importiCapitoloEntrataGestione0 the importiCapitoloEntrataGestione0 to set
	 */
	public void setImportiCapitoloEntrataGestione0(ImportiCapitoloEG importiCapitoloEntrataGestione0) {
		this.importiCapitoloEntrataGestione0 = importiCapitoloEntrataGestione0;
	}

	/**
	 * @return the importiCapitoloEntrataGestione1
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione1() {
		return importiCapitoloEntrataGestione1;
	}

	/**
	 * @param importiCapitoloEntrataGestione1 the importiCapitoloEntrataGestione1 to set
	 */
	public void setImportiCapitoloEntrataGestione1(ImportiCapitoloEG importiCapitoloEntrataGestione1) {
		this.importiCapitoloEntrataGestione1 = importiCapitoloEntrataGestione1;
	}

	/**
	 * @return the importiCapitoloEntrataGestione2
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione2() {
		return importiCapitoloEntrataGestione2;
	}

	/**
	 * @param importiCapitoloEntrataGestione2 the importiCapitoloEntrataGestione2 to set
	 */
	public void setImportiCapitoloEntrataGestione2(ImportiCapitoloEG importiCapitoloEntrataGestione2) {
		this.importiCapitoloEntrataGestione2 = importiCapitoloEntrataGestione2;
	}

	/**
	 * @return the capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestione() {
		return capitoloEntrataGestione;
	}
	
	/**
	 * Ottiene l'uid del capitolo da aggiornare.
	 * 
	 * @return l'uid del Capitolo di Entrata Previsione da aggiornare
	 */
	public int getUidDaAggiornare() {
		return capitoloEntrataGestione == null ? 0 : capitoloEntrataGestione.getUid();
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

	/**
	 * Imposta l'uid nel Capitolo di Entrata Previsione da Aggiornare.
	 * 
	 * @param uidDaAggiornare l'uid da impostare
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		if(capitoloEntrataGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setUid(uidDaAggiornare);
	}
	
	/**
	 * @param capitoloEntrataGestione the capitoloEntrataGestione to set
	 */
	public void setCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
	}
	
	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link AggiornaCapitoloDiEntrataGestione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaCapitoloDiEntrataGestione creaRequestAggiornaCapitoloDiEntrataGestione(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		AggiornaCapitoloDiEntrataGestione request = creaRequest(AggiornaCapitoloDiEntrataGestione.class);		
		capitoloEntrataGestione.setAnnoCapitolo(getBilancio().getAnno());
		
		// Modularizzazione dei campi
		//TitoloEntrata titoloEntrataDaInjettare = isTitoloEntrataEditabile() ? getTitoloEntrata() : classificatoreAggiornamento.getTitoloEntrata();
		TitoloEntrata titoloEntrataDaInjettare = valutaInserimento(getTitoloEntrata(), classificatoreAggiornamento.getTitoloEntrata(), isTitoloEntrataEditabile());
		//TipologiaTitolo tipologiaTitoloDaInjettare = isTipologiaTitoloEditabile() ? getTipologiaTitolo() : classificatoreAggiornamento.getTipologiaTitolo();
		TipologiaTitolo tipologiaTitoloDaInjettare = valutaInserimento(getTipologiaTitolo(),classificatoreAggiornamento.getTipologiaTitolo(), isTipologiaTitoloEditabile());
		
		//CategoriaTipologiaTitolo categoriaTipologiaTitoloDaInjettare = isCategoriaTipologiaTitoloEditabile() ? getCategoriaTipologiaTitolo() : classificatoreAggiornamento.getCategoriaTipologiaTitolo();
		CategoriaTipologiaTitolo categoriaTipologiaTitoloDaInjettare = valutaInserimento(getCategoriaTipologiaTitolo(), classificatoreAggiornamento.getCategoriaTipologiaTitolo(), isCategoriaTipologiaTitoloEditabile());
		
		//ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = isElementoPianoDeiContiEditabile() ? getElementoPianoDeiConti() : classificatoreAggiornamento.getElementoPianoDeiConti();
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = valutaInserimento(getElementoPianoDeiConti(), classificatoreAggiornamento.getElementoPianoDeiConti(), isElementoPianoDeiContiEditabile());
		
		//StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = isStrutturaAmministrativoContabileEditabile() ? getStrutturaAmministrativoContabile() : classificatoreAggiornamento.getStrutturaAmministrativoContabile();
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = valutaInserimento(getStrutturaAmministrativoContabile(), classificatoreAggiornamento.getStrutturaAmministrativoContabile(), isStrutturaAmministrativoContabileEditabile());
		
		// Controllo che i classificatori siano cambiati
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimento(getTipoFinanziamento(), classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimento(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(), isTipoFondoEditabile());
		
		SiopeEntrata siopeEntrataDaInjettare = valutaInserimento(getSiopeEntrata(), classificatoreAggiornamento.getSiopeEntrata(), isSiopeEntrataEditabile());
		RicorrenteEntrata ricorrenteEntrataDaInjettare = valutaInserimento(getRicorrenteEntrata(), classificatoreAggiornamento.getRicorrenteEntrata(), isRicorrenteEntrataEditabile());
		PerimetroSanitarioEntrata perimetroSanitarioEntrataDaInjettare = valutaInserimento(getPerimetroSanitarioEntrata(), classificatoreAggiornamento.getPerimetroSanitarioEntrata(), isPerimetroSanitarioEntrataEditabile());
		TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrataDaInjettare = valutaInserimento(getTransazioneUnioneEuropeaEntrata(), classificatoreAggiornamento.getTransazioneUnioneEuropeaEntrata(), isTransazioneUnioneEuropeaEntrataEditabile());
		
		// Injezione dei classificatori nella request
		capitoloEntrataGestione.setEnte(getEnte());
		capitoloEntrataGestione.setBilancio(getBilancio());
		capitoloEntrataGestione.setTitoloEntrata(titoloEntrataDaInjettare);
		capitoloEntrataGestione.setTipologiaTitolo(tipologiaTitoloDaInjettare);
		capitoloEntrataGestione.setCategoriaTipologiaTitolo(categoriaTipologiaTitoloDaInjettare);
		capitoloEntrataGestione.setElementoPianoDeiConti(elementoPianoDeiContiDaInjettare);
		capitoloEntrataGestione.setStrutturaAmministrativoContabile(strutturaAmministrativoContabileDaInjettare);
		capitoloEntrataGestione.setTipoFinanziamento(tipoFinanziamentoDaInjettare);
		capitoloEntrataGestione.setTipoFondo(tipoFondoDaInjettare);
		
		capitoloEntrataGestione.setSiopeEntrata(siopeEntrataDaInjettare);
		capitoloEntrataGestione.setRicorrenteEntrata(ricorrenteEntrataDaInjettare);
		capitoloEntrataGestione.setPerimetroSanitarioEntrata(perimetroSanitarioEntrataDaInjettare);
		capitoloEntrataGestione.setTransazioneUnioneEuropeaEntrata(transazioneUnioneEuropeaEntrataDaInjettare);
		
		// Gli importi sono obbligatori
		capitoloEntrataGestione.setListaImportiCapitoloEG(getListaImportiCapitolo());
		
		/* Gestione della lista dei classificatori generici */
		capitoloEntrataGestione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloEntrataGestione(capitoloEntrataGestione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloEntrataGestione creaRequestRicercaVariazioniCapitoloEntrataGestione() {
		RicercaVariazioniCapitoloEntrataGestione request = creaRequest(RicercaVariazioniCapitoloEntrataGestione.class);
		
		capitoloEntrataGestione.setBilancio(getBilancio());
		capitoloEntrataGestione.setEnte(getEnte());
		
		request.setCapitoloEntrataGest(capitoloEntrataGestione);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione() {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEGest(getRicercaDettaglioCapitoloEGest(capitoloEntrataGestione.getUid()));
		
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
		request.setNumeroArticolo(capitoloEntrataGestione.getNumeroArticolo());
		request.setNumeroCapitolo(capitoloEntrataGestione.getNumeroCapitolo());
		request.setNumeroUEB(capitoloEntrataGestione.getNumeroUEB());
		
		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
		
		return request;
	}
	
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Dettaglio del Capitolo di Entrata Gestione.
	 * 
	 * @param response la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloEntrataGestioneResponse response) {
		capitoloEntrataGestione = response.getCapitoloEntrataGestione();
		
		setBilancio(response.getBilancio());
		setTipoFinanziamento(response.getTipoFinanziamento());
		setTipoFondo(response.getTipoFondo());
		setCategoriaTipologiaTitolo(capitoloEntrataGestione.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(capitoloEntrataGestione.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(capitoloEntrataGestione.getStrutturaAmministrativoContabile());
		setTipologiaTitolo(capitoloEntrataGestione.getTipologiaTitolo());
		setTitoloEntrata(capitoloEntrataGestione.getTitoloEntrata());
		
		setSiopeEntrata(capitoloEntrataGestione.getSiopeEntrata());
		setRicorrenteEntrata(capitoloEntrataGestione.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitoloEntrataGestione.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitoloEntrataGestione.getTransazioneUnioneEuropeaEntrata());
		
		if(response.getListaImportiCapitoloEG() != null) {
			// Dovrebbe essere sempre non-null
			try {
				importiCapitoloEntrataGestione0 = response.getListaImportiCapitoloEG().get(0);
				importiCapitoloEntrataGestione1 = response.getListaImportiCapitoloEG().get(1);
				importiCapitoloEntrataGestione2 = response.getListaImportiCapitoloEG().get(2);
				importiCapitoloEntrataGestioneM1 = response.getListaImportiCapitoloEG().get(3);
			} catch(IndexOutOfBoundsException e) {
				// Ignoro l'eccezione
			}
		}
		
		impostaClassificatoriGenericiDaLista(capitoloEntrataGestione.getClassificatoriGenerici());
		
		if(getTitoloEntrata() != null) {
			setCodiceTitolo(Integer.parseInt(getTitoloEntrata().getCodice()));
		}
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */ 
		valorizzaStringheUtilita();
	}
	
	/**
	 * Costruisce la lista degli Importi Capitolo Entrata Gestione a partire dagli importi del Model.
	 * 
	 * @return la lista creata
	 */
	private List<ImportiCapitoloEG> getListaImportiCapitolo() {
		List<ImportiCapitoloEG> lista = new ArrayList<ImportiCapitoloEG>();
		addImportoCapitoloALista(lista, importiCapitoloEntrataGestione0, getAnnoEsercizioInt() + 0);
		addImportoCapitoloALista(lista, importiCapitoloEntrataGestione1, getAnnoEsercizioInt() + 1);
		addImportoCapitoloALista(lista, importiCapitoloEntrataGestione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * @param chiaveCapitolo la chiave univoca del capitolo
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest getRicercaDettaglioCapitoloEGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		setFlagAccertatoPerCassaEditabile(true);
	}
}
