/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.time.DateUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * Classe astratta di model per il Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/02/2014
 * @version 1.0.1 - 22/05/2015 - Estensione del modello del progetto per la gestione del wizard
 *
 */
public abstract class GenericCronoprogrammaModel extends GenericProgettoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4765535703563930099L;

	private Cronoprogramma cronoprogramma = new Cronoprogramma();
	
	private List<Missione> listaMissione = new ArrayList<Missione>();
	private List<Programma> listaProgramma = new ArrayList<Programma>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TipologiaTitolo> listaTipologiaTitolo = new ArrayList<TipologiaTitolo>();
	
	private List<DettaglioEntrataCronoprogramma> listaDettaglioEntrataCronoprogramma = new ArrayList<DettaglioEntrataCronoprogramma>();
	private List<DettaglioUscitaCronoprogramma> listaDettaglioUscitaCronoprogramma = new ArrayList<DettaglioUscitaCronoprogramma>();
	
	private DettaglioEntrataCronoprogramma dettaglioEntrataCronoprogramma;
	private DettaglioUscitaCronoprogramma dettaglioUscitaCronoprogramma;
	
	private Integer indiceDettaglioNellaLista;
	
	private List<Cronoprogramma> listaCronoprogramma = new ArrayList<Cronoprogramma>();
	private Integer uidCronoprogrammaDaCopiare;
	
	private Map<Integer, BigDecimal> mappaTotali = new TreeMap<Integer, BigDecimal>();
	
	private Map<String, BigDecimal> quadratura = new HashMap<String, BigDecimal>();
	
	// Per il passaggio dei dati
	private Integer uidProgetto;
	
	// Per i controlli sul dettaglio di spesa
	private Boolean cronoprogrammaDaDefinire;
	
	//SIAC-6255
	private Integer durataInGiorni;
	private Date dataInizioLavori;
	private String dataFineLavoriString;
	private boolean cronoAggiornabile = false;
	private List<QuadroEconomico> listaQuadroEconomico = new ArrayList<QuadroEconomico>();
	
	//SIAC-6830
	private Cronoprogramma campiCronoDaCopiare;

	/* Getter e setter */

	/**
	 * @return the cronoprogramma
	 */
	public Cronoprogramma getCronoprogramma() {
		return cronoprogramma;
	}

	/**
	 * @param cronoprogramma the cronoprogramma to set
	 */
	public void setCronoprogramma(Cronoprogramma cronoprogramma) {
		this.cronoprogramma = cronoprogramma;
	}

	/**
	 * @return the listaMissione
	 */
	public List<Missione> getListaMissione() {
		return listaMissione;
	}

	/**
	 * @param listaMissione the listaMissione to set
	 */
	public void setListaMissione(List<Missione> listaMissione) {
		this.listaMissione = listaMissione;
	}

	/**
	 * @return the listaProgramma
	 */
	public List<Programma> getListaProgramma() {
		return listaProgramma;
	}

	/**
	 * @param listaProgramma the listaProgramma to set
	 */
	public void setListaProgramma(List<Programma> listaProgramma) {
		this.listaProgramma = listaProgramma;
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

	/**
	 * @return the listaTipologiaTitolo
	 */
	public List<TipologiaTitolo> getListaTipologiaTitolo() {
		return listaTipologiaTitolo;
	}

	/**
	 * @param listaTipologiaTitolo the listaTipologiaTitolo to set
	 */
	public void setListaTipologiaTitolo(List<TipologiaTitolo> listaTipologiaTitolo) {
		this.listaTipologiaTitolo = listaTipologiaTitolo;
	}

	/**
	 * @return the listaDettaglioEntrataCronoprogramma
	 */
	public List<DettaglioEntrataCronoprogramma> getListaDettaglioEntrataCronoprogramma() {
		return listaDettaglioEntrataCronoprogramma;
	}

	/**
	 * @param listaDettaglioEntrataCronoprogramma the listaDettaglioEntrataCronoprogramma to set
	 */
	public void setListaDettaglioEntrataCronoprogramma(
			List<DettaglioEntrataCronoprogramma> listaDettaglioEntrataCronoprogramma) {
		this.listaDettaglioEntrataCronoprogramma = listaDettaglioEntrataCronoprogramma;
	}

	/**
	 * @return the listaDettaglioUscitaCronoprogramma
	 */
	public List<DettaglioUscitaCronoprogramma> getListaDettaglioUscitaCronoprogramma() {
		return listaDettaglioUscitaCronoprogramma;
	}

	/**
	 * @param listaDettaglioUscitaCronoprogramma the listaDettaglioUscitaCronoprogramma to set
	 */
	public void setListaDettaglioUscitaCronoprogramma(
			List<DettaglioUscitaCronoprogramma> listaDettaglioUscitaCronoprogramma) {
		this.listaDettaglioUscitaCronoprogramma = listaDettaglioUscitaCronoprogramma;
	}

	/**
	 * @return the dettaglioEntrataCronoprogramma
	 */
	public DettaglioEntrataCronoprogramma getDettaglioEntrataCronoprogramma() {
		return dettaglioEntrataCronoprogramma;
	}

	/**
	 * @param dettaglioEntrataCronoprogramma the dettaglioEntrataCronoprogramma to set
	 */
	public void setDettaglioEntrataCronoprogramma(
			DettaglioEntrataCronoprogramma dettaglioEntrataCronoprogramma) {
		this.dettaglioEntrataCronoprogramma = dettaglioEntrataCronoprogramma;
	}

	/**
	 * @return the dettaglioUscitaCronoprogramma
	 */
	public DettaglioUscitaCronoprogramma getDettaglioUscitaCronoprogramma() {
		return dettaglioUscitaCronoprogramma;
	}

	/**
	 * @param dettaglioUscitaCronoprogramma the dettaglioUscitaCronoprogramma to set
	 */
	public void setDettaglioUscitaCronoprogramma(
			DettaglioUscitaCronoprogramma dettaglioUscitaCronoprogramma) {
		this.dettaglioUscitaCronoprogramma = dettaglioUscitaCronoprogramma;
	}

	/**
	 * @return the indiceDettaglioNellaLista
	 */
	public Integer getIndiceDettaglioNellaLista() {
		return indiceDettaglioNellaLista;
	}

	/**
	 * @param indiceDettaglioNellaLista the indiceDettaglioNellaLista to set
	 */
	public void setIndiceDettaglioNellaLista(Integer indiceDettaglioNellaLista) {
		this.indiceDettaglioNellaLista = indiceDettaglioNellaLista;
	}

	/**
	 * @return the listaCronoprogramma
	 */
	public List<Cronoprogramma> getListaCronoprogramma() {
		return listaCronoprogramma;
	}

	/**
	 * @param listaCronoprogramma the listaCronoprogramma to set
	 */
	public void setListaCronoprogramma(List<Cronoprogramma> listaCronoprogramma) {
		this.listaCronoprogramma = listaCronoprogramma;
	}

	/**
	 * @return the uidCronoprogrammaDaCopiare
	 */
	public Integer getUidCronoprogrammaDaCopiare() {
		return uidCronoprogrammaDaCopiare;
	}

	/**
	 * @param uidCronoprogrammaDaCopiare the uidCronoprogrammaDaCopiare to set
	 */
	public void setUidCronoprogrammaDaCopiare(Integer uidCronoprogrammaDaCopiare) {
		this.uidCronoprogrammaDaCopiare = uidCronoprogrammaDaCopiare;
	}

	/**
	 * @return the mappaTotali
	 */
	public Map<Integer, BigDecimal> getMappaTotali() {
		return mappaTotali;
	}

	/**
	 * @param mappaTotali the mappaTotali to set
	 */
	public void setMappaTotali(Map<Integer, BigDecimal> mappaTotali) {
		this.mappaTotali = mappaTotali;
	}

	/**
	 * @return the quadratura
	 */
	public Map<String, BigDecimal> getQuadratura() {
		return quadratura;
	}

	/**
	 * @param quadratura the quadratura to set
	 */
	public void setQuadratura(Map<String, BigDecimal> quadratura) {
		this.quadratura = quadratura;
	}

	/**
	 * @return the uidProgetto
	 */
	public Integer getUidProgetto() {
		return uidProgetto;
	}

	/**
	 * @param uidProgetto the uidProgetto to set
	 */
	public void setUidProgetto(Integer uidProgetto) {
		this.uidProgetto = uidProgetto;
	}
	
	/**
	 * @return the cronoprogrammaDaDefinire
	 */
	public Boolean getCronoprogrammaDaDefinire() {
		return cronoprogrammaDaDefinire;
	}

	/**
	 * @param cronoprogrammaDaDefinire the cronoprogrammaDaDefinire to set
	 */
	public void setCronoprogrammaDaDefinire(Boolean cronoprogrammaDaDefinire) {
		this.cronoprogrammaDaDefinire = cronoprogrammaDaDefinire;
	}
	
	/**
	 * @return the durataInGiorni
	 */
	public Integer getDurataInGiorni() {
		return durataInGiorni;
	}

	/**
	 * @param durataInGiorni the durataInGiorni to set
	 */
	public void setDurataInGiorni(Integer durataInGiorni) {
		this.durataInGiorni = durataInGiorni;
	}

	/**
	 * @return the dataInizioLavori
	 */
	public Date getDataInizioLavori() {
		return dataInizioLavori;
	}

	/**
	 * @param dataInizioLavori the dataInizioLavori to set
	 */
	public void setDataInizioLavori(Date dataInizioLavori) {
		this.dataInizioLavori = dataInizioLavori;
	}

	/**
	 * @return the dataFineLavoriString
	 */
	public String getDataFineLavoriString() {
		return dataFineLavoriString;
	}

	/**
	 * @param dataFineLavoriString the dataFineLavoriString to set
	 */
	public void setDataFineLavoriString(String dataFineLavoriString) {
		this.dataFineLavoriString = dataFineLavoriString;
	}
	
	/**
	 * @return the cronoAggiornabile
	 */
	public boolean isCronoAggiornabile() {
		return cronoAggiornabile;
	}

	/**
	 * @param cronoAggiornabile the cronoAggiornabile to set
	 */
	public void setCronoAggiornabile(boolean cronoAggiornabile) {
		this.cronoAggiornabile = cronoAggiornabile;
	}
	
	/**
	 * @return the listaQuoadroEconomico
	 */
	public List<QuadroEconomico> getListaQuadroEconomico() {
		return listaQuadroEconomico;
	}

	/**
	 * @param listaQuoadroEconomico the listaQuoadroEconomico to set
	 */
	public void setListaQuadroEconomico(List<QuadroEconomico> listaQuoadroEconomico) {
		this.listaQuadroEconomico = listaQuoadroEconomico != null? listaQuoadroEconomico : new ArrayList<QuadroEconomico>();
	}
	
	/**
	 * @return the campiCronoDaCopiare
	 */
	public Cronoprogramma getCampiCronoDaCopiare() {
		return campiCronoDaCopiare;
	}

	/**
	 * @param campiCronoDaCopiare the campiCronoDaCopiare to set
	 */
	public void setCampiCronoDaCopiare(Cronoprogramma campiCronoDaCopiare) {
		this.campiCronoDaCopiare = campiCronoDaCopiare;
	}

	/**
	 * Checks if is collegato A progetto di previsione.
	 *
	 * @return true, if is collegato A progetto di previsione
	 */
	public boolean isCollegatoAProgettoDiPrevisione() {
		TipoProgetto tipoProgetto = getTipoProgettoCollegato();
		return tipoProgetto != null && TipoProgetto.PREVISIONE.equals(tipoProgetto);
	}

	/**
	 * Checks if is collegato A progetto di previsione.
	 *
	 * @return true, if is collegato A progetto di previsione
	 */
	public boolean isCollegatoAProgettoDiGestione() {
		TipoProgetto tipoProgetto = getTipoProgettoCollegato();
		return tipoProgetto != null &&  TipoProgetto.GESTIONE.equals(tipoProgetto);
	}
	/**
	 * @return il tipo di progetto collegato
	 */
	public TipoProgetto getTipoProgettoCollegato() {
		return getProgetto() != null && getProgetto().getTipoProgetto() != null ? getProgetto().getTipoProgetto() : null;
	}
	
	/**
	 * @return the dettaglioEntrataFromEsistente
	 */
	public boolean isDettaglioEntrataFromEsistente() {
		return dettaglioEntrataCronoprogramma != null && dettaglioEntrataCronoprogramma.getCapitolo() != null && dettaglioEntrataCronoprogramma.getCapitolo().getUid() != 0;
	}
	
	/**
	 * @return the dettaglioUscitaFromEsistente
	 */
	public boolean isDettaglioUscitaFromEsistente() {
		return dettaglioUscitaCronoprogramma != null && dettaglioUscitaCronoprogramma.getCapitolo() != null && dettaglioUscitaCronoprogramma.getCapitolo().getUid() != 0;
	}
	
	//SIAC-
	/**
	 * Calcola data fine lavori.
	 *
	 * @param durataInGiorni the durata in giorni
	 * @param dataInizioLavori the data inizio lavori
	 * @return the date
	 */
	public Date calcolaDataFineLavori(Integer durataInGiorni, Date dataInizioLavori) {
		if(durataInGiorni == null || dataInizioLavori == null) {
			return null;
		}
		return DateUtils.addDays(dataInizioLavori, durataInGiorni.intValue());
		
	}

	/* ************ Requests ************ */
	
	/**
	 * Crea una request per il servizio di {@link RicercaCronoprogramma}.
	 * 
	 * @return la request creata
	 */
	public RicercaCronoprogramma creaRequestRicercaCronoprogramma() {
		RicercaCronoprogramma request = creaRequest(RicercaCronoprogramma.class);
		
		request.setCronoprogramma(creaCronoprogrammaPerRicerca());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioProgetto}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioProgetto creaRequestRicercaDettaglioProgetto() {
		RicercaDettaglioProgetto request = creaRequest(RicercaDettaglioProgetto.class);
		request.setChiaveProgetto(uidProgetto);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioModulareCapitoloEntrataPrevisione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioModulareCapitoloEntrataPrevisione creaRequestRicercaDettaglioModulareCapitoloEntrataPrevisione() {
		RicercaDettaglioModulareCapitoloEntrataPrevisione req = creaRequest(RicercaDettaglioModulareCapitoloEntrataPrevisione.class);
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		capitoloEntrataPrevisione.setUid(getDettaglioEntrataCronoprogramma().getCapitolo().getUid());
		req.setCapitoloEntrataPrevisione(capitoloEntrataPrevisione);
		req.setModelDetails(CapitoloEntrataPrevisioneModelDetail.Categoria);
		
		return req;
	}
	
	/**
	 * Crea request ricerca dettaglio modulare capitolo entrata gestione.
	 *
	 * @return the ricerca dettaglio modulare capitolo entrata gestione
	 */
	public RicercaDettaglioModulareCapitoloEntrataGestione creaRequestRicercaDettaglioModulareCapitoloEntrataGestione() {
		RicercaDettaglioModulareCapitoloEntrataGestione req = creaRequest(RicercaDettaglioModulareCapitoloEntrataGestione.class);
		CapitoloEntrataGestione capitoloEntrataPrevisione = new CapitoloEntrataGestione();
		capitoloEntrataPrevisione.setUid(getDettaglioEntrataCronoprogramma().getCapitolo().getUid());
		req.setCapitoloEntrataGestione(capitoloEntrataPrevisione);
		req.setModelDetails(CapitoloEntrataGestioneModelDetail.Categoria);
		
		return req;
	}
	
	/* **** Metodi di utilita' **** */
	
	/**
	 * Popola il model a partire dalla response dei serviz&icirc;.
	 * 
	 * @param responseRicercaDettaglioProgetto la response della ricerca di dettaglio del progetto
	 */
	public void popolaProgettoaDallaResponse(RicercaDettaglioProgettoResponse responseRicercaDettaglioProgetto) {
		setProgetto(responseRicercaDettaglioProgetto.getProgetto());
	}
	
	/**
	 * Metodo di utilit&agrave; per la costruzione di un Cronoprogramma da injettare nella request.
	 * 
	 * @return il Cronoprogramma creato
	 */
	private Cronoprogramma creaCronoprogrammaPerRicerca() {
		cronoprogramma.setProgetto(getProgetto());
		cronoprogramma.setBilancio(getBilancio());
		cronoprogramma.setEnte(getEnte());
		cronoprogramma.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
		return cronoprogramma;
	}
	
	/**
	 * Popola i classificatori del DettaglioEntrataCronoprogramma.
	 * 
	 * @param listaTipologiaTitolo la lista delle tipologie da cui cercare il dato corretto
	 */
	public void popolaDettaglioEntrataCronoprogramma(List<TipologiaTitolo> listaTipologiaTitolo) {
		TitoloEntrata titoloEntrata = ComparatorUtils.searchByUid(listaTitoloEntrata, dettaglioEntrataCronoprogramma.getTitoloEntrata());
		dettaglioEntrataCronoprogramma.setTitoloEntrata(titoloEntrata);
		
		TipologiaTitolo tipologiaTitolo = ComparatorUtils.searchByUid(listaTipologiaTitolo, dettaglioEntrataCronoprogramma.getTipologiaTitolo());
		dettaglioEntrataCronoprogramma.setTipologiaTitolo(tipologiaTitolo);
	}
	
	/**
	 * Popola i classificatori del DettaglioEntrataCronoprogramma.
	 * 
	 * @param listaProgramma la lista deli programmi da cui cercare il dato corretto
	 */
	public void popolaDettaglioUscitaCronoprogramma(List<Programma> listaProgramma) {
		Missione missione = ComparatorUtils.searchByUid(listaMissione, dettaglioUscitaCronoprogramma.getMissione());
		dettaglioUscitaCronoprogramma.setMissione(missione);
		
		Programma programma = ComparatorUtils.searchByUid(listaProgramma, dettaglioUscitaCronoprogramma.getProgramma());
		dettaglioUscitaCronoprogramma.setProgramma(programma);
		
		TitoloSpesa titoloSpesa = ComparatorUtils.searchByUid(listaTitoloSpesa, dettaglioUscitaCronoprogramma.getTitoloSpesa());
		dettaglioUscitaCronoprogramma.setTitoloSpesa(titoloSpesa);
	}

	/**
	 * Popola la mappa dei totali per i dettagli di entrata.
	 */
	public void popolaMappaTotaliEntrata() {
		mappaTotali.clear();
		popolaMappaImporti(mappaTotali, listaDettaglioEntrataCronoprogramma);
	}
	
	/**
	 * Popola la mappa dei totali per i dettagli di uscita.
	 */
	public void popolaMappaTotaliUscita() {
		mappaTotali.clear();
		popolaMappaImporti(mappaTotali, listaDettaglioUscitaCronoprogramma);
	}
	
	/**
	 * Popola la mappa degli importi a partire dalla lista degli importi stessi.
	 * 
	 * @param mappa la mappa da popolare
	 * @param list  la lista tramite cui effettuare il popolamento
	 */
	private <T extends DettaglioBaseCronoprogramma> void popolaMappaImporti(Map<Integer, BigDecimal> mappa, List<T> list) {
		for(T t : list) {
			Integer anno = t.getAnnoCompetenza() == null ? Integer.valueOf(-1) : t.getAnnoCompetenza();
			BigDecimal bd = mappa.get(anno);
			if(bd == null) {
				bd = BigDecimal.ZERO;
			}
			bd = bd.add(t.getStanziamento());
			mappa.put(anno, bd);
		}
	}
	
	/**
	 * Calcola il totale delle spese afferenti il Cronoprogramma.
	 * 
	 * @return il totale delle spese
	 */
	public BigDecimal calcolaTotaleSpese() {
		return calcolaTotale(listaDettaglioEntrataCronoprogramma);
	}
	
	/**
	 * Calcola il totale delle entrate afferenti il Cronoprogramma.
	 * 
	 * @return il totale delle entrate
	 */
	public BigDecimal calcolaTotaleEntrate() {
		return calcolaTotale(listaDettaglioUscitaCronoprogramma);
	}
	
	/**
	 * Calcola il totale degli importi presenti nella lista fornita.
	 * 
	 * @param list la lista da cui evincere il totale degli importi
	 * 
	 * @return il totale degli importi afferenti la lista
	 */
	private <T extends DettaglioBaseCronoprogramma> BigDecimal calcolaTotale(List<T> list) {
		BigDecimal result = BigDecimal.ZERO;
		
		for(T t : list) {
			result = result.add(t.getStanziamento());
		}
		
		return result;
	}

	/**
	 * Controlla che l'anno degli importi di ciascun dettaglio di spesa sia coerente con almeno un anno dei dettagli di entrata.
	 * 
	 * @return <code>true</code> nel caso gli anni siano coerenti; <code>false</code> in caso contrario
	 */
	public boolean checkAnniImportiSpeseCoerentiConEntrate() {
		for(DettaglioUscitaCronoprogramma duc : listaDettaglioUscitaCronoprogramma) {
			boolean annoCoerente = false;
			Integer annoEntrata = duc.getAnnoEntrata();
			for(DettaglioEntrataCronoprogramma dec : listaDettaglioEntrataCronoprogramma) {
				if(dec.getAnnoCompetenza().equals(annoEntrata)) {
					// Se l'anno è coerente imposto a true il flag ed esco dal ciclo interno
					annoCoerente = true;
					break;
				}
			}
			
			if(!annoCoerente) {
				// L'anno della spesa non è coerente con l'anno di entrata
				return false;
			}
		}
		return true;
	}

	/**
	 * Controlla che gli importi di spesa per ogni anno sia coerente con gli importi di entrata per l'anno corrispondente.
	 * 
	 * @return <code>true</code> se gli importi sono corrispondenti; <code>false</code> in caso contrario
	 */
	public boolean checkImportiSpesePerAnnoCoerenteConImportiEntratePerAnno() {
		Map<Integer, BigDecimal> mappaSpese = new TreeMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> mappaEntrate = new TreeMap<Integer, BigDecimal>();
		
		// Popolo le mappe
		popolaMappaImportiPerConfrontoConEntrata(mappaSpese, listaDettaglioUscitaCronoprogramma);
		popolaMappaImporti(mappaEntrate, listaDettaglioEntrataCronoprogramma);
		
		for (Map.Entry<Integer, BigDecimal> entry : mappaSpese.entrySet()) {
			// So di avere lo stesso numero di elementi da entrambe le parti
			BigDecimal totaleSpese = entry.getValue();
			BigDecimal totaleEntrate = mappaEntrate.get(entry.getKey());
			
			if(totaleSpese.compareTo(totaleEntrate) != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Popola la mappa degli importi a partire dalla lista degli importi stessi basandosi sull'anno di entrata.
	 * 
	 * @param mappa la mappa da popolare
	 * @param lista la lista tramite cui effettuare il popolamento
	 */
	private void popolaMappaImportiPerConfrontoConEntrata(Map<Integer, BigDecimal> mappa, List<DettaglioUscitaCronoprogramma> lista) {
		for(DettaglioUscitaCronoprogramma duc : lista) {
			BigDecimal bd = mappa.get(duc.getAnnoEntrata());
			if(bd == null) {
				bd = BigDecimal.ZERO;
			}
			bd = bd.add(duc.getStanziamento());
			mappa.put(duc.getAnnoEntrata(), bd);
		}
	}
	
	/**
	 * Calcola la quadratura per il cronoprogramma.
	 */
	public void calcolaQuadraturaCronoprogramma() {
		quadratura.clear();
		BigDecimal entrata = BigDecimal.ZERO;
		BigDecimal uscita = BigDecimal.ZERO;
		for(DettaglioEntrataCronoprogramma dec : listaDettaglioEntrataCronoprogramma) {
			entrata = entrata.add(dec.getStanziamento());
		}
		for(DettaglioUscitaCronoprogramma duc : listaDettaglioUscitaCronoprogramma) {
			uscita = uscita.add(duc.getStanziamento());
		}
		
		BigDecimal differenza = entrata.subtract(uscita);
		
		quadratura.put("entrata", entrata);
		quadratura.put("uscita", uscita);
		quadratura.put("differenza", differenza);
	}

	/**
	 * Imposta default quadro economico.
	 * <br/>
	 * SIAC-6869, errore di analisi che rattoppo cosi
	 */
	protected void impostaDefaultQuadroEconomico() {
		if(isCollegatoAProgettoDiPrevisione() || getListaDettaglioUscitaCronoprogramma() == null) {
			return;
		}
		boolean gestioneQuadroEconomico = Boolean.TRUE.equals(getCronoprogramma().getGestioneQuadroEconomico());
		
		for (DettaglioUscitaCronoprogramma dtDaPopolare : getListaDettaglioUscitaCronoprogramma()) {
			impostaValoreSuDettaglio(gestioneQuadroEconomico, dtDaPopolare);
		}
	}

	/**
	 * @param gestioneQuadroEconomico
	 * @param dtDaPopolare
	 */
	private void impostaValoreSuDettaglio(boolean gestioneQuadroEconomico, DettaglioUscitaCronoprogramma dtDaPopolare) {
		boolean quadroValorizzato = dtDaPopolare.getQuadroEconomico() != null && dtDaPopolare.getQuadroEconomico().getUid() != 0; 
		if(!gestioneQuadroEconomico) {
			dtDaPopolare.setQuadroEconomico(null);
			dtDaPopolare.setImportoQuadroEconomico(null);
			return;
		}
		if(!quadroValorizzato) {
			dtDaPopolare.setImportoQuadroEconomico(null);
			return;
		}
		dtDaPopolare.setImportoQuadroEconomico(dtDaPopolare.getStanziamento());
	}
	/**
	 * Crea request ricerca sintetica quadro economico.
	 *
	 * @return the ricerca sintetica quadro economico
	 */
	public RicercaSinteticaQuadroEconomico creaRequestRicercaSinteticaQuadroEconomico() {
		RicercaSinteticaQuadroEconomico request = creaRequest(RicercaSinteticaQuadroEconomico.class);
		request.setBilancio(getBilancio());
		
		QuadroEconomico qe = new QuadroEconomico();
		qe.setStatoOperativoQuadroEconomico(StatoOperativoQuadroEconomico.VALIDO);
		request.setQuadroEconomico(qe);
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		return request;
	}

}
