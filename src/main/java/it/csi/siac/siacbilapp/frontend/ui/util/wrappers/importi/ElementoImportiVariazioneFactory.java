/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.importi;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Factory per i wrapper degli importi per la variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 04/11/2013
 *
 */
public final class ElementoImportiVariazioneFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoImportiVariazioneFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dalla lista delle variazioni.
	 * 
	 * @param listaVariazioni la lista da cui ottenere il wrapper
	 * 
	 * @return il wrapper ottenuto
	 */
	public static ElementoImportiVariazione getInstance(List<ElementoCapitoloVariazione> listaVariazioni) {
		ElementoImportiVariazione result = new ElementoImportiVariazione();
		if(listaVariazioni == null) {
			return result;
		}
		
		BigDecimal competenzaEntrata = BigDecimal.ZERO;
		BigDecimal residuoEntrata = BigDecimal.ZERO;
		BigDecimal cassaEntrata = BigDecimal.ZERO;
		BigDecimal fondoPluriennaleVincolatoEntrata = BigDecimal.ZERO;
		BigDecimal competenzaSpesa = BigDecimal.ZERO;
		BigDecimal residuoSpesa = BigDecimal.ZERO;
		BigDecimal cassaSpesa = BigDecimal.ZERO;
		BigDecimal fondoPluriennaleVincolatoSpesa = BigDecimal.ZERO;
		
		BigDecimal differenzaCompetenza;
		BigDecimal differenzaResiduo;
		BigDecimal differenzaCassa;
		BigDecimal differenzaFondoPluriennaleVincolato;
		
		for(ElementoCapitoloVariazione elemento : listaVariazioni) {
			TipoCapitolo tipoCapitolo = elemento.getTipoCapitolo();
			if(tipoCapitolo == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE || tipoCapitolo == TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE) {
				competenzaEntrata = competenzaEntrata.add(elemento.getCompetenza());
				residuoEntrata = residuoEntrata.add(elemento.getResiduo());
				cassaEntrata = cassaEntrata.add(elemento.getCassa());
				if(elemento.getFondoPluriennaleVincolato()!=null){
					fondoPluriennaleVincolatoEntrata = fondoPluriennaleVincolatoEntrata.add(elemento.getFondoPluriennaleVincolato());
				}
			} else {
				competenzaSpesa = competenzaSpesa.add(elemento.getCompetenza());
				residuoSpesa = residuoSpesa.add(elemento.getResiduo());
				cassaSpesa = cassaSpesa.add(elemento.getCassa());
				if(elemento.getFondoPluriennaleVincolato()!=null){
					fondoPluriennaleVincolatoSpesa = fondoPluriennaleVincolatoSpesa.add(elemento.getFondoPluriennaleVincolato());
				}
			}
		}
		differenzaCompetenza = competenzaEntrata.subtract(competenzaSpesa);
		differenzaResiduo = residuoEntrata.subtract(residuoSpesa);
		differenzaCassa = cassaEntrata.subtract(cassaSpesa);
		differenzaFondoPluriennaleVincolato = fondoPluriennaleVincolatoEntrata.subtract(fondoPluriennaleVincolatoSpesa);
		
		result.setTotaleEntrataCompetenza(competenzaEntrata);
		result.setTotaleEntrataResiduo(residuoEntrata);
		result.setTotaleEntrataCassa(cassaEntrata);
		result.setTotaleEntrataFondoPluriennaleVincolato(fondoPluriennaleVincolatoEntrata);
		
		result.setTotaleSpesaCompetenza(competenzaSpesa);
		result.setTotaleSpesaResiduo(residuoSpesa);
		result.setTotaleSpesaCassa(cassaSpesa);
		result.setTotaleSpesaFondoPluriennaleVincolato(fondoPluriennaleVincolatoSpesa);
		
		result.setDifferenzaCompetenza(differenzaCompetenza);
		result.setDifferenzaResiduo(differenzaResiduo);
		result.setDifferenzaCassa(differenzaCassa);
		result.setDifferenzaFondoPluriennaleVincolato(differenzaFondoPluriennaleVincolato);
		
		return result;
	}
	
}
