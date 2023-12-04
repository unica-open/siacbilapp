/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe base per la validazione massiva della prima nota integrata
 * @author Marchino Alessandro
 * @version 1.0.0 14/05/2015
 * @version 1.1.0 16/06/2015
 */
public abstract class RicercaValidazionePrimaNotaIntegrataBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2890099145883006482L;
	
	private PrimaNota primaNota;
	private String tipoElenco;
	private TipoEvento tipoEvento;
	private Evento evento;
	private Conto conto;
	private CausaleEP causaleEP;
	private Integer annoMovimento;
	private String numeroMovimento;
	private Integer numeroSubmovimento;
	private StatoOperativoPrimaNota statoOperativoPrimaNota;
	private RegistrazioneMovFin registrazioneMovFin;
	private Date dataRegistrazioneDa;
	private Date dataRegistrazioneA;
	
	private List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	private List<CausaleEP> listaCausaliEP = new ArrayList<CausaleEP>();
	private List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota = new ArrayList<StatoOperativoPrimaNota>();

	
	/* modale compilazione guidata conto*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private Soggetto soggetto;
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	// SIAC-4644
	private AttoAmministrativo attoAmministrativo;
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	// SIAC-5292
	private Capitolo<?, ?> capitolo;
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	
	//SIAC-5799
	private Accertamento    accertamento;
	private SubAccertamento subAccertamento;
	private MovimentoGestione movimentoGestione;
	private MovimentoGestione subMovimentoGestione;

	private Impegno impegno;
	private SubImpegno subImpegno;

	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private String stringaSACCapitolo;
	
	//private List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = new ArrayList<StrutturaAmministrativoContabile>();
	
	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}
	
	/**
	 * @return the tipoElenco
	 */
	public String getTipoElenco() {
		return tipoElenco;
	}

	/**
	 * @param tipoElenco the tipoElenco to set
	 */
	public void setTipoElenco(String tipoElenco) {
		this.tipoElenco = tipoElenco;
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
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @param annoMovimento the annoMovimento to set
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @return the numeroMovimento
	 */
	public String getNumeroMovimento() {
		return numeroMovimento;
	}

	/**
	 * @param numeroMovimento the numeroMovimento to set
	 */
	public void setNumeroMovimento(String numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}

	/**
	 * @return the numeroSubmovimento
	 */
	public Integer getNumeroSubmovimento() {
		return numeroSubmovimento;
	}

	/**
	 * @param numeroSubmovimento the numeroSubmovimento to set
	 */
	public void setNumeroSubmovimento(Integer numeroSubmovimento) {
		this.numeroSubmovimento = numeroSubmovimento;
	}

	/**
	 * @return the statoOperativoPrimaNota
	 */
	public StatoOperativoPrimaNota getStatoOperativoPrimaNota() {
		return statoOperativoPrimaNota;
	}

	/**
	 * @param statoOperativoPrimaNota the statoOperativoPrimaNota to set
	 */
	public void setStatoOperativoPrimaNota(StatoOperativoPrimaNota statoOperativoPrimaNota) {
		this.statoOperativoPrimaNota = statoOperativoPrimaNota;
	}

	/**
	 * @return the registrazioneMovFin
	 */
	public RegistrazioneMovFin getRegistrazioneMovFin() {
		return registrazioneMovFin;
	}

	/**
	 * @param registrazioneMovFin the registrazioneMovFin to set
	 */
	public void setRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
	}

	/**
	 * @return the dataRegistrazioneDa
	 */
	public Date getDataRegistrazioneDa() {
		return dataRegistrazioneDa == null ? null : new Date(dataRegistrazioneDa.getTime());
	}

	/**
	 * @param dataRegistrazioneDa the dataRegistrazioneDa to set
	 */
	public void setDataRegistrazioneDa(Date dataRegistrazioneDa) {
		this.dataRegistrazioneDa = dataRegistrazioneDa == null ? null : new Date(dataRegistrazioneDa.getTime());
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
	 * @return the listaTipoEvento
	 */
	public List<TipoEvento> getListaTipoEvento() {
		return listaTipoEvento;
	}

	/**
	 * @param listaTipoEvento the listaTipoEvento to set
	 */
	public void setListaTipoEvento(List<TipoEvento> listaTipoEvento) {
		this.listaTipoEvento = listaTipoEvento != null ? listaTipoEvento : new ArrayList<TipoEvento>();
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
		this.listaEvento = listaEvento != null ? listaEvento : new ArrayList<Evento>();
	}
	
	/**
	 * @return the listaCausaliEP
	 */
	public List<CausaleEP> getListaCausaliEP() {
		return listaCausaliEP;
	}

	/**
	 * @param listaCausaliEP the listaCausaliEP to set
	 */
	public void setListaCausaliEP(List<CausaleEP> listaCausaliEP) {
		this.listaCausaliEP = listaCausaliEP;
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
	public void setListaStatoOperativoPrimaNota(List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota) {
		this.listaStatoOperativoPrimaNota = listaStatoOperativoPrimaNota != null ? listaStatoOperativoPrimaNota : new ArrayList<StatoOperativoPrimaNota>();
	}
	
	
	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the stringaSACCapitolo
	 */
	public String getStringaSACCapitolo() {
		return stringaSACCapitolo;
	}

	/**
	 * @param stringaSACCapitolo the stringaSACCapitolo to set
	 */
	public void setStringaSACCapitolo(String stringaSACCapitolo) {
		this.stringaSACCapitolo = stringaSACCapitolo;
	}
	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
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
		this.listaTitoloEntrata = listaTitoloEntrata;
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
		this.listaTitoloSpesa = listaTitoloSpesa;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto != null ? listaTipoAtto : new ArrayList<TipoAtto>();
	}
	
	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
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
	 * @return the descrizioneCompletaAttoAmministrativo
	 */
	public String getDescrizioneCompletaAttoAmministrativo() {
		if(attoAmministrativo == null || attoAmministrativo.getAnno() == 0 || attoAmministrativo.getNumero() == 0
				|| attoAmministrativo.getTipoAtto() == null || attoAmministrativo.getTipoAtto().getUid() == 0) {
			return "";
		}
		return new StringBuilder()
				.append(attoAmministrativo.getTipoAtto().getDescrizione())
				.append(" - ")
				.append(attoAmministrativo.getAnno())
				.append(" - ")
				.append(attoAmministrativo.getNumero())
				.append(" - ")
				.append(attoAmministrativo.getOggetto())
				.toString();
	}

	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto() {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setConto(getConto());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link LeggiElementoPianoDeiContiByCodiceAndAnno}.
	 * 
	 * @return la request creata
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno() {
		LeggiElementoPianoDeiContiByCodiceAndAnno request = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setElementoPianoDeiConti(getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaPrimaNotaIntegrataValidabile}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPrimaNotaIntegrataValidabile creaRequestRicercaSinteticaPrimaNotaIntegrataValidabile() {
		RicercaSinteticaPrimaNotaIntegrataValidabile request = creaRequest(RicercaSinteticaPrimaNotaIntegrataValidabile.class);
		
		
		request.setBilancio(getBilancio());
		request.setPrimaNota(getPrimaNota());
		
		
		// Stato fisso
		getPrimaNota().setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		getPrimaNota().setAmbito(getAmbito());
		getPrimaNota().setSoggetto(getSoggetto());
		
		// Date registrazione
		request.setDataRegistrazioneA(getDataRegistrazioneA());
		request.setDataRegistrazioneDa(getDataRegistrazioneDa());
		
		// Entita
		request.setConto(impostaEntitaFacoltativa(getConto()));
		request.setEvento(impostaEntitaFacoltativa(getEvento()));
		request.setTipoEvento(impostaEntitaFacoltativa(getTipoEvento()));
		request.setCausaleEP(impostaEntitaFacoltativa(getCausaleEP()));
		
		// L'elemento del piano dei conti e' injettato nella registrazione
		if(getRegistrazioneMovFin() != null && impostaEntitaFacoltativa(getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato()) != null) {
			request.setRegistrazioneMovFin(getRegistrazioneMovFin());
		}
		
		// Movimento
		request.setAnnoMovimento(getAnnoMovimento());
		request.setNumeroMovimento(getNumeroMovimento());
		request.setNumeroSubmovimento(getNumeroSubmovimento());
		
		// SIAC-4644
		request.setAttoAmministrativo(getAttoAmministrativo());
		// SIAC-5292
		request.setCapitolo(impostaEntitaFacoltativa(getCapitolo()));
		
		// SIAC-5799
		request.setMovimentoGestione(impostaEntitaFacoltativa(getMovimentoGestione()));		
		request.setSubMovimentoGestione(impostaEntitaFacoltativa(getSubMovimentoGestione()));			
		request.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));

		
		//log.error("creaRequestRicercaSinteticaPrimaNotaIntegrataValidabile "," uid della sac passata " +  getStrutturaAmministrativoContabile().getUid());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	
	
	
	
	/**
	 * @return the movimentoGestione
	 */
	public MovimentoGestione getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(MovimentoGestione movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public MovimentoGestione getSubMovimentoGestione() {
		return subMovimentoGestione;
	}

	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(MovimentoGestione subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/**
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}

	/**
	 * @param subAccertamento the subAccertamento to set
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		// TODO: calcolare il suffix dall'ambito
		String suffix = "";
		return creaRequestRicercaCodifiche("ClassePiano" + suffix);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaMinimaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaMinimaCausale creaRequestRicercaMinimaCausale() {
		RicercaMinimaCausale request = creaRequest(RicercaMinimaCausale.class);
		
		request.setBilancio(getBilancio());
		
		CausaleEP cep = new CausaleEP();
		cep.setAmbito(getAmbito());
		cep.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		cep.setTipoCausale(TipoCausale.Integrata);
		cep.setEventi(Arrays.asList(getEvento()));
		request.setCausaleEP(cep);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK prsk = new ParametroRicercaSoggettoK();
		prsk.setCodice(getSoggetto().getCodiceSoggetto());
		
		request.setParametroSoggettoK(prsk);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento req = creaRequest(RicercaProvvedimento.class);
		req.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(getAttoAmministrativo().getAnno());
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero() != 0 ? getAttoAmministrativo().getNumero() : null);
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getAttoAmministrativo().getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getAttoAmministrativo().getStrutturaAmmContabile()));
		req.setRicercaAtti(ricercaAtti);
		
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaPuntualeCapitoloUscitaGestione}.
	 * @return la request creata
	 */
	public RicercaPuntualeCapitoloUscitaGestione creaRequestRicercaPuntualeCapitoloUscitaGestione() {
		RicercaPuntualeCapitoloUscitaGestione req = creaRequest(RicercaPuntualeCapitoloUscitaGestione.class);
		
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		req.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		req.setEnte(getEnte());
		
		RicercaPuntualeCapitoloUGest ricercaPuntualeCapitoloUGest = new RicercaPuntualeCapitoloUGest();
		ricercaPuntualeCapitoloUGest.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
		ricercaPuntualeCapitoloUGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaPuntualeCapitoloUGest.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
		ricercaPuntualeCapitoloUGest.setNumeroArticolo(getCapitolo().getNumeroArticolo());
		ricercaPuntualeCapitoloUGest.setNumeroUEB(getCapitolo().getNumeroUEB());
		ricercaPuntualeCapitoloUGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaPuntualeCapitoloUGest(ricercaPuntualeCapitoloUGest);
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaPuntualeCapitoloEntrataGestione}.
	 * @return la request creata
	 */
	public RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitoloEntrataGestione() {
		RicercaPuntualeCapitoloEntrataGestione req = creaRequest(RicercaPuntualeCapitoloEntrataGestione.class);
		
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		req.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		req.setEnte(getEnte());
		
		RicercaPuntualeCapitoloEGest ricercaPuntualeCapitoloEGest = new RicercaPuntualeCapitoloEGest();
		ricercaPuntualeCapitoloEGest.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
		ricercaPuntualeCapitoloEGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaPuntualeCapitoloEGest.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroArticolo(getCapitolo().getNumeroArticolo());
		ricercaPuntualeCapitoloEGest.setNumeroUEB(getCapitolo().getNumeroUEB());
		ricercaPuntualeCapitoloEGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaPuntualeCapitoloEGest(ricercaPuntualeCapitoloEGest);
		
		return req;
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
		request.setCaricaSub( getSubAccertamento()  != null && getSubAccertamento().getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		if(getAccertamento() != null) {
			RicercaAccertamentoK prak = new RicercaAccertamentoK();
			prak.setAnnoEsercizio(getAnnoEsercizioInt());
			prak.setAnnoAccertamento(Integer.valueOf(getAccertamento().getAnnoMovimento()));
			prak.setNumeroAccertamento(getAccertamento().getNumeroBigDecimal());
			prak.setNumeroSubDaCercare(getSubAccertamento() != null && getSubAccertamento().getNumeroBigDecimal() != null? getSubAccertamento().getNumeroBigDecimal() : null);
			request.setpRicercaAccertamentoK(prak);
		}
		
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
		request.setCaricaSub(getSubImpegno() != null && getSubImpegno().getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		if(getImpegno() != null) {
			RicercaImpegnoK prik = new RicercaImpegnoK();
			prik.setAnnoEsercizio(getAnnoEsercizioInt());
			prik.setAnnoImpegno(Integer.valueOf(getImpegno().getAnnoMovimento()));
			prik.setNumeroImpegno(getImpegno().getNumeroBigDecimal());
			prik.setNumeroSubDaCercare(getSubImpegno() != null && getSubImpegno().getNumeroBigDecimal() != null ? getSubImpegno().getNumeroBigDecimal() : null);
			request.setpRicercaImpegnoK(prik);
		}
		
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
