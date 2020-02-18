/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
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
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe base per la ricerca della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
public abstract class RicercaPrimaNotaIntegrataBaseModel extends GenericBilancioModel {
	
	/** Per la seriailzzazione */
	private static final long serialVersionUID = 283670536916045534L;
	
	private PrimaNota primaNota;
	private String tipoElenco;
	private TipoEvento tipoEvento;
	private Evento evento;
	private Conto conto;
	private CausaleEP causaleEP;
	private Integer annoMovimento;
	private String numeroMovimento;
	private Integer numeroSubmovimento;
	private Date dataRegistrazioneDefinitivaDa;
	private Date dataRegistrazioneDefinitivaA;
	private Date dataRegistrazioneProvvisoriaDa;
	private Date dataRegistrazioneProvvisoriaA;
	
	private List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	private List<CausaleEP> listaCausaliEP = new ArrayList<CausaleEP>();
	private List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota = new ArrayList<StatoOperativoPrimaNota>();

	/* modale compilazione guidata conto*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	/*dati da ricaricare */
	private Integer uidTipoEventoDaRicerca;
	private Integer uidEventoDaRicerca;
	private Integer uidCausaleEPDaRicerca;
	
	// SIAC-4644
	private BigDecimal importoDocumentoDa;
	private BigDecimal importoDocumentoA;
	private Soggetto soggetto;
	private AttoAmministrativo attoAmministrativo;
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	// SIAC-5291
	private Capitolo<?, ?> capitolo;
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	
	//SIAC-6002
	private Accertamento    accertamento;
	private SubAccertamento subAccertamento;
	private MovimentoGestione movimentoGestione;
	private MovimentoGestione subMovimentoGestione;

	private Impegno impegno;
	private SubImpegno subImpegno;
	
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private String stringaSACCapitolo;
	
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
	 * @return the dataRegistrazioneDefinitivaDa
	 */
	public Date getDataRegistrazioneDefinitivaDa() {
		return dataRegistrazioneDefinitivaDa != null ? new Date(dataRegistrazioneDefinitivaDa.getTime()) : null;
	}

	/**
	 * @param dataRegistrazioneDefinitivaDa the dataRegistrazioneDefinitivaDa to set
	 */
	public void setDataRegistrazioneDefinitivaDa(Date dataRegistrazioneDefinitivaDa) {
		this.dataRegistrazioneDefinitivaDa = dataRegistrazioneDefinitivaDa != null ? new Date(dataRegistrazioneDefinitivaDa.getTime()) : null;
	}

	/**
	 * @return the dataRegistrazioneDefinitivaA
	 */
	public Date getDataRegistrazioneDefinitivaA() {
		return dataRegistrazioneDefinitivaA != null ? new Date(dataRegistrazioneDefinitivaA.getTime()) : null;
	}

	/**
	 * @param dataRegistrazioneDefinitivaA the dataRegistrazioneDefinitivaA to set
	 */
	public void setDataRegistrazioneDefinitivaA(Date dataRegistrazioneDefinitivaA) {
		this.dataRegistrazioneDefinitivaA = dataRegistrazioneDefinitivaA != null ? new Date(dataRegistrazioneDefinitivaA.getTime()) : null;
	}
	
	/**
	 * @return the dataRegistrazioneProvvisoriaDa
	 */
	public Date getDataRegistrazioneProvvisoriaDa() {
		return dataRegistrazioneProvvisoriaDa != null ? new Date(dataRegistrazioneProvvisoriaDa.getTime()) : null;
	}

