/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.importi.ElementoImportiVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

/**
 * Classe di model per la specificazione della variazione degli importi nel caso della gestione senza UEB. Contiene una mappatura del model.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 15/11/2013
 *
 */
public class SpecificaVariazioneImportoModel extends SpecificaVariazioneImportoBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7357638058514734678L;

	private String tipoCapitolo;
	private ElementoCapitoloVariazione elementoCapitoloVariazione;
	private List<ElementoCapitoloVariazione> listaCapitoliNellaVariazione = new ArrayList<ElementoCapitoloVariazione>();
	private ElementoImportiVariazione elementoImportiVariazione;
	
	private Capitolo<?, ?> capitoloDaAggiungere;
	private Capitolo<?, ?> capitoloOriginale;
	private Boolean flagFondoPluriennaleVincolato;
	
	private Integer index;
	//SIAC-5016
	private CapitoloEntrataPrevisione capitoloEntrataPrevisioneNellaVariazione;
	private CapitoloUscitaPrevisione capitoloUscitaPrevisioneNellaVariazione;
	private CapitoloEntrataGestione capitoloEntrataGestioneNellaVariazione;
	private CapitoloUscitaGestione capitoloUscitaGestioneNellaVariazione;
	private ElementoCapitoloVariazione elementoCapitoloVariazioneTrovatoNellaVariazione;
	
	//SIAC-6881
	private int uidCapitoloComponenti;
	private ElementoComponenteVariazione elementoComponenteModificata;
	private int uidCapitoloAssociatoComponenti;
	//SIAC-7217
    private boolean capitoloFondino;

	private List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = new ArrayList<ElementoComponenteVariazione>();
	private List<ElementoComponenteVariazione> componentiCapitoloDettaglio = new ArrayList<ElementoComponenteVariazione>();
	private BigDecimal competenzaTotaleAnno0 = BigDecimal.ZERO;
	private BigDecimal competenzaTotaleAnno1 = BigDecimal.ZERO;
	private BigDecimal competenzaTotaleAnno2 = BigDecimal.ZERO;
	
	private BigDecimal competenzaTotaleNuovoAnno0 = BigDecimal.ZERO;
	private BigDecimal competenzaTotaleNuovoAnno1 = BigDecimal.ZERO;
	private BigDecimal competenzaTotaleNuovoAnno2 = BigDecimal.ZERO;
	
	private TipoComponenteImportiCapitolo tipoComponenteSelezionata;
	private TipoDettaglioComponenteImportiCapitolo tipoDettaglioSelezionato = TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO; 
	private boolean editabileAnno0 = false;
	private boolean editabileAnno1 = false;
	private boolean editabileAnno2 = false;
	
	/** Costruttore vuoto di default */
	public SpecificaVariazioneImportoModel() {
		super();
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
	 * @return the elementoCapitoloVariazione
	 */
	public ElementoCapitoloVariazione getElementoCapitoloVariazione() {
		return elementoCapitoloVariazione;
	}

	/**
	 * @param elementoCapitoloVariazione the elementoCapitoloVariazione to set
	 */
	public void setElementoCapitoloVariazione(ElementoCapitoloVariazione elementoCapitoloVariazione) {
		this.elementoCapitoloVariazione = elementoCapitoloVariazione;
	}
	
	/**
	 * @return the listaCapitoliNellaVariazione
	 */
	public List<ElementoCapitoloVariazione> getListaCapitoliNellaVariazione() {
		return listaCapitoliNellaVariazione;
	}

	/**
	 * @param listaCapitoliNellaVariazione the listaCapitoliNellaVariazione to set
	 */
	public void setListaCapitoliNellaVariazione(
			List<ElementoCapitoloVariazione> listaCapitoliNellaVariazione) {
		this.listaCapitoliNellaVariazione = listaCapitoliNellaVariazione;
	}

	/**
	 * @return the elementoImportiVariazione
	 */
	public ElementoImportiVariazione getElementoImportiVariazione() {
		return elementoImportiVariazione;
	}

	/**
	 * @param elementoImportiVariazione the elementoImportiVariazione to set
	 */
	public void setElementoImportiVariazione(ElementoImportiVariazione elementoImportiVariazione) {
		this.elementoImportiVariazione = elementoImportiVariazione;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return the capitoloDaAggiungere
	 */
	public Capitolo<?, ?> getCapitoloDaAggiungere() {
		return capitoloDaAggiungere;
	}

	/**
	 * @param capitoloDaAggiungere the capitoloDaAggiungere to set
	 */
	public void setCapitoloDaAggiungere(Capitolo<?, ?> capitoloDaAggiungere) {
		this.capitoloDaAggiungere = capitoloDaAggiungere;
	}
	
	/**
	 * @return the capitoloOriginale
	 */
	public Capitolo<?, ?> getCapitoloOriginale() {
		return capitoloOriginale;
	}

	/**
	 * @param capitoloOriginale the capitoloOriginale to set
	 */
	public void setCapitoloOriginale(Capitolo<?, ?> capitoloOriginale) {
		this.capitoloOriginale = capitoloOriginale;
	}

	/**
	 * @return the flagFondoPluriennaleVincolato
	 */
	public Boolean getFlagFondoPluriennaleVincolato() {
		return flagFondoPluriennaleVincolato;
	}

	/**
	 * @param flagFondoPluriennaleVincolato the flagFondoPluriennaleVincolato to set
	 */
	public void setFlagFondoPluriennaleVincolato(Boolean flagFondoPluriennaleVincolato) {
		this.flagFondoPluriennaleVincolato = flagFondoPluriennaleVincolato;
	}
	
	/**
	 * @return the capitoloEntrataPrevisioneNellaVariazione
	 */
	public CapitoloEntrataPrevisione getCapitoloEntrataPrevisioneNellaVariazione() {
		return capitoloEntrataPrevisioneNellaVariazione;
	}

	/**
	 * @param capitoloEntrataPrevisioneNellaVariazione the capitoloEntrataPrevisioneNellaVariazione to set
	 */
	public void setCapitoloEntrataPrevisioneNellaVariazione(CapitoloEntrataPrevisione capitoloEntrataPrevisioneNellaVariazione) {
		this.capitoloEntrataPrevisioneNellaVariazione = capitoloEntrataPrevisioneNellaVariazione;
	}

	/**
	 * @return the capitoloUscitaPrevisioneNellaVariazione
	 */
	public CapitoloUscitaPrevisione getCapitoloUscitaPrevisioneNellaVariazione() {
		return capitoloUscitaPrevisioneNellaVariazione;
	}

	/**
	 * @param capitoloUscitaPrevisioneNellaVariazione the capitoloUscitaPrevisioneNellaVariazione to set
	 */
	public void setCapitoloUscitaPrevisioneNellaVariazione(
			CapitoloUscitaPrevisione capitoloUscitaPrevisioneNellaVariazione) {
		this.capitoloUscitaPrevisioneNellaVariazione = capitoloUscitaPrevisioneNellaVariazione;
	}

	/**
	 * @return the capitoloEntrataGestioneNellaVariazione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestioneNellaVariazione() {
		return capitoloEntrataGestioneNellaVariazione;
	}

	/**
	 * @param capitoloEntrataGestioneNellaVariazione the capitoloEntrataGestioneNellaVariazione to set
	 */
	public void setCapitoloEntrataGestioneNellaVariazione(CapitoloEntrataGestione capitoloEntrataGestioneNellaVariazione) {
		this.capitoloEntrataGestioneNellaVariazione = capitoloEntrataGestioneNellaVariazione;
	}

	/**
	 * @return the capitoloUscitaGestioneNellaVariazione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestioneNellaVariazione() {
		return capitoloUscitaGestioneNellaVariazione;
	}

	/**
	 * @return the dettaglioVariazioneImportoCapitoloTrovatoNellaVariazione
	 */
	public ElementoCapitoloVariazione getElementoCapitoloVariazioneTrovatoNellaVariazione() {
		return elementoCapitoloVariazioneTrovatoNellaVariazione;
	}

	/**
	 * @param elementoCapitoloVariazioneTrovatoNellaVariazione the dettaglioVariazioneImportoCapitoloTrovatoNellaVariazione to set
	 */
	public void setElementoCapitoloVariazioneTrovatoNellaVariazione(ElementoCapitoloVariazione elementoCapitoloVariazioneTrovatoNellaVariazione) {
		this.elementoCapitoloVariazioneTrovatoNellaVariazione = elementoCapitoloVariazioneTrovatoNellaVariazione;
	}

	/**
	 * @param capitoloUscitaGestioneNellaVariazione the capitoloUscitaGestioneNellaVariazione to set
	 */
	public void setCapitoloUscitaGestioneNellaVariazione(CapitoloUscitaGestione capitoloUscitaGestioneNellaVariazione) {
		this.capitoloUscitaGestioneNellaVariazione = capitoloUscitaGestioneNellaVariazione;
	}

	/**
	 * @return the uidCapitoloComponenti
	 */
	public int getUidCapitoloComponenti() {
		return uidCapitoloComponenti;
	}

	/**
	 * @param uidCapitoloComponenti the uidCapitoloComponenti to set
	 */
	public void setUidCapitoloComponenti(int uidCapitoloComponenti) {
		this.uidCapitoloComponenti = uidCapitoloComponenti;
	}

	/**
	 * @return the componentiCapitoloNuovoDettaglio
	 */
	public List<ElementoComponenteVariazione> getComponentiCapitoloNuovoDettaglio() {
		return componentiCapitoloNuovoDettaglio;
	}

	/**
	 * @param componentiCapitoloNuovoDettaglio the componentiCapitoloNuovoDettaglio to set
	 */
	public void setComponentiCapitoloNuovoDettaglio(List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio) {
		this.componentiCapitoloNuovoDettaglio = componentiCapitoloNuovoDettaglio != null? componentiCapitoloNuovoDettaglio : new ArrayList<ElementoComponenteVariazione>();
	}

	/**
	 * @return the componentiCapitoloDettaglio
	 */
	public List<ElementoComponenteVariazione> getComponentiCapitoloDettaglio() {
		return componentiCapitoloDettaglio;
	}

	/**
	 * @param componentiCapitoloDettaglio the componentiCapitoloDettaglio to set
	 */
	public void setComponentiCapitoloDettaglio(List<ElementoComponenteVariazione> componentiCapitoloDettaglio) {
		this.componentiCapitoloDettaglio = componentiCapitoloDettaglio;
	}
	
	/**
	 * @return the competenzaTotaleAnno0
	 */
	public BigDecimal getCompetenzaTotaleAnno0() {
		return competenzaTotaleAnno0;
	}

	/**
	 * @param competenzaTotaleAnno0 the competenzaTotaleAnno0 to set
	 */
	public void setCompetenzaTotaleAnno0(BigDecimal competenzaTotaleAnno0) {
		this.competenzaTotaleAnno0 = competenzaTotaleAnno0;
	}

	/**
	 * @return the competenzaTotaleAnno1
	 */
	public BigDecimal getCompetenzaTotaleAnno1() {
		return competenzaTotaleAnno1;
	}

	/**
	 * @param competenzaTotaleAnno1 the competenzaTotaleAnno1 to set
	 */
	public void setCompetenzaTotaleAnno1(BigDecimal competenzaTotaleAnno1) {
		this.competenzaTotaleAnno1 = competenzaTotaleAnno1;
	}

	/**
	 * @return the competenzaTotaleAnno2
	 */
	public BigDecimal getCompetenzaTotaleAnno2() {
		return competenzaTotaleAnno2;
	}

	/**
	 * @param competenzaTotaleAnno2 the competenzaTotaleAnno2 to set
	 */
	public void setCompetenzaTotaleAnno2(BigDecimal competenzaTotaleAnno2) {
		this.competenzaTotaleAnno2 = competenzaTotaleAnno2;
	}

	/**
	 * @return the competenzaTotaleNuovoAnno0
	 */
	public BigDecimal getCompetenzaTotaleNuovoAnno0() {
		return competenzaTotaleNuovoAnno0;
	}

	/**
	 * @param competenzaTotaleNuovoAnno0 the competenzaTotaleNuovoAnno0 to set
	 */
	public void setCompetenzaTotaleNuovoAnno0(BigDecimal competenzaTotaleNuovoAnno0) {
		this.competenzaTotaleNuovoAnno0 = competenzaTotaleNuovoAnno0;
	}

	/**
	 * @return the competenzaTotaleNuovoAnno1
	 */
	public BigDecimal getCompetenzaTotaleNuovoAnno1() {
		return competenzaTotaleNuovoAnno1;
	}

	/**
	 * @param competenzaTotaleNuovoAnno1 the competenzaTotaleNuovoAnno1 to set
	 */
	public void setCompetenzaTotaleNuovoAnno1(BigDecimal competenzaTotaleNuovoAnno1) {
		this.competenzaTotaleNuovoAnno1 = competenzaTotaleNuovoAnno1;
	}

	/**
	 * @return the competenzaTotaleNuovoAnno2
	 */
	public BigDecimal getCompetenzaTotaleNuovoAnno2() {
		return competenzaTotaleNuovoAnno2;
	}

	/**
	 * @param competenzaTotaleNuovoAnno2 the competenzaTotaleNuovoAnno2 to set
	 */
	public void setCompetenzaTotaleNuovoAnno2(BigDecimal competenzaTotaleNuovoAnno2) {
		this.competenzaTotaleNuovoAnno2 = competenzaTotaleNuovoAnno2;
	}

	/**
	 * @return the tipoComponenteSelezionata
	 */
	public TipoComponenteImportiCapitolo getTipoComponenteSelezionata() {
		return tipoComponenteSelezionata;
	}

	/**
	 * @param tipoComponenteSelezionata the tipoComponenteSelezionata to set
	 */
	public void setTipoComponenteSelezionata(TipoComponenteImportiCapitolo tipoComponenteSelezionata) {
		this.tipoComponenteSelezionata = tipoComponenteSelezionata;
	}
	
	/**
	 * @return the tipoDettaglioComponenteImportiCapitoloSelezionato
	 */
	public TipoDettaglioComponenteImportiCapitolo getTipoDettaglioSelezionato() {
		return tipoDettaglioSelezionato;
	}

	/**
	 * @param tipoDettaglioComponenteImportiCapitoloSelezionato the tipoDettaglioComponenteImportiCapitoloSelezionato to set
	 */
	public void setTipoDettaglioSelezionato(TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitoloSelezionato) {
		this.tipoDettaglioSelezionato = tipoDettaglioComponenteImportiCapitoloSelezionato;
	}

	/**
	 * @return the editabileAnno0
	 */
	public boolean isEditabileAnno0() {
		return editabileAnno0;
	}

	/**
	 * @param editabileAnno0 the editabileAnno0 to set
	 */
	public void setEditabileAnno0(boolean editabileAnno0) {
		this.editabileAnno0 = editabileAnno0;
	}

	/**
	 * @return the editabileAnno1
	 */
	public boolean isEditabileAnno1() {
		return editabileAnno1;
	}

	/**
	 * @param editabileAnno1 the editabileAnno1 to set
	 */
	public void setEditabileAnno1(boolean editabileAnno1) {
		this.editabileAnno1 = editabileAnno1;
	}

	/**
	 * @return the editabileAnno2
	 */
	public boolean isEditabileAnno2() {
		return editabileAnno2;
	}

	/**
	 * @param editabileAnno2 the editabileAnno2 to set
	 */
	public void setEditabileAnno2(boolean editabileAnno2) {
		this.editabileAnno2 = editabileAnno2;
	}
	
	/**
	 * @return the elementoComponenteModificata
	 */
	public ElementoComponenteVariazione getElementoComponenteModificata() {
		return elementoComponenteModificata;
	}

	/**
	 * @param elementoComponenteModificata the elementoComponenteModificata to set
	 */
	public void setElementoComponenteModificata(ElementoComponenteVariazione elementoComponenteModificata) {
		this.elementoComponenteModificata = elementoComponenteModificata;
	}

	/**
	 * @return the uidCapitoloAssociatoComponenti
	 */
	public int getUidCapitoloAssociatoComponenti() {
		return uidCapitoloAssociatoComponenti;
	}

	/**
	 * @param uidCapitoloAssociatoComponenti the uidCapitoloAssociatoComponenti to set
	 */
	public void setUidCapitoloAssociatoComponenti(int uidCapitoloAssociatoComponenti) {
		this.uidCapitoloAssociatoComponenti = uidCapitoloAssociatoComponenti;
	}
	
	/**
	 * @return the capitoloFondino
	 */
	public boolean isCapitoloFondino() {
		return capitoloFondino;
	}

	/**
	 * @param capitoloFondino the capitoloFondino to set
	 */
	public void setCapitoloFondino(boolean capitoloFondino) {
		this.capitoloFondino = capitoloFondino;
	}

	/**
	 * Ricalcola stanziamenti nuovo dettaglio.
	 *
	 * @param componenti the componenti
	 */
	public void ricalcolaStanziamentiNuovoDettaglio() {
		BigDecimal competenzaAnnoZero = BigDecimal.ZERO;
		BigDecimal competenzaAnnoUno = BigDecimal.ZERO;
		BigDecimal competenzaAnnoDue = BigDecimal.ZERO;
		
		for (ElementoComponenteVariazione el : getComponentiCapitoloNuovoDettaglio()) {
			competenzaAnnoZero = competenzaAnnoZero.add(el.getImportoAnno0());
			competenzaAnnoUno = competenzaAnnoUno.add(el.getImportoAnno1());
			competenzaAnnoDue = competenzaAnnoDue.add(el.getImportoAnno2());
		}
		setCompetenzaTotaleNuovoAnno0(competenzaAnnoZero);
		setCompetenzaTotaleNuovoAnno1(competenzaAnnoUno);
		setCompetenzaTotaleNuovoAnno2(competenzaAnnoDue);
		
	} 
	
	/**
	 * Somma stanziamenti da componenti in lista.
	 *
	 * @param componenti the componenti
	 */
	public void ricalcolaStanziamentiDettaglio() {
		BigDecimal competenzaAnnoZero = BigDecimal.ZERO;
		BigDecimal competenzaAnnoUno = BigDecimal.ZERO;
		BigDecimal competenzaAnnoDue = BigDecimal.ZERO;
		
		for (ElementoComponenteVariazione el : getComponentiCapitoloDettaglio()) {
			competenzaAnnoZero = competenzaAnnoZero.add(el.getImportoAnno0());
			competenzaAnnoUno = competenzaAnnoUno.add(el.getImportoAnno1());
			competenzaAnnoDue = competenzaAnnoDue.add(el.getImportoAnno2());
		}
		setCompetenzaTotaleAnno0(competenzaAnnoZero);
		setCompetenzaTotaleAnno1(competenzaAnnoUno);
		setCompetenzaTotaleAnno2(competenzaAnnoDue);
		
	}
	
	
	
}
