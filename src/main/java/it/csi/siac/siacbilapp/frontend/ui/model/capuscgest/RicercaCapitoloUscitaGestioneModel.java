/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la ricerca del Capitolo di Uscita Gestione. Contiene una mappatura dei form 
 * per la ricerca del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * 
 * @version 1.0.0 07/08/2013
 *
 */
public class RicercaCapitoloUscitaGestioneModel extends CapitoloUscitaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6140627712168376122L;
	
	/* Tabella A */
	private CapitoloUscitaGestione capitoloUscitaGestione;
	private String codiceTipoClassificatoreClassificazioneCofog;
	private String codiceTipoClassificatoreElementoPianoDeiConti;
	private String codiceTipoClassificatoreSiope;
	private String codiceTipoClassificatoreStrutturaAmministrativoContabile;
	 
	
	/* Tabella B */
	private String flagFunzioniDelegate;
	private String flagRilevanteIva;
	
	/* Tabella C */
	private AttoDiLegge attoDiLegge;
	
	/* Per le ricerche */
	private List<ElementoCapitolo> listaCapitoli = new ArrayList<ElementoCapitolo>();
	
	/* Stato Operativo */
	private StatoOperativoElementoDiBilancio statoOperativo;
	
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
	
	//SIAC-7349 - SR210/SR230 - MR - STart - Property necessaria per settare il rowspan nella jsp della tabella
	private String tipologiaCapitolo;
	//END SIAC-7349
		
	/** Costruttore vuoto di default */
	public RicercaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Ricerca Capitolo Spesa Gestione");
	}
	
	public String getTipologiaCapitolo() {
		return tipologiaCapitolo;
	}

	public void setTipologiaCapitolo(String tipologiaCapitolo) {
		this.tipologiaCapitolo = tipologiaCapitolo;
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
	 * @return the statoOperativo
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativo() {
		return statoOperativo;
	}

	/**
	 * @param statoOperativo the statoOperativo to set
	 */
	public void setStatoOperativo(StatoOperativoElementoDiBilancio statoOperativo) {
		this.statoOperativo = statoOperativo;
	}
	
	/**
	 * @return the importiComponentiCapitolo
	 */
	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	/**
	 * @param importiComponentiCapitolo the importiComponentiCapitolo to set
	 */
	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		this.importiComponentiCapitolo = importiComponentiCapitolo;
	}

	/**
	 * @return the competenzaComponenti
	 */
	public List<ImportiCapitoloPerComponente> getCompetenzaComponenti() {
		return competenzaComponenti;
	}

	/**
	 * @param competenzaComponenti the competenzaComponenti to set
	 */
	public void setCompetenzaComponenti(List<ImportiCapitoloPerComponente> competenzaComponenti) {
		this.competenzaComponenti = competenzaComponenti;
	}

	/**
	 * @return the residuoComponenti
	 */
	public List<ImportiCapitoloPerComponente> getResiduoComponenti() {
		return residuoComponenti;
	}

	/**
	 * @param residuoComponenti the residuoComponenti to set
	 */
	public void setResiduoComponenti(List<ImportiCapitoloPerComponente> residuoComponenti) {
		this.residuoComponenti = residuoComponenti;
	}

	/**
	 * @return the cassaComponenti
	 */
	public List<ImportiCapitoloPerComponente> getCassaComponenti() {
		return cassaComponenti;
	}

	/**
	 * @param cassaComponenti the cassaComponenti to set
	 */
	public void setCassaComponenti(List<ImportiCapitoloPerComponente> cassaComponenti) {
		this.cassaComponenti = cassaComponenti;
	}

	/**
	 * @return the listaTipoComponenti
	 */
	public List<TipoComponenteImportiCapitolo> getListaTipoComponenti() {
		return listaTipoComponenti;
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
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @param comeDatoAggiuntivo definisce se la ricerca debba essere come dato aggiuntivo
	 * 
	 * @return la Request creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri
	 */
	public RicercaSinteticaCapitoloUscitaGestione creaRequestRicercaSinteticaCapitoloUscitaGestione(boolean comeDatoAggiuntivo) {
		RicercaSinteticaCapitoloUscitaGestione request = creaRequest(RicercaSinteticaCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		
		// Gestione delle due possibilità di ricerca
		RicercaSinteticaCapitoloUGest ricercaSinteticaCapitoloUGest = null;
		if(comeDatoAggiuntivo) {
			ricercaSinteticaCapitoloUGest = creaRicercaSinteticaCapitoloUGestComeDatoAggiuntivo();
			request.setParametriPaginazione(creaParametriPaginazioneComeDatoAggiuntivo());
		} else {
			ricercaSinteticaCapitoloUGest = creaRicercaSinteticaCapitoloUGest();
			request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
			request.setParametriPaginazione(creaParametriPaginazione());
		}
		request.setTipologieClassificatoriRichiesti(TipologiaClassificatore.PROGRAMMA, TipologiaClassificatore.MACROAGGREGATO, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I,
				TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR);
		
		request.setRicercaSinteticaCapitoloUGest(ricercaSinteticaCapitoloUGest);
		request.setCalcolaTotaleImporti(Boolean.TRUE);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloUscitaGestione creaRequestRicercaPuntualeCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUscitaGestione request = creaRequest(RicercaPuntualeCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaPuntualeCapitoloUGest(creaRicercaPuntualeCapitoloUGest());
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo la chiave da ricercare
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		
		request.setRicercaDettaglioCapitoloUGest(creaRicercaDettaglioCapitoloUGest(chiaveCapitolo));
		
		return request;
	}
	
	/* Utilita */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 * 
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
		//SIAC-7192
		injettaCodiceCodificaNellaRicercaSeValida(utility, getRisorsaAccantonata(), "setCodiceRisorsaAccantonata");
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		// Impostazione dell'atto di legge
		injettaAttoDiLeggeNellaRicercaSeValido(utility, attoDiLegge);
		
		// Impostazione del capitolo
		injettaCapitoloNellaRicercaSeValido(utility, capitoloUscitaGestione);
		
		// Flags
		injettaStringaRicercaSeValida(utility, flagFunzioniDelegate, "setFlagFunzioniDelegate");
		injettaStringaRicercaSeValida(utility, flagRilevanteIva, "setFlagRilevanteIva");
		
		// Codici tipo
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreClassificazioneCofog, "setCodiceTipoCofog");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreSiope, "setCodiceTipoSiopeSpesa");
		injettaStringaRicercaSeValida(utility, codiceTipoClassificatoreStrutturaAmministrativoContabile, "setCodiceTipoStrutturaAmmCont");
		
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Gestione, nel caso della ricerca
	 * come dato aggiuntivo.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaSinteticaCapitoloUGest creaRicercaSinteticaCapitoloUGestComeDatoAggiuntivo() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
		// L'utility creata è la stessa del caso in cui non si sia come dato aggiuntivo, ma ha meno dati popolati
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		
		/* Impostazione del capitolo. Il controllo non sarebbe necessario, ma e' effettuato per sicurezza */
		if(capitoloUscitaGestione != null) {
			utility.setAnnoCapitolo(capitoloUscitaGestione.getAnnoCapitolo());
			utility.setNumeroCapitolo(capitoloUscitaGestione.getNumeroCapitolo());
			utility.setNumeroArticolo(capitoloUscitaGestione.getNumeroArticolo());
			utility.setNumeroUEB(capitoloUscitaGestione.getNumeroUEB());
			utility.setStatoOperativo(capitoloUscitaGestione.getStatoOperativoElementoDiBilancio());
		}
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Puntuale di un Capitolo di Uscita Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloUGest creaRicercaPuntualeCapitoloUGest() {
		RicercaPuntualeCapitoloUGest utility = new RicercaPuntualeCapitoloUGest();
		
		utility.setAnnoCapitolo(capitoloUscitaGestione.getAnnoCapitolo());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroCapitolo(capitoloUscitaGestione.getNumeroCapitolo());
		utility.setNumeroArticolo(capitoloUscitaGestione.getNumeroArticolo());
		utility.setNumeroUEB(capitoloUscitaGestione.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(capitoloUscitaGestione.getStatoOperativoElementoDiBilancio());
		
		return utility;
	}
	
	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio di un Capitolo di Uscita Gestione.
	 * 
	 * @param chiaveCapitolo la chiave della ricerca
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest creaRicercaDettaglioCapitoloUGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		
		utility.setChiaveCapitolo(chiaveCapitolo);
		
		return utility;
	}
	
	//SIAC-6881

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
