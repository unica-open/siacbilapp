/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

/**
 * Classe di model per la consultazione del Progetto. Contiene una mappatura del form di consultazione.
 * 
 * @author Alessandra Osorio,Nazha Ahmad
 * @version 1.0.0 - 10/02/2014
 * @version 1.0.1 - 25/03/2015
 */
public class ConsultaProgettoModel extends GenericProgettoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6001840461586092716L;
	
	private String stato;
	private Integer uidDaConsultare;
	private Integer idCronoprogramma;
	private String descrizioneProvvedimentoPerConsulta;

	private Map<Integer, BigDecimal> mappaTotali = new TreeMap<Integer, BigDecimal>();
	//aggiunta il 24_03_2015 ahmad
	private List<ProspettoRiassuntivoCronoprogramma> listaProspettoRiassuntivoCronoprogramma;

	/** Costruttore vuoto di default */
	public ConsultaProgettoModel() {
		super();
		setTitolo("Consulta Progetto");
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public Integer getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(Integer uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}

	/**
	 * @return the idCronoprogramma
	 */
	public Integer getIdCronoprogramma() {
		return idCronoprogramma;
	}

	/**
	 * @param idCronoprogramma the idCronoprogramma to set
	 */
	public void setIdCronoprogramma(Integer idCronoprogramma) {
		this.idCronoprogramma = idCronoprogramma;
	}

	/**
	 * @return the descrizioneProvvedimentoPerConsulta
	 */
	public String getDescrizioneProvvedimentoPerConsulta() {
		return descrizioneProvvedimentoPerConsulta;
	}

	/**
	 * @param descrizioneProvvedimentoPerConsulta the descrizioneProvvedimentoPerConsulta to set
	 */
	public void setDescrizioneProvvedimentoPerConsulta(
			String descrizioneProvvedimentoPerConsulta) {
		this.descrizioneProvvedimentoPerConsulta = descrizioneProvvedimentoPerConsulta;
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
	 * Ottiene una capitalizzazione per lo stato operativo del progetto.
	 * 
	 * @return lo stato operativo del progetto correttamente capitalizzato
	 */
	public String getStatoOperativoProgettoCapitalizzato() {
		return capitaliseString(getProgetto().getStatoOperativoProgetto().name());
	}
	
	
	
	/* Requests */

	/**
	 * @return the listaProspettoRiassuntivoCronoprogramma
	 */
	public List<ProspettoRiassuntivoCronoprogramma> getListaProspettoRiassuntivoCronoprogramma() {
		return listaProspettoRiassuntivoCronoprogramma;
	}

	/**
	 * @param listaProspettoRiassuntivoCronoprogramma the listaProspettoRiassuntivoCronoprogramma to set
	 */
	public void setListaProspettoRiassuntivoCronoprogramma(
			List<ProspettoRiassuntivoCronoprogramma> listaProspettoRiassuntivoCronoprogramma) {
		this.listaProspettoRiassuntivoCronoprogramma = listaProspettoRiassuntivoCronoprogramma;
	}

	
	/**
	 * Gets the servizio.
	 *
	 * @return the servizio
	 */
	public String getServizio() {		
		return getProgetto()!= null? FormatUtils.formatCodificaCodiceDescrizione(getProgetto().getStrutturaAmministrativoContabile()) : "";
	}
	
	/**
	 * Gets the spazi finanziari.
	 *
	 * @return the spazi finanziari
	 */
	public String getSpaziFinanziari() {
		return getProgetto()!= null? FormatUtils.formatBoolean(getProgetto().getSpaziFinanziari(), "Si", "No", "") : "";
	}
	
	/**
	 * Gets the modalita affidamento.
	 *
	 * @return the modalita affidamento
	 */
	public String getModalitaAffidamento() {
		return getProgetto()!= null? FormatUtils.formatCodificaCodiceDescrizione(getProgetto().getModalitaAffidamentoProgetto()) : "";
	}
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Progetto.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioProgetto creaRequestRicercaDettaglioProgetto() {
		RicercaDettaglioProgetto request = creaRequest(RicercaDettaglioProgetto.class);
		request.setChiaveProgetto(uidDaConsultare);
		request.setProgettoModelDetails(new ProgettoModelDetail[] {ProgettoModelDetail.MutuiAssociati});
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Cronoprogramma.
	 * 
	 * @return la request creata
	 */
	
	public RicercaDettaglioCronoprogramma creaRequestRicercaDettaglioCronoprogramma() {
		RicercaDettaglioCronoprogramma request = creaRequest(RicercaDettaglioCronoprogramma.class);
		request.setCronoprogramma(generaCronoprogramma());
		
		return request;
	}
		
	
	/* Metodi di utilita' */

	@Override
	public void impostaDati(RicercaDettaglioProgettoResponse response, List<Cronoprogramma> listaCronoprogrammi) {
		super.impostaDati(response, listaCronoprogrammi);
		String provvedimento = response.getProgetto().getAttoAmministrativo()!= null?
					Integer.toString(response.getProgetto().getAttoAmministrativo().getAnno()).concat(" / ")
					.concat(Integer.toString(response.getProgetto().getAttoAmministrativo().getNumero()).concat(" / ")
					.concat(capitaliseString(response.getProgetto().getAttoAmministrativo().getTipoAtto().getDescrizione()).concat(" del "))
					.concat(FormatUtils.formatDate(response.getProgetto().getAttoAmministrativo().getDataCreazioneAttoAmministrativo())))
					:"";
		
		setDescrizioneProvvedimentoPerConsulta(provvedimento);
	}
	
	/**
	 * Genera un cronoprogramma con i dati essenziali per la ricerca.
	 * 
	 * @return il cronoprogramma generato
	 */
	private Cronoprogramma generaCronoprogramma() {
		Cronoprogramma c = new Cronoprogramma();
		c.setUid(getIdCronoprogramma());
		c.setBilancio(getBilancio());
		c.setEnte(getEnte());
		return c;
	}
	
	
	/**
	 * Popola la mappa dei totali per i dettagli di entrata.
	 * 
	 * @param cronoprogramma il cronoprogramma tramite cui popolare la mappa 
	 */
	public void popolaMappaTotaliEntrata(Cronoprogramma cronoprogramma) {
		mappaTotali.clear();
		popolaMappaImporti(mappaTotali, cronoprogramma.getCapitoliEntrata());
	}
	
	/**
	 * Popola la mappa dei totali per i dettagli di uscita.
	 * 
	 * @param cronoprogramma il cronoprogramma tramite cui popolare la mappa
	 */
	public void popolaMappaTotaliUscita(Cronoprogramma cronoprogramma) {
		mappaTotali.clear();
		popolaMappaImporti(mappaTotali, cronoprogramma.getCapitoliUscita());
	}
	
	
	/**
	 * Popola la mappa degli importi a partire dalla lista degli importi stessi.
	 * 
	 * @param mappa la mappa da popolare
	 * @param list  la lista tramite cui effettuare il popolamento
	 */
	private <T extends DettaglioBaseCronoprogramma> void popolaMappaImporti(Map<Integer, BigDecimal> mappa, List<T> list) {
		for(T t : list) {
			BigDecimal bd = mappa.get(t.getAnnoCompetenza());
			if(bd == null) {
				bd = BigDecimal.ZERO;
			}
			bd = bd.add(t.getStanziamento());
			mappa.put(t.getAnnoCompetenza(), bd);
		}
	}

//	/**
//	 * Crea una request per il servizio di Calcolo Prospetto Riassuntivo cronoprogramma di Gestione.
//	 * @param uid l'uid per cui creare la richiesta
//	 * 
//	 * @return la request creata
//	 */
//	public CalcoloProspettoRiassuntivoCronoprogramma creaRequestCalcoloProspettoRiassuntivoCronoprogramma(Integer uid) {
//		   CalcoloProspettoRiassuntivoCronoprogramma request = new CalcoloProspettoRiassuntivoCronoprogramma();
//		
//		request.setDataOra(new Date());
//		request.setRichiedente(getRichiedente());
//	
//		// progetto 
//		Progetto p = new Progetto();
//		p.setUid(uid);
//		request.setProgetto(p);
//		
//		//anno del getBilancio()
//		request.setAnno(getBilancio().getAnno());
//		return request;
//	}
	
	
}
