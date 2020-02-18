/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.model.GenericModel;
import it.csi.siac.siaccorser.frontend.webservice.msg.FindAzione;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.PaginazioneRequest;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Model per la CapUscitaPrevisione
 * 
 * @author AR
 * @author Domenico Lisi
 * @author Alessandro Marchino
 * @version 1.0.1 05/07/2013
 * 
 */
public abstract class GenericBilancioModel extends GenericModel {

	private static final long serialVersionUID = -7512424620663569220L;

	// FIXME: configurabile
	/** Gli elementi per pagina nella ricerca back-end */
	public static final int ELEMENTI_PER_PAGINA_RICERCA = 10;
	
	private String annoEsercizio;
	
	private Bilancio bilancio;
	private Ente ente;
	private Richiedente richiedente;
	private AzioneRichiesta azioneRichiesta;
	
	/**
	 * @return the annoEsercizio
	 */
	public String getAnnoEsercizio() {
		return annoEsercizio;
	}

	/**
	 * @param annoEsercizio the annoEsercizio to set
	 */
	public void setAnnoEsercizio(String annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}

	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	/**
	 * @return the richiedente
	 */
	public Richiedente getRichiedente() {
		return richiedente;
	}

	/**
	 * @param richiedente the richiedente to set
	 */
	public void setRichiedente(Richiedente richiedente) {
		this.richiedente = richiedente;
	}
	
	/**
	 * @return the operatore
	 */
	public Operatore getOperatore() {
		return getRichiedente() != null ? getRichiedente().getOperatore() : null;
	}
	
	/**
	 * @return the azioneRichiesta
	 */
	public AzioneRichiesta getAzioneRichiesta() {
		return azioneRichiesta;
	}

	/**
	 * @param azioneRichiesta the azioneRichiesta to set
	 */
	public void setAzioneRichiesta(AzioneRichiesta azioneRichiesta) {
		this.azioneRichiesta = azioneRichiesta;
	}

	/**
	 * Restituisce il suffisso per l'azione. Inizializzato a stringa vuota.
	 * 
	 * @return il suffisso
	 */
	public String getSuffisso() {
		return "";
	}
	
	/**
	 * @return the dataOdierna
	 */
	public Date getDataOdierna() {
		return new Date();
	}
	
	/* Requests comuni */

