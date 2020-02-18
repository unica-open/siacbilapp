/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.TipoAvviso;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe generica per il documento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 25/03/2014
 *
 */
public class GenericDocumentoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7966650246876691445L;

	private Soggetto soggetto;
	
	// Campo derivato
	// Il setter per il netto non è definito. Questo perché voglio evitare che tale valore sia forzato dal client
	private BigDecimal netto = BigDecimal.ZERO;
	
	// Dati provvedimento
	private AttoAmministrativo attoAmministrativo;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private Integer uidProvvedimento;
	private String statoProvvedimento;
	private TipoAtto tipoAtto;
	private ElencoDocumentiAllegato elencoDocumenti;
//	private StatoOperativoAtti statoOperativoAtti;
		
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	private List<TipoDocumento> listaTipoDocumentoClone = new ArrayList<TipoDocumento>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<CodiceBollo> listaCodiceBollo = new ArrayList<CodiceBollo>();
	
	private List<TipoAvviso> listaTipoAvviso = new ArrayList<TipoAvviso>();
	private List<NoteTesoriere> listaNoteTesoriere = new ArrayList<NoteTesoriere>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	//Stato Operativo Documento
	private List<StatoOperativoDocumento> listaStatoOperativoDocumento = new ArrayList<StatoOperativoDocumento>();
	
	private String suffisso = "";
	// Inserito per distinguere le quietanze. Non ho idea di cosa sia
	private Boolean quietanza = Boolean.FALSE;

	// SIAC-6565
	// Stato SDI Documento
	private List<StatoSDIDocumento> listaStatoSDIDocumento = new ArrayList<StatoSDIDocumento>();
	
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
	 * @return the netto
	 */
	public BigDecimal getNetto() {
		return netto;
	}
	
	/**
	 * @param netto the netto to set
	 */
	protected void impostaNetto(BigDecimal netto) {
		this.netto = netto;
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
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(
			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the uidProvvedimento
	 */
	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}

	/**
	 * @param uidProvvedimento the uidProvvedimento to set
	 */
	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}

	/**
	 * @return the statoProvvedimento
	 */
	public String getStatoProvvedimento() {
		return statoProvvedimento;
	}

	/**
	 * @param statoProvvedimento the statoProvvedimento to set
	 */
	public void setStatoProvvedimento(String statoProvvedimento) {
		this.statoProvvedimento = statoProvvedimento;
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
	 * @return the elencoDocumenti
	 */
	public ElencoDocumentiAllegato getElencoDocumenti() {
		return elencoDocumenti;
	}

	/**
	 * @param elencoDocumenti the elencoDocumenti to set
	 */
	public void setElencoDocumenti(ElencoDocumentiAllegato elencoDocumenti) {
		this.elencoDocumenti = elencoDocumenti;
	}

//	/**
//	 * @return the statoOperativoAtti
//	 */
//	public StatoOperativoAtti getStatoOperativoAtti() {
//		return statoOperativoAtti;
//	}
//
//	/**
//	 * @param statoOperativoAtti the statoOperativoAtti to set
//	 */
//	public void setStatoOperativoAtti(StatoOperativoAtti statoOperativoAtti) {
//		this.statoOperativoAtti = statoOperativoAtti;
//	}

	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}
	
	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}
	
	/**
	 * @return the listaTipoDocumentoClone
	 */
	public List<TipoDocumento> getListaTipoDocumentoClone() {
		return listaTipoDocumentoClone;
	}

	/**
	 * @param listaTipoDocumentoClone the listaTipoDocumentoClone to set
	 */
	public void setListaTipoDocumentoClone(List<TipoDocumento> listaTipoDocumentoClone) {
		this.listaTipoDocumentoClone = listaTipoDocumentoClone;
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
	 * @return the listaCodiceBollo
	 */
	public List<CodiceBollo> getListaCodiceBollo() {
		return listaCodiceBollo;
	}
	
	/**
	 * @param listaCodiceBollo the listaCodiceBollo to set
	 */
	public void setListaCodiceBollo(List<CodiceBollo> listaCodiceBollo) {
		this.listaCodiceBollo = listaCodiceBollo;
	}
	
	/**
	 * @return the listaTipoAvviso
	 */
	public List<TipoAvviso> getListaTipoAvviso() {
		return listaTipoAvviso;
	}

	/**
	 * @param listaTipoAvviso the listaTipoAvviso to set
	 */
	public void setListaTipoAvviso(List<TipoAvviso> listaTipoAvviso) {
		this.listaTipoAvviso = listaTipoAvviso;
	}

	/**
	 * @return the listaNoteTesoriere
	 */
	public List<NoteTesoriere> getListaNoteTesoriere() {
		return listaNoteTesoriere;
	}

	/**
	 * @param listaNoteTesoriere the listaNoteTesoriere to set
	 */
	public void setListaNoteTesoriere(List<NoteTesoriere> listaNoteTesoriere) {
		this.listaNoteTesoriere = listaNoteTesoriere;
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
	 * @return the listaStatoOperativoDocumento
	 */
	public List<StatoOperativoDocumento> getListaStatoOperativoDocumento() {
		return listaStatoOperativoDocumento;
	}

	/**
	 * @param listaStatoOperativoDocumento the listaStatoOperativoDocumento to set
	 */
	public void setListaStatoOperativoDocumento(
			List<StatoOperativoDocumento> listaStatoOperativoDocumento) {
		this.listaStatoOperativoDocumento = listaStatoOperativoDocumento;
	}
	
	@Override
	public String getSuffisso() {
		return suffisso;
	}

	/**
	 * @param suffisso the suffisso to set
	 */
	public void setSuffisso(String suffisso) {
		this.suffisso = suffisso;
	}
	
	/**
	 * @return the quietanza
	 */
	public Boolean getQuietanza() {
		return quietanza;
	}

	/**
	 * @param quietanza the quietanza to set
	 */
	public void setQuietanza(Boolean quietanza) {
		this.quietanza = quietanza;
	}

	/**
	 * @return the listaStatoSDIDocumento
	 */
	public List<StatoSDIDocumento> getListaStatoSDIDocumento() {
		return this.listaStatoSDIDocumento;
	}

	/**
	 * @param listaStatoSDIDocumento the listaStatoSDIDocumento to set
	 */
	public void setListaStatoSDIDocumento(List<StatoSDIDocumento> listaStatoSDIDocumento) {
		this.listaStatoSDIDocumento = listaStatoSDIDocumento != null ? listaStatoSDIDocumento : new ArrayList<StatoSDIDocumento>();
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @param tipoFamigliaDocumento la famiglia del documento (Entrata / Spesa)
	 * @param flagSubordinato       se il documento &eacute; subordinato
	 * @param flagRegolarizzazione  se il documento &eacute; una regolarizzazione
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		RicercaTipoDocumento request = new RicercaTipoDocumento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setFlagRegolarizzazione(flagRegolarizzazione);
		request.setFlagSubordinato(flagSubordinato);
		request.setRichiedente(getRichiedente());
		request.setTipoFamDoc(tipoFamigliaDocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodiceBollo}.
	 * 
	 * @return la request creata
	 */
	public RicercaCodiceBollo creaRequestRicercaCodiceBollo() {
		RicercaCodiceBollo request = new RicercaCodiceBollo();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoImpresa}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoImpresa creaRequestRicercaTipoImpresa() {
		RicercaTipoImpresa request = new RicercaTipoImpresa();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setAnno(getAnnoEsercizioInt());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoAvviso}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoAvviso creaRequestRicercaTipoAvviso() {
		RicercaTipoAvviso request = new RicercaTipoAvviso();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setAnno(getAnnoEsercizioInt());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNaturaOnere}.
	 * 
	 * @return la request creata
	 */
	public RicercaNaturaOnere creaRequestRicercaNaturaOnere() {
		RicercaNaturaOnere request = new RicercaNaturaOnere();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoOnere}.
	 * 
	 * @param naturaOnere la natura onere tramite cui filtrare il tipo
	 * 
	 * @return la request creata
	 */
	public RicercaTipoOnere creaRequestRicercaTipoOnere(NaturaOnere naturaOnere) {
		RicercaTipoOnere request = new RicercaTipoOnere();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setNaturaOnere(naturaOnere);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAttivitaOnere}.
	 * 
	 * @param tipoOnere il tipo onere tramite cui filtrare l'attivit&agrave;
	 * 
	 * @return la request creata
	 */
	public RicercaAttivitaOnere creaRequestRicercaAttivitaOnere(TipoOnere tipoOnere) {
		RicercaAttivitaOnere request = new RicercaAttivitaOnere();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setTipoOnere(tipoOnere);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCausale770}.
	 * 
	 * @param tipoOnere il tipo onere tramite cui filtrare l'attivit&agrave;
	 * 
	 * @return la request creata
	 */
	public RicercaCausale770 creaRequestRicercaCausale770(TipoOnere tipoOnere) {
		RicercaCausale770 request = new RicercaCausale770();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setTipoOnere(tipoOnere);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNoteTesoriere}.
	 * 
	 * @return la request creata
	 */
	public RicercaNoteTesoriere creaRequestRicercaNoteTesoriere() {
		RicercaNoteTesoriere request = creaRequest(RicercaNoteTesoriere.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaNoteTesoriere}.
	 * 
	 * @return la request creata
	 */
	public LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = creaRequest(LeggiContiTesoreria.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = new RicercaSoggettoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		request.setParametroSoggettoK(creaParametroSoggettoK(soggetto));
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = new RicercaProvvedimento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		
		// Injezione della utility di ricerca
		request.setRicercaAtti(creaUtilityRicercaAtti());
		
		return request;
	}
	
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un {@link ParametroRicercaSoggettoK}.
	 * 
	 * @param sogg il soggetto da cui ricavare l'utility
	 * 
	 * @return l'utility creata
	 */
	private ParametroRicercaSoggettoK creaParametroSoggettoK(Soggetto sogg) {
		ParametroRicercaSoggettoK utility = new ParametroRicercaSoggettoK();
		utility.setCodice(sogg.getCodiceSoggetto());
		return utility;
	}

	/**
	 * Reinizializza il campo netto.
	 */
	public void reinizializzaNetto() {
		impostaNetto(BigDecimal.ZERO);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoEntrata}.
	 * 
	 * @param uidDocumentoEntrata l'uid del documento di entrata
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDettaglioDocumentoEntrata creaRequestRicercaDettaglioDocumentoEntrata(Integer uidDocumentoEntrata) {
		RicercaDettaglioDocumentoEntrata request = new RicercaDettaglioDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumentoEntrata);
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioDocumentoSpesa}.
	 * 
	 * @param uidDocumentoSpesa l'uid del documento di spesa
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDettaglioDocumentoSpesa creaRequestRicercaDettaglioDocumentoSpesa(Integer uidDocumentoSpesa) {
		RicercaDettaglioDocumentoSpesa request = new RicercaDettaglioDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumentoSpesa);
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDocumentiCollegatiByDocumentoSpesa}.
	 * 
	 * @param uidDocumento l'uid del documento
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDocumentiCollegatiByDocumentoSpesa creaRequestRicercaDocumentiCollegatiByDocumentoSpesa(Integer uidDocumento) {
		RicercaDocumentiCollegatiByDocumentoSpesa request = new RicercaDocumentiCollegatiByDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumento);
		request.setDocumentoSpesa(documentoSpesa);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDocumentiCollegatiByDocumentoEntrata}.
	 * 
	 * @param uidDocumento l'uid del documento
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public RicercaDocumentiCollegatiByDocumentoEntrata creaRequestRicercaDocumentiCollegatiByDocumentoEntrata(Integer uidDocumento) {
		RicercaDocumentiCollegatiByDocumentoEntrata request = new RicercaDocumentiCollegatiByDocumentoEntrata();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumento);
		request.setDocumentoEntrata(documentoEntrata);
		return request;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link AggiornaRelazioneDocumenti}.
	 * 
	 * @param uidDocSpesaPadre l'uid del documento di spesa
	 * @param uidDocSpesaFiglio l'uid del documento di spesa
	 *  
	 * @return la request creata
	 * 
	 * @throws NullPointerException nel caso in cui l'uid fornito sia <code>null</code>
	 */
	public AggiornaRelazioneDocumenti creaRequestAggiornaRelazioneDocumentiSpesa(Integer uidDocSpesaPadre, Integer uidDocSpesaFiglio) {
		AggiornaRelazioneDocumenti request = new AggiornaRelazioneDocumenti();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		DocumentoSpesa docSpesaPadre = new DocumentoSpesa();
		docSpesaPadre.setUid(uidDocSpesaPadre);
		DocumentoSpesa docSpesaFiglio = new DocumentoSpesa();
		docSpesaFiglio.setUid(uidDocSpesaFiglio);
		
		request.setDocPadre(docSpesaPadre);
		request.setDocFiglio(docSpesaFiglio);
		request.setTipoRelazione(TipoRelazione.NOTA_CREDITO);
		
		return request;
	}
	
	
	
	/**
	 * Metodo di utilit&agrave; per la ricerca del provvedimento.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaAtti creaUtilityRicercaAtti() {
		RicercaAtti utility = new RicercaAtti();
		
		// Controllo che il provvedimento sia stato inizializzato
		if(attoAmministrativo != null) {
			// L'anno è obbligatorio, dunque lo injetto sempre
			utility.setAnnoAtto(attoAmministrativo.getAnno());
			
			int numeroAtto = attoAmministrativo.getNumero();
			String note = attoAmministrativo.getNote();
			String oggetto = attoAmministrativo.getOggetto();
			
			// Injetto il numero dell'atto se è stato inizializzato
			if(numeroAtto != 0) {
				utility.setNumeroAtto(numeroAtto);
			}
			
			// Injetto le note se sono state inizializzate
			if(StringUtils.isNotBlank(note)) {
				utility.setNote(note);
			}
			
			// Injetto l'oggetto se è stato inizializzato
			if(StringUtils.isNotBlank(oggetto)) {
				utility.setOggetto(oggetto);
			}
			
			// Injetto lo stato operativo se e solo se è stato inizializzato
			if(attoAmministrativo.getStatoOperativo() != null && !attoAmministrativo.getStatoOperativo().isEmpty()) {
				utility.setStatoOperativo(attoAmministrativo.getStatoOperativo());
			}
		}
		
		
		// Injetto il tipo di atto se e solo se è stato inizializzato
		if(tipoAtto != null && tipoAtto.getUid() != 0) {
			utility.setTipoAtto(tipoAtto);
		}
		
		// Injetto la struttura amministrativa contabile se e solo se è stata inizializzata
		if(strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0) {
			utility.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		}
		
		
		
		return utility;
	}
	
}
