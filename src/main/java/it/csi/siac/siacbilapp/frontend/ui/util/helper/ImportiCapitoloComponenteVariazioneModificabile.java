/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.helper;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

public enum ImportiCapitoloComponenteVariazioneModificabile {
	
	FRESCO_IN_PREVISIONE(ApplicazioneVariazione.PREVISIONE, MacrotipoComponenteImportiCapitolo.FRESCO, null, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	AVANZO_IN_PREVISIONE(ApplicazioneVariazione.PREVISIONE, MacrotipoComponenteImportiCapitolo.AVANZO, null, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.FALSE, Boolean.FALSE),
	FPV_BILANCIO_IN_PREVISIONE(ApplicazioneVariazione.PREVISIONE, MacrotipoComponenteImportiCapitolo.FPV, SottotipoComponenteImportiCapitolo.PROGRAMMATO_NON_IMPEGNATO, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.FALSE, Boolean.TRUE),
	//SIAC-7782
	FPV_CUMULATO_IN_PREVISIONE(ApplicazioneVariazione.PREVISIONE, MacrotipoComponenteImportiCapitolo.FPV, SottotipoComponenteImportiCapitolo.CUMULATO, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	FPV_APPLICATO_IN_PREVISIONE(ApplicazioneVariazione.PREVISIONE, MacrotipoComponenteImportiCapitolo.FPV, SottotipoComponenteImportiCapitolo.APPLICATO, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	DA_ATTRIBUIRE_IN_PREVISIONE(ApplicazioneVariazione.PREVISIONE, MacrotipoComponenteImportiCapitolo.DA_ATTRIBUIRE, null, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	
	FRESCO_IN_GESTIONE(ApplicazioneVariazione.GESTIONE, MacrotipoComponenteImportiCapitolo.FRESCO, null, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	AVANZO_IN_GESTIONE(ApplicazioneVariazione.GESTIONE, MacrotipoComponenteImportiCapitolo.AVANZO, null, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.FALSE),
	FPV_BILANCIO_IN_GESTIONE(ApplicazioneVariazione.GESTIONE, MacrotipoComponenteImportiCapitolo.FPV, SottotipoComponenteImportiCapitolo.PROGRAMMATO_NON_IMPEGNATO, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.FALSE, Boolean.FALSE),
	//SIAC-7782
	FPV_CUMULATO_IN_GESTIONE(ApplicazioneVariazione.GESTIONE, MacrotipoComponenteImportiCapitolo.FPV, SottotipoComponenteImportiCapitolo.CUMULATO, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	FPV_APPLICATO_IN_GESTIONE(ApplicazioneVariazione.GESTIONE, MacrotipoComponenteImportiCapitolo.FPV, SottotipoComponenteImportiCapitolo.APPLICATO, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	DA_ATTRIBUIRE_IN_GESTIONE(ApplicazioneVariazione.GESTIONE, MacrotipoComponenteImportiCapitolo.DA_ATTRIBUIRE, null, TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO, Boolean.TRUE, Boolean.TRUE),
	;
	private ApplicazioneVariazione applicazioneVariazione;
	private MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo;
	private SottotipoComponenteImportiCapitolo sottotipoComponenteImportiCapitolo;
	private TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo;
	private Boolean inserimentoAnno0;
	private Boolean inserimentoAnniSuccessivi;
	
	
	
	private ImportiCapitoloComponenteVariazioneModificabile(ApplicazioneVariazione applicazioneVariazione,MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo,
			SottotipoComponenteImportiCapitolo sottotipoComponenteImportiCapitolo, TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo,
			Boolean inserimentoAnno0, Boolean inserimentoAnniSuccessivi) {
		this.applicazioneVariazione = applicazioneVariazione;
		this.macrotipoComponenteImportiCapitolo = macrotipoComponenteImportiCapitolo;
		this.sottotipoComponenteImportiCapitolo = sottotipoComponenteImportiCapitolo;
		this.tipoDettaglioComponenteImportiCapitolo = tipoDettaglioComponenteImportiCapitolo;
		this.inserimentoAnno0 = inserimentoAnno0;
		this.inserimentoAnniSuccessivi = inserimentoAnniSuccessivi;
	}
	
	/**
	 * @return the macrotipoComponenteImportiCapitolo
	 */
	public ApplicazioneVariazione getApplicazioneVariazione() {
		return this. applicazioneVariazione;
	}
	
	/**
	 * @return the macrotipoComponenteImportiCapitolo
	 */
	public MacrotipoComponenteImportiCapitolo getMacrotipoComponenteImportiCapitolo() {
		return this.macrotipoComponenteImportiCapitolo;
	}



	/**
	 * @return the sottotipoComponenteImportiCapitolo
	 */
	public SottotipoComponenteImportiCapitolo getSottotipoComponenteImportiCapitolo() {
		return this.sottotipoComponenteImportiCapitolo;
	}



	/**
	 * @return the tipoDettaglioComponenteImportiCapitolo
	 */
	public TipoDettaglioComponenteImportiCapitolo getTipoDettaglioComponenteImportiCapitolo() {
		return this.tipoDettaglioComponenteImportiCapitolo;
	}



	/**
	 * @return the inserimentoAnno0
	 */
	public Boolean getInserimentoAnno0() {
		return inserimentoAnno0;
	}



	/**
	 * @return the inserimentoAnniSuccessivi
	 */
	public Boolean getInserimentoAnniSuccessivi() {
		return inserimentoAnniSuccessivi;
	}
	
	/**
	 * Checks if is almeno un importo modificabile.
	 *
	 * @return true, if is almeno un importo modificabile
	 */
	public boolean isAlmenoUnAnnualitaModificabile() {
		return Boolean.TRUE.equals(inserimentoAnno0) || Boolean.TRUE.equals(inserimentoAnniSuccessivi); 
	}
	
	public static boolean isDigitabile(ApplicazioneVariazione applicazioneVariazione,TipoComponenteImportiCapitolo tipoComponente, TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo) {
		ImportiCapitoloComponenteVariazioneModificabile importiCapitoloModificabile = getImportiCapitoloModificabile(applicazioneVariazione, tipoComponente.getMacrotipoComponenteImportiCapitolo(),	tipoComponente.getSottotipoComponenteImportiCapitolo(), tipoDettaglioComponenteImportiCapitolo);
		return importiCapitoloModificabile != null && Boolean.TRUE.equals(importiCapitoloModificabile.inserimentoAnno0);
	}



	/**
	 * Gets the importi capitolo modificabile.
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @param macrotipoComponenteImportiCapitolo the macrotipo componente importi capitolo
	 * @param sottotipoComponenteImportiCapitolo the sottotipo componente importi capitolo
	 * @param tipoDettaglioComponenteImportiCapitolo the tipo dettaglio componente importi capitolo
	 * @return the importi capitolo modificabile
	 */
	public static ImportiCapitoloComponenteVariazioneModificabile getImportiCapitoloModificabile(ApplicazioneVariazione applicazioneVariazione,MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo,	SottotipoComponenteImportiCapitolo sottotipoComponenteImportiCapitolo, TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo){
		for (ImportiCapitoloComponenteVariazioneModificabile imp : ImportiCapitoloComponenteVariazioneModificabile.values()) {
			if(applicazioneVariazione.name().equals(imp.getApplicazioneVariazione().name()) && imp.getMacrotipoComponenteImportiCapitolo().equals(macrotipoComponenteImportiCapitolo)
					&& (imp.getSottotipoComponenteImportiCapitolo() == null || imp.getSottotipoComponenteImportiCapitolo().equals(sottotipoComponenteImportiCapitolo))
					&& (imp.getTipoDettaglioComponenteImportiCapitolo().equals(tipoDettaglioComponenteImportiCapitolo))
					
					) {
				
				return imp;
				
			}
			
		}
		return null;
	}
	
	
	/**
	 * Ottiene i macrotipi che non sono modificabili
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the macrotipi non digitabili
	 */
	public static List<MacrotipoComponenteImportiCapitolo> getMacrotipiNonDigitabiliSuiTreAnni(ApplicazioneVariazione applicazioneVariazione){
		List<MacrotipoComponenteImportiCapitolo> lista = new ArrayList<MacrotipoComponenteImportiCapitolo>();
		for (ImportiCapitoloComponenteVariazioneModificabile value : ImportiCapitoloComponenteVariazioneModificabile.values()) {
			if(value.getApplicazioneVariazione().equals(applicazioneVariazione) && value.getSottotipoComponenteImportiCapitolo() == null && !value.isAlmenoUnAnnualitaModificabile()) {
				lista.add(value.getMacrotipoComponenteImportiCapitolo());
			}
		}
		return lista;
	}
	
	/**
	 * Gets the array macrotipi non digitabili.
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the array macrotipi non digitabili
	 */
	public static MacrotipoComponenteImportiCapitolo[] getArrayMacrotipiNonDigitabiliSuiTreAnni(ApplicazioneVariazione applicazioneVariazione){
		List<MacrotipoComponenteImportiCapitolo> lista = getMacrotipiNonDigitabiliSuiTreAnni(applicazioneVariazione);
		MacrotipoComponenteImportiCapitolo[] itemsArray = new MacrotipoComponenteImportiCapitolo[lista.size()];
		itemsArray = lista.toArray(itemsArray);
		return itemsArray;
	}
	
	/**
	 * Ottiene i macrotipi che non sono modificabili
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the macrotipi non digitabili
	 */
	public static List<SottotipoComponenteImportiCapitolo> getSottoTipiNonDigitabiliSuiTreAnni(ApplicazioneVariazione applicazioneVariazione){
		List<SottotipoComponenteImportiCapitolo> lista = new ArrayList<SottotipoComponenteImportiCapitolo>();
		for (ImportiCapitoloComponenteVariazioneModificabile value : ImportiCapitoloComponenteVariazioneModificabile.values()) {
			if(value.getApplicazioneVariazione().equals(applicazioneVariazione) && value.getSottotipoComponenteImportiCapitolo() != null && !value.isAlmenoUnAnnualitaModificabile()) {
				lista.add(value.getSottotipoComponenteImportiCapitolo());
			}
		}
		return lista;
	}
	
	/**
	 * Gets the array macrotipi non digitabili.
	 *
	 * @param applicazioneVariazione the applicazione variazione
	 * @return the array macrotipi non digitabili
	 */
	public static SottotipoComponenteImportiCapitolo[] getArraySottoTipiNonDigitabiliSuiTreAnni(ApplicazioneVariazione applicazioneVariazione){
		List<SottotipoComponenteImportiCapitolo> lista = getSottoTipiNonDigitabiliSuiTreAnni(applicazioneVariazione);
		SottotipoComponenteImportiCapitolo[] itemsArray = new SottotipoComponenteImportiCapitolo[lista.size()];
		itemsArray = lista.toArray(itemsArray);
		return itemsArray;
	}
	
	
}

