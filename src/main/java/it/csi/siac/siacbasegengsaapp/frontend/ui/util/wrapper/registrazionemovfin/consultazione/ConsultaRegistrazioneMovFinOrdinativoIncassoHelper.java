/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.util.List;

import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;

/**
 * Helper per la consultazione dei dati dell'ordinativo di incasso
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinOrdinativoIncassoHelper extends ConsultaRegistrazioneMovFinOrdinativoHelper<OrdinativoIncasso, SubOrdinativoIncasso> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;

	/**
	 * Costruttore di wrap
	 * @param ordinativo l'ordinativo
	 * @param listaSubOrdinativo la lista dei subordinativi sub ordinativo
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinOrdinativoIncassoHelper(OrdinativoIncasso ordinativo, List<SubOrdinativoIncasso> listaSubOrdinativo, boolean isGestioneUEB) {
		super(ordinativo, listaSubOrdinativo, isGestioneUEB);
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return ordinativo != null ? ordinativo.getCapitoloEntrataGestione() : null;
	}
	
}
