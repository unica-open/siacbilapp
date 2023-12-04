/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerBeneficiario;

/**
 * Classe di modello per la gestione della conciliazione per beneficiario.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2015
 */
public class GestioneConciliazionePerBeneficiarioModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8202232014823965355L;

	private FaseBilancio faseBilancio;
	
	// Ricerca
	private Soggetto soggettoRicerca;
	private String tipoCapitoloRicerca;
	private Capitolo<?, ?> capitoloRicerca;

	// Inserimento
	private ConciliazionePerBeneficiario conciliazionePerBeneficiario1;
	private ConciliazionePerBeneficiario conciliazionePerBeneficiario2;
	private Soggetto soggetto;
	private String tipoCapitolo;
	private Capitolo<?, ?> capitolo;
	
	// Liste
	private List<ClasseDiConciliazione> listaClasseDiConciliazione = new ArrayList<ClasseDiConciliazione>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	// Wrapper
	private ElementoConciliazionePerBeneficiario elementoConciliazionePerBeneficiario;
	
	/** Costruttore vuoto di default */
	public GestioneConciliazionePerBeneficiarioModel() {
		setTitolo("Gestione conciliazione per beneficiario");
	}

	/**
	 * @return the faseBilancio
	 */
	public FaseBilancio getFaseBilancio() {
		return faseBilancio;
	}

	/**
	 * @param faseBilancio the faseBilancio to set
	 */
	public void setFaseBilancio(FaseBilancio faseBilancio) {
		this.faseBilancio = faseBilancio;
	}

	/**
	 * @return the soggettoRicerca
	 */
	public Soggetto getSoggettoRicerca() {
		return soggettoRicerca;
	}

	/**
	 * @param soggettoRicerca the soggettoRicerca to set
	 */
	public void setSoggettoRicerca(Soggetto soggettoRicerca) {
		this.soggettoRicerca = soggettoRicerca;
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
	 * @return the tipoCapitoloRicerca
	 */
	public String getTipoCapitoloRicerca() {
		return tipoCapitoloRicerca;
	}

	/**
	 * @param tipoCapitoloRicerca the tipoCapitoloRicerca to set
	 */
	public void setTipoCapitoloRicerca(String tipoCapitoloRicerca) {
		this.tipoCapitoloRicerca = tipoCapitoloRicerca;
	}

	/**
	 * @return the capitoloRicerca
	 */
	public Capitolo<?, ?> getCapitoloRicerca() {
		return capitoloRicerca;
	}

	/**
	 * @param capitoloRicerca the capitoloRicerca to set
	 */
	public void setCapitoloRicerca(Capitolo<?, ?> capitoloRicerca) {
		this.capitoloRicerca = capitoloRicerca;
	}

	/**
	 * @return the conciliazionePerBeneficiario1
	 */
	public ConciliazionePerBeneficiario getConciliazionePerBeneficiario1() {
		return conciliazionePerBeneficiario1;
	}

	/**
	 * @param conciliazionePerBeneficiario1 the conciliazionePerBeneficiario1 to set
	 */
	public void setConciliazionePerBeneficiario1(ConciliazionePerBeneficiario conciliazionePerBeneficiario1) {
		this.conciliazionePerBeneficiario1 = conciliazionePerBeneficiario1;
	}

	/**
	 * @return the conciliazionePerBeneficiario2
	 */
	public ConciliazionePerBeneficiario getConciliazionePerBeneficiario2() {
		return conciliazionePerBeneficiario2;
	}

	/**
	 * @param conciliazionePerBeneficiario2 the conciliazionePerBeneficiario2 to set
	 */
	public void setConciliazionePerBeneficiario2(ConciliazionePerBeneficiario conciliazionePerBeneficiario2) {
		this.conciliazionePerBeneficiario2 = conciliazionePerBeneficiario2;
	}

	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
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
	 * @return the listaClasseDiConciliazione
	 */
	public List<ClasseDiConciliazione> getListaClasseDiConciliazione() {
		return listaClasseDiConciliazione;
	}

	/**
	 * @param listaClasseDiConciliazione the listaClasseDiConciliazione to set
	 */
	public void setListaClasseDiConciliazione(List<ClasseDiConciliazione> listaClasseDiConciliazione) {
		this.listaClasseDiConciliazione = listaClasseDiConciliazione != null ? listaClasseDiConciliazione : new ArrayList<ClasseDiConciliazione>();
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
	 * @return the elementoConciliazionePerBeneficiario
	 */
	public ElementoConciliazionePerBeneficiario getElementoConciliazionePerBeneficiario() {
		return elementoConciliazionePerBeneficiario;
	}

	/**
	 * @param elementoConciliazionePerBeneficiario the elementoConciliazionePerBeneficiario to set
	 */
	public void setElementoConciliazionePerBeneficiario(ElementoConciliazionePerBeneficiario elementoConciliazionePerBeneficiario) {
		this.elementoConciliazionePerBeneficiario = elementoConciliazionePerBeneficiario;
	}

	/**
	 * @return the faseBilancioChiuso
	 */
	public boolean isFaseBilancioChiuso() {
		return FaseBilancio.CHIUSO.equals(getFaseBilancio());
	}

	/**
	 * @param tipo il tipo da controllare
	 * @return <code>true</code> se il tipo &eacute; di entrata, con codice <code>E</code>; <code>false</code> altrimenti
	 */
	public boolean isEntrata(String tipo) {
		return "E".equals(tipo);
	}

	/**
	 * @param tipo il tipo da controllare
	 * @return <code>true</code> se il tipo &eacute; di spesa, con codice <code>S</code>; <code>false</code> altrimenti
	 */
	public boolean isSpesa(String tipo) {
		return "S".equals(tipo);
	}

	/**
	 * @return denominazioneBeneficiario
	 */
	public String getDenominazioneBeneficiario() {
		return getSoggettoRicerca() != null ? getSoggettoRicerca().getDenominazione() : "";
	}

	/**
	 * @return the filtroRicerca
	 */
	public String getFiltroRicerca() {
		return getDenominazioneBeneficiario();
	}
	
	/**
	 * @return the classeDiConciliazione1Spesa
	 */
	public ClasseDiConciliazione getClasseDiConciliazione1Spesa() {
		return ClasseDiConciliazione.COSTI;
	}
	
	/**
	 * @return the classeDiConciliazione2Spesa
	 */
	public ClasseDiConciliazione getClasseDiConciliazione2Spesa() {
		return ClasseDiConciliazione.DEBITI;
	}
	
	/**
	 * @return the classeDiConciliazione1Spesa
	 */
	public ClasseDiConciliazione getClasseDiConciliazione1Entrata() {
		return ClasseDiConciliazione.RICAVI;
	}
	
	/**
	 * @return the classeDiConciliazione2Spesa
	 */
	public ClasseDiConciliazione getClasseDiConciliazione2Entrata() {
		return ClasseDiConciliazione.CREDITI;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * @param sog il soggetto da cercare
	 * 
	 * @return la request create
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto sog) {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setCodificaAmbito(Ambito.AMBITO_FIN.name());
		request.setEnte(getEnte());
		request.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(sog.getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConciliazionePerBeneficiario}.
	 * @return la request creata
	 */
	public RicercaSinteticaConciliazionePerBeneficiario creaRequestRicercaSinteticaConciliazionePerBeneficiario() {
		RicercaSinteticaConciliazionePerBeneficiario request = creaRequest(RicercaSinteticaConciliazionePerBeneficiario.class);
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		ConciliazionePerBeneficiario cpb = new ConciliazionePerBeneficiario();
		cpb.setSoggetto(istanziaSoggetto(getSoggettoRicerca()));
		
		// Imposto il capitolo solo se correttamente popolato
		cpb.setCapitolo(istanziaCapitolo(getCapitoloRicerca(), getTipoCapitoloRicerca()));
		
		request.setConciliazionePerBeneficiario(cpb);
		
		return request;
	}

	/**
	 * Istanzia un nuovo soggetto a partire da quello fornito, copiando i dati minimi.
	 * 
	 * @param soggettoBase il soggetto da cui ottenere i dati
	 * @return il soggetto
	 */
	private Soggetto istanziaSoggetto(Soggetto soggettoBase) {
		Soggetto s = new Soggetto();
		s.setCodiceSoggetto(soggettoBase.getCodiceSoggetto());
		s.setUid(soggettoBase.getUid());
		return s;
	}

	/**
	 * Crea una request per il servizio di {@link EliminaConciliazionePerBeneficiario}.
	 * @return la request creata
	 */
	public EliminaConciliazionePerBeneficiario creaRequestEliminaConciliazionePerBeneficiario() {
		EliminaConciliazionePerBeneficiario request = creaRequest(EliminaConciliazionePerBeneficiario.class);
		request.setConciliazionePerBeneficiario(getConciliazionePerBeneficiario1());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioConciliazionePerBeneficiario}.
	 * @return la request creata
	 */
	public RicercaDettaglioConciliazionePerBeneficiario creaRequestRicercaDettaglioConciliazionePerBeneficiario() {
		RicercaDettaglioConciliazionePerBeneficiario request = creaRequest(RicercaDettaglioConciliazionePerBeneficiario.class);
		request.setConciliazionePerBeneficiario(getConciliazionePerBeneficiario1());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link InserisceConciliazioniPerBeneficiario}.
	 * 
	 * @return la request creata
	 */
	public InserisceConciliazioniPerBeneficiario creaRequestInserisceConciliazioniPerBeneficiario() {
		InserisceConciliazioniPerBeneficiario request = creaRequest(InserisceConciliazioniPerBeneficiario.class);
		request.setBilancio(getBilancio());
		
		List<ConciliazionePerBeneficiario> conciliazioniPerBeneficiario = new ArrayList<ConciliazionePerBeneficiario>();
		// Aggiungo le conciliazioni
		popolaConciliazione(conciliazioniPerBeneficiario, getConciliazionePerBeneficiario1());
		popolaConciliazione(conciliazioniPerBeneficiario, getConciliazionePerBeneficiario2());
		request.setConciliazioniPerBeneficiario(conciliazioniPerBeneficiario);
		
		return request;
	}

	private void popolaConciliazione(List<ConciliazionePerBeneficiario> conciliazioniPerBeneficiario, ConciliazionePerBeneficiario conciliazione) {
		if(conciliazione == null || conciliazione.getConto() == null || conciliazione.getConto().getUid() == 0) {
			return;
		}
		conciliazione.setEnte(getEnte());
		conciliazione.setCapitolo(istanziaCapitolo(getCapitolo(), getTipoCapitolo()));
		conciliazione.setSoggetto(istanziaSoggetto(getSoggetto()));
		
		conciliazioniPerBeneficiario.add(conciliazione);
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaConciliazionePerBeneficiario}.
	 * 
	 * @return la request creata
	 */
	public AggiornaConciliazionePerBeneficiario creaRequestAggiornaConciliazionePerBeneficiario() {
		AggiornaConciliazionePerBeneficiario request = creaRequest(AggiornaConciliazionePerBeneficiario.class);
		request.setBilancio(getBilancio());
		
		getConciliazionePerBeneficiario1().setEnte(getEnte());
		getConciliazionePerBeneficiario1().setCapitolo(istanziaCapitolo(getCapitolo(), getTipoCapitolo()));
		getConciliazionePerBeneficiario1().setSoggetto(getSoggetto());
		
		request.setConciliazionePerBeneficiario(getConciliazionePerBeneficiario1());
		
		return request;
	}

	/**
	 * Istanzia correttamente il capitolo di tipo corretto a partire dai dati di base e dall'essere di entrata o spesa.
	 * 
	 * @param capitoloBase il capitolod a cui copiare i dati
	 * @param tipo <code>E</code> se il capitolo &eacute; di entrata, e forza la creazione di un {@link CapitoloEntrataGestione}; in caso contrario forza la creazione di un {@link CapitoloUscitaGestione}.
	 * @return il capitolo di tipo corretto
	 */
	@SuppressWarnings("rawtypes")
	private Capitolo<?, ?> istanziaCapitolo(Capitolo<?, ?> capitoloBase, String tipo) {
		Capitolo<?, ?> cap;
		if(StringUtils.isBlank(tipo)) {
			cap = new Capitolo();
		} else {
			cap = isEntrata(tipo) ? new CapitoloEntrataGestione() : new CapitoloUscitaGestione();
		}
		
		if(capitoloBase != null) {
			cap.setAnnoCapitolo(capitoloBase.getAnnoCapitolo());
			cap.setNumeroCapitolo(capitoloBase.getNumeroCapitolo());
			cap.setNumeroArticolo(capitoloBase.getNumeroArticolo());
			cap.setNumeroUEB(capitoloBase.getNumeroUEB());
			cap.setUid(capitoloBase.getUid());
		}
		return cap;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaPuntualeCapitoloUscitaGestione}.
	 * @param cap il capitolo da cercare
	 * 
	 * @return la request create
	 */
	public RicercaPuntualeCapitoloUscitaGestione creaRequestRicercaPuntualeCapitoloUscitaGestione(Capitolo<?, ?> cap) {
		RicercaPuntualeCapitoloUscitaGestione request = creaRequest(RicercaPuntualeCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		RicercaPuntualeCapitoloUGest ricercaPuntualeCapitoloUGest = new RicercaPuntualeCapitoloUGest();
		ricercaPuntualeCapitoloUGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaPuntualeCapitoloUGest.setAnnoCapitolo(cap.getAnnoCapitolo());
		ricercaPuntualeCapitoloUGest.setNumeroCapitolo(cap.getNumeroCapitolo());
		ricercaPuntualeCapitoloUGest.setNumeroArticolo(cap.getNumeroArticolo());
		ricercaPuntualeCapitoloUGest.setNumeroUEB(cap.getNumeroUEB());
		ricercaPuntualeCapitoloUGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		request.setRicercaPuntualeCapitoloUGest(ricercaPuntualeCapitoloUGest);
		
		request.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.MISSIONE, TipologiaClassificatore.PROGRAMMA,
				TipologiaClassificatore.TITOLO_SPESA, TipologiaClassificatore.MACROAGGREGATO));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaPuntualeCapitoloEntrataGestione}.
	 * @param cap il capitolo da cercare
	 * 
	 * @return la request create
	 */
	public RicercaPuntualeCapitoloEntrataGestione creaRequestRicercaPuntualeCapitoloEntrataGestione(Capitolo<?, ?> cap) {
		RicercaPuntualeCapitoloEntrataGestione request = creaRequest(RicercaPuntualeCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		RicercaPuntualeCapitoloEGest ricercaPuntualeCapitoloEGest = new RicercaPuntualeCapitoloEGest();
		ricercaPuntualeCapitoloEGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaPuntualeCapitoloEGest.setAnnoCapitolo(cap.getAnnoCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroCapitolo(cap.getNumeroCapitolo());
		ricercaPuntualeCapitoloEGest.setNumeroArticolo(cap.getNumeroArticolo());
		ricercaPuntualeCapitoloEGest.setNumeroUEB(cap.getNumeroUEB());
		ricercaPuntualeCapitoloEGest.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		request.setRicercaPuntualeCapitoloEGest(ricercaPuntualeCapitoloEGest);
		
		request.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.TITOLO_ENTRATA, TipologiaClassificatore.TIPOLOGIA, TipologiaClassificatore.CATEGORIA));
		
		return request;
	}
}
