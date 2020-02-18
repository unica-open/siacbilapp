/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;

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
import it.csi.siac.siacbilapp.frontend.ui.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorEvento;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
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
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model base GEN/GSA per la ricerca della registrazione Movimento Fin.
 */
public abstract class RicercaRegistrazioneMovFinBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7092354469443303726L;
	
	private RegistrazioneMovFin registrazioneMovFin;
	private Integer uidTipoEvento;
	private TipoEvento tipoEvento;
	private Evento evento;
	private Integer annoMovimento;
	private String numeroMovimento;
	private Integer numeroSubmovimento;
	private Date dataRegistrazioneDa;
	private Date dataRegistrazioneA;
	private String tipoElenco;
	
	private List<TipoEvento> listaTipiEvento = new ArrayList<TipoEvento>();
	private List<TipoEvento> listaTipiEventoSpesa = new ArrayList<TipoEvento>();
	private List<TipoEvento> listaTipiEventoEntrata = new ArrayList<TipoEvento>();
	private List<Evento> listaEventi = new ArrayList<Evento>();
	private List<StatoOperativoRegistrazioneMovFin> listaStati = new ArrayList<StatoOperativoRegistrazioneMovFin>();
	
	//SIAC-5799
	private AttoAmministrativo attoAmministrativo;
	private List<AttoAmministrativo> listaAttoAmministrativo = new ArrayList<AttoAmministrativo>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();

	
	/* modale compilazione guidata conto*/
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	/*dati da ricaricare */
	private Integer uidTipoEventoDaRicerca;
	private Integer uidEventoDaRicerca;
	
	//SIAC-5290
	private Soggetto soggetto;
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private MovimentoGestione movimentoGestione;
	private MovimentoGestione subMovimentoGestione;
	private Impegno impegno;
	private Accertamento accertamento;
	private SubImpegno subImpegno;
	private SubAccertamento subAccertamento;
	private Capitolo<?,?> capitolo;
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	//SIAC-5944
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;

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
	 * @return the uidTipoEvento
	 */
	public Integer getUidTipoEvento() {
		return uidTipoEvento;
	}
	/**
	 * @param uidTipoEvento the uidTipoEvento to set
	 */
	public void setUidTipoEvento(Integer uidTipoEvento) {
		this.uidTipoEvento = uidTipoEvento;
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
	 * @return the listaTipiEvento
	 */
	public List<TipoEvento> getListaTipiEvento() {
		return listaTipiEvento;
	}
	/**
	 * @param listaTipiEvento the listaTipiEvento to set
	 */
	public void setListaTipiEvento(List<TipoEvento> listaTipiEvento) {
		this.listaTipiEvento = listaTipiEvento;
	}
	
	/**
	 * @return the listaTipiEventoSpesa
	 */
	public List<TipoEvento> getListaTipiEventoSpesa() {
		return listaTipiEventoSpesa;
	}

	/**
	 * @param listaTipiEventoSpesa the listaTipiEventoSpesa to set
	 */
	public void setListaTipiEventoSpesa(List<TipoEvento> listaTipiEventoSpesa) {
		this.listaTipiEventoSpesa = listaTipiEventoSpesa;
	}

	/**
	 * @return the listaTipiEventoEntrata
	 */
	public List<TipoEvento> getListaTipiEventoEntrata() {
		return listaTipiEventoEntrata;
	}

	/**
	 * @param listaTipiEventoEntrata the listaTipiEventoEntrata to set
	 */
	public void setListaTipiEventoEntrata(List<TipoEvento> listaTipiEventoEntrata) {
		this.listaTipiEventoEntrata = listaTipiEventoEntrata;
	}

	/**
	 * @return the listaEventi
	 */
	public List<Evento> getListaEventi() {
		// SIAC-3528 ordinamento in base al codice (ordine alfabetico)
		return new SortedSetList<Evento>(listaEventi != null ? listaEventi : new ArrayList<Evento>(), ComparatorEvento.INSTANCE);
	}
	/**
	 * @param listaEventi the listaEventi to set
	 */
	public void setListaEventi(List<Evento> listaEventi) {
		this.listaEventi = listaEventi;
	}
	/**
	 * @return the listaStati
	 */
	public List<StatoOperativoRegistrazioneMovFin> getListaStati() {
		return listaStati;
	}
	/**
	 * @param listaStati the listaStati to set
	 */
	public void setListaStati(List<StatoOperativoRegistrazioneMovFin> listaStati) {
		this.listaStati = listaStati;
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
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * Ottiene l'URL di base per la funzionalit&agrave;
	 * @return il baseUrl
	 */
	public abstract String getBaseUrl();

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
	 * Gets the capitolo.
	 *
	 * @return the capitolo
	 */
	public Capitolo<?,?> getCapitolo() {
		return capitolo;
	}
	
	/**
	 * Sets the capitolo.
	 *
	 * @param capitolo the capitolo
	 */
	public void setCapitolo(Capitolo<?,?> capitolo) {
		this.capitolo = capitolo;
	}
	
	/**
	 * Gets the lista tipi finanziamento.
	 *
	 * @return the lista tipi finanziamento
	 */
	public List<TipoFinanziamento> getListaTipiFinanziamento() {
		return listaTipiFinanziamento;
	}
	
	/**
	 * Sets the lista tipi finanziamento.
	 *
	 * @param listaTipiFinanziamento the new lista tipi finanziamento
	 */
	public void setListaTipiFinanziamento(List<TipoFinanziamento> listaTipiFinanziamento) {
		this.listaTipiFinanziamento = listaTipiFinanziamento;
	}
	/**
	 * Gets the soggetto.
	 *
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}
	
	/**
	 * Sets the soggetto.
	 *
	 * @param soggetto the new soggetto
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}
	
	/**
	 * Gets the lista classe soggetto.
	 *
	 * @return the lista classe soggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}
	
	/**
	 * Sets the lista classe soggetto.
	 *
	 * @param listaClasseSoggetto the new lista classe soggetto
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}
	
	/**
	 * Gets the movimento gestione.
	 *
	 * @return the movimentoGestione
	 */
	public MovimentoGestione getMovimentoGestione() {
		return movimentoGestione;
	}
	
	/**
	 * Sets the movimento gestione.
	 *
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(MovimentoGestione movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}
	
	/**
	 * Gets the sub movimento gestione.
	 *
	 * @return the subMovimentoGestione
	 */
	public MovimentoGestione getSubMovimentoGestione() {
		return subMovimentoGestione;
	}
	
	/**
	 * Sets the sub movimento gestione.
	 *
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
	 * Gets the accertamento.
	 *
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}
	
	/**
	 * Sets the accertamento.
	 *
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
	
	/**
	 * Gets the numero sub impegno.
	 *
	 * @return the numeroSubImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}
	
	/**
	 * Sets the numero sub impegno.
	 *
	 * @param subImpegno the new sub impegno
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}
	/**
	 * @return the numeroSubAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}
	
	/**
	 * Sets the sub accertamento.
	 *
	 * @param subAccertamento the new sub accertamento
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
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
		this.listaTipoAtto = listaTipoAtto;
	}
	/**
	 * @return the listaAttoAmministrativo
	 */
	public List<AttoAmministrativo> getListaAttoAmministrativo() {
		return listaAttoAmministrativo;
	}
	/**
	 * @param listaAttoAmministrativo the listaAttoAmministrativo to set
	 */
	public void setListaAttoAmministrativo(List<AttoAmministrativo> listaAttoAmministrativo) {
		this.listaAttoAmministrativo = listaAttoAmministrativo;
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
	 * Crea una request per il servizio di {@link RicercaEventiPerTipo}.
	 * 
	 * @return la request creata.
	 */
	public RicercaEventiPerTipo creaRequestRicercaEventiPerTipo() {
		RicercaEventiPerTipo req = creaRequest(RicercaEventiPerTipo.class);
		TipoEvento te = new TipoEvento();
		te.setUid(uidTipoEvento);
		req.setTipoEvento(te);
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRegistrazioneMovFin}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaRegistrazioneMovFin creaRequestRicercaSinteticaRegistrazioneMovFin() {
		RicercaSinteticaRegistrazioneMovFin req = creaRequest(RicercaSinteticaRegistrazioneMovFin.class);
		
		getRegistrazioneMovFin().setBilancio(getBilancio());
		getRegistrazioneMovFin().setAmbito(getAmbito());
		
		
		req.setAnnoMovimento(getAnnoMovimento());
		req.setNumeroMovimento(StringUtils.isNotBlank(getNumeroMovimento()) ? getNumeroMovimento() : null);
		req.setNumeroSubmovimento(getNumeroSubmovimento());
		req.setEvento(impostaEntitaFacoltativa(getEvento()));
		req.setTipoEvento(getTipoEvento());
		req.setDataRegistrazioneA(getDataRegistrazioneA());
		req.setDataRegistrazioneDa(getDataRegistrazioneDa());
		req.setRegistrazioneMovFin(getRegistrazioneMovFin());
		
		
		//SIAC-5290
		req.setCapitolo(impostaEntitaFacoltativa(getCapitolo()));
		req.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		

		//SIAC-5799
		req.setMovimentoGestione(impostaEntitaFacoltativa(getMovimentoGestione()));		
		req.setSubmovimentoGestione(impostaEntitaFacoltativa(getSubMovimentoGestione()));		
		req.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));	
		req.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		req.setParametriPaginazione(creaParametriPaginazione());
		
		return req;
	}

	/**
	 * Crea una request per il servizion di {@link LeggiElementoPianoDeiContiByCodiceAndAnno}.
	 * 
	 * @return la request creata
	 */
	public LeggiElementoPianoDeiContiByCodiceAndAnno creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno() {
		LeggiElementoPianoDeiContiByCodiceAndAnno req = creaRequest(LeggiElementoPianoDeiContiByCodiceAndAnno.class);
		req.setAnno(getBilancio().getAnno());
		req.setElementoPianoDeiConti(getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato());
		return req;
	}
	
	//SIAC-5290
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

	/**
	 * Composizione della stringa di riepilogo.
	 * 
	 * @return la stringa di riepilogo
	 */
	public String componiStringaRiepilogo() {
		Evento ev = ComparatorUtils.searchByUid(getListaEventi(), getEvento());
		if(ev == null || ev.getTipoEvento() == null) {
			return "";
		}
		return "Tipo Evento: " + ev.getTipoEvento().getDescrizione() + " - Evento: " + ev.getDescrizione();
	}
	
	/**
	 * Salva nel model gli uid da impostare in caso di nessun dato trovato, per reimpostare la jsp con l'ultima ricerca effettuata.
	 * 
	 */
	public void impostoDatiNelModel(){
		if (getEvento() != null && getEvento().getTipoEvento() != null) {
			setUidTipoEventoDaRicerca(getEvento().getTipoEvento().getUid());
			setUidEventoDaRicerca(getEvento().getUid());
		}
		
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(getAttoAmministrativo().getAnno());
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero());
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getAttoAmministrativo().getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getAttoAmministrativo().getStrutturaAmmContabile()));
		
		
		request.setRicercaAtti(ricercaAtti);
		return request;
	}
	

	
	
}