	/**
	 * @param dataRegistrazioneProvvisoriaDa the dataRegistrazioneProvvisoriaDa to set
	 */
	public void setDataRegistrazioneProvvisoriaDa(Date dataRegistrazioneProvvisoriaDa) {
		this.dataRegistrazioneProvvisoriaDa = dataRegistrazioneProvvisoriaDa != null ? new Date(dataRegistrazioneProvvisoriaDa.getTime()) : null;
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
		this.listaCausaliEP = listaCausaliEP != null ? listaCausaliEP : new ArrayList<CausaleEP>();
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
	 * @return the listaClassi
	 */
	public final List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public final void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi != null ? listaClassi : new ArrayList<ClassePiano>();
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
	 * @return the uidTipoEventoDaRicerca
	 */
	public Integer getUidTipoEventoDaRicerca() {
		return uidTipoEventoDaRicerca;
	}

	/**
	 * @param uidTipoEventoDaRicerca the uidTipoEventoDaRicerca to set
	 */
	public void setUidTipoEventoDaRicerca(Integer uidTipoEventoDaRicerca) {
		this.uidTipoEventoDaRicerca = uidTipoEventoDaRicerca;
	}

	/**
	 * @return the uidEventoDaRicerca
	 */
	public Integer getUidEventoDaRicerca() {
		return uidEventoDaRicerca;
	}

	/**
	 * @param uidEventoDaRicerca the uidEventoDaRicerca to set
	 */
	public void setUidEventoDaRicerca(Integer uidEventoDaRicerca) {
		this.uidEventoDaRicerca = uidEventoDaRicerca;
	}

	/**
	 * @return the uidCausaleEPDaRicerca
	 */
	public Integer getUidCausaleEPDaRicerca() {
		return uidCausaleEPDaRicerca;
	}

	/**
	 * @param uidCausaleEPDaRicerca the uidCausaleEPDaRicerca to set
	 */
	public void setUidCausaleEPDaRicerca(Integer uidCausaleEPDaRicerca) {
		this.uidCausaleEPDaRicerca = uidCausaleEPDaRicerca;
	}

	/**
	 * @return the importoDocumentoDa
	 */
	public BigDecimal getImportoDocumentoDa() {
		return importoDocumentoDa;
	}

	/**
	 * @param importoDocumentoDa the importoDocumentoDa to set
	 */
	public void setImportoDocumentoDa(BigDecimal importoDocumentoDa) {
		this.importoDocumentoDa = importoDocumentoDa;
	}

	/**
	 * @return the importoDocumentoA
	 */
	public BigDecimal getImportoDocumentoA() {
		return importoDocumentoA;
	}

	/**
	 * @param importoDocumentoA the importoDocumentoA to set
	 */
	public void setImportoDocumentoA(BigDecimal importoDocumentoA) {
		this.importoDocumentoA = importoDocumentoA;
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
	 * @return the descrizioneCompletaSoggetto
	 */
	public String getDescrizioneCompletaSoggetto() {
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto()) || StringUtils.isBlank(soggetto.getDenominazione()) || StringUtils.isBlank(soggetto.getCodiceFiscale())) {
			return "";
		}
		return new StringBuilder()
				.append(soggetto.getCodiceSoggetto())
				.append(" - ")
				.append(soggetto.getDenominazione())
				.append(" - ")
				.append(soggetto.getCodiceFiscale())
				.toString();
	}
	
