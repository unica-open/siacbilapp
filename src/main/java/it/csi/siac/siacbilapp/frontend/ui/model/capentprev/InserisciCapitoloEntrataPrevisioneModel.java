/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TotaliAnnoDiEsercizio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * Classe di model per il Capitolo di Entrata Previsione. Contiene una mappatura dei form per l'inserimento del
 * Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 31/07/2013
 *
 */
public class InserisciCapitoloEntrataPrevisioneModel extends CapitoloEntrataPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7418284100649364213L;
	
	/* Copia da capitolo gia' esistente */
	private Bilancio bilancioDaCopiare;
	private CapitoloEntrataPrevisione capitoloDaCopiare;
	
	/* Terza maschera: importi */
	// Anno in corso
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione0;
	// Anno in corso + 1
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione1;
	// Anno in corso + 2
	private ImportiCapitoloEP importiCapitoloEntrataPrevisione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	/* Maschera Totali di Previsione */
	private TotaliAnnoDiEsercizio totaliEsercizio0;
	private TotaliAnnoDiEsercizio totaliEsercizio1;
	private TotaliAnnoDiEsercizio totaliEsercizio2;
	
	// SIAC-4724
	private TipoCapitolo tipoCapitoloCopia;
	
	//SIAC-7858 CM 18/05/2021 Inizio
	private boolean flagEntrataDubbiaEsigFCDE;
	
	/**
	 * @return the flagEntrataDubbiaEsigFCDE
	 */
	public boolean isFlagEntrataDubbiaEsigFCDE() {
		return flagEntrataDubbiaEsigFCDE;
	}

	/**
	 * @param flagEntrataDubbiaEsigFCDE the flagEntrataDubbiaEsigFCDE to set
	 */
	public void setFlagEntrataDubbiaEsigFCDE(boolean flagEntrataDubbiaEsigFCDE) {
		this.flagEntrataDubbiaEsigFCDE = flagEntrataDubbiaEsigFCDE;
	}
	//SIAC-7858 CM 18/05/2021 Fine

	/** Costruttore vuoto di default */
	public InserisciCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Inserimento Capitolo Entrata Previsione");
	}

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
	public CapitoloEntrataPrevisione getCapitoloDaCopiare() {
		return capitoloDaCopiare;
	}

	/**
	 * @param capitoloDaCopiare the capitoloDaCopiare to set
	 */
	public void setCapitoloDaCopiare(CapitoloEntrataPrevisione capitoloDaCopiare) {
		this.capitoloDaCopiare = capitoloDaCopiare;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione0
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione0() {
		return importiCapitoloEntrataPrevisione0;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione0 the importiCapitoloEntrataPrevisione0 to set
	 */
	public void setImportiCapitoloEntrataPrevisione0(ImportiCapitoloEP importiCapitoloEntrataPrevisione0) {
		this.importiCapitoloEntrataPrevisione0 = importiCapitoloEntrataPrevisione0;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione1
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione1() {
		return importiCapitoloEntrataPrevisione1;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione1 the importiCapitoloEntrataPrevisione1 to set
	 */
	public void setImportiCapitoloEntrataPrevisione1(ImportiCapitoloEP importiCapitoloEntrataPrevisione1) {
		this.importiCapitoloEntrataPrevisione1 = importiCapitoloEntrataPrevisione1;
	}

	/**
	 * @return the importiCapitoloEntrataPrevisione2
	 */
	public ImportiCapitoloEP getImportiCapitoloEntrataPrevisione2() {
		return importiCapitoloEntrataPrevisione2;
	}

	/**
	 * @param importiCapitoloEntrataPrevisione2 the importiCapitoloEntrataPrevisione2 to set
	 */
	public void setImportiCapitoloEntrataPrevisione2(ImportiCapitoloEP importiCapitoloEntrataPrevisione2) {
		this.importiCapitoloEntrataPrevisione2 = importiCapitoloEntrataPrevisione2;
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
	 * @return the totaliEsercizio0
	 */
	public TotaliAnnoDiEsercizio getTotaliEsercizio0() {
		return totaliEsercizio0;
	}

	/**
	 * @param totaliEsercizio0 the totaliEsercizio0 to set
	 */
	public void setTotaliEsercizio0(TotaliAnnoDiEsercizio totaliEsercizio0) {
		this.totaliEsercizio0 = totaliEsercizio0;
	}

	/**
	 * @return the totaliEsercizio1
	 */
	public TotaliAnnoDiEsercizio getTotaliEsercizio1() {
		return totaliEsercizio1;
	}

	/**
	 * @param totaliEsercizio1 the totaliEsercizio1 to set
	 */
	public void setTotaliEsercizio1(TotaliAnnoDiEsercizio totaliEsercizio1) {
		this.totaliEsercizio1 = totaliEsercizio1;
	}

	/**
	 * @return the totaliEsercizio2
	 */
	public TotaliAnnoDiEsercizio getTotaliEsercizio2() {
		return totaliEsercizio2;
	}

	/**
	 * @param totaliEsercizio2 the totaliEsercizio2 to set
	 */
	public void setTotaliEsercizio2(TotaliAnnoDiEsercizio totaliEsercizio2) {
		this.totaliEsercizio2 = totaliEsercizio2;
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

	/* Getter per i BigDecimal, trasformati in String */
	
	/**
	 * @return i totali di stanziamento di competenza di entrata per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaEntrata0() {
		return totaliEsercizio0.getTotStanziamentiCompetenzaEntrata();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di entrata per l'anno corrente + 1
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaEntrata1() {
		return totaliEsercizio1.getTotStanziamentiCompetenzaEntrata();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di entrata per l'anno corrente + 2
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaEntrata2() {
		return totaliEsercizio2.getTotStanziamentiCompetenzaEntrata();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di Entrata per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaSpesa0() {
		return totaliEsercizio0.getTotStanziamentiDiCompetenzaSpesa();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di Entrata per l'anno corrente + 1
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaSpesa1() {
		return totaliEsercizio1.getTotStanziamentiDiCompetenzaSpesa();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di Entrata per l'anno corrente + 2
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaSpesa2() {
		return totaliEsercizio2.getTotStanziamentiDiCompetenzaSpesa();
	}
	
	/**
	 * @return i totali di stanziamento residui di entrata per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiResiduiEntrata0() {
		return totaliEsercizio0.getTotStanziamentiResiduiEntrata();
	}
	
	/**
	 * @return i totali di stanziamento residui di spesa per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiResiduiSpesa0() {
		return totaliEsercizio0.getTotaleStanziamentiResiduiSpesa();
	}
	
	/**
	 * @return i totali di stanziamento di cassa di entrata per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiCassaEntrata0() {
		return totaliEsercizio0.getTotStanziamentiDiCassaEntrata();
	}
	
	/**
	 * @return i totali di stanziamento di cassa di spesa per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiCassaSpesa0() {
		return totaliEsercizio0.getTotaleStanziamentiDiCassaSpesa();
	}
	
	/**
	 * @return la differenza tra gli stanziamenti di competenza di entrata e di spesa dell'anno di esercizio
	 */
	public BigDecimal getDifferenzaStanziamentiCompetenza0() {
		return totaliEsercizio0.getTotStanziamentiCompetenzaEntrata().subtract(totaliEsercizio0.getTotStanziamentiDiCompetenzaSpesa());
	}
	
	/**
	 * @return la differenza tra gli stanziamenti di competenza + 1 di entrata e di spesa dell'anno di esercizio
	 */
	public BigDecimal getDifferenzaStanziamentiCompetenza1() {
		return totaliEsercizio1.getTotStanziamentiCompetenzaEntrata().subtract(totaliEsercizio1.getTotStanziamentiDiCompetenzaSpesa());
	}
	
	/**
	 * @return la differenza tra gli stanziamenti di competenza + 2 di entrata e di spesa dell'anno di esercizio
	 */
	public BigDecimal getDifferenzaStanziamentiCompetenza2() {
		return totaliEsercizio2.getTotStanziamentiCompetenzaEntrata().subtract(totaliEsercizio2.getTotStanziamentiDiCompetenzaSpesa());
	}
	
	/**
	 * @return la differenza tra gli stanziamenti residui di entrata e di spesa dell'anno di esercizio
	 */
	public BigDecimal getDifferenzaStanziamentiResidui0() {
		return totaliEsercizio0.getTotStanziamentiResiduiEntrata().subtract(totaliEsercizio0.getTotaleStanziamentiResiduiSpesa());
	}
	
	/**
	 * @return la differenza tra gli stanziamenti di cassa di entrata e di spesa dell'anno di esercizio
	 */
	public BigDecimal getDifferenzaStanziamentiCassa0() {
		return totaliEsercizio0.getTotStanziamentiDiCassaEntrata().subtract(totaliEsercizio0.getTotaleStanziamentiDiCassaSpesa());
	}
	
	/**
	 * Restituisce l'uid per l'aggiornamento.
	 * 
	 * @return	l'uid per l'aggiornamento
	 */
	public int getUidDaAggiornare() {
		return capitoloEntrataPrevisione == null ? 0 : capitoloEntrataPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid per l'aggiornamento.
	 * 
	 * @param uid l'uid del Capitolo da aggiornare
	 */
	public void setUidDaAggiornare(int uid) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setUid(uid);
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link InserisceCapitoloDiEntrataPrevisione} a partire dal Model.
	 * 
	 * @param statoOperativoElementoDiBilancio	lo stato operativo del Capitolo
	 * @return 									la Request creata
	 */
	public InserisceCapitoloDiEntrataPrevisione creaRequestInserisceCapitoloDiEntrataPrevisione(StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		
		InserisceCapitoloDiEntrataPrevisione request = new InserisceCapitoloDiEntrataPrevisione();
		
		capitoloEntrataPrevisione.setAnnoCapitolo(getBilancio().getAnno());
		capitoloEntrataPrevisione.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getBilancio());
		
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getTitoloEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getTipologiaTitolo());
		
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getCategoriaTipologiaTitolo());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getEnte());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getElementoPianoDeiConti());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getStrutturaAmministrativoContabile());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getTipoFinanziamento());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getTipoFondo());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getSiopeEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getRicorrenteEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getPerimetroSanitarioEntrata());
		impostaIlParametroNelCapitolo(capitoloEntrataPrevisione, getTransazioneUnioneEuropeaEntrata());
		
		//SIAC-7517
		//passo i valori di default se non arrivano dal frontend
		if(importiCapitoloEntrataPrevisione0 != null && importiCapitoloEntrataPrevisione0.getStanziamentoResiduo() == null) {
			importiCapitoloEntrataPrevisione0.setStanziamentoResiduo(BigDecimal.ZERO);
		}
		if(importiCapitoloEntrataPrevisione0 != null && importiCapitoloEntrataPrevisione0.getStanziamentoCassa() == null) {
			importiCapitoloEntrataPrevisione0.setStanziamentoCassa(BigDecimal.ZERO);
		}
		//
		
		capitoloEntrataPrevisione.setClassificatoriGenerici(getListaClassificatoriGenerici());
		capitoloEntrataPrevisione.setListaImportiCapitoloEP(getListaImportiCapitoloEP());
		
		// Imposto il flag non presente sulla pagina di aggiornamento
		capitoloEntrataPrevisione.setFlagPerMemoria(Boolean.FALSE);
		
		//SIAC-7858 CM 11/05/2021 Inizio
		capitoloEntrataPrevisione.setFlagEntrataDubbiaEsigFCDE(isFlagEntrataDubbiaEsigFCDE());
		//SIAC-7858 CM 11/05/2021 Fine
		
		request.setCapitoloEntrataPrevisione(capitoloEntrataPrevisione);
		
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @param daCopiare	indica se la Request deve essere creata per la ricerca di un Capitolo per verificarne 
	 * 					l'esistenza (<code>false</code>) o per copiarlo (<code>true</code>).
	 * @return 			la Request creata
	 */
	public RicercaPuntualeCapitoloEntrataPrevisione creaRequestRicercaPuntualeCapitoloEntrataPrevisione(boolean daCopiare) {
		CapitoloEntrataPrevisione capitolo = daCopiare ? capitoloDaCopiare : capitoloEntrataPrevisione;
		Bilancio bilancio = daCopiare ? bilancioDaCopiare : this.getBilancio();
		
		RicercaPuntualeCapitoloEntrataPrevisione request = new RicercaPuntualeCapitoloEntrataPrevisione();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloEPrev(getRicercaPuntualeCapitoloEPrev(capitolo, bilancio));
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloEntrataGestione} a partire dal Model.
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEntrataGestione request = creaRequest(RicercaPuntualeCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloEGest(getRicercaPuntualeCapitoloEGest());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEPrev(getRicercaDettaglioCapitoloEPrev(chiaveCapitolo));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataGestione} a partire dal Model.
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEGest(getRicercaDettaglioCapitoloEGest(chiaveCapitolo));
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloEntrataPrevisione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloEntrataPrevisione.setDescrizione(cap.getDescrizione());
		capitoloEntrataPrevisione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setTitoloEntrata(cap.getTitoloEntrata());
		setTipologiaTitolo(cap.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(cap.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeEntrata(cap.getSiopeEntrata());
		
		capitoloEntrataPrevisione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloEntrataPrevisione.setFlagImpegnabile(cap.getFlagImpegnabile());
		
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteEntrata(cap.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(cap.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(cap.getTransazioneUnioneEuropeaEntrata());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloEntrataPrevisione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloEntrataPrevisione.setNote(cap.getNote());
		
		// SIAC-5820
		capitoloEntrataPrevisione.setFlagAccertatoPerCassa(cap.getFlagAccertatoPerCassa());
		//SIAC-7858 CM 11/05/2021 Inizio
		capitoloEntrataPrevisione.setFlagEntrataDubbiaEsigFCDE(cap.getFlagEntrataDubbiaEsigFCDE());
		//SIAC-7858 CM 11/05/2021 Fine
		
		// Caricamento delle stringhe di utilita'
		valorizzaStringheUtilita();
	}
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloEntrataGestione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloEntrataPrevisione.setDescrizione(cap.getDescrizione());
		capitoloEntrataPrevisione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setTitoloEntrata(cap.getTitoloEntrata());
		setTipologiaTitolo(cap.getTipologiaTitolo());
		setCategoriaTipologiaTitolo(cap.getCategoriaTipologiaTitolo());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeEntrata(cap.getSiopeEntrata());
		
		capitoloEntrataPrevisione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloEntrataPrevisione.setFlagImpegnabile(cap.getFlagImpegnabile());
		
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteEntrata(cap.getRicorrenteEntrata());
		setPerimetroSanitarioEntrata(cap.getPerimetroSanitarioEntrata());
		setTransazioneUnioneEuropeaEntrata(cap.getTransazioneUnioneEuropeaEntrata());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloEntrataPrevisione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloEntrataPrevisione.setNote(cap.getNote());
		
		// SIAC-5820
		capitoloEntrataPrevisione.setFlagAccertatoPerCassa(cap.getFlagAccertatoPerCassa());
		//SIAC-7858 CM 11/05/2021 Inizio
		capitoloEntrataPrevisione.setFlagEntrataDubbiaEsigFCDE(cap.getFlagEntrataDubbiaEsigFCDE());
		//SIAC-7858 CM 11/05/2021 Fine

		// Caricamento delle stringhe di utilita'
		valorizzaStringheUtilita();
	}
	
	/**
	 * Costruisce la lista degli Importi Capitolo Entrata Previsione a partire dagli importi del Model.
	 * <br>
	 * Il metodo carica anche gli anni di competenza degli improti, s&iacute; da evitare che vi siano problemi di
	 * non-inizializzazione del periodo durante il caricamento su database.
	 * 
	 * @return la lista creata
	 */
	private List<ImportiCapitoloEP> getListaImportiCapitoloEP() {
		List<ImportiCapitoloEP> lista = new ArrayList<ImportiCapitoloEP>();
		addImportoCapitoloALista(lista, importiCapitoloEntrataPrevisione0, getAnnoEsercizioInt() + 0);
		addImportoCapitoloALista(lista, importiCapitoloEntrataPrevisione1, getAnnoEsercizioInt() + 1);
		addImportoCapitoloALista(lista, importiCapitoloEntrataPrevisione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Entrata Previsione.
	 * 
	 * @param capitolo il Capitolo da cui ricavare l'utilit&agrave;
	 * @param bilancio il getBilancio()
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloEPrev getRicercaPuntualeCapitoloEPrev(CapitoloEntrataPrevisione capitolo, Bilancio bilancio) {
		RicercaPuntualeCapitoloEPrev utility = new RicercaPuntualeCapitoloEPrev();
		utility.setAnnoCapitolo(bilancio.getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(capitolo.getNumeroArticolo());
		utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		utility.setNumeroUEB(capitolo.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Entrata Gestione.
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloEGest getRicercaPuntualeCapitoloEGest() {
		RicercaPuntualeCapitoloEGest utility = new RicercaPuntualeCapitoloEGest();
		utility.setAnnoCapitolo(getBilancioDaCopiare().getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(getCapitoloDaCopiare().getNumeroArticolo());
		utility.setNumeroCapitolo(getCapitoloDaCopiare().getNumeroCapitolo());
		utility.setNumeroUEB(getCapitoloDaCopiare().getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev getRicercaDettaglioCapitoloEPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Gestione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest getRicercaDettaglioCapitoloEGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Controlla se gli importi debbano essere editabili o meno.
	 * 
	 * @return <code>true</code> se gli importi <em>non</em> sono editabili; <code>false</code> altrimenti
	 */
	public boolean isImportiDisabilitati() {
		return cduChiamante.contains("OP-GESC001-insVar") || cduChiamante.contains("OP-GESC002-aggVar");
	}
	
	/**
	 * Injetta i vari dati nel capitolo di uscita previsione.
	 * 
	 * @param capitoloDaResponse il capitolo ottenuto dalla response
	 */
	public void injettaDatiNelCapitolo(CapitoloEntrataPrevisione capitoloDaResponse) {
		capitoloEntrataPrevisione.setUid(capitoloDaResponse.getUid());
		capitoloEntrataPrevisione.setDataCreazione(capitoloDaResponse.getDataCreazione());
		capitoloEntrataPrevisione.setBilancio(getBilancio());
		
		List<ClassificatoreGenerico> listaClassificatoriGenerici = new ArrayList<ClassificatoreGenerico>();
		listaClassificatoriGenerici.add(getClassificatoreGenerico36());
		listaClassificatoriGenerici.add(getClassificatoreGenerico37());
		listaClassificatoriGenerici.add(getClassificatoreGenerico38());
		listaClassificatoriGenerici.add(getClassificatoreGenerico39());
		listaClassificatoriGenerici.add(getClassificatoreGenerico40());
		listaClassificatoriGenerici.add(getClassificatoreGenerico41());
		listaClassificatoriGenerici.add(getClassificatoreGenerico42());
		listaClassificatoriGenerici.add(getClassificatoreGenerico43());
		listaClassificatoriGenerici.add(getClassificatoreGenerico44());
		listaClassificatoriGenerici.add(getClassificatoreGenerico45());
		listaClassificatoriGenerici.add(getClassificatoreGenerico46());
		listaClassificatoriGenerici.add(getClassificatoreGenerico47());
		listaClassificatoriGenerici.add(getClassificatoreGenerico48());
		listaClassificatoriGenerici.add(getClassificatoreGenerico49());
		listaClassificatoriGenerici.add(getClassificatoreGenerico50());
		
		// Impostazione dei classificatori
		capitoloEntrataPrevisione.setTitoloEntrata(getTitoloEntrata());
		capitoloEntrataPrevisione.setTipologiaTitolo(getTipologiaTitolo());
		capitoloEntrataPrevisione.setCategoriaTipologiaTitolo(getCategoriaTipologiaTitolo());
		capitoloEntrataPrevisione.setElementoPianoDeiConti(getElementoPianoDeiConti());
		capitoloEntrataPrevisione.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		capitoloEntrataPrevisione.setTipoFinanziamento(getTipoFinanziamento());
		capitoloEntrataPrevisione.setTipoFondo(getTipoFondo());
		capitoloEntrataPrevisione.setClassificatoriGenerici(listaClassificatoriGenerici);
		
		capitoloEntrataPrevisione.setSiopeEntrata(getSiopeEntrata());
		capitoloEntrataPrevisione.setRicorrenteEntrata(getRicorrenteEntrata());
		capitoloEntrataPrevisione.setPerimetroSanitarioEntrata(getPerimetroSanitarioEntrata());
		capitoloEntrataPrevisione.setTransazioneUnioneEuropeaEntrata(getTransazioneUnioneEuropeaEntrata());
		
		// Impostazione degli importi
		capitoloEntrataPrevisione.setImportiCapitoloEP(importiCapitoloEntrataPrevisione0);
		capitoloEntrataPrevisione.setListaImportiCapitolo(getListaImportiCapitoloEP());
		
		//impostazione flag
		//SIAC-7858 CM 11/05/2021 Inizio
		capitoloEntrataPrevisione.setFlagEntrataDubbiaEsigFCDE(isFlagEntrataDubbiaEsigFCDE());
		//SIAC-7858 CM 11/05/2021 Fine
	}
}
