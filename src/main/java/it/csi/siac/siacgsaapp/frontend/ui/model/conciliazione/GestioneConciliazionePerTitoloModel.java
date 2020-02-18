/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.PianoDeiConti;

/**
 * Classe di modello per la gestione della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 */
public class GestioneConciliazionePerTitoloModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5568201734955590889L;
	
	private FaseBilancio faseBilancio;
	private ClasseDiConciliazione classeDiConciliazione;
	private String entrataSpesaRicerca;
	// Spesa
	private TitoloSpesa titoloSpesaRicerca;
	private Macroaggregato macroaggregatoRicerca;
	// Entrata
	private TitoloEntrata titoloEntrataRicerca;
	private TipologiaTitolo tipologiaTitoloRicerca;
	private CategoriaTipologiaTitolo categoriaTipologiaTitoloRicerca;

	// Inserimento
	private String entrataSpesa;
	private ConciliazionePerTitolo conciliazionePerTitolo;
	private ClassePiano classePiano;
	private TitoloSpesa titoloSpesa;
	private Macroaggregato macroaggregato;
	private TitoloEntrata titoloEntrata;
	private TipologiaTitolo tipologiaTitolo;
	private CategoriaTipologiaTitolo categoriaTipologiaTitolo;
	
	// Liste
	private List<ClasseDiConciliazione> listaClasseDiConciliazione = new ArrayList<ClasseDiConciliazione>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<Macroaggregato> listaMacroaggregato = new ArrayList<Macroaggregato>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TipologiaTitolo> listaTipologiaTitolo = new ArrayList<TipologiaTitolo>();
	private List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo = new ArrayList<CategoriaTipologiaTitolo>();
	private List<ClassePiano> listaClassePiano = new ArrayList<ClassePiano>();
	
	/** Costruttore vuoto di default */
	public GestioneConciliazionePerTitoloModel() {
		setTitolo("Gestione conciliazione per titolo");
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
	 * @return the classeDiConciliazione
	 */
	public ClasseDiConciliazione getClasseDiConciliazione() {
		return classeDiConciliazione;
	}

	/**
	 * @param classeDiConciliazione the classeDiConciliazione to set
	 */
	public void setClasseDiConciliazione(ClasseDiConciliazione classeDiConciliazione) {
		this.classeDiConciliazione = classeDiConciliazione;
	}

	/**
	 * @return the entrataSpesaRicerca
	 */
	public String getEntrataSpesaRicerca() {
		return entrataSpesaRicerca;
	}

	/**
	 * @param entrataSpesaRicerca the entrataSpesaRicerca to set
	 */
	public void setEntrataSpesaRicerca(String entrataSpesaRicerca) {
		this.entrataSpesaRicerca = entrataSpesaRicerca;
	}

	/**
	 * @return the titoloSpesaRicerca
	 */
	public TitoloSpesa getTitoloSpesaRicerca() {
		return titoloSpesaRicerca;
	}

	/**
	 * @param titoloSpesaRicerca the titoloSpesaRicerca to set
	 */
	public void setTitoloSpesaRicerca(TitoloSpesa titoloSpesaRicerca) {
		this.titoloSpesaRicerca = titoloSpesaRicerca;
	}

	/**
	 * @return the macroaggregatoRicerca
	 */
	public Macroaggregato getMacroaggregatoRicerca() {
		return macroaggregatoRicerca;
	}

	/**
	 * @param macroaggregatoRicerca the macroaggregatoRicerca to set
	 */
	public void setMacroaggregatoRicerca(Macroaggregato macroaggregatoRicerca) {
		this.macroaggregatoRicerca = macroaggregatoRicerca;
	}

	/**
	 * @return the titoloEntrataRicerca
	 */
	public TitoloEntrata getTitoloEntrataRicerca() {
		return titoloEntrataRicerca;
	}

	/**
	 * @param titoloEntrataRicerca the titoloEntrataRicerca to set
	 */
	public void setTitoloEntrataRicerca(TitoloEntrata titoloEntrataRicerca) {
		this.titoloEntrataRicerca = titoloEntrataRicerca;
	}

	/**
	 * @return the tipologiaTitoloRicerca
	 */
	public TipologiaTitolo getTipologiaTitoloRicerca() {
		return tipologiaTitoloRicerca;
	}

	/**
	 * @param tipologiaTitoloRicerca the tipologiaTitoloRicerca to set
	 */
	public void setTipologiaTitoloRicerca(TipologiaTitolo tipologiaTitoloRicerca) {
		this.tipologiaTitoloRicerca = tipologiaTitoloRicerca;
	}

	/**
	 * @return the categoriaTipologiaTitoloRicerca
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitoloRicerca() {
		return categoriaTipologiaTitoloRicerca;
	}

	/**
	 * @param categoriaTipologiaTitoloRicerca the categoriaTipologiaTitoloRicerca to set
	 */
	public void setCategoriaTipologiaTitoloRicerca(
			CategoriaTipologiaTitolo categoriaTipologiaTitoloRicerca) {
		this.categoriaTipologiaTitoloRicerca = categoriaTipologiaTitoloRicerca;
	}

	/**
	 * @return the entrataSpesa
	 */
	public String getEntrataSpesa() {
		return entrataSpesa;
	}

	/**
	 * @param entrataSpesa the entrataSpesa to set
	 */
	public void setEntrataSpesa(String entrataSpesa) {
		this.entrataSpesa = entrataSpesa;
	}

	/**
	 * @return the conciliazionePerTitolo
	 */
	public ConciliazionePerTitolo getConciliazionePerTitolo() {
		return conciliazionePerTitolo;
	}

	/**
	 * @param conciliazionePerTitolo the conciliazionePerTitolo to set
	 */
	public void setConciliazionePerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		this.conciliazionePerTitolo = conciliazionePerTitolo;
	}

	/**
	 * @return the classePiano
	 */
	public ClassePiano getClassePiano() {
		return classePiano;
	}

	/**
	 * @param classePiano the classePiano to set
	 */
	public void setClassePiano(ClassePiano classePiano) {
		this.classePiano = classePiano;
	}

	/**
	 * @return the titoloSpesa
	 */
	public TitoloSpesa getTitoloSpesa() {
		return titoloSpesa;
	}

	/**
	 * @param titoloSpesa the titoloSpesa to set
	 */
	public void setTitoloSpesa(TitoloSpesa titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}

	/**
	 * @return the macroaggregato
	 */
	public Macroaggregato getMacroaggregato() {
		return macroaggregato;
	}

	/**
	 * @param macroaggregato the macroaggregato to set
	 */
	public void setMacroaggregato(Macroaggregato macroaggregato) {
		this.macroaggregato = macroaggregato;
	}

	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}

	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}

	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}

	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}

	/**
	 * @return the categoriaTipologiaTitolo
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitolo() {
		return categoriaTipologiaTitolo;
	}

	/**
	 * @param categoriaTipologiaTitolo the categoriaTipologiaTitolo to set
	 */
	public void setCategoriaTipologiaTitolo(CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		this.categoriaTipologiaTitolo = categoriaTipologiaTitolo;
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
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}

	/**
	 * @return the listaMacroaggregato
	 */
	public List<Macroaggregato> getListaMacroaggregato() {
		return listaMacroaggregato;
	}

	/**
	 * @param listaMacroaggregato the listaMacroaggregato to set
	 */
	public void setListaMacroaggregato(List<Macroaggregato> listaMacroaggregato) {
		this.listaMacroaggregato = listaMacroaggregato != null ? listaMacroaggregato : new ArrayList<Macroaggregato>();
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTipologiaTitolo
	 */
	public List<TipologiaTitolo> getListaTipologiaTitolo() {
		return listaTipologiaTitolo;
	}

	/**
	 * @param listaTipologiaTitolo the listaTipologiaTitolo to set
	 */
	public void setListaTipologiaTitolo(List<TipologiaTitolo> listaTipologiaTitolo) {
		this.listaTipologiaTitolo = listaTipologiaTitolo != null ? listaTipologiaTitolo : new ArrayList<TipologiaTitolo>();
	}

	/**
	 * @return the listaCategoriaTipologiaTitolo
	 */
	public List<CategoriaTipologiaTitolo> getListaCategoriaTipologiaTitolo() {
		return listaCategoriaTipologiaTitolo;
	}

	/**
	 * @param listaCategoriaTipologiaTitolo the listaCategoriaTipologiaTitolo to set
	 */
	public void setListaCategoriaTipologiaTitolo(List<CategoriaTipologiaTitolo> listaCategoriaTipologiaTitolo) {
		this.listaCategoriaTipologiaTitolo = listaCategoriaTipologiaTitolo != null ? listaCategoriaTipologiaTitolo : new ArrayList<CategoriaTipologiaTitolo>();
	}

	/**
	 * @return the listaClassePiano
	 */
	public List<ClassePiano> getListaClassePiano() {
		return listaClassePiano;
	}

	/**
	 * @param listaClassePiano the listaClassePiano to set
	 */
	public void setListaClassePiano(List<ClassePiano> listaClassePiano) {
		this.listaClassePiano = listaClassePiano != null ? listaClassePiano : new ArrayList<ClassePiano>();
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
	 * @return the filtroRicerca
	 */
	public String getFiltroRicerca() {
		List<String> chunks = new ArrayList<String>();
		
		if(getClasseDiConciliazione() != null) {
			chunks.add(getClasseDiConciliazione().getDescrizione());
		}
		if(StringUtils.isNotBlank(getEntrataSpesaRicerca())) {
			chunks.add(getEntrataSpesaRicerca());
		}
		if(isEntrata(getEntrataSpesaRicerca()) && getTitoloEntrataRicerca() != null && getTitoloEntrataRicerca().getUid() != 0) {
			chunks.add(getTitoloEntrataRicerca().getCodice());
		}
		if(isEntrata(getEntrataSpesaRicerca()) && getTipologiaTitoloRicerca() != null && getTipologiaTitoloRicerca().getUid() != 0) {
			chunks.add(getTipologiaTitoloRicerca().getCodice());
		}
		if(isSpesa(getEntrataSpesaRicerca()) && getTitoloSpesaRicerca() != null && getTitoloSpesaRicerca().getUid() != 0) {
			chunks.add(getTitoloSpesaRicerca().getCodice());
		}
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return getListaClassePiano();
	}

	/**
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}

	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}

	/**
	 * @return the faseBilancioChiuso
	 */
	public boolean isFaseBilancioChiuso() {
		return FaseBilancio.CHIUSO.equals(getFaseBilancio());
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConciliazionePerTitolo}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConciliazionePerTitolo creaRequestRicercaSinteticaConciliazionePerTitolo() {
		RicercaSinteticaConciliazionePerTitolo request = creaRequest(RicercaSinteticaConciliazionePerTitolo.class);
		
		ConciliazionePerTitolo cpt = new ConciliazionePerTitolo();
		cpt.setClasseDiConciliazione(getClasseDiConciliazione());
		
		request.setParametriPaginazione(creaParametriPaginazione(5));
		request.setConciliazionePerTitolo(cpt);
		
		if(isEntrata(getEntrataSpesaRicerca())) {
			request.setTitolo(getTitoloEntrataRicerca());
			request.setTipologia(getTipologiaTitoloRicerca());
			cpt.setCategoriaTipologiaTitolo(getCategoriaTipologiaTitoloRicerca());
		} else if(isSpesa(getEntrataSpesaRicerca())) {
			request.setTitolo(getTitoloSpesaRicerca());
			cpt.setMacroaggregato(getMacroaggregatoRicerca());
		}
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link EliminaConciliazionePerTitolo}.
	 * 
	 * @return la request creata
	 */
	public EliminaConciliazionePerTitolo creaRequestEliminaConciliazionePerTitolo() {
		EliminaConciliazionePerTitolo request = creaRequest(EliminaConciliazionePerTitolo.class);
		
		request.setConciliazionePerTitolo(getConciliazionePerTitolo());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioConciliazionePerTitolo}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioConciliazionePerTitolo creaRequestRicercaDettaglioConciliazionePerTitolo() {
		RicercaDettaglioConciliazionePerTitolo request = creaRequest(RicercaDettaglioConciliazionePerTitolo.class);
		
		request.setConciliazionePerTitolo(getConciliazionePerTitolo());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto() {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione(1));
		
		Conto conto = new Conto();
		conto.setAmbito(Ambito.AMBITO_GSA);
		conto.setEnte(getEnte());
		conto.setCodice(getConciliazionePerTitolo().getConto().getCodice());
		
		PianoDeiConti pianoDeiConti = new PianoDeiConti();
		pianoDeiConti.setClassePiano(getClassePiano());
		conto.setPianoDeiConti(pianoDeiConti);
		
		request.setConto(conto);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link InserisceConciliazionePerTitolo}.
	 * 
	 * @return la request creata
	 */
	public InserisceConciliazionePerTitolo creaRequestInserisceConciliazionePerTitolo() {
		InserisceConciliazionePerTitolo request = creaRequest(InserisceConciliazionePerTitolo.class);
		
		request.setBilancio(getBilancio());
		
		impostaClassificatoreGenericoConciliazione(getConciliazionePerTitolo());
		getConciliazionePerTitolo().setEnte(getEnte());
		request.setConciliazionePerTitolo(getConciliazionePerTitolo());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaConciliazionePerTitolo}.
	 * 
	 * @return la request creata
	 */
	public AggiornaConciliazionePerTitolo creaRequestAggiornaConciliazionePerTitolo() {
		AggiornaConciliazionePerTitolo request = creaRequest(AggiornaConciliazionePerTitolo.class);
		
		request.setBilancio(getBilancio());
		
		impostaClassificatoreGenericoConciliazione(getConciliazionePerTitolo());
		getConciliazionePerTitolo().setEnte(getEnte());
		request.setConciliazionePerTitolo(getConciliazionePerTitolo());
		
		return request;
	}

	/**
	 * Impostazione del classificatore generico nella conciliazione
	 * 
	 * @param conciliazione la conciliazione da popolare
	 */
	private void impostaClassificatoreGenericoConciliazione(ConciliazionePerTitolo conciliazione) {
		if(isEntrata(getEntrataSpesa())) {
			conciliazione.setCategoriaTipologiaTitolo(getCategoriaTipologiaTitolo());
		} else if(isSpesa(getEntrataSpesa())) {
			conciliazione.setMacroaggregato(getMacroaggregato());
		}
	}

}
