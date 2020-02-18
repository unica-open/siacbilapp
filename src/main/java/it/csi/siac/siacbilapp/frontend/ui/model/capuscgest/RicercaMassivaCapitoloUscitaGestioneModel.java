/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;

/**
 * Classe di model per la ricerca massiva del Capitolo di Uscita Gestione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * 
 * @version 1.0.0 09/09/2013
 *
 */
public class RicercaMassivaCapitoloUscitaGestioneModel extends CapitoloUscitaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2443548233650957560L;
	
	/* Tabella A */
	private CapitoloUscitaGestione capitoloUscitaGestione;
	private String flagFondoPluriennaleVincolato;
	private String codiceTipoClassificatoreClassificazioneCofog;
	private String codiceTipoClassificatoreElementoPianoDeiConti;
	private String codiceTipoClassificatoreSiope;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	
	/* Tabella B */
	private String flagFondoSvalutazioneCrediti;
	private String flagPerMemoria;
	private String flagAssegnabile;
	private String flagFunzioniDelegate;
	private String flagRilevanteIva;
	
	/* Tabella C */
	private AttoDiLegge attoDiLegge;
	
	/* Per la ricerca di dettaglio */
	private CapitoloMassivaUscitaGestione capitoloMassivaUscitaGestione;
	private List<ElementoCapitoloVariazione> listaCapitoloVariazione = new ArrayList<ElementoCapitoloVariazione>();
	
	/* Il massimo delle UEB */
	private Integer maxUEB;
	
	/* Ricerca per la variazione */
	private ElementoCapitoloCodifiche elementoCapitoloCodifiche;
	
	/** Costruttore vuoto di default */
	public RicercaMassivaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Ricerca Capitolo Spesa Gestione (Massivo)");
	}
	
	/* Getter e Setter */

	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * @param capitoloUscitaGestione the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(
			CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
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
	 * @return the codiceTipoClassificatoreClassificazioneCofog
	 */
	public String getCodiceTipoClassificatoreClassificazioneCofog() {
		return codiceTipoClassificatoreClassificazioneCofog;
	}

	/**
	 * @param codiceTipoClassificatoreClassificazioneCofog the codiceTipoClassificatoreClassificazioneCofog to set
	 */
	public void setCodiceTipoClassificatoreClassificazioneCofog(String codiceTipoClassificatoreClassificazioneCofog) {
		this.codiceTipoClassificatoreClassificazioneCofog = codiceTipoClassificatoreClassificazioneCofog;
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
	 * @return the flagFondoSvalutazioneCrediti
	 */
	public String getFlagFondoSvalutazioneCrediti() {
		return flagFondoSvalutazioneCrediti;
	}

	/**
	 * @param flagFondoSvalutazioneCrediti the flagFondoSvalutazioneCrediti to set
	 */
	public void setFlagFondoSvalutazioneCrediti(String flagFondoSvalutazioneCrediti) {
		this.flagFondoSvalutazioneCrediti = flagFondoSvalutazioneCrediti;
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
	 * @return the flagAssegnabile
	 */
	public String getFlagAssegnabile() {
		return flagAssegnabile;
	}

	/**
	 * @param flagAssegnabile the flagAssegnabile to set
	 */
	public void setFlagAssegnabile(String flagAssegnabile) {
		this.flagAssegnabile = flagAssegnabile;
	}

	/**
	 * @return the flagFunzioniDelegate
	 */
	public String getFlagFunzioniDelegate() {
		return flagFunzioniDelegate;
	}

	/**
	 * @param flagFunzioniDelegate the flagFunzioniDelegate to set
	 */
	public void setFlagFunzioniDelegate(String flagFunzioniDelegate) {
		this.flagFunzioniDelegate = flagFunzioniDelegate;
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
	 * @return the capitoloMassivaUscitaGestione
	 */
	@JSON(name = "capitoloMassivo")
	public CapitoloMassivaUscitaGestione getCapitoloMassivaUscitaGestione() {
		return capitoloMassivaUscitaGestione;
	}

	/**
	 * @param capitoloMassivaUscitaGestione the capitoloMassivaUscitaGestione to set
	 */
	public void setCapitoloMassivaUscitaGestione(CapitoloMassivaUscitaGestione capitoloMassivaUscitaGestione) {
		this.capitoloMassivaUscitaGestione = capitoloMassivaUscitaGestione;
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
	 * Restituisce una Request di tipo {@link RicercaSinteticaMassivaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaMassivaCapitoloUscitaGestione creaRequestRicercaSinteticaMassivaCapitoloUscitaGestione() {
		RicercaSinteticaMassivaCapitoloUscitaGestione request = creaRequest(RicercaSinteticaMassivaCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloUGest(creaRicercaSinteticaCapitoloUGest());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	public RicercaDettaglioMassivaCapitoloUscitaGestione creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione() {
		RicercaDettaglioMassivaCapitoloUscitaGestione request = creaRequest(RicercaDettaglioMassivaCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloUGest(creaRicercaSinteticaCapitoloUGest());
		
		return request;
	}
	
	/* Utilita */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri 
	 */
	private RicercaSinteticaCapitoloUGest creaRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setCategoriaCapitolo(getCapitoloUscitaGestione() != null ? getCapitoloUscitaGestione().getCategoriaCapitolo() : null);
		
		// Setting puntuale dei classificatori generici
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico1(),  "setCodiceClassificatoreGenerico1");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico2(),  "setCodiceClassificatoreGenerico2");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico3(),  "setCodiceClassificatoreGenerico3");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico4(),  "setCodiceClassificatoreGenerico4");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico5(),  "setCodiceClassificatoreGenerico5");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico6(),  "setCodiceClassificatoreGenerico6");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico7(),  "setCodiceClassificatoreGenerico7");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico8(),  "setCodiceClassificatoreGenerico8");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico9(),  "setCodiceClassificatoreGenerico9");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico10(), "setCodiceClassificatoreGenerico10");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico11(), "setCodiceClassificatoreGenerico11");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico12(), "setCodiceClassificatoreGenerico12");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico13(), "setCodiceClassificatoreGenerico13");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico14(), "setCodiceClassificatoreGenerico14");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificatoreGenerico15(), "setCodiceClassificatoreGenerico15");
		
		// Classificatori
		injettaCodiceCodificaNellaRicercaSeValida(utility, getMissione(), "setCodiceMissione");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getProgramma(), "setCodiceProgramma");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getClassificazioneCofog(), "setCodiceCofog");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTitoloSpesa(), "setCodiceTitoloUscita");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getMacroaggregato(), "setCodiceMacroaggregato");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFinanziamento(), "setCodiceTipoFinanziamento");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFondo(), "setCodiceTipoFondo");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getElementoPianoDeiConti(), "setCodicePianoDeiConti");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getSiopeSpesa(), "setCodiceSiopeSpesa");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getRicorrenteSpesa(), "setCodiceRicorrenteSpesa");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getPerimetroSanitarioSpesa(), "setCodicePerimetroSanitarioSpesa");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTransazioneUnioneEuropeaSpesa(), "setCodiceTransazioneUnioneEuropeaSpesa");
		injettaCodiceCodificaNellaRicercaSeValida(utility, getPoliticheRegionaliUnitarie(), "setCodicePoliticheRegionaliUnitarie");
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		// Impostazione dell'atto di legge
		injettaAttoDiLeggeNellaRicercaSeValido(utility, attoDiLegge);
		
		// Impostazione del capitolo
		injettaCapitoloNellaRicercaSeValido(utility, capitoloUscitaGestione);
		
		// Flags
		injettaStringaRicercaSeValida(utility, flagFondoSvalutazioneCrediti, "setFlagFondoSvalutazioneCrediti");
		injettaStringaRicercaSeValida(utility, flagFondoPluriennaleVincolato, "setFlagFondoPluriennaleVincolato");
		injettaStringaRicercaSeValida(utility, flagFunzioniDelegate, "setFlagFunzioniDelegate");
		injettaStringaRicercaSeValida(utility, flagRilevanteIva, "setFlagRilevanteIva");
		
		// Codici tipo
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreClassificazioneCofog, "setCodiceTipoCofog");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreSiope, "setCodiceTipoSiopeSpesa");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreStrutturaAmministrativoContabile, "setCodiceTipoStrutturaAmmCont");
		
		return utility;
	}
	
	/**
	 * Imposta i dati della ricerca dettaglio nel wrapper per la variazione di importi.
	 * 
	 * @param response la response da cui popolare il wrapper
	 */
	public void impostaDatiDaRicercaDettaglio(RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response) {
		List<CapitoloUscitaGestione> listaCapitoli = response.getCapitoloMassivaUscitaGestione().getElencoCapitoli();
		elementoCapitoloCodifiche = ElementoCapitoloCodificheFactory.getInstancesFromCapitolo(listaCapitoli).get(0);
	}
	
	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		setDescrizioneEditabile(true);
		setDescrizioneArticoloEditabile(true);
	}
	
}
