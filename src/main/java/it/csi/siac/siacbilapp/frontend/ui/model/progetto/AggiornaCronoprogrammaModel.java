/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;

/**
 * Classe di model per l'aggiornamento del Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/02/2014
 *
 */
public class AggiornaCronoprogrammaModel extends GenericCronoprogrammaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1421319266825162220L;
	
	private List<DettaglioEntrataCronoprogramma> listaDettaglioEntrataCronoprogrammaDaEliminare = new ArrayList<DettaglioEntrataCronoprogramma>();
	private List<DettaglioUscitaCronoprogramma> listaDettaglioUscitaCronoprogrammaDaEliminare = new ArrayList<DettaglioUscitaCronoprogramma>();
	
	private Integer uidCronoprogramma;
	
	
	/** Costruttore vuoto di default */
	public AggiornaCronoprogrammaModel() {
		super();
		setTitolo("Gestione Cronoprogramma");
	}
	
	/* Getter e setter */
	
	/**
	 * @return the listaDettaglioEntrataCronoprogrammaDaEliminare
	 */
	public List<DettaglioEntrataCronoprogramma> getListaDettaglioEntrataCronoprogrammaDaEliminare() {
		return listaDettaglioEntrataCronoprogrammaDaEliminare;
	}


	/**
	 * @param listaDettaglioEntrataCronoprogrammaDaEliminare the listaDettaglioEntrataCronoprogrammaDaEliminare to set
	 */
	public void setListaDettaglioEntrataCronoprogrammaDaEliminare(
			List<DettaglioEntrataCronoprogramma> listaDettaglioEntrataCronoprogrammaDaEliminare) {
		this.listaDettaglioEntrataCronoprogrammaDaEliminare = listaDettaglioEntrataCronoprogrammaDaEliminare;
	}


	/**
	 * @return the listaDettaglioUscitaCronoprogrammaDaEliminare
	 */
	public List<DettaglioUscitaCronoprogramma> getListaDettaglioUscitaCronoprogrammaDaEliminare() {
		return listaDettaglioUscitaCronoprogrammaDaEliminare;
	}


	/**
	 * @param listaDettaglioUscitaCronoprogrammaDaEliminare the listaDettaglioUscitaCronoprogrammaDaEliminare to set
	 */
	public void setListaDettaglioUscitaCronoprogrammaDaEliminare(
			List<DettaglioUscitaCronoprogramma> listaDettaglioUscitaCronoprogrammaDaEliminare) {
		this.listaDettaglioUscitaCronoprogrammaDaEliminare = listaDettaglioUscitaCronoprogrammaDaEliminare;
	}
	
	/**
	 * @return the uidCronoprogramma
	 */
	public Integer getUidCronoprogramma() {
		return uidCronoprogramma;
	}

	/**
	 * @param uidCronoprogramma the uidCronoprogramma to set
	 */
	public void setUidCronoprogramma(Integer uidCronoprogramma) {
		this.uidCronoprogramma = uidCronoprogramma;
	}
	
	/**
	 * @return the baseUrlCronoprogramma
	 */
	public String getBaseUrlCronoprogramma() {
		return "aggiornamentoCronoprogramma";
	}
	
	/* ************ Requests ************ */

	/**
	 * Crea una request per il servizio di {@link AggiornaAnagraficaCronoprogramma}.
	 * 
	 * @return la request creata
	 */
	public AggiornaAnagraficaCronoprogramma creaRequestAggiornaAnagraficaCronoprogramma() {
		AggiornaAnagraficaCronoprogramma request = creaRequest(AggiornaAnagraficaCronoprogramma.class);
		
		request.setCronoprogramma(costruisciCronoprogrammaPerAggiornamento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioCronoprogramma}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCronoprogramma creaRequestRicercaDettaglioCronoprogramma() {
		RicercaDettaglioCronoprogramma request = creaRequest(RicercaDettaglioCronoprogramma.class);
		request.setCronoprogramma(costruisciCronoprogrammaPerRicercaDettaglio());
		return request;
	}
	
	/* **** Metodi di utilita' **** */

	/**
	 * Metodo di utilit&agrave; per la costruzione di un Cronoprogramma da injettare nella request.
	 * 
	 * @return il Cronoprogramma creato
	 */
	private Cronoprogramma costruisciCronoprogrammaPerAggiornamento() {
		/** AHMAD**/
		//FIXME PER i cronoprogrammi gia esistenti con il campo usatoPerfpv non ancora settato
		//per ora l'analisi chiede in fase di inserimento di settare questo campo a No--->false e poi in aggiornamento puo essere modificato
		//se in una fase successiva questo campo viene scelto dall'utente (non viene settato a false di default) in fase di inserimento di un nuovo cronoprogramma 
		//bisogna rimuovere questo controllo 
		if(getCronoprogramma().getUsatoPerFpv()==null) {
			getCronoprogramma().setUsatoPerFpv(Boolean.FALSE);
		}
		getCronoprogramma().setProgetto(getProgetto());
		getCronoprogramma().setBilancio(getBilancio());
		getCronoprogramma().setEnte(getEnte());
		
		//SIAC-6255
		getCronoprogramma().setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		getCronoprogramma().setDataFineLavori(calcolaDataFineLavori(getCronoprogramma().getDurataInGiorni(), getCronoprogramma().getDataInizioLavori()));
		
		impostaDefaultQuadroEconomico();
		
		List<DettaglioEntrataCronoprogramma> listaEntrate = creaListaDettagli(getListaDettaglioEntrataCronoprogramma(), listaDettaglioEntrataCronoprogrammaDaEliminare);
		List<DettaglioUscitaCronoprogramma> listaUscite = creaListaDettagli(getListaDettaglioUscitaCronoprogramma(), listaDettaglioUscitaCronoprogrammaDaEliminare);
		
		getCronoprogramma().setCapitoliEntrata(listaEntrate);
		getCronoprogramma().setCapitoliUscita(listaUscite);
		return getCronoprogramma();
	}
	
	/**
	 * Metodo di utilit&agrave; per la costruzione di un Cronoprogramma da injettare nella request.
	 * 
	 * @return il Cronoprogramma creato
	 */
	private Cronoprogramma costruisciCronoprogrammaPerRicercaDettaglio() {
		Cronoprogramma crono = new Cronoprogramma();
		crono.setUid(uidCronoprogramma);
		crono.setBilancio(getBilancio());
		crono.setEnte(getEnte());
		return crono;
	}
	
	/**
	 * Crea la lista dei dettagli da injettare a partire dalla lista da associare e dalla lista da eliminare.
	 * 
	 * @param lista            la lista dei dettagli da associare
	 * @param listaDaEliminare la lista dei dettagli da cancellare
	 * 
	 * @return la lista merge-ata
	 */
	private<T extends DettaglioBaseCronoprogramma> List<T> creaListaDettagli(List<T> lista, List<T> listaDaEliminare) {
		List<T> result = new ArrayList<T>(lista);
		Date now = new Date();
		for(T t : listaDaEliminare) {
			t.setDataFineValidita(now);
			result.add(t);
		}
		
		return result;
	}

	/**
	 * Popola il Cronoprogramma con quanto fornito in input.
	 * 
	 * @param crono il cronoprogramma con il quale popolare
	 */
	public void popolaDettaglioCronoprogramma(Cronoprogramma crono) {
		setCronoprogramma(crono);
		setUidProgetto(crono.getProgetto().getUid());
		
		List<DettaglioEntrataCronoprogramma> listaDettaglioEntrata = crono.getCapitoliEntrata();
		sortListaDettaglioEntrataByAnnoDiCompetenza(listaDettaglioEntrata);
		setListaDettaglioEntrataCronoprogramma(listaDettaglioEntrata);
		
		List<DettaglioUscitaCronoprogramma> listaDettaglioUscita = crono.getCapitoliUscita();
		sortListaDettaglioUscitaByAnnoEntrataSpesa(listaDettaglioUscita);
		setListaDettaglioUscitaCronoprogramma(listaDettaglioUscita);
		
		//SIAC-6754
		setAttoAmministrativo(crono.getAttoAmministrativo());
		
	}
	
	/**
	 * Pulisce il model dai dati vecchi.
	 */
	public void clean() {
		setCronoprogramma(null);
		setDettaglioEntrataCronoprogramma(null);
		setDettaglioUscitaCronoprogramma(null);
		setIndiceDettaglioNellaLista(null);
		getListaCronoprogramma().clear();
		getListaDettaglioEntrataCronoprogramma().clear();
		getListaDettaglioEntrataCronoprogrammaDaEliminare().clear();
		getListaDettaglioUscitaCronoprogramma().clear();
		getListaDettaglioUscitaCronoprogrammaDaEliminare().clear();
		getListaMissione().clear();
		getListaProgramma().clear();
		getListaTitoloSpesa().clear();
		getListaTitoloEntrata().clear();
		getListaTipologiaTitolo().clear();
		getMappaTotali().clear();
		setProgetto(null);
		setUidCronoprogrammaDaCopiare(null);
		setUidProgetto(null);
	}

}
