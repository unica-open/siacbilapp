/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Factory per Elemento delle scritture per lo step 1 della PrimaNotaIntegrata
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 *
 */
public final class ElementoQuotaPrimaNotaIntegrataFactory {
	/** Non instanziare la classe */
	private ElementoQuotaPrimaNotaIntegrataFactory() {
	}
	
	/**
	 * Ottiene una lista di istanze di ElementoQuotaPrimaNotaIntegrata a partire dalla prima nota. Viene creata una 
	 * entry della lista per ogni movimentoEP della prima nota per il quale la registrazione sia diversa da null 
	 * @param primaNota la prima nota
	 * @return l'istanza creata
	 */
	public static List<ElementoQuotaPrimaNotaIntegrata> getInstancesFromPrimaNota(PrimaNota primaNota){
		List<ElementoQuotaPrimaNotaIntegrata> listaInstances = new ArrayList<ElementoQuotaPrimaNotaIntegrata>();
		List<MovimentoEP> listaMovimentiEPPrimaNota = primaNota.getListaMovimentiEP();
		
		for (MovimentoEP movimentoEP : listaMovimentiEPPrimaNota) {
			RegistrazioneMovFin regMovFin = movimentoEP.getRegistrazioneMovFin();
			if( regMovFin != null){
				ElementoQuotaPrimaNotaIntegrata instance = new ElementoQuotaPrimaNotaIntegrata();
				instance.setRegistrazioneMovFin(regMovFin);
				instance.setSubdocumento((Subdocumento<?, ?>) regMovFin.getMovimento());
				instance.setMovimentoEP(movimentoEP);
				listaInstances.add(instance);
			}
			
		}
		
		return listaInstances;
	}
	
	
}
