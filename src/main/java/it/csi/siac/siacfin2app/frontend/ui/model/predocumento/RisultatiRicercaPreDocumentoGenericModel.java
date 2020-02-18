/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Model generico per la visualizzazione dei risultati di ricerca per il PreDocumento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 06/05/2014
 * 
 */
public class RisultatiRicercaPreDocumentoGenericModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5160130548654899393L;
	
	// Property necessarie per pilotare la dataTable con  il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private int uidDaAggiornare;
	private int uidDaAnnullare;
	private int uidDaConsultare;
	
	private String riepilogoRicerca;
	
	private List<Integer> listaUid = new ArrayList<Integer>();
	
	private Boolean inviaTutti = Boolean.FALSE;
	
	private Integer idAzioneAsync;
	private Boolean faseBilancioAbilitata;
	private Boolean inserisciAbilitato;
	private Boolean associaAbilitato;
	private Boolean definisciAbilitato;
	
	// SIAC-4280
	private Boolean modificaAssociazioniContabiliAbilitato;
	private Soggetto soggetto;
	private AttoAmministrativo attoAmministrativo;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileAttoAmministrativo;
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	
	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the uidDaAggiornare
	 */
	public int getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}

	/**
	 * @return the uidDaAnnullare
	 */
	public int getUidDaAnnullare() {
		return uidDaAnnullare;
	}

	/**
	 * @param uidDaAnnullare the uidDaAnnullare to set
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		this.uidDaAnnullare = uidDaAnnullare;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public int getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}
	
	/**
	 * @return the riepilogoRicerca
	 */
	public String getRiepilogoRicerca() {
		return riepilogoRicerca;
	}

	/**
	 * @param riepilogoRicerca the riepilogoRicerca to set
	 */
	public void setRiepilogoRicerca(String riepilogoRicerca) {
		this.riepilogoRicerca = riepilogoRicerca;
	}
	
	/**
	 * @return the listaUidDaCompletare
	 */
	public List<Integer> getListaUid() {
		return listaUid;
	}

	/**
	 * @param listaUid the listaUidDaCompletare to set
	 */
	public void setListaUid(List<Integer> listaUid) {
		this.listaUid = listaUid != null ? listaUid : new ArrayList<Integer>();
	}

	/**
	 * @return the inviaTutti
	 */
	public Boolean getInviaTutti() {
		return inviaTutti;
	}

	/**
	 * @param inviaTutti the inviaTutti to set
	 */
	public void setInviaTutti(Boolean inviaTutti) {
		this.inviaTutti = inviaTutti != null ? inviaTutti : Boolean.FALSE;
	}
	
	/**
	 * @return the idAzioneAsync
	 */
	public Integer getIdAzioneAsync() {
		return idAzioneAsync;
	}

	/**
	 * @param idAzioneAsync the idAzioneAsync to set
	 */
	public void setIdAzioneAsync(Integer idAzioneAsync) {
		this.idAzioneAsync = idAzioneAsync;
	}

	/**
	 * @return the faseBilancioAbilitata
	 */
	public Boolean getFaseBilancioAbilitata() {
		return faseBilancioAbilitata;
	}

	/**
	 * @param faseBilancioAbilitata the faseBilancioAbilitata to set
	 */
	public void setFaseBilancioAbilitata(Boolean faseBilancioAbilitata) {
		this.faseBilancioAbilitata = faseBilancioAbilitata;
	}

	/**
	 * @return the inserisciAbilitato
	 */
	public Boolean getInserisciAbilitato() {
		return inserisciAbilitato;
	}

	/**
	 * @param inserisciAbilitato the inserisciAbilitato to set
	 */
	public void setInserisciAbilitato(Boolean inserisciAbilitato) {
		this.inserisciAbilitato = inserisciAbilitato;
	}

	/**
	 * @return the associaAbilitato
	 */
	public Boolean getAssociaAbilitato() {
		return associaAbilitato;
	}

	/**
	 * @param associaAbilitato the associaAbilitato to set
	 */
	public void setAssociaAbilitato(Boolean associaAbilitato) {
		this.associaAbilitato = associaAbilitato;
	}

	/**
	 * @return the definisciAbilitato
	 */
	public Boolean getDefinisciAbilitato() {
		return definisciAbilitato;
	}

	/**
	 * @param definisciAbilitato the definisciAbilitato to set
	 */
	public void setDefinisciAbilitato(Boolean definisciAbilitato) {
		this.definisciAbilitato = definisciAbilitato;
	}

	/**
	 * @return the modificaAssociazioniContabiliAbilitato
	 */
	public Boolean getModificaAssociazioniContabiliAbilitato() {
		return modificaAssociazioniContabiliAbilitato;
	}

	/**
	 * @param modificaAssociazioniContabiliAbilitato the modificaAssociazioniContabiliAbilitato to set
	 */
	public void setModificaAssociazioniContabiliAbilitato(Boolean modificaAssociazioniContabiliAbilitato) {
		this.modificaAssociazioniContabiliAbilitato = modificaAssociazioniContabiliAbilitato;
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
	
	@Override
	public String getSuffisso() {
		return "AttoAmministrativo";
	}

	/**
	 * Crea una lista di PreDocumenti a partire dalla lista degli uid degli stessi.
	 * @param <PD> la tipizzazione del predocumento
	 * 
	 * @param uids la lista degli uid dei preDocumenti
	 * @param clazz    la classe concreta del preDocumento da ottenere
	 * 
	 * @return la lista dei preDocumenti aventi uid dato
	 */
	protected <PD extends PreDocumento<?, ?>> List<PD> creaPreDocumentiDaListaUid(List<Integer> uids, Class<PD> clazz) {
		List<PD> result = new ArrayList<PD>();
		
		for(Integer uid : uids) {
			PD pd = ReflectionUtil.silentlyBuildInstance(clazz);
			pd.setUid(uid);
			result.add(pd);
		}
		
		return result;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
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
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		
		request.setEnte(getEnte());
		request.setRicercaAtti(creaUtilityRicercaAtti());
		
		return request;
	}

	/**
	 * Metodo di utilit&agrave; per la ricerca del provvedimento.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaAtti creaUtilityRicercaAtti() {
		RicercaAtti ricercaAtti = new RicercaAtti();
		
		// Controllo che il provvedimento sia stato inizializzato
		if(getAttoAmministrativo() != null) {
			// L'anno e' obbligatorio, dunque lo injetto sempre
			ricercaAtti.setAnnoAtto(Integer.valueOf(getAttoAmministrativo().getAnno()));
			
			int numeroAtto = getAttoAmministrativo().getNumero();
			
			// Injetto il numero dell'atto se e' stato inizializzato
			if(numeroAtto != 0) {
				ricercaAtti.setNumeroAtto(Integer.valueOf(numeroAtto));
			}
		}
		
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabileAttoAmministrativo()));
		return ricercaAtti;
	}
	
	/**
	 * Calcola il riepilogo delle imputazioni contabili per il capitolo
	 * @param capitolo il capitolo
	 * @return il riepilogo del capitolo
	 */
	protected String calcolaRiepilogoImputazioniContabiliCapitolo(Capitolo<?, ?> capitolo) {
		if(capitolo == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append("<strong>Capitolo</strong>: ")
			.append(capitolo.getNumeroCapitolo())
			.append("/")
			.append(capitolo.getNumeroArticolo());
		if(isGestioneUEB()) {
			sb.append("/")
				.append(capitolo.getNumeroUEB());
		}
		return sb.append("<br/>")
				.toString();
	}
	
	/**
	 * Calcola il riepilogo delle imputazioni contabili per il movimento di gestione
	 * @param movimentoGestione il movimento di gestione
	 * @param submovimentoGestione il submovimento di gestione
	 * @param intestazione l'intestazione
	 * @return il riepilogo del movimento di gestione
	 */
	protected String calcolaRiepilogoImputazioniContabiliMovimentoGestione(MovimentoGestione movimentoGestione, MovimentoGestione submovimentoGestione, String intestazione) {
		if(movimentoGestione == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder()
			.append("<strong>")
			.append(intestazione)
			.append("</strong>: ");
		if(movimentoGestione.getNumero() != null) {
			sb.append("/")
				.append(movimentoGestione.getNumero().toPlainString());
		}
		if(submovimentoGestione != null && submovimentoGestione.getNumero() != null) {
			sb.append("-")
				.append(submovimentoGestione.getNumero().toPlainString());
		}
		return sb.append("<br/>")
			.toString();
	}
	
	/**
	 * Calcola il riepilogo delle imputazioni contabili per il soggetto
	 * @param sogg il soggetto
	 * @return il riepilogo del soggetto
	 */
	protected String calcolaRiepilogoImputazioniContabiliSoggetto(Soggetto sogg) {
		if(sogg == null) {
			return "";
		}
		return new StringBuilder()
				.append("<strong>Soggetto</strong>: ")
				.append(sogg.getCodiceSoggetto())
				.append(" - ")
				.append(sogg.getDenominazione())
				.append("<br/>")
				.toString();
	}
	
	/**
	 * Calcola il riepilogo delle imputazioni contabili per l'atto amministrativo
	 * @param provvedimento l'atto amministrativo
	 * @return il riepilogo dell'atto amministrativo
	 */
	protected String calcolaRiepilogoImputazioniContabiliAttoAmministrativo(AttoAmministrativo provvedimento) {
		if(provvedimento == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append("<strong>Provvedimento</strong>: ")
			.append(provvedimento.getAnno())
			.append("/")
			.append(provvedimento.getNumero());
		if(provvedimento.getTipoAtto() != null) {
			sb.append("/")
				.append(provvedimento.getTipoAtto().getCodice());
		}
		if(provvedimento.getTipoAtto() != null) {
			sb.append("/")
				.append(provvedimento.getTipoAtto().getCodice());
		}
		if(provvedimento.getStrutturaAmmContabile() != null) {
			sb.append("/")
			.append(provvedimento.getStrutturaAmmContabile().getCodice());
		}
		return sb.append("<br/>")
				.toString();
	}
	
}
