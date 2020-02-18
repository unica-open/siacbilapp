/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoProgetto;

/**
 * Classe di model per l'inserimento del Cronoprogramma.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 13/02/2014
 * @version 1.2.0 - 21/10/2015 - CR 2450 - modifica capitolo
 *
 */
public class InserisciCronoprogrammaModel extends GenericCronoprogrammaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3046630058589815304L;
	
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	private List<TipoProgetto> listaTipiProgetto = new ArrayList<TipoProgetto>();
	private TipoProgetto tipoProgettoRicerca;
	private Integer annoRicerca;
	private List<Cronoprogramma> listaCronoprogrammiDaCopiare = new ArrayList<Cronoprogramma>();
	

	/** Costruttore vuoto di default */
	public InserisciCronoprogrammaModel() {
		super();
		setTitolo("Inserisce Cronoprogramma");
	}
	
	/**
	 * @return the listaTipiFinanziamento
	 */
	public List<TipoFinanziamento> getListaTipiFinanziamento() {
		return listaTipiFinanziamento;
	}

	/**
	 * @param listaTipiFinanziamento the listaTipiFinanziamento to set
	 */
	public void setListaTipiFinanziamento(List<TipoFinanziamento> listaTipiFinanziamento) {
		this.listaTipiFinanziamento = listaTipiFinanziamento != null ? listaTipiFinanziamento : new ArrayList<TipoFinanziamento>();
	}

	/**
	 * @return the listaTipiProgetto
	 */
	public List<TipoProgetto> getListaTipiProgetto() {
		return listaTipiProgetto;
	}

	/**
	 * @param listaTipiProgetto the listaTipiProgetto to set
	 */
	public void setListaTipiProgetto(List<TipoProgetto> listaTipiProgetto) {
		this.listaTipiProgetto = listaTipiProgetto != null ? listaTipiProgetto : new ArrayList<TipoProgetto>();
	}
	
	/**
	 * @return the tipoProgettoRicerca
	 */
	public TipoProgetto getTipoProgettoRicerca() {
		return tipoProgettoRicerca;
	}

	/**
	 * @param tipoProgettoRicerca the tipoProgettoRicerca to set
	 */
	public void setTipoProgettoRicerca(TipoProgetto tipoProgettoRicerca) {
		this.tipoProgettoRicerca = tipoProgettoRicerca;
	}

	/**
	 * @return the annoRicerca
	 */
	public Integer getAnnoRicerca() {
		return annoRicerca;
	}

	/**
	 * @param annoRicerca the annoRicerca to set
	 */
	public void setAnnoRicerca(Integer annoRicerca) {
		this.annoRicerca = annoRicerca;
	}

	/**
	 * @return the listaCronoprogrammiDaCopiare
	 */
	public List<Cronoprogramma> getListaCronoprogrammiDaCopiare() {
		return listaCronoprogrammiDaCopiare;
	}

	/**
	 * @param listaCronoprogrammiDaCopiare the listaCronoprogrammiDaCopiare to set
	 */
	public void setListaCronoprogrammiDaCopiare(List<Cronoprogramma> listaCronoprogrammiDaCopiare) {
		this.listaCronoprogrammiDaCopiare = listaCronoprogrammiDaCopiare != null? listaCronoprogrammiDaCopiare : new ArrayList<Cronoprogramma>();
	}

	/**
	 * @return the baseUrlCronoprogramma
	 */
	public String getBaseUrlCronoprogramma() {
		return "inserimentoNuovoCronoprogramma";
	}
	
	/* ************ Requests ************ */

	/**
	 * Crea una request per il servizio di {@link InserisceCronoprogramma}.
	 * 
	 * @return la request creata
	 */
	public InserisceCronoprogramma creaRequestInserisceCronoprogramma() {
		InserisceCronoprogramma request = creaRequest(InserisceCronoprogramma.class);
		
		request.setCronoprogramma(costruisciCronoprogramma());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDeiCronoprogrammiCollegatiAlProgetto}.
	 * 
	 * @return la request creata
	 */
	public RicercaDeiCronoprogrammiCollegatiAlProgetto creaRequestRicercaDeiCronoprogrammiCollegatiAlProgetto() {
		RicercaDeiCronoprogrammiCollegatiAlProgetto request = creaRequest(RicercaDeiCronoprogrammiCollegatiAlProgetto.class);
		Progetto p = new Progetto();
		p.setCodice(getProgetto().getCodice());
		p.setTipoProgetto(tipoProgettoRicerca);
		request.setProgetto(p);
		request.setAnnoBilancioCronoprogrammi(annoRicerca);
		
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
	
	/**
	 * Crea una request per il servizio di Inserisce Anagrafica Progetto.
	 * 
	 * @return la request creata
	 */
	public InserisceAnagraficaProgetto creaRequestInserisceAnagraficaProgetto() {
		return creaRequestInserisceAnagraficaProgettoBase(getAttoAmministrativo());
	}

	/**
	 * @param attoAmministrativo 
	 * @return
	 */
	private InserisceAnagraficaProgetto creaRequestInserisceAnagraficaProgettoBase(AttoAmministrativo attoAmministrativo) {
		InserisceAnagraficaProgetto request = creaRequest(InserisceAnagraficaProgetto.class);
		
		request.setBilancio(getBilancio());
		
		getProgetto().setEnte(getEnte());
		
		getProgetto().setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		getProgetto().setAttoAmministrativo(attoAmministrativo);
		request.setProgetto(getProgetto());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Inserisce Anagrafica Progetto.
	 * 
	 * @return la request creata
	 */
	public InserisceAnagraficaProgetto creaRequestInserisceAnagraficaProgettoDaCronoProgramma() {
		return creaRequestInserisceAnagraficaProgettoBase(getProgetto().getAttoAmministrativo());
	}
	
	/* **** Metodi di utilita' **** */

	/**
	 * Metodo di utilit&agrave; per la costruzione di un Cronoprogramma da injettare nella request.
	 * 
	 * @return il Cronoprogramma creato
	 */
	private Cronoprogramma costruisciCronoprogramma() {
		getCronoprogramma().setProgetto(getProgetto());
		getCronoprogramma().setBilancio(getBilancio());
		getCronoprogramma().setEnte(getEnte());
		
		//SIAC-6255
		getCronoprogramma().setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		
		impostaDefaultQuadroEconomico();
		
		getCronoprogramma().setCapitoliEntrata(getListaDettaglioEntrataCronoprogramma());
		getCronoprogramma().setCapitoliUscita(getListaDettaglioUscitaCronoprogramma());
		getCronoprogramma().setUsatoPerFpv(Boolean.FALSE);
		
		getCronoprogramma().setDataFineLavori(calcolaDataFineLavori(getCronoprogramma().getDurataInGiorni(), getCronoprogramma().getDataInizioLavori()));
		
		return getCronoprogramma();
	}
	
	/**
	 * Metodo di utilit&agrave; per la costruzione di un Cronoprogramma da injettare nella request.
	 * 
	 * @return il Cronoprogramma creato
	 */
	private Cronoprogramma costruisciCronoprogrammaPerRicercaDettaglio() {
		Cronoprogramma crono = new Cronoprogramma();
		crono.setUid(getUidCronoprogrammaDaCopiare());
		crono.setBilancio(getBilancio());
		crono.setEnte(getEnte());
		return crono;
	}
	
	/**
	 * Popola i cronoprogrammi collegati al progetto per abilitare la copia dagli stessi.
	 * 
	 * @param responseRicercaDeiCronoprogrammiCollegatiAlProgetto la response da cui effettuare il popolamento
	 */
	public void popolaCronoprogrammiCollegati(RicercaDeiCronoprogrammiCollegatiAlProgettoResponse responseRicercaDeiCronoprogrammiCollegatiAlProgetto) {
		setListaCronoprogramma(responseRicercaDeiCronoprogrammiCollegatiAlProgetto.getCronoprogrami());
	}
	
	/**
	 * Popola i dettagli a partire dai valori di un cronoprogramma fornito.
	 * 
	 * @param crono il cronoprogramma da cui ottenere i dettagli
	 */
	public void popolaDettagliCronoprogrammaCopiandoliDaAltroCronoprogramma(Cronoprogramma crono) {
		setListaDettaglioEntrataCronoprogramma(crono.getCapitoliEntrata());
		setListaDettaglioUscitaCronoprogramma(crono.getCapitoliUscita());
	}
	
	
	/**
	 * Popola la lista dei cronoprogrammi collegati filtrata in base all'anno del getBilancio() in corso.
	 * 
	 * @param listaCronoprogrammaFiltrata la lista da impostare
	 */
	public void popolaCronoprogrammiCollegatiFiltrataPerAnnoBilancio(List<Cronoprogramma> listaCronoprogrammaFiltrata) {
		setListaCronoprogramma(listaCronoprogrammaFiltrata);
	}

	/**
	 * Popola dati da copiare da cronoprogramma.
	 *
	 * @param cronoprogrammaDaCopiare the cronoprogramma da copiare
	 */
	public void popolaDatiDaCopiareDaCronoprogramma(Cronoprogramma cronoprogrammaDaCopiare) {
		popolaDettagliCronoprogrammaCopiandoliDaAltroCronoprogramma(cronoprogrammaDaCopiare);
		//li copio anche qui in mdo tale che al refersh della pagina rimangano???
		getCronoprogramma().setDataAggiudicazioneLavori(cronoprogrammaDaCopiare.getDataAggiudicazioneLavori());
		getCronoprogramma().setDataApprovazioneFattibilita(cronoprogrammaDaCopiare.getDataApprovazioneFattibilita());
		getCronoprogramma().setDataApprovazioneProgettoDefinitivo(cronoprogrammaDaCopiare.getDataApprovazioneProgettoDefinitivo());
		getCronoprogramma().setDataApprovazioneProgettoEsecutivo(cronoprogrammaDaCopiare.getDataApprovazioneProgettoEsecutivo());
		getCronoprogramma().setDataAvvioProcedura(cronoprogrammaDaCopiare.getDataAvvioProcedura());
		getCronoprogramma().setDataCollaudo(cronoprogrammaDaCopiare.getDataCollaudo());
		getCronoprogramma().setDataFineLavori(cronoprogrammaDaCopiare.getDataFineLavori());
		getCronoprogramma().setDataInizioLavori(cronoprogrammaDaCopiare.getDataInizioLavori());
		getCronoprogramma().setDurataInGiorni(cronoprogrammaDaCopiare.getDurataInGiorni());
	}

}
