/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoVersioneCronoprogramma;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoVersioneCronoprogrammaFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoTotale;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.model.RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Classe di model per l'aggiornamento del Progetto. Contiene una mappatura del form di aggiornamento.
 * 
 * @author Alessandra Osorio,Nazha Ahmad
 * @version 1.0.0 - 07/02/2014
 * @version 1.0.1 - 25/03/2015
 */
public class AggiornaProgettoModel extends GenericProgettoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6001840461586092716L;
	
	private Integer uidDaAggiornare;
	
	private Integer uidCronoprogrammaDaAnnullare;
	private Integer uidCronoprogrammaDaConsultare;
	private Integer uidCronoprogrammaPerProspettoFPV;
	
	private Map<Integer, BigDecimal[]> mappaTotaliCronoprogramma = new TreeMap<Integer, BigDecimal[]>();
	
	private BigDecimal fondoPluriennaleVincolatoEntrata = BigDecimal.ZERO;
	
	// FPV
	private List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscitaCronoprogramma;
	private List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> listaRiepilogoFondoPluriennaleVincolatoPerEntrate;
	private Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> mappaFondoPluriennaleVincolatoPerAnno;
	private List<FondoPluriennaleVincolatoEntrata> listaFondoPluriennaleVincolatoEntrata = new ArrayList<FondoPluriennaleVincolatoEntrata>();
	
	//FPV GESTIONE BILANCIO AGGIUNTI IL 19/03/2015 AHMAD
	private List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio;
	private List<FondoPluriennaleVincolatoEntrata> listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate;
	private List<FondoPluriennaleVincolatoTotale> listaFondoPluriennaleVincolatoTotale;

	private List<ProspettoRiassuntivoCronoprogramma> listaProspettoRiassuntivoCronoprogramma;
	
	private Integer uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV;
	private boolean modificabileInvestimentoInCorsoDiDefinizione;
	
	// SIAC-4427
	private boolean decentrato;

	//FINE AGGUNTI 
	/** Costruttore vuoto di default */
	public AggiornaProgettoModel() {
		super();
		setTitolo("Gestione Progetto");
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
	
	/**
	 * @return the uidCronoprogrammaDaAnnullare
	 */
	public Integer getUidCronoprogrammaDaAnnullare() {
		return uidCronoprogrammaDaAnnullare;
	}

	/**
	 * @param uidCronoprogrammaDaAnnullare the uidCronoprogrammaDaAnnullare to set
	 */
	public void setUidCronoprogrammaDaAnnullare(Integer uidCronoprogrammaDaAnnullare) {
		this.uidCronoprogrammaDaAnnullare = uidCronoprogrammaDaAnnullare;
	}

	/**
	 * @return the uidCronoprogrammaDaConsultare
	 */
	public Integer getUidCronoprogrammaDaConsultare() {
		return uidCronoprogrammaDaConsultare;
	}

	/**
	 * @param uidCronoprogrammaDaConsultare the uidCronoprogrammaDaConsultare to set
	 */
	public void setUidCronoprogrammaDaConsultare(
			Integer uidCronoprogrammaDaConsultare) {
		this.uidCronoprogrammaDaConsultare = uidCronoprogrammaDaConsultare;
	}
	
	/**
	 * @return the uidCronoprogrammaPerProspettoFPV
	 */
	public Integer getUidCronoprogrammaPerProspettoFPV() {
		return uidCronoprogrammaPerProspettoFPV;
	}

	/**
	 * @param uidCronoprogrammaPerProspettoFPV the uidCronoprogrammaPerProspettoFPV to set
	 */
	public void setUidCronoprogrammaPerProspettoFPV(
			Integer uidCronoprogrammaPerProspettoFPV) {
		this.uidCronoprogrammaPerProspettoFPV = uidCronoprogrammaPerProspettoFPV;
	}



	/**
	 * @return the mappaTotaliCronoprogramma
	 */
	public Map<Integer, BigDecimal[]> getMappaTotaliCronoprogramma() {
		return mappaTotaliCronoprogramma;
	}

	/**
	 * @param mappaTotaliCronoprogramma the mappaTotaliCronoprogramma to set
	 */
	public void setMappaTotaliCronoprogramma(Map<Integer, BigDecimal[]> mappaTotaliCronoprogramma) {
		this.mappaTotaliCronoprogramma = mappaTotaliCronoprogramma;
	}
	
	/**
	 * @return the listaFondoPluriennaleVincolatoEntrata
	 */
	public List<FondoPluriennaleVincolatoEntrata> getListaFondoPluriennaleVincolatoEntrata() {
		return listaFondoPluriennaleVincolatoEntrata;
	}

	/**
	 * @param listaFondoPluriennaleVincolatoEntrata the listaFondoPluriennaleVincolatoEntrata to set
	 */
	public void setListaFondoPluriennaleVincolatoEntrata(List<FondoPluriennaleVincolatoEntrata> listaFondoPluriennaleVincolatoEntrata) {
		this.listaFondoPluriennaleVincolatoEntrata = listaFondoPluriennaleVincolatoEntrata != null ? listaFondoPluriennaleVincolatoEntrata : new ArrayList<FondoPluriennaleVincolatoEntrata>();
	}
	
	/**
	 * @return the fondoPluriennaleVincolatoEntrata
	 */
	public BigDecimal getFondoPluriennaleVincolatoEntrata() {
		return fondoPluriennaleVincolatoEntrata;
	}

	/**
	 * @param fondoPluriennaleVincolatoEntrata the fondoPluriennaleVincolatoEntrata to set
	 */
	public void setFondoPluriennaleVincolatoEntrata(BigDecimal fondoPluriennaleVincolatoEntrata) {
		this.fondoPluriennaleVincolatoEntrata = fondoPluriennaleVincolatoEntrata;
	}
	
	/**
	 * @return the listaFondoPluriennaleVincolatoUscitaCronoprogramma
	 */
	public List<FondoPluriennaleVincolatoUscitaCronoprogramma> getListaFondoPluriennaleVincolatoUscitaCronoprogramma() {
		return listaFondoPluriennaleVincolatoUscitaCronoprogramma;
	}

	/**
	 * @param listaFondoPluriennaleVincolatoUscitaCronoprogramma the listaFondoPluriennaleVincolatoUscitaCronoprogramma to set
	 */
	public void setListaFondoPluriennaleVincolatoUscitaCronoprogramma(
			List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscitaCronoprogramma) {
		this.listaFondoPluriennaleVincolatoUscitaCronoprogramma = listaFondoPluriennaleVincolatoUscitaCronoprogramma;
	}

	/**
	 * @return the listaRiepilogoFondoPluriennaleVincolatoPerEntrate
	 */
	public List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> getListaRiepilogoFondoPluriennaleVincolatoPerEntrate() {
		return listaRiepilogoFondoPluriennaleVincolatoPerEntrate;
	}

	/**
	 * @param listaRiepilogoFondoPluriennaleVincolatoPerEntrate the listaRiepilogoFondoPluriennaleVincolatoPerEntrate to set
	 */
	public void setListaRiepilogoFondoPluriennaleVincolatoPerEntrate(
			List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> listaRiepilogoFondoPluriennaleVincolatoPerEntrate) {
		this.listaRiepilogoFondoPluriennaleVincolatoPerEntrate = listaRiepilogoFondoPluriennaleVincolatoPerEntrate;
	}

	/**
	 * @return the mappaFondoPluriennaleVincolatoPerAnno
	 */
	public Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> getMappaFondoPluriennaleVincolatoPerAnno() {
		return mappaFondoPluriennaleVincolatoPerAnno;
	}

	/**
	 * @param mappaFondoPluriennaleVincolatoPerAnno the mappaFondoPluriennaleVincolatoPerAnno to set
	 */
	public void setMappaFondoPluriennaleVincolatoPerAnno(
			Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> mappaFondoPluriennaleVincolatoPerAnno) {
		this.mappaFondoPluriennaleVincolatoPerAnno = mappaFondoPluriennaleVincolatoPerAnno;
	}

	/**
	 * @return the listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio
	 */
	public List<FondoPluriennaleVincolatoUscitaCronoprogramma> getListaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio() {
		return listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio;
	}

	/**
	 * @param listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio the listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio to set
	 */
	public void setListaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio(List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio) {
		this.listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio = listaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio;
	}

	/**
	 * @return the listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate
	 */
	public List<FondoPluriennaleVincolatoEntrata> getListaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate() {
		return listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate;
	}

	/**
	 * @param listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate the listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate to set
	 */
	public void setListaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate(List<FondoPluriennaleVincolatoEntrata> listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate) {
		this.listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate = listaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate;
	}

	/**
	 * @return the listaFondoPluriennaleVincolatoTotale
	 */
	public List<FondoPluriennaleVincolatoTotale> getListaFondoPluriennaleVincolatoTotale() {
		return listaFondoPluriennaleVincolatoTotale;
	}
	
	/**
	 * 
	 * @param listaFondoPluriennaleVincolatoTotale the listaFondoPluriennaleVincolatoTotale to set
	 */
	public void setListaFondoPluriennaleVincolatoTotale(List<FondoPluriennaleVincolatoTotale> listaFondoPluriennaleVincolatoTotale) {
		this.listaFondoPluriennaleVincolatoTotale = listaFondoPluriennaleVincolatoTotale;
	}

	
	/**
	 * @return the uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV
	 */
	public Integer getUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV() {
		return uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV;
	}

	/**
	 * @param uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV the uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV to set
	 */
	public void setUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV(
			Integer uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV) {
		this.uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV = uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV;
	}
	
	/**
	 * @return the modificabileInvestimentoInCorsoDiDefinizione
	 */
	public boolean isModificabileInvestimentoInCorsoDiDefinizione() {
		return modificabileInvestimentoInCorsoDiDefinizione;
	}

	/**
	 * @param modificabileInvestimentoInCorsoDiDefinizione the modificabileInvestimentoInCorsoDiDefinizione to set
	 */
	public void setModificabileInvestimentoInCorsoDiDefinizione(boolean modificabileInvestimentoInCorsoDiDefinizione) {
		this.modificabileInvestimentoInCorsoDiDefinizione = modificabileInvestimentoInCorsoDiDefinizione;
	}
	
	
	/**
	 * @return the decentrato
	 */
	public boolean isDecentrato() {
		return decentrato;
	}

	/**
	 * @param decentrato the decentrato to set
	 */
	public void setDecentrato(boolean decentrato) {
		this.decentrato = decentrato;
	}

	
	/**
	 * @return the parereRegolaritaValido
	 */
	public boolean isParereRegolaritaValido() {
		return getProgetto() != null && getProgetto().getAttoAmministrativo() != null && Boolean.TRUE.equals(getProgetto().getAttoAmministrativo().getParereRegolaritaContabile());
	}
	
	/* Requests */


	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Vincolo.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioProgetto creaRequestRicercaDettaglioProgetto() {
		RicercaDettaglioProgetto request = creaRequest(RicercaDettaglioProgetto.class);
		
		request.setChiaveProgetto(uidDaAggiornare);
		
		return request;
	}
	
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
	 * Crea una request per il servizio di Ricerca Progetto.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaProgetto creaRequestRicercaProgetto() {
		RicercaSinteticaProgetto request = creaRequest(RicercaSinteticaProgetto.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Progetto.
	 * 
	 * @return la request creata
	 */
	public AggiornaAnagraficaProgetto creaRequestAggiornaAnagraficaProgetto() {
		AggiornaAnagraficaProgetto request = creaRequest(AggiornaAnagraficaProgetto.class);
		
		request.setProgetto(popolaProgettoPerAggiornamentoAnagrafica());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Annulla Cronoprogramma.
	 * 
	 * @return la request creata
	 */
	public AnnullaCronoprogramma creaRequestAnnullaCronoprogramma() {
		AnnullaCronoprogramma request = creaRequest(AnnullaCronoprogramma.class);
		
		Cronoprogramma c = new Cronoprogramma();
		c.setUid(uidCronoprogrammaDaAnnullare);
		
		request.setCronoprogramma(c);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Dettaglio Cronoprogramma.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCronoprogramma creaRequestRicercaDettaglioCronoprogramma() {
		RicercaDettaglioCronoprogramma request = creaRequest(RicercaDettaglioCronoprogramma.class);
		Cronoprogramma c = new Cronoprogramma();
		c.setUid(uidCronoprogrammaDaConsultare);
		
		request.setCronoprogramma(c);
		
		return request;
	}
	
	/* Metodi di utilita' */

	/**
	 * Popola il progetto in maniera adeguata per l'aggiornamento dell'anagrafica.
	 * 
	 * @return il progetto popolato
	 */
	private Progetto popolaProgettoPerAggiornamentoAnagrafica() {
		getProgetto().setAttoAmministrativo(idEntitaPresente(getAttoAmministrativo()) ? getAttoAmministrativo() : null);
		getProgetto().setEnte(getEnte());
		getProgetto().setInvestimentoInCorsoDiDefinizione(Boolean.TRUE.equals(getProgetto().getInvestimentoInCorsoDiDefinizione()));
		return getProgetto();
	}
	
	/**
	 * Imposta i wrappers per i cronoprogrammi.
	 *
	 * @param list                  la lista tramite cui popolare
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param tipoProgetto the tipo progetto
	 */
	public void impostaWrappersCronoprogrammi(List<Cronoprogramma> list, List<AzioneConsentita> listaAzioniConsentite, TipoProgetto tipoProgetto) {
		List<ElementoVersioneCronoprogramma> listaWrappers = ElementoVersioneCronoprogrammaFactory.getInstances(list, getAnnoEsercizioInt(), listaAzioniConsentite, tipoProgetto);
		setListaElementiVersioneCronoprogramma(listaWrappers);
		//setto la list<Cronoprogramma> nel model 
		setListaCronoprogrammiCollegatiAlProgetto(list);
		
//		addCronoprogrammaDaGestione(listaAzioniConsentite);
	}

	/**
	 * Popola la mappa dei totali per il cronoprogramma fornito.
	 * 
	 * @param cronoprogramma il cronoprogramma i cui totali sono da ottenere
	 */
	public void popolaMappaTotaliCronoprogramma(Cronoprogramma cronoprogramma) {
		mappaTotaliCronoprogramma.clear();
		
		for(DettaglioEntrataCronoprogramma dec : cronoprogramma.getCapitoliEntrata()) {
			popolaMappaTotaliDaDettaglio(dec, 0);
		}
		
		for(DettaglioUscitaCronoprogramma duc : cronoprogramma.getCapitoliUscita()) {
			popolaMappaTotaliDaDettaglio(duc, 1);
		}
	}
	
	/**
	 * Popola la mappa dei totali a partire dal singolo dettaglio.
	 * 
	 * @param dettaglio   il dettaglio tramite cui popolare la mappa
	 * @param indiceMappa l'indice che l'importo del dettaglio avr&agrave; nel vettore costituente il valore della mappa; in particolare &eacute; pari a 0 per
	 *                        il dettaglio di entrata e pari a 1 per il dettaglio di uscita 
	 */
	private <T extends DettaglioBaseCronoprogramma> void popolaMappaTotaliDaDettaglio(T dettaglio, int indiceMappa) {
		Integer idx = dettaglio.getAnnoCompetenza() != null ? dettaglio.getAnnoCompetenza() : Integer.valueOf(-1);
		BigDecimal[] arr = mappaTotaliCronoprogramma.get(idx);
		if(arr == null) {
			arr = new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO};
		}
		arr[indiceMappa] = arr[indiceMappa].add(dettaglio.getStanziamento());
		mappaTotaliCronoprogramma.put(idx, arr);
	}

	//aggiunti ahmad 19/03/2015
	
	/**
	 * Crea una request per il servizio di Calcolo Fondo Pluriennale Vincolato Da gestione Da getBilancio() COMPLESSIVO.
	 * 
	 * @return la request creata
	 */
	public CalcoloFondoPluriennaleVincolatoComplessivo creaRequestCalcoloFondoPluriennaleVincolatoComplessivo() {
		CalcoloFondoPluriennaleVincolatoComplessivo request = creaRequest(CalcoloFondoPluriennaleVincolatoComplessivo.class);
		
		// progetto 
		request.setProgetto(getProgetto());
		//anno del getBilancio()
		request.setAnno(getBilancio().getAnno());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Calcolo Fondo Pluriennale  Vincolato  Da gestione Da getBilancio() Entrata.
	 * 
	 * @return la request creata
	 */
	public CalcoloFondoPluriennaleVincolatoEntrata creaRequestCalcoloFondoPluriennaleVincolatoEntrata() {
		CalcoloFondoPluriennaleVincolatoEntrata request = creaRequest(CalcoloFondoPluriennaleVincolatoEntrata.class);
		
		// progetto 
		request.setProgetto(getProgetto());
		
		
		//anno del getBilancio()
		request.setAnno(getBilancio().getAnno());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Calcolo Fondo Pluriennale Da gestione Da getBilancio() Vincolato spesa.
	 * 
	 * @return la request creata
	 */
	public CalcoloFondoPluriennaleVincolatoSpesa creaRequestCalcoloFondoPluriennaleVincolatoSpesa() {
		CalcoloFondoPluriennaleVincolatoSpesa request = creaRequest(CalcoloFondoPluriennaleVincolatoSpesa.class);
		
		
		
		// progetto 
		request.setProgetto(getProgetto());
		
		//anno del getBilancio()
		request.setAnno(getBilancio().getAnno());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Calcolo Prospetto Riassuntivo cronoprogramma di Gestione  .
	 * 
	 * @return la request creata
	 */
	public CalcoloProspettoRiassuntivoCronoprogramma creaRequestCalcoloProspettoRiassuntivoCronoprogramma() {
		   CalcoloProspettoRiassuntivoCronoprogramma request = creaRequest(CalcoloProspettoRiassuntivoCronoprogramma.class);
		
		request.setProgetto(getProgetto());
		
		//anno del getBilancio()
		request.setAnno(getBilancio().getAnno());
		return request;
	}
	
	/**
	 * Viene richiamata per associare il cronoprogramma come usato per fpv ed aggiornarlo sul db
	 * 
	 * @return la request creata
	 */
	public CambiaFlagUsatoPerFpvCronoprogramma creaRequestCambiaFlagUsatoPerFpvCronoprogramma() {

		CambiaFlagUsatoPerFpvCronoprogramma request = creaRequest(CambiaFlagUsatoPerFpvCronoprogramma.class);
		
		request.setCronoprogramma(getCronoprogrammaDaAggiornare());
		request.setBilancio(getBilancio());
		
		return request;
	}

	/**
	 * Crea la request per il servizio {@link OttieniFondoPluriennaleVincolatoCronoprogramma}
	 * @return la request creata
	 */
	public OttieniFondoPluriennaleVincolatoCronoprogramma creaRequestOttieniFondoPluriennaleVincolatoCronoprogramma() {
		
		OttieniFondoPluriennaleVincolatoCronoprogramma request = creaRequest(OttieniFondoPluriennaleVincolatoCronoprogramma.class);
		Cronoprogramma c = new Cronoprogramma();
		c.setUid(uidCronoprogrammaPerProspettoFPV);
		request.setCronoprogramma(c);
		request.setBilancio(getBilancio());
		
		return request;
	}

	/**
	 * Crea una request per il servizio {@link RicercaProvvedimento}
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento req = creaRequest(RicercaProvvedimento.class);
		
		req.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setUid(getAttoAmministrativo().getUid());
		req.setRicercaAtti(ricercaAtti);
		
		return req;
	}

}
