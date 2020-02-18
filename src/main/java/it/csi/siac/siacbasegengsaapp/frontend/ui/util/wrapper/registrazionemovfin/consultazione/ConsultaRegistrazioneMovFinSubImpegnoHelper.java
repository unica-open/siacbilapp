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
 * Helper per la consultazione dei dati della modifica dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinSubImpegnoHelper extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<Impegno, SubImpegno, ModificaMovimentoGestioneSpesa,
		ElementoSubImpegnoRegistrazioneMovFin, ElementoModificaMovimentoGestioneSpesaRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;
	
	private final SubImpegno subMovimentoGestione;

	/**
	 * Costruttore di wrap
	 * @param impegno l'impegno
	 * @param subMovimentoGestione il sub impegno
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinSubImpegnoHelper(Impegno impegno, SubImpegno subMovimentoGestione, boolean isGestioneUEB) {
		super(impegno, isGestioneUEB);
		this.subMovimentoGestione = subMovimentoGestione;
		
		wrapSubMovimentoGestione();
		wrapModificaMovimentoGestione();
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubImpegno getSubMovimentoGestione() {
		return this.subMovimentoGestione;
	}

	@Override
	protected void wrapSubMovimentoGestione() {
		if(subMovimentoGestione == null) {
			return;
		}
		listaSubMovimentoGestione.add(new ElementoSubImpegnoRegistrazioneMovFin(subMovimentoGestione));
	}

	@Override
	protected void wrapModificaMovimentoGestione() {
		// TODO: impostare qualcosa?
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return movimentoGestione != null ? movimentoGestione.getCapitoloUscitaGestione() : null;
	}
	
}
