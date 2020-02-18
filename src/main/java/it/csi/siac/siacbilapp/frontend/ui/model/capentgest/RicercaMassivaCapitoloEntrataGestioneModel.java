/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;

/**
 * Classe di model per la ricerca massiva del Capitolo di Entrata Gestione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * 
 * @version 1.0.0 09/09/2013
 *
 */
public class RicercaMassivaCapitoloEntrataGestioneModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2443598433650957560L;
	
	/* Tabella A */
	private CapitoloEntrataGestione capitoloEntrataGestione;
	private String flagFondoPluriennaleVincolato;
	private String codiceTipoClassificatoreElementoPianoDeiConti;
	private String codiceTipoClassificatoreSiope;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	 
	
	/* Tabella B */
	private String flagPerMemoria;
	private String flagRilevanteIva;
	
	
	/* Tabella C */
	private AttoDiLegge attoDiLegge;
	
	/* Per la ricerca di dettaglio */
	private CapitoloMassivaEntrataGestione capitoloMassivaEntrataGestione;
	private List<ElementoCapitoloVariazione> listaCapitoloVariazione = new ArrayList<ElementoCapitoloVariazione>();
	
	/* Il massimo delle UEB */
	private Integer maxUEB;
	
	/* Ricerca per la variazione */
	private ElementoCapitoloCodifiche elementoCapitoloCodifiche;
	
	/** Costruttore vuoto di default */
	public RicercaMassivaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Ricerca Capitolo Entrata Gestione (Massivo)");
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
	public void setCapitoloEntrataGestione(
			CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
	}

	/**
	 * @return the flagFondoPluriennaleVincolato
	 */
	public String getFlagFondoPluriennaleVincolato() {
		return flagFondoPluriennaleVincolato;
	}

	/**
	 * @param flagFondoPluriennaleVincolato the flagFondoPluriennaleVincolato to set
	 */
	public void setFlagFondoPluriennaleVincolato(
			String flagFondoPluriennaleVincolato) {
		this.flagFondoPluriennaleVincolato = flagFondoPluriennaleVincolato;
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
	 * @return the capitoloMassivaEntrataGestione
	 */
	@JSON(name = "capitoloMassivo")
	public CapitoloMassivaEntrataGestione getCapitoloMassivaEntrataGestione() {
		return capitoloMassivaEntrataGestione;
	}

	/**
	 * @param capitoloMassivaEntrataGestione the capitoloMassivaEntrataGestione to set
	 */
	public void setCapitoloMassivaEntrataGestione(CapitoloMassivaEntrataGestione capitoloMassivaEntrataGestione) {
		this.capitoloMassivaEntrataGestione = capitoloMassivaEntrataGestione;
	}

	/**
	 * @return the listaCapitoloVariazione
	 */
	public List<ElementoCapitoloVariazione> getListaCapitoloVariazione() {
		return listaCapitoloVariazione;
	}

	/**
	 * @param listaCapitoloVariazione the listaCapitoloVariazione to set
	 */
	public void setListaCapitoloVariazione(List<ElementoCapitoloVariazione> listaCapitoloVariazione) {
		this.listaCapitoloVariazione = listaCapitoloVariazione;
	}
	
	/**
	 * @return the maxUEB
	 */
	public Integer getMaxUEB() {
		return maxUEB;
	}

	/**
	 * @param maxUEB the maxUEB to set
	 */
	public void setMaxUEB(Integer maxUEB) {
		this.maxUEB = maxUEB;
	}
	
	/**
	 * @return the elementoCapitoloCodifiche
	 */
	public ElementoCapitoloCodifiche getElementoCapitoloCodifiche() {
		return elementoCapitoloCodifiche;
	}

	/**
	 * @param elementoCapitoloCodifiche the elementoCapitoloCodifiche to set
	 */
	public void setElementoCapitoloCodifiche(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		this.elementoCapitoloCodifiche = elementoCapitoloCodifiche;
	}
	
	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaMassivaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaMassivaCapitoloEntrataGestione creaRequestRicercaSinteticaMassivaCapitoloEntrataGestione() {
		RicercaSinteticaMassivaCapitoloEntrataGestione request = creaRequest(RicercaSinteticaMassivaCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloEntrata(creaRicercaSinteticaCapitoloEGest());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri
	 */
	public RicercaDettaglioMassivaCapitoloEntrataGestione creaRequestRicercaDettaglioMassivaCapitoloEntrataGestione() {
		RicercaDettaglioMassivaCapitoloEntrataGestione request = creaRequest(RicercaDettaglioMassivaCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloEGest(creaRicercaSinteticaCapitoloEGest());
		
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
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico1(), "setCodiceClassificatoreGenerico1");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico2(), "setCodiceClassificatoreGenerico2");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico3(), "setCodiceClassificatoreGenerico3");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico4(), "setCodiceClassificatoreGenerico4");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico5(), "setCodiceClassificatoreGenerico5");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico6(), "setCodiceClassificatoreGenerico6");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico7(), "setCodiceClassificatoreGenerico7");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico8(), "setCodiceClassificatoreGenerico8");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico9(), "setCodiceClassificatoreGenerico9");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico10(), "setCodiceClassificatoreGenerico10");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico11(), "setCodiceClassificatoreGenerico11");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico12(), "setCodiceClassificatoreGenerico12");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico13(), "setCodiceClassificatoreGenerico13");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico14(), "setCodiceClassificatoreGenerico14");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico15(), "setCodiceClassificatoreGenerico15");
		
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
		
		// Codici tipo
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreSiope, "setCodiceTipoSiopeEntrata");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreStrutturaAmministrativoContabile, "setCodiceTipoStrutturaAmmCont");
		
		return utility;
	}
	
	/**
	 * Imposta i dati della ricerca dettaglio nel wrapper per la variazione di importi.
	 * 
	 * @param response la response da cui popolare il wrapper
	 */
	public void impostaDatiDaRicercaDettaglio(RicercaDettaglioMassivaCapitoloEntrataGestioneResponse response) {
		List<CapitoloEntrataGestione> listaCapitoli = response.getCapitoloMassivaEntrataGestione().getElencoCapitoli();
		elementoCapitoloCodifiche = ElementoCapitoloCodificheFactory.getInstancesFromCapitolo(listaCapitoli).get(0);
	}
	
	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		setDescrizioneEditabile(true);
		setDescrizioneArticoloEditabile(true);
	}
	
}
