/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.GruppoAzioni;

/**
 * Classe di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione
 * 
 * @author Alessio Romanato
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/10/2016
 */
public class InserisciConfigurazioneStampaDubbiaEsigibilitaModel extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6728629980362725537L;
	
	private List<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione = new ArrayList<CapitoloEntrataPrevisione>();
	private List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	private List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaSelezionati = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	private List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilita = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();

	private AccantonamentoFondiDubbiaEsigibilita accantonamento = new AccantonamentoFondiDubbiaEsigibilita();
	
	// SIAC-5481
	private boolean datiAnnoPrecedenteGestionePresenti;
	private Azione azioneReportistica;
	private GruppoAzioni gruppoAzioniReportistica;
	
	/** Costruttore vuoto di default */
	public InserisciConfigurazioneStampaDubbiaEsigibilitaModel(){
		super();
		setTitolo("Gestione fondo crediti dubbia esigibilità - previsione");
	}
	
	/**
	 * @return the listaCapitoloEntrataPrevisione
	 */
	public List<CapitoloEntrataPrevisione> getListaCapitoloEntrataPrevisione() {
		return listaCapitoloEntrataPrevisione;
	}

	/**
	 * @param listaCapitoloEntrataPrevisione the listaCapitoloEntrataPrevisione to set
	 */
	public void setListaCapitoloEntrataPrevisione(List<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione) {
		this.listaCapitoloEntrataPrevisione = listaCapitoloEntrataPrevisione != null ? listaCapitoloEntrataPrevisione : new ArrayList<CapitoloEntrataPrevisione>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaTemp
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> getListaAccantonamentoFondiDubbiaEsigibilitaTemp() {
		return listaAccantonamentoFondiDubbiaEsigibilitaTemp;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaTemp the listaAccantonamentoFondiDubbiaEsigibilitaTemp to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaTemp(List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaTemp) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaTemp = listaAccantonamentoFondiDubbiaEsigibilitaTemp != null ? listaAccantonamentoFondiDubbiaEsigibilitaTemp : new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaSelezionati
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> getListaAccantonamentoFondiDubbiaEsigibilitaSelezionati() {
		return listaAccantonamentoFondiDubbiaEsigibilitaSelezionati;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaSelezionati the listaAccantonamentoFondiDubbiaEsigibilitaSelezionati to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaSelezionati(List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilitaSelezionati) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaSelezionati = listaAccantonamentoFondiDubbiaEsigibilitaSelezionati != null ? listaAccantonamentoFondiDubbiaEsigibilitaSelezionati : new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilita
	 */
	public List<AccantonamentoFondiDubbiaEsigibilita> getListaAccantonamentoFondiDubbiaEsigibilita() {
		return listaAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilita the listaAccantonamentoFondiDubbiaEsigibilita to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilita(List<AccantonamentoFondiDubbiaEsigibilita> listaAccantonamentoFondiDubbiaEsigibilita) {
		this.listaAccantonamentoFondiDubbiaEsigibilita = listaAccantonamentoFondiDubbiaEsigibilita != null ? listaAccantonamentoFondiDubbiaEsigibilita : new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	}

	/**
	 * @return the accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilita getAccantonamento() {
		return accantonamento;
	}

	/**
	 * @param accantonamento the accantonamento to set
	 */
	public void setAccantonamento(AccantonamentoFondiDubbiaEsigibilita accantonamento) {
		this.accantonamento = accantonamento;
	}

	/**
	 * @return the datiAnnoPrecedenteGestionePresenti
	 */
	public boolean isDatiAnnoPrecedenteGestionePresenti() {
		return datiAnnoPrecedenteGestionePresenti;
	}

	/**
	 * @param datiAnnoPrecedenteGestionePresenti the datiAnnoPrecedenteGestionePresenti to set
	 */
	public void setDatiAnnoPrecedenteGestionePresenti(boolean datiAnnoPrecedenteGestionePresenti) {
		this.datiAnnoPrecedenteGestionePresenti = datiAnnoPrecedenteGestionePresenti;
	}

	/**
	 * @return the azioneReportistica
	 */
	public Azione getAzioneReportistica() {
		return azioneReportistica;
	}

	/**
	 * @param azioneReportistica the azioneReportistica to set
	 */
	public void setAzioneReportistica(Azione azioneReportistica) {
		this.azioneReportistica = azioneReportistica;
	}

	/**
	 * @return the gruppoAzioniReportistica
	 */
	public GruppoAzioni getGruppoAzioniReportistica() {
		return gruppoAzioniReportistica;
	}

	/**
	 * @param gruppoAzioniReportistica the gruppoAzioniReportistica to set
	 */
	public void setGruppoAzioniReportistica(GruppoAzioni gruppoAzioniReportistica) {
		this.gruppoAzioniReportistica = gruppoAzioniReportistica;
	}

	@Override
	public String getActionOperazioneAttributi() {
		return "inserisciConfigurazioneStampaDubbiaEsigibilita_" + (isAttributiBilancioPresenti() ? "aggiornaAttributiBilancio" : "salvaAttributiBilancio");
	}

	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceFondiDubbiaEsigibilita}.
	 * @return la request creata
	 */
	public InserisceFondiDubbiaEsigibilita creaRequestInserisceFondiDubbiaEsigibilita() {
		InserisceFondiDubbiaEsigibilita request = creaRequest(InserisceFondiDubbiaEsigibilita.class);
		request.setAccantonamentiFondiDubbiaEsigibilita(getListaAccantonamentoFondiDubbiaEsigibilitaSelezionati());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaFondiDubbiaEsigibilita}.
	 * @return la request creata
	 */
	public RicercaSinteticaFondiDubbiaEsigibilita creaRequestRicercaSinteticaFondiDubbiaEsigibilita() {
		RicercaSinteticaFondiDubbiaEsigibilita request = creaRequest(RicercaSinteticaFondiDubbiaEsigibilita.class);
	
		request.setAccantonamentoFondiDubbiaEsigibilitaModelDetails(AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione);
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaFondoDubbiaEsigibilita}.
	 * @return la request creata
	 */
	public AggiornaFondoDubbiaEsigibilita creaRequestAggiornaFondoDubbiaEsigibilita() {
		AggiornaFondoDubbiaEsigibilita request = creaRequest(AggiornaFondoDubbiaEsigibilita.class);
		request.setAccantonamentoFondiDubbiaEsigibilita(getAccantonamento());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link EliminaFondoDubbiaEsigibilita}.
	 * @return la request creata
	 */
	public EliminaFondoDubbiaEsigibilita creaRequestEliminaFondoDubbiaEsigibilita() {
		EliminaFondoDubbiaEsigibilita request = creaRequest(EliminaFondoDubbiaEsigibilita.class);
		request.setAccantonamentoFondiDubbiaEsigibilita(getAccantonamento());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ControllaFondiDubbiaEsigibilitaAnnoPrecedente}.
	 * @return la request creata
	 */
	public ControllaFondiDubbiaEsigibilitaAnnoPrecedente creaRequestControllaFondiDubbiaEsigibilitaAnnoPrecedente() {
		ControllaFondiDubbiaEsigibilitaAnnoPrecedente req = creaRequest(ControllaFondiDubbiaEsigibilitaAnnoPrecedente.class);
		req.setBilancio(getBilancio());
		return req;
	}

	/**
	 * Rimozione dei capitoli dalla lista dei remporanei
	 */
	public void rimuoviCapitoliDaTemp() {
		rimuoviCapitoliDaTemp(getListaAccantonamentoFondiDubbiaEsigibilitaTemp(), getListaAccantonamentoFondiDubbiaEsigibilitaSelezionati());
	}

	/**
	 * Crea una request per il servizio di {@link PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente}.
	 * @return la request creata
	 */
	public PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente creaRequestPopolaFondiDubbiaEsigibilitaDaAnnoPrecedente() {
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente req = creaRequest(PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente.class);
		req.setBilancio(getBilancio());
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente}.
	 * @return la request creata
	 */
	public PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente creaRequestPopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente() {
		PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente req = creaRequest(PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente.class);
		req.setBilancio(getBilancio());
		return req;
	}

	
}
