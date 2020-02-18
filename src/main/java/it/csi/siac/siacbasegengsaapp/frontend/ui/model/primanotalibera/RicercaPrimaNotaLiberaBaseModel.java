/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model base per la ricerca della prima nota libera (comune agli ambiti FIN e GSA)
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 7/10/2015
 *
 */
public abstract class RicercaPrimaNotaLiberaBaseModel extends GenericBilancioModel {

	/**
	 * per la serializzazione	 
	 * */
	private static final long serialVersionUID = 250072492432226842L;
	
	private PrimaNota primaNotaLibera;
	private CausaleEP causaleEP;
	private Evento evento;
	private Conto conto;
	private Cespite cespite;
	private Date dataRegistrazioneDA;
	private Date dataRegistrazioneA;
	private Date dataRegistrazioneProvvisoriaDA;
	private Date dataRegistrazioneProvvisoriaA;
	private BigDecimal importo;
	
	private List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota = new ArrayList<StatoOperativoPrimaNota>();
	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	/*  modale compilazione guidata conto */
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<TipoBeneCespite> listaTipoBeneCespite = new ArrayList<TipoBeneCespite>();
	
	// SIAC-5281
	private Missione missione;
	private Programma programma;
	private List<Missione> listaMissione = new ArrayList<Missione>();
	// SIAC-6617
	private TipoEvento tipoEvento;
	
	/** Costruttore vuoto di default */
	public RicercaPrimaNotaLiberaBaseModel() {
		setTitolo("Ricerca Prima Nota Libera");
	}
	
