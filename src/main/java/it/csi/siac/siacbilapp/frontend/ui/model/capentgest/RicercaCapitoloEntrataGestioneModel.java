/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la ricerca del Capitolo di Entrata Gestione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * 
 * @version 1.0.0 07/08/2013
 *
 */
public class RicercaCapitoloEntrataGestioneModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1994966519970639955L;
	
	/* Tabella A */
	private CapitoloEntrataGestione capitoloEntrataGestione;
	private String codiceTipoClassificatoreElementoPianoDeiConti;
	private String codiceTipoClassificatoreSiope;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	
	/* Tabella B */
	private String flagRilevanteIva;
	
	//SIAC-7858 CM 19/05/2021
	private String flagEntrataDubbiaEsigFCDE;
	
	/* Tabella C */
	private AttoDiLegge attoDiLegge;
	
	/* Liste */
	private List<ElementoCapitolo> listaCapitoli = new ArrayList<ElementoCapitolo>();
	
	//SIAC 6884 per filtro capitoli da associare alla variazione
	private boolean regionePiemonte;
	private boolean decentrato;
	private int uidVariazione;
	private String direzioneProponente;
	//***END SIAC 6884***//
	
	
	/** Costruttore vuoto di default */
	public RicercaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Ricerca Capitolo Entrata Gestione");
	}
	
	/* Getter e Setter */
	
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
	 * @return the codiceTipoClassificatoreElementoPianoDeiConti
	 */
	public String getCodiceTipoClassificatoreElementoPianoDeiConti() {
		return codiceTipoClassificatoreElementoPianoDeiConti;
	}

	/**
	 * @param codiceTipoClassificatoreElementoPianoDeiConti the codiceTipoClassificatoreElementoPianoDeiConti to set
	 */
	public void setCodiceTipoClassificatoreElementoPianoDeiConti(String codiceTipoClassificatoreElementoPianoDeiConti) {
		this.codiceTipoClassificatoreElementoPianoDeiConti = codiceTipoClassificatoreElementoPianoDeiConti;
	}

	/**
	 * @return the codiceTipoClassificatoreSiope
	 */
	public String getCodiceTipoClassificatoreSiope() {
		return codiceTipoClassificatoreSiope;
	}

	/**
	 * @param codiceTipoClassificatoreSiope the codiceTipoClassificatoreSiope to set
	 */
	public void setCodiceTipoClassificatoreSiope(String codiceTipoClassificatoreSiope) {
		this.codiceTipoClassificatoreSiope = codiceTipoClassificatoreSiope;
	}

	/**
	 * @return the codiceTipoClassificatoreStrutturaAmministrativoContabile
	 */
	public String getCodiceTipoClassificatoreStrutturaAmministrativoContabile() {
		return codiceTipoClassificatoreStrutturaAmministrativoContabile;
	}

	/**
	 * @param codiceTipoClassificatoreStrutturaAmministrativoContabile the codiceTipoClassificatoreStrutturaAmministrativoContabile to set
	 */
	public void setCodiceTipoClassificatoreStrutturaAmministrativoContabile(String codiceTipoClassificatoreStrutturaAmministrativoContabile) {
		this.codiceTipoClassificatoreStrutturaAmministrativoContabile = codiceTipoClassificatoreStrutturaAmministrativoContabile;
	}

	//SIAC-7858 CM 19/05/2021 Inizio
	/**
	 * @return the flagEntrataDubbiaEsigFCDE
	 */
	public String getFlagEntrataDubbiaEsigFCDE() {
		return flagEntrataDubbiaEsigFCDE;
	}

	/**
	 * @param flagEntrataDubbiaEsigFCDE the flagEntrataDubbiaEsigFCDE to set
	 */
	public void setFlagEntrataDubbiaEsigFCDE(String flagEntrataDubbiaEsigFCDE) {
		this.flagEntrataDubbiaEsigFCDE = flagEntrataDubbiaEsigFCDE;
	}
	//SIAC-7858 CM 19/05/2021 Fine
	/**
	 * @return the flagRilevanteIva
	 */
	public String getFlagRilevanteIva() {
		return flagRilevanteIva;
	}

	/**
	 * @param flagRilevanteIva the flagRilevanteIva to set
	 */
	public void setFlagRilevanteIva(String flagRilevanteIva) {
		this.flagRilevanteIva = flagRilevanteIva;
	}

	/**
	 * @return the attoDiLegge
	 */
	public AttoDiLegge getAttoDiLegge() {
		return attoDiLegge;
	}

	/**
	 * @param attoDiLegge the attoDiLegge to set
	 */
	public void setAttoDiLegge(AttoDiLegge attoDiLegge) {
		this.attoDiLegge = attoDiLegge;
	}

	/**
	 * @return the listaCapitoli
	 */
	public List<ElementoCapitolo> getListaCapitoli() {
		return listaCapitoli;
	}

	/**
	 * @param listaCapitoli the listaCapitoli to set
	 */
	public void setListaCapitoli(List<ElementoCapitolo> listaCapitoli) {
		this.listaCapitoli = listaCapitoli;
	}

	/* Requests */
	
	public boolean isRegionePiemonte() {
		return regionePiemonte;
	}

	public void setRegionePiemonte(boolean regionePiemonte) {
		this.regionePiemonte = regionePiemonte;
	}

	public boolean isDecentrato() {
		return decentrato;
	}

	public void setDecentrato(boolean decentrato) {
		this.decentrato = decentrato;
	}

	public int getUidVariazione() {
		return uidVariazione;
	}

	public void setUidVariazione(int uidVariazione) {
		this.uidVariazione = uidVariazione;
	}

	public String getDirezioneProponente() {
		return direzioneProponente;
	}

	public void setDirezioneProponente(String direzioneProponente) {
		this.direzioneProponente = direzioneProponente;
	}

	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @param comeDatoAggiuntivo definisce se la ricerca debba essere come dato aggiuntivo
	 * 
	 * @return la Request creata
	 * 
	 * @throws IllegalArgumentException nel caso di eccezione nell'injezione dei parametri
	 */
	public RicercaSinteticaCapitoloEntrataGestione creaRequestRicercaSinteticaCapitoloEntrataGestione(boolean comeDatoAggiuntivo) {
		RicercaSinteticaCapitoloEntrataGestione request = creaRequest(RicercaSinteticaCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		
		// Gestione delle due possibilità di ricerca
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEGest = null;
		if(comeDatoAggiuntivo) {
			ricercaSinteticaCapitoloEGest = creaRicercaSinteticaCapitoloEGestComeDatoAggiuntivo();
			request.setParametriPaginazione(creaParametriPaginazioneComeDatoAggiuntivo());
		} else {
			ricercaSinteticaCapitoloEGest = creaRicercaSinteticaCapitoloEGest();
			request.setParametriPaginazione(creaParametriPaginazione());
			request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		}
		request.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEGest);
		request.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CATEGORIA, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II,
				TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR);
		request.setCalcolaTotaleImporti(Boolean.TRUE);
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEntrataGestione request = creaRequest(RicercaPuntualeCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		
		request.setRicercaPuntualeCapitoloEGest(creaRicercaPuntualeCapitoloEGest());
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo la chiave da ricercare
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaDettaglioCapitoloEGest(creaRicercaDettaglioCapitoloEGest(chiaveCapitolo));
		
		return request;
	}
	
	/* Utilita */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Entrata Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri 
	 */
	private RicercaSinteticaCapitoloEGest creaRicercaSinteticaCapitoloEGest() {
		RicercaSinteticaCapitoloEGest utility = new RicercaSinteticaCapitoloEGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setCategoriaCapitolo(getCapitoloEntrataGestione() != null ? getCapitoloEntrataGestione().getCategoriaCapitolo() : null);
		
		// Setting puntuale dei classificatori generici
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico36(), "setCodiceClassificatoreGenerico36");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico37(), "setCodiceClassificatoreGenerico37");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico38(), "setCodiceClassificatoreGenerico38");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico39(), "setCodiceClassificatoreGenerico39");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico40(), "setCodiceClassificatoreGenerico40");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico41(), "setCodiceClassificatoreGenerico41");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico42(), "setCodiceClassificatoreGenerico42");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico43(), "setCodiceClassificatoreGenerico43");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico44(), "setCodiceClassificatoreGenerico44");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico45(), "setCodiceClassificatoreGenerico45");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico46(), "setCodiceClassificatoreGenerico46");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico47(), "setCodiceClassificatoreGenerico47");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico48(), "setCodiceClassificatoreGenerico48");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico49(), "setCodiceClassificatoreGenerico49");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico50(), "setCodiceClassificatoreGenerico50");
		
		// Classificatori
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTitoloEntrata(), "setCodiceTitoloEntrata");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipologiaTitolo(), "setCodiceTipologia");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getCategoriaTipologiaTitolo(), "setCodiceCategoria");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFinanziamento(), "setCodiceTipoFinanziamento");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFondo(), "setCodiceTipoFondo");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getElementoPianoDeiConti(), "setCodicePianoDeiConti");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getSiopeEntrata(), "setCodiceSiopeEntrata");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getRicorrenteEntrata(), "setCodiceRicorrenteEntrata");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getPerimetroSanitarioEntrata(), "setCodicePerimetroSanitarioEntrata");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTransazioneUnioneEuropeaEntrata(), "setCodiceTransazioneUnioneEuropeaEntrata");
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		// Impostazione dell'atto di legge
		injettaAttoDiLeggeNellaRicercaSeValido(utility, attoDiLegge);
		
		// impostazione del capitolo
		injettaCapitoloNellaRicercaSeValido(utility, capitoloEntrataGestione);
		
		// Flags
		injettaStringaRicercaSeValida(utility, flagRilevanteIva, "setFlagRilevanteIva");
		//SIAC-7858 CM 19/05/2021
		injettaStringaRicercaSeValida(utility, flagEntrataDubbiaEsigFCDE, "setFlagEntrataDubbiaEsigFCDE");
		
		// Codici tipo
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreSiope, "setCodiceTipoSiopeEntrata");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreStrutturaAmministrativoContabile, "setCodiceTipoStrutturaAmmCont");
		
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Entrata Gestione, nel caso della ricerca
	 * come dato aggiuntivo.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaSinteticaCapitoloEGest creaRicercaSinteticaCapitoloEGestComeDatoAggiuntivo() {
		RicercaSinteticaCapitoloEGest utility = new RicercaSinteticaCapitoloEGest();
		
		// L'utility creata è la stessa del caso in cui non si sia come dato aggiuntivo, ma ha meno dati popolati
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		
		/* Impostazione del capitolo. Il controllo non sarebbe necessario, ma e' effettuato per sicurezza */
		if(capitoloEntrataGestione != null) {
			utility.setAnnoCapitolo(capitoloEntrataGestione.getAnnoCapitolo());
			utility.setNumeroCapitolo(capitoloEntrataGestione.getNumeroCapitolo());
			utility.setNumeroArticolo(capitoloEntrataGestione.getNumeroArticolo());
			utility.setNumeroUEB(capitoloEntrataGestione.getNumeroUEB());
			utility.setStatoOperativo(capitoloEntrataGestione.getStatoOperativoElementoDiBilancio());
		}
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Puntuale di un Capitolo di Entrata Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloEGest creaRicercaPuntualeCapitoloEGest() {
		RicercaPuntualeCapitoloEGest utility = new RicercaPuntualeCapitoloEGest();
		
		utility.setAnnoCapitolo(capitoloEntrataGestione.getAnnoCapitolo());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(capitoloEntrataGestione.getNumeroCapitolo());
		utility.setNumeroArticolo(capitoloEntrataGestione.getNumeroArticolo());
		utility.setNumeroUEB(capitoloEntrataGestione.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(capitoloEntrataGestione.getStatoOperativoElementoDiBilancio());
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio di un Capitolo di Entrata Gestione.
	 * 
	 * @param chiaveCapitolo la chiave della ricerca
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest creaRicercaDettaglioCapitoloEGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		
		utility.setChiaveCapitolo(chiaveCapitolo);
		
		return utility;
	}

	//SIAC-6884-Confronta capitolo e variazione
	public RicercaDettaglioAnagraficaVariazioneBilancio creaRequestRicercaDettaglioAnagraficaVariazioneBilancio(int uidVariazione) {
		RicercaDettaglioAnagraficaVariazioneBilancio request = creaRequest(RicercaDettaglioAnagraficaVariazioneBilancio.class);
		request.setUidVariazione(uidVariazione);	
		return request;
	}
	
	
	//SIAC-6884-per ricerca capitolo nella variazione
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazione creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione(int uidVar, String tipoCapitolo) {
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = creaRequest(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class);
		request.setUidVariazione(uidVar);
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setTipoCapitolo(tipoCapitolo);
		return request;
	}
	
}