	/**
	 * @return the descrizioneCompletaAttoAmministrativo
	 */
	public String getDescrizioneCompletaAttoAmministrativo() {
		if(attoAmministrativo == null || attoAmministrativo.getAnno() == 0 || attoAmministrativo.getNumero() == 0
				|| attoAmministrativo.getTipoAtto() == null || attoAmministrativo.getTipoAtto().getUid() == 0) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(chunks, attoAmministrativo.getTipoAtto().getDescrizione());
		CollectionUtil.addIfNotNullNorEmpty(chunks, Integer.toString(attoAmministrativo.getAnno()));
		CollectionUtil.addIfNotNullNorEmpty(chunks, Integer.toString(attoAmministrativo.getNumero()));
		CollectionUtil.addIfNotNullNorEmpty(chunks, attoAmministrativo.getOggetto());
		return StringUtils.join(chunks, " - ");
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto() {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		
		getConto().setAmbito(getAmbito());
		
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setConto(getConto());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link LeggiElementoPianoDeiContiByCodiceAndAnno}.
	 * @param elementoPianoDeiConti l'elemento per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(ElementoPianoDeiConti elementoPianoDeiConti) {
		LeggiElementoPianoDeiContiByCodiceAndAnno request = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaPrimaNotaIntegrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPrimaNotaIntegrata creaRequestRicercaSinteticaPrimaNotaIntegrata() {
		RicercaSinteticaPrimaNotaIntegrata request = creaRequest(RicercaSinteticaPrimaNotaIntegrata.class);
		
		request.setBilancio(getBilancio());
		request.setPrimaNota(getPrimaNota());
		
		// Injetto l'ambito
		getPrimaNota().setAmbito(getAmbito());
		
		// Date registrazione
		request.setDataRegistrazioneA(getDataRegistrazioneDefinitivaA());
		request.setDataRegistrazioneDa(getDataRegistrazioneDefinitivaDa());
		request.setDataRegistrazioneProvvisoriaA(getDataRegistrazioneProvvisoriaA());
		request.setDataRegistrazioneProvvisoriaDa(getDataRegistrazioneProvvisoriaDa());
		
		// Entita
		request.setConto(impostaEntitaFacoltativa(getConto()));
		request.setEvento(impostaEntitaFacoltativa(getEvento()));
		request.setTipoEvento(impostaEntitaFacoltativa(getTipoEvento()));
		request.setCausaleEP(impostaEntitaFacoltativa(getCausaleEP()));
		request.setTipoElenco(getTipoElenco());
		
		injettaRegistrazioneMovFinSePresente(request);
		
		// Movimento
		request.setAnnoMovimento(getAnnoMovimento());
		request.setNumeroMovimento(getNumeroMovimento());
		request.setNumeroSubmovimento(getNumeroSubmovimento());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		// SIAC-4644
		request.setImportoDocumentoA(getImportoDocumentoA());
		request.setImportoDocumentoDa(getImportoDocumentoDa());
		request.setSoggetto(getSoggetto());
		request.setAttoAmministrativo(getAttoAmministrativo());
		
		// SIAC-5291
		request.setCapitolo(getCapitolo());
		
		//SIAC-6002
		request.setMovimentoGestione(impostaEntitaFacoltativa(getMovimentoGestione()));		
		request.setSubMovimentoGestione(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		request.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		return request;
	}

	/**
	 * Injetta la Registrazione MovFin nella request se presente e correttamente compilata.
	 * 
	 * @param request la request da popolare
	 */
	protected void injettaRegistrazioneMovFinSePresente(RicercaSinteticaPrimaNotaIntegrata request) {
		// Implementazione vuota di default
	}

	/**
	 * Crea la request per il servizio {@link RicercaCodifiche} per tipoCodifica uguale a ClassePiano.
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		String ambitoSuffix = "_" + getAmbito().name().split("_")[1];
		return creaRequestRicercaCodifiche("ClassePiano" + ambitoSuffix);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaMinimaCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaMinimaCausale creaRequestRicercaMinimaCausaleIntegrata() {
		RicercaMinimaCausale request = creaRequest(RicercaMinimaCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		
		causEpPerRequest.setAmbito(getAmbito());
		causEpPerRequest.setTipoCausale(TipoCausale.Integrata);
		causEpPerRequest.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		request.setCausaleEP(causEpPerRequest);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave req = creaRequest(RicercaSoggettoPerChiave.class);
		
		req.setEnte(getEnte());
		req.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setIncludeModif(false);
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		req.setParametroSoggettoK(parametroSoggettoK);
		
		return req;
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
	 * Salva nel model gli uid da impostare in caso di nessun dato trovato, per reimpostare la jsp con l'ultima ricerca effettuata.
	 * 
	 */
	public void impostoDatiNelModel(){
	
		setUidTipoEventoDaRicerca(getTipoEvento() != null ? getTipoEvento().getUid() : null);
		setUidEventoDaRicerca(getEvento() != null ? getEvento().getUid() : null);
		setUidCausaleEPDaRicerca(getCausaleEP() != null ? getCausaleEP().getUid() : null);
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
		request.setCaricaSub( getSubAccertamento()  != null && getSubAccertamento().getNumero() != null);
		request.setSubPaginati(true);
		
		if(getAccertamento() != null) {
			RicercaAccertamentoK prak = new RicercaAccertamentoK();
			prak.setAnnoEsercizio(getAnnoEsercizioInt());
			prak.setAnnoAccertamento(Integer.valueOf(getAccertamento().getAnnoMovimento()));
			prak.setNumeroAccertamento(getAccertamento().getNumero());
			prak.setNumeroSubDaCercare(getSubAccertamento() != null && getSubAccertamento().getNumero() != null? getSubAccertamento().getNumero() : null);
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
		request.setCaricaSub(getSubImpegno() != null && getSubImpegno().getNumero() != null);
		request.setSubPaginati(true);
		
		if(getImpegno() != null) {
			RicercaImpegnoK prik = new RicercaImpegnoK();
			prik.setAnnoEsercizio(getAnnoEsercizioInt());
			prik.setAnnoImpegno(Integer.valueOf(getImpegno().getAnnoMovimento()));
			prik.setNumeroImpegno(getImpegno().getNumero());
			prik.setNumeroSubDaCercare(getSubImpegno() != null && getSubImpegno().getNumero() != null ? getSubImpegno().getNumero() : null);
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