	/**
	 * Creazione di una Request per il servizio di Leggi Classificatori Generici By Tipo Elemento BIL
	 * 
	 * @param tipoElementoBilancio	il tipo dell'Elemento da richiedere
	 * @return						la Request creata
	 */
	public LeggiClassificatoriGenericiByTipoElementoBil creaRequestLeggiClassificatoriGenericiByTipoElementoBil(String tipoElementoBilancio) {
		LeggiClassificatoriGenericiByTipoElementoBil request = new LeggiClassificatoriGenericiByTipoElementoBil();
		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setDataOra(new Date());
		request.setIdEnteProprietario(ente.getUid());
		request.setRichiedente(richiedente);
		request.setTipoElementoBilancio(tipoElementoBilancio);
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Classificatori 
	 * By Tipo Elemento BIL.
	 * 
	 * @param tipoElementoBilancio	il tipo dell'Elemento da richiedere
	 * @return						la Request creata
	 */
	public LeggiClassificatoriByTipoElementoBil creaRequestLeggiClassificatoriByTipoElementoBil(String tipoElementoBilancio) {
		LeggiClassificatoriByTipoElementoBil request = creaRequest(LeggiClassificatoriByTipoElementoBil.class);

		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setIdEnteProprietario(ente.getUid());
		request.setTipoElementoBilancio(tipoElementoBilancio);
		
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Classificatori
	 * BIL By Id Padre.
	 * 
	 * @param idPadre	id del Classificatore padre
	 * @return			la request creata
	 */
	public LeggiClassificatoriBilByIdPadre creaRequestLeggiClassificatoriBilByIdPadre(int idPadre) {
		LeggiClassificatoriBilByIdPadre request = creaRequest(LeggiClassificatoriBilByIdPadre.class);

		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setIdEnteProprietario(ente.getUid());
		request.setIdPadre(idPadre);
		
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Classificatori By Relazione.
	 * 
	 * @param idPadre id del Classificatore padre
	 * 
	 * @return la request creata
	 */
	public LeggiClassificatoriByRelazione creaRequestLeggiClassificatoriByRelazione(Integer idPadre) {
		LeggiClassificatoriByRelazione request = new LeggiClassificatoriByRelazione();
		request.setDataOra(new Date());
		request.setEnte(ente);
		request.setIdClassif(idPadre);
		request.setRichiedente(richiedente);
		request.setAnno(getAnnoEsercizioInt());
		return request;
	}

	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Tree Piano dei Conti.
	 * 
	 * @param idPadre	id del Classificatore padre
	 * @return			la request creata
	 */
	public LeggiTreePianoDeiConti 
		creaRequestLeggiTreePianoDeiConti(int idPadre) {
		LeggiTreePianoDeiConti request = new LeggiTreePianoDeiConti();
		request.setAnno(getAnnoEsercizioInt());
		request.setDataOra(new Date());
		request.setIdCodificaPadre(idPadre);
		request.setIdEnteProprietario(ente.getUid());
		request.setRichiedente(richiedente);
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Tree SIOPE.
	 * 
	 * @param idPadre	id del Classificatore padre
	 * @return			la request creata
	 */
	public LeggiTreeSiope creaRequestLeggiTreeSiope(int idPadre) {
		LeggiTreeSiope request = new LeggiTreeSiope();
		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setDataOra(new Date());
		request.setIdCodificaPadre(idPadre);
		request.setIdEnteProprietario(ente.getUid());
		request.setRichiedente(richiedente);
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di get Lista Tipi di Atto
	 * 
	 * @return			la request creata
	 */
	public RicercaTipiAttoDiLegge 
			creaRequestRicercaTipiAttoDiLegge() {
		RicercaTipiAttoDiLegge request = new RicercaTipiAttoDiLegge();
		request.setDataOra(new Date());
		request.setEnte(ente);
		request.setRichiedente(richiedente);
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Struttura Amministrativo
	 * Contabile.
	 * 
	 * @return la request creata
	 */
	public LeggiStrutturaAmminstrativoContabile creaRequestLeggiStrutturaAmminstrativoContabile() {
		LeggiStrutturaAmminstrativoContabile request = new LeggiStrutturaAmminstrativoContabile();
		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setDataOra(new Date());
		request.setIdEnteProprietario(ente.getUid());
		request.setIdFamigliaTree(BilConstants.ID_FAMIGLIA_TREE_STRUTTURA_AMMINISTRATIVA_CONTABILE.getId());
		request.setRichiedente(richiedente);
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Calcola Totali 
	 * Stanziamenti Di Previsione.
	 * 
	 * @return la request creata
	 */
	public CalcolaTotaliStanziamentiDiPrevisione creaRequestCalcolaTotaliStanziamentiDiPrevisione() {
		CalcolaTotaliStanziamentiDiPrevisione request = new CalcolaTotaliStanziamentiDiPrevisione();
		request.setAnnoEsercizio(Integer.parseInt(annoEsercizio));
		request.setDataOra(new Date());
		request.setRichiedente(richiedente);
		request.setBilancio(getBilancio());
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Tipi Provvedimento.
	 * 
	 * @return la request creata
	 */
	public TipiProvvedimento creaRequestTipiProvvedimento() {
		TipiProvvedimento request = creaRequest(TipiProvvedimento.class);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Ricerca Categoria Capitolo.
	 * @param tipoCapitolo il tipo di capitolo
	 * 
	 * @return la request creata
	 */
	public RicercaCategoriaCapitolo creaRequestRicercaCategoriaCapitolo(TipoCapitolo tipoCapitolo) {
		RicercaCategoriaCapitolo request = new RicercaCategoriaCapitolo();
		
		request.setDataOra(new Date());
		request.setEnte(ente);
		request.setRichiedente(richiedente);
		request.setTipoCapitolo(tipoCapitolo);
		
		return request;
	}
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Ricerca Dettaglio Bilancio.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioBilancio creaRequestRicercaDettaglioBilancio() {
		RicercaDettaglioBilancio request = new RicercaDettaglioBilancio();
		
		request.setDataOra(new Date());
		request.setRichiedente(richiedente);
		
		request.setChiaveBilancio(getBilancio().getUid());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ListeGestioneSoggetto}.
	 * 
	 * @return la request creata
	 */
	public ListeGestioneSoggetto creaRequestListeGestioneSoggetto() {
		return creaRequest(ListeGestioneSoggetto.class);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodifiche}.
	 * @param codifiche le codifice da injettare. Accetta classi e stringhe
	 * 
	 * @return la request creata
	 */
	public RicercaCodifiche creaRequestRicercaCodifiche(Object... codifiche) {
		RicercaCodifiche request = creaRequest(RicercaCodifiche.class);
		request.addCodifiche(codifiche);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link FindAzione}.
	 * @param nome il nome da ricercare
	 * @return la request creata
	 */
	public FindAzione creaRequestFindAzione(String nome) {
		FindAzione request = creaRequest(FindAzione.class);
		request.setEnte(ente);
		request.setNomeAzione(nome);
		return request;
	}
	
	/**
	 * Crea un'istanza della request.
	 * @param <R> la tipizzazione della request
	 * 
	 * @param clazz la classe della request
	 * 
	 * @return la request con data e richiedente compilati
	 * 
	 * @throws IllegalArgumentException nel caso in cui non sia possibile instanziare la request
	 */
	protected <R extends ServiceRequest> R creaRequest(Class<R> clazz) {
		R request = ReflectionUtil.silentlyBuildInstance(clazz);
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		request.setAnnoBilancio(bilancio.getAnno());
		return request;
	}
	
	/* **** Metodi di utilita **** */
	
	/**
	 * Restituisce l'anno di esercizio cui il model si riferisce come intero.
	 * 
	 * @return l'anno di esercizio
	 */
	public Integer getAnnoEsercizioInt() {
		Integer anno = null;
		try {
			anno = Integer.valueOf(annoEsercizio);
		} catch(NumberFormatException e) {
			throw new GenericFrontEndMessagesException("Formato dell'anno esercizio non valido");
		}
		return anno;
	}
	
	/**
	 * Imposta l'anno di esercizio cui il Model si riferisce a partire da un intero.
	 * 
	 * @param annoEsercizioInt l'anno di esercizio come Integer
	 */
	public void setAnnoEsercizioInt(Integer annoEsercizioInt) {
		annoEsercizio = String.valueOf(annoEsercizioInt);
	}
	
	/**
	 * oOntrolla se l'ente gestisca o meno il tipologia di livello con il codice fornito
	 * @param tipologiaGestioneLivelli la tipologia da controllare
	 * @param value il valore da verificare
	 * @return <code>true</code> se l'ente gestisce il livello con dato codice
	 */
	protected boolean isGestioneLivello(TipologiaGestioneLivelli tipologiaGestioneLivelli, BilConstants value) {
		return ente != null && ente.getGestioneLivelli() != null && value.getConstant().equals(ente.getGestioneLivelli().get(tipologiaGestioneLivelli));
	}
	
	/**
	 * Controlla se l'ente gestisce o meno le UEB.
	 * 
	 * @return <code>true</code> se l'ente gestisce le UEB; <code>false</code> in caso contrario
	 */
	public boolean isGestioneUEB() {
		return isGestioneLivello(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO, BilConstants.GESTIONE_UEB);
	}
	
	
	//SIAC-6888
	/**
	 * Controlla se l'ente gestisce o meno l'abilitazione dell'inserimento dell'accertamento automatico.
	 * 
	 * @return <code>true</code> se l'ente gestisce l'inserimento dell'accertamento automatico; <code>false</code> in caso contrario
	 */
	public boolean isGestioneAbiltatoAccertamentoAutomatico() {
		return isGestioneLivello(TipologiaGestioneLivelli.ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO, BilConstants.ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO);
	}
	//
	
	
	/**
	 * Controlla se l'ente gestisca o meno l'integrazione con HR.
	 * 
	 * @return <code>true</code> se l'ente gestisce l'integrazione con HR; <code>false</code> altrimenti
	 */
	public boolean isGestioneHR() {
		return isGestioneLivello(TipologiaGestioneLivelli.INTEGRAZIONE_HR, BilConstants.INTEGRAZIONE_HR);
	}
	
	/**
	 * Controlla il tipo di gestione dell'ente  e' automatico
	 * 
	 * @return <code>true</code> se la gestione e' automatica; <code>false</code> altrimenti
	 */
	public boolean isGestioneAutomaticaOrdinativo() {
		return isGestioneLivello(TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA, BilConstants.GESTIONE_CONVALIDA_AUTOMATICA);
	}
	/**
	 * Controlla il tipo di gestione dell'ente  e' Manuale
	 * 
	 * @return <code>true</code> se la gestione e' Manuale; <code>false</code> altrimenti
	 */
	public boolean isGestioneManualeOrdinativo() {
		return !isGestioneAutomaticaOrdinativo();
	}
	/**
	 * Restituisce il massimo valore per un campo di tipo Integer (o int).
	 * 
	 * @return the maxValueForIntegerField
	 */
	public int getMaxValueForIntegerField() {
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Imposta l'anno di esercizio nel getBilancio().
	 */
	public void impostaAnnoBilancio() {
		if(getBilancio() == null) {
			setBilancio(new Bilancio());
		}
		getBilancio().setAnno(getAnnoEsercizioInt());
	}
	
	/**
	 * Controlla se l'id dell'{@link Entita} sia presente.
	 * 
	 * @param e l'Entita di cui controllare l'id
	 * 
	 * @return <code>true</code> se l'Entita ha un id valorizzato correttamente; <code>false</code> altrimenti
	 */
	public boolean idEntitaPresente(Entita e) {
		return e != null && e.getUid() != 0;
	}
	
	/**
	 * Controlla se l'id dell'{@link Entita} sia presente. In tal caso, comunica con un log la classe e la presenza di tale uid.
	 * 
	 * @param e l'Entita di cui controllare l'id
	 * 
	 * @return <code>true</code> se l'Entita ha un id valorizzato correttamente; <code>false</code> altrimenti
	 */
	public boolean checkPresenzaIdEntita(Entita e){
		return idEntitaPresente(e);
	}
	
	/**
	 * Controlla se la Stringa &eacute; stata valorizzata in modo corretto.
	 * 
	 * @param s la stringa da controllare
	 * 
	 * @return <code>true</code> se la Stringa &eacute; non-null e non vuota; <code>false</code> altrimenti
	 */
	public boolean stringaValorizzata(String s){
		return StringUtils.trimToNull(s) != null;
	}
	
	/**
	 * Controlla se la Stringa &eacute; stata valorizzata in modo corretto. In tal caso, comunica con un log il nome della stringa.
	 * 
	 * @param s         la stringa da controllare
	 * @param nomeCampo il nome del campo riferentesi
	 * 
	 * @return <code>true</code> se la Stringa &eacute; non-null e non vuota; <code>false</code> altrimenti
	 */
	public boolean checkStringaValorizzata(String s, String nomeCampo){
		return stringaValorizzata(s);
	}
	
	/**
	 * Controlla se l'oggetto &eacute; stato valorizzato in modo corretto. In tal caso, comunica con un log il nome del campo.
	 * 
	 * @param campo     il campo da controllare
	 * @param nomeCampo il nome del campo riferentesi
	 * 
	 * @return <code>true</code> se l'oggetto &eacute; non-null; <code>false</code> altrimenti
	 */
	public boolean checkCampoValorizzato(Object campo, String nomeCampo){
		return campo != null;
	}
	
	/**
	 * Controlla se la condizione fornita sia rispettata. In tal caso, comunica con un log il nome del campo.
	 * 
	 * @param condition la condizione da controllare
	 * @param nomeCampo il nome del campo riferentesi
	 * 
	 * @return <code>true</code> se la condizione &eacute; valida; <code>false</code> altrimenti
	 */
	public boolean checkCondizioneValida(boolean condition, String nomeCampo){
		return condition;
	}
	
	/**
	 * Restituisce la variabile di processo di dato nome a partire dall'azione richiesta.
	 * 
	 * @param azione l'azione da cui ottenere la variabile di processo
	 * @param nome   il nome della variabile di processo
	 * 
	 * @return la variabile di processo relativa al nome, se presente
	 */
	public VariabileProcesso getVariabileProcesso(AzioneRichiesta azione, BilConstants nome) {
		if(!StringUtils.startsWithIgnoreCase(nome.name(), "VARIABILE_PROCESSO")) {
			return null;
		}
		return getVariabileProcesso(azione,nome.getConstant());
	}
	
	/**
	 * Imposta l'entit&agrave; nel caso in cui non sia null ed abbia l'uid valorizzato.
	 * @param <E> la tipizzazione dell'entita
	 * 
	 * @param entita l'entit&agrave; da impostare
	 * 
	 * @return l'entit&agrave; corretta da impostare
	 */
	protected <E extends Entita> E impostaEntitaFacoltativa(E entita) {
		return entita == null || entita.getUid() == 0 ? null : entita;
	}
	
	/**
	 * Restituisce un oggetto di tipo Date corrispondente all'istante attuale.
	 * 
	 * @return la data corrispondente ad ora
	 */
	protected Date now() {
		return new Date();
	}
	
	/**
	 * Compone la Stringa relativa al capitolo.
	 * @param <C> la tipizzazione del capitolo
	 * 
	 * @param cap il capitolo di cui comporre la stringa
	 * 
	 * @return la stringa del capitolo
	 */
	protected <C extends Capitolo<?, ?>> String componiStringaCapitolo(C cap) {
		String result = "";
		if(cap == null) {
			return result;
		}
		
		StringBuilder sbCapitolo = new StringBuilder();
		if(cap.getNumeroCapitolo() != null) {
			sbCapitolo.append("/").append(cap.getNumeroCapitolo());
		}
		if(cap.getNumeroArticolo() != null) {
			sbCapitolo.append("/").append(cap.getNumeroArticolo());
		}
		if(cap.getNumeroUEB() != null && isGestioneUEB()) {
			sbCapitolo.append("/").append(cap.getNumeroUEB());
		}
		if(sbCapitolo.length() > 0) {
			result = "Capitolo: " + cap.getAnnoCapitolo() + sbCapitolo.toString() + " - ";
		}
		return result;
	}
	
	/**
	 * Compone la Stringa relativa all'impegno.
	 * @param <MG> la tipizzazione del movimento di gestione
	 * @param <SMG> la tipizzazione del submovimento di gestione
	 * 
	 * @param tipoMovimento        il tipo di movimento (Impegno / Accertamento)
	 * @param movimentoGestione    il movimento di gestione
	 * @param subMovimentoGestione il subMovimento di gestione
	 * 
	 * @return la stringa dell'impegno
	 */
	protected <MG extends MovimentoGestione, SMG extends MG> String componiStringaMovimentoGestione(BilConstants tipoMovimento, MG movimentoGestione, SMG subMovimentoGestione) {
		String result = "";
		if(movimentoGestione == null) {
			return result;
		}
		
		StringBuilder sbImpegno = new StringBuilder();
		if(movimentoGestione.getAnnoMovimento() != 0) {
			sbImpegno.append(movimentoGestione.getAnnoMovimento());
		}
		if(movimentoGestione.getNumero() != null) {
			sbImpegno.append("/").append(movimentoGestione.getNumero());
		}
		if(subMovimentoGestione != null && subMovimentoGestione.getNumero() != null) {
			sbImpegno.append("-").append(subMovimentoGestione.getNumero());
		}
		
		if(sbImpegno.length() != 0) {
			result = tipoMovimento.getConstant() + ": " + sbImpegno.append(" - ").toString();
		}
		
		return result;
	}
	
	/**
	 * Compone la Stringa relativa al soggetto.
	 * 
	 * @param soggetto             il soggetto
	 * @param flagSoggettoMancante se il soggetto sia da marcare come mancante
	 * 
	 * @return la stringa del soggetto
	 */
	protected String componiStringaSoggetto(Soggetto soggetto, Boolean flagSoggettoMancante) {
		if(soggetto == null) {
			return "";
		}
		if(Boolean.TRUE.equals(flagSoggettoMancante)) {
			return "Soggetto: Mancante - ";
		}
		
		String result = "";
		if(StringUtils.isNotBlank(soggetto.getCodiceSoggetto())) {
			result = "Soggetto: " + soggetto.getCodiceSoggetto() + " - ";
		}
		return result;
	}
	
	/**
	 * Compone la Stringa relativa all'atto amministrativo.
	 * 
	 * @param attoAmministrativo                    l'atto amministrativo
	 * @param tipoAtto                              il tipo di atto
	 * @param strutturaAmministrativoContabile      la struttura amministrativo contabile
	 * @param flagAttoAmministrativoMancante        se l'atto sia da marcare come mancante
	 * @param listaStrutturaAmministrativoContabile la lista delle Strutture Amministrativo Contabili
	 * @param listaTipoAtto                         la lista dei Tipi di Atto
	 * 
	 * @return la stringa dell'atto
	 */
	protected String componiStringaAttoAmministrativo(AttoAmministrativo attoAmministrativo, TipoAtto tipoAtto, StrutturaAmministrativoContabile strutturaAmministrativoContabile, 
			Boolean flagAttoAmministrativoMancante, List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile, List<TipoAtto> listaTipoAtto) {
		TipoAtto ta = ComparatorUtils.searchByUid(listaTipoAtto, tipoAtto);
		StrutturaAmministrativoContabile sac = ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, strutturaAmministrativoContabile);
		
		if(attoAmministrativo == null && ta == null && sac == null) {
			return "";
		}
		if(Boolean.TRUE.equals(flagAttoAmministrativoMancante)) {
			return "Provvedimento: Mancante - ";
		}
		
		String result = "";
		StringBuilder sbAA = new StringBuilder();
		if(attoAmministrativo.getAnno() != 0) {
			sbAA.append(attoAmministrativo.getAnno());
		}
		if(attoAmministrativo.getNumero() != 0) {
			sbAA.append("/").append(attoAmministrativo.getNumero());
		}
		if(ta != null && ta.getUid() != 0) {
			sbAA.append("/").append(ta.getCodice());
		}
		if(sac != null && sac.getUid() != 0) {
			sbAA.append("/").append(sac.getCodice());
		}
		
		if(sbAA.length() != 0) {
			result = "Provvedimento: " + sbAA.append(" - ").toString();
		}
		
		return result;
	}
	
	/**
	 * Crea un istanza di ParametriPaginazione con numero di elementi per pagina
	 * impostato al default.
	 *
	 * @return the parametri paginazione
	 */
	protected ParametriPaginazione creaParametriPaginazione() {
		return creaParametriPaginazione(ELEMENTI_PER_PAGINA_RICERCA);
	}
	
	/**
	 * Crea un istanza di ParametriPaginazione con numero di elementi per pagina
	 * passato come parametro.
	 *
	 * @param elementiPerPagina the elementi per pagina
	 * @return the parametri paginazione
	 */
	protected ParametriPaginazione creaParametriPaginazione(int elementiPerPagina) {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		
		parametriPaginazione.setElementiPerPagina(elementiPerPagina);
		parametriPaginazione.setNumeroPagina(0);
		
		return parametriPaginazione;
	}
	
	/**
	 * Crea una request di tipo paginazione (modulo FIN).
	 * @param <R> la tipizzazione della request
	 * @param clazz la classe della request
	 * @return la request creata
	 */
	protected <R extends PaginazioneRequest> R creaPaginazioneRequest(Class<R> clazz){
		return creaPaginazioneRequest(clazz, ELEMENTI_PER_PAGINA_RICERCA);
	}
	
	/**
	 * Crea una request di tipo paginazione (modulo FIN).
	 * @param <R> la tipizzazione della request
	 * @param clazz             la classe della request
	 * @param elementiPerPagina il numero di elementi per pagina
	 * @return la request creata
	 */
	protected <R extends PaginazioneRequest> R creaPaginazioneRequest(Class<R> clazz, int elementiPerPagina){
		R request = creaRequest(clazz);
		request.setNumPagina(1);
		request.setNumRisultatiPerPagina(elementiPerPagina);
		return request;
	}

	@Override
	public void addErrori(List<Errore> list) {
		for(Errore errore : list) {
			addErrore(errore);
		}
	}
	
	@Override
	public void addErrore(Errore errore) {
		// Clono l'errore
		boolean handled = false;
		
		handled = extractAndAddErrore(errore, ErroreBil.ELABORAZIONE_ATTIVA.getCodice(), ". Si prega di rieseguire piu' tardi") || handled;
		
//		if(ErroreCore.ERRORE_DI_SISTEMA.getCodice().equals(erroreToAdd.getCodice())) {
//			// Errore di sistema: lo wrappo
//			erroreToAdd = ErroreCore.ERRORE_DI_SISTEMA.getErrore("cliccare <a href=\"#\" data-original-testo-errore=\"" + errore.getTesto() + "\">qui</a> per avere maggiori informazioni");
//		}
		if(!handled) {
			super.addErrore(errore);
		}
	}
	
	/**
	 * Estrae l'eventuale errore e lo aggiunge alla action
	 * @param errore l'errore da considerare
	 * @param codice il codice dell'errore
	 * @param terminatore il terminatore
	 * @return se l'errore sia gi&agrave; stato gestito
	 */
	private boolean extractAndAddErrore(Errore errore, String codice, String terminatore) {
		if(!errore.getDescrizione().contains(codice) || codice.equals(errore.getCodice())) {
			return false;
		}
		String testo = errore.getTesto();
		// Il +3 indica la prsenza dei caratteri ' - '
		int startErrore = testo.indexOf(codice) + codice.length() + 3;
		int endErrore = testo.indexOf(terminatore, startErrore) + terminatore.length();
		String descrizione = endErrore == -1 ? testo.substring(startErrore) : testo.substring(startErrore, endErrore);
		
		Errore newErrore = new Errore();
		newErrore.setCodice(codice);
		newErrore.setDescrizione(descrizione);
		addErrore(newErrore);
		return true;
	}
	//siac 6884
//	private Boolean isDecentrato;
//	public void setIsDecentrato(Boolean isDecentrato) {
//		this.isDecentrato = isDecentrato;
//	}
	// END siac 6884
	
}
