/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * Classe di model per il Capitolo di Uscita Gestione. Contiene una mappatura dei form per l'inserimento del
 * Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/06/2013
 *
 */
public class InserisciCapitoloUscitaGestioneModel extends CapitoloUscitaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7201685765475559368L;
	
	/* Copia da capitolo gia' esistente */
	private Bilancio bilancioDaCopiare;
	private CapitoloUscitaGestione capitoloDaCopiare;
	
	/* Terza maschera: importi */
	// Anno in corso
	private ImportiCapitoloUG importiCapitoloUscitaGestione0;
	// Anno in corso + 1
	private ImportiCapitoloUG importiCapitoloUscitaGestione1;
	// Anno in corso + 2
	private ImportiCapitoloUG importiCapitoloUscitaGestione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloUscitaGestione capitoloUscitaGestione;
	
	// SIAC-4724
	private TipoCapitolo tipoCapitoloCopia;
	
	//SIAC-6884 Per Discriminare le componenti di default
	private boolean capitoloFondino = false;
	

	
	

	/** Costruttore vuoto di default */
	public InserisciCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Inserimento Capitolo Spesa Gestione");
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
	public CapitoloUscitaGestione getCapitoloDaCopiare() {
		return capitoloDaCopiare;
	}

	/**
	 * @param capitoloDaCopiare the capitoloDaCopiare to set
	 */
	public void setCapitoloDaCopiare(CapitoloUscitaGestione capitoloDaCopiare) {
		this.capitoloDaCopiare = capitoloDaCopiare;
	}

	/**
	 * @return the importiCapitoloUscitaGestione0
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione0() {
		return importiCapitoloUscitaGestione0;
	}

	/**
	 * @param importiCapitoloUscitaGestione0 the importiCapitoloUscitaGestione0 to set
	 */
	public void setImportiCapitoloUscitaGestione0(ImportiCapitoloUG importiCapitoloUscitaGestione0) {
		this.importiCapitoloUscitaGestione0 = importiCapitoloUscitaGestione0;
	}

	/**
	 * @return the importiCapitoloUscitaGestione1
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione1() {
		return importiCapitoloUscitaGestione1;
	}

	/**
	 * @param importiCapitoloUscitaGestione1 the importiCapitoloUscitaGestione1 to set
	 */
	public void setImportiCapitoloUscitaGestione1(ImportiCapitoloUG importiCapitoloUscitaGestione1) {
		this.importiCapitoloUscitaGestione1 = importiCapitoloUscitaGestione1;
	}

	/**
	 * @return the importiCapitoloUscitaGestione2
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione2() {
		return importiCapitoloUscitaGestione2;
	}

	/**
	 * @param importiCapitoloUscitaGestione2 the importiCapitoloUscitaGestione2 to set
	 */
	public void setImportiCapitoloUscitaGestione2(ImportiCapitoloUG importiCapitoloUscitaGestione2) {
		this.importiCapitoloUscitaGestione2 = importiCapitoloUscitaGestione2;
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
		return capitoloUscitaGestione == null ? 0 : capitoloUscitaGestione.getUid();
	}
	
	/**
	 * Imposta l'uid per l'aggiornamento.
	 * 
	 * @param uid l'uid del Capitolo da aggiornare
	 */
	public void setUidDaAggiornare(int uid) {
		if(capitoloUscitaGestione == null) {
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setUid(uid);
	}

	//SIAC-6884
	/**
	 * @return the capitoloFondino
	 */
	public boolean getCapitoloFondino() {
		return capitoloFondino;
	}

	/**
	 * @param capitoloFondino the capitoloFondino to set
	 */
	public void setCapitoloFondino(boolean capitoloFondino) {
		this.capitoloFondino = capitoloFondino;
	}
	
	

	/* Requests */	
	/**
	 * Restituisce una Request di tipo {@link InserisceCapitoloDiUscitaGestione} a partire dal Model.
	 * 
	 * @param statoOperativoElementoDiBilancio	lo stato operativo del Capitolo
	 * @return 									la Request creata
	 */
	public InserisceCapitoloDiUscitaGestione 
			creaRequestInserisceCapitoloDiUscitaGestione(StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		
		InserisceCapitoloDiUscitaGestione request = new InserisceCapitoloDiUscitaGestione();
		
		

		capitoloUscitaGestione.setAnnoCapitolo(getBilancio().getAnno());
		capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		capitoloUscitaGestione.setListaImportiCapitoloUG(getListaImportiCapitoloUG());
		capitoloUscitaGestione.setClassificatoriGenerici(getListaClassificatoriGenerici());
		
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getBilancio());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getEnte());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getElementoPianoDeiConti());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getMissione());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getProgramma());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getTitoloSpesa());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getMacroaggregato());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getStrutturaAmministrativoContabile());
		
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getClassificazioneCofogProgramma(getClassificazioneCofog()));
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getTipoFondo());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getTipoFinanziamento());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getSiopeSpesa());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getRicorrenteSpesa());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getPerimetroSanitarioSpesa());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getTransazioneUnioneEuropeaSpesa());
		impostaIlParametroNelCapitolo(capitoloUscitaGestione, getPoliticheRegionaliUnitarie());
		
		request.setCapitoloUscitaGestione(capitoloUscitaGestione);

		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @param daCopiare	indica se la Request deve essere creata per la ricerca di un Capitolo per verificarne 
	 * 					l'esistenza (<code>false</code>) o per copiarlo (<code>true</code>).
	 * @return 			la Request creata
	 */
	public RicercaPuntualeCapitoloUscitaGestione creaRequestRicercaPuntualeCapitoloUscitaGestione(boolean daCopiare) {
		CapitoloUscitaGestione capitolo = daCopiare ? capitoloDaCopiare : capitoloUscitaGestione;
		Bilancio bilancio = daCopiare ? bilancioDaCopiare : this.getBilancio();
		
		RicercaPuntualeCapitoloUscitaGestione request = new RicercaPuntualeCapitoloUscitaGestione();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloUGest(creaRicercaPuntualeCapitoloUGest(capitolo, bilancio));
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloUscitaPrevisione} a partire dal Model.
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloUscitaPrevisione creaRequestRicercaPuntualeCapitoloUscitaPrevisione() {
		RicercaPuntualeCapitoloUscitaPrevisione request = creaRequest(RicercaPuntualeCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloUPrev(getRicercaPuntualeCapitoloUPrev());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(getRicercaDettaglioCapitoloUGest(chiaveCapitolo));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaPrevisione} a partire dal Model.
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUPrev(getRicercaDettaglioCapitoloUPrev(chiaveCapitolo));
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloUscitaPrevisione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloUscitaGestione.setDescrizione(cap.getDescrizione());
		capitoloUscitaGestione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setMissione(cap.getMissione());
		setProgramma(cap.getProgramma());
		setClassificazioneCofog(cap.getClassificazioneCofog());
		setTitoloSpesa(cap.getTitoloSpesa());
		setMacroaggregato(cap.getMacroaggregato());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeSpesa(cap.getSiopeSpesa());
		
		capitoloUscitaGestione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloUscitaGestione.setFlagImpegnabile(cap.getFlagImpegnabile());
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteSpesa(cap.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(cap.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(cap.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(cap.getPoliticheRegionaliUnitarie());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloUscitaGestione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloUscitaGestione.setFunzDelegateRegione(cap.getFunzDelegateRegione());
		capitoloUscitaGestione.setNote(cap.getNote());
		
		// Caricamento delle stringhe di utilita'
		valorizzaStringheUtilita();
	}
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloUscitaGestione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloUscitaGestione.setDescrizione(cap.getDescrizione());
		capitoloUscitaGestione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setMissione(cap.getMissione());
		setProgramma(cap.getProgramma());
		setClassificazioneCofog(cap.getClassificazioneCofog());
		setTitoloSpesa(cap.getTitoloSpesa());
		setMacroaggregato(cap.getMacroaggregato());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeSpesa(cap.getSiopeSpesa());
		
		capitoloUscitaGestione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloUscitaGestione.setFlagImpegnabile(cap.getFlagImpegnabile());

		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteSpesa(cap.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(cap.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(cap.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(cap.getPoliticheRegionaliUnitarie());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloUscitaGestione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloUscitaGestione.setFunzDelegateRegione(cap.getFunzDelegateRegione());
		capitoloUscitaGestione.setNote(cap.getNote());
		
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
	private List<ImportiCapitoloUG> getListaImportiCapitoloUG() {
		List<ImportiCapitoloUG> lista = new ArrayList<ImportiCapitoloUG>();
		addImportoCapitoloALista(lista, importiCapitoloUscitaGestione0, getAnnoEsercizioInt() + 0);
		addImportoCapitoloALista(lista, importiCapitoloUscitaGestione1, getAnnoEsercizioInt() + 1);
		addImportoCapitoloALista(lista, importiCapitoloUscitaGestione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Uscita Previsione.
	 * 
	 * @param capitolo il Capitolo da cui ricavare l'utilit&agrave;
	 * @param bilancio il Bilancio 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloUGest creaRicercaPuntualeCapitoloUGest(CapitoloUscitaGestione capitolo, Bilancio bilancio) {
		RicercaPuntualeCapitoloUGest utility = new RicercaPuntualeCapitoloUGest();
		utility.setAnnoCapitolo(bilancio.getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(capitolo.getNumeroArticolo());
		utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		utility.setNumeroUEB(capitolo.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Uscita Previsione.
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloUPrev getRicercaPuntualeCapitoloUPrev() {
		RicercaPuntualeCapitoloUPrev utility = new RicercaPuntualeCapitoloUPrev();
		utility.setAnnoCapitolo(getBilancioDaCopiare().getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(getCapitoloDaCopiare().getNumeroArticolo());
		utility.setNumeroCapitolo(getCapitoloDaCopiare().getNumeroCapitolo());
		utility.setNumeroUEB(getCapitoloDaCopiare().getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Gestione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest getRicercaDettaglioCapitoloUGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev getRicercaDettaglioCapitoloUPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Injetta i vari dati nel capitolo di uscita previsione.
	 * 
	 * @param capitoloDaResponse il capitolo ottenuto dalla response
	 */
	public void injettaDatiNelCapitolo(CapitoloUscitaGestione capitoloDaResponse) {
		capitoloUscitaGestione.setUid(capitoloDaResponse.getUid());
		capitoloUscitaGestione.setDataCreazione(capitoloDaResponse.getDataCreazione());
		capitoloUscitaGestione.setBilancio(getBilancio());
		
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
		capitoloUscitaGestione.setMissione(getMissione());
		capitoloUscitaGestione.setProgramma(getProgramma());
		capitoloUscitaGestione.setClassificazioneCofog(getClassificazioneCofog());
		capitoloUscitaGestione.setTitoloSpesa(getTitoloSpesa());
		capitoloUscitaGestione.setMacroaggregato(getMacroaggregato());
		capitoloUscitaGestione.setElementoPianoDeiConti(getElementoPianoDeiConti());
		capitoloUscitaGestione.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		capitoloUscitaGestione.setTipoFinanziamento(getTipoFinanziamento());
		capitoloUscitaGestione.setTipoFondo(getTipoFondo());
		capitoloUscitaGestione.setClassificatoriGenerici(listaClassificatoriGenerici);
		
		capitoloUscitaGestione.setSiopeSpesa(getSiopeSpesa());
		capitoloUscitaGestione.setRicorrenteSpesa(getRicorrenteSpesa());
		capitoloUscitaGestione.setPerimetroSanitarioSpesa(getPerimetroSanitarioSpesa());
		capitoloUscitaGestione.setTransazioneUnioneEuropeaSpesa(getTransazioneUnioneEuropeaSpesa());
		capitoloUscitaGestione.setPoliticheRegionaliUnitarie(getPoliticheRegionaliUnitarie());
		
		// Impostazione degli importi
		capitoloUscitaGestione.setImportiCapitoloUG(importiCapitoloUscitaGestione0);
		capitoloUscitaGestione.setListaImportiCapitolo(getListaImportiCapitoloUG());
	}

}
