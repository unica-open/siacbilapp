/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.registroa;

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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * The Class RicercaRegistroAModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/10/2018
 */
public class RicercaRegistroACespiteModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5965995727766343226L;
	
	// Dati libera
	private PrimaNota primaNota;
	private CausaleEP causaleEP;
	private Evento evento;
	private Conto conto;
	private Date dataRegistrazioneDefinitivaDa;
	private Date dataRegistrazioneDefinitivaA;
	private Date dataRegistrazioneProvvisoriaDa;
	private Date dataRegistrazioneProvvisoriaA;
	
	
	// Dati integrata
	private String tipoElenco;
	private TipoEvento tipoEvento;
	private Integer annoMovimento;
	private String numeroMovimento;
	private Integer numeroSubmovimento;
	private RegistrazioneMovFin registrazioneMovFin;
	private BigDecimal importoDocumentoDa;
	private BigDecimal importoDocumentoA;
	private Soggetto soggetto;
	private AttoAmministrativo attoAmministrativo;
	private Capitolo<?, ?> capitolo;
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	private MovimentoGestione movimentoGestione;
	private MovimentoGestione subMovimentoGestione;
	private Impegno impegno;
	private SubImpegno subImpegno;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private String stringaSACCapitolo;
	
	// dati da ricaricare
