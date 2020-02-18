/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.util.ArrayList;

import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

/**
 * Classe di wrap per il capitolo di Entrata Previsione durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public class ElementoCapitoloVariazioneEntrataPrevisione extends ElementoCapitoloVariazione {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6147219367634570552L;
	
	/** Costruttore vuoto di default */
	public ElementoCapitoloVariazioneEntrataPrevisione() {
		super();
		setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
	}
	
	/**
	 * Costruttore a partire dalla classe padre.
	 * 
	 * @param elementoCapitoloVariazione l'istanza della classe padre
	 */
	public ElementoCapitoloVariazioneEntrataPrevisione(ElementoCapitoloVariazione elementoCapitoloVariazione) {
		this();
		setUid(elementoCapitoloVariazione.getUid());
		setAnnoCapitolo(elementoCapitoloVariazione.getAnnoCapitolo());
		setNumeroCapitolo(elementoCapitoloVariazione.getNumeroCapitolo());
		setNumeroArticolo(elementoCapitoloVariazione.getNumeroArticolo());
		setNumeroUEB(elementoCapitoloVariazione.getNumeroUEB());
		setCompetenza(elementoCapitoloVariazione.getCompetenza());
		setResiduo(elementoCapitoloVariazione.getResiduo());
		setCassa(elementoCapitoloVariazione.getCassa());
		setFondoPluriennaleVincolato(elementoCapitoloVariazione.getFondoPluriennaleVincolato());
		setDescrizione(elementoCapitoloVariazione.getDescrizione());
		setDatiAccessorii(elementoCapitoloVariazione.getDatiAccessorii());
		setDenominazione(elementoCapitoloVariazione.getDenominazione());
		setGestioneUEB(elementoCapitoloVariazione.getGestioneUEB());
		setDaAnnullare(elementoCapitoloVariazione.getDaAnnullare());
		setStatoOperativoElementoDiBilancio(elementoCapitoloVariazione.getStatoOperativoElementoDiBilancio());
		//SIAC-6881
		setComponentiCapitolo(new ArrayList<TipoComponenteImportiCapitolo>());
		for(TipoComponenteImportiCapitolo e : elementoCapitoloVariazione.getComponentiCapitolo()) {
			getComponentiCapitolo().add(e);
		}
		
		
		setListaSottoElementi(new ArrayList<ElementoCapitoloVariazione>());
		for(ElementoCapitoloVariazione e : elementoCapitoloVariazione.getListaSottoElementi()) {
			getListaSottoElementi().add(new ElementoCapitoloVariazioneEntrataGestione(e));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public CapitoloEntrataPrevisione unwrap() {
		CapitoloEntrataPrevisione capitolo = new CapitoloEntrataPrevisione();
		
		unwrapCommons(capitolo);
		
		ImportiCapitoloEP importi = new ImportiCapitoloEP();
		// FIXME: SIAC-6883: anno?
		importi.setStanziamento(getCompetenza());
		importi.setStanziamentoResiduo(getResiduo());
		importi.setStanziamentoCassa(getCassa());
		importi.setFondoPluriennaleVinc(getFondoPluriennaleVincolato());
		
		capitolo.setImportiCapitoloEP(importi);
		
		return capitolo;
	}
	
}
