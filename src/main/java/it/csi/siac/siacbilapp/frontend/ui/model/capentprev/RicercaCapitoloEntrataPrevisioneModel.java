/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la ricerca del Capitolo di Entrata Previsione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * 
 * @version 1.0.0 07/08/2013
 *
 */
public class RicercaCapitoloEntrataPrevisioneModel extends CapitoloEntrataPrevisioneModel {
	
	/** Per la serializzazione  */
	private static final long serialVersionUID = 4138619767885563103L;
	
	/* Tabella A */
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	private String codiceTipoClassificatoreElementoPianoDeiConti;
	private String codiceTipoClassificatoreSiope;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	
	/* Tabella B */
	private String flagPerMemoria;
	private String flagRilevanteIva;
	//SIAC-7858 CM 14/05/2021
	private String flagEntrataDubbiaEsigFCDE;
	
	/* Tabella C */
	private AttoDiLegge attoDiLegge;
	
	//SIAC 6884 per filtro capitoli da associare alla variazione
	private boolean regionePiemonte;
	private boolean decentrato;
	private int uidVariazione;
	private String direzioneProponente;
	//***END SIAC 6884***//
	
	/* Liste */
	private List<ElementoCapitolo> listaCapitoli = new ArrayList<ElementoCapitolo>();
	
	
	//SIAC-7858 CM 18/05/2021 Inizio
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
	//SIAC-7858 CM 18/05/2021 Fine
	
	/** Costruttore vuoto di default */
	public RicercaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Ricerca Capitolo Entrata Previsione");
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

	/**
	 * @return the flagPerMemoria
	 */
	public String getFlagPerMemoria() {
		return flagPerMemoria;
	}

	/**
	 * @param flagPerMemoria the flagPerMemoria to set
	 */
	public void setFlagPerMemoria(String flagPerMemoria) {
		this.flagPerMemoria = flagPerMemoria;
	}

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
	
	
	/* Requests */
	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * 
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri 
	 */
	public RicercaSinteticaCapitoloEntrataPrevisione creaRequestRicercaSinteticaCapitoloEntrataPrevisione() {
		RicercaSinteticaCapitoloEntrataPrevisione request = creaRequest(RicercaSinteticaCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloEPrev(creaRicercaSinteticaCapitoloEPrev());
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CATEGORIA, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II,
				TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR);
		request.setCalcolaTotaleImporti(Boolean.TRUE);
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloEntrataPrevisione creaRequestRicercaPuntualeCapitoloEntrataPrevisione() {
		RicercaPuntualeCapitoloEntrataPrevisione request = creaRequest(RicercaPuntualeCapitoloEntrataPrevisione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaPuntualeCapitoloEPrev(creaRicercaPuntualeCapitoloEPrev());
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo la chiave da ricercare
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaDettaglioCapitoloEPrev(creaRicercaDettaglioCapitoloEPrev(chiaveCapitolo));
		
		return request;
	}
	
	/* Utilita */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Entrata Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 * 
	 * @throws IllegalArgumentException nel caso in cui i dati non siano coerenti
	 */
	private RicercaSinteticaCapitoloEPrev creaRicercaSinteticaCapitoloEPrev() {
		RicercaSinteticaCapitoloEPrev utility = new RicercaSinteticaCapitoloEPrev();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setCategoriaCapitolo(getCapitoloEntrataPrevisione() != null ? getCapitoloEntrataPrevisione().getCategoriaCapitolo() : null);
		
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
		
		// Impostazione del capitolo
		injettaCapitoloNellaRicercaSeValido(utility, capitoloEntrataPrevisione);
		
		// Flags
		injettaStringaRicercaSeValida(utility, flagPerMemoria, "setFlagPerMemoria");
		injettaStringaRicercaSeValida(utility, flagRilevanteIva, "setFlagRilevanteIva");
		//SIAC-7858 CM 18/05/2021
		injettaStringaRicercaSeValida(utility, flagEntrataDubbiaEsigFCDE, "setFlagEntrataDubbiaEsigFCDE");		
		
		// Codici tipo
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreSiope, "setCodiceTipoSiopeEntrata");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreStrutturaAmministrativoContabile, "setCodiceTipoStrutturaAmmCont");
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Puntuale di un Capitolo di Entrata Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloEPrev creaRicercaPuntualeCapitoloEPrev() {
		RicercaPuntualeCapitoloEPrev utility = new RicercaPuntualeCapitoloEPrev();
		
		utility.setAnnoCapitolo(capitoloEntrataPrevisione.getAnnoCapitolo());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(capitoloEntrataPrevisione.getNumeroCapitolo());
		utility.setNumeroArticolo(capitoloEntrataPrevisione.getNumeroArticolo());
		utility.setNumeroUEB(capitoloEntrataPrevisione.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(capitoloEntrataPrevisione.getStatoOperativoElementoDiBilancio());
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio di un Capitolo di Entrata Previsione.
	 * 
	 * @param chiaveCapitolo la chiave della ricerca
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev creaRicercaDettaglioCapitoloEPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		
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
