/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.util.ArrayList;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

/**
 * Classe di wrap per il capitolo di Uscita Previsione durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public class ElementoCapitoloVariazioneEntrataGestione extends ElementoCapitoloVariazione {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -577707253835409553L;
	
	/** Costruttore vuoto di default */
	public ElementoCapitoloVariazioneEntrataGestione() {
		super();
		setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
	}
	
	/**
	 * Costruttore a partire dalla classe padre.
	 * 
	 * @param elementoCapitoloVariazione l'istanza della classe padre
	 */
	public ElementoCapitoloVariazioneEntrataGestione(ElementoCapitoloVariazione elementoCapitoloVariazione) {
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
	public CapitoloEntrataGestione unwrap() {
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		
		unwrapCommons(capitolo);
		
		ImportiCapitoloEG importi = new ImportiCapitoloEG();
		// FIXME: SIAC-6883: anno?
		importi.setStanziamento(getCompetenza());
		importi.setStanziamentoResiduo(getResiduo());
		importi.setStanziamentoCassa(getCassa());
		importi.setFondoPluriennaleVinc(getFondoPluriennaleVincolato());
		capitolo.setImportiCapitoloEG(importi);
		
		return capitolo;
	}
	
}
