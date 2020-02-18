/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Entrata Gestione. Contiene una mappatura dei form per l'aggiornamento del
 * Capitolo di Entrata Gestione nel caso massivo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
public class AggiornaMassivaCapitoloEntrataGestioneModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3963480713511420126L;

	/* Seconda maschera: altri dati */
	private CapitoloEntrataGestione capitoloEntrataGestione;
	
	// SIAC-5582
	private boolean flagAccertatoPerCassaEditabile;
	
	/** Costruttore vuoto di default */
	public AggiornaMassivaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Entrata Gestione (massivo)");
	}

	/**
	 * @return the capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestione() {
		return capitoloEntrataGestione;
	}

	/**
	 * @param capitoloEntrataGestione the capitoloEntrataGestione to set
	 */
	public void setCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
	}
	
	/**
	 * Ottiene l'anno del capitolo da aggiornare.
	 * 
	 * @return l'anno del Capitolo di Entrata Gestione da aggiornare
	 */
	public Integer getAnnoCapitoloDaAggiornare() {
		return capitoloEntrataGestione == null ? getAnnoEsercizioInt() : capitoloEntrataGestione.getAnnoCapitolo();
	}
	
	/**
	 * Imposta l'anno nel Capitolo di Entrata Gestione da Aggiornare.
	 * 
	 * @param annoCapitoloDaAggiornare l'anno da impostare
	 */
	public void setAnnoCapitoloDaAggiornare(Integer annoCapitoloDaAggiornare) {
		if(capitoloEntrataGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setAnnoCapitolo(annoCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero del capitolo da aggiornare.
	 * 
	 * @return il numero del Capitolo del Capitolo di Entrata Gestione da aggiornare
	 */
	public Integer getNumeroCapitoloDaAggiornare() {
		return capitoloEntrataGestione == null ? Integer.valueOf(0) : capitoloEntrataGestione.getNumeroCapitolo();
	}
	
	/**
	 * Imposta il numero del capitolo nel Capitolo di Entrata Gestione da Aggiornare.
	 * 
	 * @param numeroCapitoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroCapitoloDaAggiornare(Integer numeroCapitoloDaAggiornare) {
		if(capitoloEntrataGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setNumeroCapitolo(numeroCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero dell'articolo da aggiornare.
	 * 
	 * @return il numero dell'articolo del Capitolo di Entrata Gestione da aggiornare
	 */
	public Integer getNumeroArticoloDaAggiornare() {
		return capitoloEntrataGestione == null ? Integer.valueOf(0) : capitoloEntrataGestione.getNumeroArticolo();
	}
	
	/**
	 * Imposta il numero dell'articolo nel Capitolo di Entrata Gestione da Aggiornare.
	 * 
	 * @param numeroArticoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroArticoloDaAggiornare(Integer numeroArticoloDaAggiornare) {
		if(capitoloEntrataGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setNumeroArticolo(numeroArticoloDaAggiornare);
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

	/* Requests */

	/**
	 * Restituisce una Request di tipo {@link AggiornaMassivoCapitoloDiEntrataGestione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaMassivoCapitoloDiEntrataGestione creaRequestAggiornaMassivoCapitoloDiEntrataGestione
			(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		AggiornaMassivoCapitoloDiEntrataGestione request = new AggiornaMassivoCapitoloDiEntrataGestione();

		capitoloEntrataGestione.setAnnoCapitolo(getBilancio().getAnno());
		
		// I seguenti dati sono i dati di base obbligatori
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		// Modularizzazione dei campi
		TitoloEntrata titoloEntrataDaInjettare = isTitoloEntrataEditabile() ? getTitoloEntrata() : null;
		TipologiaTitolo tipologiaTitoloDaInjettare = isTipologiaTitoloEditabile() ? getTipologiaTitolo() : null;
		CategoriaTipologiaTitolo categoriaTipologiaTitoloDaInjettare = isCategoriaTipologiaTitoloEditabile() ? getCategoriaTipologiaTitolo() : null;
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = isElementoPianoDeiContiEditabile() ? getElementoPianoDeiConti() : null;
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = isStrutturaAmministrativoContabileEditabile() ? getStrutturaAmministrativoContabile() : null;
		// Controllo che i classificatori siano cambiati
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimentoMassivo(getTipoFinanziamento(), classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimentoMassivo(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(), isTipoFondoEditabile());
		
		SiopeEntrata siopeEntrataDaInjettare = valutaInserimentoMassivo(getSiopeEntrata(), classificatoreAggiornamento.getSiopeEntrata(), isSiopeEntrataEditabile());
		RicorrenteEntrata ricorrenteEntrataDaInjettare = valutaInserimentoMassivo(getRicorrenteEntrata(), classificatoreAggiornamento.getRicorrenteEntrata(), isRicorrenteEntrataEditabile());
		PerimetroSanitarioEntrata perimetroSanitarioEntrataDaInjettare = valutaInserimentoMassivo(getPerimetroSanitarioEntrata(), classificatoreAggiornamento.getPerimetroSanitarioEntrata(), isPerimetroSanitarioEntrataEditabile());
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
		
		/* Gestione della lista dei classificatori generici */
		capitoloEntrataGestione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloEntrataGestione(capitoloEntrataGestione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioMassivaCapitoloEntrataGestione creaRequestRicercaDettaglioMassivaCapitoloEntrataGestione() {
		RicercaDettaglioMassivaCapitoloEntrataGestione request = creaRequest(RicercaDettaglioMassivaCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		
		// Impostazione dei dati del capitolo da ricercare
		request.setRicercaSinteticaCapitoloEGest(ottieniRicercaSinteticaCapitoloEGest());

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link ControllaClassificatoriModificabiliCapitolo} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo() {
		ControllaClassificatoriModificabiliCapitolo request = creaRequest(ControllaClassificatoriModificabiliCapitolo.class);
		
		request.setEnte(getEnte());
		request.setNumeroArticolo(getNumeroArticoloDaAggiornare());
		request.setNumeroCapitolo(getNumeroCapitoloDaAggiornare());
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @param capitolo il capitolo per il quale costruire la request
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloEntrataGestione creaRequestRicercaVariazioniCapitoloEntrataGestione(CapitoloEntrataGestione capitolo) {
		RicercaVariazioniCapitoloEntrataGestione request =creaRequest(RicercaVariazioniCapitoloEntrataGestione.class);
		
		capitolo.setBilancio(getBilancio());
		capitolo.setEnte(getEnte());
		
		request.setCapitoloEntrataGest(capitolo);
		
		return request;
	}
	
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Sintetica massiva del Capitolo di Entrata Gestione.
	 * 
	 * @param response la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioMassivaCapitoloEntrataGestioneResponse response) {
		
		setBilancio(response.getCapitoloMassivaEntrataGestione().getBilancio());
		/* Ottenuti dalla response */
		capitoloEntrataGestione = response.getCapitoloMassivaEntrataGestione();
		
		setTipoFinanziamento(response.getCapitoloMassivaEntrataGestione().getTipoFinanziamento());
		setTipoFondo(response.getCapitoloMassivaEntrataGestione().getTipoFondo());
		setElementoPianoDeiConti(capitoloEntrataGestione.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(capitoloEntrataGestione.getStrutturaAmministrativoContabile());
		setTitoloEntrata(capitoloEntrataGestione.getTitoloEntrata());
		setTipologiaTitolo(capitoloEntrataGestione.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(capitoloEntrataGestione.getCategoriaTipologiaTitolo());
		
		setSiopeEntrata(capitoloEntrataGestione.getSiopeEntrata());
		setRicorrenteEntrata(capitoloEntrataGestione.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitoloEntrataGestione.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitoloEntrataGestione.getTransazioneUnioneEuropeaEntrata());
		
		impostaClassificatoriGenericiDaLista(response.getCapitoloMassivaEntrataGestione().getListaClassificatori());
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */
		valorizzaStringheUtilita();
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica massiva del Capitolo di Entrata Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaSinteticaCapitoloEGest ottieniRicercaSinteticaCapitoloEGest() {
		RicercaSinteticaCapitoloEGest utility = new RicercaSinteticaCapitoloEGest();
		
		// Imposto i dati del capitolo da aggiornare
		utility.setAnnoCapitolo(getAnnoCapitoloDaAggiornare());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(getNumeroCapitoloDaAggiornare());
		utility.setNumeroArticolo(getNumeroArticoloDaAggiornare());
		
		return utility;
	}
	
	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		setFlagAccertatoPerCassaEditabile(true);
	}

}
