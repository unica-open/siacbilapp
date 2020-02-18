/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.helper;

import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;

/**
 * The Class DettaglioVariazioneComponenteImportoCapitolo
 * 
 * @author elisa
 * @version 1.0.0 - 04-10-2019.
 */
public class DettaglioVariazioneComponenteImportoCapitoloHelper {
	
	private DettaglioVariazioneComponenteImportoCapitoloHelper() {
		
	}
	
	
	/**
	 * Gets the index dettaglio in lista.
	 *
	 * @param dettaglio the dettaglio
	 * @param lista the lista
	 * @return the index dettaglio in lista
	 */
	public static int getIndexDettaglioInListaDettaglio(DettaglioVariazioneComponenteImportoCapitolo dettaglio, List<DettaglioVariazioneComponenteImportoCapitolo> lista) {
		int indexDettaglioNotFound = -1;
		if(dettaglio == null || lista == null || lista.isEmpty()) {
			return indexDettaglioNotFound;
		}
		
		for (int i = 0; i < lista.size(); i++) {
			DettaglioVariazioneComponenteImportoCapitolo dettaglioInLista = lista.get(i);
			if(dettaglioInLista == null) {
				continue;
			}
			
			if (areDettagliUguali(dettaglioInLista, dettaglio)) {
				return i;
			}
		}
		
		return indexDettaglioNotFound;
		
	}
	
	/**
	 * Gets the index dettaglio in lista.
	 *
	 * @param lista the lista
	 * @param componenteModificata the componente modificata
	 * @return the index dettaglio in lista
	 */
	public static int getIndexDettaglioInListaElemento(List<ElementoComponenteVariazione> lista, ElementoComponenteVariazione componenteModificata) {
		int indexDettaglioNotFound = -1;
		if(componenteModificata == null || lista == null || lista.isEmpty()) {
			return indexDettaglioNotFound;
		}
		
		for (int i = 0; i < lista.size(); i++) {
			//
			DettaglioVariazioneComponenteImportoCapitolo dettaglioInLista = lista.get(i) != null? lista.get(i).getDettaglioVariazioneComponenteImportoCapitolo() : null;
			if(dettaglioInLista == null) {
				continue;
			}
			//per stabilire se un dettaglio sia o 
			if (areDettagliUguali(componenteModificata.getDettaglioVariazioneComponenteImportoCapitolo(), dettaglioInLista)) {
				return i;
			}
		}
	
		return indexDettaglioNotFound;
	}


	/**
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static boolean areDettagliUguali(DettaglioVariazioneComponenteImportoCapitolo d1,DettaglioVariazioneComponenteImportoCapitolo d2) {
		return areTipiComponentiUguali(d2, d1) && (isAlmenoUnDettaglionuovo(d2, d1) || dettagliUgualiByUid(d2, d1));
	}

	
	private static boolean isAlmenoUnDettaglionuovo(DettaglioVariazioneComponenteImportoCapitolo d1, DettaglioVariazioneComponenteImportoCapitolo d2) {
		return d1.getUid() == 0 || d2.getUid() == 0;
	}


	private static boolean areTipiComponentiUguali(DettaglioVariazioneComponenteImportoCapitolo d1, DettaglioVariazioneComponenteImportoCapitolo d2) {
		return d1.getComponenteImportiCapitolo() != null && d2.getComponenteImportiCapitolo() != null && d1.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo() != null && d2.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo() != null
				&& d1.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid() != 0
				&& d1.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid() == d2.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getUid();
	}


	private static boolean dettagliUgualiByUid(DettaglioVariazioneComponenteImportoCapitolo dettaglio1, DettaglioVariazioneComponenteImportoCapitolo dettaglio2) {
		
		return dettaglio1.getUid() != 0 && dettaglio2.getUid() != 0 && dettaglio1.getUid() == dettaglio2.getUid();
	}
}
