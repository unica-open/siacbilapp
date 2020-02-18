/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
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
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Entrata Previsione. Contiene una mappatura dei form per l'aggiornamento del
 * Capitolo di Entrata Previsione nel caso massivo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
public class AggiornaMassivaCapitoloEntrataPrevisioneModel extends CapitoloEntrataPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6536549546696938652L;

	/* Seconda maschera: altri dati */
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	
	// SIC-5582
	private boolean flagAccertatoPerCassaEditabile;
	
	/** Costruttore vuoto di default */
	public AggiornaMassivaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Entrata Previsione (massivo)");
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
	 * Ottiene l'anno del capitolo da aggiornare.
	 * 
	 * @return l'anno del Capitolo di Entrata Previsione da aggiornare
	 */
	public Integer getAnnoCapitoloDaAggiornare() {
		return capitoloEntrataPrevisione == null ? getAnnoEsercizioInt() : capitoloEntrataPrevisione.getAnnoCapitolo();
	}
	
	/**
	 * Imposta l'anno nel Capitolo di Entrata Previsione da Aggiornare.
	 * 
	 * @param annoCapitoloDaAggiornare l'anno da impostare
	 */
	public void setAnnoCapitoloDaAggiornare(Integer annoCapitoloDaAggiornare) {
		if(capitoloEntrataPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setAnnoCapitolo(annoCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero del capitolo da aggiornare.
	 * 
	 * @return il numero del Capitolo del Capitolo di Entrata Previsione da aggiornare
	 */
	public Integer getNumeroCapitoloDaAggiornare() {
		return capitoloEntrataPrevisione == null ? Integer.valueOf(0) : capitoloEntrataPrevisione.getNumeroCapitolo();
	}
	
	/**
	 * Imposta il numero del capitolo nel Capitolo di Entrata Previsione da Aggiornare.
	 * 
	 * @param numeroCapitoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroCapitoloDaAggiornare(Integer numeroCapitoloDaAggiornare) {
		if(capitoloEntrataPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setNumeroCapitolo(numeroCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero dell'articolo da aggiornare.
	 * 
	 * @return il numero dell'articolo del Capitolo di Entrata Previsione da aggiornare
	 */
	public Integer getNumeroArticoloDaAggiornare() {
		return capitoloEntrataPrevisione == null ? Integer.valueOf(0) : capitoloEntrataPrevisione.getNumeroArticolo();
	}
	
	/**
	 * Imposta il numero dell'articolo nel Capitolo di Entrata Previsione da Aggiornare.
	 * 
	 * @param numeroArticoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroArticoloDaAggiornare(Integer numeroArticoloDaAggiornare) {
		if(capitoloEntrataPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setNumeroArticolo(numeroArticoloDaAggiornare);
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
	 * Restituisce una Request di tipo {@link AggiornaMassivoCapitoloDiEntrataPrevisione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaMassivoCapitoloDiEntrataPrevisione creaRequestAggiornaMassivoCapitoloDiEntrataPrevisione
			(ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento) {
		AggiornaMassivoCapitoloDiEntrataPrevisione request = new AggiornaMassivoCapitoloDiEntrataPrevisione();

		capitoloEntrataPrevisione.setAnnoCapitolo(getBilancio().getAnno());
		
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
		
		/* Gestione della lista dei classificatori generici */
		capitoloEntrataPrevisione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloEntrataPrevisione(capitoloEntrataPrevisione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioMassivaCapitoloEntrataPrevisione creaRequestRicercaDettaglioMassivaCapitoloEntrataPrevisione() {
		RicercaDettaglioMassivaCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioMassivaCapitoloEntrataPrevisione.class);
		
		request.setEnte(getEnte());
		
		// Impostazione dei dati del capitolo da ricercare
		request.setRicercaSinteticaCapitoloEPrev(ottieniRicercaSinteticaCapitoloEPrev());

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
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @param capitolo il capitolo per cui costruire la request
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloEntrataPrevisione creaRequestRicercaVariazioniCapitoloEntrataPrevisione(CapitoloEntrataPrevisione capitolo) {
		RicercaVariazioniCapitoloEntrataPrevisione request = creaRequest(RicercaVariazioniCapitoloEntrataPrevisione.class);
		
		capitolo.setBilancio(getBilancio());
		capitolo.setEnte(getEnte());
		
		request.setCapitoloEntrataPrev(capitolo);

		return request;
	}
	
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Sintetica massiva del Capitolo di Entrata Previsione.
	 * 
	 * @param response la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse response) {
		
		/* Ottenuti dalla response */
		capitoloEntrataPrevisione = response.getCapitoloMassivaEntrataPrevisione();
		
		setBilancio(response.getCapitoloMassivaEntrataPrevisione().getBilancio());
		setTipoFinanziamento(response.getCapitoloMassivaEntrataPrevisione().getTipoFinanziamento());
		setTipoFondo(response.getCapitoloMassivaEntrataPrevisione().getTipoFondo());
		setElementoPianoDeiConti(capitoloEntrataPrevisione.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(capitoloEntrataPrevisione.getStrutturaAmministrativoContabile());
		setTitoloEntrata(capitoloEntrataPrevisione.getTitoloEntrata());
		setTipologiaTitolo(capitoloEntrataPrevisione.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(capitoloEntrataPrevisione.getCategoriaTipologiaTitolo());
		
		setSiopeEntrata(capitoloEntrataPrevisione.getSiopeEntrata());
		setRicorrenteEntrata(capitoloEntrataPrevisione.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(capitoloEntrataPrevisione.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(capitoloEntrataPrevisione.getTransazioneUnioneEuropeaEntrata());
		
		impostaClassificatoriGenericiDaLista(response.getCapitoloMassivaEntrataPrevisione().getListaClassificatori());
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */
		valorizzaStringheUtilita();
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica massiva del Capitolo di Entrata Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaSinteticaCapitoloEPrev ottieniRicercaSinteticaCapitoloEPrev() {
		RicercaSinteticaCapitoloEPrev utility = new RicercaSinteticaCapitoloEPrev();
		
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
