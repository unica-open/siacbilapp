/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerCapitolo;

/**
 * Classe di modello per la gestione della conciliazione per capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2015
 */
public class GestioneConciliazionePerCapitoloModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7196262958474778542L;
	
	private FaseBilancio faseBilancio;
	private String tipoCapitoloRicerca;
	
	private Capitolo<?, ?> capitoloRicerca;
	
	private ConciliazionePerCapitolo conciliazionePerCapitolo1;
	private ConciliazionePerCapitolo conciliazionePerCapitolo2;
	private String tipoCapitolo;
	private Capitolo<?, ?> capitolo;
	
	// Liste
	private List<ClasseDiConciliazione> listaClasseDiConciliazione = new ArrayList<ClasseDiConciliazione>();
	// Wrapper
	private ElementoConciliazionePerCapitolo elementoConciliazionePerCapitolo;
	
	/** Costruttore vuoto di default */
	public GestioneConciliazionePerCapitoloModel() {
		setTitolo("Gestione conciliazione per capitolo");
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
	 * @return the conciliazionePerCapitolo1
	 */
	public ConciliazionePerCapitolo getConciliazionePerCapitolo1() {
		return conciliazionePerCapitolo1;
	}

	/**
	 * @param conciliazionePerCapitolo1 the conciliazionePerCapitolo1 to set
	 */
	public void setConciliazionePerCapitolo1(ConciliazionePerCapitolo conciliazionePerCapitolo1) {
		this.conciliazionePerCapitolo1 = conciliazionePerCapitolo1;
	}

	/**
	 * @return the conciliazionePerCapitolo2
	 */
	public ConciliazionePerCapitolo getConciliazionePerCapitolo2() {
		return conciliazionePerCapitolo2;
	}

	/**
	 * @param conciliazionePerCapitolo2 the conciliazionePerCapitolo2 to set
	 */
	public void setConciliazionePerCapitolo2(ConciliazionePerCapitolo conciliazionePerCapitolo2) {
		this.conciliazionePerCapitolo2 = conciliazionePerCapitolo2;
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
	 * @return the elementoConciliazionePerCapitolo
	 */
	public ElementoConciliazionePerCapitolo getElementoConciliazionePerCapitolo() {
		return elementoConciliazionePerCapitolo;
	}

	/**
	 * @param elementoConciliazionePerCapitolo the elementoConciliazionePerCapitolo to set
	 */
	public void setElementoConciliazionePerCapitolo(ElementoConciliazionePerCapitolo elementoConciliazionePerCapitolo) {
		this.elementoConciliazionePerCapitolo = elementoConciliazionePerCapitolo;
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
	 * Se il capitolo viene trovato devono essere visualizzati i seguenti dati di riepilogo:
	 * <ul>
	 *     <li>Se spesa 'Missione: ' + codice missione + 'Programma' + codice programma + 'Titolo: ' + codice titolo + 'Macroaggregato' + codice macroaggregato</li>
	 *     <li>Se Entrata 'Titolo: ' + codice titolo + 'Tipologia' + codice tipologia + 'Categoria: ' + codice categoria.
	 *     <li>'Tipo capitolo' + descrizione del tipo capitolo + 'Descrizione:' + descrizione capitolo</li>
	 * <ul> 
	 * @return the datiCapitoloRicerca
	 */
	public String getDatiCapitoloRicerca() {
		if(getCapitoloRicerca() instanceof CapitoloUscitaGestione) {
			return computaDatiCapitolo((CapitoloUscitaGestione)getCapitoloRicerca());
		}
		if(getCapitoloRicerca() instanceof CapitoloEntrataGestione) {
			return computaDatiCapitolo((CapitoloEntrataGestione)getCapitoloRicerca());
		}
		return "";
	}

	/**
	 * Calcola i dati del capitolo.
	 * @param capitoloUscitaGestione il capitolo i cui dati devono essere calcolati
	 * @return i dati del capitolo
	 */
	private String computaDatiCapitolo(CapitoloUscitaGestione capitoloUscitaGestione) {
		List<String> chunks = new ArrayList<String>();
		addSplinterCodifica(chunks, capitoloUscitaGestione.getMissione(), "Missione: ");
		addSplinterCodifica(chunks, capitoloUscitaGestione.getProgramma(), "Programma: ");
		addSplinterCodifica(chunks, capitoloUscitaGestione.getTitoloSpesa(), "Titolo: ");
		addSplinterCodifica(chunks, capitoloUscitaGestione.getMacroaggregato(), "Macroaggregato: ");
		
		if(capitoloUscitaGestione.getCategoriaCapitolo() != null && StringUtils.isNotBlank(capitoloUscitaGestione.getCategoriaCapitolo().getDescrizione())) {
			chunks.add("Tipo capitolo: " + capitoloUscitaGestione.getCategoriaCapitolo().getDescrizione());
		}
		if(StringUtils.isNotBlank(capitoloUscitaGestione.getDescrizione())) {
			chunks.add("Descrizione: " + capitoloUscitaGestione.getDescrizione());
		}
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * Calcola i dati del capitolo.
	 * @param capitoloEntrataGestione il capitolo i cui dati devono essere calcolati
	 * @return i dati del capitolo
	 */
	private String computaDatiCapitolo(CapitoloEntrataGestione capitoloEntrataGestione) {
		List<String> chunks = new ArrayList<String>();
		addSplinterCodifica(chunks, capitoloEntrataGestione.getTitoloEntrata(), "Titolo: ");
		addSplinterCodifica(chunks, capitoloEntrataGestione.getTipologiaTitolo(), "Tipologia: ");
		addSplinterCodifica(chunks, capitoloEntrataGestione.getCategoriaTipologiaTitolo(), "Categoria: ");
		
		if(capitoloEntrataGestione.getCategoriaCapitolo() != null && StringUtils.isNotBlank(capitoloEntrataGestione.getCategoriaCapitolo().getDescrizione())) {
			chunks.add("Tipo capitolo: " + capitoloEntrataGestione.getCategoriaCapitolo().getDescrizione());
		}
		if(StringUtils.isNotBlank(capitoloEntrataGestione.getDescrizione())) {
			chunks.add("Descrizione: " + capitoloEntrataGestione.getDescrizione());
		}
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * Controlla se la codifica sia accettabile.
	 *
	 * @param chunks i pezzi per assemblare la stringa
	 * @param codifica la codifica da controllare
	 * @param prefix il prefisso per la stringa da comporre
	 */
	private void addSplinterCodifica(List<String> chunks, Codifica codifica, String prefix) {
		if(codifica != null && StringUtils.isNotBlank(codifica.getCodice())) {
			chunks.add(prefix + codifica.getCodice());
		}
	}
	
	/**
	 * @return the filtroRicerca
	 */
	public String getFiltroRicerca() {
		if(getCapitoloRicerca() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(getCapitoloRicerca().getNumeroCapitolo())
			.append("/")
			.append(getCapitoloRicerca().getNumeroArticolo());
		if(isGestioneUEB()) {
			sb.append("/")
				.append(getCapitoloRicerca().getNumeroUEB());
		}
		return sb.toString();
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

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConciliazionePerCapitolo}.
	 * @return la request creata
	 */
	public RicercaSinteticaConciliazionePerCapitolo creaRequestRicercaSinteticaConciliazionePerCapitolo() {
		RicercaSinteticaConciliazionePerCapitolo request = creaRequest(RicercaSinteticaConciliazionePerCapitolo.class);
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		ConciliazionePerCapitolo conciliazionePerCapitolo = new ConciliazionePerCapitolo();
		conciliazionePerCapitolo.setCapitolo(getCapitoloRicerca());
		request.setConciliazionePerCapitolo(conciliazionePerCapitolo);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link EliminaConciliazionePerCapitolo}.
	 * @return la request creata
	 */
	public EliminaConciliazionePerCapitolo creaRequestEliminaConciliazionePerCapitolo() {
		EliminaConciliazionePerCapitolo request = creaRequest(EliminaConciliazionePerCapitolo.class);
		request.setConciliazionePerCapitolo(getConciliazionePerCapitolo1());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioConciliazionePerCapitolo}.
	 * @return la request creata
	 */
	public RicercaDettaglioConciliazionePerCapitolo creaRequestRicercaDettaglioConciliazionePerCapitolo() {
		RicercaDettaglioConciliazionePerCapitolo request = creaRequest(RicercaDettaglioConciliazionePerCapitolo.class);
		request.setConciliazionePerCapitolo(getConciliazionePerCapitolo1());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link InserisceConciliazioniPerCapitolo}.
	 * 
	 * @return la request creata
	 */
	public InserisceConciliazioniPerCapitolo creaRequestInserisceConciliazioniPerCapitolo() {
		InserisceConciliazioniPerCapitolo request = creaRequest(InserisceConciliazioniPerCapitolo.class);
		request.setBilancio(getBilancio());
		
		List<ConciliazionePerCapitolo> conciliazioniPerCapitolo = new ArrayList<ConciliazionePerCapitolo>();
		// Aggiungo le conciliazioni
		popolaConciliazione(conciliazioniPerCapitolo, getConciliazionePerCapitolo1());
		popolaConciliazione(conciliazioniPerCapitolo, getConciliazionePerCapitolo2());
		request.setConciliazioniPerCapitolo(conciliazioniPerCapitolo);
		
		return request;
	}

	private void popolaConciliazione(List<ConciliazionePerCapitolo> conciliazioniPerCapitolo, ConciliazionePerCapitolo conciliazione) {
		if(conciliazione == null || conciliazione.getConto() == null || conciliazione.getConto().getUid() == 0) {
			return;
		}
		conciliazione.setEnte(getEnte());
		conciliazione.setCapitolo(getCapitolo());
		
		conciliazioniPerCapitolo.add(conciliazione);
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaConciliazionePerCapitolo}.
	 * 
	 * @return la request creata
	 */
	public AggiornaConciliazionePerCapitolo creaRequestAggiornaConciliazionePerCapitolo() {
		AggiornaConciliazionePerCapitolo request = creaRequest(AggiornaConciliazionePerCapitolo.class);
		request.setBilancio(getBilancio());
		
		getConciliazionePerCapitolo1().setEnte(getEnte());
		getConciliazionePerCapitolo1().setCapitolo(getCapitolo());
		
		request.setConciliazionePerCapitolo(getConciliazionePerCapitolo1());
		
		return request;
	}
}
