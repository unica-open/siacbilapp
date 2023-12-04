/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TotaliAnnoDiEsercizio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * Classe di model per il Capitolo di Uscita Previsione. Contiene una mappatura dei form per l'inserimento del
 * Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.1.2 27/06/2013
 *
 */
public class InserisciCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7673973731353039389L;
	
	/* Copia da capitolo gia' esistente */
	private Bilancio bilancioDaCopiare;
	private CapitoloUscitaPrevisione capitoloDaCopiare;
		
	/* Terza maschera: importi */
	// Anno in corso
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione0;
	// Anno in corso + 1
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione1;
	// Anno in corso + 2
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione2;
	
	/* Seconda maschera: altri dati */
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	
	/* Maschera Totali di Previsione */
	private TotaliAnnoDiEsercizio totaliEsercizio0;
	private TotaliAnnoDiEsercizio totaliEsercizio1;
	private TotaliAnnoDiEsercizio totaliEsercizio2;
	
	// SIAC-4724
	private TipoCapitolo tipoCapitoloCopia;
	
	//SIAC-6884 Per Discriminare le componenti di default
	private boolean capitoloFondino = false;
		
	
	
	
	private List<ImportiCapitolo> listaImportiCapitolo = new ArrayList<ImportiCapitolo>();
	
	private  List<ComponenteTotalePrevisioneModel>  componenteImportiAnno0 = new ArrayList<ComponenteTotalePrevisioneModel>();
	private  List<ComponenteTotalePrevisioneModel>  componenteImportiAnno1 = new ArrayList<ComponenteTotalePrevisioneModel>();
	private  List<ComponenteTotalePrevisioneModel>  componenteImportiAnno2 = new ArrayList<ComponenteTotalePrevisioneModel>();
	
	
	
	
	/**
	 * @return the componenteImportiAnno0
	 */
	public List<ComponenteTotalePrevisioneModel> getComponenteImportiAnno0() {
		return componenteImportiAnno0;
	}

	/**
	 * @param componenteImportiAnno0 the componenteImportiAnno0 to set
	 */
	public void setComponenteImportiAnno0(List<ComponenteTotalePrevisioneModel> componenteImportiAnno0) {
		this.componenteImportiAnno0 = componenteImportiAnno0;
	}

	/**
	 * @return the componenteImportiAnno1
	 */
	public List<ComponenteTotalePrevisioneModel> getComponenteImportiAnno1() {
		return componenteImportiAnno1;
	}

	/**
	 * @param componenteImportiAnno1 the componenteImportiAnno1 to set
	 */
	public void setComponenteImportiAnno1(List<ComponenteTotalePrevisioneModel> componenteImportiAnno1) {
		this.componenteImportiAnno1 = componenteImportiAnno1;
	}

	/**
	 * @return the componenteImportiAnno2
	 */
	public List<ComponenteTotalePrevisioneModel> getComponenteImportiAnno2() {
		return componenteImportiAnno2;
	}
		
	/**
	 * @param componenteImportiAnno2 the componenteImportiAnno2 to set
	 */
	public void setComponenteImportiAnno2(List<ComponenteTotalePrevisioneModel> componenteImportiAnno2) {
		this.componenteImportiAnno2 = componenteImportiAnno2;
	}
	/**
	 * @return the listaImportiCapitolo
	 */
	public List<ImportiCapitolo> getListaImportiCapitolo() {
		return listaImportiCapitolo;
	}

	/**
	 * @param listaImportiCapitolo the listaImportiCapitolo to set
	 */
	public void setListaImportiCapitolo(List<ImportiCapitolo> listaImportiCapitolo) {
		this.listaImportiCapitolo = listaImportiCapitolo;
	}

	/** Costruttore vuoto di default */
	public InserisciCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Inserimento Capitolo Spesa Previsione");
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
	public CapitoloUscitaPrevisione getCapitoloDaCopiare() {
		return capitoloDaCopiare;
	}

	/**
	 * @param capitoloDaCopiare the capitoloDaCopiare to set
	 */
	public void setCapitoloDaCopiare(CapitoloUscitaPrevisione capitoloDaCopiare) {
		this.capitoloDaCopiare = capitoloDaCopiare;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione0
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione0() {
		return importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione0 the importiCapitoloUscitaPrevisione0 to set
	 */
	public void setImportiCapitoloUscitaPrevisione0(ImportiCapitoloUP importiCapitoloUscitaPrevisione0) {
		this.importiCapitoloUscitaPrevisione0 = importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione1
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione1() {
		return importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione1 the importiCapitoloUscitaPrevisione1 to set
	 */
	public void setImportiCapitoloUscitaPrevisione1(ImportiCapitoloUP importiCapitoloUscitaPrevisione1) {
		this.importiCapitoloUscitaPrevisione1 = importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione2
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione2() {
		return importiCapitoloUscitaPrevisione2;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione2 the importiCapitoloUscitaPrevisione2 to set
	 */
	public void setImportiCapitoloUscitaPrevisione2(ImportiCapitoloUP importiCapitoloUscitaPrevisione2) {
		this.importiCapitoloUscitaPrevisione2 = importiCapitoloUscitaPrevisione2;
	}

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
	 * @return i totali di stanziamento di competenza di uscita per l'anno corrente
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaSpesa0() {
		return totaliEsercizio0.getTotStanziamentiDiCompetenzaSpesa();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di uscita per l'anno corrente + 1
	 */
	public BigDecimal getTotaliStanziamentiCompetenzaSpesa1() {
		return totaliEsercizio1.getTotStanziamentiDiCompetenzaSpesa();
	}
	
	/**
	 * @return i totali di stanziamento di competenza di uscita per l'anno corrente + 2
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
		return capitoloUscitaPrevisione == null ? 0 : capitoloUscitaPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid per l'aggiornamento.
	 * 
	 * @param uid l'uid del Capitolo da aggiornare
	 */
	public void setUidDaAggiornare(int uid) {
		if(capitoloUscitaPrevisione == null) {
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setUid(uid);
	}
	
	
	
	//SIAC-6884
	public boolean getCapitoloFondino() {
		return capitoloFondino;
	}

	public void setCapitoloFondino(boolean capitoloFondino) {
		this.capitoloFondino = capitoloFondino;
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link InserisceCapitoloDiUscitaPrevisione} a partire dal Model.
	 * 
	 * @param statoOperativoElementoDiBilancio	lo stato operativo del Capitolo
	 * @return 									la Request creata
	 */
	public InserisceCapitoloDiUscitaPrevisione creaRequestInserisceCapitoloDiUscitaPrevisione(StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		InserisceCapitoloDiUscitaPrevisione request = creaRequest(InserisceCapitoloDiUscitaPrevisione.class);
		
		capitoloUscitaPrevisione.setAnnoCapitolo(getBilancio().getAnno());
		capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		
		request.setEnte(getEnte());
		
		capitoloUscitaPrevisione.setBilancio(getBilancio());
		capitoloUscitaPrevisione.setElementoPianoDeiConti(getElementoPianoDeiConti());
		capitoloUscitaPrevisione.setEnte(getEnte());
		
		capitoloUscitaPrevisione.setMissione(getMissione());
		capitoloUscitaPrevisione.setProgramma(getProgramma());
		capitoloUscitaPrevisione.setTitoloSpesa(getTitoloSpesa());
		capitoloUscitaPrevisione.setMacroaggregato(getMacroaggregato());
		capitoloUscitaPrevisione.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		
		capitoloUscitaPrevisione.setClassificazioneCofogProgramma(impostaEntitaFacoltativa(getClassificazioneCofogProgramma(getClassificazioneCofog())));
		capitoloUscitaPrevisione.setTipoFinanziamento(impostaEntitaFacoltativa(getTipoFinanziamento()));
		capitoloUscitaPrevisione.setTipoFondo(impostaEntitaFacoltativa(getTipoFondo()));
		capitoloUscitaPrevisione.setSiopeSpesa(impostaEntitaFacoltativa(getSiopeSpesa()));
		capitoloUscitaPrevisione.setRicorrenteSpesa(impostaEntitaFacoltativa(getRicorrenteSpesa()));
		capitoloUscitaPrevisione.setPerimetroSanitarioSpesa(impostaEntitaFacoltativa(getPerimetroSanitarioSpesa()));
		capitoloUscitaPrevisione.setTransazioneUnioneEuropeaSpesa(impostaEntitaFacoltativa(getTransazioneUnioneEuropeaSpesa()));
		capitoloUscitaPrevisione.setPoliticheRegionaliUnitarie(impostaEntitaFacoltativa(getPoliticheRegionaliUnitarie()));
		
		capitoloUscitaPrevisione.setRisorsaAccantonata(impostaEntitaFacoltativa(getRisorsaAccantonata()));
		
		// Imposto il flag non presente sulla pagina di aggiornamento
		capitoloUscitaPrevisione.setFlagPerMemoria(Boolean.FALSE);
		
		// Impostazione delle liste
		capitoloUscitaPrevisione.setListaImportiCapitoloUP(getListaImportiCapitoloUP());

		capitoloUscitaPrevisione.setClassificatoriGenerici(getListaClassificatoriGenerici());
		request.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);

		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @param daCopiare	indica se la Request deve essere creata per la ricerca di un Capitolo per verificarne 
	 * 					l'esistenza (<code>false</code>) o per copiarlo (<code>true</code>).
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloUscitaPrevisione creaRequestRicercaPuntualeCapitoloUscitaPrevisione(boolean daCopiare) {
		CapitoloUscitaPrevisione capitolo = daCopiare ? capitoloDaCopiare : capitoloUscitaPrevisione;
		Bilancio b = daCopiare ? bilancioDaCopiare : getBilancio();
		StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio = StatoOperativoElementoDiBilancio.VALIDO;
		
		RicercaPuntualeCapitoloUscitaPrevisione request = new RicercaPuntualeCapitoloUscitaPrevisione();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloUPrev(getRicercaPuntualeCapitoloUPrev(capitolo, b, statoOperativoElementoDiBilancio));
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaPuntualeCapitoloUscitaGestione} a partire dal Model.
	 * @return la Request creata
	 */
	public RicercaPuntualeCapitoloUscitaGestione creaRequestRicercaPuntualeCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUscitaGestione request = creaRequest(RicercaPuntualeCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaPuntualeCapitoloUGest(getRicercaPuntualeCapitoloUGest());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUPrev(getRicercaDettaglioCapitoloUPrev(chiaveCapitolo));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(getRicercaDettaglioCapitoloUGest(chiaveCapitolo));
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloUscitaPrevisione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloUscitaPrevisione.setDescrizione(cap.getDescrizione());
		capitoloUscitaPrevisione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setMissione(cap.getMissione());
		setProgramma(cap.getProgramma());
		setClassificazioneCofog(cap.getClassificazioneCofog());
		setTitoloSpesa(cap.getTitoloSpesa());
		setMacroaggregato(cap.getMacroaggregato());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeSpesa(cap.getSiopeSpesa());
		
		capitoloUscitaPrevisione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloUscitaPrevisione.setFlagImpegnabile(cap.getFlagImpegnabile());
		//task-55
		capitoloUscitaPrevisione.setFlagNonInserireAllegatoA1(cap.getFlagNonInserireAllegatoA1());
		
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteSpesa(cap.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(cap.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(cap.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(cap.getPoliticheRegionaliUnitarie());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloUscitaPrevisione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloUscitaPrevisione.setFunzDelegateRegione(cap.getFunzDelegateRegione());
		capitoloUscitaPrevisione.setNote(cap.getNote());
		
		// Caricamento delle stringhe di utilita'
		valorizzaStringheUtilita();
	}
	
	/**
	 * Copia i dati del capitolo di uscita fornito
	 * @param cap la Response del servizio
	 */
	public void copiaDatiCapitolo(CapitoloUscitaGestione cap) {
		
		/* Injetto i dati a partire dalla response */
		capitoloUscitaPrevisione.setDescrizione(cap.getDescrizione());
		capitoloUscitaPrevisione.setDescrizioneArticolo(cap.getDescrizioneArticolo());
		
		setMissione(cap.getMissione());
		setProgramma(cap.getProgramma());
		setClassificazioneCofog(cap.getClassificazioneCofog());
		setTitoloSpesa(cap.getTitoloSpesa());
		setMacroaggregato(cap.getMacroaggregato());
		setElementoPianoDeiConti(cap.getElementoPianoDeiConti());
		setStrutturaAmministrativoContabile(cap.getStrutturaAmministrativoContabile());
		setSiopeSpesa(cap.getSiopeSpesa());
		
		capitoloUscitaPrevisione.setCategoriaCapitolo(cap.getCategoriaCapitolo());
		capitoloUscitaPrevisione.setFlagImpegnabile(cap.getFlagImpegnabile());
		//task-55
		capitoloUscitaPrevisione.setFlagNonInserireAllegatoA1(cap.getFlagNonInserireAllegatoA1());
		
		// SIAC-4878
		setTipoFinanziamento(cap.getTipoFinanziamento());
		setTipoFondo(cap.getTipoFondo());
		setRicorrenteSpesa(cap.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(cap.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(cap.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(cap.getPoliticheRegionaliUnitarie());
		impostaClassificatoriGenericiDaLista(cap.getClassificatoriGenerici());
		capitoloUscitaPrevisione.setFlagRilevanteIva(cap.getFlagRilevanteIva());
		capitoloUscitaPrevisione.setFunzDelegateRegione(cap.getFunzDelegateRegione());
		capitoloUscitaPrevisione.setNote(cap.getNote());
		
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
	private List<ImportiCapitoloUP> getListaImportiCapitoloUP() {
		List<ImportiCapitoloUP> lista = new ArrayList<ImportiCapitoloUP>();
//		addImportoCapitoloALista(lista, importiCapitoloUscitaPrevisione0, getAnnoEsercizioInt() + 0);
//		addImportoCapitoloALista(lista, importiCapitoloUscitaPrevisione1, getAnnoEsercizioInt() + 1);
//		addImportoCapitoloALista(lista, importiCapitoloUscitaPrevisione2, getAnnoEsercizioInt() + 2);
		return lista;
	}
		
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Uscita Previsione.
	 * 
	 * @param capitolo	                       il Capitolo da cui ricavare l'utilit&agrave;
	 * @param bilancio                         il Bilancio da cui ricavare l'utilit&agrave;
	 * @param statoOperativoElementoDiBilancio lo stato del Capitolo
	 * 
	 * @return 			l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloUPrev getRicercaPuntualeCapitoloUPrev(CapitoloUscitaPrevisione capitolo, Bilancio bilancio, StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		RicercaPuntualeCapitoloUPrev utility = new RicercaPuntualeCapitoloUPrev();
		utility.setAnnoCapitolo(bilancio.getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(capitolo.getNumeroArticolo());
		utility.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		utility.setNumeroUEB(capitolo.getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(statoOperativoElementoDiBilancio);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Puntuale del Capitolo di Uscita Gestione.
	 * @return l'utilit&agrave; creata
	 */
	private RicercaPuntualeCapitoloUGest getRicercaPuntualeCapitoloUGest() {
		RicercaPuntualeCapitoloUGest utility = new RicercaPuntualeCapitoloUGest();
		utility.setAnnoCapitolo(getBilancioDaCopiare().getAnno());
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setNumeroArticolo(getCapitoloDaCopiare().getNumeroArticolo());
		utility.setNumeroCapitolo(getCapitoloDaCopiare().getNumeroCapitolo());
		utility.setNumeroUEB(getCapitoloDaCopiare().getNumeroUEB());
		utility.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @param chiaveCapitolo	la chiave del Capitolo da ricercare
	 * @return 					l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev getRicercaDettaglioCapitoloUPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Gestione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest getRicercaDettaglioCapitoloUGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
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
	public void injettaDatiNelCapitolo(CapitoloUscitaPrevisione capitoloDaResponse) {
		capitoloUscitaPrevisione.setUid(capitoloDaResponse.getUid());
		capitoloUscitaPrevisione.setDataCreazione(capitoloDaResponse.getDataCreazione());
		capitoloUscitaPrevisione.setBilancio(getBilancio());
		
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
		capitoloUscitaPrevisione.setMissione(getMissione());
		capitoloUscitaPrevisione.setProgramma(getProgramma());
		capitoloUscitaPrevisione.setClassificazioneCofog(getClassificazioneCofog());
		capitoloUscitaPrevisione.setTitoloSpesa(getTitoloSpesa());
		capitoloUscitaPrevisione.setMacroaggregato(getMacroaggregato());
		capitoloUscitaPrevisione.setElementoPianoDeiConti(getElementoPianoDeiConti());
		capitoloUscitaPrevisione.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		capitoloUscitaPrevisione.setTipoFinanziamento(getTipoFinanziamento());
		capitoloUscitaPrevisione.setTipoFondo(getTipoFondo());
		capitoloUscitaPrevisione.setClassificatoriGenerici(listaClassificatoriGenerici);
		
		capitoloUscitaPrevisione.setSiopeSpesa(getSiopeSpesa());
		capitoloUscitaPrevisione.setRicorrenteSpesa(getRicorrenteSpesa());
		capitoloUscitaPrevisione.setPerimetroSanitarioSpesa(getPerimetroSanitarioSpesa());
		capitoloUscitaPrevisione.setTransazioneUnioneEuropeaSpesa(getTransazioneUnioneEuropeaSpesa());
		capitoloUscitaPrevisione.setPoliticheRegionaliUnitarie(getPoliticheRegionaliUnitarie());
		
		// Impostazione degli importi
		capitoloUscitaPrevisione.setImportiCapitoloUP(importiCapitoloUscitaPrevisione0);
		capitoloUscitaPrevisione.setListaImportiCapitolo(getListaImportiCapitoloUP());
	}
	
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo(int uidCapitolo) {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		return request;
	}
	
}
