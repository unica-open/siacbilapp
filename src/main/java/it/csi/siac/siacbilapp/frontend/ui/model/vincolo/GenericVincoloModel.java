/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.vincolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.GenereVincolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;

/**
 * Classe astratta di model per il Vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/01/2014
 *
 */
public abstract class GenericVincoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4948730122493670234L;
	
	private Vincolo vincolo;
	private Capitolo<?, ?> capitolo;
	
	private String tipoBilancio;
	
	private List<ElementoCapitoloVincolo> listaCapitoliEntrata = new ArrayList<ElementoCapitoloVincolo>();
	private List<ElementoCapitoloVincolo> listaCapitoliUscita = new ArrayList<ElementoCapitoloVincolo>();
	
	private BigDecimal totaleStanziamentoEntrata0;
	private BigDecimal totaleStanziamentoEntrata1;
	private BigDecimal totaleStanziamentoEntrata2;
	
	private BigDecimal totaleStanziamentoUscita0;
	private BigDecimal totaleStanziamentoUscita1;
	private BigDecimal totaleStanziamentoUscita2;
	
	// SIAC-5076
	private List<GenereVincolo> listaGenereVincolo = new ArrayList<GenereVincolo>();
	
	/**
	 * @return the vincolo
	 */
	public Vincolo getVincolo() {
		return vincolo;
	}

	/**
	 * @param vincolo the vincolo to set
	 */
	public void setVincolo(Vincolo vincolo) {
		this.vincolo = vincolo;
	}

	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the tipoBilancio
	 */
	public String getTipoBilancio() {
		return tipoBilancio;
	}

	/**
	 * @param tipoBilancio the tipoBilancio to set
	 */
	public void setTipoBilancio(String tipoBilancio) {
		this.tipoBilancio = tipoBilancio;
	}

	/**
	 * @return the listaCapitoliEntrata
	 */
	public List<ElementoCapitoloVincolo> getListaCapitoliEntrata() {
		return listaCapitoliEntrata;
	}

	/**
	 * @param listaCapitoliEntrata the listaCapitoliEntrata to set
	 */
	public void setListaCapitoliEntrata(
			List<ElementoCapitoloVincolo> listaCapitoliEntrata) {
		this.listaCapitoliEntrata = listaCapitoliEntrata;
	}

	/**
	 * @return the listaCapitoliUscita
	 */
	public List<ElementoCapitoloVincolo> getListaCapitoliUscita() {
		return listaCapitoliUscita;
	}

	/**
	 * @param listaCapitoliUscita the listaCapitoliUscita to set
	 */
	public void setListaCapitoliUscita(
			List<ElementoCapitoloVincolo> listaCapitoliUscita) {
		this.listaCapitoliUscita = listaCapitoliUscita;
	}

	/**
	 * @return the totaleStanziamentoEntrata0
	 */
	public BigDecimal getTotaleStanziamentoEntrata0() {
		return totaleStanziamentoEntrata0;
	}

	/**
	 * @param totaleStanziamentoEntrata0 the totaleStanziamentoEntrata0 to set
	 */
	public void setTotaleStanziamentoEntrata0(BigDecimal totaleStanziamentoEntrata0) {
		this.totaleStanziamentoEntrata0 = totaleStanziamentoEntrata0;
	}

	/**
	 * @return the totaleStanziamentoEntrata1
	 */
	public BigDecimal getTotaleStanziamentoEntrata1() {
		return totaleStanziamentoEntrata1;
	}

	/**
	 * @param totaleStanziamentoEntrata1 the totaleStanziamentoEntrata1 to set
	 */
	public void setTotaleStanziamentoEntrata1(BigDecimal totaleStanziamentoEntrata1) {
		this.totaleStanziamentoEntrata1 = totaleStanziamentoEntrata1;
	}

	/**
	 * @return the totaleStanziamentoEntrata2
	 */
	public BigDecimal getTotaleStanziamentoEntrata2() {
		return totaleStanziamentoEntrata2;
	}

	/**
	 * @param totaleStanziamentoEntrata2 the totaleStanziamentoEntrata2 to set
	 */
	public void setTotaleStanziamentoEntrata2(BigDecimal totaleStanziamentoEntrata2) {
		this.totaleStanziamentoEntrata2 = totaleStanziamentoEntrata2;
	}

	/**
	 * @return the totaleStanziamentoUscita0
	 */
	public BigDecimal getTotaleStanziamentoUscita0() {
		return totaleStanziamentoUscita0;
	}

	/**
	 * @param totaleStanziamentoUscita0 the totaleStanziamentoUscita0 to set
	 */
	public void setTotaleStanziamentoUscita0(BigDecimal totaleStanziamentoUscita0) {
		this.totaleStanziamentoUscita0 = totaleStanziamentoUscita0;
	}

	/**
	 * @return the totaleStanziamentoUscita1
	 */
	public BigDecimal getTotaleStanziamentoUscita1() {
		return totaleStanziamentoUscita1;
	}

	/**
	 * @param totaleStanziamentoUscita1 the totaleStanziamentoUscita1 to set
	 */
	public void setTotaleStanziamentoUscita1(BigDecimal totaleStanziamentoUscita1) {
		this.totaleStanziamentoUscita1 = totaleStanziamentoUscita1;
	}

	/**
	 * @return the totaleStanziamentoUscita2
	 */
	public BigDecimal getTotaleStanziamentoUscita2() {
		return totaleStanziamentoUscita2;
	}

	/**
	 * @param totaleStanziamentoUscita2 the totaleStanziamentoUscita2 to set
	 */
	public void setTotaleStanziamentoUscita2(BigDecimal totaleStanziamentoUscita2) {
		this.totaleStanziamentoUscita2 = totaleStanziamentoUscita2;
	}

	/**
	 * @return the listaGenereVincolo
	 */
	public List<GenereVincolo> getListaGenereVincolo() {
		return listaGenereVincolo;
	}

	/**
	 * @param listaGenereVincolo the listaGenereVincolo to set
	 */
	public void setListaGenereVincolo(List<GenereVincolo> listaGenereVincolo) {
		this.listaGenereVincolo = listaGenereVincolo != null ? listaGenereVincolo : new ArrayList<GenereVincolo>();
	}

	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Capitolo Uscita Previsione.
	 * 
	 * @param capitoloUscitaPrevisione il capitolo di uscita previsione rispetto cui creare la response; nel caso sia <code>null</code>,
	 *                                 si utilizza il capitolo presente nel model
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		RicercaDettaglioCapitoloUscitaPrevisione request = new RicercaDettaglioCapitoloUscitaPrevisione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		int uidDaInjettare = (capitoloUscitaPrevisione == null) ? capitolo.getUid() : capitoloUscitaPrevisione.getUid();
		
		// Metodo di utilita'
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(uidDaInjettare);
		
		request.setRicercaDettaglioCapitoloUPrev(utility);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Capitolo Uscita Gestione.
	 * 
	 * @param capitoloUscitaGestione il capitolo di uscita gestione rispetto cui creare la response; nel caso sia <code>null</code>,
	 *                               si utilizza il capitolo presente nel model
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		RicercaDettaglioCapitoloUscitaGestione request = new RicercaDettaglioCapitoloUscitaGestione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		int uidDaInjettare = (capitoloUscitaGestione == null) ? capitolo.getUid() : capitoloUscitaGestione.getUid();
		
		// Metodo di utilita'
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		utility.setChiaveCapitolo(uidDaInjettare);
		
		request.setRicercaDettaglioCapitoloUGest(utility);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Capitolo Entrata Previsione.
	 * 
	 * @param capitoloEntrataPrevisione il capitolo di entrata previsione rispetto cui creare la response; nel caso sia <code>null</code>,
	 *                                  si utilizza il capitolo presente nel model
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		RicercaDettaglioCapitoloEntrataPrevisione request = new RicercaDettaglioCapitoloEntrataPrevisione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		int uidDaInjettare = (capitoloEntrataPrevisione == null) ? capitolo.getUid() : capitoloEntrataPrevisione.getUid();
		
		// Metodo di utilita'
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(uidDaInjettare);
		
		request.setRicercaDettaglioCapitoloEPrev(utility);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Capitolo Entrata Gestione.
	 * 
	 * @param capitoloEntrataGestione il capitolo di entrata gestione rispetto cui creare la response; nel caso sia <code>null</code>,
	 *                                si utilizza il capitolo presente nel model
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		RicercaDettaglioCapitoloEntrataGestione request = new RicercaDettaglioCapitoloEntrataGestione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		int uidDaInjettare = (capitoloEntrataGestione == null) ? capitolo.getUid() : capitoloEntrataGestione.getUid();
		
		// Metodo di utilita'
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(uidDaInjettare);
		
		request.setRicercaDettaglioCapitoloEGest(utility);
		
		return request;
	}
	
	/**
	 * Imposta i dati dalla ricerca di dettaglio del vincolo al model.
	 * 
	 * @param response     la response da cui popolare il model
	 * @param listaEntrata la lista dei capitoli di entrata
	 * @param listaUscita  la lista dei capitoli di uscita
	 */
	public void impostaDati(RicercaDettaglioVincoloResponse response, List<Capitolo<?, ?>> listaEntrata, List<Capitolo<?, ?>> listaUscita) {
		VincoloCapitoli vincoloCapitoli = response.getVincoloCapitoli();
		
		// Popolo a partire dal vincoloCapitoli
		vincolo = vincoloCapitoli;
		
		// Stringhe di utilita'
		tipoBilancio = StringUtils.capitalize(vincolo.getTipoVincoloCapitoli().name().toLowerCase());
		
		listaCapitoliEntrata = ElementoCapitoloVincoloFactory.getInstances(listaEntrata, isGestioneUEB());
		listaCapitoliUscita = ElementoCapitoloVincoloFactory.getInstances(listaUscita, isGestioneUEB());
		
		totaleStanziamentoEntrata0 = BigDecimal.ZERO;
		totaleStanziamentoEntrata1 = BigDecimal.ZERO;
		totaleStanziamentoEntrata2 = BigDecimal.ZERO;
		
		totaleStanziamentoUscita0 = BigDecimal.ZERO;
		totaleStanziamentoUscita1 = BigDecimal.ZERO;
		totaleStanziamentoUscita2 = BigDecimal.ZERO;
		
		for(ElementoCapitoloVincolo capitoloEntrata : listaCapitoliEntrata) {
			totaleStanziamentoEntrata0 = totaleStanziamentoEntrata0.add(capitoloEntrata.getCompetenzaAnno0());
			totaleStanziamentoEntrata1 = totaleStanziamentoEntrata1.add(capitoloEntrata.getCompetenzaAnno1());
			totaleStanziamentoEntrata2 = totaleStanziamentoEntrata2.add(capitoloEntrata.getCompetenzaAnno2());
		}
		
		for(ElementoCapitoloVincolo capitoloUscita : listaCapitoliUscita) {
			totaleStanziamentoUscita0 = totaleStanziamentoUscita0.add(capitoloUscita.getCompetenzaAnno0());
			totaleStanziamentoUscita1 = totaleStanziamentoUscita1.add(capitoloUscita.getCompetenzaAnno1());
			totaleStanziamentoUscita2 = totaleStanziamentoUscita2.add(capitoloUscita.getCompetenzaAnno2());
		}
	}
	
}
