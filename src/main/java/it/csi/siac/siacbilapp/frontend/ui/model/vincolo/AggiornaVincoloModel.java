/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.vincolo;

import java.util.Arrays;
import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;

/**
 * Classe di model per l'aggiornamento del Vincolo. Contiene una mappatura del form di aggiornamento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 03/01/2014
 * 
 */
public class AggiornaVincoloModel extends GenericVincoloModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6001840461586092716L;
	
	private Integer uidDaAggiornare;
	
	/** Costruttore vuoto di default */
	public AggiornaVincoloModel() {
		super();
		setTitolo("Gestione Vincolo");
	}

	/**
	 * @return the uidDaAggiornare
	 */
	public Integer getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(Integer uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioVincolo creaRequestRicercaDettaglioVincolo() {
		RicercaDettaglioVincolo request = new RicercaDettaglioVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setChiaveVincolo(uidDaAggiornare);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaVincolo creaRequestRicercaVincolo() {
		RicercaVincolo request = new RicercaVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setTipiCapitolo(Arrays.asList(TipoCapitolo.values()));
		
		request.setCapitolo(getCapitolo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Aggiorna Vincolo Capitolo.
	 * 
	 * @return la request creata
	 */
	public AggiornaVincoloCapitolo creaRequestAggiornaVincoloCapitolo() {
		AggiornaVincoloCapitolo request = new AggiornaVincoloCapitolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setVincolo(getVincolo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Associa Capitolo Al Vincolo.
	 * 
	 * @return la request creata
	 */
	public AssociaCapitoloAlVincolo creaRequestAssociaCapitoloAlVincolo() {
		AssociaCapitoloAlVincolo request = new AssociaCapitoloAlVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		Vincolo vincoloDaAssociare = new Vincolo();
		vincoloDaAssociare.setEnte(getEnte());
		vincoloDaAssociare.setUid(uidDaAggiornare);
		
		request.setVincolo(vincoloDaAssociare);
		request.setCapitolo(getCapitolo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Scollega Capitolo Al Vincolo.
	 * 
	 * @return la request creata
	 */
	public ScollegaCapitoloAlVincolo creaRequestScollegaCapitoloAlVincolo() {
		ScollegaCapitoloAlVincolo request = new ScollegaCapitoloAlVincolo();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		Vincolo vincoloDaAssociare = new Vincolo();
		vincoloDaAssociare.setEnte(getEnte());
		vincoloDaAssociare.setUid(uidDaAggiornare);
		
		request.setVincolo(vincoloDaAssociare);
		request.setCapitolo(getCapitolo());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Aggiunge i valori al totale dello stanziamento di entrata.
	 * 
	 * @param elemento l'elemento da cui ottenere gli importi da aggiungere
	 */
	public void addStanziamentoEntrata(ElementoCapitoloVincolo elemento) {
		setTotaleStanziamentoEntrata0(getTotaleStanziamentoEntrata0().add(elemento.getCompetenzaAnno0()));
		setTotaleStanziamentoEntrata1(getTotaleStanziamentoEntrata1().add(elemento.getCompetenzaAnno1()));
		setTotaleStanziamentoEntrata2(getTotaleStanziamentoEntrata2().add(elemento.getCompetenzaAnno2()));
	}
	
	/**
	 * Aggiunge i valori al totale dello stanziamento di uscita.
	 * 
	 * @param elemento l'elemento da cui ottenere gli importi da aggiungere
	 */
	public void addStanziamentoUscita(ElementoCapitoloVincolo elemento) {
		setTotaleStanziamentoUscita0(getTotaleStanziamentoUscita0().add(elemento.getCompetenzaAnno0()));
		setTotaleStanziamentoUscita1(getTotaleStanziamentoUscita1().add(elemento.getCompetenzaAnno1()));
		setTotaleStanziamentoUscita2(getTotaleStanziamentoUscita2().add(elemento.getCompetenzaAnno2()));
	}
	
	/**
	 * Sottrae i valori al totale dello stanziamento di entrata.
	 * 
	 * @param elemento l'elemento da cui ottenere gli importi da sottrarre
	 */
	public void subtractStanziamentoEntrata(ElementoCapitoloVincolo elemento) {
		setTotaleStanziamentoEntrata0(getTotaleStanziamentoEntrata0().subtract(elemento.getCompetenzaAnno0()));
		setTotaleStanziamentoEntrata1(getTotaleStanziamentoEntrata1().subtract(elemento.getCompetenzaAnno1()));
		setTotaleStanziamentoEntrata2(getTotaleStanziamentoEntrata2().subtract(elemento.getCompetenzaAnno2()));
	}
	
	/**
	 * Sottrae i valori al totale dello stanziamento di uscita.
	 * 
	 * @param elemento l'elemento da cui ottenere gli importi da sottrarre
	 */
	public void subtractStanziamentoUscita(ElementoCapitoloVincolo elemento) {
		setTotaleStanziamentoUscita0(getTotaleStanziamentoUscita0().subtract(elemento.getCompetenzaAnno0()));
		setTotaleStanziamentoUscita1(getTotaleStanziamentoUscita1().subtract(elemento.getCompetenzaAnno1()));
		setTotaleStanziamentoUscita2(getTotaleStanziamentoUscita2().subtract(elemento.getCompetenzaAnno2()));
	}
	
}
