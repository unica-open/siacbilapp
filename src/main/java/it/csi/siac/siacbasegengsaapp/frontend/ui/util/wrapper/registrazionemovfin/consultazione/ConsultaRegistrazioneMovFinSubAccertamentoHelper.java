/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.ElementoSubAccertamentoRegistrazioneMovFin;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

/**
 * Helper per la consultazione dei dati dell'impegno
 * @author Marchino Alessandro
 *
 */
public class ConsultaRegistrazioneMovFinSubAccertamentoHelper extends ConsultaRegistrazioneMovFinMovimentoGestioneHelper<Accertamento, SubAccertamento, ModificaMovimentoGestioneEntrata,
		ElementoSubAccertamentoRegistrazioneMovFin, ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;
	
	private final SubAccertamento subMovimentoGestione;

	/**
	 * Costruttore di wrap
	 * @param accertamento l'accertamento
	 * @param subMovimentoGestione il subaccertamento
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ConsultaRegistrazioneMovFinSubAccertamentoHelper(Accertamento accertamento, SubAccertamento subMovimentoGestione, boolean isGestioneUEB) {
		super(accertamento, isGestioneUEB);
		this.subMovimentoGestione = subMovimentoGestione;
		wrapSubMovimentoGestione();
		wrapModificaMovimentoGestione();
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubAccertamento getSubMovimentoGestione() {
		return this.subMovimentoGestione;
	}

	@Override
	protected void wrapSubMovimentoGestione() {
		if(subMovimentoGestione == null) {
			return;
		}
		listaSubMovimentoGestione.add(new ElementoSubAccertamentoRegistrazioneMovFin(subMovimentoGestione));
	}

	@Override
	protected void wrapModificaMovimentoGestione() {
		// TODO: impostare qualcosa?
	}
	
	@Override
	protected Capitolo<?, ?> ottieniCapitolo() {
		return movimentoGestione != null ? movimentoGestione.getCapitoloEntrataGestione() : null;
	}
	
}
