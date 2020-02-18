/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Uscita Previsione. Contiene una mappatura dei form per l'aggiornamento del
 * Capitolo di Uscita Previsione nel caso massivo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
public class AggiornaMassivaCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5126976198929200774L;
	
	/* Seconda maschera: altri dati */
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	
	/** Costruttore vuoto di default */
	public AggiornaMassivaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Spesa Previsione (massivo)");
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
	 * Ottiene l'anno del capitolo da aggiornare.
	 * 
	 * @return l'anno del Capitolo di Uscita Previsione da aggiornare
	 */
	public Integer getAnnoCapitoloDaAggiornare() {
		return capitoloUscitaPrevisione == null ? getAnnoEsercizioInt() : capitoloUscitaPrevisione.getAnnoCapitolo();
	}
	
	/**
	 * Imposta l'anno nel Capitolo di Uscita Previsione da Aggiornare.
	 * 
	 * @param annoCapitoloDaAggiornare l'anno da impostare
	 */
	public void setAnnoCapitoloDaAggiornare(Integer annoCapitoloDaAggiornare) {
		if(capitoloUscitaPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setAnnoCapitolo(annoCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero del capitolo da aggiornare.
	 * 
	 * @return il numero del Capitolo del Capitolo di Uscita Previsione da aggiornare
	 */
	public Integer getNumeroCapitoloDaAggiornare() {
		return capitoloUscitaPrevisione == null ? Integer.valueOf(0) : capitoloUscitaPrevisione.getNumeroCapitolo();
	}
	
	/**
	 * Imposta il numero del capitolo nel Capitolo di Uscita Previsione da Aggiornare.
	 * 
	 * @param numeroCapitoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroCapitoloDaAggiornare(Integer numeroCapitoloDaAggiornare) {
		if(capitoloUscitaPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setNumeroCapitolo(numeroCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero dell'articolo da aggiornare.
	 * 
	 * @return il numero dell'articolo del Capitolo di Uscita Previsione da aggiornare
	 */
	public Integer getNumeroArticoloDaAggiornare() {
		return capitoloUscitaPrevisione == null ? Integer.valueOf(0) : capitoloUscitaPrevisione.getNumeroArticolo();
	}
	
	/**
	 * Imposta il numero dell'articolo nel Capitolo di Uscita Previsione da Aggiornare.
	 * 
	 * @param numeroArticoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroArticoloDaAggiornare(Integer numeroArticoloDaAggiornare) {
		if(capitoloUscitaPrevisione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setNumeroArticolo(numeroArticoloDaAggiornare);
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link AggiornaMassivoCapitoloDiUscitaPrevisione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaMassivoCapitoloDiUscitaPrevisione creaRequestAggiornaMassivoCapitoloDiUscitaPrevisione(ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento) {
		AggiornaMassivoCapitoloDiUscitaPrevisione request = creaRequest(AggiornaMassivoCapitoloDiUscitaPrevisione.class);

		capitoloUscitaPrevisione.setAnnoCapitolo(getBilancio().getAnno());
		
		// Le seguenti entita sono le entita di base obbligatorie
		
		// Modularizzazione dei campi
		Missione missioneDaInjettare = isMissioneEditabile() ? getMissione() : null;
		Programma programmaDaInjettare = isProgrammaEditabile() ? getProgramma() : null;
		TitoloSpesa titoloSpesaDaInjettare = isTitoloSpesaEditabile() ? getTitoloSpesa() : null;
		Macroaggregato macroaggregatoDaInjettare = isMacroaggregatoEditabile() ? getMacroaggregato() : null;
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = isElementoPianoDeiContiEditabile() ? getElementoPianoDeiConti() : null;
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = isStrutturaAmministrativoContabileEditabile() ? getStrutturaAmministrativoContabile() : null;
		
		// Le seguenti entita non sono obbligatorie
		// Controllo che i classificatori siano cambiati
		ClassificazioneCofog classificazioneCofogDaInjettare = valutaInserimentoMassivo(getClassificazioneCofog(), classificatoreAggiornamento.getClassificazioneCofog(), isClassificazioneCofogEditabile());
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimentoMassivo(getTipoFinanziamento(), classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimentoMassivo(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(), isTipoFondoEditabile());
		
		SiopeSpesa siopeSpesaDaInjettare = valutaInserimento(getSiopeSpesa(), classificatoreAggiornamento.getSiopeSpesa(), isSiopeSpesaEditabile());
		RicorrenteSpesa ricorrenteSpesaDaInjettare = valutaInserimento(getRicorrenteSpesa(), classificatoreAggiornamento.getRicorrenteSpesa(), isRicorrenteSpesaEditabile());
		PerimetroSanitarioSpesa perimetroSanitarioSpesaDaInjettare = valutaInserimento(getPerimetroSanitarioSpesa(), classificatoreAggiornamento.getPerimetroSanitarioSpesa(), isPerimetroSanitarioSpesaEditabile());
		TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesaDaInjettare = valutaInserimento(getTransazioneUnioneEuropeaSpesa(), classificatoreAggiornamento.getTransazioneUnioneEuropeaSpesa(), isTransazioneUnioneEuropeaSpesaEditabile());
		PoliticheRegionaliUnitarie politicheRegionaliUnitarieDaInjettare = valutaInserimento(getPoliticheRegionaliUnitarie(), classificatoreAggiornamento.getPoliticheRegionaliUnitarie(), isPoliticheRegionaliUnitarieEditabile());
		
		// Injezione dei classificatori
		capitoloUscitaPrevisione.setEnte(getEnte());
		capitoloUscitaPrevisione.setBilancio(getBilancio());
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
		
		// Gestione della lista dei classificatori generici
		capitoloUscitaPrevisione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioMassivaCapitoloUscitaPrevisione creaRequestRicercaDettaglioMassivaCapitoloUscitaPrevisione() {
		RicercaDettaglioMassivaCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioMassivaCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());
		
		// Impostazione dei dati del capitolo da ricercare
		request.setRicercaSinteticaCapitoloUPrev(ottieniRicercaSinteticaCapitoloUPrev());

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
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @param capitolo il capitolo per cui costruire la request
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloUscitaPrevisione creaRequestRicercaVariazioniCapitoloUscitaPrevisione(CapitoloUscitaPrevisione capitolo) {
		RicercaVariazioniCapitoloUscitaPrevisione request = creaRequest(RicercaVariazioniCapitoloUscitaPrevisione.class);
		
		capitolo.setBilancio(getBilancio());
		capitolo.setEnte(getEnte());
		
		request.setCapitoloUscitaPrev(capitolo);
		
		return request;
	}
	
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Sintetica massiva del Capitolo di Uscita Previsione.
	 * 
	 * @param response la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response) {
		
		
		/* Ottenuti dalla response */
		capitoloUscitaPrevisione = response.getCapitoloMassivaUscitaPrevisione();
		
		impostaClassificatoriGenericiDaLista(response.getCapitoloMassivaUscitaPrevisione().getListaClassificatori());
		
		setBilancio(capitoloUscitaPrevisione.getBilancio());
		setClassificazioneCofog(capitoloUscitaPrevisione.getClassificazioneCofog());
		setElementoPianoDeiConti(capitoloUscitaPrevisione.getElementoPianoDeiConti());
		setMacroaggregato(capitoloUscitaPrevisione.getMacroaggregato());
		setMissione(capitoloUscitaPrevisione.getMissione());
		setProgramma(capitoloUscitaPrevisione.getProgramma());
		setStrutturaAmministrativoContabile(capitoloUscitaPrevisione.getStrutturaAmministrativoContabile());
		setTitoloSpesa(capitoloUscitaPrevisione.getTitoloSpesa());
		setTipoFinanziamento(capitoloUscitaPrevisione.getTipoFinanziamento());
		setTipoFondo(capitoloUscitaPrevisione.getTipoFondo());
		
		setSiopeSpesa(capitoloUscitaPrevisione.getSiopeSpesa());
		setRicorrenteSpesa(capitoloUscitaPrevisione.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitoloUscitaPrevisione.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitoloUscitaPrevisione.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitoloUscitaPrevisione.getPoliticheRegionaliUnitarie());
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */
		valorizzaStringheUtilita();
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica massiva del Capitolo di Uscita Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaSinteticaCapitoloUPrev ottieniRicercaSinteticaCapitoloUPrev() {
		RicercaSinteticaCapitoloUPrev utility = new RicercaSinteticaCapitoloUPrev();
		
		// Imposto i dati del capitolo da aggiornare
		utility.setAnnoCapitolo(getAnnoCapitoloDaAggiornare());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(getNumeroCapitoloDaAggiornare());
		utility.setNumeroArticolo(getNumeroArticoloDaAggiornare());
		
		return utility;
	}
	
	
}
