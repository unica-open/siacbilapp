/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la ricerca del Capitolo di Uscita Previsione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @author Luciano Gallo
 * 
 * @version 1.0.0 09/07/2013
 *
 */
public class RicercaCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2329998296960769722L;
	
	/* Tabella A */
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	private String codiceTipoClassificatoreClassificazioneCofog;
	private String codiceTipoClassificatoreElementoPianoDeiConti;
	private String codiceTipoClassificatoreSiope;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	
	/* Tabella B */
	private String flagPerMemoria;
	private String flagAssegnabile;
	private String flagFunzioniDelegate;
	private String flagRilevanteIva;
	
	/* Tabella C */
	private AttoDiLegge attoDiLegge;
	
	/* Lista dei risultati, convertita */
	private List<ElementoCapitolo> listaCapitoli = new ArrayList<ElementoCapitolo>();
	
	//SIAC-6881
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	private List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
	private List<ImportiCapitoloPerComponente> residuoComponenti = new ArrayList<ImportiCapitoloPerComponente>();
	private List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
	private List<TipoComponenteImportiCapitolo> listaTipoComponenti = new ArrayList<TipoComponenteImportiCapitolo>();
	private int uidCapitoloPerRicercaComponenti;
	
	//SIAC 6884 per filtro capitoli da associare alla variazione
	private boolean regionePiemonte;
	private boolean decentrato;
	private int uidVariazione;
	private String direzioneProponente;
	//***END SIAC 6884***//
	
		
	/** Costruttore vuoto di default */
	public RicercaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Ricerca Capitolo Spesa Previsione");
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
	
	
	
	/**
	 * @return the importiComponentiCapitolo
	 */
	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	/**
	 * @return the competenzaComponenti
	 */
	public List<ImportiCapitoloPerComponente> getCompetenzaComponenti() {
		return competenzaComponenti;
	}

	/**
	 * @return the residuoComponenti
	 */
	public List<ImportiCapitoloPerComponente> getResiduoComponenti() {
		return residuoComponenti;
	}

	/**
	 * @return the cassaComponenti
	 */
	public List<ImportiCapitoloPerComponente> getCassaComponenti() {
		return cassaComponenti;
	}

	/**
	 * @return the listaTipoComponenti
	 */
	public List<TipoComponenteImportiCapitolo> getListaTipoComponenti() {
		return listaTipoComponenti;
	}

	/**
	 * @param importiComponentiCapitolo the importiComponentiCapitolo to set
	 */
	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		this.importiComponentiCapitolo = importiComponentiCapitolo;
	}

	/**
	 * @param competenzaComponenti the competenzaComponenti to set
	 */
	public void setCompetenzaComponenti(List<ImportiCapitoloPerComponente> competenzaComponenti) {
		this.competenzaComponenti = competenzaComponenti;
	}

	/**
	 * @param residuoComponenti the residuoComponenti to set
	 */
	public void setResiduoComponenti(List<ImportiCapitoloPerComponente> residuoComponenti) {
		this.residuoComponenti = residuoComponenti;
	}

	/**
	 * @param cassaComponenti the cassaComponenti to set
	 */
	public void setCassaComponenti(List<ImportiCapitoloPerComponente> cassaComponenti) {
		this.cassaComponenti = cassaComponenti;
	}

	/**
	 * @param listaTipoComponenti the listaTipoComponenti to set
	 */
	public void setListaTipoComponenti(List<TipoComponenteImportiCapitolo> listaTipoComponenti) {
		this.listaTipoComponenti = listaTipoComponenti;
	}

	
	/**
	 * @return the uidCapitoloPerRicercaComponenti
	 */
	public int getUidCapitoloPerRicercaComponenti() {
		return uidCapitoloPerRicercaComponenti;
	}

	/**
	 * @param uidCapitoloPerRicercaComponenti the uidCapitoloPerRicercaComponenti to set
	 */
	public void setUidCapitoloPerRicercaComponenti(int uidCapitoloPerRicercaComponenti) {
		this.uidCapitoloPerRicercaComponenti = uidCapitoloPerRicercaComponenti;
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
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaCapitoloUscitaPrevisione creaRequestRicercaSinteticaCapitoloUscitaPrevisione() {
		RicercaSinteticaCapitoloUscitaPrevisione request = creaRequest(RicercaSinteticaCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloUPrev(creaRicercaSinteticaCapitoloUPrev());
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setTipologieClassificatoriRichiesti(TipologiaClassificatore.PROGRAMMA, TipologiaClassificatore.MACROAGGREGATO, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I,
				TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR);
		request.setCalcolaTotaleImporti(Boolean.TRUE);
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloUscitaPrevisione creaRequestRicercaPuntualeCapitoloUscitaPrevisione() {
		RicercaPuntualeCapitoloUscitaPrevisione request = creaRequest(RicercaPuntualeCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaPuntualeCapitoloUPrev(creaRicercaPuntualeCapitoloUPrev());
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo la chiave da ricercare
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaDettaglioCapitoloUPrev(creaRicercaDettaglioCapitoloUPrev(chiaveCapitolo));
		
		return request;
	}
	
	/* Utilita */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Previsione.
	 * 
	 * @return l'utilit&agrave; creata
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
		injettaStringaRicercaSeValida(utility, flagPerMemoria, "setFlagPerMemoria");
		injettaStringaRicercaSeValida(utility, flagFunzioniDelegate, "setFlagFunzioniDelegate");
		injettaStringaRicercaSeValida(utility, flagRilevanteIva, "setFlagRilevanteIva");
		
		// Codici tipo
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreClassificazioneCofog, "setCodiceTipoCofog");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreSiope, "setCodiceTipoSiopeSpesa");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreStrutturaAmministrativoContabile, "setCodiceTipoStrutturaAmmCont");
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Puntuale di un Capitolo di Uscita Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloUPrev creaRicercaPuntualeCapitoloUPrev() {
		RicercaPuntualeCapitoloUPrev utility = new RicercaPuntualeCapitoloUPrev();
		
		utility.setAnnoCapitolo(capitoloUscitaPrevisione.getAnnoCapitolo());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(capitoloUscitaPrevisione.getNumeroCapitolo());
		utility.setNumeroArticolo(capitoloUscitaPrevisione.getNumeroArticolo());
		utility.setNumeroUEB(capitoloUscitaPrevisione.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(capitoloUscitaPrevisione.getStatoOperativoElementoDiBilancio());
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio di un Capitolo di Uscita Previsione.
	 * 
	 * @param chiaveCapitolo la chiave della ricerca
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev creaRicercaDettaglioCapitoloUPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		
		utility.setChiaveCapitolo(chiaveCapitolo);
		
		return utility;
	}

	/**
	 * Crea request ricerca componente importi capitolo.
	 *
	 * @return the ricerca componente importi capitolo
	 */
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo requestRicerca = creaRequest(RicercaComponenteImportiCapitolo.class);
		requestRicerca.setCapitolo(new CapitoloUscitaPrevisione());
		requestRicerca.getCapitolo().setUid(getUidCapitoloPerRicercaComponenti());
		return requestRicerca;
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
