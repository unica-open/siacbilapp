/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
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
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per il Capitolo di Uscita Gestione. Contiene una mappatura dei form per l'aggiornamento del
 * Capitolo di Uscita Gestione nel caso massivo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 24/09/2013
 *
 */
public class AggiornaMassivaCapitoloUscitaGestioneModel extends CapitoloUscitaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6054183857673736650L;
	/* Seconda maschera: altri dati */
	private CapitoloUscitaGestione capitoloUscitaGestione;
	
	/** Costruttore vuoto di default */
	public AggiornaMassivaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Spesa Gestione (massivo)");
	}

	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * @param capitoloUscitaGestione the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
	}
	
	/**
	 * Ottiene l'anno del capitolo da aggiornare.
	 * 
	 * @return l'anno del Capitolo di Uscita Gestione da aggiornare
	 */
	public Integer getAnnoCapitoloDaAggiornare() {
		return capitoloUscitaGestione == null ? getAnnoEsercizioInt() : capitoloUscitaGestione.getAnnoCapitolo();
	}
	
	/**
	 * Imposta l'anno nel Capitolo di Uscita Gestione da Aggiornare.
	 * 
	 * @param annoCapitoloDaAggiornare l'anno da impostare
	 */
	public void setAnnoCapitoloDaAggiornare(Integer annoCapitoloDaAggiornare) {
		if(capitoloUscitaGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setAnnoCapitolo(annoCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero del capitolo da aggiornare.
	 * 
	 * @return il numero del Capitolo del Capitolo di Uscita Gestione da aggiornare
	 */
	public Integer getNumeroCapitoloDaAggiornare() {
		return capitoloUscitaGestione == null ? Integer.valueOf(0) : capitoloUscitaGestione.getNumeroCapitolo();
	}
	
	/**
	 * Imposta il numero del capitolo nel Capitolo di Uscita Gestione da Aggiornare.
	 * 
	 * @param numeroCapitoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroCapitoloDaAggiornare(Integer numeroCapitoloDaAggiornare) {
		if(capitoloUscitaGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setNumeroCapitolo(numeroCapitoloDaAggiornare);
	}
	
	/**
	 * Ottiene il numero dell'articolo da aggiornare.
	 * 
	 * @return il numero dell'articolo del Capitolo di Uscita Gestione da aggiornare
	 */
	public Integer getNumeroArticoloDaAggiornare() {
		return capitoloUscitaGestione == null ? Integer.valueOf(0) : capitoloUscitaGestione.getNumeroArticolo();
	}
	
	/**
	 * Imposta il numero dell'articolo nel Capitolo di Uscita Gestione da Aggiornare.
	 * 
	 * @param numeroArticoloDaAggiornare il numero capitolo da impostare
	 */
	public void setNumeroArticoloDaAggiornare(Integer numeroArticoloDaAggiornare) {
		if(capitoloUscitaGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setNumeroArticolo(numeroArticoloDaAggiornare);
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link AggiornaMassivoCapitoloDiUscitaGestione} a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaMassivoCapitoloDiUscitaGestione creaRequestAggiornaMassivoCapitoloDiUscitaGestione(ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento) {
		AggiornaMassivoCapitoloDiUscitaGestione request = creaRequest(AggiornaMassivoCapitoloDiUscitaGestione.class);

		capitoloUscitaGestione.setAnnoCapitolo(getBilancio().getAnno());
		
		// Modularizzazione dei campi
		Missione missioneDaInjettare = isMissioneEditabile() ? getMissione() : null;
		Programma programmaDaInjettare = isProgrammaEditabile() ? getProgramma() : null;
		TitoloSpesa titoloSpesaDaInjettare = isTitoloSpesaEditabile() ? getTitoloSpesa() : null;
		Macroaggregato macroaggregatoDaInjettare = isMacroaggregatoEditabile() ? getMacroaggregato() : null;
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = isElementoPianoDeiContiEditabile() ? getElementoPianoDeiConti() : null;
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = isStrutturaAmministrativoContabileEditabile() ? getStrutturaAmministrativoContabile() : null;
		// Controllo che i classificatori siano cambiati
		ClassificazioneCofog classificazioneCofogDaInjettare = valutaInserimentoMassivo(getClassificazioneCofog(), classificatoreAggiornamento.getClassificazioneCofog(), isClassificazioneCofogEditabile());
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimentoMassivo(getTipoFinanziamento(), classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimentoMassivo(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(), isTipoFondoEditabile());
		
		SiopeSpesa siopeSpesaDaInjettare = valutaInserimentoMassivo(getSiopeSpesa(), classificatoreAggiornamento.getSiopeSpesa(), isSiopeSpesaEditabile());
		RicorrenteSpesa ricorrenteSpesaDaInjettare = valutaInserimentoMassivo(getRicorrenteSpesa(), classificatoreAggiornamento.getRicorrenteSpesa(), isRicorrenteSpesaEditabile());
		PerimetroSanitarioSpesa perimetroSanitarioSpesaDaInjettare = valutaInserimentoMassivo(getPerimetroSanitarioSpesa(), classificatoreAggiornamento.getPerimetroSanitarioSpesa(), isPerimetroSanitarioSpesaEditabile());
		TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesaDaInjettare = valutaInserimentoMassivo(getTransazioneUnioneEuropeaSpesa(), classificatoreAggiornamento.getTransazioneUnioneEuropeaSpesa(), isTransazioneUnioneEuropeaSpesaEditabile());
		PoliticheRegionaliUnitarie politicheRegionaliUnitarieDaInjettare = valutaInserimentoMassivo(getPoliticheRegionaliUnitarie(), classificatoreAggiornamento.getPoliticheRegionaliUnitarie(), isPoliticheRegionaliUnitarieEditabile());
		
		// Injezione dei classificatori nella request
		capitoloUscitaGestione.setEnte(getEnte());
		capitoloUscitaGestione.setBilancio(getBilancio());
		capitoloUscitaGestione.setMissione(missioneDaInjettare);
		capitoloUscitaGestione.setProgramma(programmaDaInjettare);
		capitoloUscitaGestione.setTitoloSpesa(titoloSpesaDaInjettare);
		capitoloUscitaGestione.setMacroaggregato(macroaggregatoDaInjettare);
		capitoloUscitaGestione.setElementoPianoDeiConti(elementoPianoDeiContiDaInjettare);
		capitoloUscitaGestione.setStrutturaAmministrativoContabile(strutturaAmministrativoContabileDaInjettare);
		capitoloUscitaGestione.setClassificazioneCofogProgramma(getClassificazioneCofogProgramma(classificazioneCofogDaInjettare));
		capitoloUscitaGestione.setTipoFinanziamento(tipoFinanziamentoDaInjettare);
		capitoloUscitaGestione.setTipoFondo(tipoFondoDaInjettare);
		
		capitoloUscitaGestione.setSiopeSpesa(siopeSpesaDaInjettare);
		capitoloUscitaGestione.setRicorrenteSpesa(ricorrenteSpesaDaInjettare);
		capitoloUscitaGestione.setPerimetroSanitarioSpesa(perimetroSanitarioSpesaDaInjettare);
		capitoloUscitaGestione.setTransazioneUnioneEuropeaSpesa(transazioneUnioneEuropeaSpesaDaInjettare);
		capitoloUscitaGestione.setPoliticheRegionaliUnitarie(politicheRegionaliUnitarieDaInjettare);
		
		/* Gestione della lista dei classificatori generici */
		capitoloUscitaGestione.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));
		
		request.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioMassivaCapitoloUscitaGestione creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione() {
		RicercaDettaglioMassivaCapitoloUscitaGestione request = creaRequest(RicercaDettaglioMassivaCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		
		// Impostazione dei dati del capitolo da ricercare
		request.setRicercaSinteticaCapitoloUGest(ottieniRicercaSinteticaCapitoloUGest());

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
	 * Restituisce una Request di tipo {@link RicercaVariazioniCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @param capitolo il capitolo per cui costruire la request
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloUscitaGestione creaRequestRicercaVariazioniCapitoloUscitaGestione(CapitoloUscitaGestione capitolo) {
		RicercaVariazioniCapitoloUscitaGestione request = creaRequest(RicercaVariazioniCapitoloUscitaGestione.class);
		
		capitolo.setBilancio(getBilancio());
		capitolo.setEnte(getEnte());
		
		request.setCapitoloUscitaGest(capitolo);
		
		return request;
	}
	
	
	/* Metodi di utilita' */
	
	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca Sintetica massiva del Capitolo di Uscita Gestione.
	 * 
	 * @param response la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response) {
		
		/* Ottenuti dalla response */
		capitoloUscitaGestione = response.getCapitoloMassivaUscitaGestione();
		
		setBilancio(capitoloUscitaGestione.getBilancio());
		setTipoFinanziamento(capitoloUscitaGestione.getTipoFinanziamento());
		setTipoFondo(capitoloUscitaGestione.getTipoFondo());
		setClassificazioneCofog(capitoloUscitaGestione.getClassificazioneCofog());
		setElementoPianoDeiConti(capitoloUscitaGestione.getElementoPianoDeiConti());
		setMacroaggregato(capitoloUscitaGestione.getMacroaggregato());
		setMissione(capitoloUscitaGestione.getMissione());
		setProgramma(capitoloUscitaGestione.getProgramma());
		setStrutturaAmministrativoContabile(capitoloUscitaGestione.getStrutturaAmministrativoContabile());
		setTitoloSpesa(capitoloUscitaGestione.getTitoloSpesa());
		
		setSiopeSpesa(capitoloUscitaGestione.getSiopeSpesa());
		setRicorrenteSpesa( capitoloUscitaGestione.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitoloUscitaGestione.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitoloUscitaGestione.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitoloUscitaGestione.getPoliticheRegionaliUnitarie());
		
		impostaClassificatoriGenericiDaLista(capitoloUscitaGestione.getClassificatoriGenerici());
		
		/* Stringhe di utilita' per la visualizzazione dell'elemento del piano dei conti e della struttura amministrativo contabile */
		valorizzaStringheUtilita();
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica massiva del Capitolo di Uscita Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaSinteticaCapitoloUGest ottieniRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
		// Imposto i dati del capitolo da aggiornare
		utility.setAnnoCapitolo(getAnnoCapitoloDaAggiornare());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(getNumeroCapitoloDaAggiornare());
		utility.setNumeroArticolo(getNumeroArticoloDaAggiornare());
		
		return utility;
	}
	

}