	/**
	 * @return the primaNotaLibera
	 */
	public PrimaNota getPrimaNotaLibera() {
		return primaNotaLibera;
	}
	/**
	 * @param primaNotaLibera the primaNotaLibera to set
	 */
	public void setPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
	}
	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}
	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}
	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}
	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	/**
	 * @return the conto
	 */
	public Conto getConto() {
		return conto;
	}
	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}
	/**
	 * @return the dataRegistrazioneDA
	 */
	public Date getDataRegistrazioneDA() {
		return dataRegistrazioneDA == null ? null : new Date(dataRegistrazioneDA.getTime());
	}
	/**
	 * @param dataRegistrazioneDA the dataRegistrazioneDA to set
	 */
	public void setDataRegistrazioneDA(Date dataRegistrazioneDA) {
		this.dataRegistrazioneDA = dataRegistrazioneDA == null ? null : new Date(dataRegistrazioneDA.getTime());
	}
	/**
	 * @return the dataRegistrazioneA
	 */
	public Date getDataRegistrazioneA() {
		return dataRegistrazioneA == null ? null : new Date(dataRegistrazioneA.getTime());
	}
	/**
	 * @param dataRegistrazioneA the dataRegistrazioneA to set
	 */
	public void setDataRegistrazioneA(Date dataRegistrazioneA) {
		this.dataRegistrazioneA = dataRegistrazioneA == null ? null : new Date(dataRegistrazioneA.getTime());
	}
	
	/**
	 * @return the dataRegistrazioneProvvisoriaDA
	 */
	public Date getDataRegistrazioneProvvisoriaDA() {
		return dataRegistrazioneProvvisoriaDA != null ? new Date(dataRegistrazioneProvvisoriaDA.getTime()) : null;
	}

	/**
	 * @param dataRegistrazioneProvvisoriaDA the dataRegistrazioneProvvisoriaDA to set
	 */
	public void setDataRegistrazioneProvvisoriaDA(Date dataRegistrazioneProvvisoriaDA) {
		this.dataRegistrazioneProvvisoriaDA = dataRegistrazioneProvvisoriaDA != null ? new Date(dataRegistrazioneProvvisoriaDA.getTime()) : null;
	}

	/**
	 * @return the dataRegistrazioneProvvisoriaA
	 */
	public Date getDataRegistrazioneProvvisoriaA() {
		return dataRegistrazioneProvvisoriaA != null ? new Date(dataRegistrazioneProvvisoriaA.getTime()) : null;
	}

	/**
	 * @param dataRegistrazioneProvvisoriaA the dataRegistrazioneProvvisoriaA to set
	 */
	public void setDataRegistrazioneProvvisoriaA(Date dataRegistrazioneProvvisoriaA) {
		this.dataRegistrazioneProvvisoriaA = dataRegistrazioneProvvisoriaA != null ? new Date(dataRegistrazioneProvvisoriaA.getTime()) : null;
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}
	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	/**
	 * @return the listaStatoOperativoPrimaNota
	 */
	public List<StatoOperativoPrimaNota> getListaStatoOperativoPrimaNota() {
		return listaStatoOperativoPrimaNota;
	}
	/**
	 * @param listaStatoOperativoPrimaNota the listaStatoOperativoPrimaNota to set
	 */
	public void setListaStatoOperativoPrimaNota(
			List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota) {
		this.listaStatoOperativoPrimaNota = listaStatoOperativoPrimaNota;
	}
	
	/**
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return listaCausaleEP;
	}
	/**
	 * @param listaCausaleEP the listaCausaleEP to set
	 */
	public void setListaCausaleEP(List<CausaleEP> listaCausaleEP) {
		this.listaCausaleEP = listaCausaleEP;
	}
	/**
	 * @return the listaEvento
	 */
	public List<Evento> getListaEvento() {
		return listaEvento;
	}
	/**
	 * @param listaEvento the listaEvento to set
	 */
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento;
	}

	
	/**
	 * @return the listaClassi
	 */
	public final List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public final void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public final List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public final void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public final List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public final void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}
	
	
	/**
	 * @return the missione
	 */
	public Missione getMissione() {
		return missione;
	}

	/**
	 * @param missione the missione to set
	 */
	public void setMissione(Missione missione) {
		this.missione = missione;
	}

	/**
	 * @return the programma
	 */
	public Programma getProgramma() {
		return programma;
	}

	/**
	 * @param programma the programma to set
	 */
	public void setProgramma(Programma programma) {
		this.programma = programma;
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
		this.listaMissione = listaMissione != null ? listaMissione : new ArrayList<Missione>();
	}

	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * Ottiene il suffisso dell'ambito corrispondente: pu&oacute; essere "FIN" o "GSA"
	 * 
	 * @return la stringa con il suffisso dell'ambito
	 */
	public String getAmbitoSuffix() {
		return getAmbito().getSuffix();
	}
	
	
	/**
	 * Ottiene l'ambito FIN da confrontare con il getAmbito che pu&ograve essere 
	 * AMBITO_FIN o AMBITO_GSA, serve per la modale
	 * @return l'ambito
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}
	
	/////////////////////////////
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public Ambito getAmbitoListe(){
		return getAmbito();
	}
	
	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	
	/**
	 * Ottiene il suffisso dell'ambito corrispondente: pu&oacute; essere "FIN" o "GSA" o "INV"
	 * 
	 * @return la stringa con il suffisso dell'ambito
	 */
	public final String getAmbitoListeSuffix() {
		return getAmbitoListe() != null ? getAmbitoListe().getSuffix() : "";
	}
	/////////////////////////////////////////////////////
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareCausale creaRequestRicercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale request = creaRequest(RicercaSinteticaModulareCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		request.setCausaleEPModelDetails();
		if (getEvento() != null) { 
			request.setTipoEvento(getEvento().getTipoEvento());
		}
		
		causEpPerRequest.setTipoCausale(TipoCausale.Libera);
		
		//log.info("creaRequestRicercaSinteticaModulareCausale", "getAmbitoContoCausale() " + getAmbitoListe());
		causEpPerRequest.setAmbito(getAmbito());
		request.setCausaleEP(causEpPerRequest);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPrimaNota creaRequestRicercaSinteticaPrimaNota() {
		RicercaSinteticaPrimaNota request = creaRequest(RicercaSinteticaPrimaNota.class);
		
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Libera);
		getPrimaNotaLibera().setAmbito(getAmbito());
		
		request.setCausaleEP(impostaEntitaFacoltativa(getCausaleEP()));
		request.setEventi(Arrays.asList(impostaEntitaFacoltativa(getEvento())));
		request.setConto(impostaEntitaFacoltativa(getConto()));
		
		request.setCespite(impostaEntitaFacoltativa(getCespite()));
		
		request.setDataRegistrazioneDa(getDataRegistrazioneDA());
		request.setDataRegistrazioneA(getDataRegistrazioneA());
		request.setDataRegistrazioneProvvisoriaDa(getDataRegistrazioneProvvisoriaDA());
		request.setDataRegistrazioneProvvisoriaA(getDataRegistrazioneProvvisoriaA());
		
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setPrimaNota(getPrimaNotaLibera());
		
		request.setImporto(getImporto());
		// SIAC-5281
		request.setMissione(impostaEntitaFacoltativa(getMissione()));
		request.setProgramma(impostaEntitaFacoltativa(getProgramma()));
		// SIAC-6617		
		request.setTipoEvento(impostaEntitaFacoltativa(getTipoEvento()));

		return request;
	}

	
	/**
	 * 
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param conto il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setBilancio(getBilancio());
		request.setConto(conto);
		
		//log.info("creaRequestRicercaSinteticaConto", "getAmbitoContoCausale()  " + getAmbitoListe() );
		request.getConto().setAmbito(getAmbitoListe() );
		//log.info("creaRequestRicercaSinteticaConto", "getAmbitoContoCausale()  " + request.getConto().getAmbito() );

		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	/**
	 * 
	 * Crea una request per il servizio di {@link RicercaSinteticaTipoBeneCespite}.
	 * @return la request creata
	 */
	public RicercaSinteticaTipoBeneCespite creaRequestRicercaSinteticaTipoBeneCespite() {
		RicercaSinteticaTipoBeneCespite request = creaRequest(RicercaSinteticaTipoBeneCespite.class);
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));		
		return request;
	}
	
	
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		return creaRequestRicercaCodifiche("ClassePiano_" + getAmbitoSuffix());
	}
	
	/**
	 * @return the cespite
	 */
	public Cespite getCespite() {
		return cespite;
	}

	/**
	 * @param cespite the cespite to set
	 */
	public void setCespite(Cespite cespite) {
		this.cespite = cespite;
	}

	/**
	 * 
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param cespite il cespite per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite(Cespite cespite) {
		RicercaSinteticaCespite request = creaRequest(RicercaSinteticaCespite.class);
		//request.setBilancio(getBilancio());
		request.setCespite(cespite);		
//		log.info("creaRequestRicercaSinteticaConto", "getAmbitoContoCausale()  " + getAmbitoListe() );
//		request.getCespite().setAmbito(getAmbitoListe() );
//		log.info("creaRequestRicercaSinteticaConto", "getAmbitoContoCausale()  " + request.getCespite().getAmbito() );
		request.setParametriPaginazione(creaParametriPaginazione());		
		return request;
	}

	/**
	 * @return the listaTipoBeneCespite
	 */
	public List<TipoBeneCespite> getListaTipoBeneCespite() {
		return listaTipoBeneCespite;
	}

	/**
	 * @param listaTipoBeneCespite the listaTipoBeneCespite to set
	 */
	public void setListaTipoBeneCespite(List<TipoBeneCespite> listaTipoBeneCespite) {
		this.listaTipoBeneCespite = listaTipoBeneCespite;
	}
	
	
}
