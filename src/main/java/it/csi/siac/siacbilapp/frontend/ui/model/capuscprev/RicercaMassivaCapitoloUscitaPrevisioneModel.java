/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodificheFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;

/**
 * Classe di model per la ricerca massiva del Capitolo di Uscita Previsione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * 
 * @version 1.0.0 09/09/2013
 *
 */
public class RicercaMassivaCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2443598433650957560L;
	
	/* Tabella A */
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
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
	private CapitoloMassivaUscitaPrevisione capitoloMassivaUscitaPrevisione;
	private List<ElementoCapitoloVariazione> listaCapitoloVariazione;
	
	/* Il massimo delle UEB */
	private Integer maxUEB;
	
	/* Ricerca per la variazione */
	private ElementoCapitoloCodifiche elementoCapitoloCodifiche;
	
	/** Costruttore vuoto di default */
	public RicercaMassivaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Ricerca Capitolo Spesa Previsione (Massivo)");
	}
	
	/* Getter e Setter */

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
	 * @return the flagFondoPluriennaleVincolato
	 */
	public String getFlagFondoPluriennaleVincolato() {
		return flagFondoPluriennaleVincolato;
	}

	/**
	 * @param flagFondoPluriennaleVincolato the flagFondoPluriennaleVincolato to set
	 */
	public void setFlagFondoPluriennaleVincolato(String flagFondoPluriennaleVincolato) {
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
	 * @return the capitoloMassivaUscitaPrevisione
	 */
	@JSON(name = "capitoloMassivo")
	public CapitoloMassivaUscitaPrevisione getCapitoloMassivaUscitaPrevisione() {
		return capitoloMassivaUscitaPrevisione;
	}

	/**
	 * @param capitoloMassivaUscitaPrevisione the capitoloMassivaUscitaPrevisione to set
	 */
	public void setCapitoloMassivaUscitaPrevisione(CapitoloMassivaUscitaPrevisione capitoloMassivaUscitaPrevisione) {
		this.capitoloMassivaUscitaPrevisione = capitoloMassivaUscitaPrevisione;
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
	 * Restituisce una Request di tipo {@link RicercaSinteticaMassivaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * 
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaMassivaCapitoloUscitaPrevisione creaRequestRicercaSinteticaMassivaCapitoloUscitaPrevisione() {
		RicercaSinteticaMassivaCapitoloUscitaPrevisione request = creaRequest(RicercaSinteticaMassivaCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloUPrev(creaRicercaSinteticaCapitoloUPrev());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioMassivaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * 
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri
	 */
	public RicercaDettaglioMassivaCapitoloUscitaPrevisione creaRequestRicercaDettaglioMassivaCapitoloUscitaPrevisione() {
		RicercaDettaglioMassivaCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioMassivaCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());
		request.setRicercaSinteticaCapitoloUPrev(creaRicercaSinteticaCapitoloUPrev());
		
		return request;
	}
	
	/* Utilita */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 * 
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri 
	 */
	private RicercaSinteticaCapitoloUPrev creaRicercaSinteticaCapitoloUPrev() {
		RicercaSinteticaCapitoloUPrev utility = new RicercaSinteticaCapitoloUPrev();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setCategoriaCapitolo(getCapitoloUscitaPrevisione() != null ? getCapitoloUscitaPrevisione().getCategoriaCapitolo() : null);
		
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
		injettaCapitoloNellaRicercaSeValido(utility, capitoloUscitaPrevisione);
		
		// Flags
		injettaStringaRicercaSeValida(utility, flagFondoSvalutazioneCrediti, "setFlagFondoSvalutazioneCrediti");
		injettaStringaRicercaSeValida(utility, flagFondoPluriennaleVincolato, "setFlagFondoPluriennaleVincolato");
		injettaStringaRicercaSeValida(utility, flagPerMemoria, "setFlagPerMemoria");
		injettaStringaRicercaSeValida(utility, flagFunzioniDelegate, "setFlagFunzioniDelegate");
		injettaStringaRicercaSeValida(utility, flagRilevanteIva, "setFlagRilevanteIva");
		
		// Codice Tipo Classificatore
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
	public void impostaDatiDaRicercaDettaglio(RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response) {
		List<CapitoloUscitaPrevisione> listaCapitoli = response.getCapitoloMassivaUscitaPrevisione().getElencoCapitoli();
		elementoCapitoloCodifiche = ElementoCapitoloCodificheFactory.getInstancesFromCapitolo(listaCapitoli).get(0);
	}
	
	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		setDescrizioneEditabile(true);
		setDescrizioneArticoloEditabile(true);
	}
	
}
