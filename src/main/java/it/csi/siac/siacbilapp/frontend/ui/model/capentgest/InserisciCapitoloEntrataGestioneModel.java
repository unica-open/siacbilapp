/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * Classe di model per il Capitolo di Entrata Gestione. Contiene una mappatura dei form per l'inserimento del
 * Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
public class InserisciCapitoloEntrataGestioneModel extends CapitoloEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7214749915616190448L;
	
	/* Copia da capitolo gia' esistente */
	private Bilancio bilancioDaCopiare;
	private CapitoloEntrataGestione capitoloDaCopiare;
	
	/* Terza maschera: importi */
	// Anno in corso
	private ImportiCapitoloEG importiCapitoloEntrataGestione0;
	// Anno in corso + 1
	private ImportiCapitoloEG importiCapitoloEntrataGestione1;
	// Anno in corso + 2
	private ImportiCapitoloEG importiCapitoloEntrataGestione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloEntrataGestione capitoloEntrataGestione;
	
	// SIAC-4724
	private TipoCapitolo tipoCapitoloCopia;
	
	/** Costruttore vuoto di default */
	public InserisciCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Inserimento Capitolo Entrata Gestione");
	}
	
	/* Getter e Setter */
	
	/**
	 * @return the bilancioDaCopiare
	 */
	public Bilancio getBilancioDaCopiare() {
		return bilancioDaCopiare;
	}


	/**
	 * @param bilancioDaCopiare the bilancioDaCopiare to set
	 */
	public void setBilancioDaCopiare(Bilancio bilancioDaCopiare) {
		this.bilancioDaCopiare = bilancioDaCopiare;
	}


	/**
	 * @return the capitoloDaCopiare
	 */
	public CapitoloEntrataGestione getCapitoloDaCopiare() {
		return capitoloDaCopiare;
	}


	/**
	 * @param capitoloDaCopiare the capitoloDaCopiare to set
	 */
	public void setCapitoloDaCopiare(CapitoloEntrataGestione capitoloDaCopiare) {
		this.capitoloDaCopiare = capitoloDaCopiare;
	}


	/**
	 * @return the importiCapitoloEntrataGestione0
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione0() {
		return importiCapitoloEntrataGestione0;
	}


	/**
	 * @param importiCapitoloEntrataGestione0 the importiCapitoloEntrataGestione0 to set
	 */
	public void setImportiCapitoloEntrataGestione0(ImportiCapitoloEG importiCapitoloEntrataGestione0) {
		this.importiCapitoloEntrataGestione0 = importiCapitoloEntrataGestione0;
	}


	/**
	 * @return the importiCapitoloEntrataGestione1
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione1() {
		return importiCapitoloEntrataGestione1;
	}


	/**
	 * @param importiCapitoloEntrataGestione1 the importiCapitoloEntrataGestione1 to set
	 */
	public void setImportiCapitoloEntrataGestione1(ImportiCapitoloEG importiCapitoloEntrataGestione1) {
		this.importiCapitoloEntrataGestione1 = importiCapitoloEntrataGestione1;
	}


	/**
	 * @return the importiCapitoloEntrataGestione2
	 */
	public ImportiCapitoloEG getImportiCapitoloEntrataGestione2() {
		return importiCapitoloEntrataGestione2;
	}


	/**
	 * @param importiCapitoloEntrataGestione2 the importiCapitoloEntrataGestione2 to set
	 */
	public void setImportiCapitoloEntrataGestione2(ImportiCapitoloEG importiCapitoloEntrataGestione2) {
		this.importiCapitoloEntrataGestione2 = importiCapitoloEntrataGestione2;
	}


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
	 * @return the tipoCapitoloCopia
	 */
	public TipoCapitolo getTipoCapitoloCopia() {
		return tipoCapitoloCopia;
	}

	/**
	 * @param tipoCapitoloCopia the tipoCapitoloCopia to set
	 */
	public void setTipoCapitoloCopia(TipoCapitolo tipoCapitoloCopia) {
		this.tipoCapitoloCopia = tipoCapitoloCopia;
	}

	/**
	 * Restituisce l'uid per l'aggiornamento.
	 * 
	 * @return	l'uid per l'aggiornamento
	 */
	public int getUidDaAggiornare() {
		return capitoloEntrataGestione == null ? 0 : capitoloEntrataGestione.getUid();
	}
	
	/**
	 * Imposta l'uid per l'aggiornamento.
	 * 
	 * @param uid l'uid del Capitolo da aggiornare
	 */
	public void setUidDaAggiornare(int uid) {
		if(capitoloEntrataGestione == null) {
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setUid(uid);
	}
	
	/* Requests */	
	/**
	 * Restituisce una Request di tipo {@link InserisceCapitoloDiEntrataGestione} a partire dal Model.
	 * 
	 * @param statoOperativoElementoDiBilancio	lo stato operativo del Capitolo
	 * @return 									la Request creata
	 */
	public InserisceCapitoloDiEntrataGestione creaRequestInserisceCapitoloDiEntrataGestione(StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		
		InserisceCapitoloDiEntrataGestione request = new InserisceCapitoloDiEntrataGestione();
		
		capitoloEntrataGestione.setAnnoCapitolo(getBilancio().getAnno());
		capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getBilancio());
		
		// Per controllare lato back-end la presenza della classificazione di bilancio
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getTitoloEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getTipologiaTitolo());
		
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getCategoriaTipologiaTitolo());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getElementoPianoDeiConti());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getEnte());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getStrutturaAmministrativoContabile());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getTipoFinanziamento());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getTipoFondo());
		
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getSiopeEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getRicorrenteEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getPerimetroSanitarioEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataGestione, getTransazioneUnioneEuropeaEntrata());
		
		capitoloEntrataGestione.setListaImportiCapitoloEG(getListaImportiCapitoloEG());
		capitoloEntrataGestione.setClassificatoriGenerici(getListaClassificatoriGenerici());
		
		request.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @param daCopiare	indica se la Request deve essere creata per la ricerca di un Capitolo per verificarne 
	 * 					l'esistenza (<code>false</code>) o per copiarlo (<code>true</code>).
	 * @return 			la Request creata
	 */
	public RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitoloEntrataGestione(boolean daCopiare) {
		CapitoloEntrataGestione capitolo = daCopiare ? capitoloDaCopiare : capitoloEntrataGestione;
		Bilancio bilancio = daCopiare ? getBilancioDaCopiare() : this.getBilancio();
		
		RicercaPuntualeCapitoloEntrataGestione request = new RicercaPuntualeCapitoloEntrataGestione();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloEGest(getRicercaPuntualeCapitoloEGest(capitolo, bilancio));
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloEntrataPrevisione} a partire dal Model.
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloEntrataPrevisione creaRequestRicercaPuntualeCapitoloEntrataPrevisione() {
		RicercaPuntualeCapitoloEntrataPrevisione request = creaRequest(RicercaPuntualeCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloEPrev(getRicercaPuntualeCapitoloEPrev());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEGest(getRicercaDettaglioCapitoloEGest(chiaveCapitolo));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataPrevisione} a partire dal Model.
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEPrev(getRicercaDettaglioCapitoloEPrev(chiaveCapitolo));
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloEntrataPrevisione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloEntrataGestione.setDescrizione(cap.getDescrizione());
		capitoloEntrataGestione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setTitoloEntrata(cap.getTitoloEntrata());
		setTipologiaTitolo(cap.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(cap.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeEntrata(cap.getSiopeEntrata());
		
		capitoloEntrataGestione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloEntrataGestione.setFlagImpegnabile(cap.getFlagImpegnabile());
		
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteEntrata(cap.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(cap.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(cap.getTransazioneUnioneEuropeaEntrata());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloEntrataGestione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloEntrataGestione.setNote(cap.getNote());
		
		// SIAC-5820
		capitoloEntrataGestione.setFlagAccertatoPerCassa(cap.getFlagAccertatoPerCassa());
		
		// Caricamento delle stringhe di utilita'
		valorizzaStringheUtilita();
	}
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloEntrataGestione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloEntrataGestione.setDescrizione(cap.getDescrizione());
		capitoloEntrataGestione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setTitoloEntrata(cap.getTitoloEntrata());
		setTipologiaTitolo(cap.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(cap.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeEntrata(cap.getSiopeEntrata());
		
		capitoloEntrataGestione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloEntrataGestione.setFlagImpegnabile(cap.getFlagImpegnabile());
		
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteEntrata(cap.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(cap.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(cap.getTransazioneUnioneEuropeaEntrata());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloEntrataGestione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloEntrataGestione.setNote(cap.getNote());
		
		// SIAC-5820
		capitoloEntrataGestione.setFlagAccertatoPerCassa(cap.getFlagAccertatoPerCassa());
		
		// Caricamento delle stringhe di utilita'
		valorizzaStringheUtilita();
	}
	
	/**
	 * Costruisce la lista degli Importi Capitolo Uscita Previsione a partire dagli importi del Model.
	 * <br>
	 * Il metodo carica anche gli anni di competenza degli improti, s&iacute; da evitare che vi siano problemi di
	 * non-inizializzazione del periodo durante il caricamento su database.
	 * 
	 * @return la lista creata
	 */
	private List<ImportiCapitoloEG> getListaImportiCapitoloEG() {
		List<ImportiCapitoloEG> lista = new ArrayList<ImportiCapitoloEG>();
		addImportoCapitoloALista(lista, importiCapitoloEntrataGestione0, getAnnoEsercizioInt() + 0);
		addImportoCapitoloALista(lista, importiCapitoloEntrataGestione1, getAnnoEsercizioInt() + 1);
		addImportoCapitoloALista(lista, importiCapitoloEntrataGestione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
	
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Entrata Gestione.
	 * 
	 * @param capitolo il Capitolo da cui ricavare l'utilit&agrave;
	 * @param bilancio il bilancio
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloEGest getRicercaPuntualeCapitoloEGest(CapitoloEntrataGestione capitolo, Bilancio bilancio) {
		RicercaPuntualeCapitoloEGest utility = new RicercaPuntualeCapitoloEGest();
		utility.setAnnoCapitolo(bilancio.getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(capitolo.getNumeroArticolo());
		utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		utility.setNumeroUEB(capitolo.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Entrata Previsione.
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloEPrev getRicercaPuntualeCapitoloEPrev() {
		RicercaPuntualeCapitoloEPrev utility = new RicercaPuntualeCapitoloEPrev();
		utility.setAnnoCapitolo(getBilancioDaCopiare().getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(getCapitoloDaCopiare().getNumeroArticolo());
		utility.setNumeroCapitolo(getCapitoloDaCopiare().getNumeroCapitolo());
		utility.setNumeroUEB(getCapitoloDaCopiare().getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Gestione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest getRicercaDettaglioCapitoloEGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev getRicercaDettaglioCapitoloEPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Injetta i vari dati nel capitolo di uscita previsione.
	 * 
	 * @param capitoloDaResponse il capitolo ottenuto dalla response
	 */
	public void injettaDatiNelCapitolo(CapitoloEntrataGestione capitoloDaResponse) {
		capitoloEntrataGestione.setUid(capitoloDaResponse.getUid());
		capitoloEntrataGestione.setDataCreazione(capitoloDaResponse.getDataCreazione());
		capitoloEntrataGestione.setBilancio(getBilancio());
		
		List<ClassificatoreGenerico> listaClassificatoriGenerici = new ArrayList<ClassificatoreGenerico>();
		listaClassificatoriGenerici.add(getClassificatoreGenerico1());
		listaClassificatoriGenerici.add(getClassificatoreGenerico2());
		listaClassificatoriGenerici.add(getClassificatoreGenerico3());
		listaClassificatoriGenerici.add(getClassificatoreGenerico4());
		listaClassificatoriGenerici.add(getClassificatoreGenerico5());
		listaClassificatoriGenerici.add(getClassificatoreGenerico6());
		listaClassificatoriGenerici.add(getClassificatoreGenerico7());
		listaClassificatoriGenerici.add(getClassificatoreGenerico8());
		listaClassificatoriGenerici.add(getClassificatoreGenerico9());
		listaClassificatoriGenerici.add(getClassificatoreGenerico10());
		listaClassificatoriGenerici.add(getClassificatoreGenerico11());
		listaClassificatoriGenerici.add(getClassificatoreGenerico12());
		listaClassificatoriGenerici.add(getClassificatoreGenerico13());
		listaClassificatoriGenerici.add(getClassificatoreGenerico14());
		listaClassificatoriGenerici.add(getClassificatoreGenerico15());
		
		// Impostazione dei classificatori
		capitoloEntrataGestione.setTitoloEntrata(getTitoloEntrata());
		capitoloEntrataGestione.setTipologiaTitolo(getTipologiaTitolo());
		capitoloEntrataGestione.setCategoriaTipologiaTitolo(getCategoriaTipologiaTitolo());
		capitoloEntrataGestione.setElementoPianoDeiConti(getElementoPianoDeiConti());
		capitoloEntrataGestione.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		capitoloEntrataGestione.setTipoFinanziamento(getTipoFinanziamento());
		capitoloEntrataGestione.setTipoFondo(getTipoFondo());
		capitoloEntrataGestione.setClassificatoriGenerici(listaClassificatoriGenerici);
		
		capitoloEntrataGestione.setSiopeEntrata(getSiopeEntrata());
		capitoloEntrataGestione.setRicorrenteEntrata(getRicorrenteEntrata());
		capitoloEntrataGestione.setPerimetroSanitarioEntrata(getPerimetroSanitarioEntrata());
		capitoloEntrataGestione.setTransazioneUnioneEuropeaEntrata(getTransazioneUnioneEuropeaEntrata());
		
		// Impostazione degli importi
		capitoloEntrataGestione.setImportiCapitoloEG(importiCapitoloEntrataGestione0);
		capitoloEntrataGestione.setListaImportiCapitolo(getListaImportiCapitoloEG());
	}
	
}
