/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe di model base per la ricerca della prima nota integrata manuale (comune agli ambiti FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public abstract class RicercaPrimaNotaIntegrataManualeBaseModel extends GenericBilancioModel {

	/**
	 * per la serializzazione	 
	 * */
	private static final long serialVersionUID = 250072492432226842L;
	
	private PrimaNota primaNotaLibera;
	private CausaleEP causaleEP;
	private Evento evento;
	private Conto conto;
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
	
	// SIAC-5281
	private Missione missione;
	private Programma programma;
	private List<Missione> listaMissione = new ArrayList<Missione>();
	
	// SIAC-5334
	private String tipoMovimento;
	private Integer annoMovimentoGestione;
	private Integer numeroMovimentoGestione;
	private Integer numeroSubmovimentoGestione;
	
	private Impegno impegno;
	private Accertamento accertamento;
	private SubImpegno subImpegno;
	private SubAccertamento subAccertamento;
	
	private List<Evento> eventi = new ArrayList<Evento>();
	
	
	/** Costruttore vuoto di default */
	public RicercaPrimaNotaIntegrataManualeBaseModel() {
		setTitolo("Ricerca Prima Nota Integrata Manuale");
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
	 * @return the tipoMovimento
	 */
	public String getTipoMovimento() {
		return this.tipoMovimento;
	}

	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * @return the annoMovimentoGestione
	 */
	public Integer getAnnoMovimentoGestione() {
		return this.annoMovimentoGestione;
	}

	/**
	 * @param annoMovimentoGestione the annoMovimentoGestione to set
	 */
	public void setAnnoMovimentoGestione(Integer annoMovimentoGestione) {
		this.annoMovimentoGestione = annoMovimentoGestione;
	}

	/**
	 * @return the numeroMovimentoGestione
	 */
	public Integer getNumeroMovimentoGestione() {
		return this.numeroMovimentoGestione;
	}

	/**
	 * @param numeroMovimentoGestione the numeroMovimentoGestione to set
	 */
	public void setNumeroMovimentoGestione(Integer numeroMovimentoGestione) {
		this.numeroMovimentoGestione = numeroMovimentoGestione;
	}

	/**
	 * @return the numeroSubmovimentoGestione
	 */
	public Integer getNumeroSubmovimentoGestione() {
		return this.numeroSubmovimentoGestione;
	}

	/**
	 * @param numeroSubmovimentoGestione the numeroSubmovimentoGestione to set
	 */
	public void setNumeroSubmovimentoGestione(Integer numeroSubmovimentoGestione) {
		this.numeroSubmovimentoGestione = numeroSubmovimentoGestione;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return this.impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return this.accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return this.subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return this.subAccertamento;
	}

	/**
	 * @param subAccertamento the subAccertamento to set
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}

	/**
	 * @return the eventi
	 */
	public List<Evento> getEventi() {
		return this.eventi;
	}

	/**
	 * @param eventi the eventi to set
	 */
	public void setEventi(List<Evento> eventi) {
		this.eventi = eventi != null ? eventi : new ArrayList<Evento>();
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
	public final String getAmbitoSuffix() {
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
		
		causEpPerRequest.setTipoCausale(TipoCausale.Integrata);
		causEpPerRequest.setAmbito(getAmbito());
		request.setCausaleEP(causEpPerRequest);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPrimaNotaIntegrataManuale creaRequestRicercaSinteticaPrimaNotaIntegrataManuale() {
		RicercaSinteticaPrimaNotaIntegrataManuale request = creaRequest(RicercaSinteticaPrimaNotaIntegrataManuale.class);
		
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Integrata);
		getPrimaNotaLibera().setAmbito(getAmbito());
		
		request.setCausaleEP(impostaEntitaFacoltativa(getCausaleEP()));
		request.setEventi(getEventi());
		request.setConto(impostaEntitaFacoltativa(getConto()));
		
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
		
		// SIAC-5334
		request.setEntita(ObjectUtils.firstNonNull(getSubImpegno(), getImpegno(), getSubAccertamento(), getAccertamento()));
		
		return request;
	}


	/**
	 * 
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param c il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto c) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setBilancio(getBilancio());
		request.setConto(c);
		request.getConto().setAmbito(getAmbito());
		request.setParametriPaginazione(creaParametriPaginazione());
		
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
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getNumeroSubmovimentoGestione() != null);
		request.setSubPaginati(true);
		
		RicercaAccertamentoK prak = new RicercaAccertamentoK();
		prak.setAnnoEsercizio(getAnnoEsercizioInt());
		prak.setAnnoAccertamento(getAnnoMovimentoGestione());
		prak.setNumeroAccertamento(new BigDecimal(getNumeroMovimentoGestione().intValue()));
		prak.setNumeroSubDaCercare(getNumeroSubmovimentoGestione() != null ? new BigDecimal(getNumeroSubmovimentoGestione().intValue()) : null);
		request.setpRicercaAccertamentoK(prak);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getNumeroSubmovimentoGestione() != null);
		request.setSubPaginati(true);
		
		RicercaImpegnoK prik = new RicercaImpegnoK();
		prik.setAnnoEsercizio(getAnnoEsercizioInt());
		prik.setAnnoImpegno(getAnnoMovimentoGestione());
		prik.setNumeroImpegno(new BigDecimal(getNumeroMovimentoGestione().intValue()));
		prik.setNumeroSubDaCercare(getNumeroSubmovimentoGestione() != null ? new BigDecimal(getNumeroSubmovimentoGestione().intValue()) : null);
		request.setpRicercaImpegnoK(prik);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
}
