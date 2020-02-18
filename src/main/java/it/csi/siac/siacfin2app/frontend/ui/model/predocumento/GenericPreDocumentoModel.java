/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;

/**
 * Classe di model generica per il PreDocumento di Spesa
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 *
 */
public class GenericPreDocumentoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8224877594681380300L;
	
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private TipoCausale tipoCausale;
	private Soggetto soggetto;
	private AttoAmministrativo attoAmministrativo;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabileAttoAmministrativo;	
	private Integer uidProvvedimento;
	
	//SIAC-6428
	private Ordinativo ordinativoSubCollegato;
	private Soggetto   soggettoOrdinativo;

	
	private List<TipoCausale> listaTipoCausale = new ArrayList<TipoCausale>();
	private List<CodificaFin> listaNazioni = new ArrayList<CodificaFin>();
	private List<ComuneNascita> listaComuni = new ArrayList<ComuneNascita>();
	private List<Sesso> listaSesso = new ArrayList<Sesso>();
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	
	//SIAC-7005
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
	// Per il decentrato
	private String nomeAzioneDecentrata;
	
	//aggiunti il 05/06/2015 ahmad
	private ProvvisorioDiCassa provvisorioCassa;
	
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
	 * @return the listaNazioni
	 */
	public List<CodificaFin> getListaNazioni() {
		return listaNazioni;
	}

	/**
	 * @param listaNazioni the listaNazioni to set
	 */
	public void setListaNazioni(List<CodificaFin> listaNazioni) {
		this.listaNazioni = listaNazioni != null ? listaNazioni : new ArrayList<CodificaFin>();
	}
	
	/**
	 * @param <C> il tipo della lista delle nazioni
	 * @param listaNazioni the listaNazioni to set
	 */
	public <C extends CodificaFin> void setListaNazioniFromExtended(List<C> listaNazioni) {
		this.listaNazioni = new ArrayList<CodificaFin>();
		this.listaNazioni.addAll(listaNazioni != null ? listaNazioni : new ArrayList<C>());
	}

	/**
	 * @return the listaComuni
	 */
	public List<ComuneNascita> getListaComuni() {
		return listaComuni;
	}

	/**
	 * @param listaComuni the listaComuni to set
	 */
	public void setListaComuni(List<ComuneNascita> listaComuni) {
		this.listaComuni = listaComuni != null ? listaComuni : new ArrayList<ComuneNascita>();
	}
	
	/**
	 * @return the listaSesso
	 */
	public List<Sesso> getListaSesso() {
		return listaSesso;
	}

	/**
	 * @param listaSesso the listaSesso to set
	 */
	public void setListaSesso(List<Sesso> listaSesso) {
		this.listaSesso = listaSesso != null ? listaSesso : new ArrayList<Soggetto.Sesso>();
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
		this.listaContoTesoreria = listaContoTesoreria != null? listaContoTesoreria : null;
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
	 * @return the provvisorioCassa
	 */
	public ProvvisorioDiCassa getProvvisorioCassa() {
		return provvisorioCassa;
	}

	/**
	 * @param provvisorioCassa the provvisorioCassa to set
	 */
	public void setProvvisorioCassa(ProvvisorioDiCassa provvisorioCassa) {
		this.provvisorioCassa = provvisorioCassa;
	}
	
	@Override
	public String getSuffisso() {
		return "AttoAmministrativo";
	}
	
	/**
	 * @return the datiRiferimentoSoggetto
	 */
	public String getDatiRiferimentoSoggetto() {
		Soggetto s = getSoggetto();
		if(s == null || StringUtils.isBlank(s.getCodiceSoggetto())) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(s.getCodiceSoggetto());
		if(StringUtils.isNotBlank(s.getDenominazione())) {
			sb.append(" - ");
			sb.append(s.getDenominazione());
		}
		if(StringUtils.isNotBlank(s.getCodiceFiscale())) {
			sb.append(" - ");
			sb.append(s.getCodiceFiscale());
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the datiRiferimentoAttoAmministrativo
	 */
	public String getDatiRiferimentoAttoAmministrativo() {
		AttoAmministrativo aa = getAttoAmministrativo();
		
		if(aa == null || aa.getAnno() == 0 || aa.getNumero() == 0 || aa.getUid() == 0) {
			return "";
		}
		TipoAtto ta = aa.getTipoAtto();
		StrutturaAmministrativoContabile sac = aa.getStrutturaAmmContabile();
		
		StringBuilder sb = new StringBuilder();
		sb.append(": ");
		sb.append(aa.getAnno());
		sb.append(" / ");
		sb.append(aa.getNumero());
		if(ta != null && ta.getUid() != 0 && StringUtils.isNotBlank(ta.getCodice())) {
			sb.append(" / ");
			sb.append(ta.getCodice());
		}
		if(sac != null && sac.getUid() != 0 && StringUtils.isNotBlank(sac.getCodice())) {
			sb.append(" / ");
			sb.append(sac.getCodice());
		}
		
		return sb.toString();
	}

	
	/* ***** Requests ***** */
	
	/**
	 * Crea una request per il servizio di {@link Liste}.
	 * 
	 * @param tipiLista i tipi di lista da cercare
	 * 
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
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @param tipoFamigliaDocumento il tipo della famiglia del documento
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) {
		RicercaTipoDocumento request = new RicercaTipoDocumento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setFlagRegolarizzazione(Boolean.FALSE);
		request.setFlagSubordinato(Boolean.FALSE);
		request.setRichiedente(getRichiedente());
		request.setTipoFamDoc(tipoFamigliaDocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ListaComuni}.
	 * 
	 * @param idStato l'id dello stato
	 * 
	 * @return la request creata
	 */
	public ListaComuni creaRequestListaComuni(String idStato) {
		ListaComuni request = creaRequest(ListaComuni.class);
		request.setDescrizioneComune("");
		request.setIdStato(idStato);
		return request;
	}
	
	/**
	 * Imposta la struttura amministrativo contabile se presente.
	 * 
	 * @return la Struttura, se presente
	 */
	protected StrutturaAmministrativoContabile impostaStrutturaAmministrativoContabile() {
		return (getStrutturaAmministrativoContabile() == null || getStrutturaAmministrativoContabile().getUid() == 0) ? 
				null : getStrutturaAmministrativoContabile();
	}
	
	/**
	 * Crea i parametri di paginazione per le ricerche sintetiche.
	 * 
	 * @return i parametri di paginazione
	 */
	protected ParametriPaginazione impostaParametriPaginazione() {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		// Taaaaanti elementi per non avere più di una pagina
		parametriPaginazione.setElementiPerPagina(10000);
		parametriPaginazione.setNumeroPagina(0);
		return parametriPaginazione;
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
			
			// Injetto il numero dell'atto se è stato inizializzato
			if(numeroAtto != 0) {
				utility.setNumeroAtto(numeroAtto);
			}
		}
		
		// Injetto il tipo di atto se e solo se è stato inizializzato
		if(getTipoAtto() != null && getTipoAtto().getUid() != 0) {
			utility.setTipoAtto(getTipoAtto());
		}
		
		// Injetto la struttura amministrativa contabile se e solo se è stata inizializzata
		if(getStrutturaAmministrativoContabileAttoAmministrativo() != null && getStrutturaAmministrativoContabileAttoAmministrativo().getUid() != 0) {
			utility.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabileAttoAmministrativo());
		}
		
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
	
	/**
	 * Compone la Stringa relativa al documento.
	 * @param <D> la tipizzazione del documento
	 * 
	 * @param doc                il documento
	 * @param tipoDocumento      il tipo di documento
	 * @param listaTipoDocumento la lista dei tipi di documento
	 * 
	 * @return la stringa del documento
	 */
	protected <D extends Documento<?, ?>> String componiStringaDocumento(D doc, TipoDocumento tipoDocumento, List<TipoDocumento> listaTipoDocumento) {
		TipoDocumento tipoDoc = ComparatorUtils.searchByUid(listaTipoDocumento, tipoDocumento);
		if(doc == null && tipoDoc == null) {
			return "";
		}
		
		String result = "";
		StringBuilder sbD = new StringBuilder();
		
		if(doc != null) {
			if(doc.getAnno() != null) {
				sbD.append(doc.getAnno());
			}
			if(StringUtils.isNotBlank(doc.getNumero())) {
				sbD.append("/").append(doc.getNumero());
			}
		}
		if(tipoDoc != null && tipoDoc.getUid() != 0) {
			sbD.append("/").append(tipoDoc.getCodice());
		}
		if(sbD.length() != 0) {
			result = "Documento: " + sbD.append(" - ").toString();
		}
		
		return result;
	}
	
	/**
	 * Compone la Stringa relativa all'ordinativo.
	 * 
	 * @return la stringa dell'ordinativo
	 */
	protected String componiStringaOrdinativo() {
		return "";
	}
	
	/**
	 * Popola il documento con i dati forniti, se esistenti.
	 * @param <D> la tipizzazione del documento
	 * 
	 * @param documento     il documento tramite cui popolare il risultato
	 * @param tipoDocumento il tipo di documento
	 * 
	 * @return il documento correttamente popolato, se presente; <code>null</code> in caso contrario
	 */
	protected <D extends Documento<?, ?>> D popolaDocumentoSeDatiPresenti(D documento, TipoDocumento tipoDocumento) {
		D result = null;
		
		if(documento.getAnno() != null || StringUtils.isNotBlank(documento.getNumero()) || impostaEntitaFacoltativa(tipoDocumento) != null) {
			result = documento;
			result.setTipoDocumento(impostaEntitaFacoltativa(tipoDocumento));
		}
		return result;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisoriDiCassa creaRequestRicercaProvvisorioDiCassa() {

		RicercaProvvisoriDiCassa ricercaProvvisoriDiCassa = new RicercaProvvisoriDiCassa();
		ricercaProvvisoriDiCassa.setEnte(getEnte());
		ricercaProvvisoriDiCassa.setBilancio(getBilancio());
		ricercaProvvisoriDiCassa.setRichiedente(getRichiedente());

		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		parametroRicercaProvvisorio.setAnno(provvisorioCassa.getAnno());
		parametroRicercaProvvisorio.setNumero(provvisorioCassa.getNumero());
		parametroRicercaProvvisorio.setTipoProvvisorio(provvisorioCassa.getTipoProvvisorioDiCassa());
		ricercaProvvisoriDiCassa.setParametroRicercaProvvisorio(parametroRicercaProvvisorio);

		return ricercaProvvisoriDiCassa;
	}
	
	/**
	 * Computa la denominazione del predocumento
	 * @param predoc il predoc la cui denominazione deve essere calcolata
	 * @return la denominazione
	 */
	protected String computeDenominazionePredocumento(PreDocumento<?, ?> predoc) {
		if(predoc == null || predoc.getDatiAnagraficiPreDocumento() == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		
		computeRagioneSociale(chunks, predoc.getDatiAnagraficiPreDocumento());
		computeNomeCognome(chunks, predoc.getDatiAnagraficiPreDocumento());
		computeDatiNascita(chunks, predoc.getDatiAnagraficiPreDocumento());
		
		return StringUtils.join(chunks, "<br/>");
	}

	/**
	 * Computa la ragione sociale
	 * @param chunks la lista in cui appendere i dati
	 * @param datiAnagraficiPreDocumento i dati anagrafici del predocumento
	 */
	private void computeRagioneSociale(List<String> chunks, DatiAnagraficiPreDocumento datiAnagraficiPreDocumento) {
		if(StringUtils.isNotBlank(datiAnagraficiPreDocumento.getRagioneSociale())) {
			StringBuilder sb = new StringBuilder()
				.append("<b>Ragione sociale:</b>&nbsp;")
				.append(datiAnagraficiPreDocumento.getRagioneSociale());
			chunks.add(sb.toString());
		}
	}
	
	/**
	 * Computa nome e cognome
	 * @param chunks la lista in cui appendere i dati
	 * @param datiAnagraficiPreDocumento i dati anagrafici del predocumento
	 */
	private void computeNomeCognome(List<String> chunks, DatiAnagraficiPreDocumento datiAnagraficiPreDocumento) {
		if(StringUtils.isNotBlank(datiAnagraficiPreDocumento.getCognome())) {
			StringBuilder sb = new StringBuilder()
				.append("<b>Cognome:</b>&nbsp;")
				.append(datiAnagraficiPreDocumento.getCognome());
			chunks.add(sb.toString());
		}
		if(StringUtils.isNotBlank(datiAnagraficiPreDocumento.getNome())) {
			StringBuilder sb = new StringBuilder()
				.append("<b>Nome:</b>&nbsp;")
				.append(datiAnagraficiPreDocumento.getNome());
			chunks.add(sb.toString());
		}
	}
	
	/**
	 * Computa i dati di nascita
	 * @param chunks la lista in cui appendere i dati
	 * @param datiAnagraficiPreDocumento i dati anagrafici del predocumento
	 */
	private void computeDatiNascita(List<String> chunks, DatiAnagraficiPreDocumento datiAnagraficiPreDocumento) {
		boolean hasDataNascita = false;
		StringBuilder sb = new StringBuilder();
		if(datiAnagraficiPreDocumento.getDataNascita() != null) {
			hasDataNascita = true;
			sb.append("<b>Nato il</b>&nbsp;")
				.append(FormatUtils.formatDate(datiAnagraficiPreDocumento.getDataNascita()));
		}
		if(StringUtils.isNotBlank(datiAnagraficiPreDocumento.getComuneNascita())) {
			if(hasDataNascita) {
				sb.append("<span class=\"alLeft\">&nbsp;<b>A</b></span>&nbsp;");
			} else {
				sb.append("<b>Nato a</b></span>&nbsp;");
			}
			sb.append(datiAnagraficiPreDocumento.getComuneNascita());
		}
		if(sb.length() > 0) {
			chunks.add(sb.toString());
		}
	}
	
	/**
	 * Calcola i dati di riferimento del movimento di gestione
	 * @param mg il movimento di gestione
	 * @param smg il submovimento di gestione
	 * @return the datiRiferimentoMovimentoGestione
	 */
	protected String computeDatiRiferimentoMovimentoGestione(MovimentoGestione mg, MovimentoGestione smg) {
		if(mg == null || mg.getAnnoMovimento() == 0 || mg.getNumero() == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(": ");
		sb.append(mg.getAnnoMovimento());
		sb.append(" / ");
		sb.append(FormatUtils.formatPlain(mg.getNumero()));
		if(smg != null && smg.getNumero() != null) {
			sb.append(" - ");
			sb.append(FormatUtils.formatPlain(smg.getNumero()));
		}
		
		return sb.toString();
	}
	

	/**
	 * @return the soggettoOrdinativo
	 */
	public Soggetto getSoggettoOrdinativo() {
		return soggettoOrdinativo;
	}

	/**
	 * @param soggettoOrdinativo the soggettoOrdinativo to set
	 */
	public void setSoggettoOrdinativo(Soggetto soggettoOrdinativo) {
		this.soggettoOrdinativo = soggettoOrdinativo;
	}

	/**
	 * @return the ordinativoSubCollegato
	 */
	public Ordinativo getOrdinativoSubCollegato() {
		return ordinativoSubCollegato;
	}

	/**
	 * @param ordinativoSubCollegato the ordinativoSubCollegato to set
	 */
	public void setOrdinativoSubCollegato(Ordinativo ordinativoSubCollegato) {
		this.ordinativoSubCollegato = ordinativoSubCollegato;
	}

	
	
}
