/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe astratta di model per la Causale.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 15/04/2014
 *
 */
public abstract class GenericCausaleModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4948730122493670234L;

	// Dati struttura 
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private TipoCausale tipoCausale;
	
	// Blocco sezione IMPUTAZIONI CONTABILI
	private Soggetto soggetto;
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	
	private AttoAmministrativo attoAmministrativo;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileAttoAmministrativo;
	private TipoAtto tipoAtto;
	
	private Integer uidProvvedimento;
	
	// Per il decentrato
	private String nomeAzioneDecentrata;
	
	// liste
	private List<TipoCausale> listaTipoCausale = new ArrayList<TipoCausale>();
	private List<StatoOperativoCausale> listaStatiCausale = new ArrayList<StatoOperativoCausale>();
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	
	/**
	 * @return the strutturaAmministrativoContabileAttoAmministrativo
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabileAttoAmministrativo() {
		return strutturaAmministrativoContabileAttoAmministrativo;
	}

	/**
	 * @param strutturaAmministrativoContabileAttoAmministrativo the strutturaAmministrativoContabileAttoAmministrativo to set
	 */
	public void setStrutturaAmministrativoContabileAttoAmministrativo(
			StrutturaAmministrativoContabile strutturaAmministrativoContabileAttoAmministrativo) {
		this.strutturaAmministrativoContabileAttoAmministrativo = strutturaAmministrativoContabileAttoAmministrativo;
	}

	/**
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale() {
		return tipoCausale;
	}

	/**
	 * @param tipoCausale the tipoCausale to set
	 */
	public void setTipoCausale(TipoCausale tipoCausale) {
		this.tipoCausale = tipoCausale;
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
	 * @return the sedeSecondariaSoggetto
	 */
	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	/**
	 * @param sedeSecondariaSoggetto the sedeSecondariaSoggetto to set
	 */
	public void setSedeSecondariaSoggetto(
			SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
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
	 * @return the nomeAzioneDecentrata
	 */
	public String getNomeAzioneDecentrata() {
		return nomeAzioneDecentrata;
	}

	/**
	 * @param nomeAzioneDecentrata the nomeAzioneDecentrata to set
	 */
	public void setNomeAzioneDecentrata(String nomeAzioneDecentrata) {
		this.nomeAzioneDecentrata = nomeAzioneDecentrata;
	}

	/**
	 * @return the listaTipoCausale
	 */
	public List<TipoCausale> getListaTipoCausale() {
		return listaTipoCausale;
	}

	/**
	 * @param listaTipoCausale the listaTipoCausale to set
	 */
	public void setListaTipoCausale(List<TipoCausale> listaTipoCausale) {
		this.listaTipoCausale = listaTipoCausale != null ? listaTipoCausale : new ArrayList<TipoCausale>();
	}

	/**
	 * @return the listaStatiCausale
	 */
	public List<StatoOperativoCausale> getListaStatiCausale() {
		return listaStatiCausale;
	}

	/**
	 * @param listaStatiCausale the listaStatiCausale to set
	 */
	public void setListaStatiCausale(List<StatoOperativoCausale> listaStatiCausale) {
		this.listaStatiCausale = listaStatiCausale != null ? listaStatiCausale : new ArrayList<StatoOperativoCausale>();
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
	 * @return the listaSedeSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}

	/**
	 * @param listaSedeSecondariaSoggetto the listaSedeSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = listaSedeSecondariaSoggetto != null ? listaSedeSecondariaSoggetto : new ArrayList<SedeSecondariaSoggetto>();
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto != null ? listaModalitaPagamentoSoggetto : new ArrayList<ModalitaPagamentoSoggetto>();
	}
	
	@Override
	public String getSuffisso() {
		return "AttoAmministrativo";
	}

	/* Requests */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @param sogg il soggetto da ricercare
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto sogg) {
		RicercaSoggettoPerChiave request = new RicercaSoggettoPerChiave();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		
		request.setParametroSoggettoK(parametroRicercaSoggettoK);
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
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Tipi Causale Spesa.
	 * 
	 * @return la request creata
	 */
	public LeggiTipiCausaleSpesa creaRequestLeggiTipiCausaleSpesa() {
		LeggiTipiCausaleSpesa request = new LeggiTipiCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Tipi Causale Entrata.
	 * 
	 * @return la request creata
	 */
	public LeggiTipiCausaleEntrata creaRequestLeggiTipiCausaleEntrata() {
		LeggiTipiCausaleEntrata request = new LeggiTipiCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setEnte(getEnte());
		
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
		if(getAttoAmministrativo() != null) {
			// L'anno è obbligatorio, dunque lo injetto sempre
			utility.setAnnoAtto(getAttoAmministrativo().getAnno());
			
			int numeroAtto = getAttoAmministrativo().getNumero();
			String note = getAttoAmministrativo().getNote();
			String oggetto = getAttoAmministrativo().getOggetto();
			
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
		}
		
		// Injetto il tipo di atto se e solo se è stato inizializzato
		utility.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		utility.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabileAttoAmministrativo()));
		
		return utility;
	}
		
	/**
	 * Imposta i dati dell'atto amministrativo all'interno del model.
	 * 
	 * @param attoAmministrativo l'atto i cui dati devono essere impostati
	 */
	public void impostaAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		setAttoAmministrativo(attoAmministrativo);
		if(attoAmministrativo != null) {
			setTipoAtto(attoAmministrativo.getTipoAtto());
			setStrutturaAmministrativoContabileAttoAmministrativo(attoAmministrativo.getStrutturaAmmContabile());
		}
	}
}