//	private Integer uidTipoEventoDaRicerca;
//	private Integer uidEventoDaRicerca;
//	private Integer uidCausaleEPDaRicerca;	
	// Liste
	private List<TipoCausale> listaTipoCausale = new ArrayList<TipoCausale>();
	private List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota = new ArrayList<StatoOperativoPrimaNota>();
	//SIAC-6564
	private List<StatoAccettazionePrimaNotaDefinitiva> listaStatoAccettazionePrimaNotaDefinitiva = new ArrayList<StatoAccettazionePrimaNotaDefinitiva>();
	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();
	private List<TipoEvento> listaTipoEvento = new ArrayList<TipoEvento>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<Missione> listaMissione = new ArrayList<Missione>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	
	/**
	 * Instantiates a new ricerca tipo bene model.
	 */
	public RicercaRegistroACespiteModel() {
		setTitolo("Ricerca registro inventario");
	}

	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return this.primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}

	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return this.causaleEP;
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
		return this.evento;
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
		return this.conto;
	}

	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}

	/**
	 * @return the dataRegistrazioneDefinitivaDa
	 */
	public Date getDataRegistrazioneDefinitivaDa() {
		return this.dataRegistrazioneDefinitivaDa;
	}

	/**
	 * @param dataRegistrazioneDefinitivaDa the dataRegistrazioneDefinitivaDa to set
	 */
	public void setDataRegistrazioneDefinitivaDa(Date dataRegistrazioneDefinitivaDa) {
		this.dataRegistrazioneDefinitivaDa = dataRegistrazioneDefinitivaDa;
	}

	/**
	 * @return the dataRegistrazioneDefinitivaA
	 */
	public Date getDataRegistrazioneDefinitivaA() {
		return this.dataRegistrazioneDefinitivaA;
	}

	/**
	 * @param dataRegistrazioneDefinitivaA the dataRegistrazioneDefinitivaA to set
	 */
	public void setDataRegistrazioneDefinitivaA(Date dataRegistrazioneDefinitivaA) {
		this.dataRegistrazioneDefinitivaA = dataRegistrazioneDefinitivaA;
	}

	/**
	 * @return the dataRegistrazioneProvvisoriaDa
	 */
	public Date getDataRegistrazioneProvvisoriaDa() {
		return this.dataRegistrazioneProvvisoriaDa;
	}

	/**
	 * @param dataRegistrazioneProvvisoriaDa the dataRegistrazioneProvvisoriaDa to set
	 */
	public void setDataRegistrazioneProvvisoriaDa(Date dataRegistrazioneProvvisoriaDa) {
		this.dataRegistrazioneProvvisoriaDa = dataRegistrazioneProvvisoriaDa;
	}

	/**
	 * @return the dataRegistrazioneProvvisoriaA
	 */
	public Date getDataRegistrazioneProvvisoriaA() {
		return this.dataRegistrazioneProvvisoriaA;
	}

	/**
	 * @param dataRegistrazioneProvvisoriaA the dataRegistrazioneProvvisoriaA to set
	 */
	public void setDataRegistrazioneProvvisoriaA(Date dataRegistrazioneProvvisoriaA) {
		this.dataRegistrazioneProvvisoriaA = dataRegistrazioneProvvisoriaA;
	}

	/**
	 * @return the tipoElenco
	 */
	public String getTipoElenco() {
		return this.tipoElenco;
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
		return this.tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return this.annoMovimento;
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
		return this.numeroMovimento;
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
		return this.numeroSubmovimento;
	}

	/**
	 * @param numeroSubmovimento the numeroSubmovimento to set
	 */
	public void setNumeroSubmovimento(Integer numeroSubmovimento) {
		this.numeroSubmovimento = numeroSubmovimento;
	}

	/**
	 * @return the registrazioneMovFin
	 */
	public RegistrazioneMovFin getRegistrazioneMovFin() {
		return this.registrazioneMovFin;
	}

	/**
	 * @param registrazioneMovFin the registrazioneMovFin to set
	 */
	public void setRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
	}

	/**
	 * @return the importoDocumentoDa
	 */
	public BigDecimal getImportoDocumentoDa() {
		return this.importoDocumentoDa;
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
		return this.importoDocumentoA;
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
		return this.soggetto;
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
		return this.attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return this.capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
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
	 * @return the movimentoGestione
	 */
	public MovimentoGestione getMovimentoGestione() {
		return this.movimentoGestione;
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
		return this.subMovimentoGestione;
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
		return this.impegno;
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
		return this.subImpegno;
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
		return this.strutturaAmministrativoContabile;
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
		return this.stringaSACCapitolo;
	}

	/**
	 * @param stringaSACCapitolo the stringaSACCapitolo to set
	 */
	public void setStringaSACCapitolo(String stringaSACCapitolo) {
		this.stringaSACCapitolo = stringaSACCapitolo;
	}

	/**
	 * @return the listaTipoCausale
	 */
	public List<TipoCausale> getListaTipoCausale() {
		return this.listaTipoCausale;
	}

	/**
	 * @param listaTipoCausale the listaTipoCausale to set
	 */
	public void setListaTipoCausale(List<TipoCausale> listaTipoCausale) {
		this.listaTipoCausale = listaTipoCausale != null ? listaTipoCausale : new ArrayList<TipoCausale>();
	}

	/**	
	 * @return the listaStatoOperativoPrimaNota
	 */
	public List<StatoOperativoPrimaNota> getListaStatoOperativoPrimaNota() {
		return this.listaStatoOperativoPrimaNota;
	}

	/**	
	 * @param listaStatoOperativoPrimaNota the listaStatoOperativoPrimaNota to set
	 */
	public void setListaStatoOperativoPrimaNota(List<StatoOperativoPrimaNota> listaStatoOperativoPrimaNota) {
		this.listaStatoOperativoPrimaNota = listaStatoOperativoPrimaNota != null ? listaStatoOperativoPrimaNota : new ArrayList<StatoOperativoPrimaNota>();
	}

	/** //SIAC-6564
	 * @return the listaStatoAccettazionePrimaNotaDefinitiva
	 */
	public List<StatoAccettazionePrimaNotaDefinitiva> getListaStatoAccettazionePrimaNotaDefinitiva() {
		return listaStatoAccettazionePrimaNotaDefinitiva;
	}

	/**
	 * @param listaStatoAccettazionePrimaNotaDefinitiva the listaStatoAccettazionePrimaNotaDefinitiva to set
	 */
	public void setListaStatoAccettazionePrimaNotaDefinitiva(
			List<StatoAccettazionePrimaNotaDefinitiva> listaStatoAccettazionePrimaNotaDefinitiva) {
		this.listaStatoAccettazionePrimaNotaDefinitiva = listaStatoAccettazionePrimaNotaDefinitiva;
	}

	/**
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return this.listaCausaleEP;
	}

	/**
	 * @param listaCausaleEP the listaCausaleEP to set
	 */
	public void setListaCausaleEP(List<CausaleEP> listaCausaleEP) {
		this.listaCausaleEP = listaCausaleEP != null ? listaCausaleEP : new ArrayList<CausaleEP>();
	}

	/**
	 * @return the listaTipoEvento
	 */
	public List<TipoEvento> getListaTipoEvento() {
		return this.listaTipoEvento;
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
		return this.listaEvento;
	}

	/**
	 * @param listaEvento the listaEvento to set
	 */
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento != null ? listaEvento : new ArrayList<Evento>();
	}

	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return this.listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi != null ? listaClassi : new ArrayList<ClassePiano>();
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return this.listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return this.listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}

	/**
	 * @return the listaMissione
	 */
	public List<Missione> getListaMissione() {
		return this.listaMissione;
	}

	/**
	 * @param listaMissione the listaMissione to set
	 */
	public void setListaMissione(List<Missione> listaMissione) {
		this.listaMissione = listaMissione != null ? listaMissione : new ArrayList<Missione>();
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return this.listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}

	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return this.listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto != null ? listaTipoAtto : new ArrayList<TipoAtto>();
	}

	/**
	 * @return the listaTipiFinanziamento
	 */
	public List<TipoFinanziamento> getListaTipiFinanziamento() {
		return this.listaTipiFinanziamento;
	}

	/**
	 * @param listaTipiFinanziamento the listaTipiFinanziamento to set
	 */
	public void setListaTipiFinanziamento(List<TipoFinanziamento> listaTipiFinanziamento) {
		this.listaTipiFinanziamento = listaTipiFinanziamento != null ? listaTipiFinanziamento : new ArrayList<TipoFinanziamento>();
	}
	
	/**
	 * @return the ambito
	 */
	public Ambito getAmbito() {
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
	
	// Request
	
	/**
	 * Crea una request per il servizio di ricerca sintetica modulare causale
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
		
		// Cerco solo la libera
		causEpPerRequest.setTipoCausale(TipoCausale.Libera);
		causEpPerRequest.setAmbito(getAmbito());
		request.setCausaleEP(causEpPerRequest);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di ricerca sintetica conto.
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto() {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setBilancio(getBilancio());
		request.setConto(getConto());
		request.getConto().setAmbito(getAmbito());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di lettura del piano dei conti per codice e anno.
	 * @return la request creata
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno() {
		LeggiElementoPianoDeiContiByCodiceAndAnno request = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setElementoPianoDeiConti(getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di ricerca puntuale del capitolo di uscita gestione.
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
	 * Crea una request per il servizio di ricerca puntuale del capitolo di entrata gestione.
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
	 * Crea una request per il servizio di ricerca soggetto per chiave.
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
	 * Crea una request per il servizio di ricerca provvedimento.
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento req = creaRequest(RicercaProvvedimento.class);
		req.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(Integer.valueOf(getAttoAmministrativo().getAnno()));
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero() != 0 ? Integer.valueOf(getAttoAmministrativo().getNumero()) : null);
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getAttoAmministrativo().getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getAttoAmministrativo().getStrutturaAmmContabile()));
		req.setRicercaAtti(ricercaAtti);
		
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
	/**
	 * Crea una request per il servizio di ricerca registro A del cespite
	 * @return la request creata
	 */
	public RicercaSinteticaRegistroACespite creaRequestRicercaSinteticaRegistroACespite() {
		RicercaSinteticaRegistroACespite req = creaRequest(RicercaSinteticaRegistroACespite.class);
		
		req.setAnnoMovimento(getAnnoMovimento());
		req.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		req.setBilancio(getBilancio());
		req.setCapitolo(impostaEntitaFacoltativa(getCapitolo()));
		req.setCausaleEP(impostaEntitaFacoltativa(getCausaleEP()));
		req.setConto(impostaEntitaFacoltativa(getConto()));
		req.setDataRegistrazioneDa(getDataRegistrazioneDefinitivaDa());
		req.setDataRegistrazioneA(getDataRegistrazioneDefinitivaA());
		req.setDataRegistrazioneProvvisoriaDa(getDataRegistrazioneProvvisoriaDa());
		req.setDataRegistrazioneProvvisoriaA(getDataRegistrazioneProvvisoriaA());
		//req.setDocumento(documento);
		//req.setImporto(importo);
		req.setImportoDocumentoDa(getImportoDocumentoDa());
		req.setImportoDocumentoA(getImportoDocumentoA());
		req.setMovimentoGestione(impostaEntitaFacoltativa(getMovimentoGestione()));
		req.setNumeroMovimento(getNumeroMovimento());
		req.setNumeroSubmovimento(getNumeroSubmovimento());
		req.setPrimaNota(getPrimaNota());
		req.setRegistrazioneMovFin(getRegistrazioneMovFin());
		req.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		req.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		req.setSubMovimentoGestione(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		req.setTipoElenco(getTipoElenco());
		req.setTipoEvento(impostaEntitaFacoltativa(getTipoEvento()));
		
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setModelDetails(
				PrimaNotaModelDetail.StatoOperativo,
				PrimaNotaModelDetail.ContoInventario,
				PrimaNotaModelDetail.PrimaNotaInventario
		);
		
		// Impostazione dell'evento nella lista
		if(impostaEntitaFacoltativa(getEvento()) != null) {
			req.getEventi().add(getEvento());
		}
		
		return req;
	}

	


	
}
