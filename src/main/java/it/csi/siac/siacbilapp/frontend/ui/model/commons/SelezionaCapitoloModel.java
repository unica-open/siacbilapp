/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per la selezione del Capitolo . Contiene una mappatura dei form 
 * sul modale di ricerca del Capitolo sul modale.
 * 
 * @author Alessandra Osorio
 * 
 * @version 1.0.0 09/04/2014
 *
 */
public class SelezionaCapitoloModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1994966519970639955L;
	
	/* form anno, capitolo, articolo, ueb */
	@SuppressWarnings("rawtypes")
	private Capitolo capitolo;
	
	// SIAC-4088
	private Boolean richiediAccantonamentoFondiDubbiaEsigibilita;
	
	/* Liste */
	private List<ElementoCapitolo> listaCapitoli = new ArrayList<ElementoCapitolo>();
	
	
	/** Costruttore vuoto di default */
	public SelezionaCapitoloModel() {
		super();
	}
	
	/* Getter e Setter */
	
	/**
	 * @return the capitoloEntrataGestione
	 */
	@SuppressWarnings("rawtypes")
	public Capitolo getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitoloEntrataGestione to set
	 */
	@SuppressWarnings("rawtypes")
	public void setCapitolo(Capitolo capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the richiediAccantonamentoFondiDubbiaEsigibilita
	 */
	public Boolean getRichiediAccantonamentoFondiDubbiaEsigibilita() {
		return richiediAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * @param richiediAccantonamentoFondiDubbiaEsigibilita the richiediAccantonamentoFondiDubbiaEsigibilita to set
	 */
	public void setRichiediAccantonamentoFondiDubbiaEsigibilita(Boolean richiediAccantonamentoFondiDubbiaEsigibilita) {
		this.richiediAccantonamentoFondiDubbiaEsigibilita = richiediAccantonamentoFondiDubbiaEsigibilita;
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
	
	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloEntrataGestione} a partire dal Model.
	 * @param importiDerivatiRichiesti gli importi derivati richiesti
	 * @return la Request creata
	 */
	public RicercaSinteticaCapitoloEntrataGestione creaRequestRicercaSinteticaCapitoloEntrataGestione(Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		RicercaSinteticaCapitoloEntrataGestione request = creaRequest(RicercaSinteticaCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		
		// Gestione delle due possibilità di ricerca
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEGest = creaRicercaSinteticaCapitoloEGest();
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEGest);
		request.setImportiDerivatiRichiesti(importiDerivatiRichiesti);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloEntrataPrevisione} a partire dal Model.
	 * @param importiDerivatiRichiesti gli importi derivati richiesti
	 * @return la Request creata
	 */
	public RicercaSinteticaCapitoloEntrataPrevisione creaRequestRicercaSinteticaCapitoloEntrataPrevisione(Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		RicercaSinteticaCapitoloEntrataPrevisione request = creaRequest(RicercaSinteticaCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		
		// Previsione delle due possibilità di ricerca
		RicercaSinteticaCapitoloEPrev ricercaSinteticaCapitoloEPrev = creaRicercaSinteticaCapitoloEPrev();
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloEPrev(ricercaSinteticaCapitoloEPrev);
		request.setImportiDerivatiRichiesti(importiDerivatiRichiesti);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloUscitaGestione} a partire dal Model.
	 * @param importiDerivatiRichiesti gli importi derivati richiesti
	 * @return la Request creata
	 */
	public RicercaSinteticaCapitoloUscitaGestione creaRequestRicercaSinteticaCapitoloUscitaGestione(Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		RicercaSinteticaCapitoloUscitaGestione request = creaRequest(RicercaSinteticaCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		
		// Gestione delle due possibilità di ricerca
		RicercaSinteticaCapitoloUGest ricercaSinteticaCapitoloUGest = creaRicercaSinteticaCapitoloUGest();
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloUGest(ricercaSinteticaCapitoloUGest);
		request.setImportiDerivatiRichiesti(importiDerivatiRichiesti);
		
		return request;
	}


	/**
	 * Restituisce una Request di tipo {@link RicercaSinteticaCapitoloUscitaPrevisione} a partire dal Model.
	 * @param importiDerivatiRichiesti gli importi derivati richiesti
	 * @return la Request creata
	 */
	public RicercaSinteticaCapitoloUscitaPrevisione creaRequestRicercaSinteticaCapitoloUscitaPrevisione(Set<ImportiCapitoloEnum> importiDerivatiRichiesti) {
		RicercaSinteticaCapitoloUscitaPrevisione request = creaRequest(RicercaSinteticaCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		
		// Previsione delle due possibilità di ricerca
		RicercaSinteticaCapitoloUPrev ricercaSinteticaCapitoloUPrev = creaRicercaSinteticaCapitoloUPrev();
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloUPrev(ricercaSinteticaCapitoloUPrev);
		request.setImportiDerivatiRichiesti(importiDerivatiRichiesti);
		
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
		
		// Classificatori
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFinanziamento(), "setCodiceTipoFinanziamento");
		utility.setCodicePianoDeiConti(extractCodice(getElementoPianoDeiConti()));

		// SIAC-4470
		utility.setCodiceTitoloEntrata(extractCodice(getTitoloEntrata()));
		utility.setCodiceTipologia(extractCodice(getTipologiaTitolo()));
		utility.setCodiceCategoria(extractCodice(getCategoriaTipologiaTitolo()));
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		
		// impostazione del capitolo
		if(capitolo != null) {
			utility.setAnnoCapitolo(capitolo.getAnnoCapitolo());
			utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			utility.setNumeroArticolo(capitolo.getNumeroArticolo());
			if(isGestioneUEB()) {
				utility.setNumeroUEB(capitolo.getNumeroUEB());
			}
			utility.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		}
		
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Entrata Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 * @throws IllegalArgumentException nel caso di errore nell'injezione dei parametri 
	 */
	private RicercaSinteticaCapitoloEPrev creaRicercaSinteticaCapitoloEPrev() {
		RicercaSinteticaCapitoloEPrev utility = new RicercaSinteticaCapitoloEPrev();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setRichiediAccantonamentoFondiDubbiaEsigibilita(getRichiediAccantonamentoFondiDubbiaEsigibilita());
		
		// Classificatori
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFinanziamento(), "setCodiceTipoFinanziamento");
		utility.setCodicePianoDeiConti(extractCodice(getElementoPianoDeiConti()));
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		// SIAC-4470
		utility.setCodiceTitoloEntrata(extractCodice(getTitoloEntrata()));
		utility.setCodiceTipologia(extractCodice(getTipologiaTitolo()));
		utility.setCodiceCategoria(extractCodice(getCategoriaTipologiaTitolo()));
		
		
		// impostazione del capitolo
		if(capitolo != null) {
			utility.setAnnoCapitolo(capitolo.getAnnoCapitolo());
			utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			utility.setNumeroArticolo(capitolo.getNumeroArticolo());
			if(isGestioneUEB()) {
				utility.setNumeroUEB(capitolo.getNumeroUEB());
			}
			utility.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		}
		
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Gestione.
	 * 
	 * @return l'utilit&agrave; creata
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri 
	 */
	private RicercaSinteticaCapitoloUGest creaRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		
		// Classificatori
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFinanziamento(), "setCodiceTipoFinanziamento");
		utility.setCodicePianoDeiConti(extractCodice(getElementoPianoDeiConti()));
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		
		// impostazione del capitolo
		if(capitolo != null) {
			utility.setAnnoCapitolo(capitolo.getAnnoCapitolo());
			utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			utility.setNumeroArticolo(capitolo.getNumeroArticolo());
			if(isGestioneUEB()) {
				utility.setNumeroUEB(capitolo.getNumeroUEB());
			}
			utility.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		}
		
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Sintetica del Capitolo di Uscita Previsione.
	 * 
	 * @return l'utilit&agrave; creata
	 * @throws IllegalArgumentException in caso di errore nell'injezione dei parametri 
	 */
	private RicercaSinteticaCapitoloUPrev creaRicercaSinteticaCapitoloUPrev() {
		RicercaSinteticaCapitoloUPrev utility = new RicercaSinteticaCapitoloUPrev();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		
		// Classificatori
		injettaCodiceCodificaNellaRicercaSeValida(utility, getTipoFinanziamento(), "setCodiceTipoFinanziamento");
		utility.setCodicePianoDeiConti(extractCodice(getElementoPianoDeiConti()));
		
		// StrutturaAmministrativoContabile
		injettaStrutturaAmministrativoContabileNellaRicercaSeValido(utility, getStrutturaAmministrativoContabile(), getStrutturaAmministrativoResponsabile());
		
		
		// impostazione del capitolo
		if(capitolo != null) {
			utility.setAnnoCapitolo(capitolo.getAnnoCapitolo());
			utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
			utility.setNumeroArticolo(capitolo.getNumeroArticolo());
			if(isGestioneUEB()) {
				utility.setNumeroUEB(capitolo.getNumeroUEB());
			}
			utility.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		}
		
		return utility;
	}
	
	@Override
	protected void injettaStrutturaAmministrativoContabileNellaRicercaSeValido(Serializable request, StrutturaAmministrativoContabile strutturaAmministrativoContabile, String stringaUtilita) {
		if(strutturaAmministrativoContabile == null || strutturaAmministrativoContabile.getUid() == 0 || strutturaAmministrativoContabile.getTipoClassificatore() == null) {
			return;
		}
		ReflectionUtil.invokeSetterMethod(request, "setCodiceStrutturaAmmCont", String.class, strutturaAmministrativoContabile.getCodice());
		
		String codiceTipoStrutturaAmmCont = strutturaAmministrativoContabile.getTipoClassificatore().getCodice();
		ReflectionUtil.invokeSetterMethod(request, "setCodiceTipoStrutturaAmmCont", String.class, codiceTipoStrutturaAmmCont);
	}
	
	/**
	 * Estrae il codice dalla codifica
	 * @param codifica la codifica il cui codice &eacute; da estrarrre
	 * @return il codice
	 */
	private String extractCodice(Codifica codifica) {
		return codifica != null && StringUtils.isNotBlank(codifica.getCodice()) ? codifica.getCodice() : null;
	}
}
