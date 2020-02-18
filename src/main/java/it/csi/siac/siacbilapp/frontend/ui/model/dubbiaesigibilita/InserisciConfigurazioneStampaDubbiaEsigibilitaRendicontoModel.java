/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.GruppoAzioni;

/**
 * Classe base di modello per la gestione della configurazioen delle stampe dei fondi a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
public class InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel extends InserisciConfigurazioneStampaDubbiaEsigibilitaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6054489897303243021L;
	
	private List<CapitoloEntrataGestione> listaCapitoloEntrataGestione = new ArrayList<CapitoloEntrataGestione>();
	private List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	private List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	private List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendiconto = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();

	private AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamento = new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
	
	// SIAC-5304
	private Azione azioneReportistica;
	private GruppoAzioni gruppoAzioniReportistica;
	
	/** Costruttore vuoto di default */
	public InserisciConfigurazioneStampaDubbiaEsigibilitaRendicontoModel(){
		super();
		setTitolo("Gestione fondo crediti dubbia esigibilità - rendiconto / assestamento");
	}
	
	/**
	 * @return the listaCapitoloEntrataGestione
	 */
	public List<CapitoloEntrataGestione> getListaCapitoloEntrataGestione() {
		return listaCapitoloEntrataGestione;
	}

	/**
	 * @param listaCapitoloEntrataGestione the listaCapitoloEntrataGestione to set
	 */
	public void setListaCapitoloEntrataGestione(List<CapitoloEntrataGestione> listaCapitoloEntrataGestione) {
		this.listaCapitoloEntrataGestione = listaCapitoloEntrataGestione != null ? listaCapitoloEntrataGestione : new ArrayList<CapitoloEntrataGestione>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp() {
		return listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp(List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp = listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp != null ? listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati() {
		return listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati the listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati(List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati = listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati != null ? listaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	}

	/**
	 * @return the listaAccantonamentoFondiDubbiaEsigibilitaRendiconto
	 */
	public List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto() {
		return listaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
	}

	/**
	 * @param listaAccantonamentoFondiDubbiaEsigibilitaRendiconto the listaAccantonamentoFondiDubbiaEsigibilitaRendiconto to set
	 */
	public void setListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> listaAccantonamentoFondiDubbiaEsigibilitaRendiconto) {
		this.listaAccantonamentoFondiDubbiaEsigibilitaRendiconto = listaAccantonamentoFondiDubbiaEsigibilitaRendiconto != null ? listaAccantonamentoFondiDubbiaEsigibilitaRendiconto : new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	}

	/**
	 * @return the accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaRendiconto getAccantonamento() {
		return accantonamento;
	}

	/**
	 * @param accantonamento the accantonamento to set
	 */
	public void setAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamento) {
		this.accantonamento = accantonamento;
	}

	@Override
	public String getActionOperazioneAttributi() {
		return "inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_" + (isAttributiBilancioPresenti() ? "aggiornaAttributiBilancio" : "salvaAttributiBilancio");
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


	// REQUESTS
	
	/**
	 * Crea una request per il servizio di {@link InserisceFondiDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 */
	public InserisceFondiDubbiaEsigibilitaRendiconto creaRequestInserisceFondiDubbiaEsigibilitaRendiconto() {
		InserisceFondiDubbiaEsigibilitaRendiconto request = creaRequest(InserisceFondiDubbiaEsigibilitaRendiconto.class);
		request.setAccantonamentiFondiDubbiaEsigibilitaRendiconto(getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaFondiDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 */
	public RicercaSinteticaFondiDubbiaEsigibilitaRendiconto creaRequestRicercaSinteticaFondiDubbiaEsigibilitaRendiconto() {
		RicercaSinteticaFondiDubbiaEsigibilitaRendiconto request = creaRequest(RicercaSinteticaFondiDubbiaEsigibilitaRendiconto.class);
	
		request.setAccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetails(AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.CapitoloEntrataGestione);
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaFondoDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 */
	public AggiornaFondoDubbiaEsigibilitaRendiconto creaRequestAggiornaFondoDubbiaEsigibilitaRendiconto() {
		AggiornaFondoDubbiaEsigibilitaRendiconto request = creaRequest(AggiornaFondoDubbiaEsigibilitaRendiconto.class);
		request.setAccantonamentoFondiDubbiaEsigibilitaRendiconto(getAccantonamento());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link EliminaFondoDubbiaEsigibilitaRendiconto}.
	 * @return la request creata
	 */
	public EliminaFondoDubbiaEsigibilitaRendiconto creaRequestEliminaFondoDubbiaEsigibilitaRendiconto() {
		EliminaFondoDubbiaEsigibilitaRendiconto request = creaRequest(EliminaFondoDubbiaEsigibilitaRendiconto.class);
		request.setAccantonamentoFondiDubbiaEsigibilitaRendiconto(getAccantonamento());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente}.
	 * @return la request creata
	 */
	public ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente creaRequestControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente() {
		ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente req = creaRequest(ControllaFondiDubbiaEsigibilitaRendicontoAnnoPrecedente.class);
		req.setBilancio(getBilancio());
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente}.
	 * @return la request creata
	 */
	public ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente creaRequestControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente() {
		ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente req = creaRequest(ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente.class);
		req.setBilancio(getBilancio());
		return req;
	}

	/**
	 * Rimozione dei capitoli dalla lista dei remporanei
	 */
	public void rimuoviCapitoliDaTemp() {
		rimuoviCapitoliDaTemp(getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp(), getListaAccantonamentoFondiDubbiaEsigibilitaRendicontoSelezionati());
	}
	
	@Override
	protected void setDefaultValuesAttributiBilancio() {
		super.setDefaultValuesAttributiBilancio();
		// CAmpi aggiuntivi non presenti su interfaccia
		if(getAttributiBilancio().getPercentualeAccantonamentoAnno() == null) {
			getAttributiBilancio().setPercentualeAccantonamentoAnno(BigDecimal.ZERO);
		}
		if(getAttributiBilancio().getPercentualeAccantonamentoAnno1() == null) {
			getAttributiBilancio().setPercentualeAccantonamentoAnno1(BigDecimal.ZERO);
		}
		if(getAttributiBilancio().getPercentualeAccantonamentoAnno2() == null) {
			getAttributiBilancio().setPercentualeAccantonamentoAnno2(BigDecimal.ZERO);
		}
	}

	/**
	 * Crea una request per il servizio di {@link PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente}.
	 * @return la request creata
	 */
	public PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente creaRequestPopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente() {
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente req = creaRequest(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente.class);
		req.setBilancio(getBilancio());
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente}.
	 * @return la request creata
	 */
	public PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente creaRequestPopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente() {
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente req = creaRequest(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente.class);
		req.setBilancio(getBilancio());
		return req;
	}

}
