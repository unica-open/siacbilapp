/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubImpegnoRegistrazioneMovFin;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

/**
 * Helper per la consultazione dei dati dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinImpegnoHelper extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<Impegno, SubImpegno, ModificaMovimentoGestioneSpesa,
		ElementoSubImpegnoRegistrazioneMovFin, ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2676241836167738216L;

	/**
	 * Costruttore di wrap
	 * @param impegno l'impegno
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinImpegnoHelper(Impegno impegno, boolean isGestioneUEB) {
		super(impegno, isGestioneUEB);
		
		wrapSubMovimentoGestione();
		wrapModificaMovimentoGestione();
	}
	
	@Override
	protected void wrapSubMovimentoGestione() {
		if(movimentoGestione == null || movimentoGestione.getElencoSubImpegni() == null) {
			return;
		}
		for(SubImpegno si : movimentoGestione.getElencoSubImpegni()) {
			ElementoSubImpegnoRegistrazioneMovFin wrapper = new ElementoSubImpegnoRegistrazioneMovFin(si);
			listaSubMovimentoGestione.add(wrapper);
		}
	}

	@Override
	protected void wrapModificaMovimentoGestione() {
		if(movimentoGestione == null || movimentoGestione.getListaModificheMovimentoGestioneSpesa() == null) {
			return;
		}
		for(ModificaMovimentoGestioneSpesa mmgs : movimentoGestione.getListaModificheMovimentoGestioneSpesa()) {
			ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin wrapper = new ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin(mmgs);
			listaModificaMovimentoGestione.add(wrapper);
		}
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return movimentoGestione != null ? movimentoGestione.getCapitoloUscitaGestione() : null;
	}
	
}
