/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.util.ArrayList;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

/**
 * Classe di wrap per il capitolo di Uscita Gestione durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public class ElementoCapitoloVariazioneUscitaGestione extends ElementoCapitoloVariazione {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4454254525363806908L;
	
	/** Costruttore vuoto di default */
	public ElementoCapitoloVariazioneUscitaGestione() {
		super();
		setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
	}
	
	/**
	 * Costruttore a partire dalla classe padre.
	 * 
	 * @param elementoCapitoloVariazione l'istanza della classe padre
	 */
	public ElementoCapitoloVariazioneUscitaGestione(ElementoCapitoloVariazione elementoCapitoloVariazione) {
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
	public CapitoloUscitaGestione unwrap() {
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		
		unwrapCommons(capitolo);
		ImportiCapitoloUG importi = new ImportiCapitoloUG();
		// FIXME: SIAC-6883: anno?
		importi.setStanziamento(getCompetenza());
		importi.setStanziamentoResiduo(getResiduo());
		importi.setStanziamentoCassa(getCassa());
		importi.setFondoPluriennaleVinc(getFondoPluriennaleVincolato());
		
		capitolo.setImportiCapitoloUG(importi);
		
		return capitolo;
	}

}
