/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitoloBySubdoc;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model generica per l'emissione degli ordinativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/11/2014
 *
 */
public abstract class GenericEmissioneOrdinativiModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2163831213336064643L;

	/**
	 * Enum rappresentante il tipo di emissione dell'ordinativo
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 12/11/2014
	 */
	public static enum TipoEmissioneOrdinativo {
		/** Pagina di emissione per elenco */
		ELENCO,
		/** Pagina si emissione per quote */
		QUOTE,
		/** Pagina si emissione per quote filtrato su provCassa */
		PROVCASSA,
	}
	
	private Integer idOperazioneAsincrona;
	
	private GenericEmissioneOrdinativiModel.TipoEmissioneOrdinativo tipoEmissioneOrdinativo;
	private AttoAmministrativo attoAmministrativo;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private Integer numeroElencoDa;
	private Integer numeroElencoA;
	private Soggetto soggetto;
	private Distinta distinta;
	
	private AllegatoAtto allegatoAtto;
	
	private BigDecimal totaleElenchi;
	private Integer numeroElenchi;
	
	private BigDecimal totaleQuote;
	private Integer numeroQuote;
	private String parametriRicerca;
	
	private String nota;
	private ContoTesoreria contoTesoreria;
	
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<StatoOperativoElencoDocumenti> listaStatoOperativoElencoDocumenti = new ArrayList<StatoOperativoElencoDocumenti>();
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<CodificaFin> listaDistinta = new ArrayList<CodificaFin>();
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
	
	private List<ElencoDocumentiAllegato> listElenchi = new ArrayList<ElencoDocumentiAllegato>();
	private List<CodiceBollo> listaBollo = new ArrayList<CodiceBollo>();
	private List<CommissioneDocumento> listaCommissioniDocumento = new ArrayList<CommissioneDocumento>();
	private CodiceBollo codiceBollo;
	private CommissioneDocumento commissioneDocumento;
	private Date dataScadenza;
	private Boolean flagNoDataScadenza;
	//SIAC-5302
	private List<Integer> uidsElaborati = new ArrayList<Integer>();
	private List<Integer> uidsElenchiElaborati = new ArrayList<Integer>();
	private Distinta distintaDaCercare;
	//SIAC-6175
	private Boolean flagDaTrasmettere;
	
	//SIAC-6206
	private List<ClassificatoreStipendi> listaClassificatoreStipendi = new ArrayList<ClassificatoreStipendi>();
	private ClassificatoreStipendi classificatoreStipendi;
	//SIAC-8784
	private Integer uidContoDaSelezionare;
	private List<Integer> uidsSubdocumentiSelezionati;
	

	/**
	 * @return the idOperazioneAsincrona
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
	}
	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincrona to set
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}
	/**
	 * @return the tipoEmissioneOrdinativo
	 */
	public GenericEmissioneOrdinativiModel.TipoEmissioneOrdinativo getTipoEmissioneOrdinativo() {
		return tipoEmissioneOrdinativo;
	}
	/**
	 * @param tipoEmissioneOrdinativo the tipoEmissioneOrdinativo to set
	 */
	public void setTipoEmissioneOrdinativo(GenericEmissioneOrdinativiModel.TipoEmissioneOrdinativo tipoEmissioneOrdinativo) {
		this.tipoEmissioneOrdinativo = tipoEmissioneOrdinativo;
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
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}
	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
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
	 * @return the elencoDocumentiAllegato
	 */
	public ElencoDocumentiAllegato getElencoDocumentiAllegato() {
		return elencoDocumentiAllegato;
	}
	/**
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato to set
	 */
	public void setElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
	}
	/**
	 * @return the numeroElencoDa
	 */
	public Integer getNumeroElencoDa() {
		return numeroElencoDa;
	}
	/**
	 * @param numeroElencoDa the numeroElencoDa to set
	 */
	public void setNumeroElencoDa(Integer numeroElencoDa) {
		this.numeroElencoDa = numeroElencoDa;
	}
	/**
	 * @return the numeroElencoA
	 */
	public Integer getNumeroElencoA() {
		return numeroElencoA;
	}
	/**
	 * @param numeroElencoA the numeroElencoA to set
	 */
	public void setNumeroElencoA(Integer numeroElencoA) {
		this.numeroElencoA = numeroElencoA;
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
	 * @return the distinta
	 */
	public Distinta getDistinta() {
		return distinta;
	}
	
	/**
	 * Gets the distinta da cercare.
	 *
	 * @return the distinta
	 */
	public Distinta getDistintaDaCercare() {
		return distintaDaCercare;
	}
	
	/**
	 * Sets the distinta da cercare.
	 *
	 * @param distintaDaCercare the new distinta da cercare
	 */
	public void setDistintaDaCercare(Distinta distintaDaCercare) {
		this.distintaDaCercare = distintaDaCercare;
	}
	/**
	 * @param distinta the distinta to set
	 */
	public void setDistinta(Distinta distinta) {
		this.distinta = distinta;
	}
	/**
	 * @return the allegatoAtto
	 */
	public AllegatoAtto getAllegatoAtto() {
		return allegatoAtto;
	}
	/**
	 * @param allegatoAtto the allegatoAtto to set
	 */
	public void setAllegatoAtto(AllegatoAtto allegatoAtto) {
		this.allegatoAtto = allegatoAtto;
	}
	/**
	 * @return the totaleElenchi
	 */
	public BigDecimal getTotaleElenchi() {
		return totaleElenchi;
	}
	/**
	 * @param totaleElenchi the totaleElenchi to set
	 */
	public void setTotaleElenchi(BigDecimal totaleElenchi) {
		this.totaleElenchi = totaleElenchi;
	}
	/**
	 * @return the numeroElenchi
	 */
	public Integer getNumeroElenchi() {
		return numeroElenchi;
	}
	/**
	 * @param numeroElenchi the numeroElenchi to set
	 */
	public void setNumeroElenchi(Integer numeroElenchi) {
		this.numeroElenchi = numeroElenchi;
	}
	/**
	 * @return the totaleQuote
	 */
	public BigDecimal getTotaleQuote() {
		return totaleQuote;
	}
	/**
	 * @param totaleQuote the totaleQuote to set
	 */
	public void setTotaleQuote(BigDecimal totaleQuote) {
		this.totaleQuote = totaleQuote;
	}
	/**
	 * @return the numeroQuote
	 */
	public Integer getNumeroQuote() {
		return numeroQuote;
	}
	/**
	 * @param numeroQuote the numeroQuote to set
	 */
	public void setNumeroQuote(Integer numeroQuote) {
		this.numeroQuote = numeroQuote;
	}
	/**
	 * @return the parametriRicerca
	 */
	public String getParametriRicerca() {
		return parametriRicerca;
	}
	/**
	 * @param parametriRicerca the parametriRicerca to set
	 */
	public void setParametriRicerca(String parametriRicerca) {
		this.parametriRicerca = parametriRicerca;
	}
	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}
	/**
	 * @param nota the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}
	/**
	 * @return the contoTesoreria
	 */
	public ContoTesoreria getContoTesoreria() {
		return contoTesoreria;
	}
	/**
	 * @param contoTesoreria the contoTesoreria to set
	 */
	public void setContoTesoreria(ContoTesoreria contoTesoreria) {
		this.contoTesoreria = contoTesoreria;
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
	 * @return the listaStatoOperativoElencoDocumenti
	 */
	public List<StatoOperativoElencoDocumenti> getListaStatoOperativoElencoDocumenti() {
		return listaStatoOperativoElencoDocumenti;
	}
	/**
	 * @param listaStatoOperativoElencoDocumenti the listaStatoOperativoElencoDocumenti to set
	 */
	public void setListaStatoOperativoElencoDocumenti(List<StatoOperativoElencoDocumenti> listaStatoOperativoElencoDocumenti) {
		this.listaStatoOperativoElencoDocumenti = listaStatoOperativoElencoDocumenti != null ? listaStatoOperativoElencoDocumenti : new ArrayList<StatoOperativoElencoDocumenti>();
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
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}
	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}
	/**
	 * @return the listaDistinta
	 */
	public List<CodificaFin> getListaDistinta() {
		return listaDistinta;
	}
	/**
	 * @param listaDistinta the listaDistinta to set
	 */
	public void setListaDistinta(List<CodificaFin> listaDistinta) {
		this.listaDistinta = listaDistinta != null ? listaDistinta : new ArrayList<CodificaFin>();
	}
	/**
	 * @return the listaContoTesoreria
	 */
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria;
	}
	/**
	 * @param listaContoTesoreria the listaContoTesoreria to set
	 */
	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria != null ? listaContoTesoreria : new ArrayList<ContoTesoreria>();
	}
	
	/**
	 * @return the listElenchi
	 */
	public List<ElencoDocumentiAllegato> getListElenchi() {
		return listElenchi;
	}
	/**
	 * @param listElenchi the listElenchi to set
	 */
	public void setListElenchi(List<ElencoDocumentiAllegato> listElenchi) {
		this.listElenchi = listElenchi != null ? listElenchi : new ArrayList<ElencoDocumentiAllegato>();
	}
	
	/**
	 * @return the listaBollo
	 */
	public List<CodiceBollo> getListaBollo() {
		return listaBollo;
	}
	
	/**
	 * @param listaBollo the listaBollo to set
	 */
	public void setListaBollo(List<CodiceBollo> listaBollo) {
		this.listaBollo =listaBollo !=null ? listaBollo :new ArrayList<CodiceBollo>() ;
	}

	//task-291
	public List<CommissioneDocumento> getListaCommissioniDocumento() {
		return listaCommissioniDocumento;
	}
	
	//task-291
	public void setListaCommissioniDocumento(List<CommissioneDocumento> listaCommissioniDocumento) {
		this.listaCommissioniDocumento = listaCommissioniDocumento;
	}
		
	/**
	 * @return the codiceBollo
	 */
	public CodiceBollo getCodiceBollo() {
		return codiceBollo;
	}
	/**
	 * @param codiceBollo the codiceBollo to set
	 */
	public void setCodiceBollo(CodiceBollo codiceBollo) {
		this.codiceBollo = codiceBollo;
	}


	/**
	 * @return the commissioneDocumento
	 */
	public CommissioneDocumento getCommissioneDocumento() {
		return commissioneDocumento;
	}
	/**
	 * @param commissioneDocumento the commissioneDocumento to set
	 */
	public void setCommissioneDocumento(CommissioneDocumento commissioneDocumento) {
		this.commissioneDocumento = commissioneDocumento;
	}
	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza != null ? new Date(dataScadenza.getTime()) : null;
	}
	
	
	
	/**
	 * @param dataScadenza the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza != null ? new Date(dataScadenza.getTime()) : null;
	}
	
	/**
	 * @return the flagNoDataScadenza
	 */
	public Boolean isFlagNoDataScadenza() {
		return flagNoDataScadenza;
	}
	/**
	 * @param flagNoDataScadenza the flagNoDataScadenza to set
	 */
	public void setFlagNoDataScadenza(Boolean flagNoDataScadenza) {
		this.flagNoDataScadenza = flagNoDataScadenza;
	}
	
	/**
	 * Gets the uids elaborati.
	 *
	 * @return the uidElaborati
	 */
	public List<Integer> getUidsElaborati() {
		return uidsElaborati;
	}
	
	/**
	 * Sets the uids elaborati.
	 *
	 * @param uidsElaborati the new uids elaborati
	 */
	public void setUidsElaborati(List<Integer> uidsElaborati) {
		this.uidsElaborati = uidsElaborati != null ? uidsElaborati : new ArrayList<Integer>();
	}
	
	/**
	 * Gets the uids elaborati.
	 *
	 * @return the uidElaborati
	 */
	public List<Integer> getUidsElenchiElaborati() {
		return uidsElenchiElaborati;
	}
	
	/**
	 * Sets the uids elaborati.
	 *
	 * @param uidsElenchiElaborati the new uids elenchi elaborati
	 */
	public void setUidsElenchiElaborati(List<Integer> uidsElenchiElaborati) {
		this.uidsElenchiElaborati = uidsElenchiElaborati != null ? uidsElenchiElaborati : new ArrayList<Integer>();
	}
	
	
	/**
	 * @return the flagDaTrasmettere
	 */
	public Boolean getFlagDaTrasmettere() {
		return flagDaTrasmettere;
	}
	/**
	 * @param flagDaTrasmettere the flagDaTrasmettere to set
	 */
	public void setFlagDaTrasmettere(Boolean flagDaTrasmettere) {
		this.flagDaTrasmettere = flagDaTrasmettere;
	}
	
	
	/**
	 * @return the listaClassificatoreStipendi
	 */
	public List<ClassificatoreStipendi> getListaClassificatoreStipendi() {
		return listaClassificatoreStipendi;
	}
	/**
	 * @param listaClassificatoreStipendi the listaClassificatoreStipendi to set
	 */
	public void setListaClassificatoreStipendi(List<ClassificatoreStipendi> listaClassificatoreStipendi) {
		this.listaClassificatoreStipendi = listaClassificatoreStipendi;
	}
	
	
	/**
	 * @return the classificatoreStipendi
	 */
	public ClassificatoreStipendi getClassificatoreStipendi() {
		return classificatoreStipendi;
	}
	/**
	 * @param classificatoreStipendi the classificatoreStipendi to set
	 */
	public void setClassificatoreStipendi(ClassificatoreStipendi classificatoreStipendi) {
		this.classificatoreStipendi = classificatoreStipendi;
	}
	
	public Integer getUidContoDaSelezionare() {
		return uidContoDaSelezionare;
	}
	public void setUidContoDaSelezionare(Integer uidContoDaSelezionare) {
		this.uidContoDaSelezionare = uidContoDaSelezionare;
	}
	
	public List<Integer> getUidsSubdocumentiSelezionati() {
		return uidsSubdocumentiSelezionati;
	}
	public void setUidsSubdocumentiSelezionati(List<Integer> uidsSubdocumentiSelezionati) {
		this.uidsSubdocumentiSelezionati = uidsSubdocumentiSelezionati;
	}
	/**
	 * Popola ids elenchi elaborati.
	 */
	public void popolaIdsElenchiElaborati() {
		
		for (ElencoDocumentiAllegato elenco : getListElenchi()) {
			getUidsElenchiElaborati().add(Integer.valueOf(elenco.getUid()));
		}
	}
	
	/**
	 * Popola ids subdoc elaborati.
	 */
	public void popolaIdsSubdocElaborati() {
		// Da implementare nelle sottoclassi
	}

	
	/**
	 * @return the descrizioneCompletaAttoAmministrativo
	 */
	public String getDescrizioneCompletaAttoAmministrativo() {
		final StringBuilder sb = new StringBuilder();
		if(getAttoAmministrativo() != null) {
			AttoAmministrativo aa = getAttoAmministrativo();
			sb.append(aa.getAnno())
				.append("/")
				.append(aa.getNumero());
			if(aa.getTipoAtto() != null) {
				sb.append("/")
					.append(aa.getTipoAtto().getCodice());
			}
			if(aa.getStrutturaAmmContabile() != null) {
				sb.append("/")
					.append(aa.getStrutturaAmmContabile().getCodice());
			}
			if(StringUtils.isNotBlank(aa.getOggetto())) {
				sb.append(" - ")
					.append(aa.getOggetto());
			}
		}
		return sb.toString();
	}
	
	/**
	 * Gets the etichetta classificatore stipendi.
	 *
	 * @return the etichetta classificatore stipendi
	 */
	public String getEtichettaClassificatoreStipendi() {
		String defaulStr = "Classificatore";
		if(getListaClassificatoreStipendi() == null && getListaClassificatoreStipendi().isEmpty() ) {
			return defaulStr;
		}
		ClassificatoreStipendi cs = getListaClassificatoreStipendi().get(0);
		return cs != null && cs.getTipoClassificatore() != null? StringUtils.defaultIfBlank(cs.getTipoClassificatore().getDescrizione(), defaulStr) : defaulStr;
	}
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio {@link Liste}
	 * @param tipiLista i tipi di lista da cercare
	 * @return la request creata
	 */
	public Liste creaRequestListe(TipiLista... tipiLista) {
		Liste request = creaRequest(Liste.class);
		
		request.setEnte(getEnte());
		request.setTipi(tipiLista);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiContiTesoreria}.
	 * 
	 * @return la request creata
	 */
	public LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = creaRequest(LeggiContiTesoreria.class);
		
		request.setEnte(getEnte());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaCodiceBollo}.
	 * @return la request creata
	 */
	public RicercaCodiceBollo creaRequestRicercaCodiceBollo() {
		RicercaCodiceBollo request = creaRequest(RicercaCodiceBollo.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
	public RicercaCodiceCommissioneDocumento creaRequestRicercaCodiceCommissioneDocumento() {
		RicercaCodiceCommissioneDocumento request = creaRequest(RicercaCodiceCommissioneDocumento.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @param statoOperativo lo stato per cui eventualmente effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento(String statoOperativo) {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(getAttoAmministrativo().getAnno());
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero());
		ricercaAtti.setStatoOperativo(StringUtils.isBlank(statoOperativo) ? null : statoOperativo);
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		request.setRicercaAtti(ricercaAtti);
		
		return request;
	}
	
	//SIAC-5060
	/**
	 * Creazione della request per il servizio di {@link RicercaElenco}
	 * @return la request creata
	 */
	public RicercaElenco creaRequestRicercaElenco() {
		RicercaElenco request = creaRequest(RicercaElenco.class);
		//mi interessa solo il primo, se e' unico
		request.setParametriPaginazione(new ParametriPaginazione(0,1));
		// Popolo l'elenco
		//getElencoDocumentiAllegato().setEnte(getEnte());
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
		eda.setNumero(getElencoDocumentiAllegato().getNumero()!= null? getElencoDocumentiAllegato().getNumero() : null);
		eda.setAnno(getElencoDocumentiAllegato().getAnno());
		eda.setEnte(getEnte());
		if(idEntitaPresente(getAttoAmministrativo())){
			request.setAttoAmministrativo(getAttoAmministrativo());	
		}
		request.setElencoDocumentiAllegato(eda);
		
		request.setNumeroDa(getNumeroElencoDa());
		request.setNumeroA(getNumeroElencoA());
		
		ElencoDocumentiAllegatoModelDetail[] modelDetails = new ElencoDocumentiAllegatoModelDetail[] {ElencoDocumentiAllegatoModelDetail.AttoAllegatoExtended};
		request.setModelDetails(modelDetails);
		
		return request;
	}
	
	
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaElencoDaEmettere}.
	 * 
	 * @return la request creata
	 */
	public RicercaElencoDaEmettere creaRequestRicercaElencoDaEmettere() {
		RicercaElencoDaEmettere request = creaRequest(RicercaElencoDaEmettere.class);
		
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
		eda.setEnte(getEnte());
		request.setElencoDocumentiAllegato(eda);
		
		request.setAttoAmministrativo(getAttoAmministrativo());
		request.setStatiOperativiElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.COMPLETATO));
		// Paginazione a 50
		request.setParametriPaginazione(creaParametriPaginazione(50));
		
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
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}
	
	/* **** Utilities **** */
	
	/**
	 * Crea i parametri di ricerca.
	 */
	public void creaParametriRicerca() {
		StringBuilder sb = new StringBuilder();
		creaParametriRicercaAttoAmministrativo(sb);
		creaParametriRicercaElenco(sb);
		creaParametriRicercaCapitolo(sb);
		creaParametriRicercaSoggetto(sb);
		creaParametriRicercaAltriDati(sb);
		
		String result = StringUtils.substringBeforeLast(sb.toString(), "<br/>");
		setParametriRicerca(result);
	}
	
	/**
	 * Crea i parametri di ricerca per l'atto amministrativo.
	 * 
	 * @param sb lo stringBuilder in cui injettare i dati
	 */
	private void creaParametriRicercaAttoAmministrativo(StringBuilder sb) {
		boolean done = false;
		if(getAttoAmministrativo() != null && getAttoAmministrativo().getAnno() != 0 && getAttoAmministrativo().getNumero() != 0) {
			done = true;
			sb.append("<b>Provvedimento: </b>")
				.append(getAttoAmministrativo().getAnno())
				.append("/")
				.append(getAttoAmministrativo().getNumero());
			if(getTipoAtto() != null && getTipoAtto().getUid() != 0) {
				sb.append(" <b>Tipo Atto: </b>")
					.append(getTipoAtto().getCodice())
					.append(" - ")
					.append(getTipoAtto().getDescrizione());
			}
			if(getStrutturaAmministrativoContabile() != null && getStrutturaAmministrativoContabile().getUid() != 0) {
				sb.append(" <b>Struttura: </b>")
					.append(getStrutturaAmministrativoContabile().getCodice())
					.append(" - ")
					.append(getStrutturaAmministrativoContabile().getDescrizione());
			}
		}
		if(done) {
			sb.append("<br/>");
		}
	}
	
	/**
	 * Crea i parametri di ricerca per l'elenco.
	 * 
	 * @param sb lo stringBuilder in cui injettare i dati
	 */
	private void creaParametriRicercaElenco(StringBuilder sb) {
		boolean done = false;
		if(getElencoDocumentiAllegato() != null && getElencoDocumentiAllegato().getAnno() != null) {
			done = true;
			sb.append("<b>Anno elenco: </b>")
				.append(getElencoDocumentiAllegato().getAnno());
		}
		if(getNumeroElencoDa() != null) {
			done = true;
			sb.append(" <b>Elenco numero da: </b>")
				.append(getNumeroElencoDa());
		}
		if(getNumeroElencoA() != null) {
			done = true;
			sb.append(" <b>Elenco numero a: </b>")
				.append(getNumeroElencoA());
		}
		if(done) {
			sb.append("<br/>");
		}
	}
	
	/**
	 * Crea i parametri di ricerca per il capitolo.
	 * 
	 * @param sb lo stringBuilder in cui injettare i dati
	 */
	protected abstract void creaParametriRicercaCapitolo(StringBuilder sb);
	
	/**
	 * Crea i parametri di ricerca per il soggetto.
	 * 
	 * @param sb lo stringBuilder in cui injettare i dati
	 */
	private void creaParametriRicercaSoggetto(StringBuilder sb) {
		boolean done = false;
		if(getSoggetto() != null && StringUtils.isNotBlank(getSoggetto().getCodiceSoggetto())) {
			done = true;
			sb.append("<b>Soggetto : </b>")
				.append(getSoggetto().getCodiceSoggetto());
		}
		if(done) {
			sb.append("<br/>");
		}
	}
	
	/**
	 * Crea i parametri di ricerca per gli ulteriori dati.
	 * 
	 * @param sb lo stringBuilder in cui injettare i dati
	 */
	private void creaParametriRicercaAltriDati(StringBuilder sb) {
		boolean done = false;
		if(getDistintaDaCercare() != null && getDistintaDaCercare().getUid() != 0) {
			done = true;
			sb.append("<b>Distinta : </b>")
				.append(getDistintaDaCercare().getCodice())
				.append(" - ")
				.append(getDistintaDaCercare().getDescrizione());
		}
		if(done) {
			sb.append("<br/>");
		}
	}
	
	/**
	 * Crea request leggi classificatori by tipologie classificatori.
	 *
	 * @return the leggi classificatori by tipologie classificatori
	 */
	public LeggiClassificatoriByTipologieClassificatori	creaRequestLeggiClassificatoriByTipologieClassificatori(){
		List<TipologiaClassificatore> listaTipologieClassificatori = new ArrayList<TipologiaClassificatore>();
		listaTipologieClassificatori.add(TipologiaClassificatore.CLASSIFICATORE_STIPENDI);
		return creaRequestLeggiClassificatoriByTipologieClassificatori(listaTipologieClassificatori);
	}
	public ControllaDisponibilitaCassaContoVincolatoCapitolo creaRequestControllaDisponibilitaCassaContoVincolato() {
		ControllaDisponibilitaCassaContoVincolatoCapitolo request = creaRequest(ControllaDisponibilitaCassaContoVincolatoCapitolo.class);
		impostaSubdocumentiSuRequestContoVincolato(request);
		request.impostaContoTesoreriaFromContoTesoreriaBil(getContoTesoreria());
		return request;
	}
	//SIAC-8017-CMTO
	protected abstract void impostaSubdocumentiSuRequestContoVincolato(ControllaDisponibilitaCassaContoVincolatoCapitolo request);
	
	public boolean isGestioneContiVincolati() {
		return "TRUE".equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_CONTI_VINCOLATI));	
	}
	
	/**
	 * Crea request carica sotto conti vincolati capitolo.
	 *
	 * @return the carica sotto conti vincolati capitolo
	 */
	public LeggiSottoContiVincolatiCapitoloBySubdoc creaRequestCaricaSottoContiVincolatiCapitolo() {
		LeggiSottoContiVincolatiCapitoloBySubdoc req = creaRequest(LeggiSottoContiVincolatiCapitoloBySubdoc.class);
		req.setIdsSubdocumenti(getUidsSubdocumentiSelezionati());
		return req;
	}
	
}
